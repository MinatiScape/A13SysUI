package androidx.mediarouter.app;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.util.Log;
import androidx.appcompat.widget.AppCompatSeekBar;
/* loaded from: classes.dex */
class MediaRouteVolumeSlider extends AppCompatSeekBar {
    public int mBackgroundColor;
    public final float mDisabledAlpha;
    public boolean mHideThumb;
    public int mProgressAndThumbColor;
    public Drawable mThumb;

    public MediaRouteVolumeSlider(Context context) {
        this(context, null);
    }

    public MediaRouteVolumeSlider(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 2130969700);
    }

    public final void setColor(int i, int i2) {
        if (this.mProgressAndThumbColor != i) {
            if (Color.alpha(i) != 255) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Volume slider progress and thumb color cannot be translucent: #");
                m.append(Integer.toHexString(i));
                Log.e("MediaRouteVolumeSlider", m.toString());
            }
            this.mProgressAndThumbColor = i;
        }
        if (this.mBackgroundColor != i2) {
            if (Color.alpha(i2) != 255) {
                StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Volume slider background color cannot be translucent: #");
                m2.append(Integer.toHexString(i2));
                Log.e("MediaRouteVolumeSlider", m2.toString());
            }
            this.mBackgroundColor = i2;
        }
    }

    public final void setHideThumb(boolean z) {
        Drawable drawable;
        if (this.mHideThumb != z) {
            this.mHideThumb = z;
            if (z) {
                drawable = null;
            } else {
                drawable = this.mThumb;
            }
            super.setThumb(drawable);
        }
    }

    @Override // android.widget.AbsSeekBar
    public final void setThumb(Drawable drawable) {
        this.mThumb = drawable;
        if (this.mHideThumb) {
            drawable = null;
        }
        super.setThumb(drawable);
    }

    public MediaRouteVolumeSlider(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mDisabledAlpha = MediaRouterThemeHelper.getDisabledAlpha(context);
    }

    @Override // androidx.appcompat.widget.AppCompatSeekBar, android.widget.AbsSeekBar, android.widget.ProgressBar, android.view.View
    public final void drawableStateChanged() {
        int i;
        super.drawableStateChanged();
        if (isEnabled()) {
            i = 255;
        } else {
            i = (int) (this.mDisabledAlpha * 255.0f);
        }
        this.mThumb.setColorFilter(this.mProgressAndThumbColor, PorterDuff.Mode.SRC_IN);
        this.mThumb.setAlpha(i);
        Drawable progressDrawable = getProgressDrawable();
        if (progressDrawable instanceof LayerDrawable) {
            LayerDrawable layerDrawable = (LayerDrawable) getProgressDrawable();
            Drawable findDrawableByLayerId = layerDrawable.findDrawableByLayerId(16908301);
            layerDrawable.findDrawableByLayerId(16908288).setColorFilter(this.mBackgroundColor, PorterDuff.Mode.SRC_IN);
            progressDrawable = findDrawableByLayerId;
        }
        progressDrawable.setColorFilter(this.mProgressAndThumbColor, PorterDuff.Mode.SRC_IN);
        progressDrawable.setAlpha(i);
    }
}
