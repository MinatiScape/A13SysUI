package com.android.wm.shell.pip;

import android.app.PictureInPictureParams;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Size;
import android.util.TypedValue;
import android.view.Gravity;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.pip.PipBoundsState;
import java.io.PrintWriter;
import java.util.Objects;
/* loaded from: classes.dex */
public class PipBoundsAlgorithm {
    public float mDefaultAspectRatio;
    public int mDefaultMinSize;
    public float mDefaultSizePercent;
    public int mDefaultStackGravity;
    public float mMaxAspectRatio;
    public float mMaxAspectRatioForMinSize;
    public float mMinAspectRatio;
    public float mMinAspectRatioForMinSize;
    public int mOverridableMinSize;
    public final PipBoundsState mPipBoundsState;
    public Point mScreenEdgeInsets;
    public final PipSnapAlgorithm mSnapAlgorithm;

    public static Rect getValidSourceHintRect(PictureInPictureParams pictureInPictureParams, Rect rect) {
        Rect rect2;
        if (pictureInPictureParams == null || !pictureInPictureParams.hasSourceBoundsHint()) {
            rect2 = null;
        } else {
            rect2 = pictureInPictureParams.getSourceRectHint();
        }
        if (rect2 == null || !rect.contains(rect2)) {
            return null;
        }
        return rect2;
    }

    public Rect getAdjustedDestinationBounds(Rect rect, float f) {
        return transformBoundsToAspectRatioIfValid(rect, f, true, false);
    }

    public final Size getMinimalSize(ActivityInfo activityInfo) {
        ActivityInfo.WindowLayout windowLayout;
        if (activityInfo == null || (windowLayout = activityInfo.windowLayout) == null || windowLayout.minWidth <= 0 || windowLayout.minHeight <= 0) {
            return null;
        }
        return new Size(Math.max(windowLayout.minWidth, this.mOverridableMinSize), Math.max(windowLayout.minHeight, this.mOverridableMinSize));
    }

    public final Rect getMovementBounds(Rect rect, boolean z) {
        int i;
        Rect rect2 = new Rect();
        getInsetBounds(rect2);
        if (z) {
            PipBoundsState pipBoundsState = this.mPipBoundsState;
            Objects.requireNonNull(pipBoundsState);
            if (pipBoundsState.mIsImeShowing) {
                PipBoundsState pipBoundsState2 = this.mPipBoundsState;
                Objects.requireNonNull(pipBoundsState2);
                i = pipBoundsState2.mImeHeight;
                getMovementBounds(rect, rect2, rect2, i);
                return rect2;
            }
        }
        i = 0;
        getMovementBounds(rect, rect2, rect2, i);
        return rect2;
    }

    public final void dump(PrintWriter printWriter, String str) {
        String m = SupportMenuInflater$$ExternalSyntheticOutline0.m(str, "  ");
        printWriter.println(str + "PipBoundsAlgorithm");
        printWriter.println(m + "mDefaultAspectRatio=" + this.mDefaultAspectRatio);
        printWriter.println(m + "mMinAspectRatio=" + this.mMinAspectRatio);
        printWriter.println(m + "mMaxAspectRatio=" + this.mMaxAspectRatio);
        printWriter.println(m + "mDefaultStackGravity=" + this.mDefaultStackGravity);
        printWriter.println(m + "mSnapAlgorithm" + this.mSnapAlgorithm);
    }

