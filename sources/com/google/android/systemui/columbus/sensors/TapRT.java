package com.google.android.systemui.columbus.sensors;

import java.util.ArrayDeque;
import java.util.Iterator;
/* loaded from: classes.dex */
public final class TapRT extends EventIMURT {
    public int mResult;
    public PeakDetector mPeakDetector = new PeakDetector();
    public PeakDetector mValleyDetector = new PeakDetector();
    public ArrayDeque mTimestampsBackTap = new ArrayDeque();
    public boolean mWasPeakApproaching = true;

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0046, code lost:
        if (r0.equals("Pixel 5") == false) goto L_0x0028;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public TapRT(android.content.res.AssetManager r5) {
        /*
            r4 = this;
            java.lang.String r0 = android.os.Build.MODEL
            r4.<init>()
            com.google.android.systemui.columbus.sensors.PeakDetector r1 = new com.google.android.systemui.columbus.sensors.PeakDetector
            r1.<init>()
            r4.mPeakDetector = r1
            com.google.android.systemui.columbus.sensors.PeakDetector r1 = new com.google.android.systemui.columbus.sensors.PeakDetector
            r1.<init>()
            r4.mValleyDetector = r1
            java.util.ArrayDeque r1 = new java.util.ArrayDeque
            r1.<init>()
            r4.mTimestampsBackTap = r1
            r1 = 1
            r4.mWasPeakApproaching = r1
            java.util.Objects.requireNonNull(r0)
            int r2 = r0.hashCode()
            r3 = -1
            switch(r2) {
                case -870267800: goto L_0x0049;
                case 1105847547: goto L_0x0040;
                case 1905086331: goto L_0x0035;
                case 1905116122: goto L_0x002a;
                default: goto L_0x0028;
            }
        L_0x0028:
            r1 = r3
            goto L_0x0053
        L_0x002a:
            java.lang.String r1 = "Pixel 4 XL"
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L_0x0033
            goto L_0x0028
        L_0x0033:
            r1 = 3
            goto L_0x0053
        L_0x0035:
            java.lang.String r1 = "Pixel 3 XL"
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L_0x003e
            goto L_0x0028
        L_0x003e:
            r1 = 2
            goto L_0x0053
        L_0x0040:
            java.lang.String r2 = "Pixel 5"
            boolean r0 = r0.equals(r2)
            if (r0 != 0) goto L_0x0053
            goto L_0x0028
        L_0x0049:
            java.lang.String r1 = "Pixel 4a (5G)"
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L_0x0052
            goto L_0x0028
        L_0x0052:
            r1 = 0
        L_0x0053:
            switch(r1) {
                case 0: goto L_0x0066;
                case 1: goto L_0x0062;
                case 2: goto L_0x005e;
                case 3: goto L_0x005a;
                default: goto L_0x0056;
            }
        L_0x0056:
            java.lang.String r0 = "tap7cls_flame.tflite"
            goto L_0x0069
        L_0x005a:
            java.lang.String r0 = "tap7cls_coral.tflite"
            goto L_0x0069
        L_0x005e:
            java.lang.String r0 = "tap7cls_crosshatch.tflite"
            goto L_0x0069
        L_0x0062:
            java.lang.String r0 = "tap7cls_redfin.tflite"
            goto L_0x0069
        L_0x0066:
            java.lang.String r0 = "tap7cls_bramble.tflite"
        L_0x0069:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "TapRT loaded "
            r1.append(r2)
            r1.append(r0)
            java.lang.String r1 = r1.toString()
            java.lang.String r2 = "Columbus"
            android.util.Log.d(r2, r1)
            com.google.android.systemui.columbus.sensors.TfClassifier r1 = new com.google.android.systemui.columbus.sensors.TfClassifier
            r1.<init>(r5, r0)
            r4.mClassifier = r1
            r0 = 153600000(0x927c000, double:7.5888483E-316)
            r4.mSizeWindowNs = r0
            r5 = 50
            r4.mSizeFeatureWindow = r5
            r5 = 300(0x12c, float:4.2E-43)
            r4.mNumberFeature = r5
            com.google.android.systemui.columbus.sensors.Lowpass3C r5 = r4.mLowpassAcc
            r0 = 1065353216(0x3f800000, float:1.0)
            r5.setPara(r0)
            com.google.android.systemui.columbus.sensors.Lowpass3C r5 = r4.mLowpassGyro
            r5.setPara(r0)
            com.google.android.systemui.columbus.sensors.Highpass3C r5 = r4.mHighpassAcc
            r5.setPara()
            com.google.android.systemui.columbus.sensors.Highpass3C r4 = r4.mHighpassGyro
            r4.setPara()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.columbus.sensors.TapRT.<init>(android.content.res.AssetManager):void");
    }

    public final void addToFeatureVector(ArrayDeque arrayDeque, int i, int i2) {
        Iterator it = arrayDeque.iterator();
        int i3 = 0;
        while (it.hasNext()) {
            if (i3 < i) {
                it.next();
            } else if (i3 < this.mSizeFeatureWindow + i) {
                this.mFeatureVector.set(i2, (Float) it.next());
                i2++;
            } else {
                return;
            }
            i3++;
        }
    }
}
