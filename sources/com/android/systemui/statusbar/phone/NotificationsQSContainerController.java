package com.android.systemui.statusbar.phone;

import android.view.DisplayCutout;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.leanback.R$color;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.plugins.qs.QSContainerController;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda15;
import com.android.systemui.util.ViewController;
import com.google.android.systemui.elmyra.actions.UnpinNotifications$$ExternalSyntheticLambda1;
import java.util.Objects;
import java.util.function.Consumer;
import kotlin.Pair;
/* compiled from: NotificationsQSContainerController.kt */
/* loaded from: classes.dex */
public final class NotificationsQSContainerController extends ViewController<NotificationsQuickSettingsContainer> implements QSContainerController {
    public int bottomCutoutInsets;
    public int bottomStableInsets;
    public final FeatureFlags featureFlags;
    public boolean isQSCustomizerAnimating;
    public boolean isQSCustomizing;
    public boolean isQSDetailShowing;
    public final NavigationModeController navigationModeController;
    public int notificationsBottomMargin;
    public final OverviewProxyService overviewProxyService;
    public boolean qsExpanded;
    public boolean splitShadeEnabled;
    public boolean taskbarVisible;
    public boolean isGestureNavigation = true;
    public final NotificationsQSContainerController$taskbarVisibilityListener$1 taskbarVisibilityListener = new OverviewProxyService.OverviewProxyListener() { // from class: com.android.systemui.statusbar.phone.NotificationsQSContainerController$taskbarVisibilityListener$1
        @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
        public final void onTaskbarStatusUpdated(boolean z, boolean z2) {
            NotificationsQSContainerController.this.taskbarVisible = z;
        }
    };
    public final NotificationsQSContainerController$windowInsetsListener$1 windowInsetsListener = new Consumer() { // from class: com.android.systemui.statusbar.phone.NotificationsQSContainerController$windowInsetsListener$1
        @Override // java.util.function.Consumer
        public final void accept(Object obj) {
            int i;
            WindowInsets windowInsets = (WindowInsets) obj;
            NotificationsQSContainerController.this.bottomStableInsets = windowInsets.getStableInsetBottom();
            NotificationsQSContainerController notificationsQSContainerController = NotificationsQSContainerController.this;
            DisplayCutout displayCutout = windowInsets.getDisplayCutout();
            if (displayCutout == null) {
                i = 0;
            } else {
                i = displayCutout.getSafeInsetBottom();
            }
            notificationsQSContainerController.bottomCutoutInsets = i;
            NotificationsQSContainerController.this.updateBottomSpacing();
        }
    };

    @Override // com.android.systemui.util.ViewController
    public final void onInit() {
        this.isGestureNavigation = R$color.isGesturalMode(this.navigationModeController.addListener(new NavigationModeController.ModeChangedListener() { // from class: com.android.systemui.statusbar.phone.NotificationsQSContainerController$onInit$currentMode$1
            @Override // com.android.systemui.navigationbar.NavigationModeController.ModeChangedListener
            public final void onNavigationModeChanged(int i) {
                NotificationsQSContainerController.this.isGestureNavigation = R$color.isGesturalMode(i);
            }
        }));
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        this.overviewProxyService.removeCallback((OverviewProxyService.OverviewProxyListener) this.taskbarVisibilityListener);
        NotificationsQuickSettingsContainer notificationsQuickSettingsContainer = (NotificationsQuickSettingsContainer) this.mView;
        Objects.requireNonNull(notificationsQuickSettingsContainer);
        notificationsQuickSettingsContainer.mInsetsChangedListener = UnpinNotifications$$ExternalSyntheticLambda1.INSTANCE$1;
        NotificationsQuickSettingsContainer notificationsQuickSettingsContainer2 = (NotificationsQuickSettingsContainer) this.mView;
        Objects.requireNonNull(notificationsQuickSettingsContainer2);
        notificationsQuickSettingsContainer2.mQSFragmentAttachedListener = OverviewProxyService$1$$ExternalSyntheticLambda15.INSTANCE$1;
    }

    @Override // com.android.systemui.plugins.qs.QSContainerController
    public final void setCustomizerAnimating(boolean z) {
        if (this.isQSCustomizerAnimating != z) {
            this.isQSCustomizerAnimating = z;
            ((NotificationsQuickSettingsContainer) this.mView).invalidate();
        }
    }

    @Override // com.android.systemui.plugins.qs.QSContainerController
    public final void setCustomizerShowing(boolean z) {
        this.isQSCustomizing = z;
        updateBottomSpacing();
    }

    @Override // com.android.systemui.plugins.qs.QSContainerController
    public final void setDetailShowing(boolean z) {
        this.isQSDetailShowing = z;
        updateBottomSpacing();
    }