    public final Rect getDefaultBounds(float f, Size size) {
        Size size2;
        int i;
        Rect rect = new Rect();
        int i2 = (f > (-1.0f) ? 1 : (f == (-1.0f) ? 0 : -1));
        int i3 = 0;
        if (i2 == 0 || size == null) {
            Rect rect2 = new Rect();
            getInsetBounds(rect2);
            PipBoundsState pipBoundsState = this.mPipBoundsState;
            Objects.requireNonNull(pipBoundsState);
            DisplayLayout displayLayout = pipBoundsState.mDisplayLayout;
            PipBoundsState pipBoundsState2 = this.mPipBoundsState;
            Objects.requireNonNull(pipBoundsState2);
            Size size3 = pipBoundsState2.mOverrideMinSize;
            if (size3 != null) {
                size2 = adjustSizeToAspectRatio(size3, this.mDefaultAspectRatio);
            } else {
                Objects.requireNonNull(displayLayout);
                size2 = getSizeForAspectRatio(this.mDefaultAspectRatio, this.mDefaultMinSize, displayLayout.mWidth, displayLayout.mHeight);
            }
            if (i2 != 0) {
                rect.set(0, 0, size2.getWidth(), size2.getHeight());
                Rect movementBounds = getMovementBounds(rect, true);
                Objects.requireNonNull(this.mSnapAlgorithm);
                PipSnapAlgorithm.applySnapFraction(rect, movementBounds, f);
            } else {
                int i4 = this.mDefaultStackGravity;
                int width = size2.getWidth();
                int height = size2.getHeight();
                PipBoundsState pipBoundsState3 = this.mPipBoundsState;
                Objects.requireNonNull(pipBoundsState3);
                if (pipBoundsState3.mIsImeShowing) {
                    PipBoundsState pipBoundsState4 = this.mPipBoundsState;
                    Objects.requireNonNull(pipBoundsState4);
                    i = pipBoundsState4.mImeHeight;
                } else {
                    i = 0;
                }
                PipBoundsState pipBoundsState5 = this.mPipBoundsState;
                Objects.requireNonNull(pipBoundsState5);
                if (pipBoundsState5.mIsShelfShowing) {
                    PipBoundsState pipBoundsState6 = this.mPipBoundsState;
                    Objects.requireNonNull(pipBoundsState6);
                    i3 = pipBoundsState6.mShelfHeight;
                }
                Gravity.apply(i4, width, height, rect2, 0, Math.max(i, i3), rect);
            }
            return rect;
        }
        rect.set(0, 0, size.getWidth(), size.getHeight());
        Rect movementBounds2 = getMovementBounds(rect, true);
        Objects.requireNonNull(this.mSnapAlgorithm);
        PipSnapAlgorithm.applySnapFraction(rect, movementBounds2, f);
        return rect;
    }

