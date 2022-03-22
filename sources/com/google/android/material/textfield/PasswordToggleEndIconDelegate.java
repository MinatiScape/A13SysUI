package com.google.android.material.textfield;

import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import com.google.android.material.internal.CheckableImageButton;
import com.google.android.material.internal.TextWatcherAdapter;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Objects;
/* loaded from: classes.dex */
public final class PasswordToggleEndIconDelegate extends EndIconDelegate {
    public final AnonymousClass1 textWatcher = new TextWatcherAdapter() { // from class: com.google.android.material.textfield.PasswordToggleEndIconDelegate.1
        @Override // com.google.android.material.internal.TextWatcherAdapter, android.text.TextWatcher
        public final void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            PasswordToggleEndIconDelegate passwordToggleEndIconDelegate = PasswordToggleEndIconDelegate.this;
            passwordToggleEndIconDelegate.endIconView.setChecked(!PasswordToggleEndIconDelegate.access$000(passwordToggleEndIconDelegate));
        }
    };
    public final AnonymousClass2 onEditTextAttachedListener = new AnonymousClass2();
    public final AnonymousClass3 onEndIconChangedListener = new TextInputLayout.OnEndIconChangedListener() { // from class: com.google.android.material.textfield.PasswordToggleEndIconDelegate.3
        @Override // com.google.android.material.textfield.TextInputLayout.OnEndIconChangedListener
        public final void onEndIconChanged(TextInputLayout textInputLayout, int i) {
            Objects.requireNonNull(textInputLayout);
            final EditText editText = textInputLayout.editText;
            if (editText != null && i == 1) {
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                editText.post(new Runnable() { // from class: com.google.android.material.textfield.PasswordToggleEndIconDelegate.3.1
                    @Override // java.lang.Runnable
                    public final void run() {
                        editText.removeTextChangedListener(PasswordToggleEndIconDelegate.this.textWatcher);
                    }
                });
            }
        }
    };

    /* renamed from: com.google.android.material.textfield.PasswordToggleEndIconDelegate$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 implements TextInputLayout.OnEditTextAttachedListener {
        public AnonymousClass2() {
        }

        @Override // com.google.android.material.textfield.TextInputLayout.OnEditTextAttachedListener
        public final void onEditTextAttached(TextInputLayout textInputLayout) {
            Objects.requireNonNull(textInputLayout);
            EditText editText = textInputLayout.editText;
            textInputLayout.setEndIconVisible(true);
            CheckableImageButton checkableImageButton = textInputLayout.endIconView;
            Objects.requireNonNull(checkableImageButton);
            if (!checkableImageButton.checkable) {
                checkableImageButton.checkable = true;
                checkableImageButton.sendAccessibilityEvent(0);
            }
            PasswordToggleEndIconDelegate passwordToggleEndIconDelegate = PasswordToggleEndIconDelegate.this;
            passwordToggleEndIconDelegate.endIconView.setChecked(!PasswordToggleEndIconDelegate.access$000(passwordToggleEndIconDelegate));
            editText.removeTextChangedListener(PasswordToggleEndIconDelegate.this.textWatcher);
            editText.addTextChangedListener(PasswordToggleEndIconDelegate.this.textWatcher);
        }
    }

    @Override // com.google.android.material.textfield.EndIconDelegate
    public final void initialize() {
        boolean z;
        TextInputLayout textInputLayout = this.textInputLayout;
        int i = this.customEndIcon;
        if (i == 0) {
            i = 2131231678;
        }
        textInputLayout.setEndIconDrawable(i);
        TextInputLayout textInputLayout2 = this.textInputLayout;
        textInputLayout2.setEndIconContentDescription(textInputLayout2.getResources().getText(2131952960));
        TextInputLayout textInputLayout3 = this.textInputLayout;
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.google.android.material.textfield.PasswordToggleEndIconDelegate.4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TextInputLayout textInputLayout4 = PasswordToggleEndIconDelegate.this.textInputLayout;
                Objects.requireNonNull(textInputLayout4);
                EditText editText = textInputLayout4.editText;
                if (editText != null) {
                    int selectionEnd = editText.getSelectionEnd();
                    if (PasswordToggleEndIconDelegate.access$000(PasswordToggleEndIconDelegate.this)) {
                        editText.setTransformationMethod(null);
                    } else {
                        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                    if (selectionEnd >= 0) {
                        editText.setSelection(selectionEnd);
                    }
                    TextInputLayout textInputLayout5 = PasswordToggleEndIconDelegate.this.textInputLayout;
                    Objects.requireNonNull(textInputLayout5);
                    textInputLayout5.refreshIconDrawableState(textInputLayout5.endIconView, textInputLayout5.endIconTintList);
                }
            }
        };
        Objects.requireNonNull(textInputLayout3);
        CheckableImageButton checkableImageButton = textInputLayout3.endIconView;
        View.OnLongClickListener onLongClickListener = textInputLayout3.endIconOnLongClickListener;
        checkableImageButton.setOnClickListener(onClickListener);
        TextInputLayout.setIconClickable(checkableImageButton, onLongClickListener);
        TextInputLayout textInputLayout4 = this.textInputLayout;
        AnonymousClass2 r1 = this.onEditTextAttachedListener;
        Objects.requireNonNull(textInputLayout4);
        textInputLayout4.editTextAttachedListeners.add(r1);
        if (textInputLayout4.editText != null) {
            r1.onEditTextAttached(textInputLayout4);
        }
        TextInputLayout textInputLayout5 = this.textInputLayout;
        AnonymousClass3 r12 = this.onEndIconChangedListener;
        Objects.requireNonNull(textInputLayout5);
        textInputLayout5.endIconChangedListeners.add(r12);
        TextInputLayout textInputLayout6 = this.textInputLayout;
        Objects.requireNonNull(textInputLayout6);
        EditText editText = textInputLayout6.editText;
        if (editText == null || !(editText.getInputType() == 16 || editText.getInputType() == 128 || editText.getInputType() == 144 || editText.getInputType() == 224)) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.google.android.material.textfield.PasswordToggleEndIconDelegate$1] */
    /* JADX WARN: Type inference failed for: r1v3, types: [com.google.android.material.textfield.PasswordToggleEndIconDelegate$3] */
    public PasswordToggleEndIconDelegate(TextInputLayout textInputLayout, int i) {
        super(textInputLayout, i);
    }

    public static boolean access$000(PasswordToggleEndIconDelegate passwordToggleEndIconDelegate) {
        Objects.requireNonNull(passwordToggleEndIconDelegate);
        TextInputLayout textInputLayout = passwordToggleEndIconDelegate.textInputLayout;
        Objects.requireNonNull(textInputLayout);
        EditText editText = textInputLayout.editText;
        if (editText == null || !(editText.getTransformationMethod() instanceof PasswordTransformationMethod)) {
            return false;
        }
        return true;
    }
}
