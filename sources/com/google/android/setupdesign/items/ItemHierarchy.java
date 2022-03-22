package com.google.android.setupdesign.items;
/* loaded from: classes.dex */
public interface ItemHierarchy {

    /* loaded from: classes.dex */
    public interface Observer {
        void onItemRangeChanged(ItemHierarchy itemHierarchy, int i);

        void onItemRangeInserted(ItemHierarchy itemHierarchy, int i, int i2);
    }

    int getCount();

    AbstractItem getItemAt(int i);

    void registerObserver(Observer observer);
}
