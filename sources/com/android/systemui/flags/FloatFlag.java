package com.android.systemui.flags;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Flag.kt */
/* loaded from: classes.dex */
public final class FloatFlag implements ParcelableFlag<Float> {
    public static final Parcelable.Creator<FloatFlag> CREATOR = new Parcelable.Creator<FloatFlag>() { // from class: com.android.systemui.flags.FloatFlag$Companion$CREATOR$1
        @Override // android.os.Parcelable.Creator
        public final FloatFlag createFromParcel(Parcel parcel) {
            return new FloatFlag(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public final FloatFlag[] newArray(int i) {
            return new FloatFlag[i];
        }
    };

    /* renamed from: default  reason: not valid java name */
    public final float f2default;
    public final int id;

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FloatFlag)) {
            return false;
        }
        FloatFlag floatFlag = (FloatFlag) obj;
        int i = this.id;
        Objects.requireNonNull(floatFlag);
        return i == floatFlag.id && Intrinsics.areEqual(Float.valueOf(this.f2default), Float.valueOf(floatFlag.f2default));
    }

    public final int hashCode() {
        return Float.valueOf(this.f2default).hashCode() + (Integer.hashCode(this.id) * 31);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("FloatFlag(id=");
        m.append(this.id);
        m.append(", default=");
        m.append(Float.valueOf(this.f2default).floatValue());
        m.append(')');
        return m.toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeFloat(Float.valueOf(this.f2default).floatValue());
    }

    public FloatFlag(Parcel parcel) {
        int readInt = parcel.readInt();
        float readFloat = parcel.readFloat();
        this.id = readInt;
        this.f2default = readFloat;
    }
}
