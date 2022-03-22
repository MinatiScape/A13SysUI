package com.android.systemui.globalactions;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.constraintlayout.helper.widget.Flow;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.HardwareBgDrawable;
import java.util.Objects;
/* loaded from: classes.dex */
public class GlobalActionsLayoutLite extends GlobalActionsLayout {
    public static final /* synthetic */ int $r8$clinit = 0;

    @Override // com.android.systemui.globalactions.GlobalActionsLayout
    public final HardwareBgDrawable getBackgroundDrawable(int i) {
        return null;
    }

    @Override // com.android.systemui.globalactions.GlobalActionsLayout
    @VisibleForTesting
    public boolean shouldReverseListItems() {
        return false;
    }

    public GlobalActionsLayoutLite(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOnClickListener(GlobalActionsLayoutLite$$ExternalSyntheticLambda0.INSTANCE);
    }

    @Override // com.android.systemui.globalactions.GlobalActionsLayout
    public final void addToListView(View view, boolean z) {
        super.addToListView(view, z);
        ((Flow) findViewById(2131428262)).addView(view);
    }

    @VisibleForTesting
    public float getAnimationDistance() {
        return getGridItemSize() / 2.0f;
    }

    @VisibleForTesting
    public float getGridItemSize() {
        return getContext().getResources().getDimension(2131165768);
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        ViewGroup listView = getListView();
        boolean z2 = false;
        for (int i5 = 0; i5 < listView.getChildCount(); i5++) {
            View childAt = listView.getChildAt(i5);
            if (childAt instanceof GlobalActionsItem) {
                GlobalActionsItem globalActionsItem = (GlobalActionsItem) childAt;
                if (z2 || globalActionsItem.isTruncated()) {
                    z2 = true;
                } else {
                    z2 = false;
                }
            }
        }
        if (z2) {
            for (int i6 = 0; i6 < listView.getChildCount(); i6++) {
                View childAt2 = listView.getChildAt(i6);
                if (childAt2 instanceof GlobalActionsItem) {
                    GlobalActionsItem globalActionsItem2 = (GlobalActionsItem) childAt2;
                    Objects.requireNonNull(globalActionsItem2);
                    TextView textView = (TextView) globalActionsItem2.findViewById(16908299);
                    textView.setSingleLine(true);
                    textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                }
            }
        }
    }

    @Override // com.android.systemui.globalactions.GlobalActionsLayout, com.android.systemui.MultiListLayout
    public final void onUpdateList() {
        super.onUpdateList();
        int integer = getResources().getInteger(2131493023);
        if (getListView().getChildCount() - 1 == integer + 1 && integer > 2) {
            integer--;
        }
        Flow flow = (Flow) findViewById(2131428262);
        Objects.requireNonNull(flow);
        androidx.constraintlayout.solver.widgets.Flow flow2 = flow.mFlow;
        Objects.requireNonNull(flow2);
        flow2.mMaxElementsWrap = integer;
        flow.requestLayout();
    }

    @Override // com.android.systemui.MultiListLayout
    public final void removeAllListViews() {
        View findViewById = findViewById(2131428262);
        super.removeAllListViews();
        super.addToListView(findViewById, false);
    }
}
