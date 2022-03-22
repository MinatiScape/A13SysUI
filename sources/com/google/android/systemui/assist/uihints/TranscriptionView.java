package com.google.android.systemui.assist.uihints;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;
import android.text.SpannableStringBuilder;
import android.text.style.ReplacementSpan;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.animation.PathInterpolator;
import android.widget.TextView;
import androidx.leanback.R$color;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.assist.DeviceConfigHelper;
import com.android.systemui.qs.tileimpl.QSTileImpl$$ExternalSyntheticLambda1;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda34;
import com.google.android.systemui.assist.uihints.TranscriptionController;
import com.google.common.util.concurrent.ImmediateFuture;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import java.util.ArrayList;
import java.util.Objects;
import kotlinx.atomicfu.AtomicFU;
/* loaded from: classes.dex */
public class TranscriptionView extends TextView implements TranscriptionController.TranscriptionSpaceView {
    public static final PathInterpolator INTERPOLATOR_SCROLL = new PathInterpolator(0.17f, 0.17f, 0.67f, 1.0f);
    public final float BUMPER_DISTANCE_END_PX;
    public final float BUMPER_DISTANCE_START_PX;
    public final float FADE_DISTANCE_END_PX;
    public final float FADE_DISTANCE_START_PX;
    public final int TEXT_COLOR_DARK;
    public final int TEXT_COLOR_LIGHT;
    public boolean mCardVisible;
    public DeviceConfigHelper mDeviceConfigHelper;
    public int mDisplayWidthPx;
    public boolean mHasDarkBackground;
    public SettableFuture<Void> mHideFuture;
    public Matrix mMatrix;
    public int mRequestedTextColor;
    public float[] mStops;
    public ValueAnimator mTranscriptionAnimation;
    public TranscriptionAnimator mTranscriptionAnimator;
    public SpannableStringBuilder mTranscriptionBuilder;
    public AnimatorSet mVisibilityAnimators;

    /* loaded from: classes.dex */
    public class TranscriptionAnimator implements ValueAnimator.AnimatorUpdateListener {
        public float mDistance;
        public ArrayList mSpans = new ArrayList();
        public float mStartX;

