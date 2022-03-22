package com.android.systemui.controls.management;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Objects;
/* compiled from: ControlAdapter.kt */
/* loaded from: classes.dex */
public final class MarginItemDecorator extends RecyclerView.ItemDecoration {
    public final int sideMargins;
    public final int topMargin;

    public MarginItemDecorator(int i, int i2) {
        this.topMargin = i;
        this.sideMargins = i2;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public final void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        Integer num;
        Objects.requireNonNull(recyclerView);
        int childAdapterPosition = RecyclerView.getChildAdapterPosition(view);
        if (childAdapterPosition != -1) {
            RecyclerView.Adapter adapter = recyclerView.mAdapter;
            if (adapter == null) {
                num = null;
            } else {
                num = Integer.valueOf(adapter.getItemViewType(childAdapterPosition));
            }
            if (num != null && num.intValue() == 1) {
                rect.top = this.topMargin * 2;
                int i = this.sideMargins;
                rect.left = i;
                rect.right = i;
                rect.bottom = 0;
            } else if (num != null && num.intValue() == 0 && childAdapterPosition == 0) {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
                rect.top = -((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
                rect.left = 0;
                rect.right = 0;
                rect.bottom = 0;
            }
        }
    }
}
