package com.android.launcher3.icons;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import android.os.UserHandle;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import com.android.launcher3.icons.BitmapInfo;
import com.android.launcher3.util.FlagOp;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes.dex */
public class BaseIconFactory implements AutoCloseable {
    public static int PLACEHOLDER_BACKGROUND_COLOR = Color.rgb(245, 245, 245);
    public final Canvas mCanvas;
    public final Context mContext;
    public final int mFillResIconDpi;
    public final int mIconBitmapSize;
    public IconNormalizer mNormalizer;
    public final PackageManager mPm;
    public ShadowGenerator mShadowGenerator;
    public final boolean mShapeDetection;
    public int mWrapperBackgroundColor;
    public Drawable mWrapperIcon;
    public final Rect mOldBounds = new Rect();
    public final SparseBooleanArray mIsUserBadged = new SparseBooleanArray();
    public final ColorExtractor mColorExtractor = new ColorExtractor();

    /* loaded from: classes.dex */
    public static class IconOptions {
        public UserHandle mUserHandle;
    }

    /* loaded from: classes.dex */
    public static class NoopDrawable extends ColorDrawable {
        @Override // android.graphics.drawable.Drawable
        public final int getIntrinsicHeight() {
            return 1;
        }

        @Override // android.graphics.drawable.Drawable
        public final int getIntrinsicWidth() {
            return 1;
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        this.mWrapperBackgroundColor = -1;
    }

