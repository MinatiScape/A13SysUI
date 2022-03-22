package com.android.systemui.flags;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Flag.kt */
/* loaded from: classes.dex */
public final class StringFlag implements ParcelableFlag<String> {
    public static final Parcelable.Creator<StringFlag> CREATOR = new Parcelable.Creator<StringFlag>() { // from class: com.android.systemui.flags.StringFlag$Companion$CREATOR$1
        @Override // android.os.Parcelable.Creator
        public final StringFlag createFromParcel(Parcel parcel) {
            return new StringFlag(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public final StringFlag[] newArray(int i) {
            return new StringFlag[i];
        }
    };

    /* renamed from: default  reason: not valid java name */
    public final String f5default;
    public final int id;

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StringFlag)) {
            return false;
        }
        StringFlag stringFlag = (StringFlag) obj;
        int i = this.id;
        Objects.requireNonNull(stringFlag);
        return i == stringFlag.id && Intrinsics.areEqual(this.f5default, stringFlag.f5default);
    }

    public final int hashCode() {
        return this.f5default.hashCode() + (Integer.hashCode(this.id) * 31);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("StringFlag(id=");
        m.append(this.id);
        m.append(", default=");
        m.append(this.f5default);
        m.append(')');
        return m.toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.f5default);
    }

    public StringFlag(Parcel parcel) {
        int readInt = parcel.readInt();
        String readString = parcel.readString();
        readString = readString == null ? "" : readString;
        this.id = readInt;
        this.f5default = readString;
    }
}
