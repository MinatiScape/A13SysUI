package androidx.constraintlayout.solver.widgets.analyzer;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.Helper;
import androidx.constraintlayout.solver.widgets.analyzer.DependencyNode;
import androidx.constraintlayout.solver.widgets.analyzer.WidgetRun;
import java.util.Objects;
/* loaded from: classes.dex */
public final class VerticalWidgetRun extends WidgetRun {
    public DependencyNode baseline;
    public BaselineDimensionDependency baselineDimension = null;

    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    public final void clear() {
        this.runGroup = null;
        this.start.clear();
        this.end.clear();
        this.baseline.clear();
        this.dimension.clear();
        this.resolved = false;
    }

    public final void reset() {
        this.resolved = false;
        this.start.clear();
        this.start.resolved = false;
        this.end.clear();
        this.end.resolved = false;
        this.baseline.clear();
        this.baseline.resolved = false;
        this.dimension.resolved = false;
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    public final void apply() {
        ConstraintWidget.DimensionBehaviour dimensionBehaviour = ConstraintWidget.DimensionBehaviour.MATCH_PARENT;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.FIXED;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour3 = ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        ConstraintWidget constraintWidget = this.widget;
        if (constraintWidget.measured) {
            this.dimension.resolve(constraintWidget.getHeight());
        }
        if (!this.dimension.resolved) {
            ConstraintWidget constraintWidget2 = this.widget;
            Objects.requireNonNull(constraintWidget2);
            this.dimensionBehavior = constraintWidget2.mListDimensionBehaviors[1];
            ConstraintWidget constraintWidget3 = this.widget;
            Objects.requireNonNull(constraintWidget3);
            if (constraintWidget3.hasBaseline) {
                this.baselineDimension = new BaselineDimensionDependency(this);
            }
            ConstraintWidget.DimensionBehaviour dimensionBehaviour4 = this.dimensionBehavior;
            if (dimensionBehaviour4 != dimensionBehaviour3) {
                if (dimensionBehaviour4 == dimensionBehaviour) {
                    ConstraintWidget constraintWidget4 = this.widget;
                    Objects.requireNonNull(constraintWidget4);
                    ConstraintWidget constraintWidget5 = constraintWidget4.mParent;
                    if (constraintWidget5 != null && constraintWidget5.mListDimensionBehaviors[1] == dimensionBehaviour2) {
                        int height = (constraintWidget5.getHeight() - this.widget.mTop.getMargin()) - this.widget.mBottom.getMargin();
                        WidgetRun.addTarget(this.start, constraintWidget5.verticalRun.start, this.widget.mTop.getMargin());
                        WidgetRun.addTarget(this.end, constraintWidget5.verticalRun.end, -this.widget.mBottom.getMargin());
                        this.dimension.resolve(height);
                        return;
                    }
                }
                if (this.dimensionBehavior == dimensionBehaviour2) {
                    this.dimension.resolve(this.widget.getHeight());
                }
            }
        } else if (this.dimensionBehavior == dimensionBehaviour) {
            ConstraintWidget constraintWidget6 = this.widget;
            Objects.requireNonNull(constraintWidget6);
            ConstraintWidget constraintWidget7 = constraintWidget6.mParent;
            if (constraintWidget7 != null && constraintWidget7.mListDimensionBehaviors[1] == dimensionBehaviour2) {
                WidgetRun.addTarget(this.start, constraintWidget7.verticalRun.start, this.widget.mTop.getMargin());
                WidgetRun.addTarget(this.end, constraintWidget7.verticalRun.end, -this.widget.mBottom.getMargin());
                return;
            }
        }
        DimensionDependency dimensionDependency = this.dimension;
        boolean z = dimensionDependency.resolved;
        if (z) {
            ConstraintWidget constraintWidget8 = this.widget;
            if (constraintWidget8.measured) {
                ConstraintAnchor[] constraintAnchorArr = constraintWidget8.mListAnchors;
                if (constraintAnchorArr[2].mTarget != null && constraintAnchorArr[3].mTarget != null) {
                    if (constraintWidget8.isInVerticalChain()) {
                        this.start.margin = this.widget.mListAnchors[2].getMargin();
                        this.end.margin = -this.widget.mListAnchors[3].getMargin();
                    } else {
                        DependencyNode target = WidgetRun.getTarget(this.widget.mListAnchors[2]);
                        if (target != null) {
                            WidgetRun.addTarget(this.start, target, this.widget.mListAnchors[2].getMargin());
                        }
                        DependencyNode target2 = WidgetRun.getTarget(this.widget.mListAnchors[3]);
                        if (target2 != null) {
                            WidgetRun.addTarget(this.end, target2, -this.widget.mListAnchors[3].getMargin());
                        }
                        this.start.delegateToWidgetRun = true;
                        this.end.delegateToWidgetRun = true;
                    }
                    ConstraintWidget constraintWidget9 = this.widget;
                    Objects.requireNonNull(constraintWidget9);
                    if (constraintWidget9.hasBaseline) {
                        DependencyNode dependencyNode = this.baseline;
                        DependencyNode dependencyNode2 = this.start;
                        ConstraintWidget constraintWidget10 = this.widget;
                        Objects.requireNonNull(constraintWidget10);
                        WidgetRun.addTarget(dependencyNode, dependencyNode2, constraintWidget10.mBaselineDistance);
                        return;
                    }
                    return;
                } else if (constraintAnchorArr[2].mTarget != null) {
                    DependencyNode target3 = WidgetRun.getTarget(constraintAnchorArr[2]);
                    if (target3 != null) {
                        WidgetRun.addTarget(this.start, target3, this.widget.mListAnchors[2].getMargin());
                        WidgetRun.addTarget(this.end, this.start, this.dimension.value);
                        ConstraintWidget constraintWidget11 = this.widget;
                        Objects.requireNonNull(constraintWidget11);
                        if (constraintWidget11.hasBaseline) {
                            DependencyNode dependencyNode3 = this.baseline;
                            DependencyNode dependencyNode4 = this.start;
                            ConstraintWidget constraintWidget12 = this.widget;
                            Objects.requireNonNull(constraintWidget12);
                            WidgetRun.addTarget(dependencyNode3, dependencyNode4, constraintWidget12.mBaselineDistance);
                            return;
                        }
                        return;
                    }
                    return;
                } else if (constraintAnchorArr[3].mTarget != null) {
                    DependencyNode target4 = WidgetRun.getTarget(constraintAnchorArr[3]);
                    if (target4 != null) {
                        WidgetRun.addTarget(this.end, target4, -this.widget.mListAnchors[3].getMargin());
                        WidgetRun.addTarget(this.start, this.end, -this.dimension.value);
                    }
                    ConstraintWidget constraintWidget13 = this.widget;
                    Objects.requireNonNull(constraintWidget13);
                    if (constraintWidget13.hasBaseline) {
                        DependencyNode dependencyNode5 = this.baseline;
                        DependencyNode dependencyNode6 = this.start;
                        ConstraintWidget constraintWidget14 = this.widget;
                        Objects.requireNonNull(constraintWidget14);
                        WidgetRun.addTarget(dependencyNode5, dependencyNode6, constraintWidget14.mBaselineDistance);
                        return;
                    }
                    return;
                } else if (constraintAnchorArr[4].mTarget != null) {
                    DependencyNode target5 = WidgetRun.getTarget(constraintAnchorArr[4]);
                    if (target5 != null) {
                        WidgetRun.addTarget(this.baseline, target5, 0);
                        DependencyNode dependencyNode7 = this.start;
                        DependencyNode dependencyNode8 = this.baseline;
                        ConstraintWidget constraintWidget15 = this.widget;
                        Objects.requireNonNull(constraintWidget15);
                        WidgetRun.addTarget(dependencyNode7, dependencyNode8, -constraintWidget15.mBaselineDistance);
                        WidgetRun.addTarget(this.end, this.start, this.dimension.value);
                        return;
                    }
                    return;
                } else if (!(constraintWidget8 instanceof Helper) && constraintWidget8.mParent != null && constraintWidget8.getAnchor(ConstraintAnchor.Type.CENTER).mTarget == null) {
                    ConstraintWidget constraintWidget16 = this.widget;
                    Objects.requireNonNull(constraintWidget16);
                    WidgetRun.addTarget(this.start, constraintWidget16.mParent.verticalRun.start, this.widget.getY());
                    WidgetRun.addTarget(this.end, this.start, this.dimension.value);
                    ConstraintWidget constraintWidget17 = this.widget;
                    Objects.requireNonNull(constraintWidget17);
                    if (constraintWidget17.hasBaseline) {
                        DependencyNode dependencyNode9 = this.baseline;
                        DependencyNode dependencyNode10 = this.start;
                        ConstraintWidget constraintWidget18 = this.widget;
                        Objects.requireNonNull(constraintWidget18);
                        WidgetRun.addTarget(dependencyNode9, dependencyNode10, constraintWidget18.mBaselineDistance);
                        return;
                    }
                    return;
                } else {
                    return;
                }
            }
        }
        if (z || this.dimensionBehavior != dimensionBehaviour3) {
            dimensionDependency.addDependency(this);
        } else {
            ConstraintWidget constraintWidget19 = this.widget;
            int i = constraintWidget19.mMatchConstraintDefaultHeight;
            if (i == 2) {
                ConstraintWidget constraintWidget20 = constraintWidget19.mParent;
                if (constraintWidget20 != null) {
                    DimensionDependency dimensionDependency2 = constraintWidget20.verticalRun.dimension;
                    dimensionDependency.targets.add(dimensionDependency2);
                    dimensionDependency2.dependencies.add(this.dimension);
                    DimensionDependency dimensionDependency3 = this.dimension;
                    dimensionDependency3.delegateToWidgetRun = true;
                    dimensionDependency3.dependencies.add(this.start);
                    this.dimension.dependencies.add(this.end);
                }
            } else if (i == 3 && !constraintWidget19.isInVerticalChain()) {
                ConstraintWidget constraintWidget21 = this.widget;
                if (constraintWidget21.mMatchConstraintDefaultWidth != 3) {
                    DimensionDependency dimensionDependency4 = constraintWidget21.horizontalRun.dimension;
                    this.dimension.targets.add(dimensionDependency4);
                    dimensionDependency4.dependencies.add(this.dimension);
                    DimensionDependency dimensionDependency5 = this.dimension;
                    dimensionDependency5.delegateToWidgetRun = true;
                    dimensionDependency5.dependencies.add(this.start);
                    this.dimension.dependencies.add(this.end);
                }
            }
        }
        ConstraintWidget constraintWidget22 = this.widget;
        ConstraintAnchor[] constraintAnchorArr2 = constraintWidget22.mListAnchors;
        if (constraintAnchorArr2[2].mTarget != null && constraintAnchorArr2[3].mTarget != null) {
            if (constraintWidget22.isInVerticalChain()) {
                this.start.margin = this.widget.mListAnchors[2].getMargin();
                this.end.margin = -this.widget.mListAnchors[3].getMargin();
            } else {
                DependencyNode target6 = WidgetRun.getTarget(this.widget.mListAnchors[2]);
                DependencyNode target7 = WidgetRun.getTarget(this.widget.mListAnchors[3]);
                target6.addDependency(this);
                target7.addDependency(this);
                this.mRunType = WidgetRun.RunType.CENTER;
            }
            ConstraintWidget constraintWidget23 = this.widget;
            Objects.requireNonNull(constraintWidget23);
            if (constraintWidget23.hasBaseline) {
                addTarget(this.baseline, this.start, 1, this.baselineDimension);
            }
        } else if (constraintAnchorArr2[2].mTarget != null) {
            DependencyNode target8 = WidgetRun.getTarget(constraintAnchorArr2[2]);
            if (target8 != null) {
                WidgetRun.addTarget(this.start, target8, this.widget.mListAnchors[2].getMargin());
                addTarget(this.end, this.start, 1, this.dimension);
                ConstraintWidget constraintWidget24 = this.widget;
                Objects.requireNonNull(constraintWidget24);
                if (constraintWidget24.hasBaseline) {
                    addTarget(this.baseline, this.start, 1, this.baselineDimension);
                }
                if (this.dimensionBehavior == dimensionBehaviour3) {
                    ConstraintWidget constraintWidget25 = this.widget;
                    Objects.requireNonNull(constraintWidget25);
                    if (constraintWidget25.mDimensionRatio > 0.0f) {
                        HorizontalWidgetRun horizontalWidgetRun = this.widget.horizontalRun;
                        if (horizontalWidgetRun.dimensionBehavior == dimensionBehaviour3) {
                            horizontalWidgetRun.dimension.dependencies.add(this.dimension);
                            this.dimension.targets.add(this.widget.horizontalRun.dimension);
                            this.dimension.updateDelegate = this;
                        }
                    }
                }
            }
        } else if (constraintAnchorArr2[3].mTarget != null) {
            DependencyNode target9 = WidgetRun.getTarget(constraintAnchorArr2[3]);
            if (target9 != null) {
                WidgetRun.addTarget(this.end, target9, -this.widget.mListAnchors[3].getMargin());
                addTarget(this.start, this.end, -1, this.dimension);
                ConstraintWidget constraintWidget26 = this.widget;
                Objects.requireNonNull(constraintWidget26);
                if (constraintWidget26.hasBaseline) {
                    addTarget(this.baseline, this.start, 1, this.baselineDimension);
                }
            }
        } else if (constraintAnchorArr2[4].mTarget != null) {
            DependencyNode target10 = WidgetRun.getTarget(constraintAnchorArr2[4]);
            if (target10 != null) {
                WidgetRun.addTarget(this.baseline, target10, 0);
                addTarget(this.start, this.baseline, -1, this.baselineDimension);
                addTarget(this.end, this.start, 1, this.dimension);
            }
        } else if (!(constraintWidget22 instanceof Helper) && constraintWidget22.mParent != null) {
            Objects.requireNonNull(constraintWidget22);
            WidgetRun.addTarget(this.start, constraintWidget22.mParent.verticalRun.start, this.widget.getY());
            addTarget(this.end, this.start, 1, this.dimension);
            ConstraintWidget constraintWidget27 = this.widget;
            Objects.requireNonNull(constraintWidget27);
            if (constraintWidget27.hasBaseline) {
                addTarget(this.baseline, this.start, 1, this.baselineDimension);
            }
            if (this.dimensionBehavior == dimensionBehaviour3) {
                ConstraintWidget constraintWidget28 = this.widget;
                Objects.requireNonNull(constraintWidget28);
                if (constraintWidget28.mDimensionRatio > 0.0f) {
                    HorizontalWidgetRun horizontalWidgetRun2 = this.widget.horizontalRun;
                    if (horizontalWidgetRun2.dimensionBehavior == dimensionBehaviour3) {
                        horizontalWidgetRun2.dimension.dependencies.add(this.dimension);
                        this.dimension.targets.add(this.widget.horizontalRun.dimension);
                        this.dimension.updateDelegate = this;
                    }
                }
            }
        }
        if (this.dimension.targets.size() == 0) {
            this.dimension.readyToSolve = true;
        }
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    public final void applyToWidget() {
        DependencyNode dependencyNode = this.start;
        if (dependencyNode.resolved) {
            ConstraintWidget constraintWidget = this.widget;
            int i = dependencyNode.value;
            Objects.requireNonNull(constraintWidget);
            constraintWidget.mY = i;
        }
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    public final boolean supportsWrapComputation() {
        if (this.dimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || this.widget.mMatchConstraintDefaultHeight == 0) {
            return true;
        }
        return false;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("VerticalRun ");
        ConstraintWidget constraintWidget = this.widget;
        Objects.requireNonNull(constraintWidget);
        m.append(constraintWidget.mDebugName);
        return m.toString();
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun, androidx.constraintlayout.solver.widgets.analyzer.Dependency
    public final void update(Dependency dependency) {
        DimensionDependency dimensionDependency;
        int i;
        float f;
        float f2;
        float f3;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour = ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (this.mRunType.ordinal() != 3) {
            DimensionDependency dimensionDependency2 = this.dimension;
            if (dimensionDependency2.readyToSolve && !dimensionDependency2.resolved && this.dimensionBehavior == dimensionBehaviour) {
                ConstraintWidget constraintWidget = this.widget;
                int i2 = constraintWidget.mMatchConstraintDefaultHeight;
                if (i2 == 2) {
                    ConstraintWidget constraintWidget2 = constraintWidget.mParent;
                    if (constraintWidget2 != null) {
                        if (constraintWidget2.verticalRun.dimension.resolved) {
                            dimensionDependency2.resolve((int) ((dimensionDependency.value * constraintWidget.mMatchConstraintPercentHeight) + 0.5f));
                        }
                    }
                } else if (i2 == 3) {
                    DimensionDependency dimensionDependency3 = constraintWidget.horizontalRun.dimension;
                    if (dimensionDependency3.resolved) {
                        int i3 = constraintWidget.mDimensionRatioSide;
                        if (i3 == -1) {
                            f3 = dimensionDependency3.value;
                            f2 = constraintWidget.mDimensionRatio;
                        } else if (i3 == 0) {
                            f = dimensionDependency3.value * constraintWidget.mDimensionRatio;
                            i = (int) (f + 0.5f);
                            dimensionDependency2.resolve(i);
                        } else if (i3 != 1) {
                            i = 0;
                            dimensionDependency2.resolve(i);
                        } else {
                            f3 = dimensionDependency3.value;
                            f2 = constraintWidget.mDimensionRatio;
                        }
                        f = f3 / f2;
                        i = (int) (f + 0.5f);
                        dimensionDependency2.resolve(i);
                    }
                }
            }
            DependencyNode dependencyNode = this.start;
            if (dependencyNode.readyToSolve) {
                DependencyNode dependencyNode2 = this.end;
                if (dependencyNode2.readyToSolve) {
                    if (!dependencyNode.resolved || !dependencyNode2.resolved || !this.dimension.resolved) {
                        if (!this.dimension.resolved && this.dimensionBehavior == dimensionBehaviour) {
                            ConstraintWidget constraintWidget3 = this.widget;
                            if (constraintWidget3.mMatchConstraintDefaultWidth == 0 && !constraintWidget3.isInVerticalChain()) {
                                int i4 = ((DependencyNode) this.start.targets.get(0)).value;
                                DependencyNode dependencyNode3 = this.start;
                                int i5 = i4 + dependencyNode3.margin;
                                int i6 = ((DependencyNode) this.end.targets.get(0)).value + this.end.margin;
                                dependencyNode3.resolve(i5);
                                this.end.resolve(i6);
                                this.dimension.resolve(i6 - i5);
                                return;
                            }
                        }
                        if (!this.dimension.resolved && this.dimensionBehavior == dimensionBehaviour && this.matchConstraintsType == 1 && this.start.targets.size() > 0 && this.end.targets.size() > 0) {
                            int i7 = (((DependencyNode) this.end.targets.get(0)).value + this.end.margin) - (((DependencyNode) this.start.targets.get(0)).value + this.start.margin);
                            DimensionDependency dimensionDependency4 = this.dimension;
                            int i8 = dimensionDependency4.wrapValue;
                            if (i7 < i8) {
                                dimensionDependency4.resolve(i7);
                            } else {
                                dimensionDependency4.resolve(i8);
                            }
                        }
                        if (this.dimension.resolved && this.start.targets.size() > 0 && this.end.targets.size() > 0) {
                            DependencyNode dependencyNode4 = (DependencyNode) this.start.targets.get(0);
                            DependencyNode dependencyNode5 = (DependencyNode) this.end.targets.get(0);
                            int i9 = dependencyNode4.value + this.start.margin;
                            int i10 = dependencyNode5.value + this.end.margin;
                            ConstraintWidget constraintWidget4 = this.widget;
                            Objects.requireNonNull(constraintWidget4);
                            float f4 = constraintWidget4.mVerticalBiasPercent;
                            if (dependencyNode4 == dependencyNode5) {
                                i9 = dependencyNode4.value;
                                i10 = dependencyNode5.value;
                                f4 = 0.5f;
                            }
                            this.start.resolve((int) ((((i10 - i9) - this.dimension.value) * f4) + i9 + 0.5f));
                            this.end.resolve(this.start.value + this.dimension.value);
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            return;
        }
        ConstraintWidget constraintWidget5 = this.widget;
        updateRunCenter(constraintWidget5.mTop, constraintWidget5.mBottom, 1);
    }

    public VerticalWidgetRun(ConstraintWidget constraintWidget) {
        super(constraintWidget);
        DependencyNode dependencyNode = new DependencyNode(this);
        this.baseline = dependencyNode;
        this.start.type = DependencyNode.Type.TOP;
        this.end.type = DependencyNode.Type.BOTTOM;
        dependencyNode.type = DependencyNode.Type.BASELINE;
        this.orientation = 1;
    }
}
