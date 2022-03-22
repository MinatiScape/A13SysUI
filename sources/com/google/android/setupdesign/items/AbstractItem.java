package com.google.android.setupdesign.items;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
/* loaded from: classes.dex */
public abstract class AbstractItem extends AbstractItemHierarchy {
    public AbstractItem() {
    }

    @Override // com.google.android.setupdesign.items.ItemHierarchy
    public int getCount() {
        return 1;
    }

    @Override // com.google.android.setupdesign.items.ItemHierarchy
    public final AbstractItem getItemAt(int i) {
        return this;
    }

    public abstract int getLayoutResource();

    public abstract boolean isEnabled();

    public abstract void onBindView(View view);

    public AbstractItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
