package com.google.android.setupdesign.items;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.setupdesign.DividerItemDecoration;
/* loaded from: classes.dex */
public final class ItemViewHolder extends RecyclerView.ViewHolder implements DividerItemDecoration.DividedViewHolder {
    public boolean isEnabled;
    public AbstractItem item;

    @Override // com.google.android.setupdesign.DividerItemDecoration.DividedViewHolder
    public final boolean isDividerAllowedAbove() {
        AbstractItem abstractItem = this.item;
        if (abstractItem instanceof Dividable) {
            return ((Dividable) abstractItem).isDividerAllowedAbove();
        }
        return this.isEnabled;
    }

    @Override // com.google.android.setupdesign.DividerItemDecoration.DividedViewHolder
    public final boolean isDividerAllowedBelow() {
        AbstractItem abstractItem = this.item;
        if (abstractItem instanceof Dividable) {
            return ((Dividable) abstractItem).isDividerAllowedBelow();
        }
        return this.isEnabled;
    }

    public ItemViewHolder(View view) {
        super(view);
    }
}
