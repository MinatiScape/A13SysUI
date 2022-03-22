package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;
import androidx.constraintlayout.solver.widgets.analyzer.ChainRun;
import androidx.constraintlayout.solver.widgets.analyzer.DependencyGraph;
import androidx.constraintlayout.solver.widgets.analyzer.WidgetRun;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ConstraintWidgetContainer extends WidgetContainer {
    public int mPaddingLeft;
    public int mPaddingTop;
    public BasicMeasure mBasicMeasureSolver = new BasicMeasure(this);
    public DependencyGraph mDependencyGraph = new DependencyGraph(this);
    public BasicMeasure.Measurer mMeasurer = null;
    public boolean mIsRtl = false;
    public LinearSystem mSystem = new LinearSystem();
    public int mHorizontalChainsSize = 0;
    public int mVerticalChainsSize = 0;
    public ChainHead[] mVerticalChainsArray = new ChainHead[4];
    public ChainHead[] mHorizontalChainsArray = new ChainHead[4];
    public int mOptimizationLevel = 7;
    public boolean mWidthMeasuredTooSmall = false;
    public boolean mHeightMeasuredTooSmall = false;

    public final void addChain(ConstraintWidget constraintWidget, int i) {
        if (i == 0) {
            int i2 = this.mHorizontalChainsSize + 1;
            ChainHead[] chainHeadArr = this.mHorizontalChainsArray;
            if (i2 >= chainHeadArr.length) {
                this.mHorizontalChainsArray = (ChainHead[]) Arrays.copyOf(chainHeadArr, chainHeadArr.length * 2);
            }
            ChainHead[] chainHeadArr2 = this.mHorizontalChainsArray;
            int i3 = this.mHorizontalChainsSize;
            chainHeadArr2[i3] = new ChainHead(constraintWidget, 0, this.mIsRtl);
            this.mHorizontalChainsSize = i3 + 1;
        } else if (i == 1) {
            int i4 = this.mVerticalChainsSize + 1;
            ChainHead[] chainHeadArr3 = this.mVerticalChainsArray;
            if (i4 >= chainHeadArr3.length) {
                this.mVerticalChainsArray = (ChainHead[]) Arrays.copyOf(chainHeadArr3, chainHeadArr3.length * 2);
            }
            ChainHead[] chainHeadArr4 = this.mVerticalChainsArray;
            int i5 = this.mVerticalChainsSize;
            chainHeadArr4[i5] = new ChainHead(constraintWidget, 1, this.mIsRtl);
            this.mVerticalChainsSize = i5 + 1;
        }
    }

    public final void addChildrenToSolver(LinearSystem linearSystem) {
        ConstraintWidget.DimensionBehaviour dimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        addToSolver(linearSystem);
        int size = this.mChildren.size();
        for (int i = 0; i < size; i++) {
            ConstraintWidget constraintWidget = this.mChildren.get(i);
            if ((constraintWidget instanceof VirtualLayout) || (constraintWidget instanceof Guideline)) {
                constraintWidget.addToSolver(linearSystem);
            }
        }
        for (int i2 = 0; i2 < size; i2++) {
            ConstraintWidget constraintWidget2 = this.mChildren.get(i2);
            if (constraintWidget2 instanceof ConstraintWidgetContainer) {
                ConstraintWidget.DimensionBehaviour[] dimensionBehaviourArr = constraintWidget2.mListDimensionBehaviors;
                ConstraintWidget.DimensionBehaviour dimensionBehaviour3 = dimensionBehaviourArr[0];
                ConstraintWidget.DimensionBehaviour dimensionBehaviour4 = dimensionBehaviourArr[1];
                if (dimensionBehaviour3 == dimensionBehaviour2) {
                    constraintWidget2.setHorizontalDimensionBehaviour(dimensionBehaviour);
                }
                if (dimensionBehaviour4 == dimensionBehaviour2) {
                    constraintWidget2.setVerticalDimensionBehaviour(dimensionBehaviour);
                }
                constraintWidget2.addToSolver(linearSystem);
                if (dimensionBehaviour3 == dimensionBehaviour2) {
                    constraintWidget2.setHorizontalDimensionBehaviour(dimensionBehaviour3);
                }
                if (dimensionBehaviour4 == dimensionBehaviour2) {
                    constraintWidget2.setVerticalDimensionBehaviour(dimensionBehaviour4);
                }
            } else {
                ConstraintWidget.DimensionBehaviour dimensionBehaviour5 = ConstraintWidget.DimensionBehaviour.MATCH_PARENT;
                constraintWidget2.mHorizontalResolution = -1;
                constraintWidget2.mVerticalResolution = -1;
                if (this.mListDimensionBehaviors[0] != dimensionBehaviour2 && constraintWidget2.mListDimensionBehaviors[0] == dimensionBehaviour5) {
                    int i3 = constraintWidget2.mLeft.mMargin;
                    int width = getWidth() - constraintWidget2.mRight.mMargin;
                    ConstraintAnchor constraintAnchor = constraintWidget2.mLeft;
                    constraintAnchor.mSolverVariable = linearSystem.createObjectVariable(constraintAnchor);
                    ConstraintAnchor constraintAnchor2 = constraintWidget2.mRight;
                    constraintAnchor2.mSolverVariable = linearSystem.createObjectVariable(constraintAnchor2);
                    linearSystem.addEquality(constraintWidget2.mLeft.mSolverVariable, i3);
                    linearSystem.addEquality(constraintWidget2.mRight.mSolverVariable, width);
                    constraintWidget2.mHorizontalResolution = 2;
                    constraintWidget2.mX = i3;
                    int i4 = width - i3;
                    constraintWidget2.mWidth = i4;
                    int i5 = constraintWidget2.mMinWidth;
                    if (i4 < i5) {
                        constraintWidget2.mWidth = i5;
                    }
                }
                if (this.mListDimensionBehaviors[1] != dimensionBehaviour2 && constraintWidget2.mListDimensionBehaviors[1] == dimensionBehaviour5) {
                    int i6 = constraintWidget2.mTop.mMargin;
                    int height = getHeight() - constraintWidget2.mBottom.mMargin;
                    ConstraintAnchor constraintAnchor3 = constraintWidget2.mTop;
                    constraintAnchor3.mSolverVariable = linearSystem.createObjectVariable(constraintAnchor3);
                    ConstraintAnchor constraintAnchor4 = constraintWidget2.mBottom;
                    constraintAnchor4.mSolverVariable = linearSystem.createObjectVariable(constraintAnchor4);
                    linearSystem.addEquality(constraintWidget2.mTop.mSolverVariable, i6);
                    linearSystem.addEquality(constraintWidget2.mBottom.mSolverVariable, height);
                    if (constraintWidget2.mBaselineDistance > 0 || constraintWidget2.mVisibility == 8) {
                        ConstraintAnchor constraintAnchor5 = constraintWidget2.mBaseline;
                        constraintAnchor5.mSolverVariable = linearSystem.createObjectVariable(constraintAnchor5);
                        linearSystem.addEquality(constraintWidget2.mBaseline.mSolverVariable, constraintWidget2.mBaselineDistance + i6);
                    }
                    constraintWidget2.mVerticalResolution = 2;
                    constraintWidget2.mY = i6;
                    int i7 = height - i6;
                    constraintWidget2.mHeight = i7;
                    int i8 = constraintWidget2.mMinHeight;
                    if (i7 < i8) {
                        constraintWidget2.mHeight = i8;
                    }
                }
                if (!(constraintWidget2 instanceof VirtualLayout) && !(constraintWidget2 instanceof Guideline)) {
                    constraintWidget2.addToSolver(linearSystem);
                }
            }
        }
        if (this.mHorizontalChainsSize > 0) {
            Chain.applyChainConstraints(this, linearSystem, 0);
        }
        if (this.mVerticalChainsSize > 0) {
            Chain.applyChainConstraints(this, linearSystem, 1);
        }
    }

    public final boolean directMeasureWithOrientation(boolean z, int i) {
        boolean z2;
        DependencyGraph dependencyGraph = this.mDependencyGraph;
        Objects.requireNonNull(dependencyGraph);
        ConstraintWidget.DimensionBehaviour dimensionBehaviour = ConstraintWidget.DimensionBehaviour.MATCH_PARENT;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour3 = ConstraintWidget.DimensionBehaviour.FIXED;
        boolean z3 = true;
        boolean z4 = z & true;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour4 = dependencyGraph.container.getDimensionBehaviour(0);
        ConstraintWidget.DimensionBehaviour dimensionBehaviour5 = dependencyGraph.container.getDimensionBehaviour(1);
        int x = dependencyGraph.container.getX();
        int y = dependencyGraph.container.getY();
        if (z4 && (dimensionBehaviour4 == dimensionBehaviour2 || dimensionBehaviour5 == dimensionBehaviour2)) {
            Iterator<WidgetRun> it = dependencyGraph.mRuns.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                WidgetRun next = it.next();
                if (next.orientation == i && !next.supportsWrapComputation()) {
                    z4 = false;
                    break;
                }
            }
            if (i == 0) {
                if (z4 && dimensionBehaviour4 == dimensionBehaviour2) {
                    dependencyGraph.container.setHorizontalDimensionBehaviour(dimensionBehaviour3);
                    ConstraintWidgetContainer constraintWidgetContainer = dependencyGraph.container;
                    constraintWidgetContainer.setWidth(dependencyGraph.computeWrap(constraintWidgetContainer, 0));
                    ConstraintWidgetContainer constraintWidgetContainer2 = dependencyGraph.container;
                    constraintWidgetContainer2.horizontalRun.dimension.resolve(constraintWidgetContainer2.getWidth());
                }
            } else if (z4 && dimensionBehaviour5 == dimensionBehaviour2) {
                dependencyGraph.container.setVerticalDimensionBehaviour(dimensionBehaviour3);
                ConstraintWidgetContainer constraintWidgetContainer3 = dependencyGraph.container;
                constraintWidgetContainer3.setHeight(dependencyGraph.computeWrap(constraintWidgetContainer3, 1));
                ConstraintWidgetContainer constraintWidgetContainer4 = dependencyGraph.container;
                constraintWidgetContainer4.verticalRun.dimension.resolve(constraintWidgetContainer4.getHeight());
            }
        }
        if (i == 0) {
            ConstraintWidgetContainer constraintWidgetContainer5 = dependencyGraph.container;
            ConstraintWidget.DimensionBehaviour[] dimensionBehaviourArr = constraintWidgetContainer5.mListDimensionBehaviors;
            if (dimensionBehaviourArr[0] == dimensionBehaviour3 || dimensionBehaviourArr[0] == dimensionBehaviour) {
                int width = constraintWidgetContainer5.getWidth() + x;
                dependencyGraph.container.horizontalRun.end.resolve(width);
                dependencyGraph.container.horizontalRun.dimension.resolve(width - x);
                z2 = true;
            }
            z2 = false;
        } else {
            ConstraintWidgetContainer constraintWidgetContainer6 = dependencyGraph.container;
            ConstraintWidget.DimensionBehaviour[] dimensionBehaviourArr2 = constraintWidgetContainer6.mListDimensionBehaviors;
            if (dimensionBehaviourArr2[1] == dimensionBehaviour3 || dimensionBehaviourArr2[1] == dimensionBehaviour) {
                int height = constraintWidgetContainer6.getHeight() + y;
                dependencyGraph.container.verticalRun.end.resolve(height);
                dependencyGraph.container.verticalRun.dimension.resolve(height - y);
                z2 = true;
            }
            z2 = false;
        }
        dependencyGraph.measureWidgets();
        Iterator<WidgetRun> it2 = dependencyGraph.mRuns.iterator();
        while (it2.hasNext()) {
            WidgetRun next2 = it2.next();
            if (next2.orientation == i && (next2.widget != dependencyGraph.container || next2.resolved)) {
                next2.applyToWidget();
            }
        }
        Iterator<WidgetRun> it3 = dependencyGraph.mRuns.iterator();
        while (it3.hasNext()) {
            WidgetRun next3 = it3.next();
            if (next3.orientation == i && (z2 || next3.widget != dependencyGraph.container)) {
                if (!next3.start.resolved || !next3.end.resolved || (!(next3 instanceof ChainRun) && !next3.dimension.resolved)) {
                    z3 = false;
                    break;
                }
            }
        }
        dependencyGraph.container.setHorizontalDimensionBehaviour(dimensionBehaviour4);
        dependencyGraph.container.setVerticalDimensionBehaviour(dimensionBehaviour5);
        return z3;
    }

    /* JADX WARN: Removed duplicated region for block: B:110:0x022e  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0069  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0082  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x010c  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x012e  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x01c6  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x01e0  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x01eb  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x01ee  */
    /* JADX WARN: Type inference failed for: r4v6, types: [boolean] */
    /* JADX WARN: Type inference failed for: r4v8 */
    /* JADX WARN: Type inference failed for: r4v9 */
    /* JADX WARN: Unknown variable types count: 1 */
    @Override // androidx.constraintlayout.solver.widgets.WidgetContainer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void layout() {
        /*
            Method dump skipped, instructions count: 577
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer.layout():void");
    }

    @Override // androidx.constraintlayout.solver.widgets.WidgetContainer, androidx.constraintlayout.solver.widgets.ConstraintWidget
    public final void reset() {
        this.mSystem.reset();
        this.mPaddingLeft = 0;
        this.mPaddingTop = 0;
        super.reset();
    }

    public final void updateHierarchy() {
        BasicMeasure basicMeasure = this.mBasicMeasureSolver;
        Objects.requireNonNull(basicMeasure);
        basicMeasure.mVariableDimensionsWidgets.clear();
        int size = this.mChildren.size();
        for (int i = 0; i < size; i++) {
            ConstraintWidget constraintWidget = this.mChildren.get(i);
            Objects.requireNonNull(constraintWidget);
            ConstraintWidget.DimensionBehaviour[] dimensionBehaviourArr = constraintWidget.mListDimensionBehaviors;
            ConstraintWidget.DimensionBehaviour dimensionBehaviour = dimensionBehaviourArr[0];
            ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
            if (dimensionBehaviour != dimensionBehaviour2) {
                ConstraintWidget.DimensionBehaviour dimensionBehaviour3 = dimensionBehaviourArr[0];
                ConstraintWidget.DimensionBehaviour dimensionBehaviour4 = ConstraintWidget.DimensionBehaviour.MATCH_PARENT;
                if (!(dimensionBehaviour3 == dimensionBehaviour4 || dimensionBehaviourArr[1] == dimensionBehaviour2 || dimensionBehaviourArr[1] == dimensionBehaviour4)) {
                }
            }
            basicMeasure.mVariableDimensionsWidgets.add(constraintWidget);
        }
        DependencyGraph dependencyGraph = this.mDependencyGraph;
        Objects.requireNonNull(dependencyGraph);
        dependencyGraph.mNeedBuildGraph = true;
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public final void updateFromRuns(boolean z, boolean z2) {
        super.updateFromRuns(z, z2);
        int size = this.mChildren.size();
        for (int i = 0; i < size; i++) {
            this.mChildren.get(i).updateFromRuns(z, z2);
        }
    }
}
