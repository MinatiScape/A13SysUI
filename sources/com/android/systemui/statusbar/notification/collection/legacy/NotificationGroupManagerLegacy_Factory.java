package com.android.systemui.statusbar.notification.collection.legacy;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import com.android.wm.shell.bubbles.Bubbles;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationGroupManagerLegacy_Factory implements Factory<NotificationGroupManagerLegacy> {
    public final Provider<Optional<Bubbles>> bubblesOptionalProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<PeopleNotificationIdentifier> peopleNotificationIdentifierProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new NotificationGroupManagerLegacy(this.statusBarStateControllerProvider.mo144get(), DoubleCheck.lazy(this.peopleNotificationIdentifierProvider), this.bubblesOptionalProvider.mo144get(), this.dumpManagerProvider.mo144get());
    }

    public NotificationGroupManagerLegacy_Factory(Provider<StatusBarStateController> provider, Provider<PeopleNotificationIdentifier> provider2, Provider<Optional<Bubbles>> provider3, Provider<DumpManager> provider4) {
        this.statusBarStateControllerProvider = provider;
        this.peopleNotificationIdentifierProvider = provider2;
        this.bubblesOptionalProvider = provider3;
        this.dumpManagerProvider = provider4;
    }
}
