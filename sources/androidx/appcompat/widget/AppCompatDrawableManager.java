package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.ResourceManagerInternal;
import androidx.core.graphics.ColorUtils;
import java.util.Objects;
/* loaded from: classes.dex */
public final class AppCompatDrawableManager {
    public static final PorterDuff.Mode DEFAULT_MODE = PorterDuff.Mode.SRC_IN;
    public static AppCompatDrawableManager INSTANCE;
    public ResourceManagerInternal mResourceManager;

    public final synchronized Drawable getDrawable(Context context, int i) {
        return this.mResourceManager.getDrawable(context, i);
    }

    /* renamed from: androidx.appcompat.widget.AppCompatDrawableManager$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements ResourceManagerInternal.ResourceManagerHooks {
        public final int[] COLORFILTER_TINT_COLOR_CONTROL_NORMAL = {2131231584, 2131231582, 2131231501};
        public final int[] TINT_COLOR_CONTROL_NORMAL = {2131231526, 2131231563, 2131231533, 2131231528, 2131231529, 2131231532, 2131231531};
        public final int[] COLORFILTER_COLOR_CONTROL_ACTIVATED = {2131231581, 2131231583, 2131231518, 2131231577, 2131231578, 2131231579, 2131231580};
        public final int[] COLORFILTER_COLOR_BACKGROUND_MULTIPLY = {2131231553, 2131231516, 2131231552};
        public final int[] TINT_COLOR_CONTROL_STATE_LIST = {2131231575, 2131231585};
        public final int[] TINT_CHECKABLE_BUTTON_LIST = {2131231504, 2131231510, 2131231505, 2131231511};

        public static boolean arrayContains(int[] iArr, int i) {
            for (int i2 : iArr) {
                if (i2 == i) {
                    return true;
                }
            }
            return false;
        }

        public static ColorStateList createButtonColorStateList(Context context, int i) {
            int themeAttrColor = ThemeUtils.getThemeAttrColor(context, 2130968819);
            return new ColorStateList(new int[][]{ThemeUtils.DISABLED_STATE_SET, ThemeUtils.PRESSED_STATE_SET, ThemeUtils.FOCUSED_STATE_SET, ThemeUtils.EMPTY_STATE_SET}, new int[]{ThemeUtils.getDisabledThemeAttrColor(context, 2130968816), ColorUtils.compositeColors(themeAttrColor, i), ColorUtils.compositeColors(themeAttrColor, i), i});
        }

        public static LayerDrawable getRatingBarLayerDrawable(ResourceManagerInternal resourceManagerInternal, Context context, int i) {
            BitmapDrawable bitmapDrawable;
            BitmapDrawable bitmapDrawable2;
            BitmapDrawable bitmapDrawable3;
            int dimensionPixelSize = context.getResources().getDimensionPixelSize(i);
            Drawable drawable = resourceManagerInternal.getDrawable(context, 2131231571);
            Drawable drawable2 = resourceManagerInternal.getDrawable(context, 2131231572);
            if ((drawable instanceof BitmapDrawable) && drawable.getIntrinsicWidth() == dimensionPixelSize && drawable.getIntrinsicHeight() == dimensionPixelSize) {
                bitmapDrawable2 = (BitmapDrawable) drawable;
                bitmapDrawable = new BitmapDrawable(bitmapDrawable2.getBitmap());
            } else {
                Bitmap createBitmap = Bitmap.createBitmap(dimensionPixelSize, dimensionPixelSize, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                drawable.setBounds(0, 0, dimensionPixelSize, dimensionPixelSize);
                drawable.draw(canvas);
                bitmapDrawable2 = new BitmapDrawable(createBitmap);
                bitmapDrawable = new BitmapDrawable(createBitmap);
            }
            bitmapDrawable.setTileModeX(Shader.TileMode.REPEAT);
            if ((drawable2 instanceof BitmapDrawable) && drawable2.getIntrinsicWidth() == dimensionPixelSize && drawable2.getIntrinsicHeight() == dimensionPixelSize) {
                bitmapDrawable3 = (BitmapDrawable) drawable2;
            } else {
                Bitmap createBitmap2 = Bitmap.createBitmap(dimensionPixelSize, dimensionPixelSize, Bitmap.Config.ARGB_8888);
                Canvas canvas2 = new Canvas(createBitmap2);
                drawable2.setBounds(0, 0, dimensionPixelSize, dimensionPixelSize);
                drawable2.draw(canvas2);
                bitmapDrawable3 = new BitmapDrawable(createBitmap2);
            }
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{bitmapDrawable2, bitmapDrawable3, bitmapDrawable});
            layerDrawable.setId(0, 16908288);
            layerDrawable.setId(1, 16908303);
            layerDrawable.setId(2, 16908301);
            return layerDrawable;
        }

        public static void setPorterDuffColorFilter(Drawable drawable, int i, PorterDuff.Mode mode) {
            if (DrawableUtils.canSafelyMutateDrawable(drawable)) {
                drawable = drawable.mutate();
            }
            if (mode == null) {
                mode = AppCompatDrawableManager.DEFAULT_MODE;
            }
            drawable.setColorFilter(AppCompatDrawableManager.getPorterDuffColorFilter(i, mode));
        }

        public final ColorStateList getTintListForDrawableRes(Context context, int i) {
            if (i == 2131231521) {
                return AppCompatResources.getColorStateList(context, 2131099685);
            }
            if (i == 2131231574) {
                return AppCompatResources.getColorStateList(context, 2131099688);
            }
            if (i == 2131231573) {
                int[][] iArr = new int[3];
                int[] iArr2 = new int[3];
                ColorStateList themeAttrColorStateList = ThemeUtils.getThemeAttrColorStateList(context, 2130968850);
                if (themeAttrColorStateList == null || !themeAttrColorStateList.isStateful()) {
                    iArr[0] = ThemeUtils.DISABLED_STATE_SET;
                    iArr2[0] = ThemeUtils.getDisabledThemeAttrColor(context, 2130968850);
                    iArr[1] = ThemeUtils.CHECKED_STATE_SET;
                    iArr2[1] = ThemeUtils.getThemeAttrColor(context, 2130968818);
                    iArr[2] = ThemeUtils.EMPTY_STATE_SET;
                    iArr2[2] = ThemeUtils.getThemeAttrColor(context, 2130968850);
                } else {
                    iArr[0] = ThemeUtils.DISABLED_STATE_SET;
                    iArr2[0] = themeAttrColorStateList.getColorForState(iArr[0], 0);
                    iArr[1] = ThemeUtils.CHECKED_STATE_SET;
                    iArr2[1] = ThemeUtils.getThemeAttrColor(context, 2130968818);
                    iArr[2] = ThemeUtils.EMPTY_STATE_SET;
                    iArr2[2] = themeAttrColorStateList.getDefaultColor();
                }
                return new ColorStateList(iArr, iArr2);
            } else if (i == 2131231509) {
                return createButtonColorStateList(context, ThemeUtils.getThemeAttrColor(context, 2130968816));
            } else {
                if (i == 2131231503) {
                    return createButtonColorStateList(context, 0);
                }
                if (i == 2131231508) {
                    return createButtonColorStateList(context, ThemeUtils.getThemeAttrColor(context, 2130968814));
                }
                if (i == 2131231569 || i == 2131231570) {
                    return AppCompatResources.getColorStateList(context, 2131099687);
                }
                if (arrayContains(this.TINT_COLOR_CONTROL_NORMAL, i)) {
                    return ThemeUtils.getThemeAttrColorStateList(context, 2130968820);
                }
                if (arrayContains(this.TINT_COLOR_CONTROL_STATE_LIST, i)) {
                    return AppCompatResources.getColorStateList(context, 2131099684);
                }
                if (arrayContains(this.TINT_CHECKABLE_BUTTON_LIST, i)) {
                    return AppCompatResources.getColorStateList(context, 2131099683);
                }
                if (i == 2131231562) {
                    return AppCompatResources.getColorStateList(context, 2131099686);
                }
                return null;
            }
        }
    }

    public static synchronized AppCompatDrawableManager get() {
        AppCompatDrawableManager appCompatDrawableManager;
        synchronized (AppCompatDrawableManager.class) {
            if (INSTANCE == null) {
                preload();
            }
            appCompatDrawableManager = INSTANCE;
        }
        return appCompatDrawableManager;
    }

    public static synchronized PorterDuffColorFilter getPorterDuffColorFilter(int i, PorterDuff.Mode mode) {
        PorterDuffColorFilter porterDuffColorFilter;
        synchronized (AppCompatDrawableManager.class) {
            porterDuffColorFilter = ResourceManagerInternal.getPorterDuffColorFilter(i, mode);
        }
        return porterDuffColorFilter;
    }

    public static synchronized void preload() {
        synchronized (AppCompatDrawableManager.class) {
            if (INSTANCE == null) {
                AppCompatDrawableManager appCompatDrawableManager = new AppCompatDrawableManager();
                INSTANCE = appCompatDrawableManager;
                appCompatDrawableManager.mResourceManager = ResourceManagerInternal.get();
                ResourceManagerInternal resourceManagerInternal = INSTANCE.mResourceManager;
                AnonymousClass1 r2 = new AnonymousClass1();
                Objects.requireNonNull(resourceManagerInternal);
                synchronized (resourceManagerInternal) {
                    resourceManagerInternal.mHooks = r2;
                }
            }
        }
    }

    public static void tintDrawable(Drawable drawable, TintInfo tintInfo, int[] iArr) {
        ColorStateList colorStateList;
        PorterDuff.Mode mode;
        PorterDuff.Mode mode2 = ResourceManagerInternal.DEFAULT_MODE;
        if (!DrawableUtils.canSafelyMutateDrawable(drawable) || drawable.mutate() == drawable) {
            boolean z = tintInfo.mHasTintList;
            if (z || tintInfo.mHasTintMode) {
                PorterDuffColorFilter porterDuffColorFilter = null;
                if (z) {
                    colorStateList = tintInfo.mTintList;
                } else {
                    colorStateList = null;
                }
                if (tintInfo.mHasTintMode) {
                    mode = tintInfo.mTintMode;
                } else {
                    mode = ResourceManagerInternal.DEFAULT_MODE;
                }
                if (!(colorStateList == null || mode == null)) {
                    porterDuffColorFilter = ResourceManagerInternal.getPorterDuffColorFilter(colorStateList.getColorForState(iArr, 0), mode);
                }
                drawable.setColorFilter(porterDuffColorFilter);
                return;
            }
            drawable.clearColorFilter();
            return;
        }
        Log.d("ResourceManagerInternal", "Mutated drawable is not the same instance as the input.");
    }
}
