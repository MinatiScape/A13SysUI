package com.google.android.setupdesign.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import com.google.android.setupcompat.PartnerCustomizationLayout;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.GlifLayout;
import java.util.Locale;
import java.util.Objects;
/* loaded from: classes.dex */
public final class PartnerStyleHelper {
    public static TemplateLayout findLayoutFromActivity(Activity activity) {
        View findViewById;
        if (activity == null || (findViewById = activity.findViewById(2131428953)) == null) {
            return null;
        }
        return (TemplateLayout) findViewById.getParent();
    }

    public static boolean shouldApplyPartnerHeavyThemeResource(View view) {
        boolean z;
        boolean z2 = false;
        if (view == null) {
            return false;
        }
        if (view instanceof GlifLayout) {
            return ((GlifLayout) view).shouldApplyPartnerHeavyThemeResource();
        }
        Context context = view.getContext();
        try {
            TemplateLayout findLayoutFromActivity = findLayoutFromActivity(PartnerCustomizationLayout.lookupActivityFromContext(context));
            if (findLayoutFromActivity instanceof GlifLayout) {
                return ((GlifLayout) findLayoutFromActivity).shouldApplyPartnerHeavyThemeResource();
            }
        } catch (ClassCastException | IllegalArgumentException unused) {
        }
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{2130969903});
        boolean z3 = obtainStyledAttributes.getBoolean(0, false);
        obtainStyledAttributes.recycle();
        if (z3 || PartnerConfigHelper.shouldApplyExtendedPartnerConfig(context)) {
            z = true;
        } else {
            z = false;
        }
        if (shouldApplyPartnerResource(context) && z) {
            z2 = true;
        }
        return z2;
    }

    public static boolean shouldApplyPartnerResource(View view) {
        if (view == null) {
            return false;
        }
        if (view instanceof PartnerCustomizationLayout) {
            return ((PartnerCustomizationLayout) view).shouldApplyPartnerResource();
        }
        return shouldApplyPartnerResource(view.getContext());
    }

    public static int getLayoutGravity(Context context) {
        String string = PartnerConfigHelper.get(context).getString(context, PartnerConfig.CONFIG_LAYOUT_GRAVITY);
        if (string == null) {
            return 0;
        }
        String lowerCase = string.toLowerCase(Locale.ROOT);
        Objects.requireNonNull(lowerCase);
        if (lowerCase.equals("center")) {
            return 17;
        }
        if (!lowerCase.equals("start")) {
            return 0;
        }
        return 8388611;
    }

    public static boolean shouldApplyPartnerResource(Context context) {
        if (!PartnerConfigHelper.get(context).isAvailable()) {
            return false;
        }
        Activity activity = null;
        try {
            activity = PartnerCustomizationLayout.lookupActivityFromContext(context);
            if (activity != null) {
                TemplateLayout findLayoutFromActivity = findLayoutFromActivity(activity);
                if (findLayoutFromActivity instanceof PartnerCustomizationLayout) {
                    return ((PartnerCustomizationLayout) findLayoutFromActivity).shouldApplyPartnerResource();
                }
            }
        } catch (ClassCastException | IllegalArgumentException unused) {
        }
        boolean isAnySetupWizard = activity != null ? WizardManagerHelper.isAnySetupWizard(activity.getIntent()) : false;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{2130969825});
        boolean z = obtainStyledAttributes.getBoolean(0, true);
        obtainStyledAttributes.recycle();
        return isAnySetupWizard || z;
    }
}
