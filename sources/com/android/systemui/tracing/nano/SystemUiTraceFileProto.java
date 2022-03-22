package com.android.systemui.tracing.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
/* loaded from: classes.dex */
public final class SystemUiTraceFileProto extends MessageNano {
    public SystemUiTraceEntryProto[] entry;
    public long magicNumber = 0;

    @Override // com.google.protobuf.nano.MessageNano
    public final int computeSerializedSize() {
        int i;
        int i2 = 0;
        if (this.magicNumber != 0) {
            i = CodedOutputByteBufferNano.computeTagSize(1) + 8 + 0;
        } else {
            i = 0;
        }
        SystemUiTraceEntryProto[] systemUiTraceEntryProtoArr = this.entry;
        if (systemUiTraceEntryProtoArr != null && systemUiTraceEntryProtoArr.length > 0) {
            while (true) {
                SystemUiTraceEntryProto[] systemUiTraceEntryProtoArr2 = this.entry;
                if (i2 >= systemUiTraceEntryProtoArr2.length) {
                    break;
                }
                SystemUiTraceEntryProto systemUiTraceEntryProto = systemUiTraceEntryProtoArr2[i2];
                if (systemUiTraceEntryProto != null) {
                    i += CodedOutputByteBufferNano.computeMessageSize(2, systemUiTraceEntryProto);
                }
                i2++;
            }
        }
        return i;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        long j = this.magicNumber;
        if (j != 0) {
            codedOutputByteBufferNano.writeFixed64(j);
        }
        SystemUiTraceEntryProto[] systemUiTraceEntryProtoArr = this.entry;
        if (systemUiTraceEntryProtoArr != null && systemUiTraceEntryProtoArr.length > 0) {
            int i = 0;
            while (true) {
                SystemUiTraceEntryProto[] systemUiTraceEntryProtoArr2 = this.entry;
                if (i < systemUiTraceEntryProtoArr2.length) {
                    SystemUiTraceEntryProto systemUiTraceEntryProto = systemUiTraceEntryProtoArr2[i];
                    if (systemUiTraceEntryProto != null) {
                        codedOutputByteBufferNano.writeMessage(2, systemUiTraceEntryProto);
                    }
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public SystemUiTraceFileProto() {
        if (SystemUiTraceEntryProto._emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (SystemUiTraceEntryProto._emptyArray == null) {
                    SystemUiTraceEntryProto._emptyArray = new SystemUiTraceEntryProto[0];
                }
            }
        }
        this.entry = SystemUiTraceEntryProto._emptyArray;
        this.cachedSize = -1;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        int i;
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                break;
            } else if (readTag == 9) {
                this.magicNumber = codedInputByteBufferNano.readRawLittleEndian64();
            } else if (readTag == 18) {
                int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 18);
                SystemUiTraceEntryProto[] systemUiTraceEntryProtoArr = this.entry;
                if (systemUiTraceEntryProtoArr == null) {
                    i = 0;
                } else {
                    i = systemUiTraceEntryProtoArr.length;
                }
                int i2 = repeatedFieldArrayLength + i;
                SystemUiTraceEntryProto[] systemUiTraceEntryProtoArr2 = new SystemUiTraceEntryProto[i2];
                if (i != 0) {
                    System.arraycopy(systemUiTraceEntryProtoArr, 0, systemUiTraceEntryProtoArr2, 0, i);
                }
                while (i < i2 - 1) {
                    systemUiTraceEntryProtoArr2[i] = new SystemUiTraceEntryProto();
                    codedInputByteBufferNano.readMessage(systemUiTraceEntryProtoArr2[i]);
                    codedInputByteBufferNano.readTag();
                    i++;
                }
                systemUiTraceEntryProtoArr2[i] = new SystemUiTraceEntryProto();
                codedInputByteBufferNano.readMessage(systemUiTraceEntryProtoArr2[i]);
                this.entry = systemUiTraceEntryProtoArr2;
            } else if (!codedInputByteBufferNano.skipField(readTag)) {
                break;
            }
        }
        return this;
    }
}
