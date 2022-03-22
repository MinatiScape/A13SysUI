package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Trace;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.android.internal.util.ContrastColorUtil;
import com.android.settingslib.Utils;
import com.android.systemui.R$array;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.dreams.touch.DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda6;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda10;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.android.systemui.wmshell.WMShell$7$$ExternalSyntheticLambda0;
import com.android.wm.shell.bubbles.Bubbles;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public final class NotificationIconAreaController implements DarkIconDispatcher.DarkReceiver, StatusBarStateController.StateListener, NotificationWakeUpCoordinator.WakeUpListener, DemoMode {
    public static final /* synthetic */ int $r8$clinit = 0;
    public boolean mAnimationsEnabled;
    public int mAodIconAppearTranslation;
    public int mAodIconTint;
    public NotificationIconContainer mAodIcons;
    public boolean mAodIconsVisible;
    public final Optional<Bubbles> mBubblesOptional;
    public final KeyguardBypassController mBypassController;
    public Context mContext;
    public final ContrastColorUtil mContrastColorUtil;
    public final DozeParameters mDozeParameters;
    public int mIconHPadding;
    public int mIconSize;
    public final NotificationMediaManager mMediaManager;
    public View mNotificationIconArea;
    public NotificationIconContainer mNotificationIcons;
    public final ScreenOffAnimationController mScreenOffAnimationController;
    public final NotificationListener.NotificationSettingsListener mSettingsListener;
    public NotificationIconContainer mShelfIcons;
    public final StatusBarStateController mStatusBarStateController;
    public final StatusBarWindowController mStatusBarWindowController;
    public final NotificationWakeUpCoordinator mWakeUpCoordinator;
    public final WMShell$7$$ExternalSyntheticLambda0 mUpdateStatusBarIcons = new WMShell$7$$ExternalSyntheticLambda0(this, 5);
    public int mIconTint = -1;
    public List<ListEntry> mNotificationEntries = List.of();
    public final ArrayList<Rect> mTintAreas = new ArrayList<>();
    public boolean mShowLowPriority = true;

    public final void applyNotificationIconsTint() {
        for (int i = 0; i < this.mNotificationIcons.getChildCount(); i++) {
            StatusBarIconView statusBarIconView = (StatusBarIconView) this.mNotificationIcons.getChildAt(i);
            if (statusBarIconView.getWidth() != 0) {
                updateTintForIcon(statusBarIconView, this.mIconTint);
            } else {
                statusBarIconView.mLayoutRunnable = new NotificationIconAreaController$$ExternalSyntheticLambda0(this, statusBarIconView, 0);
            }
        }
        updateAodIconColors();
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public final void onStateChanged(int i) {
        updateAodIconsVisibility(false, false);
        updateAnimations();
    }

    public final void animateInAodIconTranslation() {
        this.mAodIcons.animate().setInterpolator(Interpolators.DECELERATE_QUINT).translationY(0.0f).setDuration(200L).start();
    }

    @Override // com.android.systemui.demomode.DemoMode
    public final List<String> demoCommands() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("notifications");
        return arrayList;
    }

    @Override // com.android.systemui.demomode.DemoModeCommandReceiver
    public final void dispatchDemoCommand(String str, Bundle bundle) {
        int i;
        if (this.mNotificationIconArea != null) {
            if ("false".equals(bundle.getString("visible"))) {
                i = 4;
            } else {
                i = 0;
            }
            this.mNotificationIconArea.setVisibility(i);
        }
    }

    @Override // com.android.systemui.plugins.DarkIconDispatcher.DarkReceiver
    public final void onDarkChanged(ArrayList<Rect> arrayList, float f, int i) {
        this.mTintAreas.clear();
        this.mTintAreas.addAll(arrayList);
        if (DarkIconDispatcher.isInAreas(arrayList, this.mNotificationIconArea)) {
            this.mIconTint = i;
        }
        applyNotificationIconsTint();
    }

    @Override // com.android.systemui.demomode.DemoModeCommandReceiver
    public final void onDemoModeFinished() {
        View view = this.mNotificationIconArea;
        if (view != null) {
            view.setVisibility(0);
        }
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public final void onDozingChanged(boolean z) {
        boolean z2;
        if (this.mAodIcons != null) {
            if (!this.mDozeParameters.getAlwaysOn() || this.mDozeParameters.getDisplayNeedsBlanking()) {
                z2 = false;
            } else {
                z2 = true;
            }
            NotificationIconContainer notificationIconContainer = this.mAodIcons;
            Objects.requireNonNull(notificationIconContainer);
            notificationIconContainer.mDozing = z;
            notificationIconContainer.mDisallowNextAnimation |= !z2;
            for (int i = 0; i < notificationIconContainer.getChildCount(); i++) {
                View childAt = notificationIconContainer.getChildAt(i);
                if (childAt instanceof StatusBarIconView) {
                    ((StatusBarIconView) childAt).setDozing(z, z2);
                }
            }
        }
    }

    @Override // com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator.WakeUpListener
    public final void onFullyHiddenChanged(boolean z) {
        boolean z2 = true;
        if (!this.mBypassController.getBypassEnabled()) {
            if (!this.mDozeParameters.getAlwaysOn() || this.mDozeParameters.getDisplayNeedsBlanking()) {
                z2 = false;
            }
            z2 &= z;
        }
        updateAodIconsVisibility(z2, false);
        updateAodNotificationIcons();
    }

    @Override // com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator.WakeUpListener
    public final void onPulseExpansionChanged(boolean z) {
        if (z) {
            updateAodIconsVisibility(true, false);
        }
    }

    public final void updateAnimations() {
        boolean z;
        boolean z2;
        boolean z3 = true;
        if (this.mStatusBarStateController.getState() == 0) {
            z = true;
        } else {
            z = false;
        }
        NotificationIconContainer notificationIconContainer = this.mAodIcons;
        if (notificationIconContainer != null) {
            if (!this.mAnimationsEnabled || z) {
                z2 = false;
            } else {
                z2 = true;
            }
            notificationIconContainer.setAnimationsEnabled(z2);
        }
        NotificationIconContainer notificationIconContainer2 = this.mNotificationIcons;
        if (!this.mAnimationsEnabled || !z) {
            z3 = false;
        }
        notificationIconContainer2.setAnimationsEnabled(z3);
    }

    public final void updateAodIconColors() {
        if (this.mAodIcons != null) {
            for (int i = 0; i < this.mAodIcons.getChildCount(); i++) {
                StatusBarIconView statusBarIconView = (StatusBarIconView) this.mAodIcons.getChildAt(i);
                if (statusBarIconView.getWidth() != 0) {
                    updateTintForIcon(statusBarIconView, this.mAodIconTint);
                } else {
                    statusBarIconView.mLayoutRunnable = new InternetDialog$$ExternalSyntheticLambda10(this, statusBarIconView, 2);
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0024  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0055  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x008e  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0124  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateAodIconsVisibility(boolean r8, boolean r9) {
        /*
            Method dump skipped, instructions count: 312
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NotificationIconAreaController.updateAodIconsVisibility(boolean, boolean):void");
    }

    public final void updateAodNotificationIcons() {
        NotificationIconContainer notificationIconContainer = this.mAodIcons;
        if (notificationIconContainer != null) {
            updateIconsForLayout(DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda6.INSTANCE$1, notificationIconContainer, false, true, true, true, true, this.mBypassController.getBypassEnabled());
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0047, code lost:
        if (r7.equals(r8.mMediaNotificationKey) != false) goto L_0x0115;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00d0, code lost:
        if (r7 != false) goto L_0x0115;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00fa, code lost:
        if (r5.mPulseSupressed != false) goto L_0x00fd;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0113, code lost:
        if (r16.mBubblesOptional.get().isBubbleExpanded(r5.mKey) != false) goto L_0x0115;
     */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0118  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateIconsForLayout(java.util.function.Function<com.android.systemui.statusbar.notification.collection.NotificationEntry, com.android.systemui.statusbar.StatusBarIconView> r17, com.android.systemui.statusbar.phone.NotificationIconContainer r18, boolean r19, boolean r20, boolean r21, boolean r22, boolean r23, boolean r24) {
        /*
            Method dump skipped, instructions count: 599
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NotificationIconAreaController.updateIconsForLayout(java.util.function.Function, com.android.systemui.statusbar.phone.NotificationIconContainer, boolean, boolean, boolean, boolean, boolean, boolean):void");
    }

    public final void updateNotificationIcons(List<ListEntry> list) {
        this.mNotificationEntries = list;
        Trace.beginSection("NotificationIconAreaController.updateNotificationIcons");
        updateIconsForLayout(NotificationIconAreaController$$ExternalSyntheticLambda2.INSTANCE, this.mNotificationIcons, false, this.mShowLowPriority, true, true, false, false);
        NotificationIconContainer notificationIconContainer = this.mShelfIcons;
        if (notificationIconContainer != null) {
            updateIconsForLayout(NotificationIconAreaController$$ExternalSyntheticLambda1.INSTANCE, notificationIconContainer, true, true, false, false, false, false);
        }
        updateAodNotificationIcons();
        applyNotificationIconsTint();
        Trace.endSection();
    }

    public final void updateTintForIcon(StatusBarIconView statusBarIconView, int i) {
        boolean z;
        int i2 = 0;
        if (!Boolean.TRUE.equals(statusBarIconView.getTag(2131428107)) || R$array.isGrayscale(statusBarIconView, this.mContrastColorUtil)) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            i2 = DarkIconDispatcher.getTint(this.mTintAreas, statusBarIconView, i);
        }
        statusBarIconView.setStaticDrawableColor(i2);
        statusBarIconView.setDecorColor(i);
    }

    public NotificationIconAreaController(Context context, StatusBarStateController statusBarStateController, NotificationWakeUpCoordinator notificationWakeUpCoordinator, KeyguardBypassController keyguardBypassController, NotificationMediaManager notificationMediaManager, NotificationListener notificationListener, DozeParameters dozeParameters, Optional<Bubbles> optional, DemoModeController demoModeController, DarkIconDispatcher darkIconDispatcher, StatusBarWindowController statusBarWindowController, ScreenOffAnimationController screenOffAnimationController) {
        NotificationListener.NotificationSettingsListener notificationSettingsListener = new NotificationListener.NotificationSettingsListener() { // from class: com.android.systemui.statusbar.phone.NotificationIconAreaController.1
            @Override // com.android.systemui.statusbar.NotificationListener.NotificationSettingsListener
            public final void onStatusBarIconsBehaviorChanged(boolean z) {
                NotificationIconAreaController notificationIconAreaController = NotificationIconAreaController.this;
                boolean z2 = !z;
                notificationIconAreaController.mShowLowPriority = z2;
                notificationIconAreaController.updateIconsForLayout(NotificationIconAreaController$$ExternalSyntheticLambda2.INSTANCE, notificationIconAreaController.mNotificationIcons, false, z2, true, true, false, false);
            }
        };
        this.mSettingsListener = notificationSettingsListener;
        this.mContrastColorUtil = ContrastColorUtil.getInstance(context);
        this.mContext = context;
        this.mStatusBarStateController = statusBarStateController;
        statusBarStateController.addCallback(this);
        this.mMediaManager = notificationMediaManager;
        this.mDozeParameters = dozeParameters;
        this.mWakeUpCoordinator = notificationWakeUpCoordinator;
        Objects.requireNonNull(notificationWakeUpCoordinator);
        notificationWakeUpCoordinator.wakeUpListeners.add(this);
        this.mBypassController = keyguardBypassController;
        this.mBubblesOptional = optional;
        demoModeController.addCallback((DemoMode) this);
        this.mStatusBarWindowController = statusBarWindowController;
        this.mScreenOffAnimationController = screenOffAnimationController;
        Objects.requireNonNull(notificationListener);
        notificationListener.mSettingsListeners.add(notificationSettingsListener);
        reloadDimens(context);
        View inflate = LayoutInflater.from(context).inflate(2131624329, (ViewGroup) null);
        this.mNotificationIconArea = inflate;
        this.mNotificationIcons = (NotificationIconContainer) inflate.findViewById(2131428506);
        this.mAodIconTint = Utils.getColorAttrDefaultColor(this.mContext, 2130970103);
        darkIconDispatcher.addDarkReceiver(this);
    }

    public final void reloadDimens(Context context) {
        Resources resources = context.getResources();
        this.mIconSize = resources.getDimensionPixelSize(17105554);
        this.mIconHPadding = resources.getDimensionPixelSize(2131167065);
        this.mAodIconAppearTranslation = resources.getDimensionPixelSize(2131167011);
    }

    public final void updateIconLayoutParams(Context context) {
        reloadDimens(context);
        int i = (this.mIconHPadding * 2) + this.mIconSize;
        StatusBarWindowController statusBarWindowController = this.mStatusBarWindowController;
        Objects.requireNonNull(statusBarWindowController);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(i, statusBarWindowController.mBarHeight);
        for (int i2 = 0; i2 < this.mNotificationIcons.getChildCount(); i2++) {
            this.mNotificationIcons.getChildAt(i2).setLayoutParams(layoutParams);
        }
        if (this.mShelfIcons != null) {
            for (int i3 = 0; i3 < this.mShelfIcons.getChildCount(); i3++) {
                this.mShelfIcons.getChildAt(i3).setLayoutParams(layoutParams);
            }
        }
        if (this.mAodIcons != null) {
            for (int i4 = 0; i4 < this.mAodIcons.getChildCount(); i4++) {
                this.mAodIcons.getChildAt(i4).setLayoutParams(layoutParams);
            }
        }
    }

    public boolean shouldShouldLowPriorityIcons() {
        return this.mShowLowPriority;
    }
}
