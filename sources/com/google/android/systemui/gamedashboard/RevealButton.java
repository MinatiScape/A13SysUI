package com.google.android.systemui.gamedashboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.IntProperty;
import android.view.View;
import com.google.android.systemui.gamedashboard.RevealButton;
import java.util.Objects;
@SuppressLint({"AppCompatCustomView"})
/* loaded from: classes.dex */
public class RevealButton extends View {
    public final RevealButtonBackground mBackground;
    public final Drawable mLeftArrow;
    public final Drawable mLeftRecordingDot;
    public final Drawable mRightArrow;
    public final Drawable mRightRecordingDot;
    public ValueAnimator mValueAnimator;
    public static final AnonymousClass1 BACKGROUND_WIDTH = new IntProperty<RevealButton>() { // from class: com.google.android.systemui.gamedashboard.RevealButton.1
        @Override // android.util.Property
        public final Integer get(Object obj) {
            RevealButton revealButton = (RevealButton) obj;
            AnonymousClass1 r0 = RevealButton.BACKGROUND_WIDTH;
            Objects.requireNonNull(revealButton);
            return Integer.valueOf(revealButton.mBackground.getBounds().width());
        }

        @Override // android.util.IntProperty
        public final void setValue(RevealButton revealButton, int i) {
            RevealButton revealButton2 = revealButton;
            AnonymousClass1 r2 = RevealButton.BACKGROUND_WIDTH;
            Objects.requireNonNull(revealButton2);
            Rect bounds = revealButton2.mBackground.getBounds();
            RevealButtonBackground revealButtonBackground = revealButton2.mBackground;
            int i2 = bounds.left;
            revealButtonBackground.setBounds(i2, bounds.top, i + i2, bounds.bottom);
        }
    };
    public static final AnonymousClass2 BACKGROUND_HEIGHT = new IntProperty<RevealButton>() { // from class: com.google.android.systemui.gamedashboard.RevealButton.2
        @Override // android.util.Property
        public final Integer get(Object obj) {
            RevealButton revealButton = (RevealButton) obj;
            AnonymousClass1 r0 = RevealButton.BACKGROUND_WIDTH;
            Objects.requireNonNull(revealButton);
            return Integer.valueOf(revealButton.mBackground.getBounds().height());
        }

        @Override // android.util.IntProperty
        public final void setValue(RevealButton revealButton, int i) {
            RevealButton revealButton2 = revealButton;
            AnonymousClass1 r2 = RevealButton.BACKGROUND_WIDTH;
            Objects.requireNonNull(revealButton2);
            Rect bounds = revealButton2.mBackground.getBounds();
            int i2 = i / 2;
            revealButton2.mBackground.setBounds(bounds.left, bounds.centerY() - i2, bounds.right, bounds.centerY() + i2);
        }
    };
    public static final AnonymousClass3 ICON_ALPHA = new IntProperty<RevealButton>() { // from class: com.google.android.systemui.gamedashboard.RevealButton.3
        @Override // android.util.Property
        public final Integer get(Object obj) {
            RevealButton revealButton = (RevealButton) obj;
            AnonymousClass1 r0 = RevealButton.BACKGROUND_WIDTH;
            Objects.requireNonNull(revealButton);
            return Integer.valueOf(revealButton.mIconAlpha);
        }

        @Override // android.util.IntProperty
        public final void setValue(RevealButton revealButton, int i) {
            RevealButton revealButton2 = revealButton;
            AnonymousClass1 r0 = RevealButton.BACKGROUND_WIDTH;
            Objects.requireNonNull(revealButton2);
            revealButton2.mIconAlpha = i;
            revealButton2.invalidate();
        }
    };
    public boolean mRightSide = true;
    public boolean mIsRecording = false;
    public int mIconAlpha = 255;

    public final void bounce(boolean z) {
        float f;
        ValueAnimator valueAnimator = this.mValueAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        final Rect copyBounds = this.mBackground.copyBounds();
        final int width = copyBounds.width();
        final int height = copyBounds.height();
        float[] fArr = new float[3];
        fArr[0] = 1.0f;
        if (z) {
            f = 1.25f;
        } else {
            f = 0.75f;
        }
        fArr[1] = f;
        fArr[2] = 1.0f;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
        this.mValueAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.systemui.gamedashboard.RevealButton$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                RevealButton revealButton = RevealButton.this;
                int i = width;
                Rect rect = copyBounds;
                int i2 = height;
                RevealButton.AnonymousClass1 r3 = RevealButton.BACKGROUND_WIDTH;
                Objects.requireNonNull(revealButton);
                float floatValue = (((Float) valueAnimator2.getAnimatedValue()).floatValue() * i) / 2.0f;
                int centerX = (int) (rect.centerX() + floatValue);
                float f2 = i2 / 2.0f;
                revealButton.mBackground.setBounds((int) (rect.centerX() - floatValue), (int) (rect.centerY() - f2), centerX, (int) (rect.centerY() + f2));
            }
        });
        this.mValueAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.gamedashboard.RevealButton.4
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationCancel(Animator animator) {
                super.onAnimationCancel(animator);
                RevealButton.this.mBackground.setBounds(copyBounds);
            }
        });
        this.mValueAnimator.setDuration(300L);
        this.mValueAnimator.start();
    }

    public final void drawDrawable(Drawable drawable, Canvas canvas, int i, int i2, int i3, int i4) {
        int i5 = i / 2;
        drawable.setBounds(0, 0, i5, i5);
        canvas.translate(i3, ((i2 - drawable.getBounds().width()) / 2) + i4);
        drawable.setAlpha(this.mIconAlpha);
        drawable.draw(canvas);
    }

    public RevealButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        getContext();
        RevealButtonBackground revealButtonBackground = new RevealButtonBackground();
        this.mBackground = revealButtonBackground;
        setWillNotDraw(false);
        Resources resources = getResources();
        Resources.Theme theme = context.getTheme();
        this.mLeftArrow = resources.getDrawable(2131232331, theme);
        this.mRightArrow = resources.getDrawable(2131232332, theme);
        this.mLeftRecordingDot = resources.getDrawable(2131232241, theme);
        this.mRightRecordingDot = resources.getDrawable(2131232242, theme);
        setBackground(revealButtonBackground);
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        Drawable drawable;
        Drawable drawable2;
        super.onDraw(canvas);
        canvas.save();
        Rect bounds = this.mBackground.getBounds();
        int width = bounds.width();
        int height = bounds.height();
        if (this.mRightSide) {
            if (this.mIsRecording) {
                drawable2 = this.mLeftRecordingDot;
            } else {
                drawable2 = this.mLeftArrow;
            }
            drawDrawable(drawable2, canvas, width, height, bounds.left, bounds.top);
        } else {
            if (this.mIsRecording) {
                drawable = this.mRightRecordingDot;
            } else {
                drawable = this.mRightArrow;
            }
            drawDrawable(drawable, canvas, width, height, (width / 2) + bounds.left, bounds.top);
        }
        canvas.restore();
    }

    @Override // android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mBackground.setBounds(0, 0, getWidth(), getHeight());
    }

    @Override // android.view.View
    public final boolean performClick() {
        super.performClick();
        return true;
    }
}
