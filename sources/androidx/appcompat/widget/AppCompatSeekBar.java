package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.SeekBar;
import java.util.Objects;
/* loaded from: classes.dex */
public class AppCompatSeekBar extends SeekBar {
    public final AppCompatSeekBarHelper mAppCompatSeekBarHelper;

    public AppCompatSeekBar(Context context) {
        this(context, null);
    }

    @Override // android.widget.AbsSeekBar, android.widget.ProgressBar, android.view.View
    public final synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mAppCompatSeekBarHelper.drawTickMarks(canvas);
    }

    public AppCompatSeekBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 2130969700);
    }

    public AppCompatSeekBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        ThemeUtils.checkAppCompatTheme(this, getContext());
        AppCompatSeekBarHelper appCompatSeekBarHelper = new AppCompatSeekBarHelper(this);
        this.mAppCompatSeekBarHelper = appCompatSeekBarHelper;
        appCompatSeekBarHelper.loadFromAttributes(attributeSet, i);
    }

    @Override // android.widget.AbsSeekBar, android.widget.ProgressBar, android.view.View
    public void drawableStateChanged() {
        super.drawableStateChanged();
        AppCompatSeekBarHelper appCompatSeekBarHelper = this.mAppCompatSeekBarHelper;
        Objects.requireNonNull(appCompatSeekBarHelper);
        Drawable drawable = appCompatSeekBarHelper.mTickMark;
        if (drawable != null && drawable.isStateful() && drawable.setState(appCompatSeekBarHelper.mView.getDrawableState())) {
            appCompatSeekBarHelper.mView.invalidateDrawable(drawable);
        }
    }

    @Override // android.widget.AbsSeekBar, android.widget.ProgressBar, android.view.View
    public final void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        AppCompatSeekBarHelper appCompatSeekBarHelper = this.mAppCompatSeekBarHelper;
        Objects.requireNonNull(appCompatSeekBarHelper);
        Drawable drawable = appCompatSeekBarHelper.mTickMark;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
    }
}
