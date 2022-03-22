package androidx.constraintlayout.helper.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.widget.ConstraintHelper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.R$styleable;
import java.util.Objects;
/* loaded from: classes.dex */
public class Layer extends ConstraintHelper {
    public boolean mApplyElevationOnAttach;
    public boolean mApplyVisibilityOnAttach;
    public ConstraintLayout mContainer;
    public float mRotationCenterX = Float.NaN;
    public float mRotationCenterY = Float.NaN;
    public float mGroupRotateAngle = Float.NaN;
    public float mScaleX = 1.0f;
    public float mScaleY = 1.0f;
    public float mComputedCenterX = Float.NaN;
    public float mComputedCenterY = Float.NaN;
    public float mComputedMaxX = Float.NaN;
    public float mComputedMaxY = Float.NaN;
    public float mComputedMinX = Float.NaN;
    public float mComputedMinY = Float.NaN;
    public boolean mNeedBounds = true;
    public View[] mViews = null;
    public float mShiftX = 0.0f;
    public float mShiftY = 0.0f;

    public final void calcCenters() {
        if (this.mContainer != null) {
            if (!(this.mNeedBounds || Float.isNaN(this.mComputedCenterX) || Float.isNaN(this.mComputedCenterY))) {
                return;
            }
            if (Float.isNaN(this.mRotationCenterX) || Float.isNaN(this.mRotationCenterY)) {
                ConstraintLayout constraintLayout = this.mContainer;
                View[] viewArr = super.mViews;
                if (viewArr == null || viewArr.length != this.mCount) {
                    super.mViews = new View[this.mCount];
                }
                for (int i = 0; i < this.mCount; i++) {
                    super.mViews[i] = constraintLayout.getViewById(this.mIds[i]);
                }
                View[] viewArr2 = super.mViews;
                int left = viewArr2[0].getLeft();
                int top = viewArr2[0].getTop();
                int right = viewArr2[0].getRight();
                int bottom = viewArr2[0].getBottom();
                for (int i2 = 0; i2 < this.mCount; i2++) {
                    View view = viewArr2[i2];
                    left = Math.min(left, view.getLeft());
                    top = Math.min(top, view.getTop());
                    right = Math.max(right, view.getRight());
                    bottom = Math.max(bottom, view.getBottom());
                }
                this.mComputedMaxX = right;
                this.mComputedMaxY = bottom;
                this.mComputedMinX = left;
                this.mComputedMinY = top;
                if (Float.isNaN(this.mRotationCenterX)) {
                    this.mComputedCenterX = (left + right) / 2;
                } else {
                    this.mComputedCenterX = this.mRotationCenterX;
                }
                if (Float.isNaN(this.mRotationCenterY)) {
                    this.mComputedCenterY = (top + bottom) / 2;
                } else {
                    this.mComputedCenterY = this.mRotationCenterY;
                }
            } else {
                this.mComputedCenterY = this.mRotationCenterY;
                this.mComputedCenterX = this.mRotationCenterX;
            }
        }
    }

    public final void reCacheViews() {
        int i;
        if (!(this.mContainer == null || (i = this.mCount) == 0)) {
            View[] viewArr = this.mViews;
            if (viewArr == null || viewArr.length != i) {
                this.mViews = new View[i];
            }
            for (int i2 = 0; i2 < this.mCount; i2++) {
                this.mViews[i2] = this.mContainer.getViewById(this.mIds[i2]);
            }
        }
    }

    @Override // android.view.View
    public final void setPivotX(float f) {
        this.mRotationCenterX = f;
        transform();
    }

    @Override // android.view.View
    public final void setPivotY(float f) {
        this.mRotationCenterY = f;
        transform();
    }

    @Override // android.view.View
    public final void setRotation(float f) {
        this.mGroupRotateAngle = f;
        transform();
    }

    @Override // android.view.View
    public final void setScaleX(float f) {
        this.mScaleX = f;
        transform();
    }

    @Override // android.view.View
    public final void setScaleY(float f) {
        this.mScaleY = f;
        transform();
    }

    @Override // android.view.View
    public final void setTranslationX(float f) {
        this.mShiftX = f;
        transform();
    }

