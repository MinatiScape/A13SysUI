package com.google.android.systemui.gamedashboard;

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.android.settingslib.Utils;
import com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda4;
import com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda0;
/* loaded from: classes.dex */
public class GameDashboardButton extends ImageView {
    public static final /* synthetic */ int $r8$clinit = 0;
    public ValueAnimator mAnimator;
    public final BlendModeColorFilter mBackgroundColorFilter;
    public final BlendModeColorFilter mBackgroundColorFilterToggled;
    public final GradientDrawable mBackgroundDrawable;
    public final PorterDuffColorFilter mDrawableColorFilter;
    public final PorterDuffColorFilter mDrawableColorFilterToggled;
    public boolean mToggled;

    public final void refresh(boolean z) {
        BlendModeColorFilter blendModeColorFilter;
        float f;
        PorterDuffColorFilter porterDuffColorFilter;
        ValueAnimator valueAnimator;
        GradientDrawable gradientDrawable = this.mBackgroundDrawable;
        if (this.mToggled) {
            blendModeColorFilter = this.mBackgroundColorFilterToggled;
        } else {
            blendModeColorFilter = this.mBackgroundColorFilter;
        }
        gradientDrawable.setColorFilter(blendModeColorFilter);
        if (this.mToggled) {
            f = 0.5f;
        } else {
            f = 0.36f;
        }
        float width = f * getWidth();
        if (z && (valueAnimator = this.mAnimator) != null && valueAnimator.isRunning()) {
            this.mAnimator.cancel();
        }
        if (z) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.mBackgroundDrawable.getCornerRadius(), width);
            this.mAnimator = ofFloat;
            ofFloat.addUpdateListener(new ScreenshotView$$ExternalSyntheticLambda0(this, 2));
            this.mAnimator.setDuration(200L);
            this.mAnimator.setEvaluator(new FloatEvaluator());
            this.mAnimator.start();
        } else {
            this.mBackgroundDrawable.setCornerRadius(width);
        }
        Drawable drawable = getDrawable();
        if (drawable != null) {
            if (this.mToggled) {
                porterDuffColorFilter = this.mDrawableColorFilterToggled;
            } else {
                porterDuffColorFilter = this.mDrawableColorFilter;
            }
            drawable.setColorFilter(porterDuffColorFilter);
        }
    }

    @Override // android.view.View
    public final void setOnClickListener(View.OnClickListener onClickListener) {
        super.setOnClickListener(new MediaControlPanel$$ExternalSyntheticLambda4(this, onClickListener, 1));
    }

    public final void setToggled(boolean z, boolean z2) {
        this.mToggled = z;
        refresh(z2);
        String string = ((ImageView) this).mContext.getString(2131951748);
        String string2 = ((ImageView) this).mContext.getString(2131951747);
        if (!this.mToggled) {
            string = string2;
        }
        setStateDescription(string);
    }

    public GameDashboardButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(context, 17956900);
        int colorAttrDefaultColor2 = Utils.getColorAttrDefaultColor(context, 17956912);
        int colorAttrDefaultColor3 = Utils.getColorAttrDefaultColor(context, 16842806);
        int colorAttrDefaultColor4 = Utils.getColorAttrDefaultColor(context, 17957103);
        GradientDrawable gradientDrawable = (GradientDrawable) context.getDrawable(2131232628).mutate();
        this.mBackgroundDrawable = gradientDrawable;
        BlendModeColorFilter blendModeColorFilter = new BlendModeColorFilter(colorAttrDefaultColor2, BlendMode.SRC_ATOP);
        this.mBackgroundColorFilter = blendModeColorFilter;
        gradientDrawable.setColorFilter(blendModeColorFilter);
        this.mBackgroundColorFilterToggled = new BlendModeColorFilter(colorAttrDefaultColor, BlendMode.SRC_ATOP);
        this.mDrawableColorFilter = new PorterDuffColorFilter(colorAttrDefaultColor3, PorterDuff.Mode.SRC_ATOP);
        this.mDrawableColorFilterToggled = new PorterDuffColorFilter(colorAttrDefaultColor4, PorterDuff.Mode.SRC_ATOP);
        setBackground(gradientDrawable);
    }

    @Override // android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        refresh(false);
    }

    @Override // android.widget.ImageView
    public final void setImageDrawable(Drawable drawable) {
        PorterDuffColorFilter porterDuffColorFilter;
        super.setImageDrawable(drawable);
        Drawable drawable2 = getDrawable();
        if (drawable2 != null) {
            if (this.mToggled) {
                porterDuffColorFilter = this.mDrawableColorFilterToggled;
            } else {
                porterDuffColorFilter = this.mDrawableColorFilter;
            }
            drawable2.setColorFilter(porterDuffColorFilter);
        }
    }
}
