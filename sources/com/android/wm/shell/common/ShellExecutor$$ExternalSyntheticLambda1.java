package com.android.wm.shell.common;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter$$ExternalSyntheticLambda1;
import com.android.systemui.statusbar.phone.StatusBarNotificationPresenter;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.function.Supplier;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ShellExecutor$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;
    public final /* synthetic */ Object f$2;

    public /* synthetic */ ShellExecutor$$ExternalSyntheticLambda1(Object obj, Object obj2, Object obj3, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
        this.f$2 = obj3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                ((Object[]) this.f$0)[0] = ((Supplier) this.f$1).get();
                ((CountDownLatch) this.f$2).countDown();
                return;
            default:
                StatusBarNotificationActivityStarter statusBarNotificationActivityStarter = (StatusBarNotificationActivityStarter) this.f$0;
                Objects.requireNonNull(statusBarNotificationActivityStarter);
                StatusBarNotificationActivityStarter$$ExternalSyntheticLambda1 statusBarNotificationActivityStarter$$ExternalSyntheticLambda1 = new StatusBarNotificationActivityStarter$$ExternalSyntheticLambda1(statusBarNotificationActivityStarter, (NotificationEntry) this.f$1, (NotificationEntry) this.f$2, 0);
                if (((StatusBarNotificationPresenter) statusBarNotificationActivityStarter.mPresenter).isCollapsing()) {
                    statusBarNotificationActivityStarter.mShadeController.addPostCollapseAction(statusBarNotificationActivityStarter$$ExternalSyntheticLambda1);
                    return;
                } else {
                    statusBarNotificationActivityStarter$$ExternalSyntheticLambda1.run();
                    return;
                }
        }
    }
}
