package com.android.launcher3.icons;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.ColorDrawable;
import androidx.core.graphics.ColorUtils;
/* loaded from: classes.dex */
public final class PlaceHolderIconDrawable extends FastBitmapDrawable {
    public final Path mProgressPath;

    public PlaceHolderIconDrawable(BitmapInfo bitmapInfo, Context context) {
        super(bitmapInfo.icon, bitmapInfo.color);
        int i = GraphicsUtils.$r8$clinit;
        AdaptiveIconDrawable adaptiveIconDrawable = new AdaptiveIconDrawable(new ColorDrawable(-16777216), new ColorDrawable(-16777216));
        adaptiveIconDrawable.setBounds(0, 0, 100, 100);
        this.mProgressPath = new Path(adaptiveIconDrawable.getIconMask());
        this.mPaint.setColor(ColorUtils.compositeColors(GraphicsUtils.getAttrColor(context, 2130969384), bitmapInfo.color));
    }

    @Override // com.android.launcher3.icons.FastBitmapDrawable
    public final void drawInternal(Canvas canvas, Rect rect) {
        int save = canvas.save();
        canvas.translate(rect.left, rect.top);
        canvas.scale(rect.width() / 100.0f, rect.height() / 100.0f);
        canvas.drawPath(this.mProgressPath, this.mPaint);
        canvas.restoreToCount(save);
    }
}
