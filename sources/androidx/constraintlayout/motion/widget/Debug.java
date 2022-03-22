package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.view.View;
/* loaded from: classes.dex */
public final class Debug {
    public static String getName(View view) {
        try {
            return view.getContext().getResources().getResourceEntryName(view.getId());
        } catch (Exception unused) {
            return "UNKNOWN";
        }
    }

    public static String getState(MotionLayout motionLayout, int i) {
        if (i == -1) {
            return "UNDEFINED";
        }
        return motionLayout.getContext().getResources().getResourceEntryName(i);
    }

    public static String getLocation() {
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[1];
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m(".(");
        m.append(stackTraceElement.getFileName());
        m.append(":");
        m.append(stackTraceElement.getLineNumber());
        m.append(")");
        return m.toString();
    }

    public static String getName(Context context, int i) {
        if (i != -1) {
            try {
                return context.getResources().getResourceEntryName(i);
            } catch (Exception unused) {
            }
        }
        return "UNKNOWN";
    }
}
