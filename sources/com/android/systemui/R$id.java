package com.android.systemui;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.android.systemui.theme.ThemeOverlayApplier;
/* loaded from: classes.dex */
public final class R$id {
    public static final int[] CollapsingCoordinatorLayout = {2130968812, 2130968880};
    public static final int[] SucFooterBarMixin = {2130969793, 2130969794, 2130969795, 2130969796, 2130969797, 2130969798, 2130969799, 2130969800, 2130969801, 2130969802, 2130969803, 2130969804, 2130969805, 2130969806, 2130969807, 2130969808};
    public static final int[] SucFooterButton = {16842752, 16843087, 2130969791, 2130969809, 2130969810};
    public static final int[] SucPartnerCustomizationLayout = {2130969811, 2130969818, 2130969825};
    public static final int[] SucStatusBarMixin = {2130969820, 2130969822};
    public static final int[] SucSystemNavBarMixin = {2130969821, 2130969823, 2130969824};
    public static final int[] SucTemplateLayout = {16842994, 2130969792};

    public static boolean isDescendant(ViewGroup viewGroup, View view) {
        while (view != null) {
            if (view == viewGroup) {
                return true;
            }
            ViewParent parent = view.getParent();
            if (!(parent instanceof View)) {
                return false;
            }
            view = (View) parent;
        }
        return false;
    }

    public static int getCornerRadiusBottom(Context context) {
        int i;
        int identifier = context.getResources().getIdentifier("config_rounded_mask_size_bottom", "dimen", ThemeOverlayApplier.SYSUI_PACKAGE);
        int i2 = 0;
        if (identifier > 0) {
            i = context.getResources().getDimensionPixelSize(identifier);
        } else {
            i = 0;
        }
        if (i != 0) {
            return i;
        }
        int identifier2 = context.getResources().getIdentifier("config_rounded_mask_size", "dimen", ThemeOverlayApplier.SYSUI_PACKAGE);
        if (identifier2 > 0) {
            i2 = context.getResources().getDimensionPixelSize(identifier2);
        }
        return i2;
    }

    public static int getCornerRadiusTop(Context context) {
        int i;
        int identifier = context.getResources().getIdentifier("config_rounded_mask_size_top", "dimen", ThemeOverlayApplier.SYSUI_PACKAGE);
        int i2 = 0;
        if (identifier > 0) {
            i = context.getResources().getDimensionPixelSize(identifier);
        } else {
            i = 0;
        }
        if (i != 0) {
            return i;
        }
        int identifier2 = context.getResources().getIdentifier("config_rounded_mask_size", "dimen", ThemeOverlayApplier.SYSUI_PACKAGE);
        if (identifier2 > 0) {
            i2 = context.getResources().getDimensionPixelSize(identifier2);
        }
        return i2;
    }

    public static int getHeight(Context context) {
        Display display = context.getDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getRealMetrics(displayMetrics);
        int rotation = display.getRotation();
        if (rotation == 0 || rotation == 2) {
            return displayMetrics.heightPixels;
        }
        return displayMetrics.widthPixels;
    }

    public static int getWidth(Context context) {
        Display display = context.getDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getRealMetrics(displayMetrics);
        int rotation = display.getRotation();
        if (rotation == 0 || rotation == 2) {
            return displayMetrics.widthPixels;
        }
        return displayMetrics.heightPixels;
    }
}
