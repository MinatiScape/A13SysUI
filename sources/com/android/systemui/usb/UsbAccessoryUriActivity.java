package com.android.systemui.usb;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.hardware.usb.UsbAccessory;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import com.android.internal.app.AlertActivity;
import com.android.internal.app.AlertController;
/* loaded from: classes.dex */
public class UsbAccessoryUriActivity extends AlertActivity implements DialogInterface.OnClickListener {
    public UsbAccessory mAccessory;
    public Uri mUri;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        if (i == -1) {
            Intent intent = new Intent("android.intent.action.VIEW", this.mUri);
            intent.addCategory("android.intent.category.BROWSABLE");
            intent.addFlags(268435456);
            try {
                startActivityAsUser(intent, UserHandle.CURRENT);
            } catch (ActivityNotFoundException unused) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("startActivity failed for ");
                m.append(this.mUri);
                Log.e("UsbAccessoryUriActivity", m.toString());
            }
        }
        finish();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void onCreate(Bundle bundle) {
        Uri uri;
        getWindow().addSystemFlags(524288);
        UsbAccessoryUriActivity.super.onCreate(bundle);
        Intent intent = getIntent();
        this.mAccessory = (UsbAccessory) intent.getParcelableExtra("accessory");
        String stringExtra = intent.getStringExtra("uri");
        if (stringExtra == null) {
            uri = null;
        } else {
            uri = Uri.parse(stringExtra);
        }
        this.mUri = uri;
        if (uri == null) {
            Log.e("UsbAccessoryUriActivity", "could not parse Uri " + stringExtra);
            finish();
            return;
        }
        String scheme = uri.getScheme();
        if ("http".equals(scheme) || "https".equals(scheme)) {
            AlertController.AlertParams alertParams = ((AlertActivity) this).mAlertParams;
            String description = this.mAccessory.getDescription();
            alertParams.mTitle = description;
            if (description == null || description.length() == 0) {
                alertParams.mTitle = getString(2131953371);
            }
            alertParams.mMessage = getString(2131953444, this.mUri);
            alertParams.mPositiveButtonText = getString(2131952608);
            alertParams.mNegativeButtonText = getString(17039360);
            alertParams.mPositiveButtonListener = this;
            alertParams.mNegativeButtonListener = this;
            setupAlert();
            return;
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Uri not http or https: ");
        m.append(this.mUri);
        Log.e("UsbAccessoryUriActivity", m.toString());
        finish();
    }
}
