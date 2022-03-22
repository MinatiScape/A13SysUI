package com.android.systemui.qs.tileimpl;

import android.os.RemoteException;
import android.view.CompositionSamplingListener;
import android.view.WindowInsets;
import android.view.animation.PathInterpolator;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import com.android.keyguard.KeyguardPasswordView;
import com.android.keyguard.KeyguardPasswordViewController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.wm.shell.onehanded.OneHandedController;
import com.android.wm.shell.pip.PipAnimationController;
import com.google.android.systemui.assist.uihints.TranscriptionView;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class QSTileImpl$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ QSTileImpl$$ExternalSyntheticLambda1(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                QSTileImpl qSTileImpl = (QSTileImpl) this.f$0;
                Objects.requireNonNull(qSTileImpl);
                LifecycleRegistry lifecycleRegistry = qSTileImpl.mLifecycle;
                Objects.requireNonNull(lifecycleRegistry);
                if (!lifecycleRegistry.mState.equals(Lifecycle.State.DESTROYED)) {
                    qSTileImpl.mLifecycle.setCurrentState(Lifecycle.State.STARTED);
                    return;
                }
                return;
            case 1:
                KeyguardPasswordViewController keyguardPasswordViewController = (KeyguardPasswordViewController) this.f$0;
                Objects.requireNonNull(keyguardPasswordViewController);
                if (((KeyguardPasswordView) keyguardPasswordViewController.mView).isShown()) {
                    keyguardPasswordViewController.mPasswordEntry.requestFocus();
                    keyguardPasswordViewController.mPasswordEntry.getWindowInsetsController().show(WindowInsets.Type.ime());
                    return;
                }
                return;
            case 2:
                ((CompositionSamplingListener) this.f$0).destroy();
                return;
            case 3:
                StatusBar statusBar = (StatusBar) this.f$0;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                Objects.requireNonNull(statusBar);
                try {
                    statusBar.mDreamManager.awaken();
                    return;
                } catch (RemoteException e) {
                    e.printStackTrace();
                    return;
                }
            case 4:
                OneHandedController oneHandedController = (OneHandedController) this.f$0;
                Objects.requireNonNull(oneHandedController);
                oneHandedController.stopOneHanded();
                return;
            case 5:
                PipAnimationController.PipTransitionAnimator pipTransitionAnimator = (PipAnimationController.PipTransitionAnimator) this.f$0;
                Objects.requireNonNull(pipTransitionAnimator);
                pipTransitionAnimator.mContentOverlay = null;
                return;
            default:
                TranscriptionView transcriptionView = (TranscriptionView) this.f$0;
                PathInterpolator pathInterpolator = TranscriptionView.INTERPOLATOR_SCROLL;
                Objects.requireNonNull(transcriptionView);
                transcriptionView.setVisibility(8);
                transcriptionView.setAlpha(1.0f);
                transcriptionView.setTranslationY(0.0f);
                transcriptionView.setTranscription("");
                transcriptionView.mTranscriptionAnimator = new TranscriptionView.TranscriptionAnimator();
                transcriptionView.mHideFuture.set(null);
                return;
        }
    }
}
