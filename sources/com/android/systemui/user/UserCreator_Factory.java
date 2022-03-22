package com.android.systemui.user;

import android.content.Context;
import android.os.UserManager;
import android.view.WindowManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import com.google.android.systemui.gamedashboard.FpsController;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class UserCreator_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider contextProvider;
    public final Provider userManagerProvider;

    public /* synthetic */ UserCreator_Factory(Provider provider, Provider provider2, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.userManagerProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new UserCreator((Context) this.contextProvider.mo144get(), (UserManager) this.userManagerProvider.mo144get());
            case 1:
                return new RecordingController((BroadcastDispatcher) this.contextProvider.mo144get(), (UserContextProvider) this.userManagerProvider.mo144get());
            case 2:
                return new HighPriorityProvider((PeopleNotificationIdentifier) this.contextProvider.mo144get(), (GroupMembershipManager) this.userManagerProvider.mo144get());
            default:
                return new FpsController((Executor) this.contextProvider.mo144get(), (WindowManager) this.userManagerProvider.mo144get());
        }
    }
}
