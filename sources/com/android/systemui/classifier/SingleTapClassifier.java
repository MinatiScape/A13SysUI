package com.android.systemui.classifier;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.view.MotionEvent;
import com.android.systemui.classifier.FalsingClassifier;
import java.util.List;
/* loaded from: classes.dex */
public final class SingleTapClassifier extends FalsingClassifier {
    public final float mTouchSlop;

    public SingleTapClassifier(FalsingDataProvider falsingDataProvider, float f) {
        super(falsingDataProvider);
        this.mTouchSlop = f;
    }

    @Override // com.android.systemui.classifier.FalsingClassifier
    public final FalsingClassifier.Result calculateFalsingResult(int i) {
        return isTap(getRecentMotionEvents(), 0.5d);
    }

    public final FalsingClassifier.Result isTap(List<MotionEvent> list, double d) {
        if (list.isEmpty()) {
            return falsed(0.0d, "no motion events");
        }
        float x = list.get(0).getX();
        float y = list.get(0).getY();
        for (MotionEvent motionEvent : list) {
            if (Math.abs(motionEvent.getX() - x) >= this.mTouchSlop) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("dX too big for a tap: ");
                m.append(Math.abs(motionEvent.getX() - x));
                m.append("vs ");
                m.append(this.mTouchSlop);
                return falsed(d, m.toString());
            } else if (Math.abs(motionEvent.getY() - y) >= this.mTouchSlop) {
                StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("dY too big for a tap: ");
                m2.append(Math.abs(motionEvent.getY() - y));
                m2.append(" vs ");
                m2.append(this.mTouchSlop);
                return falsed(d, m2.toString());
            }
        }
        return FalsingClassifier.Result.passed(0.0d);
    }
}
