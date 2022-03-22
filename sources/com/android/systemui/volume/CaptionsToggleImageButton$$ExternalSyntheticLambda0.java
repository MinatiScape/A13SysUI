package com.android.systemui.volume;

import android.view.View;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class CaptionsToggleImageButton$$ExternalSyntheticLambda0 implements AccessibilityViewCommand {
    public final /* synthetic */ Object f$0;

    public /* synthetic */ CaptionsToggleImageButton$$ExternalSyntheticLambda0(Object obj) {
        this.f$0 = obj;
    }

    @Override // androidx.core.view.accessibility.AccessibilityViewCommand
    public final boolean perform(View view) {
        CaptionsToggleImageButton captionsToggleImageButton = (CaptionsToggleImageButton) this.f$0;
        int[] iArr = CaptionsToggleImageButton.OPTED_OUT_STATE;
        Objects.requireNonNull(captionsToggleImageButton);
        return captionsToggleImageButton.tryToSendTapConfirmedEvent();
    }
}
