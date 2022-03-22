package com.android.settingslib.drawer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.UserHandle;
import java.util.ArrayList;
/* loaded from: classes.dex */
public abstract class Tile implements Parcelable {
    public static final Parcelable.Creator<Tile> CREATOR = new Parcelable.Creator<Tile>() { // from class: com.android.settingslib.drawer.Tile.1
        @Override // android.os.Parcelable.Creator
        public final Tile createFromParcel(Parcel parcel) {
            boolean readBoolean = parcel.readBoolean();
            parcel.setDataPosition(0);
            if (!readBoolean) {
                return new ActivityTile(parcel);
            }
            new ProviderTile(parcel);
            throw null;
        }

        @Override // android.os.Parcelable.Creator
        public final Tile[] newArray(int i) {
            return new Tile[i];
        }
    };
    public String mCategory;
    public final String mComponentName;
    public final String mComponentPackage;
    public final Intent mIntent;
    public long mLastUpdateTime;
    public Bundle mMetaData;
    public ArrayList<UserHandle> userHandle = new ArrayList<>();

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeBoolean(this instanceof ProviderTile);
        parcel.writeString(this.mComponentPackage);
        parcel.writeString(this.mComponentName);
        int size = this.userHandle.size();
        parcel.writeInt(size);
        for (int i2 = 0; i2 < size; i2++) {
            this.userHandle.get(i2).writeToParcel(parcel, i);
        }
        parcel.writeString(this.mCategory);
        parcel.writeBundle(this.mMetaData);
    }

    public Tile(Parcel parcel) {
        String readString = parcel.readString();
        this.mComponentPackage = readString;
        String readString2 = parcel.readString();
        this.mComponentName = readString2;
        this.mIntent = new Intent().setClassName(readString, readString2);
        int readInt = parcel.readInt();
        boolean z = false;
        for (int i = 0; i < readInt; i++) {
            this.userHandle.add((UserHandle) UserHandle.CREATOR.createFromParcel(parcel));
        }
        this.mCategory = parcel.readString();
        Bundle readBundle = parcel.readBundle();
        this.mMetaData = readBundle;
        if (readBundle != null && readBundle.containsKey("com.android.settings.new_task")) {
            z = this.mMetaData.getBoolean("com.android.settings.new_task");
        }
        if (z) {
            this.mIntent.addFlags(268435456);
        }
    }
}
