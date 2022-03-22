package com.google.android.systemui.elmyra.sensors;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Binder;
import android.os.SystemClock;
import android.util.Slog;
import android.util.TypedValue;
import com.android.systemui.Dumpable;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.google.android.systemui.elmyra.SnapshotConfiguration;
import com.google.android.systemui.elmyra.SnapshotController;
import com.google.android.systemui.elmyra.SnapshotLogger;
import com.google.android.systemui.elmyra.WestworldLogger;
import com.google.android.systemui.elmyra.proto.nano.ChassisProtos$Chassis;
import com.google.android.systemui.elmyra.proto.nano.SnapshotProtos$Snapshot;
import com.google.android.systemui.elmyra.proto.nano.SnapshotProtos$Snapshots;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import com.google.android.systemui.elmyra.sensors.config.GestureConfiguration;
import com.google.protobuf.nano.MessageNano;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
/* loaded from: classes.dex */
public final class AssistGestureController implements Dumpable {
    public ChassisProtos$Chassis mChassis;
    public SnapshotLogger mCompleteGestures;
    public final long mFalsePrimeWindow;
    public final GestureConfiguration mGestureConfiguration;
    public final long mGestureCooldownTime;
    public GestureSensor.Listener mGestureListener;
    public float mGestureProgress;
    public final GestureSensor mGestureSensor;
    public SnapshotLogger mIncompleteGestures;
    public boolean mIsFalsePrimed;
    public long mLastDetectionTime;
    public OPAQueryReceiver mOpaQueryReceiver = new OPAQueryReceiver();
    public final float mProgressAlpha;
    public final float mProgressReportThreshold;
    public final SnapshotController mSnapshotController;
    public WestworldLogger mWestworldLogger;

