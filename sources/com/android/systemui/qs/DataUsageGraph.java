package com.android.systemui.qs;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import com.android.settingslib.Utils;
/* loaded from: classes.dex */
public class DataUsageGraph extends View {
    public final int mMarkerWidth;
    public final int mTrackColor;
    public final int mUsageColor;
    public final int mWarningColor;
    public final RectF mTmpRect = new RectF();
    public final Paint mTmpPaint = new Paint();

    public DataUsageGraph(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Resources resources = context.getResources();
        this.mTrackColor = Utils.getColorStateListDefaultColor(context, 2131099800);
        this.mWarningColor = Utils.getColorStateListDefaultColor(context, 2131099801);
        this.mUsageColor = Utils.getColorAttrDefaultColor(context, 16843829);
        Utils.getColorAttrDefaultColor(context, 16844099);
        this.mMarkerWidth = resources.getDimensionPixelSize(2131165599);
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = this.mTmpRect;
        Paint paint = this.mTmpPaint;
        int width = getWidth();
        int height = getHeight();
        float f = width;
        float f2 = (float) 0;
        float f3 = (f2 / f2) * f;
        rectF.set(0.0f, 0.0f, f, height);
        paint.setColor(this.mTrackColor);
        canvas.drawRect(rectF, paint);
        float f4 = height;
        rectF.set(0.0f, 0.0f, f3, f4);
        paint.setColor(this.mUsageColor);
        canvas.drawRect(rectF, paint);
        float min = Math.min(Math.max(f3 - (this.mMarkerWidth / 2), 0.0f), width - this.mMarkerWidth);
        rectF.set(min, 0.0f, this.mMarkerWidth + min, f4);
        paint.setColor(this.mWarningColor);
        canvas.drawRect(rectF, paint);
    }
}
