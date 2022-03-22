package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.widgets.Barrier;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.analyzer.DependencyNode;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class HelperReferences extends WidgetRun {
    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    public final void clear() {
        this.runGroup = null;
        this.start.clear();
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    public final boolean supportsWrapComputation() {
        return false;
    }

    public final void addDependency(DependencyNode dependencyNode) {
        this.start.dependencies.add(dependencyNode);
        dependencyNode.targets.add(this.start);
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    public final void apply() {
        ConstraintWidget constraintWidget = this.widget;
        if (constraintWidget instanceof Barrier) {
            this.start.delegateToWidgetRun = true;
            Barrier barrier = (Barrier) constraintWidget;
            Objects.requireNonNull(barrier);
            int i = barrier.mBarrierType;
            boolean z = barrier.mAllowsGoneWidget;
            int i2 = 0;
            if (i == 0) {
                this.start.type = DependencyNode.Type.LEFT;
                while (i2 < barrier.mWidgetsCount) {
                    ConstraintWidget constraintWidget2 = barrier.mWidgets[i2];
                    if (!z) {
                        Objects.requireNonNull(constraintWidget2);
                        if (constraintWidget2.mVisibility == 8) {
                            i2++;
                        }
                    }
                    DependencyNode dependencyNode = constraintWidget2.horizontalRun.start;
                    dependencyNode.dependencies.add(this.start);
                    this.start.targets.add(dependencyNode);
                    i2++;
                }
                addDependency(this.widget.horizontalRun.start);
                addDependency(this.widget.horizontalRun.end);
            } else if (i == 1) {
                this.start.type = DependencyNode.Type.RIGHT;
                while (i2 < barrier.mWidgetsCount) {
                    ConstraintWidget constraintWidget3 = barrier.mWidgets[i2];
                    if (!z) {
                        Objects.requireNonNull(constraintWidget3);
                        if (constraintWidget3.mVisibility == 8) {
                            i2++;
                        }
                    }
                    DependencyNode dependencyNode2 = constraintWidget3.horizontalRun.end;
                    dependencyNode2.dependencies.add(this.start);
                    this.start.targets.add(dependencyNode2);
                    i2++;
                }
                addDependency(this.widget.horizontalRun.start);
                addDependency(this.widget.horizontalRun.end);
            } else if (i == 2) {
                this.start.type = DependencyNode.Type.TOP;
                while (i2 < barrier.mWidgetsCount) {
                    ConstraintWidget constraintWidget4 = barrier.mWidgets[i2];
                    if (!z) {
                        Objects.requireNonNull(constraintWidget4);
                        if (constraintWidget4.mVisibility == 8) {
                            i2++;
                        }
                    }
                    DependencyNode dependencyNode3 = constraintWidget4.verticalRun.start;
                    dependencyNode3.dependencies.add(this.start);
                    this.start.targets.add(dependencyNode3);
                    i2++;
                }
                addDependency(this.widget.verticalRun.start);
                addDependency(this.widget.verticalRun.end);
            } else if (i == 3) {
                this.start.type = DependencyNode.Type.BOTTOM;
                while (i2 < barrier.mWidgetsCount) {
                    ConstraintWidget constraintWidget5 = barrier.mWidgets[i2];
                    if (!z) {
                        Objects.requireNonNull(constraintWidget5);
                        if (constraintWidget5.mVisibility == 8) {
                            i2++;
                        }
                    }
                    DependencyNode dependencyNode4 = constraintWidget5.verticalRun.end;
                    dependencyNode4.dependencies.add(this.start);
                    this.start.targets.add(dependencyNode4);
                    i2++;
                }
                addDependency(this.widget.verticalRun.start);
                addDependency(this.widget.verticalRun.end);
            }
        }
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    public final void applyToWidget() {
        ConstraintWidget constraintWidget = this.widget;
        if (constraintWidget instanceof Barrier) {
            Barrier barrier = (Barrier) constraintWidget;
            Objects.requireNonNull(barrier);
            int i = barrier.mBarrierType;
            if (i == 0 || i == 1) {
                ConstraintWidget constraintWidget2 = this.widget;
                int i2 = this.start.value;
                Objects.requireNonNull(constraintWidget2);
                constraintWidget2.mX = i2;
                return;
            }
            ConstraintWidget constraintWidget3 = this.widget;
            int i3 = this.start.value;
            Objects.requireNonNull(constraintWidget3);
            constraintWidget3.mY = i3;
        }
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun, androidx.constraintlayout.solver.widgets.analyzer.Dependency
    public final void update(Dependency dependency) {
        Barrier barrier = (Barrier) this.widget;
        Objects.requireNonNull(barrier);
        int i = barrier.mBarrierType;
        Iterator it = this.start.targets.iterator();
        int i2 = 0;
        int i3 = -1;
        while (it.hasNext()) {
            int i4 = ((DependencyNode) it.next()).value;
            if (i3 == -1 || i4 < i3) {
                i3 = i4;
            }
            if (i2 < i4) {
                i2 = i4;
            }
        }
        if (i == 0 || i == 2) {
            this.start.resolve(i3 + barrier.mMargin);
        } else {
            this.start.resolve(i2 + barrier.mMargin);
        }
    }

    public HelperReferences(ConstraintWidget constraintWidget) {
        super(constraintWidget);
    }
}
