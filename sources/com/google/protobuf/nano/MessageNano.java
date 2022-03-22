package com.google.protobuf.nano;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
/* loaded from: classes.dex */
public abstract class MessageNano {
    public volatile int cachedSize = -1;

    public static final <T extends MessageNano> T mergeFrom(T t, byte[] bArr) throws InvalidProtocolBufferNanoException {
        try {
            CodedInputByteBufferNano codedInputByteBufferNano = new CodedInputByteBufferNano(bArr, bArr.length);
            t.mergeFrom(codedInputByteBufferNano);
            if (codedInputByteBufferNano.lastTag == 0) {
                return t;
            }
            throw new InvalidProtocolBufferNanoException("Protocol message end-group tag did not match expected tag.");
        } catch (InvalidProtocolBufferNanoException e) {
            throw e;
        } catch (IOException unused) {
            throw new RuntimeException("Reading from a byte array threw an IOException (should never happen).");
        }
    }

    public int computeSerializedSize() {
        return 0;
    }

    public abstract MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException;

    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
    }

    public MessageNano clone() throws CloneNotSupportedException {
        return (MessageNano) super.clone();
    }

    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            MessageNanoPrinter.print(null, this, new StringBuffer(), stringBuffer);
            return stringBuffer.toString();
        } catch (IllegalAccessException e) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Error printing proto: ");
            m.append(e.getMessage());
            return m.toString();
        } catch (InvocationTargetException e2) {
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Error printing proto: ");
            m2.append(e2.getMessage());
            return m2.toString();
        }
    }

    public static final byte[] toByteArray(MessageNano messageNano) {
        int serializedSize = messageNano.getSerializedSize();
        byte[] bArr = new byte[serializedSize];
        try {
            CodedOutputByteBufferNano codedOutputByteBufferNano = new CodedOutputByteBufferNano(bArr, serializedSize);
            messageNano.writeTo(codedOutputByteBufferNano);
            if (codedOutputByteBufferNano.buffer.remaining() == 0) {
                return bArr;
            }
            throw new IllegalStateException("Did not write as much data as expected.");
        } catch (IOException e) {
            throw new RuntimeException("Serializing to a byte array threw an IOException (should never happen).", e);
        }
    }

    public final int getSerializedSize() {
        int computeSerializedSize = computeSerializedSize();
        this.cachedSize = computeSerializedSize;
        return computeSerializedSize;
    }
}
