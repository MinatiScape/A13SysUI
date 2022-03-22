package com.android.wm.shell.animation;

import android.graphics.Rect;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
/* compiled from: FloatProperties.kt */
/* loaded from: classes.dex */
public final class FloatProperties {
    public static final FloatProperties$Companion$RECT_X$1 RECT_X = new FloatPropertyCompat<Rect>() { // from class: com.android.wm.shell.animation.FloatProperties$Companion$RECT_X$1
        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final float getValue(Rect rect) {
            Rect rect2 = rect;
            if (rect2 == null) {
                return -3.4028235E38f;
            }
            return rect2.left;
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final void setValue(Rect rect, float f) {
            Rect rect2 = rect;
            if (rect2 != null) {
                rect2.offsetTo((int) f, rect2.top);
            }
        }
    };
    public static final FloatProperties$Companion$RECT_Y$1 RECT_Y = new FloatPropertyCompat<Rect>() { // from class: com.android.wm.shell.animation.FloatProperties$Companion$RECT_Y$1
        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final float getValue(Rect rect) {
            Rect rect2 = rect;
            if (rect2 == null) {
                return -3.4028235E38f;
            }
            return rect2.top;
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final void setValue(Rect rect, float f) {
            Rect rect2 = rect;
            if (rect2 != null) {
                rect2.offsetTo(rect2.left, (int) f);
            }
        }
    };
    public static final FloatProperties$Companion$RECT_WIDTH$1 RECT_WIDTH = new FloatPropertyCompat<Rect>() { // from class: com.android.wm.shell.animation.FloatProperties$Companion$RECT_WIDTH$1
        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final float getValue(Rect rect) {
            return rect.width();
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final void setValue(Rect rect, float f) {
            Rect rect2 = rect;
            rect2.right = rect2.left + ((int) f);
        }
    };
    public static final FloatProperties$Companion$RECT_HEIGHT$1 RECT_HEIGHT = new FloatPropertyCompat<Rect>() { // from class: com.android.wm.shell.animation.FloatProperties$Companion$RECT_HEIGHT$1
        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final float getValue(Rect rect) {
            return rect.height();
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final void setValue(Rect rect, float f) {
            Rect rect2 = rect;
            rect2.bottom = rect2.top + ((int) f);
        }
    };
}
