package com.android.wm.shell.compatui;

import android.app.TaskInfo;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.window.TaskAppearedInfo;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.FrameworkStatsLog;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.compatui.CompatUIController;
import java.util.Objects;
/* loaded from: classes.dex */
public final class CompatUIWindowManager extends CompatUIWindowManagerAbstract {
    public final CompatUIController.CompatUICallback mCallback;
    @VisibleForTesting
    public int mCameraCompatControlState;
    @VisibleForTesting
    public CompatUIHintsState mCompatUIHintsState;
    @VisibleForTesting
    public boolean mHasSizeCompat;
    @VisibleForTesting
    public CompatUILayout mLayout;

    /* loaded from: classes.dex */
    public static class CompatUIHintsState {
        @VisibleForTesting
        public boolean mHasShownCameraCompatHint;
        @VisibleForTesting
        public boolean mHasShownSizeCompatHint;
    }

    public CompatUIWindowManager(Context context, TaskInfo taskInfo, SyncTransactionQueue syncTransactionQueue, CompatUIController.CompatUICallback compatUICallback, ShellTaskOrganizer.TaskListener taskListener, DisplayLayout displayLayout, CompatUIHintsState compatUIHintsState) {
        super(context, taskInfo, syncTransactionQueue, taskListener, displayLayout);
        this.mCallback = compatUICallback;
        this.mHasSizeCompat = taskInfo.topActivityInSizeCompat;
        this.mCameraCompatControlState = taskInfo.cameraCompatControlState;
        this.mCompatUIHintsState = compatUIHintsState;
    }

    @Override // com.android.wm.shell.compatui.CompatUIWindowManagerAbstract
    public final int getZOrder() {
        return 2147483646;
    }

    @Override // com.android.wm.shell.compatui.CompatUIWindowManagerAbstract
    public final void removeLayout() {
        this.mLayout = null;
    }

    @Override // com.android.wm.shell.compatui.CompatUIWindowManagerAbstract
    public final boolean eligibleToShowLayout() {
        if (this.mHasSizeCompat || shouldShowCameraControl()) {
            return true;
        }
        return false;
    }

    @VisibleForTesting
    public CompatUILayout inflateLayout() {
        return (CompatUILayout) LayoutInflater.from(this.mContext).inflate(2131624039, (ViewGroup) null);
    }

    public final boolean shouldShowCameraControl() {
        int i = this.mCameraCompatControlState;
        if (i == 0 || i == 3) {
            return false;
        }
        return true;
    }

    @Override // com.android.wm.shell.compatui.CompatUIWindowManagerAbstract
    public final boolean updateCompatInfo(TaskInfo taskInfo, ShellTaskOrganizer.TaskListener taskListener, boolean z) {
        boolean z2 = this.mHasSizeCompat;
        int i = this.mCameraCompatControlState;
        this.mHasSizeCompat = taskInfo.topActivityInSizeCompat;
        this.mCameraCompatControlState = taskInfo.cameraCompatControlState;
        if (!super.updateCompatInfo(taskInfo, taskListener, z)) {
            return false;
        }
        if (z2 == this.mHasSizeCompat && i == this.mCameraCompatControlState) {
            return true;
        }
        updateVisibilityOfViews();
        return true;
    }

