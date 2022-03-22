package com.google.android.setupdesign.items;

import android.content.Context;
/* loaded from: classes.dex */
public final class ItemInflater extends ReflectionInflater<ItemHierarchy> {

    /* loaded from: classes.dex */
    public interface ItemParent {
        void addChild(ItemHierarchy itemHierarchy);
    }

    public ItemInflater(Context context) {
        super(context);
        this.defaultPackage = Item.class.getPackage().getName() + ".";
    }
}
