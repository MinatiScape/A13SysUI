package com.google.android.setupcompat.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import com.android.systemui.qs.tileimpl.QSTileImpl;
/* loaded from: classes.dex */
public class StatusBarBackgroundLayout extends FrameLayout {
    public WindowInsets lastInsets;
    public Drawable statusBarBackground;

    public StatusBarBackgroundLayout(Context context) {
        super(context);
    }

    public StatusBarBackgroundLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public final WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        this.lastInsets = windowInsets;
        return super.onApplyWindowInsets(windowInsets);
    }

    @TargetApi(QSTileImpl.H.STALE)
    public StatusBarBackgroundLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.lastInsets == null) {
            requestApplyInsets();
        }
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        int systemWindowInsetTop;
        super.onDraw(canvas);
        WindowInsets windowInsets = this.lastInsets;
        if (windowInsets != null && (systemWindowInsetTop = windowInsets.getSystemWindowInsetTop()) > 0) {
            this.statusBarBackground.setBounds(0, 0, getWidth(), systemWindowInsetTop);
            this.statusBarBackground.draw(canvas);
        }
    }
}