    @Override // com.android.wm.shell.compatui.CompatUIWindowManagerAbstract
    @VisibleForTesting
    public void updateSurfacePosition() {
        int i;
        int i2;
        if (this.mLayout != null) {
            Rect bounds = this.mTaskConfig.windowConfiguration.getBounds();
            Rect rect = new Rect(this.mStableBounds);
            rect.intersect(this.mTaskConfig.windowConfiguration.getBounds());
            if (this.mContext.getResources().getConfiguration().getLayoutDirection() == 1) {
                i2 = rect.left;
                i = bounds.left;
            } else {
                i2 = rect.right - bounds.left;
                i = this.mLayout.getMeasuredWidth();
            }
            final int i3 = i2 - i;
            final int measuredHeight = (rect.bottom - bounds.top) - this.mLayout.getMeasuredHeight();
            if (this.mLeash != null) {
                this.mSyncQueue.runInSync(new SyncTransactionQueue.TransactionRunnable() { // from class: com.android.wm.shell.compatui.CompatUIWindowManagerAbstract$$ExternalSyntheticLambda1
                    @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
                    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
                        CompatUIWindowManagerAbstract compatUIWindowManagerAbstract = CompatUIWindowManagerAbstract.this;
                        int i4 = i3;
                        int i5 = measuredHeight;
                        Objects.requireNonNull(compatUIWindowManagerAbstract);
                        SurfaceControl surfaceControl = compatUIWindowManagerAbstract.mLeash;
                        if (surfaceControl == null || !surfaceControl.isValid()) {
                            Log.w(compatUIWindowManagerAbstract.getClass().getSimpleName(), "The leash has been released.");
                        } else {
                            transaction.setPosition(compatUIWindowManagerAbstract.mLeash, i4, i5);
                        }
                    }
                });
            }
        }
    }

    public final void updateVisibilityOfViews() {
        CompatUILayout compatUILayout = this.mLayout;
        if (compatUILayout != null) {
            boolean z = this.mHasSizeCompat;
            compatUILayout.setViewVisibility(2131428868, z);
            if (!z) {
                compatUILayout.setViewVisibility(2131428867, false);
            }
            if (this.mHasSizeCompat && !this.mCompatUIHintsState.mHasShownSizeCompatHint) {
                CompatUILayout compatUILayout2 = this.mLayout;
                Objects.requireNonNull(compatUILayout2);
                compatUILayout2.setViewVisibility(2131428867, true);
                this.mCompatUIHintsState.mHasShownSizeCompatHint = true;
            }
            CompatUILayout compatUILayout3 = this.mLayout;
            boolean shouldShowCameraControl = shouldShowCameraControl();
            Objects.requireNonNull(compatUILayout3);
            compatUILayout3.setViewVisibility(2131427655, shouldShowCameraControl);
            if (!shouldShowCameraControl) {
                compatUILayout3.setViewVisibility(2131427657, false);
            }
            if (shouldShowCameraControl() && !this.mCompatUIHintsState.mHasShownCameraCompatHint) {
                CompatUILayout compatUILayout4 = this.mLayout;
                Objects.requireNonNull(compatUILayout4);
                compatUILayout4.setViewVisibility(2131427657, true);
                this.mCompatUIHintsState.mHasShownCameraCompatHint = true;
            }
            if (shouldShowCameraControl()) {
                this.mLayout.updateCameraTreatmentButton(this.mCameraCompatControlState);
            }
        }
    }

    @Override // com.android.wm.shell.compatui.CompatUIWindowManagerAbstract
    public final View createLayout() {
        TaskAppearedInfo taskAppearedInfo;
        ActivityInfo activityInfo;
        CompatUILayout inflateLayout = inflateLayout();
        this.mLayout = inflateLayout;
        Objects.requireNonNull(inflateLayout);
        inflateLayout.mWindowManager = this;
        updateVisibilityOfViews();
        if (this.mHasSizeCompat) {
            CompatUIController.CompatUICallback compatUICallback = this.mCallback;
            int i = this.mTaskId;
            ShellTaskOrganizer shellTaskOrganizer = (ShellTaskOrganizer) compatUICallback;
            Objects.requireNonNull(shellTaskOrganizer);
            synchronized (shellTaskOrganizer.mLock) {
                taskAppearedInfo = shellTaskOrganizer.mTasks.get(i);
            }
            if (!(taskAppearedInfo == null || (activityInfo = taskAppearedInfo.getTaskInfo().topActivityInfo) == null)) {
                FrameworkStatsLog.write(387, activityInfo.applicationInfo.uid, 1);
            }
        }
        return this.mLayout;
    }

    @Override // com.android.wm.shell.compatui.CompatUIWindowManagerAbstract
    public final View getLayout() {
        return this.mLayout;
    }
}
