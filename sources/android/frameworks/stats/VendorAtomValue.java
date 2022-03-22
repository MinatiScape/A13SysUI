package android.frameworks.stats;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public final class VendorAtomValue implements Parcelable {
    public static final Parcelable.Creator<VendorAtomValue> CREATOR = new Parcelable.Creator<VendorAtomValue>() { // from class: android.frameworks.stats.VendorAtomValue.1
        @Override // android.os.Parcelable.Creator
        public final VendorAtomValue createFromParcel(Parcel parcel) {
            return new VendorAtomValue(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public final VendorAtomValue[] newArray(int i) {
            return new VendorAtomValue[i];
        }
    };
    public int _tag;
    public Object _value;

    public VendorAtomValue() {
        this._tag = 0;
        this._value = 0;
    }

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    public final int getStability() {
        return 1;
    }

    public static String _tagString(int i) {
        if (i == 0) {
            return "intValue";
        }
        if (i == 1) {
            return "longValue";
        }
        if (i == 2) {
            return "floatValue";
        }
        if (i == 3) {
            return "stringValue";
        }
        throw new IllegalStateException(VendorAtomValue$$ExternalSyntheticOutline0.m("unknown field: ", i));
    }

    public static VendorAtomValue intValue(int i) {
        return new VendorAtomValue(0, Integer.valueOf(i));
    }

    public final void _assertTag(int i) {
        if (this._tag != i) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("bad access: ");
            m.append(_tagString(i));
            m.append(", ");
            m.append(_tagString(this._tag));
            m.append(" is available.");
            throw new IllegalStateException(m.toString());
        }
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this._tag);
        int i2 = this._tag;
        if (i2 == 0) {
            _assertTag(0);
            parcel.writeInt(((Integer) this._value).intValue());
        } else if (i2 == 1) {
            _assertTag(1);
            parcel.writeLong(((Long) this._value).longValue());
        } else if (i2 == 2) {
            _assertTag(2);
            parcel.writeFloat(((Float) this._value).floatValue());
        } else if (i2 == 3) {
            _assertTag(3);
            parcel.writeString((String) this._value);
        }
    }

    public VendorAtomValue(Parcel parcel) {
        int readInt = parcel.readInt();
        if (readInt == 0) {
            Integer valueOf = Integer.valueOf(parcel.readInt());
            this._tag = readInt;
            this._value = valueOf;
        } else if (readInt == 1) {
            Long valueOf2 = Long.valueOf(parcel.readLong());
            this._tag = readInt;
            this._value = valueOf2;
        } else if (readInt == 2) {
            Float valueOf3 = Float.valueOf(parcel.readFloat());
            this._tag = readInt;
            this._value = valueOf3;
        } else if (readInt == 3) {
            String readString = parcel.readString();
            this._tag = readInt;
            this._value = readString;
        } else {
            throw new IllegalArgumentException(VendorAtomValue$$ExternalSyntheticOutline0.m("union: unknown tag: ", readInt));
        }
    }

    public VendorAtomValue(int i, Number number) {
        this._tag = i;
        this._value = number;
    }
}
