package com.android.systemui.statusbar.notification.row;

import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import com.google.android.systemui.assist.uihints.ChipsContainer;
import com.google.android.systemui.assist.uihints.TranscriptionController;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationInfo$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;
    public final /* synthetic */ Object f$2;

    public /* synthetic */ NotificationInfo$$ExternalSyntheticLambda2(Object obj, Object obj2, Object obj3, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
        this.f$2 = obj3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                int i = NotificationInfo.$r8$clinit;
                ((View) this.f$0).setSelected(false);
                ((View) this.f$1).setSelected(true);
                ((View) this.f$2).setSelected(false);
                return;
            default:
                TranscriptionController transcriptionController = (TranscriptionController) this.f$0;
                Objects.requireNonNull(transcriptionController);
                final ChipsContainer chipsContainer = (ChipsContainer) transcriptionController.mViewMap.get(TranscriptionController.State.CHIPS);
                float floatValue = ((Float) ((Optional) this.f$2).get()).floatValue();
                Objects.requireNonNull(chipsContainer);
                chipsContainer.mChips = (List) this.f$1;
                chipsContainer.setChipsInternal();
                if (chipsContainer.mAnimator.isRunning()) {
                    Log.w("ChipsContainer", "Already animating in chips view; ignoring");
                    return;
                }
                chipsContainer.mAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
                chipsContainer.mAnimator.setInterpolator(new OvershootInterpolator(Math.min(10.0f, (floatValue / 1.2f) + 3.0f)));
                chipsContainer.mAnimator.setDuration(400L);
                chipsContainer.mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.qs.SlashDrawable$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        switch (r2) {
                            case 0:
                                SlashDrawable slashDrawable = (SlashDrawable) chipsContainer;
                                Objects.requireNonNull(slashDrawable);
                                slashDrawable.invalidateSelf();
                                return;
                            default:
                                ChipsContainer chipsContainer2 = (ChipsContainer) chipsContainer;
                                int i2 = ChipsContainer.$r8$clinit;
                                Objects.requireNonNull(chipsContainer2);
                                chipsContainer2.setTranslationY((1.0f - ((Float) valueAnimator.getAnimatedValue()).floatValue()) * chipsContainer2.START_DELTA);
                                return;
                        }
                    }
                });
                chipsContainer.setVisibility(0);
                chipsContainer.mAnimator.start();
                return;
        }
    }
}
