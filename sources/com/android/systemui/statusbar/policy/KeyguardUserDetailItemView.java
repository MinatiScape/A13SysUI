package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
import com.android.keyguard.KeyguardConstants;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda6;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.qs.tiles.UserDetailItemView;
/* loaded from: classes.dex */
public class KeyguardUserDetailItemView extends UserDetailItemView {
    public static final boolean DEBUG = KeyguardConstants.DEBUG;
    public float mDarkAmount;
    public int mTextColor;

    public KeyguardUserDetailItemView(Context context) {
        this(context, null);
    }

    @Override // com.android.systemui.qs.tiles.UserDetailItemView
    public final int getFontSizeDimen() {
        return 2131165868;
    }

    public KeyguardUserDetailItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public final void updateVisibilities(boolean z, boolean z2, boolean z3) {
        int i;
        int i2 = 0;
        if (DEBUG) {
            Log.d("KeyguardUserDetailItemView", String.format("updateVisibilities itemIsShown=%b nameIsShown=%b animate=%b", Boolean.valueOf(z), Boolean.valueOf(z2), Boolean.valueOf(z3)));
        }
        Drawable background = getBackground();
        if (!z || !z2) {
            i = 0;
        } else {
            i = 255;
        }
        background.setAlpha(i);
        if (z) {
            if (z2) {
                this.mName.setVisibility(0);
                if (z3) {
                    this.mName.setAlpha(0.0f);
                    this.mName.animate().alpha(1.0f).setDuration(240L).setInterpolator(Interpolators.ALPHA_IN);
                } else {
                    this.mName.setAlpha(1.0f);
                }
            } else if (z3) {
                this.mName.setVisibility(0);
                this.mName.setAlpha(1.0f);
                this.mName.animate().alpha(0.0f).setDuration(240L).setInterpolator(Interpolators.ALPHA_OUT).withEndAction(new KeyguardUpdateMonitor$$ExternalSyntheticLambda6(this, 8));
            } else {
                this.mName.setVisibility(8);
                this.mName.setAlpha(1.0f);
            }
            setVisibility(0);
            setAlpha(1.0f);
            return;
        }
        setVisibility(8);
        setAlpha(1.0f);
        TextView textView = this.mName;
        if (!z2) {
            i2 = 8;
        }
        textView.setVisibility(i2);
        this.mName.setAlpha(1.0f);
    }

    public KeyguardUserDetailItemView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    @Override // com.android.systemui.qs.tiles.UserDetailItemView, android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        int currentTextColor = this.mName.getCurrentTextColor();
        this.mTextColor = currentTextColor;
        this.mName.setTextColor(ColorUtils.blendARGB(currentTextColor, -1, this.mDarkAmount));
    }

    public KeyguardUserDetailItemView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }
}
