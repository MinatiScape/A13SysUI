package com.android.wm.shell.bubbles;

import com.android.systemui.classifier.BrightLineFalsingManager;
import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.plugins.FalsingManager;
import com.android.wm.shell.bubbles.BubbleViewInfoTask;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BubbleController$$ExternalSyntheticLambda7 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ BubbleController$$ExternalSyntheticLambda7(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                final BubbleController bubbleController = (BubbleController) this.f$0;
                final Bubble bubble = (Bubble) obj;
                Objects.requireNonNull(bubbleController);
                BubbleData bubbleData = bubbleController.mBubbleData;
                Objects.requireNonNull(bubble);
                if (!bubbleData.hasAnyBubbleWithKey(bubble.mKey)) {
                    bubble.inflate(new BubbleViewInfoTask.Callback() { // from class: com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda2
                        @Override // com.android.wm.shell.bubbles.BubbleViewInfoTask.Callback
                        public final void onBubbleViewsReady(Bubble bubble2) {
                            BubbleController bubbleController2 = BubbleController.this;
                            Bubble bubble3 = bubble;
                            Objects.requireNonNull(bubbleController2);
                            bubbleController2.mBubbleData.overflowBubble(15, bubble3);
                        }
                    }, bubbleController.mContext, bubbleController, bubbleController.mStackView, bubbleController.mBubbleIconFactory, bubbleController.mBubbleBadgeIconFactory, true);
                    return;
                }
                return;
            default:
                boolean z = BrightLineFalsingManager.DEBUG;
                ((FalsingClassifier) obj).onProximityEvent((FalsingManager.ProximityEvent) this.f$0);
                return;
        }
    }
}
