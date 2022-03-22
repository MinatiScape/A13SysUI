package com.google.android.setupcompat.partnerconfig;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import java.util.EnumMap;
import java.util.Objects;
/* loaded from: classes.dex */
public final class PartnerConfigHelper {
    public static final String IS_DYNAMIC_COLOR_ENABLED_METHOD = "isDynamicColorEnabled";
    public static final String IS_EXTENDED_PARTNER_CONFIG_ENABLED_METHOD = "isExtendedPartnerConfigEnabled";
    public static final String IS_MATERIAL_YOU_STYLE_ENABLED_METHOD = "IsMaterialYouStyleEnabled";
    public static final String IS_NEUTRAL_BUTTON_STYLE_ENABLED_METHOD = "isNeutralButtonStyleEnabled";
    public static final String IS_SUW_DAY_NIGHT_ENABLED_METHOD = "isSuwDayNightEnabled";
    public static final String KEY_FALLBACK_CONFIG = "fallbackConfig";
    public static final String SUW_AUTHORITY = "com.google.android.setupwizard.partner";
    public static final String SUW_GET_PARTNER_CONFIG_METHOD = "getOverlayConfig";
    public static Bundle applyDynamicColorBundle = null;
    public static Bundle applyExtendedPartnerConfigBundle = null;
    public static Bundle applyMaterialYouConfigBundle = null;
    public static Bundle applyNeutralButtonStyleBundle = null;
    public static AnonymousClass1 contentObserver = null;
    public static PartnerConfigHelper instance = null;
    public static int savedConfigUiMode = 0;
    public static int savedOrientation = 1;
    public static int savedScreenHeight;
    public static int savedScreenWidth;
    public static Bundle suwDayNightEnabledBundle;
    public final EnumMap<PartnerConfig, Object> partnerResourceCache;
    public Bundle resultBundle;

    public static synchronized PartnerConfigHelper get(Context context) {
        PartnerConfigHelper partnerConfigHelper;
        synchronized (PartnerConfigHelper.class) {
            if (!isValidInstance(context)) {
                instance = new PartnerConfigHelper(context);
            }
            partnerConfigHelper = instance;
        }
        return partnerConfigHelper;
    }

    public static Uri getContentUri() {
        return new Uri.Builder().scheme("content").authority(SUW_AUTHORITY).build();
    }

    public static TypedValue getTypedValueFromResource(Resources resources, int i) {
        TypedValue typedValue = new TypedValue();
        resources.getValue(i, typedValue, true);
        if (typedValue.type == 5) {
            return typedValue;
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Resource ID #0x");
        m.append(Integer.toHexString(i));
        m.append(" type #0x");
        m.append(Integer.toHexString(typedValue.type));
        m.append(" is not valid");
        throw new Resources.NotFoundException(m.toString());
    }

    public static boolean isSetupWizardDayNightEnabled(Context context) {
        if (suwDayNightEnabledBundle == null) {
            try {
                suwDayNightEnabledBundle = context.getContentResolver().call(getContentUri(), IS_SUW_DAY_NIGHT_ENABLED_METHOD, (String) null, (Bundle) null);
            } catch (IllegalArgumentException | SecurityException unused) {
                Log.w("PartnerConfigHelper", "SetupWizard DayNight supporting status unknown; return as false.");
                suwDayNightEnabledBundle = null;
                return false;
            }
        }
        Bundle bundle = suwDayNightEnabledBundle;
        if (bundle == null || !bundle.getBoolean(IS_SUW_DAY_NIGHT_ENABLED_METHOD, false)) {
            return false;
        }
        return true;
    }

    public static synchronized void resetInstance() {
        synchronized (PartnerConfigHelper.class) {
            instance = null;
            suwDayNightEnabledBundle = null;
            applyExtendedPartnerConfigBundle = null;
            applyMaterialYouConfigBundle = null;
            applyDynamicColorBundle = null;
            applyNeutralButtonStyleBundle = null;
        }
    }

    public static boolean shouldApplyExtendedPartnerConfig(Context context) {
        if (applyExtendedPartnerConfigBundle == null) {
            try {
                applyExtendedPartnerConfigBundle = context.getContentResolver().call(getContentUri(), IS_EXTENDED_PARTNER_CONFIG_ENABLED_METHOD, (String) null, (Bundle) null);
            } catch (IllegalArgumentException | SecurityException unused) {
                Log.w("PartnerConfigHelper", "SetupWizard extended partner configs supporting status unknown; return as false.");
                applyExtendedPartnerConfigBundle = null;
                return false;
            }
        }
        Bundle bundle = applyExtendedPartnerConfigBundle;
        if (bundle == null || !bundle.getBoolean(IS_EXTENDED_PARTNER_CONFIG_ENABLED_METHOD, false)) {
            return false;
        }
        return true;
    }

    public ResourceEntry getResourceEntryFromKey(Context context, String str) {
        boolean z;
        Bundle bundle = this.resultBundle.getBundle(str);
        Bundle bundle2 = this.resultBundle.getBundle("fallbackConfig");
        if (bundle2 != null) {
            bundle.putBundle("fallbackConfig", bundle2.getBundle(str));
        }
        ResourceEntry fromBundle = ResourceEntry.fromBundle(context, bundle);
        Objects.requireNonNull(fromBundle);
        Resources resources = fromBundle.resources;
        Configuration configuration = resources.getConfiguration();
        if (!isSetupWizardDayNightEnabled(context)) {
            int i = configuration.uiMode;
            if ((i & 48) == 32) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                configuration.uiMode = (i & (-49)) | 16;
                resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            }
        }
        return fromBundle;
    }

