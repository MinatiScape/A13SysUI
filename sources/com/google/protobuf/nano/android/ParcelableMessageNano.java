package com.google.protobuf.nano.android;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.protobuf.nano.MessageNano;
/* loaded from: classes.dex */
public abstract class ParcelableMessageNano extends MessageNano implements Parcelable {
    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getClass().getName());
        parcel.writeByteArray(MessageNano.toByteArray(this));
    }
}
