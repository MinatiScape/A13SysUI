package androidx.slice.core;

import android.text.TextUtils;
import androidx.slice.Slice;
import androidx.slice.SliceItem;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SliceQuery {

    /* loaded from: classes.dex */
    public interface Filter<T> {
        boolean filter(SliceItem sliceItem);
    }

    public static SliceItem find(Slice slice, String str, String str2) {
        return find(slice, str, new String[]{str2}, new String[]{null});
    }

    public static ArrayList findAll(SliceItem sliceItem, final String str, final String[] strArr, final String[] strArr2) {
        ArrayList arrayList = new ArrayList();
        ArrayDeque arrayDeque = new ArrayDeque();
        arrayDeque.add(sliceItem);
        findAll(arrayDeque, new Filter<SliceItem>() { // from class: androidx.slice.core.SliceQuery.3
            @Override // androidx.slice.core.SliceQuery.Filter
            public final boolean filter(SliceItem sliceItem2) {
                if (!SliceQuery.checkFormat(sliceItem2, str) || !SliceQuery.hasHints(sliceItem2, strArr) || SliceQuery.hasAnyHints(sliceItem2, strArr2)) {
                    return false;
                }
                return true;
            }
        }, arrayList);
        return arrayList;
    }

    public static SliceItem findSubtype(Slice slice, final String str, final String str2) {
        if (slice == null) {
            return null;
        }
        ArrayDeque arrayDeque = new ArrayDeque();
        Collections.addAll(arrayDeque, slice.mItems);
        return findSliceItem(arrayDeque, new Filter<SliceItem>() { // from class: androidx.slice.core.SliceQuery.5
            @Override // androidx.slice.core.SliceQuery.Filter
            public final boolean filter(SliceItem sliceItem) {
                if (!SliceQuery.checkFormat(sliceItem, str) || !SliceQuery.checkSubtype(sliceItem, str2)) {
                    return false;
                }
                return true;
            }
        });
    }

    public static boolean hasAnyHints(SliceItem sliceItem, String... strArr) {
        if (strArr == null) {
            return false;
        }
        for (String str : strArr) {
            if (sliceItem.hasHint(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasHints(SliceItem sliceItem, String... strArr) {
        if (strArr == null) {
            return true;
        }
        for (String str : strArr) {
            if (!(TextUtils.isEmpty(str) || sliceItem.hasHint(str))) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkFormat(SliceItem sliceItem, String str) {
        if (str != null) {
            Objects.requireNonNull(sliceItem);
            if (!str.equals(sliceItem.mFormat)) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkSubtype(SliceItem sliceItem, String str) {
        if (str != null) {
            Objects.requireNonNull(sliceItem);
            if (!str.equals(sliceItem.mSubType)) {
                return false;
            }
        }
        return true;
    }

    public static SliceItem find(SliceItem sliceItem, String str, String str2) {
        return find(sliceItem, str, new String[]{str2}, new String[]{null});
    }

    public static SliceItem find(Slice slice, final String str, final String[] strArr, final String[] strArr2) {
        if (slice == null) {
            return null;
        }
        ArrayDeque arrayDeque = new ArrayDeque();
        Collections.addAll(arrayDeque, slice.mItems);
        return findSliceItem(arrayDeque, new Filter<SliceItem>() { // from class: androidx.slice.core.SliceQuery.4
            @Override // androidx.slice.core.SliceQuery.Filter
            public final boolean filter(SliceItem sliceItem) {
                if (!SliceQuery.checkFormat(sliceItem, str) || !SliceQuery.hasHints(sliceItem, strArr) || SliceQuery.hasAnyHints(sliceItem, strArr2)) {
                    return false;
                }
                return true;
            }
        });
    }

    public static SliceItem findSliceItem(ArrayDeque arrayDeque, Filter filter) {
        while (!arrayDeque.isEmpty()) {
            SliceItem sliceItem = (SliceItem) arrayDeque.poll();
            if (filter.filter(sliceItem)) {
                return sliceItem;
            }
            Objects.requireNonNull(sliceItem);
            if ("slice".equals(sliceItem.mFormat) || "action".equals(sliceItem.mFormat)) {
                Slice slice = sliceItem.getSlice();
                Objects.requireNonNull(slice);
                Collections.addAll(arrayDeque, slice.mItems);
            }
        }
        return null;
    }

    public static SliceItem findTopLevelItem(Slice slice, String str, String str2, String[] strArr) {
        SliceItem[] sliceItemArr;
        Objects.requireNonNull(slice);
        for (SliceItem sliceItem : slice.mItems) {
            if (checkFormat(sliceItem, str) && checkSubtype(sliceItem, str2) && hasHints(sliceItem, strArr) && !hasAnyHints(sliceItem, null)) {
                return sliceItem;
            }
        }
        return null;
    }

    public static void findAll(ArrayDeque arrayDeque, Filter filter, ArrayList arrayList) {
        while (!arrayDeque.isEmpty()) {
            SliceItem sliceItem = (SliceItem) arrayDeque.poll();
            if (filter.filter(sliceItem)) {
                arrayList.add(sliceItem);
            }
            Objects.requireNonNull(sliceItem);
            if ("slice".equals(sliceItem.mFormat) || "action".equals(sliceItem.mFormat)) {
                Slice slice = sliceItem.getSlice();
                Objects.requireNonNull(slice);
                Collections.addAll(arrayDeque, slice.mItems);
            }
        }
    }

    public static SliceItem findSubtype(SliceItem sliceItem, final String str, final String str2) {
        if (sliceItem == null) {
            return null;
        }
        ArrayDeque arrayDeque = new ArrayDeque();
        arrayDeque.add(sliceItem);
        return findSliceItem(arrayDeque, new Filter<SliceItem>() { // from class: androidx.slice.core.SliceQuery.6
            @Override // androidx.slice.core.SliceQuery.Filter
            public final boolean filter(SliceItem sliceItem2) {
                if (!SliceQuery.checkFormat(sliceItem2, str) || !SliceQuery.checkSubtype(sliceItem2, str2)) {
                    return false;
                }
                return true;
            }
        });
    }

    public static SliceItem find(SliceItem sliceItem, final String str, final String[] strArr, final String[] strArr2) {
        if (sliceItem == null) {
            return null;
        }
        ArrayDeque arrayDeque = new ArrayDeque();
        arrayDeque.add(sliceItem);
        return findSliceItem(arrayDeque, new Filter<SliceItem>() { // from class: androidx.slice.core.SliceQuery.7
            @Override // androidx.slice.core.SliceQuery.Filter
            public final boolean filter(SliceItem sliceItem2) {
                if (!SliceQuery.checkFormat(sliceItem2, str) || !SliceQuery.hasHints(sliceItem2, strArr) || SliceQuery.hasAnyHints(sliceItem2, strArr2)) {
                    return false;
                }
                return true;
            }
        });
    }
}
