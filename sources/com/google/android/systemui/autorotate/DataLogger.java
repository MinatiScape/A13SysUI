package com.google.android.systemui.autorotate;

import android.app.StatsManager;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import android.util.StatsEvent;
import com.android.internal.util.FrameworkStatsLog;
import com.google.android.systemui.autorotate.proto.nano.AutorotateProto$DeviceRotatedSensorData;
import com.google.android.systemui.autorotate.proto.nano.AutorotateProto$DeviceRotatedSensorHeader;
import com.google.android.systemui.autorotate.proto.nano.AutorotateProto$DeviceRotatedSensorSample;
import com.google.protobuf.nano.MessageNano;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DataLogger {
    public LinkedList mDataQueue = new LinkedList();
    public long mLastPullTimeNanos = 0;
    public StatsManager mStatsManager;

    /* loaded from: classes.dex */
    public class StatsPullAtomCallbackImpl implements StatsManager.StatsPullAtomCallback {
        public StatsPullAtomCallbackImpl() {
        }

        public final int onPullAtom(int i, List<StatsEvent> list) {
            int i2;
            if (i == 10097) {
                DataLogger dataLogger = DataLogger.this;
                Objects.requireNonNull(dataLogger);
                Log.d("Autorotate-DataLogger", "Received pull request from statsd.");
                dataLogger.mLastPullTimeNanos = SystemClock.elapsedRealtimeNanos();
                Pair pair = (Pair) dataLogger.mDataQueue.poll();
                SensorData[] sensorDataArr = (SensorData[]) pair.first;
                int intValue = ((Integer) pair.second).intValue();
                if (!(sensorDataArr == null || sensorDataArr.length == 0 || sensorDataArr[0] == null)) {
                    AutorotateProto$DeviceRotatedSensorData autorotateProto$DeviceRotatedSensorData = new AutorotateProto$DeviceRotatedSensorData();
                    AutorotateProto$DeviceRotatedSensorHeader autorotateProto$DeviceRotatedSensorHeader = new AutorotateProto$DeviceRotatedSensorHeader();
                    autorotateProto$DeviceRotatedSensorHeader.timestampBase = sensorDataArr[0].mTimestampMillis;
                    autorotateProto$DeviceRotatedSensorData.header = autorotateProto$DeviceRotatedSensorHeader;
                    AutorotateProto$DeviceRotatedSensorSample[] autorotateProto$DeviceRotatedSensorSampleArr = new AutorotateProto$DeviceRotatedSensorSample[sensorDataArr.length];
                    int i3 = 0;
                    while (true) {
                        i2 = 4;
                        int i4 = 2;
                        if (i3 >= sensorDataArr.length) {
                            break;
                        }
                        AutorotateProto$DeviceRotatedSensorSample autorotateProto$DeviceRotatedSensorSample = new AutorotateProto$DeviceRotatedSensorSample();
                        autorotateProto$DeviceRotatedSensorSample.timestampOffset = (int) (sensorDataArr[i3].mTimestampMillis - autorotateProto$DeviceRotatedSensorHeader.timestampBase);
                        int i5 = sensorDataArr[i3].mSensorIdentifier;
                        if (i5 != 4) {
                            i4 = i5;
                        }
                        autorotateProto$DeviceRotatedSensorSample.sensorType = i4;
                        autorotateProto$DeviceRotatedSensorSample.xValue = sensorDataArr[i3].mValueX;
                        autorotateProto$DeviceRotatedSensorSample.yValue = sensorDataArr[i3].mValueY;
                        autorotateProto$DeviceRotatedSensorSample.zValue = sensorDataArr[i3].mValueZ;
                        autorotateProto$DeviceRotatedSensorSampleArr[i3] = autorotateProto$DeviceRotatedSensorSample;
                        i3++;
                    }
                    autorotateProto$DeviceRotatedSensorData.sample = autorotateProto$DeviceRotatedSensorSampleArr;
                    byte[] byteArray = MessageNano.toByteArray(autorotateProto$DeviceRotatedSensorData);
                    int i6 = 1;
                    if (intValue != 0) {
                        if (intValue != 1) {
                            i6 = 3;
                            if (intValue != 2) {
                                if (intValue != 3) {
                                    i2 = 0;
                                }
                            }
                        } else {
                            i2 = 2;
                        }
                        list.add(FrameworkStatsLog.buildStatsEvent(10097, byteArray, i2));
                    }
                    i2 = i6;
                    list.add(FrameworkStatsLog.buildStatsEvent(10097, byteArray, i2));
                }
                return 0;
            }
            throw new UnsupportedOperationException(VendorAtomValue$$ExternalSyntheticOutline0.m("Unknown tagId: ", i));
        }
    }

    public DataLogger(StatsManager statsManager) {
        this.mStatsManager = statsManager;
    }
}
