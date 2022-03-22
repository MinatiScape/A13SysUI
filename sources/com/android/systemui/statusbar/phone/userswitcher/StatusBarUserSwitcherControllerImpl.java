package com.android.systemui.statusbar.phone.userswitcher;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.GhostedViewLaunchAnimatorController;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.qs.user.UserSwitchDialogController;
import com.android.systemui.user.UserSwitcherActivity;
import com.android.systemui.util.ViewController;
import java.util.Objects;
/* compiled from: StatusBarUserSwitcherController.kt */
/* loaded from: classes.dex */
public final class StatusBarUserSwitcherControllerImpl extends ViewController<StatusBarUserSwitcherContainer> implements StatusBarUserSwitcherController {
    public final ActivityStarter activityStarter;
    public final StatusBarUserSwitcherFeatureController featureController;
    public final FeatureFlags featureFlags;
    public final StatusBarUserInfoTracker tracker;
    public final UserSwitchDialogController userSwitcherDialogController;
    public final StatusBarUserSwitcherControllerImpl$listener$1 listener = new CurrentUserChipInfoUpdatedListener() { // from class: com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherControllerImpl$listener$1
        @Override // com.android.systemui.statusbar.phone.userswitcher.CurrentUserChipInfoUpdatedListener
        public final void onCurrentUserChipInfoUpdated() {
            StatusBarUserSwitcherControllerImpl.this.updateChip();
        }

        @Override // com.android.systemui.statusbar.phone.userswitcher.CurrentUserChipInfoUpdatedListener
        public final void onStatusBarUserSwitcherSettingChanged() {
            StatusBarUserSwitcherControllerImpl.this.updateEnabled();
        }
    };
    public final StatusBarUserSwitcherControllerImpl$featureFlagListener$1 featureFlagListener = new OnUserSwitcherPreferenceChangeListener() { // from class: com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherControllerImpl$featureFlagListener$1
    };

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        this.tracker.addCallback((CurrentUserChipInfoUpdatedListener) this.listener);
        this.featureController.addCallback((OnUserSwitcherPreferenceChangeListener) this.featureFlagListener);
        ((StatusBarUserSwitcherContainer) this.mView).setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherControllerImpl$onViewAttached$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                if (StatusBarUserSwitcherControllerImpl.this.featureFlags.isEnabled(Flags.FULL_SCREEN_USER_SWITCHER)) {
                    Intent intent = new Intent(StatusBarUserSwitcherControllerImpl.this.getContext(), UserSwitcherActivity.class);
                    intent.addFlags(335544320);
                    ActivityStarter activityStarter = StatusBarUserSwitcherControllerImpl.this.activityStarter;
                    GhostedViewLaunchAnimatorController ghostedViewLaunchAnimatorController = null;
                    if (!(view.getParent() instanceof ViewGroup)) {
                        Log.wtf("ActivityLaunchAnimator", "Skipping animation as view " + view + " is not attached to a ViewGroup", new Exception());
                    } else {
                        ghostedViewLaunchAnimatorController = new GhostedViewLaunchAnimatorController(view, (Integer) null, 4);
                    }
                    activityStarter.startActivity(intent, true, (ActivityLaunchAnimator.Controller) ghostedViewLaunchAnimatorController, true);
                    return;
                }
                StatusBarUserSwitcherControllerImpl.this.userSwitcherDialogController.showDialog(view);
            }
        });
        updateEnabled();
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        StatusBarUserInfoTracker statusBarUserInfoTracker = this.tracker;
        StatusBarUserSwitcherControllerImpl$listener$1 statusBarUserSwitcherControllerImpl$listener$1 = this.listener;
        Objects.requireNonNull(statusBarUserInfoTracker);
        statusBarUserInfoTracker.listeners.remove(statusBarUserSwitcherControllerImpl$listener$1);
        if (statusBarUserInfoTracker.listeners.isEmpty()) {
            statusBarUserInfoTracker.listening = false;
            statusBarUserInfoTracker.userInfoController.removeCallback(statusBarUserInfoTracker.userInfoChangedListener);
        }
        StatusBarUserSwitcherFeatureController statusBarUserSwitcherFeatureController = this.featureController;
        StatusBarUserSwitcherControllerImpl$featureFlagListener$1 statusBarUserSwitcherControllerImpl$featureFlagListener$1 = this.featureFlagListener;
        Objects.requireNonNull(statusBarUserSwitcherFeatureController);
        statusBarUserSwitcherFeatureController.listeners.remove(statusBarUserSwitcherControllerImpl$featureFlagListener$1);
        ((StatusBarUserSwitcherContainer) this.mView).setOnClickListener(null);
    }

    public final void updateChip() {
        StatusBarUserSwitcherContainer statusBarUserSwitcherContainer = (StatusBarUserSwitcherContainer) this.mView;
        Objects.requireNonNull(statusBarUserSwitcherContainer);
        TextView textView = statusBarUserSwitcherContainer.text;
        ImageView imageView = null;
        if (textView == null) {
            textView = null;
        }
        StatusBarUserInfoTracker statusBarUserInfoTracker = this.tracker;
        Objects.requireNonNull(statusBarUserInfoTracker);
        textView.setText(statusBarUserInfoTracker.currentUserName);
        StatusBarUserSwitcherContainer statusBarUserSwitcherContainer2 = (StatusBarUserSwitcherContainer) this.mView;
        Objects.requireNonNull(statusBarUserSwitcherContainer2);
        ImageView imageView2 = statusBarUserSwitcherContainer2.avatar;
        if (imageView2 != null) {
            imageView = imageView2;
        }
        StatusBarUserInfoTracker statusBarUserInfoTracker2 = this.tracker;
        Objects.requireNonNull(statusBarUserInfoTracker2);
        imageView.setImageDrawable(statusBarUserInfoTracker2.currentUserAvatar);
    }

    public final void updateEnabled() {
        StatusBarUserSwitcherFeatureController statusBarUserSwitcherFeatureController = this.featureController;
        Objects.requireNonNull(statusBarUserSwitcherFeatureController);
        if (statusBarUserSwitcherFeatureController.flags.isEnabled(Flags.STATUS_BAR_USER_SWITCHER)) {
            StatusBarUserInfoTracker statusBarUserInfoTracker = this.tracker;
            Objects.requireNonNull(statusBarUserInfoTracker);
            if (statusBarUserInfoTracker.userSwitcherEnabled) {
                ((StatusBarUserSwitcherContainer) this.mView).setVisibility(0);
                updateChip();
                return;
            }
        }
        ((StatusBarUserSwitcherContainer) this.mView).setVisibility(8);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherControllerImpl$listener$1] */
    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherControllerImpl$featureFlagListener$1] */
    public StatusBarUserSwitcherControllerImpl(StatusBarUserSwitcherContainer statusBarUserSwitcherContainer, StatusBarUserInfoTracker statusBarUserInfoTracker, StatusBarUserSwitcherFeatureController statusBarUserSwitcherFeatureController, UserSwitchDialogController userSwitchDialogController, FeatureFlags featureFlags, ActivityStarter activityStarter) {
        super(statusBarUserSwitcherContainer);
        this.tracker = statusBarUserInfoTracker;
        this.featureController = statusBarUserSwitcherFeatureController;
        this.userSwitcherDialogController = userSwitchDialogController;
        this.featureFlags = featureFlags;
        this.activityStarter = activityStarter;
    }
}
