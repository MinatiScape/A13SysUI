package com.android.systemui.communal;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.Log;
import androidx.core.view.ViewCompat$$ExternalSyntheticLambda0;
import com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda2;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.keyguard.KeyguardVisibilityHelper;
import com.android.systemui.communal.CommunalStateController;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.ViewController;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class CommunalHostViewController extends ViewController<CommunalHostView> {
    public static final AnimationProperties COMMUNAL_ANIMATION_PROPERTIES;
    public static final boolean DEBUG = Log.isLoggable("CommunalController", 3);
    public final CommunalStateController mCommunalStateController;
    public WeakReference<CommunalSource> mCurrentSource;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final KeyguardVisibilityHelper mKeyguardVisibilityHelper;
    public final Executor mMainExecutor;
    public float mQsExpansion;
    public float mShadeExpansion;
    public int mState;
    public final StatusBarStateController mStatusBarStateController;
    public Optional<ShowRequest> mLastRequest = Optional.empty();
    public AnonymousClass1 mKeyguardUpdateCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.communal.CommunalHostViewController.1
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onKeyguardBouncerChanged(boolean z) {
            if (CommunalHostViewController.DEBUG) {
                ViewCompat$$ExternalSyntheticLambda0.m("onKeyguardBouncerChanged:", z, "CommunalController");
            }
            CommunalHostViewController.this.setState(4, z);
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onKeyguardOccludedChanged(boolean z) {
            if (CommunalHostViewController.DEBUG) {
                ViewCompat$$ExternalSyntheticLambda0.m("onKeyguardOccludedChanged", z, "CommunalController");
            }
            CommunalHostViewController.this.setState(8, z);
        }
    };
    public AnonymousClass2 mKeyguardCallback = new KeyguardStateController.Callback() { // from class: com.android.systemui.communal.CommunalHostViewController.2
        @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
        public final void onKeyguardShowingChanged() {
            boolean isShowing = CommunalHostViewController.this.mKeyguardStateController.isShowing();
            if (CommunalHostViewController.DEBUG) {
                ViewCompat$$ExternalSyntheticLambda0.m("setKeyguardShowing:", isShowing, "CommunalController");
            }
            CommunalHostViewController.this.setState(1, isShowing);
        }
    };
    public AnonymousClass3 mDozeCallback = new StatusBarStateController.StateListener() { // from class: com.android.systemui.communal.CommunalHostViewController.3
        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public final void onDozingChanged(boolean z) {
            if (CommunalHostViewController.DEBUG) {
                ViewCompat$$ExternalSyntheticLambda0.m("setDozing:", z, "CommunalController");
            }
            CommunalHostViewController.this.setState(2, z);
        }

        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public final void onStateChanged(int i) {
            CommunalHostViewController.this.updateCommunalViewOccluded();
        }
    };

    /* loaded from: classes.dex */
    public static class ShowRequest {
        public boolean mShouldShow;
        public WeakReference<CommunalSource> mSource;

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ShowRequest)) {
                return false;
            }
            ShowRequest showRequest = (ShowRequest) obj;
            return this.mShouldShow == showRequest.mShouldShow && Objects.equals(getSource(), showRequest.getSource());
        }

        public final int hashCode() {
            return Objects.hash(Boolean.valueOf(this.mShouldShow), this.mSource);
        }

        public final CommunalSource getSource() {
            WeakReference<CommunalSource> weakReference = this.mSource;
            if (weakReference != null) {
                return weakReference.get();
            }
            return null;
        }

        public ShowRequest(boolean z, WeakReference<CommunalSource> weakReference) {
            this.mShouldShow = z;
            this.mSource = weakReference;
        }
    }

    public static String describeState(int i) {
        return i != 1 ? i != 2 ? i != 4 ? "UNDEFINED_STATE" : "bouncer_showing" : "dozing" : "keyguard_showing";
    }

    static {
        AnimationProperties animationProperties = new AnimationProperties();
        animationProperties.duration = 360L;
        COMMUNAL_ANIMATION_PROPERTIES = animationProperties;
    }

    @Override // com.android.systemui.util.ViewController
    public final void onInit() {
        setState(1, this.mKeyguardStateController.isShowing());
        setState(2, this.mStatusBarStateController.isDozing());
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        this.mKeyguardStateController.addCallback(this.mKeyguardCallback);
        this.mStatusBarStateController.addCallback(this.mDozeCallback);
        this.mKeyguardUpdateMonitor.registerCallback(this.mKeyguardUpdateCallback);
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        this.mKeyguardStateController.removeCallback(this.mKeyguardCallback);
        this.mStatusBarStateController.removeCallback(this.mDozeCallback);
        this.mKeyguardUpdateMonitor.removeCallback(this.mKeyguardUpdateCallback);
    }

    public final void setState(int i, boolean z) {
        int i2 = this.mState;
        boolean z2 = DEBUG;
        if (z2) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("setState flag:");
            m.append(describeState(i));
            m.append(" enabled:");
            m.append(z);
            Log.d("CommunalController", m.toString());
        }
        if (z) {
            this.mState = i | this.mState;
        } else {
            this.mState = (~i) & this.mState;
        }
        if (z2) {
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("updated state:");
            StringBuilder sb = new StringBuilder();
            if ((this.mState & 1) == 1) {
                sb.append(String.format("[%s]", describeState(1)));
            }
            if ((this.mState & 2) == 2) {
                sb.append(String.format("[%s]", describeState(2)));
            }
            if ((this.mState & 4) == 4) {
                sb.append(String.format("[%s]", describeState(4)));
            }
            m2.append(sb.toString());
            Log.d("CommunalController", m2.toString());
        }
        if (i2 != this.mState) {
            showSource();
        }
        updateCommunalViewOccluded();
    }

    public final void showSource() {
        boolean z;
        int i = this.mState;
        if ((i & 1) == 1 && (i & 10) == 0 && this.mCurrentSource != null) {
            z = true;
        } else {
            z = false;
        }
        ShowRequest showRequest = new ShowRequest(z, this.mCurrentSource);
        if (!this.mLastRequest.isPresent() || !Objects.equals(this.mLastRequest.get(), showRequest)) {
            this.mLastRequest = Optional.of(showRequest);
            this.mMainExecutor.execute(new CarrierTextManager$$ExternalSyntheticLambda2(this, showRequest, 1));
        }
    }

    public final void updateCommunalViewOccluded() {
        boolean z;
        boolean z2;
        boolean z3 = true;
        if ((this.mState & 4) == 4) {
            z = true;
        } else {
            z = false;
        }
        int state = this.mStatusBarStateController.getState();
        if (state == 0 || state == 2) {
            z2 = true;
        } else {
            z2 = false;
        }
        CommunalStateController communalStateController = this.mCommunalStateController;
        if (!z && !z2 && this.mQsExpansion <= 0.0f && this.mShadeExpansion <= 0.0f) {
            z3 = false;
        }
        Objects.requireNonNull(communalStateController);
        if (communalStateController.mCommunalViewOccluded != z3) {
            communalStateController.mCommunalViewOccluded = z3;
            ArrayList arrayList = new ArrayList(communalStateController.mCallbacks);
            for (int i = 0; i < arrayList.size(); i++) {
                Objects.requireNonNull((CommunalStateController.Callback) arrayList.get(i));
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.systemui.communal.CommunalHostViewController$1] */
    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.systemui.communal.CommunalHostViewController$2] */
    /* JADX WARN: Type inference failed for: r0v3, types: [com.android.systemui.communal.CommunalHostViewController$3] */
    public CommunalHostViewController(Executor executor, CommunalStateController communalStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardStateController keyguardStateController, ScreenOffAnimationController screenOffAnimationController, StatusBarStateController statusBarStateController, CommunalHostView communalHostView) {
        super(communalHostView);
        this.mCommunalStateController = communalStateController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mMainExecutor = executor;
        this.mKeyguardStateController = keyguardStateController;
        this.mStatusBarStateController = statusBarStateController;
        this.mKeyguardVisibilityHelper = new KeyguardVisibilityHelper(communalHostView, communalStateController, keyguardStateController, screenOffAnimationController, false, true);
    }
}
