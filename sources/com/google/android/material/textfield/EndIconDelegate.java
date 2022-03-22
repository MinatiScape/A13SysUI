package com.google.android.material.textfield;

import android.content.Context;
import com.google.android.material.internal.CheckableImageButton;
/* loaded from: classes.dex */
public abstract class EndIconDelegate {
    public Context context;
    public final int customEndIcon;
    public CheckableImageButton endIconView;
    public TextInputLayout textInputLayout;

    public abstract void initialize();

    public boolean isBoxBackgroundModeSupported(int i) {
        return true;
    }

    public void onSuffixVisibilityChanged(boolean z) {
    }

    public EndIconDelegate(TextInputLayout textInputLayout, int i) {
        this.textInputLayout = textInputLayout;
        this.context = textInputLayout.getContext();
        this.endIconView = textInputLayout.endIconView;
        this.customEndIcon = i;
    }
}
