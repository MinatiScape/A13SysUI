package com.google.android.systemui.autorotate;

import android.app.StatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.provider.DeviceConfig;
import android.util.Log;
import android.util.Pair;
import com.android.internal.util.ConcurrentUtils;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.LatencyTracker;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.google.android.systemui.autorotate.DataLogger;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public final class AutorotateDataService {
    public final BroadcastDispatcher mBroadcastDispatcher;
    public final Context mContext;
    public Sensor mDebugSensor;
    public final DeviceConfigProxy mDeviceConfig;
    public LatencyTracker mLatencyTracker;
    public final DelayableExecutor mMainExecutor;
    public Sensor mPreindicationSensor;
    public boolean mRawSensorLoggingEnabled;
    public boolean mRotationPreindicationEnabled;
    public final DataLogger mSensorDataLogger;
    public final SensorManager mSensorManager;
    public boolean mServiceRunning = false;
    public int mLastPreIndication = -1;
    public SensorData[] mSensorData = new SensorData[600];
    public int mSensorDataIndex = 0;
    public final AnonymousClass1 mScreenEventBroadcastReceiver = new BroadcastReceiver() { // from class: com.google.android.systemui.autorotate.AutorotateDataService.1
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                AutorotateDataService.this.registerRequiredSensors();
            } else if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                AutorotateDataService autorotateDataService = AutorotateDataService.this;
                Objects.requireNonNull(autorotateDataService);
                autorotateDataService.mSensorManager.unregisterListener(autorotateDataService.mSensorListener);
                DataLogger dataLogger = autorotateDataService.mSensorDataLogger;
                Objects.requireNonNull(dataLogger);
                dataLogger.mDataQueue.clear();
                AutorotateDataService.this.mLastPreIndication = -1;
            }
        }
    };
    public final SensorListener mSensorListener = new SensorListener();

    /* loaded from: classes.dex */
    public class SensorDataReadyRunnable implements Runnable {
        public int mRotation;
        public long mRotationTimestampMillis;

        public SensorDataReadyRunnable(int i, long j) {
            this.mRotation = i;
            this.mRotationTimestampMillis = j;
        }

        @Override // java.lang.Runnable
        public final void run() {
            AutorotateDataService autorotateDataService = AutorotateDataService.this;
            DataLogger dataLogger = autorotateDataService.mSensorDataLogger;
            SensorData[] sensorDataArr = autorotateDataService.mSensorData;
            int i = this.mRotation;
            Objects.requireNonNull(dataLogger);
            int i2 = 0;
            if (!(sensorDataArr == null || sensorDataArr.length == 0 || sensorDataArr[0] == null)) {
                if (SystemClock.elapsedRealtimeNanos() - dataLogger.mLastPullTimeNanos > 5000000000L) {
                    dataLogger.mDataQueue.clear();
                }
                dataLogger.mDataQueue.add(Pair.create(sensorDataArr, Integer.valueOf(i)));
            }
            DataLogger dataLogger2 = AutorotateDataService.this.mSensorDataLogger;
            long j = this.mRotationTimestampMillis;
            int i3 = this.mRotation;
            Objects.requireNonNull(dataLogger2);
            if (i3 == 0) {
                i2 = 1;
            } else if (i3 == 1) {
                i2 = 2;
            } else if (i3 == 2) {
                i2 = 3;
            } else if (i3 == 3) {
                i2 = 4;
            }
            FrameworkStatsLog.write(333, j, i2, 3);
        }
    }

    /* loaded from: classes.dex */
    public class SensorListener implements SensorEventListener {
        @Override // android.hardware.SensorEventListener
        public final void onAccuracyChanged(Sensor sensor, int i) {
        }

        public SensorListener() {
        }

        @Override // android.hardware.SensorEventListener
        public final void onSensorChanged(SensorEvent sensorEvent) {
            int i = 0;
            if (sensorEvent.sensor.getType() == 27) {
                int i2 = (int) sensorEvent.values[0];
                if (i2 >= 0 && i2 <= 3) {
                    AutorotateDataService autorotateDataService = AutorotateDataService.this;
                    autorotateDataService.mSensorData = new SensorData[600];
                    autorotateDataService.mSensorDataIndex = 0;
                    autorotateDataService.mMainExecutor.executeDelayed(new SensorDataReadyRunnable(i2, sensorEvent.timestamp / 1000000), 2300L, TimeUnit.MILLISECONDS);
                    AutorotateDataService autorotateDataService2 = AutorotateDataService.this;
                    if (autorotateDataService2.mPreindicationSensor != null && i2 == autorotateDataService2.mLastPreIndication) {
                        autorotateDataService2.mLatencyTracker.onActionEnd(9);
                    }
                }
            } else if (sensorEvent.sensor.getType() == 65548) {
                AutorotateDataService autorotateDataService3 = AutorotateDataService.this;
                int i3 = autorotateDataService3.mSensorDataIndex;
                if (i3 < 600) {
                    SensorData[] sensorDataArr = autorotateDataService3.mSensorData;
                    float[] fArr = sensorEvent.values;
                    sensorDataArr[i3] = new SensorData(fArr[0], fArr[1], fArr[2], (int) fArr[3], sensorEvent.timestamp / 1000000);
                    autorotateDataService3.mSensorDataIndex = i3 + 1;
                    return;
                }
                autorotateDataService3.mSensorDataIndex = 0;
            } else if (sensorEvent.sensor.getType() == 65553) {
                AutorotateDataService autorotateDataService4 = AutorotateDataService.this;
                if (autorotateDataService4.mPreindicationSensor != null) {
                    int i4 = (int) sensorEvent.values[0];
                    autorotateDataService4.mLatencyTracker.onActionStart(9);
                    AutorotateDataService autorotateDataService5 = AutorotateDataService.this;
                    autorotateDataService5.mLastPreIndication = i4;
                    DataLogger dataLogger = autorotateDataService5.mSensorDataLogger;
                    long j = sensorEvent.timestamp / 1000000;
                    Objects.requireNonNull(dataLogger);
                    if (i4 == 0) {
                        i = 1;
                    } else if (i4 == 1) {
                        i = 2;
                    } else if (i4 == 2) {
                        i = 3;
                    } else if (i4 == 3) {
                        i = 4;
                    }
                    FrameworkStatsLog.write(333, j, i, 1);
                }
            }
        }
    }

    public final void registerRequiredSensors() {
        this.mSensorManager.registerListener(this.mSensorListener, this.mSensorManager.getDefaultSensor(27), 1);
        if (this.mRawSensorLoggingEnabled) {
            Sensor defaultSensor = this.mSensorManager.getDefaultSensor(65548);
            this.mDebugSensor = defaultSensor;
            this.mSensorManager.registerListener(this.mSensorListener, defaultSensor, 1);
        }
        if (this.mRotationPreindicationEnabled) {
            Sensor defaultSensor2 = this.mSensorManager.getDefaultSensor(65553);
            this.mPreindicationSensor = defaultSensor2;
            this.mSensorManager.registerListener(this.mSensorListener, defaultSensor2, 1);
        }
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.google.android.systemui.autorotate.AutorotateDataService$1] */
    public AutorotateDataService(Context context, SensorManager sensorManager, DataLogger dataLogger, BroadcastDispatcher broadcastDispatcher, DeviceConfigProxy deviceConfigProxy, DelayableExecutor delayableExecutor) {
        this.mContext = context;
        this.mMainExecutor = delayableExecutor;
        this.mSensorDataLogger = dataLogger;
        this.mSensorManager = sensorManager;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mDeviceConfig = deviceConfigProxy;
    }

    public final void readFlagsToControlSensorLogging() {
        Sensor sensor;
        Sensor sensor2;
        this.mRawSensorLoggingEnabled = DeviceConfig.getBoolean("window_manager", "log_raw_sensor_data", false);
        boolean z = DeviceConfig.getBoolean("window_manager", "log_rotation_preindication", false);
        this.mRotationPreindicationEnabled = z;
        boolean z2 = this.mRawSensorLoggingEnabled;
        if (z2 || z) {
            if (z2 || z) {
                if (!this.mServiceRunning) {
                    DataLogger dataLogger = this.mSensorDataLogger;
                    Objects.requireNonNull(dataLogger);
                    StatsManager statsManager = dataLogger.mStatsManager;
                    if (statsManager != null) {
                        statsManager.setPullAtomCallback(10097, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, new DataLogger.StatsPullAtomCallbackImpl());
                        Log.d("Autorotate-DataLogger", "Registered to statsd for pull");
                    }
                    IntentFilter intentFilter = new IntentFilter("android.intent.action.SCREEN_ON");
                    intentFilter.addAction("android.intent.action.SCREEN_OFF");
                    BroadcastDispatcher broadcastDispatcher = this.mBroadcastDispatcher;
                    AnonymousClass1 r3 = this.mScreenEventBroadcastReceiver;
                    Objects.requireNonNull(broadcastDispatcher);
                    BroadcastDispatcher.registerReceiver$default(broadcastDispatcher, r3, intentFilter, null, null, 24);
                    this.mServiceRunning = true;
                }
                registerRequiredSensors();
            }
            if (!this.mRawSensorLoggingEnabled && (sensor2 = this.mDebugSensor) != null) {
                this.mSensorManager.unregisterListener(this.mSensorListener, sensor2);
            }
            if (!this.mRotationPreindicationEnabled && (sensor = this.mPreindicationSensor) != null) {
                this.mSensorManager.unregisterListener(this.mSensorListener, sensor);
                return;
            }
            return;
        }
        if (this.mServiceRunning) {
            DataLogger dataLogger2 = this.mSensorDataLogger;
            Objects.requireNonNull(dataLogger2);
            StatsManager statsManager2 = dataLogger2.mStatsManager;
            if (statsManager2 != null) {
                statsManager2.clearPullAtomCallback(10097);
            }
            this.mBroadcastDispatcher.unregisterReceiver(this.mScreenEventBroadcastReceiver);
            this.mSensorManager.unregisterListener(this.mSensorListener);
            DataLogger dataLogger3 = this.mSensorDataLogger;
            Objects.requireNonNull(dataLogger3);
            dataLogger3.mDataQueue.clear();
            this.mServiceRunning = false;
        }
        this.mSensorManager.unregisterListener(this.mSensorListener);
        DataLogger dataLogger4 = this.mSensorDataLogger;
        Objects.requireNonNull(dataLogger4);
        dataLogger4.mDataQueue.clear();
    }
}
