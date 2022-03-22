package androidx.constraintlayout.solver.widgets.analyzer;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.analyzer.DependencyNode;
import java.util.Objects;
/* loaded from: classes.dex */
public final class HorizontalWidgetRun extends WidgetRun {
    public static int[] tempDimensions = new int[2];

    public static void computeInsetRatio(int[] iArr, int i, int i2, int i3, int i4, float f, int i5) {
        int i6 = i2 - i;
        int i7 = i4 - i3;
        if (i5 == -1) {
            int i8 = (int) ((i7 * f) + 0.5f);
            int i9 = (int) ((i6 / f) + 0.5f);
            if (i8 <= i6) {
                iArr[0] = i8;
                iArr[1] = i7;
            } else if (i9 <= i7) {
                iArr[0] = i6;
                iArr[1] = i9;
            }
        } else if (i5 == 0) {
            iArr[0] = (int) ((i7 * f) + 0.5f);
            iArr[1] = i7;
        } else if (i5 == 1) {
            iArr[0] = i6;
            iArr[1] = (int) ((i6 * f) + 0.5f);
        }
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    public final void clear() {
        this.runGroup = null;
        this.start.clear();
        this.end.clear();
        this.dimension.clear();
        this.resolved = false;
    }

    public final void reset() {
        this.resolved = false;
        this.start.clear();
        this.start.resolved = false;
        this.end.clear();
        this.end.resolved = false;
        this.dimension.resolved = false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0041, code lost:
        if (r3.mListDimensionBehaviors[0] == r1) goto L_0x0043;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00ac, code lost:
        if (r3.mListDimensionBehaviors[0] == r1) goto L_0x00ae;
     */
    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void apply() {
        /*
            Method dump skipped, instructions count: 984
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun.apply():void");
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    public final void applyToWidget() {
        DependencyNode dependencyNode = this.start;
        if (dependencyNode.resolved) {
            ConstraintWidget constraintWidget = this.widget;
            int i = dependencyNode.value;
            Objects.requireNonNull(constraintWidget);
            constraintWidget.mX = i;
        }
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    public final boolean supportsWrapComputation() {
        if (this.dimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || this.widget.mMatchConstraintDefaultWidth == 0) {
            return true;
        }
        return false;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("HorizontalRun ");
        ConstraintWidget constraintWidget = this.widget;
        Objects.requireNonNull(constraintWidget);
        m.append(constraintWidget.mDebugName);
        return m.toString();
    }

    /* JADX WARN: Code restructure failed: missing block: B:105:0x027f, code lost:
        if (r15 != 1) goto L_0x02de;
     */
    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun, androidx.constraintlayout.solver.widgets.analyzer.Dependency
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void update(androidx.constraintlayout.solver.widgets.analyzer.Dependency r21) {
        /*
            Method dump skipped, instructions count: 1030
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun.update(androidx.constraintlayout.solver.widgets.analyzer.Dependency):void");
    }

    public HorizontalWidgetRun(ConstraintWidget constraintWidget) {
        super(constraintWidget);
        this.start.type = DependencyNode.Type.LEFT;
        this.end.type = DependencyNode.Type.RIGHT;
        this.orientation = 0;
    }
}
