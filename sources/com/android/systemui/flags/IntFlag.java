package com.android.systemui.flags;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;
/* compiled from: Flag.kt */
/* loaded from: classes.dex */
public final class IntFlag implements ParcelableFlag<Integer> {
    public static final Parcelable.Creator<IntFlag> CREATOR = new Parcelable.Creator<IntFlag>() { // from class: com.android.systemui.flags.IntFlag$Companion$CREATOR$1
        @Override // android.os.Parcelable.Creator
        public final IntFlag createFromParcel(Parcel parcel) {
            return new IntFlag(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public final IntFlag[] newArray(int i) {
            return new IntFlag[i];
        }
    };

    /* renamed from: default  reason: not valid java name */
    public final int f3default;
    public final int id;

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof IntFlag)) {
            return false;
        }
        IntFlag intFlag = (IntFlag) obj;
        int i = this.id;
        Objects.requireNonNull(intFlag);
        return i == intFlag.id && Integer.valueOf(this.f3default).intValue() == Integer.valueOf(intFlag.f3default).intValue();
    }

    public final int hashCode() {
        return Integer.valueOf(this.f3default).hashCode() + (Integer.hashCode(this.id) * 31);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("IntFlag(id=");
        m.append(this.id);
        m.append(", default=");
        m.append(Integer.valueOf(this.f3default).intValue());
        m.append(')');
        return m.toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeInt(Integer.valueOf(this.f3default).intValue());
    }

    public IntFlag(Parcel parcel) {
        int readInt = parcel.readInt();
        int readInt2 = parcel.readInt();
        this.id = readInt;
        this.f3default = readInt2;
    }
}
