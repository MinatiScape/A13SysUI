package com.android.systemui.qs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.permission.PermissionManager;
import android.provider.DeviceConfig;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.GhostedViewLaunchAnimatorController;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.privacy.OngoingPrivacyChip;
import com.android.systemui.privacy.PrivacyChipEvent;
import com.android.systemui.privacy.PrivacyDialogController;
import com.android.systemui.privacy.PrivacyItem;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.statusbar.phone.StatusIconContainer;
import com.android.systemui.util.DeviceConfigProxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
/* compiled from: HeaderPrivacyIconsController.kt */
/* loaded from: classes.dex */
public final class HeaderPrivacyIconsController {
    public final ActivityStarter activityStarter;
    public final AppOpsController appOpsController;
    public final Executor backgroundExecutor;
    public final String cameraSlot;
    public ChipVisibilityListener chipVisibilityListener;
    public final StatusIconContainer iconContainer;
    public boolean listening;
    public boolean locationIndicatorsEnabled;
    public final String locationSlot;
    public boolean micCameraIndicatorsEnabled;
    public final String micSlot;
    public final PermissionManager permissionManager;
    public final OngoingPrivacyChip privacyChip;
    public boolean privacyChipLogged;
    public final PrivacyDialogController privacyDialogController;
    public final PrivacyItemController privacyItemController;
    public final PrivacyLogger privacyLogger;
    public final UiEventLogger uiEventLogger;
    public final Executor uiExecutor;
    public boolean safetyCenterEnabled = DeviceConfig.getBoolean("privacy", "safety_center_is_enabled", false);
    public final HeaderPrivacyIconsController$picCallback$1 picCallback = new PrivacyItemController.Callback() { // from class: com.android.systemui.qs.HeaderPrivacyIconsController$picCallback$1
        @Override // com.android.systemui.privacy.PrivacyItemController.Callback
        public final void onFlagLocationChanged(boolean z) {
            HeaderPrivacyIconsController headerPrivacyIconsController = HeaderPrivacyIconsController.this;
            if (headerPrivacyIconsController.locationIndicatorsEnabled != z) {
                headerPrivacyIconsController.locationIndicatorsEnabled = z;
                headerPrivacyIconsController.updatePrivacyIconSlots();
                HeaderPrivacyIconsController headerPrivacyIconsController2 = HeaderPrivacyIconsController.this;
                OngoingPrivacyChip ongoingPrivacyChip = headerPrivacyIconsController2.privacyChip;
                Objects.requireNonNull(ongoingPrivacyChip);
                headerPrivacyIconsController2.setChipVisibility(!ongoingPrivacyChip.privacyList.isEmpty());
            }
        }

        @Override // com.android.systemui.privacy.PrivacyItemController.Callback
        public final void onFlagMicCameraChanged(boolean z) {
            HeaderPrivacyIconsController headerPrivacyIconsController = HeaderPrivacyIconsController.this;
            if (headerPrivacyIconsController.micCameraIndicatorsEnabled != z) {
                headerPrivacyIconsController.micCameraIndicatorsEnabled = z;
                headerPrivacyIconsController.updatePrivacyIconSlots();
                HeaderPrivacyIconsController headerPrivacyIconsController2 = HeaderPrivacyIconsController.this;
                OngoingPrivacyChip ongoingPrivacyChip = headerPrivacyIconsController2.privacyChip;
                Objects.requireNonNull(ongoingPrivacyChip);
                headerPrivacyIconsController2.setChipVisibility(!ongoingPrivacyChip.privacyList.isEmpty());
            }
        }

        @Override // com.android.systemui.privacy.PrivacyItemController.Callback
        public final void onPrivacyItemsChanged(List<PrivacyItem> list) {
            HeaderPrivacyIconsController.this.privacyChip.setPrivacyList(list);
            HeaderPrivacyIconsController.this.setChipVisibility(!list.isEmpty());
        }
    };

