package com.android.wm.shell.dagger;

import android.content.Context;
import android.hardware.display.ColorDisplayManager;
import android.os.Looper;
import android.telephony.SubscriptionManager;
import com.android.keyguard.KeyguardHostView;
import com.android.keyguard.KeyguardSecurityContainer;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import com.android.systemui.statusbar.notification.row.RowContentBindStageLogger;
import com.android.systemui.util.concurrency.ExecutorImpl;
import com.android.wifitrackerlib.WifiEntry$$ExternalSyntheticLambda3;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WMShellBaseModule_ProvideSplitScreenFactory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider splitScreenControllerProvider;

    public /* synthetic */ WMShellBaseModule_ProvideSplitScreenFactory(Provider provider, int i) {
        this.$r8$classId = i;
        this.splitScreenControllerProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                Optional map = ((Optional) this.splitScreenControllerProvider.mo144get()).map(WifiEntry$$ExternalSyntheticLambda3.INSTANCE$1);
                Objects.requireNonNull(map, "Cannot return null from a non-@Nullable @Provides method");
                return map;
            case 1:
                KeyguardSecurityContainer keyguardSecurityContainer = (KeyguardSecurityContainer) ((KeyguardHostView) this.splitScreenControllerProvider.mo144get()).findViewById(2131428188);
                Objects.requireNonNull(keyguardSecurityContainer, "Cannot return null from a non-@Nullable @Provides method");
                return keyguardSecurityContainer;
            case 2:
                SubscriptionManager subscriptionManager = (SubscriptionManager) ((Context) this.splitScreenControllerProvider.mo144get()).getSystemService(SubscriptionManager.class);
                Objects.requireNonNull(subscriptionManager, "Cannot return null from a non-@Nullable @Provides method");
                return subscriptionManager;
            case 3:
                return ((LogBufferFactory) this.splitScreenControllerProvider.mo144get()).create("NotifInteractionLog", 50);
            case 4:
                return Boolean.valueOf(ColorDisplayManager.isReduceBrightColorsAvailable((Context) this.splitScreenControllerProvider.mo144get()));
            case 5:
                return new RowContentBindStageLogger((LogBuffer) this.splitScreenControllerProvider.mo144get());
            default:
                return new ExecutorImpl((Looper) this.splitScreenControllerProvider.mo144get());
        }
    }
}
