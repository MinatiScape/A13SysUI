package com.android.systemui.statusbar;

import android.view.View;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationViewController;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
/* loaded from: classes.dex */
public final class NotificationShelfController {
    public final ActivatableNotificationViewController mActivatableNotificationViewController;
    public AmbientState mAmbientState;
    public final KeyguardBypassController mKeyguardBypassController;
    public final AnonymousClass1 mOnAttachStateChangeListener = new AnonymousClass1();
    public final SysuiStatusBarStateController mStatusBarStateController;
    public final NotificationShelf mView;

    /* renamed from: com.android.systemui.statusbar.NotificationShelfController$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements View.OnAttachStateChangeListener {
        public AnonymousClass1() {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public final void onViewAttachedToWindow(View view) {
            NotificationShelfController notificationShelfController = NotificationShelfController.this;
            notificationShelfController.mStatusBarStateController.addCallback(notificationShelfController.mView, 3);
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public final void onViewDetachedFromWindow(View view) {
            NotificationShelfController notificationShelfController = NotificationShelfController.this;
            notificationShelfController.mStatusBarStateController.removeCallback(notificationShelfController.mView);
        }
    }

    public NotificationShelfController(NotificationShelf notificationShelf, ActivatableNotificationViewController activatableNotificationViewController, KeyguardBypassController keyguardBypassController, SysuiStatusBarStateController sysuiStatusBarStateController) {
        this.mView = notificationShelf;
        this.mActivatableNotificationViewController = activatableNotificationViewController;
        this.mKeyguardBypassController = keyguardBypassController;
        this.mStatusBarStateController = sysuiStatusBarStateController;
    }
}
