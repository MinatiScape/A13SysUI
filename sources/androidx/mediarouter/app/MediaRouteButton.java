package androidx.mediarouter.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.BackStackRecord;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentController;
import androidx.fragment.app.FragmentManagerImpl;
import androidx.mediarouter.media.MediaRouteSelector;
import androidx.mediarouter.media.MediaRouter;
import androidx.mediarouter.media.MediaRouterParams;
import com.google.android.setupdesign.R$styleable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public class MediaRouteButton extends View {
    public static ConnectivityReceiver sConnectivityReceiver;
    public boolean mAlwaysVisible;
    public boolean mAttachedToWindow;
    public ColorStateList mButtonTint;
    public final MediaRouterCallback mCallback;
    public boolean mCheatSheetEnabled;
    public int mConnectionState;
    public R$styleable mDialogFactory;
    public int mLastConnectionState;
    public int mMinHeight;
    public int mMinWidth;
    public Drawable mRemoteIndicator;
    public RemoteIndicatorLoader mRemoteIndicatorLoader;
    public int mRemoteIndicatorResIdToLoad;
    public final MediaRouter mRouter;
    public MediaRouteSelector mSelector;
    public int mVisibility;
    public static final SparseArray<Drawable.ConstantState> sRemoteIndicatorCache = new SparseArray<>(2);
    public static final int[] CHECKED_STATE_SET = {16842912};
    public static final int[] CHECKABLE_STATE_SET = {16842911};

    /* loaded from: classes.dex */
    public final class MediaRouterCallback extends MediaRouter.Callback {
        public MediaRouterCallback() {
        }

        @Override // androidx.mediarouter.media.MediaRouter.Callback
        public final void onProviderAdded() {
            MediaRouteButton.this.refreshRoute();
        }

        @Override // androidx.mediarouter.media.MediaRouter.Callback
        public final void onProviderChanged() {
            MediaRouteButton.this.refreshRoute();
        }

        @Override // androidx.mediarouter.media.MediaRouter.Callback
        public final void onProviderRemoved() {
            MediaRouteButton.this.refreshRoute();
        }

        @Override // androidx.mediarouter.media.MediaRouter.Callback
        public final void onRouteAdded() {
            MediaRouteButton.this.refreshRoute();
        }

        @Override // androidx.mediarouter.media.MediaRouter.Callback
        public final void onRouteChanged(MediaRouter.RouteInfo routeInfo) {
            MediaRouteButton.this.refreshRoute();
        }

        @Override // androidx.mediarouter.media.MediaRouter.Callback
        public final void onRouteRemoved() {
            MediaRouteButton.this.refreshRoute();
        }

        @Override // androidx.mediarouter.media.MediaRouter.Callback
        public final void onRouteSelected(MediaRouter.RouteInfo routeInfo) {
            MediaRouteButton.this.refreshRoute();
        }

        @Override // androidx.mediarouter.media.MediaRouter.Callback
        public final void onRouteUnselected() {
            MediaRouteButton.this.refreshRoute();
        }

        @Override // androidx.mediarouter.media.MediaRouter.Callback
        public final void onRouterParamsChanged(MediaRouterParams mediaRouterParams) {
            if (mediaRouterParams == null) {
                Objects.requireNonNull(MediaRouteButton.this);
                return;
            }
            throw null;
        }
    }

    /* loaded from: classes.dex */
    public final class RemoteIndicatorLoader extends AsyncTask<Void, Void, Drawable> {
        public final Context mContext;
        public final int mResId;

        public RemoteIndicatorLoader(int i, Context context) {
            this.mResId = i;
            this.mContext = context;
        }

        @Override // android.os.AsyncTask
        public final Drawable doInBackground(Void[] voidArr) {
            if (MediaRouteButton.sRemoteIndicatorCache.get(this.mResId) == null) {
                return AppCompatResources.getDrawable(this.mContext, this.mResId);
            }
            return null;
        }

        @Override // android.os.AsyncTask
        public final void onCancelled(Drawable drawable) {
            Drawable drawable2 = drawable;
            if (drawable2 != null) {
                MediaRouteButton.sRemoteIndicatorCache.put(this.mResId, drawable2.getConstantState());
            }
            MediaRouteButton.this.mRemoteIndicatorLoader = null;
        }

        @Override // android.os.AsyncTask
        public final void onPostExecute(Drawable drawable) {
            Drawable drawable2 = drawable;
            if (drawable2 != null) {
                MediaRouteButton.sRemoteIndicatorCache.put(this.mResId, drawable2.getConstantState());
                MediaRouteButton.this.mRemoteIndicatorLoader = null;
            } else {
                Drawable.ConstantState constantState = MediaRouteButton.sRemoteIndicatorCache.get(this.mResId);
                if (constantState != null) {
                    drawable2 = constantState.newDrawable();
                }
                MediaRouteButton.this.mRemoteIndicatorLoader = null;
            }
            MediaRouteButton.this.setRemoteIndicatorDrawableInternal(drawable2);
        }
    }

    @Override // android.view.View
    public final int[] onCreateDrawableState(int i) {
        int[] onCreateDrawableState = super.onCreateDrawableState(i + 1);
        if (this.mRouter == null) {
            return onCreateDrawableState;
        }
        int i2 = this.mConnectionState;
        if (i2 == 1) {
            View.mergeDrawableStates(onCreateDrawableState, CHECKABLE_STATE_SET);
        } else if (i2 == 2) {
            View.mergeDrawableStates(onCreateDrawableState, CHECKED_STATE_SET);
        }
        return onCreateDrawableState;
    }

    /* loaded from: classes.dex */
    public static final class ConnectivityReceiver extends BroadcastReceiver {
        public final Context mContext;
        public boolean mIsConnected = true;
        public ArrayList mButtons = new ArrayList();

        public ConnectivityReceiver(Context context) {
            this.mContext = context;
        }

        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            boolean z;
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction()) && this.mIsConnected != (!intent.getBooleanExtra("noConnectivity", false))) {
                this.mIsConnected = z;
                Iterator it = this.mButtons.iterator();
                while (it.hasNext()) {
                    ((MediaRouteButton) it.next()).refreshVisibility();
                }
            }
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public MediaRouteButton(android.content.Context r10, android.util.AttributeSet r11) {
        /*
            Method dump skipped, instructions count: 245
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.mediarouter.app.MediaRouteButton.<init>(android.content.Context, android.util.AttributeSet):void");
    }

    public final void loadRemoteIndicatorIfNeeded() {
        if (this.mRemoteIndicatorResIdToLoad > 0) {
            RemoteIndicatorLoader remoteIndicatorLoader = this.mRemoteIndicatorLoader;
            if (remoteIndicatorLoader != null) {
                remoteIndicatorLoader.cancel(false);
            }
            RemoteIndicatorLoader remoteIndicatorLoader2 = new RemoteIndicatorLoader(this.mRemoteIndicatorResIdToLoad, getContext());
            this.mRemoteIndicatorLoader = remoteIndicatorLoader2;
            this.mRemoteIndicatorResIdToLoad = 0;
            remoteIndicatorLoader2.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new Void[0]);
        }
    }

    public final void refreshRoute() {
        int i;
        Objects.requireNonNull(this.mRouter);
        MediaRouter.RouteInfo selectedRoute = MediaRouter.getSelectedRoute();
        boolean z = true;
        boolean z2 = !selectedRoute.isDefaultOrBluetooth();
        if (z2) {
            i = selectedRoute.mConnectionState;
        } else {
            i = 0;
        }
        if (this.mConnectionState != i) {
            this.mConnectionState = i;
            updateContentDescription();
            refreshDrawableState();
        }
        if (i == 1) {
            loadRemoteIndicatorIfNeeded();
        }
        if (this.mAttachedToWindow) {
            if (!this.mAlwaysVisible && !z2) {
                MediaRouter mediaRouter = this.mRouter;
                MediaRouteSelector mediaRouteSelector = this.mSelector;
                Objects.requireNonNull(mediaRouter);
                if (!MediaRouter.isRouteAvailable(mediaRouteSelector)) {
                    z = false;
                }
            }
            setEnabled(z);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x001c  */
    /* JADX WARN: Removed duplicated region for block: B:18:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void refreshVisibility() {
        /*
            r2 = this;
            int r0 = r2.mVisibility
            if (r0 != 0) goto L_0x0013
            boolean r0 = r2.mAlwaysVisible
            if (r0 != 0) goto L_0x0013
            androidx.mediarouter.app.MediaRouteButton$ConnectivityReceiver r0 = androidx.mediarouter.app.MediaRouteButton.sConnectivityReceiver
            java.util.Objects.requireNonNull(r0)
            boolean r0 = r0.mIsConnected
            if (r0 != 0) goto L_0x0013
            r0 = 4
            goto L_0x0015
        L_0x0013:
            int r0 = r2.mVisibility
        L_0x0015:
            super.setVisibility(r0)
            android.graphics.drawable.Drawable r0 = r2.mRemoteIndicator
            if (r0 == 0) goto L_0x0029
            int r2 = r2.getVisibility()
            r1 = 0
            if (r2 != 0) goto L_0x0025
            r2 = 1
            goto L_0x0026
        L_0x0025:
            r2 = r1
        L_0x0026:
            r0.setVisible(r2, r1)
        L_0x0029:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.mediarouter.app.MediaRouteButton.refreshVisibility():void");
    }

    public final void setRemoteIndicatorDrawableInternal(Drawable drawable) {
        boolean z;
        RemoteIndicatorLoader remoteIndicatorLoader = this.mRemoteIndicatorLoader;
        if (remoteIndicatorLoader != null) {
            remoteIndicatorLoader.cancel(false);
        }
        Drawable drawable2 = this.mRemoteIndicator;
        if (drawable2 != null) {
            drawable2.setCallback(null);
            unscheduleDrawable(this.mRemoteIndicator);
        }
        if (drawable != null) {
            if (this.mButtonTint != null) {
                drawable = drawable.mutate();
                drawable.setTintList(this.mButtonTint);
            }
            drawable.setCallback(this);
            drawable.setState(getDrawableState());
            if (getVisibility() == 0) {
                z = true;
            } else {
                z = false;
            }
            drawable.setVisible(z, false);
        }
        this.mRemoteIndicator = drawable;
        refreshDrawableState();
    }

    @Override // android.view.View
    public final void setVisibility(int i) {
        this.mVisibility = i;
        refreshVisibility();
    }

    public final void updateContentDescription() {
        int i;
        int i2 = this.mConnectionState;
        if (i2 == 1) {
            i = 2131952802;
        } else if (i2 != 2) {
            i = 2131952803;
        } else {
            i = 2131952801;
        }
        String string = getContext().getString(i);
        setContentDescription(string);
        if (!this.mCheatSheetEnabled || TextUtils.isEmpty(string)) {
            string = null;
        }
        setTooltipText(string);
    }

    @Override // android.view.View
    public final void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mRemoteIndicator != null) {
            this.mRemoteIndicator.setState(getDrawableState());
            if (this.mRemoteIndicator.getCurrent() instanceof AnimationDrawable) {
                AnimationDrawable animationDrawable = (AnimationDrawable) this.mRemoteIndicator.getCurrent();
                int i = this.mConnectionState;
                if (i == 1 || this.mLastConnectionState != i) {
                    if (!animationDrawable.isRunning()) {
                        animationDrawable.start();
                    }
                } else if (i == 2 && !animationDrawable.isRunning()) {
                    animationDrawable.selectDrawable(animationDrawable.getNumberOfFrames() - 1);
                }
            }
            invalidate();
        }
        this.mLastConnectionState = this.mConnectionState;
    }

    @Override // android.view.View
    public final void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.mRemoteIndicator;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
    }

    @Override // android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.mAttachedToWindow = true;
            if (!this.mSelector.isEmpty()) {
                MediaRouter mediaRouter = this.mRouter;
                MediaRouteSelector mediaRouteSelector = this.mSelector;
                MediaRouterCallback mediaRouterCallback = this.mCallback;
                Objects.requireNonNull(mediaRouter);
                mediaRouter.addCallback(mediaRouteSelector, mediaRouterCallback, 0);
            }
            refreshRoute();
            ConnectivityReceiver connectivityReceiver = sConnectivityReceiver;
            Objects.requireNonNull(connectivityReceiver);
            if (connectivityReceiver.mButtons.size() == 0) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                connectivityReceiver.mContext.registerReceiver(connectivityReceiver, intentFilter);
            }
            connectivityReceiver.mButtons.add(this);
        }
    }

    @Override // android.view.View
    public final void onDetachedFromWindow() {
        if (!isInEditMode()) {
            this.mAttachedToWindow = false;
            if (!this.mSelector.isEmpty()) {
                this.mRouter.removeCallback(this.mCallback);
            }
            ConnectivityReceiver connectivityReceiver = sConnectivityReceiver;
            Objects.requireNonNull(connectivityReceiver);
            connectivityReceiver.mButtons.remove(this);
            if (connectivityReceiver.mButtons.size() == 0) {
                connectivityReceiver.mContext.unregisterReceiver(connectivityReceiver);
            }
        }
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mRemoteIndicator != null) {
            int paddingLeft = getPaddingLeft();
            int width = getWidth() - getPaddingRight();
            int paddingTop = getPaddingTop();
            int height = getHeight() - getPaddingBottom();
            int intrinsicWidth = this.mRemoteIndicator.getIntrinsicWidth();
            int intrinsicHeight = this.mRemoteIndicator.getIntrinsicHeight();
            int i = (((width - paddingLeft) - intrinsicWidth) / 2) + paddingLeft;
            int i2 = (((height - paddingTop) - intrinsicHeight) / 2) + paddingTop;
            this.mRemoteIndicator.setBounds(i, i2, intrinsicWidth + i, intrinsicHeight + i2);
            this.mRemoteIndicator.draw(canvas);
        }
    }

    @Override // android.view.View
    public final void onMeasure(int i, int i2) {
        int i3;
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        int mode = View.MeasureSpec.getMode(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        int i4 = this.mMinWidth;
        Drawable drawable = this.mRemoteIndicator;
        int i5 = 0;
        if (drawable != null) {
            i3 = getPaddingRight() + getPaddingLeft() + drawable.getIntrinsicWidth();
        } else {
            i3 = 0;
        }
        int max = Math.max(i4, i3);
        int i6 = this.mMinHeight;
        Drawable drawable2 = this.mRemoteIndicator;
        if (drawable2 != null) {
            i5 = getPaddingBottom() + getPaddingTop() + drawable2.getIntrinsicHeight();
        }
        int max2 = Math.max(i6, i5);
        if (mode == Integer.MIN_VALUE) {
            size = Math.min(size, max);
        } else if (mode != 1073741824) {
            size = max;
        }
        if (mode2 == Integer.MIN_VALUE) {
            size2 = Math.min(size2, max2);
        } else if (mode2 != 1073741824) {
            size2 = max2;
        }
        setMeasuredDimension(size, size2);
    }

    @Override // android.view.View
    public final boolean performClick() {
        boolean z;
        MediaRouteSelector mediaRouteSelector;
        Activity activity;
        FragmentManagerImpl fragmentManagerImpl;
        boolean performClick = super.performClick();
        if (!performClick) {
            playSoundEffect(0);
        }
        loadRemoteIndicatorIfNeeded();
        if (this.mAttachedToWindow) {
            Objects.requireNonNull(this.mRouter);
            MediaRouter.checkCallingThread();
            MediaRouter.getGlobalRouter();
            Context context = getContext();
            while (true) {
                mediaRouteSelector = null;
                if (!(context instanceof ContextWrapper)) {
                    activity = null;
                    break;
                } else if (context instanceof Activity) {
                    activity = (Activity) context;
                    break;
                } else {
                    context = ((ContextWrapper) context).getBaseContext();
                }
            }
            if (activity instanceof FragmentActivity) {
                FragmentActivity fragmentActivity = (FragmentActivity) activity;
                Objects.requireNonNull(fragmentActivity);
                FragmentController fragmentController = fragmentActivity.mFragments;
                Objects.requireNonNull(fragmentController);
                fragmentManagerImpl = fragmentController.mHost.mFragmentManager;
            } else {
                fragmentManagerImpl = null;
            }
            if (fragmentManagerImpl != null) {
                Objects.requireNonNull(this.mRouter);
                if (MediaRouter.getSelectedRoute().isDefaultOrBluetooth()) {
                    if (fragmentManagerImpl.findFragmentByTag("android.support.v7.mediarouter:MediaRouteChooserDialogFragment") != null) {
                        Log.w("MediaRouteButton", "showDialog(): Route chooser dialog already showing!");
                    } else {
                        Objects.requireNonNull(this.mDialogFactory);
                        MediaRouteChooserDialogFragment mediaRouteChooserDialogFragment = new MediaRouteChooserDialogFragment();
                        MediaRouteSelector mediaRouteSelector2 = this.mSelector;
                        if (mediaRouteSelector2 != null) {
                            mediaRouteChooserDialogFragment.ensureRouteSelector();
                            if (!mediaRouteChooserDialogFragment.mSelector.equals(mediaRouteSelector2)) {
                                mediaRouteChooserDialogFragment.mSelector = mediaRouteSelector2;
                                Bundle bundle = mediaRouteChooserDialogFragment.mArguments;
                                if (bundle == null) {
                                    bundle = new Bundle();
                                }
                                bundle.putBundle("selector", mediaRouteSelector2.mBundle);
                                mediaRouteChooserDialogFragment.setArguments(bundle);
                                AppCompatDialog appCompatDialog = mediaRouteChooserDialogFragment.mDialog;
                                if (appCompatDialog != null) {
                                    if (mediaRouteChooserDialogFragment.mUseDynamicGroup) {
                                        ((MediaRouteDynamicChooserDialog) appCompatDialog).setRouteSelector(mediaRouteSelector2);
                                    } else {
                                        ((MediaRouteChooserDialog) appCompatDialog).setRouteSelector(mediaRouteSelector2);
                                    }
                                }
                            }
                            BackStackRecord backStackRecord = new BackStackRecord(fragmentManagerImpl);
                            backStackRecord.doAddOp(0, mediaRouteChooserDialogFragment, "android.support.v7.mediarouter:MediaRouteChooserDialogFragment", 1);
                            backStackRecord.commitInternal(true);
                            z = true;
                        } else {
                            throw new IllegalArgumentException("selector must not be null");
                        }
                    }
                } else if (fragmentManagerImpl.findFragmentByTag("android.support.v7.mediarouter:MediaRouteControllerDialogFragment") != null) {
                    Log.w("MediaRouteButton", "showDialog(): Route controller dialog already showing!");
                } else {
                    Objects.requireNonNull(this.mDialogFactory);
                    MediaRouteControllerDialogFragment mediaRouteControllerDialogFragment = new MediaRouteControllerDialogFragment();
                    MediaRouteSelector mediaRouteSelector3 = this.mSelector;
                    if (mediaRouteSelector3 != null) {
                        if (mediaRouteControllerDialogFragment.mSelector == null) {
                            Bundle bundle2 = mediaRouteControllerDialogFragment.mArguments;
                            if (bundle2 != null) {
                                Bundle bundle3 = bundle2.getBundle("selector");
                                MediaRouteSelector mediaRouteSelector4 = MediaRouteSelector.EMPTY;
                                if (bundle3 != null) {
                                    mediaRouteSelector = new MediaRouteSelector(bundle3, null);
                                }
                                mediaRouteControllerDialogFragment.mSelector = mediaRouteSelector;
                            }
                            if (mediaRouteControllerDialogFragment.mSelector == null) {
                                mediaRouteControllerDialogFragment.mSelector = MediaRouteSelector.EMPTY;
                            }
                        }
                        if (!mediaRouteControllerDialogFragment.mSelector.equals(mediaRouteSelector3)) {
                            mediaRouteControllerDialogFragment.mSelector = mediaRouteSelector3;
                            Bundle bundle4 = mediaRouteControllerDialogFragment.mArguments;
                            if (bundle4 == null) {
                                bundle4 = new Bundle();
                            }
                            bundle4.putBundle("selector", mediaRouteSelector3.mBundle);
                            mediaRouteControllerDialogFragment.setArguments(bundle4);
                            AppCompatDialog appCompatDialog2 = mediaRouteControllerDialogFragment.mDialog;
                            if (appCompatDialog2 != null && mediaRouteControllerDialogFragment.mUseDynamicGroup) {
                                ((MediaRouteDynamicControllerDialog) appCompatDialog2).setRouteSelector(mediaRouteSelector3);
                            }
                        }
                        BackStackRecord backStackRecord2 = new BackStackRecord(fragmentManagerImpl);
                        backStackRecord2.doAddOp(0, mediaRouteControllerDialogFragment, "android.support.v7.mediarouter:MediaRouteControllerDialogFragment", 1);
                        backStackRecord2.commitInternal(true);
                        z = true;
                    } else {
                        throw new IllegalArgumentException("selector must not be null");
                    }
                }
                if (!z || performClick) {
                    return true;
                }
                return false;
            }
            throw new IllegalStateException("The activity must be a subclass of FragmentActivity");
        }
        z = false;
        if (!z) {
        }
        return true;
    }

    @Override // android.view.View
    public final boolean verifyDrawable(Drawable drawable) {
        if (super.verifyDrawable(drawable) || drawable == this.mRemoteIndicator) {
            return true;
        }
        return false;
    }
}
