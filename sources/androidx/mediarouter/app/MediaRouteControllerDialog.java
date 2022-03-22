package androidx.mediarouter.app;

import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.ColorUtils;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline1;
import androidx.mediarouter.app.OverlayListView;
import androidx.mediarouter.media.MediaRouteSelector;
import androidx.mediarouter.media.MediaRouter;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public final class MediaRouteControllerDialog extends AlertDialog {
    public AccelerateDecelerateInterpolator mAccelerateDecelerateInterpolator;
    public final AccessibilityManager mAccessibilityManager;
    public int mArtIconBackgroundColor;
    public Bitmap mArtIconBitmap;
    public boolean mArtIconIsLoaded;
    public Bitmap mArtIconLoadedBitmap;
    public Uri mArtIconUri;
    public ImageView mArtView;
    public boolean mAttachedToWindow;
    public final MediaRouterCallback mCallback;
    public boolean mCreated;
    public FrameLayout mDefaultControlLayout;
    public MediaDescriptionCompat mDescription;
    public LinearLayout mDialogAreaLayout;
    public int mDialogContentWidth;
    public Button mDisconnectButton;
    public View mDividerView;
    public final boolean mEnableGroupVolumeUX;
    public FrameLayout mExpandableAreaLayout;
    public Interpolator mFastOutSlowInInterpolator;
    public FetchArtTask mFetchArtTask;
    public MediaRouteExpandCollapseButton mGroupExpandCollapseButton;
    public int mGroupListAnimationDurationMs;
    public int mGroupListFadeInDurationMs;
    public int mGroupListFadeOutDurationMs;
    public ArrayList mGroupMemberRoutes;
    public HashSet mGroupMemberRoutesAdded;
    public HashSet mGroupMemberRoutesAnimatingWithBitmap;
    public HashSet mGroupMemberRoutesRemoved;
    public boolean mHasPendingUpdate;
    public Interpolator mInterpolator;
    public boolean mIsGroupExpanded;
    public boolean mIsGroupListAnimating;
    public boolean mIsGroupListAnimationPending;
    public Interpolator mLinearOutSlowInInterpolator;
    public MediaControllerCompat mMediaController;
    public LinearLayout mMediaMainControlLayout;
    public boolean mPendingUpdateAnimationNeeded;
    public ImageButton mPlaybackControlButton;
    public RelativeLayout mPlaybackControlLayout;
    public final MediaRouter.RouteInfo mRoute;
    public MediaRouter.RouteInfo mRouteInVolumeSliderTouched;
    public TextView mRouteNameTextView;
    public PlaybackStateCompat mState;
    public Button mStopCastingButton;
    public TextView mSubtitleView;
    public TextView mTitleView;
    public VolumeChangeListener mVolumeChangeListener;
    public LinearLayout mVolumeControlLayout;
    public VolumeGroupAdapter mVolumeGroupAdapter;
    public OverlayListView mVolumeGroupList;
    public int mVolumeGroupListItemHeight;
    public int mVolumeGroupListItemIconSize;
    public int mVolumeGroupListMaxHeight;
    public final int mVolumeGroupListPaddingTop;
    public SeekBar mVolumeSlider;
    public HashMap mVolumeSliderMap;
    public static final boolean DEBUG = Log.isLoggable("MediaRouteCtrlDialog", 3);
    public static final int CONNECTION_TIMEOUT_MILLIS = (int) TimeUnit.SECONDS.toMillis(30);
    public boolean mVolumeControlEnabled = true;
    public AnonymousClass1 mGroupListFadeInAnimation = new Runnable() { // from class: androidx.mediarouter.app.MediaRouteControllerDialog.1
        @Override // java.lang.Runnable
        public final void run() {
            final MediaRouteControllerDialog mediaRouteControllerDialog = MediaRouteControllerDialog.this;
            Objects.requireNonNull(mediaRouteControllerDialog);
            mediaRouteControllerDialog.clearGroupListAnimation(true);
            mediaRouteControllerDialog.mVolumeGroupList.requestLayout();
            mediaRouteControllerDialog.mVolumeGroupList.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: androidx.mediarouter.app.MediaRouteControllerDialog.11
                @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
                public final void onGlobalLayout() {
                    MediaRouteControllerDialog.this.mVolumeGroupList.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    final MediaRouteControllerDialog mediaRouteControllerDialog2 = MediaRouteControllerDialog.this;
                    Objects.requireNonNull(mediaRouteControllerDialog2);
                    HashSet hashSet = mediaRouteControllerDialog2.mGroupMemberRoutesAdded;
                    if (hashSet == null || hashSet.size() == 0) {
                        mediaRouteControllerDialog2.finishAnimation(true);
                        return;
                    }
                    Animation.AnimationListener animationListener = new Animation.AnimationListener() { // from class: androidx.mediarouter.app.MediaRouteControllerDialog.12
                        @Override // android.view.animation.Animation.AnimationListener
                        public final void onAnimationRepeat(Animation animation) {
                        }

                        @Override // android.view.animation.Animation.AnimationListener
                        public final void onAnimationStart(Animation animation) {
                        }

                        @Override // android.view.animation.Animation.AnimationListener
                        public final void onAnimationEnd(Animation animation) {
                            MediaRouteControllerDialog.this.finishAnimation(true);
                        }
                    };
                    int firstVisiblePosition = mediaRouteControllerDialog2.mVolumeGroupList.getFirstVisiblePosition();
                    boolean z = false;
                    for (int i = 0; i < mediaRouteControllerDialog2.mVolumeGroupList.getChildCount(); i++) {
                        View childAt = mediaRouteControllerDialog2.mVolumeGroupList.getChildAt(i);
                        if (mediaRouteControllerDialog2.mGroupMemberRoutesAdded.contains(mediaRouteControllerDialog2.mVolumeGroupAdapter.getItem(firstVisiblePosition + i))) {
                            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                            alphaAnimation.setDuration(mediaRouteControllerDialog2.mGroupListFadeInDurationMs);
                            alphaAnimation.setFillEnabled(true);
                            alphaAnimation.setFillAfter(true);
                            if (!z) {
                                alphaAnimation.setAnimationListener(animationListener);
                                z = true;
                            }
                            childAt.clearAnimation();
                            childAt.startAnimation(alphaAnimation);
                        }
                    }
                }
            });
        }
    };
    public Context mContext = getContext();
    public MediaControllerCallback mControllerCallback = new MediaControllerCallback();
    public final MediaRouter mRouter = MediaRouter.getInstance(this.mContext);

    /* renamed from: androidx.mediarouter.app.MediaRouteControllerDialog$10  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass10 implements OverlayListView.OverlayObject.OnAnimationEndListener {
        public final /* synthetic */ MediaRouter.RouteInfo val$route;

        public AnonymousClass10(MediaRouter.RouteInfo routeInfo) {
            this.val$route = routeInfo;
        }
    }

    /* loaded from: classes.dex */
    public final class ClickListener implements View.OnClickListener {
        public ClickListener() {
        }

        /* JADX WARN: Removed duplicated region for block: B:39:0x00ae  */
        /* JADX WARN: Removed duplicated region for block: B:56:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
        @Override // android.view.View.OnClickListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void onClick(android.view.View r11) {
            /*
                Method dump skipped, instructions count: 271
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.mediarouter.app.MediaRouteControllerDialog.ClickListener.onClick(android.view.View):void");
        }
    }

    /* loaded from: classes.dex */
    public class FetchArtTask extends AsyncTask<Void, Void, Bitmap> {
        public int mBackgroundColor;
        public final Bitmap mIconBitmap;
        public final Uri mIconUri;
        public long mStartTimeMillis;

        public FetchArtTask() {
            Bitmap bitmap;
            boolean z;
            MediaDescriptionCompat mediaDescriptionCompat = MediaRouteControllerDialog.this.mDescription;
            Uri uri = null;
            if (mediaDescriptionCompat == null) {
                bitmap = null;
            } else {
                bitmap = mediaDescriptionCompat.mIcon;
            }
            if (bitmap == null || !bitmap.isRecycled()) {
                z = false;
            } else {
                z = true;
            }
            if (z) {
                Log.w("MediaRouteCtrlDialog", "Can't fetch the given art bitmap because it's already recycled.");
                bitmap = null;
            }
            this.mIconBitmap = bitmap;
            MediaDescriptionCompat mediaDescriptionCompat2 = MediaRouteControllerDialog.this.mDescription;
            this.mIconUri = mediaDescriptionCompat2 != null ? mediaDescriptionCompat2.mIconUri : uri;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:52:0x00e4  */
        /* JADX WARN: Removed duplicated region for block: B:53:0x00f9  */
        /* JADX WARN: Type inference failed for: r4v0 */
        /* JADX WARN: Type inference failed for: r4v2, types: [java.io.InputStream] */
        /* JADX WARN: Type inference failed for: r4v3 */
        /* JADX WARN: Unknown variable types count: 1 */
        @Override // android.os.AsyncTask
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final android.graphics.Bitmap doInBackground(java.lang.Void[] r11) {
            /*
                Method dump skipped, instructions count: 306
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.mediarouter.app.MediaRouteControllerDialog.FetchArtTask.doInBackground(java.lang.Object[]):java.lang.Object");
        }

        @Override // android.os.AsyncTask
        public final void onPostExecute(Bitmap bitmap) {
            Bitmap bitmap2 = bitmap;
            MediaRouteControllerDialog mediaRouteControllerDialog = MediaRouteControllerDialog.this;
            mediaRouteControllerDialog.mFetchArtTask = null;
            if (!Objects.equals(mediaRouteControllerDialog.mArtIconBitmap, this.mIconBitmap) || !Objects.equals(MediaRouteControllerDialog.this.mArtIconUri, this.mIconUri)) {
                MediaRouteControllerDialog mediaRouteControllerDialog2 = MediaRouteControllerDialog.this;
                mediaRouteControllerDialog2.mArtIconBitmap = this.mIconBitmap;
                mediaRouteControllerDialog2.mArtIconLoadedBitmap = bitmap2;
                mediaRouteControllerDialog2.mArtIconUri = this.mIconUri;
                mediaRouteControllerDialog2.mArtIconBackgroundColor = this.mBackgroundColor;
                boolean z = true;
                mediaRouteControllerDialog2.mArtIconIsLoaded = true;
                MediaRouteControllerDialog mediaRouteControllerDialog3 = MediaRouteControllerDialog.this;
                if (SystemClock.uptimeMillis() - this.mStartTimeMillis <= 120) {
                    z = false;
                }
                mediaRouteControllerDialog3.update(z);
            }
        }

        @Override // android.os.AsyncTask
        public final void onPreExecute() {
            this.mStartTimeMillis = SystemClock.uptimeMillis();
            MediaRouteControllerDialog mediaRouteControllerDialog = MediaRouteControllerDialog.this;
            Objects.requireNonNull(mediaRouteControllerDialog);
            mediaRouteControllerDialog.mArtIconIsLoaded = false;
            mediaRouteControllerDialog.mArtIconLoadedBitmap = null;
            mediaRouteControllerDialog.mArtIconBackgroundColor = 0;
        }

        public final BufferedInputStream openInputStreamByScheme(Uri uri) throws IOException {
            InputStream inputStream;
            String lowerCase = uri.getScheme().toLowerCase();
            if ("android.resource".equals(lowerCase) || "content".equals(lowerCase) || "file".equals(lowerCase)) {
                inputStream = MediaRouteControllerDialog.this.mContext.getContentResolver().openInputStream(uri);
            } else {
                URLConnection openConnection = new URL(uri.toString()).openConnection();
                int i = MediaRouteControllerDialog.CONNECTION_TIMEOUT_MILLIS;
                openConnection.setConnectTimeout(i);
                openConnection.setReadTimeout(i);
                inputStream = openConnection.getInputStream();
            }
            if (inputStream == null) {
                return null;
            }
            return new BufferedInputStream(inputStream);
        }
    }

    /* loaded from: classes.dex */
    public final class MediaControllerCallback extends MediaControllerCompat.Callback {
        public MediaControllerCallback() {
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.Callback
        public final void onMetadataChanged(MediaMetadataCompat mediaMetadataCompat) {
            MediaDescriptionCompat mediaDescriptionCompat;
            MediaRouteControllerDialog mediaRouteControllerDialog = MediaRouteControllerDialog.this;
            if (mediaMetadataCompat == null) {
                mediaDescriptionCompat = null;
            } else {
                mediaDescriptionCompat = mediaMetadataCompat.getDescription();
            }
            mediaRouteControllerDialog.mDescription = mediaDescriptionCompat;
            MediaRouteControllerDialog.this.updateArtIconIfNeeded();
            MediaRouteControllerDialog.this.update(false);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.Callback
        public final void onPlaybackStateChanged(PlaybackStateCompat playbackStateCompat) {
            MediaRouteControllerDialog mediaRouteControllerDialog = MediaRouteControllerDialog.this;
            mediaRouteControllerDialog.mState = playbackStateCompat;
            mediaRouteControllerDialog.update(false);
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.Callback
        public final void onSessionDestroyed() {
            MediaRouteControllerDialog mediaRouteControllerDialog = MediaRouteControllerDialog.this;
            MediaControllerCompat mediaControllerCompat = mediaRouteControllerDialog.mMediaController;
            if (mediaControllerCompat != null) {
                mediaControllerCompat.unregisterCallback(mediaRouteControllerDialog.mControllerCallback);
                MediaRouteControllerDialog.this.mMediaController = null;
            }
        }
    }

    /* loaded from: classes.dex */
    public final class MediaRouterCallback extends MediaRouter.Callback {
        public MediaRouterCallback() {
        }

        @Override // androidx.mediarouter.media.MediaRouter.Callback
        public final void onRouteChanged(MediaRouter.RouteInfo routeInfo) {
            MediaRouteControllerDialog.this.update(true);
        }

        @Override // androidx.mediarouter.media.MediaRouter.Callback
        public final void onRouteUnselected() {
            MediaRouteControllerDialog.this.update(false);
        }

        @Override // androidx.mediarouter.media.MediaRouter.Callback
        public final void onRouteVolumeChanged(MediaRouter.RouteInfo routeInfo) {
            SeekBar seekBar = (SeekBar) MediaRouteControllerDialog.this.mVolumeSliderMap.get(routeInfo);
            int i = routeInfo.mVolume;
            if (MediaRouteControllerDialog.DEBUG) {
                ExifInterface$$ExternalSyntheticOutline1.m("onRouteVolumeChanged(), route.getVolume:", i, "MediaRouteCtrlDialog");
            }
            if (seekBar != null && MediaRouteControllerDialog.this.mRouteInVolumeSliderTouched != routeInfo) {
                seekBar.setProgress(i);
            }
        }
    }

    /* loaded from: classes.dex */
    public class VolumeChangeListener implements SeekBar.OnSeekBarChangeListener {
        public final AnonymousClass1 mStopTrackingTouch = new Runnable() { // from class: androidx.mediarouter.app.MediaRouteControllerDialog.VolumeChangeListener.1
            @Override // java.lang.Runnable
            public final void run() {
                MediaRouteControllerDialog mediaRouteControllerDialog = MediaRouteControllerDialog.this;
                if (mediaRouteControllerDialog.mRouteInVolumeSliderTouched != null) {
                    mediaRouteControllerDialog.mRouteInVolumeSliderTouched = null;
                    if (mediaRouteControllerDialog.mHasPendingUpdate) {
                        mediaRouteControllerDialog.update(mediaRouteControllerDialog.mPendingUpdateAnimationNeeded);
                    }
                }
            }
        };

        /* JADX WARN: Type inference failed for: r1v1, types: [androidx.mediarouter.app.MediaRouteControllerDialog$VolumeChangeListener$1] */
        public VolumeChangeListener() {
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public final void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            if (z) {
                MediaRouter.RouteInfo routeInfo = (MediaRouter.RouteInfo) seekBar.getTag();
                if (MediaRouteControllerDialog.DEBUG) {
                    Log.d("MediaRouteCtrlDialog", "onProgressChanged(): calling MediaRouter.RouteInfo.requestSetVolume(" + i + ")");
                }
                routeInfo.requestSetVolume(i);
            }
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public final void onStartTrackingTouch(SeekBar seekBar) {
            MediaRouteControllerDialog mediaRouteControllerDialog = MediaRouteControllerDialog.this;
            if (mediaRouteControllerDialog.mRouteInVolumeSliderTouched != null) {
                mediaRouteControllerDialog.mVolumeSlider.removeCallbacks(this.mStopTrackingTouch);
            }
            MediaRouteControllerDialog.this.mRouteInVolumeSliderTouched = (MediaRouter.RouteInfo) seekBar.getTag();
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public final void onStopTrackingTouch(SeekBar seekBar) {
            MediaRouteControllerDialog.this.mVolumeSlider.postDelayed(this.mStopTrackingTouch, 500L);
        }
    }

    /* loaded from: classes.dex */
    public class VolumeGroupAdapter extends ArrayAdapter<MediaRouter.RouteInfo> {
        public final float mDisabledAlpha;

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public final View getView(int i, View view, ViewGroup viewGroup) {
            int i2;
            boolean z;
            int i3 = 0;
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(2131624280, viewGroup, false);
            } else {
                MediaRouteControllerDialog mediaRouteControllerDialog = MediaRouteControllerDialog.this;
                Objects.requireNonNull(mediaRouteControllerDialog);
                MediaRouteControllerDialog.setLayoutHeight((LinearLayout) view.findViewById(2131429221), mediaRouteControllerDialog.mVolumeGroupListItemHeight);
                View findViewById = view.findViewById(2131428447);
                ViewGroup.LayoutParams layoutParams = findViewById.getLayoutParams();
                int i4 = mediaRouteControllerDialog.mVolumeGroupListItemIconSize;
                layoutParams.width = i4;
                layoutParams.height = i4;
                findViewById.setLayoutParams(layoutParams);
            }
            MediaRouter.RouteInfo item = getItem(i);
            if (item != null) {
                boolean z2 = item.mEnabled;
                TextView textView = (TextView) view.findViewById(2131428436);
                textView.setEnabled(z2);
                textView.setText(item.mName);
                MediaRouteVolumeSlider mediaRouteVolumeSlider = (MediaRouteVolumeSlider) view.findViewById(2131428448);
                MediaRouterThemeHelper.setVolumeSliderColor(viewGroup.getContext(), mediaRouteVolumeSlider, MediaRouteControllerDialog.this.mVolumeGroupList);
                mediaRouteVolumeSlider.setTag(item);
                MediaRouteControllerDialog.this.mVolumeSliderMap.put(item, mediaRouteVolumeSlider);
                mediaRouteVolumeSlider.setHideThumb(!z2);
                mediaRouteVolumeSlider.setEnabled(z2);
                if (z2) {
                    MediaRouteControllerDialog mediaRouteControllerDialog2 = MediaRouteControllerDialog.this;
                    Objects.requireNonNull(mediaRouteControllerDialog2);
                    if (!mediaRouteControllerDialog2.mVolumeControlEnabled || item.getVolumeHandling() != 1) {
                        z = false;
                    } else {
                        z = true;
                    }
                    if (z) {
                        mediaRouteVolumeSlider.setMax(item.mVolumeMax);
                        mediaRouteVolumeSlider.setProgress(item.mVolume);
                        mediaRouteVolumeSlider.setOnSeekBarChangeListener(MediaRouteControllerDialog.this.mVolumeChangeListener);
                    } else {
                        mediaRouteVolumeSlider.setMax(100);
                        mediaRouteVolumeSlider.setProgress(100);
                        mediaRouteVolumeSlider.setEnabled(false);
                    }
                }
                ImageView imageView = (ImageView) view.findViewById(2131428447);
                if (z2) {
                    i2 = 255;
                } else {
                    i2 = (int) (this.mDisabledAlpha * 255.0f);
                }
                imageView.setAlpha(i2);
                LinearLayout linearLayout = (LinearLayout) view.findViewById(2131429221);
                if (MediaRouteControllerDialog.this.mGroupMemberRoutesAnimatingWithBitmap.contains(item)) {
                    i3 = 4;
                }
                linearLayout.setVisibility(i3);
                HashSet hashSet = MediaRouteControllerDialog.this.mGroupMemberRoutesAdded;
                if (hashSet != null && hashSet.contains(item)) {
                    AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 0.0f);
                    alphaAnimation.setDuration(0L);
                    alphaAnimation.setFillEnabled(true);
                    alphaAnimation.setFillAfter(true);
                    view.clearAnimation();
                    view.startAnimation(alphaAnimation);
                }
            }
            return view;
        }

        @Override // android.widget.BaseAdapter, android.widget.ListAdapter
        public final boolean isEnabled(int i) {
            return false;
        }

        public VolumeGroupAdapter(Context context, List<MediaRouter.RouteInfo> list) {
            super(context, 0, list);
            this.mDisabledAlpha = MediaRouterThemeHelper.getDisabledAlpha(context);
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /* JADX WARN: Type inference failed for: r1v1, types: [androidx.mediarouter.app.MediaRouteControllerDialog$1] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public MediaRouteControllerDialog(android.content.Context r3) {
        /*
            r2 = this;
            r0 = 1
            android.view.ContextThemeWrapper r3 = androidx.mediarouter.app.MediaRouterThemeHelper.createThemedDialogContext(r3, r0)
            int r1 = androidx.mediarouter.app.MediaRouterThemeHelper.createThemedDialogStyle(r3)
            r2.<init>(r3, r1)
            r2.mVolumeControlEnabled = r0
            androidx.mediarouter.app.MediaRouteControllerDialog$1 r1 = new androidx.mediarouter.app.MediaRouteControllerDialog$1
            r1.<init>()
            r2.mGroupListFadeInAnimation = r1
            android.content.Context r1 = r2.getContext()
            r2.mContext = r1
            androidx.mediarouter.app.MediaRouteControllerDialog$MediaControllerCallback r1 = new androidx.mediarouter.app.MediaRouteControllerDialog$MediaControllerCallback
            r1.<init>()
            r2.mControllerCallback = r1
            android.content.Context r1 = r2.mContext
            androidx.mediarouter.media.MediaRouter r1 = androidx.mediarouter.media.MediaRouter.getInstance(r1)
            r2.mRouter = r1
            androidx.mediarouter.media.MediaRouter$GlobalMediaRouter r1 = androidx.mediarouter.media.MediaRouter.sGlobal
            if (r1 != 0) goto L_0x0030
            r0 = 0
            goto L_0x0037
        L_0x0030:
            androidx.mediarouter.media.MediaRouter$GlobalMediaRouter r1 = androidx.mediarouter.media.MediaRouter.getGlobalRouter()
            java.util.Objects.requireNonNull(r1)
        L_0x0037:
            r2.mEnableGroupVolumeUX = r0
            androidx.mediarouter.app.MediaRouteControllerDialog$MediaRouterCallback r0 = new androidx.mediarouter.app.MediaRouteControllerDialog$MediaRouterCallback
            r0.<init>()
            r2.mCallback = r0
            androidx.mediarouter.media.MediaRouter$RouteInfo r0 = androidx.mediarouter.media.MediaRouter.getSelectedRoute()
            r2.mRoute = r0
            r2.setMediaSession()
            android.content.Context r0 = r2.mContext
            android.content.res.Resources r0 = r0.getResources()
            r1 = 2131166378(0x7f0704aa, float:1.7947E38)
            int r0 = r0.getDimensionPixelSize(r1)
            r2.mVolumeGroupListPaddingTop = r0
            android.content.Context r0 = r2.mContext
            java.lang.String r1 = "accessibility"
            java.lang.Object r0 = r0.getSystemService(r1)
            android.view.accessibility.AccessibilityManager r0 = (android.view.accessibility.AccessibilityManager) r0
            r2.mAccessibilityManager = r0
            r0 = 2131558409(0x7f0d0009, float:1.8742133E38)
            android.view.animation.Interpolator r0 = android.view.animation.AnimationUtils.loadInterpolator(r3, r0)
            r2.mLinearOutSlowInInterpolator = r0
            r0 = 2131558408(0x7f0d0008, float:1.874213E38)
            android.view.animation.Interpolator r3 = android.view.animation.AnimationUtils.loadInterpolator(r3, r0)
            r2.mFastOutSlowInInterpolator = r3
            android.view.animation.AccelerateDecelerateInterpolator r3 = new android.view.animation.AccelerateDecelerateInterpolator
            r3.<init>()
            r2.mAccelerateDecelerateInterpolator = r3
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.mediarouter.app.MediaRouteControllerDialog.<init>(android.content.Context):void");
    }

    public final void finishAnimation(boolean z) {
        this.mGroupMemberRoutesAdded = null;
        this.mGroupMemberRoutesRemoved = null;
        this.mIsGroupListAnimating = false;
        if (this.mIsGroupListAnimationPending) {
            this.mIsGroupListAnimationPending = false;
            updateLayoutHeight(z);
        }
        this.mVolumeGroupList.setEnabled(true);
    }

    public final int getMainControllerHeight(boolean z) {
        int i;
        if (!z && this.mVolumeControlLayout.getVisibility() != 0) {
            return 0;
        }
        int paddingBottom = this.mMediaMainControlLayout.getPaddingBottom() + this.mMediaMainControlLayout.getPaddingTop() + 0;
        if (z) {
            paddingBottom += this.mPlaybackControlLayout.getMeasuredHeight();
        }
        if (this.mVolumeControlLayout.getVisibility() == 0) {
            i = this.mVolumeControlLayout.getMeasuredHeight() + paddingBottom;
        } else {
            i = paddingBottom;
        }
        if (!z || this.mVolumeControlLayout.getVisibility() != 0) {
            return i;
        }
        return i + this.mDividerView.getMeasuredHeight();
    }

    public final boolean canShowPlaybackControlLayout() {
        if (this.mDescription == null && this.mState == null) {
            return false;
        }
        return true;
    }

    public final void clearGroupListAnimation(boolean z) {
        HashSet hashSet;
        int firstVisiblePosition = this.mVolumeGroupList.getFirstVisiblePosition();
        for (int i = 0; i < this.mVolumeGroupList.getChildCount(); i++) {
            View childAt = this.mVolumeGroupList.getChildAt(i);
            MediaRouter.RouteInfo item = this.mVolumeGroupAdapter.getItem(firstVisiblePosition + i);
            if (!z || (hashSet = this.mGroupMemberRoutesAdded) == null || !hashSet.contains(item)) {
                ((LinearLayout) childAt.findViewById(2131429221)).setVisibility(0);
                AnimationSet animationSet = new AnimationSet(true);
                AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 1.0f);
                alphaAnimation.setDuration(0L);
                animationSet.addAnimation(alphaAnimation);
                new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f).setDuration(0L);
                animationSet.setFillAfter(true);
                animationSet.setFillEnabled(true);
                childAt.clearAnimation();
                childAt.startAnimation(animationSet);
            }
        }
        OverlayListView overlayListView = this.mVolumeGroupList;
        Objects.requireNonNull(overlayListView);
        Iterator it = overlayListView.mOverlayObjects.iterator();
        while (it.hasNext()) {
            OverlayListView.OverlayObject overlayObject = (OverlayListView.OverlayObject) it.next();
            Objects.requireNonNull(overlayObject);
            overlayObject.mIsAnimationStarted = true;
            overlayObject.mIsAnimationEnded = true;
            OverlayListView.OverlayObject.OnAnimationEndListener onAnimationEndListener = overlayObject.mListener;
            if (onAnimationEndListener != null) {
                AnonymousClass10 r2 = (AnonymousClass10) onAnimationEndListener;
                MediaRouteControllerDialog.this.mGroupMemberRoutesAnimatingWithBitmap.remove(r2.val$route);
                MediaRouteControllerDialog.this.mVolumeGroupAdapter.notifyDataSetChanged();
            }
        }
        if (!z) {
            finishAnimation(false);
        }
    }

    public final boolean isGroup() {
        if (!this.mRoute.isGroup() || this.mRoute.getMemberRoutes().size() <= 1) {
            return false;
        }
        return true;
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public final void onDetachedFromWindow() {
        this.mRouter.removeCallback(this.mCallback);
        setMediaSession();
        this.mAttachedToWindow = false;
        super.onDetachedFromWindow();
    }

    @Override // androidx.appcompat.app.AlertDialog, android.app.Dialog, android.view.KeyEvent.Callback
    public final boolean onKeyDown(int i, KeyEvent keyEvent) {
        int i2;
        if (i != 25 && i != 24) {
            return super.onKeyDown(i, keyEvent);
        }
        if (this.mEnableGroupVolumeUX || !this.mIsGroupExpanded) {
            MediaRouter.RouteInfo routeInfo = this.mRoute;
            if (i == 25) {
                i2 = -1;
            } else {
                i2 = 1;
            }
            routeInfo.requestUpdateVolume(i2);
        }
        return true;
    }

    @Override // androidx.appcompat.app.AlertDialog, android.app.Dialog, android.view.KeyEvent.Callback
    public final boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i == 25 || i == 24) {
            return true;
        }
        return super.onKeyUp(i, keyEvent);
    }

    public final void setMediaSession() {
        MediaControllerCompat mediaControllerCompat = this.mMediaController;
        if (mediaControllerCompat != null) {
            mediaControllerCompat.unregisterCallback(this.mControllerCallback);
            this.mMediaController = null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:125:0x01e9  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x01ef  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0165  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0167  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x016f  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0171  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0179  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void update(boolean r12) {
        /*
            Method dump skipped, instructions count: 525
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.mediarouter.app.MediaRouteControllerDialog.update(boolean):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0035, code lost:
        if (r0 == false) goto L_0x0037;
     */
    /* JADX WARN: Removed duplicated region for block: B:29:0x003c  */
    /* JADX WARN: Removed duplicated region for block: B:38:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateArtIconIfNeeded() {
        /*
            r6 = this;
            android.support.v4.media.MediaDescriptionCompat r0 = r6.mDescription
            r1 = 0
            if (r0 != 0) goto L_0x0007
            r2 = r1
            goto L_0x0009
        L_0x0007:
            android.graphics.Bitmap r2 = r0.mIcon
        L_0x0009:
            if (r0 != 0) goto L_0x000c
            goto L_0x000e
        L_0x000c:
            android.net.Uri r1 = r0.mIconUri
        L_0x000e:
            androidx.mediarouter.app.MediaRouteControllerDialog$FetchArtTask r0 = r6.mFetchArtTask
            if (r0 != 0) goto L_0x0015
            android.graphics.Bitmap r3 = r6.mArtIconBitmap
            goto L_0x0017
        L_0x0015:
            android.graphics.Bitmap r3 = r0.mIconBitmap
        L_0x0017:
            if (r0 != 0) goto L_0x001c
            android.net.Uri r0 = r6.mArtIconUri
            goto L_0x001e
        L_0x001c:
            android.net.Uri r0 = r0.mIconUri
        L_0x001e:
            r4 = 0
            r5 = 1
            if (r3 == r2) goto L_0x0023
            goto L_0x0037
        L_0x0023:
            if (r3 != 0) goto L_0x0039
            if (r0 == 0) goto L_0x002e
            boolean r2 = r0.equals(r1)
            if (r2 == 0) goto L_0x002e
            goto L_0x0032
        L_0x002e:
            if (r0 != 0) goto L_0x0034
            if (r1 != 0) goto L_0x0034
        L_0x0032:
            r0 = r5
            goto L_0x0035
        L_0x0034:
            r0 = r4
        L_0x0035:
            if (r0 != 0) goto L_0x0039
        L_0x0037:
            r0 = r5
            goto L_0x003a
        L_0x0039:
            r0 = r4
        L_0x003a:
            if (r0 == 0) goto L_0x005a
            boolean r0 = r6.isGroup()
            if (r0 == 0) goto L_0x0047
            boolean r0 = r6.mEnableGroupVolumeUX
            if (r0 != 0) goto L_0x0047
            goto L_0x005a
        L_0x0047:
            androidx.mediarouter.app.MediaRouteControllerDialog$FetchArtTask r0 = r6.mFetchArtTask
            if (r0 == 0) goto L_0x004e
            r0.cancel(r5)
        L_0x004e:
            androidx.mediarouter.app.MediaRouteControllerDialog$FetchArtTask r0 = new androidx.mediarouter.app.MediaRouteControllerDialog$FetchArtTask
            r0.<init>()
            r6.mFetchArtTask = r0
            java.lang.Void[] r6 = new java.lang.Void[r4]
            r0.execute(r6)
        L_0x005a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.mediarouter.app.MediaRouteControllerDialog.updateArtIconIfNeeded():void");
    }

    public final void updateLayout() {
        int dialogWidth = MediaRouteDialogHelper.getDialogWidth(this.mContext);
        getWindow().setLayout(dialogWidth, -2);
        View decorView = getWindow().getDecorView();
        this.mDialogContentWidth = (dialogWidth - decorView.getPaddingLeft()) - decorView.getPaddingRight();
        Resources resources = this.mContext.getResources();
        this.mVolumeGroupListItemIconSize = resources.getDimensionPixelSize(2131166376);
        this.mVolumeGroupListItemHeight = resources.getDimensionPixelSize(2131166375);
        this.mVolumeGroupListMaxHeight = resources.getDimensionPixelSize(2131166377);
        this.mArtIconBitmap = null;
        this.mArtIconUri = null;
        updateArtIconIfNeeded();
        update(false);
    }

    public final void updateLayoutHeight(final boolean z) {
        this.mDefaultControlLayout.requestLayout();
        this.mDefaultControlLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: androidx.mediarouter.app.MediaRouteControllerDialog.6
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public final void onGlobalLayout() {
                int i;
                int i2;
                boolean z2;
                boolean z3;
                final HashMap hashMap;
                final HashMap hashMap2;
                Bitmap bitmap;
                float f;
                float f2;
                ImageView.ScaleType scaleType;
                MediaRouteControllerDialog.this.mDefaultControlLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                final MediaRouteControllerDialog mediaRouteControllerDialog = MediaRouteControllerDialog.this;
                if (mediaRouteControllerDialog.mIsGroupListAnimating) {
                    mediaRouteControllerDialog.mIsGroupListAnimationPending = true;
                    return;
                }
                boolean z4 = z;
                int i3 = mediaRouteControllerDialog.mMediaMainControlLayout.getLayoutParams().height;
                MediaRouteControllerDialog.setLayoutHeight(mediaRouteControllerDialog.mMediaMainControlLayout, -1);
                mediaRouteControllerDialog.updateMediaControlVisibility(mediaRouteControllerDialog.canShowPlaybackControlLayout());
                View decorView = mediaRouteControllerDialog.getWindow().getDecorView();
                decorView.measure(View.MeasureSpec.makeMeasureSpec(mediaRouteControllerDialog.getWindow().getAttributes().width, 1073741824), 0);
                MediaRouteControllerDialog.setLayoutHeight(mediaRouteControllerDialog.mMediaMainControlLayout, i3);
                if (!(mediaRouteControllerDialog.mArtView.getDrawable() instanceof BitmapDrawable) || (bitmap = ((BitmapDrawable) mediaRouteControllerDialog.mArtView.getDrawable()).getBitmap()) == null) {
                    i = 0;
                } else {
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    if (width >= height) {
                        f = mediaRouteControllerDialog.mDialogContentWidth * height;
                        f2 = width;
                    } else {
                        f = mediaRouteControllerDialog.mDialogContentWidth * 9.0f;
                        f2 = 16.0f;
                    }
                    i = (int) ((f / f2) + 0.5f);
                    ImageView imageView = mediaRouteControllerDialog.mArtView;
                    if (bitmap.getWidth() >= bitmap.getHeight()) {
                        scaleType = ImageView.ScaleType.FIT_XY;
                    } else {
                        scaleType = ImageView.ScaleType.FIT_CENTER;
                    }
                    imageView.setScaleType(scaleType);
                }
                int mainControllerHeight = mediaRouteControllerDialog.getMainControllerHeight(mediaRouteControllerDialog.canShowPlaybackControlLayout());
                int size = mediaRouteControllerDialog.mGroupMemberRoutes.size();
                if (mediaRouteControllerDialog.isGroup()) {
                    i2 = mediaRouteControllerDialog.mRoute.getMemberRoutes().size() * mediaRouteControllerDialog.mVolumeGroupListItemHeight;
                } else {
                    i2 = 0;
                }
                if (size > 0) {
                    i2 += mediaRouteControllerDialog.mVolumeGroupListPaddingTop;
                }
                int min = Math.min(i2, mediaRouteControllerDialog.mVolumeGroupListMaxHeight);
                if (!mediaRouteControllerDialog.mIsGroupExpanded) {
                    min = 0;
                }
                int max = Math.max(i, min) + mainControllerHeight;
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                int height2 = rect.height() - (mediaRouteControllerDialog.mDialogAreaLayout.getMeasuredHeight() - mediaRouteControllerDialog.mDefaultControlLayout.getMeasuredHeight());
                if (i <= 0 || max > height2) {
                    if (mediaRouteControllerDialog.mMediaMainControlLayout.getMeasuredHeight() + mediaRouteControllerDialog.mVolumeGroupList.getLayoutParams().height >= mediaRouteControllerDialog.mDefaultControlLayout.getMeasuredHeight()) {
                        mediaRouteControllerDialog.mArtView.setVisibility(8);
                    }
                    max = min + mainControllerHeight;
                    i = 0;
                } else {
                    mediaRouteControllerDialog.mArtView.setVisibility(0);
                    MediaRouteControllerDialog.setLayoutHeight(mediaRouteControllerDialog.mArtView, i);
                }
                if (!mediaRouteControllerDialog.canShowPlaybackControlLayout() || max > height2) {
                    mediaRouteControllerDialog.mPlaybackControlLayout.setVisibility(8);
                } else {
                    mediaRouteControllerDialog.mPlaybackControlLayout.setVisibility(0);
                }
                if (mediaRouteControllerDialog.mPlaybackControlLayout.getVisibility() == 0) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                mediaRouteControllerDialog.updateMediaControlVisibility(z2);
                if (mediaRouteControllerDialog.mPlaybackControlLayout.getVisibility() == 0) {
                    z3 = true;
                } else {
                    z3 = false;
                }
                int mainControllerHeight2 = mediaRouteControllerDialog.getMainControllerHeight(z3);
                int max2 = Math.max(i, min) + mainControllerHeight2;
                if (max2 > height2) {
                    min -= max2 - height2;
                } else {
                    height2 = max2;
                }
                mediaRouteControllerDialog.mMediaMainControlLayout.clearAnimation();
                mediaRouteControllerDialog.mVolumeGroupList.clearAnimation();
                mediaRouteControllerDialog.mDefaultControlLayout.clearAnimation();
                if (z4) {
                    mediaRouteControllerDialog.animateLayoutHeight(mediaRouteControllerDialog.mMediaMainControlLayout, mainControllerHeight2);
                    mediaRouteControllerDialog.animateLayoutHeight(mediaRouteControllerDialog.mVolumeGroupList, min);
                    mediaRouteControllerDialog.animateLayoutHeight(mediaRouteControllerDialog.mDefaultControlLayout, height2);
                } else {
                    MediaRouteControllerDialog.setLayoutHeight(mediaRouteControllerDialog.mMediaMainControlLayout, mainControllerHeight2);
                    MediaRouteControllerDialog.setLayoutHeight(mediaRouteControllerDialog.mVolumeGroupList, min);
                    MediaRouteControllerDialog.setLayoutHeight(mediaRouteControllerDialog.mDefaultControlLayout, height2);
                }
                MediaRouteControllerDialog.setLayoutHeight(mediaRouteControllerDialog.mExpandableAreaLayout, rect.height());
                List<MediaRouter.RouteInfo> memberRoutes = mediaRouteControllerDialog.mRoute.getMemberRoutes();
                if (memberRoutes.isEmpty()) {
                    mediaRouteControllerDialog.mGroupMemberRoutes.clear();
                    mediaRouteControllerDialog.mVolumeGroupAdapter.notifyDataSetChanged();
                } else if (new HashSet(mediaRouteControllerDialog.mGroupMemberRoutes).equals(new HashSet(memberRoutes))) {
                    mediaRouteControllerDialog.mVolumeGroupAdapter.notifyDataSetChanged();
                } else {
                    if (z4) {
                        OverlayListView overlayListView = mediaRouteControllerDialog.mVolumeGroupList;
                        VolumeGroupAdapter volumeGroupAdapter = mediaRouteControllerDialog.mVolumeGroupAdapter;
                        hashMap = new HashMap();
                        int firstVisiblePosition = overlayListView.getFirstVisiblePosition();
                        for (int i4 = 0; i4 < overlayListView.getChildCount(); i4++) {
                            MediaRouter.RouteInfo item = volumeGroupAdapter.getItem(firstVisiblePosition + i4);
                            View childAt = overlayListView.getChildAt(i4);
                            hashMap.put(item, new Rect(childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom()));
                        }
                    } else {
                        hashMap = null;
                    }
                    if (z4) {
                        Context context = mediaRouteControllerDialog.mContext;
                        OverlayListView overlayListView2 = mediaRouteControllerDialog.mVolumeGroupList;
                        VolumeGroupAdapter volumeGroupAdapter2 = mediaRouteControllerDialog.mVolumeGroupAdapter;
                        hashMap2 = new HashMap();
                        int firstVisiblePosition2 = overlayListView2.getFirstVisiblePosition();
                        for (int i5 = 0; i5 < overlayListView2.getChildCount(); i5++) {
                            MediaRouter.RouteInfo item2 = volumeGroupAdapter2.getItem(firstVisiblePosition2 + i5);
                            View childAt2 = overlayListView2.getChildAt(i5);
                            Bitmap createBitmap = Bitmap.createBitmap(childAt2.getWidth(), childAt2.getHeight(), Bitmap.Config.ARGB_8888);
                            childAt2.draw(new Canvas(createBitmap));
                            hashMap2.put(item2, new BitmapDrawable(context.getResources(), createBitmap));
                        }
                    } else {
                        hashMap2 = null;
                    }
                    ArrayList arrayList = mediaRouteControllerDialog.mGroupMemberRoutes;
                    HashSet hashSet = new HashSet(memberRoutes);
                    hashSet.removeAll(arrayList);
                    mediaRouteControllerDialog.mGroupMemberRoutesAdded = hashSet;
                    HashSet hashSet2 = new HashSet(mediaRouteControllerDialog.mGroupMemberRoutes);
                    hashSet2.removeAll(memberRoutes);
                    mediaRouteControllerDialog.mGroupMemberRoutesRemoved = hashSet2;
                    mediaRouteControllerDialog.mGroupMemberRoutes.addAll(0, mediaRouteControllerDialog.mGroupMemberRoutesAdded);
                    mediaRouteControllerDialog.mGroupMemberRoutes.removeAll(mediaRouteControllerDialog.mGroupMemberRoutesRemoved);
                    mediaRouteControllerDialog.mVolumeGroupAdapter.notifyDataSetChanged();
                    if (z4 && mediaRouteControllerDialog.mIsGroupExpanded) {
                        if (mediaRouteControllerDialog.mGroupMemberRoutesRemoved.size() + mediaRouteControllerDialog.mGroupMemberRoutesAdded.size() > 0) {
                            mediaRouteControllerDialog.mVolumeGroupList.setEnabled(false);
                            mediaRouteControllerDialog.mVolumeGroupList.requestLayout();
                            mediaRouteControllerDialog.mIsGroupListAnimating = true;
                            mediaRouteControllerDialog.mVolumeGroupList.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: androidx.mediarouter.app.MediaRouteControllerDialog.8
                                @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
                                public final void onGlobalLayout() {
                                    OverlayListView.OverlayObject overlayObject;
                                    int i6;
                                    MediaRouteControllerDialog.this.mVolumeGroupList.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                    final MediaRouteControllerDialog mediaRouteControllerDialog2 = MediaRouteControllerDialog.this;
                                    Map map = hashMap;
                                    Map map2 = hashMap2;
                                    Objects.requireNonNull(mediaRouteControllerDialog2);
                                    HashSet hashSet3 = mediaRouteControllerDialog2.mGroupMemberRoutesAdded;
                                    if (!(hashSet3 == null || mediaRouteControllerDialog2.mGroupMemberRoutesRemoved == null)) {
                                        int size2 = hashSet3.size() - mediaRouteControllerDialog2.mGroupMemberRoutesRemoved.size();
                                        Animation.AnimationListener animationListener = new Animation.AnimationListener() { // from class: androidx.mediarouter.app.MediaRouteControllerDialog.9
                                            @Override // android.view.animation.Animation.AnimationListener
                                            public final void onAnimationEnd(Animation animation) {
                                            }

                                            @Override // android.view.animation.Animation.AnimationListener
                                            public final void onAnimationRepeat(Animation animation) {
                                            }

                                            @Override // android.view.animation.Animation.AnimationListener
                                            public final void onAnimationStart(Animation animation) {
                                                OverlayListView overlayListView3 = MediaRouteControllerDialog.this.mVolumeGroupList;
                                                Objects.requireNonNull(overlayListView3);
                                                Iterator it = overlayListView3.mOverlayObjects.iterator();
                                                while (it.hasNext()) {
                                                    OverlayListView.OverlayObject overlayObject2 = (OverlayListView.OverlayObject) it.next();
                                                    Objects.requireNonNull(overlayObject2);
                                                    if (!overlayObject2.mIsAnimationStarted) {
                                                        overlayObject2.mStartTime = overlayListView3.getDrawingTime();
                                                        overlayObject2.mIsAnimationStarted = true;
                                                    }
                                                }
                                                MediaRouteControllerDialog mediaRouteControllerDialog3 = MediaRouteControllerDialog.this;
                                                mediaRouteControllerDialog3.mVolumeGroupList.postDelayed(mediaRouteControllerDialog3.mGroupListFadeInAnimation, mediaRouteControllerDialog3.mGroupListAnimationDurationMs);
                                            }
                                        };
                                        int firstVisiblePosition3 = mediaRouteControllerDialog2.mVolumeGroupList.getFirstVisiblePosition();
                                        boolean z5 = false;
                                        for (int i7 = 0; i7 < mediaRouteControllerDialog2.mVolumeGroupList.getChildCount(); i7++) {
                                            View childAt3 = mediaRouteControllerDialog2.mVolumeGroupList.getChildAt(i7);
                                            MediaRouter.RouteInfo item3 = mediaRouteControllerDialog2.mVolumeGroupAdapter.getItem(firstVisiblePosition3 + i7);
                                            Rect rect2 = (Rect) map.get(item3);
                                            int top = childAt3.getTop();
                                            if (rect2 != null) {
                                                i6 = rect2.top;
                                            } else {
                                                i6 = (mediaRouteControllerDialog2.mVolumeGroupListItemHeight * size2) + top;
                                            }
                                            AnimationSet animationSet = new AnimationSet(true);
                                            HashSet hashSet4 = mediaRouteControllerDialog2.mGroupMemberRoutesAdded;
                                            if (hashSet4 != null && hashSet4.contains(item3)) {
                                                AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 0.0f);
                                                alphaAnimation.setDuration(mediaRouteControllerDialog2.mGroupListFadeInDurationMs);
                                                animationSet.addAnimation(alphaAnimation);
                                                i6 = top;
                                            }
                                            TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, i6 - top, 0.0f);
                                            translateAnimation.setDuration(mediaRouteControllerDialog2.mGroupListAnimationDurationMs);
                                            animationSet.addAnimation(translateAnimation);
                                            animationSet.setFillAfter(true);
                                            animationSet.setFillEnabled(true);
                                            animationSet.setInterpolator(mediaRouteControllerDialog2.mInterpolator);
                                            if (!z5) {
                                                animationSet.setAnimationListener(animationListener);
                                                z5 = true;
                                            }
                                            childAt3.clearAnimation();
                                            childAt3.startAnimation(animationSet);
                                            map.remove(item3);
                                            map2.remove(item3);
                                        }
                                        for (Map.Entry entry : map2.entrySet()) {
                                            MediaRouter.RouteInfo routeInfo = (MediaRouter.RouteInfo) entry.getKey();
                                            BitmapDrawable bitmapDrawable = (BitmapDrawable) entry.getValue();
                                            Rect rect3 = (Rect) map.get(routeInfo);
                                            if (mediaRouteControllerDialog2.mGroupMemberRoutesRemoved.contains(routeInfo)) {
                                                overlayObject = new OverlayListView.OverlayObject(bitmapDrawable, rect3);
                                                overlayObject.mStartAlpha = 1.0f;
                                                overlayObject.mEndAlpha = 0.0f;
                                                overlayObject.mDuration = mediaRouteControllerDialog2.mGroupListFadeOutDurationMs;
                                                overlayObject.mInterpolator = mediaRouteControllerDialog2.mInterpolator;
                                            } else {
                                                OverlayListView.OverlayObject overlayObject2 = new OverlayListView.OverlayObject(bitmapDrawable, rect3);
                                                overlayObject2.mDeltaY = mediaRouteControllerDialog2.mVolumeGroupListItemHeight * size2;
                                                overlayObject2.mDuration = mediaRouteControllerDialog2.mGroupListAnimationDurationMs;
                                                overlayObject2.mInterpolator = mediaRouteControllerDialog2.mInterpolator;
                                                overlayObject2.mListener = new AnonymousClass10(routeInfo);
                                                mediaRouteControllerDialog2.mGroupMemberRoutesAnimatingWithBitmap.add(routeInfo);
                                                overlayObject = overlayObject2;
                                            }
                                            OverlayListView overlayListView3 = mediaRouteControllerDialog2.mVolumeGroupList;
                                            Objects.requireNonNull(overlayListView3);
                                            overlayListView3.mOverlayObjects.add(overlayObject);
                                        }
                                    }
                                }
                            });
                            return;
                        }
                    }
                    mediaRouteControllerDialog.mGroupMemberRoutesAdded = null;
                    mediaRouteControllerDialog.mGroupMemberRoutesRemoved = null;
                }
            }
        });
    }

    public final void updateMediaControlVisibility(boolean z) {
        int i;
        View view = this.mDividerView;
        int i2 = 0;
        if (this.mVolumeControlLayout.getVisibility() != 0 || !z) {
            i = 8;
        } else {
            i = 0;
        }
        view.setVisibility(i);
        LinearLayout linearLayout = this.mMediaMainControlLayout;
        if (this.mVolumeControlLayout.getVisibility() == 8 && !z) {
            i2 = 8;
        }
        linearLayout.setVisibility(i2);
    }

    public static void setLayoutHeight(View view, int i) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = i;
        view.setLayoutParams(layoutParams);
    }

    public final void animateLayoutHeight(final ViewGroup viewGroup, final int i) {
        final int i2 = viewGroup.getLayoutParams().height;
        Animation animation = new Animation() { // from class: androidx.mediarouter.app.MediaRouteControllerDialog.7
            @Override // android.view.animation.Animation
            public final void applyTransformation(float f, Transformation transformation) {
                int i3 = i2;
                MediaRouteControllerDialog.setLayoutHeight(viewGroup, i3 - ((int) ((i3 - i) * f)));
            }
        };
        animation.setDuration(this.mGroupListAnimationDurationMs);
        animation.setInterpolator(this.mInterpolator);
        viewGroup.startAnimation(animation);
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mAttachedToWindow = true;
        this.mRouter.addCallback(MediaRouteSelector.EMPTY, this.mCallback, 2);
        Objects.requireNonNull(this.mRouter);
        boolean z = MediaRouter.DEBUG;
        setMediaSession();
    }

    @Override // androidx.appcompat.app.AlertDialog, androidx.appcompat.app.AppCompatDialog, android.app.Dialog
    public final void onCreate(Bundle bundle) {
        Interpolator interpolator;
        super.onCreate(bundle);
        getWindow().setBackgroundDrawableResource(17170445);
        setContentView(2131624279);
        findViewById(16908315).setVisibility(8);
        ClickListener clickListener = new ClickListener();
        FrameLayout frameLayout = (FrameLayout) findViewById(2131428432);
        this.mExpandableAreaLayout = frameLayout;
        frameLayout.setOnClickListener(new View.OnClickListener() { // from class: androidx.mediarouter.app.MediaRouteControllerDialog.2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MediaRouteControllerDialog.this.dismiss();
            }
        });
        LinearLayout linearLayout = (LinearLayout) findViewById(2131428431);
        this.mDialogAreaLayout = linearLayout;
        linearLayout.setOnClickListener(new View.OnClickListener() { // from class: androidx.mediarouter.app.MediaRouteControllerDialog.3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
            }
        });
        Context context = this.mContext;
        int themeColor = MediaRouterThemeHelper.getThemeColor(context, 0, 2130968838);
        if (ColorUtils.calculateContrast(themeColor, MediaRouterThemeHelper.getThemeColor(context, 0, 16842801)) < 3.0d) {
            themeColor = MediaRouterThemeHelper.getThemeColor(context, 0, 2130968814);
        }
        Button button = (Button) findViewById(16908314);
        this.mDisconnectButton = button;
        button.setText(2131952811);
        this.mDisconnectButton.setTextColor(themeColor);
        this.mDisconnectButton.setOnClickListener(clickListener);
        Button button2 = (Button) findViewById(16908313);
        this.mStopCastingButton = button2;
        button2.setText(2131952818);
        this.mStopCastingButton.setTextColor(themeColor);
        this.mStopCastingButton.setOnClickListener(clickListener);
        this.mRouteNameTextView = (TextView) findViewById(2131428436);
        ((ImageButton) findViewById(2131428423)).setOnClickListener(clickListener);
        FrameLayout frameLayout2 = (FrameLayout) findViewById(2131428429);
        this.mDefaultControlLayout = (FrameLayout) findViewById(2131428430);
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: androidx.mediarouter.app.MediaRouteControllerDialog.4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MediaControllerCompat mediaControllerCompat = MediaRouteControllerDialog.this.mMediaController;
                if (mediaControllerCompat != null) {
                    MediaControllerCompat.MediaControllerImplApi21 mediaControllerImplApi21 = mediaControllerCompat.mImpl;
                    Objects.requireNonNull(mediaControllerImplApi21);
                    PendingIntent sessionActivity = mediaControllerImplApi21.mControllerFwk.getSessionActivity();
                    if (sessionActivity != null) {
                        try {
                            sessionActivity.send();
                            MediaRouteControllerDialog.this.dismiss();
                        } catch (PendingIntent.CanceledException unused) {
                            Log.e("MediaRouteCtrlDialog", sessionActivity + " was not sent, it had been canceled.");
                        }
                    }
                }
            }
        };
        ImageView imageView = (ImageView) findViewById(2131428396);
        this.mArtView = imageView;
        imageView.setOnClickListener(onClickListener);
        findViewById(2131428428).setOnClickListener(onClickListener);
        this.mMediaMainControlLayout = (LinearLayout) findViewById(2131428435);
        this.mDividerView = findViewById(2131428424);
        this.mPlaybackControlLayout = (RelativeLayout) findViewById(2131428443);
        this.mTitleView = (TextView) findViewById(2131428427);
        this.mSubtitleView = (TextView) findViewById(2131428426);
        ImageButton imageButton = (ImageButton) findViewById(2131428425);
        this.mPlaybackControlButton = imageButton;
        imageButton.setOnClickListener(clickListener);
        LinearLayout linearLayout2 = (LinearLayout) findViewById(2131428445);
        this.mVolumeControlLayout = linearLayout2;
        linearLayout2.setVisibility(8);
        SeekBar seekBar = (SeekBar) findViewById(2131428448);
        this.mVolumeSlider = seekBar;
        seekBar.setTag(this.mRoute);
        VolumeChangeListener volumeChangeListener = new VolumeChangeListener();
        this.mVolumeChangeListener = volumeChangeListener;
        this.mVolumeSlider.setOnSeekBarChangeListener(volumeChangeListener);
        this.mVolumeGroupList = (OverlayListView) findViewById(2131428446);
        this.mGroupMemberRoutes = new ArrayList();
        VolumeGroupAdapter volumeGroupAdapter = new VolumeGroupAdapter(this.mVolumeGroupList.getContext(), this.mGroupMemberRoutes);
        this.mVolumeGroupAdapter = volumeGroupAdapter;
        this.mVolumeGroupList.setAdapter((ListAdapter) volumeGroupAdapter);
        this.mGroupMemberRoutesAnimatingWithBitmap = new HashSet();
        Context context2 = this.mContext;
        LinearLayout linearLayout3 = this.mMediaMainControlLayout;
        OverlayListView overlayListView = this.mVolumeGroupList;
        boolean isGroup = isGroup();
        int themeColor2 = MediaRouterThemeHelper.getThemeColor(context2, 0, 2130968838);
        int themeColor3 = MediaRouterThemeHelper.getThemeColor(context2, 0, 2130968840);
        if (isGroup && MediaRouterThemeHelper.getControllerColor(context2, 0) == -570425344) {
            themeColor3 = themeColor2;
            themeColor2 = -1;
        }
        linearLayout3.setBackgroundColor(themeColor2);
        overlayListView.setBackgroundColor(themeColor3);
        linearLayout3.setTag(Integer.valueOf(themeColor2));
        overlayListView.setTag(Integer.valueOf(themeColor3));
        MediaRouterThemeHelper.setVolumeSliderColor(this.mContext, (MediaRouteVolumeSlider) this.mVolumeSlider, this.mMediaMainControlLayout);
        HashMap hashMap = new HashMap();
        this.mVolumeSliderMap = hashMap;
        hashMap.put(this.mRoute, this.mVolumeSlider);
        MediaRouteExpandCollapseButton mediaRouteExpandCollapseButton = (MediaRouteExpandCollapseButton) findViewById(2131428433);
        this.mGroupExpandCollapseButton = mediaRouteExpandCollapseButton;
        mediaRouteExpandCollapseButton.setOnClickListener(new View.OnClickListener() { // from class: androidx.mediarouter.app.MediaRouteControllerDialog.5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                Interpolator interpolator2;
                MediaRouteControllerDialog mediaRouteControllerDialog = MediaRouteControllerDialog.this;
                boolean z = !mediaRouteControllerDialog.mIsGroupExpanded;
                mediaRouteControllerDialog.mIsGroupExpanded = z;
                if (z) {
                    mediaRouteControllerDialog.mVolumeGroupList.setVisibility(0);
                }
                MediaRouteControllerDialog mediaRouteControllerDialog2 = MediaRouteControllerDialog.this;
                Objects.requireNonNull(mediaRouteControllerDialog2);
                if (mediaRouteControllerDialog2.mIsGroupExpanded) {
                    interpolator2 = mediaRouteControllerDialog2.mLinearOutSlowInInterpolator;
                } else {
                    interpolator2 = mediaRouteControllerDialog2.mFastOutSlowInInterpolator;
                }
                mediaRouteControllerDialog2.mInterpolator = interpolator2;
                MediaRouteControllerDialog.this.updateLayoutHeight(true);
            }
        });
        if (this.mIsGroupExpanded) {
            interpolator = this.mLinearOutSlowInInterpolator;
        } else {
            interpolator = this.mFastOutSlowInInterpolator;
        }
        this.mInterpolator = interpolator;
        this.mGroupListAnimationDurationMs = this.mContext.getResources().getInteger(2131493002);
        this.mGroupListFadeInDurationMs = this.mContext.getResources().getInteger(2131493003);
        this.mGroupListFadeOutDurationMs = this.mContext.getResources().getInteger(2131493004);
        this.mCreated = true;
        updateLayout();
    }
}
