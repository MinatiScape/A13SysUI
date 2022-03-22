package com.android.systemui.wmshell;

import android.app.admin.DevicePolicyManager;
import android.app.usage.StorageStats;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.content.pm.UserInfo;
import android.content.res.Resources;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import com.android.settingslib.applications.ApplicationsState;
import com.android.systemui.model.SysUiState;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.power.PowerNotificationWarnings;
import com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda10;
import com.android.systemui.screenshot.ScreenshotController;
import com.android.systemui.screenshot.ScreenshotEvent;
import com.android.systemui.statusbar.KeyguardIndicationController;
import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import com.android.systemui.statusbar.phone.NotificationIconAreaController$$ExternalSyntheticLambda2;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.android.systemui.volume.Events;
import com.android.systemui.wmshell.WMShell;
import com.android.wm.shell.bubbles.BubbleStackView;
import com.android.wm.shell.bubbles.BubbleViewProvider;
import com.android.wm.shell.bubbles.ManageEducationView;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.google.android.systemui.power.BatteryDefenderNotification;
import com.google.android.systemui.power.BatteryDefenderNotification$$ExternalSyntheticLambda0;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.Callable;
import vendor.google.google_battery.V1_2.IGoogleBattery;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WMShell$7$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ WMShell$7$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        String str;
        int i;
        IGoogleBattery iGoogleBattery = null;
        r5 = null;
        final CharSequence charSequence = null;
        switch (this.$r8$classId) {
            case 0:
                WMShell.AnonymousClass7 r9 = (WMShell.AnonymousClass7) this.f$0;
                Objects.requireNonNull(r9);
                SysUiState sysUiState = WMShell.this.mSysUiState;
                sysUiState.setFlag(65536, true);
                sysUiState.commitUpdate(0);
                return;
            case 1:
                ApplicationsState.BackgroundHandler backgroundHandler = (ApplicationsState.BackgroundHandler) this.f$0;
                int i2 = ApplicationsState.BackgroundHandler.$r8$clinit;
                Objects.requireNonNull(backgroundHandler);
                try {
                    try {
                        ApplicationsState applicationsState = ApplicationsState.this;
                        StorageStats queryStatsForPackage = applicationsState.mStats.queryStatsForPackage(applicationsState.mCurComputingSizeUuid, applicationsState.mCurComputingSizePkg, UserHandle.of(applicationsState.mCurComputingSizeUserId));
                        ApplicationsState applicationsState2 = ApplicationsState.this;
                        PackageStats packageStats = new PackageStats(applicationsState2.mCurComputingSizePkg, applicationsState2.mCurComputingSizeUserId);
                        packageStats.codeSize = queryStatsForPackage.getAppBytes();
                        packageStats.dataSize = queryStatsForPackage.getDataBytes();
                        packageStats.cacheSize = queryStatsForPackage.getCacheBytes();
                        backgroundHandler.mStatsObserver.onGetStatsCompleted(packageStats, true);
                    } catch (PackageManager.NameNotFoundException | IOException e) {
                        Log.w("ApplicationsState", "Failed to query stats: " + e);
                        Objects.requireNonNull(backgroundHandler.mStatsObserver);
                    }
                    return;
                } catch (RemoteException unused) {
                    return;
                }
            case 2:
                final PowerNotificationWarnings powerNotificationWarnings = (PowerNotificationWarnings) this.f$0;
                boolean z = PowerNotificationWarnings.DEBUG;
                Objects.requireNonNull(powerNotificationWarnings);
                if (powerNotificationWarnings.mUsbHighTempDialog == null) {
                    SystemUIDialog systemUIDialog = new SystemUIDialog(powerNotificationWarnings.mContext, 2132018184);
                    systemUIDialog.setCancelable(false);
                    systemUIDialog.setIconAttribute(16843605);
                    systemUIDialog.setTitle(2131952441);
                    SystemUIDialog.setShowForAllUsers(systemUIDialog);
                    systemUIDialog.setMessage(powerNotificationWarnings.mContext.getString(2131952440, ""));
                    systemUIDialog.setPositiveButton(17039370, new DialogInterface.OnClickListener() { // from class: com.android.systemui.power.PowerNotificationWarnings$$ExternalSyntheticLambda0
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i3) {
                            PowerNotificationWarnings powerNotificationWarnings2 = PowerNotificationWarnings.this;
                            Objects.requireNonNull(powerNotificationWarnings2);
                            powerNotificationWarnings2.mUsbHighTempDialog = null;
                        }
                    });
                    systemUIDialog.setNegativeButton(2131952438, new DialogInterface.OnClickListener() { // from class: com.android.systemui.power.PowerNotificationWarnings$$ExternalSyntheticLambda2
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i3) {
                            final PowerNotificationWarnings powerNotificationWarnings2 = PowerNotificationWarnings.this;
                            Objects.requireNonNull(powerNotificationWarnings2);
                            String string = powerNotificationWarnings2.mContext.getString(2131952439);
                            Intent intent = new Intent();
                            intent.setClassName(ThemeOverlayApplier.SETTINGS_PACKAGE, "com.android.settings.HelpTrampoline");
                            intent.putExtra("android.intent.extra.TEXT", string);
                            powerNotificationWarnings2.mActivityStarter.startActivity(intent, true, new ActivityStarter.Callback() { // from class: com.android.systemui.power.PowerNotificationWarnings$$ExternalSyntheticLambda8
                                @Override // com.android.systemui.plugins.ActivityStarter.Callback
                                public final void onActivityStarted(int i4) {
                                    PowerNotificationWarnings powerNotificationWarnings3 = PowerNotificationWarnings.this;
                                    Objects.requireNonNull(powerNotificationWarnings3);
                                    powerNotificationWarnings3.mUsbHighTempDialog = null;
                                }
                            });
                        }
                    });
                    systemUIDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.android.systemui.power.PowerNotificationWarnings$$ExternalSyntheticLambda7
                        @Override // android.content.DialogInterface.OnDismissListener
                        public final void onDismiss(DialogInterface dialogInterface) {
                            PowerNotificationWarnings powerNotificationWarnings2 = PowerNotificationWarnings.this;
                            Objects.requireNonNull(powerNotificationWarnings2);
                            powerNotificationWarnings2.mUsbHighTempDialog = null;
                            Events.writeEvent(20, 9, Boolean.valueOf(powerNotificationWarnings2.mKeyguard.isKeyguardLocked()));
                        }
                    });
                    systemUIDialog.getWindow().addFlags(2097280);
                    systemUIDialog.show();
                    powerNotificationWarnings.mUsbHighTempDialog = systemUIDialog;
                    Events.writeEvent(19, 3, Boolean.valueOf(powerNotificationWarnings.mKeyguard.isKeyguardLocked()));
                    return;
                }
                return;
            case 3:
                ScreenshotController screenshotController = (ScreenshotController) this.f$0;
                ScreenshotController.AnonymousClass1 r0 = ScreenshotController.SCREENSHOT_REMOTE_RUNNER;
                Objects.requireNonNull(screenshotController);
                screenshotController.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_INTERACTION_TIMEOUT, 0, screenshotController.mPackageName);
                screenshotController.dismissScreenshot();
                return;
            case 4:
                KeyguardIndicationController keyguardIndicationController = (KeyguardIndicationController) this.f$0;
                Objects.requireNonNull(keyguardIndicationController);
                if (keyguardIndicationController.mDevicePolicyManager.isDeviceManaged()) {
                    charSequence = keyguardIndicationController.mDevicePolicyManager.getDeviceOwnerOrganizationName();
                } else if (keyguardIndicationController.mDevicePolicyManager.isOrganizationOwnedDeviceWithManagedProfile()) {
                    Iterator it = keyguardIndicationController.mUserManager.getProfiles(UserHandle.myUserId()).iterator();
                    while (true) {
                        if (it.hasNext()) {
                            UserInfo userInfo = (UserInfo) it.next();
                            if (userInfo.isManagedProfile()) {
                                i = userInfo.id;
                            }
                        } else {
                            i = -10000;
                        }
                    }
                    if (i != -10000) {
                        charSequence = keyguardIndicationController.mDevicePolicyManager.getOrganizationNameForUser(i);
                    }
                }
                final Resources resources = keyguardIndicationController.mContext.getResources();
                if (charSequence == null) {
                    str = keyguardIndicationController.mDevicePolicyManager.getString("SystemUi.KEYGUARD_MANAGEMENT_DISCLOSURE", new Callable() { // from class: com.android.systemui.statusbar.KeyguardIndicationController$$ExternalSyntheticLambda2
                        @Override // java.util.concurrent.Callable
                        public final Object call() {
                            return resources.getString(2131952290);
                        }
                    });
                } else {
                    if (keyguardIndicationController.mDevicePolicyManager.isDeviceManaged()) {
                        DevicePolicyManager devicePolicyManager = keyguardIndicationController.mDevicePolicyManager;
                        if (devicePolicyManager.getDeviceOwnerType(devicePolicyManager.getDeviceOwnerComponentOnAnyUser()) == 1) {
                            str = resources.getString(2131952292, charSequence);
                        }
                    }
                    str = keyguardIndicationController.mDevicePolicyManager.getString("SystemUi.KEYGUARD_MANAGEMENT_DISCLOSURE", new Callable() { // from class: com.android.systemui.statusbar.KeyguardIndicationController$$ExternalSyntheticLambda3
                        @Override // java.util.concurrent.Callable
                        public final Object call() {
                            return resources.getString(2131952291, charSequence);
                        }
                    }, new Object[]{charSequence});
                }
                keyguardIndicationController.mExecutor.execute(new InternetDialog$$ExternalSyntheticLambda10(keyguardIndicationController, str, 1));
                return;
            case 5:
                NotificationIconAreaController notificationIconAreaController = (NotificationIconAreaController) this.f$0;
                Objects.requireNonNull(notificationIconAreaController);
                notificationIconAreaController.updateIconsForLayout(NotificationIconAreaController$$ExternalSyntheticLambda2.INSTANCE, notificationIconAreaController.mNotificationIcons, false, notificationIconAreaController.mShowLowPriority, true, true, false, false);
                return;
            case FalsingManager.VERSION /* 6 */:
                ScrimController scrimController = (ScrimController) this.f$0;
                boolean z2 = ScrimController.DEBUG;
                Objects.requireNonNull(scrimController);
                scrimController.mBlankingTransitionRunnable = null;
                scrimController.mPendingFrameCallback = null;
                scrimController.mBlankScreen = false;
                scrimController.updateScrims();
                return;
            case 7:
                BubbleStackView bubbleStackView = (BubbleStackView) this.f$0;
                int i3 = BubbleStackView.FLYOUT_HIDE_AFTER;
                Objects.requireNonNull(bubbleStackView);
                BubbleViewProvider bubbleViewProvider = bubbleStackView.mExpandedBubble;
                bubbleStackView.mIsExpansionAnimating = true;
                bubbleStackView.hideFlyoutImmediate();
                bubbleStackView.updateExpandedBubble();
                bubbleStackView.updateExpandedView();
                ManageEducationView manageEducationView = bubbleStackView.mManageEduView;
                if (manageEducationView != null) {
                    manageEducationView.hide();
                }
                bubbleStackView.updateOverflowVisibility();
                bubbleStackView.updateZOrder();
                bubbleStackView.updateBadges(true);
                bubbleStackView.mIsExpansionAnimating = false;
                bubbleStackView.updateExpandedView();
                bubbleStackView.requestUpdate();
                if (bubbleViewProvider != null) {
                    bubbleViewProvider.setTaskViewVisibility();
                    return;
                }
                return;
            case 8:
                PipTaskOrganizer pipTaskOrganizer = (PipTaskOrganizer) this.f$0;
                Objects.requireNonNull(pipTaskOrganizer);
                pipTaskOrganizer.mTaskOrganizer.addListenerForType(pipTaskOrganizer, -4);
                return;
            default:
                Objects.requireNonNull((BatteryDefenderNotification) this.f$0);
                BatteryDefenderNotification$$ExternalSyntheticLambda0 batteryDefenderNotification$$ExternalSyntheticLambda0 = BatteryDefenderNotification$$ExternalSyntheticLambda0.INSTANCE;
                try {
                    IGoogleBattery service = IGoogleBattery.getService();
                    if (service != null) {
                        service.linkToDeath(batteryDefenderNotification$$ExternalSyntheticLambda0);
                    }
                    iGoogleBattery = service;
                } catch (RemoteException | NoSuchElementException e2) {
                    Log.e("BatteryDefenderNotification", "failed to get Google Battery HAL: ", e2);
                }
                if (iGoogleBattery == null) {
                    Log.d("BatteryDefenderNotification", "Can not init hal interface");
                }
                try {
                    try {
                        iGoogleBattery.clearBatteryDefender();
                    } catch (RemoteException e3) {
                        Log.e("BatteryDefenderNotification", "setProperty error: " + e3);
                    }
                    try {
                        iGoogleBattery.unlinkToDeath(BatteryDefenderNotification$$ExternalSyntheticLambda0.INSTANCE);
                        return;
                    } catch (RemoteException e4) {
                        Log.e("BatteryDefenderNotification", "unlinkToDeath failed: ", e4);
                        return;
                    }
                } catch (Throwable th) {
                    try {
                        iGoogleBattery.unlinkToDeath(BatteryDefenderNotification$$ExternalSyntheticLambda0.INSTANCE);
                    } catch (RemoteException e5) {
                        Log.e("BatteryDefenderNotification", "unlinkToDeath failed: ", e5);
                    }
                    throw th;
                }
        }
    }
}
