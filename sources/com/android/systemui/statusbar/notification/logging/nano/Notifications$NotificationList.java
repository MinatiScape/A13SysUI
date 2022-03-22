package com.android.systemui.statusbar.notification.logging.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
/* loaded from: classes.dex */
public final class Notifications$NotificationList extends MessageNano {
    public Notifications$Notification[] notifications;

    @Override // com.google.protobuf.nano.MessageNano
    public final int computeSerializedSize() {
        Notifications$Notification[] notifications$NotificationArr = this.notifications;
        int i = 0;
        if (notifications$NotificationArr == null || notifications$NotificationArr.length <= 0) {
            return 0;
        }
        int i2 = 0;
        while (true) {
            Notifications$Notification[] notifications$NotificationArr2 = this.notifications;
            if (i >= notifications$NotificationArr2.length) {
                return i2;
            }
            Notifications$Notification notifications$Notification = notifications$NotificationArr2[i];
            if (notifications$Notification != null) {
                i2 += CodedOutputByteBufferNano.computeMessageSize(1, notifications$Notification);
            }
            i++;
        }
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        Notifications$Notification[] notifications$NotificationArr = this.notifications;
        if (notifications$NotificationArr != null && notifications$NotificationArr.length > 0) {
            int i = 0;
            while (true) {
                Notifications$Notification[] notifications$NotificationArr2 = this.notifications;
                if (i < notifications$NotificationArr2.length) {
                    Notifications$Notification notifications$Notification = notifications$NotificationArr2[i];
                    if (notifications$Notification != null) {
                        codedOutputByteBufferNano.writeMessage(1, notifications$Notification);
                    }
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public Notifications$NotificationList() {
        if (Notifications$Notification._emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (Notifications$Notification._emptyArray == null) {
                    Notifications$Notification._emptyArray = new Notifications$Notification[0];
                }
            }
        }
        this.notifications = Notifications$Notification._emptyArray;
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
                Notifications$Notification[] notifications$NotificationArr = this.notifications;
                if (notifications$NotificationArr == null) {
                    i = 0;
                } else {
                    i = notifications$NotificationArr.length;
                }
                int i2 = repeatedFieldArrayLength + i;
                Notifications$Notification[] notifications$NotificationArr2 = new Notifications$Notification[i2];
                if (i != 0) {
                    System.arraycopy(notifications$NotificationArr, 0, notifications$NotificationArr2, 0, i);
                }
                while (i < i2 - 1) {
                    notifications$NotificationArr2[i] = new Notifications$Notification();
                    codedInputByteBufferNano.readMessage(notifications$NotificationArr2[i]);
                    codedInputByteBufferNano.readTag();
                    i++;
                }
                notifications$NotificationArr2[i] = new Notifications$Notification();
                codedInputByteBufferNano.readMessage(notifications$NotificationArr2[i]);
                this.notifications = notifications$NotificationArr2;
            } else if (!codedInputByteBufferNano.skipField(readTag)) {
                break;
            }
        }
        return this;
    }
}
