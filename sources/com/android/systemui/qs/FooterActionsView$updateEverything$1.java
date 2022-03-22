package com.android.systemui.qs;

import android.os.UserManager;
import android.view.View;
import com.android.systemui.statusbar.phone.MultiUserSwitch;
import com.android.systemui.statusbar.phone.SettingsButton;
import java.util.Objects;
/* compiled from: FooterActionsView.kt */
/* loaded from: classes.dex */
public final class FooterActionsView$updateEverything$1 implements Runnable {
    public final /* synthetic */ boolean $multiUserEnabled;
    public final /* synthetic */ FooterActionsView this$0;

    public FooterActionsView$updateEverything$1(FooterActionsView footerActionsView, boolean z) {
        this.this$0 = footerActionsView;
        this.$multiUserEnabled = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        int i;
        int i2;
        MultiUserSwitch multiUserSwitch;
        boolean z;
        SettingsButton settingsButton;
        FooterActionsView footerActionsView = this.this$0;
        boolean z2 = this.$multiUserEnabled;
        int i3 = FooterActionsView.$r8$clinit;
        Objects.requireNonNull(footerActionsView);
        View view = footerActionsView.settingsContainer;
        SettingsButton settingsButton2 = null;
        if (view == null) {
            view = null;
        }
        int i4 = 8;
        if (footerActionsView.qsDisabled) {
            i = 8;
        } else {
            i = 0;
        }
        view.setVisibility(i);
        MultiUserSwitch multiUserSwitch2 = footerActionsView.multiUserSwitch;
        if (multiUserSwitch2 == null) {
            multiUserSwitch2 = null;
        }
        if (z2) {
            i4 = 0;
        }
        multiUserSwitch2.setVisibility(i4);
        boolean isDeviceInDemoMode = UserManager.isDeviceInDemoMode(footerActionsView.getContext());
        SettingsButton settingsButton3 = footerActionsView.settingsButton;
        if (settingsButton3 == null) {
            settingsButton3 = null;
        }
        if (isDeviceInDemoMode) {
            i2 = 4;
        } else {
            i2 = 0;
        }
        settingsButton3.setVisibility(i2);
        FooterActionsView footerActionsView2 = this.this$0;
        Objects.requireNonNull(footerActionsView2);
        MultiUserSwitch multiUserSwitch3 = footerActionsView2.multiUserSwitch;
        if (multiUserSwitch3 == null) {
            multiUserSwitch = null;
        } else {
            multiUserSwitch = multiUserSwitch3;
        }
        if (multiUserSwitch3 == null) {
            multiUserSwitch3 = null;
        }
        boolean z3 = true;
        if (multiUserSwitch3.getVisibility() == 0) {
            z = true;
        } else {
            z = false;
        }
        multiUserSwitch.setClickable(z);
        SettingsButton settingsButton4 = footerActionsView2.settingsButton;
        if (settingsButton4 == null) {
            settingsButton = null;
        } else {
            settingsButton = settingsButton4;
        }
        if (settingsButton4 != null) {
            settingsButton2 = settingsButton4;
        }
        if (settingsButton2.getVisibility() != 0) {
            z3 = false;
        }
        settingsButton.setClickable(z3);
        this.this$0.setClickable(false);
    }
}
