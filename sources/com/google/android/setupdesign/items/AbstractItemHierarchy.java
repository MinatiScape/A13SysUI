package com.google.android.setupdesign.items;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline1;
import com.google.android.setupdesign.R$styleable;
import com.google.android.setupdesign.items.ItemHierarchy;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes.dex */
public abstract class AbstractItemHierarchy implements ItemHierarchy {
    public int id;
    public final ArrayList<ItemHierarchy.Observer> observers;

    public AbstractItemHierarchy() {
        this.observers = new ArrayList<>();
        this.id = -1;
    }

    public final void notifyItemRangeChanged(int i, int i2) {
        if (i < 0) {
            GridLayoutManager$$ExternalSyntheticOutline1.m("notifyItemRangeChanged: Invalid position=", i, "AbstractItemHierarchy");
            return;
        }
        Iterator<ItemHierarchy.Observer> it = this.observers.iterator();
        while (it.hasNext()) {
            it.next().onItemRangeChanged(this, i);
        }
    }

    public final void notifyItemRangeInserted(int i, int i2) {
        if (i < 0) {
            GridLayoutManager$$ExternalSyntheticOutline1.m("notifyItemRangeInserted: Invalid position=", i, "AbstractItemHierarchy");
        } else if (i2 < 0) {
            GridLayoutManager$$ExternalSyntheticOutline1.m("notifyItemRangeInserted: Invalid itemCount=", i2, "AbstractItemHierarchy");
        } else {
            Iterator<ItemHierarchy.Observer> it = this.observers.iterator();
            while (it.hasNext()) {
                it.next().onItemRangeInserted(this, i, i2);
            }
        }
    }

    @Override // com.google.android.setupdesign.items.ItemHierarchy
    public final void registerObserver(ItemHierarchy.Observer observer) {
        this.observers.add(observer);
    }

    public AbstractItemHierarchy(Context context, AttributeSet attributeSet) {
        this.observers = new ArrayList<>();
        this.id = -1;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.SudAbstractItem);
        this.id = obtainStyledAttributes.getResourceId(0, -1);
        obtainStyledAttributes.recycle();
    }
}
