package com.android.systemui.statusbar.phone;

import com.android.keyguard.KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.window.StatusBarWindowStateController;
import com.android.systemui.statusbar.window.StatusBarWindowStateListener;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: StatusBarHideIconsForBouncerManager.kt */
/* loaded from: classes.dex */
public final class StatusBarHideIconsForBouncerManager implements Dumpable {
    public boolean bouncerShowing;
    public boolean bouncerWasShowingWhenHidden;
    public final CommandQueue commandQueue;
    public int displayId;
    public boolean hideIconsForBouncer;
    public boolean isOccluded;
    public final DelayableExecutor mainExecutor;
    public boolean panelExpanded;
    public boolean statusBarWindowHidden;
    public boolean topAppHidesStatusBar;
    public boolean wereIconsJustHidden;

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("---- State variables set externally ----");
        KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0.m(this.panelExpanded, "panelExpanded=", printWriter);
        KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0.m(this.isOccluded, "isOccluded=", printWriter);
        KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0.m(this.bouncerShowing, "bouncerShowing=", printWriter);
        KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0.m(this.topAppHidesStatusBar, "topAppHideStatusBar=", printWriter);
        KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0.m(this.statusBarWindowHidden, "statusBarWindowHidden=", printWriter);
        printWriter.println(Intrinsics.stringPlus("displayId=", Integer.valueOf(this.displayId)));
        printWriter.println("---- State variables calculated internally ----");
        KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0.m(this.hideIconsForBouncer, "hideIconsForBouncer=", printWriter);
        KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0.m(this.bouncerWasShowingWhenHidden, "bouncerWasShowingWhenHidden=", printWriter);
        KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0.m(this.wereIconsJustHidden, "wereIconsJustHidden=", printWriter);
    }

    public final void updateHideIconsForBouncer(boolean z) {
        boolean z2;
        boolean z3;
        boolean z4 = false;
        if (!this.topAppHidesStatusBar || !this.isOccluded || (!this.statusBarWindowHidden && !this.bouncerShowing)) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (this.panelExpanded || this.isOccluded || !this.bouncerShowing) {
            z3 = false;
        } else {
            z3 = true;
        }
        if (z2 || z3) {
            z4 = true;
        }
        if (this.hideIconsForBouncer != z4) {
            this.hideIconsForBouncer = z4;
            if (z4 || !this.bouncerWasShowingWhenHidden) {
                this.commandQueue.recomputeDisableFlags(this.displayId, z);
            } else {
                this.wereIconsJustHidden = true;
                this.mainExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBarHideIconsForBouncerManager$updateHideIconsForBouncer$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        StatusBarHideIconsForBouncerManager statusBarHideIconsForBouncerManager = StatusBarHideIconsForBouncerManager.this;
                        statusBarHideIconsForBouncerManager.wereIconsJustHidden = false;
                        statusBarHideIconsForBouncerManager.commandQueue.recomputeDisableFlags(statusBarHideIconsForBouncerManager.displayId, true);
                    }
                }, 500L);
            }
        }
        if (z4) {
            this.bouncerWasShowingWhenHidden = this.bouncerShowing;
        }
    }

    public StatusBarHideIconsForBouncerManager(CommandQueue commandQueue, DelayableExecutor delayableExecutor, StatusBarWindowStateController statusBarWindowStateController, DumpManager dumpManager) {
        this.commandQueue = commandQueue;
        this.mainExecutor = delayableExecutor;
        dumpManager.registerDumpable(this);
        StatusBarWindowStateListener statusBarWindowStateListener = new StatusBarWindowStateListener() { // from class: com.android.systemui.statusbar.phone.StatusBarHideIconsForBouncerManager.1
            @Override // com.android.systemui.statusbar.window.StatusBarWindowStateListener
            public final void onStatusBarWindowStateChanged(int i) {
                boolean z;
                StatusBarHideIconsForBouncerManager statusBarHideIconsForBouncerManager = StatusBarHideIconsForBouncerManager.this;
                Objects.requireNonNull(statusBarHideIconsForBouncerManager);
                if (i == 2) {
                    z = true;
                } else {
                    z = false;
                }
                statusBarHideIconsForBouncerManager.statusBarWindowHidden = z;
                statusBarHideIconsForBouncerManager.updateHideIconsForBouncer(false);
            }
        };
        Objects.requireNonNull(statusBarWindowStateController);
        statusBarWindowStateController.listeners.add(statusBarWindowStateListener);
    }
}
