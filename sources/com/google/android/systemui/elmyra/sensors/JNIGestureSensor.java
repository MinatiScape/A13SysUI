package com.google.android.systemui.elmyra.sensors;

import android.content.Context;
import android.content.res.Resources;
import android.provider.Settings;
import android.util.Log;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import com.google.android.systemui.elmyra.sensors.config.GestureConfiguration;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes.dex */
public class JNIGestureSensor implements GestureSensor {
    private static final String DISABLE_SETTING = "com.google.android.systemui.elmyra.disable_jni";
    private static final int SENSOR_RATE = 20000;
    private static final String TAG = "Elmyra/JNIGestureSensor";
    private static boolean sLibraryLoaded;
    private final Context mContext;
    private final AssistGestureController mController;
    private final GestureConfiguration mGestureConfiguration;
    private boolean mIsListening;
    private final KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback;
    private long mNativeService;
    private int mSensorCount;
    private final String mSensorStringType;

    /* renamed from: $r8$lambda$a-O_x7eA8yddVal2dPJJUh34SdI */
    public static /* synthetic */ void m168$r8$lambda$aO_x7eA8yddVal2dPJJUh34SdI(JNIGestureSensor jNIGestureSensor, GestureConfiguration gestureConfiguration) {
        jNIGestureSensor.lambda$new$0(gestureConfiguration);
    }

    private native boolean createNativeService(byte[] bArr);

    private native void destroyNativeService();

    private native boolean setGestureDetector(byte[] bArr);

    private native boolean startListeningNative(String str, int i);

    private native void stopListeningNative();

    private void updateConfiguration() {
    }

    static {
        try {
            System.loadLibrary("elmyra");
            sLibraryLoaded = true;
        } catch (Throwable th) {
            Log.w(TAG, "Could not load JNI component: " + th);
            sLibraryLoaded = false;
        }
    }

    private void onGestureDetected() {
        this.mController.onGestureDetected(null);
    }

    private void onGestureProgress(float f) {
        this.mController.onGestureProgress(f);
    }

    private static byte[] readAllBytes(InputStream inputStream) throws IOException {
        int i = 1024;
        byte[] bArr = new byte[1024];
        int i2 = 0;
        while (true) {
            int read = inputStream.read(bArr, i2, i - i2);
            if (read > 0) {
                i2 += read;
            } else if (read < 0) {
                break;
            } else {
                i <<= 1;
                bArr = Arrays.copyOf(bArr, i);
            }
        }
        if (i == i2) {
            return bArr;
        }
        return Arrays.copyOf(bArr, i2);
    }

    @Override // com.google.android.systemui.elmyra.sensors.GestureSensor
    public void setGestureListener(GestureSensor.Listener listener) {
        AssistGestureController assistGestureController = this.mController;
        Objects.requireNonNull(assistGestureController);
        assistGestureController.mGestureListener = listener;
    }

    @Override // com.google.android.systemui.elmyra.sensors.Sensor
    public void startListening() {
        if (!this.mIsListening && startListeningNative(this.mSensorStringType, SENSOR_RATE)) {
            updateConfiguration();
            this.mIsListening = true;
        }
    }

