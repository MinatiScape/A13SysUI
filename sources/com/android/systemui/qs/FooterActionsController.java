package com.android.systemui.qs;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.UserManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.DejankUtils;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.GhostedViewLaunchAnimatorController;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.TouchAnimator;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.phone.MultiUserSwitch;
import com.android.systemui.statusbar.phone.MultiUserSwitchController;
import com.android.systemui.statusbar.phone.MultiUserSwitchController$$ExternalSyntheticLambda1;
import com.android.systemui.statusbar.phone.SettingsButton;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.UserInfoController;
import com.android.systemui.util.DualHeightHorizontalLinearLayout;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.settings.GlobalSettings;
import java.util.Objects;
/* compiled from: FooterActionsController.kt */
/* loaded from: classes.dex */
public final class FooterActionsController extends ViewController<FooterActionsView> {
    public final ActivityStarter activityStarter;
    public final TouchAnimator alphaAnimator;
    public final DeviceProvisionedController deviceProvisionedController;
    public final FalsingManager falsingManager;
    public final FeatureFlags featureFlags;
    public final QSFgsManagerFooter fgsManagerFooterController;
    public final GlobalActionsDialogLite globalActionsDialog;
    public final GlobalSettings globalSetting;
    public boolean listening;
    public final MetricsLogger metricsLogger;
    public final FooterActionsController$multiUserSetting$1 multiUserSetting;
    public final MultiUserSwitchController multiUserSwitchController;
    public final View powerMenuLite;
    public final QSSecurityFooter securityFooterController;
    public final ViewGroup securityFootersContainer;
    public final View securityFootersSeparator;
    public final SettingsButton settingsButton;
    public final View settingsButtonContainer;
    public final boolean showPMLiteButton;
    public final UiEventLogger uiEventLogger;
    public final UserInfoController userInfoController;
    public final UserManager userManager;
    public final UserTracker userTracker;
    public float lastExpansion = -1.0f;
    public boolean visible = true;
    public final FooterActionsController$onUserInfoChangedListener$1 onUserInfoChangedListener = new UserInfoController.OnUserInfoChangedListener() { // from class: com.android.systemui.qs.FooterActionsController$onUserInfoChangedListener$1
        @Override // com.android.systemui.statusbar.policy.UserInfoController.OnUserInfoChangedListener
        public final void onUserInfoChanged(String str, Drawable drawable) {
            ((FooterActionsView) FooterActionsController.this.mView).onUserInfoChanged(drawable, FooterActionsController.this.userManager.isGuestUser(KeyguardUpdateMonitor.getCurrentUser()));
        }
    };
    public final FooterActionsController$onClickListener$1 onClickListener = new View.OnClickListener() { // from class: com.android.systemui.qs.FooterActionsController$onClickListener$1

        /* compiled from: FooterActionsController.kt */
        /* renamed from: com.android.systemui.qs.FooterActionsController$onClickListener$1$1  reason: invalid class name */
        /* loaded from: classes.dex */
        public static final class AnonymousClass1 implements Runnable {
            public static final AnonymousClass1 INSTANCE = new AnonymousClass1();

            @Override // java.lang.Runnable
            public final void run() {
            }
        }

        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            FooterActionsController footerActionsController = FooterActionsController.this;
            Objects.requireNonNull(footerActionsController);
            if (footerActionsController.visible && !FooterActionsController.this.falsingManager.isFalseTap(1)) {
                FooterActionsController footerActionsController2 = FooterActionsController.this;
                if (view == footerActionsController2.settingsButton) {
                    if (!footerActionsController2.deviceProvisionedController.isCurrentUserSetup()) {
                        FooterActionsController.this.activityStarter.postQSRunnableDismissingKeyguard(AnonymousClass1.INSTANCE);
                        return;
                    }
                    FooterActionsController.this.metricsLogger.action(406);
                    FooterActionsController footerActionsController3 = FooterActionsController.this;
                    Objects.requireNonNull(footerActionsController3);
                    View view2 = footerActionsController3.settingsButtonContainer;
                    GhostedViewLaunchAnimatorController ghostedViewLaunchAnimatorController = null;
                    if (view2 != null) {
                        if (!(view2.getParent() instanceof ViewGroup)) {
                            Log.wtf("ActivityLaunchAnimator", "Skipping animation as view " + view2 + " is not attached to a ViewGroup", new Exception());
                        } else {
                            ghostedViewLaunchAnimatorController = new GhostedViewLaunchAnimatorController(view2, (Integer) 33, 4);
                        }
                    }
                    footerActionsController3.activityStarter.startActivity(new Intent("android.settings.SETTINGS"), true, (ActivityLaunchAnimator.Controller) ghostedViewLaunchAnimatorController);
                } else if (view == footerActionsController2.powerMenuLite) {
                    footerActionsController2.uiEventLogger.log(GlobalActionsDialogLite.GlobalActionsEvent.GA_OPEN_QS);
                    FooterActionsController.this.globalActionsDialog.showOrHideDialog(false, true, view);
                }
            }
        }
    };

    /* JADX WARN: Type inference failed for: r1v4, types: [com.android.systemui.qs.FooterActionsController$onUserInfoChangedListener$1] */
    /* JADX WARN: Type inference failed for: r1v6, types: [com.android.systemui.qs.FooterActionsController$onClickListener$1] */
    /* JADX WARN: Type inference failed for: r2v4, types: [com.android.systemui.qs.FooterActionsController$multiUserSetting$1] */
    public FooterActionsController(FooterActionsView footerActionsView, MultiUserSwitchController.Factory factory, ActivityStarter activityStarter, UserManager userManager, UserTracker userTracker, UserInfoController userInfoController, DeviceProvisionedController deviceProvisionedController, QSSecurityFooter qSSecurityFooter, QSFgsManagerFooter qSFgsManagerFooter, FalsingManager falsingManager, MetricsLogger metricsLogger, GlobalActionsDialogLite globalActionsDialogLite, UiEventLogger uiEventLogger, boolean z, final GlobalSettings globalSettings, final Handler handler, FeatureFlags featureFlags) {
        super(footerActionsView);
        this.activityStarter = activityStarter;
        this.userManager = userManager;
        this.userTracker = userTracker;
        this.userInfoController = userInfoController;
        this.deviceProvisionedController = deviceProvisionedController;
        this.securityFooterController = qSSecurityFooter;
        this.fgsManagerFooterController = qSFgsManagerFooter;
        this.falsingManager = falsingManager;
        this.metricsLogger = metricsLogger;
        this.globalActionsDialog = globalActionsDialogLite;
        this.uiEventLogger = uiEventLogger;
        this.showPMLiteButton = z;
        this.globalSetting = globalSettings;
        this.featureFlags = featureFlags;
        TouchAnimator.Builder builder = new TouchAnimator.Builder();
        builder.addFloat(footerActionsView, "alpha", 0.0f, 1.0f);
        builder.mStartDelay = 0.9f;
        this.alphaAnimator = builder.build();
        this.settingsButton = (SettingsButton) footerActionsView.findViewById(2131428838);
        this.settingsButtonContainer = footerActionsView.findViewById(2131428839);
        this.securityFootersContainer = (ViewGroup) footerActionsView.findViewById(2131428817);
        this.powerMenuLite = footerActionsView.findViewById(2131428600);
        this.multiUserSwitchController = new MultiUserSwitchController((MultiUserSwitch) footerActionsView.findViewById(2131428473), factory.mUserManager, factory.mUserSwitcherController, factory.mFalsingManager, factory.mUserSwitchDialogController, factory.mFeatureFlags, factory.mActivityStarter);
        View view = new View(getContext());
        view.setVisibility(8);
        this.securityFootersSeparator = view;
        final int userId = userTracker.getUserId();
        this.multiUserSetting = new SettingObserver(globalSettings, handler, userId) { // from class: com.android.systemui.qs.FooterActionsController$multiUserSetting$1
            @Override // com.android.systemui.qs.SettingObserver
            public final void handleValueChanged(int i, boolean z2) {
                if (z2) {
                    FooterActionsController.this.updateView();
                }
            }
        };
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        setListening(false);
    }

    @Override // com.android.systemui.util.ViewController
    public final void onInit() {
        this.multiUserSwitchController.init();
        this.fgsManagerFooterController.init();
    }

    @Override // com.android.systemui.util.ViewController
    public void onViewAttached() {
        if (this.showPMLiteButton) {
            this.powerMenuLite.setVisibility(0);
            this.powerMenuLite.setOnClickListener(this.onClickListener);
        } else {
            this.powerMenuLite.setVisibility(8);
        }
        this.settingsButton.setOnClickListener(this.onClickListener);
        if (this.featureFlags.isEnabled(Flags.NEW_FOOTER)) {
            QSSecurityFooter qSSecurityFooter = this.securityFooterController;
            Objects.requireNonNull(qSSecurityFooter);
            View view = qSSecurityFooter.mRootView;
            Objects.requireNonNull(view, "null cannot be cast to non-null type com.android.systemui.util.DualHeightHorizontalLinearLayout");
            final DualHeightHorizontalLinearLayout dualHeightHorizontalLinearLayout = (DualHeightHorizontalLinearLayout) view;
            ViewGroup viewGroup = this.securityFootersContainer;
            if (viewGroup != null) {
                viewGroup.addView(dualHeightHorizontalLinearLayout);
            }
            int dimensionPixelSize = getResources().getDimensionPixelSize(2131166613);
            ViewGroup viewGroup2 = this.securityFootersContainer;
            if (viewGroup2 != null) {
                viewGroup2.addView(this.securityFootersSeparator, dimensionPixelSize, 1);
            }
            ViewGroup.LayoutParams layoutParams = dualHeightHorizontalLinearLayout.getLayoutParams();
            Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type android.widget.LinearLayout.LayoutParams");
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layoutParams;
            layoutParams2.bottomMargin = 0;
            layoutParams2.width = 0;
            layoutParams2.weight = 1.0f;
            layoutParams2.setMarginEnd(getResources().getDimensionPixelSize(2131166613));
            dualHeightHorizontalLinearLayout.alwaysSingleLine = true;
            TextView textView = dualHeightHorizontalLinearLayout.textView;
            if (textView != null) {
                textView.setSingleLine();
            }
            QSFgsManagerFooter qSFgsManagerFooter = this.fgsManagerFooterController;
            Objects.requireNonNull(qSFgsManagerFooter);
            final View view2 = qSFgsManagerFooter.mRootView;
            ViewGroup viewGroup3 = this.securityFootersContainer;
            if (viewGroup3 != null) {
                viewGroup3.addView(view2);
            }
            ViewGroup.LayoutParams layoutParams3 = view2.getLayoutParams();
            Objects.requireNonNull(layoutParams3, "null cannot be cast to non-null type android.widget.LinearLayout.LayoutParams");
            LinearLayout.LayoutParams layoutParams4 = (LinearLayout.LayoutParams) layoutParams3;
            layoutParams4.width = 0;
            layoutParams4.weight = 1.0f;
            VisibilityChangedDispatcher$OnVisibilityChangedListener footerActionsController$onViewAttached$visibilityListener$1 = new VisibilityChangedDispatcher$OnVisibilityChangedListener() { // from class: com.android.systemui.qs.FooterActionsController$onViewAttached$visibilityListener$1
                @Override // com.android.systemui.qs.VisibilityChangedDispatcher$OnVisibilityChangedListener
                public final void onVisibilityChanged(int i) {
                    boolean z;
                    int i2;
                    float f;
                    int i3 = 0;
                    int i4 = 8;
                    if (i == 8) {
                        FooterActionsController.this.securityFootersSeparator.setVisibility(8);
                    } else if (dualHeightHorizontalLinearLayout.getVisibility() == 0 && view2.getVisibility() == 0) {
                        FooterActionsController.this.securityFootersSeparator.setVisibility(0);
                    } else {
                        FooterActionsController.this.securityFootersSeparator.setVisibility(8);
                    }
                    QSFgsManagerFooter qSFgsManagerFooter2 = FooterActionsController.this.fgsManagerFooterController;
                    if (dualHeightHorizontalLinearLayout.getVisibility() == 0) {
                        z = true;
                    } else {
                        z = false;
                    }
                    Objects.requireNonNull(qSFgsManagerFooter2);
                    View view3 = qSFgsManagerFooter2.mTextContainer;
                    if (z) {
                        i2 = 8;
                    } else {
                        i2 = 0;
                    }
                    view3.setVisibility(i2);
                    View view4 = qSFgsManagerFooter2.mNumberContainer;
                    if (z) {
                        i4 = 0;
                    }
                    view4.setVisibility(i4);
                    LinearLayout.LayoutParams layoutParams5 = (LinearLayout.LayoutParams) qSFgsManagerFooter2.mRootView.getLayoutParams();
                    if (z) {
                        i3 = -2;
                    }
                    layoutParams5.width = i3;
                    if (z) {
                        f = 0.0f;
                    } else {
                        f = 1.0f;
                    }
                    layoutParams5.weight = f;
                    qSFgsManagerFooter2.mRootView.setLayoutParams(layoutParams5);
                }
            };
            QSSecurityFooter qSSecurityFooter2 = this.securityFooterController;
            Objects.requireNonNull(qSSecurityFooter2);
            qSSecurityFooter2.mVisibilityChangedListener = footerActionsController$onViewAttached$visibilityListener$1;
            QSFgsManagerFooter qSFgsManagerFooter2 = this.fgsManagerFooterController;
            Objects.requireNonNull(qSFgsManagerFooter2);
            qSFgsManagerFooter2.mVisibilityChangedListener = footerActionsController$onViewAttached$visibilityListener$1;
        }
        updateView();
    }

    public final void setExpansion(float f) {
        boolean z;
        if (this.featureFlags.isEnabled(Flags.NEW_FOOTER)) {
            float f2 = this.lastExpansion;
            if (f == f2) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                if (f >= 1.0f) {
                    ((FooterActionsView) this.mView).animate().alpha(1.0f).setDuration(500L).start();
                } else if (f2 >= 1.0f && f < 1.0f) {
                    ((FooterActionsView) this.mView).animate().alpha(0.0f).setDuration(250L).start();
                }
                this.lastExpansion = f;
                return;
            }
            return;
        }
        this.alphaAnimator.setPosition(f);
    }

    public final void setListening(boolean z) {
        if (this.listening != z) {
            this.listening = z;
            setListening(z);
            if (this.listening) {
                this.userInfoController.addCallback(this.onUserInfoChangedListener);
                updateView();
            } else {
                this.userInfoController.removeCallback(this.onUserInfoChangedListener);
            }
            if (this.featureFlags.isEnabled(Flags.NEW_FOOTER)) {
                this.fgsManagerFooterController.setListening(z);
                QSSecurityFooter qSSecurityFooter = this.securityFooterController;
                Objects.requireNonNull(qSSecurityFooter);
                if (z) {
                    qSSecurityFooter.mSecurityController.addCallback(qSSecurityFooter.mCallback);
                    qSSecurityFooter.mHandler.sendEmptyMessage(1);
                    return;
                }
                qSSecurityFooter.mSecurityController.removeCallback(qSSecurityFooter.mCallback);
            }
        }
    }

    public final void setVisible(boolean z) {
        int i;
        this.visible = z;
        int visibility = ((FooterActionsView) this.mView).getVisibility();
        FooterActionsView footerActionsView = (FooterActionsView) this.mView;
        if (this.visible) {
            i = 0;
        } else {
            i = 4;
        }
        footerActionsView.setVisibility(i);
        if (visibility != ((FooterActionsView) this.mView).getVisibility()) {
            updateView();
        }
    }

    public final void updateView() {
        FooterActionsView footerActionsView = (FooterActionsView) this.mView;
        MultiUserSwitchController multiUserSwitchController = this.multiUserSwitchController;
        Objects.requireNonNull(multiUserSwitchController);
        boolean booleanValue = ((Boolean) DejankUtils.whitelistIpcs(new MultiUserSwitchController$$ExternalSyntheticLambda1(multiUserSwitchController))).booleanValue();
        Objects.requireNonNull(footerActionsView);
        footerActionsView.post(new FooterActionsView$updateEverything$1(footerActionsView, booleanValue));
    }
}
