package com.android.systemui.colorextraction;

import android.app.WallpaperColors;
import android.app.WallpaperManager;
import android.content.Context;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.internal.colorextraction.types.ExtractionType;
import com.android.internal.colorextraction.types.Tonal;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.LockIconView$$ExternalSyntheticOutline0;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;
/* loaded from: classes.dex */
public final class SysuiColorExtractor extends ColorExtractor implements Dumpable, ConfigurationController.ConfigurationListener {
    public final ColorExtractor.GradientColors mBackdropColors;
    public boolean mHasMediaArtwork;
    public final ColorExtractor.GradientColors mNeutralColorsLock;
    public final Tonal mTonal;

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("SysuiColorExtractor:");
        printWriter.println("  Current wallpaper colors:");
        printWriter.println("    system: " + ((ColorExtractor) this).mSystemColors);
        printWriter.println("    lock: " + ((ColorExtractor) this).mLockColors);
        StringBuilder m = LockIconView$$ExternalSyntheticOutline0.m(printWriter, "  Gradients:", "    system: ");
        m.append(Arrays.toString((ColorExtractor.GradientColors[]) ((ColorExtractor) this).mGradientColors.get(1)));
        printWriter.println(m.toString());
        printWriter.println("    lock: " + Arrays.toString((ColorExtractor.GradientColors[]) ((ColorExtractor) this).mGradientColors.get(2)));
        printWriter.println("  Neutral colors: " + this.mNeutralColorsLock);
        printWriter.println("  Has media backdrop: " + this.mHasMediaArtwork);
    }

    public final ColorExtractor.GradientColors getColors(int i, int i2) {
        if (!this.mHasMediaArtwork || (i & 2) == 0) {
            return SysuiColorExtractor.super.getColors(i, i2);
        }
        return this.mBackdropColors;
    }

    public final void setHasMediaArtwork(boolean z) {
        if (this.mHasMediaArtwork != z) {
            this.mHasMediaArtwork = z;
            triggerColorsChanged(2);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @VisibleForTesting
    public SysuiColorExtractor(Context context, ExtractionType extractionType, ConfigurationController configurationController, WallpaperManager wallpaperManager, DumpManager dumpManager, boolean z) {
        super(context, extractionType, z, wallpaperManager);
        Tonal tonal;
        if (extractionType instanceof Tonal) {
            tonal = (Tonal) extractionType;
        } else {
            tonal = new Tonal(context);
        }
        this.mTonal = tonal;
        this.mNeutralColorsLock = new ColorExtractor.GradientColors();
        configurationController.addCallback(this);
        dumpManager.registerDumpable("SysuiColorExtractor", this);
        ColorExtractor.GradientColors gradientColors = new ColorExtractor.GradientColors();
        this.mBackdropColors = gradientColors;
        gradientColors.setMainColor(-16777216);
        if (wallpaperManager.isWallpaperSupported()) {
            wallpaperManager.removeOnColorsChangedListener(this);
            wallpaperManager.addOnColorsChangedListener(this, null, -1);
        }
    }

    public final void extractWallpaperColors() {
        ColorExtractor.GradientColors gradientColors;
        SysuiColorExtractor.super.extractWallpaperColors();
        Tonal tonal = this.mTonal;
        if (tonal != null && (gradientColors = this.mNeutralColorsLock) != null) {
            WallpaperColors wallpaperColors = ((ColorExtractor) this).mLockColors;
            if (wallpaperColors == null) {
                wallpaperColors = ((ColorExtractor) this).mSystemColors;
            }
            tonal.applyFallback(wallpaperColors, gradientColors);
        }
    }

    public final void onColorsChanged(WallpaperColors wallpaperColors, int i, int i2) {
        if (i2 == KeyguardUpdateMonitor.getCurrentUser()) {
            if ((i & 2) != 0) {
                this.mTonal.applyFallback(wallpaperColors, this.mNeutralColorsLock);
            }
            onColorsChanged(wallpaperColors, i);
        }
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public final void onUiModeChanged() {
        extractWallpaperColors();
        triggerColorsChanged(3);
    }
}
