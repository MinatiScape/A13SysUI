package com.android.systemui.tracing.nano;

import com.android.wm.shell.nano.WmShellTraceProto;
import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.MessageNano;
import java.io.IOException;
/* loaded from: classes.dex */
public final class SystemUiTraceProto extends MessageNano {
    public EdgeBackGestureHandlerProto edgeBackGestureHandler = null;
    public WmShellTraceProto wmShell = null;

    @Override // com.google.protobuf.nano.MessageNano
    public final int computeSerializedSize() {
        EdgeBackGestureHandlerProto edgeBackGestureHandlerProto = this.edgeBackGestureHandler;
        int i = 0;
        if (edgeBackGestureHandlerProto != null) {
            i = 0 + CodedOutputByteBufferNano.computeMessageSize(1, edgeBackGestureHandlerProto);
        }
        WmShellTraceProto wmShellTraceProto = this.wmShell;
        if (wmShellTraceProto != null) {
            return i + CodedOutputByteBufferNano.computeMessageSize(2, wmShellTraceProto);
        }
        return i;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        EdgeBackGestureHandlerProto edgeBackGestureHandlerProto = this.edgeBackGestureHandler;
        if (edgeBackGestureHandlerProto != null) {
            codedOutputByteBufferNano.writeMessage(1, edgeBackGestureHandlerProto);
        }
        WmShellTraceProto wmShellTraceProto = this.wmShell;
        if (wmShellTraceProto != null) {
            codedOutputByteBufferNano.writeMessage(2, wmShellTraceProto);
        }
    }

    public SystemUiTraceProto() {
        this.cachedSize = -1;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                break;
            } else if (readTag == 10) {
                if (this.edgeBackGestureHandler == null) {
                    this.edgeBackGestureHandler = new EdgeBackGestureHandlerProto();
                }
                codedInputByteBufferNano.readMessage(this.edgeBackGestureHandler);
            } else if (readTag == 18) {
                if (this.wmShell == null) {
                    this.wmShell = new WmShellTraceProto();
                }
                codedInputByteBufferNano.readMessage(this.wmShell);
            } else if (!codedInputByteBufferNano.skipField(readTag)) {
                break;
            }
        }
        return this;
    }
}
