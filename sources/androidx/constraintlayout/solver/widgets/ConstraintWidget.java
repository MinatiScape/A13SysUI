package androidx.constraintlayout.solver.widgets;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline1;
import androidx.constraintlayout.solver.Cache;
import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.analyzer.ChainRun;
import androidx.constraintlayout.solver.widgets.analyzer.DependencyNode;
import androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun;
import androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun;
import com.android.systemui.plugins.FalsingManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
/* loaded from: classes.dex */
public class ConstraintWidget {
    public ChainRun horizontalChainRun;
    public ArrayList<ConstraintAnchor> mAnchors;
    public ConstraintAnchor mBaseline;
    public ConstraintAnchor mBottom;
    public ConstraintAnchor mCenter;
    public Object mCompanionWidget;
    public ConstraintAnchor mLeft;
    public ConstraintAnchor[] mListAnchors;
    public DimensionBehaviour[] mListDimensionBehaviors;
    public int mMinHeight;
    public int mMinWidth;
    public ConstraintAnchor mRight;
    public ConstraintAnchor mTop;
    public ChainRun verticalChainRun;
    public boolean measured = false;
    public HorizontalWidgetRun horizontalRun = new HorizontalWidgetRun(this);
    public VerticalWidgetRun verticalRun = new VerticalWidgetRun(this);
    public boolean[] isTerminalWidget = {true, true};
    public int[] wrapMeasure = {0, 0};
    public int mHorizontalResolution = -1;
    public int mVerticalResolution = -1;
    public int mMatchConstraintDefaultWidth = 0;
    public int mMatchConstraintDefaultHeight = 0;
    public int[] mResolvedMatchConstraintDefault = new int[2];
    public int mMatchConstraintMinWidth = 0;
    public int mMatchConstraintMaxWidth = 0;
    public float mMatchConstraintPercentWidth = 1.0f;
    public int mMatchConstraintMinHeight = 0;
    public int mMatchConstraintMaxHeight = 0;
    public float mMatchConstraintPercentHeight = 1.0f;
    public int mResolvedDimensionRatioSide = -1;
    public float mResolvedDimensionRatio = 1.0f;
    public int[] mMaxDimension = {Integer.MAX_VALUE, Integer.MAX_VALUE};
    public float mCircleConstraintAngle = 0.0f;
    public boolean hasBaseline = false;
    public ConstraintAnchor mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
    public ConstraintAnchor mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
    public ConstraintWidget mParent = null;
    public int mWidth = 0;
    public int mHeight = 0;
    public float mDimensionRatio = 0.0f;
    public int mDimensionRatioSide = -1;
    public int mX = 0;
    public int mY = 0;
    public int mBaselineDistance = 0;
    public float mHorizontalBiasPercent = 0.5f;
    public float mVerticalBiasPercent = 0.5f;
    public int mVisibility = 0;
    public String mDebugName = null;
    public String mType = null;
    public int mHorizontalChainStyle = 0;
    public int mVerticalChainStyle = 0;
    public float[] mWeight = {-1.0f, -1.0f};
    public ConstraintWidget[] mListNextMatchConstraintsWidget = {null, null};
    public ConstraintWidget[] mNextChainWidget = {null, null};
    public ConstraintWidget mHorizontalNextWidget = null;
    public ConstraintWidget mVerticalNextWidget = null;

    /* loaded from: classes.dex */
    public enum DimensionBehaviour {
        FIXED,
        WRAP_CONTENT,
        MATCH_CONSTRAINT,
        MATCH_PARENT
    }

