package androidx.transition;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import java.util.Objects;
import java.util.WeakHashMap;
import org.xmlpull.v1.XmlPullParser;
/* loaded from: classes.dex */
public class ChangeTransform extends Transition {
    public boolean mReparent;
    public Matrix mTempMatrix;
    public boolean mUseOverlay;
    public static final String[] sTransitionProperties = {"android:changeTransform:matrix", "android:changeTransform:transforms", "android:changeTransform:parentMatrix"};
    public static final AnonymousClass1 NON_TRANSLATIONS_PROPERTY = new Property<PathAnimatorMatrix, float[]>() { // from class: androidx.transition.ChangeTransform.1
        @Override // android.util.Property
        public final /* bridge */ /* synthetic */ float[] get(PathAnimatorMatrix pathAnimatorMatrix) {
            return null;
        }

        @Override // android.util.Property
        public final void set(PathAnimatorMatrix pathAnimatorMatrix, float[] fArr) {
            PathAnimatorMatrix pathAnimatorMatrix2 = pathAnimatorMatrix;
            float[] fArr2 = fArr;
            Objects.requireNonNull(pathAnimatorMatrix2);
            System.arraycopy(fArr2, 0, pathAnimatorMatrix2.mValues, 0, fArr2.length);
            pathAnimatorMatrix2.setAnimationMatrix();
        }
    };
    public static final AnonymousClass2 TRANSLATIONS_PROPERTY = new Property<PathAnimatorMatrix, PointF>(PointF.class) { // from class: androidx.transition.ChangeTransform.2
        @Override // android.util.Property
        public final /* bridge */ /* synthetic */ PointF get(PathAnimatorMatrix pathAnimatorMatrix) {
            return null;
        }

        @Override // android.util.Property
        public final void set(PathAnimatorMatrix pathAnimatorMatrix, PointF pointF) {
            PathAnimatorMatrix pathAnimatorMatrix2 = pathAnimatorMatrix;
            PointF pointF2 = pointF;
            Objects.requireNonNull(pathAnimatorMatrix2);
            pathAnimatorMatrix2.mTranslationX = pointF2.x;
            pathAnimatorMatrix2.mTranslationY = pointF2.y;
            pathAnimatorMatrix2.setAnimationMatrix();
        }
    };
    public static final boolean SUPPORTS_VIEW_REMOVAL_SUPPRESSION = true;

    /* loaded from: classes.dex */
    public static class GhostListener extends TransitionListenerAdapter {
        public GhostView mGhostView;
        public View mView;

        @Override // androidx.transition.TransitionListenerAdapter, androidx.transition.Transition.TransitionListener
        public final void onTransitionPause() {
            this.mGhostView.setVisibility(4);
        }

        @Override // androidx.transition.TransitionListenerAdapter, androidx.transition.Transition.TransitionListener
        public final void onTransitionResume() {
            this.mGhostView.setVisibility(0);
        }

        public GhostListener(View view, GhostViewPort ghostViewPort) {
            this.mView = view;
            this.mGhostView = ghostViewPort;
        }

        @Override // androidx.transition.Transition.TransitionListener
        public final void onTransitionEnd(Transition transition) {
            transition.removeListener(this);
            View view = this.mView;
            int i = GhostViewPort.$r8$clinit;
            GhostViewPort ghostViewPort = (GhostViewPort) view.getTag(2131428020);
            if (ghostViewPort != null) {
                int i2 = ghostViewPort.mReferences - 1;
                ghostViewPort.mReferences = i2;
                if (i2 <= 0) {
                    ((GhostViewHolder) ghostViewPort.getParent()).removeView(ghostViewPort);
                }
            }
            this.mView.setTag(2131429099, null);
            this.mView.setTag(2131428565, null);
        }
    }

    /* loaded from: classes.dex */
    public static class PathAnimatorMatrix {
        public final Matrix mMatrix = new Matrix();
        public float mTranslationX;
        public float mTranslationY;
        public final float[] mValues;
        public final View mView;

        public final void setAnimationMatrix() {
            float[] fArr = this.mValues;
            fArr[2] = this.mTranslationX;
            fArr[5] = this.mTranslationY;
            this.mMatrix.setValues(fArr);
            View view = this.mView;
            Matrix matrix = this.mMatrix;
            ViewUtilsApi29 viewUtilsApi29 = ViewUtils.IMPL;
            view.setAnimationMatrix(matrix);
        }

        public PathAnimatorMatrix(View view, float[] fArr) {
            this.mView = view;
            float[] fArr2 = (float[]) fArr.clone();
            this.mValues = fArr2;
            this.mTranslationX = fArr2[2];
            this.mTranslationY = fArr2[5];
            setAnimationMatrix();
        }
    }

    /* loaded from: classes.dex */
    public static class Transforms {
        public final float mRotationX;
        public final float mRotationY;
        public final float mRotationZ;
        public final float mScaleX;
        public final float mScaleY;
        public final float mTranslationX;
        public final float mTranslationY;
        public final float mTranslationZ;

        public final boolean equals(Object obj) {
            if (!(obj instanceof Transforms)) {
                return false;
            }
            Transforms transforms = (Transforms) obj;
            if (transforms.mTranslationX == this.mTranslationX && transforms.mTranslationY == this.mTranslationY && transforms.mTranslationZ == this.mTranslationZ && transforms.mScaleX == this.mScaleX && transforms.mScaleY == this.mScaleY && transforms.mRotationX == this.mRotationX && transforms.mRotationY == this.mRotationY && transforms.mRotationZ == this.mRotationZ) {
                return true;
            }
            return false;
        }

