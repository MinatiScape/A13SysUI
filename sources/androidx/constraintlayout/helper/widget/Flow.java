package androidx.constraintlayout.helper.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.HelperWidget;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;
import androidx.constraintlayout.widget.R$styleable;
import androidx.constraintlayout.widget.VirtualLayout;
import java.util.Objects;
/* loaded from: classes.dex */
public class Flow extends VirtualLayout {
    public androidx.constraintlayout.solver.widgets.Flow mFlow;

    public Flow(Context context) {
        super(context);
    }

    @Override // androidx.constraintlayout.widget.ConstraintHelper, android.view.View
    @SuppressLint({"WrongCall"})
    public final void onMeasure(int i, int i2) {
        onMeasure(this.mFlow, i, i2);
    }

    public Flow(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // androidx.constraintlayout.widget.VirtualLayout
    public final void onMeasure(androidx.constraintlayout.solver.widgets.VirtualLayout virtualLayout, int i, int i2) {
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        int size2 = View.MeasureSpec.getSize(i2);
        if (virtualLayout != null) {
            virtualLayout.measure(mode, size, mode2, size2);
            setMeasuredDimension(virtualLayout.mMeasuredWidth, virtualLayout.mMeasuredHeight);
            return;
        }
        setMeasuredDimension(0, 0);
    }

    @Override // androidx.constraintlayout.widget.ConstraintHelper
    public final void resolveRtl(ConstraintWidget constraintWidget, boolean z) {
        androidx.constraintlayout.solver.widgets.Flow flow = this.mFlow;
        Objects.requireNonNull(flow);
        int i = flow.mPaddingStart;
        if (i <= 0 && flow.mPaddingEnd <= 0) {
            return;
        }
        if (z) {
            flow.mResolvedPaddingLeft = flow.mPaddingEnd;
            flow.mResolvedPaddingRight = i;
            return;
        }
        flow.mResolvedPaddingLeft = i;
        flow.mResolvedPaddingRight = flow.mPaddingEnd;
    }

    public Flow(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // androidx.constraintlayout.widget.VirtualLayout, androidx.constraintlayout.widget.ConstraintHelper
    public final void init(AttributeSet attributeSet) {
        super.init(attributeSet);
        this.mFlow = new androidx.constraintlayout.solver.widgets.Flow();
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.ConstraintLayout_Layout);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = obtainStyledAttributes.getIndex(i);
                if (index == 0) {
                    androidx.constraintlayout.solver.widgets.Flow flow = this.mFlow;
                    int i2 = obtainStyledAttributes.getInt(index, 0);
                    Objects.requireNonNull(flow);
                    flow.mOrientation = i2;
                } else if (index == 1) {
                    androidx.constraintlayout.solver.widgets.Flow flow2 = this.mFlow;
                    int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(index, 0);
                    Objects.requireNonNull(flow2);
                    flow2.mPaddingTop = dimensionPixelSize;
                    flow2.mPaddingBottom = dimensionPixelSize;
                    flow2.mPaddingStart = dimensionPixelSize;
                    flow2.mPaddingEnd = dimensionPixelSize;
                } else if (index == 11) {
                    androidx.constraintlayout.solver.widgets.Flow flow3 = this.mFlow;
                    int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(index, 0);
                    Objects.requireNonNull(flow3);
                    flow3.mPaddingStart = dimensionPixelSize2;
                    flow3.mResolvedPaddingLeft = dimensionPixelSize2;
                    flow3.mResolvedPaddingRight = dimensionPixelSize2;
                } else if (index == 12) {
                    androidx.constraintlayout.solver.widgets.Flow flow4 = this.mFlow;
                    int dimensionPixelSize3 = obtainStyledAttributes.getDimensionPixelSize(index, 0);
                    Objects.requireNonNull(flow4);
                    flow4.mPaddingEnd = dimensionPixelSize3;
                } else if (index == 2) {
                    androidx.constraintlayout.solver.widgets.Flow flow5 = this.mFlow;
                    int dimensionPixelSize4 = obtainStyledAttributes.getDimensionPixelSize(index, 0);
                    Objects.requireNonNull(flow5);
                    flow5.mResolvedPaddingLeft = dimensionPixelSize4;
                } else if (index == 3) {
                    androidx.constraintlayout.solver.widgets.Flow flow6 = this.mFlow;
                    int dimensionPixelSize5 = obtainStyledAttributes.getDimensionPixelSize(index, 0);
                    Objects.requireNonNull(flow6);
                    flow6.mPaddingTop = dimensionPixelSize5;
                } else if (index == 4) {
                    androidx.constraintlayout.solver.widgets.Flow flow7 = this.mFlow;
                    int dimensionPixelSize6 = obtainStyledAttributes.getDimensionPixelSize(index, 0);
                    Objects.requireNonNull(flow7);
                    flow7.mResolvedPaddingRight = dimensionPixelSize6;
                } else if (index == 5) {
                    androidx.constraintlayout.solver.widgets.Flow flow8 = this.mFlow;
                    int dimensionPixelSize7 = obtainStyledAttributes.getDimensionPixelSize(index, 0);
                    Objects.requireNonNull(flow8);
                    flow8.mPaddingBottom = dimensionPixelSize7;
                } else if (index == 37) {
                    androidx.constraintlayout.solver.widgets.Flow flow9 = this.mFlow;
                    int i3 = obtainStyledAttributes.getInt(index, 0);
                    Objects.requireNonNull(flow9);
                    flow9.mWrapMode = i3;
                } else if (index == 27) {
                    androidx.constraintlayout.solver.widgets.Flow flow10 = this.mFlow;
                    int i4 = obtainStyledAttributes.getInt(index, 0);
                    Objects.requireNonNull(flow10);
                    flow10.mHorizontalStyle = i4;
                } else if (index == 36) {
                    androidx.constraintlayout.solver.widgets.Flow flow11 = this.mFlow;
                    int i5 = obtainStyledAttributes.getInt(index, 0);
                    Objects.requireNonNull(flow11);
                    flow11.mVerticalStyle = i5;
                } else if (index == 21) {
                    androidx.constraintlayout.solver.widgets.Flow flow12 = this.mFlow;
                    int i6 = obtainStyledAttributes.getInt(index, 0);
                    Objects.requireNonNull(flow12);
                    flow12.mFirstHorizontalStyle = i6;
                } else if (index == 29) {
                    androidx.constraintlayout.solver.widgets.Flow flow13 = this.mFlow;
                    int i7 = obtainStyledAttributes.getInt(index, 0);
                    Objects.requireNonNull(flow13);
                    flow13.mLastHorizontalStyle = i7;
                } else if (index == 23) {
                    androidx.constraintlayout.solver.widgets.Flow flow14 = this.mFlow;
                    int i8 = obtainStyledAttributes.getInt(index, 0);
                    Objects.requireNonNull(flow14);
                    flow14.mFirstVerticalStyle = i8;
                } else if (index == 31) {
                    androidx.constraintlayout.solver.widgets.Flow flow15 = this.mFlow;
                    int i9 = obtainStyledAttributes.getInt(index, 0);
                    Objects.requireNonNull(flow15);
                    flow15.mLastVerticalStyle = i9;
                } else if (index == 25) {
                    androidx.constraintlayout.solver.widgets.Flow flow16 = this.mFlow;
                    float f = obtainStyledAttributes.getFloat(index, 0.5f);
                    Objects.requireNonNull(flow16);
                    flow16.mHorizontalBias = f;
                } else if (index == 20) {
                    androidx.constraintlayout.solver.widgets.Flow flow17 = this.mFlow;
                    float f2 = obtainStyledAttributes.getFloat(index, 0.5f);
                    Objects.requireNonNull(flow17);
                    flow17.mFirstHorizontalBias = f2;
                } else if (index == 28) {
                    androidx.constraintlayout.solver.widgets.Flow flow18 = this.mFlow;
                    float f3 = obtainStyledAttributes.getFloat(index, 0.5f);
                    Objects.requireNonNull(flow18);
                    flow18.mLastHorizontalBias = f3;
                } else if (index == 22) {
                    androidx.constraintlayout.solver.widgets.Flow flow19 = this.mFlow;
                    float f4 = obtainStyledAttributes.getFloat(index, 0.5f);
                    Objects.requireNonNull(flow19);
                    flow19.mFirstVerticalBias = f4;
                } else if (index == 30) {
                    androidx.constraintlayout.solver.widgets.Flow flow20 = this.mFlow;
                    float f5 = obtainStyledAttributes.getFloat(index, 0.5f);
                    Objects.requireNonNull(flow20);
                    flow20.mLastVerticalBias = f5;
                } else if (index == 34) {
                    androidx.constraintlayout.solver.widgets.Flow flow21 = this.mFlow;
                    float f6 = obtainStyledAttributes.getFloat(index, 0.5f);
                    Objects.requireNonNull(flow21);
                    flow21.mVerticalBias = f6;
                } else if (index == 24) {
                    androidx.constraintlayout.solver.widgets.Flow flow22 = this.mFlow;
                    int i10 = obtainStyledAttributes.getInt(index, 2);
                    Objects.requireNonNull(flow22);
                    flow22.mHorizontalAlign = i10;
                } else if (index == 33) {
                    androidx.constraintlayout.solver.widgets.Flow flow23 = this.mFlow;
                    int i11 = obtainStyledAttributes.getInt(index, 2);
                    Objects.requireNonNull(flow23);
                    flow23.mVerticalAlign = i11;
                } else if (index == 26) {
                    androidx.constraintlayout.solver.widgets.Flow flow24 = this.mFlow;
                    int dimensionPixelSize8 = obtainStyledAttributes.getDimensionPixelSize(index, 0);
                    Objects.requireNonNull(flow24);
                    flow24.mHorizontalGap = dimensionPixelSize8;
                } else if (index == 35) {
                    androidx.constraintlayout.solver.widgets.Flow flow25 = this.mFlow;
                    int dimensionPixelSize9 = obtainStyledAttributes.getDimensionPixelSize(index, 0);
                    Objects.requireNonNull(flow25);
                    flow25.mVerticalGap = dimensionPixelSize9;
                } else if (index == 32) {
                    androidx.constraintlayout.solver.widgets.Flow flow26 = this.mFlow;
                    int i12 = obtainStyledAttributes.getInt(index, -1);
                    Objects.requireNonNull(flow26);
                    flow26.mMaxElementsWrap = i12;
                }
            }
        }
        this.mHelperWidget = this.mFlow;
        validateParams();
    }

    @Override // androidx.constraintlayout.widget.ConstraintHelper
    public final void loadParameters(ConstraintSet.Constraint constraint, HelperWidget helperWidget, Constraints.LayoutParams layoutParams, SparseArray sparseArray) {
        super.loadParameters(constraint, helperWidget, layoutParams, sparseArray);
        if (helperWidget instanceof androidx.constraintlayout.solver.widgets.Flow) {
            androidx.constraintlayout.solver.widgets.Flow flow = (androidx.constraintlayout.solver.widgets.Flow) helperWidget;
            int i = layoutParams.orientation;
            if (i != -1) {
                flow.mOrientation = i;
            }
        }
    }
}
