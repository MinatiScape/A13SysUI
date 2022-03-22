package com.android.systemui.flags;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;
/* compiled from: Flag.kt */
/* loaded from: classes.dex */
public final class LongFlag implements ParcelableFlag<Long> {
    public static final Parcelable.Creator<LongFlag> CREATOR = new Parcelable.Creator<LongFlag>() { // from class: com.android.systemui.flags.LongFlag$Companion$CREATOR$1
        @Override // android.os.Parcelable.Creator
        public final LongFlag createFromParcel(Parcel parcel) {
            return new LongFlag(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public final LongFlag[] newArray(int i) {
            return new LongFlag[i];
        }
    };

    /* renamed from: default  reason: not valid java name */
    public final long f4default;
    public final int id;

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LongFlag)) {
            return false;
        }
        LongFlag longFlag = (LongFlag) obj;
        int i = this.id;
        Objects.requireNonNull(longFlag);
        return i == longFlag.id && Long.valueOf(this.f4default).longValue() == Long.valueOf(longFlag.f4default).longValue();
    }

    public final int hashCode() {
        return Long.valueOf(this.f4default).hashCode() + (Integer.hashCode(this.id) * 31);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("LongFlag(id=");
        m.append(this.id);
        m.append(", default=");
        m.append(Long.valueOf(this.f4default).longValue());
        m.append(')');
        return m.toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeLong(Long.valueOf(this.f4default).longValue());
    }

    public LongFlag(Parcel parcel) {
        int readInt = parcel.readInt();
        long readLong = parcel.readLong();
        this.id = readInt;
        this.f4default = readLong;
    }
}
