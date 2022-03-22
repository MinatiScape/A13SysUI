package com.android.systemui.controls.management;

import android.view.View;
/* compiled from: ControlAdapter.kt */
/* loaded from: classes.dex */
public final class DividerHolder extends Holder {
    public final View divider;
    public final View frame;

    @Override // com.android.systemui.controls.management.Holder
    public final void bindData(ElementWrapper elementWrapper) {
        int i;
        DividerWrapper dividerWrapper = (DividerWrapper) elementWrapper;
        View view = this.frame;
        int i2 = 0;
        if (dividerWrapper.showNone) {
            i = 0;
        } else {
            i = 8;
        }
        view.setVisibility(i);
        View view2 = this.divider;
        if (!dividerWrapper.showDivider) {
            i2 = 8;
        }
        view2.setVisibility(i2);
    }

    public DividerHolder(View view) {
        super(view);
        this.frame = view.requireViewById(2131427993);
        this.divider = view.requireViewById(2131427855);
    }
}
