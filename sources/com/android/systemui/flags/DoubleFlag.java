package com.android.systemui.flags;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Flag.kt */
/* loaded from: classes.dex */
public final class DoubleFlag implements ParcelableFlag<Double> {
    public static final Parcelable.Creator<DoubleFlag> CREATOR = new Parcelable.Creator<DoubleFlag>() { // from class: com.android.systemui.flags.DoubleFlag$Companion$CREATOR$1
        @Override // android.os.Parcelable.Creator
        public final DoubleFlag createFromParcel(Parcel parcel) {
            return new DoubleFlag(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public final DoubleFlag[] newArray(int i) {
            return new DoubleFlag[i];
        }
    };

    /* renamed from: default  reason: not valid java name */
    public final double f1default;
    public final int id;

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DoubleFlag)) {
            return false;
        }
        DoubleFlag doubleFlag = (DoubleFlag) obj;
        int i = this.id;
        Objects.requireNonNull(doubleFlag);
        return i == doubleFlag.id && Intrinsics.areEqual(Double.valueOf(this.f1default), Double.valueOf(doubleFlag.f1default));
    }

    public final int hashCode() {
        return Double.valueOf(this.f1default).hashCode() + (Integer.hashCode(this.id) * 31);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("DoubleFlag(id=");
        m.append(this.id);
        m.append(", default=");
        m.append(Double.valueOf(this.f1default).doubleValue());
        m.append(')');
        return m.toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeDouble(Double.valueOf(this.f1default).doubleValue());
    }

    public DoubleFlag(Parcel parcel) {
        int readInt = parcel.readInt();
        double readDouble = parcel.readDouble();
        this.id = readInt;
        this.f1default = readDouble;
    }
}
