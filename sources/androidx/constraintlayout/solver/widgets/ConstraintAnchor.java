package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.SolverVariable;
import com.android.systemui.plugins.FalsingManager;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ConstraintAnchor {
    public final ConstraintWidget mOwner;
    public SolverVariable mSolverVariable;
    public ConstraintAnchor mTarget;
    public final Type mType;
    public int mMargin = 0;
    public int mGoneMargin = -1;

    /* loaded from: classes.dex */
    public enum Type {
        /* JADX INFO: Fake field, exist only in values array */
        NONE,
        LEFT,
        TOP,
        RIGHT,
        BOTTOM,
        BASELINE,
        CENTER,
        CENTER_X,
        CENTER_Y
    }

    public final boolean connect(ConstraintAnchor constraintAnchor, int i, int i2, boolean z) {
        if (constraintAnchor == null) {
            this.mTarget = null;
            this.mMargin = 0;
            this.mGoneMargin = -1;
            return true;
        } else if (!z && !isValidConnection(constraintAnchor)) {
            return false;
        } else {
            this.mTarget = constraintAnchor;
            if (i > 0) {
                this.mMargin = i;
            } else {
                this.mMargin = 0;
            }
            this.mGoneMargin = i2;
            return true;
        }
    }

    public final void reset() {
        this.mTarget = null;
        this.mMargin = 0;
        this.mGoneMargin = -1;
    }

    public final int getMargin() {
        ConstraintAnchor constraintAnchor;
        ConstraintWidget constraintWidget = this.mOwner;
        Objects.requireNonNull(constraintWidget);
        if (constraintWidget.mVisibility == 8) {
            return 0;
        }
        if (this.mGoneMargin > -1 && (constraintAnchor = this.mTarget) != null) {
            ConstraintWidget constraintWidget2 = constraintAnchor.mOwner;
            Objects.requireNonNull(constraintWidget2);
            if (constraintWidget2.mVisibility == 8) {
                return this.mGoneMargin;
            }
        }
        return this.mMargin;
    }

    public final ConstraintAnchor getOpposite() {
        switch (this.mType.ordinal()) {
            case 0:
            case 5:
            case FalsingManager.VERSION /* 6 */:
            case 7:
            case 8:
                return null;
            case 1:
                return this.mOwner.mRight;
            case 2:
                return this.mOwner.mBottom;
            case 3:
                return this.mOwner.mLeft;
            case 4:
                return this.mOwner.mTop;
            default:
                throw new AssertionError(this.mType.name());
        }
    }

    public final boolean isConnected() {
        if (this.mTarget != null) {
            return true;
        }
        return false;
    }

    public final boolean isValidConnection(ConstraintAnchor constraintAnchor) {
        boolean z;
        boolean z2;
        Type type = Type.CENTER_Y;
        Type type2 = Type.CENTER_X;
        Type type3 = Type.BASELINE;
        boolean z3 = false;
        if (constraintAnchor == null) {
            return false;
        }
        Type type4 = constraintAnchor.mType;
        Type type5 = this.mType;
        if (type4 == type5) {
            if (type5 == type3) {
                ConstraintWidget constraintWidget = constraintAnchor.mOwner;
                Objects.requireNonNull(constraintWidget);
                if (constraintWidget.hasBaseline) {
                    ConstraintWidget constraintWidget2 = this.mOwner;
                    Objects.requireNonNull(constraintWidget2);
                    if (!constraintWidget2.hasBaseline) {
                    }
                }
                return false;
            }
            return true;
        }
        switch (type5.ordinal()) {
            case 0:
            case 5:
            case 7:
            case 8:
                return false;
            case 1:
            case 3:
                if (type4 == Type.LEFT || type4 == Type.RIGHT) {
                    z = true;
                } else {
                    z = false;
                }
                if (!(constraintAnchor.mOwner instanceof Guideline)) {
                    return z;
                }
                if (z || type4 == type2) {
                    z3 = true;
                }
                return z3;
            case 2:
            case 4:
                if (type4 == Type.TOP || type4 == Type.BOTTOM) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (!(constraintAnchor.mOwner instanceof Guideline)) {
                    return z2;
                }
                if (z2 || type4 == type) {
                    z3 = true;
                }
                return z3;
            case FalsingManager.VERSION /* 6 */:
                if (type4 == type3 || type4 == type2 || type4 == type) {
                    return false;
                }
                return true;
            default:
                throw new AssertionError(this.mType.name());
        }
    }

    public final void resetSolverVariable() {
        SolverVariable solverVariable = this.mSolverVariable;
        if (solverVariable == null) {
            this.mSolverVariable = new SolverVariable(SolverVariable.Type.UNRESTRICTED);
        } else {
            solverVariable.reset();
        }
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        ConstraintWidget constraintWidget = this.mOwner;
        Objects.requireNonNull(constraintWidget);
        sb.append(constraintWidget.mDebugName);
        sb.append(":");
        sb.append(this.mType.toString());
        return sb.toString();
    }

    public ConstraintAnchor(ConstraintWidget constraintWidget, Type type) {
        this.mOwner = constraintWidget;
        this.mType = type;
    }

    public final boolean connect(ConstraintAnchor constraintAnchor, int i) {
        return connect(constraintAnchor, i, -1, false);
    }
}
