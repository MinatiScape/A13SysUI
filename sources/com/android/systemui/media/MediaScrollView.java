package com.android.systemui.media;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import com.android.systemui.Gefingerpoken;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.animation.PhysicsAnimatorKt;
import java.util.Objects;
import java.util.WeakHashMap;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
/* compiled from: MediaScrollView.kt */
/* loaded from: classes.dex */
public final class MediaScrollView extends HorizontalScrollView {
    public float animationTargetX;
    public ViewGroup contentContainer;
    public Gefingerpoken touchListener;

    public MediaScrollView(Context context) {
        this(context, null, 0, 6, null);
    }

    public MediaScrollView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, null);
    }

    public /* synthetic */ MediaScrollView(Context context, AttributeSet attributeSet, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i2 & 2) != 0 ? null : attributeSet, (i2 & 4) != 0 ? 0 : i);
    }

    public MediaScrollView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public final float getContentTranslation() {
        ViewGroup viewGroup = this.contentContainer;
        ViewGroup viewGroup2 = null;
        if (viewGroup == null) {
            viewGroup = null;
        }
        WeakHashMap<Object, PhysicsAnimator<?>> weakHashMap = PhysicsAnimatorKt.animators;
        Function1<Object, ? extends PhysicsAnimator<?>> function1 = PhysicsAnimator.instanceConstructor;
        if (PhysicsAnimator.Companion.getInstance(viewGroup).isRunning()) {
            return this.animationTargetX;
        }
        ViewGroup viewGroup3 = this.contentContainer;
        if (viewGroup3 != null) {
            viewGroup2 = viewGroup3;
        }
        return viewGroup2.getTranslationX();
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean z;
        Gefingerpoken gefingerpoken = this.touchListener;
        if (gefingerpoken == null) {
            z = false;
        } else {
            z = gefingerpoken.onInterceptTouchEvent(motionEvent);
        }
        if (super.onInterceptTouchEvent(motionEvent) || z) {
            return true;
        }
        return false;
    }

    @Override // android.widget.HorizontalScrollView, android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z;
        Gefingerpoken gefingerpoken = this.touchListener;
        if (gefingerpoken == null) {
            z = false;
        } else {
            z = gefingerpoken.onTouchEvent(motionEvent);
        }
        if (super.onTouchEvent(motionEvent) || z) {
            return true;
        }
        return false;
    }

    @Override // android.widget.HorizontalScrollView, android.view.View
    public final void scrollTo(int i, int i2) {
        int i3 = ((HorizontalScrollView) this).mScrollX;
        if (i3 != i || ((HorizontalScrollView) this).mScrollY != i2) {
            int i4 = ((HorizontalScrollView) this).mScrollY;
            ((HorizontalScrollView) this).mScrollX = i;
            ((HorizontalScrollView) this).mScrollY = i2;
            invalidateParentCaches();
            onScrollChanged(((HorizontalScrollView) this).mScrollX, ((HorizontalScrollView) this).mScrollY, i3, i4);
            if (!awakenScrollBars()) {
                postInvalidateOnAnimation();
            }
        }
    }

    public final void cancelCurrentScroll() {
        long uptimeMillis = SystemClock.uptimeMillis();
        MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
        obtain.setSource(4098);
        super.onTouchEvent(obtain);
        obtain.recycle();
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        View childAt = getChildAt(0);
        Objects.requireNonNull(childAt, "null cannot be cast to non-null type android.view.ViewGroup");
        this.contentContainer = (ViewGroup) childAt;
    }

    @Override // android.view.View
    public final boolean overScrollBy(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, boolean z) {
        boolean z2;
        if (getContentTranslation() == 0.0f) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (!z2) {
            return false;
        }
        return super.overScrollBy(i, i2, i3, i4, i5, i6, i7, i8, z);
    }
}
