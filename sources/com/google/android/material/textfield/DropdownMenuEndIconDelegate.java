package com.google.android.material.textfield;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.LinearInterpolator;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.leanback.R$string;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.internal.CheckableImageButton;
import com.google.android.material.internal.TextWatcherAdapter;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.shape.AbsoluteCornerSize;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public final class DropdownMenuEndIconDelegate extends EndIconDelegate {
    public AccessibilityManager accessibilityManager;
    public ValueAnimator fadeInAnim;
    public ValueAnimator fadeOutAnim;
    public StateListDrawable filledPopupBackground;
    public MaterialShapeDrawable outlinedPopupBackground;
    public final AnonymousClass1 exposedDropdownEndIconTextWatcher = new TextWatcherAdapter() { // from class: com.google.android.material.textfield.DropdownMenuEndIconDelegate.1
        @Override // com.google.android.material.internal.TextWatcherAdapter, android.text.TextWatcher
        public final void afterTextChanged(Editable editable) {
            boolean z;
            TextInputLayout textInputLayout = DropdownMenuEndIconDelegate.this.textInputLayout;
            Objects.requireNonNull(textInputLayout);
            EditText editText = textInputLayout.editText;
            if (editText instanceof AutoCompleteTextView) {
                final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) editText;
                if (DropdownMenuEndIconDelegate.this.accessibilityManager.isTouchExplorationEnabled()) {
                    if (autoCompleteTextView.getKeyListener() != null) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (z && !DropdownMenuEndIconDelegate.this.endIconView.hasFocus()) {
                        autoCompleteTextView.dismissDropDown();
                    }
                }
                autoCompleteTextView.post(new Runnable() { // from class: com.google.android.material.textfield.DropdownMenuEndIconDelegate.1.1
                    @Override // java.lang.Runnable
                    public final void run() {
                        boolean isPopupShowing = autoCompleteTextView.isPopupShowing();
                        DropdownMenuEndIconDelegate.this.setEndIconChecked(isPopupShowing);
                        DropdownMenuEndIconDelegate.this.dropdownPopupDirty = isPopupShowing;
                    }
                });
                return;
            }
            throw new RuntimeException("EditText needs to be an AutoCompleteTextView if an Exposed Dropdown Menu is being used.");
        }
    };
    public final AnonymousClass2 onFocusChangeListener = new View.OnFocusChangeListener() { // from class: com.google.android.material.textfield.DropdownMenuEndIconDelegate.2
        @Override // android.view.View.OnFocusChangeListener
        public final void onFocusChange(View view, boolean z) {
            TextInputLayout textInputLayout = DropdownMenuEndIconDelegate.this.textInputLayout;
            Objects.requireNonNull(textInputLayout);
            textInputLayout.endIconView.setActivated(z);
            if (!z) {
                DropdownMenuEndIconDelegate.this.setEndIconChecked(false);
                DropdownMenuEndIconDelegate.this.dropdownPopupDirty = false;
            }
        }
    };
    public final AnonymousClass3 accessibilityDelegate = new TextInputLayout.AccessibilityDelegate(this.textInputLayout) { // from class: com.google.android.material.textfield.DropdownMenuEndIconDelegate.3
        @Override // com.google.android.material.textfield.TextInputLayout.AccessibilityDelegate, androidx.core.view.AccessibilityDelegateCompat
        public final void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            boolean z;
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            TextInputLayout textInputLayout = DropdownMenuEndIconDelegate.this.textInputLayout;
            Objects.requireNonNull(textInputLayout);
            if (textInputLayout.editText.getKeyListener() != null) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                accessibilityNodeInfoCompat.setClassName(Spinner.class.getName());
            }
            if (accessibilityNodeInfoCompat.mInfo.isShowingHintText()) {
                accessibilityNodeInfoCompat.mInfo.setHintText(null);
            }
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public final void onPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            super.onPopulateAccessibilityEvent(view, accessibilityEvent);
            TextInputLayout textInputLayout = DropdownMenuEndIconDelegate.this.textInputLayout;
            Objects.requireNonNull(textInputLayout);
            EditText editText = textInputLayout.editText;
            if (editText instanceof AutoCompleteTextView) {
                AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) editText;
                boolean z = true;
                if (accessibilityEvent.getEventType() == 1 && DropdownMenuEndIconDelegate.this.accessibilityManager.isTouchExplorationEnabled()) {
                    TextInputLayout textInputLayout2 = DropdownMenuEndIconDelegate.this.textInputLayout;
                    Objects.requireNonNull(textInputLayout2);
                    if (textInputLayout2.editText.getKeyListener() == null) {
                        z = false;
                    }
                    if (!z) {
                        DropdownMenuEndIconDelegate.access$500(DropdownMenuEndIconDelegate.this, autoCompleteTextView);
                        return;
                    }
                    return;
                }
                return;
            }
            throw new RuntimeException("EditText needs to be an AutoCompleteTextView if an Exposed Dropdown Menu is being used.");
        }
    };
    public final AnonymousClass4 dropdownMenuOnEditTextAttachedListener = new AnonymousClass4();
    @SuppressLint({"ClickableViewAccessibility"})
    public final AnonymousClass5 endIconChangedListener = new TextInputLayout.OnEndIconChangedListener() { // from class: com.google.android.material.textfield.DropdownMenuEndIconDelegate.5
        @Override // com.google.android.material.textfield.TextInputLayout.OnEndIconChangedListener
        public final void onEndIconChanged(TextInputLayout textInputLayout, int i) {
            Objects.requireNonNull(textInputLayout);
            final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) textInputLayout.editText;
            if (autoCompleteTextView != null && i == 3) {
                autoCompleteTextView.post(new Runnable() { // from class: com.google.android.material.textfield.DropdownMenuEndIconDelegate.5.1
                    @Override // java.lang.Runnable
                    public final void run() {
                        autoCompleteTextView.removeTextChangedListener(DropdownMenuEndIconDelegate.this.exposedDropdownEndIconTextWatcher);
                    }
                });
                if (autoCompleteTextView.getOnFocusChangeListener() == DropdownMenuEndIconDelegate.this.onFocusChangeListener) {
                    autoCompleteTextView.setOnFocusChangeListener(null);
                }
                autoCompleteTextView.setOnTouchListener(null);
                autoCompleteTextView.setOnDismissListener(null);
            }
        }
    };
    public boolean dropdownPopupDirty = false;
    public boolean isEndIconChecked = false;
    public long dropdownPopupActivatedAt = Long.MAX_VALUE;

    /* renamed from: com.google.android.material.textfield.DropdownMenuEndIconDelegate$4  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass4 implements TextInputLayout.OnEditTextAttachedListener {
        public AnonymousClass4() {
        }

        @Override // com.google.android.material.textfield.TextInputLayout.OnEditTextAttachedListener
        public final void onEditTextAttached(TextInputLayout textInputLayout) {
            boolean z;
            Objects.requireNonNull(textInputLayout);
            EditText editText = textInputLayout.editText;
            if (editText instanceof AutoCompleteTextView) {
                final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) editText;
                DropdownMenuEndIconDelegate dropdownMenuEndIconDelegate = DropdownMenuEndIconDelegate.this;
                Objects.requireNonNull(dropdownMenuEndIconDelegate);
                TextInputLayout textInputLayout2 = dropdownMenuEndIconDelegate.textInputLayout;
                Objects.requireNonNull(textInputLayout2);
                int i = textInputLayout2.boxBackgroundMode;
                if (i == 2) {
                    autoCompleteTextView.setDropDownBackgroundDrawable(dropdownMenuEndIconDelegate.outlinedPopupBackground);
                } else if (i == 1) {
                    autoCompleteTextView.setDropDownBackgroundDrawable(dropdownMenuEndIconDelegate.filledPopupBackground);
                }
                DropdownMenuEndIconDelegate dropdownMenuEndIconDelegate2 = DropdownMenuEndIconDelegate.this;
                Objects.requireNonNull(dropdownMenuEndIconDelegate2);
                boolean z2 = false;
                if (autoCompleteTextView.getKeyListener() != null) {
                    z = true;
                } else {
                    z = false;
                }
                if (!z) {
                    TextInputLayout textInputLayout3 = dropdownMenuEndIconDelegate2.textInputLayout;
                    Objects.requireNonNull(textInputLayout3);
                    int i2 = textInputLayout3.boxBackgroundMode;
                    TextInputLayout textInputLayout4 = dropdownMenuEndIconDelegate2.textInputLayout;
                    Objects.requireNonNull(textInputLayout4);
                    int i3 = textInputLayout4.boxBackgroundMode;
                    if (i3 == 1 || i3 == 2) {
                        MaterialShapeDrawable materialShapeDrawable = textInputLayout4.boxBackground;
                        int color = R$string.getColor(autoCompleteTextView, 2130968819);
                        int[][] iArr = {new int[]{16842919}, new int[0]};
                        if (i2 == 2) {
                            int color2 = R$string.getColor(autoCompleteTextView, 2130968847);
                            Objects.requireNonNull(materialShapeDrawable);
                            MaterialShapeDrawable materialShapeDrawable2 = new MaterialShapeDrawable(materialShapeDrawable.drawableState.shapeAppearanceModel);
                            int layer = R$string.layer(color, color2, 0.1f);
                            materialShapeDrawable2.setFillColor(new ColorStateList(iArr, new int[]{layer, 0}));
                            materialShapeDrawable2.setTint(color2);
                            ColorStateList colorStateList = new ColorStateList(iArr, new int[]{layer, color2});
                            MaterialShapeDrawable materialShapeDrawable3 = new MaterialShapeDrawable(materialShapeDrawable.drawableState.shapeAppearanceModel);
                            materialShapeDrawable3.setTint(-1);
                            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{new RippleDrawable(colorStateList, materialShapeDrawable2, materialShapeDrawable3), materialShapeDrawable});
                            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                            ViewCompat.Api16Impl.setBackground(autoCompleteTextView, layerDrawable);
                        } else if (i2 == 1) {
                            TextInputLayout textInputLayout5 = dropdownMenuEndIconDelegate2.textInputLayout;
                            Objects.requireNonNull(textInputLayout5);
                            int i4 = textInputLayout5.boxBackgroundColor;
                            RippleDrawable rippleDrawable = new RippleDrawable(new ColorStateList(iArr, new int[]{R$string.layer(color, i4, 0.1f), i4}), materialShapeDrawable, materialShapeDrawable);
                            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
                            ViewCompat.Api16Impl.setBackground(autoCompleteTextView, rippleDrawable);
                        }
                    } else {
                        throw new IllegalStateException();
                    }
                }
                final DropdownMenuEndIconDelegate dropdownMenuEndIconDelegate3 = DropdownMenuEndIconDelegate.this;
                Objects.requireNonNull(dropdownMenuEndIconDelegate3);
                autoCompleteTextView.setOnTouchListener(new View.OnTouchListener() { // from class: com.google.android.material.textfield.DropdownMenuEndIconDelegate.7
                    @Override // android.view.View.OnTouchListener
                    public final boolean onTouch(View view, MotionEvent motionEvent) {
                        int action = motionEvent.getAction();
                        boolean z3 = true;
                        if (action == 1) {
                            DropdownMenuEndIconDelegate dropdownMenuEndIconDelegate4 = DropdownMenuEndIconDelegate.this;
                            Objects.requireNonNull(dropdownMenuEndIconDelegate4);
                            long currentTimeMillis = System.currentTimeMillis() - dropdownMenuEndIconDelegate4.dropdownPopupActivatedAt;
                            if (currentTimeMillis >= 0 && currentTimeMillis <= 300) {
                                z3 = false;
                            }
                            if (z3) {
                                DropdownMenuEndIconDelegate.this.dropdownPopupDirty = false;
                            }
                            DropdownMenuEndIconDelegate.access$500(DropdownMenuEndIconDelegate.this, autoCompleteTextView);
                        }
                        return false;
                    }
                });
                autoCompleteTextView.setOnFocusChangeListener(dropdownMenuEndIconDelegate3.onFocusChangeListener);
                autoCompleteTextView.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() { // from class: com.google.android.material.textfield.DropdownMenuEndIconDelegate.8
                    @Override // android.widget.AutoCompleteTextView.OnDismissListener
                    public final void onDismiss() {
                        DropdownMenuEndIconDelegate dropdownMenuEndIconDelegate4 = DropdownMenuEndIconDelegate.this;
                        dropdownMenuEndIconDelegate4.dropdownPopupDirty = true;
                        dropdownMenuEndIconDelegate4.dropdownPopupActivatedAt = System.currentTimeMillis();
                        DropdownMenuEndIconDelegate.this.setEndIconChecked(false);
                    }
                });
                autoCompleteTextView.setThreshold(0);
                autoCompleteTextView.removeTextChangedListener(DropdownMenuEndIconDelegate.this.exposedDropdownEndIconTextWatcher);
                autoCompleteTextView.addTextChangedListener(DropdownMenuEndIconDelegate.this.exposedDropdownEndIconTextWatcher);
                CheckableImageButton checkableImageButton = textInputLayout.endIconView;
                Objects.requireNonNull(checkableImageButton);
                if (!checkableImageButton.checkable) {
                    checkableImageButton.checkable = true;
                    checkableImageButton.sendAccessibilityEvent(0);
                }
                textInputLayout.errorIconView.setImageDrawable(null);
                textInputLayout.setErrorIconVisible(false);
                if (autoCompleteTextView.getKeyListener() != null) {
                    z2 = true;
                }
                if (!z2) {
                    CheckableImageButton checkableImageButton2 = DropdownMenuEndIconDelegate.this.endIconView;
                    WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap3 = ViewCompat.sViewPropertyAnimatorMap;
                    ViewCompat.Api16Impl.setImportantForAccessibility(checkableImageButton2, 2);
                }
                AnonymousClass3 r11 = DropdownMenuEndIconDelegate.this.accessibilityDelegate;
                EditText editText2 = textInputLayout.editText;
                if (editText2 != null) {
                    ViewCompat.setAccessibilityDelegate(editText2, r11);
                }
                textInputLayout.setEndIconVisible(true);
                return;
            }
            throw new RuntimeException("EditText needs to be an AutoCompleteTextView if an Exposed Dropdown Menu is being used.");
        }
    }

    @Override // com.google.android.material.textfield.EndIconDelegate
    public final boolean isBoxBackgroundModeSupported(int i) {
        return i != 0;
    }

    public static void access$500(DropdownMenuEndIconDelegate dropdownMenuEndIconDelegate, AutoCompleteTextView autoCompleteTextView) {
        boolean z;
        if (autoCompleteTextView == null) {
            Objects.requireNonNull(dropdownMenuEndIconDelegate);
            return;
        }
        Objects.requireNonNull(dropdownMenuEndIconDelegate);
        long currentTimeMillis = System.currentTimeMillis() - dropdownMenuEndIconDelegate.dropdownPopupActivatedAt;
        if (currentTimeMillis < 0 || currentTimeMillis > 300) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            dropdownMenuEndIconDelegate.dropdownPopupDirty = false;
        }
        if (!dropdownMenuEndIconDelegate.dropdownPopupDirty) {
            dropdownMenuEndIconDelegate.setEndIconChecked(!dropdownMenuEndIconDelegate.isEndIconChecked);
            if (dropdownMenuEndIconDelegate.isEndIconChecked) {
                autoCompleteTextView.requestFocus();
                autoCompleteTextView.showDropDown();
                return;
            }
            autoCompleteTextView.dismissDropDown();
            return;
        }
        dropdownMenuEndIconDelegate.dropdownPopupDirty = false;
    }

    public final MaterialShapeDrawable getPopUpMaterialShapeDrawable(float f, float f2, float f3, int i) {
        ShapeAppearanceModel.Builder builder = new ShapeAppearanceModel.Builder();
        builder.topLeftCornerSize = new AbsoluteCornerSize(f);
        builder.topRightCornerSize = new AbsoluteCornerSize(f);
        builder.bottomLeftCornerSize = new AbsoluteCornerSize(f2);
        builder.bottomRightCornerSize = new AbsoluteCornerSize(f2);
        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel(builder);
        Context context = this.context;
        Paint paint = MaterialShapeDrawable.clearPaint;
        int resolveOrThrow = MaterialAttributes.resolveOrThrow(context, 2130968847, "MaterialShapeDrawable");
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
        materialShapeDrawable.initializeElevationOverlay(context);
        materialShapeDrawable.setFillColor(ColorStateList.valueOf(resolveOrThrow));
        materialShapeDrawable.setElevation(f3);
        materialShapeDrawable.setShapeAppearanceModel(shapeAppearanceModel);
        MaterialShapeDrawable.MaterialShapeDrawableState materialShapeDrawableState = materialShapeDrawable.drawableState;
        if (materialShapeDrawableState.padding == null) {
            materialShapeDrawableState.padding = new Rect();
        }
        materialShapeDrawable.drawableState.padding.set(0, i, 0, i);
        materialShapeDrawable.invalidateSelf();
        return materialShapeDrawable;
    }

    @Override // com.google.android.material.textfield.EndIconDelegate
    public final void initialize() {
        float dimensionPixelOffset = this.context.getResources().getDimensionPixelOffset(2131166546);
        float dimensionPixelOffset2 = this.context.getResources().getDimensionPixelOffset(2131166479);
        int dimensionPixelOffset3 = this.context.getResources().getDimensionPixelOffset(2131166481);
        MaterialShapeDrawable popUpMaterialShapeDrawable = getPopUpMaterialShapeDrawable(dimensionPixelOffset, dimensionPixelOffset, dimensionPixelOffset2, dimensionPixelOffset3);
        MaterialShapeDrawable popUpMaterialShapeDrawable2 = getPopUpMaterialShapeDrawable(0.0f, dimensionPixelOffset, dimensionPixelOffset2, dimensionPixelOffset3);
        this.outlinedPopupBackground = popUpMaterialShapeDrawable;
        StateListDrawable stateListDrawable = new StateListDrawable();
        this.filledPopupBackground = stateListDrawable;
        stateListDrawable.addState(new int[]{16842922}, popUpMaterialShapeDrawable);
        this.filledPopupBackground.addState(new int[0], popUpMaterialShapeDrawable2);
        int i = this.customEndIcon;
        if (i == 0) {
            i = 2131232501;
        }
        this.textInputLayout.setEndIconDrawable(i);
        TextInputLayout textInputLayout = this.textInputLayout;
        textInputLayout.setEndIconContentDescription(textInputLayout.getResources().getText(2131952339));
        TextInputLayout textInputLayout2 = this.textInputLayout;
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.google.android.material.textfield.DropdownMenuEndIconDelegate.6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TextInputLayout textInputLayout3 = DropdownMenuEndIconDelegate.this.textInputLayout;
                Objects.requireNonNull(textInputLayout3);
                DropdownMenuEndIconDelegate.access$500(DropdownMenuEndIconDelegate.this, (AutoCompleteTextView) textInputLayout3.editText);
            }
        };
        Objects.requireNonNull(textInputLayout2);
        CheckableImageButton checkableImageButton = textInputLayout2.endIconView;
        View.OnLongClickListener onLongClickListener = textInputLayout2.endIconOnLongClickListener;
        checkableImageButton.setOnClickListener(onClickListener);
        TextInputLayout.setIconClickable(checkableImageButton, onLongClickListener);
        TextInputLayout textInputLayout3 = this.textInputLayout;
        AnonymousClass4 r1 = this.dropdownMenuOnEditTextAttachedListener;
        Objects.requireNonNull(textInputLayout3);
        textInputLayout3.editTextAttachedListeners.add(r1);
        if (textInputLayout3.editText != null) {
            r1.onEditTextAttached(textInputLayout3);
        }
        TextInputLayout textInputLayout4 = this.textInputLayout;
        AnonymousClass5 r12 = this.endIconChangedListener;
        Objects.requireNonNull(textInputLayout4);
        textInputLayout4.endIconChangedListeners.add(r12);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        LinearInterpolator linearInterpolator = AnimationUtils.LINEAR_INTERPOLATOR;
        ofFloat.setInterpolator(linearInterpolator);
        ofFloat.setDuration(67);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.material.textfield.DropdownMenuEndIconDelegate.10
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                DropdownMenuEndIconDelegate.this.endIconView.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        });
        this.fadeInAnim = ofFloat;
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(1.0f, 0.0f);
        ofFloat2.setInterpolator(linearInterpolator);
        ofFloat2.setDuration(50);
        ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.material.textfield.DropdownMenuEndIconDelegate.10
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                DropdownMenuEndIconDelegate.this.endIconView.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        });
        this.fadeOutAnim = ofFloat2;
        ofFloat2.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.material.textfield.DropdownMenuEndIconDelegate.9
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                DropdownMenuEndIconDelegate dropdownMenuEndIconDelegate = DropdownMenuEndIconDelegate.this;
                dropdownMenuEndIconDelegate.endIconView.setChecked(dropdownMenuEndIconDelegate.isEndIconChecked);
                DropdownMenuEndIconDelegate.this.fadeInAnim.start();
            }
        });
        this.accessibilityManager = (AccessibilityManager) this.context.getSystemService("accessibility");
    }

    public final void setEndIconChecked(boolean z) {
        if (this.isEndIconChecked != z) {
            this.isEndIconChecked = z;
            this.fadeInAnim.cancel();
            this.fadeOutAnim.start();
        }
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.google.android.material.textfield.DropdownMenuEndIconDelegate$1] */
    /* JADX WARN: Type inference failed for: r1v2, types: [com.google.android.material.textfield.DropdownMenuEndIconDelegate$2] */
    /* JADX WARN: Type inference failed for: r1v3, types: [com.google.android.material.textfield.DropdownMenuEndIconDelegate$3] */
    /* JADX WARN: Type inference failed for: r1v5, types: [com.google.android.material.textfield.DropdownMenuEndIconDelegate$5] */
    public DropdownMenuEndIconDelegate(TextInputLayout textInputLayout, int i) {
        super(textInputLayout, i);
    }
}
