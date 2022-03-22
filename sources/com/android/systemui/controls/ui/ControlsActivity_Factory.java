package com.android.systemui.controls.ui;

import android.content.Context;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.hdmi.HdmiCecSetMenuLanguageHelper;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRowDragController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.util.settings.SecureSettings;
import com.google.android.systemui.columbus.gates.PowerState;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ControlsActivity_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider broadcastDispatcherProvider;
    public final Provider uiControllerProvider;

    public /* synthetic */ ControlsActivity_Factory(Provider provider, Provider provider2, int i) {
        this.$r8$classId = i;
        this.uiControllerProvider = provider;
        this.broadcastDispatcherProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new ControlsActivity((ControlsUiController) this.uiControllerProvider.mo144get(), (BroadcastDispatcher) this.broadcastDispatcherProvider.mo144get());
            case 1:
                return new HdmiCecSetMenuLanguageHelper((Executor) this.uiControllerProvider.mo144get(), (SecureSettings) this.broadcastDispatcherProvider.mo144get());
            case 2:
                return new ExpandableNotificationRowDragController((Context) this.uiControllerProvider.mo144get(), (HeadsUpManager) this.broadcastDispatcherProvider.mo144get());
            default:
                return new PowerState((Context) this.uiControllerProvider.mo144get(), DoubleCheck.lazy(this.broadcastDispatcherProvider));
        }
    }
}
