package androidx.palette.graphics;

import android.graphics.Color;
import androidx.core.graphics.ColorUtils;
import androidx.palette.graphics.Palette;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.PriorityQueue;
/* loaded from: classes.dex */
public final class ColorCutQuantizer {
    public static final AnonymousClass1 VBOX_COMPARATOR_VOLUME = new Comparator<Vbox>() { // from class: androidx.palette.graphics.ColorCutQuantizer.1
        @Override // java.util.Comparator
        public final int compare(Vbox vbox, Vbox vbox2) {
            Vbox vbox3 = vbox;
            Vbox vbox4 = vbox2;
            Objects.requireNonNull(vbox4);
            int i = ((vbox4.mMaxBlue - vbox4.mMinBlue) + 1) * ((vbox4.mMaxGreen - vbox4.mMinGreen) + 1) * ((vbox4.mMaxRed - vbox4.mMinRed) + 1);
            Objects.requireNonNull(vbox3);
            return i - (((vbox3.mMaxBlue - vbox3.mMinBlue) + 1) * (((vbox3.mMaxGreen - vbox3.mMinGreen) + 1) * ((vbox3.mMaxRed - vbox3.mMinRed) + 1)));
        }
    };
    public final int[] mColors;
    public final Palette.Filter[] mFilters;
    public final int[] mHistogram;
    public final ArrayList mQuantizedColors;
    public final float[] mTempHsl = new float[3];

    /* loaded from: classes.dex */
    public class Vbox {
        public int mLowerIndex;
        public int mMaxBlue;
        public int mMaxGreen;
        public int mMaxRed;
        public int mMinBlue;
        public int mMinGreen;
        public int mMinRed;
        public int mPopulation;
        public int mUpperIndex;

        public Vbox(int i, int i2) {
            this.mLowerIndex = i;
            this.mUpperIndex = i2;
            fitBox();
        }

        public final void fitBox() {
            ColorCutQuantizer colorCutQuantizer = ColorCutQuantizer.this;
            int[] iArr = colorCutQuantizer.mColors;
            int[] iArr2 = colorCutQuantizer.mHistogram;
            int i = Integer.MAX_VALUE;
            int i2 = Integer.MIN_VALUE;
            int i3 = Integer.MIN_VALUE;
            int i4 = Integer.MIN_VALUE;
            int i5 = 0;
            int i6 = Integer.MAX_VALUE;
            int i7 = Integer.MAX_VALUE;
            for (int i8 = this.mLowerIndex; i8 <= this.mUpperIndex; i8++) {
                int i9 = iArr[i8];
                i5 += iArr2[i9];
                int i10 = (i9 >> 10) & 31;
                int i11 = (i9 >> 5) & 31;
                int i12 = i9 & 31;
                if (i10 > i2) {
                    i2 = i10;
                }
                if (i10 < i) {
                    i = i10;
                }
                if (i11 > i3) {
                    i3 = i11;
                }
                if (i11 < i6) {
                    i6 = i11;
                }
                if (i12 > i4) {
                    i4 = i12;
                }
                if (i12 < i7) {
                    i7 = i12;
                }
            }
            this.mMinRed = i;
            this.mMaxRed = i2;
            this.mMinGreen = i6;
            this.mMaxGreen = i3;
            this.mMinBlue = i7;
            this.mMaxBlue = i4;
            this.mPopulation = i5;
        }
    }

    public static void modifySignificantOctet(int[] iArr, int i, int i2, int i3) {
        if (i == -2) {
            while (i2 <= i3) {
                int i4 = iArr[i2];
                iArr[i2] = (i4 & 31) | (((i4 >> 5) & 31) << 10) | (((i4 >> 10) & 31) << 5);
                i2++;
            }
        } else if (i == -1) {
            while (i2 <= i3) {
                int i5 = iArr[i2];
                iArr[i2] = ((i5 >> 10) & 31) | ((i5 & 31) << 10) | (((i5 >> 5) & 31) << 5);
                i2++;
            }
        }
    }

    public static int modifyWordWidth(int i, int i2, int i3) {
        return (i3 > i2 ? i << (i3 - i2) : i >> (i2 - i3)) & ((1 << i3) - 1);
    }

