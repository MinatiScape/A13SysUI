package com.android.systemui.net;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.INetworkPolicyManager;
import android.net.NetworkTemplate;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import java.util.Objects;
/* loaded from: classes.dex */
public class NetworkOverLimitActivity extends Activity {
    public static final /* synthetic */ int $r8$clinit = 0;

    @Override // android.app.Activity
    public final void onCreate(Bundle bundle) {
        int i;
        super.onCreate(bundle);
        final NetworkTemplate parcelableExtra = getIntent().getParcelableExtra("android.net.NETWORK_TEMPLATE");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (parcelableExtra.getMatchRule() != 1) {
            i = 2131952241;
        } else {
            i = 2131952240;
        }
        builder.setTitle(i);
        builder.setMessage(2131952238);
        builder.setPositiveButton(17039370, (DialogInterface.OnClickListener) null);
        builder.setNegativeButton(2131952239, new DialogInterface.OnClickListener() { // from class: com.android.systemui.net.NetworkOverLimitActivity.1
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                NetworkOverLimitActivity networkOverLimitActivity = NetworkOverLimitActivity.this;
                NetworkTemplate networkTemplate = parcelableExtra;
                int i3 = NetworkOverLimitActivity.$r8$clinit;
                Objects.requireNonNull(networkOverLimitActivity);
                try {
                    INetworkPolicyManager.Stub.asInterface(ServiceManager.getService("netpolicy")).snoozeLimit(networkTemplate);
                } catch (RemoteException e) {
                    Log.w("NetworkOverLimitActivity", "problem snoozing network policy", e);
                }
            }
        });
        AlertDialog create = builder.create();
        create.getWindow().setType(2003);
        create.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.android.systemui.net.NetworkOverLimitActivity.2
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                NetworkOverLimitActivity.this.finish();
            }
        });
        create.show();
    }
}
