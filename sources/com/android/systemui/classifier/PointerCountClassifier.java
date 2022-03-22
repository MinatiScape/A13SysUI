package com.android.systemui.classifier;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.view.MotionEvent;
import com.android.systemui.classifier.FalsingClassifier;
/* loaded from: classes.dex */
public final class PointerCountClassifier extends FalsingClassifier {
    public int mMaxPointerCount;

    @Override // com.android.systemui.classifier.FalsingClassifier
    public final FalsingClassifier.Result calculateFalsingResult(int i) {
        int i2;
        if (i == 0 || i == 2) {
            i2 = 2;
        } else {
            i2 = 1;
        }
        int i3 = this.mMaxPointerCount;
        if (i3 > i2) {
            return falsed(1.0d, String.format(null, "{pointersObserved=%d, threshold=%d}", Integer.valueOf(i3), Integer.valueOf(i2)));
        }
        return FalsingClassifier.Result.passed(0.0d);
    }

    @Override // com.android.systemui.classifier.FalsingClassifier
    public final void onTouchEvent(MotionEvent motionEvent) {
        int i = this.mMaxPointerCount;
        if (motionEvent.getActionMasked() == 0) {
            this.mMaxPointerCount = motionEvent.getPointerCount();
        } else {
            this.mMaxPointerCount = Math.max(this.mMaxPointerCount, motionEvent.getPointerCount());
        }
        if (i != this.mMaxPointerCount) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Pointers observed:");
            m.append(this.mMaxPointerCount);
            BrightLineFalsingManager.logDebug(m.toString());
        }
    }

    public PointerCountClassifier(FalsingDataProvider falsingDataProvider) {
        super(falsingDataProvider);
    }
}
