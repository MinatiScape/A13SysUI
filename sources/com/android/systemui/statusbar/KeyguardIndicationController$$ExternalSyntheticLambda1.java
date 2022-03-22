package com.android.systemui.statusbar;

import android.util.Slog;
import com.android.wm.shell.common.DisplayController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class KeyguardIndicationController$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ KeyguardIndicationController$$ExternalSyntheticLambda1(Object obj, int i, int i2) {
        this.$r8$classId = i2;
        this.f$0 = obj;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        String str;
        switch (this.$r8$classId) {
            case 0:
                KeyguardIndicationController keyguardIndicationController = (KeyguardIndicationController) this.f$0;
                int i = this.f$1;
                Objects.requireNonNull(keyguardIndicationController);
                if (i == 1) {
                    str = keyguardIndicationController.mContext.getResources().getString(2131952294);
                } else if (i == 2) {
                    str = keyguardIndicationController.mContext.getResources().getString(2131952293);
                } else {
                    str = "";
                }
                if (!str.equals(keyguardIndicationController.mAlignmentIndication)) {
                    keyguardIndicationController.mAlignmentIndication = str;
                    keyguardIndicationController.updateIndication(false);
                    return;
                }
                return;
            default:
                DisplayController.DisplayWindowListenerImpl displayWindowListenerImpl = (DisplayController.DisplayWindowListenerImpl) this.f$0;
                int i2 = this.f$1;
                int i3 = DisplayController.DisplayWindowListenerImpl.$r8$clinit;
                Objects.requireNonNull(displayWindowListenerImpl);
                DisplayController displayController = DisplayController.this;
                Objects.requireNonNull(displayController);
                synchronized (displayController.mDisplays) {
                    if (!(displayController.mDisplays.get(i2) == null || displayController.getDisplay(i2) == null)) {
                        int size = displayController.mDisplayChangedListeners.size();
                        while (true) {
                            size--;
                            if (size >= 0) {
                                displayController.mDisplayChangedListeners.get(size).onFixedRotationFinished();
                            } else {
                                return;
                            }
                        }
                    }
                    Slog.w("DisplayController", "Skipping onFixedRotationFinished on unknown display, displayId=" + i2);
                    return;
                }
        }
    }
}
