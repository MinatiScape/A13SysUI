package com.android.systemui.statusbar.phone;

import android.content.Intent;
import android.os.UserManager;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.DejankUtils;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.user.UserSwitchDialogController;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.user.UserSwitcherActivity;
import com.android.systemui.util.ViewController;
import java.util.Objects;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public final class MultiUserSwitchController extends ViewController<MultiUserSwitch> {
    public final ActivityStarter mActivityStarter;
    public final FalsingManager mFalsingManager;
    public final FeatureFlags mFeatureFlags;
    public final AnonymousClass1 mOnClickListener = new View.OnClickListener() { // from class: com.android.systemui.statusbar.phone.MultiUserSwitchController.1
        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            if (!MultiUserSwitchController.this.mFalsingManager.isFalseTap(1)) {
                if (MultiUserSwitchController.this.mFeatureFlags.isEnabled(Flags.FULL_SCREEN_USER_SWITCHER)) {
                    Intent intent = new Intent(view.getContext(), UserSwitcherActivity.class);
                    intent.addFlags(335544320);
                    MultiUserSwitchController.this.mActivityStarter.startActivity(intent, true, (ActivityLaunchAnimator.Controller) ActivityLaunchAnimator.Controller.fromView(view, null), true);
                    return;
                }
                MultiUserSwitchController.this.mUserSwitchDialogController.showDialog(view);
            }
        }
    };
    public AnonymousClass2 mUserListener;
    public final UserManager mUserManager;
    public final UserSwitchDialogController mUserSwitchDialogController;
    public final UserSwitcherController mUserSwitcherController;

    /* loaded from: classes.dex */
    public static class Factory {
        public final ActivityStarter mActivityStarter;
        public final FalsingManager mFalsingManager;
        public final FeatureFlags mFeatureFlags;
        public final UserManager mUserManager;
        public final UserSwitchDialogController mUserSwitchDialogController;
        public final UserSwitcherController mUserSwitcherController;

        public Factory(UserManager userManager, UserSwitcherController userSwitcherController, FalsingManager falsingManager, UserSwitchDialogController userSwitchDialogController, FeatureFlags featureFlags, ActivityStarter activityStarter) {
            this.mUserManager = userManager;
            this.mUserSwitcherController = userSwitcherController;
            this.mFalsingManager = falsingManager;
            this.mUserSwitchDialogController = userSwitchDialogController;
            this.mActivityStarter = activityStarter;
            this.mFeatureFlags = featureFlags;
        }
    }

    public final String getCurrentUser() {
        if (((Boolean) DejankUtils.whitelistIpcs(new Supplier() { // from class: com.android.systemui.statusbar.phone.MultiUserSwitchController$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                MultiUserSwitchController multiUserSwitchController = MultiUserSwitchController.this;
                Objects.requireNonNull(multiUserSwitchController);
                return Boolean.valueOf(multiUserSwitchController.mUserManager.isUserSwitcherEnabled());
            }
        })).booleanValue()) {
            return this.mUserSwitcherController.getCurrentUserName();
        }
        return null;
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [com.android.systemui.statusbar.phone.MultiUserSwitchController$2] */
    @Override // com.android.systemui.util.ViewController
    public final void onInit() {
        UserSwitcherController userSwitcherController;
        if (this.mUserManager.isUserSwitcherEnabled() && this.mUserListener == null && (userSwitcherController = this.mUserSwitcherController) != null) {
            this.mUserListener = new UserSwitcherController.BaseUserAdapter(userSwitcherController) { // from class: com.android.systemui.statusbar.phone.MultiUserSwitchController.2
                @Override // android.widget.Adapter
                public final View getView(int i, View view, ViewGroup viewGroup) {
                    return null;
                }

                @Override // android.widget.BaseAdapter
                public final void notifyDataSetChanged() {
                    MultiUserSwitchController multiUserSwitchController = MultiUserSwitchController.this;
                    ((MultiUserSwitch) multiUserSwitchController.mView).refreshContentDescription(multiUserSwitchController.getCurrentUser());
                }
            };
            ((MultiUserSwitch) this.mView).refreshContentDescription(getCurrentUser());
        }
        ((MultiUserSwitch) this.mView).refreshContentDescription(getCurrentUser());
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        ((MultiUserSwitch) this.mView).setOnClickListener(this.mOnClickListener);
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        ((MultiUserSwitch) this.mView).setOnClickListener(null);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.statusbar.phone.MultiUserSwitchController$1] */
    public MultiUserSwitchController(MultiUserSwitch multiUserSwitch, UserManager userManager, UserSwitcherController userSwitcherController, FalsingManager falsingManager, UserSwitchDialogController userSwitchDialogController, FeatureFlags featureFlags, ActivityStarter activityStarter) {
        super(multiUserSwitch);
        this.mUserManager = userManager;
        this.mUserSwitcherController = userSwitcherController;
        this.mFalsingManager = falsingManager;
        this.mUserSwitchDialogController = userSwitchDialogController;
        this.mFeatureFlags = featureFlags;
        this.mActivityStarter = activityStarter;
    }
}
