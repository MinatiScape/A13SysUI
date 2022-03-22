package com.android.wm.shell.bubbles.animation;

import androidx.dynamicanimation.animation.DynamicAnimation;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter$$ExternalSyntheticLambda1;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda15;
import com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PhysicsAnimationLayout$PhysicsAnimationController$$ExternalSyntheticLambda0 {
    public final /* synthetic */ PhysicsAnimationLayout.PhysicsAnimationController f$0;
    public final /* synthetic */ Set f$1;
    public final /* synthetic */ List f$2;

    public /* synthetic */ PhysicsAnimationLayout$PhysicsAnimationController$$ExternalSyntheticLambda0(PhysicsAnimationLayout.PhysicsAnimationController physicsAnimationController, HashSet hashSet, ArrayList arrayList) {
        this.f$0 = physicsAnimationController;
        this.f$1 = hashSet;
        this.f$2 = arrayList;
    }

    public final void startAll(Runnable[] runnableArr) {
        PhysicsAnimationLayout.PhysicsAnimationController physicsAnimationController = this.f$0;
        Set set = this.f$1;
        List<PhysicsAnimationLayout.PhysicsPropertyAnimator> list = this.f$2;
        Objects.requireNonNull(physicsAnimationController);
        BubbleStackView$$ExternalSyntheticLambda15 bubbleStackView$$ExternalSyntheticLambda15 = new BubbleStackView$$ExternalSyntheticLambda15(runnableArr, 7);
        if (physicsAnimationController.mLayout.getChildCount() == 0) {
            bubbleStackView$$ExternalSyntheticLambda15.run();
            return;
        }
        DynamicAnimation.ViewProperty[] viewPropertyArr = (DynamicAnimation.ViewProperty[]) set.toArray(new DynamicAnimation.ViewProperty[0]);
        StatusBarNotificationActivityStarter$$ExternalSyntheticLambda1 statusBarNotificationActivityStarter$$ExternalSyntheticLambda1 = new StatusBarNotificationActivityStarter$$ExternalSyntheticLambda1(physicsAnimationController, viewPropertyArr, bubbleStackView$$ExternalSyntheticLambda15, 1);
        for (DynamicAnimation.ViewProperty viewProperty : viewPropertyArr) {
            physicsAnimationController.mLayout.mEndActionForProperty.put(viewProperty, statusBarNotificationActivityStarter$$ExternalSyntheticLambda1);
        }
        for (PhysicsAnimationLayout.PhysicsPropertyAnimator physicsPropertyAnimator : list) {
            physicsPropertyAnimator.start(new Runnable[0]);
        }
    }
}