    public final void connect(ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, int i) {
        Objects.requireNonNull(constraintAnchor);
        if (constraintAnchor.mOwner == this) {
            ConstraintAnchor.Type type = constraintAnchor.mType;
            Objects.requireNonNull(constraintAnchor2);
            connect(type, constraintAnchor2.mOwner, constraintAnchor2.mType, i);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:278:0x04aa, code lost:
        if (r13.mVisibility == 8) goto L_0x04af;
     */
    /* JADX WARN: Removed duplicated region for block: B:191:0x02ea  */
    /* JADX WARN: Removed duplicated region for block: B:195:0x02f4  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x02fe  */
    /* JADX WARN: Removed duplicated region for block: B:204:0x0309  */
    /* JADX WARN: Removed duplicated region for block: B:205:0x030c  */
    /* JADX WARN: Removed duplicated region for block: B:208:0x031e  */
    /* JADX WARN: Removed duplicated region for block: B:231:0x03f2  */
    /* JADX WARN: Removed duplicated region for block: B:235:0x0408  */
    /* JADX WARN: Removed duplicated region for block: B:250:0x0461  */
    /* JADX WARN: Removed duplicated region for block: B:251:0x0463  */
    /* JADX WARN: Removed duplicated region for block: B:253:0x0466  */
    /* JADX WARN: Removed duplicated region for block: B:291:0x0520  */
    /* JADX WARN: Removed duplicated region for block: B:293:0x0526  */
    /* JADX WARN: Removed duplicated region for block: B:297:0x0585  */
    /* JADX WARN: Removed duplicated region for block: B:300:0x0591  */
    /* JADX WARN: Removed duplicated region for block: B:306:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void addToSolver(androidx.constraintlayout.solver.LinearSystem r50) {
        /*
            Method dump skipped, instructions count: 1609
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.ConstraintWidget.addToSolver(androidx.constraintlayout.solver.LinearSystem):void");
    }

    public boolean allowedInBarrier() {
        if (this.mVisibility != 8) {
            return true;
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:144:0x0245, code lost:
        if (r0 == false) goto L_0x022c;
     */
    /* JADX WARN: Removed duplicated region for block: B:170:0x0289  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x02b4  */
    /* JADX WARN: Removed duplicated region for block: B:174:0x02c3  */
    /* JADX WARN: Removed duplicated region for block: B:184:0x02e5  */
    /* JADX WARN: Removed duplicated region for block: B:186:0x02e9  */
    /* JADX WARN: Removed duplicated region for block: B:213:0x0330  */
    /* JADX WARN: Removed duplicated region for block: B:221:0x0344 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:232:0x0365 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:240:0x037d  */
    /* JADX WARN: Removed duplicated region for block: B:242:0x038a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0076  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0079  */
    /* JADX WARN: Removed duplicated region for block: B:265:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:268:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x007d  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x009d  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00ba  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0198  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void applyConstraints(androidx.constraintlayout.solver.LinearSystem r27, boolean r28, boolean r29, boolean r30, boolean r31, androidx.constraintlayout.solver.SolverVariable r32, androidx.constraintlayout.solver.SolverVariable r33, androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour r34, boolean r35, androidx.constraintlayout.solver.widgets.ConstraintAnchor r36, androidx.constraintlayout.solver.widgets.ConstraintAnchor r37, int r38, int r39, int r40, int r41, float r42, boolean r43, boolean r44, boolean r45, int r46, int r47, int r48, int r49, float r50, boolean r51) {
        /*
            Method dump skipped, instructions count: 966
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.ConstraintWidget.applyConstraints(androidx.constraintlayout.solver.LinearSystem, boolean, boolean, boolean, boolean, androidx.constraintlayout.solver.SolverVariable, androidx.constraintlayout.solver.SolverVariable, androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour, boolean, androidx.constraintlayout.solver.widgets.ConstraintAnchor, androidx.constraintlayout.solver.widgets.ConstraintAnchor, int, int, int, int, float, boolean, boolean, boolean, int, int, int, int, float, boolean):void");
    }

    public void copy(ConstraintWidget constraintWidget, HashMap<ConstraintWidget, ConstraintWidget> hashMap) {
        ConstraintWidget constraintWidget2;
        ConstraintWidget constraintWidget3;
        this.mHorizontalResolution = constraintWidget.mHorizontalResolution;
        this.mVerticalResolution = constraintWidget.mVerticalResolution;
        this.mMatchConstraintDefaultWidth = constraintWidget.mMatchConstraintDefaultWidth;
        this.mMatchConstraintDefaultHeight = constraintWidget.mMatchConstraintDefaultHeight;
        int[] iArr = this.mResolvedMatchConstraintDefault;
        int[] iArr2 = constraintWidget.mResolvedMatchConstraintDefault;
        iArr[0] = iArr2[0];
        iArr[1] = iArr2[1];
        this.mMatchConstraintMinWidth = constraintWidget.mMatchConstraintMinWidth;
        this.mMatchConstraintMaxWidth = constraintWidget.mMatchConstraintMaxWidth;
        this.mMatchConstraintMinHeight = constraintWidget.mMatchConstraintMinHeight;
        this.mMatchConstraintMaxHeight = constraintWidget.mMatchConstraintMaxHeight;
        this.mMatchConstraintPercentHeight = constraintWidget.mMatchConstraintPercentHeight;
        this.mResolvedDimensionRatioSide = constraintWidget.mResolvedDimensionRatioSide;
        this.mResolvedDimensionRatio = constraintWidget.mResolvedDimensionRatio;
        int[] iArr3 = constraintWidget.mMaxDimension;
        this.mMaxDimension = Arrays.copyOf(iArr3, iArr3.length);
        this.mCircleConstraintAngle = constraintWidget.mCircleConstraintAngle;
        this.hasBaseline = constraintWidget.hasBaseline;
        this.mLeft.reset();
        this.mTop.reset();
        this.mRight.reset();
        this.mBottom.reset();
        this.mBaseline.reset();
        this.mCenterX.reset();
        this.mCenterY.reset();
        this.mCenter.reset();
        this.mListDimensionBehaviors = (DimensionBehaviour[]) Arrays.copyOf(this.mListDimensionBehaviors, 2);
        ConstraintWidget constraintWidget4 = null;
        if (this.mParent == null) {
            constraintWidget2 = null;
        } else {
            constraintWidget2 = hashMap.get(constraintWidget.mParent);
        }
        this.mParent = constraintWidget2;
        this.mWidth = constraintWidget.mWidth;
        this.mHeight = constraintWidget.mHeight;
        this.mDimensionRatio = constraintWidget.mDimensionRatio;
        this.mDimensionRatioSide = constraintWidget.mDimensionRatioSide;
        this.mX = constraintWidget.mX;
        this.mY = constraintWidget.mY;
        this.mBaselineDistance = constraintWidget.mBaselineDistance;
        this.mMinWidth = constraintWidget.mMinWidth;
        this.mMinHeight = constraintWidget.mMinHeight;
        this.mHorizontalBiasPercent = constraintWidget.mHorizontalBiasPercent;
        this.mVerticalBiasPercent = constraintWidget.mVerticalBiasPercent;
        this.mCompanionWidget = constraintWidget.mCompanionWidget;
        this.mVisibility = constraintWidget.mVisibility;
        this.mDebugName = constraintWidget.mDebugName;
        this.mType = constraintWidget.mType;
        this.mHorizontalChainStyle = constraintWidget.mHorizontalChainStyle;
        this.mVerticalChainStyle = constraintWidget.mVerticalChainStyle;
        float[] fArr = this.mWeight;
        float[] fArr2 = constraintWidget.mWeight;
        fArr[0] = fArr2[0];
        fArr[1] = fArr2[1];
        ConstraintWidget[] constraintWidgetArr = this.mListNextMatchConstraintsWidget;
        ConstraintWidget[] constraintWidgetArr2 = constraintWidget.mListNextMatchConstraintsWidget;
        constraintWidgetArr[0] = constraintWidgetArr2[0];
        constraintWidgetArr[1] = constraintWidgetArr2[1];
        ConstraintWidget[] constraintWidgetArr3 = this.mNextChainWidget;
        ConstraintWidget[] constraintWidgetArr4 = constraintWidget.mNextChainWidget;
        constraintWidgetArr3[0] = constraintWidgetArr4[0];
        constraintWidgetArr3[1] = constraintWidgetArr4[1];
        ConstraintWidget constraintWidget5 = constraintWidget.mHorizontalNextWidget;
        if (constraintWidget5 == null) {
            constraintWidget3 = null;
        } else {
            constraintWidget3 = hashMap.get(constraintWidget5);
        }
        this.mHorizontalNextWidget = constraintWidget3;
        ConstraintWidget constraintWidget6 = constraintWidget.mVerticalNextWidget;
        if (constraintWidget6 != null) {
            constraintWidget4 = hashMap.get(constraintWidget6);
        }
        this.mVerticalNextWidget = constraintWidget4;
    }

    public final void createObjectVariables(LinearSystem linearSystem) {
        linearSystem.createObjectVariable(this.mLeft);
        linearSystem.createObjectVariable(this.mTop);
        linearSystem.createObjectVariable(this.mRight);
        linearSystem.createObjectVariable(this.mBottom);
        if (this.mBaselineDistance > 0) {
            linearSystem.createObjectVariable(this.mBaseline);
        }
    }

    public final DimensionBehaviour getDimensionBehaviour(int i) {
        if (i == 0) {
            return this.mListDimensionBehaviors[0];
        }
        if (i == 1) {
            return this.mListDimensionBehaviors[1];
        }
        return null;
    }

    public final int getHeight() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mHeight;
    }

    public final ConstraintWidget getNextChainMember(int i) {
        ConstraintAnchor constraintAnchor;
        ConstraintAnchor constraintAnchor2;
        if (i == 0) {
            ConstraintAnchor constraintAnchor3 = this.mRight;
            ConstraintAnchor constraintAnchor4 = constraintAnchor3.mTarget;
            if (constraintAnchor4 == null || constraintAnchor4.mTarget != constraintAnchor3) {
                return null;
            }
            return constraintAnchor4.mOwner;
        } else if (i == 1 && (constraintAnchor2 = (constraintAnchor = this.mBottom).mTarget) != null && constraintAnchor2.mTarget == constraintAnchor) {
            return constraintAnchor2.mOwner;
        } else {
            return null;
        }
    }

    public final ConstraintWidget getPreviousChainMember(int i) {
        ConstraintAnchor constraintAnchor;
        ConstraintAnchor constraintAnchor2;
        if (i == 0) {
            ConstraintAnchor constraintAnchor3 = this.mLeft;
            ConstraintAnchor constraintAnchor4 = constraintAnchor3.mTarget;
            if (constraintAnchor4 == null || constraintAnchor4.mTarget != constraintAnchor3) {
                return null;
            }
            return constraintAnchor4.mOwner;
        } else if (i == 1 && (constraintAnchor2 = (constraintAnchor = this.mTop).mTarget) != null && constraintAnchor2.mTarget == constraintAnchor) {
            return constraintAnchor2.mOwner;
        } else {
            return null;
        }
    }

    public final int getWidth() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mWidth;
    }

    public final int getX() {
        ConstraintWidget constraintWidget = this.mParent;
        if (constraintWidget == null || !(constraintWidget instanceof ConstraintWidgetContainer)) {
            return this.mX;
        }
        return ((ConstraintWidgetContainer) constraintWidget).mPaddingLeft + this.mX;
    }

    public final int getY() {
        ConstraintWidget constraintWidget = this.mParent;
        if (constraintWidget == null || !(constraintWidget instanceof ConstraintWidgetContainer)) {
            return this.mY;
        }
        return ((ConstraintWidgetContainer) constraintWidget).mPaddingTop + this.mY;
    }

    public final boolean isChainHead(int i) {
        int i2 = i * 2;
        ConstraintAnchor[] constraintAnchorArr = this.mListAnchors;
        if (!(constraintAnchorArr[i2].mTarget == null || constraintAnchorArr[i2].mTarget.mTarget == constraintAnchorArr[i2])) {
            int i3 = i2 + 1;
            if (constraintAnchorArr[i3].mTarget != null && constraintAnchorArr[i3].mTarget.mTarget == constraintAnchorArr[i3]) {
                return true;
            }
        }
        return false;
    }

    public final boolean isInHorizontalChain() {
        ConstraintAnchor constraintAnchor = this.mLeft;
        ConstraintAnchor constraintAnchor2 = constraintAnchor.mTarget;
        if (constraintAnchor2 != null && constraintAnchor2.mTarget == constraintAnchor) {
            return true;
        }
        ConstraintAnchor constraintAnchor3 = this.mRight;
        ConstraintAnchor constraintAnchor4 = constraintAnchor3.mTarget;
        if (constraintAnchor4 == null || constraintAnchor4.mTarget != constraintAnchor3) {
            return false;
        }
        return true;
    }

    public final boolean isInVerticalChain() {
        ConstraintAnchor constraintAnchor = this.mTop;
        ConstraintAnchor constraintAnchor2 = constraintAnchor.mTarget;
        if (constraintAnchor2 != null && constraintAnchor2.mTarget == constraintAnchor) {
            return true;
        }
        ConstraintAnchor constraintAnchor3 = this.mBottom;
        ConstraintAnchor constraintAnchor4 = constraintAnchor3.mTarget;
        if (constraintAnchor4 == null || constraintAnchor4.mTarget != constraintAnchor3) {
            return false;
        }
        return true;
    }

    public void reset() {
        this.mLeft.reset();
        this.mTop.reset();
        this.mRight.reset();
        this.mBottom.reset();
        this.mBaseline.reset();
        this.mCenterX.reset();
        this.mCenterY.reset();
        this.mCenter.reset();
        this.mParent = null;
        this.mCircleConstraintAngle = 0.0f;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mX = 0;
        this.mY = 0;
        this.mBaselineDistance = 0;
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        this.mHorizontalBiasPercent = 0.5f;
        this.mVerticalBiasPercent = 0.5f;
        DimensionBehaviour[] dimensionBehaviourArr = this.mListDimensionBehaviors;
        DimensionBehaviour dimensionBehaviour = DimensionBehaviour.FIXED;
        dimensionBehaviourArr[0] = dimensionBehaviour;
        dimensionBehaviourArr[1] = dimensionBehaviour;
        this.mCompanionWidget = null;
        this.mVisibility = 0;
        this.mType = null;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        float[] fArr = this.mWeight;
        fArr[0] = -1.0f;
        fArr[1] = -1.0f;
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        int[] iArr = this.mMaxDimension;
        iArr[0] = Integer.MAX_VALUE;
        iArr[1] = Integer.MAX_VALUE;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mMatchConstraintMaxWidth = Integer.MAX_VALUE;
        this.mMatchConstraintMaxHeight = Integer.MAX_VALUE;
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMinHeight = 0;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        boolean[] zArr = this.isTerminalWidget;
        zArr[0] = true;
        zArr[1] = true;
    }

    public final void resetAnchors() {
        ConstraintWidget constraintWidget = this.mParent;
        if (constraintWidget != null && (constraintWidget instanceof ConstraintWidgetContainer)) {
            Objects.requireNonNull((ConstraintWidgetContainer) constraintWidget);
        }
        int size = this.mAnchors.size();
        for (int i = 0; i < size; i++) {
            this.mAnchors.get(i).reset();
        }
    }

    public void resetSolverVariables(Cache cache) {
        this.mLeft.resetSolverVariable();
        this.mTop.resetSolverVariable();
        this.mRight.resetSolverVariable();
        this.mBottom.resetSolverVariable();
        this.mBaseline.resetSolverVariable();
        this.mCenter.resetSolverVariable();
        this.mCenterX.resetSolverVariable();
        this.mCenterY.resetSolverVariable();
    }

    public final void setHeight(int i) {
        this.mHeight = i;
        int i2 = this.mMinHeight;
        if (i < i2) {
            this.mHeight = i2;
        }
    }

    public final void setHorizontalDimensionBehaviour(DimensionBehaviour dimensionBehaviour) {
        this.mListDimensionBehaviors[0] = dimensionBehaviour;
    }

    public final void setVerticalDimensionBehaviour(DimensionBehaviour dimensionBehaviour) {
        this.mListDimensionBehaviors[1] = dimensionBehaviour;
    }

    public final void setWidth(int i) {
        this.mWidth = i;
        int i2 = this.mMinWidth;
        if (i < i2) {
            this.mWidth = i2;
        }
    }

    public final String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        String str2 = "";
        if (this.mType != null) {
            str = MotionController$$ExternalSyntheticOutline1.m(VendorAtomValue$$ExternalSyntheticOutline1.m("type: "), this.mType, " ");
        } else {
            str = str2;
        }
        sb.append(str);
        if (this.mDebugName != null) {
            str2 = MotionController$$ExternalSyntheticOutline1.m(VendorAtomValue$$ExternalSyntheticOutline1.m("id: "), this.mDebugName, " ");
        }
        sb.append(str2);
        sb.append("(");
        sb.append(this.mX);
        sb.append(", ");
        sb.append(this.mY);
        sb.append(") - (");
        sb.append(this.mWidth);
        sb.append(" x ");
        sb.append(this.mHeight);
        sb.append(")");
        return sb.toString();
    }

    public void updateFromRuns(boolean z, boolean z2) {
        int i;
        int i2;
        DimensionBehaviour dimensionBehaviour = DimensionBehaviour.FIXED;
        HorizontalWidgetRun horizontalWidgetRun = this.horizontalRun;
        Objects.requireNonNull(horizontalWidgetRun);
        boolean z3 = z & horizontalWidgetRun.resolved;
        VerticalWidgetRun verticalWidgetRun = this.verticalRun;
        Objects.requireNonNull(verticalWidgetRun);
        boolean z4 = z2 & verticalWidgetRun.resolved;
        HorizontalWidgetRun horizontalWidgetRun2 = this.horizontalRun;
        int i3 = horizontalWidgetRun2.start.value;
        VerticalWidgetRun verticalWidgetRun2 = this.verticalRun;
        int i4 = verticalWidgetRun2.start.value;
        int i5 = horizontalWidgetRun2.end.value;
        int i6 = verticalWidgetRun2.end.value;
        int i7 = i6 - i4;
        if (i5 - i3 < 0 || i7 < 0 || i3 == Integer.MIN_VALUE || i3 == Integer.MAX_VALUE || i4 == Integer.MIN_VALUE || i4 == Integer.MAX_VALUE || i5 == Integer.MIN_VALUE || i5 == Integer.MAX_VALUE || i6 == Integer.MIN_VALUE || i6 == Integer.MAX_VALUE) {
            i5 = 0;
            i3 = 0;
            i6 = 0;
            i4 = 0;
        }
        int i8 = i5 - i3;
        int i9 = i6 - i4;
        if (z3) {
            this.mX = i3;
        }
        if (z4) {
            this.mY = i4;
        }
        if (this.mVisibility == 8) {
            this.mWidth = 0;
            this.mHeight = 0;
            return;
        }
        if (z3) {
            if (this.mListDimensionBehaviors[0] == dimensionBehaviour && i8 < (i2 = this.mWidth)) {
                i8 = i2;
            }
            this.mWidth = i8;
            int i10 = this.mMinWidth;
            if (i8 < i10) {
                this.mWidth = i10;
            }
        }
        if (z4) {
            if (this.mListDimensionBehaviors[1] == dimensionBehaviour && i9 < (i = this.mHeight)) {
                i9 = i;
            }
            this.mHeight = i9;
            int i11 = this.mMinHeight;
            if (i9 < i11) {
                this.mHeight = i11;
            }
        }
    }

    public void updateFromSolver(LinearSystem linearSystem) {
        int i;
        int i2;
        ConstraintAnchor constraintAnchor = this.mLeft;
        Objects.requireNonNull(linearSystem);
        int objectVariableValue = LinearSystem.getObjectVariableValue(constraintAnchor);
        int objectVariableValue2 = LinearSystem.getObjectVariableValue(this.mTop);
        int objectVariableValue3 = LinearSystem.getObjectVariableValue(this.mRight);
        int objectVariableValue4 = LinearSystem.getObjectVariableValue(this.mBottom);
        HorizontalWidgetRun horizontalWidgetRun = this.horizontalRun;
        DependencyNode dependencyNode = horizontalWidgetRun.start;
        if (dependencyNode.resolved) {
            DependencyNode dependencyNode2 = horizontalWidgetRun.end;
            if (dependencyNode2.resolved) {
                objectVariableValue = dependencyNode.value;
                objectVariableValue3 = dependencyNode2.value;
            }
        }
        VerticalWidgetRun verticalWidgetRun = this.verticalRun;
        DependencyNode dependencyNode3 = verticalWidgetRun.start;
        if (dependencyNode3.resolved) {
            DependencyNode dependencyNode4 = verticalWidgetRun.end;
            if (dependencyNode4.resolved) {
                objectVariableValue2 = dependencyNode3.value;
                objectVariableValue4 = dependencyNode4.value;
            }
        }
        int i3 = objectVariableValue4 - objectVariableValue2;
        if (objectVariableValue3 - objectVariableValue < 0 || i3 < 0 || objectVariableValue == Integer.MIN_VALUE || objectVariableValue == Integer.MAX_VALUE || objectVariableValue2 == Integer.MIN_VALUE || objectVariableValue2 == Integer.MAX_VALUE || objectVariableValue3 == Integer.MIN_VALUE || objectVariableValue3 == Integer.MAX_VALUE || objectVariableValue4 == Integer.MIN_VALUE || objectVariableValue4 == Integer.MAX_VALUE) {
            objectVariableValue = 0;
            objectVariableValue2 = 0;
            objectVariableValue3 = 0;
            objectVariableValue4 = 0;
        }
        int i4 = objectVariableValue3 - objectVariableValue;
        int i5 = objectVariableValue4 - objectVariableValue2;
        this.mX = objectVariableValue;
        this.mY = objectVariableValue2;
        if (this.mVisibility == 8) {
            this.mWidth = 0;
            this.mHeight = 0;
            return;
        }
        DimensionBehaviour[] dimensionBehaviourArr = this.mListDimensionBehaviors;
        DimensionBehaviour dimensionBehaviour = dimensionBehaviourArr[0];
        DimensionBehaviour dimensionBehaviour2 = DimensionBehaviour.FIXED;
        if (dimensionBehaviour == dimensionBehaviour2 && i4 < (i2 = this.mWidth)) {
            i4 = i2;
        }
        if (dimensionBehaviourArr[1] == dimensionBehaviour2 && i5 < (i = this.mHeight)) {
            i5 = i;
        }
        this.mWidth = i4;
        this.mHeight = i5;
        int i6 = this.mMinHeight;
        if (i5 < i6) {
            this.mHeight = i6;
        }
        int i7 = this.mMinWidth;
        if (i4 < i7) {
            this.mWidth = i7;
        }
    }

    public ConstraintWidget() {
        ConstraintAnchor constraintAnchor = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
        this.mLeft = constraintAnchor;
        ConstraintAnchor constraintAnchor2 = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
        this.mTop = constraintAnchor2;
        ConstraintAnchor constraintAnchor3 = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
        this.mRight = constraintAnchor3;
        ConstraintAnchor constraintAnchor4 = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
        this.mBottom = constraintAnchor4;
        ConstraintAnchor constraintAnchor5 = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
        this.mBaseline = constraintAnchor5;
        ConstraintAnchor constraintAnchor6 = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
        this.mCenter = constraintAnchor6;
        this.mListAnchors = new ConstraintAnchor[]{constraintAnchor, constraintAnchor3, constraintAnchor2, constraintAnchor4, constraintAnchor5, constraintAnchor6};
        ArrayList<ConstraintAnchor> arrayList = new ArrayList<>();
        this.mAnchors = arrayList;
        DimensionBehaviour dimensionBehaviour = DimensionBehaviour.FIXED;
        this.mListDimensionBehaviors = new DimensionBehaviour[]{dimensionBehaviour, dimensionBehaviour};
        arrayList.add(this.mLeft);
        this.mAnchors.add(this.mTop);
        this.mAnchors.add(this.mRight);
        this.mAnchors.add(this.mBottom);
        this.mAnchors.add(this.mCenterX);
        this.mAnchors.add(this.mCenterY);
        this.mAnchors.add(this.mCenter);
        this.mAnchors.add(this.mBaseline);
    }

    public ConstraintAnchor getAnchor(ConstraintAnchor.Type type) {
        switch (type.ordinal()) {
            case 0:
                return null;
            case 1:
                return this.mLeft;
            case 2:
                return this.mTop;
            case 3:
                return this.mRight;
            case 4:
                return this.mBottom;
            case 5:
                return this.mBaseline;
            case FalsingManager.VERSION /* 6 */:
                return this.mCenter;
            case 7:
                return this.mCenterX;
            case 8:
                return this.mCenterY;
            default:
                throw new AssertionError(type.name());
        }
    }

