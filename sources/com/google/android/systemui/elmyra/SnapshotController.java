package com.google.android.systemui.elmyra;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.google.android.systemui.elmyra.proto.nano.SnapshotProtos$SnapshotHeader;
import com.google.android.systemui.elmyra.sensors.CHREGestureSensor;
import com.google.android.systemui.elmyra.sensors.CHREGestureSensor$$ExternalSyntheticLambda0;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import com.google.protobuf.nano.MessageNano;
import java.util.Objects;
import java.util.Random;
/* loaded from: classes.dex */
public final class SnapshotController implements GestureSensor.Listener {
    public final int mSnapshotDelayAfterGesture;
    public Listener mSnapshotListener;
    public int mLastGestureStage = 0;
    public final AnonymousClass1 mHandler = new Handler(Looper.getMainLooper()) { // from class: com.google.android.systemui.elmyra.SnapshotController.1
        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            if (message.what == 1) {
                SnapshotController snapshotController = SnapshotController.this;
                SnapshotProtos$SnapshotHeader snapshotProtos$SnapshotHeader = (SnapshotProtos$SnapshotHeader) message.obj;
                Objects.requireNonNull(snapshotController);
                Listener listener = snapshotController.mSnapshotListener;
                if (listener != null) {
                    CHREGestureSensor cHREGestureSensor = (CHREGestureSensor) ((CHREGestureSensor$$ExternalSyntheticLambda0) listener).f$0;
                    Objects.requireNonNull(cHREGestureSensor);
                    cHREGestureSensor.sendMessageToNanoApp(203, MessageNano.toByteArray(snapshotProtos$SnapshotHeader));
                }
            }
        }
    };

    /* loaded from: classes.dex */
    public interface Listener {
    }

    @Override // com.google.android.systemui.elmyra.sensors.GestureSensor.Listener
    public final void onGestureDetected(GestureSensor.DetectionProperties detectionProperties) {
        long j;
        SnapshotProtos$SnapshotHeader snapshotProtos$SnapshotHeader = new SnapshotProtos$SnapshotHeader();
        snapshotProtos$SnapshotHeader.gestureType = 1;
        if (detectionProperties != null) {
            j = detectionProperties.mActionId;
        } else {
            j = 0;
        }
        snapshotProtos$SnapshotHeader.identifier = j;
        this.mLastGestureStage = 0;
        AnonymousClass1 r5 = this.mHandler;
        r5.sendMessageDelayed(r5.obtainMessage(1, snapshotProtos$SnapshotHeader), this.mSnapshotDelayAfterGesture);
    }

    @Override // com.google.android.systemui.elmyra.sensors.GestureSensor.Listener
    public final void onGestureProgress(float f, int i) {
        if (this.mLastGestureStage == 2 && i != 2) {
            SnapshotProtos$SnapshotHeader snapshotProtos$SnapshotHeader = new SnapshotProtos$SnapshotHeader();
            snapshotProtos$SnapshotHeader.identifier = new Random().nextLong();
            snapshotProtos$SnapshotHeader.gestureType = 2;
            Listener listener = this.mSnapshotListener;
            if (listener != null) {
                CHREGestureSensor cHREGestureSensor = (CHREGestureSensor) ((CHREGestureSensor$$ExternalSyntheticLambda0) listener).f$0;
                Objects.requireNonNull(cHREGestureSensor);
                cHREGestureSensor.sendMessageToNanoApp(203, MessageNano.toByteArray(snapshotProtos$SnapshotHeader));
            }
        }
        this.mLastGestureStage = i;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.google.android.systemui.elmyra.SnapshotController$1] */
    public SnapshotController(SnapshotConfiguration snapshotConfiguration) {
        Objects.requireNonNull(snapshotConfiguration);
        this.mSnapshotDelayAfterGesture = snapshotConfiguration.mSnapshotDelayAfterGesture;
    }
}
