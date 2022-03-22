package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.widgets.analyzer.DependencyNode;
import java.util.Iterator;
/* loaded from: classes.dex */
public class DimensionDependency extends DependencyNode {
    public int wrapValue;

    @Override // androidx.constraintlayout.solver.widgets.analyzer.DependencyNode
    public final void resolve(int i) {
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

    public DimensionDependency(WidgetRun widgetRun) {
        super(widgetRun);
        if (widgetRun instanceof HorizontalWidgetRun) {
            this.type = DependencyNode.Type.HORIZONTAL_DIMENSION;
        } else {
            this.type = DependencyNode.Type.VERTICAL_DIMENSION;
        }
    }
}
