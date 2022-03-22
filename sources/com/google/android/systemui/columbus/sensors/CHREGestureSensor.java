package com.google.android.systemui.columbus.sensors;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.hardware.google.pixel.vendor.PixelAtoms$DoubleTapNanoappEventReported$Type;
import android.hardware.location.ContextHubClient;
import android.hardware.location.ContextHubClientCallback;
import android.hardware.location.ContextHubInfo;
import android.hardware.location.ContextHubManager;
import android.hardware.location.NanoAppMessage;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.util.StatsEvent;
import android.util.StatsLog;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.util.RingBuffer;
import com.android.systemui.Dumpable;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.google.android.systemui.columbus.proto.nano.ColumbusProto$GestureDetected;
import com.google.android.systemui.columbus.proto.nano.ColumbusProto$NanoappEvent;
import com.google.android.systemui.columbus.proto.nano.ColumbusProto$NanoappEvents;
import com.google.android.systemui.columbus.proto.nano.ColumbusProto$RecognizerStart;
import com.google.android.systemui.columbus.proto.nano.ColumbusProto$ScreenStateUpdate;
import com.google.android.systemui.columbus.proto.nano.ColumbusProto$SensitivityUpdate;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import com.google.android.systemui.columbus.sensors.config.GestureConfiguration;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: CHREGestureSensor.kt */
/* loaded from: classes.dex */
public final class CHREGestureSensor extends GestureSensor implements Dumpable {
    public final Handler bgHandler;
    public final Context context;
    public ContextHubClient contextHubClient;
    public final GestureConfiguration gestureConfiguration;
    public boolean isAwake;
    public boolean isDozing;
    public boolean isListening;
    public boolean screenOn;
    public boolean screenStateUpdated;
    public final UiEventLogger uiEventLogger;
    public final FeatureVectorDumper featureVectorDumper = new FeatureVectorDumper();
    public final CHREGestureSensor$contextHubClientCallback$1 contextHubClientCallback = new ContextHubClientCallback() { // from class: com.google.android.systemui.columbus.sensors.CHREGestureSensor$contextHubClientCallback$1
        public final void onMessageFromNanoApp(ContextHubClient contextHubClient, NanoAppMessage nanoAppMessage) {
            if (nanoAppMessage.getNanoAppId() == 5147455389092024345L) {
                try {
                    int messageType = nanoAppMessage.getMessageType();
                    if (messageType == 300) {
                        CHREGestureSensor cHREGestureSensor = CHREGestureSensor.this;
                        byte[] messageBody = nanoAppMessage.getMessageBody();
                        ColumbusProto$GestureDetected columbusProto$GestureDetected = new ColumbusProto$GestureDetected();
                        MessageNano.mergeFrom(columbusProto$GestureDetected, messageBody);
                        CHREGestureSensor.access$handleGestureDetection(cHREGestureSensor, columbusProto$GestureDetected);
                    } else if (messageType != 500) {
                        Log.e("Columbus/GestureSensor", Intrinsics.stringPlus("Unknown message type: ", Integer.valueOf(nanoAppMessage.getMessageType())));
                    } else {
                        CHREGestureSensor cHREGestureSensor2 = CHREGestureSensor.this;
                        byte[] messageBody2 = nanoAppMessage.getMessageBody();
                        ColumbusProto$NanoappEvents columbusProto$NanoappEvents = new ColumbusProto$NanoappEvents();
                        MessageNano.mergeFrom(columbusProto$NanoappEvents, messageBody2);
                        CHREGestureSensor.access$handleNanoappEvents(cHREGestureSensor2, columbusProto$NanoappEvents);
                    }
                } catch (InvalidProtocolBufferNanoException e) {
                    Log.e("Columbus/GestureSensor", "Invalid protocol buffer", e);
                }
            }
        }

        public final void onHubReset(ContextHubClient contextHubClient) {
            Log.d("Columbus/GestureSensor", Intrinsics.stringPlus("HubReset: ", Integer.valueOf(contextHubClient.getAttachedHub().getId())));
        }

        public final void onNanoAppAborted(ContextHubClient contextHubClient, long j, int i) {
            if (j == 5147455389092024345L) {
                Log.e("Columbus/GestureSensor", Intrinsics.stringPlus("Nanoapp aborted, code: ", Integer.valueOf(i)));
            }
        }

        public final void onNanoAppLoaded(ContextHubClient contextHubClient, long j) {
            if (j == 5147455389092024345L) {
                CHREGestureSensor cHREGestureSensor = CHREGestureSensor.this;
                Objects.requireNonNull(cHREGestureSensor);
                if (cHREGestureSensor.isListening) {
                    Log.d("Columbus/GestureSensor", "Nanoapp loaded");
                    CHREGestureSensor.this.updateScreenState();
                    CHREGestureSensor.this.startRecognizer();
                }
            }
        }
    };