    @TargetApi(10000)
    public final BitmapInfo createBadgedIconBitmap(Drawable drawable, IconOptions iconOptions) {
        FlagOp flagOp;
        UserHandle userHandle;
        boolean z;
        int i;
        int i2;
        float f;
        boolean z2 = true;
        float[] fArr = new float[1];
        Drawable normalizeAndWrapToAdaptiveIcon = normalizeAndWrapToAdaptiveIcon(drawable, null, fArr);
        float f2 = fArr[0];
        int i3 = this.mIconBitmapSize;
        Bitmap createBitmap = Bitmap.createBitmap(i3, i3, Bitmap.Config.ARGB_8888);
        if (normalizeAndWrapToAdaptiveIcon != null) {
            this.mCanvas.setBitmap(createBitmap);
            this.mOldBounds.set(normalizeAndWrapToAdaptiveIcon.getBounds());
            if (normalizeAndWrapToAdaptiveIcon instanceof AdaptiveIconDrawable) {
                int max = Math.max((int) Math.ceil(0.035f * f), Math.round(((1.0f - f2) * i3) / 2.0f));
                int i4 = (i3 - max) - max;
                normalizeAndWrapToAdaptiveIcon.setBounds(0, 0, i4, i4);
                int save = this.mCanvas.save();
                float f3 = max;
                this.mCanvas.translate(f3, f3);
                if (normalizeAndWrapToAdaptiveIcon instanceof BitmapInfo.Extender) {
                    ((BitmapInfo.Extender) normalizeAndWrapToAdaptiveIcon).drawForPersistence(this.mCanvas);
                } else {
                    normalizeAndWrapToAdaptiveIcon.draw(this.mCanvas);
                }
                this.mCanvas.restoreToCount(save);
            } else {
                if (normalizeAndWrapToAdaptiveIcon instanceof BitmapDrawable) {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) normalizeAndWrapToAdaptiveIcon;
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    if (createBitmap != null && bitmap.getDensity() == 0) {
                        bitmapDrawable.setTargetDensity(this.mContext.getResources().getDisplayMetrics());
                    }
                }
                int intrinsicWidth = normalizeAndWrapToAdaptiveIcon.getIntrinsicWidth();
                int intrinsicHeight = normalizeAndWrapToAdaptiveIcon.getIntrinsicHeight();
                if (intrinsicWidth > 0 && intrinsicHeight > 0) {
                    float f4 = intrinsicWidth / intrinsicHeight;
                    if (intrinsicWidth > intrinsicHeight) {
                        i = (int) (i3 / f4);
                        i2 = i3;
                    } else if (intrinsicHeight > intrinsicWidth) {
                        i2 = (int) (i3 * f4);
                        i = i3;
                    }
                    int i5 = (i3 - i2) / 2;
                    int i6 = (i3 - i) / 2;
                    normalizeAndWrapToAdaptiveIcon.setBounds(i5, i6, i2 + i5, i + i6);
                    this.mCanvas.save();
                    float f5 = i3 / 2;
                    this.mCanvas.scale(f2, f2, f5, f5);
                    normalizeAndWrapToAdaptiveIcon.draw(this.mCanvas);
                    this.mCanvas.restore();
                }
                i2 = i3;
                i = i2;
                int i52 = (i3 - i2) / 2;
                int i62 = (i3 - i) / 2;
                normalizeAndWrapToAdaptiveIcon.setBounds(i52, i62, i2 + i52, i + i62);
                this.mCanvas.save();
                float f52 = i3 / 2;
                this.mCanvas.scale(f2, f2, f52, f52);
                normalizeAndWrapToAdaptiveIcon.draw(this.mCanvas);
                this.mCanvas.restore();
            }
            normalizeAndWrapToAdaptiveIcon.setBounds(this.mOldBounds);
            this.mCanvas.setBitmap(null);
        }
        if (normalizeAndWrapToAdaptiveIcon instanceof AdaptiveIconDrawable) {
            this.mCanvas.setBitmap(createBitmap);
            if (this.mShadowGenerator == null) {
                this.mShadowGenerator = new ShadowGenerator(this.mIconBitmapSize);
            }
            this.mShadowGenerator.recreateIcon(Bitmap.createBitmap(createBitmap), this.mCanvas);
            this.mCanvas.setBitmap(null);
        }
        int extractColor = extractColor(createBitmap);
        BitmapInfo bitmapInfo = new BitmapInfo(createBitmap, extractColor);
        if (normalizeAndWrapToAdaptiveIcon instanceof BitmapInfo.Extender) {
            bitmapInfo = ((BitmapInfo.Extender) normalizeAndWrapToAdaptiveIcon).getExtendedInfo(createBitmap, extractColor, this, fArr[0]);
        }
        DebugInfo$$ExternalSyntheticOutline0 debugInfo$$ExternalSyntheticOutline0 = DebugInfo$$ExternalSyntheticOutline0.INSTANCE;
        if (iconOptions == null || (userHandle = iconOptions.mUserHandle) == null) {
            flagOp = debugInfo$$ExternalSyntheticOutline0;
        } else {
            int hashCode = userHandle.hashCode();
            int indexOfKey = this.mIsUserBadged.indexOfKey(hashCode);
            if (indexOfKey >= 0) {
                z = this.mIsUserBadged.valueAt(indexOfKey);
            } else {
                NoopDrawable noopDrawable = new NoopDrawable();
                if (noopDrawable == this.mPm.getUserBadgedIcon(noopDrawable, iconOptions.mUserHandle)) {
                    z2 = false;
                }
                this.mIsUserBadged.put(hashCode, z2);
                z = z2;
            }
            if (z) {
                flagOp = new FlagOp() { // from class: com.android.launcher3.util.FlagOp$$ExternalSyntheticLambda0
                    public final /* synthetic */ FlagOp f$0 = DebugInfo$$ExternalSyntheticOutline0.INSTANCE;
                    public final /* synthetic */ int f$1 = 1;

                    @Override // com.android.launcher3.util.FlagOp
                    public final int apply(int i7) {
                        FlagOp flagOp2 = this.f$0;
                        int i8 = this.f$1;
                        Objects.requireNonNull(flagOp2);
                        return i8 | flagOp2.apply(i7);
                    }
                };
            } else {
                flagOp = new FlagOp() { // from class: com.android.launcher3.util.FlagOp$$ExternalSyntheticLambda1
                    public final /* synthetic */ FlagOp f$0 = DebugInfo$$ExternalSyntheticOutline0.INSTANCE;
                    public final /* synthetic */ int f$1 = 1;

                    @Override // com.android.launcher3.util.FlagOp
                    public final int apply(int i7) {
                        FlagOp flagOp2 = this.f$0;
                        int i8 = this.f$1;
                        Objects.requireNonNull(flagOp2);
                        return (~i8) & flagOp2.apply(i7);
                    }
                };
            }
        }
        Objects.requireNonNull(bitmapInfo);
        if (flagOp == debugInfo$$ExternalSyntheticOutline0) {
            return bitmapInfo;
        }
        BitmapInfo clone = bitmapInfo.clone();
        clone.flags = flagOp.apply(clone.flags);
        return clone;
    }

