package com.android.systemui.tracing.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.MessageNano;
import java.io.IOException;
/* loaded from: classes.dex */
public final class EdgeBackGestureHandlerProto extends MessageNano {
    public boolean allowGesture = false;

    @Override // com.google.protobuf.nano.MessageNano
    public final int computeSerializedSize() {
        if (this.allowGesture) {
            return 0 + CodedOutputByteBufferNano.computeBoolSize(1);
        }
        return 0;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        boolean z = this.allowGesture;
        if (z) {
            codedOutputByteBufferNano.writeBool(1, z);
        }
    }

    public EdgeBackGestureHandlerProto() {
        this.cachedSize = -1;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                break;
            } else if (readTag == 8) {
                this.allowGesture = codedInputByteBufferNano.readBool();
            } else if (!codedInputByteBufferNano.skipField(readTag)) {
                break;
            }
        }
        return this;
    }
}
