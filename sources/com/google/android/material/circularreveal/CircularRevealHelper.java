package com.google.android.material.circularreveal;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.startup.R$string;
import com.google.android.material.circularreveal.CircularRevealWidget;
/* loaded from: classes.dex */
public final class CircularRevealHelper {
    public final Delegate delegate;
    public Drawable overlayDrawable;
    public CircularRevealWidget.RevealInfo revealInfo;
    public final Paint scrimPaint;
    public final View view;

    /* loaded from: classes.dex */
    public interface Delegate {
        void actualDraw(Canvas canvas);

        boolean actualIsOpaque();
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x001b  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0048  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x007f  */
    /* JADX WARN: Removed duplicated region for block: B:33:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void draw(android.graphics.Canvas r10) {
        /*
            r9 = this;
            com.google.android.material.circularreveal.CircularRevealWidget$RevealInfo r0 = r9.revealInfo
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L_0x0017
            float r0 = r0.radius
            r3 = 2139095039(0x7f7fffff, float:3.4028235E38)
            int r0 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r0 != 0) goto L_0x0011
            r0 = r2
            goto L_0x0012
        L_0x0011:
            r0 = r1
        L_0x0012:
            if (r0 == 0) goto L_0x0015
            goto L_0x0017
        L_0x0015:
            r0 = r1
            goto L_0x0018
        L_0x0017:
            r0 = r2
        L_0x0018:
            r0 = r0 ^ r2
            if (r0 == 0) goto L_0x0048
            com.google.android.material.circularreveal.CircularRevealHelper$Delegate r0 = r9.delegate
            r0.actualDraw(r10)
            android.graphics.Paint r0 = r9.scrimPaint
            int r0 = r0.getColor()
            int r0 = android.graphics.Color.alpha(r0)
            if (r0 == 0) goto L_0x002e
            r0 = r2
            goto L_0x002f
        L_0x002e:
            r0 = r1
        L_0x002f:
            if (r0 == 0) goto L_0x0074
            r4 = 0
            r5 = 0
            android.view.View r0 = r9.view
            int r0 = r0.getWidth()
            float r6 = (float) r0
            android.view.View r0 = r9.view
            int r0 = r0.getHeight()
            float r7 = (float) r0
            android.graphics.Paint r8 = r9.scrimPaint
            r3 = r10
            r3.drawRect(r4, r5, r6, r7, r8)
            goto L_0x0074
        L_0x0048:
            com.google.android.material.circularreveal.CircularRevealHelper$Delegate r0 = r9.delegate
            r0.actualDraw(r10)
            android.graphics.Paint r0 = r9.scrimPaint
            int r0 = r0.getColor()
            int r0 = android.graphics.Color.alpha(r0)
            if (r0 == 0) goto L_0x005b
            r0 = r2
            goto L_0x005c
        L_0x005b:
            r0 = r1
        L_0x005c:
            if (r0 == 0) goto L_0x0074
            r4 = 0
            r5 = 0
            android.view.View r0 = r9.view
            int r0 = r0.getWidth()
            float r6 = (float) r0
            android.view.View r0 = r9.view
            int r0 = r0.getHeight()
            float r7 = (float) r0
            android.graphics.Paint r8 = r9.scrimPaint
            r3 = r10
            r3.drawRect(r4, r5, r6, r7, r8)
        L_0x0074:
            android.graphics.drawable.Drawable r0 = r9.overlayDrawable
            if (r0 == 0) goto L_0x007d
            com.google.android.material.circularreveal.CircularRevealWidget$RevealInfo r3 = r9.revealInfo
            if (r3 == 0) goto L_0x007d
            r1 = r2
        L_0x007d:
            if (r1 == 0) goto L_0x00a8
            android.graphics.Rect r0 = r0.getBounds()
            com.google.android.material.circularreveal.CircularRevealWidget$RevealInfo r1 = r9.revealInfo
            float r1 = r1.centerX
            int r2 = r0.width()
            float r2 = (float) r2
            r3 = 1073741824(0x40000000, float:2.0)
            float r2 = r2 / r3
            float r1 = r1 - r2
            com.google.android.material.circularreveal.CircularRevealWidget$RevealInfo r2 = r9.revealInfo
            float r2 = r2.centerY
            int r0 = r0.height()
            float r0 = (float) r0
            float r0 = r0 / r3
            float r2 = r2 - r0
            r10.translate(r1, r2)
            android.graphics.drawable.Drawable r9 = r9.overlayDrawable
            r9.draw(r10)
            float r9 = -r1
            float r0 = -r2
            r10.translate(r9, r0)
        L_0x00a8:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.circularreveal.CircularRevealHelper.draw(android.graphics.Canvas):void");
    }

    public final int getCircularRevealScrimColor() {
        return this.scrimPaint.getColor();
    }

    public final CircularRevealWidget.RevealInfo getRevealInfo() {
        boolean z;
        CircularRevealWidget.RevealInfo revealInfo = this.revealInfo;
        if (revealInfo == null) {
            return null;
        }
        CircularRevealWidget.RevealInfo revealInfo2 = new CircularRevealWidget.RevealInfo(revealInfo);
        if (revealInfo2.radius == Float.MAX_VALUE) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            revealInfo2.radius = R$string.distanceToFurthestCorner(revealInfo2.centerX, revealInfo2.centerY, this.view.getWidth(), this.view.getHeight());
        }
        return revealInfo2;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0023  */
    /* JADX WARN: Removed duplicated region for block: B:18:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean isOpaque() {
        /*
            r3 = this;
            com.google.android.material.circularreveal.CircularRevealHelper$Delegate r0 = r3.delegate
            boolean r0 = r0.actualIsOpaque()
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L_0x0024
            com.google.android.material.circularreveal.CircularRevealWidget$RevealInfo r3 = r3.revealInfo
            if (r3 == 0) goto L_0x001f
            float r3 = r3.radius
            r0 = 2139095039(0x7f7fffff, float:3.4028235E38)
            int r3 = (r3 > r0 ? 1 : (r3 == r0 ? 0 : -1))
            if (r3 != 0) goto L_0x0019
            r3 = r2
            goto L_0x001a
        L_0x0019:
            r3 = r1
        L_0x001a:
            if (r3 == 0) goto L_0x001d
            goto L_0x001f
        L_0x001d:
            r3 = r1
            goto L_0x0020
        L_0x001f:
            r3 = r2
        L_0x0020:
            r3 = r3 ^ r2
            if (r3 != 0) goto L_0x0024
            r1 = r2
        L_0x0024:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.circularreveal.CircularRevealHelper.isOpaque():boolean");
    }

    public final void setCircularRevealOverlayDrawable(Drawable drawable) {
        this.overlayDrawable = drawable;
        this.view.invalidate();
    }

    public final void setCircularRevealScrimColor(int i) {
        this.scrimPaint.setColor(i);
        this.view.invalidate();
    }

    public final void setRevealInfo(CircularRevealWidget.RevealInfo revealInfo) {
        boolean z;
        if (revealInfo == null) {
            this.revealInfo = null;
        } else {
            CircularRevealWidget.RevealInfo revealInfo2 = this.revealInfo;
            if (revealInfo2 == null) {
                this.revealInfo = new CircularRevealWidget.RevealInfo(revealInfo);
            } else {
                float f = revealInfo.centerX;
                float f2 = revealInfo.centerY;
                float f3 = revealInfo.radius;
                revealInfo2.centerX = f;
                revealInfo2.centerY = f2;
                revealInfo2.radius = f3;
            }
            if (revealInfo.radius + 1.0E-4f >= R$string.distanceToFurthestCorner(revealInfo.centerX, revealInfo.centerY, this.view.getWidth(), this.view.getHeight())) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                this.revealInfo.radius = Float.MAX_VALUE;
            }
        }
        this.view.invalidate();
    }

    public CircularRevealHelper(Delegate delegate) {
        this.delegate = delegate;
        View view = (View) delegate;
        this.view = view;
        view.setWillNotDraw(false);
        new Path();
        new Paint(7);
        Paint paint = new Paint(1);
        this.scrimPaint = paint;
        paint.setColor(0);
    }
}
