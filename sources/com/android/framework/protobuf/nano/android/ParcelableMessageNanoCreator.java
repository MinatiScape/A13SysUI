package com.android.framework.protobuf.nano.android;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.framework.protobuf.nano.MessageNano;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ParcelableMessageNanoCreator<T extends MessageNano> implements Parcelable.Creator<T> {
    public static <T extends MessageNano> void writeToParcel(Class<T> cls, MessageNano messageNano, Parcel parcel) {
        parcel.writeString(cls.getName());
        Objects.requireNonNull(messageNano);
        messageNano.computeSerializedSize();
        byte[] bArr = new byte[0];
        try {
            ByteBuffer wrap = ByteBuffer.wrap(bArr, 0, 0);
            wrap.order(ByteOrder.LITTLE_ENDIAN);
            messageNano.writeTo();
            if (wrap.remaining() == 0) {
                parcel.writeByteArray(bArr);
                return;
            }
            throw new IllegalStateException("Did not write as much data as expected.");
        } catch (IOException e) {
            throw new RuntimeException("Serializing to a byte array threw an IOException (should never happen).", e);
        }
    }
}
