package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.InsetsVisibilities;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import androidx.lifecycle.Observer;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.view.AppearanceRegion;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStore;
import com.android.systemui.util.ViewController;
import java.util.Objects;
/* loaded from: classes.dex */
public final class LightsOutNotifController extends ViewController<View> {
    @VisibleForTesting
    public int mAppearance;
    public final CommandQueue mCommandQueue;
    public int mDisplayId;
    public final NotifLiveDataStore mNotifDataStore;
    public final WindowManager mWindowManager;
    public final LightsOutNotifController$$ExternalSyntheticLambda0 mObserver = new Observer() { // from class: com.android.systemui.statusbar.phone.LightsOutNotifController$$ExternalSyntheticLambda0
        @Override // androidx.lifecycle.Observer
        public final void onChanged(Object obj) {
            LightsOutNotifController lightsOutNotifController = LightsOutNotifController.this;
            Boolean bool = (Boolean) obj;
            Objects.requireNonNull(lightsOutNotifController);
            lightsOutNotifController.updateLightsOutView();
        }
    };
    public final AnonymousClass2 mCallback = new CommandQueue.Callbacks() { // from class: com.android.systemui.statusbar.phone.LightsOutNotifController.2
        @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
        public final void onSystemBarAttributesChanged(int i, int i2, AppearanceRegion[] appearanceRegionArr, boolean z, int i3, InsetsVisibilities insetsVisibilities, String str) {
            LightsOutNotifController lightsOutNotifController = LightsOutNotifController.this;
            if (i == lightsOutNotifController.mDisplayId) {
                lightsOutNotifController.mAppearance = i2;
                lightsOutNotifController.updateLightsOutView();
            }
        }
    };

    @VisibleForTesting
    public boolean areLightsOut() {
        if ((this.mAppearance & 4) != 0) {
            return true;
        }
        return false;
    }

    @VisibleForTesting
    public boolean isShowingDot() {
        if (this.mView.getVisibility() == 0 && this.mView.getAlpha() == 1.0f) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        this.mView.setVisibility(8);
        this.mView.setAlpha(0.0f);
        this.mDisplayId = this.mWindowManager.getDefaultDisplay().getDisplayId();
        this.mNotifDataStore.getHasActiveNotifs().addSyncObserver(this.mObserver);
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this.mCallback);
        updateLightsOutView();
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        this.mNotifDataStore.getHasActiveNotifs().removeObserver(this.mObserver);
        this.mCommandQueue.removeCallback((CommandQueue.Callbacks) this.mCallback);
    }

    @VisibleForTesting
    public boolean shouldShowDot() {
        if (!((Boolean) this.mNotifDataStore.getHasActiveNotifs().getValue()).booleanValue() || !areLightsOut()) {
            return false;
        }
        return true;
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.statusbar.phone.LightsOutNotifController$$ExternalSyntheticLambda0] */
    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.statusbar.phone.LightsOutNotifController$2] */
    public LightsOutNotifController(View view, WindowManager windowManager, NotifLiveDataStore notifLiveDataStore, CommandQueue commandQueue) {
        super(view);
        this.mWindowManager = windowManager;
        this.mNotifDataStore = notifLiveDataStore;
        this.mCommandQueue = commandQueue;
    }

    @VisibleForTesting
    public void updateLightsOutView() {
        long j;
        final boolean shouldShowDot = shouldShowDot();
        if (shouldShowDot != isShowingDot()) {
            float f = 0.0f;
            if (shouldShowDot) {
                this.mView.setAlpha(0.0f);
                this.mView.setVisibility(0);
            }
            ViewPropertyAnimator animate = this.mView.animate();
            if (shouldShowDot) {
                f = 1.0f;
            }
            ViewPropertyAnimator alpha = animate.alpha(f);
            if (shouldShowDot) {
                j = 750;
            } else {
                j = 250;
            }
            alpha.setDuration(j).setInterpolator(new AccelerateInterpolator(2.0f)).setListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.LightsOutNotifController.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    float f2;
                    int i;
                    T t = LightsOutNotifController.this.mView;
                    if (shouldShowDot) {
                        f2 = 1.0f;
                    } else {
                        f2 = 0.0f;
                    }
                    t.setAlpha(f2);
                    T t2 = LightsOutNotifController.this.mView;
                    if (shouldShowDot) {
                        i = 0;
                    } else {
                        i = 8;
                    }
                    t2.setVisibility(i);
                    LightsOutNotifController.this.mView.animate().setListener(null);
                }
            }).start();
        }
    }
}
