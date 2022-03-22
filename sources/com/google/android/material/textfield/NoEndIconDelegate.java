package com.google.android.material.textfield;

import android.view.View;
import com.google.android.material.internal.CheckableImageButton;
import java.util.Objects;
/* loaded from: classes.dex */
public final class NoEndIconDelegate extends EndIconDelegate {
    public NoEndIconDelegate(TextInputLayout textInputLayout) {
        super(textInputLayout, 0);
    }

    @Override // com.google.android.material.textfield.EndIconDelegate
    public final void initialize() {
        TextInputLayout textInputLayout = this.textInputLayout;
        Objects.requireNonNull(textInputLayout);
        CheckableImageButton checkableImageButton = textInputLayout.endIconView;
        View.OnLongClickListener onLongClickListener = textInputLayout.endIconOnLongClickListener;
        checkableImageButton.setOnClickListener(null);
        TextInputLayout.setIconClickable(checkableImageButton, onLongClickListener);
        TextInputLayout textInputLayout2 = this.textInputLayout;
        Objects.requireNonNull(textInputLayout2);
        textInputLayout2.endIconView.setImageDrawable(null);
        this.textInputLayout.setEndIconContentDescription(null);
    }
}
