package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.util.DisplayMetrics;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.dagger.GlobalModule;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.log.LogBuffer;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SystemUIDialogManager_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider dumpManagerProvider;
    public final Object statusBarKeyguardViewManagerProvider;

    public /* synthetic */ SystemUIDialogManager_Factory(Provider provider, Provider provider2, int i) {
        this.$r8$classId = i;
        this.dumpManagerProvider = provider;
        this.statusBarKeyguardViewManagerProvider = provider2;
    }

    public SystemUIDialogManager_Factory(GlobalModule globalModule, Provider provider) {
        this.$r8$classId = 2;
        this.statusBarKeyguardViewManagerProvider = globalModule;
        this.dumpManagerProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new SystemUIDialogManager((DumpManager) this.dumpManagerProvider.mo144get(), (StatusBarKeyguardViewManager) ((Provider) this.statusBarKeyguardViewManagerProvider).mo144get());
            case 1:
                return new NotificationVoiceReplyLogger((LogBuffer) this.dumpManagerProvider.mo144get(), (UiEventLogger) ((Provider) this.statusBarKeyguardViewManagerProvider).mo144get());
            default:
                Objects.requireNonNull((GlobalModule) this.statusBarKeyguardViewManagerProvider);
                DisplayMetrics displayMetrics = new DisplayMetrics();
                ((Context) this.dumpManagerProvider.mo144get()).getDisplay().getMetrics(displayMetrics);
                return displayMetrics;
        }
    }
}
