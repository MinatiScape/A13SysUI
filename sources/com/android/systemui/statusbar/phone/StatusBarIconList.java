package com.android.systemui.statusbar.phone;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public class StatusBarIconList {
    public ArrayList<Slot> mSlots = new ArrayList<>();

    /* loaded from: classes.dex */
    public static class Slot {
        public StatusBarIconHolder mHolder = null;
        public final String mName;
        public ArrayList<StatusBarIconHolder> mSubSlots;

        @VisibleForTesting
        public void clear() {
            this.mHolder = null;
            if (this.mSubSlots != null) {
                this.mSubSlots = null;
            }
        }

        public final int getIndexForTag(int i) {
            for (int i2 = 0; i2 < this.mSubSlots.size(); i2++) {
                StatusBarIconHolder statusBarIconHolder = this.mSubSlots.get(i2);
                Objects.requireNonNull(statusBarIconHolder);
                if (statusBarIconHolder.mTag == i) {
                    return i2;
                }
            }
            return -1;
        }

        public final String toString() {
            Object[] objArr = new Object[2];
            objArr[0] = this.mName;
            String str = "";
            if (this.mSubSlots != null) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m(str);
                m.append(this.mSubSlots.size());
                m.append(" subSlots");
                str = m.toString();
            }
            objArr[1] = str;
            return String.format("(%s) %s", objArr);
        }

        public final StatusBarIconHolder getHolderForTag(int i) {
            if (i == 0) {
                return this.mHolder;
            }
            ArrayList<StatusBarIconHolder> arrayList = this.mSubSlots;
            if (arrayList == null) {
                return null;
            }
            Iterator<StatusBarIconHolder> it = arrayList.iterator();
            while (it.hasNext()) {
                StatusBarIconHolder next = it.next();
                Objects.requireNonNull(next);
                if (next.mTag == i) {
                    return next;
                }
            }
            return null;
        }

        public final ArrayList getHolderListInViewOrder() {
            ArrayList arrayList = new ArrayList();
            ArrayList<StatusBarIconHolder> arrayList2 = this.mSubSlots;
            if (arrayList2 != null) {
                int size = arrayList2.size();
                while (true) {
                    size--;
                    if (size < 0) {
                        break;
                    }
                    arrayList.add(this.mSubSlots.get(size));
                }
            }
            StatusBarIconHolder statusBarIconHolder = this.mHolder;
            if (statusBarIconHolder != null) {
                arrayList.add(statusBarIconHolder);
            }
            return arrayList;
        }

        public final boolean hasIconsInSlot() {
            if (this.mHolder != null) {
                return true;
            }
            ArrayList<StatusBarIconHolder> arrayList = this.mSubSlots;
            if (arrayList != null && arrayList.size() > 0) {
                return true;
            }
            return false;
        }

        public Slot(String str) {
            this.mName = str;
        }
    }

    public final int getViewIndex(int i, int i2) {
        int i3;
        int i4 = 0;
        int i5 = 0;
        for (int i6 = 0; i6 < i; i6++) {
            Slot slot = this.mSlots.get(i6);
            if (slot.hasIconsInSlot()) {
                if (slot.mHolder == null) {
                    i3 = 0;
                } else {
                    i3 = 1;
                }
                ArrayList<StatusBarIconHolder> arrayList = slot.mSubSlots;
                if (arrayList != null) {
                    i3 += arrayList.size();
                }
                i5 += i3;
            }
        }
        Slot slot2 = this.mSlots.get(i);
        Objects.requireNonNull(slot2);
        ArrayList<StatusBarIconHolder> arrayList2 = slot2.mSubSlots;
        if (arrayList2 != null) {
            i4 = arrayList2.size();
            if (i2 != 0) {
                i4 = (i4 - slot2.getIndexForTag(i2)) - 1;
            }
        }
        return i5 + i4;
    }

    public final StatusBarIconHolder getIcon(int i, int i2) {
        return this.mSlots.get(i).getHolderForTag(i2);
    }

    public final Slot getSlot(String str) {
        return this.mSlots.get(getSlotIndex(str));
    }

    public final int getSlotIndex(String str) {
        int size = this.mSlots.size();
        for (int i = 0; i < size; i++) {
            Slot slot = this.mSlots.get(i);
            Objects.requireNonNull(slot);
            if (slot.mName.equals(str)) {
                return i;
            }
        }
        this.mSlots.add(0, new Slot(str));
        return 0;
    }

    public StatusBarIconList(String[] strArr) {
        for (String str : strArr) {
            this.mSlots.add(new Slot(str));
        }
    }
}
