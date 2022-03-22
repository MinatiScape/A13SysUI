package com.android.wm.shell.dagger;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptLogger;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelperController;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider mainExecutorProvider;
    public final Provider taskOrganizerProvider;

    public /* synthetic */ WMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory(Provider provider, Provider provider2, int i) {
        this.$r8$classId = i;
        this.taskOrganizerProvider = provider;
        this.mainExecutorProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                Optional ofNullable = Optional.ofNullable(new TaskSurfaceHelperController((ShellTaskOrganizer) this.taskOrganizerProvider.mo144get(), (ShellExecutor) this.mainExecutorProvider.mo144get()));
                Objects.requireNonNull(ofNullable, "Cannot return null from a non-@Nullable @Provides method");
                return ofNullable;
            default:
                return new NotificationInterruptLogger((LogBuffer) this.taskOrganizerProvider.mo144get(), (LogBuffer) this.mainExecutorProvider.mo144get());
        }
    }
}
