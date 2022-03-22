package android.hardware.common;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
/* loaded from: classes.dex */
public class Ashmem implements Parcelable {
    public static final Parcelable.Creator<Ashmem> CREATOR = new Parcelable.Creator<Ashmem>() { // from class: android.hardware.common.Ashmem.1
        @Override // android.os.Parcelable.Creator
        public final Ashmem createFromParcel(Parcel parcel) {
            Ashmem ashmem = new Ashmem();
            int dataPosition = parcel.dataPosition();
            int readInt = parcel.readInt();
            try {
                if (readInt >= 4) {
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        ashmem.fd = (ParcelFileDescriptor) parcel.readTypedObject(ParcelFileDescriptor.CREATOR);
                        if (parcel.dataPosition() - dataPosition < readInt) {
                            ashmem.size = parcel.readLong();
                            if (dataPosition > Integer.MAX_VALUE - readInt) {
                                throw new BadParcelableException("Overflow in the size of parcelable");
                            }
                        } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                            throw new BadParcelableException("Overflow in the size of parcelable");
                        }
                    } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                        throw new BadParcelableException("Overflow in the size of parcelable");
                    }
                    parcel.setDataPosition(dataPosition + readInt);
                    return ashmem;
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
        public final Ashmem[] newArray(int i) {
            return new Ashmem[i];
        }
    };
    public ParcelFileDescriptor fd;
    public long size = 0;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final int describeContents() {
        int i;
        ParcelFileDescriptor parcelFileDescriptor = this.fd;
        if (parcelFileDescriptor == null) {
            i = 0;
        } else {
            i = parcelFileDescriptor.describeContents();
        }
        return i | 0;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeTypedObject(this.fd, 0);
        parcel.writeLong(this.size);
        int dataPosition2 = parcel.dataPosition();
        parcel.setDataPosition(dataPosition);
        parcel.writeInt(dataPosition2 - dataPosition);
        parcel.setDataPosition(dataPosition2);
    }
}
