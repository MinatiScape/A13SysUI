package com.android.systemui.qs.customize;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.tileimpl.QSIconViewImpl;
import com.android.systemui.qs.tileimpl.QSTileViewImpl;
/* compiled from: CustomizeTileView.kt */
/* loaded from: classes.dex */
public final class CustomizeTileView extends QSTileViewImpl {
    public boolean showAppLabel;
    public boolean showSideView = true;

    public CustomizeTileView(Context context, QSIconViewImpl qSIconViewImpl) {
        super(context, qSIconViewImpl, false);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileViewImpl
    public final boolean animationsEnabled() {
        return false;
    }

    @Override // android.view.View
    public final boolean isLongClickable() {
        return false;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileViewImpl
    public final void handleStateChanged(QSTile.State state) {
        super.handleStateChanged(state);
        int i = 0;
        this.showRippleEffect = false;
        TextView secondaryLabel = mo78getSecondaryLabel();
        CharSequence charSequence = state.secondaryLabel;
        if (!this.showAppLabel || TextUtils.isEmpty(charSequence)) {
            i = 8;
        }
        secondaryLabel.setVisibility(i);
        if (!this.showSideView) {
            getSideView().setVisibility(8);
        }
    }
}
