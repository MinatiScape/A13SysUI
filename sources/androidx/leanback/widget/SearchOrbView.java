package androidx.leanback.widget;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.leanback.R$styleable;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class SearchOrbView extends FrameLayout implements View.OnClickListener {
    public static final /* synthetic */ int $r8$clinit = 0;
    public boolean mAttachedToWindow;
    public boolean mColorAnimationEnabled;
    public ValueAnimator mColorAnimator;
    public final ArgbEvaluator mColorEvaluator;
    public Colors mColors;
    public final SearchOrbView$$ExternalSyntheticLambda0 mFocusUpdateListener;
    public final float mFocusedZ;
    public final float mFocusedZoom;
    public final ImageView mIcon;
    public View.OnClickListener mListener;
    public final int mPulseDurationMs;
    public final View mRootView;
    public final int mScaleDurationMs;
    public final View mSearchOrbView;
    public ValueAnimator mShadowFocusAnimator;
    public final float mUnfocusedZ;
    public final SearchOrbView$$ExternalSyntheticLambda1 mUpdateListener;

    public SearchOrbView(Context context) {
        this(context, null);
    }

    public int getLayoutResourceId() {
        return 2131624228;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        this.mAttachedToWindow = false;
        updateColorAnimator();
        super.onDetachedFromWindow();
    }

    /* loaded from: classes.dex */
    public static class Colors {
        public int brightColor;
        public int color;
        public int iconColor;

        public Colors(int i, int i2, int i3) {
            this.color = i;
            if (i2 == i) {
                i2 = Color.argb((int) ((Color.alpha(i) * 0.85f) + 38.25f), (int) ((Color.red(i) * 0.85f) + 38.25f), (int) ((Color.green(i) * 0.85f) + 38.25f), (int) ((Color.blue(i) * 0.85f) + 38.25f));
            }
            this.brightColor = i2;
            this.iconColor = i3;
        }
    }

    public SearchOrbView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 2130969694);
    }

    public final void animateOnFocus(boolean z) {
        float f;
        if (z) {
            f = this.mFocusedZoom;
        } else {
            f = 1.0f;
        }
        this.mRootView.animate().scaleX(f).scaleY(f).setDuration(this.mScaleDurationMs).start();
        int i = this.mScaleDurationMs;
        if (this.mShadowFocusAnimator == null) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.mShadowFocusAnimator = ofFloat;
            ofFloat.addUpdateListener(this.mFocusUpdateListener);
        }
        if (z) {
            this.mShadowFocusAnimator.start();
        } else {
            this.mShadowFocusAnimator.reverse();
        }
        this.mShadowFocusAnimator.setDuration(i);
        this.mColorAnimationEnabled = z;
        updateColorAnimator();
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        View.OnClickListener onClickListener = this.mListener;
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
    }

    public final void updateColorAnimator() {
        ValueAnimator valueAnimator = this.mColorAnimator;
        if (valueAnimator != null) {
            valueAnimator.end();
            this.mColorAnimator = null;
        }
        if (this.mColorAnimationEnabled && this.mAttachedToWindow) {
            ValueAnimator ofObject = ValueAnimator.ofObject(this.mColorEvaluator, Integer.valueOf(this.mColors.color), Integer.valueOf(this.mColors.brightColor), Integer.valueOf(this.mColors.color));
            this.mColorAnimator = ofObject;
            ofObject.setRepeatCount(-1);
            this.mColorAnimator.setDuration(this.mPulseDurationMs * 2);
            this.mColorAnimator.addUpdateListener(this.mUpdateListener);
            this.mColorAnimator.start();
        }
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [androidx.leanback.widget.SearchOrbView$$ExternalSyntheticLambda1] */
    @SuppressLint({"CustomViewStyleable"})
    public SearchOrbView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mColorEvaluator = new ArgbEvaluator();
        this.mUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: androidx.leanback.widget.SearchOrbView$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                SearchOrbView searchOrbView = SearchOrbView.this;
                int i2 = SearchOrbView.$r8$clinit;
                Objects.requireNonNull(searchOrbView);
                int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                if (searchOrbView.mSearchOrbView.getBackground() instanceof GradientDrawable) {
                    ((GradientDrawable) searchOrbView.mSearchOrbView.getBackground()).setColor(intValue);
                }
            }
        };
        this.mFocusUpdateListener = new SearchOrbView$$ExternalSyntheticLambda0(this, 0);
        Resources resources = context.getResources();
        View inflate = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(getLayoutResourceId(), (ViewGroup) this, true);
        this.mRootView = inflate;
        View findViewById = inflate.findViewById(2131428806);
        this.mSearchOrbView = findViewById;
        ImageView imageView = (ImageView) inflate.findViewById(2131428102);
        this.mIcon = imageView;
        this.mFocusedZoom = context.getResources().getFraction(2131361811, 1, 1);
        this.mPulseDurationMs = context.getResources().getInteger(2131492975);
        this.mScaleDurationMs = context.getResources().getInteger(2131492976);
        float dimensionPixelSize = context.getResources().getDimensionPixelSize(2131166107);
        this.mFocusedZ = dimensionPixelSize;
        float dimensionPixelSize2 = context.getResources().getDimensionPixelSize(2131166113);
        this.mUnfocusedZ = dimensionPixelSize2;
        int[] iArr = R$styleable.lbSearchOrbView;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr, i, 0);
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        ViewCompat.Api29Impl.saveAttributeDataForStyleable(this, context, iArr, attributeSet, obtainStyledAttributes, i, 0);
        Drawable drawable = obtainStyledAttributes.getDrawable(2);
        imageView.setImageDrawable(drawable == null ? resources.getDrawable(2131232369) : drawable);
        int color = obtainStyledAttributes.getColor(1, resources.getColor(2131099913));
        Colors colors = new Colors(color, obtainStyledAttributes.getColor(0, color), obtainStyledAttributes.getColor(3, 0));
        this.mColors = colors;
        imageView.setColorFilter(colors.iconColor);
        if (this.mColorAnimator == null) {
            int i2 = this.mColors.color;
            if (findViewById.getBackground() instanceof GradientDrawable) {
                ((GradientDrawable) findViewById.getBackground()).setColor(i2);
            }
        } else {
            this.mColorAnimationEnabled = true;
            updateColorAnimator();
        }
        obtainStyledAttributes.recycle();
        setFocusable(true);
        setClipChildren(false);
        setOnClickListener(this);
        setSoundEffectsEnabled(false);
        ViewCompat.Api21Impl.setZ(findViewById, ((dimensionPixelSize - dimensionPixelSize2) * 0.0f) + dimensionPixelSize2);
        ViewCompat.Api21Impl.setZ(imageView, dimensionPixelSize);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mAttachedToWindow = true;
        updateColorAnimator();
    }

    @Override // android.view.View
    public final void onFocusChanged(boolean z, int i, Rect rect) {
        super.onFocusChanged(z, i, rect);
        animateOnFocus(z);
    }
}
