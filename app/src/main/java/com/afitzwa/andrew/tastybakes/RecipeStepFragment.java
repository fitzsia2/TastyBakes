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
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
public class RecipeStepFragment extends Fragment implements AdaptiveMediaSourceEventListener,
        LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {
    private static final String TAG = RecipeStepFragment.class.getSimpleName();

    private static final String SAVE_STATE_POSITION = "video_position";
    private static final String SAVE_STATE_RECIPE_FK = "recipe_fk";
    private static final String SAVE_STATE_STEP_ID = "step_id";

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
    private long mPosition;

    private Context mContext;
    private IRecipeStepFragment mCaller;
    private int mStepNum;
    private int mRecipeFk;

    String mDescription;
    String mShortDescription;
    String mVideoUrl;
    String mThumbnailUrl;

    @BindView(R.id.recipe_step_detail) TextView mStepDescriptionView;
    @BindView(R.id.player_view) SimpleExoPlayerView mSimpleExoPlayerView;
    @BindView(R.id.next_button) Button mNextButtonView;
    @BindView(R.id.previous_button) Button mPrevButtonView;

    public RecipeStepFragment() {
    }

    // Only called on (> API 22)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setupInterface(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "[onCreate]");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_step, container, false);

        ButterKnife.bind(this, view);

        Log.v(TAG, "[onCreateView]");

        mNextButtonView.setOnClickListener(this);
        mPrevButtonView.setOnClickListener(this);

        Bundle bundle = getArguments();

        mContext = inflater.getContext();

        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getLong(SAVE_STATE_POSITION);
            mRecipeFk = savedInstanceState.getInt(SAVE_STATE_RECIPE_FK);
            mStepNum = savedInstanceState.getInt(SAVE_STATE_STEP_ID);
        } else {
            mPosition = 0;
        }

        if (mCaller == null) {
            setupInterface(mContext);
        }

        if (bundle != null) {
            Log.v(TAG, "[onCreateView] fk(" + bundle.getInt(ARG_RECIPE_FK_ID, -1) + ") row(" + bundle.getInt(ARG_STEP_ID, -1) + ")");

            mRecipeFk = bundle.getInt(ARG_RECIPE_FK_ID, -1);
            mStepNum = bundle.getInt(ARG_STEP_ID, -1);
            if (mStepNum == 0) {
                mPrevButtonView.setVisibility(GONE);
            }

            getLoaderManager().initLoader(0, null, this);

            setupPlayer();
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "[onActivityCreated]");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "[onStart]");
    }

    @Override
    public void onResume() {
        Log.d(TAG, "[onResume]");
        super.onResume();
        if (mSimpleExoPlayer != null) {
            Log.d(TAG, "[onResume] Found player");
            mSimpleExoPlayer.seekTo(mPosition);
        } else {
            Log.d(TAG, "[onResume] No player");
            setupPlayer();
            initializePlayer(mDescription, mShortDescription, mVideoUrl, mThumbnailUrl, mPosition);
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "[onPause]");
        super.onPause();
        if (mSimpleExoPlayer != null) {
            mPosition = mSimpleExoPlayer.getCurrentPosition();
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "[onStop]");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v(TAG, "[onDestroyView]");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "[onDestroy]");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.v(TAG, "[onDetach]");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(SAVE_STATE_POSITION, mPosition);
        outState.putInt(SAVE_STATE_RECIPE_FK, mRecipeFk);
        outState.putInt(SAVE_STATE_STEP_ID, mStepNum);
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

    private void setupPlayer() {
        mMainHandler = new Handler();

        mTrackSelector = new DefaultTrackSelector();
        mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, mTrackSelector);
        mSimpleExoPlayerView.setPlayer(mSimpleExoPlayer);

        mediaDataSourceFactory = new DefaultDataSourceFactory(
                mContext, Util.getUserAgent(mContext, "TastyBakes")
        );
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

    private void releasePlayer() {
        mSimpleExoPlayer.stop();
        mSimpleExoPlayer.release();
        mSimpleExoPlayer = null;
    }

    private void initializePlayer(String description, String shortDescription, String videoUrl, String thumbnailUrl, long position) {
        Log.v(TAG, "[initializePlayer]");

        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(shortDescription);
        }

        mStepDescriptionView.setText(description);
        mMp4VideoUri = Uri.parse(videoUrl);
        if (TextUtils.isEmpty(videoUrl)) {
            Log.d(TAG, shortDescription + ": No video URL");
            mSimpleExoPlayerView.setVisibility(GONE);
        } else {
            mMediaSource = buildMediaSource(mMp4VideoUri, null);
            mSimpleExoPlayer.prepare(mMediaSource);
            mSimpleExoPlayer.seekTo(position);
            mSimpleExoPlayer.setPlayWhenReady(PLAY_WHEN_READY);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(TAG, "[onCreateLoader] recipeId:" + mRecipeFk + " stepId:" + mStepNum);
        return new CursorLoader(mContext,
                StepProvider.Steps.CONTENT_URI,
                null,
                StepColumns.RECIPE_FK + " = ? AND " + StepColumns.STEP_ORDER + " = ?",
                new String[]{String.valueOf(mRecipeFk), String.valueOf(mStepNum)},
                StepColumns.STEP_ORDER + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            mDescription = data.getString(data.getColumnIndexOrThrow(StepColumns.DESCRIPTION));
            mShortDescription = data.getString(data.getColumnIndexOrThrow(StepColumns.SHORT_DESC));
            mVideoUrl = data.getString(data.getColumnIndexOrThrow(StepColumns.VIDEO_URL));
            mThumbnailUrl = data.getString(data.getColumnIndexOrThrow(StepColumns.THUMB_URL));
            Log.v(TAG, "[onLoadFinished] Position: " + mPosition);
            initializePlayer(mDescription, mShortDescription, mVideoUrl, mThumbnailUrl, mPosition);

            mStepDescriptionView.setText(mDescription);
        } else {
            Log.e(TAG, "[onLoadFinished] Could not find any steps!");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_button:
                mCaller.onStepNavigation(mStepNum + 1);
                break;
            case R.id.previous_button:
                mCaller.onStepNavigation(mStepNum - 1);
                break;
            default:
                Log.e(TAG, "[onClick] Illegal button click from: " + view.toString());
                break;
        }
    }

    private void setupInterface(Context context) {
        try {
            mCaller = (IRecipeStepFragment) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
}
