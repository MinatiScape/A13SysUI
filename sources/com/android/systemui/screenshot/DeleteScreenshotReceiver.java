package com.android.systemui.screenshot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.android.keyguard.clock.ClockManager$$ExternalSyntheticLambda1;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class DeleteScreenshotReceiver extends BroadcastReceiver {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final Executor mBackgroundExecutor;
    public final ScreenshotSmartActions mScreenshotSmartActions;

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        if (intent.hasExtra("android:screenshot_uri_id")) {
            this.mBackgroundExecutor.execute(new ClockManager$$ExternalSyntheticLambda1(context, Uri.parse(intent.getStringExtra("android:screenshot_uri_id")), 2));
            if (intent.getBooleanExtra("android:smart_actions_enabled", false)) {
                ScreenshotSmartActions screenshotSmartActions = this.mScreenshotSmartActions;
                String stringExtra = intent.getStringExtra("android:screenshot_id");
                Objects.requireNonNull(screenshotSmartActions);
                ScreenshotSmartActions.notifyScreenshotAction(context, stringExtra, "Delete", false, null);
            }
        }
    }

    public DeleteScreenshotReceiver(ScreenshotSmartActions screenshotSmartActions, Executor executor) {
        this.mScreenshotSmartActions = screenshotSmartActions;
        this.mBackgroundExecutor = executor;
    }
}
