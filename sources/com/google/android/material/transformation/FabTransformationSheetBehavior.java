package com.google.android.material.transformation;

import android.content.Context;
import android.util.AttributeSet;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.animation.Positioning;
import com.google.android.material.transformation.FabTransformationBehavior;
import java.util.HashMap;
@Deprecated
/* loaded from: classes.dex */
public class FabTransformationSheetBehavior extends FabTransformationBehavior {
    public HashMap importantForAccessibilityMap;

    public FabTransformationSheetBehavior() {
    }

    public FabTransformationSheetBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.google.android.material.transformation.FabTransformationBehavior
    public final FabTransformationBehavior.FabTransformationSpec onCreateMotionSpec(Context context, boolean z) {
        int i;
        if (z) {
            i = 2130837555;
        } else {
            i = 2130837554;
        }
        FabTransformationBehavior.FabTransformationSpec fabTransformationSpec = new FabTransformationBehavior.FabTransformationSpec();
        fabTransformationSpec.timings = MotionSpec.createFromResource(context, i);
        fabTransformationSpec.positioning = new Positioning();
        return fabTransformationSpec;
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0042  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x005e  */
    @Override // com.google.android.material.transformation.ExpandableTransformationBehavior, com.google.android.material.transformation.ExpandableBehavior
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onExpandedStateChange(android.view.View r8, android.view.View r9, boolean r10, boolean r11) {
        /*
            r7 = this;
            android.view.ViewParent r0 = r9.getParent()
            boolean r1 = r0 instanceof androidx.coordinatorlayout.widget.CoordinatorLayout
            if (r1 != 0) goto L_0x000a
            goto L_0x0079
        L_0x000a:
            androidx.coordinatorlayout.widget.CoordinatorLayout r0 = (androidx.coordinatorlayout.widget.CoordinatorLayout) r0
            int r1 = r0.getChildCount()
            if (r10 == 0) goto L_0x0019
            java.util.HashMap r2 = new java.util.HashMap
            r2.<init>(r1)
            r7.importantForAccessibilityMap = r2
        L_0x0019:
            r2 = 0
            r3 = r2
        L_0x001b:
            if (r3 >= r1) goto L_0x0074
            android.view.View r4 = r0.getChildAt(r3)
            android.view.ViewGroup$LayoutParams r5 = r4.getLayoutParams()
            boolean r5 = r5 instanceof androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
            if (r5 == 0) goto L_0x003a
            android.view.ViewGroup$LayoutParams r5 = r4.getLayoutParams()
            androidx.coordinatorlayout.widget.CoordinatorLayout$LayoutParams r5 = (androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams) r5
            java.util.Objects.requireNonNull(r5)
            androidx.coordinatorlayout.widget.CoordinatorLayout$Behavior r5 = r5.mBehavior
            boolean r5 = r5 instanceof com.google.android.material.transformation.FabTransformationScrimBehavior
            if (r5 == 0) goto L_0x003a
            r5 = 1
            goto L_0x003b
        L_0x003a:
            r5 = r2
        L_0x003b:
            if (r4 == r9) goto L_0x0071
            if (r5 == 0) goto L_0x0040
            goto L_0x0071
        L_0x0040:
            if (r10 != 0) goto L_0x005e
            java.util.HashMap r5 = r7.importantForAccessibilityMap
            if (r5 == 0) goto L_0x0071
            boolean r5 = r5.containsKey(r4)
            if (r5 == 0) goto L_0x0071
            java.util.HashMap r5 = r7.importantForAccessibilityMap
            java.lang.Object r5 = r5.get(r4)
            java.lang.Integer r5 = (java.lang.Integer) r5
            int r5 = r5.intValue()
            java.util.WeakHashMap<android.view.View, androidx.core.view.ViewPropertyAnimatorCompat> r6 = androidx.core.view.ViewCompat.sViewPropertyAnimatorMap
            androidx.core.view.ViewCompat.Api16Impl.setImportantForAccessibility(r4, r5)
            goto L_0x0071
        L_0x005e:
            java.util.HashMap r5 = r7.importantForAccessibilityMap
            int r6 = r4.getImportantForAccessibility()
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)
            r5.put(r4, r6)
            r5 = 4
            java.util.WeakHashMap<android.view.View, androidx.core.view.ViewPropertyAnimatorCompat> r6 = androidx.core.view.ViewCompat.sViewPropertyAnimatorMap
            androidx.core.view.ViewCompat.Api16Impl.setImportantForAccessibility(r4, r5)
        L_0x0071:
            int r3 = r3 + 1
            goto L_0x001b
        L_0x0074:
            if (r10 != 0) goto L_0x0079
            r0 = 0
            r7.importantForAccessibilityMap = r0
        L_0x0079:
            super.onExpandedStateChange(r8, r9, r10, r11)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.transformation.FabTransformationSheetBehavior.onExpandedStateChange(android.view.View, android.view.View, boolean, boolean):void");
    }
}
