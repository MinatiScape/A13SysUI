package com.google.input;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public class ContextPacket implements Parcelable {
    public static final Parcelable.Creator<ContextPacket> CREATOR = new Parcelable.Creator<ContextPacket>() { // from class: com.google.input.ContextPacket.1
        @Override // android.os.Parcelable.Creator
        public final ContextPacket createFromParcel(Parcel parcel) {
            ContextPacket contextPacket = new ContextPacket();
            int dataPosition = parcel.dataPosition();
            int readInt = parcel.readInt();
            try {
                if (readInt >= 4) {
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        contextPacket.orientation = parcel.readByte();
                        if (dataPosition > Integer.MAX_VALUE - readInt) {
                            throw new BadParcelableException("Overflow in the size of parcelable");
                        }
                    } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                        throw new BadParcelableException("Overflow in the size of parcelable");
                    }
                    parcel.setDataPosition(dataPosition + readInt);
                    return contextPacket;
                }
                throw new BadParcelableException("Parcelable too small");
            } catch (Throwable th) {
                if (dataPosition > Integer.MAX_VALUE - readInt) {
                    throw new BadParcelableException("Overflow in the size of parcelable");
                }
                parcel.setDataPosition(dataPosition + readInt);
                throw th;
            }
        }

        @Override // android.os.Parcelable.Creator
        public final ContextPacket[] newArray(int i) {
            return new ContextPacket[i];
        }
    };
    public byte orientation;

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeByte(this.orientation);
        int dataPosition2 = parcel.dataPosition();
        parcel.setDataPosition(dataPosition);
        parcel.writeInt(dataPosition2 - dataPosition);
        parcel.setDataPosition(dataPosition2);
    }
}
