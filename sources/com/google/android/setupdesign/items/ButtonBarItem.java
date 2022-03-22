package com.google.android.setupdesign.items;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import com.google.android.setupdesign.items.ItemInflater;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public class ButtonBarItem extends AbstractItem implements ItemInflater.ItemParent {
    public final ArrayList<ButtonItem> buttons = new ArrayList<>();
    public boolean visible = true;

    public ButtonBarItem() {
    }

    @Override // com.google.android.setupdesign.items.AbstractItem
    public final int getLayoutResource() {
        return 2131624557;
    }

    @Override // com.google.android.setupdesign.items.AbstractItem
    public final boolean isEnabled() {
        return false;
    }

    @Override // com.google.android.setupdesign.items.AbstractItem
    public final void onBindView(View view) {
        LinearLayout linearLayout = (LinearLayout) view;
        linearLayout.removeAllViews();
        Iterator<ButtonItem> it = this.buttons.iterator();
        while (it.hasNext()) {
            ButtonItem next = it.next();
            Objects.requireNonNull(next);
            Button button = next.button;
            if (button == null) {
                Context context = linearLayout.getContext();
                if (next.theme != 0) {
                    context = new ContextThemeWrapper(context, next.theme);
                }
                Button button2 = (Button) LayoutInflater.from(context).inflate(2131624531, (ViewGroup) null, false);
                next.button = button2;
                button2.setOnClickListener(next);
            } else if (button.getParent() instanceof ViewGroup) {
                ((ViewGroup) next.button.getParent()).removeView(next.button);
            }
            next.button.setEnabled(next.enabled);
            next.button.setText(next.text);
            next.button.setId(next.id);
            linearLayout.addView(next.button);
        }
        view.setId(this.id);
    }

    @Override // com.google.android.setupdesign.items.ItemInflater.ItemParent
    public final void addChild(ItemHierarchy itemHierarchy) {
        if (itemHierarchy instanceof ButtonItem) {
            this.buttons.add((ButtonItem) itemHierarchy);
            return;
        }
        throw new UnsupportedOperationException("Cannot add non-button item to Button Bar");
    }

    public ButtonBarItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.google.android.setupdesign.items.AbstractItem, com.google.android.setupdesign.items.ItemHierarchy
    public final int getCount() {
        return this.visible ? 1 : 0;
    }
}
