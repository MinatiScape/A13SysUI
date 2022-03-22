package com.android.systemui.wallet.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import com.google.android.systemui.columbus.ColumbusTargetRequestDialog;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WalletScreenController$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ WalletScreenController$$ExternalSyntheticLambda0(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                WalletScreenController walletScreenController = (WalletScreenController) this.f$0;
                Objects.requireNonNull(walletScreenController);
                walletScreenController.mActivityStarter.startActivity((Intent) this.f$1, true);
                return;
            default:
                ColumbusTargetRequestDialog columbusTargetRequestDialog = (ColumbusTargetRequestDialog) this.f$0;
                int i = ColumbusTargetRequestDialog.$r8$clinit;
                Objects.requireNonNull(columbusTargetRequestDialog);
                ((DialogInterface.OnClickListener) this.f$1).onClick(columbusTargetRequestDialog, -1);
                columbusTargetRequestDialog.dismiss();
                return;
        }
    }
}
