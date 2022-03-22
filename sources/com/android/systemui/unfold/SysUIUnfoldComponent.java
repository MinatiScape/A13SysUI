package com.android.systemui.unfold;

import com.android.keyguard.KeyguardUnfoldTransition;
import com.android.systemui.statusbar.phone.NotificationPanelUnfoldAnimationController;
import com.android.systemui.statusbar.phone.StatusBarMoveFromCenterAnimationController;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import com.android.systemui.unfold.util.ScopedUnfoldTransitionProgressProvider;
/* compiled from: SysUIUnfoldModule.kt */
/* loaded from: classes.dex */
public interface SysUIUnfoldComponent {

    /* compiled from: SysUIUnfoldModule.kt */
    /* loaded from: classes.dex */
    public interface Factory {
        SysUIUnfoldComponent create(UnfoldTransitionProgressProvider unfoldTransitionProgressProvider, NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider, ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider);
    }

    FoldAodAnimationController getFoldAodAnimationController();

    KeyguardUnfoldTransition getKeyguardUnfoldTransition();

    NotificationPanelUnfoldAnimationController getNotificationPanelUnfoldAnimationController();

    StatusBarMoveFromCenterAnimationController getStatusBarMoveFromCenterAnimationController();

    UnfoldLightRevealOverlayAnimation getUnfoldLightRevealOverlayAnimation();

    UnfoldTransitionWallpaperController getUnfoldTransitionWallpaperController();
}
