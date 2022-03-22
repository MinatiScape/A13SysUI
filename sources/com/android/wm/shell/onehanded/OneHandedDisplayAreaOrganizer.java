package com.android.wm.shell.onehanded;

import android.content.Context;
import android.graphics.Rect;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.SurfaceControl;
import android.window.DisplayAreaAppearedInfo;
import android.window.DisplayAreaInfo;
import android.window.DisplayAreaOrganizer;
import android.window.WindowContainerToken;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.onehanded.OneHandedAnimationController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
/* loaded from: classes.dex */
public final class OneHandedDisplayAreaOrganizer extends DisplayAreaOrganizer {
    public OneHandedAnimationController mAnimationController;
    public final Context mContext;
    public int mEnterExitAnimationDurationMs;
    public boolean mIsReady;
    public final InteractionJankMonitor mJankMonitor;
    public OneHandedTutorialHandler mTutorialHandler;
    public DisplayLayout mDisplayLayout = new DisplayLayout();
    public final Rect mLastVisualDisplayBounds = new Rect();
    public final Rect mDefaultDisplayBounds = new Rect();
    public float mLastVisualOffset = 0.0f;
    public ArrayMap<WindowContainerToken, SurfaceControl> mDisplayAreaTokenMap = new ArrayMap<>();
    public ArrayList mTransitionCallbacks = new ArrayList();
    public OneHandedAnimationCallback mOneHandedAnimationCallback = new OneHandedAnimationCallback() { // from class: com.android.wm.shell.onehanded.OneHandedDisplayAreaOrganizer.1
        @Override // com.android.wm.shell.onehanded.OneHandedAnimationCallback
        public final void onOneHandedAnimationCancel(OneHandedAnimationController.OneHandedTransitionAnimator oneHandedTransitionAnimator) {
            int i;
            OneHandedAnimationController oneHandedAnimationController = OneHandedDisplayAreaOrganizer.this.mAnimationController;
            WindowContainerToken windowContainerToken = oneHandedTransitionAnimator.mToken;
            Objects.requireNonNull(oneHandedAnimationController);
            OneHandedAnimationController.OneHandedTransitionAnimator remove = oneHandedAnimationController.mAnimatorMap.remove(windowContainerToken);
            if (remove != null && remove.isRunning()) {
                remove.cancel();
            }
            boolean z = true;
            if (oneHandedTransitionAnimator.mTransitionDirection != 1) {
                z = false;
            }
            OneHandedAnimationController oneHandedAnimationController2 = OneHandedDisplayAreaOrganizer.this.mAnimationController;
            Objects.requireNonNull(oneHandedAnimationController2);
            if (oneHandedAnimationController2.mAnimatorMap.isEmpty()) {
                OneHandedDisplayAreaOrganizer oneHandedDisplayAreaOrganizer = OneHandedDisplayAreaOrganizer.this;
                if (z) {
                    i = 42;
                } else {
                    i = 43;
                }
                Objects.requireNonNull(oneHandedDisplayAreaOrganizer);
                oneHandedDisplayAreaOrganizer.mJankMonitor.cancel(i);
                OneHandedDisplayAreaOrganizer.this.finishOffset((int) (oneHandedTransitionAnimator.mEndValue - oneHandedTransitionAnimator.mStartValue), oneHandedTransitionAnimator.mTransitionDirection);
            }
        }

        @Override // com.android.wm.shell.onehanded.OneHandedAnimationCallback
        public final void onOneHandedAnimationEnd(OneHandedAnimationController.OneHandedTransitionAnimator oneHandedTransitionAnimator) {
            int i;
            OneHandedAnimationController oneHandedAnimationController = OneHandedDisplayAreaOrganizer.this.mAnimationController;
            WindowContainerToken windowContainerToken = oneHandedTransitionAnimator.mToken;
            Objects.requireNonNull(oneHandedAnimationController);
            OneHandedAnimationController.OneHandedTransitionAnimator remove = oneHandedAnimationController.mAnimatorMap.remove(windowContainerToken);
            if (remove != null && remove.isRunning()) {
                remove.cancel();
            }
            boolean z = true;
            if (oneHandedTransitionAnimator.mTransitionDirection != 1) {
                z = false;
            }
            OneHandedAnimationController oneHandedAnimationController2 = OneHandedDisplayAreaOrganizer.this.mAnimationController;
            Objects.requireNonNull(oneHandedAnimationController2);
            if (oneHandedAnimationController2.mAnimatorMap.isEmpty()) {
                OneHandedDisplayAreaOrganizer oneHandedDisplayAreaOrganizer = OneHandedDisplayAreaOrganizer.this;
                if (z) {
                    i = 42;
                } else {
                    i = 43;
                }
                Objects.requireNonNull(oneHandedDisplayAreaOrganizer);
                oneHandedDisplayAreaOrganizer.mJankMonitor.end(i);
                OneHandedDisplayAreaOrganizer.this.finishOffset((int) (oneHandedTransitionAnimator.mEndValue - oneHandedTransitionAnimator.mStartValue), oneHandedTransitionAnimator.mTransitionDirection);
            }
        }

        @Override // com.android.wm.shell.onehanded.OneHandedAnimationCallback
        public final void onOneHandedAnimationStart(OneHandedAnimationController.OneHandedTransitionAnimator oneHandedTransitionAnimator) {
            if (!OneHandedDisplayAreaOrganizer.this.mTransitionCallbacks.isEmpty()) {
                for (int size = OneHandedDisplayAreaOrganizer.this.mTransitionCallbacks.size() - 1; size >= 0; size--) {
                    ((OneHandedTransitionCallback) OneHandedDisplayAreaOrganizer.this.mTransitionCallbacks.get(size)).onStartTransition();
                }
            }
        }
    };
    public OneHandedDisplayAreaOrganizer$$ExternalSyntheticLambda0 mSurfaceControlTransactionFactory = OneHandedDisplayAreaOrganizer$$ExternalSyntheticLambda0.INSTANCE;

