package androidx.vectordrawable.graphics.drawable;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
/* loaded from: classes.dex */
public interface Animatable2Compat extends Animatable {

    /* loaded from: classes.dex */
    public static abstract class AnimationCallback {
        public void onAnimationEnd(Drawable drawable) {
        }
    }

    void clearAnimationCallbacks();

    void registerAnimationCallback(AnimationCallback animationCallback);
}
