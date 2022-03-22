package com.android.systemui;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.view.ContextThemeWrapper;
import androidx.core.graphics.Insets$$ExternalSyntheticOutline0;
import com.android.keyguard.FontInterpolator$VarFontKey$$ExternalSyntheticOutline0;
import com.android.settingslib.Utils;
import java.util.Objects;
/* compiled from: DualToneHandler.kt */
/* loaded from: classes.dex */
public final class DualToneHandler {
    public Color darkColor;
    public Color lightColor;

    /* compiled from: DualToneHandler.kt */
    /* loaded from: classes.dex */
    public static final class Color {
        public final int background;
        public final int fill;
        public final int single;

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Color)) {
                return false;
            }
            Color color = (Color) obj;
            return this.single == color.single && this.background == color.background && this.fill == color.fill;
        }

        public final int hashCode() {
            return Integer.hashCode(this.fill) + FontInterpolator$VarFontKey$$ExternalSyntheticOutline0.m(this.background, Integer.hashCode(this.single) * 31, 31);
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Color(single=");
            m.append(this.single);
            m.append(", background=");
            m.append(this.background);
            m.append(", fill=");
            return Insets$$ExternalSyntheticOutline0.m(m, this.fill, ')');
        }

        public Color(int i, int i2, int i3) {
            this.single = i;
            this.background = i2;
            this.fill = i3;
        }
    }

    public final int getSingleColor(float f) {
        Color color = this.lightColor;
        Color color2 = null;
        if (color == null) {
            color = null;
        }
        Objects.requireNonNull(color);
        int i = color.single;
        Color color3 = this.darkColor;
        if (color3 != null) {
            color2 = color3;
        }
        Objects.requireNonNull(color2);
        return getColorForDarkIntensity(f, i, color2.single);
    }

    public final void setColorsFromContext(Context context) {
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, Utils.getThemeAttr(context, 2130968913));
        ContextThemeWrapper contextThemeWrapper2 = new ContextThemeWrapper(context, Utils.getThemeAttr(context, 2130969364));
        this.darkColor = new Color(Utils.getColorAttrDefaultColor(contextThemeWrapper, 2130969739), Utils.getColorAttrDefaultColor(contextThemeWrapper, 2130969211), Utils.getColorAttrDefaultColor(contextThemeWrapper, 2130969056));
        this.lightColor = new Color(Utils.getColorAttrDefaultColor(contextThemeWrapper2, 2130969739), Utils.getColorAttrDefaultColor(contextThemeWrapper2, 2130969211), Utils.getColorAttrDefaultColor(contextThemeWrapper2, 2130969056));
    }

    public DualToneHandler(Context context) {
        setColorsFromContext(context);
    }

    public static int getColorForDarkIntensity(float f, int i, int i2) {
        Object evaluate = ArgbEvaluator.getInstance().evaluate(f, Integer.valueOf(i), Integer.valueOf(i2));
        Objects.requireNonNull(evaluate, "null cannot be cast to non-null type kotlin.Int");
        return ((Integer) evaluate).intValue();
    }
}
