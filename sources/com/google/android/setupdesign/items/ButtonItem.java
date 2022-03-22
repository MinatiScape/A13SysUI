package com.google.android.setupdesign.items;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import com.google.android.setupdesign.R$styleable;
/* loaded from: classes.dex */
public class ButtonItem extends AbstractItem implements View.OnClickListener {
    public Button button;
    public boolean enabled;
    public CharSequence text;
    public int theme;

    public ButtonItem() {
        this.enabled = true;
        this.theme = 2132017696;
    }

    @Override // com.google.android.setupdesign.items.AbstractItem, com.google.android.setupdesign.items.ItemHierarchy
    public final int getCount() {
        return 0;
    }

    @Override // com.google.android.setupdesign.items.AbstractItem
    public final int getLayoutResource() {
        return 0;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
    }

    @Override // com.google.android.setupdesign.items.AbstractItem
    public final void onBindView(View view) {
        throw new UnsupportedOperationException("Cannot bind to ButtonItem's view");
    }

    public ButtonItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.enabled = true;
        this.theme = 2132017696;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.SudButtonItem);
        this.enabled = obtainStyledAttributes.getBoolean(1, true);
        this.text = obtainStyledAttributes.getText(3);
        this.theme = obtainStyledAttributes.getResourceId(0, 2132017696);
        obtainStyledAttributes.recycle();
    }

    @Override // com.google.android.setupdesign.items.AbstractItem
    public final boolean isEnabled() {
        return this.enabled;
    }
}