    public final void immediateConnect(ConstraintAnchor.Type type, ConstraintWidget constraintWidget, ConstraintAnchor.Type type2, int i, int i2) {
        getAnchor(type).connect(constraintWidget.getAnchor(type2), i, i2, true);
    }

    public final void connect(ConstraintAnchor.Type type, ConstraintWidget constraintWidget, ConstraintAnchor.Type type2, int i) {
        boolean z;
        ConstraintAnchor.Type type3 = ConstraintAnchor.Type.CENTER_Y;
        ConstraintAnchor.Type type4 = ConstraintAnchor.Type.CENTER_X;
        ConstraintAnchor.Type type5 = ConstraintAnchor.Type.LEFT;
        ConstraintAnchor.Type type6 = ConstraintAnchor.Type.TOP;
        ConstraintAnchor.Type type7 = ConstraintAnchor.Type.RIGHT;
        ConstraintAnchor.Type type8 = ConstraintAnchor.Type.BOTTOM;
        ConstraintAnchor.Type type9 = ConstraintAnchor.Type.CENTER;
        if (type == type9) {
            if (type2 == type9) {
                ConstraintAnchor anchor = getAnchor(type5);
                ConstraintAnchor anchor2 = getAnchor(type7);
                ConstraintAnchor anchor3 = getAnchor(type6);
                ConstraintAnchor anchor4 = getAnchor(type8);
                boolean z2 = true;
                if ((anchor == null || !anchor.isConnected()) && (anchor2 == null || !anchor2.isConnected())) {
                    connect(type5, constraintWidget, type5, 0);
                    connect(type7, constraintWidget, type7, 0);
                    z = true;
                } else {
                    z = false;
                }
                if ((anchor3 == null || !anchor3.isConnected()) && (anchor4 == null || !anchor4.isConnected())) {
                    connect(type6, constraintWidget, type6, 0);
                    connect(type8, constraintWidget, type8, 0);
                } else {
                    z2 = false;
                }
                if (z && z2) {
                    getAnchor(type9).connect(constraintWidget.getAnchor(type9), 0);
                } else if (z) {
                    getAnchor(type4).connect(constraintWidget.getAnchor(type4), 0);
                } else if (z2) {
                    getAnchor(type3).connect(constraintWidget.getAnchor(type3), 0);
                }
            } else if (type2 == type5 || type2 == type7) {
                connect(type5, constraintWidget, type2, 0);
                connect(type7, constraintWidget, type2, 0);
                getAnchor(type9).connect(constraintWidget.getAnchor(type2), 0);
            } else if (type2 == type6 || type2 == type8) {
                connect(type6, constraintWidget, type2, 0);
                connect(type8, constraintWidget, type2, 0);
                getAnchor(type9).connect(constraintWidget.getAnchor(type2), 0);
            }
        } else if (type == type4 && (type2 == type5 || type2 == type7)) {
            ConstraintAnchor anchor5 = getAnchor(type5);
            ConstraintAnchor anchor6 = constraintWidget.getAnchor(type2);
            ConstraintAnchor anchor7 = getAnchor(type7);
            anchor5.connect(anchor6, 0);
            anchor7.connect(anchor6, 0);
            getAnchor(type4).connect(anchor6, 0);
        } else if (type == type3 && (type2 == type6 || type2 == type8)) {
            ConstraintAnchor anchor8 = constraintWidget.getAnchor(type2);
            getAnchor(type6).connect(anchor8, 0);
            getAnchor(type8).connect(anchor8, 0);
            getAnchor(type3).connect(anchor8, 0);
        } else if (type == type4 && type2 == type4) {
            getAnchor(type5).connect(constraintWidget.getAnchor(type5), 0);
            getAnchor(type7).connect(constraintWidget.getAnchor(type7), 0);
            getAnchor(type4).connect(constraintWidget.getAnchor(type2), 0);
        } else if (type == type3 && type2 == type3) {
            getAnchor(type6).connect(constraintWidget.getAnchor(type6), 0);
            getAnchor(type8).connect(constraintWidget.getAnchor(type8), 0);
            getAnchor(type3).connect(constraintWidget.getAnchor(type2), 0);
        } else {
            ConstraintAnchor anchor9 = getAnchor(type);
            ConstraintAnchor anchor10 = constraintWidget.getAnchor(type2);
            if (anchor9.isValidConnection(anchor10)) {
                ConstraintAnchor.Type type10 = ConstraintAnchor.Type.BASELINE;
                if (type == type10) {
                    ConstraintAnchor anchor11 = getAnchor(type6);
                    ConstraintAnchor anchor12 = getAnchor(type8);
                    if (anchor11 != null) {
                        anchor11.reset();
                    }
                    if (anchor12 != null) {
                        anchor12.reset();
                    }
                    i = 0;
                } else if (type == type6 || type == type8) {
                    ConstraintAnchor anchor13 = getAnchor(type10);
                    if (anchor13 != null) {
                        anchor13.reset();
                    }
                    ConstraintAnchor anchor14 = getAnchor(type9);
                    Objects.requireNonNull(anchor14);
                    if (anchor14.mTarget != anchor10) {
                        anchor14.reset();
                    }
                    ConstraintAnchor opposite = getAnchor(type).getOpposite();
                    ConstraintAnchor anchor15 = getAnchor(type3);
                    if (anchor15.isConnected()) {
                        opposite.reset();
                        anchor15.reset();
                    }
                } else if (type == type5 || type == type7) {
                    ConstraintAnchor anchor16 = getAnchor(type9);
                    Objects.requireNonNull(anchor16);
                    if (anchor16.mTarget != anchor10) {
                        anchor16.reset();
                    }
                    ConstraintAnchor opposite2 = getAnchor(type).getOpposite();
                    ConstraintAnchor anchor17 = getAnchor(type4);
                    if (anchor17.isConnected()) {
                        opposite2.reset();
                        anchor17.reset();
                    }
                }
                anchor9.connect(anchor10, i);
            }
        }
    }
}