    /* loaded from: classes.dex */
    public class OPAQueryReceiver extends BroadcastReceiver {
        public OPAQueryReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.google.android.systemui.OPA_ELMYRA_QUERY_SUBMITTED")) {
                SnapshotLogger snapshotLogger = AssistGestureController.this.mCompleteGestures;
                Objects.requireNonNull(snapshotLogger);
                if (snapshotLogger.mSnapshots.size() > 0) {
                    ArrayList arrayList = snapshotLogger.mSnapshots;
                    SnapshotLogger.Snapshot snapshot = (SnapshotLogger.Snapshot) arrayList.get(arrayList.size() - 1);
                    Objects.requireNonNull(snapshot);
                    snapshot.mSnapshot.header.feedback = 1;
                }
                Objects.requireNonNull(AssistGestureController.this.mWestworldLogger);
                SysUiStatsLog.write(175, 2);
            }
        }
    }

    public final void onGestureProgress(float f) {
        if (f == 0.0f) {
            this.mGestureProgress = 0.0f;
            this.mIsFalsePrimed = false;
        } else {
            float f2 = this.mProgressAlpha;
            this.mGestureProgress = ((1.0f - f2) * this.mGestureProgress) + (f2 * f);
        }
        long uptimeMillis = SystemClock.uptimeMillis() - this.mLastDetectionTime;
        if (uptimeMillis >= this.mGestureCooldownTime && !this.mIsFalsePrimed) {
            int i = (uptimeMillis > this.mFalsePrimeWindow ? 1 : (uptimeMillis == this.mFalsePrimeWindow ? 0 : -1));
            int i2 = 1;
            if (i >= 0 || f != 1.0f) {
                float f3 = this.mGestureProgress;
                float f4 = this.mProgressReportThreshold;
                if (f3 < f4) {
                    GestureSensor.Listener listener = this.mGestureListener;
                    if (listener != null) {
                        listener.onGestureProgress(0.0f, 0);
                    }
                    SnapshotController snapshotController = this.mSnapshotController;
                    if (snapshotController != null) {
                        snapshotController.onGestureProgress(0.0f, 0);
                    }
                    this.mWestworldLogger.onGestureProgress(0.0f, 0);
                    return;
                }
                float f5 = (f3 - f4) / (1.0f - f4);
                if (f == 1.0f) {
                    i2 = 2;
                }
                GestureSensor.Listener listener2 = this.mGestureListener;
                if (listener2 != null) {
                    listener2.onGestureProgress(f5, i2);
                }
                SnapshotController snapshotController2 = this.mSnapshotController;
                if (snapshotController2 != null) {
                    snapshotController2.onGestureProgress(f5, i2);
                }
                this.mWestworldLogger.onGestureProgress(f5, i2);
                return;
            }
            this.mIsFalsePrimed = true;
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        long clearCallingIdentity;
        if (this.mChassis != null) {
            for (int i = 0; i < this.mChassis.sensors.length; i++) {
                printWriter.print("sensors {");
                printWriter.print("  source: " + this.mChassis.sensors[i].source);
                printWriter.print("  gain: " + this.mChassis.sensors[i].gain);
                printWriter.print("  sensitivity: " + this.mChassis.sensors[i].sensitivity);
                printWriter.print("}");
            }
            printWriter.println();
        }
        boolean z = false;
        boolean z2 = false;
        for (String str : strArr) {
            if (str.equals("GoogleServices")) {
                z = true;
            } else if (str.equals("proto")) {
                z2 = true;
            }
        }
        if (!z || !z2) {
            SnapshotLogger snapshotLogger = this.mCompleteGestures;
            Objects.requireNonNull(snapshotLogger);
            clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                snapshotLogger.dumpInternal(printWriter);
                Binder.restoreCallingIdentity(clearCallingIdentity);
                SnapshotLogger snapshotLogger2 = this.mIncompleteGestures;
                Objects.requireNonNull(snapshotLogger2);
                clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    snapshotLogger2.dumpInternal(printWriter);
                } finally {
                }
            } finally {
            }
        } else {
            SnapshotLogger snapshotLogger3 = this.mIncompleteGestures;
            Objects.requireNonNull(snapshotLogger3);
            ArrayList arrayList = snapshotLogger3.mSnapshots;
            SnapshotLogger snapshotLogger4 = this.mCompleteGestures;
            Objects.requireNonNull(snapshotLogger4);
            ArrayList arrayList2 = snapshotLogger4.mSnapshots;
            if (arrayList2.size() + arrayList.size() != 0) {
                SnapshotProtos$Snapshots snapshotProtos$Snapshots = new SnapshotProtos$Snapshots();
                snapshotProtos$Snapshots.snapshots = new SnapshotProtos$Snapshot[arrayList2.size() + arrayList.size()];
                int i2 = 0;
                while (i2 < arrayList.size()) {
                    SnapshotLogger.Snapshot snapshot = (SnapshotLogger.Snapshot) arrayList.get(i2);
                    Objects.requireNonNull(snapshot);
                    snapshotProtos$Snapshots.snapshots[i2] = snapshot.mSnapshot;
                    i2++;
                }
                for (int i3 = 0; i3 < arrayList2.size(); i3++) {
                    SnapshotLogger.Snapshot snapshot2 = (SnapshotLogger.Snapshot) arrayList2.get(i3);
                    Objects.requireNonNull(snapshot2);
                    snapshotProtos$Snapshots.snapshots[i2 + i3] = snapshot2.mSnapshot;
                }
                byte[] byteArray = MessageNano.toByteArray(snapshotProtos$Snapshots);
                FileOutputStream fileOutputStream = new FileOutputStream(fileDescriptor);
                clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    try {
                        fileOutputStream.write(byteArray);
                        fileOutputStream.flush();
                    } finally {
                        SnapshotLogger snapshotLogger5 = this.mCompleteGestures;
                        Objects.requireNonNull(snapshotLogger5);
                        snapshotLogger5.mSnapshots.clear();
                        SnapshotLogger snapshotLogger6 = this.mIncompleteGestures;
                        Objects.requireNonNull(snapshotLogger6);
                        snapshotLogger6.mSnapshots.clear();
                    }
                } catch (IOException unused) {
                    Slog.e("Elmyra/AssistGestureController", "Error writing to output stream");
                }
            }
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("user_sensitivity: ");
        m.append(this.mGestureConfiguration.getSensitivity());
        printWriter.println(m.toString());
    }

    public final void onSnapshotReceived(SnapshotProtos$Snapshot snapshotProtos$Snapshot) {
        int i = snapshotProtos$Snapshot.header.gestureType;
        if (i == 4) {
            WestworldLogger westworldLogger = this.mWestworldLogger;
            Objects.requireNonNull(westworldLogger);
            synchronized (westworldLogger.mMutex) {
                westworldLogger.mSnapshot = snapshotProtos$Snapshot;
                CountDownLatch countDownLatch = westworldLogger.mCountDownLatch;
                if (countDownLatch != null) {
                    countDownLatch.countDown();
                }
            }
        } else if (i == 1) {
            this.mCompleteGestures.addSnapshot(snapshotProtos$Snapshot, System.currentTimeMillis());
        } else {
            this.mIncompleteGestures.addSnapshot(snapshotProtos$Snapshot, System.currentTimeMillis());
        }
    }

    public AssistGestureController(Context context, GestureSensor gestureSensor, GestureConfiguration gestureConfiguration, SnapshotConfiguration snapshotConfiguration) {
        int i;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.google.android.systemui.OPA_ELMYRA_QUERY_SUBMITTED");
        context.registerReceiver(this.mOpaQueryReceiver, intentFilter, 2);
        this.mGestureSensor = gestureSensor;
        this.mGestureConfiguration = gestureConfiguration;
        Resources resources = context.getResources();
        TypedValue typedValue = new TypedValue();
        int i2 = 0;
        if (snapshotConfiguration != null) {
            i = snapshotConfiguration.mCompleteGestures;
        } else {
            i = 0;
        }
        this.mCompleteGestures = new SnapshotLogger(i);
        this.mIncompleteGestures = new SnapshotLogger(snapshotConfiguration != null ? snapshotConfiguration.mIncompleteGestures : i2);
        resources.getValue(2131165701, typedValue, true);
        this.mProgressAlpha = typedValue.getFloat();
        resources.getValue(2131165703, typedValue, true);
        this.mProgressReportThreshold = typedValue.getFloat();
        long integer = resources.getInteger(2131492923);
        this.mGestureCooldownTime = integer;
        this.mFalsePrimeWindow = integer + resources.getInteger(2131492922);
        if (snapshotConfiguration != null) {
            this.mSnapshotController = new SnapshotController(snapshotConfiguration);
        } else {
            this.mSnapshotController = null;
        }
        this.mWestworldLogger = new WestworldLogger(context, gestureConfiguration, this.mSnapshotController);
    }

    public final void onGestureDetected(GestureSensor.DetectionProperties detectionProperties) {
        long uptimeMillis = SystemClock.uptimeMillis();
        if (uptimeMillis - this.mLastDetectionTime >= this.mGestureCooldownTime && !this.mIsFalsePrimed) {
            GestureSensor.Listener listener = this.mGestureListener;
            if (listener != null) {
                listener.onGestureDetected(detectionProperties);
            }
            SnapshotController snapshotController = this.mSnapshotController;
            if (snapshotController != null) {
                snapshotController.onGestureDetected(detectionProperties);
            }
            this.mWestworldLogger.onGestureDetected(detectionProperties);
            this.mLastDetectionTime = uptimeMillis;
        }
    }
}
