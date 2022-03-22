package com.google.android.systemui.elmyra.proto.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
/* loaded from: classes.dex */
public final class ChassisProtos$Chassis extends MessageNano {
    public ElmyraFilters$Filter[] defaultFilters;
    public ChassisProtos$Sensor[] sensors;

    @Override // com.google.protobuf.nano.MessageNano
    public final int computeSerializedSize() {
        int i;
        ChassisProtos$Sensor[] chassisProtos$SensorArr = this.sensors;
        int i2 = 0;
        if (chassisProtos$SensorArr != null && chassisProtos$SensorArr.length > 0) {
            int i3 = 0;
            i = 0;
            while (true) {
                ChassisProtos$Sensor[] chassisProtos$SensorArr2 = this.sensors;
                if (i3 >= chassisProtos$SensorArr2.length) {
                    break;
                }
                ChassisProtos$Sensor chassisProtos$Sensor = chassisProtos$SensorArr2[i3];
                if (chassisProtos$Sensor != null) {
                    i += CodedOutputByteBufferNano.computeMessageSize(1, chassisProtos$Sensor);
                }
                i3++;
            }
        } else {
            i = 0;
        }
        ElmyraFilters$Filter[] elmyraFilters$FilterArr = this.defaultFilters;
        if (elmyraFilters$FilterArr != null && elmyraFilters$FilterArr.length > 0) {
            while (true) {
                ElmyraFilters$Filter[] elmyraFilters$FilterArr2 = this.defaultFilters;
                if (i2 >= elmyraFilters$FilterArr2.length) {
                    break;
                }
                ElmyraFilters$Filter elmyraFilters$Filter = elmyraFilters$FilterArr2[i2];
                if (elmyraFilters$Filter != null) {
                    i += CodedOutputByteBufferNano.computeMessageSize(2, elmyraFilters$Filter);
                }
                i2++;
            }
        }
        return i;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        ChassisProtos$Sensor[] chassisProtos$SensorArr = this.sensors;
        int i = 0;
        if (chassisProtos$SensorArr != null && chassisProtos$SensorArr.length > 0) {
            int i2 = 0;
            while (true) {
                ChassisProtos$Sensor[] chassisProtos$SensorArr2 = this.sensors;
                if (i2 >= chassisProtos$SensorArr2.length) {
                    break;
                }
                ChassisProtos$Sensor chassisProtos$Sensor = chassisProtos$SensorArr2[i2];
                if (chassisProtos$Sensor != null) {
                    codedOutputByteBufferNano.writeMessage(1, chassisProtos$Sensor);
                }
                i2++;
            }
        }
        ElmyraFilters$Filter[] elmyraFilters$FilterArr = this.defaultFilters;
        if (elmyraFilters$FilterArr != null && elmyraFilters$FilterArr.length > 0) {
            while (true) {
                ElmyraFilters$Filter[] elmyraFilters$FilterArr2 = this.defaultFilters;
                if (i < elmyraFilters$FilterArr2.length) {
                    ElmyraFilters$Filter elmyraFilters$Filter = elmyraFilters$FilterArr2[i];
                    if (elmyraFilters$Filter != null) {
                        codedOutputByteBufferNano.writeMessage(2, elmyraFilters$Filter);
                    }
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public ChassisProtos$Chassis() {
        if (ChassisProtos$Sensor._emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (ChassisProtos$Sensor._emptyArray == null) {
                    ChassisProtos$Sensor._emptyArray = new ChassisProtos$Sensor[0];
                }
            }
        }
        this.sensors = ChassisProtos$Sensor._emptyArray;
        if (ElmyraFilters$Filter._emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (ElmyraFilters$Filter._emptyArray == null) {
                    ElmyraFilters$Filter._emptyArray = new ElmyraFilters$Filter[0];
                }
            }
        }
        this.defaultFilters = ElmyraFilters$Filter._emptyArray;
        this.cachedSize = -1;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        int i;
        int i2;
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                break;
            } else if (readTag == 10) {
                int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 10);
                ChassisProtos$Sensor[] chassisProtos$SensorArr = this.sensors;
                if (chassisProtos$SensorArr == null) {
                    i2 = 0;
                } else {
                    i2 = chassisProtos$SensorArr.length;
                }
                int i3 = repeatedFieldArrayLength + i2;
                ChassisProtos$Sensor[] chassisProtos$SensorArr2 = new ChassisProtos$Sensor[i3];
                if (i2 != 0) {
                    System.arraycopy(chassisProtos$SensorArr, 0, chassisProtos$SensorArr2, 0, i2);
                }
                while (i2 < i3 - 1) {
                    chassisProtos$SensorArr2[i2] = new ChassisProtos$Sensor();
                    codedInputByteBufferNano.readMessage(chassisProtos$SensorArr2[i2]);
                    codedInputByteBufferNano.readTag();
                    i2++;
                }
                chassisProtos$SensorArr2[i2] = new ChassisProtos$Sensor();
                codedInputByteBufferNano.readMessage(chassisProtos$SensorArr2[i2]);
                this.sensors = chassisProtos$SensorArr2;
            } else if (readTag == 18) {
                int repeatedFieldArrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 18);
                ElmyraFilters$Filter[] elmyraFilters$FilterArr = this.defaultFilters;
                if (elmyraFilters$FilterArr == null) {
                    i = 0;
                } else {
                    i = elmyraFilters$FilterArr.length;
                }
                int i4 = repeatedFieldArrayLength2 + i;
                ElmyraFilters$Filter[] elmyraFilters$FilterArr2 = new ElmyraFilters$Filter[i4];
                if (i != 0) {
                    System.arraycopy(elmyraFilters$FilterArr, 0, elmyraFilters$FilterArr2, 0, i);
                }
                while (i < i4 - 1) {
                    elmyraFilters$FilterArr2[i] = new ElmyraFilters$Filter();
                    codedInputByteBufferNano.readMessage(elmyraFilters$FilterArr2[i]);
                    codedInputByteBufferNano.readTag();
                    i++;
                }
                elmyraFilters$FilterArr2[i] = new ElmyraFilters$Filter();
                codedInputByteBufferNano.readMessage(elmyraFilters$FilterArr2[i]);
                this.defaultFilters = elmyraFilters$FilterArr2;
            } else if (!codedInputByteBufferNano.skipField(readTag)) {
                break;
            }
        }
        return this;
    }
}