    public final void updateBottomSpacing() {
        int i;
        int i2;
        int i3 = this.notificationsBottomMargin;
        if (this.splitShadeEnabled) {
            if (this.isGestureNavigation) {
                i = this.bottomCutoutInsets;
            } else if (this.taskbarVisible) {
                i = this.bottomStableInsets;
            } else {
                i3 += this.bottomStableInsets;
                i = 0;
            }
        } else if (this.isQSCustomizing || this.isQSDetailShowing) {
            i3 = 0;
            i = 0;
        } else if (this.isGestureNavigation) {
            i = this.bottomCutoutInsets;
        } else {
            if (this.taskbarVisible) {
                i = this.bottomStableInsets;
            }
            i = 0;
        }
        Pair pair = new Pair(Integer.valueOf(i), Integer.valueOf(i3));
        int intValue = ((Number) pair.component1()).intValue();
        int intValue2 = ((Number) pair.component2()).intValue();
        boolean isEnabled = this.featureFlags.isEnabled(Flags.NEW_FOOTER);
        if (!isEnabled && !this.splitShadeEnabled && !this.isQSCustomizing && !this.isQSDetailShowing && !this.isGestureNavigation && !this.taskbarVisible) {
            i2 = this.bottomStableInsets;
        } else if (!isEnabled || this.isQSCustomizing || this.isQSDetailShowing) {
            i2 = 0;
        } else {
            i2 = this.bottomStableInsets;
        }
        ((NotificationsQuickSettingsContainer) this.mView).setPadding(0, 0, 0, intValue);
        NotificationsQuickSettingsContainer notificationsQuickSettingsContainer = (NotificationsQuickSettingsContainer) this.mView;
        Objects.requireNonNull(notificationsQuickSettingsContainer);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) notificationsQuickSettingsContainer.mStackScroller.getLayoutParams();
        ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = intValue2;
        notificationsQuickSettingsContainer.mStackScroller.setLayoutParams(layoutParams);
        if (isEnabled) {
            NotificationsQuickSettingsContainer notificationsQuickSettingsContainer2 = (NotificationsQuickSettingsContainer) this.mView;
            Objects.requireNonNull(notificationsQuickSettingsContainer2);
            View view = notificationsQuickSettingsContainer2.mQSContainer;
            if (view != null) {
                view.setPadding(view.getPaddingLeft(), notificationsQuickSettingsContainer2.mQSContainer.getPaddingTop(), notificationsQuickSettingsContainer2.mQSContainer.getPaddingRight(), i2);
                return;
            }
            return;
        }
        NotificationsQuickSettingsContainer notificationsQuickSettingsContainer3 = (NotificationsQuickSettingsContainer) this.mView;
        Objects.requireNonNull(notificationsQuickSettingsContainer3);
        View view2 = notificationsQuickSettingsContainer3.mQSScrollView;
        if (view2 != null) {
            view2.setPaddingRelative(view2.getPaddingLeft(), notificationsQuickSettingsContainer3.mQSScrollView.getPaddingTop(), notificationsQuickSettingsContainer3.mQSScrollView.getPaddingRight(), i2);
        }
    }

    public final void updateMargins$2() {
        NotificationsQuickSettingsContainer notificationsQuickSettingsContainer = (NotificationsQuickSettingsContainer) this.mView;
        Objects.requireNonNull(notificationsQuickSettingsContainer);
        this.notificationsBottomMargin = ((ViewGroup.MarginLayoutParams) ((ConstraintLayout.LayoutParams) notificationsQuickSettingsContainer.mStackScroller.getLayoutParams())).bottomMargin;
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.statusbar.phone.NotificationsQSContainerController$taskbarVisibilityListener$1] */
    /* JADX WARN: Type inference failed for: r1v3, types: [com.android.systemui.statusbar.phone.NotificationsQSContainerController$windowInsetsListener$1] */
    public NotificationsQSContainerController(NotificationsQuickSettingsContainer notificationsQuickSettingsContainer, NavigationModeController navigationModeController, OverviewProxyService overviewProxyService, FeatureFlags featureFlags) {
        super(notificationsQuickSettingsContainer);
        this.navigationModeController = navigationModeController;
        this.overviewProxyService = overviewProxyService;
        this.featureFlags = featureFlags;
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        updateMargins$2();
        this.overviewProxyService.addCallback((OverviewProxyService.OverviewProxyListener) this.taskbarVisibilityListener);
        NotificationsQuickSettingsContainer notificationsQuickSettingsContainer = (NotificationsQuickSettingsContainer) this.mView;
        NotificationsQSContainerController$windowInsetsListener$1 notificationsQSContainerController$windowInsetsListener$1 = this.windowInsetsListener;
        Objects.requireNonNull(notificationsQuickSettingsContainer);
        notificationsQuickSettingsContainer.mInsetsChangedListener = notificationsQSContainerController$windowInsetsListener$1;
        NotificationsQuickSettingsContainer notificationsQuickSettingsContainer2 = (NotificationsQuickSettingsContainer) this.mView;
        Consumer<QS> notificationsQSContainerController$onViewAttached$1 = new Consumer() { // from class: com.android.systemui.statusbar.phone.NotificationsQSContainerController$onViewAttached$1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((QS) obj).setContainerController(NotificationsQSContainerController.this);
            }
        };
        Objects.requireNonNull(notificationsQuickSettingsContainer2);
        notificationsQuickSettingsContainer2.mQSFragmentAttachedListener = notificationsQSContainerController$onViewAttached$1;
        QS qs = notificationsQuickSettingsContainer2.mQs;
        if (qs != null) {
            notificationsQSContainerController$onViewAttached$1.accept(qs);
        }
    }
}
