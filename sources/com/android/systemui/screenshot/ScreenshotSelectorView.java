package com.android.systemui.screenshot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.Objects;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class ScreenshotSelectorView extends View {
    public static final /* synthetic */ int $r8$clinit = 0;
    public Consumer<Rect> mOnScreenshotSelected;
    public final Paint mPaintBackground;
    public final Paint mPaintSelection;
    public Rect mSelectionRect;
    public Point mStartPoint;

    public ScreenshotSelectorView(Context context) {
        this(context, null);
    }

    public ScreenshotSelectorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Paint paint = new Paint(-16777216);
        this.mPaintBackground = paint;
        paint.setAlpha(160);
        Paint paint2 = new Paint(0);
        this.mPaintSelection = paint2;
        paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        setOnTouchListener(new View.OnTouchListener() { // from class: com.android.systemui.screenshot.ScreenshotSelectorView$$ExternalSyntheticLambda0
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                ScreenshotSelectorView screenshotSelectorView = ScreenshotSelectorView.this;
                int i = ScreenshotSelectorView.$r8$clinit;
                Objects.requireNonNull(screenshotSelectorView);
                int action = motionEvent.getAction();
                if (action == 0) {
                    int x = (int) motionEvent.getX();
                    int y = (int) motionEvent.getY();
                    screenshotSelectorView.mStartPoint = new Point(x, y);
                    screenshotSelectorView.mSelectionRect = new Rect(x, y, x, y);
                    return true;
                } else if (action == 1) {
                    screenshotSelectorView.setVisibility(8);
                    Rect rect = screenshotSelectorView.mSelectionRect;
                    if (!(screenshotSelectorView.mOnScreenshotSelected == null || rect == null || rect.width() == 0 || rect.height() == 0)) {
                        screenshotSelectorView.mOnScreenshotSelected.accept(rect);
                    }
                    screenshotSelectorView.mStartPoint = null;
                    screenshotSelectorView.mSelectionRect = null;
                    return true;
                } else if (action != 2) {
                    return false;
                } else {
                    int x2 = (int) motionEvent.getX();
                    int y2 = (int) motionEvent.getY();
                    Rect rect2 = screenshotSelectorView.mSelectionRect;
                    if (rect2 == null) {
                        return true;
                    }
                    rect2.left = Math.min(screenshotSelectorView.mStartPoint.x, x2);
                    screenshotSelectorView.mSelectionRect.right = Math.max(screenshotSelectorView.mStartPoint.x, x2);
                    screenshotSelectorView.mSelectionRect.top = Math.min(screenshotSelectorView.mStartPoint.y, y2);
                    screenshotSelectorView.mSelectionRect.bottom = Math.max(screenshotSelectorView.mStartPoint.y, y2);
                    screenshotSelectorView.invalidate();
                    return true;
                }
            }
        });
    }

    @Override // android.view.View
    public final void draw(Canvas canvas) {
        canvas.drawRect(((View) this).mLeft, ((View) this).mTop, ((View) this).mRight, ((View) this).mBottom, this.mPaintBackground);
        Rect rect = this.mSelectionRect;
        if (rect != null) {
            canvas.drawRect(rect, this.mPaintSelection);
        }
    }
}
