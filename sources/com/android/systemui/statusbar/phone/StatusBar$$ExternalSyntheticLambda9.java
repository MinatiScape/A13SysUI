package com.android.systemui.statusbar.phone;

import android.app.PendingIntent;
import android.view.RemoteAnimationAdapter;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class StatusBar$$ExternalSyntheticLambda9 implements NotificationGuts.OnHeightChangedListener, ActivityLaunchAnimator.PendingIntentStarter {
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ StatusBar$$ExternalSyntheticLambda9(Object obj, Object obj2) {
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.PendingIntentStarter
    public final int startPendingIntent(RemoteAnimationAdapter remoteAnimationAdapter) {
        StatusBar statusBar = (StatusBar) this.f$0;
        PendingIntent pendingIntent = (PendingIntent) this.f$1;
        long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
        Objects.requireNonNull(statusBar);
        return pendingIntent.sendAndReturnResult(null, 0, null, null, null, null, StatusBar.getActivityOptions(statusBar.mDisplayId, remoteAnimationAdapter));
    }
}
