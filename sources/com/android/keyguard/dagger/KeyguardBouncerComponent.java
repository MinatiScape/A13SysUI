package com.android.keyguard.dagger;

import android.view.ViewGroup;
import com.android.keyguard.KeyguardHostViewController;
/* loaded from: classes.dex */
public interface KeyguardBouncerComponent {

    /* loaded from: classes.dex */
    public interface Factory {
        KeyguardBouncerComponent create(ViewGroup viewGroup);
    }

    KeyguardHostViewController getKeyguardHostViewController();
}
