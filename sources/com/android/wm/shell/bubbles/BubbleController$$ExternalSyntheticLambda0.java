package com.android.wm.shell.bubbles;

import android.view.View;
import android.view.WindowInsets;
import com.android.systemui.accessibility.WindowMagnificationController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BubbleController$$ExternalSyntheticLambda0 implements View.OnApplyWindowInsetsListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ BubbleController$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.View.OnApplyWindowInsetsListener
    public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
        switch (this.$r8$classId) {
            case 0:
                BubbleController bubbleController = (BubbleController) this.f$0;
                Objects.requireNonNull(bubbleController);
                if (!windowInsets.equals(bubbleController.mWindowInsets)) {
                    bubbleController.mWindowInsets = windowInsets;
                    bubbleController.mBubblePositioner.update();
                    bubbleController.mStackView.onDisplaySizeChanged();
                }
                return windowInsets;
            default:
                WindowMagnificationController windowMagnificationController = (WindowMagnificationController) this.f$0;
                boolean z = WindowMagnificationController.DEBUG;
                Objects.requireNonNull(windowMagnificationController);
                if (!windowMagnificationController.mHandler.hasCallbacks(windowMagnificationController.mWindowInsetChangeRunnable)) {
                    windowMagnificationController.mHandler.post(windowMagnificationController.mWindowInsetChangeRunnable);
                }
                return view.onApplyWindowInsets(windowInsets);
        }
    }
}
