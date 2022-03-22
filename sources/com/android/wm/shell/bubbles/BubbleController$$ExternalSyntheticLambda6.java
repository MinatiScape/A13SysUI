package com.android.wm.shell.bubbles;

import android.graphics.PointF;
import android.os.RemoteException;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BubbleController$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;
    public final /* synthetic */ boolean f$2;
    public final /* synthetic */ Object f$3;

    public /* synthetic */ BubbleController$$ExternalSyntheticLambda6(Object obj, Object obj2, boolean z, Object obj3, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
        this.f$2 = z;
        this.f$3 = obj3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        float f;
        switch (this.$r8$classId) {
            case 0:
                BubbleController bubbleController = (BubbleController) this.f$0;
                BubbleEntry bubbleEntry = (BubbleEntry) this.f$1;
                boolean z = this.f$2;
                Bubble bubble = (Bubble) this.f$3;
                Objects.requireNonNull(bubbleController);
                if (bubbleEntry != null) {
                    Objects.requireNonNull(bubble);
                    boolean isEnabled = bubble.isEnabled(1);
                    bubbleEntry.setFlagBubble(z);
                    int i = 0;
                    if (isEnabled) {
                        i = 3;
                    }
                    try {
                        bubbleController.mBarService.onNotificationBubbleChanged(bubbleEntry.getKey(), z, i);
                    } catch (RemoteException unused) {
                        return;
                    }
                } else if (z) {
                    Bubble orCreateBubble = bubbleController.mBubbleData.getOrCreateBubble(null, bubble);
                    Objects.requireNonNull(orCreateBubble);
                    bubbleController.ensureStackViewCreated();
                    orCreateBubble.setInflateSynchronously(bubbleController.mInflateSynchronously);
                    orCreateBubble.inflate(new BubbleController$$ExternalSyntheticLambda3(bubbleController, orCreateBubble.isEnabled(1), !orCreateBubble.isEnabled(1)), bubbleController.mContext, bubbleController, bubbleController.mStackView, bubbleController.mBubbleIconFactory, bubbleController.mBubbleBadgeIconFactory, false);
                    return;
                } else {
                    return;
                }
            default:
                BubbleFlyoutView bubbleFlyoutView = (BubbleFlyoutView) this.f$0;
                PointF pointF = (PointF) this.f$1;
                boolean z2 = this.f$2;
                Runnable runnable = (Runnable) this.f$3;
                int i2 = BubbleFlyoutView.$r8$clinit;
                Objects.requireNonNull(bubbleFlyoutView);
                float height = ((bubbleFlyoutView.mBubbleSize - bubbleFlyoutView.mFlyoutTextContainer.getHeight()) / 2.0f) + pointF.y;
                bubbleFlyoutView.mFlyoutY = height;
                bubbleFlyoutView.setTranslationY(height);
                float f2 = pointF.x;
                if (bubbleFlyoutView.mArrowPointingLeft) {
                    f = f2 + bubbleFlyoutView.mBubbleSize + bubbleFlyoutView.mFlyoutSpaceFromBubble;
                } else {
                    f = (f2 - bubbleFlyoutView.getWidth()) - bubbleFlyoutView.mFlyoutSpaceFromBubble;
                }
                bubbleFlyoutView.mRestingTranslationX = f;
                bubbleFlyoutView.updateDot(pointF, z2);
                if (runnable != null) {
                    runnable.run();
                    return;
                }
                return;
        }
    }
}
