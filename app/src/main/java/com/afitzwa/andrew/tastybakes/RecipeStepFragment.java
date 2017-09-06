package com.afitzwa.andrew.tastybakes;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afitzwa.andrew.tastybakes.data.StepColumns;
import com.afitzwa.andrew.tastybakes.data.StepProvider;
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

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeStepFragment extends Fragment implements AdaptiveMediaSourceEventListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = RecipeStepFragment.class.getSimpleName();

    public static final String ARG_RECIPE_FK_ID = "recipe_fk_id";
    public static final String ARG_STEP_ID = "step_id";

    final static boolean PLAY_WHEN_READY = true;

    // Exoplayer resources
    private Handler mMainHandler;
    private Uri mMp4VideoUri;
    private SimpleExoPlayer mSimpleExoPlayer;
    private DefaultTrackSelector mTrackSelector;
    private MediaSource mMediaSource;
    private DataSource.Factory mediaDataSourceFactory;
    private int positionMs;

    private Context mContext;

    @BindView(R.id.recipe_step_detail) TextView mStepDescriptionView;
    @BindView(R.id.player_view) SimpleExoPlayerView mSimpleExoPlayerView;

    public RecipeStepFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();

        mContext = inflater.getContext();

        if (bundle != null) {
            Bundle loaderBundle = new Bundle(bundle);

            getLoaderManager().initLoader(0, loaderBundle, this);

            mMainHandler = new Handler();

            mTrackSelector = new DefaultTrackSelector();
            mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, mTrackSelector);
            mSimpleExoPlayerView.setPlayer(mSimpleExoPlayer);

            mediaDataSourceFactory = new DefaultDataSourceFactory(
                    mContext, Util.getUserAgent(mContext, "TastyBakes")
            );

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

    private void initializePlayer(String description, String shortDescription, String videoUrl, String thumbnailUrl) {

        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(shortDescription);
        }

        mStepDescriptionView.setText(description);
        mMp4VideoUri = Uri.parse(videoUrl);
        if (videoUrl.isEmpty()) {
            Log.d(TAG, shortDescription + ": No video URL");
            mSimpleExoPlayerView.setVisibility(GONE);
        } else {
            mMediaSource = buildMediaSource(mMp4VideoUri, null);
            mSimpleExoPlayer.prepare(mMediaSource);
            positionMs = 0;
            mSimpleExoPlayer.seekTo(positionMs);
            mSimpleExoPlayer.setPlayWhenReady(PLAY_WHEN_READY);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        int recipeFK = args.getInt(ARG_RECIPE_FK_ID, -1);
        int stepId = args.getInt(ARG_STEP_ID);
        return new CursorLoader(mContext,
                StepProvider.Steps.CONTENT_URI,
                null,
                StepColumns.RECIPE_FK + " = ? AND " + StepColumns.STEP_ORDER + " = ?",
                new String[]{String.valueOf(recipeFK), String.valueOf(stepId)},
                StepColumns.STEP_ORDER + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            String description = data.getString(data.getColumnIndexOrThrow(StepColumns.DESCRIPTION));
            String shortDescription = data.getString(data.getColumnIndexOrThrow(StepColumns.SHORT_DESC));
            String videoUrl = data.getString(data.getColumnIndexOrThrow(StepColumns.VIDEO_URL));
            String thumbnailUrl = data.getString(data.getColumnIndexOrThrow(StepColumns.THUMB_URL));

            initializePlayer(description, shortDescription, videoUrl, thumbnailUrl);

            mStepDescriptionView.setText(description);
        } else {
            Log.e(TAG, "[onLoadFinished] Could not find any steps!");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
