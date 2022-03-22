package androidx.transition;

import android.graphics.Rect;
import android.util.Property;
import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public final class ViewUtils {
    public static final ViewUtilsApi29 IMPL = new ViewUtilsApi29();
    public static final AnonymousClass1 TRANSITION_ALPHA = new Property<View, Float>() { // from class: androidx.transition.ViewUtils.1
        @Override // android.util.Property
        public final Float get(View view) {
            return Float.valueOf(view.getTransitionAlpha());
        }

        @Override // android.util.Property
        public final void set(View view, Float f) {
            view.setTransitionAlpha(f.floatValue());
        }
    };
    public static final AnonymousClass2 CLIP_BOUNDS = new Property<View, Rect>(Rect.class) { // from class: androidx.transition.ViewUtils.2
        @Override // android.util.Property
        public final Rect get(View view) {
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            return ViewCompat.Api18Impl.getClipBounds(view);
        }

        @Override // android.util.Property
        public final void set(View view, Rect rect) {
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            ViewCompat.Api18Impl.setClipBounds(view, rect);
        }
    };
}
