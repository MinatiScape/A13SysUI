package com.android.wm.shell.legacysplitscreen;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.util.ArraySet;
import com.android.systemui.user.CreateUserActivity$$ExternalSyntheticLambda2;
import com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda1;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.legacysplitscreen.DividerView;
/* loaded from: classes.dex */
public final class ForcedResizableInfoActivityController implements DividerView.DividerCallbacks {
    public final Context mContext;
    public boolean mDividerDragging;
    public final ShellCommandHandlerImpl$$ExternalSyntheticLambda1 mDockedStackExistsListener;
    public final ShellExecutor mMainExecutor;
    public final ArraySet<PendingTaskRecord> mPendingTasks = new ArraySet<>();
    public final ArraySet<String> mPackagesShownInSession = new ArraySet<>();
    public final CreateUserActivity$$ExternalSyntheticLambda2 mTimeoutRunnable = new CreateUserActivity$$ExternalSyntheticLambda2(this, 6);

    /* loaded from: classes.dex */
    public class PendingTaskRecord {
        public int mReason;
        public int mTaskId;

        public PendingTaskRecord(int i, int i2) {
            this.mTaskId = i;
            this.mReason = i2;
        }
    }

    public final void showPending() {
        this.mMainExecutor.removeCallbacks(this.mTimeoutRunnable);
        for (int size = this.mPendingTasks.size() - 1; size >= 0; size--) {
            PendingTaskRecord valueAt = this.mPendingTasks.valueAt(size);
            Intent intent = new Intent(this.mContext, ForcedResizableInfoActivity.class);
            ActivityOptions makeBasic = ActivityOptions.makeBasic();
            makeBasic.setLaunchTaskId(valueAt.mTaskId);
            makeBasic.setTaskOverlay(true, true);
            intent.putExtra("extra_forced_resizeable_reason", valueAt.mReason);
            this.mContext.startActivityAsUser(intent, makeBasic.toBundle(), UserHandle.CURRENT);
        }
        this.mPendingTasks.clear();
    }

    public ForcedResizableInfoActivityController(Context context, LegacySplitScreenController legacySplitScreenController, ShellExecutor shellExecutor) {
        ShellCommandHandlerImpl$$ExternalSyntheticLambda1 shellCommandHandlerImpl$$ExternalSyntheticLambda1 = new ShellCommandHandlerImpl$$ExternalSyntheticLambda1(this, 3);
        this.mDockedStackExistsListener = shellCommandHandlerImpl$$ExternalSyntheticLambda1;
        this.mContext = context;
        this.mMainExecutor = shellExecutor;
        legacySplitScreenController.registerInSplitScreenListener(shellCommandHandlerImpl$$ExternalSyntheticLambda1);
    }
}
