package androidx.mediarouter.app;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MediaRouterThemeHelper {
    public static Drawable getIconByAttrId(Context context, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{i});
        Drawable drawable = AppCompatResources.getDrawable(context, obtainStyledAttributes.getResourceId(0, 0));
        if (isLightTheme(context)) {
            Object obj = ContextCompat.sLock;
            drawable.setTint(context.getColor(2131100410));
        }
        obtainStyledAttributes.recycle();
        return drawable;
    }

    public static int getThemeColor(Context context, int i, int i2) {
        if (i != 0) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(i, new int[]{i2});
            int color = obtainStyledAttributes.getColor(0, 0);
            obtainStyledAttributes.recycle();
            if (color != 0) {
                return color;
            }
        }
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(i2, typedValue, true);
        if (typedValue.resourceId != 0) {
            return context.getResources().getColor(typedValue.resourceId);
        }
        return typedValue.data;
    }

    public static void setVolumeSliderColor(Context context, MediaRouteVolumeSlider mediaRouteVolumeSlider, ViewGroup viewGroup) {
        int controllerColor = getControllerColor(context, 0);
        if (Color.alpha(controllerColor) != 255) {
            controllerColor = ColorUtils.compositeColors(controllerColor, ((Integer) viewGroup.getTag()).intValue());
        }
        Objects.requireNonNull(mediaRouteVolumeSlider);
        mediaRouteVolumeSlider.setColor(controllerColor, controllerColor);
    }

    public static ContextThemeWrapper createThemedDialogContext(Context context, boolean z) {
        int i;
        if (!z) {
            i = 2130968948;
        } else {
            i = 2130968621;
        }
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, getThemeResource(context, i));
        if (getThemeResource(contextThemeWrapper, 2130969469) != 0) {
            return new ContextThemeWrapper(contextThemeWrapper, getRouterThemeId(contextThemeWrapper));
        }
        return contextThemeWrapper;
    }

    public static float getDisabledAlpha(Context context) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(16842803, typedValue, true)) {
            return typedValue.getFloat();
        }
        return 0.5f;
    }

    public static int getThemeResource(Context context, int i) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(i, typedValue, true)) {
            return typedValue.resourceId;
        }
        return 0;
    }

    public static boolean isLightTheme(Context context) {
        TypedValue typedValue = new TypedValue();
        if (!context.getTheme().resolveAttribute(2130969245, typedValue, true) || typedValue.data == 0) {
            return false;
        }
        return true;
    }

    public static int createThemedDialogStyle(ContextThemeWrapper contextThemeWrapper) {
        int themeResource = getThemeResource(contextThemeWrapper, 2130969469);
        if (themeResource == 0) {
            return getRouterThemeId(contextThemeWrapper);
        }
        return themeResource;
    }

    public static int getControllerColor(Context context, int i) {
        if (ColorUtils.calculateContrast(-1, getThemeColor(context, i, 2130968838)) >= 3.0d) {
            return -1;
        }
        return -570425344;
    }

    public static int getRouterThemeId(Context context) {
        if (isLightTheme(context)) {
            if (getControllerColor(context, 0) == -570425344) {
                return 2132018175;
            }
            return 2132018176;
        } else if (getControllerColor(context, 0) == -570425344) {
            return 2132018177;
        } else {
            return 2132018174;
        }
    }

    public static void setDialogBackgroundColor(Context context, Dialog dialog) {
        int i;
        View decorView = dialog.getWindow().getDecorView();
        if (isLightTheme(context)) {
            i = 2131100406;
        } else {
            i = 2131100405;
        }
        Object obj = ContextCompat.sLock;
        decorView.setBackgroundColor(context.getColor(i));
    }

    public static void setIndeterminateProgressBarColor(Context context, ProgressBar progressBar) {
        int i;
        if (progressBar.isIndeterminate()) {
            if (isLightTheme(context)) {
                i = 2131100402;
            } else {
                i = 2131100401;
            }
            Object obj = ContextCompat.sLock;
            progressBar.getIndeterminateDrawable().setColorFilter(context.getColor(i), PorterDuff.Mode.SRC_IN);
        }
    }
}
