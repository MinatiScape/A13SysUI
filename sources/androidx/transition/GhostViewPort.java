package androidx.transition;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import java.util.WeakHashMap;
@SuppressLint({"ViewConstructor"})
/* loaded from: classes.dex */
public final class GhostViewPort extends ViewGroup implements GhostView {
    public static final /* synthetic */ int $r8$clinit = 0;
    public Matrix mMatrix;
    public final AnonymousClass1 mOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener() { // from class: androidx.transition.GhostViewPort.1
        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public final boolean onPreDraw() {
            View view;
            GhostViewPort ghostViewPort = GhostViewPort.this;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            ViewCompat.Api16Impl.postInvalidateOnAnimation(ghostViewPort);
            GhostViewPort ghostViewPort2 = GhostViewPort.this;
            ViewGroup viewGroup = ghostViewPort2.mStartParent;
            if (viewGroup == null || (view = ghostViewPort2.mStartView) == null) {
                return true;
            }
            viewGroup.endViewTransition(view);
            ViewCompat.Api16Impl.postInvalidateOnAnimation(GhostViewPort.this.mStartParent);
            GhostViewPort ghostViewPort3 = GhostViewPort.this;
            ghostViewPort3.mStartParent = null;
            ghostViewPort3.mStartView = null;
            return true;
        }
    };
    public int mReferences;
    public ViewGroup mStartParent;
    public View mStartView;
    public final View mView;

    @Override // android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        this.mView.getViewTreeObserver().removeOnPreDrawListener(this.mOnPreDrawListener);
        View view = this.mView;
        ViewUtilsApi29 viewUtilsApi29 = ViewUtils.IMPL;
        view.setTransitionVisibility(0);
        this.mView.setTag(2131428020, null);
        if (this.mView.getParent() != null) {
            ((View) this.mView.getParent()).invalidate();
        }
        super.onDetachedFromWindow();
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [androidx.transition.GhostViewPort$1] */
    public GhostViewPort(View view) {
        super(view.getContext());
        this.mView = view;
        setWillNotDraw(false);
        setClipChildren(false);
        setLayerType(2, null);
    }

    public static void copySize(View view, ViewGroup viewGroup) {
        int left = viewGroup.getLeft();
        int top = viewGroup.getTop();
        int width = view.getWidth() + viewGroup.getLeft();
        int height = view.getHeight() + viewGroup.getTop();
        ViewUtilsApi29 viewUtilsApi29 = ViewUtils.IMPL;
        viewGroup.setLeftTopRightBottom(left, top, width, height);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mView.setTag(2131428020, this);
        this.mView.getViewTreeObserver().addOnPreDrawListener(this.mOnPreDrawListener);
        View view = this.mView;
        ViewUtilsApi29 viewUtilsApi29 = ViewUtils.IMPL;
        view.setTransitionVisibility(4);
        if (this.mView.getParent() != null) {
            ((View) this.mView.getParent()).invalidate();
        }
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        canvas.enableZ();
        canvas.setMatrix(this.mMatrix);
        View view = this.mView;
        ViewUtilsApi29 viewUtilsApi29 = ViewUtils.IMPL;
        view.setTransitionVisibility(0);
        this.mView.invalidate();
        this.mView.setTransitionVisibility(4);
        drawChild(canvas, this.mView, getDrawingTime());
        canvas.disableZ();
    }

    @Override // android.view.View, androidx.transition.GhostView
    public final void setVisibility(int i) {
        int i2;
        super.setVisibility(i);
        if (((GhostViewPort) this.mView.getTag(2131428020)) == this) {
            if (i == 0) {
                i2 = 4;
            } else {
                i2 = 0;
            }
            View view = this.mView;
            ViewUtilsApi29 viewUtilsApi29 = ViewUtils.IMPL;
            view.setTransitionVisibility(i2);
        }
    }
}