    @Override // com.google.android.systemui.elmyra.sensors.Sensor
    public void stopListening() {
        if (this.mIsListening) {
            stopListeningNative();
            this.mIsListening = false;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0085 A[Catch: Exception -> 0x00c2, TryCatch #2 {Exception -> 0x00c2, blocks: (B:7:0x0047, B:8:0x0055, B:11:0x005b, B:13:0x0076, B:15:0x007d, B:18:0x0085, B:20:0x008f, B:21:0x00a7, B:22:0x00bb, B:23:0x00be), top: B:27:0x0047, inners: #3 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public JNIGestureSensor(android.content.Context r11, com.google.android.systemui.elmyra.sensors.config.GestureConfiguration r12) {
        /*
            r10 = this;
            java.lang.String r0 = "touch_2_sensitivity"
            java.lang.String r1 = "Elmyra/JNIGestureSensor"
            r10.<init>()
            com.google.android.systemui.elmyra.sensors.JNIGestureSensor$1 r2 = new com.google.android.systemui.elmyra.sensors.JNIGestureSensor$1
            r2.<init>()
            r10.mKeyguardUpdateMonitorCallback = r2
            r10.mContext = r11
            com.google.android.systemui.elmyra.sensors.AssistGestureController r3 = new com.google.android.systemui.elmyra.sensors.AssistGestureController
            r4 = 0
            r3.<init>(r11, r10, r12, r4)
            r10.mController = r3
            android.content.res.Resources r3 = r11.getResources()
            r5 = 2131952311(0x7f1302b7, float:1.9541061E38)
            java.lang.String r3 = r3.getString(r5)
            r10.mSensorStringType = r3
            r10.mGestureConfiguration = r12
            com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda2 r3 = new com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda2
            r3.<init>(r10)
            java.util.Objects.requireNonNull(r12)
            r12.mListener = r3
            java.lang.Class<com.android.keyguard.KeyguardUpdateMonitor> r12 = com.android.keyguard.KeyguardUpdateMonitor.class
            java.lang.Object r12 = com.android.systemui.Dependency.get(r12)
            com.android.keyguard.KeyguardUpdateMonitor r12 = (com.android.keyguard.KeyguardUpdateMonitor) r12
            r12.registerCallback(r2)
            byte[] r11 = getChassisAsset(r11)
            if (r11 == 0) goto L_0x00ca
            int r12 = r11.length
            if (r12 == 0) goto L_0x00ca
            r12 = 0
            com.google.android.systemui.elmyra.proto.nano.ChassisProtos$Chassis r2 = new com.google.android.systemui.elmyra.proto.nano.ChassisProtos$Chassis     // Catch: Exception -> 0x00c2
            r2.<init>()     // Catch: Exception -> 0x00c2
            com.google.protobuf.nano.MessageNano.mergeFrom(r2, r11)     // Catch: Exception -> 0x00c2
            com.google.android.systemui.elmyra.proto.nano.ChassisProtos$Sensor[] r3 = r2.sensors     // Catch: Exception -> 0x00c2
            int r3 = r3.length     // Catch: Exception -> 0x00c2
            r10.mSensorCount = r3     // Catch: Exception -> 0x00c2
            r3 = r12
        L_0x0055:
            int r5 = r10.mSensorCount     // Catch: Exception -> 0x00c2
            if (r3 >= r5) goto L_0x00be
            java.lang.String r5 = "Elmyra/SensorCalibration"
            java.io.FileInputStream r6 = new java.io.FileInputStream     // Catch: SecurityException -> 0x0075, FileNotFoundException -> 0x007c, Exception -> 0x00c2
            java.lang.String r7 = "/persist/sensors/elmyra/calibration.%d"
            r8 = 1
            java.lang.Object[] r8 = new java.lang.Object[r8]     // Catch: SecurityException -> 0x0075, FileNotFoundException -> 0x007c, Exception -> 0x00c2
            java.lang.Integer r9 = java.lang.Integer.valueOf(r3)     // Catch: SecurityException -> 0x0075, FileNotFoundException -> 0x007c, Exception -> 0x00c2
            r8[r12] = r9     // Catch: SecurityException -> 0x0075, FileNotFoundException -> 0x007c, Exception -> 0x00c2
            java.lang.String r7 = java.lang.String.format(r7, r8)     // Catch: SecurityException -> 0x0075, FileNotFoundException -> 0x007c, Exception -> 0x00c2
            r6.<init>(r7)     // Catch: SecurityException -> 0x0075, FileNotFoundException -> 0x007c, Exception -> 0x00c2
            com.google.android.systemui.smartspace.ProtoStore r7 = new com.google.android.systemui.smartspace.ProtoStore     // Catch: SecurityException -> 0x0075, FileNotFoundException -> 0x007c, Exception -> 0x00c2
            r7.<init>(r6)     // Catch: SecurityException -> 0x0075, FileNotFoundException -> 0x007c, Exception -> 0x00c2
            goto L_0x0083
        L_0x0075:
            r6 = move-exception
            java.lang.String r7 = "Could not open calibration file"
            android.util.Log.e(r5, r7, r6)     // Catch: Exception -> 0x00c2
            goto L_0x0082
        L_0x007c:
            r6 = move-exception
            java.lang.String r7 = "Could not find calibration file"
            android.util.Log.e(r5, r7, r6)     // Catch: Exception -> 0x00c2
        L_0x0082:
            r7 = r4
        L_0x0083:
            if (r7 == 0) goto L_0x00a7
            java.lang.Object r5 = r7.mContext     // Catch: Exception -> 0x00c2
            java.util.Map r5 = (java.util.Map) r5     // Catch: Exception -> 0x00c2
            boolean r5 = r5.containsKey(r0)     // Catch: Exception -> 0x00c2
            if (r5 == 0) goto L_0x00a7
            com.google.android.systemui.elmyra.proto.nano.ChassisProtos$Sensor[] r5 = r2.sensors     // Catch: Exception -> 0x00c2
            r5 = r5[r3]     // Catch: Exception -> 0x00c2
            r6 = 1065353216(0x3f800000, float:1.0)
            java.lang.Object r7 = r7.mContext     // Catch: Exception -> 0x00c2
            java.util.Map r7 = (java.util.Map) r7     // Catch: Exception -> 0x00c2
            java.lang.Object r7 = r7.get(r0)     // Catch: Exception -> 0x00c2
            java.lang.Float r7 = (java.lang.Float) r7     // Catch: Exception -> 0x00c2
            float r7 = r7.floatValue()     // Catch: Exception -> 0x00c2
            float r6 = r6 / r7
            r5.sensitivity = r6     // Catch: Exception -> 0x00c2
            goto L_0x00bb
        L_0x00a7:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: Exception -> 0x00c2
            r5.<init>()     // Catch: Exception -> 0x00c2
            java.lang.String r6 = "Error reading calibration for sensor "
            r5.append(r6)     // Catch: Exception -> 0x00c2
            r5.append(r3)     // Catch: Exception -> 0x00c2
            java.lang.String r5 = r5.toString()     // Catch: Exception -> 0x00c2
            android.util.Log.w(r1, r5)     // Catch: Exception -> 0x00c2
        L_0x00bb:
            int r3 = r3 + 1
            goto L_0x0055
        L_0x00be:
            r10.createNativeService(r11)     // Catch: Exception -> 0x00c2
            goto L_0x00ca
        L_0x00c2:
            r11 = move-exception
            java.lang.String r0 = "Error reading chassis file"
            android.util.Log.e(r1, r0, r11)
            r10.mSensorCount = r12
        L_0x00ca:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.elmyra.sensors.JNIGestureSensor.<init>(android.content.Context, com.google.android.systemui.elmyra.sensors.config.GestureConfiguration):void");
    }

    private static byte[] getChassisAsset(Context context) {
        try {
            return readAllBytes(context.getResources().openRawResource(2131886080));
        } catch (Resources.NotFoundException | IOException e) {
            Log.e(TAG, "Could not load chassis resource", e);
            return null;
        }
    }

    public static boolean isAvailable(Context context) {
        byte[] chassisAsset;
        if (Settings.Secure.getInt(context.getContentResolver(), DISABLE_SETTING, 0) == 1 || !sLibraryLoaded || (chassisAsset = getChassisAsset(context)) == null || chassisAsset.length == 0) {
            return false;
        }
        return true;
    }

    public void finalize() throws Throwable {
        super.finalize();
        destroyNativeService();
    }

    public /* synthetic */ void lambda$new$0(GestureConfiguration gestureConfiguration) {
        updateConfiguration();
    }

    @Override // com.google.android.systemui.elmyra.sensors.Sensor
    public boolean isListening() {
        return this.mIsListening;
    }
}
