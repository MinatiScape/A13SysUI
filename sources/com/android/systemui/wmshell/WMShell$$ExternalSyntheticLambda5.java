package com.android.systemui.wmshell;

import android.content.res.Configuration;
import android.view.GestureDetector;
import android.view.MotionEvent;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.wm.shell.bubbles.Bubble;
import com.android.wm.shell.bubbles.BubbleData;
import com.android.wm.shell.draganddrop.DragAndDrop;
import com.android.wm.shell.onehanded.OneHandedAnimationCallback;
import com.android.wm.shell.onehanded.OneHandedAnimationController;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WMShell$$ExternalSyntheticLambda5 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ WMShell$$ExternalSyntheticLambda5(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                WMShell wMShell = (WMShell) this.f$0;
                final DragAndDrop dragAndDrop = (DragAndDrop) obj;
                Objects.requireNonNull(wMShell);
                wMShell.mConfigurationController.addCallback(new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.wmshell.WMShell.16
                    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
                    public final void onConfigChanged(Configuration configuration) {
                        dragAndDrop.onConfigChanged(configuration);
                    }

                    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
                    public final void onThemeChanged() {
                        dragAndDrop.onThemeChanged();
                    }
                });
                return;
            case 1:
                ((GestureDetector.OnGestureListener) obj).onLongPress((MotionEvent) this.f$0);
                return;
            case 2:
                BubbleData bubbleData = (BubbleData) this.f$0;
                Bubble bubble = (Bubble) obj;
                Comparator<Bubble> comparator = BubbleData.BUBBLES_BY_SORT_KEY_DESCENDING;
                Objects.requireNonNull(bubbleData);
                Objects.requireNonNull(bubble);
                bubbleData.doRemove(bubble.mKey, 2);
                return;
            default:
                OneHandedAnimationController.OneHandedTransitionAnimator oneHandedTransitionAnimator = (OneHandedAnimationController.OneHandedTransitionAnimator) this.f$0;
                int i = OneHandedAnimationController.OneHandedTransitionAnimator.$r8$clinit;
                Objects.requireNonNull(oneHandedTransitionAnimator);
                ((OneHandedAnimationCallback) obj).onOneHandedAnimationCancel(oneHandedTransitionAnimator);
                return;
        }
    }
}
