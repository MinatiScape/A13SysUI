package com.google.android.systemui.elmyra.proto.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.MessageNano;
import java.io.IOException;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SnapshotProtos$Event extends MessageNano {
    public static volatile SnapshotProtos$Event[] _emptyArray;
    public int typesCase_ = 0;
    public Object types_ = null;

    @Override // com.google.protobuf.nano.MessageNano
    public final int computeSerializedSize() {
        int i = 0;
        if (this.typesCase_ == 1) {
            i = 0 + CodedOutputByteBufferNano.computeMessageSize(1, (MessageNano) this.types_);
        }
        if (this.typesCase_ != 2) {
            return i;
        }
        int intValue = ((Integer) this.types_).intValue();
        return i + CodedOutputByteBufferNano.computeRawVarint32Size(intValue) + CodedOutputByteBufferNano.computeTagSize(2);
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        if (this.typesCase_ == 1) {
            codedOutputByteBufferNano.writeMessage(1, (MessageNano) this.types_);
        }
        if (this.typesCase_ == 2) {
            int intValue = ((Integer) this.types_).intValue();
            Objects.requireNonNull(codedOutputByteBufferNano);
            codedOutputByteBufferNano.writeTag(2, 0);
            codedOutputByteBufferNano.writeRawVarint32(intValue);
        }
    }

    public SnapshotProtos$Event() {
        this.cachedSize = -1;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                break;
            } else if (readTag == 10) {
                if (this.typesCase_ != 1) {
                    this.types_ = new ChassisProtos$SensorEvent();
                }
                codedInputByteBufferNano.readMessage((MessageNano) this.types_);
                this.typesCase_ = 1;
            } else if (readTag == 16) {
                this.types_ = Integer.valueOf(codedInputByteBufferNano.readRawVarint32());
                this.typesCase_ = 2;
            } else if (!codedInputByteBufferNano.skipField(readTag)) {
                break;
            }
        }
        return this;
    }
}
