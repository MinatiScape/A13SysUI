package androidx.preference;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
/* loaded from: classes.dex */
public final class R$id {
    public static Resources getResourcesForRotation(int i, Context context) {
        int i2 = 2;
        if (i != 0) {
            if (i != 1) {
                if (i != 2) {
                    if (i != 3) {
                        throw new IllegalArgumentException(VendorAtomValue$$ExternalSyntheticOutline0.m("Unknown rotation: ", i));
                    }
                }
            }
            Configuration configuration = new Configuration(context.getResources().getConfiguration());
            configuration.orientation = i2;
            return context.createConfigurationContext(configuration).getResources();
        }
        i2 = 1;
        Configuration configuration2 = new Configuration(context.getResources().getConfiguration());
        configuration2.orientation = i2;
        return context.createConfigurationContext(configuration2).getResources();
    }

    public static String toShortString(int i) {
        if (i == 0) {
            return "SHD";
        }
        if (i == 1) {
            return "KGRD";
        }
        if (i != 2) {
            return VendorAtomValue$$ExternalSyntheticOutline0.m("bad_value_", i);
        }
        return "SHD_LCK";
    }

    public static int getExactRotation(Context context) {
        int rotation = context.getDisplay().getRotation();
        if (rotation == 1) {
            return 1;
        }
        if (rotation == 3) {
            return 3;
        }
        if (rotation == 2) {
            return 2;
        }
        return 0;
    }

    public static int getRotation(Context context) {
        int rotation = context.getDisplay().getRotation();
        if (rotation == 1) {
            return 1;
        }
        if (rotation == 3) {
            return 3;
        }
        return 0;
    }
}
