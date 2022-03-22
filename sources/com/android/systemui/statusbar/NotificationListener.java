package com.android.systemui.statusbar;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda12;
import com.android.systemui.plugins.NotificationListenerController;
import com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda0;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.phone.NotificationListenerWithPlugins;
import com.android.systemui.util.time.SystemClock;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda19;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executor;
@SuppressLint({"OverrideAbstract"})
/* loaded from: classes.dex */
public final class NotificationListener extends NotificationListenerWithPlugins {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final Context mContext;
    public final Executor mMainExecutor;
    public final NotificationManager mNotificationManager;
    public final SystemClock mSystemClock;
    public final ArrayList mNotificationHandlers = new ArrayList();
    public final ArrayList<NotificationSettingsListener> mSettingsListeners = new ArrayList<>();
    public final ConcurrentLinkedDeque mRankingMapQueue = new ConcurrentLinkedDeque();
    public final BubbleStackView$$ExternalSyntheticLambda19 mDispatchRankingUpdateRunnable = new BubbleStackView$$ExternalSyntheticLambda19(this, 4);
    public long mSkippingRankingUpdatesSince = -1;

    /* loaded from: classes.dex */
    public interface NotificationHandler {
        default void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
        }

        void onNotificationPosted(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap);

        void onNotificationRankingUpdate(NotificationListenerService.RankingMap rankingMap);