    public void finishOffset(int i, int i2) {
        if (i2 == 2) {
            resetWindowsOffset();
        }
        float f = i2 == 1 ? i : 0.0f;
        this.mLastVisualOffset = f;
        this.mLastVisualDisplayBounds.offsetTo(0, Math.round(f));
        for (int size = this.mTransitionCallbacks.size() - 1; size >= 0; size--) {
            OneHandedTransitionCallback oneHandedTransitionCallback = (OneHandedTransitionCallback) this.mTransitionCallbacks.get(size);
            if (i2 == 1) {
                oneHandedTransitionCallback.onStartFinished(this.mLastVisualDisplayBounds);
            } else {
                oneHandedTransitionCallback.onStopFinished(this.mLastVisualDisplayBounds);
            }
        }
    }

    public final void onDisplayAreaAppeared(DisplayAreaInfo displayAreaInfo, SurfaceControl surfaceControl) {
        this.mDisplayAreaTokenMap.put(displayAreaInfo.token, surfaceControl);
    }

    public final void onDisplayAreaVanished(DisplayAreaInfo displayAreaInfo) {
        this.mDisplayAreaTokenMap.remove(displayAreaInfo.token);
    }

    public void resetWindowsOffset() {
        Objects.requireNonNull(this.mSurfaceControlTransactionFactory);
        final SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
        this.mDisplayAreaTokenMap.forEach(new BiConsumer() { // from class: com.android.wm.shell.onehanded.OneHandedDisplayAreaOrganizer$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                OneHandedDisplayAreaOrganizer oneHandedDisplayAreaOrganizer = OneHandedDisplayAreaOrganizer.this;
                SurfaceControl.Transaction transaction2 = transaction;
                SurfaceControl surfaceControl = (SurfaceControl) obj2;
                Objects.requireNonNull(oneHandedDisplayAreaOrganizer);
                OneHandedAnimationController oneHandedAnimationController = oneHandedDisplayAreaOrganizer.mAnimationController;
                Objects.requireNonNull(oneHandedAnimationController);
                OneHandedAnimationController.OneHandedTransitionAnimator remove = oneHandedAnimationController.mAnimatorMap.remove((WindowContainerToken) obj);
                if (remove != null && remove.isRunning()) {
                    remove.cancel();
                }
                transaction2.setPosition(surfaceControl, 0.0f, 0.0f).setWindowCrop(surfaceControl, -1, -1).setCornerRadius(surfaceControl, -1.0f);
            }
        });
        transaction.apply();
        this.mLastVisualOffset = 0.0f;
        this.mLastVisualDisplayBounds.offsetTo(0, 0);
    }

    public final void scheduleOffset(final int i) {
        final int i2;
        final float f = this.mLastVisualOffset;
        if (i > 0) {
            i2 = 1;
        } else {
            i2 = 2;
        }
        if (i2 == 1) {
            beginCUJTracing(42, "enterOneHanded");
        } else {
            beginCUJTracing(43, "stopOneHanded");
        }
        this.mDisplayAreaTokenMap.forEach(new BiConsumer() { // from class: com.android.wm.shell.onehanded.OneHandedDisplayAreaOrganizer$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                OneHandedDisplayAreaOrganizer oneHandedDisplayAreaOrganizer = OneHandedDisplayAreaOrganizer.this;
                float f2 = f;
                int i3 = i;
                int i4 = i2;
                WindowContainerToken windowContainerToken = (WindowContainerToken) obj;
                SurfaceControl surfaceControl = (SurfaceControl) obj2;
                Objects.requireNonNull(oneHandedDisplayAreaOrganizer);
                float f3 = i3;
                int i5 = oneHandedDisplayAreaOrganizer.mEnterExitAnimationDurationMs;
                OneHandedAnimationController oneHandedAnimationController = oneHandedDisplayAreaOrganizer.mAnimationController;
                Rect rect = oneHandedDisplayAreaOrganizer.mLastVisualDisplayBounds;
                Objects.requireNonNull(oneHandedAnimationController);
                OneHandedAnimationController.OneHandedTransitionAnimator oneHandedTransitionAnimator = oneHandedAnimationController.mAnimatorMap.get(windowContainerToken);
                if (oneHandedTransitionAnimator == null) {
                    HashMap<WindowContainerToken, OneHandedAnimationController.OneHandedTransitionAnimator> hashMap = oneHandedAnimationController.mAnimatorMap;
                    OneHandedAnimationController.OneHandedTransitionAnimator ofYOffset = OneHandedAnimationController.OneHandedTransitionAnimator.ofYOffset(windowContainerToken, surfaceControl, f2, f3, rect);
                    oneHandedAnimationController.setupOneHandedTransitionAnimator(ofYOffset);
                    hashMap.put(windowContainerToken, ofYOffset);
                } else if (oneHandedTransitionAnimator.isRunning()) {
                    oneHandedTransitionAnimator.mEndValue = f3;
                } else {
                    oneHandedTransitionAnimator.cancel();
                    HashMap<WindowContainerToken, OneHandedAnimationController.OneHandedTransitionAnimator> hashMap2 = oneHandedAnimationController.mAnimatorMap;
                    OneHandedAnimationController.OneHandedTransitionAnimator ofYOffset2 = OneHandedAnimationController.OneHandedTransitionAnimator.ofYOffset(windowContainerToken, surfaceControl, f2, f3, rect);
                    oneHandedAnimationController.setupOneHandedTransitionAnimator(ofYOffset2);
                    hashMap2.put(windowContainerToken, ofYOffset2);
                }
                OneHandedAnimationController.OneHandedTransitionAnimator oneHandedTransitionAnimator2 = oneHandedAnimationController.mAnimatorMap.get(windowContainerToken);
                if (oneHandedTransitionAnimator2 != null) {
                    oneHandedTransitionAnimator2.mTransitionDirection = i4;
                    OneHandedAnimationCallback oneHandedAnimationCallback = oneHandedDisplayAreaOrganizer.mOneHandedAnimationCallback;
                    if (oneHandedAnimationCallback != null) {
                        oneHandedTransitionAnimator2.mOneHandedAnimationCallbacks.add(oneHandedAnimationCallback);
                    }
                    OneHandedTutorialHandler oneHandedTutorialHandler = oneHandedDisplayAreaOrganizer.mTutorialHandler;
                    if (oneHandedTutorialHandler != null) {
                        oneHandedTransitionAnimator2.mOneHandedAnimationCallbacks.add(oneHandedTutorialHandler);
                    }
                    oneHandedTransitionAnimator2.setDuration(i5).start();
                }
            }
        });
        this.mLastVisualOffset = i;
    }

    public void setDisplayLayout(DisplayLayout displayLayout) {
        this.mDisplayLayout.set(displayLayout);
        updateDisplayBounds();
    }

    public void updateDisplayBounds() {
        Rect rect = this.mDefaultDisplayBounds;
        DisplayLayout displayLayout = this.mDisplayLayout;
        Objects.requireNonNull(displayLayout);
        int i = displayLayout.mWidth;
        DisplayLayout displayLayout2 = this.mDisplayLayout;
        Objects.requireNonNull(displayLayout2);
        rect.set(0, 0, i, displayLayout2.mHeight);
        this.mLastVisualDisplayBounds.set(this.mDefaultDisplayBounds);
    }

    public OneHandedDisplayAreaOrganizer(Context context, DisplayLayout displayLayout, OneHandedAnimationController oneHandedAnimationController, OneHandedTutorialHandler oneHandedTutorialHandler, InteractionJankMonitor interactionJankMonitor, ShellExecutor shellExecutor) {
        super(shellExecutor);
        this.mContext = context;
        setDisplayLayout(displayLayout);
        this.mAnimationController = oneHandedAnimationController;
        this.mJankMonitor = interactionJankMonitor;
        this.mEnterExitAnimationDurationMs = SystemProperties.getInt("persist.debug.one_handed_translate_animation_duration", context.getResources().getInteger(2131492898));
        this.mTutorialHandler = oneHandedTutorialHandler;
    }

    public final void beginCUJTracing(int i, String str) {
        InteractionJankMonitor.Configuration.Builder withSurface = InteractionJankMonitor.Configuration.Builder.withSurface(i, this.mContext, getDisplayAreaTokenMap().entrySet().iterator().next().getValue());
        if (!TextUtils.isEmpty(str)) {
            withSurface.setTag(str);
        }
        this.mJankMonitor.begin(withSurface);
    }

    public final List<DisplayAreaAppearedInfo> registerOrganizer(int i) {
        List<DisplayAreaAppearedInfo> registerOrganizer = OneHandedDisplayAreaOrganizer.super.registerOrganizer(i);
        for (int i2 = 0; i2 < registerOrganizer.size(); i2++) {
            DisplayAreaAppearedInfo displayAreaAppearedInfo = registerOrganizer.get(i2);
            onDisplayAreaAppeared(displayAreaAppearedInfo.getDisplayAreaInfo(), displayAreaAppearedInfo.getLeash());
        }
        this.mIsReady = true;
        updateDisplayBounds();
        return registerOrganizer;
    }

    public final void unregisterOrganizer() {
        OneHandedDisplayAreaOrganizer.super.unregisterOrganizer();
        this.mIsReady = false;
        resetWindowsOffset();
    }

    public ArrayMap<WindowContainerToken, SurfaceControl> getDisplayAreaTokenMap() {
        return this.mDisplayAreaTokenMap;
    }

    public Rect getLastDisplayBounds() {
        return this.mLastVisualDisplayBounds;
    }
}
