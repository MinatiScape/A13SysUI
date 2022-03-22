package com.android.systemui.classifier;

import com.android.systemui.lowlightclock.LowLightClockController;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BrightLineFalsingManager$$ExternalSyntheticLambda1 implements Consumer {
    public static final /* synthetic */ BrightLineFalsingManager$$ExternalSyntheticLambda1 INSTANCE = new BrightLineFalsingManager$$ExternalSyntheticLambda1(0);
    public static final /* synthetic */ BrightLineFalsingManager$$ExternalSyntheticLambda1 INSTANCE$1 = new BrightLineFalsingManager$$ExternalSyntheticLambda1(1);
    public final /* synthetic */ int $r8$classId;

    public /* synthetic */ BrightLineFalsingManager$$ExternalSyntheticLambda1(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                FalsingClassifier falsingClassifier = (FalsingClassifier) obj;
                Objects.requireNonNull(falsingClassifier);
                FalsingDataProvider falsingDataProvider = falsingClassifier.mDataProvider;
                FalsingClassifier$$ExternalSyntheticLambda0 falsingClassifier$$ExternalSyntheticLambda0 = falsingClassifier.mMotionEventListener;
                Objects.requireNonNull(falsingDataProvider);
                falsingDataProvider.mMotionEventListeners.remove(falsingClassifier$$ExternalSyntheticLambda0);
                return;
            default:
                ((LowLightClockController) obj).dozeTimeTick();
                return;
        }
    }
}
