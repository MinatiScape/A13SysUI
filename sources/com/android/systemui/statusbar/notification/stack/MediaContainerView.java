package com.android.systemui.statusbar.notification.stack;

import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import com.android.systemui.statusbar.notification.row.ExpandableView;
/* compiled from: MediaContainerView.kt */
/* loaded from: classes.dex */
public final class MediaContainerView extends ExpandableView {
    public int clipHeight;
    public RectF clipRect = new RectF();
    public Path clipPath = new Path();
    public float cornerRadius = getContext().getResources().getDimensionPixelSize(2131166625);

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final void performAddAnimation(long j, long j2, boolean z) {
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final long performRemoveAnimation(long j, long j2, float f, boolean z, float f2, Runnable runnable, AnimatorListenerAdapter animatorListenerAdapter) {
        return 0L;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final void updateClipping() {
        int i = this.clipHeight;
        int i2 = this.mActualHeight;
        if (i != i2) {
            this.clipHeight = i2;
        }
        invalidate();
    }

    public MediaContainerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setWillNotDraw(false);
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView, android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.cornerRadius = getContext().getResources().getDimensionPixelSize(2131166625);
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect clipBounds = canvas.getClipBounds();
        clipBounds.bottom = this.clipHeight;
        this.clipRect.set(clipBounds);
        this.clipPath.reset();
        Path path = this.clipPath;
        RectF rectF = this.clipRect;
        float f = this.cornerRadius;
        path.addRoundRect(rectF, f, f, Path.Direction.CW);
        canvas.clipPath(this.clipPath);
    }
}
