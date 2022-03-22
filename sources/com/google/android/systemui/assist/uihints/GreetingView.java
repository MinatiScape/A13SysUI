package com.google.android.systemui.assist.uihints;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import androidx.leanback.widget.SearchOrbView$$ExternalSyntheticLambda0;
import com.google.android.systemui.assist.uihints.GreetingView;
import com.google.android.systemui.assist.uihints.TranscriptionController;
import com.google.common.util.concurrent.ImmediateFuture;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public class GreetingView extends TextView implements TranscriptionController.TranscriptionSpaceView {
    public final int START_DELTA;
    public final int TEXT_COLOR_DARK;
    public final int TEXT_COLOR_LIGHT;
    public AnimatorSet mAnimatorSet;
    public final SpannableStringBuilder mGreetingBuilder;
    public float mMaxAlpha;
    public final ArrayList<StaggeredSpan> mSpans;

    /* loaded from: classes.dex */
    public class StaggeredSpan extends CharacterStyle {
        public static final /* synthetic */ int $r8$clinit = 0;
        public int mShift = 0;
        public int mAlpha = 0;

        public StaggeredSpan() {
        }

        @Override // android.text.style.CharacterStyle
        public final void updateDrawState(TextPaint textPaint) {
            textPaint.baselineShift -= this.mShift;
            textPaint.setAlpha(this.mAlpha);
            GreetingView.this.invalidate();
        }
    }

    public GreetingView(Context context) {
        this(context, null);
    }

    public GreetingView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @Override // com.google.android.systemui.assist.uihints.TranscriptionController.TranscriptionSpaceView
    public final ListenableFuture<Void> hide(boolean z) {
        if (this.mAnimatorSet.isRunning()) {
            this.mAnimatorSet.cancel();
        }
        setVisibility(8);
        return ImmediateFuture.NULL;
    }

    @Override // com.google.android.systemui.assist.uihints.TranscriptionController.TranscriptionSpaceView
    public final void onFontSizeChanged() {
        setTextSize(0, ((TextView) this).mContext.getResources().getDimension(2131167244));
    }

    public final void setGreetingAnimated(String str, float f) {
        setPadding(0, 0, 0, -this.START_DELTA);
        String[] split = str.split("\\s+");
        this.mGreetingBuilder.clear();
        this.mSpans.clear();
        this.mGreetingBuilder.append((CharSequence) str);
        int length = split.length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            String str2 = split[i];
            StaggeredSpan staggeredSpan = new StaggeredSpan();
            int indexOf = str.indexOf(str2, i2);
            int length2 = str2.length() + indexOf;
            this.mGreetingBuilder.setSpan(staggeredSpan, indexOf, length2, 33);
            this.mSpans.add(staggeredSpan);
            i++;
            i2 = length2;
        }
        setText(this.mGreetingBuilder);
        float abs = Math.abs(f);
        if (this.mAnimatorSet.isRunning()) {
            Log.w("GreetingView", "Already animating in greeting view; ignoring");
            return;
        }
        this.mAnimatorSet = new AnimatorSet();
        float min = Math.min(10.0f, (abs / 1.2f) + 3.0f);
        OvershootInterpolator overshootInterpolator = new OvershootInterpolator(min);
        long j = 0;
        Iterator<StaggeredSpan> it = this.mSpans.iterator();
        while (it.hasNext()) {
            final StaggeredSpan next = it.next();
            AnimatorSet animatorSet = this.mAnimatorSet;
            Objects.requireNonNull(next);
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat.setInterpolator(overshootInterpolator);
            ofFloat.addUpdateListener(new SearchOrbView$$ExternalSyntheticLambda0(next, 1));
            ofFloat.setDuration(400L);
            ofFloat.setStartDelay(j);
            animatorSet.play(ofFloat);
            ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.systemui.assist.uihints.GreetingView$StaggeredSpan$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    GreetingView.StaggeredSpan staggeredSpan2 = GreetingView.StaggeredSpan.this;
                    Objects.requireNonNull(staggeredSpan2);
                    staggeredSpan2.mAlpha = (int) (((Float) valueAnimator.getAnimatedValue()).floatValue() * GreetingView.this.mMaxAlpha);
                }
            });
            ofFloat2.setDuration(100L);
            ofFloat2.setStartDelay(j);
            animatorSet.play(ofFloat2);
            j += 8;
        }
        float applyDimension = (int) TypedValue.applyDimension(2, getResources().getDimension(2131167244), ((TextView) this).mContext.getResources().getDisplayMetrics());
        float interpolation = overshootInterpolator.getInterpolation(((2.0f * min) + 6.0f) / ((min * 6.0f) + 6.0f)) * this.START_DELTA;
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            layoutParams.height = (int) (interpolation + applyDimension);
        }
        setVisibility(0);
        requestLayout();
        this.mAnimatorSet.start();
    }

    @Override // com.google.android.systemui.assist.uihints.TranscriptionController.TranscriptionSpaceView
    public final void setHasDarkBackground(boolean z) {
        int i;
        if (z) {
            i = this.TEXT_COLOR_DARK;
        } else {
            i = this.TEXT_COLOR_LIGHT;
        }
        setTextColor(i);
        this.mMaxAlpha = Color.alpha(getCurrentTextColor());
    }

    public GreetingView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public GreetingView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mGreetingBuilder = new SpannableStringBuilder();
        this.mSpans = new ArrayList<>();
        this.mAnimatorSet = new AnimatorSet();
        this.TEXT_COLOR_DARK = getResources().getColor(2131100753);
        this.TEXT_COLOR_LIGHT = getResources().getColor(2131100754);
        this.START_DELTA = (int) getResources().getDimension(2131165343);
        this.mMaxAlpha = Color.alpha(getCurrentTextColor());
    }
}
