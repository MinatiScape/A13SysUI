package androidx.leanback.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import java.util.ArrayList;
/* loaded from: classes.dex */
class ControlBar extends LinearLayout {
    public int mLastFocusIndex = -1;
    public boolean mDefaultFocusToMiddle = true;

    public ControlBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void addFocusables(ArrayList<View> arrayList, int i, int i2) {
        int i3;
        if (i == 33 || i == 130) {
            int i4 = this.mLastFocusIndex;
            if (i4 >= 0 && i4 < getChildCount()) {
                arrayList.add(getChildAt(this.mLastFocusIndex));
            } else if (getChildCount() > 0) {
                if (this.mDefaultFocusToMiddle) {
                    i3 = getChildCount() / 2;
                } else {
                    i3 = 0;
                }
                arrayList.add(getChildAt(i3));
            }
        } else {
            super.addFocusables(arrayList, i, i2);
        }
    }

    @Override // android.view.ViewGroup
    public final boolean onRequestFocusInDescendants(int i, Rect rect) {
        int i2;
        if (getChildCount() > 0) {
            int i3 = this.mLastFocusIndex;
            if (i3 >= 0 && i3 < getChildCount()) {
                i2 = this.mLastFocusIndex;
            } else if (this.mDefaultFocusToMiddle) {
                i2 = getChildCount() / 2;
            } else {
                i2 = 0;
            }
            if (getChildAt(i2).requestFocus(i, rect)) {
                return true;
            }
        }
        return super.onRequestFocusInDescendants(i, rect);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final void requestChildFocus(View view, View view2) {
        super.requestChildFocus(view, view2);
        this.mLastFocusIndex = indexOfChild(view);
    }

    public ControlBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.widget.LinearLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
    }
}
