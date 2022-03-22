package com.android.wm.shell.draganddrop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.StatusBarManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.Rect;
import android.view.WindowInsets;
import android.widget.LinearLayout;
import com.android.launcher3.icons.IconProvider;
import com.android.wm.shell.draganddrop.DragAndDropPolicy;
import com.android.wm.shell.splitscreen.SplitScreenController;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DragLayout extends LinearLayout {
    public int mDisplayMargin;
    public int mDividerSize;
    public DropZoneView mDropZoneView1;
    public DropZoneView mDropZoneView2;
    public boolean mHasDropped;
    public final IconProvider mIconProvider;
    public boolean mIsShowing;
    public final DragAndDropPolicy mPolicy;
    public final SplitScreenController mSplitScreenController;
    public final StatusBarManager mStatusBarManager;
    public DragAndDropPolicy.Target mCurrentTarget = null;
    public Insets mInsets = Insets.NONE;

    public static int getResizingBackgroundColor(ActivityManager.RunningTaskInfo runningTaskInfo) {
        int backgroundColor = runningTaskInfo.taskDescription.getBackgroundColor();
        if (backgroundColor == -1) {
            backgroundColor = -1;
        }
        return Color.valueOf(backgroundColor).toArgb();
    }

    public final void animateSplitContainers(boolean z, final Runnable runnable) {
        int i;
        ObjectAnimator objectAnimator;
        StatusBarManager statusBarManager = this.mStatusBarManager;
        if (z) {
            i = 9830400;
        } else {
            i = 0;
        }
        statusBarManager.disable(i);
        this.mDropZoneView1.setShowingMargin(z);
        this.mDropZoneView2.setShowingMargin(z);
        DropZoneView dropZoneView = this.mDropZoneView1;
        Objects.requireNonNull(dropZoneView);
        ObjectAnimator objectAnimator2 = dropZoneView.mMarginAnimator;
        if (objectAnimator2 == null || !objectAnimator2.isRunning()) {
            ObjectAnimator objectAnimator3 = dropZoneView.mBackgroundAnimator;
            if (objectAnimator3 == null || !objectAnimator3.isRunning()) {
                objectAnimator = null;
            } else {
                objectAnimator = dropZoneView.mBackgroundAnimator;
            }
        } else {
            objectAnimator = dropZoneView.mMarginAnimator;
        }
        if (runnable == null) {
            return;
        }
        if (objectAnimator != null) {
            objectAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.wm.shell.draganddrop.DragLayout.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    runnable.run();
                }
            });
        } else {
            runnable.run();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x007d  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x010f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void recomputeDropTargets() {
        /*
            Method dump skipped, instructions count: 328
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.draganddrop.DragLayout.recomputeDropTargets():void");
    }

    public final void updateContainerMargins(int i) {
        int i2 = this.mDisplayMargin;
        float f = i2 / 2.0f;
        if (i == 2) {
            this.mDropZoneView1.setContainerMargin(i2, i2, f, i2);
            DropZoneView dropZoneView = this.mDropZoneView2;
            int i3 = this.mDisplayMargin;
            dropZoneView.setContainerMargin(f, i3, i3, i3);
        } else if (i == 1) {
            this.mDropZoneView1.setContainerMargin(i2, i2, i2, f);
            DropZoneView dropZoneView2 = this.mDropZoneView2;
            int i4 = this.mDisplayMargin;
            dropZoneView2.setContainerMargin(i4, f, i4, i4);
        }
    }

    @SuppressLint({"WrongConstant"})
    public DragLayout(Context context, SplitScreenController splitScreenController, IconProvider iconProvider) {
        super(context);
        this.mSplitScreenController = splitScreenController;
        this.mIconProvider = iconProvider;
        this.mPolicy = new DragAndDropPolicy(context, ActivityTaskManager.getInstance(), splitScreenController, new DragAndDropPolicy.DefaultStarter(context));
        this.mStatusBarManager = (StatusBarManager) context.getSystemService(StatusBarManager.class);
        this.mDisplayMargin = context.getResources().getDimensionPixelSize(2131165699);
        this.mDividerSize = context.getResources().getDimensionPixelSize(2131167037);
        this.mDropZoneView1 = new DropZoneView(context, null);
        this.mDropZoneView2 = new DropZoneView(context, null);
        addView(this.mDropZoneView1, new LinearLayout.LayoutParams(-1, -1));
        addView(this.mDropZoneView2, new LinearLayout.LayoutParams(-1, -1));
        ((LinearLayout.LayoutParams) this.mDropZoneView1.getLayoutParams()).weight = 1.0f;
        ((LinearLayout.LayoutParams) this.mDropZoneView2.getLayoutParams()).weight = 1.0f;
        updateContainerMargins(getResources().getConfiguration().orientation);
    }

    @Override // android.view.View
    public final WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        this.mInsets = windowInsets.getInsets(WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
        recomputeDropTargets();
        int i = getResources().getConfiguration().orientation;
        if (i == 2) {
            this.mDropZoneView1.setBottomInset(this.mInsets.bottom);
            this.mDropZoneView2.setBottomInset(this.mInsets.bottom);
        } else if (i == 1) {
            this.mDropZoneView1.setBottomInset(0.0f);
            this.mDropZoneView2.setBottomInset(this.mInsets.bottom);
        }
        return super.onApplyWindowInsets(windowInsets);
    }

    public final void updateDropZoneSizes(Rect rect, Rect rect2) {
        float f;
        int i;
        int i2;
        int i3;
        boolean z = true;
        if (getResources().getConfiguration().orientation != 1) {
            z = false;
        }
        int i4 = this.mDividerSize / 2;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mDropZoneView1.getLayoutParams();
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.mDropZoneView2.getLayoutParams();
        int i5 = -1;
        if (z) {
            layoutParams.width = -1;
            layoutParams2.width = -1;
            if (rect != null) {
                i3 = rect.height() + i4;
            } else {
                i3 = -1;
            }
            layoutParams.height = i3;
            if (rect2 != null) {
                i5 = rect2.height() + i4;
            }
            layoutParams2.height = i5;
        } else {
            if (rect != null) {
                i = rect.width() + i4;
            } else {
                i = -1;
            }
            layoutParams.width = i;
            if (rect2 != null) {
                i2 = rect2.width() + i4;
            } else {
                i2 = -1;
            }
            layoutParams2.width = i2;
            layoutParams.height = -1;
            layoutParams2.height = -1;
        }
        float f2 = 0.0f;
        if (rect != null) {
            f = 0.0f;
        } else {
            f = 1.0f;
        }
        layoutParams.weight = f;
        if (rect2 == null) {
            f2 = 1.0f;
        }
        layoutParams2.weight = f2;
        this.mDropZoneView1.setLayoutParams(layoutParams);
        this.mDropZoneView2.setLayoutParams(layoutParams2);
    }
}
