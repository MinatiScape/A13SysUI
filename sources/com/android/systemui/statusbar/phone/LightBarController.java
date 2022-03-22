package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.InsetsFlags;
import android.view.ViewDebug;
import com.android.internal.view.AppearanceRegion;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.policy.BatteryController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class LightBarController implements BatteryController.BatteryStateChangeCallback, Dumpable {
    public int mAppearance;
    public AppearanceRegion[] mAppearanceRegions = new AppearanceRegion[0];
    public final BatteryController mBatteryController;
    public BiometricUnlockController mBiometricUnlockController;
    public boolean mDirectReplying;
    public boolean mForceDarkForScrim;
    public boolean mHasLightNavigationBar;
    public boolean mNavbarColorManagedByIme;
    public LightBarTransitionsController mNavigationBarController;
    public int mNavigationBarMode;
    public boolean mNavigationLight;
    public int mNavigationMode;
    public boolean mQsCustomizing;
    public final SysuiDarkIconDispatcher mStatusBarIconController;
    public int mStatusBarMode;

    public static boolean isLight(int i, int i2, int i3) {
        return (i2 == 0 || i2 == 6) && ((i & i3) != 0);
    }

    public final void onStatusBarAppearanceChanged(AppearanceRegion[] appearanceRegionArr, boolean z, int i, boolean z2) {
        boolean z3;
        int length = appearanceRegionArr.length;
        if (this.mAppearanceRegions.length != length) {
            z3 = true;
        } else {
            z3 = false;
        }
        for (int i2 = 0; i2 < length && !z3; i2++) {
            z3 |= !appearanceRegionArr[i2].equals(this.mAppearanceRegions[i2]);
        }
        if (z3 || z) {
            this.mAppearanceRegions = appearanceRegionArr;
            onStatusBarModeChanged(i);
        }
        this.mNavbarColorManagedByIme = z2;
    }

    /* loaded from: classes.dex */
    public static class Factory {
        public final BatteryController mBatteryController;
        public final DarkIconDispatcher mDarkIconDispatcher;
        public final DumpManager mDumpManager;
        public final NavigationModeController mNavModeController;

        public Factory(DarkIconDispatcher darkIconDispatcher, BatteryController batteryController, NavigationModeController navigationModeController, DumpManager dumpManager) {
            this.mDarkIconDispatcher = darkIconDispatcher;
            this.mBatteryController = batteryController;
            this.mNavModeController = navigationModeController;
            this.mDumpManager = dumpManager;
        }
    }

    public final boolean animateChange() {
        BiometricUnlockController biometricUnlockController = this.mBiometricUnlockController;
        if (biometricUnlockController == null) {
            return false;
        }
        Objects.requireNonNull(biometricUnlockController);
        int i = biometricUnlockController.mMode;
        if (i == 2 || i == 1) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("LightBarController: ");
        printWriter.print(" mAppearance=");
        printWriter.println(ViewDebug.flagsToString(InsetsFlags.class, "appearance", this.mAppearance));
        int length = this.mAppearanceRegions.length;
        for (int i = 0; i < length; i++) {
            boolean isLight = isLight(this.mAppearanceRegions[i].getAppearance(), this.mStatusBarMode, 8);
            printWriter.print(" stack #");
            printWriter.print(i);
            printWriter.print(": ");
            printWriter.print(this.mAppearanceRegions[i].toString());
            printWriter.print(" isLight=");
            printWriter.println(isLight);
        }
        printWriter.print(" mNavigationLight=");
        printWriter.print(this.mNavigationLight);
        printWriter.print(" mHasLightNavigationBar=");
        printWriter.println(this.mHasLightNavigationBar);
        printWriter.print(" mStatusBarMode=");
        printWriter.print(this.mStatusBarMode);
        printWriter.print(" mNavigationBarMode=");
        printWriter.println(this.mNavigationBarMode);
        printWriter.print(" mForceDarkForScrim=");
        printWriter.print(this.mForceDarkForScrim);
        printWriter.print(" mQsCustomizing=");
        printWriter.print(this.mQsCustomizing);
        printWriter.print(" mDirectReplying=");
        printWriter.println(this.mDirectReplying);
        printWriter.print(" mNavbarColorManagedByIme=");
        printWriter.println(this.mNavbarColorManagedByIme);
        printWriter.println();
        LightBarTransitionsController transitionsController = this.mStatusBarIconController.getTransitionsController();
        if (transitionsController != null) {
            printWriter.println(" StatusBarTransitionsController:");
            transitionsController.dump(fileDescriptor, printWriter, strArr);
            printWriter.println();
        }
        if (this.mNavigationBarController != null) {
            printWriter.println(" NavigationBarTransitionsController:");
            this.mNavigationBarController.dump(fileDescriptor, printWriter, strArr);
            printWriter.println();
        }
    }

    public final void onNavigationBarAppearanceChanged(int i, boolean z, int i2, boolean z2) {
        boolean z3;
        LightBarTransitionsController lightBarTransitionsController;
        if (((this.mAppearance ^ i) & 16) != 0 || z) {
            boolean z4 = this.mNavigationLight;
            boolean isLight = isLight(i, i2, 16);
            this.mHasLightNavigationBar = isLight;
            if (!isLight || (((!this.mDirectReplying || !this.mNavbarColorManagedByIme) && this.mForceDarkForScrim) || this.mQsCustomizing)) {
                z3 = false;
            } else {
                z3 = true;
            }
            this.mNavigationLight = z3;
            if (!(z3 == z4 || (lightBarTransitionsController = this.mNavigationBarController) == null || !lightBarTransitionsController.supportsIconTintForNavMode(this.mNavigationMode))) {
                this.mNavigationBarController.setIconsDark(this.mNavigationLight, animateChange());
            }
        }
        this.mAppearance = i;
        this.mNavigationBarMode = i2;
        this.mNavbarColorManagedByIme = z2;
    }

    public final void onStatusBarModeChanged(int i) {
        this.mStatusBarMode = i;
        int length = this.mAppearanceRegions.length;
        ArrayList<Rect> arrayList = new ArrayList<>();
        for (int i2 = 0; i2 < length; i2++) {
            AppearanceRegion appearanceRegion = this.mAppearanceRegions[i2];
            if (isLight(appearanceRegion.getAppearance(), this.mStatusBarMode, 8)) {
                arrayList.add(appearanceRegion.getBounds());
            }
        }
        if (arrayList.isEmpty()) {
            this.mStatusBarIconController.getTransitionsController().setIconsDark(false, animateChange());
        } else if (arrayList.size() == length) {
            this.mStatusBarIconController.setIconsDarkArea(null);
            this.mStatusBarIconController.getTransitionsController().setIconsDark(true, animateChange());
        } else {
            this.mStatusBarIconController.setIconsDarkArea(arrayList);
            this.mStatusBarIconController.getTransitionsController().setIconsDark(true, animateChange());
        }
    }

    public final void reevaluate() {
        onStatusBarAppearanceChanged(this.mAppearanceRegions, true, this.mStatusBarMode, this.mNavbarColorManagedByIme);
        onNavigationBarAppearanceChanged(this.mAppearance, true, this.mNavigationBarMode, this.mNavbarColorManagedByIme);
    }

    public LightBarController(Context context, DarkIconDispatcher darkIconDispatcher, BatteryController batteryController, NavigationModeController navigationModeController, DumpManager dumpManager) {
        Color.valueOf(context.getColor(2131099796));
        this.mStatusBarIconController = (SysuiDarkIconDispatcher) darkIconDispatcher;
        this.mBatteryController = batteryController;
        batteryController.addCallback(this);
        this.mNavigationMode = navigationModeController.addListener(new NavigationModeController.ModeChangedListener() { // from class: com.android.systemui.statusbar.phone.LightBarController$$ExternalSyntheticLambda0
            @Override // com.android.systemui.navigationbar.NavigationModeController.ModeChangedListener
            public final void onNavigationModeChanged(int i) {
                LightBarController lightBarController = LightBarController.this;
                Objects.requireNonNull(lightBarController);
                lightBarController.mNavigationMode = i;
            }
        });
        if (context.getDisplayId() == 0) {
            dumpManager.registerDumpable("LightBarController", this);
        }
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
    public final void onPowerSaveChanged(boolean z) {
        reevaluate();
    }
}
