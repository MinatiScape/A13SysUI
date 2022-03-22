package com.android.wm.shell.compatui;

import android.app.TaskInfo;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.IWindow;
import android.view.SurfaceControl;
import android.view.SurfaceControlViewHost;
import android.view.SurfaceSession;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowlessWindowManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.SyncTransactionQueue;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class CompatUIWindowManagerAbstract extends WindowlessWindowManager {
    public Context mContext;
    public final int mDisplayId;
    public DisplayLayout mDisplayLayout;
    public SurfaceControl mLeash;
    public final Rect mStableBounds;
    public final SyncTransactionQueue mSyncQueue;
    public Configuration mTaskConfig;
    public final int mTaskId;
    public ShellTaskOrganizer.TaskListener mTaskListener;
    public SurfaceControlViewHost mViewHost;

    public final void attachToParentSurface(IWindow iWindow, SurfaceControl.Builder builder) {
        String simpleName = getClass().getSimpleName();
        SurfaceControl.Builder containerLayer = new SurfaceControl.Builder(new SurfaceSession()).setContainerLayer();
        SurfaceControl.Builder hidden = containerLayer.setName(simpleName + "Leash").setHidden(false);
        SurfaceControl.Builder callsite = hidden.setCallsite(simpleName + "#attachToParentSurface");
        attachToParentSurface(callsite);
        SurfaceControl build = callsite.build();
        this.mLeash = build;
        builder.setParent(build);
        final SurfaceControl surfaceControl = this.mLeash;
        final int zOrder = getZOrder();
        this.mSyncQueue.runInSync(new SyncTransactionQueue.TransactionRunnable() { // from class: com.android.wm.shell.compatui.CompatUIWindowManagerAbstract$$ExternalSyntheticLambda2
            @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
            public final void runWithTransaction(SurfaceControl.Transaction transaction) {
                CompatUIWindowManagerAbstract compatUIWindowManagerAbstract = CompatUIWindowManagerAbstract.this;
                SurfaceControl surfaceControl2 = surfaceControl;
                int i = zOrder;
                if (surfaceControl2 != null) {
                    Objects.requireNonNull(compatUIWindowManagerAbstract);
                    if (surfaceControl2.isValid()) {
                        transaction.setLayer(surfaceControl2, i);
                        return;
                    }
                }
                Objects.requireNonNull(compatUIWindowManagerAbstract);
                Log.w(compatUIWindowManagerAbstract.getClass().getSimpleName(), "The leash has been released.");
            }
        });
    }

    public abstract View createLayout();

    public abstract boolean eligibleToShowLayout();

    public abstract View getLayout();

    public WindowManager.LayoutParams getWindowLayoutParams() {
        View layout = getLayout();
        if (layout == null) {
            return new WindowManager.LayoutParams();
        }
        layout.measure(0, 0);
        return getWindowLayoutParams(layout.getMeasuredWidth(), layout.getMeasuredHeight());
    }

    public abstract int getZOrder();

    public abstract void removeLayout();

    public abstract void updateSurfacePosition();

    public CompatUIWindowManagerAbstract(Context context, TaskInfo taskInfo, SyncTransactionQueue syncTransactionQueue, ShellTaskOrganizer.TaskListener taskListener, DisplayLayout displayLayout) {
        super(taskInfo.configuration, (SurfaceControl) null, (IBinder) null);
        this.mContext = context;
        this.mSyncQueue = syncTransactionQueue;
        this.mTaskConfig = taskInfo.configuration;
        this.mDisplayId = context.getDisplayId();
        this.mTaskId = taskInfo.taskId;
        this.mTaskListener = taskListener;
        this.mDisplayLayout = displayLayout;
        Rect rect = new Rect();
        this.mStableBounds = rect;
        DisplayLayout displayLayout2 = this.mDisplayLayout;
        Objects.requireNonNull(displayLayout2);
        rect.set(0, 0, displayLayout2.mWidth, displayLayout2.mHeight);
        rect.inset(displayLayout2.mStableInsets);
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    public SurfaceControlViewHost createSurfaceViewHost() {
        Context context = this.mContext;
        return new SurfaceControlViewHost(context, context.getDisplay(), (WindowlessWindowManager) this);
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PROTECTED)
    public boolean updateCompatInfo(TaskInfo taskInfo, ShellTaskOrganizer.TaskListener taskListener, boolean z) {
        Configuration configuration = this.mTaskConfig;
        ShellTaskOrganizer.TaskListener taskListener2 = this.mTaskListener;
        Configuration configuration2 = taskInfo.configuration;
        this.mTaskConfig = configuration2;
        this.mTaskListener = taskListener;
        setConfiguration(configuration2);
        boolean z2 = false;
        if (!eligibleToShowLayout()) {
            release();
            return false;
        }
        View layout = getLayout();
        if (layout == null || taskListener2 != taskListener) {
            release();
            return createLayout(z);
        }
        boolean z3 = !this.mTaskConfig.windowConfiguration.getBounds().equals(configuration.windowConfiguration.getBounds());
        if (this.mTaskConfig.getLayoutDirection() != configuration.getLayoutDirection()) {
            z2 = true;
        }
        if (z3 || z2) {
            onParentBoundsChanged();
        }
        if (z2) {
            layout.setLayoutDirection(this.mTaskConfig.getLayoutDirection());
        }
        return true;
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public void updateDisplayLayout(DisplayLayout displayLayout) {
        Rect rect = this.mStableBounds;
        Rect rect2 = new Rect();
        Objects.requireNonNull(displayLayout);
        rect2.set(0, 0, displayLayout.mWidth, displayLayout.mHeight);
        rect2.inset(displayLayout.mStableInsets);
        this.mDisplayLayout = displayLayout;
        if (!rect.equals(rect2)) {
            this.mStableBounds.set(rect2);
            onParentBoundsChanged();
        }
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PROTECTED)
    public boolean createLayout(boolean z) {
        if (!eligibleToShowLayout()) {
            return false;
        }
        if (!z || getLayout() != null) {
            return true;
        }
        if (this.mViewHost == null) {
            SurfaceControlViewHost createSurfaceViewHost = createSurfaceViewHost();
            this.mViewHost = createSurfaceViewHost;
            createSurfaceViewHost.setView(createLayout(), getWindowLayoutParams());
            updateSurfacePosition();
            return true;
        }
        throw new IllegalStateException("A UI has already been created with this window manager.");
    }

    public void release() {
        View layout = getLayout();
        if (layout != null) {
            layout.setVisibility(8);
        }
        removeLayout();
        SurfaceControlViewHost surfaceControlViewHost = this.mViewHost;
        if (surfaceControlViewHost != null) {
            surfaceControlViewHost.release();
            this.mViewHost = null;
        }
        final SurfaceControl surfaceControl = this.mLeash;
        if (surfaceControl != null) {
            this.mSyncQueue.runInSync(new SyncTransactionQueue.TransactionRunnable() { // from class: com.android.wm.shell.compatui.CompatUIWindowManagerAbstract$$ExternalSyntheticLambda0
                @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
                public final void runWithTransaction(SurfaceControl.Transaction transaction) {
                    transaction.remove(surfaceControl);
                }
            });
            this.mLeash = null;
        }
    }

    public final void setConfiguration(Configuration configuration) {
        CompatUIWindowManagerAbstract.super.setConfiguration(configuration);
        this.mContext = this.mContext.createConfigurationContext(configuration);
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public void updateVisibility(boolean z) {
        int i;
        View layout = getLayout();
        if (layout == null) {
            createLayout(z);
            return;
        }
        if (!z || !eligibleToShowLayout()) {
            i = 8;
        } else {
            i = 0;
        }
        if (layout.getVisibility() != i) {
            layout.setVisibility(i);
        }
    }

    public final WindowManager.LayoutParams getWindowLayoutParams(int i, int i2) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(i, i2, 2038, 40, -3);
        layoutParams.token = new Binder();
        layoutParams.setTitle(getClass().getSimpleName() + this.mTaskId);
        layoutParams.privateFlags = layoutParams.privateFlags | 536870976;
        return layoutParams;
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    public void attachToParentSurface(SurfaceControl.Builder builder) {
        this.mTaskListener.attachChildSurfaceToTask(this.mTaskId, builder);
    }

    public void onParentBoundsChanged() {
        updateSurfacePosition();
    }
}
