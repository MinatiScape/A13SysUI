package com.google.android.systemui.elmyra.proto.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.MessageNano;
import java.io.IOException;
/* loaded from: classes.dex */
public final class ContextHubMessages$RecognizerStart extends MessageNano {
    public float progressReportThreshold = 0.0f;
    public float sensitivity = 0.0f;

    @Override // com.google.protobuf.nano.MessageNano
    public final int computeSerializedSize() {
        int i = 0;
        if (Float.floatToIntBits(this.progressReportThreshold) != Float.floatToIntBits(0.0f)) {
            i = 0 + CodedOutputByteBufferNano.computeFloatSize(1);
        }
        if (Float.floatToIntBits(this.sensitivity) != Float.floatToIntBits(0.0f)) {
            return i + CodedOutputByteBufferNano.computeFloatSize(2);
        }
        return i;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        if (Float.floatToIntBits(this.progressReportThreshold) != Float.floatToIntBits(0.0f)) {
            codedOutputByteBufferNano.writeFloat(1, this.progressReportThreshold);
        }
        if (Float.floatToIntBits(this.sensitivity) != Float.floatToIntBits(0.0f)) {
            codedOutputByteBufferNano.writeFloat(2, this.sensitivity);
        }
    }

    public ContextHubMessages$RecognizerStart() {
        this.cachedSize = -1;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                break;
            } else if (readTag == 13) {
                this.progressReportThreshold = codedInputByteBufferNano.readFloat();
            } else if (readTag == 21) {
                this.sensitivity = codedInputByteBufferNano.readFloat();
            } else if (!codedInputByteBufferNano.skipField(readTag)) {
                break;
            }
        }
        return this;
    }
}
