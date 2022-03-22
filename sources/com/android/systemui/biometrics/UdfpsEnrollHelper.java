package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.PointF;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.accessibility.AccessibilityManager;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final class UdfpsEnrollHelper {
    public final boolean mAccessibilityEnabled;
    public final Context mContext;
    public final int mEnrollReason;
    public final FingerprintManager mFingerprintManager;
    public final ArrayList mGuidedEnrollmentPoints;
    public Listener mListener;
    public int mTotalSteps = -1;
    public int mRemainingSteps = -1;
    public int mLocationsEnrolled = 0;
    public int mCenterTouchCount = 0;

    /* loaded from: classes.dex */
    public interface Listener {
    }

    public final int getStageThresholdSteps(int i, int i2) {
        return Math.round(this.mFingerprintManager.getEnrollStageThreshold(i2) * i);
    }

    public final PointF getNextGuidedEnrollmentPoint() {
        if (this.mAccessibilityEnabled || !isGuidedEnrollmentStage()) {
            return new PointF(0.0f, 0.0f);
        }
        float f = 0.5f;
        if (Build.IS_ENG || Build.IS_USERDEBUG) {
            f = Settings.Secure.getFloatForUser(this.mContext.getContentResolver(), "com.android.systemui.biometrics.UdfpsEnrollHelper.scale", 0.5f, -2);
        }
        ArrayList arrayList = this.mGuidedEnrollmentPoints;
        PointF pointF = (PointF) arrayList.get((this.mLocationsEnrolled - this.mCenterTouchCount) % arrayList.size());
        return new PointF(pointF.x * f, pointF.y * f);
    }

    public final boolean isCenterEnrollmentStage() {
        int i;
        int i2 = this.mTotalSteps;
        if (i2 == -1 || (i = this.mRemainingSteps) == -1 || i2 - i < getStageThresholdSteps(i2, 0)) {
            return true;
        }
        return false;
    }

    public final boolean isGuidedEnrollmentStage() {
        int i;
        int i2;
        int i3;
        if (this.mAccessibilityEnabled || (i = this.mTotalSteps) == -1 || (i2 = this.mRemainingSteps) == -1 || (i3 = i - i2) < getStageThresholdSteps(i, 0) || i3 >= getStageThresholdSteps(this.mTotalSteps, 1)) {
            return false;
        }
        return true;
    }

    public UdfpsEnrollHelper(Context context, FingerprintManager fingerprintManager, int i) {
        boolean z = false;
        this.mContext = context;
        this.mFingerprintManager = fingerprintManager;
        this.mEnrollReason = i;
        this.mAccessibilityEnabled = ((AccessibilityManager) context.getSystemService(AccessibilityManager.class)).isEnabled();
        ArrayList arrayList = new ArrayList();
        this.mGuidedEnrollmentPoints = arrayList;
        float applyDimension = TypedValue.applyDimension(5, 1.0f, context.getResources().getDisplayMetrics());
        if (!(Settings.Secure.getIntForUser(context.getContentResolver(), "com.android.systemui.biometrics.UdfpsNewCoords", 0, -2) != 0 ? true : z) || (!Build.IS_ENG && !Build.IS_USERDEBUG)) {
            Log.v("UdfpsEnrollHelper", "Using old coordinates");
            arrayList.add(new PointF(2.0f * applyDimension, 0.0f * applyDimension));
            arrayList.add(new PointF(0.87f * applyDimension, (-2.7f) * applyDimension));
            float f = (-1.8f) * applyDimension;
            arrayList.add(new PointF(f, (-1.31f) * applyDimension));
            arrayList.add(new PointF(f, 1.31f * applyDimension));
            arrayList.add(new PointF(0.88f * applyDimension, 2.7f * applyDimension));
            arrayList.add(new PointF(3.94f * applyDimension, (-1.06f) * applyDimension));
            arrayList.add(new PointF(2.9f * applyDimension, (-4.14f) * applyDimension));
            arrayList.add(new PointF((-0.52f) * applyDimension, (-5.95f) * applyDimension));
            float f2 = (-3.33f) * applyDimension;
            arrayList.add(new PointF(f2, f2));
            arrayList.add(new PointF((-3.99f) * applyDimension, (-0.35f) * applyDimension));
            arrayList.add(new PointF((-3.62f) * applyDimension, 2.54f * applyDimension));
            arrayList.add(new PointF((-1.49f) * applyDimension, 5.57f * applyDimension));
            arrayList.add(new PointF(2.29f * applyDimension, 4.92f * applyDimension));
            arrayList.add(new PointF(3.82f * applyDimension, applyDimension * 1.78f));
            return;
        }
        Log.v("UdfpsEnrollHelper", "Using new coordinates");
        float f3 = (-0.15f) * applyDimension;
        arrayList.add(new PointF(f3, (-1.02f) * applyDimension));
        arrayList.add(new PointF(f3, 1.02f * applyDimension));
        float f4 = 0.0f * applyDimension;
        arrayList.add(new PointF(0.29f * applyDimension, f4));
        float f5 = 2.17f * applyDimension;
        arrayList.add(new PointF(f5, (-2.35f) * applyDimension));
        float f6 = 1.07f * applyDimension;
        arrayList.add(new PointF(f6, (-3.96f) * applyDimension));
        float f7 = (-0.37f) * applyDimension;
        arrayList.add(new PointF(f7, (-4.31f) * applyDimension));
        float f8 = (-1.69f) * applyDimension;
        arrayList.add(new PointF(f8, (-3.29f) * applyDimension));
        float f9 = (-2.48f) * applyDimension;
        arrayList.add(new PointF(f9, (-1.23f) * applyDimension));
        arrayList.add(new PointF(f9, 1.23f * applyDimension));
        arrayList.add(new PointF(f8, 3.29f * applyDimension));
        arrayList.add(new PointF(f7, 4.31f * applyDimension));
        arrayList.add(new PointF(f6, 3.96f * applyDimension));
        arrayList.add(new PointF(f5, 2.35f * applyDimension));
        arrayList.add(new PointF(applyDimension * 2.58f, f4));
    }
}
