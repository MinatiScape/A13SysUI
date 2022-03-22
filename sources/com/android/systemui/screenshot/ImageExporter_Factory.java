package com.android.systemui.screenshot;

import android.content.ContentResolver;
import android.content.Context;
import android.permission.PermissionManager;
import com.android.keyguard.clock.ClockManager;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.statusbar.notification.collection.coalescer.GroupCoalescerLogger;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherFeatureController;
import com.android.systemui.statusbar.policy.HeadsUpManagerLogger;
import com.android.wm.shell.RootDisplayAreaOrganizer;
import com.android.wm.shell.common.ShellExecutor;
import dagger.internal.Factory;
import java.util.ArrayList;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ImageExporter_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider resolverProvider;

    public /* synthetic */ ImageExporter_Factory(Provider provider, int i) {
        this.$r8$classId = i;
        this.resolverProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new ImageExporter((ContentResolver) this.resolverProvider.mo144get());
            case 1:
                ClockManager clockManager = (ClockManager) this.resolverProvider.mo144get();
                Objects.requireNonNull(clockManager);
                ClockManager.AvailableClocks availableClocks = clockManager.mPreviewClocks;
                Objects.requireNonNull(availableClocks);
                ArrayList arrayList = availableClocks.mClockInfo;
                Objects.requireNonNull(arrayList, "Cannot return null from a non-@Nullable @Provides method");
                return arrayList;
            case 2:
                PermissionManager permissionManager = (PermissionManager) ((Context) this.resolverProvider.mo144get()).getSystemService(PermissionManager.class);
                if (permissionManager != null) {
                    permissionManager.initializeUsageHelper();
                }
                Objects.requireNonNull(permissionManager, "Cannot return null from a non-@Nullable @Provides method");
                return permissionManager;
            case 3:
                return new GroupCoalescerLogger((LogBuffer) this.resolverProvider.mo144get());
            case 4:
                return new StatusBarUserSwitcherFeatureController((FeatureFlags) this.resolverProvider.mo144get());
            case 5:
                return new HeadsUpManagerLogger((LogBuffer) this.resolverProvider.mo144get());
            default:
                return new RootDisplayAreaOrganizer((ShellExecutor) this.resolverProvider.mo144get());
        }
    }
}