    public final Drawable normalizeAndWrapToAdaptiveIcon(Drawable drawable, RectF rectF, float[] fArr) {
        float f;
        if (drawable == null) {
            return null;
        }
        if (!(drawable instanceof AdaptiveIconDrawable)) {
            if (this.mWrapperIcon == null) {
                this.mWrapperIcon = this.mContext.getDrawable(2131231591).mutate();
            }
            AdaptiveIconDrawable adaptiveIconDrawable = (AdaptiveIconDrawable) this.mWrapperIcon;
            adaptiveIconDrawable.setBounds(0, 0, 1, 1);
            boolean[] zArr = new boolean[1];
            f = getNormalizer().getScale(drawable, rectF, adaptiveIconDrawable.getIconMask(), zArr);
            if (!zArr[0]) {
                FixedScaleDrawable fixedScaleDrawable = (FixedScaleDrawable) adaptiveIconDrawable.getForeground();
                fixedScaleDrawable.setDrawable(drawable);
                float intrinsicHeight = fixedScaleDrawable.getIntrinsicHeight();
                float intrinsicWidth = fixedScaleDrawable.getIntrinsicWidth();
                float f2 = f * 0.46669f;
                fixedScaleDrawable.mScaleX = f2;
                fixedScaleDrawable.mScaleY = f2;
                if (intrinsicHeight > intrinsicWidth && intrinsicWidth > 0.0f) {
                    fixedScaleDrawable.mScaleX = (intrinsicWidth / intrinsicHeight) * f2;
                } else if (intrinsicWidth > intrinsicHeight && intrinsicHeight > 0.0f) {
                    fixedScaleDrawable.mScaleY = (intrinsicHeight / intrinsicWidth) * f2;
                }
                f = getNormalizer().getScale(adaptiveIconDrawable, rectF, null, null);
                ((ColorDrawable) adaptiveIconDrawable.getBackground()).setColor(this.mWrapperBackgroundColor);
                drawable = adaptiveIconDrawable;
            }
        } else {
            f = getNormalizer().getScale(drawable, rectF, null, null);
        }
        fArr[0] = f;
        return drawable;
    }

