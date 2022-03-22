package com.google.android.setupcompat.portal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public class NotificationComponent implements Parcelable {
    public static final Parcelable.Creator<NotificationComponent> CREATOR = new Parcelable.Creator<NotificationComponent>() { // from class: com.google.android.setupcompat.portal.NotificationComponent.1
        @Override // android.os.Parcelable.Creator
        public final NotificationComponent createFromParcel(Parcel parcel) {
            return new NotificationComponent(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public final NotificationComponent[] newArray(int i) {
            return new NotificationComponent[i];
        }
    };
    public Bundle extraBundle;
    public final int notificationType;

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.notificationType);
        parcel.writeBundle(this.extraBundle);
    }

    public NotificationComponent(Parcel parcel) {
        int readInt = parcel.readInt();
        this.extraBundle = new Bundle();
        this.notificationType = readInt;
        this.extraBundle = parcel.readBundle(Bundle.class.getClassLoader());
    }
}
