package com.android.wm.shell.startingsurface;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Trace;
import com.android.internal.graphics.palette.Palette;
import com.android.internal.graphics.palette.Quantizer;
import com.android.internal.graphics.palette.VariationalKMeansQuantizer;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import java.util.List;
import java.util.Objects;
import java.util.function.IntPredicate;
/* loaded from: classes.dex */
public final class SplashscreenContentDrawer$DrawableColorTester$ComplexDrawableTester implements SplashscreenContentDrawer$DrawableColorTester$ColorTester {
    public static final AlphaFilterQuantizer ALPHA_FILTER_QUANTIZER = new AlphaFilterQuantizer();
    public final boolean mFilterTransparent;
    public final Palette mPalette;

    /* loaded from: classes.dex */
    public static class AlphaFilterQuantizer implements Quantizer {
        public IntPredicate mFilter;
        public float mPassFilterRatio;
        public final SplashscreenContentDrawer$DrawableColorTester$ComplexDrawableTester$AlphaFilterQuantizer$$ExternalSyntheticLambda0 mTransparentFilter;
        public final VariationalKMeansQuantizer mInnerQuantizer = new VariationalKMeansQuantizer();
        public final SplashscreenContentDrawer$DrawableColorTester$ComplexDrawableTester$AlphaFilterQuantizer$$ExternalSyntheticLambda1 mTranslucentFilter = SplashscreenContentDrawer$DrawableColorTester$ComplexDrawableTester$AlphaFilterQuantizer$$ExternalSyntheticLambda1.INSTANCE;

        public final void quantize(int[] iArr, int i) {
            this.mPassFilterRatio = 0.0f;
            int i2 = 0;
            int i3 = 0;
            for (int length = iArr.length - 1; length > 0; length--) {
                if (this.mFilter.test(iArr[length])) {
                    i3++;
                }
            }
            if (i3 == 0) {
                if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
                    ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, -424415681, 0, null, null);
                }
                this.mInnerQuantizer.quantize(iArr, i);
                return;
            }
            this.mPassFilterRatio = i3 / iArr.length;
            int[] iArr2 = new int[i3];
            int length2 = iArr.length;
            while (true) {
                length2--;
                if (length2 <= 0) {
                    this.mInnerQuantizer.quantize(iArr2, i);
                    return;
                } else if (this.mFilter.test(iArr[length2])) {
                    iArr2[i2] = iArr[length2];
                    i2++;
                }
            }
        }

        public final List<Palette.Swatch> getQuantizedColors() {
            return this.mInnerQuantizer.getQuantizedColors();
        }

        public AlphaFilterQuantizer() {
            SplashscreenContentDrawer$DrawableColorTester$ComplexDrawableTester$AlphaFilterQuantizer$$ExternalSyntheticLambda0 splashscreenContentDrawer$DrawableColorTester$ComplexDrawableTester$AlphaFilterQuantizer$$ExternalSyntheticLambda0 = SplashscreenContentDrawer$DrawableColorTester$ComplexDrawableTester$AlphaFilterQuantizer$$ExternalSyntheticLambda0.INSTANCE;
            this.mTransparentFilter = splashscreenContentDrawer$DrawableColorTester$ComplexDrawableTester$AlphaFilterQuantizer$$ExternalSyntheticLambda0;
            this.mFilter = splashscreenContentDrawer$DrawableColorTester$ComplexDrawableTester$AlphaFilterQuantizer$$ExternalSyntheticLambda0;
        }
    }

    @Override // com.android.wm.shell.startingsurface.SplashscreenContentDrawer$DrawableColorTester$ColorTester
    public final int getDominantColor() {
        Palette.Swatch dominantSwatch = this.mPalette.getDominantSwatch();
        if (dominantSwatch != null) {
            return dominantSwatch.getInt();
        }
        return -16777216;
    }

    @Override // com.android.wm.shell.startingsurface.SplashscreenContentDrawer$DrawableColorTester$ColorTester
    public final boolean isComplexColor() {
        if (this.mPalette.getSwatches().size() > 1) {
            return true;
        }
        return false;
    }

    @Override // com.android.wm.shell.startingsurface.SplashscreenContentDrawer$DrawableColorTester$ColorTester
    public final boolean isGrayscale() {
        boolean z;
        List swatches = this.mPalette.getSwatches();
        if (swatches != null) {
            for (int size = swatches.size() - 1; size >= 0; size--) {
                int i = ((Palette.Swatch) swatches.get(size)).getInt();
                int red = Color.red(i);
                int green = Color.green(i);
                int blue = Color.blue(i);
                if (red == green && green == blue) {
                    z = true;
                } else {
                    z = false;
                }
                if (!z) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override // com.android.wm.shell.startingsurface.SplashscreenContentDrawer$DrawableColorTester$ColorTester
    public final float passFilterRatio() {
        if (this.mFilterTransparent) {
            return ALPHA_FILTER_QUANTIZER.mPassFilterRatio;
        }
        return 1.0f;
    }

    public SplashscreenContentDrawer$DrawableColorTester$ComplexDrawableTester(Drawable drawable, int i) {
        int i2;
        Palette.Builder builder;
        Trace.traceBegin(32L, "ComplexDrawableTester");
        Rect copyBounds = drawable.copyBounds();
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        int i3 = 40;
        if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
            i2 = 40;
        } else {
            i3 = Math.min(intrinsicWidth, 40);
            i2 = Math.min(intrinsicHeight, 40);
        }
        Bitmap createBitmap = Bitmap.createBitmap(i3, i2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        boolean z = false;
        drawable.setBounds(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
        drawable.draw(canvas);
        drawable.setBounds(copyBounds);
        z = i != 0 ? true : z;
        this.mFilterTransparent = z;
        if (z) {
            AlphaFilterQuantizer alphaFilterQuantizer = ALPHA_FILTER_QUANTIZER;
            Objects.requireNonNull(alphaFilterQuantizer);
            if (i != 2) {
                alphaFilterQuantizer.mFilter = alphaFilterQuantizer.mTransparentFilter;
            } else {
                alphaFilterQuantizer.mFilter = alphaFilterQuantizer.mTranslucentFilter;
            }
            builder = new Palette.Builder(createBitmap, alphaFilterQuantizer).maximumColorCount(5);
        } else {
            builder = new Palette.Builder(createBitmap, (Quantizer) null).maximumColorCount(5);
        }
        this.mPalette = builder.generate();
        createBitmap.recycle();
        Trace.traceEnd(32L);
    }
}
