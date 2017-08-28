package com.afitzwa.andrew.tastybakes;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afitzwa.andrew.tastybakes.data.RecipeContent;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;

import static android.view.View.GONE;
import static com.afitzwa.andrew.tastybakes.data.RecipeContent.RECIPE_MAP;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeStepFragment extends Fragment implements AdaptiveMediaSourceEventListener {
    private static final String TAG = RecipeStepFragment.class.getSimpleName();
    public static final String ARG_RECIPE_ID = "item_id";
    public static final String ARG_RECIPE_STEP_ID = "recipe_step_id";

    final static boolean PLAY_WHEN_READY = true;

    // Exoplayer resources
    private Handler mMainHandler;
    private Uri mMp4VideoUri;
    private SimpleExoPlayer mSimpleExoPlayer;
    private SimpleExoPlayerView mSimpleExoPlayerView;
    private DefaultTrackSelector mTrackSelector;
    private MediaSource mMediaSource;
    private DataSource.Factory mediaDataSourceFactory;
    private int positionMs;

    public RecipeStepFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_step, container, false);

        Bundle bundle = getArguments();

        if (bundle != null) {
            String recipeName = bundle.getString(ARG_RECIPE_ID);
            int stepInstruction = bundle.getInt(ARG_RECIPE_STEP_ID);
            RecipeContent.Recipe recipe = RECIPE_MAP.get(recipeName);
            RecipeContent.Recipe.RecipeStep recipeStep = recipe.getSteps().get(stepInstruction);
            ((TextView) view.findViewById(R.id.recipe_step_detail)).setText(recipeStep.getDescription());


            mSimpleExoPlayerView = view.findViewById(R.id.player_view);

            mMainHandler = new Handler();

            mTrackSelector = new DefaultTrackSelector();
            mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), mTrackSelector);
            mSimpleExoPlayerView.setPlayer(mSimpleExoPlayer);

            mediaDataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "TastyBakes"));

            mMp4VideoUri = Uri.parse(recipeStep.getVideoURL());
            if (recipeStep.getVideoURL().isEmpty()) {
                Log.d(TAG, recipeStep.getShortDesc() + ": No video URL");
                mSimpleExoPlayerView.setVisibility(GONE);

                TextView noVideoText = new TextView(getContext());
                noVideoText.setText(R.string.video_missing_text);

                ((ConstraintLayout) view).addView(noVideoText);
            } else {
                mMediaSource = buildMediaSource(mMp4VideoUri, null);
                mSimpleExoPlayer.prepare(mMediaSource);
                mSimpleExoPlayer.setPlayWhenReady(PLAY_WHEN_READY);
                positionMs = 0;
                mSimpleExoPlayer.seekTo(positionMs);
            }
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ReleasePlayer();
    }

    @Override
    public void onLoadStarted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs) {
        Log.d(TAG, "onLoadStarted");
    }

    @Override
    public void onLoadCompleted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {
        Log.d(TAG, "onLoadComplete");
    }

    @Override
    public void onLoadCanceled(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {
        Log.d(TAG, "onLoadCanceled");
    }

    @Override
    public void onLoadError(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded, IOException error, boolean wasCanceled) {
        Log.d(TAG, String.format(
                "\ndataType:%d" +
                        "\ntrackType:%d" +
                        "\ntrackSelectionReason:%d" +
                        "\nerror:%s" +
                        "\nwasCancelled:%s",
                dataType,
                trackType,
                trackSelectionReason,
                error.toString(),
                String.valueOf(wasCanceled)));
    }

    @Override
    public void onUpstreamDiscarded(int trackType, long mediaStartTimeMs, long mediaEndTimeMs) {
        Log.d(TAG, "onUpstreamDiscarded");
    }

    @Override
    public void onDownstreamFormatChanged(int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaTimeMs) {
        Log.d(TAG, "onDownstreamFormatChanged");
    }

    /**
     * Taken from ExoPlayer's demo application
     *
     * @param uri
     * @param overrideExtension
     * @return
     */
    private MediaSource buildMediaSource(Uri uri, String overrideExtension) {
        int type = TextUtils.isEmpty(overrideExtension) ? Util.inferContentType(uri)
                : Util.inferContentType("." + overrideExtension);
        switch (type) {
            case C.TYPE_SS:
                return new SsMediaSource(uri, mediaDataSourceFactory,
                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory), mMainHandler, null);
            case C.TYPE_DASH:
                return new DashMediaSource(uri, mediaDataSourceFactory,
                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory), mMainHandler, null);
            case C.TYPE_HLS:
                return new HlsMediaSource(uri, mediaDataSourceFactory, mMainHandler, null);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource(uri, mediaDataSourceFactory, new DefaultExtractorsFactory(),
                        mMainHandler, null);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }

    private void ReleasePlayer() {
        mSimpleExoPlayer.stop();
        mSimpleExoPlayer.release();
        mSimpleExoPlayer = null;
    }
}
