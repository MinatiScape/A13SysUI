package com.android.keyguard;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.TextView;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.PasswordTextView;
import com.android.systemui.R$styleable;
import com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda0;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
import java.util.Objects;
/* loaded from: classes.dex */
public class NumPadKey extends ViewGroup {
    public static String[] sKlondike;
    public NumPadAnimator mAnimator;
    public int mDigit;
    public final TextView mDigitText;
    public final TextView mKlondikeText;
    public AnonymousClass1 mListener;
    public int mOrientation;
    public final PowerManager mPM;
    public PasswordTextView mTextView;
    public int mTextViewResId;

    public NumPadKey(Context context) {
        this(context, null);
    }

    @Override // android.view.View
    public final boolean hasOverlappingRendering() {
        return false;
    }

    public NumPadKey(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 2130969520);
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        this.mOrientation = configuration.orientation;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int measuredHeight = this.mDigitText.getMeasuredHeight();
        int measuredHeight2 = this.mKlondikeText.getMeasuredHeight();
        int height = (getHeight() / 2) - ((measuredHeight + measuredHeight2) / 2);
        int width = getWidth() / 2;
        int measuredWidth = width - (this.mDigitText.getMeasuredWidth() / 2);
        int i5 = measuredHeight + height;
        TextView textView = this.mDigitText;
        textView.layout(measuredWidth, height, textView.getMeasuredWidth() + measuredWidth, i5);
        int i6 = (int) (i5 - (measuredHeight2 * 0.35f));
        int measuredWidth2 = width - (this.mKlondikeText.getMeasuredWidth() / 2);
        TextView textView2 = this.mKlondikeText;
        textView2.layout(measuredWidth2, i6, textView2.getMeasuredWidth() + measuredWidth2, measuredHeight2 + i6);
        NumPadAnimator numPadAnimator = this.mAnimator;
        if (numPadAnimator != null) {
            numPadAnimator.onLayout(i4 - i2);
        }
    }

    public NumPadKey(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 2131624162);
    }

    @Override // android.view.View
    public final void onMeasure(int i, int i2) {
        boolean z;
        super.onMeasure(i, i2);
        measureChildren(i, i2);
        int measuredWidth = getMeasuredWidth();
        if (this.mAnimator == null || this.mOrientation == 2) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            measuredWidth = (int) (measuredWidth * 0.66f);
        }
        setMeasuredDimension(getMeasuredWidth(), measuredWidth);
    }

    @Override // android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0) {
            performHapticFeedback(1, 3);
            NumPadAnimator numPadAnimator = this.mAnimator;
            if (numPadAnimator != null) {
                numPadAnimator.mAnimator.cancel();
                numPadAnimator.mAnimator.start();
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.keyguard.NumPadKey$1] */
    public NumPadKey(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i);
        this.mDigit = -1;
        this.mListener = new View.OnClickListener() { // from class: com.android.keyguard.NumPadKey.1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PasswordTextView.CharState charState;
                boolean z;
                boolean z2;
                boolean z3;
                View findViewById;
                NumPadKey numPadKey = NumPadKey.this;
                if (numPadKey.mTextView == null && numPadKey.mTextViewResId > 0 && (findViewById = numPadKey.getRootView().findViewById(NumPadKey.this.mTextViewResId)) != null && (findViewById instanceof PasswordTextView)) {
                    NumPadKey.this.mTextView = (PasswordTextView) findViewById;
                }
                PasswordTextView passwordTextView = NumPadKey.this.mTextView;
                if (passwordTextView != null && passwordTextView.isEnabled()) {
                    NumPadKey numPadKey2 = NumPadKey.this;
                    PasswordTextView passwordTextView2 = numPadKey2.mTextView;
                    char forDigit = Character.forDigit(numPadKey2.mDigit, 10);
                    Objects.requireNonNull(passwordTextView2);
                    int size = passwordTextView2.mTextChars.size();
                    StringBuilder transformedText = passwordTextView2.getTransformedText();
                    String str = passwordTextView2.mText + forDigit;
                    passwordTextView2.mText = str;
                    int length = str.length();
                    if (length > size) {
                        if (passwordTextView2.mCharPool.isEmpty()) {
                            charState = new PasswordTextView.CharState();
                        } else {
                            charState = passwordTextView2.mCharPool.pop();
                            charState.reset();
                        }
                        charState.whichChar = forDigit;
                        passwordTextView2.mTextChars.add(charState);
                    } else {
                        charState = passwordTextView2.mTextChars.get(length - 1);
                        charState.whichChar = forDigit;
                    }
                    boolean z4 = PasswordTextView.this.mShowPassword;
                    if (z4 || (charState.dotAnimator != null && charState.dotAnimationIsGrowing)) {
                        z = false;
                    } else {
                        z = true;
                    }
                    if (!z4 || (charState.textAnimator != null && charState.textAnimationIsGrowing)) {
                        z2 = false;
                    } else {
                        z2 = true;
                    }
                    if (charState.widthAnimator == null || !charState.widthAnimationIsGrowing) {
                        z3 = true;
                    } else {
                        z3 = false;
                    }
                    if (z) {
                        charState.startDotAppearAnimation(0L);
                    }
                    if (z2) {
                        PasswordTextView.CharState.cancelAnimator(charState.textAnimator);
                        ValueAnimator ofFloat = ValueAnimator.ofFloat(charState.currentTextSizeFactor, 1.0f);
                        charState.textAnimator = ofFloat;
                        ofFloat.addUpdateListener(charState.textSizeUpdater);
                        charState.textAnimator.addListener(charState.textFinishListener);
                        charState.textAnimator.setInterpolator(PasswordTextView.this.mAppearInterpolator);
                        charState.textAnimator.setDuration((1.0f - charState.currentTextSizeFactor) * 160.0f);
                        charState.textAnimator.start();
                        charState.textAnimationIsGrowing = true;
                        if (charState.textTranslateAnimator == null) {
                            ValueAnimator ofFloat2 = ValueAnimator.ofFloat(1.0f, 0.0f);
                            charState.textTranslateAnimator = ofFloat2;
                            ofFloat2.addUpdateListener(charState.textTranslationUpdater);
                            charState.textTranslateAnimator.addListener(charState.textTranslateFinishListener);
                            charState.textTranslateAnimator.setInterpolator(PasswordTextView.this.mAppearInterpolator);
                            charState.textTranslateAnimator.setDuration(160L);
                            charState.textTranslateAnimator.start();
                        }
                    }
                    if (z3) {
                        PasswordTextView.CharState.cancelAnimator(charState.widthAnimator);
                        ValueAnimator ofFloat3 = ValueAnimator.ofFloat(charState.currentWidthFactor, 1.0f);
                        charState.widthAnimator = ofFloat3;
                        ofFloat3.addUpdateListener(charState.widthUpdater);
                        charState.widthAnimator.addListener(charState.widthFinishListener);
                        charState.widthAnimator.setDuration((1.0f - charState.currentWidthFactor) * 160.0f);
                        charState.widthAnimator.start();
                        charState.widthAnimationIsGrowing = true;
                    }
                    PasswordTextView passwordTextView3 = PasswordTextView.this;
                    if (passwordTextView3.mShowPassword) {
                        passwordTextView3.removeCallbacks(charState.dotSwapperRunnable);
                        charState.isDotSwapPending = false;
                        PasswordTextView.this.postDelayed(charState.dotSwapperRunnable, 1300L);
                        charState.isDotSwapPending = true;
                    }
                    if (length > 1) {
                        PasswordTextView.CharState charState2 = passwordTextView2.mTextChars.get(length - 2);
                        if (charState2.isDotSwapPending) {
                            PasswordTextView.this.removeCallbacks(charState2.dotSwapperRunnable);
                            charState2.isDotSwapPending = false;
                            ValueAnimator valueAnimator = charState2.textAnimator;
                            if (valueAnimator != null) {
                                PasswordTextView.this.removeCallbacks(charState2.dotSwapperRunnable);
                                charState2.isDotSwapPending = false;
                                PasswordTextView.this.postDelayed(charState2.dotSwapperRunnable, (valueAnimator.getDuration() - charState2.textAnimator.getCurrentPlayTime()) + 100);
                                charState2.isDotSwapPending = true;
                            } else {
                                charState2.startTextDisappearAnimation(0L);
                                charState2.startDotAppearAnimation(30L);
                            }
                        }
                    }
                    passwordTextView2.mPM.userActivity(SystemClock.uptimeMillis(), false);
                    PasswordTextView.UserActivityListener userActivityListener = passwordTextView2.mUserActivityListener;
                    if (userActivityListener != null) {
                        ((KeyguardPinBasedInputViewController) ((DozeTriggers$$ExternalSyntheticLambda0) userActivityListener).f$0).onUserInput();
                    }
                    passwordTextView2.sendAccessibilityEventTypeViewTextChanged(transformedText, transformedText.length(), 0, 1);
                }
                NumPadKey numPadKey3 = NumPadKey.this;
                Objects.requireNonNull(numPadKey3);
                numPadKey3.mPM.userActivity(SystemClock.uptimeMillis(), false);
            }
        };
        setFocusable(true);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.NumPadKey, i, i2);
        try {
            this.mDigit = obtainStyledAttributes.getInt(0, this.mDigit);
            this.mTextViewResId = obtainStyledAttributes.getResourceId(1, 0);
            obtainStyledAttributes.recycle();
            setOnClickListener(this.mListener);
            setOnHoverListener(new LiftToActivateListener((AccessibilityManager) context.getSystemService("accessibility")));
            new LockPatternUtils(context);
            this.mPM = (PowerManager) ((ViewGroup) this).mContext.getSystemService(GlobalActionsDialogLite.GLOBAL_ACTION_KEY_POWER);
            ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(i2, (ViewGroup) this, true);
            TextView textView = (TextView) findViewById(2131427843);
            this.mDigitText = textView;
            textView.setText(Integer.toString(this.mDigit));
            TextView textView2 = (TextView) findViewById(2131428200);
            this.mKlondikeText = textView2;
            if (this.mDigit >= 0) {
                if (sKlondike == null) {
                    sKlondike = getResources().getStringArray(2130903115);
                }
                String[] strArr = sKlondike;
                if (strArr != null) {
                    int length = strArr.length;
                    int i3 = this.mDigit;
                    if (length > i3) {
                        String str = strArr[i3];
                        if (str.length() > 0) {
                            textView2.setText(str);
                        } else if (textView2.getVisibility() != 8) {
                            textView2.setVisibility(4);
                        }
                    }
                }
            }
            setContentDescription(textView.getText().toString());
            Drawable background = getBackground();
            if (background instanceof RippleDrawable) {
                this.mAnimator = new NumPadAnimator(context, (RippleDrawable) background, 2132017547);
            } else {
                this.mAnimator = null;
            }
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }
}
