package com.android.wm.shell.splitscreen;

import android.animation.RectEvaluator;
import android.content.Context;
import android.graphics.Insets;
import android.graphics.Rect;
import android.util.SparseArray;
import android.view.InsetsSource;
import android.view.InsetsState;
import android.view.SurfaceControl;
import com.android.internal.policy.ScreenDecorationsUtils;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.unfold.ShellUnfoldProgressProvider;
import com.android.wm.shell.unfold.UnfoldBackgroundController;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class StageTaskUnfoldController implements ShellUnfoldProgressProvider.UnfoldListener, DisplayInsetsController.OnInsetsChangedListener {
    public static final RectEvaluator RECT_EVALUATOR = new RectEvaluator(new Rect());
    public final UnfoldBackgroundController mBackgroundController;
    public boolean mBothStagesVisible;
    public final DisplayInsetsController mDisplayInsetsController;
    public final Executor mExecutor;
    public final int mExpandedTaskBarHeight;
    public InsetsSource mTaskbarInsetsSource;
    public final TransactionPool mTransactionPool;
    public final ShellUnfoldProgressProvider mUnfoldProgressProvider;
    public final float mWindowCornerRadiusPx;
    public final SparseArray<AnimationContext> mAnimationContextByTaskId = new SparseArray<>();
    public final Rect mStageBounds = new Rect();

    /* loaded from: classes.dex */
    public class AnimationContext {
        public final SurfaceControl mLeash;
        public final Rect mStartCropRect = new Rect();
        public final Rect mEndCropRect = new Rect();
        public final Rect mCurrentCropRect = new Rect();
        public int mSplitPosition = -1;
        public boolean mIsLandscape = false;

        public AnimationContext(SurfaceControl surfaceControl) {
            this.mLeash = surfaceControl;
            update$1();
        }

        public final void update$1() {
            boolean z;
            Insets insets;
            int i;
            int i2;
            int i3;
            this.mStartCropRect.set(StageTaskUnfoldController.this.mStageBounds);
            InsetsSource insetsSource = StageTaskUnfoldController.this.mTaskbarInsetsSource;
            int i4 = 0;
            if (insetsSource == null || insetsSource.getFrame().height() < StageTaskUnfoldController.this.mExpandedTaskBarHeight) {
                z = false;
            } else {
                z = true;
            }
            if (z) {
                Rect rect = this.mStartCropRect;
                rect.inset(StageTaskUnfoldController.this.mTaskbarInsetsSource.calculateVisibleInsets(rect));
            }
            this.mStartCropRect.offsetTo(0, 0);
            this.mEndCropRect.set(this.mStartCropRect);
            int max = (int) (Math.max(this.mEndCropRect.width(), this.mEndCropRect.height()) * 0.05f);
            if (this.mIsLandscape) {
                if (z) {
                    i2 = 0;
                } else {
                    i2 = max;
                }
                if (this.mSplitPosition == 0) {
                    i3 = 0;
                    i4 = max;
                } else {
                    i3 = max;
                }
                insets = Insets.of(i4, max, i3, i2);
            } else {
                if (this.mSplitPosition == 0) {
                    i = 0;
                    i4 = max;
                } else if (z) {
                    i = 0;
                } else {
                    i = max;
                }
                insets = Insets.of(max, i4, max, i);
            }
            this.mStartCropRect.inset(insets);
        }
    }

    @Override // com.android.wm.shell.common.DisplayInsetsController.OnInsetsChangedListener
    public final void insetsChanged(InsetsState insetsState) {
        this.mTaskbarInsetsSource = insetsState.getSource(21);
        int size = this.mAnimationContextByTaskId.size();
        while (true) {
            size--;
            if (size >= 0) {
                this.mAnimationContextByTaskId.valueAt(size).update$1();
            } else {
                return;
            }
        }
    }

    public final void onLayoutChanged(Rect rect, int i, boolean z) {
        this.mStageBounds.set(rect);
        int size = this.mAnimationContextByTaskId.size();
        while (true) {
            size--;
            if (size >= 0) {
                AnimationContext valueAt = this.mAnimationContextByTaskId.valueAt(size);
                Objects.requireNonNull(valueAt);
                valueAt.mSplitPosition = i;
                valueAt.mIsLandscape = z;
                valueAt.update$1();
            } else {
                return;
            }
        }
    }

    @Override // com.android.wm.shell.unfold.ShellUnfoldProgressProvider.UnfoldListener
    public final void onStateChangeProgress(float f) {
        if (this.mAnimationContextByTaskId.size() != 0 && this.mBothStagesVisible) {
            SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
            this.mBackgroundController.ensureBackground(acquire);
            int size = this.mAnimationContextByTaskId.size();
            while (true) {
                size--;
                if (size >= 0) {
                    AnimationContext valueAt = this.mAnimationContextByTaskId.valueAt(size);
                    valueAt.mCurrentCropRect.set((Rect) RECT_EVALUATOR.evaluate(f, valueAt.mStartCropRect, valueAt.mEndCropRect));
                    acquire.setWindowCrop(valueAt.mLeash, valueAt.mCurrentCropRect).setCornerRadius(valueAt.mLeash, this.mWindowCornerRadiusPx);
                } else {
                    acquire.apply();
                    this.mTransactionPool.release(acquire);
                    return;
                }
            }
        }
    }

    public final void resetTransformations() {
        SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
        int size = this.mAnimationContextByTaskId.size();
        while (true) {
            size--;
            if (size >= 0) {
                AnimationContext valueAt = this.mAnimationContextByTaskId.valueAt(size);
                acquire.setWindowCrop(valueAt.mLeash, null).setCornerRadius(valueAt.mLeash, 0.0f);
            } else {
                this.mBackgroundController.removeBackground(acquire);
                acquire.apply();
                this.mTransactionPool.release(acquire);
                return;
            }
        }
    }

    public StageTaskUnfoldController(Context context, TransactionPool transactionPool, ShellUnfoldProgressProvider shellUnfoldProgressProvider, DisplayInsetsController displayInsetsController, UnfoldBackgroundController unfoldBackgroundController, ShellExecutor shellExecutor) {
        this.mUnfoldProgressProvider = shellUnfoldProgressProvider;
        this.mTransactionPool = transactionPool;
        this.mExecutor = shellExecutor;
        this.mBackgroundController = unfoldBackgroundController;
        this.mDisplayInsetsController = displayInsetsController;
        this.mWindowCornerRadiusPx = ScreenDecorationsUtils.getWindowCornerRadius(context);
        this.mExpandedTaskBarHeight = context.getResources().getDimensionPixelSize(17105563);
    }

    @Override // com.android.wm.shell.unfold.ShellUnfoldProgressProvider.UnfoldListener
    public final void onStateChangeFinished() {
        resetTransformations();
    }
}
