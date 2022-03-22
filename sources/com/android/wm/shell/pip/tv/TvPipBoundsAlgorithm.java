package com.android.wm.shell.pip.tv;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.Gravity;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipSnapAlgorithm;
import java.util.Objects;
/* loaded from: classes.dex */
public final class TvPipBoundsAlgorithm extends PipBoundsAlgorithm {
    public int mFixedExpandedHeightInPx;
    public int mFixedExpandedWidthInPx;
    public final TvPipBoundsState mTvPipBoundsState;

    @Override // com.android.wm.shell.pip.PipBoundsAlgorithm
    public final Rect getAdjustedDestinationBounds(Rect rect, float f) {
        TvPipBoundsState tvPipBoundsState = this.mTvPipBoundsState;
        Objects.requireNonNull(tvPipBoundsState);
        return getTvPipBounds(tvPipBoundsState.mIsTvPipExpanded);
    }

    @Override // com.android.wm.shell.pip.PipBoundsAlgorithm
    public final Rect getEntryDestinationBounds() {
        TvPipBoundsState tvPipBoundsState = this.mTvPipBoundsState;
        Objects.requireNonNull(tvPipBoundsState);
        if (tvPipBoundsState.mTvExpandedAspectRatio != 0.0f) {
            TvPipBoundsState tvPipBoundsState2 = this.mTvPipBoundsState;
            Objects.requireNonNull(tvPipBoundsState2);
            if (!tvPipBoundsState2.mTvPipManuallyCollapsed) {
                updatePositionOnExpandToggled(0, true);
            }
        }
        return getTvPipBounds(true);
    }

    public final Rect getTvNormalBounds() {
        Rect defaultBounds = getDefaultBounds(-1.0f, null);
        PipBoundsState pipBoundsState = this.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState);
        Rect transformBoundsToAspectRatioIfValid = transformBoundsToAspectRatioIfValid(defaultBounds, pipBoundsState.mAspectRatio, false, false);
        Rect rect = new Rect();
        getInsetBounds(rect);
        TvPipBoundsState tvPipBoundsState = this.mTvPipBoundsState;
        Objects.requireNonNull(tvPipBoundsState);
        if (tvPipBoundsState.mIsImeShowing) {
            int i = rect.bottom;
            TvPipBoundsState tvPipBoundsState2 = this.mTvPipBoundsState;
            Objects.requireNonNull(tvPipBoundsState2);
            rect.bottom = i - tvPipBoundsState2.mImeHeight;
        }
        Rect rect2 = new Rect();
        TvPipBoundsState tvPipBoundsState3 = this.mTvPipBoundsState;
        Objects.requireNonNull(tvPipBoundsState3);
        Gravity.apply(tvPipBoundsState3.mTvPipGravity, transformBoundsToAspectRatioIfValid.width(), transformBoundsToAspectRatioIfValid.height(), rect, rect2);
        TvPipBoundsState tvPipBoundsState4 = this.mTvPipBoundsState;
        Objects.requireNonNull(tvPipBoundsState4);
        tvPipBoundsState4.mIsTvPipExpanded = false;
        return rect2;
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x00a2  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00a7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final android.graphics.Rect getTvPipBounds(boolean r7) {
        /*
            Method dump skipped, instructions count: 218
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.pip.tv.TvPipBoundsAlgorithm.getTvPipBounds(boolean):android.graphics.Rect");
    }

    public final int updatePositionOnExpandToggled(int i, boolean z) {
        TvPipBoundsState tvPipBoundsState = this.mTvPipBoundsState;
        Objects.requireNonNull(tvPipBoundsState);
        int i2 = 0;
        if (!tvPipBoundsState.mIsTvExpandedPipEnabled) {
            return 0;
        }
        if (z) {
            TvPipBoundsState tvPipBoundsState2 = this.mTvPipBoundsState;
            Objects.requireNonNull(tvPipBoundsState2);
            if (tvPipBoundsState2.mTvFixedPipOrientation == 0) {
                TvPipBoundsState tvPipBoundsState3 = this.mTvPipBoundsState;
                Objects.requireNonNull(tvPipBoundsState3);
                float f = tvPipBoundsState3.mTvExpandedAspectRatio;
                if (f == 0.0f) {
                    return 0;
                }
                if (f < 1.0f) {
                    TvPipBoundsState tvPipBoundsState4 = this.mTvPipBoundsState;
                    Objects.requireNonNull(tvPipBoundsState4);
                    tvPipBoundsState4.mTvFixedPipOrientation = 1;
                } else {
                    TvPipBoundsState tvPipBoundsState5 = this.mTvPipBoundsState;
                    Objects.requireNonNull(tvPipBoundsState5);
                    tvPipBoundsState5.mTvFixedPipOrientation = 2;
                }
            }
        }
        TvPipBoundsState tvPipBoundsState6 = this.mTvPipBoundsState;
        Objects.requireNonNull(tvPipBoundsState6);
        int i3 = tvPipBoundsState6.mTvPipGravity;
        if (z) {
            TvPipBoundsState tvPipBoundsState7 = this.mTvPipBoundsState;
            Objects.requireNonNull(tvPipBoundsState7);
            i2 = tvPipBoundsState7.mTvPipGravity;
            TvPipBoundsState tvPipBoundsState8 = this.mTvPipBoundsState;
            Objects.requireNonNull(tvPipBoundsState8);
            if (tvPipBoundsState8.mTvFixedPipOrientation == 2) {
                i = (i3 & 112) | 1;
            } else {
                i = (i3 & 7) | 16;
            }
        } else if (i == 0) {
            TvPipBoundsState tvPipBoundsState9 = this.mTvPipBoundsState;
            Objects.requireNonNull(tvPipBoundsState9);
            if (tvPipBoundsState9.mTvFixedPipOrientation == 2) {
                i = (i3 & 112) | 5;
            } else {
                i = (i3 & 7) | 80;
            }
        }
        TvPipBoundsState tvPipBoundsState10 = this.mTvPipBoundsState;
        Objects.requireNonNull(tvPipBoundsState10);
        tvPipBoundsState10.mTvPipGravity = i;
        return i2;
    }

    public TvPipBoundsAlgorithm(Context context, TvPipBoundsState tvPipBoundsState, PipSnapAlgorithm pipSnapAlgorithm) {
        super(context, tvPipBoundsState, pipSnapAlgorithm);
        this.mTvPipBoundsState = tvPipBoundsState;
    }

    @Override // com.android.wm.shell.pip.PipBoundsAlgorithm
    public final void reloadResources(Context context) {
        super.reloadResources(context);
        Resources resources = context.getResources();
        this.mFixedExpandedHeightInPx = resources.getDimensionPixelSize(17105091);
        this.mFixedExpandedWidthInPx = resources.getDimensionPixelSize(17105092);
    }
}
