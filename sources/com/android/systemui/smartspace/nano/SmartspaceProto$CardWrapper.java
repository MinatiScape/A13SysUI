package com.android.systemui.smartspace.nano;

import com.android.systemui.smartspace.nano.SmartspaceProto$SmartspaceUpdate;
import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SmartspaceProto$CardWrapper extends MessageNano {
    public SmartspaceProto$SmartspaceUpdate.SmartspaceCard card = null;
    public long publishTime = 0;
    public long gsaUpdateTime = 0;
    public int gsaVersionCode = 0;
    public byte[] icon = WireFormatNano.EMPTY_BYTES;
    public boolean isIconGrayscale = false;

    @Override // com.google.protobuf.nano.MessageNano
    public final int computeSerializedSize() {
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard smartspaceCard = this.card;
        int i = 0;
        if (smartspaceCard != null) {
            i = 0 + CodedOutputByteBufferNano.computeMessageSize(1, smartspaceCard);
        }
        long j = this.publishTime;
        if (j != 0) {
            i += CodedOutputByteBufferNano.computeInt64Size(2, j);
        }
        long j2 = this.gsaUpdateTime;
        if (j2 != 0) {
            i += CodedOutputByteBufferNano.computeInt64Size(3, j2);
        }
        int i2 = this.gsaVersionCode;
        if (i2 != 0) {
            i += CodedOutputByteBufferNano.computeInt32Size(4, i2);
        }
        if (!Arrays.equals(this.icon, WireFormatNano.EMPTY_BYTES)) {
            byte[] bArr = this.icon;
            i += CodedOutputByteBufferNano.computeRawVarint32Size(bArr.length) + bArr.length + CodedOutputByteBufferNano.computeTagSize(5);
        }
        if (this.isIconGrayscale) {
            return i + CodedOutputByteBufferNano.computeBoolSize(6);
        }
        return i;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard smartspaceCard = this.card;
        if (smartspaceCard != null) {
            codedOutputByteBufferNano.writeMessage(1, smartspaceCard);
        }
        long j = this.publishTime;
        if (j != 0) {
            codedOutputByteBufferNano.writeInt64(2, j);
        }
        long j2 = this.gsaUpdateTime;
        if (j2 != 0) {
            codedOutputByteBufferNano.writeInt64(3, j2);
        }
        int i = this.gsaVersionCode;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(4, i);
        }
        if (!Arrays.equals(this.icon, WireFormatNano.EMPTY_BYTES)) {
            byte[] bArr = this.icon;
            Objects.requireNonNull(codedOutputByteBufferNano);
            codedOutputByteBufferNano.writeTag(5, 2);
            codedOutputByteBufferNano.writeRawVarint32(bArr.length);
            int length = bArr.length;
            if (codedOutputByteBufferNano.buffer.remaining() >= length) {
                codedOutputByteBufferNano.buffer.put(bArr, 0, length);
            } else {
                throw new CodedOutputByteBufferNano.OutOfSpaceException(codedOutputByteBufferNano.buffer.position(), codedOutputByteBufferNano.buffer.limit());
            }
        }
        boolean z = this.isIconGrayscale;
        if (z) {
            codedOutputByteBufferNano.writeBool(6, z);
        }
    }

    public SmartspaceProto$CardWrapper() {
        this.cachedSize = -1;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        byte[] bArr;
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                break;
            } else if (readTag == 10) {
                if (this.card == null) {
                    this.card = new SmartspaceProto$SmartspaceUpdate.SmartspaceCard();
                }
                codedInputByteBufferNano.readMessage(this.card);
            } else if (readTag == 16) {
                this.publishTime = codedInputByteBufferNano.readRawVarint64();
            } else if (readTag == 24) {
                this.gsaUpdateTime = codedInputByteBufferNano.readRawVarint64();
            } else if (readTag == 32) {
                this.gsaVersionCode = codedInputByteBufferNano.readRawVarint32();
            } else if (readTag == 42) {
                int readRawVarint32 = codedInputByteBufferNano.readRawVarint32();
                int i = codedInputByteBufferNano.bufferSize;
                int i2 = codedInputByteBufferNano.bufferPos;
                if (readRawVarint32 <= i - i2 && readRawVarint32 > 0) {
                    bArr = new byte[readRawVarint32];
                    System.arraycopy(codedInputByteBufferNano.buffer, i2, bArr, 0, readRawVarint32);
                    codedInputByteBufferNano.bufferPos += readRawVarint32;
                } else if (readRawVarint32 == 0) {
                    bArr = WireFormatNano.EMPTY_BYTES;
                } else {
                    bArr = codedInputByteBufferNano.readRawBytes(readRawVarint32);
                }
                this.icon = bArr;
            } else if (readTag == 48) {
                this.isIconGrayscale = codedInputByteBufferNano.readBool();
            } else if (!codedInputByteBufferNano.skipField(readTag)) {
                break;
            }
        }
        return this;
    }
}
