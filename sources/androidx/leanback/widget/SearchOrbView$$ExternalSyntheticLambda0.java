package androidx.leanback.widget;

import android.animation.ValueAnimator;
import android.view.View;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import com.google.android.systemui.assist.uihints.GreetingView;
import java.util.Objects;
import java.util.WeakHashMap;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class SearchOrbView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ SearchOrbView$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        switch (this.$r8$classId) {
            case 0:
                SearchOrbView searchOrbView = (SearchOrbView) this.f$0;
                int i = SearchOrbView.$r8$clinit;
                Objects.requireNonNull(searchOrbView);
                float animatedFraction = valueAnimator.getAnimatedFraction();
                View view = searchOrbView.mSearchOrbView;
                float f = searchOrbView.mUnfocusedZ;
                float m = MotionController$$ExternalSyntheticOutline0.m(searchOrbView.mFocusedZ, f, animatedFraction, f);
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                ViewCompat.Api21Impl.setZ(view, m);
                return;
            default:
                GreetingView.StaggeredSpan staggeredSpan = (GreetingView.StaggeredSpan) this.f$0;
                int i2 = GreetingView.StaggeredSpan.$r8$clinit;
                Objects.requireNonNull(staggeredSpan);
                staggeredSpan.mShift = (int) (((Float) valueAnimator.getAnimatedValue()).floatValue() * GreetingView.this.START_DELTA);
                return;
        }
    }
}
