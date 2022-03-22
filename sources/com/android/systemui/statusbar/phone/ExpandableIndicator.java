package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
/* loaded from: classes.dex */
public class ExpandableIndicator extends ImageView {
    public boolean mIsDefaultDirection = true;

    public ExpandableIndicator(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        int i;
        super.onFinishInflate();
        if (this.mIsDefaultDirection) {
            i = 2131232313;
        } else {
            i = 2131232312;
        }
        setImageResource(i);
        setContentDescription(((ImageView) this).mContext.getString(2131951792));
    }
}
