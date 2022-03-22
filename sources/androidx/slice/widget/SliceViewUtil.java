package androidx.slice.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import java.util.Calendar;
/* loaded from: classes.dex */
public final class SliceViewUtil {
    public static int getColorAttr(Context context, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{i});
        int color = obtainStyledAttributes.getColor(0, 0);
        obtainStyledAttributes.recycle();
        return color;
    }

    public static Drawable getDrawable(Context context, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{i});
        Drawable drawable = obtainStyledAttributes.getDrawable(0);
        obtainStyledAttributes.recycle();
        return drawable;
    }

    public static CharSequence getTimestampString(Context context, long j) {
        if (j < System.currentTimeMillis() || DateUtils.isToday(j)) {
            return DateUtils.getRelativeTimeSpanString(j, Calendar.getInstance().getTimeInMillis(), 60000L, 262144);
        }
        return DateUtils.formatDateTime(context, j, 8);
    }
}