    /* compiled from: CHREGestureSensor.kt */
    /* loaded from: classes.dex */
    public static final class FeatureVector implements Dumpable {
        public final int gesture;
        public final long timestamp = SystemClock.elapsedRealtime();
        public final float[] vector;

        @Override // com.android.systemui.Dumpable
        public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("      Gesture: ");
            m.append(this.gesture);
            m.append(" Time: ");
            m.append(this.timestamp - SystemClock.elapsedRealtime());
            printWriter.println(m.toString());
            float[] fArr = this.vector;
            StringBuilder sb = new StringBuilder();
            sb.append((CharSequence) "[ ");
            int length = fArr.length;
            int i = 0;
            int i2 = 0;
            while (i < length) {
                float f = fArr[i];
                i++;
                i2++;
                if (i2 > 1) {
                    sb.append((CharSequence) ", ");
                }
                sb.append((CharSequence) String.valueOf(f));
            }
            sb.append((CharSequence) " ]");
            printWriter.println(Intrinsics.stringPlus("      ", sb.toString()));
        }

        public FeatureVector(ColumbusProto$GestureDetected columbusProto$GestureDetected) {
            this.vector = columbusProto$GestureDetected.featureVector;
            this.gesture = columbusProto$GestureDetected.gestureType;
        }
    }

    /* compiled from: CHREGestureSensor.kt */
    /* loaded from: classes.dex */
    public static final class FeatureVectorDumper implements Dumpable {
        public final RingBuffer<FeatureVector> featureVectors = new RingBuffer<>(FeatureVector.class, 10);
        public FeatureVector lastSingleTapFeatureVector;

        @Override // com.android.systemui.Dumpable
        public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            printWriter.println("    Feature Vectors:");
            Object[] array = this.featureVectors.toArray();
            int length = array.length;
            int i = 0;
            while (i < length) {
                Object obj = array[i];
                i++;
                FeatureVector featureVector = (FeatureVector) obj;
                Objects.requireNonNull(featureVector);
                long elapsedRealtime = featureVector.timestamp - SystemClock.elapsedRealtime();
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("      Gesture: ");
                m.append(featureVector.gesture);
                m.append(" Time: ");
                m.append(elapsedRealtime);
                printWriter.println(m.toString());
                float[] fArr = featureVector.vector;
                StringBuilder sb = new StringBuilder();
                sb.append((CharSequence) "[ ");
                int length2 = fArr.length;
                int i2 = 0;
                int i3 = 0;
                while (i2 < length2) {
                    float f = fArr[i2];
                    i2++;
                    i3++;
                    if (i3 > 1) {
                        sb.append((CharSequence) ", ");
                    }
                    sb.append((CharSequence) String.valueOf(f));
                }
                sb.append((CharSequence) " ]");
                printWriter.println(Intrinsics.stringPlus("      ", sb.toString()));
            }
        }
    }

    @Override // com.google.android.systemui.columbus.sensors.Sensor
    public final void startListening() {
        this.isListening = true;
        startRecognizer();
        sendScreenState();
    }

    @Override // com.google.android.systemui.columbus.sensors.Sensor
    public final void stopListening() {
        sendMessageToNanoApp(101, new byte[0], new CHREGestureSensor$stopListening$1(this), null);
        this.isListening = false;
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        FeatureVectorDumper featureVectorDumper = this.featureVectorDumper;
        Objects.requireNonNull(featureVectorDumper);
        printWriter.println("    Feature Vectors:");
        Object[] array = featureVectorDumper.featureVectors.toArray();
        int length = array.length;
        int i = 0;
        while (i < length) {
            Object obj = array[i];
            i++;
            FeatureVector featureVector = (FeatureVector) obj;
            Objects.requireNonNull(featureVector);
            long elapsedRealtime = featureVector.timestamp - SystemClock.elapsedRealtime();
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("      Gesture: ");
            m.append(featureVector.gesture);
            m.append(" Time: ");
            m.append(elapsedRealtime);
            printWriter.println(m.toString());
            float[] fArr = featureVector.vector;
            StringBuilder sb = new StringBuilder();
            sb.append((CharSequence) "[ ");
            int length2 = fArr.length;
            int i2 = 0;
            int i3 = 0;
            while (i2 < length2) {
                float f = fArr[i2];
                i2++;
                i3++;
                if (i3 > 1) {
                    sb.append((CharSequence) ", ");
                }
                sb.append((CharSequence) String.valueOf(f));
            }
            sb.append((CharSequence) " ]");
            printWriter.println(Intrinsics.stringPlus("      ", sb.toString()));
        }
    }

    public final void initializeContextHubClientIfNull() {
        List list;
        int i;
        if (this.contextHubClient == null) {
            ContextHubManager contextHubManager = (ContextHubManager) this.context.getSystemService("contexthub");
            if (contextHubManager == null) {
                list = null;
            } else {
                list = contextHubManager.getContextHubs();
            }
            if (list == null) {
                i = 0;
            } else {
                i = list.size();
            }
            if (i == 0) {
                Log.e("Columbus/GestureSensor", "No context hubs found");
            } else if (list != null) {
                this.contextHubClient = contextHubManager.createClient((ContextHubInfo) list.get(0), this.contextHubClientCallback);
            }
        }
    }

    public final void sendScreenState() {
        int i;
        ColumbusProto$ScreenStateUpdate columbusProto$ScreenStateUpdate = new ColumbusProto$ScreenStateUpdate();
        if (this.screenOn) {
            i = 1;
        } else {
            i = 2;
        }
        columbusProto$ScreenStateUpdate.screenState = i;
        sendMessageToNanoApp(400, MessageNano.toByteArray(columbusProto$ScreenStateUpdate), new CHREGestureSensor$sendScreenState$1(this), new CHREGestureSensor$sendScreenState$2(this));
    }

    public final void startRecognizer() {
        ColumbusProto$RecognizerStart columbusProto$RecognizerStart = new ColumbusProto$RecognizerStart();
        GestureConfiguration gestureConfiguration = this.gestureConfiguration;
        Objects.requireNonNull(gestureConfiguration);
        columbusProto$RecognizerStart.sensitivity = gestureConfiguration.sensitivity;
        sendMessageToNanoApp(100, MessageNano.toByteArray(columbusProto$RecognizerStart), new CHREGestureSensor$startRecognizer$1(this), null);
    }

    public final void updateScreenState() {
        boolean z;
        if (!this.isAwake || this.isDozing) {
            z = false;
        } else {
            z = true;
        }
        if (this.screenOn != z || !this.screenStateUpdated) {
            this.screenOn = z;
            if (this.isListening) {
                sendScreenState();
            }
        }
    }

    public CHREGestureSensor(Context context, UiEventLogger uiEventLogger, GestureConfiguration gestureConfiguration, StatusBarStateController statusBarStateController, WakefulnessLifecycle wakefulnessLifecycle, Handler handler) {
        boolean z;
        this.context = context;
        this.uiEventLogger = uiEventLogger;
        this.gestureConfiguration = gestureConfiguration;
        this.bgHandler = handler;
        StatusBarStateController.StateListener cHREGestureSensor$statusBarStateListener$1 = new StatusBarStateController.StateListener() { // from class: com.google.android.systemui.columbus.sensors.CHREGestureSensor$statusBarStateListener$1
            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public final void onDozingChanged(boolean z2) {
                CHREGestureSensor cHREGestureSensor = CHREGestureSensor.this;
                Objects.requireNonNull(cHREGestureSensor);
                if (cHREGestureSensor.isDozing != z2) {
                    cHREGestureSensor.isDozing = z2;
                    cHREGestureSensor.updateScreenState();
                }
            }
        };
        WakefulnessLifecycle.Observer cHREGestureSensor$wakefulnessLifecycleObserver$1 = new WakefulnessLifecycle.Observer() { // from class: com.google.android.systemui.columbus.sensors.CHREGestureSensor$wakefulnessLifecycleObserver$1
            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public final void onFinishedGoingToSleep() {
                CHREGestureSensor.access$handleWakefullnessChanged(CHREGestureSensor.this, false);
            }

            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public final void onFinishedWakingUp() {
                CHREGestureSensor.access$handleWakefullnessChanged(CHREGestureSensor.this, true);
            }

            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public final void onStartedGoingToSleep() {
                CHREGestureSensor.access$handleWakefullnessChanged(CHREGestureSensor.this, false);
            }

            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public final void onStartedWakingUp() {
                CHREGestureSensor.access$handleWakefullnessChanged(CHREGestureSensor.this, false);
            }
        };
        boolean isDozing = statusBarStateController.isDozing();
        this.isDozing = isDozing;
        boolean z2 = false;
        if (wakefulnessLifecycle.mWakefulness == 2) {
            z = true;
        } else {
            z = false;
        }
        this.isAwake = z;
        if (z && !isDozing) {
            z2 = true;
        }
        this.screenOn = z2;
        this.screenStateUpdated = true;
        GestureConfiguration.Listener listener = new GestureConfiguration.Listener() { // from class: com.google.android.systemui.columbus.sensors.CHREGestureSensor.1
            @Override // com.google.android.systemui.columbus.sensors.config.GestureConfiguration.Listener
            public final void onGestureConfigurationChanged(GestureConfiguration gestureConfiguration2) {
                CHREGestureSensor cHREGestureSensor = CHREGestureSensor.this;
                Objects.requireNonNull(cHREGestureSensor);
                ColumbusProto$SensitivityUpdate columbusProto$SensitivityUpdate = new ColumbusProto$SensitivityUpdate();
                columbusProto$SensitivityUpdate.sensitivity = gestureConfiguration2.sensitivity;
                cHREGestureSensor.sendMessageToNanoApp(200, MessageNano.toByteArray(columbusProto$SensitivityUpdate), null, null);
            }
        };
        Objects.requireNonNull(gestureConfiguration);
        gestureConfiguration.listener = listener;
        statusBarStateController.addCallback(cHREGestureSensor$statusBarStateListener$1);
        wakefulnessLifecycle.mObservers.add(cHREGestureSensor$wakefulnessLifecycleObserver$1);
        initializeContextHubClientIfNull();
    }

    public static final void access$handleGestureDetection(CHREGestureSensor cHREGestureSensor, ColumbusProto$GestureDetected columbusProto$GestureDetected) {
        boolean z;
        Objects.requireNonNull(cHREGestureSensor);
        int i = columbusProto$GestureDetected.gestureType;
        int i2 = 0;
        if (i == 2) {
            z = true;
        } else {
            z = false;
        }
        if (i == 1) {
            i2 = 1;
        } else if (i == 2) {
            i2 = 2;
        }
        GestureSensor.DetectionProperties detectionProperties = new GestureSensor.DetectionProperties(z);
        GestureSensor.Listener listener = cHREGestureSensor.listener;
        if (listener != null) {
            listener.onGestureDetected(cHREGestureSensor, i2, detectionProperties);
        }
        FeatureVectorDumper featureVectorDumper = cHREGestureSensor.featureVectorDumper;
        Objects.requireNonNull(featureVectorDumper);
        int i3 = columbusProto$GestureDetected.gestureType;
        if (i3 == 1) {
            FeatureVector featureVector = featureVectorDumper.lastSingleTapFeatureVector;
            featureVectorDumper.lastSingleTapFeatureVector = null;
            if (featureVector == null) {
                Log.w("Columbus/GestureSensor", "Received double tap without single taps, event will not appear in sysdump");
                return;
            }
            featureVectorDumper.featureVectors.append(featureVector);
            featureVectorDumper.featureVectors.append(new FeatureVector(columbusProto$GestureDetected));
        } else if (i3 == 2) {
            featureVectorDumper.lastSingleTapFeatureVector = new FeatureVector(columbusProto$GestureDetected);
        }
    }

    public static final void access$handleNanoappEvents(CHREGestureSensor cHREGestureSensor, ColumbusProto$NanoappEvents columbusProto$NanoappEvents) {
        int i;
        Objects.requireNonNull(cHREGestureSensor);
        ColumbusProto$NanoappEvent[] columbusProto$NanoappEventArr = columbusProto$NanoappEvents.batchedEvents;
        int length = columbusProto$NanoappEventArr.length;
        int i2 = 0;
        while (i2 < length) {
            ColumbusProto$NanoappEvent columbusProto$NanoappEvent = columbusProto$NanoappEventArr[i2];
            i2++;
            StatsEvent.Builder writeLong = StatsEvent.newBuilder().setAtomId(100051).writeLong(columbusProto$NanoappEvent.timestamp);
            switch (columbusProto$NanoappEvent.type) {
                case 1:
                    i = PixelAtoms$DoubleTapNanoappEventReported$Type.GATE_START.getNumber();
                    break;
                case 2:
                    i = PixelAtoms$DoubleTapNanoappEventReported$Type.GATE_STOP.getNumber();
                    break;
                case 3:
                    i = PixelAtoms$DoubleTapNanoappEventReported$Type.HIGH_IMU_ODR_START.getNumber();
                    break;
                case 4:
                    i = PixelAtoms$DoubleTapNanoappEventReported$Type.HIGH_IMU_ODR_STOP.getNumber();
                    break;
                case 5:
                    i = PixelAtoms$DoubleTapNanoappEventReported$Type.ML_PREDICTION_START.getNumber();
                    break;
                case FalsingManager.VERSION /* 6 */:
                    i = PixelAtoms$DoubleTapNanoappEventReported$Type.ML_PREDICTION_STOP.getNumber();
                    break;
                case 7:
                    i = PixelAtoms$DoubleTapNanoappEventReported$Type.SINGLE_TAP.getNumber();
                    break;
                case 8:
                    i = PixelAtoms$DoubleTapNanoappEventReported$Type.DOUBLE_TAP.getNumber();
                    break;
                default:
                    i = PixelAtoms$DoubleTapNanoappEventReported$Type.UNKNOWN.getNumber();
                    break;
            }
            StatsLog.write(writeLong.writeInt(i).build());
        }
    }

    public static final void access$handleWakefullnessChanged(CHREGestureSensor cHREGestureSensor, boolean z) {
        Objects.requireNonNull(cHREGestureSensor);
        if (cHREGestureSensor.isAwake != z) {
            cHREGestureSensor.isAwake = z;
            cHREGestureSensor.updateScreenState();
        }
    }

    public final void sendMessageToNanoApp(final int i, final byte[] bArr, final Function0<Unit> function0, final Function0<Unit> function02) {
        initializeContextHubClientIfNull();
        if (this.contextHubClient == null) {
            Log.e("Columbus/GestureSensor", "ContextHubClient null");
        } else {
            this.bgHandler.post(new Runnable() { // from class: com.google.android.systemui.columbus.sensors.CHREGestureSensor$sendMessageToNanoApp$1
                @Override // java.lang.Runnable
                public final void run() {
                    Integer num;
                    NanoAppMessage createMessageToNanoApp = NanoAppMessage.createMessageToNanoApp(5147455389092024345L, i, bArr);
                    ContextHubClient contextHubClient = this.contextHubClient;
                    if (contextHubClient == null) {
                        num = null;
                    } else {
                        num = Integer.valueOf(contextHubClient.sendMessageToNanoApp(createMessageToNanoApp));
                    }
                    if (num != null && num.intValue() == 0) {
                        Function0<Unit> function03 = function0;
                        if (function03 != null) {
                            function03.invoke();
                            return;
                        }
                        return;
                    }
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Unable to send message ");
                    m.append(i);
                    m.append(" to nanoapp, error code ");
                    m.append(num);
                    Log.e("Columbus/GestureSensor", m.toString());
                    Function0<Unit> function04 = function02;
                    if (function04 != null) {
                        function04.invoke();
                    }
                }
            });
        }
    }

    @Override // com.google.android.systemui.columbus.sensors.Sensor
    public final boolean isListening() {
        return this.isListening;
    }
}
