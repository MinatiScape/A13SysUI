package com.android.systemui.controls.management;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
/* compiled from: ControlAdapter.kt */
/* loaded from: classes.dex */
public abstract class Holder extends RecyclerView.ViewHolder {
    public abstract void bindData(ElementWrapper elementWrapper);

    public void updateFavorite(boolean z) {
    }

    public Holder(View view) {
        super(view);
    }
}
