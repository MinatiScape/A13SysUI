package com.android.systemui;

import android.content.Context;
import android.graphics.Color;
import android.os.Looper;
import android.widget.ImageView;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import com.android.internal.util.ContrastColorUtil;
import java.util.concurrent.CancellationException;
import kotlinx.coroutines.channels.ReceiveChannel;
/* loaded from: classes.dex */
public final class R$array {
    public static final int[] sLocationBase = new int[2];
    public static final int[] sLocationOffset = new int[2];

    public static final void cancelConsumed(ReceiveChannel receiveChannel, Throwable th) {
        CancellationException cancellationException = null;
        if (th != null) {
            if (th instanceof CancellationException) {
                cancellationException = (CancellationException) th;
            }
            if (cancellationException == null) {
                cancellationException = new CancellationException("Channel was consumed, consumer had failed");
                cancellationException.initCause(th);
            }
        }
        receiveChannel.cancel(cancellationException);
    }

    public static float interpolate(float f, float f2, float f3) {
        return (f2 * f3) + ((1.0f - f3) * f);
    }

    public static void checkArgument(boolean z, String str) {
        if (!z) {
            throw new IllegalArgumentException(str);
        }
    }

    public static String logKey(String str) {
        if (str == null) {
            return "null";
        }
        return str.replace("\n", "");
    }

    public static void ensureOnMainThread(String str) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException(SupportMenuInflater$$ExternalSyntheticOutline0.m(str, " must be called from the UI thread."));
        }
    }

    public static int getFontScaledHeight(Context context, int i) {
        return (int) (context.getResources().getDimensionPixelSize(i) * Math.max(1.0f, context.getResources().getDisplayMetrics().scaledDensity / context.getResources().getDisplayMetrics().density));
    }

    public static int interpolateColors(int i, int i2, float f) {
        return Color.argb((int) interpolate(Color.alpha(i), Color.alpha(i2), f), (int) interpolate(Color.red(i), Color.red(i2), f), (int) interpolate(Color.green(i), Color.green(i2), f), (int) interpolate(Color.blue(i), Color.blue(i2), f));
    }

    public static boolean isGrayscale(ImageView imageView, ContrastColorUtil contrastColorUtil) {
        Object tag = imageView.getTag(2131428106);
        if (tag != null) {
            return Boolean.TRUE.equals(tag);
        }
        boolean isGrayscaleIcon = contrastColorUtil.isGrayscaleIcon(imageView.getDrawable());
        imageView.setTag(2131428106, Boolean.valueOf(isGrayscaleIcon));
        return isGrayscaleIcon;
    }
}
