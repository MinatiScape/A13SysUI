package com.android.systemui.privacy;

import android.content.Context;
import com.android.systemui.privacy.PrivacyDialogController;
import java.util.ArrayList;
import kotlin.jvm.functions.Function4;
/* compiled from: PrivacyDialogController.kt */
/* loaded from: classes.dex */
public final class PrivacyDialogControllerKt {
    public static final PrivacyDialogControllerKt$defaultDialogProvider$1 defaultDialogProvider = new PrivacyDialogController.DialogProvider() { // from class: com.android.systemui.privacy.PrivacyDialogControllerKt$defaultDialogProvider$1
        @Override // com.android.systemui.privacy.PrivacyDialogController.DialogProvider
        public final PrivacyDialog makeDialog(Context context, ArrayList arrayList, Function4 function4) {
            return new PrivacyDialog(context, arrayList, function4);
        }
    };
}