    @Override // android.view.View
    public final void setTranslationY(float f) {
        this.mShiftY = f;
        transform();
    }

    public final void transform() {
        if (this.mContainer != null) {
            if (this.mViews == null) {
                reCacheViews();
            }
            calcCenters();
            double radians = Math.toRadians(this.mGroupRotateAngle);
            float sin = (float) Math.sin(radians);
            float cos = (float) Math.cos(radians);
            float f = this.mScaleX;
            float f2 = f * cos;
            float f3 = this.mScaleY;
            float f4 = (-f3) * sin;
            float f5 = f * sin;
            float f6 = f3 * cos;
            for (int i = 0; i < this.mCount; i++) {
                View view = this.mViews[i];
                int left = view.getLeft();
                int top = view.getTop();
                float right = ((view.getRight() + left) / 2) - this.mComputedCenterX;
                float bottom = ((view.getBottom() + top) / 2) - this.mComputedCenterY;
                view.setTranslationX((((f4 * bottom) + (f2 * right)) - right) + this.mShiftX);
                view.setTranslationY((((f6 * bottom) + (right * f5)) - bottom) + this.mShiftY);
                view.setScaleY(this.mScaleY);
                view.setScaleX(this.mScaleX);
                view.setRotation(this.mGroupRotateAngle);
            }
        }
    }

    @Override // androidx.constraintlayout.widget.ConstraintHelper
    public final void updatePreDraw(ConstraintLayout constraintLayout) {
        this.mContainer = constraintLayout;
        getVisibility();
        float rotation = getRotation();
        if (rotation != 0.0f) {
            this.mGroupRotateAngle = rotation;
        } else if (!Float.isNaN(this.mGroupRotateAngle)) {
            this.mGroupRotateAngle = rotation;
        }
        String str = this.mReferenceIds;
        if (str != null) {
            setIds(str);
        }
    }

    public Layer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // androidx.constraintlayout.widget.ConstraintHelper
    public final void init(AttributeSet attributeSet) {
        super.init(attributeSet);
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.ConstraintLayout_Layout);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = obtainStyledAttributes.getIndex(i);
                if (index == 6) {
                    this.mApplyVisibilityOnAttach = true;
                } else if (index == 13) {
                    this.mApplyElevationOnAttach = true;
                }
            }
        }
    }

    @Override // android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mContainer = (ConstraintLayout) getParent();
        if (this.mApplyVisibilityOnAttach || this.mApplyElevationOnAttach) {
            int visibility = getVisibility();
            float elevation = getElevation();
            for (int i = 0; i < this.mCount; i++) {
                View viewById = this.mContainer.getViewById(this.mIds[i]);
                if (viewById != null) {
                    if (this.mApplyVisibilityOnAttach) {
                        viewById.setVisibility(visibility);
                    }
                    if (this.mApplyElevationOnAttach && elevation > 0.0f) {
                        viewById.setTranslationZ(viewById.getTranslationZ() + elevation);
                    }
                }
            }
        }
    }

    @Override // android.view.View
    public final void setElevation(float f) {
        super.setElevation(f);
        applyLayoutFeatures$1();
    }

    @Override // android.view.View
    public final void setVisibility(int i) {
        super.setVisibility(i);
        applyLayoutFeatures$1();
    }

    @Override // androidx.constraintlayout.widget.ConstraintHelper
    public final void updatePostLayout() {
        reCacheViews();
        this.mComputedCenterX = Float.NaN;
        this.mComputedCenterY = Float.NaN;
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) getLayoutParams();
        Objects.requireNonNull(layoutParams);
        ConstraintWidget constraintWidget = layoutParams.widget;
        constraintWidget.setWidth(0);
        constraintWidget.setHeight(0);
        calcCenters();
        layout(((int) this.mComputedMinX) - getPaddingLeft(), ((int) this.mComputedMinY) - getPaddingTop(), getPaddingRight() + ((int) this.mComputedMaxX), getPaddingBottom() + ((int) this.mComputedMaxY));
        if (!Float.isNaN(this.mGroupRotateAngle)) {
            transform();
        }
    }
}
