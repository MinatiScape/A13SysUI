package com.android.systemui.statusbar.notification.stack;

import android.app.PendingIntent;
import com.android.systemui.statusbar.notification.row.wrapper.NotificationTemplateViewWrapper;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class StackStateAnimator$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;
    public final /* synthetic */ Runnable f$2;

    public /* synthetic */ StackStateAnimator$$ExternalSyntheticLambda0(Object obj, Object obj2, Runnable runnable, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
        this.f$2 = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                StackStateAnimator stackStateAnimator = (StackStateAnimator) this.f$0;
                Runnable runnable = this.f$2;
                Objects.requireNonNull(stackStateAnimator);
                stackStateAnimator.mLogger.disappearAnimationEnded((String) this.f$1);
                if (runnable != null) {
                    runnable.run();
                    return;
                }
                return;
            default:
                NotificationTemplateViewWrapper notificationTemplateViewWrapper = (NotificationTemplateViewWrapper) this.f$0;
                Runnable runnable2 = this.f$2;
                int i = NotificationTemplateViewWrapper.$r8$clinit;
                Objects.requireNonNull(notificationTemplateViewWrapper);
                notificationTemplateViewWrapper.mCancelledPendingIntents.add((PendingIntent) this.f$1);
                runnable2.run();
                return;
        }
    }
}
