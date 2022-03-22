package com.android.systemui.globalactions;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda19;
/* loaded from: classes.dex */
public class GlobalActionsColumnLayout extends GlobalActionsLayout {
    public static final /* synthetic */ int $r8$clinit = 0;
    public boolean mLastSnap;

    @VisibleForTesting
    public void centerAlongEdge() {
        int currentRotation = getCurrentRotation();
        if (currentRotation == 1) {
            setPadding(0, 0, 0, 0);
            setGravity(49);
        } else if (currentRotation != 3) {
            setPadding(0, 0, 0, 0);
            setGravity(21);
        } else {
            setPadding(0, 0, 0, 0);
            setGravity(81);
        }
    }

    @VisibleForTesting
    public float getAnimationDistance() {
        return getGridItemSize() / 2.0f;
    }

    @VisibleForTesting
    public float getGridItemSize() {
        return getContext().getResources().getDimension(2131165768);
    }

    @VisibleForTesting
    public int getPowerButtonOffsetDistance() {
        return Math.round(getContext().getResources().getDimension(2131165779));
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        post(new BubbleStackView$$ExternalSyntheticLambda19(this, 2));
    }

    @Override // com.android.systemui.globalactions.GlobalActionsLayout, com.android.systemui.MultiListLayout
    public final void onUpdateList() {
        super.onUpdateList();
        if (shouldReverseListItems()) {
            getListView().bringToFront();
        } else {
            getSeparatedView().bringToFront();
        }
    }

    @Override // com.android.systemui.globalactions.GlobalActionsLayout
    @VisibleForTesting
    public boolean shouldReverseListItems() {
        int currentRotation = getCurrentRotation();
        if (currentRotation == 0) {
            return false;
        }
        if (getCurrentLayoutDirection() == 1) {
            if (currentRotation == 1) {
                return true;
            }
            return false;
        } else if (currentRotation == 3) {
            return true;
        } else {
            return false;
        }
    }

    @VisibleForTesting
    public boolean shouldSnapToPowerButton() {
        int i;
        int i2;
        int powerButtonOffsetDistance = getPowerButtonOffsetDistance();
        View childAt = getChildAt(0);
        if (getCurrentRotation() == 0) {
            i2 = childAt.getMeasuredHeight();
            i = getMeasuredHeight();
        } else {
            i2 = childAt.getMeasuredWidth();
            i = getMeasuredWidth();
        }
        if (i2 + powerButtonOffsetDistance < i) {
            return true;
        }
        return false;
    }

    @VisibleForTesting
    public void snapToPowerButton() {
        int powerButtonOffsetDistance = getPowerButtonOffsetDistance();
        int currentRotation = getCurrentRotation();
        if (currentRotation == 1) {
            setPadding(powerButtonOffsetDistance, 0, 0, 0);
            setGravity(51);
        } else if (currentRotation != 3) {
            setPadding(0, powerButtonOffsetDistance, 0, 0);
            setGravity(53);
        } else {
            setPadding(0, 0, powerButtonOffsetDistance, 0);
            setGravity(85);
        }
    }

    @VisibleForTesting
    public void updateSnap() {
        boolean shouldSnapToPowerButton = shouldSnapToPowerButton();
        if (shouldSnapToPowerButton != this.mLastSnap) {
            if (shouldSnapToPowerButton) {
                snapToPowerButton();
            } else {
                centerAlongEdge();
            }
        }
        this.mLastSnap = shouldSnapToPowerButton;
    }

    public GlobalActionsColumnLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.android.systemui.globalactions.GlobalActionsLayout, android.widget.LinearLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
    }
}
