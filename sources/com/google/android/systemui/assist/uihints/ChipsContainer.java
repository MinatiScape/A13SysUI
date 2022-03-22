package com.google.android.systemui.assist.uihints;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Display;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.leanback.R$color;
import com.google.android.systemui.assist.uihints.TranscriptionController;
import com.google.common.util.concurrent.ImmediateFuture;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class ChipsContainer extends LinearLayout implements TranscriptionController.TranscriptionSpaceView {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final int CHIP_MARGIN;
    public final int START_DELTA;
    public ValueAnimator mAnimator;
    public int mAvailableWidth;
    public ArrayList mChipViews;
    public List<Bundle> mChips;
    public boolean mDarkBackground;

    public ChipsContainer(Context context) {
        this(context, null);
    }

    public ChipsContainer(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @Override // com.google.android.systemui.assist.uihints.TranscriptionController.TranscriptionSpaceView
    public final ListenableFuture<Void> hide(boolean z) {
        if (this.mAnimator.isRunning()) {
            this.mAnimator.cancel();
        }
        removeAllViews();
        setVisibility(8);
        setTranslationY(0.0f);
        return ImmediateFuture.NULL;
    }

    @Override // com.google.android.systemui.assist.uihints.TranscriptionController.TranscriptionSpaceView
    public final void onFontSizeChanged() {
        float dimension = ((LinearLayout) this).mContext.getResources().getDimension(2131165337);
        Iterator it = this.mChipViews.iterator();
        while (it.hasNext()) {
            ChipView chipView = (ChipView) it.next();
            Objects.requireNonNull(chipView);
            chipView.mLabelView.setTextSize(0, dimension);
        }
        requestLayout();
    }

    /* JADX WARN: Removed duplicated region for block: B:50:0x00db A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x000a A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void setChipsInternal() {
        /*
            Method dump skipped, instructions count: 309
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.assist.uihints.ChipsContainer.setChipsInternal():void");
    }

    @Override // com.google.android.systemui.assist.uihints.TranscriptionController.TranscriptionSpaceView
    public final void setHasDarkBackground(boolean z) {
        Drawable drawable;
        int i;
        if (this.mDarkBackground != z) {
            this.mDarkBackground = z;
            for (int i2 = 0; i2 < getChildCount(); i2++) {
                ChipView chipView = (ChipView) getChildAt(i2);
                Objects.requireNonNull(chipView);
                LinearLayout linearLayout = chipView.mChip;
                if (z) {
                    drawable = chipView.BACKGROUND_DARK;
                } else {
                    drawable = chipView.BACKGROUND_LIGHT;
                }
                linearLayout.setBackground(drawable);
                TextView textView = chipView.mLabelView;
                if (z) {
                    i = chipView.TEXT_COLOR_DARK;
                } else {
                    i = chipView.TEXT_COLOR_LIGHT;
                }
                textView.setTextColor(i);
            }
        }
    }

    public ChipsContainer(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    @Override // android.widget.LinearLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        Display defaultDisplay = R$color.getDefaultDisplay(getContext());
        Point point = new Point();
        defaultDisplay.getRealSize(point);
        int i3 = point.x;
        if (i3 != this.mAvailableWidth) {
            this.mAvailableWidth = i3;
            setChipsInternal();
        }
        super.onMeasure(i, i2);
    }

    public ChipsContainer(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mChips = new ArrayList();
        this.mChipViews = new ArrayList();
        this.mAnimator = new ValueAnimator();
        this.CHIP_MARGIN = (int) getResources().getDimension(2131165334);
        this.START_DELTA = (int) getResources().getDimension(2131165343);
    }
}
