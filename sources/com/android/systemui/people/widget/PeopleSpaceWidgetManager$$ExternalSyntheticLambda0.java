package com.android.systemui.people.widget;

import android.content.ComponentName;
import android.os.RemoteException;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.people.PeopleSpaceUtils;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import java.util.HashSet;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PeopleSpaceWidgetManager$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;
    public final /* synthetic */ Object f$2;

    public /* synthetic */ PeopleSpaceWidgetManager$$ExternalSyntheticLambda0(Object obj, Object obj2, Object obj3, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
        this.f$2 = obj3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                PeopleSpaceWidgetManager peopleSpaceWidgetManager = (PeopleSpaceWidgetManager) this.f$0;
                StatusBarNotification statusBarNotification = (StatusBarNotification) this.f$1;
                PeopleSpaceUtils.NotificationAction notificationAction = (PeopleSpaceUtils.NotificationAction) this.f$2;
                Objects.requireNonNull(peopleSpaceWidgetManager);
                try {
                    PeopleTileKey peopleTileKey = new PeopleTileKey(statusBarNotification.getShortcutId(), statusBarNotification.getUser().getIdentifier(), statusBarNotification.getPackageName());
                    if (PeopleTileKey.isValid(peopleTileKey)) {
                        if (peopleSpaceWidgetManager.mAppWidgetManager.getAppWidgetIds(new ComponentName(peopleSpaceWidgetManager.mContext, PeopleSpaceWidgetProvider.class)).length == 0) {
                            Log.d("PeopleSpaceWidgetMgr", "No app widget ids returned");
                        } else {
                            synchronized (peopleSpaceWidgetManager.mLock) {
                                HashSet matchingKeyWidgetIds = peopleSpaceWidgetManager.getMatchingKeyWidgetIds(peopleTileKey);
                                matchingKeyWidgetIds.addAll(peopleSpaceWidgetManager.getMatchingUriWidgetIds(statusBarNotification, notificationAction));
                                peopleSpaceWidgetManager.updateWidgetIdsBasedOnNotifications(matchingKeyWidgetIds);
                            }
                        }
                    }
                    return;
                } catch (Exception e) {
                    Log.e("PeopleSpaceWidgetMgr", "Throwing exception: " + e);
                    return;
                }
            default:
                NotificationLogger notificationLogger = (NotificationLogger) this.f$0;
                NotificationVisibility[] notificationVisibilityArr = (NotificationVisibility[]) this.f$1;
                NotificationVisibility[] notificationVisibilityArr2 = (NotificationVisibility[]) this.f$2;
                Objects.requireNonNull(notificationLogger);
                try {
                    notificationLogger.mBarService.onNotificationVisibilityChanged(notificationVisibilityArr, notificationVisibilityArr2);
                } catch (RemoteException unused) {
                }
                int length = notificationVisibilityArr.length;
                if (length > 0) {
                    String[] strArr = new String[length];
                    for (int i = 0; i < length; i++) {
                        strArr[i] = notificationVisibilityArr[i].key;
                    }
                    try {
                        notificationLogger.mNotificationListener.setNotificationsShown(strArr);
                    } catch (RuntimeException e2) {
                        Log.d("NotificationLogger", "failed setNotificationsShown: ", e2);
                    }
                }
                int length2 = notificationVisibilityArr.length;
                for (int i2 = 0; i2 < length2; i2++) {
                    if (notificationVisibilityArr[i2] != null) {
                        notificationVisibilityArr[i2].recycle();
                    }
                }
                int length3 = notificationVisibilityArr2.length;
                for (int i3 = 0; i3 < length3; i3++) {
                    if (notificationVisibilityArr2[i3] != null) {
                        notificationVisibilityArr2[i3].recycle();
                    }
                }
                return;
        }
    }
}
