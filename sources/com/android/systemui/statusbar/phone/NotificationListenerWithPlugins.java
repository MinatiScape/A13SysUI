package com.android.systemui.statusbar.phone;

import android.content.ComponentName;
import android.content.Context;
import android.os.RemoteException;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import com.android.systemui.plugins.NotificationListenerController;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.shared.plugins.PluginManager;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes.dex */
public class NotificationListenerWithPlugins extends NotificationListenerService implements PluginListener<NotificationListenerController> {
    public static final /* synthetic */ int $r8$clinit = 0;
    public boolean mConnected;
    public PluginManager mPluginManager;
    public ArrayList<NotificationListenerController> mPlugins = new ArrayList<>();

    /* renamed from: com.android.systemui.statusbar.phone.NotificationListenerWithPlugins$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass1 implements NotificationListenerController.NotificationProvider {
        public AnonymousClass1() {
        }

        @Override // com.android.systemui.plugins.NotificationListenerController.NotificationProvider
        public final void addNotification(StatusBarNotification statusBarNotification) {
            NotificationListenerWithPlugins.this.onNotificationPosted(statusBarNotification, getRankingMap());
        }

        @Override // com.android.systemui.plugins.NotificationListenerController.NotificationProvider
        public final StatusBarNotification[] getActiveNotifications() {
            return NotificationListenerWithPlugins.super.getActiveNotifications();
        }

        @Override // com.android.systemui.plugins.NotificationListenerController.NotificationProvider
        public final NotificationListenerService.RankingMap getRankingMap() {
            return NotificationListenerWithPlugins.super.getCurrentRanking();
        }

        @Override // com.android.systemui.plugins.NotificationListenerController.NotificationProvider
        public final void removeNotification(StatusBarNotification statusBarNotification) {
            NotificationListenerWithPlugins.this.onNotificationRemoved(statusBarNotification, getRankingMap());
        }

        @Override // com.android.systemui.plugins.NotificationListenerController.NotificationProvider
        public final void updateRanking() {
            NotificationListenerWithPlugins.this.onNotificationRankingUpdate(getRankingMap());
        }
    }

    @Override // com.android.systemui.plugins.PluginListener
    public final void onPluginConnected(NotificationListenerController notificationListenerController, Context context) {
        NotificationListenerController notificationListenerController2 = notificationListenerController;
        this.mPlugins.add(notificationListenerController2);
        if (this.mConnected) {
            notificationListenerController2.onListenerConnected(new AnonymousClass1());
        }
    }

    @Override // com.android.systemui.plugins.PluginListener
    public final void onPluginDisconnected(NotificationListenerController notificationListenerController) {
        this.mPlugins.remove(notificationListenerController);
    }

    public NotificationListenerWithPlugins(PluginManager pluginManager) {
        this.mPluginManager = pluginManager;
    }

    @Override // android.service.notification.NotificationListenerService
    public final StatusBarNotification[] getActiveNotifications() {
        StatusBarNotification[] activeNotifications = super.getActiveNotifications();
        Iterator<NotificationListenerController> it = this.mPlugins.iterator();
        while (it.hasNext()) {
            activeNotifications = it.next().getActiveNotifications(activeNotifications);
        }
        return activeNotifications;
    }

    @Override // android.service.notification.NotificationListenerService
    public final NotificationListenerService.RankingMap getCurrentRanking() {
        NotificationListenerService.RankingMap currentRanking = super.getCurrentRanking();
        Iterator<NotificationListenerController> it = this.mPlugins.iterator();
        while (it.hasNext()) {
            currentRanking = it.next().getCurrentRanking(currentRanking);
        }
        return currentRanking;
    }

    public final void registerAsSystemService(Context context, ComponentName componentName, int i) throws RemoteException {
        super.registerAsSystemService(context, componentName, i);
        this.mPluginManager.addPluginListener(this, NotificationListenerController.class);
    }

    public final void unregisterAsSystemService() throws RemoteException {
        super.unregisterAsSystemService();
        this.mPluginManager.removePluginListener(this);
    }
}