    public final BitmapInfo createIconBitmap(Bitmap bitmap) {
        int i;
        int i2;
        float f;
        if (!(this.mIconBitmapSize == bitmap.getWidth() && this.mIconBitmapSize == bitmap.getHeight())) {
            BitmapDrawable bitmapDrawable = new BitmapDrawable(this.mContext.getResources(), bitmap);
            int i3 = this.mIconBitmapSize;
            Bitmap createBitmap = Bitmap.createBitmap(i3, i3, Bitmap.Config.ARGB_8888);
            this.mCanvas.setBitmap(createBitmap);
            this.mOldBounds.set(bitmapDrawable.getBounds());
            if (bitmapDrawable instanceof AdaptiveIconDrawable) {
                int max = Math.max((int) Math.ceil(0.035f * f), Math.round((0.0f * i3) / 2.0f));
                int i4 = (i3 - max) - max;
                bitmapDrawable.setBounds(0, 0, i4, i4);
                int save = this.mCanvas.save();
                float f2 = max;
                this.mCanvas.translate(f2, f2);
                if (bitmapDrawable instanceof BitmapInfo.Extender) {
                    ((BitmapInfo.Extender) bitmapDrawable).drawForPersistence(this.mCanvas);
                } else {
                    bitmapDrawable.draw(this.mCanvas);
                }
                this.mCanvas.restoreToCount(save);
            } else {
                Bitmap bitmap2 = bitmapDrawable.getBitmap();
                if (createBitmap != null && bitmap2.getDensity() == 0) {
                    bitmapDrawable.setTargetDensity(this.mContext.getResources().getDisplayMetrics());
                }
                int intrinsicWidth = bitmapDrawable.getIntrinsicWidth();
                int intrinsicHeight = bitmapDrawable.getIntrinsicHeight();
                if (intrinsicWidth > 0 && intrinsicHeight > 0) {
                    float f3 = intrinsicWidth / intrinsicHeight;
                    if (intrinsicWidth > intrinsicHeight) {
                        i = (int) (i3 / f3);
                        i2 = i3;
                    } else if (intrinsicHeight > intrinsicWidth) {
                        i2 = (int) (i3 * f3);
                        i = i3;
                    }
                    int i5 = (i3 - i2) / 2;
                    int i6 = (i3 - i) / 2;
                    bitmapDrawable.setBounds(i5, i6, i2 + i5, i + i6);
                    this.mCanvas.save();
                    float f4 = i3 / 2;
                    this.mCanvas.scale(1.0f, 1.0f, f4, f4);
                    bitmapDrawable.draw(this.mCanvas);
                    this.mCanvas.restore();
                }
                i2 = i3;
                i = i2;
                int i52 = (i3 - i2) / 2;
                int i62 = (i3 - i) / 2;
                bitmapDrawable.setBounds(i52, i62, i2 + i52, i + i62);
                this.mCanvas.save();
                float f42 = i3 / 2;
                this.mCanvas.scale(1.0f, 1.0f, f42, f42);
                bitmapDrawable.draw(this.mCanvas);
                this.mCanvas.restore();
            }
            bitmapDrawable.setBounds(this.mOldBounds);
            this.mCanvas.setBitmap(null);
            bitmap = createBitmap;
        }
        return new BitmapInfo(bitmap, extractColor(bitmap));
    }

    public final int extractColor(Bitmap bitmap) {
        int i;
        char c;
        ColorExtractor colorExtractor = this.mColorExtractor;
        Objects.requireNonNull(colorExtractor);
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        char c2 = 20;
        int sqrt = (int) Math.sqrt((height * width) / 20);
        if (sqrt < 1) {
            sqrt = 1;
        }
        float[] fArr = colorExtractor.mTmpHsv;
        Arrays.fill(fArr, 0.0f);
        float[] fArr2 = colorExtractor.mTmpHueScoreHistogram;
        Arrays.fill(fArr2, 0.0f);
        int i2 = -1;
        int[] iArr = colorExtractor.mTmpPixels;
        int i3 = 0;
        Arrays.fill(iArr, 0);
        int i4 = 0;
        int i5 = 0;
        float f = -1.0f;
        while (true) {
            i = -16777216;
            if (i4 >= height) {
                break;
            }
            int i6 = i3;
            while (i6 < width) {
                int pixel = bitmap.getPixel(i6, i4);
                if (((pixel >> 24) & 255) >= 128) {
                    int i7 = pixel | (-16777216);
                    Color.colorToHSV(i7, fArr);
                    int i8 = (int) fArr[i3];
                    if (i8 >= 0 && i8 < fArr2.length) {
                        c = 20;
                        if (i5 < 20) {
                            iArr[i5] = i7;
                            i5++;
                        }
                        fArr2[i8] = fArr2[i8] + (fArr[1] * fArr[2]);
                        if (fArr2[i8] > f) {
                            f = fArr2[i8];
                            i2 = i8;
                        }
                        i6 += sqrt;
                        c2 = c;
                        i3 = 0;
                    }
                }
                c = 20;
                i6 += sqrt;
                c2 = c;
                i3 = 0;
            }
            i4 += sqrt;
            i3 = 0;
        }
        SparseArray<Float> sparseArray = colorExtractor.mTmpRgbScores;
        sparseArray.clear();
        float f2 = -1.0f;
        for (int i9 = 0; i9 < i5; i9++) {
            int i10 = iArr[i9];
            Color.colorToHSV(i10, fArr);
            if (((int) fArr[0]) == i2) {
                float f3 = fArr[1];
                float f4 = fArr[2];
                int i11 = ((int) (100.0f * f3)) + ((int) (10000.0f * f4));
                float f5 = f3 * f4;
                Float f6 = sparseArray.get(i11);
                if (f6 != null) {
                    f5 += f6.floatValue();
                }
                sparseArray.put(i11, Float.valueOf(f5));
                if (f5 > f2) {
                    i = i10;
                    f2 = f5;
                }
            }
        }
        return i;
    }

