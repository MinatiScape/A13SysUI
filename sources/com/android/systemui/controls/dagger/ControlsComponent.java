package com.android.systemui.controls.dagger;

import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.controller.ControlsTileResourceConfiguration;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.ui.ControlsUiController;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.settings.SecureSettings;
import dagger.Lazy;
import java.util.Optional;
/* compiled from: ControlsComponent.kt */
/* loaded from: classes.dex */
public final class ControlsComponent {
    public boolean canShowWhileLockedSetting;
    public final ControlsTileResourceConfiguration controlsTileResourceConfiguration;
    public final boolean featureEnabled;
    public final KeyguardStateController keyguardStateController;
    public final Lazy<ControlsController> lazyControlsController;
    public final Lazy<ControlsListingController> lazyControlsListingController;
    public final Lazy<ControlsUiController> lazyControlsUiController;
    public final LockPatternUtils lockPatternUtils;
    public final SecureSettings secureSettings;
    public final ControlsComponent$showWhileLockedObserver$1 showWhileLockedObserver;
    public final UserTracker userTracker;

    /* compiled from: ControlsComponent.kt */
    /* loaded from: classes.dex */
    public enum Visibility {
        AVAILABLE,
        AVAILABLE_AFTER_UNLOCK,
        UNAVAILABLE
    }

    public final Optional<ControlsController> getControlsController() {
        if (this.featureEnabled) {
            return Optional.of(this.lazyControlsController.get());
        }
        return Optional.empty();
    }

    public final Optional<ControlsListingController> getControlsListingController() {
        if (this.featureEnabled) {
            return Optional.of(this.lazyControlsListingController.get());
        }
        return Optional.empty();
    }

    public final Visibility getVisibility() {
        Visibility visibility = Visibility.AVAILABLE_AFTER_UNLOCK;
        if (!this.featureEnabled) {
            return Visibility.UNAVAILABLE;
        }
        if (this.lockPatternUtils.getStrongAuthForUser(this.userTracker.getUserHandle().getIdentifier()) == 1) {
            return visibility;
        }
        if (this.canShowWhileLockedSetting || this.keyguardStateController.isUnlocked()) {
            return Visibility.AVAILABLE;
        }
        return visibility;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v4, types: [com.android.systemui.controls.dagger.ControlsComponent$showWhileLockedObserver$1, android.database.ContentObserver] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public ControlsComponent(boolean r1, dagger.Lazy r2, dagger.Lazy r3, dagger.Lazy r4, com.android.internal.widget.LockPatternUtils r5, com.android.systemui.statusbar.policy.KeyguardStateController r6, com.android.systemui.settings.UserTracker r7, com.android.systemui.util.settings.SecureSettings r8, java.util.Optional r9) {
        /*
            r0 = this;
            r0.<init>()
            r0.featureEnabled = r1
            r0.lazyControlsController = r2
            r0.lazyControlsUiController = r3
            r0.lazyControlsListingController = r4
            r0.lockPatternUtils = r5
            r0.keyguardStateController = r6
            r0.userTracker = r7
            r0.secureSettings = r8
            com.android.systemui.controls.controller.ControlsTileResourceConfigurationImpl r2 = new com.android.systemui.controls.controller.ControlsTileResourceConfigurationImpl
            r2.<init>()
            java.lang.Object r2 = r9.orElse(r2)
            com.android.systemui.controls.controller.ControlsTileResourceConfiguration r2 = (com.android.systemui.controls.controller.ControlsTileResourceConfiguration) r2
            r0.controlsTileResourceConfiguration = r2
            com.android.systemui.controls.dagger.ControlsComponent$showWhileLockedObserver$1 r2 = new com.android.systemui.controls.dagger.ControlsComponent$showWhileLockedObserver$1
            r2.<init>()
            r0.showWhileLockedObserver = r2
            if (r1 == 0) goto L_0x003c
            java.lang.String r1 = "lockscreen_show_controls"
            android.net.Uri r3 = android.provider.Settings.Secure.getUriFor(r1)
            r4 = 0
            r8.registerContentObserver(r3, r4, r2)
            int r1 = r8.getInt(r1, r4)
            if (r1 == 0) goto L_0x003a
            r4 = 1
        L_0x003a:
            r0.canShowWhileLockedSetting = r4
        L_0x003c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.dagger.ControlsComponent.<init>(boolean, dagger.Lazy, dagger.Lazy, dagger.Lazy, com.android.internal.widget.LockPatternUtils, com.android.systemui.statusbar.policy.KeyguardStateController, com.android.systemui.settings.UserTracker, com.android.systemui.util.settings.SecureSettings, java.util.Optional):void");
    }
}