    public Rect getEntryDestinationBounds() {
        Rect rect;
        boolean z;
        PipBoundsState pipBoundsState = this.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState);
        PipBoundsState.PipReentryState pipReentryState = pipBoundsState.mPipReentryState;
        if (pipReentryState != null) {
            rect = getDefaultBounds(pipReentryState.mSnapFraction, pipReentryState.mSize);
        } else {
            rect = getDefaultBounds(-1.0f, null);
        }
        if (pipReentryState == null || pipReentryState.mSize == null) {
            z = false;
        } else {
            z = true;
        }
        PipBoundsState pipBoundsState2 = this.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState2);
        return transformBoundsToAspectRatioIfValid(rect, pipBoundsState2.mAspectRatio, false, z);
    }

    public final void getInsetBounds(Rect rect) {
        PipBoundsState pipBoundsState = this.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState);
        DisplayLayout displayLayout = pipBoundsState.mDisplayLayout;
        PipBoundsState pipBoundsState2 = this.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState2);
        DisplayLayout displayLayout2 = pipBoundsState2.mDisplayLayout;
        Objects.requireNonNull(displayLayout2);
        Rect rect2 = displayLayout2.mStableInsets;
        int i = rect2.left;
        Point point = this.mScreenEdgeInsets;
        Objects.requireNonNull(displayLayout);
        Point point2 = this.mScreenEdgeInsets;
        rect.set(i + point.x, rect2.top + point.y, (displayLayout.mWidth - rect2.right) - point2.x, (displayLayout.mHeight - rect2.bottom) - point2.y);
    }

    public final void transformBoundsToAspectRatio(Rect rect, float f, boolean z, boolean z2) {
        Size size;
        int i;
        int i2;
        PipSnapAlgorithm pipSnapAlgorithm = this.mSnapAlgorithm;
        Rect movementBounds = getMovementBounds(rect, true);
        PipBoundsState pipBoundsState = this.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState);
        float snapFraction = pipSnapAlgorithm.getSnapFraction(rect, movementBounds, pipBoundsState.mStashedState);
        PipBoundsState pipBoundsState2 = this.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState2);
        Size size2 = pipBoundsState2.mOverrideMinSize;
        if (z || z2) {
            if (size2 == null) {
                i = this.mDefaultMinSize;
            } else {
                PipBoundsState pipBoundsState3 = this.mPipBoundsState;
                Objects.requireNonNull(pipBoundsState3);
                Size size3 = pipBoundsState3.mOverrideMinSize;
                if (size3 == null) {
                    i = 0;
                } else {
                    i = Math.min(size3.getWidth(), pipBoundsState3.mOverrideMinSize.getHeight());
                }
            }
            if (z) {
                PipBoundsState pipBoundsState4 = this.mPipBoundsState;
                Objects.requireNonNull(pipBoundsState4);
                i = pipBoundsState4.mMinEdgeSize;
            }
            Size size4 = new Size(rect.width(), rect.height());
            int max = (int) Math.max(i, Math.min(size4.getWidth(), size4.getHeight()));
            if (f <= 1.0f) {
                i2 = Math.round(max / f);
            } else {
                max = Math.round(max * f);
                i2 = max;
            }
            size = new Size(max, i2);
        } else if (size2 != null) {
            size = adjustSizeToAspectRatio(size2, f);
        } else {
            PipBoundsState pipBoundsState5 = this.mPipBoundsState;
            Objects.requireNonNull(pipBoundsState5);
            DisplayLayout displayLayout = pipBoundsState5.mDisplayLayout;
            Objects.requireNonNull(displayLayout);
            size = getSizeForAspectRatio(f, this.mDefaultMinSize, displayLayout.mWidth, displayLayout.mHeight);
        }
        int centerX = (int) (rect.centerX() - (size.getWidth() / 2.0f));
        int centerY = (int) (rect.centerY() - (size.getHeight() / 2.0f));
        rect.set(centerX, centerY, size.getWidth() + centerX, size.getHeight() + centerY);
        PipSnapAlgorithm pipSnapAlgorithm2 = this.mSnapAlgorithm;
        Rect movementBounds2 = getMovementBounds(rect, true);
        Objects.requireNonNull(pipSnapAlgorithm2);
        PipSnapAlgorithm.applySnapFraction(rect, movementBounds2, snapFraction);
    }

    public final Rect transformBoundsToAspectRatioIfValid(Rect rect, float f, boolean z, boolean z2) {
        boolean z3;
        Rect rect2 = new Rect(rect);
        if (Float.compare(this.mMinAspectRatio, f) > 0 || Float.compare(f, this.mMaxAspectRatio) > 0) {
            z3 = false;
        } else {
            z3 = true;
        }
        if (z3) {
            transformBoundsToAspectRatio(rect2, f, z, z2);
        }
        return rect2;
    }

    public PipBoundsAlgorithm(Context context, PipBoundsState pipBoundsState, PipSnapAlgorithm pipSnapAlgorithm) {
        this.mPipBoundsState = pipBoundsState;
        this.mSnapAlgorithm = pipSnapAlgorithm;
        reloadResources(context);
        float f = this.mDefaultAspectRatio;
        Objects.requireNonNull(pipBoundsState);
        pipBoundsState.mAspectRatio = f;
        pipBoundsState.mMinEdgeSize = this.mDefaultMinSize;
    }

    public static Size adjustSizeToAspectRatio(Size size, float f) {
        if (size.getWidth() / size.getHeight() > f) {
            return new Size(size.getWidth(), (int) (size.getWidth() / f));
        }
        return new Size((int) (size.getHeight() * f), size.getHeight());
    }

    public final Size getSizeForAspectRatio(float f, float f2, int i, int i2) {
        int i3;
        int i4;
        int max = (int) Math.max(f2, Math.min(i, i2) * this.mDefaultSizePercent);
        if (f > this.mMinAspectRatioForMinSize) {
            float f3 = this.mMaxAspectRatioForMinSize;
            if (f <= f3) {
                float f4 = max;
                float length = PointF.length(f3 * f4, f4);
                max = (int) Math.round(Math.sqrt((length * length) / ((f * f) + 1.0f)));
                i4 = Math.round(max * f);
                max = i4;
                i3 = max;
                return new Size(max, i3);
            }
        }
        if (f <= 1.0f) {
            i3 = Math.round(max / f);
            return new Size(max, i3);
        }
        i4 = Math.round(max * f);
        max = i4;
        i3 = max;
        return new Size(max, i3);
    }

    public void reloadResources(Context context) {
        Size size;
        Point point;
        Resources resources = context.getResources();
        this.mDefaultAspectRatio = resources.getFloat(17105089);
        this.mDefaultStackGravity = resources.getInteger(17694793);
        this.mDefaultMinSize = resources.getDimensionPixelSize(17105183);
        this.mOverridableMinSize = resources.getDimensionPixelSize(17105447);
        String string = resources.getString(17039923);
        if (!string.isEmpty()) {
            size = Size.parseSize(string);
        } else {
            size = null;
        }
        if (size == null) {
            point = new Point();
        } else {
            point = new Point((int) TypedValue.applyDimension(1, size.getWidth(), resources.getDisplayMetrics()), (int) TypedValue.applyDimension(1, size.getHeight(), resources.getDisplayMetrics()));
        }
        this.mScreenEdgeInsets = point;
        this.mMinAspectRatio = resources.getFloat(17105094);
        this.mMaxAspectRatio = resources.getFloat(17105093);
        this.mDefaultSizePercent = resources.getFloat(17105090);
        float f = resources.getFloat(17105088);
        this.mMaxAspectRatioForMinSize = f;
        this.mMinAspectRatioForMinSize = 1.0f / f;
    }

    public static void getMovementBounds(Rect rect, Rect rect2, Rect rect3, int i) {
        rect3.set(rect2);
        rect3.right = Math.max(rect2.left, rect2.right - rect.width());
        rect3.bottom = Math.max(rect2.top, rect2.bottom - rect.height()) - i;
    }
}
