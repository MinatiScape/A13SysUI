package androidx.leanback;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.google.android.material.behavior.SwipeDismissBehavior;
import com.google.common.base.Strings;
import java.util.Objects;
/* loaded from: classes.dex */
public final class R$layout {
    public static Boolean sIsEnabled;
    public static final int[] PHONE_SIGNAL_STRENGTH = {2131951763, 2131951769, 2131951772, 2131951771, 2131951770};
    public static final int[] WIFI_CONNECTION_STRENGTH = {2131951764, 2131951823, 2131951828, 2131951827, 2131951826};
    public static final int[] ETHERNET_CONNECTION_VALUES = {2131951719, 2131951718};

    public static int saturatedCast(long j) {
        if (j > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        if (j < -2147483648L) {
            return Integer.MIN_VALUE;
        }
        return (int) j;
    }

    public static final String toString(byte b) {
        if (b == 0) {
            return "BUILT_IN";
        }
        if (b == 1) {
            return "FIXED";
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("0x");
        m.append(Integer.toHexString(Byte.toUnsignedInt(b)));
        return m.toString();
    }

    public /* synthetic */ R$layout(SwipeDismissBehavior swipeDismissBehavior) {
        Objects.requireNonNull(swipeDismissBehavior);
        swipeDismissBehavior.alphaStartSwipeDistance = Math.min(Math.max(0.0f, 0.1f), 1.0f);
        swipeDismissBehavior.alphaEndSwipeDistance = Math.min(Math.max(0.0f, 0.6f), 1.0f);
        swipeDismissBehavior.swipeDirection = 0;
    }

    public static int constrainToRange(int i, int i2) {
        boolean z;
        if (i2 <= 1073741823) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            return Math.min(Math.max(i, i2), 1073741823);
        }
        throw new IllegalArgumentException(Strings.lenientFormat("min (%s) must be less than or equal to max (%s)", Integer.valueOf(i2), 1073741823));
    }
}
