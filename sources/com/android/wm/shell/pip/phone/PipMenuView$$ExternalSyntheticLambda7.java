package com.android.wm.shell.pip.phone;

import android.graphics.Rect;
import android.os.RemoteException;
import com.android.systemui.accessibility.MagnificationModeSwitch;
import com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda5;
import com.android.systemui.screenshot.ScrollCaptureClient;
import com.android.systemui.statusbar.phone.KeyguardBouncer;
import com.android.wm.shell.pip.phone.PipAppOpsListener;
import com.android.wm.shell.transition.Transitions;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PipMenuView$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ PipMenuView$$ExternalSyntheticLambda7(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                PhonePipMenuController phonePipMenuController = (PhonePipMenuController) this.f$0;
                Objects.requireNonNull(phonePipMenuController);
                phonePipMenuController.mListeners.forEach(QSTileHost$$ExternalSyntheticLambda5.INSTANCE$3);
                return;
            case 1:
                MagnificationModeSwitch magnificationModeSwitch = (MagnificationModeSwitch) this.f$0;
                Objects.requireNonNull(magnificationModeSwitch);
                Rect draggableWindowBounds = magnificationModeSwitch.getDraggableWindowBounds();
                if (!magnificationModeSwitch.mDraggableWindowBounds.equals(draggableWindowBounds)) {
                    magnificationModeSwitch.mDraggableWindowBounds.set(draggableWindowBounds);
                    magnificationModeSwitch.stickToScreenEdge(magnificationModeSwitch.mToLeftScreenEdge);
                    return;
                }
                return;
            case 2:
                ScrollCaptureClient.SessionWrapper sessionWrapper = (ScrollCaptureClient.SessionWrapper) this.f$0;
                int i = ScrollCaptureClient.SessionWrapper.$r8$clinit;
                Objects.requireNonNull(sessionWrapper);
                try {
                    sessionWrapper.mCancellationSignal.cancel();
                    return;
                } catch (RemoteException unused) {
                    return;
                }
            case 3:
                KeyguardBouncer keyguardBouncer = (KeyguardBouncer) this.f$0;
                Objects.requireNonNull(keyguardBouncer);
                keyguardBouncer.mContainer.removeAllViews();
                keyguardBouncer.mInitialized = false;
                return;
            case 4:
                PipAppOpsListener.AnonymousClass1 r3 = (PipAppOpsListener.AnonymousClass1) this.f$0;
                Objects.requireNonNull(r3);
                ((PipMotionHelper) PipAppOpsListener.this.mCallback).dismissPip();
                return;
            default:
                Transitions.SettingsObserver settingsObserver = (Transitions.SettingsObserver) this.f$0;
                int i2 = Transitions.SettingsObserver.$r8$clinit;
                Objects.requireNonNull(settingsObserver);
                Transitions transitions = Transitions.this;
                float f = transitions.mTransitionAnimationScaleSetting;
                int size = transitions.mHandlers.size();
                while (true) {
                    size--;
                    if (size >= 0) {
                        transitions.mHandlers.get(size).setAnimScaleSetting(f);
                    } else {
                        return;
                    }
                }
        }
    }
}