    /* JADX WARN: Removed duplicated region for block: B:19:0x0033  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x003d  */
    /* JADX WARN: Removed duplicated region for block: B:24:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void setChipVisibility(boolean r4) {
        /*
            r3 = this;
            r0 = 0
            if (r4 == 0) goto L_0x0029
            boolean r1 = r3.micCameraIndicatorsEnabled
            r2 = 1
            if (r1 != 0) goto L_0x000f
            boolean r1 = r3.locationIndicatorsEnabled
            if (r1 == 0) goto L_0x000d
            goto L_0x000f
        L_0x000d:
            r1 = r0
            goto L_0x0010
        L_0x000f:
            r1 = r2
        L_0x0010:
            if (r1 == 0) goto L_0x0029
            com.android.systemui.privacy.logging.PrivacyLogger r1 = r3.privacyLogger
            r1.logChipVisible(r2)
            boolean r1 = r3.privacyChipLogged
            if (r1 != 0) goto L_0x002e
            boolean r1 = r3.listening
            if (r1 == 0) goto L_0x002e
            r3.privacyChipLogged = r2
            com.android.internal.logging.UiEventLogger r1 = r3.uiEventLogger
            com.android.systemui.privacy.PrivacyChipEvent r2 = com.android.systemui.privacy.PrivacyChipEvent.ONGOING_INDICATORS_CHIP_VIEW
            r1.log(r2)
            goto L_0x002e
        L_0x0029:
            com.android.systemui.privacy.logging.PrivacyLogger r1 = r3.privacyLogger
            r1.logChipVisible(r0)
        L_0x002e:
            com.android.systemui.privacy.OngoingPrivacyChip r1 = r3.privacyChip
            if (r4 == 0) goto L_0x0033
            goto L_0x0035
        L_0x0033:
            r0 = 8
        L_0x0035:
            r1.setVisibility(r0)
            com.android.systemui.qs.ChipVisibilityListener r3 = r3.chipVisibilityListener
            if (r3 != 0) goto L_0x003d
            goto L_0x0040
        L_0x003d:
            r3.onChipVisibilityRefreshed(r4)
        L_0x0040:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.HeaderPrivacyIconsController.setChipVisibility(boolean):void");
    }

    public final void startListening() {
        this.listening = true;
        PrivacyItemController privacyItemController = this.privacyItemController;
        Objects.requireNonNull(privacyItemController);
        this.micCameraIndicatorsEnabled = privacyItemController.micCameraAvailable;
        PrivacyItemController privacyItemController2 = this.privacyItemController;
        Objects.requireNonNull(privacyItemController2);
        this.locationIndicatorsEnabled = privacyItemController2.locationAvailable;
        this.privacyItemController.addCallback(this.picCallback);
    }

    public final void onParentVisible() {
        boolean z;
        this.privacyChip.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.qs.HeaderPrivacyIconsController$onParentVisible$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HeaderPrivacyIconsController.this.uiEventLogger.log(PrivacyChipEvent.ONGOING_INDICATORS_CHIP_CLICK);
                final HeaderPrivacyIconsController headerPrivacyIconsController = HeaderPrivacyIconsController.this;
                if (headerPrivacyIconsController.safetyCenterEnabled) {
                    headerPrivacyIconsController.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.qs.HeaderPrivacyIconsController$showSafetyHub$1
                        @Override // java.lang.Runnable
                        public final void run() {
                            HeaderPrivacyIconsController headerPrivacyIconsController2 = HeaderPrivacyIconsController.this;
                            Objects.requireNonNull(headerPrivacyIconsController2);
                            ArrayList<? extends Parcelable> arrayList = new ArrayList<>(headerPrivacyIconsController2.permissionManager.getIndicatorAppOpUsageData(headerPrivacyIconsController2.appOpsController.isMicMuted()));
                            HeaderPrivacyIconsController.this.privacyLogger.logUnfilteredPermGroupUsage(arrayList);
                            final Intent intent = new Intent("android.intent.action.VIEW_SAFETY_HUB");
                            intent.putParcelableArrayListExtra("android.permission.extra.PERMISSION_USAGES", arrayList);
                            intent.setFlags(268435456);
                            final HeaderPrivacyIconsController headerPrivacyIconsController3 = HeaderPrivacyIconsController.this;
                            headerPrivacyIconsController3.uiExecutor.execute(new Runnable() { // from class: com.android.systemui.qs.HeaderPrivacyIconsController$showSafetyHub$1.1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    HeaderPrivacyIconsController headerPrivacyIconsController4 = HeaderPrivacyIconsController.this;
                                    ActivityStarter activityStarter = headerPrivacyIconsController4.activityStarter;
                                    Intent intent2 = intent;
                                    OngoingPrivacyChip ongoingPrivacyChip = headerPrivacyIconsController4.privacyChip;
                                    GhostedViewLaunchAnimatorController ghostedViewLaunchAnimatorController = null;
                                    if (!(ongoingPrivacyChip.getParent() instanceof ViewGroup)) {
                                        Log.wtf("ActivityLaunchAnimator", "Skipping animation as view " + ongoingPrivacyChip + " is not attached to a ViewGroup", new Exception());
                                    } else {
                                        ghostedViewLaunchAnimatorController = new GhostedViewLaunchAnimatorController(ongoingPrivacyChip, (Integer) null, 4);
                                    }
                                    activityStarter.startActivity(intent2, true, (ActivityLaunchAnimator.Controller) ghostedViewLaunchAnimatorController);
                                }
                            });
                        }
                    });
                    return;
                }
                final PrivacyDialogController privacyDialogController = headerPrivacyIconsController.privacyDialogController;
                final Context context = headerPrivacyIconsController.privacyChip.getContext();
                Objects.requireNonNull(privacyDialogController);
                Dialog dialog = privacyDialogController.dialog;
                if (dialog != null) {
                    dialog.dismiss();
                }
                privacyDialogController.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.privacy.PrivacyDialogController$showDialog$1
                    /* JADX WARN: Code restructure failed: missing block: B:26:0x008a, code lost:
                        if (r10.micCameraAvailable != false) goto L_0x009a;
                     */
                    /* JADX WARN: Code restructure failed: missing block: B:29:0x0096, code lost:
                        if (r10.locationAvailable != false) goto L_0x009a;
                     */
                    /* JADX WARN: Removed duplicated region for block: B:22:0x007d  */
                    /* JADX WARN: Removed duplicated region for block: B:23:0x007f  */
                    /* JADX WARN: Removed duplicated region for block: B:35:0x00a6  */
                    /* JADX WARN: Removed duplicated region for block: B:43:0x00c4  */
                    /* JADX WARN: Removed duplicated region for block: B:49:0x00d7  */
                    /* JADX WARN: Removed duplicated region for block: B:51:0x00dc  */
                    /* JADX WARN: Removed duplicated region for block: B:56:0x0125  */
                    /* JADX WARN: Removed duplicated region for block: B:57:0x0128  */
                    /* JADX WARN: Removed duplicated region for block: B:60:0x0144  */
                    /* JADX WARN: Removed duplicated region for block: B:64:0x0190  */
                    /* JADX WARN: Removed duplicated region for block: B:68:0x01a1  */
                    /* JADX WARN: Removed duplicated region for block: B:76:0x01a4 A[SYNTHETIC] */
                    /* JADX WARN: Removed duplicated region for block: B:77:0x00bf A[SYNTHETIC] */
                    @Override // java.lang.Runnable
                    /*
                        Code decompiled incorrectly, please refer to instructions dump.
                        To view partially-correct add '--show-bad-code' argument
                    */
                    public final void run() {
                        /*
                            Method dump skipped, instructions count: 439
                            To view this dump add '--comments-level debug' option
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.privacy.PrivacyDialogController$showDialog$1.run():void");
                    }
                });
            }
        });
        if (this.privacyChip.getVisibility() == 0) {
            z = true;
        } else {
            z = false;
        }
        setChipVisibility(z);
        PrivacyItemController privacyItemController = this.privacyItemController;
        Objects.requireNonNull(privacyItemController);
        this.micCameraIndicatorsEnabled = privacyItemController.micCameraAvailable;
        PrivacyItemController privacyItemController2 = this.privacyItemController;
        Objects.requireNonNull(privacyItemController2);
        this.locationIndicatorsEnabled = privacyItemController2.locationAvailable;
        updatePrivacyIconSlots();
    }

    public final void updatePrivacyIconSlots() {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4 = this.micCameraIndicatorsEnabled;
        boolean z5 = false;
        if (z4 || this.locationIndicatorsEnabled) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            if (z4) {
                StatusIconContainer statusIconContainer = this.iconContainer;
                String str = this.cameraSlot;
                Objects.requireNonNull(statusIconContainer);
                if (statusIconContainer.mIgnoredSlots.contains(str)) {
                    z2 = false;
                } else {
                    statusIconContainer.mIgnoredSlots.add(str);
                    z2 = true;
                }
                if (z2) {
                    statusIconContainer.requestLayout();
                }
                StatusIconContainer statusIconContainer2 = this.iconContainer;
                String str2 = this.micSlot;
                Objects.requireNonNull(statusIconContainer2);
                if (statusIconContainer2.mIgnoredSlots.contains(str2)) {
                    z3 = false;
                } else {
                    statusIconContainer2.mIgnoredSlots.add(str2);
                    z3 = true;
                }
                if (z3) {
                    statusIconContainer2.requestLayout();
                }
            } else {
                this.iconContainer.removeIgnoredSlot(this.cameraSlot);
                this.iconContainer.removeIgnoredSlot(this.micSlot);
            }
            if (this.locationIndicatorsEnabled) {
                StatusIconContainer statusIconContainer3 = this.iconContainer;
                String str3 = this.locationSlot;
                Objects.requireNonNull(statusIconContainer3);
                if (!statusIconContainer3.mIgnoredSlots.contains(str3)) {
                    statusIconContainer3.mIgnoredSlots.add(str3);
                    z5 = true;
                }
                if (z5) {
                    statusIconContainer3.requestLayout();
                    return;
                }
                return;
            }
            this.iconContainer.removeIgnoredSlot(this.locationSlot);
            return;
        }
        this.iconContainer.removeIgnoredSlot(this.cameraSlot);
        this.iconContainer.removeIgnoredSlot(this.micSlot);
        this.iconContainer.removeIgnoredSlot(this.locationSlot);
    }

    /* JADX WARN: Type inference failed for: r1v8, types: [com.android.systemui.qs.HeaderPrivacyIconsController$picCallback$1] */
    public HeaderPrivacyIconsController(PrivacyItemController privacyItemController, UiEventLogger uiEventLogger, OngoingPrivacyChip ongoingPrivacyChip, PrivacyDialogController privacyDialogController, PrivacyLogger privacyLogger, StatusIconContainer statusIconContainer, PermissionManager permissionManager, Executor executor, Executor executor2, ActivityStarter activityStarter, AppOpsController appOpsController, DeviceConfigProxy deviceConfigProxy) {
        this.privacyItemController = privacyItemController;
        this.uiEventLogger = uiEventLogger;
        this.privacyChip = ongoingPrivacyChip;
        this.privacyDialogController = privacyDialogController;
        this.privacyLogger = privacyLogger;
        this.iconContainer = statusIconContainer;
        this.permissionManager = permissionManager;
        this.backgroundExecutor = executor;
        this.uiExecutor = executor2;
        this.activityStarter = activityStarter;
        this.appOpsController = appOpsController;
        this.cameraSlot = ongoingPrivacyChip.getResources().getString(17041538);
        this.micSlot = ongoingPrivacyChip.getResources().getString(17041550);
        this.locationSlot = ongoingPrivacyChip.getResources().getString(17041548);
        DeviceConfig.OnPropertiesChangedListener headerPrivacyIconsController$devicePropertiesChangedListener$1 = new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.systemui.qs.HeaderPrivacyIconsController$devicePropertiesChangedListener$1
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                HeaderPrivacyIconsController.this.safetyCenterEnabled = properties.getBoolean("safety_center_is_enabled", false);
            }
        };
        Objects.requireNonNull(deviceConfigProxy);
        DeviceConfig.addOnPropertiesChangedListener("privacy", executor2, headerPrivacyIconsController$devicePropertiesChangedListener$1);
    }
}