    public final boolean isAvailable() {
        Bundle bundle = this.resultBundle;
        if (bundle == null || bundle.isEmpty()) {
            return false;
        }
        return true;
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [com.google.android.setupcompat.partnerconfig.PartnerConfigHelper$1] */
    public PartnerConfigHelper(Context context) {
        Object obj;
        this.resultBundle = null;
        EnumMap<PartnerConfig, Object> enumMap = new EnumMap<>(PartnerConfig.class);
        this.partnerResourceCache = enumMap;
        Bundle bundle = this.resultBundle;
        if (bundle == null || bundle.isEmpty()) {
            try {
                this.resultBundle = context.getContentResolver().call(getContentUri(), SUW_GET_PARTNER_CONFIG_METHOD, (String) null, (Bundle) null);
                enumMap.clear();
                StringBuilder sb = new StringBuilder();
                sb.append("PartnerConfigsBundle=");
                Bundle bundle2 = this.resultBundle;
                if (bundle2 != null) {
                    obj = Integer.valueOf(bundle2.size());
                } else {
                    obj = "(null)";
                }
                sb.append(obj);
                Log.i("PartnerConfigHelper", sb.toString());
            } catch (IllegalArgumentException | SecurityException unused) {
                Log.w("PartnerConfigHelper", "Fail to get config from suw provider");
            }
        }
        if (isSetupWizardDayNightEnabled(context)) {
            if (contentObserver != null) {
                try {
                    context.getContentResolver().unregisterContentObserver(contentObserver);
                    contentObserver = null;
                } catch (IllegalArgumentException | NullPointerException | SecurityException e) {
                    Log.w("PartnerConfigHelper", "Failed to unregister content observer: " + e);
                }
            }
            Uri contentUri = getContentUri();
            try {
                contentObserver = new ContentObserver() { // from class: com.google.android.setupcompat.partnerconfig.PartnerConfigHelper.1
                    @Override // android.database.ContentObserver
                    public final void onChange(boolean z) {
                        super.onChange(z);
                        PartnerConfigHelper.resetInstance();
                    }
                };
                context.getContentResolver().registerContentObserver(contentUri, true, contentObserver);
            } catch (IllegalArgumentException | NullPointerException | SecurityException e2) {
                Log.w("PartnerConfigHelper", "Failed to register content observer for " + contentUri + ": " + e2);
            }
        }
    }

    public static boolean isValidInstance(Context context) {
        boolean z;
        Configuration configuration = context.getResources().getConfiguration();
        if (instance == null) {
            savedConfigUiMode = configuration.uiMode & 48;
            savedOrientation = configuration.orientation;
            savedScreenWidth = configuration.screenWidthDp;
            savedScreenHeight = configuration.screenHeightDp;
            return false;
        }
        if (!isSetupWizardDayNightEnabled(context) || (configuration.uiMode & 48) == savedConfigUiMode) {
            z = false;
        } else {
            z = true;
        }
        if (!z && configuration.orientation == savedOrientation && configuration.screenWidthDp == savedScreenWidth && configuration.screenHeightDp == savedScreenHeight) {
            return true;
        }
        savedConfigUiMode = configuration.uiMode & 48;
        savedOrientation = configuration.orientation;
        savedScreenHeight = configuration.screenHeightDp;
        savedScreenWidth = configuration.screenWidthDp;
        resetInstance();
        return false;
    }

    public final boolean getBoolean(Context context, PartnerConfig partnerConfig, boolean z) {
        if (partnerConfig.getResourceType() != PartnerConfig.ResourceType.BOOL) {
            throw new IllegalArgumentException("Not a bool resource");
        } else if (this.partnerResourceCache.containsKey(partnerConfig)) {
            return ((Boolean) this.partnerResourceCache.get(partnerConfig)).booleanValue();
        } else {
            try {
                ResourceEntry resourceEntryFromKey = getResourceEntryFromKey(context, partnerConfig.getResourceName());
                Objects.requireNonNull(resourceEntryFromKey);
                z = resourceEntryFromKey.resources.getBoolean(resourceEntryFromKey.resourceId);
                this.partnerResourceCache.put((EnumMap<PartnerConfig, Object>) partnerConfig, (PartnerConfig) Boolean.valueOf(z));
                return z;
            } catch (Resources.NotFoundException | NullPointerException unused) {
                return z;
            }
        }
    }

    public final int getColor(Context context, PartnerConfig partnerConfig) {
        if (partnerConfig.getResourceType() != PartnerConfig.ResourceType.COLOR) {
            throw new IllegalArgumentException("Not a color resource");
        } else if (this.partnerResourceCache.containsKey(partnerConfig)) {
            return ((Integer) this.partnerResourceCache.get(partnerConfig)).intValue();
        } else {
            int i = 0;
            try {
                ResourceEntry resourceEntryFromKey = getResourceEntryFromKey(context, partnerConfig.getResourceName());
                Objects.requireNonNull(resourceEntryFromKey);
                Resources resources = resourceEntryFromKey.resources;
                int i2 = resourceEntryFromKey.resourceId;
                TypedValue typedValue = new TypedValue();
                resources.getValue(i2, typedValue, true);
                if (typedValue.type == 1 && typedValue.data == 0) {
                    return 0;
                }
                i = resources.getColor(i2, null);
                this.partnerResourceCache.put((EnumMap<PartnerConfig, Object>) partnerConfig, (PartnerConfig) Integer.valueOf(i));
                return i;
            } catch (NullPointerException unused) {
                return i;
            }
        }
    }

    public final float getDimension(Context context, PartnerConfig partnerConfig, float f) {
        if (partnerConfig.getResourceType() != PartnerConfig.ResourceType.DIMENSION) {
            throw new IllegalArgumentException("Not a dimension resource");
        } else if (this.partnerResourceCache.containsKey(partnerConfig)) {
            return ((TypedValue) this.partnerResourceCache.get(partnerConfig)).getDimension(context.getResources().getDisplayMetrics());
        } else {
            try {
                ResourceEntry resourceEntryFromKey = getResourceEntryFromKey(context, partnerConfig.getResourceName());
                Objects.requireNonNull(resourceEntryFromKey);
                Resources resources = resourceEntryFromKey.resources;
                int i = resourceEntryFromKey.resourceId;
                f = resources.getDimension(i);
                this.partnerResourceCache.put((EnumMap<PartnerConfig, Object>) partnerConfig, (PartnerConfig) getTypedValueFromResource(resources, i));
                return ((TypedValue) this.partnerResourceCache.get(partnerConfig)).getDimension(context.getResources().getDisplayMetrics());
            } catch (Resources.NotFoundException | NullPointerException unused) {
                return f;
            }
        }
    }

    public final Drawable getDrawable(Context context, PartnerConfig partnerConfig) {
        if (partnerConfig.getResourceType() != PartnerConfig.ResourceType.DRAWABLE) {
            throw new IllegalArgumentException("Not a drawable resource");
        } else if (this.partnerResourceCache.containsKey(partnerConfig)) {
            return (Drawable) this.partnerResourceCache.get(partnerConfig);
        } else {
            Drawable drawable = null;
            try {
                ResourceEntry resourceEntryFromKey = getResourceEntryFromKey(context, partnerConfig.getResourceName());
                Objects.requireNonNull(resourceEntryFromKey);
                Resources resources = resourceEntryFromKey.resources;
                int i = resourceEntryFromKey.resourceId;
                TypedValue typedValue = new TypedValue();
                resources.getValue(i, typedValue, true);
                if (typedValue.type == 1 && typedValue.data == 0) {
                    return null;
                }
                drawable = resources.getDrawable(i, null);
                this.partnerResourceCache.put((EnumMap<PartnerConfig, Object>) partnerConfig, (PartnerConfig) drawable);
                return drawable;
            } catch (Resources.NotFoundException | NullPointerException unused) {
                return drawable;
            }
        }
    }

    public final float getFraction(Context context, PartnerConfig partnerConfig) {
        if (partnerConfig.getResourceType() != PartnerConfig.ResourceType.FRACTION) {
            throw new IllegalArgumentException("Not a fraction resource");
        } else if (this.partnerResourceCache.containsKey(partnerConfig)) {
            return ((Float) this.partnerResourceCache.get(partnerConfig)).floatValue();
        } else {
            try {
                ResourceEntry resourceEntryFromKey = getResourceEntryFromKey(context, partnerConfig.getResourceName());
                Objects.requireNonNull(resourceEntryFromKey);
                float fraction = resourceEntryFromKey.resources.getFraction(resourceEntryFromKey.resourceId, 1, 1);
                try {
                    this.partnerResourceCache.put((EnumMap<PartnerConfig, Object>) partnerConfig, (PartnerConfig) Float.valueOf(fraction));
                    return fraction;
                } catch (Resources.NotFoundException | NullPointerException unused) {
                    return fraction;
                }
            } catch (Resources.NotFoundException | NullPointerException unused2) {
                return 0.0f;
            }
        }
    }

    public final int getInteger(Context context, PartnerConfig partnerConfig) {
        if (partnerConfig.getResourceType() != PartnerConfig.ResourceType.INTEGER) {
            throw new IllegalArgumentException("Not a integer resource");
        } else if (this.partnerResourceCache.containsKey(partnerConfig)) {
            return ((Integer) this.partnerResourceCache.get(partnerConfig)).intValue();
        } else {
            try {
                ResourceEntry resourceEntryFromKey = getResourceEntryFromKey(context, partnerConfig.getResourceName());
                Objects.requireNonNull(resourceEntryFromKey);
                int integer = resourceEntryFromKey.resources.getInteger(resourceEntryFromKey.resourceId);
                try {
                    this.partnerResourceCache.put((EnumMap<PartnerConfig, Object>) partnerConfig, (PartnerConfig) Integer.valueOf(integer));
                    return integer;
                } catch (Resources.NotFoundException | NullPointerException unused) {
                    return integer;
                }
            } catch (Resources.NotFoundException | NullPointerException unused2) {
                return 0;
            }
        }
    }

    public final String getString(Context context, PartnerConfig partnerConfig) {
        if (partnerConfig.getResourceType() != PartnerConfig.ResourceType.STRING) {
            throw new IllegalArgumentException("Not a string resource");
        } else if (this.partnerResourceCache.containsKey(partnerConfig)) {
            return (String) this.partnerResourceCache.get(partnerConfig);
        } else {
            String str = null;
            try {
                ResourceEntry resourceEntryFromKey = getResourceEntryFromKey(context, partnerConfig.getResourceName());
                Objects.requireNonNull(resourceEntryFromKey);
                str = resourceEntryFromKey.resources.getString(resourceEntryFromKey.resourceId);
                this.partnerResourceCache.put((EnumMap<PartnerConfig, Object>) partnerConfig, (PartnerConfig) str);
                return str;
            } catch (NullPointerException unused) {
                return str;
            }
        }
    }

    public final boolean isPartnerConfigAvailable(PartnerConfig partnerConfig) {
        if (!isAvailable() || !this.resultBundle.containsKey(partnerConfig.getResourceName())) {
            return false;
        }
        return true;
    }
}
