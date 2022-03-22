package com.google.android.setupdesign.items;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import com.google.android.setupdesign.items.ItemHierarchy;
import com.google.android.setupdesign.items.ItemInflater;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class ItemGroup extends AbstractItemHierarchy implements ItemInflater.ItemParent, ItemHierarchy.Observer {
    public final ArrayList children = new ArrayList();
    public final SparseIntArray hierarchyStart = new SparseIntArray();
    public int count = 0;
    public boolean dirty = false;

    public ItemGroup() {
    }

    @Override // com.google.android.setupdesign.items.ItemInflater.ItemParent
    public final void addChild(ItemHierarchy itemHierarchy) {
        this.dirty = true;
        this.children.add(itemHierarchy);
        itemHierarchy.registerObserver(this);
        int count = itemHierarchy.getCount();
        if (count > 0) {
            notifyItemRangeInserted(getChildPosition(itemHierarchy), count);
        }
    }

    @Override // com.google.android.setupdesign.items.ItemHierarchy.Observer
    public final void onItemRangeInserted(ItemHierarchy itemHierarchy, int i, int i2) {
        this.dirty = true;
        int childPosition = getChildPosition(itemHierarchy);
        if (childPosition >= 0) {
            notifyItemRangeInserted(childPosition + i, i2);
            return;
        }
        Log.e("ItemGroup", "Unexpected child insert " + itemHierarchy);
    }

    public final int getChildPosition(ItemHierarchy itemHierarchy) {
        ArrayList arrayList = this.children;
        int size = arrayList.size();
        int i = 0;
        while (true) {
            if (i >= size) {
                i = -1;
                break;
            } else if (arrayList.get(i) == itemHierarchy) {
                break;
            } else {
                i++;
            }
        }
        updateDataIfNeeded();
        if (i == -1) {
            return -1;
        }
        int size2 = this.children.size();
        int i2 = -1;
        while (i2 < 0 && i < size2) {
            i2 = this.hierarchyStart.get(i, -1);
            i++;
        }
        if (i2 >= 0) {
            return i2;
        }
        updateDataIfNeeded();
        return this.count;
    }

    public final void updateDataIfNeeded() {
        if (this.dirty) {
            this.count = 0;
            this.hierarchyStart.clear();
            for (int i = 0; i < this.children.size(); i++) {
                ItemHierarchy itemHierarchy = (ItemHierarchy) this.children.get(i);
                if (itemHierarchy.getCount() > 0) {
                    this.hierarchyStart.put(i, this.count);
                }
                this.count = itemHierarchy.getCount() + this.count;
            }
            this.dirty = false;
        }
    }

    @Override // com.google.android.setupdesign.items.ItemHierarchy
    public final int getCount() {
        updateDataIfNeeded();
        return this.count;
    }

    @Override // com.google.android.setupdesign.items.ItemHierarchy
    public final AbstractItem getItemAt(int i) {
        int keyAt;
        updateDataIfNeeded();
        if (i < 0 || i >= this.count) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("size=");
            m.append(this.count);
            m.append("; index=");
            m.append(i);
            throw new IndexOutOfBoundsException(m.toString());
        }
        SparseIntArray sparseIntArray = this.hierarchyStart;
        int size = sparseIntArray.size() - 1;
        int i2 = 0;
        while (true) {
            if (i2 <= size) {
                int i3 = (i2 + size) >>> 1;
                int valueAt = sparseIntArray.valueAt(i3);
                if (valueAt >= i) {
                    if (valueAt <= i) {
                        keyAt = sparseIntArray.keyAt(i3);
                        break;
                    }
                    size = i3 - 1;
                } else {
                    i2 = i3 + 1;
                }
            } else {
                keyAt = sparseIntArray.keyAt(i2 - 1);
                break;
            }
        }
        if (keyAt >= 0) {
            return ((ItemHierarchy) this.children.get(keyAt)).getItemAt(i - this.hierarchyStart.get(keyAt));
        }
        throw new IllegalStateException("Cannot have item start index < 0");
    }

    @Override // com.google.android.setupdesign.items.ItemHierarchy.Observer
    public final void onItemRangeChanged(ItemHierarchy itemHierarchy, int i) {
        int childPosition = getChildPosition(itemHierarchy);
        if (childPosition >= 0) {
            notifyItemRangeChanged(childPosition + i, 1);
            return;
        }
        Log.e("ItemGroup", "Unexpected child change " + itemHierarchy);
    }

    public ItemGroup(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
