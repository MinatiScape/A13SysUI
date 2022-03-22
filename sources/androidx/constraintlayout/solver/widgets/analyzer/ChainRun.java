package androidx.constraintlayout.solver.widgets.analyzer;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ChainRun extends WidgetRun {
    public int chainStyle;
    public ArrayList<WidgetRun> widgets = new ArrayList<>();

    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    public final void applyToWidget() {
        for (int i = 0; i < this.widgets.size(); i++) {
            this.widgets.get(i).applyToWidget();
        }
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    public final void clear() {
        this.runGroup = null;
        Iterator<WidgetRun> it = this.widgets.iterator();
        while (it.hasNext()) {
            it.next().clear();
        }
    }

    public final ConstraintWidget getFirstVisibleWidget() {
        for (int i = 0; i < this.widgets.size(); i++) {
            WidgetRun widgetRun = this.widgets.get(i);
            ConstraintWidget constraintWidget = widgetRun.widget;
            Objects.requireNonNull(constraintWidget);
            if (constraintWidget.mVisibility != 8) {
                return widgetRun.widget;
            }
        }
        return null;
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    public final void apply() {
        Iterator<WidgetRun> it = this.widgets.iterator();
        while (it.hasNext()) {
            it.next().apply();
        }
        int size = this.widgets.size();
        if (size >= 1) {
            ConstraintWidget constraintWidget = this.widgets.get(0).widget;
            ConstraintWidget constraintWidget2 = this.widgets.get(size - 1).widget;
            if (this.orientation == 0) {
                ConstraintAnchor constraintAnchor = constraintWidget.mLeft;
                ConstraintAnchor constraintAnchor2 = constraintWidget2.mRight;
                DependencyNode target = WidgetRun.getTarget(constraintAnchor, 0);
                int margin = constraintAnchor.getMargin();
                ConstraintWidget firstVisibleWidget = getFirstVisibleWidget();
                if (firstVisibleWidget != null) {
                    margin = firstVisibleWidget.mLeft.getMargin();
                }
                if (target != null) {
                    WidgetRun.addTarget(this.start, target, margin);
                }
                DependencyNode target2 = WidgetRun.getTarget(constraintAnchor2, 0);
                int margin2 = constraintAnchor2.getMargin();
                ConstraintWidget lastVisibleWidget = getLastVisibleWidget();
                if (lastVisibleWidget != null) {
                    margin2 = lastVisibleWidget.mRight.getMargin();
                }
                if (target2 != null) {
                    WidgetRun.addTarget(this.end, target2, -margin2);
                }
            } else {
                ConstraintAnchor constraintAnchor3 = constraintWidget.mTop;
                ConstraintAnchor constraintAnchor4 = constraintWidget2.mBottom;
                DependencyNode target3 = WidgetRun.getTarget(constraintAnchor3, 1);
                int margin3 = constraintAnchor3.getMargin();
                ConstraintWidget firstVisibleWidget2 = getFirstVisibleWidget();
                if (firstVisibleWidget2 != null) {
                    margin3 = firstVisibleWidget2.mTop.getMargin();
                }
                if (target3 != null) {
                    WidgetRun.addTarget(this.start, target3, margin3);
                }
                DependencyNode target4 = WidgetRun.getTarget(constraintAnchor4, 1);
                int margin4 = constraintAnchor4.getMargin();
                ConstraintWidget lastVisibleWidget2 = getLastVisibleWidget();
                if (lastVisibleWidget2 != null) {
                    margin4 = lastVisibleWidget2.mBottom.getMargin();
                }
                if (target4 != null) {
                    WidgetRun.addTarget(this.end, target4, -margin4);
                }
            }
            this.start.updateDelegate = this;
            this.end.updateDelegate = this;
        }
    }

    public final ConstraintWidget getLastVisibleWidget() {
        for (int size = this.widgets.size() - 1; size >= 0; size--) {
            WidgetRun widgetRun = this.widgets.get(size);
            ConstraintWidget constraintWidget = widgetRun.widget;
            Objects.requireNonNull(constraintWidget);
            if (constraintWidget.mVisibility != 8) {
                return widgetRun.widget;
            }
        }
        return null;
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    public final long getWrapDimension() {
        int size = this.widgets.size();
        long j = 0;
        for (int i = 0; i < size; i++) {
            WidgetRun widgetRun = this.widgets.get(i);
            j = widgetRun.end.margin + widgetRun.getWrapDimension() + j + widgetRun.start.margin;
        }
        return j;
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    public final boolean supportsWrapComputation() {
        int size = this.widgets.size();
        for (int i = 0; i < size; i++) {
            if (!this.widgets.get(i).supportsWrapComputation()) {
                return false;
            }
        }
        return true;
    }

    public final String toString() {
        String str;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("ChainRun ");
        if (this.orientation == 0) {
            str = "horizontal : ";
        } else {
            str = "vertical : ";
        }
        m.append(str);
        String sb = m.toString();
        Iterator<WidgetRun> it = this.widgets.iterator();
        while (it.hasNext()) {
            String m2 = SupportMenuInflater$$ExternalSyntheticOutline0.m(sb, "<");
            sb = SupportMenuInflater$$ExternalSyntheticOutline0.m(m2 + it.next(), "> ");
        }
        return sb;
    }

    /* JADX WARN: Code restructure failed: missing block: B:106:0x01aa, code lost:
        if (r1 != r10) goto L_0x01d2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x01d0, code lost:
        if (r1 != r10) goto L_0x01d2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:115:0x01d2, code lost:
        r16 = r16 + 1;
        r10 = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x01d5, code lost:
        r11.dimension.resolve(r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:273:0x0420, code lost:
        r10 = r10 - r8;
     */
    /* JADX WARN: Removed duplicated region for block: B:62:0x00dc  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x00ee  */
    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun, androidx.constraintlayout.solver.widgets.analyzer.Dependency
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void update(androidx.constraintlayout.solver.widgets.analyzer.Dependency r27) {
        /*
            Method dump skipped, instructions count: 1095
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.analyzer.ChainRun.update(androidx.constraintlayout.solver.widgets.analyzer.Dependency):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x00ad  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00b5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public ChainRun(androidx.constraintlayout.solver.widgets.ConstraintWidget r5, int r6) {
        /*
            r4 = this;
            r4.<init>(r5)
            java.util.ArrayList r5 = new java.util.ArrayList
            r5.<init>()
            r4.widgets = r5
            r4.orientation = r6
            androidx.constraintlayout.solver.widgets.ConstraintWidget r5 = r4.widget
            androidx.constraintlayout.solver.widgets.ConstraintWidget r6 = r5.getPreviousChainMember(r6)
        L_0x0012:
            r3 = r6
            r6 = r5
            r5 = r3
            if (r5 == 0) goto L_0x001e
            int r6 = r4.orientation
            androidx.constraintlayout.solver.widgets.ConstraintWidget r6 = r5.getPreviousChainMember(r6)
            goto L_0x0012
        L_0x001e:
            r4.widget = r6
            java.util.ArrayList<androidx.constraintlayout.solver.widgets.analyzer.WidgetRun> r5 = r4.widgets
            int r0 = r4.orientation
            r1 = 0
            r2 = 1
            if (r0 != 0) goto L_0x002b
            androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun r0 = r6.horizontalRun
            goto L_0x0031
        L_0x002b:
            if (r0 != r2) goto L_0x0030
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r0 = r6.verticalRun
            goto L_0x0031
        L_0x0030:
            r0 = r1
        L_0x0031:
            r5.add(r0)
            int r5 = r4.orientation
            androidx.constraintlayout.solver.widgets.ConstraintWidget r5 = r6.getNextChainMember(r5)
        L_0x003a:
            if (r5 == 0) goto L_0x0055
            java.util.ArrayList<androidx.constraintlayout.solver.widgets.analyzer.WidgetRun> r6 = r4.widgets
            int r0 = r4.orientation
            if (r0 != 0) goto L_0x0045
            androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun r0 = r5.horizontalRun
            goto L_0x004b
        L_0x0045:
            if (r0 != r2) goto L_0x004a
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r0 = r5.verticalRun
            goto L_0x004b
        L_0x004a:
            r0 = r1
        L_0x004b:
            r6.add(r0)
            int r6 = r4.orientation
            androidx.constraintlayout.solver.widgets.ConstraintWidget r5 = r5.getNextChainMember(r6)
            goto L_0x003a
        L_0x0055:
            java.util.ArrayList<androidx.constraintlayout.solver.widgets.analyzer.WidgetRun> r5 = r4.widgets
            java.util.Iterator r5 = r5.iterator()
        L_0x005b:
            boolean r6 = r5.hasNext()
            if (r6 == 0) goto L_0x0077
            java.lang.Object r6 = r5.next()
            androidx.constraintlayout.solver.widgets.analyzer.WidgetRun r6 = (androidx.constraintlayout.solver.widgets.analyzer.WidgetRun) r6
            int r0 = r4.orientation
            if (r0 != 0) goto L_0x0070
            androidx.constraintlayout.solver.widgets.ConstraintWidget r6 = r6.widget
            r6.horizontalChainRun = r4
            goto L_0x005b
        L_0x0070:
            if (r0 != r2) goto L_0x005b
            androidx.constraintlayout.solver.widgets.ConstraintWidget r6 = r6.widget
            r6.verticalChainRun = r4
            goto L_0x005b
        L_0x0077:
            int r5 = r4.orientation
            if (r5 != 0) goto L_0x008d
            androidx.constraintlayout.solver.widgets.ConstraintWidget r5 = r4.widget
            java.util.Objects.requireNonNull(r5)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r5 = r5.mParent
            androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer r5 = (androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer) r5
            java.util.Objects.requireNonNull(r5)
            boolean r5 = r5.mIsRtl
            if (r5 == 0) goto L_0x008d
            r5 = r2
            goto L_0x008e
        L_0x008d:
            r5 = 0
        L_0x008e:
            if (r5 == 0) goto L_0x00a9
            java.util.ArrayList<androidx.constraintlayout.solver.widgets.analyzer.WidgetRun> r5 = r4.widgets
            int r5 = r5.size()
            if (r5 <= r2) goto L_0x00a9
            java.util.ArrayList<androidx.constraintlayout.solver.widgets.analyzer.WidgetRun> r5 = r4.widgets
            int r6 = r5.size()
            int r6 = r6 - r2
            java.lang.Object r5 = r5.get(r6)
            androidx.constraintlayout.solver.widgets.analyzer.WidgetRun r5 = (androidx.constraintlayout.solver.widgets.analyzer.WidgetRun) r5
            androidx.constraintlayout.solver.widgets.ConstraintWidget r5 = r5.widget
            r4.widget = r5
        L_0x00a9:
            int r5 = r4.orientation
            if (r5 != 0) goto L_0x00b5
            androidx.constraintlayout.solver.widgets.ConstraintWidget r5 = r4.widget
            java.util.Objects.requireNonNull(r5)
            int r5 = r5.mHorizontalChainStyle
            goto L_0x00bc
        L_0x00b5:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r5 = r4.widget
            java.util.Objects.requireNonNull(r5)
            int r5 = r5.mVerticalChainStyle
        L_0x00bc:
            r4.chainStyle = r5
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.analyzer.ChainRun.<init>(androidx.constraintlayout.solver.widgets.ConstraintWidget, int):void");
    }
}
