package com.android.systemui.flags;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;
/* compiled from: Flag.kt */
/* loaded from: classes.dex */
public final class BooleanFlag implements ParcelableFlag<Boolean> {
    public static final Parcelable.Creator<BooleanFlag> CREATOR = new Parcelable.Creator<BooleanFlag>() { // from class: com.android.systemui.flags.BooleanFlag$Companion$CREATOR$1
        @Override // android.os.Parcelable.Creator
        public final BooleanFlag createFromParcel(Parcel parcel) {
            return new BooleanFlag(parcel.readInt(), parcel.readBoolean());
        }

        @Override // android.os.Parcelable.Creator
        public final BooleanFlag[] newArray(int i) {
            return new BooleanFlag[i];
        }
    };

    /* renamed from: default  reason: not valid java name */
    public final boolean f0default;
    public final int id;

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BooleanFlag)) {
            return false;
        }
        BooleanFlag booleanFlag = (BooleanFlag) obj;
        int i = this.id;
        Objects.requireNonNull(booleanFlag);
        return i == booleanFlag.id && getDefault().booleanValue() == booleanFlag.getDefault().booleanValue();
    }

    public final Boolean getDefault() {
        return Boolean.valueOf(this.f0default);
    }

    public final int hashCode() {
        return getDefault().hashCode() + (Integer.hashCode(this.id) * 31);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("BooleanFlag(id=");
        m.append(this.id);
        m.append(", default=");
        m.append(getDefault().booleanValue());
        m.append(')');
        return m.toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeBoolean(getDefault().booleanValue());
    }

    public BooleanFlag(int i, boolean z) {
        this.id = i;
        this.f0default = z;
    }
}
