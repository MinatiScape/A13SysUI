package com.android.systemui.assist.ui;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;
import android.util.PathParser;
import com.android.systemui.R$id;
import com.android.systemui.assist.ui.CornerPathRenderer;
/* loaded from: classes.dex */
public final class PathSpecCornerPathRenderer extends CornerPathRenderer {
    public final int mBottomCornerRadius;
    public final int mHeight;
    public final float mPathScale;
    public final Path mRoundedPath;
    public final int mTopCornerRadius;
    public final int mWidth;
    public final Path mPath = new Path();
    public final Matrix mMatrix = new Matrix();

    @Override // com.android.systemui.assist.ui.CornerPathRenderer
    public final Path getCornerPath(CornerPathRenderer.Corner corner) {
        int i;
        int i2;
        int i3;
        if (this.mRoundedPath.isEmpty()) {
            return this.mRoundedPath;
        }
        int ordinal = corner.ordinal();
        int i4 = 0;
        if (ordinal == 1) {
            i = this.mBottomCornerRadius;
            i4 = 180;
            i3 = this.mWidth;
            i2 = this.mHeight;
        } else if (ordinal == 2) {
            i = this.mTopCornerRadius;
            i4 = 90;
            i3 = this.mWidth;
            i2 = 0;
        } else if (ordinal != 3) {
            i = this.mBottomCornerRadius;
            i2 = this.mHeight;
            i4 = 270;
            i3 = 0;
        } else {
            i = this.mTopCornerRadius;
            i3 = 0;
            i2 = 0;
        }
        this.mPath.reset();
        this.mMatrix.reset();
        this.mPath.addPath(this.mRoundedPath);
        Matrix matrix = this.mMatrix;
        float f = i;
        float f2 = this.mPathScale;
        matrix.preScale(f / f2, f / f2);
        this.mMatrix.postRotate(i4);
        this.mMatrix.postTranslate(i3, i2);
        this.mPath.transform(this.mMatrix);
        return this.mPath;
    }

    public PathSpecCornerPathRenderer(Context context) {
        this.mWidth = R$id.getWidth(context);
        this.mHeight = R$id.getHeight(context);
        this.mBottomCornerRadius = R$id.getCornerRadiusBottom(context);
        this.mTopCornerRadius = R$id.getCornerRadiusTop(context);
        Path createPathFromPathData = PathParser.createPathFromPathData(context.getResources().getString(2131952145));
        if (createPathFromPathData == null) {
            Log.e("PathSpecCornerPathRenderer", "No rounded corner path found!");
            this.mRoundedPath = new Path();
        } else {
            this.mRoundedPath = createPathFromPathData;
        }
        RectF rectF = new RectF();
        this.mRoundedPath.computeBounds(rectF, true);
        this.mPathScale = Math.min(Math.abs(rectF.right - rectF.left), Math.abs(rectF.top - rectF.bottom));
    }
}
