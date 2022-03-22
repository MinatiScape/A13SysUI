package com.android.systemui;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import android.util.ArraySet;
import com.android.systemui.ForegroundServiceController;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ForegroundServiceNotificationListener {
    public final Context mContext;
    public final ForegroundServiceController mForegroundServiceController;

    /* renamed from: com.android.systemui.ForegroundServiceNotificationListener$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass3 implements ForegroundServiceController.UserStateUpdateCallback {
        public final /* synthetic */ StatusBarNotification val$sbn;

        public AnonymousClass3(StatusBarNotification statusBarNotification) {
            this.val$sbn = statusBarNotification;
        }

        @Override // com.android.systemui.ForegroundServiceController.UserStateUpdateCallback
        public final boolean updateUserState(ForegroundServicesUserState foregroundServicesUserState) {
            boolean z;
            ForegroundServiceController foregroundServiceController = ForegroundServiceNotificationListener.this.mForegroundServiceController;
            StatusBarNotification statusBarNotification = this.val$sbn;
            Objects.requireNonNull(foregroundServiceController);
            if (ForegroundServiceController.isDisclosureNotification(statusBarNotification)) {
                foregroundServicesUserState.mRunning = null;
                foregroundServicesUserState.mServiceStartTime = 0L;
                return true;
            }
            String packageName = this.val$sbn.getPackageName();
            String key = this.val$sbn.getKey();
            ArrayMap<String, ArraySet<String>> arrayMap = foregroundServicesUserState.mImportantNotifications;
            ArraySet<String> arraySet = arrayMap.get(packageName);
            boolean z2 = false;
            if (arraySet == null) {
                z = false;
            } else {
                z = arraySet.remove(key);
                if (arraySet.size() == 0) {
                    arrayMap.remove(packageName);
                }
            }
            boolean z3 = z | false;
            ArrayMap<String, ArraySet<String>> arrayMap2 = foregroundServicesUserState.mStandardLayoutNotifications;
            ArraySet<String> arraySet2 = arrayMap2.get(packageName);
            if (arraySet2 != null) {
                z2 = arraySet2.remove(key);
                if (arraySet2.size() == 0) {
                    arrayMap2.remove(packageName);
                }
            }
            return z2 | z3;
        }
    }

    public ForegroundServiceNotificationListener(Context context, ForegroundServiceController foregroundServiceController, NotificationEntryManager notificationEntryManager, NotifPipeline notifPipeline) {
        this.mContext = context;
        this.mForegroundServiceController = foregroundServiceController;
        notificationEntryManager.addNotificationEntryListener(new NotificationEntryListener() { // from class: com.android.systemui.ForegroundServiceNotificationListener.1
            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public final void onEntryRemoved(NotificationEntry notificationEntry, boolean z) {
                ForegroundServiceNotificationListener foregroundServiceNotificationListener = ForegroundServiceNotificationListener.this;
                StatusBarNotification statusBarNotification = notificationEntry.mSbn;
                Objects.requireNonNull(foregroundServiceNotificationListener);
                foregroundServiceNotificationListener.mForegroundServiceController.updateUserState(statusBarNotification.getUserId(), new AnonymousClass3(statusBarNotification), false);
            }

            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public final void onPendingEntryAdded(NotificationEntry notificationEntry) {
                ForegroundServiceNotificationListener foregroundServiceNotificationListener = ForegroundServiceNotificationListener.this;
                int importance = notificationEntry.getImportance();
                Objects.requireNonNull(foregroundServiceNotificationListener);
                foregroundServiceNotificationListener.updateNotification(notificationEntry, importance);
            }

            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public final void onPreEntryUpdated(NotificationEntry notificationEntry) {
                ForegroundServiceNotificationListener.this.updateNotification(notificationEntry, notificationEntry.getImportance());
            }
        });
        notifPipeline.addCollectionListener(new NotifCollectionListener() { // from class: com.android.systemui.ForegroundServiceNotificationListener.2
            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public final void onEntryAdded(NotificationEntry notificationEntry) {
                ForegroundServiceNotificationListener foregroundServiceNotificationListener = ForegroundServiceNotificationListener.this;
                int importance = notificationEntry.getImportance();
                Objects.requireNonNull(foregroundServiceNotificationListener);
                foregroundServiceNotificationListener.updateNotification(notificationEntry, importance);
            }

            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public final void onEntryRemoved(NotificationEntry notificationEntry, int i) {
                ForegroundServiceNotificationListener foregroundServiceNotificationListener = ForegroundServiceNotificationListener.this;
                Objects.requireNonNull(notificationEntry);
                StatusBarNotification statusBarNotification = notificationEntry.mSbn;
                Objects.requireNonNull(foregroundServiceNotificationListener);
                foregroundServiceNotificationListener.mForegroundServiceController.updateUserState(statusBarNotification.getUserId(), new AnonymousClass3(statusBarNotification), false);
            }

            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public final void onEntryUpdated(NotificationEntry notificationEntry) {
                ForegroundServiceNotificationListener.this.updateNotification(notificationEntry, notificationEntry.getImportance());
            }
        });
    }

    public final void updateNotification(NotificationEntry notificationEntry, final int i) {
        Objects.requireNonNull(notificationEntry);
        final StatusBarNotification statusBarNotification = notificationEntry.mSbn;
        this.mForegroundServiceController.updateUserState(statusBarNotification.getUserId(), new ForegroundServiceController.UserStateUpdateCallback() { // from class: com.android.systemui.ForegroundServiceNotificationListener$$ExternalSyntheticLambda0
            @Override // com.android.systemui.ForegroundServiceController.UserStateUpdateCallback
            public final boolean updateUserState(ForegroundServicesUserState foregroundServicesUserState) {
                String[] strArr;
                ForegroundServiceNotificationListener foregroundServiceNotificationListener = ForegroundServiceNotificationListener.this;
                StatusBarNotification statusBarNotification2 = statusBarNotification;
                int i2 = i;
                Objects.requireNonNull(foregroundServiceNotificationListener);
                Objects.requireNonNull(foregroundServiceNotificationListener.mForegroundServiceController);
                if (ForegroundServiceController.isDisclosureNotification(statusBarNotification2)) {
                    Bundle bundle = statusBarNotification2.getNotification().extras;
                    if (bundle != null) {
                        String[] stringArray = bundle.getStringArray("android.foregroundApps");
                        long j = statusBarNotification2.getNotification().when;
                        if (stringArray != null) {
                            strArr = (String[]) Arrays.copyOf(stringArray, stringArray.length);
                        } else {
                            strArr = null;
                        }
                        foregroundServicesUserState.mRunning = strArr;
                        foregroundServicesUserState.mServiceStartTime = j;
                    }
                } else {
                    String packageName = statusBarNotification2.getPackageName();
                    String key = statusBarNotification2.getKey();
                    ArrayMap<String, ArraySet<String>> arrayMap = foregroundServicesUserState.mImportantNotifications;
                    ArraySet<String> arraySet = arrayMap.get(packageName);
                    if (arraySet != null) {
                        arraySet.remove(key);
                        if (arraySet.size() == 0) {
                            arrayMap.remove(packageName);
                        }
                    }
                    ArrayMap<String, ArraySet<String>> arrayMap2 = foregroundServicesUserState.mStandardLayoutNotifications;
                    ArraySet<String> arraySet2 = arrayMap2.get(packageName);
                    if (arraySet2 != null) {
                        arraySet2.remove(key);
                        if (arraySet2.size() == 0) {
                            arrayMap2.remove(packageName);
                        }
                    }
                    if ((statusBarNotification2.getNotification().flags & 64) != 0 && i2 > 1) {
                        String packageName2 = statusBarNotification2.getPackageName();
                        String key2 = statusBarNotification2.getKey();
                        ArrayMap<String, ArraySet<String>> arrayMap3 = foregroundServicesUserState.mImportantNotifications;
                        if (arrayMap3.get(packageName2) == null) {
                            arrayMap3.put(packageName2, new ArraySet<>());
                        }
                        arrayMap3.get(packageName2).add(key2);
                    }
                    if (Notification.Builder.recoverBuilder(foregroundServiceNotificationListener.mContext, statusBarNotification2.getNotification()).usesStandardHeader()) {
                        String packageName3 = statusBarNotification2.getPackageName();
                        String key3 = statusBarNotification2.getKey();
                        ArrayMap<String, ArraySet<String>> arrayMap4 = foregroundServicesUserState.mStandardLayoutNotifications;
                        if (arrayMap4.get(packageName3) == null) {
                            arrayMap4.put(packageName3, new ArraySet<>());
                        }
                        arrayMap4.get(packageName3).add(key3);
                    }
                }
                return true;
            }
        }, true);
    }
}