    public ColorCutQuantizer(int[] iArr, int i, Palette.Filter[] filterArr) {
        boolean z;
        Vbox vbox;
        boolean z2;
        boolean z3;
        int i2;
        int i3;
        boolean z4;
        this.mFilters = filterArr;
        int[] iArr2 = new int[32768];
        this.mHistogram = iArr2;
        for (int i4 = 0; i4 < iArr.length; i4++) {
            int i5 = iArr[i4];
            int modifyWordWidth = modifyWordWidth(Color.blue(i5), 8, 5) | (modifyWordWidth(Color.red(i5), 8, 5) << 10) | (modifyWordWidth(Color.green(i5), 8, 5) << 5);
            iArr[i4] = modifyWordWidth;
            iArr2[modifyWordWidth] = iArr2[modifyWordWidth] + 1;
        }
        int i6 = 0;
        for (int i7 = 0; i7 < 32768; i7++) {
            if (iArr2[i7] > 0) {
                int rgb = Color.rgb(modifyWordWidth((i7 >> 10) & 31, 5, 8), modifyWordWidth((i7 >> 5) & 31, 5, 8), modifyWordWidth(i7 & 31, 5, 8));
                float[] fArr = this.mTempHsl;
                ThreadLocal<double[]> threadLocal = ColorUtils.TEMP_ARRAY;
                ColorUtils.RGBToHSL(Color.red(rgb), Color.green(rgb), Color.blue(rgb), fArr);
                float[] fArr2 = this.mTempHsl;
                Palette.Filter[] filterArr2 = this.mFilters;
                if (filterArr2 != null && filterArr2.length > 0) {
                    int length = filterArr2.length;
                    for (int i8 = 0; i8 < length; i8++) {
                        if (!this.mFilters[i8].isAllowed(fArr2)) {
                            z4 = true;
                            break;
                        }
                    }
                }
                z4 = false;
                if (z4) {
                    iArr2[i7] = 0;
                }
            }
            if (iArr2[i7] > 0) {
                i6++;
            }
        }
        int[] iArr3 = new int[i6];
        this.mColors = iArr3;
        int i9 = 0;
        for (int i10 = 0; i10 < 32768; i10++) {
            if (iArr2[i10] > 0) {
                iArr3[i9] = i10;
                i9++;
            }
        }
        if (i6 <= i) {
            this.mQuantizedColors = new ArrayList();
            for (int i11 = 0; i11 < i6; i11++) {
                int i12 = iArr3[i11];
                this.mQuantizedColors.add(new Palette.Swatch(Color.rgb(modifyWordWidth((i12 >> 10) & 31, 5, 8), modifyWordWidth((i12 >> 5) & 31, 5, 8), modifyWordWidth(i12 & 31, 5, 8)), iArr2[i12]));
            }
            return;
        }
        PriorityQueue priorityQueue = new PriorityQueue(i, VBOX_COMPARATOR_VOLUME);
        priorityQueue.offer(new Vbox(0, this.mColors.length - 1));
        while (priorityQueue.size() < i && (vbox = (Vbox) priorityQueue.poll()) != null) {
            int i13 = vbox.mUpperIndex;
            int i14 = vbox.mLowerIndex;
            if ((i13 + 1) - i14 > 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (!z2) {
                break;
            }
            if ((i13 + 1) - i14 > 1) {
                z3 = true;
            } else {
                z3 = false;
            }
            if (z3) {
                int i15 = vbox.mMaxRed - vbox.mMinRed;
                int i16 = vbox.mMaxGreen - vbox.mMinGreen;
                int i17 = vbox.mMaxBlue - vbox.mMinBlue;
                if (i15 >= i16 && i15 >= i17) {
                    i2 = -3;
                } else if (i16 < i15 || i16 < i17) {
                    i2 = -1;
                } else {
                    i2 = -2;
                }
                ColorCutQuantizer colorCutQuantizer = ColorCutQuantizer.this;
                int[] iArr4 = colorCutQuantizer.mColors;
                int[] iArr5 = colorCutQuantizer.mHistogram;
                modifySignificantOctet(iArr4, i2, i14, i13);
                Arrays.sort(iArr4, vbox.mLowerIndex, vbox.mUpperIndex + 1);
                modifySignificantOctet(iArr4, i2, vbox.mLowerIndex, vbox.mUpperIndex);
                int i18 = vbox.mPopulation / 2;
                int i19 = vbox.mLowerIndex;
                int i20 = 0;
                while (true) {
                    int i21 = vbox.mUpperIndex;
                    if (i19 > i21) {
                        i3 = vbox.mLowerIndex;
                        break;
                    }
                    i20 += iArr5[iArr4[i19]];
                    if (i20 >= i18) {
                        i3 = Math.min(i21 - 1, i19);
                        break;
                    }
                    i19++;
                }
                Vbox vbox2 = new Vbox(i3 + 1, vbox.mUpperIndex);
                vbox.mUpperIndex = i3;
                vbox.fitBox();
                priorityQueue.offer(vbox2);
                priorityQueue.offer(vbox);
            } else {
                throw new IllegalStateException("Can not split a box with only 1 color");
            }
        }
        ArrayList arrayList = new ArrayList(priorityQueue.size());
        Iterator it = priorityQueue.iterator();
        while (it.hasNext()) {
            Vbox vbox3 = (Vbox) it.next();
            Objects.requireNonNull(vbox3);
            ColorCutQuantizer colorCutQuantizer2 = ColorCutQuantizer.this;
            int[] iArr6 = colorCutQuantizer2.mColors;
            int[] iArr7 = colorCutQuantizer2.mHistogram;
            int i22 = 0;
            int i23 = 0;
            int i24 = 0;
            int i25 = 0;
            for (int i26 = vbox3.mLowerIndex; i26 <= vbox3.mUpperIndex; i26++) {
                int i27 = iArr6[i26];
                int i28 = iArr7[i27];
                i23 += i28;
                i22 = (((i27 >> 10) & 31) * i28) + i22;
                i24 = (((i27 >> 5) & 31) * i28) + i24;
                i25 = (i28 * (i27 & 31)) + i25;
            }
            float f = i23;
            Palette.Swatch swatch = new Palette.Swatch(Color.rgb(modifyWordWidth(Math.round(i22 / f), 5, 8), modifyWordWidth(Math.round(i24 / f), 5, 8), modifyWordWidth(Math.round(i25 / f), 5, 8)), i23);
            float[] hsl = swatch.getHsl();
            Palette.Filter[] filterArr3 = this.mFilters;
            if (filterArr3 != null && filterArr3.length > 0) {
                int length2 = filterArr3.length;
                for (int i29 = 0; i29 < length2; i29++) {
                    if (!this.mFilters[i29].isAllowed(hsl)) {
                        z = true;
                        break;
                    }
                }
            }
            z = false;
            if (!z) {
                arrayList.add(swatch);
            }
        }
        this.mQuantizedColors = arrayList;
    }
}
