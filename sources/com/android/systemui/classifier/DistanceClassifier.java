package com.android.systemui.classifier;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.provider.DeviceConfig;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import com.android.systemui.util.DeviceConfigProxy;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DistanceClassifier extends FalsingClassifier {
    public DistanceVectors mCachedDistance;
    public final float mHorizontalFlingThresholdPx;
    public final float mHorizontalSwipeThresholdPx;
    public final float mVerticalFlingThresholdPx;
    public final float mVerticalSwipeThresholdPx;
    public final float mVelocityToDistanceMultiplier = DeviceConfig.getFloat("systemui", "brightline_falsing_distance_velcoity_to_distance", 30.0f);
    public boolean mDistanceDirty = true;

    @Override // com.android.systemui.classifier.FalsingClassifier
    public final void onTouchEvent(MotionEvent motionEvent) {
        this.mDistanceDirty = true;
    }

    /* loaded from: classes.dex */
    public class DistanceVectors {
        public final float mDx;
        public final float mDy;
        public final float mVx;
        public final float mVy;

        public final String toString() {
            return String.format(null, "{dx=%f, vx=%f, dy=%f, vy=%f}", Float.valueOf(this.mDx), Float.valueOf(this.mVx), Float.valueOf(this.mDy), Float.valueOf(this.mVy));
        }

        public DistanceVectors(float f, float f2, float f3, float f4) {
            this.mDx = f;
            this.mDy = f2;
            this.mVx = f3;
            this.mVy = f4;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0073, code lost:
        if (java.lang.Math.abs(r1) >= r7.mHorizontalFlingThresholdPx) goto L_0x00b3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x00af, code lost:
        if (java.lang.Math.abs(r3) >= r7.mVerticalFlingThresholdPx) goto L_0x00b3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x00b2, code lost:
        r2 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x00b5, code lost:
        if (r2 != false) goto L_0x00c0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x00c4, code lost:
        return com.android.systemui.classifier.FalsingClassifier.Result.passed(0.5d);
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:?, code lost:
        return falsed(0.5d, getReason());
     */
    @Override // com.android.systemui.classifier.FalsingClassifier
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.android.systemui.classifier.FalsingClassifier.Result calculateFalsingResult(int r8) {
        /*
            r7 = this;
            r0 = 10
            if (r8 == r0) goto L_0x00c5
            r0 = 11
            if (r8 == r0) goto L_0x00c5
            r0 = 12
            if (r8 == r0) goto L_0x00c5
            r0 = 13
            if (r8 == r0) goto L_0x00c5
            r0 = 14
            if (r8 == r0) goto L_0x00c5
            r0 = 15
            if (r8 != r0) goto L_0x001a
            goto L_0x00c5
        L_0x001a:
            com.android.systemui.classifier.DistanceClassifier$DistanceVectors r8 = r7.getDistances()
            float r0 = r8.mDx
            float r1 = r8.mVx
            float r2 = r7.mVelocityToDistanceMultiplier
            float r1 = r1 * r2
            float r1 = r1 + r0
            float r0 = r8.mDy
            float r3 = r8.mVy
            float r3 = r3 * r2
            float r3 = r3 + r0
            com.android.systemui.classifier.FalsingDataProvider r0 = r7.mDataProvider
            boolean r0 = r0.isHorizontal()
            r2 = 1
            r4 = 0
            java.lang.String r5 = "Threshold: "
            java.lang.String r6 = ", "
            if (r0 == 0) goto L_0x0076
            java.lang.String r0 = "Horizontal swipe and fling distance: "
            java.lang.StringBuilder r0 = android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1.m(r0)
            float r3 = r8.mDx
            r0.append(r3)
            r0.append(r6)
            float r8 = r8.mVx
            float r3 = r7.mVelocityToDistanceMultiplier
            float r8 = r8 * r3
            r0.append(r8)
            java.lang.String r8 = r0.toString()
            com.android.systemui.classifier.BrightLineFalsingManager.logDebug(r8)
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            r8.append(r5)
            float r0 = r7.mHorizontalFlingThresholdPx
            r8.append(r0)
            java.lang.String r8 = r8.toString()
            com.android.systemui.classifier.BrightLineFalsingManager.logDebug(r8)
            float r8 = java.lang.Math.abs(r1)
            float r0 = r7.mHorizontalFlingThresholdPx
            int r8 = (r8 > r0 ? 1 : (r8 == r0 ? 0 : -1))
            if (r8 < 0) goto L_0x00b2
            goto L_0x00b3
        L_0x0076:
            java.lang.String r0 = "Vertical swipe and fling distance: "
            java.lang.StringBuilder r0 = android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1.m(r0)
            float r1 = r8.mDy
            r0.append(r1)
            r0.append(r6)
            float r8 = r8.mVy
            float r1 = r7.mVelocityToDistanceMultiplier
            float r8 = r8 * r1
            r0.append(r8)
            java.lang.String r8 = r0.toString()
            com.android.systemui.classifier.BrightLineFalsingManager.logDebug(r8)
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            r8.append(r5)
            float r0 = r7.mVerticalFlingThresholdPx
            r8.append(r0)
            java.lang.String r8 = r8.toString()
            com.android.systemui.classifier.BrightLineFalsingManager.logDebug(r8)
            float r8 = java.lang.Math.abs(r3)
            float r0 = r7.mVerticalFlingThresholdPx
            int r8 = (r8 > r0 ? 1 : (r8 == r0 ? 0 : -1))
            if (r8 < 0) goto L_0x00b2
            goto L_0x00b3
        L_0x00b2:
            r2 = r4
        L_0x00b3:
            r0 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            if (r2 != 0) goto L_0x00c0
            java.lang.String r8 = r7.getReason()
            com.android.systemui.classifier.FalsingClassifier$Result r7 = r7.falsed(r0, r8)
            goto L_0x00c4
        L_0x00c0:
            com.android.systemui.classifier.FalsingClassifier$Result r7 = com.android.systemui.classifier.FalsingClassifier.Result.passed(r0)
        L_0x00c4:
            return r7
        L_0x00c5:
            r7 = 0
            com.android.systemui.classifier.FalsingClassifier$Result r7 = com.android.systemui.classifier.FalsingClassifier.Result.passed(r7)
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.classifier.DistanceClassifier.calculateFalsingResult(int):com.android.systemui.classifier.FalsingClassifier$Result");
    }

    public final DistanceVectors getDistances() {
        DistanceVectors distanceVectors;
        if (this.mDistanceDirty) {
            TimeLimitedMotionEventBuffer recentMotionEvents = getRecentMotionEvents();
            if (recentMotionEvents.size() < 3) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Only ");
                m.append(recentMotionEvents.size());
                m.append(" motion events recorded.");
                BrightLineFalsingManager.logDebug(m.toString());
                distanceVectors = new DistanceVectors(0.0f, 0.0f, 0.0f, 0.0f);
            } else {
                VelocityTracker obtain = VelocityTracker.obtain();
                Iterator<MotionEvent> it = recentMotionEvents.iterator();
                while (it.hasNext()) {
                    obtain.addMovement(it.next());
                }
                obtain.computeCurrentVelocity(1);
                float xVelocity = obtain.getXVelocity();
                float yVelocity = obtain.getYVelocity();
                obtain.recycle();
                FalsingDataProvider falsingDataProvider = this.mDataProvider;
                Objects.requireNonNull(falsingDataProvider);
                falsingDataProvider.recalculateData();
                float x = falsingDataProvider.mLastMotionEvent.getX();
                FalsingDataProvider falsingDataProvider2 = this.mDataProvider;
                Objects.requireNonNull(falsingDataProvider2);
                falsingDataProvider2.recalculateData();
                float x2 = x - falsingDataProvider2.mFirstRecentMotionEvent.getX();
                FalsingDataProvider falsingDataProvider3 = this.mDataProvider;
                Objects.requireNonNull(falsingDataProvider3);
                falsingDataProvider3.recalculateData();
                float y = falsingDataProvider3.mLastMotionEvent.getY();
                FalsingDataProvider falsingDataProvider4 = this.mDataProvider;
                Objects.requireNonNull(falsingDataProvider4);
                falsingDataProvider4.recalculateData();
                distanceVectors = new DistanceVectors(x2, y - falsingDataProvider4.mFirstRecentMotionEvent.getY(), xVelocity, yVelocity);
            }
            this.mCachedDistance = distanceVectors;
            this.mDistanceDirty = false;
        }
        return this.mCachedDistance;
    }

    public DistanceClassifier(FalsingDataProvider falsingDataProvider, DeviceConfigProxy deviceConfigProxy) {
        super(falsingDataProvider);
        Objects.requireNonNull(deviceConfigProxy);
        float f = DeviceConfig.getFloat("systemui", "brightline_falsing_distance_horizontal_fling_threshold_in", 1.0f);
        float f2 = DeviceConfig.getFloat("systemui", "brightline_falsing_distance_vertical_fling_threshold_in", 1.5f);
        float f3 = DeviceConfig.getFloat("systemui", "brightline_falsing_distance_horizontal_swipe_threshold_in", 3.0f);
        float f4 = DeviceConfig.getFloat("systemui", "brightline_falsing_distance_horizontal_swipe_threshold_in", 3.0f);
        float f5 = DeviceConfig.getFloat("systemui", "brightline_falsing_distance_screen_fraction_max_distance", 0.8f);
        Objects.requireNonNull(falsingDataProvider);
        Objects.requireNonNull(falsingDataProvider);
        this.mHorizontalFlingThresholdPx = Math.min(falsingDataProvider.mWidthPixels * f5, falsingDataProvider.mXdpi * f);
        Objects.requireNonNull(falsingDataProvider);
        Objects.requireNonNull(falsingDataProvider);
        this.mVerticalFlingThresholdPx = Math.min(falsingDataProvider.mHeightPixels * f5, falsingDataProvider.mYdpi * f2);
        Objects.requireNonNull(falsingDataProvider);
        Objects.requireNonNull(falsingDataProvider);
        this.mHorizontalSwipeThresholdPx = Math.min(falsingDataProvider.mWidthPixels * f5, falsingDataProvider.mXdpi * f3);
        Objects.requireNonNull(falsingDataProvider);
        Objects.requireNonNull(falsingDataProvider);
        this.mVerticalSwipeThresholdPx = Math.min(falsingDataProvider.mHeightPixels * f5, falsingDataProvider.mYdpi * f4);
    }

    public final String getReason() {
        return String.format(null, "{distanceVectors=%s, isHorizontal=%s, velocityToDistanceMultiplier=%f, horizontalFlingThreshold=%f, verticalFlingThreshold=%f, horizontalSwipeThreshold=%f, verticalSwipeThreshold=%s}", getDistances(), Boolean.valueOf(this.mDataProvider.isHorizontal()), Float.valueOf(this.mVelocityToDistanceMultiplier), Float.valueOf(this.mHorizontalFlingThresholdPx), Float.valueOf(this.mVerticalFlingThresholdPx), Float.valueOf(this.mHorizontalSwipeThresholdPx), Float.valueOf(this.mVerticalSwipeThresholdPx));
    }
}
