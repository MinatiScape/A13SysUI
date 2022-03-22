package com.android.systemui.qs;

import android.content.pm.PackageManager;
import com.android.systemui.qs.FgsManagerController;
import java.util.Objects;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: FgsManagerController.kt */
/* loaded from: classes.dex */
public final class FgsManagerController$UserPackage$uiControl$2 extends Lambda implements Function0<FgsManagerController.UIControl> {
    public final /* synthetic */ FgsManagerController this$0;
    public final /* synthetic */ FgsManagerController.UserPackage this$1;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FgsManagerController$UserPackage$uiControl$2(FgsManagerController fgsManagerController, FgsManagerController.UserPackage userPackage) {
        super(0);
        this.this$0 = fgsManagerController;
        this.this$1 = userPackage;
    }

    @Override // kotlin.jvm.functions.Function0
    public final FgsManagerController.UIControl invoke() {
        PackageManager packageManager = this.this$0.packageManager;
        FgsManagerController.UserPackage userPackage = this.this$1;
        Objects.requireNonNull(userPackage);
        String str = userPackage.packageName;
        FgsManagerController.UserPackage userPackage2 = this.this$1;
        Objects.requireNonNull(userPackage2);
        int backgroundRestrictionExemptionReason = this.this$0.activityManager.getBackgroundRestrictionExemptionReason(packageManager.getPackageUidAsUser(str, userPackage2.userId));
        if (!(backgroundRestrictionExemptionReason == 10 || backgroundRestrictionExemptionReason == 11)) {
            if (backgroundRestrictionExemptionReason == 51 || backgroundRestrictionExemptionReason == 63) {
                return FgsManagerController.UIControl.HIDE_ENTRY;
            }
            if (!(backgroundRestrictionExemptionReason == 318 || backgroundRestrictionExemptionReason == 320 || backgroundRestrictionExemptionReason == 55 || backgroundRestrictionExemptionReason == 56)) {
                return FgsManagerController.UIControl.NORMAL;
            }
        }
        return FgsManagerController.UIControl.HIDE_BUTTON;
    }
}
