package com.android.wm.shell.onehanded;

import androidx.dynamicanimation.animation.DynamicAnimation;
import com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class OneHandedDisplayAreaOrganizer$$ExternalSyntheticLambda0 implements PhysicsAnimationLayout.PhysicsAnimationController.ChildAnimationConfigurator {
    public static final /* synthetic */ OneHandedDisplayAreaOrganizer$$ExternalSyntheticLambda0 INSTANCE$1 = new OneHandedDisplayAreaOrganizer$$ExternalSyntheticLambda0();
    public static final /* synthetic */ OneHandedDisplayAreaOrganizer$$ExternalSyntheticLambda0 INSTANCE = new OneHandedDisplayAreaOrganizer$$ExternalSyntheticLambda0();

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController.ChildAnimationConfigurator
    public final void configureAnimationForChildAtIndex(int i, PhysicsAnimationLayout.PhysicsPropertyAnimator physicsPropertyAnimator) {
        physicsPropertyAnimator.property(DynamicAnimation.SCALE_X, 1.0f, new Runnable[0]);
        physicsPropertyAnimator.property(DynamicAnimation.SCALE_Y, 1.0f, new Runnable[0]);
        physicsPropertyAnimator.property(DynamicAnimation.ALPHA, 1.0f, new Runnable[0]);
    }
}
