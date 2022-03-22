package com.android.systemui.keyguard;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.View;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.broadcast.BroadcastDispatcher;
import java.util.Objects;
import java.util.concurrent.Callable;
/* loaded from: classes.dex */
public class WorkLockActivity extends Activity {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public KeyguardManager mKgm;
    public final AnonymousClass1 mLockEventReceiver = new BroadcastReceiver() { // from class: com.android.systemui.keyguard.WorkLockActivity.1
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            int targetUserId = WorkLockActivity.this.getTargetUserId();
            if (intent.getIntExtra("android.intent.extra.user_handle", targetUserId) == targetUserId && !WorkLockActivity.this.getKeyguardManager().isDeviceLocked(targetUserId)) {
                WorkLockActivity.this.finish();
            }
        }
    };

    @Override // android.app.Activity
    public final void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1 && i2 != -1) {
            Intent intent2 = new Intent("android.intent.action.MAIN");
            intent2.addCategory("android.intent.category.HOME");
            intent2.setFlags(268435456);
            startActivity(intent2);
        }
    }

    @Override // android.app.Activity
    public final void onBackPressed() {
    }

    @Override // android.app.Activity
    public final void setTaskDescription(ActivityManager.TaskDescription taskDescription) {
    }

    public final KeyguardManager getKeyguardManager() {
        if (this.mKgm == null) {
            this.mKgm = (KeyguardManager) getSystemService("keyguard");
        }
        return this.mKgm;
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public final void onWindowFocusChanged(boolean z) {
        Intent createConfirmDeviceCredentialIntent;
        if (z && !isFinishing() && getKeyguardManager().isDeviceLocked(getTargetUserId()) && (createConfirmDeviceCredentialIntent = getKeyguardManager().createConfirmDeviceCredentialIntent(null, null, getTargetUserId(), true)) != null) {
            ActivityOptions makeBasic = ActivityOptions.makeBasic();
            makeBasic.setLaunchTaskId(getTaskId());
            PendingIntent activity = PendingIntent.getActivity(this, -1, getIntent(), 1409286144, makeBasic.toBundle());
            if (activity != null) {
                createConfirmDeviceCredentialIntent.putExtra("android.intent.extra.INTENT", activity.getIntentSender());
            }
            ActivityOptions makeBasic2 = ActivityOptions.makeBasic();
            makeBasic2.setLaunchTaskId(getTaskId());
            makeBasic2.setTaskOverlay(true, true);
            startActivityForResult(createConfirmDeviceCredentialIntent, 1, makeBasic2.toBundle());
        }
    }

    @VisibleForTesting
    public void unregisterBroadcastReceiver() {
        this.mBroadcastDispatcher.unregisterReceiver(this.mLockEventReceiver);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.keyguard.WorkLockActivity$1] */
    public WorkLockActivity(BroadcastDispatcher broadcastDispatcher) {
        this.mBroadcastDispatcher = broadcastDispatcher;
    }

    @VisibleForTesting
    public final int getPrimaryColor() {
        ActivityManager.TaskDescription taskDescription = (ActivityManager.TaskDescription) getIntent().getExtra("com.android.systemui.keyguard.extra.TASK_DESCRIPTION");
        if (taskDescription == null || Color.alpha(taskDescription.getPrimaryColor()) != 255) {
            return ((DevicePolicyManager) getSystemService("device_policy")).getOrganizationColorForUser(getTargetUserId());
        }
        return taskDescription.getPrimaryColor();
    }

    @VisibleForTesting
    public final int getTargetUserId() {
        return getIntent().getIntExtra("android.intent.extra.USER_ID", UserHandle.myUserId());
    }

    @Override // android.app.Activity
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mBroadcastDispatcher.registerReceiver(this.mLockEventReceiver, new IntentFilter("android.intent.action.DEVICE_LOCKED_CHANGED"), null, UserHandle.ALL);
        if (!getKeyguardManager().isDeviceLocked(getTargetUserId())) {
            finish();
            return;
        }
        setOverlayWithDecorCaptionEnabled(true);
        String string = ((DevicePolicyManager) getSystemService(DevicePolicyManager.class)).getString("SystemUi.WORK_LOCK_ACCESSIBILITY", new Callable() { // from class: com.android.systemui.keyguard.WorkLockActivity$$ExternalSyntheticLambda0
            @Override // java.util.concurrent.Callable
            public final Object call() {
                WorkLockActivity workLockActivity = WorkLockActivity.this;
                int i = WorkLockActivity.$r8$clinit;
                Objects.requireNonNull(workLockActivity);
                return workLockActivity.getString(2131951713);
            }
        });
        View view = new View(this);
        view.setContentDescription(string);
        view.setBackgroundColor(getPrimaryColor());
        setContentView(view);
    }

    @Override // android.app.Activity
    public final void onDestroy() {
        unregisterBroadcastReceiver();
        super.onDestroy();
    }
}