        void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap, int i);

        void onNotificationsInitialized();
    }

    /* loaded from: classes.dex */
    public interface NotificationSettingsListener {
        default void onStatusBarIconsBehaviorChanged(boolean z) {
        }
    }

    @Override // android.service.notification.NotificationListenerService
    public final void onListenerConnected() {
        this.mConnected = true;
        this.mPlugins.forEach(new NavigationBar$$ExternalSyntheticLambda12(this, 1));
        final StatusBarNotification[] activeNotifications = getActiveNotifications();
        if (activeNotifications == null) {
            Log.w("NotificationListener", "onListenerConnected unable to get active notifications.");
            return;
        }
        final NotificationListenerService.RankingMap currentRanking = getCurrentRanking();
        this.mMainExecutor.execute(new Runnable() { // from class: com.android.systemui.statusbar.NotificationListener$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                NotificationListener notificationListener = NotificationListener.this;
                StatusBarNotification[] statusBarNotificationArr = activeNotifications;
                NotificationListenerService.RankingMap rankingMap = currentRanking;
                Objects.requireNonNull(notificationListener);
                ArrayList arrayList = new ArrayList();
                for (StatusBarNotification statusBarNotification : statusBarNotificationArr) {
                    String key = statusBarNotification.getKey();
                    NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
                    if (!rankingMap.getRanking(key, ranking)) {
                        ranking.populate(key, 0, false, 0, 0, 0, null, null, null, new ArrayList(), new ArrayList(), false, 0, false, 0L, false, new ArrayList(), new ArrayList(), false, false, false, null, 0, false);
                    }
                    arrayList.add(ranking);
                }
                NotificationListenerService.RankingMap rankingMap2 = new NotificationListenerService.RankingMap((NotificationListenerService.Ranking[]) arrayList.toArray(new NotificationListenerService.Ranking[0]));
                for (StatusBarNotification statusBarNotification2 : statusBarNotificationArr) {
                    Iterator it = notificationListener.mNotificationHandlers.iterator();
                    while (it.hasNext()) {
                        ((NotificationListener.NotificationHandler) it.next()).onNotificationPosted(statusBarNotification2, rankingMap2);
                    }
                }
                Iterator it2 = notificationListener.mNotificationHandlers.iterator();
                while (it2.hasNext()) {
                    ((NotificationListener.NotificationHandler) it2.next()).onNotificationsInitialized();
                }
            }
        });
        onSilentStatusBarIconsVisibilityChanged(this.mNotificationManager.shouldHideSilentStatusBarIcons());
    }

    @Override // android.service.notification.NotificationListenerService
    public final void onNotificationRemoved(final StatusBarNotification statusBarNotification, final NotificationListenerService.RankingMap rankingMap, final int i) {
        boolean z;
        if (statusBarNotification != null) {
            Iterator<NotificationListenerController> it = this.mPlugins.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (it.next().onNotificationRemoved(statusBarNotification, rankingMap)) {
                        z = true;
                        break;
                    }
                } else {
                    z = false;
                    break;
                }
            }
            if (!z) {
                this.mMainExecutor.execute(new Runnable() { // from class: com.android.systemui.statusbar.NotificationListener$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        NotificationListener notificationListener = NotificationListener.this;
                        StatusBarNotification statusBarNotification2 = statusBarNotification;
                        NotificationListenerService.RankingMap rankingMap2 = rankingMap;
                        int i2 = i;
                        Objects.requireNonNull(notificationListener);
                        Iterator it2 = notificationListener.mNotificationHandlers.iterator();
                        while (it2.hasNext()) {
                            ((NotificationListener.NotificationHandler) it2.next()).onNotificationRemoved(statusBarNotification2, rankingMap2, i2);
                        }
                    }
                });
            }
        }
    }

    public final void addNotificationHandler(NotificationHandler notificationHandler) {
        if (!this.mNotificationHandlers.contains(notificationHandler)) {
            this.mNotificationHandlers.add(notificationHandler);
            return;
        }
        throw new IllegalArgumentException("Listener is already added");
    }

    @Override // android.service.notification.NotificationListenerService
    public final void onNotificationChannelModified(final String str, final UserHandle userHandle, final NotificationChannel notificationChannel, final int i) {
        boolean z;
        Iterator<NotificationListenerController> it = this.mPlugins.iterator();
        while (true) {
            if (it.hasNext()) {
                if (it.next().onNotificationChannelModified(str, userHandle, notificationChannel, i)) {
                    z = true;
                    break;
                }
            } else {
                z = false;
                break;
            }
        }
        if (!z) {
            this.mMainExecutor.execute(new Runnable() { // from class: com.android.systemui.statusbar.NotificationListener$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationListener notificationListener = NotificationListener.this;
                    String str2 = str;
                    UserHandle userHandle2 = userHandle;
                    NotificationChannel notificationChannel2 = notificationChannel;
                    int i2 = i;
                    Objects.requireNonNull(notificationListener);
                    Iterator it2 = notificationListener.mNotificationHandlers.iterator();
                    while (it2.hasNext()) {
                        ((NotificationListener.NotificationHandler) it2.next()).onNotificationChannelModified(str2, userHandle2, notificationChannel2, i2);
                    }
                }
            });
        }
    }

    @Override // android.service.notification.NotificationListenerService
    public final void onNotificationPosted(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
        boolean z;
        if (statusBarNotification != null) {
            Iterator<NotificationListenerController> it = this.mPlugins.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (it.next().onNotificationPosted(statusBarNotification, rankingMap)) {
                        z = true;
                        break;
                    }
                } else {
                    z = false;
                    break;
                }
            }
            if (!z) {
                this.mMainExecutor.execute(new OverviewProxyService$1$$ExternalSyntheticLambda0(this, statusBarNotification, rankingMap, 1));
            }
        }
    }

    @Override // android.service.notification.NotificationListenerService
    public final void onNotificationRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
        if (rankingMap != null) {
            Iterator<NotificationListenerController> it = this.mPlugins.iterator();
            while (it.hasNext()) {
                rankingMap = it.next().getCurrentRanking(rankingMap);
            }
            this.mRankingMapQueue.addLast(rankingMap);
            this.mMainExecutor.execute(this.mDispatchRankingUpdateRunnable);
        }
    }

    @Override // android.service.notification.NotificationListenerService
    public final void onSilentStatusBarIconsVisibilityChanged(boolean z) {
        Iterator<NotificationSettingsListener> it = this.mSettingsListeners.iterator();
        while (it.hasNext()) {
            it.next().onStatusBarIconsBehaviorChanged(z);
        }
    }

    public final void registerAsSystemService() {
        try {
            registerAsSystemService(this.mContext, new ComponentName(this.mContext.getPackageName(), NotificationListener.class.getCanonicalName()), -1);
        } catch (RemoteException e) {
            Log.e("NotificationListener", "Unable to register notification listener", e);
        }
    }

    public NotificationListener(Context context, NotificationManager notificationManager, SystemClock systemClock, Executor executor, PluginManager pluginManager) {
        super(pluginManager);
        this.mContext = context;
        this.mNotificationManager = notificationManager;
        this.mSystemClock = systemClock;
        this.mMainExecutor = executor;
    }

    @Override // android.service.notification.NotificationListenerService
    public final void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
        onNotificationRemoved(statusBarNotification, rankingMap, 0);
    }
}
