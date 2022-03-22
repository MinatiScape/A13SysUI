package com.google.android.systemui.elmyra.sensors;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.hardware.location.ContextHubClient;
import android.hardware.location.ContextHubClientCallback;
import android.hardware.location.ContextHubInfo;
import android.hardware.location.ContextHubManager;
import android.hardware.location.NanoAppMessage;
import android.os.Binder;
import android.util.Log;
import android.util.Slog;
import android.util.TypedValue;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline1;
import com.android.keyguard.LockIconView$$ExternalSyntheticOutline0;
import com.android.systemui.Dumpable;
import com.android.systemui.battery.BatteryMeterViewController$$ExternalSyntheticLambda0;
import com.google.android.systemui.elmyra.SnapshotConfiguration;
import com.google.android.systemui.elmyra.SnapshotController;
import com.google.android.systemui.elmyra.SnapshotLogger;
import com.google.android.systemui.elmyra.WestworldLogger;
import com.google.android.systemui.elmyra.proto.nano.ChassisProtos$Chassis;
import com.google.android.systemui.elmyra.proto.nano.ContextHubMessages$GestureDetected;
import com.google.android.systemui.elmyra.proto.nano.ContextHubMessages$GestureProgress;
import com.google.android.systemui.elmyra.proto.nano.ContextHubMessages$RecognizerStart;
import com.google.android.systemui.elmyra.proto.nano.SnapshotProtos$Snapshot;
import com.google.android.systemui.elmyra.proto.nano.SnapshotProtos$Snapshots;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import com.google.android.systemui.elmyra.sensors.config.GestureConfiguration;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class CHREGestureSensor implements Dumpable, GestureSensor {
    public final Context mContext;
    public ContextHubClient mContextHubClient;
    public final AnonymousClass1 mContextHubClientCallback = new ContextHubClientCallback() { // from class: com.google.android.systemui.elmyra.sensors.CHREGestureSensor.1
        public final void onHubReset(ContextHubClient contextHubClient) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("HubReset: ");
            m.append(contextHubClient.getAttachedHub().getId());
            Log.d("Elmyra/GestureSensor", m.toString());
        }

        public final void onMessageFromNanoApp(ContextHubClient contextHubClient, NanoAppMessage nanoAppMessage) {
            if (nanoAppMessage.getNanoAppId() == 5147455389092024334L) {
                try {
                    int messageType = nanoAppMessage.getMessageType();
                    if (messageType != 1) {
                        switch (messageType) {
                            case 300:
                                byte[] messageBody = nanoAppMessage.getMessageBody();
                                ContextHubMessages$GestureProgress contextHubMessages$GestureProgress = new ContextHubMessages$GestureProgress();
                                MessageNano.mergeFrom(contextHubMessages$GestureProgress, messageBody);
                                CHREGestureSensor.this.mController.onGestureProgress(contextHubMessages$GestureProgress.progress);
                                break;
                            case 301:
                                byte[] messageBody2 = nanoAppMessage.getMessageBody();
                                ContextHubMessages$GestureDetected contextHubMessages$GestureDetected = new ContextHubMessages$GestureDetected();
                                MessageNano.mergeFrom(contextHubMessages$GestureDetected, messageBody2);
                                CHREGestureSensor.this.mController.onGestureDetected(new GestureSensor.DetectionProperties(contextHubMessages$GestureDetected.hapticConsumed, contextHubMessages$GestureDetected.hostSuspended));
                                break;
                            case 302:
                                byte[] messageBody3 = nanoAppMessage.getMessageBody();
                                SnapshotProtos$Snapshot snapshotProtos$Snapshot = new SnapshotProtos$Snapshot();
                                MessageNano.mergeFrom(snapshotProtos$Snapshot, messageBody3);
                                snapshotProtos$Snapshot.sensitivitySetting = CHREGestureSensor.this.mGestureConfiguration.getSensitivity();
                                CHREGestureSensor.this.mController.onSnapshotReceived(snapshotProtos$Snapshot);
                                break;
                            case 303:
                                byte[] messageBody4 = nanoAppMessage.getMessageBody();
                                ChassisProtos$Chassis chassisProtos$Chassis = new ChassisProtos$Chassis();
                                MessageNano.mergeFrom(chassisProtos$Chassis, messageBody4);
                                AssistGestureController assistGestureController = CHREGestureSensor.this.mController;
                                Objects.requireNonNull(assistGestureController);
                                assistGestureController.mChassis = chassisProtos$Chassis;
                                WestworldLogger westworldLogger = assistGestureController.mWestworldLogger;
                                Objects.requireNonNull(westworldLogger);
                                westworldLogger.mChassis = chassisProtos$Chassis;
                                break;
                            case 304:
                            case 305:
                                break;
                            default:
                                Log.e("Elmyra/GestureSensor", "Unknown message type: " + nanoAppMessage.getMessageType());
                                break;
                        }
                    } else {
                        CHREGestureSensor cHREGestureSensor = CHREGestureSensor.this;
                        if (cHREGestureSensor.mIsListening) {
                            cHREGestureSensor.startRecognizer();
                        }
                    }
                } catch (InvalidProtocolBufferNanoException e) {
                    Log.e("Elmyra/GestureSensor", "Invalid protocol buffer", e);
                }
            }
        }

        public final void onNanoAppAborted(ContextHubClient contextHubClient, long j, int i) {
            if (j == 5147455389092024334L) {
                KeyguardUpdateMonitor$$ExternalSyntheticOutline1.m("Nanoapp aborted, code: ", i, "Elmyra/GestureSensor");
            }
        }
    };
    public int mContextHubRetryCount;
    public final AssistGestureController mController;
    public final GestureConfiguration mGestureConfiguration;
    public boolean mIsListening;
    public final float mProgressDetectThreshold;

    @Override // com.google.android.systemui.elmyra.sensors.Sensor
    public final void startListening() {
        this.mIsListening = true;
        startRecognizer();
    }

    @Override // com.google.android.systemui.elmyra.sensors.Sensor
    public final void stopListening() {
        sendMessageToNanoApp(201, new byte[0]);
        this.mIsListening = false;
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        long clearCallingIdentity;
        StringBuilder m = LockIconView$$ExternalSyntheticOutline0.m(printWriter, "CHREGestureSensor state:", "  mIsListening: ");
        m.append(this.mIsListening);
        printWriter.println(m.toString());
        if (this.mContextHubClient == null) {
            printWriter.println("  mContextHubClient is null. Likely no context hubs were found");
        }
        StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("  mContextHubRetryCount: ");
        m2.append(this.mContextHubRetryCount);
        printWriter.println(m2.toString());
        AssistGestureController assistGestureController = this.mController;
        Objects.requireNonNull(assistGestureController);
        if (assistGestureController.mChassis != null) {
            for (int i = 0; i < assistGestureController.mChassis.sensors.length; i++) {
                printWriter.print("sensors {");
                printWriter.print("  source: " + assistGestureController.mChassis.sensors[i].source);
                printWriter.print("  gain: " + assistGestureController.mChassis.sensors[i].gain);
                printWriter.print("  sensitivity: " + assistGestureController.mChassis.sensors[i].sensitivity);
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
            SnapshotLogger snapshotLogger = assistGestureController.mCompleteGestures;
            Objects.requireNonNull(snapshotLogger);
            clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                snapshotLogger.dumpInternal(printWriter);
                Binder.restoreCallingIdentity(clearCallingIdentity);
                SnapshotLogger snapshotLogger2 = assistGestureController.mIncompleteGestures;
                Objects.requireNonNull(snapshotLogger2);
                clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    snapshotLogger2.dumpInternal(printWriter);
                } finally {
                }
            } finally {
            }
        } else {
            SnapshotLogger snapshotLogger3 = assistGestureController.mIncompleteGestures;
            Objects.requireNonNull(snapshotLogger3);
            ArrayList arrayList = snapshotLogger3.mSnapshots;
            SnapshotLogger snapshotLogger4 = assistGestureController.mCompleteGestures;
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
                    } catch (IOException unused) {
                        Slog.e("Elmyra/AssistGestureController", "Error writing to output stream");
                    }
                } finally {
                    SnapshotLogger snapshotLogger5 = assistGestureController.mCompleteGestures;
                    Objects.requireNonNull(snapshotLogger5);
                    snapshotLogger5.mSnapshots.clear();
                    SnapshotLogger snapshotLogger6 = assistGestureController.mIncompleteGestures;
                    Objects.requireNonNull(snapshotLogger6);
                    snapshotLogger6.mSnapshots.clear();
                }
            }
        }
        StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("user_sensitivity: ");
        m3.append(assistGestureController.mGestureConfiguration.getSensitivity());
        printWriter.println(m3.toString());
    }

    public final void initializeContextHubClientIfNull() {
        if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.context_hub") && this.mContextHubClient == null) {
            ContextHubManager contextHubManager = (ContextHubManager) this.mContext.getSystemService("contexthub");
            List contextHubs = contextHubManager.getContextHubs();
            if (contextHubs.size() == 0) {
                Log.e("Elmyra/GestureSensor", "No context hubs found");
                return;
            }
            this.mContextHubClient = contextHubManager.createClient((ContextHubInfo) contextHubs.get(0), this.mContextHubClientCallback);
            this.mContextHubRetryCount++;
        }
    }

    @Override // com.google.android.systemui.elmyra.sensors.GestureSensor
    public final void setGestureListener(GestureSensor.Listener listener) {
        AssistGestureController assistGestureController = this.mController;
        Objects.requireNonNull(assistGestureController);
        assistGestureController.mGestureListener = listener;
    }

    public final void startRecognizer() {
        ContextHubMessages$RecognizerStart contextHubMessages$RecognizerStart = new ContextHubMessages$RecognizerStart();
        contextHubMessages$RecognizerStart.progressReportThreshold = this.mProgressDetectThreshold;
        contextHubMessages$RecognizerStart.sensitivity = this.mGestureConfiguration.getSensitivity();
        sendMessageToNanoApp(200, MessageNano.toByteArray(contextHubMessages$RecognizerStart));
        AssistGestureController assistGestureController = this.mController;
        Objects.requireNonNull(assistGestureController);
        if (assistGestureController.mChassis == null) {
            sendMessageToNanoApp(204, new byte[0]);
        }
    }

    public CHREGestureSensor(Context context, GestureConfiguration gestureConfiguration, SnapshotConfiguration snapshotConfiguration) {
        this.mContext = context;
        TypedValue typedValue = new TypedValue();
        context.getResources().getValue(2131165702, typedValue, true);
        this.mProgressDetectThreshold = typedValue.getFloat();
        AssistGestureController assistGestureController = new AssistGestureController(context, this, gestureConfiguration, snapshotConfiguration);
        this.mController = assistGestureController;
        CHREGestureSensor$$ExternalSyntheticLambda0 cHREGestureSensor$$ExternalSyntheticLambda0 = new CHREGestureSensor$$ExternalSyntheticLambda0(this);
        SnapshotController snapshotController = assistGestureController.mSnapshotController;
        if (snapshotController != null) {
            snapshotController.mSnapshotListener = cHREGestureSensor$$ExternalSyntheticLambda0;
        }
        this.mGestureConfiguration = gestureConfiguration;
        gestureConfiguration.mListener = new BatteryMeterViewController$$ExternalSyntheticLambda0(this);
        initializeContextHubClientIfNull();
    }

    public final void sendMessageToNanoApp(int i, byte[] bArr) {
        initializeContextHubClientIfNull();
        if (this.mContextHubClient == null) {
            Log.e("Elmyra/GestureSensor", "ContextHubClient null");
            return;
        }
        int sendMessageToNanoApp = this.mContextHubClient.sendMessageToNanoApp(NanoAppMessage.createMessageToNanoApp(5147455389092024334L, i, bArr));
        if (sendMessageToNanoApp != 0) {
            Log.e("Elmyra/GestureSensor", String.format("Unable to send message %d to nanoapp, error code %d", Integer.valueOf(i), Integer.valueOf(sendMessageToNanoApp)));
        }
    }

    @Override // com.google.android.systemui.elmyra.sensors.Sensor
    public final boolean isListening() {
        return this.mIsListening;
    }
}
