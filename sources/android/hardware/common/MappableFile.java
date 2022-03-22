package android.hardware.common;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
/* loaded from: classes.dex */
public class MappableFile implements Parcelable {
    public static final Parcelable.Creator<MappableFile> CREATOR = new Parcelable.Creator<MappableFile>() { // from class: android.hardware.common.MappableFile.1
        @Override // android.os.Parcelable.Creator
        public final MappableFile createFromParcel(Parcel parcel) {
            MappableFile mappableFile = new MappableFile();
            int dataPosition = parcel.dataPosition();
            int readInt = parcel.readInt();
            try {
                if (readInt >= 4) {
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        mappableFile.length = parcel.readLong();
                        if (parcel.dataPosition() - dataPosition < readInt) {
                            mappableFile.prot = parcel.readInt();
                            if (parcel.dataPosition() - dataPosition < readInt) {
                                mappableFile.fd = (ParcelFileDescriptor) parcel.readTypedObject(ParcelFileDescriptor.CREATOR);
                                if (parcel.dataPosition() - dataPosition < readInt) {
                                    mappableFile.offset = parcel.readLong();
                                    if (dataPosition > Integer.MAX_VALUE - readInt) {
                                        throw new BadParcelableException("Overflow in the size of parcelable");
                                    }
                                } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                    throw new BadParcelableException("Overflow in the size of parcelable");
                                }
                            } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                throw new BadParcelableException("Overflow in the size of parcelable");
                            }
                        } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                            throw new BadParcelableException("Overflow in the size of parcelable");
                        }
                    } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                        throw new BadParcelableException("Overflow in the size of parcelable");
                    }
                    parcel.setDataPosition(dataPosition + readInt);
                    return mappableFile;
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
        public final MappableFile[] newArray(int i) {
            return new MappableFile[i];
        }
    };
    public ParcelFileDescriptor fd;
    public long length = 0;
    public int prot = 0;
    public long offset = 0;

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
        parcel.writeLong(this.length);
        parcel.writeInt(this.prot);
        parcel.writeTypedObject(this.fd, 0);
        parcel.writeLong(this.offset);
        int dataPosition2 = parcel.dataPosition();
        parcel.setDataPosition(dataPosition);
        parcel.writeInt(dataPosition2 - dataPosition);
        parcel.setDataPosition(dataPosition2);
    }
}
