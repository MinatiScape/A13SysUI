package com.android.wm.shell.startingsurface;

import android.app.ActivityManager;
import android.app.ActivityThread;
import android.app.ContextImpl;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.RemoteException;
import android.util.MergedConfiguration;
import android.view.IWindowSession;
import android.view.InsetsState;
import android.view.SurfaceControl;
import android.view.WindowManagerGlobal;
import android.window.ClientWindowFrames;
import android.window.TaskSnapshot;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.policy.DecorView;
import com.android.internal.view.BaseIWindow;
import com.android.systemui.screenshot.ScreenshotController$$ExternalSyntheticLambda6;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.startingsurface.TaskSnapshotWindow;
import java.util.Objects;
/* loaded from: classes.dex */
public final class TaskSnapshotWindow {
    public final int mActivityType;
    public final Paint mBackgroundPaint;
    public final Runnable mClearWindowHandler;
    public boolean mHasDrawn;
    public final boolean mHasImeSurface;
    public final int mOrientationOnCreation;
    public ScreenshotController$$ExternalSyntheticLambda6 mScheduledRunnable;
    public final IWindowSession mSession;
    public boolean mSizeMismatch;
    public TaskSnapshot mSnapshot;
    public final ShellExecutor mSplashScreenExecutor;
    public final int mStatusBarColor;
    public final SurfaceControl mSurfaceControl;
    public final SystemBarBackgroundPainter mSystemBarBackgroundPainter;
    public final Rect mTaskBounds;
    public final CharSequence mTitle;
    public final SurfaceControl.Transaction mTransaction;
    public final Window mWindow;
    public final Rect mFrame = new Rect();
    public final Rect mSystemBarInsets = new Rect();
    public final RectF mTmpSnapshotSize = new RectF();
    public final RectF mTmpDstFrame = new RectF();
    public final Matrix mSnapshotMatrix = new Matrix();
    public final float[] mTmpFloat9 = new float[9];

    /* loaded from: classes.dex */
    public static class SystemBarBackgroundPainter {
        public final InsetsState mInsetsState;
        public final int mNavigationBarColor;
        public final Paint mNavigationBarPaint;
        public final int mStatusBarColor;
        public final Paint mStatusBarPaint;
        public final int mWindowFlags;
        public final int mWindowPrivateFlags;
        public final Rect mSystemBarInsets = new Rect();
        public final float mScale = 1.0f;

        public SystemBarBackgroundPainter(int i, int i2, int i3, ActivityManager.TaskDescription taskDescription, InsetsState insetsState) {
            boolean z;
            Paint paint = new Paint();
            this.mStatusBarPaint = paint;
            Paint paint2 = new Paint();
            this.mNavigationBarPaint = paint2;
            this.mWindowFlags = i;
            this.mWindowPrivateFlags = i2;
            ContextImpl systemUiContext = ActivityThread.currentActivityThread().getSystemUiContext();
            int color = systemUiContext.getColor(17171091);
            int calculateBarColor = DecorView.calculateBarColor(i, 67108864, color, taskDescription.getStatusBarColor(), i3, 8, taskDescription.getEnsureStatusBarContrastWhenTransparent());
            this.mStatusBarColor = calculateBarColor;
            int navigationBarColor = taskDescription.getNavigationBarColor();
            if (!taskDescription.getEnsureNavigationBarContrastWhenTransparent() || !systemUiContext.getResources().getBoolean(17891702)) {
                z = false;
            } else {
                z = true;
            }
            int calculateBarColor2 = DecorView.calculateBarColor(i, 134217728, color, navigationBarColor, i3, 16, z);
            this.mNavigationBarColor = calculateBarColor2;
            paint.setColor(calculateBarColor);
            paint2.setColor(calculateBarColor2);
            this.mInsetsState = insetsState;
        }

        @VisibleForTesting
        public void drawNavigationBarBackground(Canvas canvas) {
            boolean z;
            Rect rect = new Rect();
            DecorView.getNavigationBarRect(canvas.getWidth(), canvas.getHeight(), this.mSystemBarInsets, rect, this.mScale);
            if ((this.mWindowPrivateFlags & 131072) != 0) {
                z = true;
            } else {
                z = false;
            }
            if (DecorView.NAVIGATION_BAR_COLOR_VIEW_ATTRIBUTES.isVisible(this.mInsetsState, this.mNavigationBarColor, this.mWindowFlags, z) && Color.alpha(this.mNavigationBarColor) != 0 && !rect.isEmpty()) {
                canvas.drawRect(rect, this.mNavigationBarPaint);
            }
        }

