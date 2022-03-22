package com.android.keyguard.clock;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.plugins.ClockPlugin;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
/* loaded from: classes.dex */
public final class DefaultClockController implements ClockPlugin {
    public final SysuiColorExtractor mColorExtractor;
    public final LayoutInflater mLayoutInflater;
    public final ViewPreviewer mRenderer = new ViewPreviewer();
    public final Resources mResources;
    public TextView mTextDate;
    public TextView mTextTime;
    public View mView;

    @Override // com.android.systemui.plugins.ClockPlugin
    public final String getName() {
        return "default";
    }

    @Override // com.android.systemui.plugins.ClockPlugin
    public final View getView() {
        return null;
    }

    @Override // com.android.systemui.plugins.ClockPlugin
    public final void onDestroyView() {
        this.mView = null;
        this.mTextTime = null;
        this.mTextDate = null;
    }

    @Override // com.android.systemui.plugins.ClockPlugin
    public final void onTimeTick() {
    }

    @Override // com.android.systemui.plugins.ClockPlugin
    public final void onTimeZoneChanged(TimeZone timeZone) {
    }

    @Override // com.android.systemui.plugins.ClockPlugin
    public final void setColorPalette(boolean z, int[] iArr) {
    }

    @Override // com.android.systemui.plugins.ClockPlugin
    public final void setDarkAmount(float f) {
    }

    @Override // com.android.systemui.plugins.ClockPlugin
    public final void setStyle(Paint.Style style) {
    }

    @Override // com.android.systemui.plugins.ClockPlugin
    public final boolean shouldShowStatusArea() {
        return true;
    }

    @Override // com.android.systemui.plugins.ClockPlugin
    public final View getBigClockView() {
        if (this.mView == null) {
            View inflate = this.mLayoutInflater.inflate(2131624066, (ViewGroup) null);
            this.mView = inflate;
            this.mTextTime = (TextView) inflate.findViewById(2131429052);
            this.mTextDate = (TextView) this.mView.findViewById(2131427797);
        }
        return this.mView;
    }

    @Override // com.android.systemui.plugins.ClockPlugin
    public final Bitmap getThumbnail() {
        return BitmapFactory.decodeResource(this.mResources, 2131231674);
    }

    @Override // com.android.systemui.plugins.ClockPlugin
    public final String getTitle() {
        return this.mResources.getString(2131952124);
    }

    @Override // com.android.systemui.plugins.ClockPlugin
    public final void setTextColor(int i) {
        this.mTextTime.setTextColor(i);
        this.mTextDate.setTextColor(i);
    }

    public DefaultClockController(Resources resources, LayoutInflater layoutInflater, SysuiColorExtractor sysuiColorExtractor) {
        this.mResources = resources;
        this.mLayoutInflater = layoutInflater;
        this.mColorExtractor = sysuiColorExtractor;
    }

    @Override // com.android.systemui.plugins.ClockPlugin
    public final Bitmap getPreview(final int i, final int i2) {
        final View bigClockView = getBigClockView();
        setTextColor(-1);
        ColorExtractor.GradientColors colors = this.mColorExtractor.getColors(2);
        colors.supportsDarkText();
        colors.getColorPalette();
        final ViewPreviewer viewPreviewer = this.mRenderer;
        Objects.requireNonNull(viewPreviewer);
        if (bigClockView == null) {
            return null;
        }
        FutureTask futureTask = new FutureTask(new Callable<Bitmap>() { // from class: com.android.keyguard.clock.ViewPreviewer.1
            @Override // java.util.concurrent.Callable
            public final Bitmap call() throws Exception {
                Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                canvas.drawColor(-16777216);
                ViewPreviewer viewPreviewer2 = viewPreviewer;
                View view = bigClockView;
                Objects.requireNonNull(viewPreviewer2);
                ViewPreviewer.dispatchVisibilityAggregated(view, true);
                bigClockView.measure(View.MeasureSpec.makeMeasureSpec(i, 1073741824), View.MeasureSpec.makeMeasureSpec(i2, 1073741824));
                bigClockView.layout(0, 0, i, i2);
                bigClockView.draw(canvas);
                return createBitmap;
            }
        });
        if (Looper.myLooper() == Looper.getMainLooper()) {
            futureTask.run();
        } else {
            viewPreviewer.mMainHandler.post(futureTask);
        }
        try {
            return (Bitmap) futureTask.get();
        } catch (Exception e) {
            Log.e("ViewPreviewer", "Error completing task", e);
            return null;
        }
    }

    @Override // com.android.systemui.plugins.ClockPlugin
    public final int getPreferredY(int i) {
        return i / 2;
    }
}
