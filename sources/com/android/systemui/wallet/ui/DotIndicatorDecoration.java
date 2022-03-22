package com.android.systemui.wallet.ui;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DotIndicatorDecoration extends RecyclerView.ItemDecoration {
    public WalletCardCarousel mCardCarousel;
    public final int mDotMargin;
    public final Paint mPaint = new Paint(1);
    public final int mSelectedColor;
    public final int mSelectedRadius;
    public final int mUnselectedColor;
    public final int mUnselectedRadius;

    /* JADX WARN: Code restructure failed: missing block: B:32:0x00bb, code lost:
        if (r6.mEdgeToCenterDistance >= 0.0f) goto L_0x00e0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00c5, code lost:
        if (r6.mEdgeToCenterDistance < 0.0f) goto L_0x00e0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00d4, code lost:
        if (r6.mEdgeToCenterDistance >= 0.0f) goto L_0x00e0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00de, code lost:
        if (r6.mEdgeToCenterDistance < 0.0f) goto L_0x00e0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00e0, code lost:
        r5 = true;
     */
    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onDrawOver(android.graphics.Canvas r12, androidx.recyclerview.widget.RecyclerView r13) {
        /*
            Method dump skipped, instructions count: 306
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.wallet.ui.DotIndicatorDecoration.onDrawOver(android.graphics.Canvas, androidx.recyclerview.widget.RecyclerView):void");
    }

    public DotIndicatorDecoration(Context context) {
        this.mUnselectedRadius = context.getResources().getDimensionPixelSize(2131165475);
        this.mSelectedRadius = context.getResources().getDimensionPixelSize(2131165474);
        this.mDotMargin = context.getResources().getDimensionPixelSize(2131165472);
        this.mUnselectedColor = context.getColor(2131100295);
        this.mSelectedColor = context.getColor(2131100289);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public final void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        super.getItemOffsets(rect, view, recyclerView, state);
        Objects.requireNonNull(recyclerView);
        if (recyclerView.mAdapter.getItemCount() > 1) {
            rect.bottom = view.getResources().getDimensionPixelSize(2131165473);
        }
    }
}
