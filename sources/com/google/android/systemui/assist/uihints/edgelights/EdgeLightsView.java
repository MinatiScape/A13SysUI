package com.google.android.systemui.assist.uihints.edgelights;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.metrics.LogMaker;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import androidx.leanback.R$color;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.assist.ui.EdgeLight;
import com.android.systemui.assist.ui.PathSpecCornerPathRenderer;
import com.android.systemui.assist.ui.PerimeterPathGuide;
import com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda14;
import com.android.systemui.screenshot.ScreenshotController$$ExternalSyntheticLambda6;
import com.google.android.systemui.assist.uihints.edgelights.mode.Gone;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
/* loaded from: classes.dex */
public class EdgeLightsView extends View {
    public static final /* synthetic */ int $r8$clinit = 0;
    public ArrayList mAssistInvocationLights;
    public EdgeLight[] mAssistLights;
    public HashSet mListeners;
    public Mode mMode;
    public final Paint mPaint;
    public final Path mPath;
    public final PerimeterPathGuide mPerimeterPathGuide;
    public int[] mScreenLocation;

    /* loaded from: classes.dex */
    public interface Mode {
        int getSubType();

        default void onAudioLevelUpdate(float f) {
        }

        default void onConfigurationChanged() {
        }

        void onNewModeRequest(EdgeLightsView edgeLightsView, Mode mode);

        default boolean preventsInvocations() {
            return false;
        }

        void start(EdgeLightsView edgeLightsView, PerimeterPathGuide perimeterPathGuide, Mode mode);

        default void logState() {
            MetricsLogger.action(new LogMaker(1716).setType(6).setSubtype(getSubType()));
        }
    }

    public EdgeLightsView(Context context) {
        this(context, null);
    }

    public EdgeLightsView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public final void commitModeTransition(Mode mode) {
        mode.start(this, this.mPerimeterPathGuide, this.mMode);
        this.mMode = mode;
        this.mListeners.forEach(new NavigationBar$$ExternalSyntheticLambda14(this, 2));
        this.mAssistInvocationLights.forEach(EdgeLightsView$$ExternalSyntheticLambda0.INSTANCE);
        invalidate();
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        getLocationOnScreen(this.mScreenLocation);
        int[] iArr = this.mScreenLocation;
        canvas.translate(-iArr[0], -iArr[1]);
        EdgeLight[] edgeLightArr = this.mAssistLights;
        if (edgeLightArr.length != 0) {
            this.mPaint.setStrokeCap(Paint.Cap.ROUND);
            renderLight(canvas, edgeLightArr[0]);
            if (edgeLightArr.length > 1) {
                renderLight(canvas, edgeLightArr[edgeLightArr.length - 1]);
            }
            if (edgeLightArr.length > 2) {
                this.mPaint.setStrokeCap(Paint.Cap.BUTT);
                for (int i = 1; i < edgeLightArr.length - 1; i++) {
                    renderLight(canvas, edgeLightArr[i]);
                }
            }
        }
        ArrayList arrayList = this.mAssistInvocationLights;
        if (!arrayList.isEmpty()) {
            this.mPaint.setStrokeCap(Paint.Cap.ROUND);
            renderLight(canvas, (EdgeLight) arrayList.get(0));
            if (arrayList.size() > 1) {
                renderLight(canvas, (EdgeLight) arrayList.get(arrayList.size() - 1));
            }
            if (arrayList.size() > 2) {
                this.mPaint.setStrokeCap(Paint.Cap.BUTT);
                for (EdgeLight edgeLight : arrayList.subList(1, arrayList.size() - 1)) {
                    renderLight(canvas, edgeLight);
                }
            }
        }
    }

