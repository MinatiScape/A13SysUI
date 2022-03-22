package androidx.mediarouter.app;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
/* loaded from: classes.dex */
public final class MediaRouteDialogHelper {
    public static int getDialogWidth(Context context) {
        boolean z;
        int i;
        float fraction;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        if (displayMetrics.widthPixels < displayMetrics.heightPixels) {
            z = true;
        } else {
            z = false;
        }
        TypedValue typedValue = new TypedValue();
        Resources resources = context.getResources();
        if (z) {
            i = 2131166380;
        } else {
            i = 2131166379;
        }
        resources.getValue(i, typedValue, true);
        int i2 = typedValue.type;
        if (i2 == 5) {
            fraction = typedValue.getDimension(displayMetrics);
        } else if (i2 != 6) {
            return -2;
        } else {
            int i3 = displayMetrics.widthPixels;
            fraction = typedValue.getFraction(i3, i3);
        }
        return (int) fraction;
    }
}
