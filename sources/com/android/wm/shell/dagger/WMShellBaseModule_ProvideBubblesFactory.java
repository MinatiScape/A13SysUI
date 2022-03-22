package com.android.wm.shell.dagger;

import android.app.AlarmManager;
import android.content.Context;
import android.view.View;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger;
import com.android.systemui.statusbar.phone.StatusIconContainer;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WMShellBaseModule_ProvideBubblesFactory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider bubbleControllerProvider;

    public /* synthetic */ WMShellBaseModule_ProvideBubblesFactory(Provider provider, int i) {
        this.$r8$classId = i;
        this.bubbleControllerProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                Optional map = ((Optional) this.bubbleControllerProvider.mo144get()).map(WMShellBaseModule$$ExternalSyntheticLambda3.INSTANCE);
                Objects.requireNonNull(map, "Cannot return null from a non-@Nullable @Provides method");
                return map;
            case 1:
                AlarmManager alarmManager = (AlarmManager) ((Context) this.bubbleControllerProvider.mo144get()).getSystemService(AlarmManager.class);
                Objects.requireNonNull(alarmManager, "Cannot return null from a non-@Nullable @Provides method");
                return alarmManager;
            case 2:
                return new NotificationSectionsLogger((LogBuffer) this.bubbleControllerProvider.mo144get());
            default:
                StatusIconContainer statusIconContainer = (StatusIconContainer) ((View) this.bubbleControllerProvider.mo144get()).findViewById(2131428922);
                Objects.requireNonNull(statusIconContainer, "Cannot return null from a non-@Nullable @Provides method");
                return statusIconContainer;
        }
    }
}
