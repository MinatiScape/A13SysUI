package com.google.android.systemui.elmyra;

import android.app.StatsManager;
import android.content.Context;
import android.util.Log;
import android.util.StatsEvent;
import com.android.internal.util.ConcurrentUtils;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.google.android.systemui.elmyra.SnapshotController;
import com.google.android.systemui.elmyra.proto.nano.ChassisProtos$Chassis;
import com.google.android.systemui.elmyra.proto.nano.SnapshotProtos$Snapshot;
import com.google.android.systemui.elmyra.proto.nano.SnapshotProtos$SnapshotHeader;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import com.google.android.systemui.elmyra.sensors.config.GestureConfiguration;
import com.google.protobuf.nano.MessageNano;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public final class WestworldLogger implements GestureSensor.Listener {
    public CountDownLatch mCountDownLatch;
    public GestureConfiguration mGestureConfiguration;
    public SnapshotController mSnapshotController;
    public final WestworldLogger$$ExternalSyntheticLambda0 mWestworldCallback;
    public ChassisProtos$Chassis mChassis = null;
    public SnapshotProtos$Snapshot mSnapshot = null;
    public Object mMutex = new Object();

    @Override // com.google.android.systemui.elmyra.sensors.GestureSensor.Listener
    public final void onGestureDetected(GestureSensor.DetectionProperties detectionProperties) {
        SysUiStatsLog.write(174, 3);
    }

    @Override // com.google.android.systemui.elmyra.sensors.GestureSensor.Listener
    public final void onGestureProgress(float f, int i) {
        SysUiStatsLog.write(176, (int) (f * 100.0f));
        SysUiStatsLog.write(174, i);
    }

    public WestworldLogger(Context context, GestureConfiguration gestureConfiguration, SnapshotController snapshotController) {
        StatsManager.StatsPullAtomCallback westworldLogger$$ExternalSyntheticLambda0 = new StatsManager.StatsPullAtomCallback() { // from class: com.google.android.systemui.elmyra.WestworldLogger$$ExternalSyntheticLambda0
            public final int onPullAtom(int i, List list) {
                WestworldLogger westworldLogger = WestworldLogger.this;
                Objects.requireNonNull(westworldLogger);
                Log.d("Elmyra/Logger", "Receiving pull request from statsd.");
                int i2 = 1;
                if (westworldLogger.mSnapshotController == null) {
                    Log.d("Elmyra/Logger", "Snapshot Controller is null, returning.");
                } else {
                    synchronized (westworldLogger.mMutex) {
                        if (westworldLogger.mCountDownLatch == null) {
                            westworldLogger.mCountDownLatch = new CountDownLatch(1);
                            SnapshotController snapshotController2 = westworldLogger.mSnapshotController;
                            Objects.requireNonNull(snapshotController2);
                            SnapshotProtos$SnapshotHeader snapshotProtos$SnapshotHeader = new SnapshotProtos$SnapshotHeader();
                            snapshotProtos$SnapshotHeader.gestureType = 4;
                            snapshotProtos$SnapshotHeader.identifier = 0L;
                            SnapshotController.AnonymousClass1 r0 = snapshotController2.mHandler;
                            r0.sendMessage(r0.obtainMessage(1, snapshotProtos$SnapshotHeader));
                            try {
                                long currentTimeMillis = System.currentTimeMillis();
                                westworldLogger.mCountDownLatch.await(50L, TimeUnit.MILLISECONDS);
                                Log.d("Elmyra/Logger", "Snapshot took " + Long.toString(System.currentTimeMillis() - currentTimeMillis) + " milliseconds.");
                            } catch (IllegalMonitorStateException e) {
                                Log.d("Elmyra/Logger", e.getMessage());
                            } catch (InterruptedException e2) {
                                Log.d("Elmyra/Logger", e2.getMessage());
                            }
                            synchronized (westworldLogger.mMutex) {
                                if (!(westworldLogger.mSnapshot == null || westworldLogger.mChassis == null)) {
                                    float sensitivity = westworldLogger.mGestureConfiguration.getSensitivity();
                                    SnapshotProtos$Snapshot snapshotProtos$Snapshot = westworldLogger.mSnapshot;
                                    snapshotProtos$Snapshot.sensitivitySetting = sensitivity;
                                    list.add(StatsEvent.newBuilder().setAtomId(i).writeByteArray(MessageNano.toByteArray(snapshotProtos$Snapshot)).writeByteArray(MessageNano.toByteArray(westworldLogger.mChassis)).build());
                                    westworldLogger.mSnapshot = null;
                                    synchronized (westworldLogger.mMutex) {
                                        westworldLogger.mCountDownLatch = null;
                                        westworldLogger.mSnapshot = null;
                                    }
                                    i2 = 0;
                                }
                                westworldLogger.mCountDownLatch = null;
                            }
                        }
                    }
                }
                return i2;
            }
        };
        this.mWestworldCallback = westworldLogger$$ExternalSyntheticLambda0;
        this.mGestureConfiguration = gestureConfiguration;
        this.mSnapshotController = snapshotController;
        StatsManager statsManager = (StatsManager) context.getSystemService("stats");
        if (statsManager == null) {
            Log.d("Elmyra/Logger", "Failed to get StatsManager");
        }
        try {
            statsManager.setPullAtomCallback(150000, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, westworldLogger$$ExternalSyntheticLambda0);
        } catch (RuntimeException e) {
            Log.d("Elmyra/Logger", "Failed to register callback with StatsManager");
            e.printStackTrace();
        }
    }
}
