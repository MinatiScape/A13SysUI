package androidx.mediarouter.app;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.view.ActionProvider;
import androidx.fragment.app.BackStackRecord;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentController;
import androidx.fragment.app.FragmentManagerImpl;
import androidx.mediarouter.app.MediaRouteButton;
import androidx.mediarouter.media.MediaRouteSelector;
import androidx.mediarouter.media.MediaRouter;
import com.google.android.setupdesign.R$styleable;
import java.lang.ref.WeakReference;
import java.util.Objects;
/* loaded from: classes.dex */
public class MediaRouteActionProvider extends ActionProvider {
    public MediaRouteButton mButton;
    public final MediaRouter mRouter;
    public MediaRouteSelector mSelector = MediaRouteSelector.EMPTY;
    public R$styleable mDialogFactory = R$styleable.sDefault;

    @Override // androidx.core.view.ActionProvider
    public final boolean isVisible() {
        MediaRouter mediaRouter = this.mRouter;
        MediaRouteSelector mediaRouteSelector = this.mSelector;
        Objects.requireNonNull(mediaRouter);
        return MediaRouter.isRouteAvailable(mediaRouteSelector);
    }

    @Override // androidx.core.view.ActionProvider
    public final View onCreateActionView() {
        if (this.mButton != null) {
            Log.e("MRActionProvider", "onCreateActionView: this ActionProvider is already associated with a menu item. Don't reuse MediaRouteActionProvider instances! Abandoning the old menu item...");
        }
        MediaRouteButton mediaRouteButton = new MediaRouteButton(this.mContext, null);
        this.mButton = mediaRouteButton;
        Objects.requireNonNull(mediaRouteButton);
        if (true != mediaRouteButton.mCheatSheetEnabled) {
            mediaRouteButton.mCheatSheetEnabled = true;
            mediaRouteButton.updateContentDescription();
        }
        MediaRouteButton mediaRouteButton2 = this.mButton;
        MediaRouteSelector mediaRouteSelector = this.mSelector;
        if (mediaRouteSelector != null) {
            if (!mediaRouteButton2.mSelector.equals(mediaRouteSelector)) {
                if (mediaRouteButton2.mAttachedToWindow) {
                    if (!mediaRouteButton2.mSelector.isEmpty()) {
                        mediaRouteButton2.mRouter.removeCallback(mediaRouteButton2.mCallback);
                    }
                    if (!mediaRouteSelector.isEmpty()) {
                        MediaRouter mediaRouter = mediaRouteButton2.mRouter;
                        MediaRouteButton.MediaRouterCallback mediaRouterCallback = mediaRouteButton2.mCallback;
                        Objects.requireNonNull(mediaRouter);
                        mediaRouter.addCallback(mediaRouteSelector, mediaRouterCallback, 0);
                    }
                }
                mediaRouteButton2.mSelector = mediaRouteSelector;
                mediaRouteButton2.refreshRoute();
            }
            MediaRouteButton mediaRouteButton3 = this.mButton;
            Objects.requireNonNull(mediaRouteButton3);
            if (mediaRouteButton3.mAlwaysVisible) {
                mediaRouteButton3.mAlwaysVisible = false;
                mediaRouteButton3.refreshVisibility();
                mediaRouteButton3.refreshRoute();
            }
            MediaRouteButton mediaRouteButton4 = this.mButton;
            R$styleable r$styleable = this.mDialogFactory;
            if (r$styleable != null) {
                mediaRouteButton4.mDialogFactory = r$styleable;
                mediaRouteButton4.setLayoutParams(new ViewGroup.LayoutParams(-2, -1));
                return this.mButton;
            }
            Objects.requireNonNull(mediaRouteButton4);
            throw new IllegalArgumentException("factory must not be null");
        }
        Objects.requireNonNull(mediaRouteButton2);
        throw new IllegalArgumentException("selector must not be null");
    }

    @Override // androidx.core.view.ActionProvider
    public final boolean onPerformDefaultAction() {
        MediaRouteSelector mediaRouteSelector;
        Activity activity;
        FragmentManagerImpl fragmentManagerImpl;
        MediaRouteButton mediaRouteButton = this.mButton;
        if (mediaRouteButton == null || !mediaRouteButton.mAttachedToWindow) {
            return false;
        }
        Objects.requireNonNull(mediaRouteButton.mRouter);
        MediaRouter.checkCallingThread();
        MediaRouter.getGlobalRouter();
        Context context = mediaRouteButton.getContext();
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
            Objects.requireNonNull(mediaRouteButton.mRouter);
            if (MediaRouter.getSelectedRoute().isDefaultOrBluetooth()) {
                if (fragmentManagerImpl.findFragmentByTag("android.support.v7.mediarouter:MediaRouteChooserDialogFragment") != null) {
                    Log.w("MediaRouteButton", "showDialog(): Route chooser dialog already showing!");
                    return false;
                }
                Objects.requireNonNull(mediaRouteButton.mDialogFactory);
                MediaRouteChooserDialogFragment mediaRouteChooserDialogFragment = new MediaRouteChooserDialogFragment();
                MediaRouteSelector mediaRouteSelector2 = mediaRouteButton.mSelector;
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
                } else {
                    throw new IllegalArgumentException("selector must not be null");
                }
            } else if (fragmentManagerImpl.findFragmentByTag("android.support.v7.mediarouter:MediaRouteControllerDialogFragment") != null) {
                Log.w("MediaRouteButton", "showDialog(): Route controller dialog already showing!");
                return false;
            } else {
                Objects.requireNonNull(mediaRouteButton.mDialogFactory);
                MediaRouteControllerDialogFragment mediaRouteControllerDialogFragment = new MediaRouteControllerDialogFragment();
                MediaRouteSelector mediaRouteSelector3 = mediaRouteButton.mSelector;
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
                } else {
                    throw new IllegalArgumentException("selector must not be null");
                }
            }
            return true;
        }
        throw new IllegalStateException("The activity must be a subclass of FragmentActivity");
    }

    public MediaRouteActionProvider(Context context) {
        super(context);
        this.mRouter = MediaRouter.getInstance(context);
        new WeakReference(this);
    }
}
