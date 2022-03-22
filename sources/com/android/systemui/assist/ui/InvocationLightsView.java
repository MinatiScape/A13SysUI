package com.android.systemui.assist.ui;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.MathUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
import androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline1;
import com.android.settingslib.Utils;
import com.android.systemui.Dependency;
import com.android.systemui.R$id;
import com.android.systemui.assist.ui.PerimeterPathGuide;
import com.android.systemui.navigationbar.NavigationBar;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.navigationbar.NavigationBarTransitions;
import com.android.systemui.statusbar.phone.LightBarTransitionsController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public class InvocationLightsView extends View implements NavigationBarTransitions.DarkIntensityListener {
    public final ArrayList<EdgeLight> mAssistInvocationLights;
    public final int mDarkColor;
    public final PerimeterPathGuide mGuide;
    public final int mLightColor;
    public final Paint mPaint;
    public final Path mPath;
    public boolean mRegistered;
    public int[] mScreenLocation;
    public boolean mUseNavBarColor;
    public final int mViewHeight;

    public InvocationLightsView(Context context) {
        this(context, null);
    }

    public void onInvocationProgress(float f) {
        if (f == 0.0f) {
            setVisibility(8);
        } else {
            attemptRegisterNavBarListener();
            float regionWidth = this.mGuide.getRegionWidth(PerimeterPathGuide.Region.BOTTOM_LEFT);
            float f2 = (regionWidth - (0.6f * regionWidth)) / 2.0f;
            PerimeterPathGuide perimeterPathGuide = this.mGuide;
            PerimeterPathGuide.Region region = PerimeterPathGuide.Region.BOTTOM;
            float lerp = MathUtils.lerp(0.0f, perimeterPathGuide.getRegionWidth(region) / 4.0f, f);
            float f3 = 1.0f - f;
            float f4 = ((-regionWidth) + f2) * f3;
            float m = MotionController$$ExternalSyntheticOutline0.m(regionWidth, f2, f3, this.mGuide.getRegionWidth(region));
            float f5 = f4 + lerp;
            setLight(0, f4, f5);
            float f6 = 2.0f * lerp;
            setLight(1, f5, f4 + f6);
            float f7 = m - lerp;
            setLight(2, m - f6, f7);
            setLight(3, f7, m);
            setVisibility(0);
        }
        invalidate();
    }

    public InvocationLightsView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public final void attemptRegisterNavBarListener() {
        NavigationBarController navigationBarController;
        NavigationBar navigationBar;
        if (!this.mRegistered && (navigationBarController = (NavigationBarController) Dependency.get(NavigationBarController.class)) != null && (navigationBar = navigationBarController.mNavigationBars.get(0)) != null) {
            NavigationBarTransitions barTransitions = navigationBar.getBarTransitions();
            Objects.requireNonNull(barTransitions);
            barTransitions.mDarkIntensityListeners.add(this);
            LightBarTransitionsController lightBarTransitionsController = barTransitions.mLightTransitionsController;
            Objects.requireNonNull(lightBarTransitionsController);
            updateDarkness(lightBarTransitionsController.mDarkIntensity);
            this.mRegistered = true;
        }
    }

    public final void attemptUnregisterNavBarListener() {
        NavigationBarController navigationBarController;
        NavigationBar navigationBar;
        if (this.mRegistered && (navigationBarController = (NavigationBarController) Dependency.get(NavigationBarController.class)) != null && (navigationBar = navigationBarController.mNavigationBars.get(0)) != null) {
            NavigationBarTransitions barTransitions = navigationBar.getBarTransitions();
            Objects.requireNonNull(barTransitions);
            barTransitions.mDarkIntensityListeners.remove(this);
            this.mRegistered = false;
        }
    }

    public CornerPathRenderer createCornerPathRenderer(Context context) {
        return new CircularCornerPathRenderer(context);
    }

    public final void hide() {
        setVisibility(8);
        Iterator<EdgeLight> it = this.mAssistInvocationLights.iterator();
        while (it.hasNext()) {
            it.next().setEndpoints(0.0f, 0.0f);
        }
        attemptUnregisterNavBarListener();
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        getLocationOnScreen(this.mScreenLocation);
        int[] iArr = this.mScreenLocation;
        canvas.translate(-iArr[0], -iArr[1]);
        if (this.mUseNavBarColor) {
            Iterator<EdgeLight> it = this.mAssistInvocationLights.iterator();
            while (it.hasNext()) {
                renderLight(it.next(), canvas);
            }
            return;
        }
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        renderLight(this.mAssistInvocationLights.get(0), canvas);
        renderLight(this.mAssistInvocationLights.get(3), canvas);
        this.mPaint.setStrokeCap(Paint.Cap.BUTT);
        renderLight(this.mAssistInvocationLights.get(1), canvas);
        renderLight(this.mAssistInvocationLights.get(2), canvas);
    }

    public final void setLight(int i, float f, float f2) {
        if (i < 0 || i >= 4) {
            GridLayoutManager$$ExternalSyntheticOutline1.m("invalid invocation light index: ", i, "InvocationLightsView");
        }
        this.mAssistInvocationLights.get(i).setEndpoints(f, f2);
    }

    public final void updateDarkness(float f) {
        boolean z;
        if (this.mUseNavBarColor) {
            int intValue = ((Integer) ArgbEvaluator.getInstance().evaluate(f, Integer.valueOf(this.mLightColor), Integer.valueOf(this.mDarkColor))).intValue();
            Iterator<EdgeLight> it = this.mAssistInvocationLights.iterator();
            boolean z2 = true;
            while (it.hasNext()) {
                EdgeLight next = it.next();
                Objects.requireNonNull(next);
                if (next.mColor != intValue) {
                    z = true;
                } else {
                    z = false;
                }
                next.mColor = intValue;
                z2 &= z;
            }
            if (z2) {
                invalidate();
            }
        }
    }

    public InvocationLightsView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        getLayoutParams().height = this.mViewHeight;
        requestLayout();
    }

    @Override // android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mGuide.setRotation(getContext().getDisplay().getRotation());
    }

    public final void renderLight(EdgeLight edgeLight, Canvas canvas) {
        boolean z;
        Objects.requireNonNull(edgeLight);
        float f = edgeLight.mLength;
        float f2 = 0.0f;
        if (f > 0.0f) {
            PerimeterPathGuide perimeterPathGuide = this.mGuide;
            Path path = this.mPath;
            float f3 = edgeLight.mStart;
            float f4 = f + f3;
            Objects.requireNonNull(perimeterPathGuide);
            path.reset();
            float f5 = ((f3 % 1.0f) + 1.0f) % 1.0f;
            float f6 = ((f4 % 1.0f) + 1.0f) % 1.0f;
            if (f5 > f6) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                perimeterPathGuide.strokeSegmentInternal(path, f5, 1.0f);
            } else {
                f2 = f5;
            }
            perimeterPathGuide.strokeSegmentInternal(path, f2, f6);
            this.mPaint.setColor(edgeLight.mColor);
            canvas.drawPath(this.mPath, this.mPaint);
        }
    }

    public InvocationLightsView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        DisplayMetrics displayMetrics;
        DisplayMetrics displayMetrics2;
        this.mAssistInvocationLights = new ArrayList<>();
        Paint paint = new Paint();
        this.mPaint = paint;
        this.mPath = new Path();
        this.mScreenLocation = new int[2];
        this.mRegistered = false;
        this.mUseNavBarColor = true;
        context.getDisplay().getRealMetrics(new DisplayMetrics());
        int ceil = (int) Math.ceil(displayMetrics.density * 3.0f);
        paint.setStrokeWidth(ceil);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.MITER);
        paint.setAntiAlias(true);
        this.mGuide = new PerimeterPathGuide(context, createCornerPathRenderer(context), ceil / 2, R$id.getWidth(context), R$id.getHeight(context));
        int max = Math.max(R$id.getCornerRadiusBottom(context), R$id.getCornerRadiusTop(context));
        context.getDisplay().getRealMetrics(new DisplayMetrics());
        this.mViewHeight = Math.max(max, (int) Math.ceil(displayMetrics2.density * 3.0f));
        int themeAttr = Utils.getThemeAttr(((View) this).mContext, 2130968913);
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(((View) this).mContext, Utils.getThemeAttr(((View) this).mContext, 2130969364));
        ContextThemeWrapper contextThemeWrapper2 = new ContextThemeWrapper(((View) this).mContext, themeAttr);
        this.mLightColor = Utils.getColorAttrDefaultColor(contextThemeWrapper, 2130969739);
        this.mDarkColor = Utils.getColorAttrDefaultColor(contextThemeWrapper2, 2130969739);
        for (int i3 = 0; i3 < 4; i3++) {
            this.mAssistInvocationLights.add(new EdgeLight(0));
        }
    }

    @Override // com.android.systemui.navigationbar.NavigationBarTransitions.DarkIntensityListener
    public final void onDarkIntensity(float f) {
        updateDarkness(f);
    }
}
