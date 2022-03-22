package com.android.systemui.doze;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent;
import com.android.systemui.util.NotificationChannels;
import com.android.wm.shell.pip.PipBoundsState;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozeAuthRemover_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider keyguardUpdateMonitorProvider;

    public /* synthetic */ DozeAuthRemover_Factory(Provider provider, int i) {
        this.$r8$classId = i;
        this.keyguardUpdateMonitorProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new DozeAuthRemover((KeyguardUpdateMonitor) this.keyguardUpdateMonitorProvider.mo144get());
            case 1:
                DevicePolicyManager devicePolicyManager = (DevicePolicyManager) ((Context) this.keyguardUpdateMonitorProvider.mo144get()).getSystemService(DevicePolicyManager.class);
                Objects.requireNonNull(devicePolicyManager, "Cannot return null from a non-@Nullable @Provides method");
                return devicePolicyManager;
            case 2:
                SectionHeaderController headerController = ((SectionHeaderControllerSubcomponent) this.keyguardUpdateMonitorProvider.mo144get()).getHeaderController();
                Objects.requireNonNull(headerController, "Cannot return null from a non-@Nullable @Provides method");
                return headerController;
            case 3:
                return new NotificationChannels((Context) this.keyguardUpdateMonitorProvider.mo144get());
            default:
                return new PipBoundsState((Context) this.keyguardUpdateMonitorProvider.mo144get());
        }
    }
}
