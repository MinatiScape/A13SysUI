package com.google.android.setupdesign.items;

import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.google.android.setupdesign.items.ItemHierarchy;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ItemAdapter extends BaseAdapter implements ItemHierarchy.Observer {
    public final ItemHierarchy itemHierarchy;
    public final ViewTypes viewTypes = new ViewTypes();

    /* loaded from: classes.dex */
    public static class ViewTypes {
        public final SparseIntArray positionMap = new SparseIntArray();
        public int nextPosition = 0;
    }

    @Override // android.widget.Adapter
    public final long getItemId(int i) {
        return i;
    }

    public final void refreshViewTypes() {
        for (int i = 0; i < getCount(); i++) {
            AbstractItem itemAt = this.itemHierarchy.getItemAt(i);
            ViewTypes viewTypes = this.viewTypes;
            int layoutResource = itemAt.getLayoutResource();
            Objects.requireNonNull(viewTypes);
            if (viewTypes.positionMap.indexOfKey(layoutResource) < 0) {
                viewTypes.positionMap.put(layoutResource, viewTypes.nextPosition);
                viewTypes.nextPosition++;
            }
            viewTypes.positionMap.get(layoutResource);
        }
    }

    @Override // android.widget.Adapter
    public final int getCount() {
        return this.itemHierarchy.getCount();
    }

    @Override // android.widget.Adapter
    public final Object getItem(int i) {
        return this.itemHierarchy.getItemAt(i);
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public final int getItemViewType(int i) {
        int layoutResource = this.itemHierarchy.getItemAt(i).getLayoutResource();
        ViewTypes viewTypes = this.viewTypes;
        Objects.requireNonNull(viewTypes);
        return viewTypes.positionMap.get(layoutResource);
    }

    @Override // android.widget.Adapter
    public final View getView(int i, View view, ViewGroup viewGroup) {
        AbstractItem itemAt = this.itemHierarchy.getItemAt(i);
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(itemAt.getLayoutResource(), viewGroup, false);
        }
        itemAt.onBindView(view);
        return view;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public final int getViewTypeCount() {
        ViewTypes viewTypes = this.viewTypes;
        Objects.requireNonNull(viewTypes);
        return viewTypes.positionMap.size();
    }

    @Override // android.widget.BaseAdapter, android.widget.ListAdapter
    public final boolean isEnabled(int i) {
        return this.itemHierarchy.getItemAt(i).isEnabled();
    }

    public ItemAdapter(ItemGroup itemGroup) {
        this.itemHierarchy = itemGroup;
        itemGroup.registerObserver(this);
        refreshViewTypes();
    }

    @Override // com.google.android.setupdesign.items.ItemHierarchy.Observer
    public final void onItemRangeChanged(ItemHierarchy itemHierarchy, int i) {
        refreshViewTypes();
        notifyDataSetChanged();
    }

    @Override // com.google.android.setupdesign.items.ItemHierarchy.Observer
    public final void onItemRangeInserted(ItemHierarchy itemHierarchy, int i, int i2) {
        refreshViewTypes();
        notifyDataSetChanged();
    }
}
