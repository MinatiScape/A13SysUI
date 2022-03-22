package com.android.systemui.statusbar.notification.collection.inflation;

import android.content.Context;
import com.android.internal.util.NotificationMessagingUtil;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.collection.legacy.LowPriorityInflationHelper;
import com.android.systemui.statusbar.notification.icon.IconManager;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
import com.android.systemui.statusbar.notification.row.RowInflaterTask;
import com.android.systemui.statusbar.notification.row.RowInflaterTask_Factory;
import com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationRowBinderImpl_Factory implements Factory<NotificationRowBinderImpl> {
    public final Provider<Context> contextProvider;
    public final Provider<ExpandableNotificationRowComponent.Builder> expandableNotificationRowComponentBuilderProvider;
    public final Provider<IconManager> iconManagerProvider;
    public final Provider<LowPriorityInflationHelper> lowPriorityInflationHelperProvider;
    public final Provider<NotifBindPipeline> notifBindPipelineProvider;
    public final Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
    public final Provider<NotificationLockscreenUserManager> notificationLockscreenUserManagerProvider;
    public final Provider<NotificationMessagingUtil> notificationMessagingUtilProvider;
    public final Provider<NotificationRemoteInputManager> notificationRemoteInputManagerProvider;
    public final Provider<RowContentBindStage> rowContentBindStageProvider;
    public final Provider<RowInflaterTask> rowInflaterTaskProvider;

    public NotificationRowBinderImpl_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10) {
        RowInflaterTask_Factory rowInflaterTask_Factory = RowInflaterTask_Factory.InstanceHolder.INSTANCE;
        this.contextProvider = provider;
        this.notificationMessagingUtilProvider = provider2;
        this.notificationRemoteInputManagerProvider = provider3;
        this.notificationLockscreenUserManagerProvider = provider4;
        this.notifBindPipelineProvider = provider5;
        this.rowContentBindStageProvider = provider6;
        this.rowInflaterTaskProvider = rowInflaterTask_Factory;
        this.expandableNotificationRowComponentBuilderProvider = provider7;
        this.iconManagerProvider = provider8;
        this.lowPriorityInflationHelperProvider = provider9;
        this.notifPipelineFlagsProvider = provider10;
    }

    public static NotificationRowBinderImpl_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10) {
        return new NotificationRowBinderImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new NotificationRowBinderImpl(this.contextProvider.mo144get(), this.notificationMessagingUtilProvider.mo144get(), this.notificationRemoteInputManagerProvider.mo144get(), this.notificationLockscreenUserManagerProvider.mo144get(), this.notifBindPipelineProvider.mo144get(), this.rowContentBindStageProvider.mo144get(), this.rowInflaterTaskProvider, this.expandableNotificationRowComponentBuilderProvider.mo144get(), this.iconManagerProvider.mo144get(), this.lowPriorityInflationHelperProvider.mo144get(), this.notifPipelineFlagsProvider.mo144get());
    }
}
