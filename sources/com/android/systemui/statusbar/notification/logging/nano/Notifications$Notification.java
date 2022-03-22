package com.android.systemui.statusbar.notification.logging.nano;

import com.android.systemui.plugins.FalsingManager;
import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.MessageNano;
import java.io.IOException;
/* loaded from: classes.dex */
public final class Notifications$Notification extends MessageNano {
    public static volatile Notifications$Notification[] _emptyArray;
    public int uid = 0;
    public String packageName = "";
    public int instanceId = 0;
    public int groupInstanceId = 0;
    public boolean isGroupSummary = false;
    public int section = 0;

    @Override // com.google.protobuf.nano.MessageNano
    public final int computeSerializedSize() {
        int i = this.uid;
        int i2 = 0;
        if (i != 0) {
            i2 = 0 + CodedOutputByteBufferNano.computeInt32Size(1, i);
        }
        if (!this.packageName.equals("")) {
            i2 += CodedOutputByteBufferNano.computeStringSize(2, this.packageName);
        }
        int i3 = this.instanceId;
        if (i3 != 0) {
            i2 += CodedOutputByteBufferNano.computeInt32Size(3, i3);
        }
        int i4 = this.groupInstanceId;
        if (i4 != 0) {
            i2 += CodedOutputByteBufferNano.computeInt32Size(4, i4);
        }
        if (this.isGroupSummary) {
            i2 += CodedOutputByteBufferNano.computeBoolSize(5);
        }
        int i5 = this.section;
        if (i5 != 0) {
            return i2 + CodedOutputByteBufferNano.computeInt32Size(6, i5);
        }
        return i2;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        int i = this.uid;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(1, i);
        }
        if (!this.packageName.equals("")) {
            codedOutputByteBufferNano.writeString(2, this.packageName);
        }
        int i2 = this.instanceId;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(3, i2);
        }
        int i3 = this.groupInstanceId;
        if (i3 != 0) {
            codedOutputByteBufferNano.writeInt32(4, i3);
        }
        boolean z = this.isGroupSummary;
        if (z) {
            codedOutputByteBufferNano.writeBool(5, z);
        }
        int i4 = this.section;
        if (i4 != 0) {
            codedOutputByteBufferNano.writeInt32(6, i4);
        }
    }

    public Notifications$Notification() {
        this.cachedSize = -1;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag != 0) {
                if (readTag == 8) {
                    this.uid = codedInputByteBufferNano.readRawVarint32();
                } else if (readTag == 18) {
                    this.packageName = codedInputByteBufferNano.readString();
                } else if (readTag == 24) {
                    this.instanceId = codedInputByteBufferNano.readRawVarint32();
                } else if (readTag == 32) {
                    this.groupInstanceId = codedInputByteBufferNano.readRawVarint32();
                } else if (readTag == 40) {
                    this.isGroupSummary = codedInputByteBufferNano.readBool();
                } else if (readTag == 48) {
                    int readRawVarint32 = codedInputByteBufferNano.readRawVarint32();
                    switch (readRawVarint32) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case FalsingManager.VERSION /* 6 */:
                            this.section = readRawVarint32;
                            continue;
                    }
                } else if (!codedInputByteBufferNano.skipField(readTag)) {
                }
            }
        }
        return this;
    }
}