    public final IconNormalizer getNormalizer() {
        if (this.mNormalizer == null) {
            this.mNormalizer = new IconNormalizer(this.mContext, this.mIconBitmapSize, this.mShapeDetection);
        }
        return this.mNormalizer;
    }

    public BaseIconFactory(Context context, int i, int i2, boolean z) {
        this.mWrapperBackgroundColor = -1;
        Paint paint = new Paint(3);
        Context applicationContext = context.getApplicationContext();
        this.mContext = applicationContext;
        this.mShapeDetection = z;
        this.mFillResIconDpi = i;
        this.mIconBitmapSize = i2;
        this.mPm = applicationContext.getPackageManager();
        Canvas canvas = new Canvas();
        this.mCanvas = canvas;
        canvas.setDrawFilter(new PaintFlagsDrawFilter(4, 2));
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(PLACEHOLDER_BACKGROUND_COLOR);
        paint.setTextSize(context.getResources().getDisplayMetrics().density * 20.0f);
        this.mWrapperBackgroundColor = -1;
    }

    public final Bitmap createScaledBitmapWithShadow(AdaptiveIconDrawable adaptiveIconDrawable) {
        float f;
        float scale = getNormalizer().getScale(adaptiveIconDrawable, null, null, null);
        int i = this.mIconBitmapSize;
        Bitmap createBitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
        this.mCanvas.setBitmap(createBitmap);
        this.mOldBounds.set(adaptiveIconDrawable.getBounds());
        int max = Math.max((int) Math.ceil(0.035f * f), Math.round(((1.0f - scale) * i) / 2.0f));
        int i2 = (i - max) - max;
        adaptiveIconDrawable.setBounds(0, 0, i2, i2);
        int save = this.mCanvas.save();
        float f2 = max;
        this.mCanvas.translate(f2, f2);
        if (adaptiveIconDrawable instanceof BitmapInfo.Extender) {
            ((BitmapInfo.Extender) adaptiveIconDrawable).drawForPersistence(this.mCanvas);
        } else {
            adaptiveIconDrawable.draw(this.mCanvas);
        }
        this.mCanvas.restoreToCount(save);
        adaptiveIconDrawable.setBounds(this.mOldBounds);
        this.mCanvas.setBitmap(null);
        this.mCanvas.setBitmap(createBitmap);
        if (this.mShadowGenerator == null) {
            this.mShadowGenerator = new ShadowGenerator(this.mIconBitmapSize);
        }
        this.mShadowGenerator.recreateIcon(Bitmap.createBitmap(createBitmap), this.mCanvas);
        this.mCanvas.setBitmap(null);
        return createBitmap;
    }
}
