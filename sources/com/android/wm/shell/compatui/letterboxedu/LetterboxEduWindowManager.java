package com.android.wm.shell.compatui.letterboxedu;

import android.animation.ObjectAnimator;
import android.app.TaskInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.SurfaceControlViewHost;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda8;
import com.android.systemui.ScreenDecorations$$ExternalSyntheticLambda4;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda5;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda6;
import com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda5;
import com.android.wm.shell.bubbles.BubbleFlyoutView$$ExternalSyntheticLambda2;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.compatui.CompatUIWindowManagerAbstract;
import com.android.wm.shell.compatui.letterboxedu.LetterboxEduAnimationController;
import java.util.Objects;
/* loaded from: classes.dex */
public final class LetterboxEduWindowManager extends CompatUIWindowManagerAbstract {
    @VisibleForTesting
    public static final String HAS_SEEN_LETTERBOX_EDUCATION_PREF_NAME = "has_seen_letterbox_education";
    public final LetterboxEduAnimationController mAnimationController;
    public boolean mEligibleForLetterboxEducation;
    @VisibleForTesting
    public LetterboxEduDialogLayout mLayout;
    public final Runnable mOnDismissCallback;
    public final SharedPreferences mSharedPreferences = this.mContext.getSharedPreferences(HAS_SEEN_LETTERBOX_EDUCATION_PREF_NAME, 0);
    public final int mDialogVerticalMargin = (int) this.mContext.getResources().getDimension(2131166118);

    @Override // com.android.wm.shell.compatui.CompatUIWindowManagerAbstract
    public final int getZOrder() {
        return Integer.MAX_VALUE;
    }

    @Override // com.android.wm.shell.compatui.CompatUIWindowManagerAbstract
    public final void removeLayout() {
        this.mLayout = null;
    }

    @Override // com.android.wm.shell.compatui.CompatUIWindowManagerAbstract
    public final void updateSurfacePosition() {
    }

    @Override // com.android.wm.shell.compatui.CompatUIWindowManagerAbstract
    public final View createLayout() {
        this.mSharedPreferences.edit().putBoolean(String.valueOf(this.mContext.getUserId()), true).apply();
        this.mLayout = (LetterboxEduDialogLayout) LayoutInflater.from(this.mContext).inflate(2131624237, (ViewGroup) null);
        updateDialogMargins();
        LetterboxEduAnimationController letterboxEduAnimationController = this.mAnimationController;
        LetterboxEduDialogLayout letterboxEduDialogLayout = this.mLayout;
        TaskView$$ExternalSyntheticLambda6 taskView$$ExternalSyntheticLambda6 = new TaskView$$ExternalSyntheticLambda6(this, 7);
        Objects.requireNonNull(letterboxEduAnimationController);
        letterboxEduAnimationController.cancelAnimation();
        Objects.requireNonNull(letterboxEduDialogLayout);
        View view = letterboxEduDialogLayout.mDialogContainer;
        Animation loadAnimation = letterboxEduAnimationController.loadAnimation(0);
        letterboxEduAnimationController.mDialogAnimation = loadAnimation;
        if (loadAnimation == null) {
            taskView$$ExternalSyntheticLambda6.run();
        } else {
            loadAnimation.setAnimationListener(new LetterboxEduAnimationController.AnonymousClass1(new ScreenDecorations$$ExternalSyntheticLambda4(view, 3), new BubbleController$$ExternalSyntheticLambda5(letterboxEduAnimationController, taskView$$ExternalSyntheticLambda6, 3)));
            Drawable drawable = letterboxEduDialogLayout.mBackgroundDim;
            long duration = letterboxEduAnimationController.mDialogAnimation.getDuration();
            ObjectAnimator ofInt = ObjectAnimator.ofInt(drawable, LetterboxEduAnimationController.DRAWABLE_ALPHA, 204);
            ofInt.setDuration(duration);
            letterboxEduAnimationController.mBackgroundDimAnimator = ofInt;
            ofInt.addListener(new LetterboxEduAnimationController.AnonymousClass2());
            letterboxEduAnimationController.mDialogAnimation.setStartOffset(500L);
            letterboxEduAnimationController.mBackgroundDimAnimator.setStartDelay(500L);
            view.startAnimation(letterboxEduAnimationController.mDialogAnimation);
            letterboxEduAnimationController.mBackgroundDimAnimator.start();
        }
        return this.mLayout;
    }

    @Override // com.android.wm.shell.compatui.CompatUIWindowManagerAbstract
    public final boolean eligibleToShowLayout() {
        if (!this.mEligibleForLetterboxEducation || isTaskbarEduShowing()) {
            return false;
        }
        if (this.mLayout != null || !this.mSharedPreferences.getBoolean(String.valueOf(this.mContext.getUserId()), false)) {
            return true;
        }
        return false;
    }

    @Override // com.android.wm.shell.compatui.CompatUIWindowManagerAbstract
    public final WindowManager.LayoutParams getWindowLayoutParams() {
        Rect bounds = this.mTaskConfig.windowConfiguration.getBounds();
        return getWindowLayoutParams(bounds.width(), bounds.height());
    }

    @VisibleForTesting
    public boolean isTaskbarEduShowing() {
        if (Settings.Secure.getInt(this.mContext.getContentResolver(), "launcher_taskbar_education_showing", 0) == 1) {
            return true;
        }
        return false;
    }

    @Override // com.android.wm.shell.compatui.CompatUIWindowManagerAbstract
    public final void onParentBoundsChanged() {
        if (this.mLayout != null) {
            WindowManager.LayoutParams windowLayoutParams = getWindowLayoutParams();
            this.mLayout.setLayoutParams(windowLayoutParams);
            updateDialogMargins();
            SurfaceControlViewHost surfaceControlViewHost = this.mViewHost;
            if (surfaceControlViewHost != null) {
                surfaceControlViewHost.relayout(windowLayoutParams);
            }
        }
    }

