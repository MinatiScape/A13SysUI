package com.android.systemui.charging;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.settingslib.Utils;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.charging.ChargingRippleView;
import com.android.systemui.statusbar.charging.RippleShader;
import java.text.NumberFormat;
import java.util.Objects;
/* loaded from: classes.dex */
public class WirelessChargingLayout extends FrameLayout {
    public ChargingRippleView mRippleView;

    public WirelessChargingLayout(Context context, int i, int i2) {
        super(context);
        init(context, i, i2);
    }

    public final void init(Context context, int i, int i2) {
        boolean z;
        float f;
        if (i != -1) {
            z = true;
        } else {
            z = false;
        }
        View.inflate(new ContextThemeWrapper(context, 2132017467), 2131624658, this);
        TextView textView = (TextView) findViewById(2131429273);
        if (i2 != -1) {
            textView.setText(NumberFormat.getPercentInstance().format(i2 / 100.0f));
            textView.setAlpha(0.0f);
        }
        long integer = context.getResources().getInteger(2131493054);
        long integer2 = context.getResources().getInteger(2131493053);
        float f2 = context.getResources().getFloat(2131167338);
        float f3 = context.getResources().getFloat(2131167337);
        if (z) {
            f = 0.75f;
        } else {
            f = 1.0f;
        }
        float f4 = f3 * f;
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(textView, "textSize", f2, f4);
        ofFloat.setInterpolator(new PathInterpolator(0.0f, 0.0f, 0.0f, 1.0f));
        ofFloat.setDuration(context.getResources().getInteger(2131493052));
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(textView, "alpha", 0.0f, 1.0f);
        LinearInterpolator linearInterpolator = Interpolators.LINEAR;
        ofFloat2.setInterpolator(linearInterpolator);
        ofFloat2.setDuration(context.getResources().getInteger(2131493051));
        ofFloat2.setStartDelay(context.getResources().getInteger(2131493050));
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(textView, "alpha", 1.0f, 0.0f);
        ofFloat3.setDuration(integer2);
        ofFloat3.setInterpolator(linearInterpolator);
        ofFloat3.setStartDelay(integer);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ofFloat, ofFloat2, ofFloat3);
        ObjectAnimator ofArgb = ObjectAnimator.ofArgb(this, "backgroundColor", 0, 1275068416);
        ofArgb.setDuration(300L);
        ofArgb.setInterpolator(linearInterpolator);
        ObjectAnimator ofArgb2 = ObjectAnimator.ofArgb(this, "backgroundColor", 1275068416, 0);
        ofArgb2.setDuration(300L);
        ofArgb2.setInterpolator(linearInterpolator);
        ofArgb2.setStartDelay(1200L);
        AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.playTogether(ofArgb, ofArgb2);
        animatorSet2.start();
        ChargingRippleView chargingRippleView = (ChargingRippleView) findViewById(2131429274);
        this.mRippleView = chargingRippleView;
        chargingRippleView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.android.systemui.charging.WirelessChargingLayout.1
            @Override // android.view.View.OnAttachStateChangeListener
            public final void onViewDetachedFromWindow(View view) {
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public final void onViewAttachedToWindow(View view) {
                ChargingRippleView chargingRippleView2 = WirelessChargingLayout.this.mRippleView;
                Objects.requireNonNull(chargingRippleView2);
                chargingRippleView2.duration = 1500L;
                ChargingRippleView chargingRippleView3 = WirelessChargingLayout.this.mRippleView;
                Objects.requireNonNull(chargingRippleView3);
                chargingRippleView3.startRipple(null);
                WirelessChargingLayout.this.mRippleView.removeOnAttachStateChangeListener(this);
            }
        });
        if (!z) {
            animatorSet.start();
            return;
        }
        TextView textView2 = (TextView) findViewById(2131428702);
        textView2.setVisibility(0);
        textView2.setText(NumberFormat.getPercentInstance().format(i / 100.0f));
        textView2.setAlpha(0.0f);
        ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(textView2, "textSize", f2, f4);
        ofFloat4.setInterpolator(new PathInterpolator(0.0f, 0.0f, 0.0f, 1.0f));
        ofFloat4.setDuration(context.getResources().getInteger(2131493052));
        ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat(textView2, "alpha", 0.0f, 1.0f);
        ofFloat5.setInterpolator(linearInterpolator);
        ofFloat5.setDuration(context.getResources().getInteger(2131493051));
        ofFloat5.setStartDelay(context.getResources().getInteger(2131493050));
        ObjectAnimator ofFloat6 = ObjectAnimator.ofFloat(textView2, "alpha", 1.0f, 0.0f);
        ofFloat6.setDuration(integer2);
        ofFloat6.setInterpolator(linearInterpolator);
        ofFloat6.setStartDelay(integer);
        AnimatorSet animatorSet3 = new AnimatorSet();
        animatorSet3.playTogether(ofFloat4, ofFloat5, ofFloat6);
        ImageView imageView = (ImageView) findViewById(2131428701);
        imageView.setVisibility(0);
        int round = Math.round(TypedValue.applyDimension(1, f4, getResources().getDisplayMetrics()));
        imageView.setPadding(round, 0, round, 0);
        ObjectAnimator ofFloat7 = ObjectAnimator.ofFloat(imageView, "alpha", 0.0f, 1.0f);
        ofFloat7.setInterpolator(linearInterpolator);
        ofFloat7.setDuration(context.getResources().getInteger(2131493051));
        ofFloat7.setStartDelay(context.getResources().getInteger(2131493050));
        ObjectAnimator ofFloat8 = ObjectAnimator.ofFloat(imageView, "alpha", 1.0f, 0.0f);
        ofFloat8.setDuration(integer2);
        ofFloat8.setInterpolator(linearInterpolator);
        ofFloat8.setStartDelay(integer);
        AnimatorSet animatorSet4 = new AnimatorSet();
        animatorSet4.playTogether(ofFloat7, ofFloat8);
        animatorSet.start();
        animatorSet3.start();
        animatorSet4.start();
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.mRippleView != null) {
            int measuredWidth = getMeasuredWidth();
            int measuredHeight = getMeasuredHeight();
            ChargingRippleView chargingRippleView = this.mRippleView;
            chargingRippleView.rippleShader.setColor(Utils.getColorAttr(chargingRippleView.getContext(), 16843829).getDefaultColor());
            ChargingRippleView chargingRippleView2 = this.mRippleView;
            PointF pointF = new PointF(measuredWidth / 2, measuredHeight / 2);
            Objects.requireNonNull(chargingRippleView2);
            RippleShader rippleShader = chargingRippleView2.rippleShader;
            Objects.requireNonNull(rippleShader);
            rippleShader.setFloatUniform("in_origin", pointF.x, pointF.y);
            chargingRippleView2.origin = pointF;
            ChargingRippleView chargingRippleView3 = this.mRippleView;
            float max = Math.max(measuredWidth, measuredHeight) * 0.5f;
            Objects.requireNonNull(chargingRippleView3);
            RippleShader rippleShader2 = chargingRippleView3.rippleShader;
            Objects.requireNonNull(rippleShader2);
            rippleShader2.radius = max;
            rippleShader2.setFloatUniform("in_maxRadius", max);
            chargingRippleView3.radius = max;
        }
        super.onLayout(z, i, i2, i3, i4);
    }

    public WirelessChargingLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, -1, -1);
    }
}