        public final int getStatusBarColorViewHeight() {
            boolean z;
            if ((this.mWindowPrivateFlags & 131072) != 0) {
                z = true;
            } else {
                z = false;
            }
            if (DecorView.STATUS_BAR_COLOR_VIEW_ATTRIBUTES.isVisible(this.mInsetsState, this.mStatusBarColor, this.mWindowFlags, z)) {
                return (int) (this.mSystemBarInsets.top * this.mScale);
            }
            return 0;
        }
    }

    /* loaded from: classes.dex */
    public static class Window extends BaseIWindow {
        public TaskSnapshotWindow mOuter;

        public final void resized(ClientWindowFrames clientWindowFrames, final boolean z, final MergedConfiguration mergedConfiguration, boolean z2, boolean z3, int i) {
            TaskSnapshotWindow taskSnapshotWindow = this.mOuter;
            if (taskSnapshotWindow != null) {
                taskSnapshotWindow.mSplashScreenExecutor.execute(new Runnable() { // from class: com.android.wm.shell.startingsurface.TaskSnapshotWindow$Window$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        TaskSnapshotWindow.Window window = TaskSnapshotWindow.Window.this;
                        MergedConfiguration mergedConfiguration2 = mergedConfiguration;
                        boolean z4 = z;
                        if (mergedConfiguration2 != null && window.mOuter.mOrientationOnCreation != mergedConfiguration2.getMergedConfiguration().orientation) {
                            TaskSnapshotWindow taskSnapshotWindow2 = window.mOuter;
                            Objects.requireNonNull(taskSnapshotWindow2);
                            taskSnapshotWindow2.mSplashScreenExecutor.executeDelayed(taskSnapshotWindow2.mClearWindowHandler, 0L);
                        } else if (z4) {
                            TaskSnapshotWindow taskSnapshotWindow3 = window.mOuter;
                            if (taskSnapshotWindow3.mHasDrawn) {
                                try {
                                    taskSnapshotWindow3.mSession.finishDrawing(taskSnapshotWindow3.mWindow, (SurfaceControl.Transaction) null);
                                } catch (RemoteException unused) {
                                    taskSnapshotWindow3.mSplashScreenExecutor.executeDelayed(taskSnapshotWindow3.mClearWindowHandler, 0L);
                                }
                            }
                        } else {
                            Objects.requireNonNull(window);
                        }
                    }
                });
            }
        }
    }

    public TaskSnapshotWindow(SurfaceControl surfaceControl, TaskSnapshot taskSnapshot, CharSequence charSequence, ActivityManager.TaskDescription taskDescription, int i, int i2, int i3, Rect rect, int i4, int i5, InsetsState insetsState, StartingSurfaceDrawer$$ExternalSyntheticLambda2 startingSurfaceDrawer$$ExternalSyntheticLambda2, ShellExecutor shellExecutor) {
        Paint paint = new Paint();
        this.mBackgroundPaint = paint;
        this.mSplashScreenExecutor = shellExecutor;
        IWindowSession windowSession = WindowManagerGlobal.getWindowSession();
        this.mSession = windowSession;
        Window window = new Window();
        this.mWindow = window;
        window.setSession(windowSession);
        this.mSurfaceControl = surfaceControl;
        this.mSnapshot = taskSnapshot;
        this.mTitle = charSequence;
        int backgroundColor = taskDescription.getBackgroundColor();
        paint.setColor(backgroundColor == 0 ? -1 : backgroundColor);
        this.mTaskBounds = rect;
        this.mSystemBarBackgroundPainter = new SystemBarBackgroundPainter(i2, i3, i, taskDescription, insetsState);
        this.mStatusBarColor = taskDescription.getStatusBarColor();
        this.mOrientationOnCreation = i4;
        this.mActivityType = i5;
        this.mTransaction = new SurfaceControl.Transaction();
        this.mClearWindowHandler = startingSurfaceDrawer$$ExternalSyntheticLambda2;
        this.mHasImeSurface = taskSnapshot.hasImeSurface();
    }

    public final void removeImmediately() {
        this.mSplashScreenExecutor.removeCallbacks(this.mScheduledRunnable);
        try {
            if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, 1218213214, 3, null, Boolean.valueOf(this.mHasDrawn));
            }
            this.mSession.remove(this.mWindow);
        } catch (RemoteException unused) {
        }
    }
}
