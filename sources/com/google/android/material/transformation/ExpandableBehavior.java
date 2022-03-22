package com.google.android.material.transformation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import com.google.android.material.expandable.ExpandableWidget;
import java.util.List;
import java.util.WeakHashMap;
@Deprecated
/* loaded from: classes.dex */
public abstract class ExpandableBehavior extends CoordinatorLayout.Behavior<View> {
    public int currentState = 0;

    public ExpandableBehavior() {
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public abstract boolean layoutDependsOn(View view, View view2);

    public abstract void onExpandedStateChange(View view, View view2, boolean z, boolean z2);

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public final boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, View view, View view2) {
        boolean z;
        int i;
        ExpandableWidget expandableWidget = (ExpandableWidget) view2;
        int i2 = 2;
        if (!expandableWidget.isExpanded() ? this.currentState != 1 : !((i = this.currentState) == 0 || i == 2)) {
            z = false;
        } else {
            z = true;
        }
        if (!z) {
            return false;
        }
        if (expandableWidget.isExpanded()) {
            i2 = 1;
        }
        this.currentState = i2;
        onExpandedStateChange((View) expandableWidget, view, expandableWidget.isExpanded(), true);
        return true;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public final boolean onLayoutChild(CoordinatorLayout coordinatorLayout, final View view, int i) {
        final ExpandableWidget expandableWidget;
        boolean z;
        int i2;
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        if (!ViewCompat.Api19Impl.isLaidOut(view)) {
            List<View> dependencies = coordinatorLayout.getDependencies(view);
            int size = dependencies.size();
            int i3 = 0;
            while (true) {
                if (i3 >= size) {
                    expandableWidget = null;
                    break;
                }
                View view2 = dependencies.get(i3);
                if (layoutDependsOn(view, view2)) {
                    expandableWidget = (ExpandableWidget) view2;
                    break;
                }
                i3++;
            }
            if (expandableWidget != null) {
                final int i4 = 2;
                if (!expandableWidget.isExpanded() ? this.currentState != 1 : !((i2 = this.currentState) == 0 || i2 == 2)) {
                    z = false;
                } else {
                    z = true;
                }
                if (z) {
                    if (expandableWidget.isExpanded()) {
                        i4 = 1;
                    }
                    this.currentState = i4;
                    view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: com.google.android.material.transformation.ExpandableBehavior.1
                        @Override // android.view.ViewTreeObserver.OnPreDrawListener
                        public final boolean onPreDraw() {
                            view.getViewTreeObserver().removeOnPreDrawListener(this);
                            ExpandableBehavior expandableBehavior = ExpandableBehavior.this;
                            if (expandableBehavior.currentState == i4) {
                                ExpandableWidget expandableWidget2 = expandableWidget;
                                expandableBehavior.onExpandedStateChange((View) expandableWidget2, view, expandableWidget2.isExpanded(), false);
                            }
                            return false;
                        }
                    });
                }
            }
        }
        return false;
    }

    public ExpandableBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
