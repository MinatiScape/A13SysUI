package com.airbnb.lottie.utils;

import android.graphics.PointF;
import com.airbnb.lottie.animation.content.KeyPathElementContent;
import com.airbnb.lottie.model.KeyPath;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MiscUtils {
    public static PointF pathFromDataCurrentPoint = new PointF();

    public static int floorMod(float f, float f2) {
        boolean z;
        int i = (int) f;
        int i2 = (int) f2;
        int i3 = i / i2;
        if ((i ^ i2) >= 0) {
            z = true;
        } else {
            z = false;
        }
        int i4 = i % i2;
        if (!z && i4 != 0) {
            i3--;
        }
        return i - (i2 * i3);
    }

    public static PointF addPoints(PointF pointF, PointF pointF2) {
        return new PointF(pointF.x + pointF2.x, pointF.y + pointF2.y);
    }

    public static float clamp(float f, float f2, float f3) {
        return Math.max(f2, Math.min(f3, f));
    }

    public static void resolveKeyPath(KeyPath keyPath, int i, ArrayList arrayList, KeyPath keyPath2, KeyPathElementContent keyPathElementContent) {
        if (keyPath.fullyResolvesTo(keyPathElementContent.getName(), i)) {
            String name = keyPathElementContent.getName();
            Objects.requireNonNull(keyPath2);
            KeyPath keyPath3 = new KeyPath(keyPath2);
            keyPath3.keys.add(name);
            KeyPath keyPath4 = new KeyPath(keyPath3);
            keyPath4.resolvedElement = keyPathElementContent;
            arrayList.add(keyPath4);
        }
    }
}