        public TranscriptionAnimator() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            if (Math.abs(floatValue - this.mStartX) < Math.abs(this.mDistance)) {
                TranscriptionView.this.setX(floatValue);
                TranscriptionView transcriptionView = TranscriptionView.this;
                PathInterpolator pathInterpolator = TranscriptionView.INTERPOLATOR_SCROLL;
                transcriptionView.updateColor();
            }
            this.mSpans.forEach(new StatusBar$$ExternalSyntheticLambda34(valueAnimator, 4));
            TranscriptionView.this.invalidate();
        }
    }

    /* loaded from: classes.dex */
    public class TranscriptionSpan extends ReplacementSpan {
        public float mCurrentFraction;
        public float mStartFraction;

        public TranscriptionSpan() {
            this.mCurrentFraction = 0.0f;
            this.mStartFraction = 0.0f;
        }

        @Override // android.text.style.ReplacementSpan
        public final void draw(Canvas canvas, CharSequence charSequence, int i, int i2, float f, int i3, int i4, int i5, Paint paint) {
            float f2 = this.mStartFraction;
            float f3 = 1.0f;
            if (f2 != 1.0f) {
                f3 = AtomicFU.clamp((((1.0f - f2) / 1.0f) * this.mCurrentFraction) + f2, 0.0f, 1.0f);
            }
            paint.setAlpha((int) Math.ceil(f3 * 255.0f));
            canvas.drawText(charSequence, i, i2, f, i4, paint);
        }

        @Override // android.text.style.ReplacementSpan
        public final int getSize(Paint paint, CharSequence charSequence, int i, int i2, Paint.FontMetricsInt fontMetricsInt) {
            return (int) Math.ceil(TranscriptionView.this.getPaint().measureText(charSequence, 0, charSequence.length()));
        }

        public TranscriptionSpan(TranscriptionSpan transcriptionSpan) {
            this.mCurrentFraction = 0.0f;
            this.mStartFraction = 0.0f;
            Objects.requireNonNull(transcriptionSpan);
            this.mStartFraction = AtomicFU.clamp(transcriptionSpan.mCurrentFraction, 0.0f, 1.0f);
        }
    }

    public TranscriptionView(Context context) {
        this(context, null);
    }

    @VisibleForTesting
    public static float interpolate(long j, long j2, float f) {
        return (((float) (j2 - j)) * f) + ((float) j);
    }

    public TranscriptionView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @VisibleForTesting
    public long getAdaptiveDuration(float f, float f2) {
        Objects.requireNonNull(this.mDeviceConfigHelper);
        long j = DeviceConfigHelper.getLong("assist_transcription_duration_per_px_regular", 4L);
        Objects.requireNonNull(this.mDeviceConfigHelper);
        float interpolate = interpolate(j, DeviceConfigHelper.getLong("assist_transcription_duration_per_px_fast", 3L), f / f2);
        Objects.requireNonNull(this.mDeviceConfigHelper);
        long j2 = DeviceConfigHelper.getLong("assist_transcription_max_duration", 400L);
        Objects.requireNonNull(this.mDeviceConfigHelper);
        return Math.min(j2, Math.max(DeviceConfigHelper.getLong("assist_transcription_min_duration", 20L), f * interpolate));
    }

    public final float getFullyVisibleDistance(float f) {
        int i = this.mDisplayWidthPx;
        float f2 = this.BUMPER_DISTANCE_END_PX;
        float f3 = this.FADE_DISTANCE_END_PX;
        if (f < i - (((this.BUMPER_DISTANCE_START_PX + f2) + f3) + this.FADE_DISTANCE_START_PX)) {
            return (i - f) / 2.0f;
        }
        return ((i - f) - f3) - f2;
    }

    @Override // com.google.android.systemui.assist.uihints.TranscriptionController.TranscriptionSpaceView
    public final ListenableFuture<Void> hide(boolean z) {
        SettableFuture<Void> settableFuture = this.mHideFuture;
        if (settableFuture != null && !settableFuture.isDone()) {
            return this.mHideFuture;
        }
        this.mHideFuture = new SettableFuture<>();
        final QSTileImpl$$ExternalSyntheticLambda1 qSTileImpl$$ExternalSyntheticLambda1 = new QSTileImpl$$ExternalSyntheticLambda1(this, 6);
        if (!z) {
            if (this.mVisibilityAnimators.isRunning()) {
                this.mVisibilityAnimators.end();
            } else {
                qSTileImpl$$ExternalSyntheticLambda1.run();
            }
            return ImmediateFuture.NULL;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        this.mVisibilityAnimators = animatorSet;
        animatorSet.play(ObjectAnimator.ofFloat(this, View.ALPHA, getAlpha(), 0.0f).setDuration(400L));
        if (!this.mCardVisible) {
            this.mVisibilityAnimators.play(ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, getTranslationY(), getHeight() * (-1)).setDuration(700L));
        }
        this.mVisibilityAnimators.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.uihints.TranscriptionView.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                qSTileImpl$$ExternalSyntheticLambda1.run();
            }
        });
        this.mVisibilityAnimators.start();
        return this.mHideFuture;
    }

    @Override // com.google.android.systemui.assist.uihints.TranscriptionController.TranscriptionSpaceView
    public final void onFontSizeChanged() {
        setTextSize(0, ((TextView) this).mContext.getResources().getDimension(2131167244));
    }

    @Override // com.google.android.systemui.assist.uihints.TranscriptionController.TranscriptionSpaceView
    public final void setHasDarkBackground(boolean z) {
        if (z != this.mHasDarkBackground) {
            this.mHasDarkBackground = z;
            updateColor();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:47:0x00ef  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void setTranscription(java.lang.String r18) {
        /*
            Method dump skipped, instructions count: 498
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.assist.uihints.TranscriptionView.setTranscription(java.lang.String):void");
    }

    public final void setUpSpans(int i, TranscriptionSpan transcriptionSpan) {
        TranscriptionSpan transcriptionSpan2;
        TranscriptionAnimator transcriptionAnimator = this.mTranscriptionAnimator;
        Objects.requireNonNull(transcriptionAnimator);
        transcriptionAnimator.mSpans.clear();
        String spannableStringBuilder = this.mTranscriptionBuilder.toString();
        String substring = spannableStringBuilder.substring(i);
        if (substring.length() > 0) {
            int indexOf = spannableStringBuilder.indexOf(substring, i);
            int length = substring.length() + indexOf;
            if (transcriptionSpan == null) {
                transcriptionSpan2 = new TranscriptionSpan();
            } else {
                transcriptionSpan2 = new TranscriptionSpan(transcriptionSpan);
            }
            this.mTranscriptionBuilder.setSpan(transcriptionSpan2, indexOf, length, 33);
            TranscriptionAnimator transcriptionAnimator2 = this.mTranscriptionAnimator;
            Objects.requireNonNull(transcriptionAnimator2);
            transcriptionAnimator2.mSpans.add(transcriptionSpan2);
        }
        setText(this.mTranscriptionBuilder, TextView.BufferType.SPANNABLE);
        updateColor();
    }

    public final void updateColor() {
        int i = this.mRequestedTextColor;
        if (i == 0) {
            if (this.mHasDarkBackground) {
                i = this.TEXT_COLOR_DARK;
            } else {
                i = this.TEXT_COLOR_LIGHT;
            }
        }
        LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, this.mDisplayWidthPx, 0.0f, new int[]{0, i, i, 0}, this.mStops, Shader.TileMode.CLAMP);
        this.mMatrix.setTranslate(-getTranslationX(), 0.0f);
        linearGradient.setLocalMatrix(this.mMatrix);
        getPaint().setShader(linearGradient);
        invalidate();
    }

    public final void updateDisplayWidth() {
        Display defaultDisplay = R$color.getDefaultDisplay(((TextView) this).mContext);
        Point point = new Point();
        defaultDisplay.getRealSize(point);
        int i = point.x;
        this.mDisplayWidthPx = i;
        float f = this.BUMPER_DISTANCE_START_PX;
        float f2 = i;
        this.mStops = new float[]{f / f2, (f + this.FADE_DISTANCE_START_PX) / f2, ((f2 - this.FADE_DISTANCE_END_PX) - this.BUMPER_DISTANCE_END_PX) / f2, 1.0f};
        updateColor();
    }

    public TranscriptionView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    @Override // android.widget.TextView, android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        String spannableStringBuilder = this.mTranscriptionBuilder.toString();
        setTranscription("");
        this.mTranscriptionAnimator = new TranscriptionAnimator();
        setTranscription(spannableStringBuilder);
    }

    public TranscriptionView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mTranscriptionBuilder = new SpannableStringBuilder();
        this.mVisibilityAnimators = new AnimatorSet();
        this.mHideFuture = null;
        this.mHasDarkBackground = false;
        this.mCardVisible = false;
        this.mRequestedTextColor = 0;
        this.mMatrix = new Matrix();
        this.mDisplayWidthPx = 0;
        this.mTranscriptionAnimator = new TranscriptionAnimator();
        initializeDeviceConfigHelper(new DeviceConfigHelper());
        this.BUMPER_DISTANCE_START_PX = context.getResources().getDimension(2131167347) + context.getResources().getDimension(2131167345);
        this.BUMPER_DISTANCE_END_PX = context.getResources().getDimension(2131165830) + context.getResources().getDimension(2131165828);
        this.FADE_DISTANCE_START_PX = context.getResources().getDimension(2131167346);
        this.FADE_DISTANCE_END_PX = context.getResources().getDimension(2131165829) / 2.0f;
        this.TEXT_COLOR_DARK = context.getResources().getColor(2131100753);
        this.TEXT_COLOR_LIGHT = context.getResources().getColor(2131100754);
        updateDisplayWidth();
        setHasDarkBackground(!this.mHasDarkBackground);
    }

    @VisibleForTesting
    public void initializeDeviceConfigHelper(DeviceConfigHelper deviceConfigHelper) {
        this.mDeviceConfigHelper = deviceConfigHelper;
    }

    @Override // com.google.android.systemui.assist.uihints.TranscriptionController.TranscriptionSpaceView
    public final void setCardVisible(boolean z) {
        this.mCardVisible = z;
    }
}
