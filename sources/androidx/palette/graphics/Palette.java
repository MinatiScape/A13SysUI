package androidx.palette.graphics;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.SparseBooleanArray;
import androidx.collection.SimpleArrayMap;
import androidx.core.graphics.ColorUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class Palette {
    public static final AnonymousClass1 DEFAULT_FILTER = new Filter() { // from class: androidx.palette.graphics.Palette.1
        @Override // androidx.palette.graphics.Palette.Filter
        public final boolean isAllowed(float[] fArr) {
            boolean z;
            boolean z2;
            boolean z3;
            if (fArr[2] >= 0.95f) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                if (fArr[2] <= 0.05f) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (!z2) {
                    if (fArr[0] < 10.0f || fArr[0] > 37.0f || fArr[1] > 0.82f) {
                        z3 = false;
                    } else {
                        z3 = true;
                    }
                    if (!z3) {
                        return true;
                    }
                }
            }
            return false;
        }
    };
    public final Swatch mDominantSwatch;
    public final List<Swatch> mSwatches;
    public final List<Target> mTargets;
    public final SparseBooleanArray mUsedColors = new SparseBooleanArray();
    public final SimpleArrayMap<Target, Swatch> mSelectedSwatches = new SimpleArrayMap<>();

    /* loaded from: classes.dex */
    public static final class Builder {
        public final Bitmap mBitmap;
        public final ArrayList mFilters;
        public Rect mRegion;
        public final ArrayList mTargets;
        public int mMaxColors = 16;
        public int mResizeArea = 12544;
        public int mResizeMaxDimension = -1;

        /* JADX WARN: Removed duplicated region for block: B:66:0x01a7  */
        /* JADX WARN: Removed duplicated region for block: B:87:0x020b  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final androidx.palette.graphics.Palette generate() {
            /*
                Method dump skipped, instructions count: 559
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.palette.graphics.Palette.Builder.generate():androidx.palette.graphics.Palette");
        }

        public Builder(Bitmap bitmap) {
            ArrayList arrayList = new ArrayList();
            this.mTargets = arrayList;
            ArrayList arrayList2 = new ArrayList();
            this.mFilters = arrayList2;
            if (!bitmap.isRecycled()) {
                arrayList2.add(Palette.DEFAULT_FILTER);
                this.mBitmap = bitmap;
                arrayList.add(Target.LIGHT_VIBRANT);
                arrayList.add(Target.VIBRANT);
                arrayList.add(Target.DARK_VIBRANT);
                arrayList.add(Target.LIGHT_MUTED);
                arrayList.add(Target.MUTED);
                arrayList.add(Target.DARK_MUTED);
                return;
            }
            throw new IllegalArgumentException("Bitmap is not valid");
        }
    }

    /* loaded from: classes.dex */
    public interface Filter {
        boolean isAllowed(float[] fArr);
    }

    /* loaded from: classes.dex */
    public static final class Swatch {
        public final int mBlue;
        public int mBodyTextColor;
        public boolean mGeneratedTextColors;
        public final int mGreen;
        public float[] mHsl;
        public final int mPopulation;
        public final int mRed;
        public final int mRgb;
        public int mTitleTextColor;

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || Swatch.class != obj.getClass()) {
                return false;
            }
            Swatch swatch = (Swatch) obj;
            return this.mPopulation == swatch.mPopulation && this.mRgb == swatch.mRgb;
        }

        public final void ensureTextColorsGenerated() {
            int i;
            int i2;
            if (!this.mGeneratedTextColors) {
                int calculateMinimumAlpha = ColorUtils.calculateMinimumAlpha(-1, this.mRgb, 4.5f);
                int calculateMinimumAlpha2 = ColorUtils.calculateMinimumAlpha(-1, this.mRgb, 3.0f);
                if (calculateMinimumAlpha == -1 || calculateMinimumAlpha2 == -1) {
                    int calculateMinimumAlpha3 = ColorUtils.calculateMinimumAlpha(-16777216, this.mRgb, 4.5f);
                    int calculateMinimumAlpha4 = ColorUtils.calculateMinimumAlpha(-16777216, this.mRgb, 3.0f);
                    if (calculateMinimumAlpha3 == -1 || calculateMinimumAlpha4 == -1) {
                        if (calculateMinimumAlpha != -1) {
                            i = ColorUtils.setAlphaComponent(-1, calculateMinimumAlpha);
                        } else {
                            i = ColorUtils.setAlphaComponent(-16777216, calculateMinimumAlpha3);
                        }
                        this.mBodyTextColor = i;
                        if (calculateMinimumAlpha2 != -1) {
                            i2 = ColorUtils.setAlphaComponent(-1, calculateMinimumAlpha2);
                        } else {
                            i2 = ColorUtils.setAlphaComponent(-16777216, calculateMinimumAlpha4);
                        }
                        this.mTitleTextColor = i2;
                        this.mGeneratedTextColors = true;
                        return;
                    }
                    this.mBodyTextColor = ColorUtils.setAlphaComponent(-16777216, calculateMinimumAlpha3);
                    this.mTitleTextColor = ColorUtils.setAlphaComponent(-16777216, calculateMinimumAlpha4);
                    this.mGeneratedTextColors = true;
                    return;
                }
                this.mBodyTextColor = ColorUtils.setAlphaComponent(-1, calculateMinimumAlpha);
                this.mTitleTextColor = ColorUtils.setAlphaComponent(-1, calculateMinimumAlpha2);
                this.mGeneratedTextColors = true;
            }
        }

        public final float[] getHsl() {
            if (this.mHsl == null) {
                this.mHsl = new float[3];
            }
            ColorUtils.RGBToHSL(this.mRed, this.mGreen, this.mBlue, this.mHsl);
            return this.mHsl;
        }

        public final int hashCode() {
            return (this.mRgb * 31) + this.mPopulation;
        }

        public final String toString() {
            StringBuilder sb = new StringBuilder(Swatch.class.getSimpleName());
            sb.append(" [RGB: #");
            sb.append(Integer.toHexString(this.mRgb));
            sb.append(']');
            sb.append(" [HSL: ");
            sb.append(Arrays.toString(getHsl()));
            sb.append(']');
            sb.append(" [Population: ");
            sb.append(this.mPopulation);
            sb.append(']');
            sb.append(" [Title Text: #");
            ensureTextColorsGenerated();
            sb.append(Integer.toHexString(this.mTitleTextColor));
            sb.append(']');
            sb.append(" [Body Text: #");
            ensureTextColorsGenerated();
            sb.append(Integer.toHexString(this.mBodyTextColor));
            sb.append(']');
            return sb.toString();
        }

        public Swatch(int i, int i2) {
            this.mRed = Color.red(i);
            this.mGreen = Color.green(i);
            this.mBlue = Color.blue(i);
            this.mRgb = i;
            this.mPopulation = i2;
        }
    }

    public Palette(ArrayList arrayList, ArrayList arrayList2) {
        this.mSwatches = arrayList;
        this.mTargets = arrayList2;
        int size = arrayList.size();
        int i = Integer.MIN_VALUE;
        Swatch swatch = null;
        for (int i2 = 0; i2 < size; i2++) {
            Swatch swatch2 = this.mSwatches.get(i2);
            Objects.requireNonNull(swatch2);
            int i3 = swatch2.mPopulation;
            if (i3 > i) {
                swatch = swatch2;
                i = i3;
            }
        }
        this.mDominantSwatch = swatch;
    }
}
