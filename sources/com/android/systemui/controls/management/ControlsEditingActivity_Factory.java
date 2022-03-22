package com.android.systemui.controls.management;

import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.CustomIconCache;
import com.android.systemui.controls.controller.ControlsControllerImpl;
import com.android.systemui.controls.ui.ControlsUiController;
import com.google.android.systemui.columbus.gates.ChargingState;
import com.google.android.systemui.columbus.gates.ScreenTouch;
import com.google.android.systemui.columbus.gates.SystemKeyPress;
import com.google.android.systemui.columbus.gates.UsbState;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Set;
import javax.inject.Provider;
import kotlin.collections.SetsKt__SetsKt;
/* loaded from: classes.dex */
public final class ControlsEditingActivity_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider broadcastDispatcherProvider;
    public final Provider controllerProvider;
    public final Provider customIconCacheProvider;
    public final Provider uiControllerProvider;

    public /* synthetic */ ControlsEditingActivity_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, int i) {
        this.$r8$classId = i;
        this.controllerProvider = provider;
        this.broadcastDispatcherProvider = provider2;
        this.customIconCacheProvider = provider3;
        this.uiControllerProvider = provider4;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new ControlsEditingActivity((ControlsControllerImpl) this.controllerProvider.mo144get(), (BroadcastDispatcher) this.broadcastDispatcherProvider.mo144get(), (CustomIconCache) this.customIconCacheProvider.mo144get(), (ControlsUiController) this.uiControllerProvider.mo144get());
            default:
                Set of = SetsKt__SetsKt.setOf((ChargingState) this.controllerProvider.mo144get(), (UsbState) this.broadcastDispatcherProvider.mo144get(), (SystemKeyPress) this.customIconCacheProvider.mo144get(), (ScreenTouch) this.uiControllerProvider.mo144get());
                Objects.requireNonNull(of, "Cannot return null from a non-@Nullable @Provides method");
                return of;
        }
    }
}
