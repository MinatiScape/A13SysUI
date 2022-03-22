package com.google.android.systemui.columbus.proto.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
/* loaded from: classes.dex */
public final class ColumbusProto$NanoappEvents extends MessageNano {
    public ColumbusProto$NanoappEvent[] batchedEvents;

    @Override // com.google.protobuf.nano.MessageNano
    public final int computeSerializedSize() {
        ColumbusProto$NanoappEvent[] columbusProto$NanoappEventArr = this.batchedEvents;
        int i = 0;
        if (columbusProto$NanoappEventArr == null || columbusProto$NanoappEventArr.length <= 0) {
            return 0;
        }
        int i2 = 0;
        while (true) {
            ColumbusProto$NanoappEvent[] columbusProto$NanoappEventArr2 = this.batchedEvents;
            if (i >= columbusProto$NanoappEventArr2.length) {
                return i2;
            }
            ColumbusProto$NanoappEvent columbusProto$NanoappEvent = columbusProto$NanoappEventArr2[i];
            if (columbusProto$NanoappEvent != null) {
                i2 += CodedOutputByteBufferNano.computeMessageSize(1, columbusProto$NanoappEvent);
            }
            i++;
        }
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        ColumbusProto$NanoappEvent[] columbusProto$NanoappEventArr = this.batchedEvents;
        if (columbusProto$NanoappEventArr != null && columbusProto$NanoappEventArr.length > 0) {
            int i = 0;
            while (true) {
                ColumbusProto$NanoappEvent[] columbusProto$NanoappEventArr2 = this.batchedEvents;
                if (i < columbusProto$NanoappEventArr2.length) {
                    ColumbusProto$NanoappEvent columbusProto$NanoappEvent = columbusProto$NanoappEventArr2[i];
                    if (columbusProto$NanoappEvent != null) {
                        codedOutputByteBufferNano.writeMessage(1, columbusProto$NanoappEvent);
                    }
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public ColumbusProto$NanoappEvents() {
        if (ColumbusProto$NanoappEvent._emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (ColumbusProto$NanoappEvent._emptyArray == null) {
                    ColumbusProto$NanoappEvent._emptyArray = new ColumbusProto$NanoappEvent[0];
                }
            }
        }
        this.batchedEvents = ColumbusProto$NanoappEvent._emptyArray;
        this.cachedSize = -1;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        int i;
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                break;
            } else if (readTag == 10) {
                int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 10);
                ColumbusProto$NanoappEvent[] columbusProto$NanoappEventArr = this.batchedEvents;
                if (columbusProto$NanoappEventArr == null) {
                    i = 0;
                } else {
                    i = columbusProto$NanoappEventArr.length;
                }
                int i2 = repeatedFieldArrayLength + i;
                ColumbusProto$NanoappEvent[] columbusProto$NanoappEventArr2 = new ColumbusProto$NanoappEvent[i2];
                if (i != 0) {
                    System.arraycopy(columbusProto$NanoappEventArr, 0, columbusProto$NanoappEventArr2, 0, i);
                }
                while (i < i2 - 1) {
                    columbusProto$NanoappEventArr2[i] = new ColumbusProto$NanoappEvent();
                    codedInputByteBufferNano.readMessage(columbusProto$NanoappEventArr2[i]);
                    codedInputByteBufferNano.readTag();
                    i++;
                }
                columbusProto$NanoappEventArr2[i] = new ColumbusProto$NanoappEvent();
                codedInputByteBufferNano.readMessage(columbusProto$NanoappEventArr2[i]);
                this.batchedEvents = columbusProto$NanoappEventArr2;
            } else if (!codedInputByteBufferNano.skipField(readTag)) {
                break;
            }
        }
        return this;
    }
}
