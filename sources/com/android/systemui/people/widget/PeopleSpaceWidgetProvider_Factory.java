package com.android.systemui.people.widget;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import com.android.keyguard.LockIconView;
import com.android.systemui.statusbar.notification.collection.NotifInflaterImpl;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.row.NotifInflationErrorManager;
import com.android.systemui.statusbar.notification.row.NotifRemoteViewCacheImpl;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import com.android.systemui.statusbar.policy.DeviceProvisionedControllerImpl;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PeopleSpaceWidgetProvider_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider peopleSpaceWidgetManagerProvider;

    public /* synthetic */ PeopleSpaceWidgetProvider_Factory(Provider provider, int i) {
        this.$r8$classId = i;
        this.peopleSpaceWidgetManagerProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new PeopleSpaceWidgetProvider((PeopleSpaceWidgetManager) this.peopleSpaceWidgetManagerProvider.mo144get());
            case 1:
                DeviceProvisionedControllerImpl deviceProvisionedControllerImpl = (DeviceProvisionedControllerImpl) this.peopleSpaceWidgetManagerProvider.mo144get();
                deviceProvisionedControllerImpl.init();
                return deviceProvisionedControllerImpl;
            case 2:
                Lifecycle lifecycle = ((LifecycleOwner) this.peopleSpaceWidgetManagerProvider.mo144get()).getLifecycle();
                Objects.requireNonNull(lifecycle, "Cannot return null from a non-@Nullable @Provides method");
                return lifecycle;
            case 3:
                return new NotifInflaterImpl((NotifInflationErrorManager) this.peopleSpaceWidgetManagerProvider.mo144get());
            case 4:
                return new NotifRemoteViewCacheImpl((CommonNotifCollection) this.peopleSpaceWidgetManagerProvider.mo144get());
            default:
                LockIconView lockIconView = (LockIconView) ((NotificationShadeWindowView) this.peopleSpaceWidgetManagerProvider.mo144get()).findViewById(2131428276);
                Objects.requireNonNull(lockIconView, "Cannot return null from a non-@Nullable @Provides method");
                return lockIconView;
        }
    }
}
