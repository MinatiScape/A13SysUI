package com.google.android.setupcompat.portal;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.systemui.R$array;
import java.util.Objects;
/* loaded from: classes.dex */
public class ProgressServiceComponent implements Parcelable {
    public static final Parcelable.Creator<ProgressServiceComponent> CREATOR = new Parcelable.Creator<ProgressServiceComponent>() { // from class: com.google.android.setupcompat.portal.ProgressServiceComponent.1
        @Override // android.os.Parcelable.Creator
        public final ProgressServiceComponent createFromParcel(Parcel parcel) {
            boolean z;
            boolean z2;
            boolean z3;
            String readString = parcel.readString();
            String readString2 = parcel.readString();
            boolean z4 = false;
            if (parcel.readInt() == 1) {
                z = true;
            } else {
                z = false;
            }
            int readInt = parcel.readInt();
            int readInt2 = parcel.readInt();
            Intent intent = (Intent) parcel.readParcelable(Intent.class.getClassLoader());
            Intent intent2 = (Intent) parcel.readParcelable(Intent.class.getClassLoader());
            if (parcel.readInt() == 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            long readLong = parcel.readLong();
            Objects.requireNonNull(readString, "packageName cannot be null.");
            Objects.requireNonNull(readString2, "serviceClass cannot be null.");
            Objects.requireNonNull(intent, "Service intent cannot be null.");
            Objects.requireNonNull(intent2, "Item click intent cannot be null");
            if (!z) {
                if (readInt != 0) {
                    z3 = true;
                } else {
                    z3 = false;
                }
                R$array.checkArgument(z3, "Invalidate resource id of display name");
                if (readInt2 != 0) {
                    z4 = true;
                }
                R$array.checkArgument(z4, "Invalidate resource id of display icon");
            }
            return new ProgressServiceComponent(readString, readString2, z, z2, readLong, readInt, readInt2, intent, intent2);
        }

        @Override // android.os.Parcelable.Creator
        public final ProgressServiceComponent[] newArray(int i) {
            return new ProgressServiceComponent[i];
        }
    };
    public final boolean autoRebind;
    public final int displayIconResId;
    public final int displayNameResId;
    public final boolean isSilent;
    public final Intent itemClickIntent;
    public final String packageName;
    public final Intent serviceIntent;
    public final String taskName;
    public final long timeoutForReRegister;

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.packageName);
        parcel.writeString(this.taskName);
        parcel.writeInt(this.isSilent ? 1 : 0);
        parcel.writeInt(this.displayNameResId);
        parcel.writeInt(this.displayIconResId);
        parcel.writeParcelable(this.serviceIntent, 0);
        parcel.writeParcelable(this.itemClickIntent, 0);
        parcel.writeInt(this.autoRebind ? 1 : 0);
        parcel.writeLong(this.timeoutForReRegister);
    }

    public ProgressServiceComponent(String str, String str2, boolean z, boolean z2, long j, int i, int i2, Intent intent, Intent intent2) {
        this.packageName = str;
        this.taskName = str2;
        this.isSilent = z;
        this.autoRebind = z2;
        this.timeoutForReRegister = j;
        this.displayNameResId = i;
        this.displayIconResId = i2;
        this.serviceIntent = intent;
        this.itemClickIntent = intent2;
    }
}
