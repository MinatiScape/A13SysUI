package com.google.android.material.textfield;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.internal.CheckableImageButton;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ClearTextEndIconDelegate extends EndIconDelegate {
    public AnimatorSet iconInAnim;
    public ValueAnimator iconOutAnim;
    public final AnonymousClass1 clearTextEndIconTextWatcher = new TextWatcher() { // from class: com.google.android.material.textfield.ClearTextEndIconDelegate.1
        @Override // android.text.TextWatcher
        public final void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public final void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public final void afterTextChanged(Editable editable) {
            TextInputLayout textInputLayout = ClearTextEndIconDelegate.this.textInputLayout;
            Objects.requireNonNull(textInputLayout);
            if (textInputLayout.suffixText == null) {
                ClearTextEndIconDelegate clearTextEndIconDelegate = ClearTextEndIconDelegate.this;
                clearTextEndIconDelegate.animateIcon(ClearTextEndIconDelegate.access$000(clearTextEndIconDelegate));
            }
        }
    };
    public final AnonymousClass2 onFocusChangeListener = new View.OnFocusChangeListener() { // from class: com.google.android.material.textfield.ClearTextEndIconDelegate.2
        @Override // android.view.View.OnFocusChangeListener
        public final void onFocusChange(View view, boolean z) {
            ClearTextEndIconDelegate clearTextEndIconDelegate = ClearTextEndIconDelegate.this;
            clearTextEndIconDelegate.animateIcon(ClearTextEndIconDelegate.access$000(clearTextEndIconDelegate));
        }
    };
    public final AnonymousClass3 clearTextOnEditTextAttachedListener = new AnonymousClass3();
    public final AnonymousClass4 endIconChangedListener = new TextInputLayout.OnEndIconChangedListener() { // from class: com.google.android.material.textfield.ClearTextEndIconDelegate.4
        @Override // com.google.android.material.textfield.TextInputLayout.OnEndIconChangedListener
        public final void onEndIconChanged(TextInputLayout textInputLayout, int i) {
            Objects.requireNonNull(textInputLayout);
            final EditText editText = textInputLayout.editText;
            if (editText != null && i == 2) {
                editText.post(new Runnable() { // from class: com.google.android.material.textfield.ClearTextEndIconDelegate.4.1
                    @Override // java.lang.Runnable
                    public final void run() {
                        editText.removeTextChangedListener(ClearTextEndIconDelegate.this.clearTextEndIconTextWatcher);
                    }
                });
                if (editText.getOnFocusChangeListener() == ClearTextEndIconDelegate.this.onFocusChangeListener) {
                    editText.setOnFocusChangeListener(null);
                }
                View.OnFocusChangeListener onFocusChangeListener = ClearTextEndIconDelegate.this.endIconView.getOnFocusChangeListener();
                ClearTextEndIconDelegate clearTextEndIconDelegate = ClearTextEndIconDelegate.this;
                if (onFocusChangeListener == clearTextEndIconDelegate.onFocusChangeListener) {
                    clearTextEndIconDelegate.endIconView.setOnFocusChangeListener(null);
                }
            }
        }
    };

    /* renamed from: com.google.android.material.textfield.ClearTextEndIconDelegate$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass3 implements TextInputLayout.OnEditTextAttachedListener {
        public AnonymousClass3() {
        }

        @Override // com.google.android.material.textfield.TextInputLayout.OnEditTextAttachedListener
        public final void onEditTextAttached(TextInputLayout textInputLayout) {
            Objects.requireNonNull(textInputLayout);
            EditText editText = textInputLayout.editText;
            textInputLayout.setEndIconVisible(ClearTextEndIconDelegate.access$000(ClearTextEndIconDelegate.this));
            CheckableImageButton checkableImageButton = textInputLayout.endIconView;
            Objects.requireNonNull(checkableImageButton);
            if (checkableImageButton.checkable) {
                checkableImageButton.checkable = false;
                checkableImageButton.sendAccessibilityEvent(0);
            }
            editText.setOnFocusChangeListener(ClearTextEndIconDelegate.this.onFocusChangeListener);
            ClearTextEndIconDelegate clearTextEndIconDelegate = ClearTextEndIconDelegate.this;
            clearTextEndIconDelegate.endIconView.setOnFocusChangeListener(clearTextEndIconDelegate.onFocusChangeListener);
            editText.removeTextChangedListener(ClearTextEndIconDelegate.this.clearTextEndIconTextWatcher);
            editText.addTextChangedListener(ClearTextEndIconDelegate.this.clearTextEndIconTextWatcher);
        }
    }

    public final void animateIcon(boolean z) {
        boolean z2;
        if (this.textInputLayout.isEndIconVisible() == z) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (z && !this.iconInAnim.isRunning()) {
            this.iconOutAnim.cancel();
            this.iconInAnim.start();
            if (z2) {
                this.iconInAnim.end();
            }
        } else if (!z) {
            this.iconInAnim.cancel();
            this.iconOutAnim.start();
            if (z2) {
                this.iconOutAnim.end();
            }
        }
    }

    @Override // com.google.android.material.textfield.EndIconDelegate
    public final void initialize() {
        TextInputLayout textInputLayout = this.textInputLayout;
        int i = this.customEndIcon;
        if (i == 0) {
            i = 2131232504;
        }
        textInputLayout.setEndIconDrawable(i);
        TextInputLayout textInputLayout2 = this.textInputLayout;
        textInputLayout2.setEndIconContentDescription(textInputLayout2.getResources().getText(2131952110));
        TextInputLayout textInputLayout3 = this.textInputLayout;
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.google.android.material.textfield.ClearTextEndIconDelegate.5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TextInputLayout textInputLayout4 = ClearTextEndIconDelegate.this.textInputLayout;
                Objects.requireNonNull(textInputLayout4);
                Editable text = textInputLayout4.editText.getText();
                if (text != null) {
                    text.clear();
                }
                TextInputLayout textInputLayout5 = ClearTextEndIconDelegate.this.textInputLayout;
                Objects.requireNonNull(textInputLayout5);
                textInputLayout5.refreshIconDrawableState(textInputLayout5.endIconView, textInputLayout5.endIconTintList);
            }
        };
        Objects.requireNonNull(textInputLayout3);
        CheckableImageButton checkableImageButton = textInputLayout3.endIconView;
        View.OnLongClickListener onLongClickListener = textInputLayout3.endIconOnLongClickListener;
        checkableImageButton.setOnClickListener(onClickListener);
        TextInputLayout.setIconClickable(checkableImageButton, onLongClickListener);
        TextInputLayout textInputLayout4 = this.textInputLayout;
        AnonymousClass3 r1 = this.clearTextOnEditTextAttachedListener;
        Objects.requireNonNull(textInputLayout4);
        textInputLayout4.editTextAttachedListeners.add(r1);
        if (textInputLayout4.editText != null) {
            r1.onEditTextAttached(textInputLayout4);
        }
        TextInputLayout textInputLayout5 = this.textInputLayout;
        AnonymousClass4 r12 = this.endIconChangedListener;
        Objects.requireNonNull(textInputLayout5);
        textInputLayout5.endIconChangedListeners.add(r12);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.8f, 1.0f);
        ofFloat.setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
        ofFloat.setDuration(150L);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.material.textfield.ClearTextEndIconDelegate.9
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                ClearTextEndIconDelegate.this.endIconView.setScaleX(floatValue);
                ClearTextEndIconDelegate.this.endIconView.setScaleY(floatValue);
            }
        });
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
        LinearInterpolator linearInterpolator = AnimationUtils.LINEAR_INTERPOLATOR;
        ofFloat2.setInterpolator(linearInterpolator);
        ofFloat2.setDuration(100L);
        ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.material.textfield.ClearTextEndIconDelegate.8
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ClearTextEndIconDelegate.this.endIconView.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        this.iconInAnim = animatorSet;
        animatorSet.playTogether(ofFloat, ofFloat2);
        this.iconInAnim.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.material.textfield.ClearTextEndIconDelegate.6
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationStart(Animator animator) {
                ClearTextEndIconDelegate.this.textInputLayout.setEndIconVisible(true);
            }
        });
        ValueAnimator ofFloat3 = ValueAnimator.ofFloat(1.0f, 0.0f);
        ofFloat3.setInterpolator(linearInterpolator);
        ofFloat3.setDuration(100L);
        ofFloat3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.material.textfield.ClearTextEndIconDelegate.8
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ClearTextEndIconDelegate.this.endIconView.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        });
        this.iconOutAnim = ofFloat3;
        ofFloat3.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.material.textfield.ClearTextEndIconDelegate.7
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                ClearTextEndIconDelegate.this.textInputLayout.setEndIconVisible(false);
            }
        });
    }

    @Override // com.google.android.material.textfield.EndIconDelegate
    public final void onSuffixVisibilityChanged(boolean z) {
        TextInputLayout textInputLayout = this.textInputLayout;
        Objects.requireNonNull(textInputLayout);
        if (textInputLayout.suffixText != null) {
            animateIcon(z);
        }
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.google.android.material.textfield.ClearTextEndIconDelegate$1] */
    /* JADX WARN: Type inference failed for: r1v2, types: [com.google.android.material.textfield.ClearTextEndIconDelegate$2] */
    /* JADX WARN: Type inference failed for: r1v4, types: [com.google.android.material.textfield.ClearTextEndIconDelegate$4] */
    public ClearTextEndIconDelegate(TextInputLayout textInputLayout, int i) {
        super(textInputLayout, i);
    }

    public static boolean access$000(ClearTextEndIconDelegate clearTextEndIconDelegate) {
        Objects.requireNonNull(clearTextEndIconDelegate);
        TextInputLayout textInputLayout = clearTextEndIconDelegate.textInputLayout;
        Objects.requireNonNull(textInputLayout);
        EditText editText = textInputLayout.editText;
        if (editText == null || ((!editText.hasFocus() && !clearTextEndIconDelegate.endIconView.hasFocus()) || editText.getText().length() <= 0)) {
            return false;
        }
        return true;
    }
}
