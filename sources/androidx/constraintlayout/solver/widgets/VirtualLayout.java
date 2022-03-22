package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;
import androidx.constraintlayout.widget.ConstraintLayout;
/* loaded from: classes.dex */
public class VirtualLayout extends HelperWidget {
    public int mPaddingTop = 0;
    public int mPaddingBottom = 0;
    public int mPaddingStart = 0;
    public int mPaddingEnd = 0;
    public int mResolvedPaddingLeft = 0;
    public int mResolvedPaddingRight = 0;
    public boolean mNeedsCallFromSolver = false;
    public int mMeasuredWidth = 0;
    public int mMeasuredHeight = 0;
    public BasicMeasure.Measure mMeasure = new BasicMeasure.Measure();
    public BasicMeasure.Measurer mMeasurer = null;

    public void measure(int i, int i2, int i3, int i4) {
    }

    @Override // androidx.constraintlayout.solver.widgets.HelperWidget, androidx.constraintlayout.solver.widgets.Helper
    public final void updateConstraints() {
        for (int i = 0; i < this.mWidgetsCount; i++) {
            ConstraintWidget constraintWidget = this.mWidgets[i];
        }
    }

    public final void measure(ConstraintWidget constraintWidget, ConstraintWidget.DimensionBehaviour dimensionBehaviour, int i, ConstraintWidget.DimensionBehaviour dimensionBehaviour2, int i2) {
        BasicMeasure.Measurer measurer;
        boolean z;
        ConstraintWidget constraintWidget2;
        while (true) {
            measurer = this.mMeasurer;
            if (measurer != null || (constraintWidget2 = this.mParent) == null) {
                break;
            }
            this.mMeasurer = ((ConstraintWidgetContainer) constraintWidget2).mMeasurer;
        }
        BasicMeasure.Measure measure = this.mMeasure;
        measure.horizontalBehavior = dimensionBehaviour;
        measure.verticalBehavior = dimensionBehaviour2;
        measure.horizontalDimension = i;
        measure.verticalDimension = i2;
        ((ConstraintLayout.Measurer) measurer).measure(constraintWidget, measure);
        constraintWidget.setWidth(this.mMeasure.measuredWidth);
        constraintWidget.setHeight(this.mMeasure.measuredHeight);
        BasicMeasure.Measure measure2 = this.mMeasure;
        constraintWidget.hasBaseline = measure2.measuredHasBaseline;
        int i3 = measure2.measuredBaseline;
        constraintWidget.mBaselineDistance = i3;
        if (i3 > 0) {
            z = true;
        } else {
            z = false;
        }
        constraintWidget.hasBaseline = z;
    }
}
