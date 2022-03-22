package com.google.android.material.textfield;

import android.view.View;
import com.google.android.material.internal.CheckableImageButton;
import java.util.Objects;
/* loaded from: classes.dex */
public final class CustomEndIconDelegate extends EndIconDelegate {
    @Override // com.google.android.material.textfield.EndIconDelegate
    public final void initialize() {
        this.textInputLayout.setEndIconDrawable(this.customEndIcon);
        TextInputLayout textInputLayout = this.textInputLayout;
        Objects.requireNonNull(textInputLayout);
        CheckableImageButton checkableImageButton = textInputLayout.endIconView;
        View.OnLongClickListener onLongClickListener = textInputLayout.endIconOnLongClickListener;
        checkableImageButton.setOnClickListener(null);
        TextInputLayout.setIconClickable(checkableImageButton, onLongClickListener);
        TextInputLayout textInputLayout2 = this.textInputLayout;
        Objects.requireNonNull(textInputLayout2);
        textInputLayout2.endIconOnLongClickListener = null;
        CheckableImageButton checkableImageButton2 = textInputLayout2.endIconView;
        checkableImageButton2.setOnLongClickListener(null);
        TextInputLayout.setIconClickable(checkableImageButton2, null);
    }

    public CustomEndIconDelegate(TextInputLayout textInputLayout, int i) {
        super(textInputLayout, i);
    }
}
