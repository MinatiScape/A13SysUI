package com.android.wm.shell.pip;

import android.app.ActivityTaskManager;
import android.app.IActivityTaskManager;
import android.app.PictureInPictureParams;
import android.app.PictureInPictureUiState;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.RemoteException;
import android.util.Log;
import android.util.Size;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.function.TriConsumer;
import com.android.wm.shell.common.DisplayLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class PipBoundsState {
    public float mAspectRatio;
    public final Context mContext;
    public boolean mHasUserResizedPip;
    public int mImeHeight;
    public boolean mIsImeShowing;
    public boolean mIsShelfShowing;
    public ComponentName mLastPipComponentName;
    public int mMinEdgeSize;
    public Runnable mOnMinimalSizeChangeCallback;
    public TriConsumer<Boolean, Integer, Boolean> mOnShelfVisibilityChangeCallback;
    public Size mOverrideMinSize;
    public PipReentryState mPipReentryState;
    public int mShelfHeight;
    public int mStashOffset;
    public final Rect mBounds = new Rect();
    public final Rect mMovementBounds = new Rect();
    public final Rect mNormalBounds = new Rect();
    public final Rect mExpandedBounds = new Rect();
    public final Rect mNormalMovementBounds = new Rect();
    public final Rect mExpandedMovementBounds = new Rect();
    public final Point mMaxSize = new Point();
    public final Point mMinSize = new Point();
    public int mStashedState = 0;
    public int mDisplayId = 0;
    public final DisplayLayout mDisplayLayout = new DisplayLayout();
    public final MotionBoundsState mMotionBoundsState = new MotionBoundsState();
    public ArrayList mOnPipExclusionBoundsChangeCallbacks = new ArrayList();

    /* loaded from: classes.dex */
    public static class MotionBoundsState {
        public final Rect mBoundsInMotion = new Rect();
        public final Rect mAnimatingToBounds = new Rect();
    }

    @VisibleForTesting
    public void clearReentryState() {
        this.mPipReentryState = null;
    }

    /* loaded from: classes.dex */
    public static final class PipReentryState {
        public final Size mSize;
        public final float mSnapFraction;

        public PipReentryState(Size size, float f) {
            this.mSize = size;
            this.mSnapFraction = f;
        }
    }

    public final Rect getBounds() {
        return new Rect(this.mBounds);
    }

    public final Rect getDisplayBounds() {
        DisplayLayout displayLayout = this.mDisplayLayout;
        Objects.requireNonNull(displayLayout);
        int i = displayLayout.mWidth;
        DisplayLayout displayLayout2 = this.mDisplayLayout;
        Objects.requireNonNull(displayLayout2);
        return new Rect(0, 0, i, displayLayout2.mHeight);
    }

    public final boolean isStashed() {
        if (this.mStashedState != 0) {
            return true;
        }
        return false;
    }

    public final void setBounds(Rect rect) {
        this.mBounds.set(rect);
        Iterator it = this.mOnPipExclusionBoundsChangeCallbacks.iterator();
        while (it.hasNext()) {
            ((Consumer) it.next()).accept(rect);
        }
    }

    public final void setLastPipComponentName(ComponentName componentName) {
        boolean z = !Objects.equals(this.mLastPipComponentName, componentName);
        this.mLastPipComponentName = componentName;
        if (z) {
            clearReentryState();
            this.mHasUserResizedPip = false;
        }
    }

    public final void setShelfVisibility(boolean z, int i, boolean z2) {
        boolean z3;
        if (!z || i <= 0) {
            z3 = false;
        } else {
            z3 = true;
        }
        if (z3 != this.mIsShelfShowing || i != this.mShelfHeight) {
            this.mIsShelfShowing = z;
            this.mShelfHeight = i;
            TriConsumer<Boolean, Integer, Boolean> triConsumer = this.mOnShelfVisibilityChangeCallback;
            if (triConsumer != null) {
                triConsumer.accept(Boolean.valueOf(z), Integer.valueOf(this.mShelfHeight), Boolean.valueOf(z2));
            }
        }
    }

    public final void setStashed(int i) {
        boolean z;
        if (this.mStashedState != i) {
            this.mStashedState = i;
            try {
                IActivityTaskManager service = ActivityTaskManager.getService();
                if (i != 0) {
                    z = true;
                } else {
                    z = false;
                }
                service.onPictureInPictureStateChanged(new PictureInPictureUiState(z));
            } catch (RemoteException unused) {
                Log.e("PipBoundsState", "Unable to set alert PiP state change.");
            }
        }
    }

    public PipBoundsState(Context context) {
        this.mContext = context;
        this.mStashOffset = context.getResources().getDimensionPixelSize(2131166804);
    }

    public void setBoundsStateForEntry(ComponentName componentName, ActivityInfo activityInfo, PictureInPictureParams pictureInPictureParams, PipBoundsAlgorithm pipBoundsAlgorithm) {
        float f;
        boolean z;
        Runnable runnable;
        setLastPipComponentName(componentName);
        if (pictureInPictureParams != null) {
            Objects.requireNonNull(pipBoundsAlgorithm);
            if (pictureInPictureParams.hasSetAspectRatio()) {
                f = pictureInPictureParams.getAspectRatio();
                this.mAspectRatio = f;
                Size minimalSize = pipBoundsAlgorithm.getMinimalSize(activityInfo);
                z = !Objects.equals(minimalSize, this.mOverrideMinSize);
                this.mOverrideMinSize = minimalSize;
                if (z && (runnable = this.mOnMinimalSizeChangeCallback) != null) {
                    runnable.run();
                    return;
                }
            }
        }
        f = pipBoundsAlgorithm.mDefaultAspectRatio;
        this.mAspectRatio = f;
        Size minimalSize2 = pipBoundsAlgorithm.getMinimalSize(activityInfo);
        z = !Objects.equals(minimalSize2, this.mOverrideMinSize);
        this.mOverrideMinSize = minimalSize2;
        if (z) {
        }
    }
}
