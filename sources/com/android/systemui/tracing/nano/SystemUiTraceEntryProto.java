package com.android.systemui.tracing.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.MessageNano;
import java.io.IOException;
/* loaded from: classes.dex */
public final class SystemUiTraceEntryProto extends MessageNano {
    public static volatile SystemUiTraceEntryProto[] _emptyArray;
    public long elapsedRealtimeNanos = 0;
    public SystemUiTraceProto systemUi = null;

    @Override // com.google.protobuf.nano.MessageNano
    public final int computeSerializedSize() {
        int i = 0;
        if (this.elapsedRealtimeNanos != 0) {
            i = 0 + CodedOutputByteBufferNano.computeTagSize(1) + 8;
        }
        SystemUiTraceProto systemUiTraceProto = this.systemUi;
        if (systemUiTraceProto != null) {
            return i + CodedOutputByteBufferNano.computeMessageSize(3, systemUiTraceProto);
        }
        return i;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        long j = this.elapsedRealtimeNanos;
        if (j != 0) {
            codedOutputByteBufferNano.writeFixed64(j);
        }
        SystemUiTraceProto systemUiTraceProto = this.systemUi;
        if (systemUiTraceProto != null) {
            codedOutputByteBufferNano.writeMessage(3, systemUiTraceProto);
        }
    }

    public SystemUiTraceEntryProto() {
        this.cachedSize = -1;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                break;
            } else if (readTag == 9) {
                this.elapsedRealtimeNanos = codedInputByteBufferNano.readRawLittleEndian64();
            } else if (readTag == 26) {
                if (this.systemUi == null) {
                    this.systemUi = new SystemUiTraceProto();
                }
                codedInputByteBufferNano.readMessage(this.systemUi);
            } else if (!codedInputByteBufferNano.skipField(readTag)) {
                break;
            }
        }
        return this;
    }
}
