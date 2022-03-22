package com.google.android.systemui.theme;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline0;
import com.android.systemui.theme.ThemeOverlayController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* compiled from: ThemeOverlayControllerGoogle.kt */
/* loaded from: classes.dex */
public final class ThemeOverlayControllerGoogle extends ThemeOverlayController {
    @Override // com.android.systemui.theme.ThemeOverlayController, com.android.systemui.CoreStartable, com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("mSystemColors=");
        m.append(this.mCurrentColors);
        printWriter.println(m.toString());
        printWriter.println("mMainWallpaperColor=" + Integer.toHexString(this.mMainWallpaperColor));
        printWriter.println("mSecondaryOverlay=" + this.mSecondaryOverlay);
        printWriter.println("mNeutralOverlay=" + this.mNeutralOverlay);
        StringBuilder sb = new StringBuilder();
        sb.append("mIsMonetEnabled=");
        StringBuilder m2 = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(sb, this.mIsMonetEnabled, printWriter, "mColorScheme=");
        m2.append(this.mColorScheme);
        printWriter.println(m2.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("mNeedsOverlayCreation=");
        StringBuilder m3 = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(sb2, this.mNeedsOverlayCreation, printWriter, "mAcceptColorEvents="), this.mAcceptColorEvents, printWriter, "mDeferredThemeEvaluation="), this.mDeferredThemeEvaluation, printWriter, "mThemeStyle=");
        m3.append(this.mThemeStyle);
        printWriter.println(m3.toString());
        printWriter.println("ThemeOverlayControllerGoogle: yes");
    }
}
