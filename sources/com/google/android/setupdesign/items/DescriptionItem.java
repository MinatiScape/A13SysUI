package com.google.android.setupdesign.items;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
@Deprecated
/* loaded from: classes.dex */
public class DescriptionItem extends Item {
    public DescriptionItem() {
    }

    public DescriptionItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.google.android.setupdesign.items.Item, com.google.android.setupdesign.items.AbstractItem
    public final void onBindView(View view) {
        super.onBindView(view);
        TextView textView = (TextView) view.findViewById(2131428969);
    }
}
