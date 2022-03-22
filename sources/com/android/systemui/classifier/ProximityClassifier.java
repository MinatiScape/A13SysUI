package com.android.systemui.classifier;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.provider.DeviceConfig;
import android.view.MotionEvent;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.util.DeviceConfigProxy;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ProximityClassifier extends FalsingClassifier {
    public final DistanceClassifier mDistanceClassifier;
    public long mGestureStartTimeNs;
    public boolean mNear;
    public long mNearDurationNs;
    public final float mPercentCoveredThreshold = DeviceConfig.getFloat("systemui", "brightline_falsing_proximity_percent_covered_threshold", 0.1f);
    public float mPercentNear;
    public long mPrevNearTimeNs;

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0063, code lost:
        if (java.lang.Math.abs(r2.mDx) >= r9.mHorizontalSwipeThresholdPx) goto L_0x009c;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x009a, code lost:
        if (java.lang.Math.abs(r2.mDy) >= r9.mVerticalSwipeThresholdPx) goto L_0x009c;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x009c, code lost:
        r2 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x009e, code lost:
        r2 = false;
     */
    @Override // com.android.systemui.classifier.FalsingClassifier
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.android.systemui.classifier.FalsingClassifier.Result calculateFalsingResult(int r9) {
        /*
            Method dump skipped, instructions count: 254
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.classifier.ProximityClassifier.calculateFalsingResult(int):com.android.systemui.classifier.FalsingClassifier$Result");
    }

    @Override // com.android.systemui.classifier.FalsingClassifier
    public final void onSessionEnded() {
        this.mPrevNearTimeNs = 0L;
        this.mPercentNear = 0.0f;
    }

    @Override // com.android.systemui.classifier.FalsingClassifier
    public final void onSessionStarted() {
        this.mPrevNearTimeNs = 0L;
        this.mPercentNear = 0.0f;
    }

    public final void update(boolean z, long j) {
        long j2 = this.mPrevNearTimeNs;
        if (j2 != 0 && j > j2 && this.mNear) {
            this.mNearDurationNs = (j - j2) + this.mNearDurationNs;
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Updating duration: ");
            m.append(this.mNearDurationNs);
            BrightLineFalsingManager.logDebug(m.toString());
        }
        if (z) {
            BrightLineFalsingManager.logDebug("Set prevNearTimeNs: " + j);
            this.mPrevNearTimeNs = j;
        }
        this.mNear = z;
    }

    public ProximityClassifier(DistanceClassifier distanceClassifier, FalsingDataProvider falsingDataProvider, DeviceConfigProxy deviceConfigProxy) {
        super(falsingDataProvider);
        this.mDistanceClassifier = distanceClassifier;
        Objects.requireNonNull(deviceConfigProxy);
    }

    @Override // com.android.systemui.classifier.FalsingClassifier
    public final void onProximityEvent(FalsingManager.ProximityEvent proximityEvent) {
        boolean covered = proximityEvent.getCovered();
        long timestampNs = proximityEvent.getTimestampNs();
        BrightLineFalsingManager.logDebug("Sensor is: " + covered + " at time " + timestampNs);
        update(covered, timestampNs);
    }

    @Override // com.android.systemui.classifier.FalsingClassifier
    public final void onTouchEvent(MotionEvent motionEvent) {
        float f;
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mGestureStartTimeNs = motionEvent.getEventTimeNano();
            if (this.mPrevNearTimeNs > 0) {
                this.mPrevNearTimeNs = motionEvent.getEventTimeNano();
            }
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Gesture start time: ");
            m.append(this.mGestureStartTimeNs);
            BrightLineFalsingManager.logDebug(m.toString());
            this.mNearDurationNs = 0L;
        }
        if (actionMasked == 1 || actionMasked == 3) {
            update(this.mNear, motionEvent.getEventTimeNano());
            long eventTimeNano = motionEvent.getEventTimeNano() - this.mGestureStartTimeNs;
            BrightLineFalsingManager.logDebug("Gesture duration, Proximity duration: " + eventTimeNano + ", " + this.mNearDurationNs);
            if (eventTimeNano == 0) {
                if (this.mNear) {
                    f = 1.0f;
                } else {
                    f = 0.0f;
                }
                this.mPercentNear = f;
                return;
            }
            this.mPercentNear = ((float) this.mNearDurationNs) / ((float) eventTimeNano);
        }
    }
}
