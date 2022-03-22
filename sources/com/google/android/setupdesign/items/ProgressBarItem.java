package com.google.android.setupdesign.items;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
/* loaded from: classes.dex */
public class ProgressBarItem extends Item {
    public ProgressBarItem() {
    }

    @Override // com.google.android.setupdesign.items.Item
    public final int getDefaultLayoutResource() {
        return 2131624561;
    }

    @Override // com.google.android.setupdesign.items.Item, com.google.android.setupdesign.items.AbstractItem
    public final boolean isEnabled() {
        return false;
    }

    @Override // com.google.android.setupdesign.items.Item, com.google.android.setupdesign.items.AbstractItem
    public final void onBindView(View view) {
    }

    public ProgressBarItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
