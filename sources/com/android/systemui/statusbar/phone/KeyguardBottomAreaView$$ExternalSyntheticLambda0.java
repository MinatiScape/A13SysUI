package com.android.systemui.statusbar.phone;

import android.content.Intent;
import com.android.systemui.plugins.IntentButtonProvider;
import com.android.systemui.statusbar.policy.ExtensionController;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class KeyguardBottomAreaView$$ExternalSyntheticLambda0 implements ExtensionController.PluginConverter {
    public static final /* synthetic */ KeyguardBottomAreaView$$ExternalSyntheticLambda0 INSTANCE = new KeyguardBottomAreaView$$ExternalSyntheticLambda0();

    @Override // com.android.systemui.statusbar.policy.ExtensionController.PluginConverter
    public final IntentButtonProvider.IntentButton getInterfaceFromPlugin(Object obj) {
        Intent intent = KeyguardBottomAreaView.PHONE_INTENT;
        return ((IntentButtonProvider) obj).getIntentButton();
    }
}
