package com.android.systemui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.Display;
import android.view.DisplayCutout;
import android.view.DisplayInfo;
import android.view.View;
import com.android.systemui.RegionInterceptingFrameLayout;
import com.android.systemui.animation.Interpolators;
import java.util.List;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DisplayCutoutBaseView.kt */
/* loaded from: classes.dex */
public class DisplayCutoutBaseView extends View implements RegionInterceptingFrameLayout.RegionInterceptableView {
    public ValueAnimator cameraProtectionAnimator;
    public float cameraProtectionProgress;
    public final Path cutoutPath;
    public final DisplayInfo displayInfo;
    public Display.Mode displayMode;
    public int displayRotation;
    public final int[] location;
    public final Paint paint;
    public final Path protectionPath;
    public final Path protectionPathOrig;
    public final RectF protectionRect;
    public final RectF protectionRectOrig;
    public final boolean shouldDrawCutout;
    public boolean showProtection;

    public DisplayCutoutBaseView(Context context) {
        super(context);
        Resources resources = getContext().getResources();
        Display display = getContext().getDisplay();
        this.shouldDrawCutout = DisplayCutout.getFillBuiltInDisplayCutout(resources, display == null ? null : display.getUniqueId());
        this.location = new int[2];
        this.displayInfo = new DisplayInfo();
        this.paint = new Paint();
        this.cutoutPath = new Path();
        this.protectionRect = new RectF();
        this.protectionPath = new Path();
        this.protectionRectOrig = new RectF();
        this.protectionPathOrig = new Path();
        this.cameraProtectionProgress = 0.5f;
    }

    public static /* synthetic */ void getDisplayInfo$annotations() {
    }

    public static /* synthetic */ void getProtectionPath$annotations() {
    }

    public static /* synthetic */ void getProtectionRect$annotations() {
    }

    public static final void transformPhysicalToLogicalCoordinates(int i, int i2, int i3, Matrix matrix) {
        if (i == 0) {
            matrix.reset();
        } else if (i == 1) {
            matrix.setRotate(270.0f);
            matrix.postTranslate(0.0f, i2);
        } else if (i == 2) {
            matrix.setRotate(180.0f);
            matrix.postTranslate(i2, i3);
        } else if (i == 3) {
            matrix.setRotate(90.0f);
            matrix.postTranslate(i3, 0.0f);
        } else {
            throw new IllegalArgumentException(Intrinsics.stringPlus("Unknown rotation: ", Integer.valueOf(i)));
        }
    }

    public void drawCutouts(Canvas canvas) {
        Path path;
        DisplayCutout displayCutout = this.displayInfo.displayCutout;
        if (displayCutout == null) {
            path = null;
        } else {
            path = displayCutout.getCutoutPath();
        }
        if (path != null) {
            canvas.drawPath(this.cutoutPath, this.paint);
        }
    }

