package com.google.android.systemui.columbus;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.wallet.ui.WalletScreenController$$ExternalSyntheticLambda0;
import com.google.android.systemui.columbus.ColumbusTargetRequestService$IncomingMessageHandler$displayDialog$1;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ColumbusTargetRequestDialog extends SystemUIDialog {
    public static final /* synthetic */ int $r8$clinit = 0;
    public TextView mContent;
    public Button mNegativeButton;
    public Button mPositiveButton;
    public TextView mTitle;

    @Override // android.app.AlertDialog
    public final void setMessage(CharSequence charSequence) {
        this.mContent.setText(charSequence);
    }

    @Override // com.android.systemui.statusbar.phone.SystemUIDialog
    public final void setNegativeButton(int i, DialogInterface.OnClickListener onClickListener) {
        this.mNegativeButton.setText(2131952127);
        final ColumbusTargetRequestService$IncomingMessageHandler$displayDialog$1.AnonymousClass1 r3 = (ColumbusTargetRequestService$IncomingMessageHandler$displayDialog$1.AnonymousClass1) onClickListener;
        this.mNegativeButton.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.systemui.columbus.ColumbusTargetRequestDialog$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ColumbusTargetRequestDialog columbusTargetRequestDialog = ColumbusTargetRequestDialog.this;
                DialogInterface.OnClickListener onClickListener2 = r3;
                Objects.requireNonNull(columbusTargetRequestDialog);
                onClickListener2.onClick(columbusTargetRequestDialog, -2);
                columbusTargetRequestDialog.dismiss();
            }
        });
    }

    @Override // com.android.systemui.statusbar.phone.SystemUIDialog
    public final void setPositiveButton(int i, DialogInterface.OnClickListener onClickListener) {
        this.mPositiveButton.setText(i);
        this.mPositiveButton.setOnClickListener(new WalletScreenController$$ExternalSyntheticLambda0(this, onClickListener, 1));
    }

    @Override // com.android.systemui.statusbar.phone.SystemUIDialog, android.app.AlertDialog, android.app.Dialog
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(2131624035);
        this.mTitle = (TextView) requireViewById(2131429057);
        this.mContent = (TextView) requireViewById(2131427738);
        this.mPositiveButton = (Button) requireViewById(2131428603);
        this.mNegativeButton = (Button) requireViewById(2131428490);
    }

    @Override // android.app.AlertDialog, android.app.Dialog
    public final void setTitle(CharSequence charSequence) {
        getWindow().setTitle(charSequence);
        getWindow().getAttributes().setTitle(charSequence);
        this.mTitle.setText(charSequence);
    }

    public ColumbusTargetRequestDialog(Context context) {
        super(context);
    }
}
