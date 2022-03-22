package com.google.android.systemui.statusbar.phone;

import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.google.android.collect.Sets;
import java.util.HashSet;
import java.util.Objects;
/* loaded from: classes.dex */
public final class WallpaperNotifier {
    public final Context mContext;
    public final CommonNotifCollection mNotifCollection;
    public boolean mShouldBroadcastNotifications;
    public final AnonymousClass1 mUserTracker;
    public String mWallpaperPackage;
    public static final String[] NOTIFYABLE_WALLPAPERS = {"com.breel.wallpapers.imprint", "com.breel.wallpapers18.tactile", "com.breel.wallpapers18.delight", "com.breel.wallpapers18.miniman", "com.google.pixel.livewallpaper.imprint", "com.google.pixel.livewallpaper.tactile", "com.google.pixel.livewallpaper.delight", "com.google.pixel.livewallpaper.miniman"};
    public static final HashSet<String> NOTIFYABLE_PACKAGES = Sets.newHashSet(new String[]{"com.breel.wallpapers", "com.breel.wallpapers18", "com.google.pixel.livewallpaper"});
    public final AnonymousClass2 mNotifListener = new NotifCollectionListener() { // from class: com.google.android.systemui.statusbar.phone.WallpaperNotifier.2
        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public final void onEntryAdded(NotificationEntry notificationEntry) {
            boolean z;
            if (getCurrentUserId() == 0) {
                z = true;
            } else {
                z = false;
            }
            if (WallpaperNotifier.this.mShouldBroadcastNotifications && z) {
                Intent intent = new Intent();
                intent.setPackage(WallpaperNotifier.this.mWallpaperPackage);
                intent.setAction("com.breel.wallpapers.NOTIFICATION_RECEIVED");
                Objects.requireNonNull(notificationEntry);
                intent.putExtra("notification_color", notificationEntry.mSbn.getNotification().color);
                WallpaperNotifier.this.mContext.sendBroadcast(intent, "com.breel.wallpapers.notifications");
            }
        }
    };
    public AnonymousClass3 mWallpaperChangedReceiver = new BroadcastReceiver() { // from class: com.google.android.systemui.statusbar.phone.WallpaperNotifier.3
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.WALLPAPER_CHANGED")) {
                WallpaperNotifier.this.checkNotificationBroadcastSupport();
            }
        }
    };

    public final void checkNotificationBroadcastSupport() {
        WallpaperInfo wallpaperInfo;
        this.mShouldBroadcastNotifications = false;
        WallpaperManager wallpaperManager = (WallpaperManager) this.mContext.getSystemService(WallpaperManager.class);
        if (!(wallpaperManager == null || (wallpaperInfo = wallpaperManager.getWallpaperInfo()) == null)) {
            ComponentName component = wallpaperInfo.getComponent();
            String packageName = component.getPackageName();
            if (NOTIFYABLE_PACKAGES.contains(packageName)) {
                this.mWallpaperPackage = packageName;
                String className = component.getClassName();
                String[] strArr = NOTIFYABLE_WALLPAPERS;
                for (int i = 0; i < 8; i++) {
                    if (className.startsWith(strArr[i])) {
                        this.mShouldBroadcastNotifications = true;
                        return;
                    }
                }
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.google.android.systemui.statusbar.phone.WallpaperNotifier$2] */
    /* JADX WARN: Type inference failed for: r0v1, types: [com.google.android.systemui.statusbar.phone.WallpaperNotifier$3] */
    /* JADX WARN: Type inference failed for: r2v1, types: [com.google.android.systemui.statusbar.phone.WallpaperNotifier$1] */
    public WallpaperNotifier(Context context, CommonNotifCollection commonNotifCollection, BroadcastDispatcher broadcastDispatcher) {
        this.mContext = context;
        this.mNotifCollection = commonNotifCollection;
        this.mUserTracker = new CurrentUserTracker(broadcastDispatcher) { // from class: com.google.android.systemui.statusbar.phone.WallpaperNotifier.1
            @Override // com.android.systemui.settings.CurrentUserTracker
            public final void onUserSwitched(int i) {
            }
        };
    }
}
