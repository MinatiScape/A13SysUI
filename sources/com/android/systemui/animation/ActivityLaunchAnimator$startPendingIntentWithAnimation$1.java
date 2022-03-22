package com.android.systemui.animation;

import android.view.RemoteAnimationAdapter;
import com.android.systemui.animation.ActivityLaunchAnimator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ActivityLaunchAnimator.kt */
/* loaded from: classes.dex */
public final class ActivityLaunchAnimator$startPendingIntentWithAnimation$1 extends Lambda implements Function1<RemoteAnimationAdapter, Integer> {
    public final /* synthetic */ ActivityLaunchAnimator.PendingIntentStarter $intentStarter;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ActivityLaunchAnimator$startPendingIntentWithAnimation$1(ActivityLaunchAnimator.PendingIntentStarter pendingIntentStarter) {
        super(1);
        this.$intentStarter = pendingIntentStarter;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Integer invoke(RemoteAnimationAdapter remoteAnimationAdapter) {
        return Integer.valueOf(this.$intentStarter.startPendingIntent(remoteAnimationAdapter));
    }
}
