package com.android.systemui.media.dream;

import android.view.View;
import android.widget.FrameLayout;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.dreams.complication.ComplicationLayoutParams;
/* loaded from: classes.dex */
public final class MediaViewHolder implements Complication.ViewHolder {
    public final FrameLayout mContainer;
    public final ComplicationLayoutParams mLayoutParams;

    public MediaViewHolder(FrameLayout frameLayout, MediaComplicationViewController mediaComplicationViewController, ComplicationLayoutParams complicationLayoutParams) {
        this.mContainer = frameLayout;
        mediaComplicationViewController.init();
        this.mLayoutParams = complicationLayoutParams;
    }

    @Override // com.android.systemui.dreams.complication.Complication.ViewHolder
    public final ComplicationLayoutParams getLayoutParams() {
        return this.mLayoutParams;
    }

    @Override // com.android.systemui.dreams.complication.Complication.ViewHolder
    public final View getView() {
        return this.mContainer;
    }
}
