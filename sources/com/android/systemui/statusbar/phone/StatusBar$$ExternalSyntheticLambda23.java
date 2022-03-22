package com.android.systemui.statusbar.phone;

import android.content.Intent;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.dreams.touch.DreamOverlayTouchMonitor;
import com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda17;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class StatusBar$$ExternalSyntheticLambda23 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;
    public final /* synthetic */ Object f$2;

    public /* synthetic */ StatusBar$$ExternalSyntheticLambda23(Object obj, Object obj2, Object obj3, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
        this.f$2 = obj3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                StatusBar statusBar = (StatusBar) this.f$0;
                ActivityLaunchAnimator.Controller controller = (ActivityLaunchAnimator.Controller) this.f$2;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                Objects.requireNonNull(statusBar);
                statusBar.startActivityDismissingKeyguard((Intent) this.f$1, true, true, false, null, 0, controller);
                return;
            default:
                DreamOverlayTouchMonitor dreamOverlayTouchMonitor = (DreamOverlayTouchMonitor) this.f$0;
                DreamOverlayTouchMonitor.TouchSessionImpl touchSessionImpl = (DreamOverlayTouchMonitor.TouchSessionImpl) this.f$1;
                CallbackToFutureAdapter.Completer completer = (CallbackToFutureAdapter.Completer) this.f$2;
                Objects.requireNonNull(dreamOverlayTouchMonitor);
                if (dreamOverlayTouchMonitor.mActiveTouchSessions.remove(touchSessionImpl)) {
                    Objects.requireNonNull(touchSessionImpl);
                    touchSessionImpl.mCallbacks.forEach(NavigationBar$$ExternalSyntheticLambda17.INSTANCE$1);
                    DreamOverlayTouchMonitor.TouchSessionImpl touchSessionImpl2 = touchSessionImpl.mPredecessor;
                    if (touchSessionImpl2 != null) {
                        dreamOverlayTouchMonitor.mActiveTouchSessions.add(touchSessionImpl2);
                    }
                    completer.set(touchSessionImpl2);
                    return;
                }
                return;
        }
    }
}