    public final void renderLight(Canvas canvas, EdgeLight edgeLight) {
        boolean z;
        PerimeterPathGuide perimeterPathGuide = this.mPerimeterPathGuide;
        Path path = this.mPath;
        Objects.requireNonNull(edgeLight);
        float f = edgeLight.mStart;
        float f2 = edgeLight.mLength + f;
        Objects.requireNonNull(perimeterPathGuide);
        path.reset();
        float f3 = ((f % 1.0f) + 1.0f) % 1.0f;
        float f4 = ((f2 % 1.0f) + 1.0f) % 1.0f;
        if (f3 > f4) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            perimeterPathGuide.strokeSegmentInternal(path, f3, 1.0f);
            f3 = 0.0f;
        }
        perimeterPathGuide.strokeSegmentInternal(path, f3, f4);
        this.mPaint.setColor(edgeLight.mColor);
        canvas.drawPath(this.mPath, this.mPaint);
    }

    public final void setAssistLights(EdgeLight[] edgeLightArr) {
        post(new ScreenshotController$$ExternalSyntheticLambda6(this, edgeLightArr, 5));
    }

    public EdgeLightsView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public final EdgeLight[] getAssistLights() {
        if (Looper.getMainLooper().isCurrentThread()) {
            return this.mAssistLights;
        }
        throw new IllegalStateException("Must be called from main thread");
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateRotation();
        this.mMode.onConfigurationChanged();
    }

    @Override // android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        updateRotation();
    }

    @Override // android.view.View
    public final void setVisibility(int i) {
        int visibility = getVisibility();
        super.setVisibility(i);
        if (visibility == 8) {
            updateRotation();
        }
    }

    public final void updateRotation() {
        this.mPerimeterPathGuide.setRotation(getContext().getDisplay().getRotation());
    }

    public EdgeLightsView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        int i3;
        int i4;
        Paint paint = new Paint();
        this.mPaint = paint;
        this.mAssistLights = new EdgeLight[0];
        this.mAssistInvocationLights = new ArrayList();
        this.mPath = new Path();
        this.mListeners = new HashSet();
        this.mScreenLocation = new int[2];
        Display defaultDisplay = R$color.getDefaultDisplay(context);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getRealMetrics(displayMetrics);
        int ceil = (int) Math.ceil(3.0f * displayMetrics.density);
        paint.setStrokeWidth(ceil);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.MITER);
        paint.setAntiAlias(true);
        PathSpecCornerPathRenderer pathSpecCornerPathRenderer = new PathSpecCornerPathRenderer(context);
        int i5 = ceil / 2;
        Display defaultDisplay2 = R$color.getDefaultDisplay(context);
        DisplayMetrics displayMetrics2 = new DisplayMetrics();
        defaultDisplay2.getRealMetrics(displayMetrics2);
        int rotation = defaultDisplay2.getRotation();
        if (rotation == 0 || rotation == 2) {
            i3 = displayMetrics2.widthPixels;
        } else {
            i3 = displayMetrics2.heightPixels;
        }
        Display defaultDisplay3 = R$color.getDefaultDisplay(context);
        DisplayMetrics displayMetrics3 = new DisplayMetrics();
        defaultDisplay3.getRealMetrics(displayMetrics3);
        int rotation2 = defaultDisplay3.getRotation();
        if (rotation2 == 0 || rotation2 == 2) {
            i4 = displayMetrics3.heightPixels;
        } else {
            i4 = displayMetrics3.widthPixels;
        }
        this.mPerimeterPathGuide = new PerimeterPathGuide(context, pathSpecCornerPathRenderer, i5, i3, i4);
        Gone gone = new Gone();
        this.mMode = gone;
        commitModeTransition(gone);
        Resources resources = getResources();
        this.mAssistInvocationLights.add(new EdgeLight(resources.getColor(2131099853)));
        this.mAssistInvocationLights.add(new EdgeLight(resources.getColor(2131099855)));
        this.mAssistInvocationLights.add(new EdgeLight(resources.getColor(2131099856)));
        this.mAssistInvocationLights.add(new EdgeLight(resources.getColor(2131099854)));
    }
}
