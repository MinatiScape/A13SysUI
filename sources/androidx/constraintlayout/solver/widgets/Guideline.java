package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.ArrayRow;
import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.SolverVariable;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import com.android.systemui.plugins.FalsingManager;
import java.util.HashMap;
import java.util.Objects;
/* loaded from: classes.dex */
public final class Guideline extends ConstraintWidget {
    public float mRelativePercent = -1.0f;
    public int mRelativeBegin = -1;
    public int mRelativeEnd = -1;
    public ConstraintAnchor mAnchor = this.mTop;
    public int mOrientation = 0;

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public final boolean allowedInBarrier() {
        return true;
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public final void addToSolver(LinearSystem linearSystem) {
        boolean z;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        ConstraintWidgetContainer constraintWidgetContainer = (ConstraintWidgetContainer) this.mParent;
        if (constraintWidgetContainer != null) {
            ConstraintAnchor anchor = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.LEFT);
            ConstraintAnchor anchor2 = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.RIGHT);
            ConstraintWidget constraintWidget = this.mParent;
            boolean z2 = true;
            if (constraintWidget == null || constraintWidget.mListDimensionBehaviors[0] != dimensionBehaviour) {
                z = false;
            } else {
                z = true;
            }
            if (this.mOrientation == 0) {
                anchor = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.TOP);
                anchor2 = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.BOTTOM);
                ConstraintWidget constraintWidget2 = this.mParent;
                if (constraintWidget2 == null || constraintWidget2.mListDimensionBehaviors[1] != dimensionBehaviour) {
                    z2 = false;
                }
                z = z2;
            }
            if (this.mRelativeBegin != -1) {
                SolverVariable createObjectVariable = linearSystem.createObjectVariable(this.mAnchor);
                linearSystem.addEquality(createObjectVariable, linearSystem.createObjectVariable(anchor), this.mRelativeBegin, 7);
                if (z) {
                    linearSystem.addGreaterThan(linearSystem.createObjectVariable(anchor2), createObjectVariable, 0, 5);
                }
            } else if (this.mRelativeEnd != -1) {
                SolverVariable createObjectVariable2 = linearSystem.createObjectVariable(this.mAnchor);
                SolverVariable createObjectVariable3 = linearSystem.createObjectVariable(anchor2);
                linearSystem.addEquality(createObjectVariable2, createObjectVariable3, -this.mRelativeEnd, 7);
                if (z) {
                    linearSystem.addGreaterThan(createObjectVariable2, linearSystem.createObjectVariable(anchor), 0, 5);
                    linearSystem.addGreaterThan(createObjectVariable3, createObjectVariable2, 0, 5);
                }
            } else if (this.mRelativePercent != -1.0f) {
                SolverVariable createObjectVariable4 = linearSystem.createObjectVariable(this.mAnchor);
                SolverVariable createObjectVariable5 = linearSystem.createObjectVariable(anchor2);
                float f = this.mRelativePercent;
                ArrayRow createRow = linearSystem.createRow();
                createRow.variables.put(createObjectVariable4, -1.0f);
                createRow.variables.put(createObjectVariable5, f);
                linearSystem.addConstraint(createRow);
            }
        }
    }

    public final void setOrientation(int i) {
        if (this.mOrientation != i) {
            this.mOrientation = i;
            this.mAnchors.clear();
            if (this.mOrientation == 1) {
                this.mAnchor = this.mLeft;
            } else {
                this.mAnchor = this.mTop;
            }
            this.mAnchors.add(this.mAnchor);
            int length = this.mListAnchors.length;
            for (int i2 = 0; i2 < length; i2++) {
                this.mListAnchors[i2] = this.mAnchor;
            }
        }
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public final void updateFromSolver(LinearSystem linearSystem) {
        if (this.mParent != null) {
            ConstraintAnchor constraintAnchor = this.mAnchor;
            Objects.requireNonNull(linearSystem);
            int objectVariableValue = LinearSystem.getObjectVariableValue(constraintAnchor);
            if (this.mOrientation == 1) {
                this.mX = objectVariableValue;
                this.mY = 0;
                setHeight(this.mParent.getHeight());
                setWidth(0);
                return;
            }
            this.mX = 0;
            this.mY = objectVariableValue;
            setWidth(this.mParent.getWidth());
            setHeight(0);
        }
    }

    public Guideline() {
        this.mAnchors.clear();
        this.mAnchors.add(this.mAnchor);
        int length = this.mListAnchors.length;
        for (int i = 0; i < length; i++) {
            this.mListAnchors[i] = this.mAnchor;
        }
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public final void copy(ConstraintWidget constraintWidget, HashMap<ConstraintWidget, ConstraintWidget> hashMap) {
        super.copy(constraintWidget, hashMap);
        Guideline guideline = (Guideline) constraintWidget;
        this.mRelativePercent = guideline.mRelativePercent;
        this.mRelativeBegin = guideline.mRelativeBegin;
        this.mRelativeEnd = guideline.mRelativeEnd;
        setOrientation(guideline.mOrientation);
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public final ConstraintAnchor getAnchor(ConstraintAnchor.Type type) {
        switch (type.ordinal()) {
            case 0:
            case 5:
            case FalsingManager.VERSION /* 6 */:
            case 7:
            case 8:
                return null;
            case 1:
            case 3:
                if (this.mOrientation == 1) {
                    return this.mAnchor;
                }
                break;
            case 2:
            case 4:
                if (this.mOrientation == 0) {
                    return this.mAnchor;
                }
                break;
        }
        throw new AssertionError(type.name());
    }
}
