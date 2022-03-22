package com.android.systemui;

import android.os.Handler;
import android.os.UserHandle;
import android.service.notification.StatusBarNotification;
import android.util.ArraySet;
import android.util.SparseArray;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.android.systemui.util.Assert;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ForegroundServiceController {
    public static final int[] APP_OPS = {24};
    public final Handler mMainHandler;
    public final SparseArray<ForegroundServicesUserState> mUserServices = new SparseArray<>();
    public final Object mMutex = new Object();

    /* loaded from: classes.dex */
    public interface UserStateUpdateCallback {
        boolean updateUserState(ForegroundServicesUserState foregroundServicesUserState);
    }

    public final boolean isDisclosureNeededForUser(int i) {
        synchronized (this.mMutex) {
            ForegroundServicesUserState foregroundServicesUserState = this.mUserServices.get(i);
            boolean z = false;
            if (foregroundServicesUserState == null) {
                return false;
            }
            if (foregroundServicesUserState.mRunning != null && System.currentTimeMillis() - foregroundServicesUserState.mServiceStartTime >= 5000) {
                for (String str : foregroundServicesUserState.mRunning) {
                    ArraySet<String> arraySet = foregroundServicesUserState.mImportantNotifications.get(str);
                    if (arraySet == null || arraySet.size() == 0) {
                    }
                    z = true;
                    break;
                }
            }
            return z;
        }
    }

    public final boolean updateUserState(int i, UserStateUpdateCallback userStateUpdateCallback, boolean z) {
        synchronized (this.mMutex) {
            ForegroundServicesUserState foregroundServicesUserState = this.mUserServices.get(i);
            if (foregroundServicesUserState == null) {
                if (!z) {
                    return false;
                }
                foregroundServicesUserState = new ForegroundServicesUserState();
                this.mUserServices.put(i, foregroundServicesUserState);
            }
            return userStateUpdateCallback.updateUserState(foregroundServicesUserState);
        }
    }

    public ForegroundServiceController(AppOpsController appOpsController, Handler handler) {
        this.mMainHandler = handler;
        appOpsController.addCallback(APP_OPS, new AppOpsController.Callback() { // from class: com.android.systemui.ForegroundServiceController$$ExternalSyntheticLambda0
            @Override // com.android.systemui.appops.AppOpsController.Callback
            public final void onActiveStateChanged(final int i, final int i2, final String str, final boolean z) {
                final ForegroundServiceController foregroundServiceController = ForegroundServiceController.this;
                Objects.requireNonNull(foregroundServiceController);
                foregroundServiceController.mMainHandler.post(new Runnable() { // from class: com.android.systemui.ForegroundServiceController$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        ForegroundServiceController foregroundServiceController2 = ForegroundServiceController.this;
                        int i3 = i;
                        int i4 = i2;
                        String str2 = str;
                        boolean z2 = z;
                        Objects.requireNonNull(foregroundServiceController2);
                        Assert.isMainThread();
                        int userId = UserHandle.getUserId(i4);
                        synchronized (foregroundServiceController2.mMutex) {
                            ForegroundServicesUserState foregroundServicesUserState = foregroundServiceController2.mUserServices.get(userId);
                            if (foregroundServicesUserState == null) {
                                foregroundServicesUserState = new ForegroundServicesUserState();
                                foregroundServiceController2.mUserServices.put(userId, foregroundServicesUserState);
                            }
                            if (z2) {
                                if (foregroundServicesUserState.mAppOps.get(str2) == null) {
                                    foregroundServicesUserState.mAppOps.put(str2, new ArraySet<>(3));
                                }
                                foregroundServicesUserState.mAppOps.get(str2).add(Integer.valueOf(i3));
                            } else {
                                ArraySet<Integer> arraySet = foregroundServicesUserState.mAppOps.get(str2);
                                if (arraySet != null) {
                                    arraySet.remove(Integer.valueOf(i3));
                                    if (arraySet.size() == 0) {
                                        foregroundServicesUserState.mAppOps.remove(str2);
                                    }
                                }
                            }
                        }
                    }
                });
            }
        });
    }

    public static boolean isDisclosureNotification(StatusBarNotification statusBarNotification) {
        if (statusBarNotification.getId() == 40 && statusBarNotification.getTag() == null && statusBarNotification.getPackageName().equals(ThemeOverlayApplier.ANDROID_PACKAGE)) {
            return true;
        }
        return false;
    }
}
