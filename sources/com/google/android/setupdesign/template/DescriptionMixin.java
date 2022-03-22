package com.google.android.setupdesign.template;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.template.Mixin;
import com.google.android.setupdesign.R$styleable;
/* loaded from: classes.dex */
public final class DescriptionMixin implements Mixin {
    public final TemplateLayout templateLayout;

    public DescriptionMixin(TemplateLayout templateLayout, AttributeSet attributeSet, int i) {
        TextView textView;
        TextView textView2;
        this.templateLayout = templateLayout;
        TypedArray obtainStyledAttributes = templateLayout.getContext().obtainStyledAttributes(attributeSet, R$styleable.SudDescriptionMixin, i, 0);
        CharSequence text = obtainStyledAttributes.getText(0);
        if (!(text == null || (textView2 = (TextView) templateLayout.findManagedViewById(2131428985)) == null)) {
            textView2.setText(text);
            TextView textView3 = (TextView) templateLayout.findManagedViewById(2131428985);
            if (textView3 != null) {
                textView3.setVisibility(0);
            }
        }
        ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(1);
        if (!(colorStateList == null || (textView = (TextView) templateLayout.findManagedViewById(2131428985)) == null)) {
            textView.setTextColor(colorStateList);
        }
        obtainStyledAttributes.recycle();
    }
}
