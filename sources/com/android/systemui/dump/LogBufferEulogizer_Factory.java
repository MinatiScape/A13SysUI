package com.android.systemui.dump;

import android.content.Context;
import com.android.systemui.statusbar.NotificationShelf;
import com.android.systemui.statusbar.NotificationShelfController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationViewController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.util.io.Files;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LogBufferEulogizer_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider contextProvider;
    public final Provider dumpManagerProvider;
    public final Provider filesProvider;
    public final Provider systemClockProvider;

    public /* synthetic */ LogBufferEulogizer_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.dumpManagerProvider = provider2;
        this.systemClockProvider = provider3;
        this.filesProvider = provider4;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new LogBufferEulogizer((Context) this.contextProvider.mo144get(), (DumpManager) this.dumpManagerProvider.mo144get(), (SystemClock) this.systemClockProvider.mo144get(), (Files) this.filesProvider.mo144get());
            default:
                return new NotificationShelfController((NotificationShelf) this.contextProvider.mo144get(), (ActivatableNotificationViewController) this.dumpManagerProvider.mo144get(), (KeyguardBypassController) this.systemClockProvider.mo144get(), (SysuiStatusBarStateController) this.filesProvider.mo144get());
        }
    }
}
