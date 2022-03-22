package com.google.android.setupdesign.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;
import com.android.systemui.qs.tileimpl.QSTileImpl;
/* loaded from: classes.dex */
public class CheckableLinearLayout extends LinearLayout implements Checkable {
    public boolean checked = false;

    public CheckableLinearLayout(Context context) {
        super(context);
        setFocusable(true);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final int[] onCreateDrawableState(int i) {
        if (this.checked) {
            return View.mergeDrawableStates(super.onCreateDrawableState(i + 1), new int[]{16842912});
        }
        return super.onCreateDrawableState(i);
    }

    @Override // android.widget.Checkable
    public final void setChecked(boolean z) {
        this.checked = z;
        refreshDrawableState();
    }

    @Override // android.widget.Checkable
    public final void toggle() {
        setChecked(!isChecked());
    }

    public CheckableLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setFocusable(true);
    }

    @TargetApi(QSTileImpl.H.STALE)
    public CheckableLinearLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setFocusable(true);
    }

    @TargetApi(21)
    public CheckableLinearLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        setFocusable(true);
    }

    @Override // android.widget.Checkable
    public final boolean isChecked() {
        return this.checked;
    }
}
