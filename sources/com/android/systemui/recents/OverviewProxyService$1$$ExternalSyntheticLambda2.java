package com.android.systemui.recents;

import android.os.Binder;
import com.android.systemui.accessibility.MagnificationModeSwitch;
import com.android.systemui.accessibility.ModeSwitchesController;
import com.android.systemui.accessibility.WindowMagnificationConnectionImpl;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.statusbar.phone.StatusBar;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class OverviewProxyService$1$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Binder f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ OverviewProxyService$1$$ExternalSyntheticLambda2(Binder binder, int i, int i2) {
        this.$r8$classId = i2;
        this.f$0 = binder;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                OverviewProxyService.AnonymousClass1 r0 = (OverviewProxyService.AnonymousClass1) this.f$0;
                final int i = this.f$1;
                Objects.requireNonNull(r0);
                OverviewProxyService.this.mStatusBarOptionalLazy.get().ifPresent(new Consumer() { // from class: com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda13
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ((StatusBar) obj).showScreenPinningRequest(i, false);
                    }
                });
                return;
            default:
                WindowMagnificationConnectionImpl windowMagnificationConnectionImpl = (WindowMagnificationConnectionImpl) this.f$0;
                int i2 = this.f$1;
                int i3 = WindowMagnificationConnectionImpl.$r8$clinit;
                Objects.requireNonNull(windowMagnificationConnectionImpl);
                ModeSwitchesController modeSwitchesController = windowMagnificationConnectionImpl.mModeSwitchesController;
                Objects.requireNonNull(modeSwitchesController);
                MagnificationModeSwitch magnificationModeSwitch = modeSwitchesController.mSwitchSupplier.get(i2);
                if (magnificationModeSwitch != null) {
                    magnificationModeSwitch.removeButton();
                    return;
                }
                return;
        }
    }
}
