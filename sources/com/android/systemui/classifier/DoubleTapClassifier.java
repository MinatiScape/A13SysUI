package com.android.systemui.classifier;

import android.view.MotionEvent;
import com.android.systemui.classifier.FalsingClassifier;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DoubleTapClassifier extends FalsingClassifier {
    public final float mDoubleTapSlop;
    public final long mDoubleTapTimeMs;
    public final SingleTapClassifier mSingleTapClassifier;

    public DoubleTapClassifier(FalsingDataProvider falsingDataProvider, SingleTapClassifier singleTapClassifier, float f, long j) {
        super(falsingDataProvider);
        this.mSingleTapClassifier = singleTapClassifier;
        this.mDoubleTapSlop = f;
        this.mDoubleTapTimeMs = j;
    }

    @Override // com.android.systemui.classifier.FalsingClassifier
    public final FalsingClassifier.Result calculateFalsingResult(int i) {
        TimeLimitedMotionEventBuffer recentMotionEvents = getRecentMotionEvents();
        FalsingDataProvider falsingDataProvider = this.mDataProvider;
        Objects.requireNonNull(falsingDataProvider);
        List<MotionEvent> list = falsingDataProvider.mPriorMotionEvents;
        StringBuilder sb = new StringBuilder();
        if (list == null) {
            return falsed(0.0d, "Only one gesture recorded");
        }
        FalsingClassifier.Result isTap = this.mSingleTapClassifier.isTap(list, 0.5d);
        boolean z = false;
        if (isTap.mFalsed) {
            sb.append("First gesture is not a tap. ");
            sb.append(isTap.getReason());
        } else {
            FalsingClassifier.Result isTap2 = this.mSingleTapClassifier.isTap(recentMotionEvents, 0.5d);
            if (isTap2.mFalsed) {
                sb.append("Second gesture is not a tap. ");
                sb.append(isTap2.getReason());
            } else {
                MotionEvent motionEvent = list.get(list.size() - 1);
                MotionEvent motionEvent2 = (MotionEvent) recentMotionEvents.get(recentMotionEvents.size() - 1);
                long eventTime = motionEvent2.getEventTime() - motionEvent.getEventTime();
                if (eventTime > this.mDoubleTapTimeMs) {
                    sb.append("Time between taps too large: ");
                    sb.append(eventTime);
                    sb.append("ms");
                } else if (Math.abs(motionEvent.getX() - motionEvent2.getX()) >= this.mDoubleTapSlop) {
                    sb.append("Delta X between taps too large:");
                    sb.append(Math.abs(motionEvent.getX() - motionEvent2.getX()));
                    sb.append(" vs ");
                    sb.append(this.mDoubleTapSlop);
                } else if (Math.abs(motionEvent.getY() - motionEvent2.getY()) >= this.mDoubleTapSlop) {
                    sb.append("Delta Y between taps too large:");
                    sb.append(Math.abs(motionEvent.getY() - motionEvent2.getY()));
                    sb.append(" vs ");
                    sb.append(this.mDoubleTapSlop);
                } else {
                    z = true;
                }
            }
        }
        if (!z) {
            return falsed(0.5d, sb.toString());
        }
        return FalsingClassifier.Result.passed(0.5d);
    }
}
