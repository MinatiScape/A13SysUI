package com.android.systemui.statusbar.phone;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.window.BackNavigationInfo;
import android.window.IOnBackInvokedCallback;
import com.android.systemui.navigationbar.TaskbarDelegate;
import com.android.wm.shell.back.BackAnimationController;
import com.android.wm.shell.pip.IPip;
import com.android.wm.shell.pip.Pip;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class StatusBar$$ExternalSyntheticLambda30 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ StatusBar$$ExternalSyntheticLambda30(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                StatusBar statusBar = (StatusBar) this.f$0;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                Objects.requireNonNull(statusBar);
                statusBar.mNotificationShadeWindowController.setScrimsVisibility(((Integer) obj).intValue());
                return;
            case 1:
                TaskbarDelegate taskbarDelegate = (TaskbarDelegate) this.f$0;
                int i = TaskbarDelegate.$r8$clinit;
                Objects.requireNonNull(taskbarDelegate);
                if (((Boolean) obj).booleanValue()) {
                    taskbarDelegate.mAutoHideController.touchAutoHide();
                    return;
                }
                return;
            case 2:
                IPip.Stub stub = (IPip.Stub) ((Pip) obj).createExternalInterface();
                Objects.requireNonNull(stub);
                ((Bundle) this.f$0).putBinder("extra_shell_pip", stub);
                return;
            default:
                BackAnimationController.IBackAnimationImpl iBackAnimationImpl = (BackAnimationController.IBackAnimationImpl) this.f$0;
                BackAnimationController backAnimationController = (BackAnimationController) obj;
                int i2 = BackAnimationController.IBackAnimationImpl.$r8$clinit;
                Objects.requireNonNull(iBackAnimationImpl);
                BackAnimationController backAnimationController2 = iBackAnimationImpl.mController;
                int i3 = BackAnimationController.PROGRESS_THRESHOLD;
                Objects.requireNonNull(backAnimationController2);
                BackNavigationInfo backNavigationInfo = backAnimationController2.mBackNavigationInfo;
                if (backNavigationInfo != null) {
                    IOnBackInvokedCallback onBackInvokedCallback = backNavigationInfo.getOnBackInvokedCallback();
                    if (backAnimationController2.mTriggerBack) {
                        if (onBackInvokedCallback != null) {
                            try {
                                onBackInvokedCallback.onBackInvoked();
                            } catch (RemoteException e) {
                                Log.e("BackAnimationController", "dispatchOnBackInvoked error: ", e);
                            }
                        }
                    } else if (onBackInvokedCallback != null) {
                        try {
                            onBackInvokedCallback.onBackCancelled();
                        } catch (RemoteException e2) {
                            Log.e("BackAnimationController", "dispatchOnBackCancelled error: ", e2);
                        }
                    }
                }
                backAnimationController2.finishAnimation();
                return;
        }
    }
}
