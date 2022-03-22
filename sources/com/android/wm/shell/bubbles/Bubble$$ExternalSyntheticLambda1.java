package com.android.wm.shell.bubbles;

import android.view.View;
import androidx.lifecycle.Lifecycle;
import com.android.systemui.dock.DockManager;
import com.android.systemui.dreams.DreamOverlayService;
import com.android.systemui.screenshot.ImageLoader$$ExternalSyntheticLambda0;
import com.android.wm.shell.bubbles.Bubbles;
import com.google.android.systemui.lowlightclock.LowLightDockManager;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class Bubble$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ Bubble$$ExternalSyntheticLambda1(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                Bubble bubble = (Bubble) this.f$0;
                Objects.requireNonNull(bubble);
                ImageLoader$$ExternalSyntheticLambda0 imageLoader$$ExternalSyntheticLambda0 = (ImageLoader$$ExternalSyntheticLambda0) ((Bubbles.PendingIntentCanceledListener) this.f$1);
                Objects.requireNonNull(imageLoader$$ExternalSyntheticLambda0);
                BubbleController bubbleController = (BubbleController) imageLoader$$ExternalSyntheticLambda0.f$0;
                Objects.requireNonNull(bubbleController);
                if (bubble.mIntent != null) {
                    if (bubble.mIntentActive || bubbleController.mBubbleData.hasBubbleInStackWithKey(bubble.mKey)) {
                        bubble.mPendingIntentCanceled = true;
                        return;
                    } else {
                        bubbleController.mMainExecutor.execute(new BubbleController$$ExternalSyntheticLambda5(bubbleController, bubble, 0));
                        return;
                    }
                } else {
                    return;
                }
            case 1:
                DreamOverlayService dreamOverlayService = (DreamOverlayService) this.f$0;
                boolean z = DreamOverlayService.DEBUG;
                Objects.requireNonNull(dreamOverlayService);
                dreamOverlayService.mLifecycleRegistry.setCurrentState((Lifecycle.State) this.f$1);
                return;
            case 2:
                ((Consumer) this.f$0).accept((String) this.f$1);
                return;
            case 3:
                ((View) this.f$0).setTag(2131428691, null);
                ((Runnable) this.f$1).run();
                return;
            default:
                LowLightDockManager lowLightDockManager = (LowLightDockManager) this.f$0;
                boolean z2 = LowLightDockManager.DEBUG;
                Objects.requireNonNull(lowLightDockManager);
                ((DockManager.DockEventListener) this.f$1).onEvent(lowLightDockManager.mDockState);
                return;
        }
    }
}
