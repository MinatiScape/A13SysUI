package android.hardware.common;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
/* loaded from: classes.dex */
public class NativeHandle implements Parcelable {
    public static final Parcelable.Creator<NativeHandle> CREATOR = new Parcelable.Creator<NativeHandle>() { // from class: android.hardware.common.NativeHandle.1
        @Override // android.os.Parcelable.Creator
        public final NativeHandle createFromParcel(Parcel parcel) {
            NativeHandle nativeHandle = new NativeHandle();
            int dataPosition = parcel.dataPosition();
            int readInt = parcel.readInt();
            try {
                if (readInt >= 4) {
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        nativeHandle.fds = (ParcelFileDescriptor[]) parcel.createTypedArray(ParcelFileDescriptor.CREATOR);
                        if (parcel.dataPosition() - dataPosition < readInt) {
                            nativeHandle.ints = parcel.createIntArray();
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
                    return nativeHandle;
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
        public final NativeHandle[] newArray(int i) {
            return new NativeHandle[i];
        }
    };
    public ParcelFileDescriptor[] fds;
    public int[] ints;

    @Override // android.os.Parcelable
    public final int describeContents() {
        return describeContents(this.fds) | 0;
    }

    public final int getStability() {
        return 1;
    }

    public static int describeContents(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Object[]) {
            int i = 0;
            for (Object obj2 : (Object[]) obj) {
                i |= describeContents(obj2);
            }
            return i;
        } else if (obj instanceof Parcelable) {
            return ((Parcelable) obj).describeContents();
        } else {
            return 0;
        }
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeTypedArray(this.fds, 0);
        parcel.writeIntArray(this.ints);
        int dataPosition2 = parcel.dataPosition();
        parcel.setDataPosition(dataPosition);
        parcel.writeInt(dataPosition2 - dataPosition);
        parcel.setDataPosition(dataPosition2);
    }
}
