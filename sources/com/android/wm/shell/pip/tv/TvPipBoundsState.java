package com.android.wm.shell.pip.tv;

import android.app.PictureInPictureParams;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.Size;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
/* loaded from: classes.dex */
public final class TvPipBoundsState extends PipBoundsState {
    public boolean mIsTvExpandedPipEnabled;
    public boolean mIsTvPipExpanded;
    public float mTvExpandedAspectRatio;
    public Size mTvExpandedSize;
    public int mTvFixedPipOrientation;
    public int mTvPipGravity;
    public boolean mTvPipManuallyCollapsed;

    public final void setTvExpandedAspectRatio(float f, boolean z) {
        int i;
        if (z || (i = this.mTvFixedPipOrientation) == 0 || f == 0.0f) {
            this.mTvExpandedAspectRatio = f;
            this.mTvFixedPipOrientation = 0;
            this.mTvPipGravity = 85;
        } else if ((f > 1.0f && i == 2) || (f <= 1.0f && i == 1)) {
            this.mTvExpandedAspectRatio = f;
        }
    }

    public TvPipBoundsState(Context context) {
        super(context);
        this.mIsTvExpandedPipEnabled = context.getPackageManager().hasSystemFeature("android.software.expanded_picture_in_picture");
    }

    @Override // com.android.wm.shell.pip.PipBoundsState
    public final void setBoundsStateForEntry(ComponentName componentName, ActivityInfo activityInfo, PictureInPictureParams pictureInPictureParams, PipBoundsAlgorithm pipBoundsAlgorithm) {
        super.setBoundsStateForEntry(componentName, activityInfo, pictureInPictureParams, pipBoundsAlgorithm);
        setTvExpandedAspectRatio(pictureInPictureParams.getExpandedAspectRatio(), true);
    }
}
