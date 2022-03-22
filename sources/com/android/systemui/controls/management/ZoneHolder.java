package com.android.systemui.controls.management;

import android.view.View;
import android.widget.TextView;
/* compiled from: ControlAdapter.kt */
/* loaded from: classes.dex */
public final class ZoneHolder extends Holder {
    public final TextView zone;

    @Override // com.android.systemui.controls.management.Holder
    public final void bindData(ElementWrapper elementWrapper) {
        this.zone.setText(((ZoneNameWrapper) elementWrapper).zoneName);
    }

    public ZoneHolder(View view) {
        super(view);
        this.zone = (TextView) view;
    }
}
