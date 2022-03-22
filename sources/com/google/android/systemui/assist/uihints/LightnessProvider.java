package com.google.android.systemui.assist.uihints;

import android.os.Handler;
import android.os.Looper;
import android.view.CompositionSamplingListener;
import com.google.android.systemui.assist.uihints.LightnessProvider;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import java.util.Objects;
/* loaded from: classes.dex */
public final class LightnessProvider implements NgaMessageHandler.CardInfoListener {
    public NgaUiController$$ExternalSyntheticLambda1 mListener;
    public boolean mCardVisible = false;
    public int mColorMode = 0;
    public boolean mIsMonitoringColor = false;
    public boolean mMuted = false;
    public final Handler mUiHandler = new Handler(Looper.getMainLooper());
    public final AnonymousClass1 mColorMonitor = new AnonymousClass1(this);

    /* renamed from: com.google.android.systemui.assist.uihints.LightnessProvider$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends CompositionSamplingListener {
        public final /* synthetic */ LightnessProvider this$0;

        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public AnonymousClass1(com.google.android.systemui.assist.uihints.LightnessProvider r2) {
            /*
                r1 = this;
                com.android.systemui.screenshot.SaveImageInBackgroundTask$$ExternalSyntheticLambda0 r0 = com.android.systemui.screenshot.SaveImageInBackgroundTask$$ExternalSyntheticLambda0.INSTANCE
                r1.this$0 = r2
                r1.<init>(r0)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.assist.uihints.LightnessProvider.AnonymousClass1.<init>(com.google.android.systemui.assist.uihints.LightnessProvider):void");
        }

        public final void onSampleCollected(final float f) {
            this.this$0.mUiHandler.post(new Runnable() { // from class: com.google.android.systemui.assist.uihints.LightnessProvider$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    LightnessProvider.AnonymousClass1 r0 = LightnessProvider.AnonymousClass1.this;
                    float f2 = f;
                    Objects.requireNonNull(r0);
                    LightnessProvider lightnessProvider = r0.this$0;
                    NgaUiController$$ExternalSyntheticLambda1 ngaUiController$$ExternalSyntheticLambda1 = lightnessProvider.mListener;
                    if (ngaUiController$$ExternalSyntheticLambda1 != null && !lightnessProvider.mMuted) {
                        if (!lightnessProvider.mCardVisible || lightnessProvider.mColorMode == 0) {
                            ngaUiController$$ExternalSyntheticLambda1.onLightnessUpdate(f2);
                        }
                    }
                }
            });
        }
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.CardInfoListener
    public final void onCardInfo(boolean z, int i, boolean z2, boolean z3) {
        this.mCardVisible = z;
        this.mColorMode = i;
        NgaUiController$$ExternalSyntheticLambda1 ngaUiController$$ExternalSyntheticLambda1 = this.mListener;
        if (ngaUiController$$ExternalSyntheticLambda1 != null && z) {
            if (i == 1) {
                ngaUiController$$ExternalSyntheticLambda1.onLightnessUpdate(0.0f);
            } else if (i == 2) {
                ngaUiController$$ExternalSyntheticLambda1.onLightnessUpdate(1.0f);
            }
        }
    }
}