    public final void enableShowProtection(boolean z) {
        float f;
        if (this.showProtection != z) {
            this.showProtection = z;
            updateProtectionBoundingPath();
            if (this.showProtection) {
                requestLayout();
            }
            ValueAnimator valueAnimator = this.cameraProtectionAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            float[] fArr = new float[2];
            fArr[0] = this.cameraProtectionProgress;
            if (this.showProtection) {
                f = 1.0f;
            } else {
                f = 0.5f;
            }
            fArr[1] = f;
            ValueAnimator duration = ValueAnimator.ofFloat(fArr).setDuration(750L);
            this.cameraProtectionAnimator = duration;
            if (duration != null) {
                duration.setInterpolator(Interpolators.DECELERATE_QUINT);
            }
            ValueAnimator valueAnimator2 = this.cameraProtectionAnimator;
            if (valueAnimator2 != null) {
                valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.DisplayCutoutBaseView$enableShowProtection$1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                        DisplayCutoutBaseView displayCutoutBaseView = DisplayCutoutBaseView.this;
                        Object animatedValue = valueAnimator3.getAnimatedValue();
                        Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                        displayCutoutBaseView.cameraProtectionProgress = ((Float) animatedValue).floatValue();
                        DisplayCutoutBaseView.this.invalidate();
                    }
                });
            }
            ValueAnimator valueAnimator3 = this.cameraProtectionAnimator;
            if (valueAnimator3 != null) {
                valueAnimator3.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.DisplayCutoutBaseView$enableShowProtection$2
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator) {
                        DisplayCutoutBaseView displayCutoutBaseView = DisplayCutoutBaseView.this;
                        displayCutoutBaseView.cameraProtectionAnimator = null;
                        if (!displayCutoutBaseView.showProtection) {
                            displayCutoutBaseView.requestLayout();
                        }
                    }
                });
            }
            ValueAnimator valueAnimator4 = this.cameraProtectionAnimator;
            if (valueAnimator4 != null) {
                valueAnimator4.start();
            }
        }
    }

    @Override // com.android.systemui.RegionInterceptingFrameLayout.RegionInterceptableView
    public final Region getInterceptRegion() {
        DisplayCutout displayCutout = this.displayInfo.displayCutout;
        List<Rect> list = null;
        if (displayCutout == null) {
            return null;
        }
        if (displayCutout != null) {
            list = displayCutout.getBoundingRects();
        }
        Region obtain = Region.obtain();
        if (list != null) {
            for (Rect rect : list) {
                if (rect != null && !rect.isEmpty()) {
                    obtain.op(rect, Region.Op.UNION);
                }
            }
        }
        getRootView().getLocationOnScreen(this.location);
        int[] iArr = this.location;
        obtain.translate(-iArr[0], -iArr[1]);
        obtain.op(getRootView().getLeft(), getRootView().getTop(), getRootView().getRight(), getRootView().getBottom(), Region.Op.INTERSECT);
        return obtain;
    }

    public final void setProtection(Path path, Rect rect) {
        this.protectionPathOrig.reset();
        this.protectionPathOrig.set(path);
        this.protectionPath.reset();
        this.protectionRectOrig.setEmpty();
        this.protectionRectOrig.set(rect);
        this.protectionRect.setEmpty();
    }

    @Override // com.android.systemui.RegionInterceptingFrameLayout.RegionInterceptableView
    public final boolean shouldInterceptTouch() {
        if (this.displayInfo.displayCutout == null || getVisibility() != 0 || !this.shouldDrawCutout) {
            return false;
        }
        return true;
    }

    public void updateCutout() {
        Path cutoutPath;
        this.cutoutPath.reset();
        getDisplay().getDisplayInfo(this.displayInfo);
        DisplayCutout displayCutout = this.displayInfo.displayCutout;
        if (!(displayCutout == null || (cutoutPath = displayCutout.getCutoutPath()) == null)) {
            this.cutoutPath.set(cutoutPath);
        }
        invalidate();
    }

    public final void updateProtectionBoundingPath() {
        int i;
        DisplayInfo displayInfo = this.displayInfo;
        int i2 = displayInfo.logicalWidth;
        int i3 = displayInfo.logicalHeight;
        int i4 = displayInfo.rotation;
        boolean z = true;
        if (!(i4 == 1 || i4 == 3)) {
            z = false;
        }
        if (z) {
            i = i3;
        } else {
            i = i2;
        }
        if (!z) {
            i2 = i3;
        }
        Matrix matrix = new Matrix();
        transformPhysicalToLogicalCoordinates(this.displayInfo.rotation, i, i2, matrix);
        if (!this.protectionPathOrig.isEmpty()) {
            this.protectionPath.set(this.protectionPathOrig);
            this.protectionPath.transform(matrix);
            matrix.mapRect(this.protectionRect, this.protectionRectOrig);
        }
    }

    @Override // android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        updateCutout();
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.shouldDrawCutout) {
            canvas.save();
            getLocationOnScreen(this.location);
            int[] iArr = this.location;
            canvas.translate(-iArr[0], -iArr[1]);
            drawCutouts(canvas);
            if (this.cameraProtectionProgress > 0.5f && !this.protectionRect.isEmpty()) {
                float f = this.cameraProtectionProgress;
                canvas.scale(f, f, this.protectionRect.centerX(), this.protectionRect.centerY());
                canvas.drawPath(this.protectionPath, this.paint);
            }
            canvas.restore();
        }
    }

    public DisplayCutoutBaseView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Resources resources = getContext().getResources();
        Display display = getContext().getDisplay();
        this.shouldDrawCutout = DisplayCutout.getFillBuiltInDisplayCutout(resources, display == null ? null : display.getUniqueId());
        this.location = new int[2];
        this.displayInfo = new DisplayInfo();
        this.paint = new Paint();
        this.cutoutPath = new Path();
        this.protectionRect = new RectF();
        this.protectionPath = new Path();
        this.protectionRectOrig = new RectF();
        this.protectionPathOrig = new Path();
        this.cameraProtectionProgress = 0.5f;
    }
}