    @Override // com.android.wm.shell.compatui.CompatUIWindowManagerAbstract
    public final void release() {
        this.mAnimationController.cancelAnimation();
        super.release();
    }

    @Override // com.android.wm.shell.compatui.CompatUIWindowManagerAbstract
    public final boolean updateCompatInfo(TaskInfo taskInfo, ShellTaskOrganizer.TaskListener taskListener, boolean z) {
        this.mEligibleForLetterboxEducation = taskInfo.topActivityEligibleForLetterboxEducation;
        return super.updateCompatInfo(taskInfo, taskListener, z);
    }

    public final void updateDialogMargins() {
        LetterboxEduDialogLayout letterboxEduDialogLayout = this.mLayout;
        if (letterboxEduDialogLayout != null) {
            View view = letterboxEduDialogLayout.mDialogContainer;
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            Rect bounds = this.mTaskConfig.windowConfiguration.getBounds();
            Rect rect = new Rect(this.mStableBounds);
            rect.intersect(this.mTaskConfig.windowConfiguration.getBounds());
            int i = this.mDialogVerticalMargin;
            marginLayoutParams.topMargin = (rect.top - bounds.top) + i;
            marginLayoutParams.bottomMargin = (bounds.bottom - rect.bottom) + i;
            view.setLayoutParams(marginLayoutParams);
        }
    }

    public static void $r8$lambda$4SqjxbhVhyBXNciFEvygSHxAi3k(LetterboxEduWindowManager letterboxEduWindowManager) {
        Objects.requireNonNull(letterboxEduWindowManager);
        LetterboxEduDialogLayout letterboxEduDialogLayout = letterboxEduWindowManager.mLayout;
        if (letterboxEduDialogLayout != null) {
            letterboxEduDialogLayout.setDismissOnClickListener(new TaskView$$ExternalSyntheticLambda5(letterboxEduWindowManager, 6));
        }
    }

    public static void $r8$lambda$dlwl1DggfpDk0GTTamsPQUWfcQI(LetterboxEduWindowManager letterboxEduWindowManager) {
        Objects.requireNonNull(letterboxEduWindowManager);
        LetterboxEduDialogLayout letterboxEduDialogLayout = letterboxEduWindowManager.mLayout;
        if (letterboxEduDialogLayout != null) {
            letterboxEduDialogLayout.setDismissOnClickListener(null);
            final LetterboxEduAnimationController letterboxEduAnimationController = letterboxEduWindowManager.mAnimationController;
            LetterboxEduDialogLayout letterboxEduDialogLayout2 = letterboxEduWindowManager.mLayout;
            final KeyguardUpdateMonitor$$ExternalSyntheticLambda8 keyguardUpdateMonitor$$ExternalSyntheticLambda8 = new KeyguardUpdateMonitor$$ExternalSyntheticLambda8(letterboxEduWindowManager, 4);
            Objects.requireNonNull(letterboxEduAnimationController);
            letterboxEduAnimationController.cancelAnimation();
            Objects.requireNonNull(letterboxEduDialogLayout2);
            final View view = letterboxEduDialogLayout2.mDialogContainer;
            Animation loadAnimation = letterboxEduAnimationController.loadAnimation(1);
            letterboxEduAnimationController.mDialogAnimation = loadAnimation;
            if (loadAnimation == null) {
                keyguardUpdateMonitor$$ExternalSyntheticLambda8.run();
                return;
            }
            loadAnimation.setAnimationListener(new LetterboxEduAnimationController.AnonymousClass1(BubbleFlyoutView$$ExternalSyntheticLambda2.INSTANCE$1, new Runnable() { // from class: com.android.wm.shell.compatui.letterboxedu.LetterboxEduAnimationController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    LetterboxEduAnimationController letterboxEduAnimationController2 = LetterboxEduAnimationController.this;
                    View view2 = view;
                    Runnable runnable = keyguardUpdateMonitor$$ExternalSyntheticLambda8;
                    Objects.requireNonNull(letterboxEduAnimationController2);
                    view2.setAlpha(0.0f);
                    letterboxEduAnimationController2.mDialogAnimation = null;
                    runnable.run();
                }
            }));
            Drawable drawable = letterboxEduDialogLayout2.mBackgroundDim;
            long duration = letterboxEduAnimationController.mDialogAnimation.getDuration();
            ObjectAnimator ofInt = ObjectAnimator.ofInt(drawable, LetterboxEduAnimationController.DRAWABLE_ALPHA, 0);
            ofInt.setDuration(duration);
            letterboxEduAnimationController.mBackgroundDimAnimator = ofInt;
            ofInt.addListener(new LetterboxEduAnimationController.AnonymousClass2());
            view.startAnimation(letterboxEduAnimationController.mDialogAnimation);
            letterboxEduAnimationController.mBackgroundDimAnimator.start();
        }
    }

    @VisibleForTesting
    public LetterboxEduWindowManager(Context context, TaskInfo taskInfo, SyncTransactionQueue syncTransactionQueue, ShellTaskOrganizer.TaskListener taskListener, DisplayLayout displayLayout, Runnable runnable, LetterboxEduAnimationController letterboxEduAnimationController) {
        super(context, taskInfo, syncTransactionQueue, taskListener, displayLayout);
        this.mOnDismissCallback = runnable;
        this.mAnimationController = letterboxEduAnimationController;
        this.mEligibleForLetterboxEducation = taskInfo.topActivityEligibleForLetterboxEducation;
    }

    @Override // com.android.wm.shell.compatui.CompatUIWindowManagerAbstract
    public final View getLayout() {
        return this.mLayout;
    }
}
