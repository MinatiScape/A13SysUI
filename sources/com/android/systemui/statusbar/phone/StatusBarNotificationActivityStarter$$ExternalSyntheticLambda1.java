package com.android.systemui.statusbar.phone;

import androidx.dynamicanimation.animation.DynamicAnimation;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class StatusBarNotificationActivityStarter$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;
    public final /* synthetic */ Object f$2;

    public /* synthetic */ StatusBarNotificationActivityStarter$$ExternalSyntheticLambda1(Object obj, Object obj2, Object obj3, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
        this.f$2 = obj3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        boolean z = true;
        switch (this.$r8$classId) {
            case 0:
                StatusBarNotificationActivityStarter statusBarNotificationActivityStarter = (StatusBarNotificationActivityStarter) this.f$0;
                Objects.requireNonNull(statusBarNotificationActivityStarter);
                statusBarNotificationActivityStarter.mOnUserInteractionCallback.onDismiss((NotificationEntry) this.f$1, 1, (NotificationEntry) this.f$2);
                return;
            default:
                PhysicsAnimationLayout.PhysicsAnimationController physicsAnimationController = (PhysicsAnimationLayout.PhysicsAnimationController) this.f$0;
                DynamicAnimation.ViewProperty[] viewPropertyArr = (DynamicAnimation.ViewProperty[]) this.f$1;
                Runnable runnable = (Runnable) this.f$2;
                Objects.requireNonNull(physicsAnimationController);
                PhysicsAnimationLayout physicsAnimationLayout = physicsAnimationController.mLayout;
                Objects.requireNonNull(physicsAnimationLayout);
                int i = 0;
                while (true) {
                    if (i >= physicsAnimationLayout.getChildCount()) {
                        z = false;
                    } else if (!PhysicsAnimationLayout.arePropertiesAnimatingOnView(physicsAnimationLayout.getChildAt(i), viewPropertyArr)) {
                        i++;
                    }
                }
                if (!z) {
                    runnable.run();
                    for (DynamicAnimation.ViewProperty viewProperty : viewPropertyArr) {
                        physicsAnimationController.mLayout.mEndActionForProperty.remove(viewProperty);
                    }
                    return;
                }
                return;
        }
    }
}
