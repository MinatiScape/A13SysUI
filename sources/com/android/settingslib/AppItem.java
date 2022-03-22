package com.android.settingslib;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseBooleanArray;
import java.util.Objects;
/* loaded from: classes.dex */
public class AppItem implements Comparable<AppItem>, Parcelable {
    public static final Parcelable.Creator<AppItem> CREATOR = new Parcelable.Creator<AppItem>() { // from class: com.android.settingslib.AppItem.1
        @Override // android.os.Parcelable.Creator
        public final AppItem createFromParcel(Parcel parcel) {
            return new AppItem(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public final AppItem[] newArray(int i) {
            return new AppItem[i];
        }
    };
    public final int key;
    public long total;
    public SparseBooleanArray uids;

    public AppItem() {
        this.uids = new SparseBooleanArray();
        this.key = 0;
    }

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    @Override // java.lang.Comparable
    public final int compareTo(AppItem appItem) {
        AppItem appItem2 = appItem;
        Objects.requireNonNull(appItem2);
        int compare = Integer.compare(0, 0);
        if (compare == 0) {
            return Long.compare(appItem2.total, this.total);
        }
        return compare;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.key);
        parcel.writeSparseBooleanArray(this.uids);
        parcel.writeLong(this.total);
    }

    public AppItem(Parcel parcel) {
        this.uids = new SparseBooleanArray();
        this.key = parcel.readInt();
        this.uids = parcel.readSparseBooleanArray();
        this.total = parcel.readLong();
    }
}
