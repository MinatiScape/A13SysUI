package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class BasicMeasure {
    public ConstraintWidgetContainer constraintWidgetContainer;
    public final ArrayList<ConstraintWidget> mVariableDimensionsWidgets = new ArrayList<>();
    public Measure mMeasure = new Measure();

    /* loaded from: classes.dex */
    public static class Measure {
        public ConstraintWidget.DimensionBehaviour horizontalBehavior;
        public int horizontalDimension;
        public int measuredBaseline;
        public boolean measuredHasBaseline;
        public int measuredHeight;
        public boolean measuredNeedsSolverPass;
        public int measuredWidth;
        public boolean useDeprecated;
        public ConstraintWidget.DimensionBehaviour verticalBehavior;
        public int verticalDimension;
    }

    /* loaded from: classes.dex */
    public interface Measurer {
    }

    public final boolean measure(Measurer measurer, ConstraintWidget constraintWidget, boolean z) {
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
        Measure measure = this.mMeasure;
        ConstraintWidget.DimensionBehaviour[] dimensionBehaviourArr = constraintWidget.mListDimensionBehaviors;
        measure.horizontalBehavior = dimensionBehaviourArr[0];
        boolean z6 = true;
        measure.verticalBehavior = dimensionBehaviourArr[1];
        measure.horizontalDimension = constraintWidget.getWidth();
        this.mMeasure.verticalDimension = constraintWidget.getHeight();
        Measure measure2 = this.mMeasure;
        measure2.measuredNeedsSolverPass = false;
        measure2.useDeprecated = z;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = measure2.horizontalBehavior;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour3 = ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (dimensionBehaviour2 == dimensionBehaviour3) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (measure2.verticalBehavior == dimensionBehaviour3) {
            z3 = true;
        } else {
            z3 = false;
        }
        if (!z2 || constraintWidget.mDimensionRatio <= 0.0f) {
            z4 = false;
        } else {
            z4 = true;
        }
        if (!z3 || constraintWidget.mDimensionRatio <= 0.0f) {
            z5 = false;
        } else {
            z5 = true;
        }
        if (z4 && constraintWidget.mResolvedMatchConstraintDefault[0] == 4) {
            measure2.horizontalBehavior = dimensionBehaviour;
        }
        if (z5 && constraintWidget.mResolvedMatchConstraintDefault[1] == 4) {
            measure2.verticalBehavior = dimensionBehaviour;
        }
        ((ConstraintLayout.Measurer) measurer).measure(constraintWidget, measure2);
        constraintWidget.setWidth(this.mMeasure.measuredWidth);
        constraintWidget.setHeight(this.mMeasure.measuredHeight);
        Measure measure3 = this.mMeasure;
        constraintWidget.hasBaseline = measure3.measuredHasBaseline;
        int i = measure3.measuredBaseline;
        constraintWidget.mBaselineDistance = i;
        if (i <= 0) {
            z6 = false;
        }
        constraintWidget.hasBaseline = z6;
        measure3.useDeprecated = false;
        return measure3.measuredNeedsSolverPass;
    }

    public BasicMeasure(ConstraintWidgetContainer constraintWidgetContainer) {
        this.constraintWidgetContainer = constraintWidgetContainer;
    }

    public final void solveLinearSystem(ConstraintWidgetContainer constraintWidgetContainer, int i, int i2) {
        Objects.requireNonNull(constraintWidgetContainer);
        int i3 = constraintWidgetContainer.mMinWidth;
        int i4 = constraintWidgetContainer.mMinHeight;
        constraintWidgetContainer.mMinWidth = 0;
        constraintWidgetContainer.mMinHeight = 0;
        constraintWidgetContainer.setWidth(i);
        constraintWidgetContainer.setHeight(i2);
        if (i3 < 0) {
            constraintWidgetContainer.mMinWidth = 0;
        } else {
            constraintWidgetContainer.mMinWidth = i3;
        }
        if (i4 < 0) {
            constraintWidgetContainer.mMinHeight = 0;
        } else {
            constraintWidgetContainer.mMinHeight = i4;
        }
        this.constraintWidgetContainer.layout();
    }
}
