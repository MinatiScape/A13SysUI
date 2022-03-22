package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public class DependencyNode implements Dependency {
    public int margin;
    public WidgetRun run;
    public int value;
    public WidgetRun updateDelegate = null;
    public boolean delegateToWidgetRun = false;
    public boolean readyToSolve = false;
    public Type type = Type.UNKNOWN;
    public int marginFactor = 1;
    public DimensionDependency marginDependency = null;
    public boolean resolved = false;
    public ArrayList dependencies = new ArrayList();
    public ArrayList targets = new ArrayList();

    /* loaded from: classes.dex */
    public enum Type {
        UNKNOWN,
        HORIZONTAL_DIMENSION,
        VERTICAL_DIMENSION,
        LEFT,
        RIGHT,
        TOP,
        BOTTOM,
        BASELINE
    }

    public final void addDependency(Dependency dependency) {
        this.dependencies.add(dependency);
        if (this.resolved) {
            dependency.update(dependency);
        }
    }

    public final void clear() {
        this.targets.clear();
        this.dependencies.clear();
        this.resolved = false;
        this.value = 0;
        this.readyToSolve = false;
        this.delegateToWidgetRun = false;
    }

    public void resolve(int i) {
        if (!this.resolved) {
            this.resolved = true;
            this.value = i;
            Iterator it = this.dependencies.iterator();
            while (it.hasNext()) {
                Dependency dependency = (Dependency) it.next();
                dependency.update(dependency);
            }
        }
    }

    public final String toString() {
        Object obj;
        StringBuilder sb = new StringBuilder();
        ConstraintWidget constraintWidget = this.run.widget;
        Objects.requireNonNull(constraintWidget);
        sb.append(constraintWidget.mDebugName);
        sb.append(":");
        sb.append(this.type);
        sb.append("(");
        if (this.resolved) {
            obj = Integer.valueOf(this.value);
        } else {
            obj = "unresolved";
        }
        sb.append(obj);
        sb.append(") <t=");
        sb.append(this.targets.size());
        sb.append(":d=");
        sb.append(this.dependencies.size());
        sb.append(">");
        return sb.toString();
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.Dependency
    public final void update(Dependency dependency) {
        Iterator it = this.targets.iterator();
        while (it.hasNext()) {
            if (!((DependencyNode) it.next()).resolved) {
                return;
            }
        }
        this.readyToSolve = true;
        WidgetRun widgetRun = this.updateDelegate;
        if (widgetRun != null) {
            widgetRun.update(this);
        }
        if (this.delegateToWidgetRun) {
            this.run.update(this);
            return;
        }
        DependencyNode dependencyNode = null;
        int i = 0;
        Iterator it2 = this.targets.iterator();
        while (it2.hasNext()) {
            DependencyNode dependencyNode2 = (DependencyNode) it2.next();
            if (!(dependencyNode2 instanceof DimensionDependency)) {
                i++;
                dependencyNode = dependencyNode2;
            }
        }
        if (dependencyNode != null && i == 1 && dependencyNode.resolved) {
            DimensionDependency dimensionDependency = this.marginDependency;
            if (dimensionDependency != null) {
                if (dimensionDependency.resolved) {
                    this.margin = this.marginFactor * dimensionDependency.value;
                } else {
                    return;
                }
            }
            resolve(dependencyNode.value + this.margin);
        }
        WidgetRun widgetRun2 = this.updateDelegate;
        if (widgetRun2 != null) {
            widgetRun2.update(this);
        }
    }

    public DependencyNode(WidgetRun widgetRun) {
        this.run = widgetRun;
    }
}
