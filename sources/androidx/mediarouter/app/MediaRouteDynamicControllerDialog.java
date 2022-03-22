package androidx.mediarouter.app;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline1;
import androidx.mediarouter.media.MediaRouteProvider;
import androidx.mediarouter.media.MediaRouteSelector;
import androidx.mediarouter.media.MediaRouter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MediaRouteDynamicControllerDialog extends AppCompatDialog {
    public static final boolean DEBUG = Log.isLoggable("MediaRouteCtrlDialog", 3);
    public RecyclerAdapter mAdapter;
    public int mArtIconBackgroundColor;
    public Bitmap mArtIconBitmap;
    public boolean mArtIconIsLoaded;
    public Bitmap mArtIconLoadedBitmap;
    public Uri mArtIconUri;
    public ImageView mArtView;
    public boolean mAttachedToWindow;
    public final MediaRouterCallback mCallback;
    public ImageButton mCloseButton;
    public Context mContext;
    public MediaControllerCallback mControllerCallback;
    public boolean mCreated;
    public MediaDescriptionCompat mDescription;
    public final boolean mEnableGroupVolumeUX;
    public FetchArtTask mFetchArtTask;
    public boolean mIsAnimatingVolumeSliderLayout;
    public long mLastUpdateTime;
    public MediaControllerCompat mMediaController;
    public ImageView mMetadataBackground;
    public View mMetadataBlackScrim;
    public RecyclerView mRecyclerView;
    public MediaRouter.RouteInfo mRouteForVolumeUpdatingByUser;
    public final MediaRouter mRouter;
    public MediaRouter.RouteInfo mSelectedRoute;
    public Button mStopCastingButton;
    public TextView mSubtitleView;
    public String mTitlePlaceholder;
    public TextView mTitleView;
    public HashMap mUnmutedVolumeMap;
    public boolean mUpdateMetadataViewsDeferred;
    public boolean mUpdateRoutesViewDeferred;
    public VolumeChangeListener mVolumeChangeListener;
    public HashMap mVolumeSliderHolderMap;
    public MediaRouteSelector mSelector = MediaRouteSelector.EMPTY;
    public final ArrayList mMemberRoutes = new ArrayList();
    public final ArrayList mGroupableRoutes = new ArrayList();
    public final ArrayList mTransferableRoutes = new ArrayList();
    public final ArrayList mUngroupableRoutes = new ArrayList();
    public final AnonymousClass1 mHandler = new Handler() { // from class: androidx.mediarouter.app.MediaRouteDynamicControllerDialog.1
        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                MediaRouteDynamicControllerDialog.this.updateRoutesView();
            } else if (i == 2) {
                MediaRouteDynamicControllerDialog mediaRouteDynamicControllerDialog = MediaRouteDynamicControllerDialog.this;
                if (mediaRouteDynamicControllerDialog.mRouteForVolumeUpdatingByUser != null) {
                    mediaRouteDynamicControllerDialog.mRouteForVolumeUpdatingByUser = null;
                    mediaRouteDynamicControllerDialog.updateViewsIfNeeded();
                }
            }
        }
    };

    /* loaded from: classes.dex */
    public class FetchArtTask extends AsyncTask<Void, Void, Bitmap> {
        public int mBackgroundColor;
        public final Bitmap mIconBitmap;
        public final Uri mIconUri;

        public FetchArtTask() {
            Bitmap bitmap;
            boolean z;
            MediaDescriptionCompat mediaDescriptionCompat = MediaRouteDynamicControllerDialog.this.mDescription;
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
            MediaDescriptionCompat mediaDescriptionCompat2 = MediaRouteDynamicControllerDialog.this.mDescription;
            this.mIconUri = mediaDescriptionCompat2 != null ? mediaDescriptionCompat2.mIconUri : uri;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:47:0x00d3  */
        /* JADX WARN: Removed duplicated region for block: B:48:0x00e8  */
        /* JADX WARN: Type inference failed for: r4v0 */
        /* JADX WARN: Type inference failed for: r4v2, types: [java.io.InputStream] */
        /* JADX WARN: Type inference failed for: r4v3 */
        /* JADX WARN: Unknown variable types count: 1 */
        @Override // android.os.AsyncTask
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final android.graphics.Bitmap doInBackground(java.lang.Void[] r9) {
            /*
                Method dump skipped, instructions count: 289
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.mediarouter.app.MediaRouteDynamicControllerDialog.FetchArtTask.doInBackground(java.lang.Object[]):java.lang.Object");
        }

        @Override // android.os.AsyncTask
        public final void onPostExecute(Bitmap bitmap) {
            Bitmap bitmap2 = bitmap;
            MediaRouteDynamicControllerDialog mediaRouteDynamicControllerDialog = MediaRouteDynamicControllerDialog.this;
            mediaRouteDynamicControllerDialog.mFetchArtTask = null;
            if (!Objects.equals(mediaRouteDynamicControllerDialog.mArtIconBitmap, this.mIconBitmap) || !Objects.equals(MediaRouteDynamicControllerDialog.this.mArtIconUri, this.mIconUri)) {
                MediaRouteDynamicControllerDialog mediaRouteDynamicControllerDialog2 = MediaRouteDynamicControllerDialog.this;
                mediaRouteDynamicControllerDialog2.mArtIconBitmap = this.mIconBitmap;
                mediaRouteDynamicControllerDialog2.mArtIconLoadedBitmap = bitmap2;
                mediaRouteDynamicControllerDialog2.mArtIconUri = this.mIconUri;
                mediaRouteDynamicControllerDialog2.mArtIconBackgroundColor = this.mBackgroundColor;
                mediaRouteDynamicControllerDialog2.mArtIconIsLoaded = true;
                mediaRouteDynamicControllerDialog2.updateMetadataViews();
            }
        }

        @Override // android.os.AsyncTask
        public final void onPreExecute() {
            MediaRouteDynamicControllerDialog mediaRouteDynamicControllerDialog = MediaRouteDynamicControllerDialog.this;
            Objects.requireNonNull(mediaRouteDynamicControllerDialog);
            mediaRouteDynamicControllerDialog.mArtIconIsLoaded = false;
            mediaRouteDynamicControllerDialog.mArtIconLoadedBitmap = null;
            mediaRouteDynamicControllerDialog.mArtIconBackgroundColor = 0;
        }

        public final BufferedInputStream openInputStreamByScheme(Uri uri) throws IOException {
            InputStream inputStream;
            String lowerCase = uri.getScheme().toLowerCase();
            if ("android.resource".equals(lowerCase) || "content".equals(lowerCase) || "file".equals(lowerCase)) {
                inputStream = MediaRouteDynamicControllerDialog.this.mContext.getContentResolver().openInputStream(uri);
            } else {
                URLConnection openConnection = new URL(uri.toString()).openConnection();
                openConnection.setConnectTimeout(30000);
                openConnection.setReadTimeout(30000);
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
            MediaRouteDynamicControllerDialog mediaRouteDynamicControllerDialog = MediaRouteDynamicControllerDialog.this;
            if (mediaMetadataCompat == null) {
                mediaDescriptionCompat = null;
            } else {
                mediaDescriptionCompat = mediaMetadataCompat.getDescription();
            }
            mediaRouteDynamicControllerDialog.mDescription = mediaDescriptionCompat;
            MediaRouteDynamicControllerDialog.this.reloadIconIfNeeded();
            MediaRouteDynamicControllerDialog.this.updateMetadataViews();
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.Callback
        public final void onSessionDestroyed() {
            MediaRouteDynamicControllerDialog mediaRouteDynamicControllerDialog = MediaRouteDynamicControllerDialog.this;
            MediaControllerCompat mediaControllerCompat = mediaRouteDynamicControllerDialog.mMediaController;
            if (mediaControllerCompat != null) {
                mediaControllerCompat.unregisterCallback(mediaRouteDynamicControllerDialog.mControllerCallback);
                MediaRouteDynamicControllerDialog.this.mMediaController = null;
            }
        }
    }

    /* loaded from: classes.dex */
    public abstract class MediaRouteVolumeSliderHolder extends RecyclerView.ViewHolder {
        public final ImageButton mMuteButton;
        public MediaRouter.RouteInfo mRoute;
        public final MediaRouteVolumeSlider mVolumeSlider;

        public MediaRouteVolumeSliderHolder(View view, ImageButton imageButton, MediaRouteVolumeSlider mediaRouteVolumeSlider) {
            super(view);
            int i;
            int i2;
            this.mMuteButton = imageButton;
            this.mVolumeSlider = mediaRouteVolumeSlider;
            Context context = MediaRouteDynamicControllerDialog.this.mContext;
            Drawable drawable = AppCompatResources.getDrawable(context, 2131232482);
            if (MediaRouterThemeHelper.isLightTheme(context)) {
                Object obj = ContextCompat.sLock;
                drawable.setTint(context.getColor(2131100410));
            }
            imageButton.setImageDrawable(drawable);
            Context context2 = MediaRouteDynamicControllerDialog.this.mContext;
            if (MediaRouterThemeHelper.isLightTheme(context2)) {
                Object obj2 = ContextCompat.sLock;
                i = context2.getColor(2131100402);
                i2 = context2.getColor(2131100400);
            } else {
                Object obj3 = ContextCompat.sLock;
                i = context2.getColor(2131100401);
                i2 = context2.getColor(2131100399);
            }
            mediaRouteVolumeSlider.setColor(i, i2);
        }

        public final void bindRouteVolumeSliderHolder(MediaRouter.RouteInfo routeInfo) {
            boolean z;
            this.mRoute = routeInfo;
            Objects.requireNonNull(routeInfo);
            int i = routeInfo.mVolume;
            if (i == 0) {
                z = true;
            } else {
                z = false;
            }
            this.mMuteButton.setActivated(z);
            this.mMuteButton.setOnClickListener(new View.OnClickListener() { // from class: androidx.mediarouter.app.MediaRouteDynamicControllerDialog.MediaRouteVolumeSliderHolder.1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    MediaRouteDynamicControllerDialog mediaRouteDynamicControllerDialog = MediaRouteDynamicControllerDialog.this;
                    if (mediaRouteDynamicControllerDialog.mRouteForVolumeUpdatingByUser != null) {
                        mediaRouteDynamicControllerDialog.mHandler.removeMessages(2);
                    }
                    MediaRouteVolumeSliderHolder mediaRouteVolumeSliderHolder = MediaRouteVolumeSliderHolder.this;
                    MediaRouteDynamicControllerDialog.this.mRouteForVolumeUpdatingByUser = mediaRouteVolumeSliderHolder.mRoute;
                    int i2 = 1;
                    boolean z2 = !view.isActivated();
                    if (z2) {
                        i2 = 0;
                    } else {
                        MediaRouteVolumeSliderHolder mediaRouteVolumeSliderHolder2 = MediaRouteVolumeSliderHolder.this;
                        Objects.requireNonNull(mediaRouteVolumeSliderHolder2);
                        HashMap hashMap = MediaRouteDynamicControllerDialog.this.mUnmutedVolumeMap;
                        MediaRouter.RouteInfo routeInfo2 = mediaRouteVolumeSliderHolder2.mRoute;
                        Objects.requireNonNull(routeInfo2);
                        Integer num = (Integer) hashMap.get(routeInfo2.mUniqueId);
                        if (num != null) {
                            i2 = Math.max(1, num.intValue());
                        }
                    }
                    MediaRouteVolumeSliderHolder.this.setMute(z2);
                    MediaRouteVolumeSliderHolder.this.mVolumeSlider.setProgress(i2);
                    MediaRouteVolumeSliderHolder.this.mRoute.requestSetVolume(i2);
                    sendEmptyMessageDelayed(2, 500L);
                }
            });
            this.mVolumeSlider.setTag(this.mRoute);
            MediaRouteVolumeSlider mediaRouteVolumeSlider = this.mVolumeSlider;
            Objects.requireNonNull(routeInfo);
            mediaRouteVolumeSlider.setMax(routeInfo.mVolumeMax);
            this.mVolumeSlider.setProgress(i);
            this.mVolumeSlider.setOnSeekBarChangeListener(MediaRouteDynamicControllerDialog.this.mVolumeChangeListener);
        }

        public final void setMute(boolean z) {
            if (this.mMuteButton.isActivated() != z) {
                this.mMuteButton.setActivated(z);
                if (z) {
                    HashMap hashMap = MediaRouteDynamicControllerDialog.this.mUnmutedVolumeMap;
                    MediaRouter.RouteInfo routeInfo = this.mRoute;
                    Objects.requireNonNull(routeInfo);
                    hashMap.put(routeInfo.mUniqueId, Integer.valueOf(this.mVolumeSlider.getProgress()));
                    return;
                }
                HashMap hashMap2 = MediaRouteDynamicControllerDialog.this.mUnmutedVolumeMap;
                MediaRouter.RouteInfo routeInfo2 = this.mRoute;
                Objects.requireNonNull(routeInfo2);
                hashMap2.remove(routeInfo2.mUniqueId);
            }
        }
    }

    /* loaded from: classes.dex */
    public final class MediaRouterCallback extends MediaRouter.Callback {
        public MediaRouterCallback() {
        }

        @Override // androidx.mediarouter.media.MediaRouter.Callback
        public final void onRouteAdded() {
            MediaRouteDynamicControllerDialog.this.updateRoutesView();
        }

        @Override // androidx.mediarouter.media.MediaRouter.Callback
        public final void onRouteChanged(MediaRouter.RouteInfo routeInfo) {
            boolean z;
            MediaRouter.RouteInfo.DynamicGroupState dynamicGroupState;
            if (routeInfo == MediaRouteDynamicControllerDialog.this.mSelectedRoute && MediaRouter.RouteInfo.getDynamicGroupController() != null) {
                MediaRouter.ProviderInfo providerInfo = routeInfo.mProvider;
                Objects.requireNonNull(providerInfo);
                MediaRouter.checkCallingThread();
                for (MediaRouter.RouteInfo routeInfo2 : Collections.unmodifiableList(providerInfo.mRoutes)) {
                    if (!MediaRouteDynamicControllerDialog.this.mSelectedRoute.getMemberRoutes().contains(routeInfo2) && (dynamicGroupState = MediaRouteDynamicControllerDialog.this.mSelectedRoute.getDynamicGroupState(routeInfo2)) != null && dynamicGroupState.isGroupable() && !MediaRouteDynamicControllerDialog.this.mGroupableRoutes.contains(routeInfo2)) {
                        z = true;
                        break;
                    }
                }
            }
            z = false;
            if (z) {
                MediaRouteDynamicControllerDialog.this.updateViewsIfNeeded();
                MediaRouteDynamicControllerDialog.this.updateRoutes();
                return;
            }
            MediaRouteDynamicControllerDialog.this.updateRoutesView();
        }

        @Override // androidx.mediarouter.media.MediaRouter.Callback
        public final void onRouteRemoved() {
            MediaRouteDynamicControllerDialog.this.updateRoutesView();
        }

        @Override // androidx.mediarouter.media.MediaRouter.Callback
        public final void onRouteSelected(MediaRouter.RouteInfo routeInfo) {
            MediaRouteDynamicControllerDialog mediaRouteDynamicControllerDialog = MediaRouteDynamicControllerDialog.this;
            mediaRouteDynamicControllerDialog.mSelectedRoute = routeInfo;
            mediaRouteDynamicControllerDialog.updateViewsIfNeeded();
            MediaRouteDynamicControllerDialog.this.updateRoutes();
        }

        @Override // androidx.mediarouter.media.MediaRouter.Callback
        public final void onRouteUnselected() {
            MediaRouteDynamicControllerDialog.this.updateRoutesView();
        }

        @Override // androidx.mediarouter.media.MediaRouter.Callback
        public final void onRouteVolumeChanged(MediaRouter.RouteInfo routeInfo) {
            MediaRouteVolumeSliderHolder mediaRouteVolumeSliderHolder;
            boolean z;
            int i = routeInfo.mVolume;
            if (MediaRouteDynamicControllerDialog.DEBUG) {
                ExifInterface$$ExternalSyntheticOutline1.m("onRouteVolumeChanged(), route.getVolume:", i, "MediaRouteCtrlDialog");
            }
            MediaRouteDynamicControllerDialog mediaRouteDynamicControllerDialog = MediaRouteDynamicControllerDialog.this;
            if (mediaRouteDynamicControllerDialog.mRouteForVolumeUpdatingByUser != routeInfo && (mediaRouteVolumeSliderHolder = (MediaRouteVolumeSliderHolder) mediaRouteDynamicControllerDialog.mVolumeSliderHolderMap.get(routeInfo.mUniqueId)) != null) {
                MediaRouter.RouteInfo routeInfo2 = mediaRouteVolumeSliderHolder.mRoute;
                Objects.requireNonNull(routeInfo2);
                int i2 = routeInfo2.mVolume;
                if (i2 == 0) {
                    z = true;
                } else {
                    z = false;
                }
                mediaRouteVolumeSliderHolder.setMute(z);
                mediaRouteVolumeSliderHolder.mVolumeSlider.setProgress(i2);
            }
        }
    }

    /* loaded from: classes.dex */
    public final class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public final Drawable mDefaultIcon;
        public Item mGroupVolumeItem;
        public final LayoutInflater mInflater;
        public final int mLayoutAnimationDurationMs;
        public final Drawable mSpeakerGroupIcon;
        public final Drawable mSpeakerIcon;
        public final Drawable mTvIcon;
        public final ArrayList<Item> mItems = new ArrayList<>();
        public final AccelerateDecelerateInterpolator mAccelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();

        /* loaded from: classes.dex */
        public class GroupViewHolder extends RecyclerView.ViewHolder {
            public final float mDisabledAlpha;
            public final ImageView mImageView;
            public final View mItemView;
            public final ProgressBar mProgressBar;
            public MediaRouter.RouteInfo mRoute;
            public final TextView mTextView;

            public GroupViewHolder(View view) {
                super(view);
                this.mItemView = view;
                this.mImageView = (ImageView) view.findViewById(2131428400);
                ProgressBar progressBar = (ProgressBar) view.findViewById(2131428402);
                this.mProgressBar = progressBar;
                this.mTextView = (TextView) view.findViewById(2131428401);
                this.mDisabledAlpha = MediaRouterThemeHelper.getDisabledAlpha(MediaRouteDynamicControllerDialog.this.mContext);
                MediaRouterThemeHelper.setIndeterminateProgressBarColor(MediaRouteDynamicControllerDialog.this.mContext, progressBar);
            }
        }

        /* loaded from: classes.dex */
        public class GroupVolumeViewHolder extends MediaRouteVolumeSliderHolder {
            public final int mExpandedHeight;
            public final TextView mTextView;

            public GroupVolumeViewHolder(View view) {
                super(view, (ImageButton) view.findViewById(2131428410), (MediaRouteVolumeSlider) view.findViewById(2131428416));
                this.mTextView = (TextView) view.findViewById(2131428434);
                Resources resources = MediaRouteDynamicControllerDialog.this.mContext.getResources();
                DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                TypedValue typedValue = new TypedValue();
                resources.getValue(2131166384, typedValue, true);
                this.mExpandedHeight = (int) typedValue.getDimension(displayMetrics);
            }
        }

        /* loaded from: classes.dex */
        public class RouteViewHolder extends MediaRouteVolumeSliderHolder {
            public final CheckBox mCheckBox;
            public final float mDisabledAlpha;
            public final int mExpandedLayoutHeight;
            public final ImageView mImageView;
            public final View mItemView;
            public final ProgressBar mProgressBar;
            public final TextView mTextView;
            public final AnonymousClass1 mViewClickListener = new View.OnClickListener() { // from class: androidx.mediarouter.app.MediaRouteDynamicControllerDialog.RecyclerAdapter.RouteViewHolder.1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    boolean z;
                    int i;
                    boolean z2;
                    RouteViewHolder routeViewHolder = RouteViewHolder.this;
                    boolean z3 = true;
                    boolean z4 = !routeViewHolder.isSelected(routeViewHolder.mRoute);
                    boolean isGroup = RouteViewHolder.this.mRoute.isGroup();
                    int i2 = 0;
                    if (z4) {
                        RouteViewHolder routeViewHolder2 = RouteViewHolder.this;
                        MediaRouter mediaRouter = MediaRouteDynamicControllerDialog.this.mRouter;
                        MediaRouter.RouteInfo routeInfo = routeViewHolder2.mRoute;
                        Objects.requireNonNull(mediaRouter);
                        Objects.requireNonNull(routeInfo, "route must not be null");
                        MediaRouter.checkCallingThread();
                        MediaRouter.GlobalMediaRouter globalRouter = MediaRouter.getGlobalRouter();
                        Objects.requireNonNull(globalRouter);
                        if (globalRouter.mSelectedRouteController instanceof MediaRouteProvider.DynamicGroupRouteController) {
                            MediaRouter.RouteInfo.DynamicGroupState dynamicGroupState = globalRouter.mSelectedRoute.getDynamicGroupState(routeInfo);
                            if (globalRouter.mSelectedRoute.getMemberRoutes().contains(routeInfo) || dynamicGroupState == null || !dynamicGroupState.isGroupable()) {
                                Log.w("MediaRouter", "Ignoring attempt to add a non-groupable route to dynamic group : " + routeInfo);
                            } else {
                                ((MediaRouteProvider.DynamicGroupRouteController) globalRouter.mSelectedRouteController).onAddMemberRoute(routeInfo.mDescriptorId);
                            }
                        } else {
                            throw new IllegalStateException("There is no currently selected dynamic group route.");
                        }
                    } else {
                        RouteViewHolder routeViewHolder3 = RouteViewHolder.this;
                        MediaRouter mediaRouter2 = MediaRouteDynamicControllerDialog.this.mRouter;
                        MediaRouter.RouteInfo routeInfo2 = routeViewHolder3.mRoute;
                        Objects.requireNonNull(mediaRouter2);
                        Objects.requireNonNull(routeInfo2, "route must not be null");
                        MediaRouter.checkCallingThread();
                        MediaRouter.GlobalMediaRouter globalRouter2 = MediaRouter.getGlobalRouter();
                        Objects.requireNonNull(globalRouter2);
                        if (globalRouter2.mSelectedRouteController instanceof MediaRouteProvider.DynamicGroupRouteController) {
                            MediaRouter.RouteInfo.DynamicGroupState dynamicGroupState2 = globalRouter2.mSelectedRoute.getDynamicGroupState(routeInfo2);
                            if (globalRouter2.mSelectedRoute.getMemberRoutes().contains(routeInfo2) && dynamicGroupState2 != null) {
                                MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor dynamicRouteDescriptor = dynamicGroupState2.mDynamicDescriptor;
                                if (dynamicRouteDescriptor == null || dynamicRouteDescriptor.mIsUnselectable) {
                                    z2 = true;
                                } else {
                                    z2 = false;
                                }
                                if (z2) {
                                    if (globalRouter2.mSelectedRoute.getMemberRoutes().size() <= 1) {
                                        Log.w("MediaRouter", "Ignoring attempt to remove the last member route.");
                                    } else {
                                        ((MediaRouteProvider.DynamicGroupRouteController) globalRouter2.mSelectedRouteController).onRemoveMemberRoute(routeInfo2.mDescriptorId);
                                    }
                                }
                            }
                            Log.w("MediaRouter", "Ignoring attempt to remove a non-unselectable member route : " + routeInfo2);
                        } else {
                            throw new IllegalStateException("There is no currently selected dynamic group route.");
                        }
                    }
                    RouteViewHolder.this.showSelectingProgress(z4, !isGroup);
                    if (isGroup) {
                        List<MediaRouter.RouteInfo> memberRoutes = MediaRouteDynamicControllerDialog.this.mSelectedRoute.getMemberRoutes();
                        for (MediaRouter.RouteInfo routeInfo3 : RouteViewHolder.this.mRoute.getMemberRoutes()) {
                            if (memberRoutes.contains(routeInfo3) != z4) {
                                HashMap hashMap = MediaRouteDynamicControllerDialog.this.mVolumeSliderHolderMap;
                                Objects.requireNonNull(routeInfo3);
                                MediaRouteVolumeSliderHolder mediaRouteVolumeSliderHolder = (MediaRouteVolumeSliderHolder) hashMap.get(routeInfo3.mUniqueId);
                                if (mediaRouteVolumeSliderHolder instanceof RouteViewHolder) {
                                    ((RouteViewHolder) mediaRouteVolumeSliderHolder).showSelectingProgress(z4, true);
                                }
                            }
                        }
                    }
                    RouteViewHolder routeViewHolder4 = RouteViewHolder.this;
                    RecyclerAdapter recyclerAdapter = RecyclerAdapter.this;
                    MediaRouter.RouteInfo routeInfo4 = routeViewHolder4.mRoute;
                    Objects.requireNonNull(recyclerAdapter);
                    List<MediaRouter.RouteInfo> memberRoutes2 = MediaRouteDynamicControllerDialog.this.mSelectedRoute.getMemberRoutes();
                    int max = Math.max(1, memberRoutes2.size());
                    int i3 = -1;
                    if (routeInfo4.isGroup()) {
                        for (MediaRouter.RouteInfo routeInfo5 : routeInfo4.getMemberRoutes()) {
                            if (memberRoutes2.contains(routeInfo5) != z4) {
                                if (z4) {
                                    i = 1;
                                } else {
                                    i = -1;
                                }
                                max += i;
                            }
                        }
                    } else {
                        if (z4) {
                            i3 = 1;
                        }
                        max += i3;
                    }
                    MediaRouteDynamicControllerDialog mediaRouteDynamicControllerDialog = MediaRouteDynamicControllerDialog.this;
                    if (!mediaRouteDynamicControllerDialog.mEnableGroupVolumeUX || mediaRouteDynamicControllerDialog.mSelectedRoute.getMemberRoutes().size() <= 1) {
                        z = false;
                    } else {
                        z = true;
                    }
                    MediaRouteDynamicControllerDialog mediaRouteDynamicControllerDialog2 = MediaRouteDynamicControllerDialog.this;
                    if (!mediaRouteDynamicControllerDialog2.mEnableGroupVolumeUX || max < 2) {
                        z3 = false;
                    }
                    if (z != z3) {
                        RecyclerView.ViewHolder findViewHolderForAdapterPosition = mediaRouteDynamicControllerDialog2.mRecyclerView.findViewHolderForAdapterPosition(0);
                        if (findViewHolderForAdapterPosition instanceof GroupVolumeViewHolder) {
                            GroupVolumeViewHolder groupVolumeViewHolder = (GroupVolumeViewHolder) findViewHolderForAdapterPosition;
                            View view2 = groupVolumeViewHolder.itemView;
                            if (z3) {
                                i2 = groupVolumeViewHolder.mExpandedHeight;
                            }
                            recyclerAdapter.animateLayoutHeight(view2, i2);
                        }
                    }
                }
            };
            public final RelativeLayout mVolumeSliderLayout;

            /* JADX WARN: Type inference failed for: r0v1, types: [androidx.mediarouter.app.MediaRouteDynamicControllerDialog$RecyclerAdapter$RouteViewHolder$1] */
            public RouteViewHolder(View view) {
                super(view, (ImageButton) view.findViewById(2131428410), (MediaRouteVolumeSlider) view.findViewById(2131428416));
                this.mItemView = view;
                this.mImageView = (ImageView) view.findViewById(2131428411);
                ProgressBar progressBar = (ProgressBar) view.findViewById(2131428413);
                this.mProgressBar = progressBar;
                this.mTextView = (TextView) view.findViewById(2131428412);
                this.mVolumeSliderLayout = (RelativeLayout) view.findViewById(2131428415);
                CheckBox checkBox = (CheckBox) view.findViewById(2131428397);
                this.mCheckBox = checkBox;
                Context context = MediaRouteDynamicControllerDialog.this.mContext;
                Drawable drawable = AppCompatResources.getDrawable(context, 2131232480);
                if (MediaRouterThemeHelper.isLightTheme(context)) {
                    Object obj = ContextCompat.sLock;
                    drawable.setTint(context.getColor(2131100410));
                }
                checkBox.setButtonDrawable(drawable);
                MediaRouterThemeHelper.setIndeterminateProgressBarColor(MediaRouteDynamicControllerDialog.this.mContext, progressBar);
                this.mDisabledAlpha = MediaRouterThemeHelper.getDisabledAlpha(MediaRouteDynamicControllerDialog.this.mContext);
                Resources resources = MediaRouteDynamicControllerDialog.this.mContext.getResources();
                DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                TypedValue typedValue = new TypedValue();
                resources.getValue(2131166383, typedValue, true);
                this.mExpandedLayoutHeight = (int) typedValue.getDimension(displayMetrics);
            }

            public final void showSelectingProgress(boolean z, boolean z2) {
                int i = 0;
                this.mCheckBox.setEnabled(false);
                this.mItemView.setEnabled(false);
                this.mCheckBox.setChecked(z);
                if (z) {
                    this.mImageView.setVisibility(4);
                    this.mProgressBar.setVisibility(0);
                }
                if (z2) {
                    RecyclerAdapter recyclerAdapter = RecyclerAdapter.this;
                    RelativeLayout relativeLayout = this.mVolumeSliderLayout;
                    if (z) {
                        i = this.mExpandedLayoutHeight;
                    }
                    recyclerAdapter.animateLayoutHeight(relativeLayout, i);
                }
            }

            public final boolean isSelected(MediaRouter.RouteInfo routeInfo) {
                int i;
                if (routeInfo.isSelected()) {
                    return true;
                }
                MediaRouter.RouteInfo.DynamicGroupState dynamicGroupState = MediaRouteDynamicControllerDialog.this.mSelectedRoute.getDynamicGroupState(routeInfo);
                if (dynamicGroupState != null) {
                    MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor dynamicRouteDescriptor = dynamicGroupState.mDynamicDescriptor;
                    if (dynamicRouteDescriptor != null) {
                        i = dynamicRouteDescriptor.mSelectionState;
                    } else {
                        i = 1;
                    }
                    if (i == 3) {
                        return true;
                    }
                }
                return false;
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public final RecyclerView.ViewHolder onCreateViewHolder(RecyclerView recyclerView, int i) {
            if (i == 1) {
                return new GroupVolumeViewHolder(this.mInflater.inflate(2131624273, (ViewGroup) recyclerView, false));
            }
            if (i == 2) {
                return new HeaderViewHolder(this.mInflater.inflate(2131624274, (ViewGroup) recyclerView, false));
            }
            if (i == 3) {
                return new RouteViewHolder(this.mInflater.inflate(2131624276, (ViewGroup) recyclerView, false));
            }
            if (i == 4) {
                return new GroupViewHolder(this.mInflater.inflate(2131624272, (ViewGroup) recyclerView, false));
            }
            Log.w("MediaRouteCtrlDialog", "Cannot create ViewHolder because of wrong view type");
            return null;
        }

        /* loaded from: classes.dex */
        public class HeaderViewHolder extends RecyclerView.ViewHolder {
            public final TextView mTextView;

            public HeaderViewHolder(View view) {
                super(view);
                this.mTextView = (TextView) view.findViewById(2131428403);
            }
        }

        /* loaded from: classes.dex */
        public class Item {
            public final Object mData;
            public final int mType;

            public Item(Object obj, int i) {
                this.mData = obj;
                this.mType = i;
            }
        }

        public RecyclerAdapter() {
            this.mInflater = LayoutInflater.from(MediaRouteDynamicControllerDialog.this.mContext);
            this.mDefaultIcon = MediaRouterThemeHelper.getIconByAttrId(MediaRouteDynamicControllerDialog.this.mContext, 2130969461);
            this.mTvIcon = MediaRouterThemeHelper.getIconByAttrId(MediaRouteDynamicControllerDialog.this.mContext, 2130969470);
            this.mSpeakerIcon = MediaRouterThemeHelper.getIconByAttrId(MediaRouteDynamicControllerDialog.this.mContext, 2130969467);
            this.mSpeakerGroupIcon = MediaRouterThemeHelper.getIconByAttrId(MediaRouteDynamicControllerDialog.this.mContext, 2130969466);
            this.mLayoutAnimationDurationMs = MediaRouteDynamicControllerDialog.this.mContext.getResources().getInteger(2131493001);
            updateItems();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public final int getItemCount() {
            return this.mItems.size() + 1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public final int getItemViewType(int i) {
            Item item;
            if (i == 0) {
                item = this.mGroupVolumeItem;
            } else {
                item = this.mItems.get(i - 1);
            }
            Objects.requireNonNull(item);
            return item.mType;
        }

        public final void notifyAdapterDataSetChanged() {
            MediaRouteDynamicControllerDialog.this.mUngroupableRoutes.clear();
            MediaRouteDynamicControllerDialog mediaRouteDynamicControllerDialog = MediaRouteDynamicControllerDialog.this;
            ArrayList arrayList = mediaRouteDynamicControllerDialog.mUngroupableRoutes;
            ArrayList arrayList2 = mediaRouteDynamicControllerDialog.mGroupableRoutes;
            ArrayList arrayList3 = new ArrayList();
            MediaRouter.RouteInfo routeInfo = mediaRouteDynamicControllerDialog.mSelectedRoute;
            Objects.requireNonNull(routeInfo);
            MediaRouter.ProviderInfo providerInfo = routeInfo.mProvider;
            Objects.requireNonNull(providerInfo);
            MediaRouter.checkCallingThread();
            for (MediaRouter.RouteInfo routeInfo2 : Collections.unmodifiableList(providerInfo.mRoutes)) {
                MediaRouter.RouteInfo.DynamicGroupState dynamicGroupState = mediaRouteDynamicControllerDialog.mSelectedRoute.getDynamicGroupState(routeInfo2);
                if (dynamicGroupState != null && dynamicGroupState.isGroupable()) {
                    arrayList3.add(routeInfo2);
                }
            }
            HashSet hashSet = new HashSet(arrayList2);
            hashSet.removeAll(arrayList3);
            arrayList.addAll(hashSet);
            notifyDataSetChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public final void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
            MediaRouteDynamicControllerDialog.this.mVolumeSliderHolderMap.values().remove(viewHolder);
        }

        public final void updateItems() {
            String str;
            String str2;
            this.mItems.clear();
            MediaRouteDynamicControllerDialog mediaRouteDynamicControllerDialog = MediaRouteDynamicControllerDialog.this;
            this.mGroupVolumeItem = new Item(mediaRouteDynamicControllerDialog.mSelectedRoute, 1);
            if (!mediaRouteDynamicControllerDialog.mMemberRoutes.isEmpty()) {
                Iterator it = MediaRouteDynamicControllerDialog.this.mMemberRoutes.iterator();
                while (it.hasNext()) {
                    this.mItems.add(new Item((MediaRouter.RouteInfo) it.next(), 3));
                }
            } else {
                this.mItems.add(new Item(MediaRouteDynamicControllerDialog.this.mSelectedRoute, 3));
            }
            boolean z = false;
            if (!MediaRouteDynamicControllerDialog.this.mGroupableRoutes.isEmpty()) {
                Iterator it2 = MediaRouteDynamicControllerDialog.this.mGroupableRoutes.iterator();
                boolean z2 = false;
                while (it2.hasNext()) {
                    MediaRouter.RouteInfo routeInfo = (MediaRouter.RouteInfo) it2.next();
                    if (!MediaRouteDynamicControllerDialog.this.mMemberRoutes.contains(routeInfo)) {
                        if (!z2) {
                            Objects.requireNonNull(MediaRouteDynamicControllerDialog.this.mSelectedRoute);
                            MediaRouteProvider.DynamicGroupRouteController dynamicGroupController = MediaRouter.RouteInfo.getDynamicGroupController();
                            if (dynamicGroupController != null) {
                                str2 = dynamicGroupController.getGroupableSelectionTitle();
                            } else {
                                str2 = null;
                            }
                            if (TextUtils.isEmpty(str2)) {
                                str2 = MediaRouteDynamicControllerDialog.this.mContext.getString(2131952821);
                            }
                            this.mItems.add(new Item(str2, 2));
                            z2 = true;
                        }
                        this.mItems.add(new Item(routeInfo, 3));
                    }
                }
            }
            if (!MediaRouteDynamicControllerDialog.this.mTransferableRoutes.isEmpty()) {
                Iterator it3 = MediaRouteDynamicControllerDialog.this.mTransferableRoutes.iterator();
                while (it3.hasNext()) {
                    MediaRouter.RouteInfo routeInfo2 = (MediaRouter.RouteInfo) it3.next();
                    MediaRouter.RouteInfo routeInfo3 = MediaRouteDynamicControllerDialog.this.mSelectedRoute;
                    if (routeInfo3 != routeInfo2) {
                        if (!z) {
                            Objects.requireNonNull(routeInfo3);
                            MediaRouteProvider.DynamicGroupRouteController dynamicGroupController2 = MediaRouter.RouteInfo.getDynamicGroupController();
                            if (dynamicGroupController2 != null) {
                                str = dynamicGroupController2.getTransferableSectionTitle();
                            } else {
                                str = null;
                            }
                            if (TextUtils.isEmpty(str)) {
                                str = MediaRouteDynamicControllerDialog.this.mContext.getString(2131952822);
                            }
                            this.mItems.add(new Item(str, 2));
                            z = true;
                        }
                        this.mItems.add(new Item(routeInfo2, 4));
                    }
                }
            }
            notifyAdapterDataSetChanged();
        }

        public final void animateLayoutHeight(final View view, final int i) {
            final int i2 = view.getLayoutParams().height;
            Animation animation = new Animation() { // from class: androidx.mediarouter.app.MediaRouteDynamicControllerDialog.RecyclerAdapter.1
                @Override // android.view.animation.Animation
                public final void applyTransformation(float f, Transformation transformation) {
                    int i3 = i;
                    int i4 = i2;
                    View view2 = view;
                    int i5 = i4 + ((int) ((i3 - i4) * f));
                    boolean z = MediaRouteDynamicControllerDialog.DEBUG;
                    ViewGroup.LayoutParams layoutParams = view2.getLayoutParams();
                    layoutParams.height = i5;
                    view2.setLayoutParams(layoutParams);
                }
            };
            animation.setAnimationListener(new Animation.AnimationListener() { // from class: androidx.mediarouter.app.MediaRouteDynamicControllerDialog.RecyclerAdapter.2
                @Override // android.view.animation.Animation.AnimationListener
                public final void onAnimationRepeat(Animation animation2) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public final void onAnimationEnd(Animation animation2) {
                    MediaRouteDynamicControllerDialog mediaRouteDynamicControllerDialog = MediaRouteDynamicControllerDialog.this;
                    mediaRouteDynamicControllerDialog.mIsAnimatingVolumeSliderLayout = false;
                    mediaRouteDynamicControllerDialog.updateViewsIfNeeded();
                }

                @Override // android.view.animation.Animation.AnimationListener
                public final void onAnimationStart(Animation animation2) {
                    MediaRouteDynamicControllerDialog.this.mIsAnimatingVolumeSliderLayout = true;
                }
            });
            animation.setDuration(this.mLayoutAnimationDurationMs);
            animation.setInterpolator(this.mAccelerateDecelerateInterpolator);
            view.startAnimation(animation);
        }

        public final Drawable getIconDrawable(MediaRouter.RouteInfo routeInfo) {
            Objects.requireNonNull(routeInfo);
            Uri uri = routeInfo.mIconUri;
            if (uri != null) {
                try {
                    Drawable createFromStream = Drawable.createFromStream(MediaRouteDynamicControllerDialog.this.mContext.getContentResolver().openInputStream(uri), null);
                    if (createFromStream != null) {
                        return createFromStream;
                    }
                } catch (IOException e) {
                    Log.w("MediaRouteCtrlDialog", "Failed to load " + uri, e);
                }
            }
            int i = routeInfo.mDeviceType;
            if (i == 1) {
                return this.mTvIcon;
            }
            if (i == 2) {
                return this.mSpeakerIcon;
            }
            if (routeInfo.isGroup()) {
                return this.mSpeakerGroupIcon;
            }
            return this.mDefaultIcon;
        }

        /* JADX WARN: Code restructure failed: missing block: B:49:0x012e, code lost:
            if (r7 != false) goto L_0x0133;
         */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void onBindViewHolder(androidx.recyclerview.widget.RecyclerView.ViewHolder r8, int r9) {
            /*
                Method dump skipped, instructions count: 517
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.mediarouter.app.MediaRouteDynamicControllerDialog.RecyclerAdapter.onBindViewHolder(androidx.recyclerview.widget.RecyclerView$ViewHolder, int):void");
        }
    }

    /* loaded from: classes.dex */
    public static final class RouteComparator implements Comparator<MediaRouter.RouteInfo> {
        public static final RouteComparator sInstance = new RouteComparator();

        @Override // java.util.Comparator
        public final int compare(MediaRouter.RouteInfo routeInfo, MediaRouter.RouteInfo routeInfo2) {
            MediaRouter.RouteInfo routeInfo3 = routeInfo;
            MediaRouter.RouteInfo routeInfo4 = routeInfo2;
            Objects.requireNonNull(routeInfo3);
            String str = routeInfo3.mName;
            Objects.requireNonNull(routeInfo4);
            return str.compareToIgnoreCase(routeInfo4.mName);
        }
    }

    /* loaded from: classes.dex */
    public class VolumeChangeListener implements SeekBar.OnSeekBarChangeListener {
        public VolumeChangeListener() {
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public final void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            boolean z2;
            if (z) {
                MediaRouter.RouteInfo routeInfo = (MediaRouter.RouteInfo) seekBar.getTag();
                HashMap hashMap = MediaRouteDynamicControllerDialog.this.mVolumeSliderHolderMap;
                Objects.requireNonNull(routeInfo);
                MediaRouteVolumeSliderHolder mediaRouteVolumeSliderHolder = (MediaRouteVolumeSliderHolder) hashMap.get(routeInfo.mUniqueId);
                if (mediaRouteVolumeSliderHolder != null) {
                    if (i == 0) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    mediaRouteVolumeSliderHolder.setMute(z2);
                }
                routeInfo.requestSetVolume(i);
            }
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public final void onStartTrackingTouch(SeekBar seekBar) {
            MediaRouteDynamicControllerDialog mediaRouteDynamicControllerDialog = MediaRouteDynamicControllerDialog.this;
            if (mediaRouteDynamicControllerDialog.mRouteForVolumeUpdatingByUser != null) {
                mediaRouteDynamicControllerDialog.mHandler.removeMessages(2);
            }
            MediaRouteDynamicControllerDialog.this.mRouteForVolumeUpdatingByUser = (MediaRouter.RouteInfo) seekBar.getTag();
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public final void onStopTrackingTouch(SeekBar seekBar) {
            sendEmptyMessageDelayed(2, 500L);
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /* JADX WARN: Type inference failed for: r3v7, types: [androidx.mediarouter.app.MediaRouteDynamicControllerDialog$1] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public MediaRouteDynamicControllerDialog(android.content.Context r3) {
        /*
            r2 = this;
            r0 = 0
            android.view.ContextThemeWrapper r3 = androidx.mediarouter.app.MediaRouterThemeHelper.createThemedDialogContext(r3, r0)
            int r1 = androidx.mediarouter.app.MediaRouterThemeHelper.createThemedDialogStyle(r3)
            r2.<init>(r3, r1)
            androidx.mediarouter.media.MediaRouteSelector r3 = androidx.mediarouter.media.MediaRouteSelector.EMPTY
            r2.mSelector = r3
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            r2.mMemberRoutes = r3
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            r2.mGroupableRoutes = r3
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            r2.mTransferableRoutes = r3
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            r2.mUngroupableRoutes = r3
            androidx.mediarouter.app.MediaRouteDynamicControllerDialog$1 r3 = new androidx.mediarouter.app.MediaRouteDynamicControllerDialog$1
            r3.<init>()
            r2.mHandler = r3
            android.content.Context r3 = r2.getContext()
            r2.mContext = r3
            androidx.mediarouter.media.MediaRouter r3 = androidx.mediarouter.media.MediaRouter.getInstance(r3)
            r2.mRouter = r3
            androidx.mediarouter.media.MediaRouter$GlobalMediaRouter r3 = androidx.mediarouter.media.MediaRouter.sGlobal
            if (r3 != 0) goto L_0x0044
            goto L_0x004c
        L_0x0044:
            androidx.mediarouter.media.MediaRouter$GlobalMediaRouter r3 = androidx.mediarouter.media.MediaRouter.getGlobalRouter()
            java.util.Objects.requireNonNull(r3)
            r0 = 1
        L_0x004c:
            r2.mEnableGroupVolumeUX = r0
            androidx.mediarouter.app.MediaRouteDynamicControllerDialog$MediaRouterCallback r3 = new androidx.mediarouter.app.MediaRouteDynamicControllerDialog$MediaRouterCallback
            r3.<init>()
            r2.mCallback = r3
            androidx.mediarouter.media.MediaRouter$RouteInfo r3 = androidx.mediarouter.media.MediaRouter.getSelectedRoute()
            r2.mSelectedRoute = r3
            androidx.mediarouter.app.MediaRouteDynamicControllerDialog$MediaControllerCallback r3 = new androidx.mediarouter.app.MediaRouteDynamicControllerDialog$MediaControllerCallback
            r3.<init>()
            r2.mControllerCallback = r3
            r2.setMediaSession()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.mediarouter.app.MediaRouteDynamicControllerDialog.<init>(android.content.Context):void");
    }

    public final void reloadIconIfNeeded() {
        Bitmap bitmap;
        Bitmap bitmap2;
        Uri uri;
        MediaDescriptionCompat mediaDescriptionCompat = this.mDescription;
        Uri uri2 = null;
        if (mediaDescriptionCompat == null) {
            bitmap = null;
        } else {
            Objects.requireNonNull(mediaDescriptionCompat);
            bitmap = mediaDescriptionCompat.mIcon;
        }
        MediaDescriptionCompat mediaDescriptionCompat2 = this.mDescription;
        if (mediaDescriptionCompat2 != null) {
            Objects.requireNonNull(mediaDescriptionCompat2);
            uri2 = mediaDescriptionCompat2.mIconUri;
        }
        FetchArtTask fetchArtTask = this.mFetchArtTask;
        if (fetchArtTask == null) {
            bitmap2 = this.mArtIconBitmap;
        } else {
            Objects.requireNonNull(fetchArtTask);
            bitmap2 = fetchArtTask.mIconBitmap;
        }
        FetchArtTask fetchArtTask2 = this.mFetchArtTask;
        if (fetchArtTask2 == null) {
            uri = this.mArtIconUri;
        } else {
            Objects.requireNonNull(fetchArtTask2);
            uri = fetchArtTask2.mIconUri;
        }
        if (bitmap2 != bitmap || (bitmap2 == null && !Objects.equals(uri, uri2))) {
            FetchArtTask fetchArtTask3 = this.mFetchArtTask;
            if (fetchArtTask3 != null) {
                fetchArtTask3.cancel(true);
            }
            FetchArtTask fetchArtTask4 = new FetchArtTask();
            this.mFetchArtTask = fetchArtTask4;
            fetchArtTask4.execute(new Void[0]);
        }
    }

    public final void setMediaSession() {
        MediaControllerCompat mediaControllerCompat = this.mMediaController;
        if (mediaControllerCompat != null) {
            mediaControllerCompat.unregisterCallback(this.mControllerCallback);
            this.mMediaController = null;
        }
    }

    public final void setRouteSelector(MediaRouteSelector mediaRouteSelector) {
        if (mediaRouteSelector == null) {
            throw new IllegalArgumentException("selector must not be null");
        } else if (!this.mSelector.equals(mediaRouteSelector)) {
            this.mSelector = mediaRouteSelector;
            if (this.mAttachedToWindow) {
                this.mRouter.removeCallback(this.mCallback);
                this.mRouter.addCallback(mediaRouteSelector, this.mCallback, 1);
                updateRoutes();
            }
        }
    }

    public final void updateLayout() {
        int i;
        Context context = this.mContext;
        int i2 = -1;
        if (!context.getResources().getBoolean(2131034200)) {
            i = -1;
        } else {
            i = MediaRouteDialogHelper.getDialogWidth(context);
        }
        if (this.mContext.getResources().getBoolean(2131034200)) {
            i2 = -2;
        }
        getWindow().setLayout(i, i2);
        this.mArtIconBitmap = null;
        this.mArtIconUri = null;
        reloadIconIfNeeded();
        updateMetadataViews();
        updateRoutesView();
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x00dc  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00de  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00ea  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00f3  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00f9  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0102  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x010d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateMetadataViews() {
        /*
            Method dump skipped, instructions count: 275
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.mediarouter.app.MediaRouteDynamicControllerDialog.updateMetadataViews():void");
    }

    public final void updateRoutes() {
        boolean z;
        this.mMemberRoutes.clear();
        this.mGroupableRoutes.clear();
        this.mTransferableRoutes.clear();
        this.mMemberRoutes.addAll(this.mSelectedRoute.getMemberRoutes());
        MediaRouter.RouteInfo routeInfo = this.mSelectedRoute;
        Objects.requireNonNull(routeInfo);
        MediaRouter.ProviderInfo providerInfo = routeInfo.mProvider;
        Objects.requireNonNull(providerInfo);
        MediaRouter.checkCallingThread();
        for (MediaRouter.RouteInfo routeInfo2 : Collections.unmodifiableList(providerInfo.mRoutes)) {
            MediaRouter.RouteInfo.DynamicGroupState dynamicGroupState = this.mSelectedRoute.getDynamicGroupState(routeInfo2);
            if (dynamicGroupState != null) {
                if (dynamicGroupState.isGroupable()) {
                    this.mGroupableRoutes.add(routeInfo2);
                }
                MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor dynamicRouteDescriptor = dynamicGroupState.mDynamicDescriptor;
                if (dynamicRouteDescriptor == null || !dynamicRouteDescriptor.mIsTransferable) {
                    z = false;
                } else {
                    z = true;
                }
                if (z) {
                    this.mTransferableRoutes.add(routeInfo2);
                }
            }
        }
        onFilterRoutes(this.mGroupableRoutes);
        onFilterRoutes(this.mTransferableRoutes);
        ArrayList arrayList = this.mMemberRoutes;
        RouteComparator routeComparator = RouteComparator.sInstance;
        Collections.sort(arrayList, routeComparator);
        Collections.sort(this.mGroupableRoutes, routeComparator);
        Collections.sort(this.mTransferableRoutes, routeComparator);
        this.mAdapter.updateItems();
    }

    public final void updateRoutesView() {
        boolean z;
        if (!this.mAttachedToWindow) {
            return;
        }
        if (SystemClock.uptimeMillis() - this.mLastUpdateTime >= 300) {
            if (this.mRouteForVolumeUpdatingByUser != null || this.mIsAnimatingVolumeSliderLayout) {
                z = true;
            } else {
                z = !this.mCreated;
            }
            if (z) {
                this.mUpdateRoutesViewDeferred = true;
                return;
            }
            this.mUpdateRoutesViewDeferred = false;
            if (!this.mSelectedRoute.isSelected() || this.mSelectedRoute.isDefaultOrBluetooth()) {
                dismiss();
            }
            this.mLastUpdateTime = SystemClock.uptimeMillis();
            this.mAdapter.notifyAdapterDataSetChanged();
            return;
        }
        removeMessages(1);
        sendEmptyMessageAtTime(1, this.mLastUpdateTime + 300);
    }

    public final void updateViewsIfNeeded() {
        if (this.mUpdateRoutesViewDeferred) {
            updateRoutesView();
        }
        if (this.mUpdateMetadataViewsDeferred) {
            updateMetadataViews();
        }
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mAttachedToWindow = true;
        this.mRouter.addCallback(this.mSelector, this.mCallback, 1);
        updateRoutes();
        Objects.requireNonNull(this.mRouter);
        boolean z = MediaRouter.DEBUG;
        setMediaSession();
    }

    @Override // androidx.appcompat.app.AppCompatDialog, android.app.Dialog
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(2131624271);
        MediaRouterThemeHelper.setDialogBackgroundColor(this.mContext, this);
        ImageButton imageButton = (ImageButton) findViewById(2131428398);
        this.mCloseButton = imageButton;
        imageButton.setColorFilter(-1);
        this.mCloseButton.setOnClickListener(new View.OnClickListener() { // from class: androidx.mediarouter.app.MediaRouteDynamicControllerDialog.2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MediaRouteDynamicControllerDialog.this.dismiss();
            }
        });
        Button button = (Button) findViewById(2131428414);
        this.mStopCastingButton = button;
        button.setTextColor(-1);
        this.mStopCastingButton.setOnClickListener(new View.OnClickListener() { // from class: androidx.mediarouter.app.MediaRouteDynamicControllerDialog.3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                if (MediaRouteDynamicControllerDialog.this.mSelectedRoute.isSelected()) {
                    Objects.requireNonNull(MediaRouteDynamicControllerDialog.this.mRouter);
                    MediaRouter.unselect(2);
                }
                MediaRouteDynamicControllerDialog.this.dismiss();
            }
        });
        this.mAdapter = new RecyclerAdapter();
        RecyclerView recyclerView = (RecyclerView) findViewById(2131428404);
        this.mRecyclerView = recyclerView;
        recyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(1));
        this.mVolumeChangeListener = new VolumeChangeListener();
        this.mVolumeSliderHolderMap = new HashMap();
        this.mUnmutedVolumeMap = new HashMap();
        this.mMetadataBackground = (ImageView) findViewById(2131428406);
        this.mMetadataBlackScrim = findViewById(2131428407);
        this.mArtView = (ImageView) findViewById(2131428405);
        TextView textView = (TextView) findViewById(2131428409);
        this.mTitleView = textView;
        textView.setTextColor(-1);
        TextView textView2 = (TextView) findViewById(2131428408);
        this.mSubtitleView = textView2;
        textView2.setTextColor(-1);
        this.mTitlePlaceholder = this.mContext.getResources().getString(2131952804);
        this.mCreated = true;
        updateLayout();
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mAttachedToWindow = false;
        this.mRouter.removeCallback(this.mCallback);
        removeCallbacksAndMessages(null);
        setMediaSession();
    }

    public final void onFilterRoutes(List<MediaRouter.RouteInfo> list) {
        boolean z;
        for (int size = list.size() - 1; size >= 0; size--) {
            MediaRouter.RouteInfo routeInfo = list.get(size);
            if (routeInfo.isDefaultOrBluetooth() || !routeInfo.mEnabled || !routeInfo.matchesSelector(this.mSelector) || this.mSelectedRoute == routeInfo) {
                z = false;
            } else {
                z = true;
            }
            if (!z) {
                list.remove(size);
            }
        }
    }
}
