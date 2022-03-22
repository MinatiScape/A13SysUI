package androidx.transition;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class ChangeBounds extends Transition {
    public boolean mResizeClip;
    public int[] mTempLocation;
    public static final String[] sTransitionProperties = {"android:changeBounds:bounds", "android:changeBounds:clip", "android:changeBounds:parent", "android:changeBounds:windowX", "android:changeBounds:windowY"};
    public static final AnonymousClass1 DRAWABLE_ORIGIN_PROPERTY = new Property<Drawable, PointF>(PointF.class) { // from class: androidx.transition.ChangeBounds.1
        public Rect mBounds = new Rect();

        @Override // android.util.Property
        public final PointF get(Drawable drawable) {
            drawable.copyBounds(this.mBounds);
            Rect rect = this.mBounds;
            return new PointF(rect.left, rect.top);
        }

        @Override // android.util.Property
        public final void set(Drawable drawable, PointF pointF) {
            Drawable drawable2 = drawable;
            PointF pointF2 = pointF;
            drawable2.copyBounds(this.mBounds);
            this.mBounds.offsetTo(Math.round(pointF2.x), Math.round(pointF2.y));
            drawable2.setBounds(this.mBounds);
        }
    };
    public static final AnonymousClass2 TOP_LEFT_PROPERTY = new Property<ViewBounds, PointF>(PointF.class) { // from class: androidx.transition.ChangeBounds.2
        @Override // android.util.Property
        public final /* bridge */ /* synthetic */ PointF get(ViewBounds viewBounds) {
            return null;
        }

        @Override // android.util.Property
        public final void set(ViewBounds viewBounds, PointF pointF) {
            ViewBounds viewBounds2 = viewBounds;
            PointF pointF2 = pointF;
            Objects.requireNonNull(viewBounds2);
            viewBounds2.mLeft = Math.round(pointF2.x);
            int round = Math.round(pointF2.y);
            viewBounds2.mTop = round;
            int i = viewBounds2.mTopLeftCalls + 1;
            viewBounds2.mTopLeftCalls = i;
            if (i == viewBounds2.mBottomRightCalls) {
                View view = viewBounds2.mView;
                int i2 = viewBounds2.mLeft;
                int i3 = viewBounds2.mRight;
                int i4 = viewBounds2.mBottom;
                ViewUtilsApi29 viewUtilsApi29 = ViewUtils.IMPL;
                view.setLeftTopRightBottom(i2, round, i3, i4);
                viewBounds2.mTopLeftCalls = 0;
                viewBounds2.mBottomRightCalls = 0;
            }
        }
    };
    public static final AnonymousClass3 BOTTOM_RIGHT_PROPERTY = new Property<ViewBounds, PointF>(PointF.class) { // from class: androidx.transition.ChangeBounds.3
        @Override // android.util.Property
        public final /* bridge */ /* synthetic */ PointF get(ViewBounds viewBounds) {
            return null;
        }

        @Override // android.util.Property
        public final void set(ViewBounds viewBounds, PointF pointF) {
            ViewBounds viewBounds2 = viewBounds;
            PointF pointF2 = pointF;
            Objects.requireNonNull(viewBounds2);
            viewBounds2.mRight = Math.round(pointF2.x);
            int round = Math.round(pointF2.y);
            viewBounds2.mBottom = round;
            int i = viewBounds2.mBottomRightCalls + 1;
            viewBounds2.mBottomRightCalls = i;
            if (viewBounds2.mTopLeftCalls == i) {
                View view = viewBounds2.mView;
                int i2 = viewBounds2.mLeft;
                int i3 = viewBounds2.mTop;
                int i4 = viewBounds2.mRight;
                ViewUtilsApi29 viewUtilsApi29 = ViewUtils.IMPL;
                view.setLeftTopRightBottom(i2, i3, i4, round);
                viewBounds2.mTopLeftCalls = 0;
                viewBounds2.mBottomRightCalls = 0;
            }
        }
    };
    public static final AnonymousClass4 BOTTOM_RIGHT_ONLY_PROPERTY = new Property<View, PointF>(PointF.class) { // from class: androidx.transition.ChangeBounds.4
        @Override // android.util.Property
        public final /* bridge */ /* synthetic */ PointF get(View view) {
            return null;
        }

        @Override // android.util.Property
        public final void set(View view, PointF pointF) {
            View view2 = view;
            PointF pointF2 = pointF;
            int left = view2.getLeft();
            int top = view2.getTop();
            int round = Math.round(pointF2.x);
            int round2 = Math.round(pointF2.y);
            ViewUtilsApi29 viewUtilsApi29 = ViewUtils.IMPL;
            view2.setLeftTopRightBottom(left, top, round, round2);
        }
    };
    public static final AnonymousClass5 TOP_LEFT_ONLY_PROPERTY = new Property<View, PointF>(PointF.class) { // from class: androidx.transition.ChangeBounds.5
        @Override // android.util.Property
        public final /* bridge */ /* synthetic */ PointF get(View view) {
            return null;
        }

        @Override // android.util.Property
        public final void set(View view, PointF pointF) {
            View view2 = view;
            PointF pointF2 = pointF;
            int round = Math.round(pointF2.x);
            int round2 = Math.round(pointF2.y);
            int right = view2.getRight();
            int bottom = view2.getBottom();
            ViewUtilsApi29 viewUtilsApi29 = ViewUtils.IMPL;
            view2.setLeftTopRightBottom(round, round2, right, bottom);
        }
    };
    public static final AnonymousClass6 POSITION_PROPERTY = new Property<View, PointF>(PointF.class) { // from class: androidx.transition.ChangeBounds.6
        @Override // android.util.Property
        public final /* bridge */ /* synthetic */ PointF get(View view) {
            return null;
        }

        @Override // android.util.Property
        public final void set(View view, PointF pointF) {
            View view2 = view;
            PointF pointF2 = pointF;
            int round = Math.round(pointF2.x);
            int round2 = Math.round(pointF2.y);
            ViewUtilsApi29 viewUtilsApi29 = ViewUtils.IMPL;
            view2.setLeftTopRightBottom(round, round2, view2.getWidth() + round, view2.getHeight() + round2);
        }
    };
    public static RectEvaluator sRectEvaluator = new RectEvaluator();

    public ChangeBounds() {
        this.mTempLocation = new int[2];
        this.mResizeClip = false;
    }

    /* loaded from: classes.dex */
    public static class ViewBounds {
        public int mBottom;
        public int mBottomRightCalls;
        public int mLeft;
        public int mRight;
        public int mTop;
        public int mTopLeftCalls;
        public View mView;

        public ViewBounds(View view) {
            this.mView = view;
        }
    }

    public final void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        if (ViewCompat.Api19Impl.isLaidOut(view) || view.getWidth() != 0 || view.getHeight() != 0) {
            transitionValues.values.put("android:changeBounds:bounds", new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()));
            transitionValues.values.put("android:changeBounds:parent", transitionValues.view.getParent());
            if (this.mResizeClip) {
                transitionValues.values.put("android:changeBounds:clip", ViewCompat.Api18Impl.getClipBounds(view));
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:64:0x01ae  */
    @Override // androidx.transition.Transition
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final android.animation.Animator createAnimator(android.view.ViewGroup r20, androidx.transition.TransitionValues r21, androidx.transition.TransitionValues r22) {
        /*
            Method dump skipped, instructions count: 454
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.transition.ChangeBounds.createAnimator(android.view.ViewGroup, androidx.transition.TransitionValues, androidx.transition.TransitionValues):android.animation.Animator");
    }

    @SuppressLint({"RestrictedApi"})
    public ChangeBounds(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mTempLocation = new int[2];
        boolean z = false;
        this.mResizeClip = false;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, Styleable.CHANGE_BOUNDS);
        z = TypedArrayUtils.hasAttribute((XmlResourceParser) attributeSet, "resizeClip") ? obtainStyledAttributes.getBoolean(0, false) : z;
        obtainStyledAttributes.recycle();
        this.mResizeClip = z;
    }

    @Override // androidx.transition.Transition
    public final void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override // androidx.transition.Transition
    public final void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override // androidx.transition.Transition
    public final String[] getTransitionProperties() {
        return sTransitionProperties;
    }
}
