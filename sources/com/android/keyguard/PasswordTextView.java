package com.android.keyguard;

import android.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.EditText;
import com.android.systemui.R$styleable;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;
/* loaded from: classes.dex */
public class PasswordTextView extends View {
    public static final /* synthetic */ int $r8$clinit = 0;
    public Interpolator mAppearInterpolator;
    public int mCharPadding;
    public Stack<CharState> mCharPool;
    public Interpolator mDisappearInterpolator;
    public int mDotSize;
    public final Paint mDrawPaint;
    public final int mGravity;
    public PowerManager mPM;
    public boolean mShowPassword;
    public String mText;
    public ArrayList<CharState> mTextChars;
    public int mTextHeightRaw;
    public UserActivityListener mUserActivityListener;

    /* loaded from: classes.dex */
    public class CharState {
        public float currentDotSizeFactor;
        public float currentTextSizeFactor;
        public float currentWidthFactor;
        public boolean dotAnimationIsGrowing;
        public Animator dotAnimator;
        public boolean isDotSwapPending;
        public boolean textAnimationIsGrowing;
        public ValueAnimator textAnimator;
        public ValueAnimator textTranslateAnimator;
        public char whichChar;
        public boolean widthAnimationIsGrowing;
        public ValueAnimator widthAnimator;
        public float currentTextTranslationY = 1.0f;
        public AnonymousClass1 removeEndListener = new AnimatorListenerAdapter() { // from class: com.android.keyguard.PasswordTextView.CharState.1
            public boolean mCancelled;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationCancel(Animator animator) {
                this.mCancelled = true;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationStart(Animator animator) {
                this.mCancelled = false;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                if (!this.mCancelled) {
                    CharState charState = CharState.this;
                    PasswordTextView.this.mTextChars.remove(charState);
                    CharState charState2 = CharState.this;
                    PasswordTextView.this.mCharPool.push(charState2);
                    CharState.this.reset();
                    CharState.cancelAnimator(CharState.this.textTranslateAnimator);
                    CharState.this.textTranslateAnimator = null;
                }
            }
        };
        public AnonymousClass2 dotFinishListener = new AnimatorListenerAdapter() { // from class: com.android.keyguard.PasswordTextView.CharState.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                CharState.this.dotAnimator = null;
            }
        };
        public AnonymousClass3 textFinishListener = new AnimatorListenerAdapter() { // from class: com.android.keyguard.PasswordTextView.CharState.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                CharState.this.textAnimator = null;
            }
        };
        public AnonymousClass4 textTranslateFinishListener = new AnimatorListenerAdapter() { // from class: com.android.keyguard.PasswordTextView.CharState.4
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                CharState.this.textTranslateAnimator = null;
            }
        };
        public AnonymousClass5 widthFinishListener = new AnimatorListenerAdapter() { // from class: com.android.keyguard.PasswordTextView.CharState.5
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                CharState.this.widthAnimator = null;
            }
        };
        public AnonymousClass6 dotSizeUpdater = new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.keyguard.PasswordTextView.CharState.6
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                CharState.this.currentDotSizeFactor = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                PasswordTextView.this.invalidate();
            }
        };
        public AnonymousClass7 textSizeUpdater = new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.keyguard.PasswordTextView.CharState.7
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                boolean isCharVisibleForA11y = CharState.this.isCharVisibleForA11y();
                CharState charState = CharState.this;
                float f = charState.currentTextSizeFactor;
                charState.currentTextSizeFactor = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                if (isCharVisibleForA11y != CharState.this.isCharVisibleForA11y()) {
                    CharState charState2 = CharState.this;
                    charState2.currentTextSizeFactor = f;
                    PasswordTextView passwordTextView = PasswordTextView.this;
                    int i = PasswordTextView.$r8$clinit;
                    StringBuilder transformedText = passwordTextView.getTransformedText();
                    CharState.this.currentTextSizeFactor = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                    CharState charState3 = CharState.this;
                    int indexOf = PasswordTextView.this.mTextChars.indexOf(charState3);
                    if (indexOf >= 0) {
                        PasswordTextView.this.sendAccessibilityEventTypeViewTextChanged(transformedText, indexOf, 1, 1);
                    }
                }
                PasswordTextView.this.invalidate();
            }
        };
        public AnonymousClass8 textTranslationUpdater = new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.keyguard.PasswordTextView.CharState.8
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                CharState.this.currentTextTranslationY = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                PasswordTextView.this.invalidate();
            }
        };
        public AnonymousClass9 widthUpdater = new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.keyguard.PasswordTextView.CharState.9
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                CharState.this.currentWidthFactor = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                PasswordTextView.this.invalidate();
            }
        };
        public AnonymousClass10 dotSwapperRunnable = new Runnable() { // from class: com.android.keyguard.PasswordTextView.CharState.10
            @Override // java.lang.Runnable
            public final void run() {
                CharState charState = CharState.this;
                Objects.requireNonNull(charState);
                charState.startTextDisappearAnimation(0L);
                charState.startDotAppearAnimation(30L);
                CharState.this.isDotSwapPending = false;
            }
        };

        public final void reset() {
            this.whichChar = (char) 0;
            this.currentTextSizeFactor = 0.0f;
            this.currentDotSizeFactor = 0.0f;
            this.currentWidthFactor = 0.0f;
            cancelAnimator(this.textAnimator);
            this.textAnimator = null;
            cancelAnimator(this.dotAnimator);
            this.dotAnimator = null;
            cancelAnimator(this.widthAnimator);
            this.widthAnimator = null;
            this.currentTextTranslationY = 1.0f;
            PasswordTextView.this.removeCallbacks(this.dotSwapperRunnable);
            this.isDotSwapPending = false;
        }

        /* JADX WARN: Type inference failed for: r1v10, types: [com.android.keyguard.PasswordTextView$CharState$9] */
        /* JADX WARN: Type inference failed for: r1v11, types: [com.android.keyguard.PasswordTextView$CharState$10] */
        /* JADX WARN: Type inference failed for: r1v2, types: [com.android.keyguard.PasswordTextView$CharState$1] */
        /* JADX WARN: Type inference failed for: r1v3, types: [com.android.keyguard.PasswordTextView$CharState$2] */
        /* JADX WARN: Type inference failed for: r1v4, types: [com.android.keyguard.PasswordTextView$CharState$3] */
        /* JADX WARN: Type inference failed for: r1v5, types: [com.android.keyguard.PasswordTextView$CharState$4] */
        /* JADX WARN: Type inference failed for: r1v6, types: [com.android.keyguard.PasswordTextView$CharState$5] */
        /* JADX WARN: Type inference failed for: r1v7, types: [com.android.keyguard.PasswordTextView$CharState$6] */
        /* JADX WARN: Type inference failed for: r1v8, types: [com.android.keyguard.PasswordTextView$CharState$7] */
        /* JADX WARN: Type inference failed for: r1v9, types: [com.android.keyguard.PasswordTextView$CharState$8] */
        public CharState() {
        }

        public static void cancelAnimator(Animator animator) {
            if (animator != null) {
                animator.cancel();
            }
        }

        public final boolean isCharVisibleForA11y() {
            boolean z;
            if (this.textAnimator == null || !this.textAnimationIsGrowing) {
                z = false;
            } else {
                z = true;
            }
            if (this.currentTextSizeFactor > 0.0f || z) {
                return true;
            }
            return false;
        }

        public final void startDotAppearAnimation(long j) {
            cancelAnimator(this.dotAnimator);
            if (!PasswordTextView.this.mShowPassword) {
                ValueAnimator ofFloat = ValueAnimator.ofFloat(this.currentDotSizeFactor, 1.5f);
                ofFloat.addUpdateListener(this.dotSizeUpdater);
                ofFloat.setInterpolator(PasswordTextView.this.mAppearInterpolator);
                ofFloat.setDuration(160L);
                ValueAnimator ofFloat2 = ValueAnimator.ofFloat(1.5f, 1.0f);
                ofFloat2.addUpdateListener(this.dotSizeUpdater);
                ofFloat2.setDuration(160L);
                ofFloat2.addListener(this.dotFinishListener);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playSequentially(ofFloat, ofFloat2);
                animatorSet.setStartDelay(j);
                animatorSet.start();
                this.dotAnimator = animatorSet;
            } else {
                ValueAnimator ofFloat3 = ValueAnimator.ofFloat(this.currentDotSizeFactor, 1.0f);
                ofFloat3.addUpdateListener(this.dotSizeUpdater);
                ofFloat3.setDuration((1.0f - this.currentDotSizeFactor) * 160.0f);
                ofFloat3.addListener(this.dotFinishListener);
                ofFloat3.setStartDelay(j);
                ofFloat3.start();
                this.dotAnimator = ofFloat3;
            }
            this.dotAnimationIsGrowing = true;
        }

        public final void startRemoveAnimation(long j, long j2) {
            boolean z;
            boolean z2;
            boolean z3;
            if ((this.currentDotSizeFactor <= 0.0f || this.dotAnimator != null) && (this.dotAnimator == null || !this.dotAnimationIsGrowing)) {
                z = false;
            } else {
                z = true;
            }
            if ((this.currentTextSizeFactor <= 0.0f || this.textAnimator != null) && (this.textAnimator == null || !this.textAnimationIsGrowing)) {
                z2 = false;
            } else {
                z2 = true;
            }
            if ((this.currentWidthFactor <= 0.0f || this.widthAnimator != null) && (this.widthAnimator == null || !this.widthAnimationIsGrowing)) {
                z3 = false;
            } else {
                z3 = true;
            }
            if (z) {
                cancelAnimator(this.dotAnimator);
                ValueAnimator ofFloat = ValueAnimator.ofFloat(this.currentDotSizeFactor, 0.0f);
                ofFloat.addUpdateListener(this.dotSizeUpdater);
                ofFloat.addListener(this.dotFinishListener);
                ofFloat.setInterpolator(PasswordTextView.this.mDisappearInterpolator);
                ofFloat.setDuration(Math.min(this.currentDotSizeFactor, 1.0f) * 160.0f);
                ofFloat.setStartDelay(j);
                ofFloat.start();
                this.dotAnimator = ofFloat;
                this.dotAnimationIsGrowing = false;
            }
            if (z2) {
                startTextDisappearAnimation(j);
            }
            if (z3) {
                cancelAnimator(this.widthAnimator);
                ValueAnimator ofFloat2 = ValueAnimator.ofFloat(this.currentWidthFactor, 0.0f);
                this.widthAnimator = ofFloat2;
                ofFloat2.addUpdateListener(this.widthUpdater);
                this.widthAnimator.addListener(this.widthFinishListener);
                this.widthAnimator.addListener(this.removeEndListener);
                this.widthAnimator.setDuration(this.currentWidthFactor * 160.0f);
                this.widthAnimator.setStartDelay(j2);
                this.widthAnimator.start();
                this.widthAnimationIsGrowing = false;
            }
        }

        public final void startTextDisappearAnimation(long j) {
            cancelAnimator(this.textAnimator);
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.currentTextSizeFactor, 0.0f);
            this.textAnimator = ofFloat;
            ofFloat.addUpdateListener(this.textSizeUpdater);
            this.textAnimator.addListener(this.textFinishListener);
            this.textAnimator.setInterpolator(PasswordTextView.this.mDisappearInterpolator);
            this.textAnimator.setDuration(this.currentTextSizeFactor * 160.0f);
            this.textAnimator.setStartDelay(j);
            this.textAnimator.start();
            this.textAnimationIsGrowing = false;
        }
    }

    /* loaded from: classes.dex */
    public interface UserActivityListener {
    }

    public PasswordTextView(Context context) {
        this(context, null);
    }

    @Override // android.view.View
    public final boolean hasOverlappingRendering() {
        return false;
    }

    public PasswordTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public final Rect getCharBounds() {
        this.mDrawPaint.setTextSize(this.mTextHeightRaw * getResources().getDisplayMetrics().scaledDensity);
        Rect rect = new Rect();
        this.mDrawPaint.getTextBounds("0", 0, 1, rect);
        return rect;
    }

    public final StringBuilder getTransformedText() {
        char c;
        int size = this.mTextChars.size();
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            CharState charState = this.mTextChars.get(i);
            if (charState.dotAnimator == null || charState.dotAnimationIsGrowing) {
                if (charState.isCharVisibleForA11y()) {
                    c = charState.whichChar;
                } else {
                    c = 8226;
                }
                sb.append(c);
            }
        }
        return sb;
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        float f;
        boolean z;
        boolean z2;
        PasswordTextView passwordTextView;
        int size = this.mTextChars.size();
        Rect charBounds = getCharBounds();
        int i = charBounds.right - charBounds.left;
        int i2 = 0;
        for (int i3 = 0; i3 < size; i3++) {
            CharState charState = this.mTextChars.get(i3);
            if (i3 != 0) {
                i2 = (int) ((this.mCharPadding * charState.currentWidthFactor) + i2);
            }
            i2 = (int) ((i * charState.currentWidthFactor) + i2);
        }
        float f2 = i2;
        int i4 = this.mGravity;
        if ((i4 & 7) != 3) {
            f = (getWidth() - getPaddingRight()) - f2;
            float width = (getWidth() / 2.0f) - (f2 / 2.0f);
            if (width > 0.0f) {
                f = width;
            }
        } else if ((i4 & 8388608) == 0 || getLayoutDirection() != 1) {
            f = getPaddingLeft();
        } else {
            f = (getWidth() - getPaddingRight()) - f2;
        }
        int size2 = this.mTextChars.size();
        Rect charBounds2 = getCharBounds();
        int i5 = charBounds2.bottom - charBounds2.top;
        float paddingTop = getPaddingTop() + (((getHeight() - getPaddingBottom()) - getPaddingTop()) / 2);
        canvas.clipRect(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        float f3 = charBounds2.right - charBounds2.left;
        for (int i6 = 0; i6 < size2; i6++) {
            CharState charState2 = this.mTextChars.get(i6);
            Objects.requireNonNull(charState2);
            float f4 = charState2.currentTextSizeFactor;
            if (f4 > 0.0f) {
                z = true;
            } else {
                z = false;
            }
            if (charState2.currentDotSizeFactor > 0.0f) {
                z2 = true;
            } else {
                z2 = false;
            }
            float f5 = charState2.currentWidthFactor * f3;
            if (z) {
                float f6 = i5;
                float f7 = f6 * charState2.currentTextTranslationY * 0.8f;
                canvas.save();
                canvas.translate((f5 / 2.0f) + f, f7 + ((f6 / 2.0f) * f4) + paddingTop);
                float f8 = charState2.currentTextSizeFactor;
                canvas.scale(f8, f8);
                canvas.drawText(Character.toString(charState2.whichChar), 0.0f, 0.0f, PasswordTextView.this.mDrawPaint);
                canvas.restore();
            }
            if (z2) {
                canvas.save();
                canvas.translate((f5 / 2.0f) + f, paddingTop);
                canvas.drawCircle(0.0f, 0.0f, (passwordTextView.mDotSize / 2) * charState2.currentDotSizeFactor, PasswordTextView.this.mDrawPaint);
                canvas.restore();
            }
            f += (PasswordTextView.this.mCharPadding * charState2.currentWidthFactor) + f5;
        }
    }

    public final void sendAccessibilityEventTypeViewTextChanged(StringBuilder sb, int i, int i2, int i3) {
        if (!AccessibilityManager.getInstance(((View) this).mContext).isEnabled()) {
            return;
        }
        if (isFocused() || (isSelected() && isShown())) {
            AccessibilityEvent obtain = AccessibilityEvent.obtain(16);
            obtain.setFromIndex(i);
            obtain.setRemovedCount(i2);
            obtain.setAddedCount(i3);
            obtain.setBeforeText(sb);
            StringBuilder transformedText = getTransformedText();
            if (!TextUtils.isEmpty(transformedText)) {
                obtain.getText().add(transformedText);
            }
            obtain.setPassword(true);
            sendAccessibilityEventUnchecked(obtain);
        }
    }

    public PasswordTextView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        this.mTextHeightRaw = getContext().getResources().getInteger(2131493035);
    }

    @Override // android.view.View
    public final void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(EditText.class.getName());
        accessibilityEvent.setPassword(true);
    }

    @Override // android.view.View
    public final void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(EditText.class.getName());
        accessibilityNodeInfo.setPassword(true);
        accessibilityNodeInfo.setText(getTransformedText());
        accessibilityNodeInfo.setEditable(true);
        accessibilityNodeInfo.setInputType(16);
    }

    /* JADX WARN: Finally extract failed */
    public PasswordTextView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mTextChars = new ArrayList<>();
        this.mText = "";
        this.mCharPool = new Stack<>();
        Paint paint = new Paint();
        this.mDrawPaint = paint;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.View);
        boolean z = true;
        try {
            boolean z2 = obtainStyledAttributes.getBoolean(19, true);
            boolean z3 = obtainStyledAttributes.getBoolean(20, true);
            setFocusable(z2);
            setFocusableInTouchMode(z3);
            obtainStyledAttributes.recycle();
            TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, R$styleable.PasswordTextView);
            try {
                this.mTextHeightRaw = obtainStyledAttributes2.getInt(4, 0);
                this.mGravity = obtainStyledAttributes2.getInt(1, 17);
                this.mDotSize = obtainStyledAttributes2.getDimensionPixelSize(3, getContext().getResources().getDimensionPixelSize(2131166760));
                this.mCharPadding = obtainStyledAttributes2.getDimensionPixelSize(2, getContext().getResources().getDimensionPixelSize(2131166759));
                paint.setColor(obtainStyledAttributes2.getColor(0, -1));
                obtainStyledAttributes2.recycle();
                paint.setFlags(129);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTypeface(Typeface.create(context.getString(17039968), 0));
                this.mShowPassword = Settings.System.getInt(((View) this).mContext.getContentResolver(), "show_password", 1) != 1 ? false : z;
                this.mAppearInterpolator = AnimationUtils.loadInterpolator(((View) this).mContext, 17563662);
                this.mDisappearInterpolator = AnimationUtils.loadInterpolator(((View) this).mContext, 17563663);
                AnimationUtils.loadInterpolator(((View) this).mContext, 17563661);
                this.mPM = (PowerManager) ((View) this).mContext.getSystemService(GlobalActionsDialogLite.GLOBAL_ACTION_KEY_POWER);
            } catch (Throwable th) {
                obtainStyledAttributes2.recycle();
                throw th;
            }
        } catch (Throwable th2) {
            obtainStyledAttributes.recycle();
            throw th2;
        }
    }
}
