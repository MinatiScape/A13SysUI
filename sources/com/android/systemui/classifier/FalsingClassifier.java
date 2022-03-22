package com.android.systemui.classifier;

import android.view.MotionEvent;
import com.android.systemui.plugins.FalsingManager;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class FalsingClassifier {
    public final FalsingDataProvider mDataProvider;
    public final FalsingClassifier$$ExternalSyntheticLambda0 mMotionEventListener;

    /* loaded from: classes.dex */
    public static class Result {
        public final double mConfidence;
        public final String mContext;
        public final boolean mFalsed;
        public final String mReason;

        public final String getReason() {
            return String.format("{context=%s reason=%s}", this.mContext, this.mReason);
        }

        public static Result passed(double d) {
            return new Result(false, d, null, null);
        }

        public Result(boolean z, double d, String str, String str2) {
            this.mFalsed = z;
            this.mConfidence = d;
            this.mContext = str;
            this.mReason = str2;
        }
    }

    public abstract Result calculateFalsingResult(int i);

    public void onProximityEvent(FalsingManager.ProximityEvent proximityEvent) {
    }

    public void onSessionEnded() {
    }

    public void onSessionStarted() {
    }

    public void onTouchEvent(MotionEvent motionEvent) {
    }

    public final TimeLimitedMotionEventBuffer getRecentMotionEvents() {
        FalsingDataProvider falsingDataProvider = this.mDataProvider;
        Objects.requireNonNull(falsingDataProvider);
        return falsingDataProvider.mRecentMotionEvents;
    }

    public final boolean isRight() {
        FalsingDataProvider falsingDataProvider = this.mDataProvider;
        Objects.requireNonNull(falsingDataProvider);
        falsingDataProvider.recalculateData();
        if (!falsingDataProvider.mRecentMotionEvents.isEmpty() && falsingDataProvider.mLastMotionEvent.getX() > falsingDataProvider.mFirstRecentMotionEvent.getX()) {
            return true;
        }
        return false;
    }

    public final boolean isUp() {
        FalsingDataProvider falsingDataProvider = this.mDataProvider;
        Objects.requireNonNull(falsingDataProvider);
        falsingDataProvider.recalculateData();
        if (!falsingDataProvider.mRecentMotionEvents.isEmpty() && falsingDataProvider.mLastMotionEvent.getY() < falsingDataProvider.mFirstRecentMotionEvent.getY()) {
            return true;
        }
        return false;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.Object, com.android.systemui.classifier.FalsingClassifier$$ExternalSyntheticLambda0] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public FalsingClassifier(com.android.systemui.classifier.FalsingDataProvider r2) {
        /*
            r1 = this;
            r1.<init>()
            com.android.systemui.classifier.FalsingClassifier$$ExternalSyntheticLambda0 r0 = new com.android.systemui.classifier.FalsingClassifier$$ExternalSyntheticLambda0
            r0.<init>()
            r1.mMotionEventListener = r0
            r1.mDataProvider = r2
            java.util.Objects.requireNonNull(r2)
            java.util.ArrayList r1 = r2.mMotionEventListeners
            r1.add(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.classifier.FalsingClassifier.<init>(com.android.systemui.classifier.FalsingDataProvider):void");
    }

    public final Result falsed(double d, String str) {
        return new Result(true, d, getClass().getSimpleName(), str);
    }
}
