package com.android.wm.shell.dagger;

import android.content.Context;
import android.provider.Settings;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManagerImpl;
import com.android.systemui.util.settings.SecureSettings;
import com.android.wm.shell.RootTaskDisplayAreaOrganizer;
import com.android.wm.shell.unfold.UnfoldBackgroundController;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WMShellModule_ProvideUnfoldBackgroundControllerFactory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider contextProvider;
    public final Provider rootTaskDisplayAreaOrganizerProvider;

    public /* synthetic */ WMShellModule_ProvideUnfoldBackgroundControllerFactory(Provider provider, Provider provider2, int i) {
        this.$r8$classId = i;
        this.rootTaskDisplayAreaOrganizerProvider = provider;
        this.contextProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        int i;
        Object obj;
        switch (this.$r8$classId) {
            case 0:
                return new UnfoldBackgroundController((Context) this.contextProvider.mo144get(), (RootTaskDisplayAreaOrganizer) this.rootTaskDisplayAreaOrganizerProvider.mo144get());
            case 1:
                Lazy lazy = DoubleCheck.lazy(this.contextProvider);
                if (((NotifPipelineFlags) this.rootTaskDisplayAreaOrganizerProvider.mo144get()).isNewPipelineEnabled()) {
                    obj = new GroupMembershipManagerImpl();
                } else {
                    obj = (GroupMembershipManager) lazy.get();
                }
                Objects.requireNonNull(obj, "Cannot return null from a non-@Nullable @Provides method");
                return obj;
            default:
                try {
                    i = ((SecureSettings) this.rootTaskDisplayAreaOrganizerProvider.mo144get()).getIntForUser(((Integer) this.contextProvider.mo144get()).intValue());
                } catch (Settings.SettingNotFoundException unused) {
                    i = 0;
                }
                return Integer.valueOf(i);
        }
    }
}
