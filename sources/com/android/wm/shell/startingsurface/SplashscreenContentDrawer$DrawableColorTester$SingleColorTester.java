package com.android.wm.shell.startingsurface;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
/* loaded from: classes.dex */
public final class SplashscreenContentDrawer$DrawableColorTester$SingleColorTester implements SplashscreenContentDrawer$DrawableColorTester$ColorTester {
    public final ColorDrawable mColorDrawable;

    @Override // com.android.wm.shell.startingsurface.SplashscreenContentDrawer$DrawableColorTester$ColorTester
    public final boolean isComplexColor() {
        return false;
    }

    @Override // com.android.wm.shell.startingsurface.SplashscreenContentDrawer$DrawableColorTester$ColorTester
    public final int getDominantColor() {
        return this.mColorDrawable.getColor();
    }

    @Override // com.android.wm.shell.startingsurface.SplashscreenContentDrawer$DrawableColorTester$ColorTester
    public final boolean isGrayscale() {
        int color = this.mColorDrawable.getColor();
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        if (red == green && green == blue) {
            return true;
        }
        return false;
    }

    @Override // com.android.wm.shell.startingsurface.SplashscreenContentDrawer$DrawableColorTester$ColorTester
    public final float passFilterRatio() {
        return this.mColorDrawable.getAlpha() / 255;
    }

    public SplashscreenContentDrawer$DrawableColorTester$SingleColorTester(ColorDrawable colorDrawable) {
        this.mColorDrawable = colorDrawable;
    }
}
