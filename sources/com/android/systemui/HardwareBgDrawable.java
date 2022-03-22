package com.android.systemui;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
/* loaded from: classes.dex */
public final class HardwareBgDrawable extends LayerDrawable {
    public final Drawable[] mLayers;
    public final Paint mPaint = new Paint();
    public final boolean mRoundTop;

    @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
    public final int getOpacity() {
        return -1;
    }

    @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
    public final void setAlpha(int i) {
        this.mPaint.setAlpha(i);
    }

    @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
    public final void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public HardwareBgDrawable(android.content.Context r6) {
        /*
            r5 = this;
            r0 = 2131232616(0x7f080768, float:1.8081346E38)
            r1 = 0
            r2 = 2
            android.graphics.drawable.Drawable[] r3 = new android.graphics.drawable.Drawable[r2]
            android.graphics.drawable.Drawable r4 = r6.getDrawable(r0)
            android.graphics.drawable.Drawable r4 = r4.mutate()
            r3[r1] = r4
            android.graphics.drawable.Drawable r0 = r6.getDrawable(r0)
            android.graphics.drawable.Drawable r0 = r0.mutate()
            r1 = 1
            r3[r1] = r0
            r0 = r3[r1]
            r4 = 16843827(0x1010433, float:2.369657E-38)
            android.content.res.ColorStateList r6 = com.android.settingslib.Utils.getColorAttr(r6, r4)
            r0.setTintList(r6)
            r5.<init>(r3)
            android.graphics.Paint r6 = new android.graphics.Paint
            r6.<init>()
            r5.mPaint = r6
            int r6 = r3.length
            if (r6 != r2) goto L_0x003a
            r5.mRoundTop = r1
            r5.mLayers = r3
            return
        L_0x003a:
            java.lang.IllegalArgumentException r5 = new java.lang.IllegalArgumentException
            java.lang.String r6 = "Need 2 layers"
            r5.<init>(r6)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.HardwareBgDrawable.<init>(android.content.Context):void");
    }

    @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
    public final void draw(Canvas canvas) {
        Rect bounds = getBounds();
        int i = bounds.top;
        int i2 = i + 0;
        int i3 = bounds.bottom;
        if (i2 > i3) {
            i2 = i3;
        }
        if (this.mRoundTop) {
            this.mLayers[0].setBounds(bounds.left, i, bounds.right, i2);
        } else {
            this.mLayers[1].setBounds(bounds.left, i2, bounds.right, i3);
        }
        if (this.mRoundTop) {
            this.mLayers[1].draw(canvas);
            this.mLayers[0].draw(canvas);
            return;
        }
        this.mLayers[0].draw(canvas);
        this.mLayers[1].draw(canvas);
    }
}