        public final int hashCode() {
            int i;
            int i2;
            int i3;
            int i4;
            int i5;
            int i6;
            int i7;
            float f = this.mTranslationX;
            int i8 = 0;
            if (f != 0.0f) {
                i = Float.floatToIntBits(f);
            } else {
                i = 0;
            }
            int i9 = i * 31;
            float f2 = this.mTranslationY;
            if (f2 != 0.0f) {
                i2 = Float.floatToIntBits(f2);
            } else {
                i2 = 0;
            }
            int i10 = (i9 + i2) * 31;
            float f3 = this.mTranslationZ;
            if (f3 != 0.0f) {
                i3 = Float.floatToIntBits(f3);
            } else {
                i3 = 0;
            }
            int i11 = (i10 + i3) * 31;
            float f4 = this.mScaleX;
            if (f4 != 0.0f) {
                i4 = Float.floatToIntBits(f4);
            } else {
                i4 = 0;
            }
            int i12 = (i11 + i4) * 31;
            float f5 = this.mScaleY;
            if (f5 != 0.0f) {
                i5 = Float.floatToIntBits(f5);
            } else {
                i5 = 0;
            }
            int i13 = (i12 + i5) * 31;
            float f6 = this.mRotationX;
            if (f6 != 0.0f) {
                i6 = Float.floatToIntBits(f6);
            } else {
                i6 = 0;
            }
            int i14 = (i13 + i6) * 31;
            float f7 = this.mRotationY;
            if (f7 != 0.0f) {
                i7 = Float.floatToIntBits(f7);
            } else {
                i7 = 0;
            }
            int i15 = (i14 + i7) * 31;
            float f8 = this.mRotationZ;
            if (f8 != 0.0f) {
                i8 = Float.floatToIntBits(f8);
            }
            return i15 + i8;
        }

        public Transforms(View view) {
            this.mTranslationX = view.getTranslationX();
            this.mTranslationY = view.getTranslationY();
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            this.mTranslationZ = ViewCompat.Api21Impl.getTranslationZ(view);
            this.mScaleX = view.getScaleX();
            this.mScaleY = view.getScaleY();
            this.mRotationX = view.getRotationX();
            this.mRotationY = view.getRotationY();
            this.mRotationZ = view.getRotation();
        }
    }

    public ChangeTransform() {
        this.mUseOverlay = true;
        this.mReparent = true;
        this.mTempMatrix = new Matrix();
    }

    public final void captureValues(TransitionValues transitionValues) {
        Matrix matrix;
        View view = transitionValues.view;
        if (view.getVisibility() != 8) {
            transitionValues.values.put("android:changeTransform:parent", view.getParent());
            transitionValues.values.put("android:changeTransform:transforms", new Transforms(view));
            Matrix matrix2 = view.getMatrix();
            if (matrix2 == null || matrix2.isIdentity()) {
                matrix = null;
            } else {
                matrix = new Matrix(matrix2);
            }
            transitionValues.values.put("android:changeTransform:matrix", matrix);
            if (this.mReparent) {
                Matrix matrix3 = new Matrix();
                ViewGroup viewGroup = (ViewGroup) view.getParent();
                ViewUtilsApi29 viewUtilsApi29 = ViewUtils.IMPL;
                viewGroup.transformMatrixToGlobal(matrix3);
                matrix3.preTranslate(-viewGroup.getScrollX(), -viewGroup.getScrollY());
                transitionValues.values.put("android:changeTransform:parentMatrix", matrix3);
                transitionValues.values.put("android:changeTransform:intermediateMatrix", view.getTag(2131429099));
                transitionValues.values.put("android:changeTransform:intermediateParentMatrix", view.getTag(2131428565));
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x0050, code lost:
        r0 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x027c, code lost:
        if (r14.getZ() > r2.getZ()) goto L_0x02ae;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x02a5, code lost:
        if (r3.size() == r4) goto L_0x02ae;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x02a8, code lost:
        r2 = false;
     */
    /* JADX WARN: Removed duplicated region for block: B:120:0x0324  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0068  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x007b  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0082  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00ce  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00d2  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00de  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00e6  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x02b1  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x02b7  */
    @Override // androidx.transition.Transition
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final android.animation.Animator createAnimator(android.view.ViewGroup r26, androidx.transition.TransitionValues r27, androidx.transition.TransitionValues r28) {
        /*
            Method dump skipped, instructions count: 814
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.transition.ChangeTransform.createAnimator(android.view.ViewGroup, androidx.transition.TransitionValues, androidx.transition.TransitionValues):android.animation.Animator");
    }

    @Override // androidx.transition.Transition
    public final void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
        if (!SUPPORTS_VIEW_REMOVAL_SUPPRESSION) {
            ((ViewGroup) transitionValues.view.getParent()).startViewTransition(transitionValues.view);
        }
    }

    @SuppressLint({"RestrictedApi"})
    public ChangeTransform(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        boolean z = true;
        this.mUseOverlay = true;
        this.mReparent = true;
        this.mTempMatrix = new Matrix();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, Styleable.CHANGE_TRANSFORM);
        XmlPullParser xmlPullParser = (XmlPullParser) attributeSet;
        this.mUseOverlay = !TypedArrayUtils.hasAttribute(xmlPullParser, "reparentWithOverlay") ? true : obtainStyledAttributes.getBoolean(1, true);
        this.mReparent = TypedArrayUtils.hasAttribute(xmlPullParser, "reparent") ? obtainStyledAttributes.getBoolean(0, true) : z;
        obtainStyledAttributes.recycle();
    }

    @Override // androidx.transition.Transition
    public final void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override // androidx.transition.Transition
    public final String[] getTransitionProperties() {
        return sTransitionProperties;
    }
}
