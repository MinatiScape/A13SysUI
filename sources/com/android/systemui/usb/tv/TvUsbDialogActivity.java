package com.android.systemui.usb.tv;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.tv.TvBottomSheetActivity;
import com.android.systemui.usb.UsbDialogHelper;
import com.android.systemui.usb.UsbDisconnectedReceiver;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class TvUsbDialogActivity extends TvBottomSheetActivity implements View.OnClickListener {
    public UsbDialogHelper mDialogHelper;

    public abstract void onConfirm();

    @Override // android.app.Activity
    public void onPause() {
        UsbDisconnectedReceiver usbDisconnectedReceiver;
        UsbDialogHelper usbDialogHelper = this.mDialogHelper;
        if (!(usbDialogHelper == null || (usbDisconnectedReceiver = usbDialogHelper.mDisconnectedReceiver) == null)) {
            try {
                unregisterReceiver(usbDisconnectedReceiver);
            } catch (Exception unused) {
            }
            usbDialogHelper.mDisconnectedReceiver = null;
        }
        super.onPause();
    }

    public final void initUI(CharSequence charSequence, CharSequence charSequence2) {
        Button button = (Button) findViewById(2131427598);
        Button button2 = (Button) findViewById(2131427597);
        ((TextView) findViewById(2131427600)).setText(charSequence);
        ((TextView) findViewById(2131427595)).setText(charSequence2);
        ((ImageView) findViewById(2131427596)).setImageResource(17302876);
        ((ImageView) findViewById(2131427599)).setVisibility(8);
        button.setText(17039370);
        button.setOnClickListener(this);
        button2.setText(17039360);
        button2.setOnClickListener(this);
        button2.requestFocus();
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        if (view.getId() == 2131427598) {
            onConfirm();
        } else {
            finish();
        }
    }

    @Override // com.android.systemui.tv.TvBottomSheetActivity, android.app.Activity
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addPrivateFlags(524288);
        try {
            this.mDialogHelper = new UsbDialogHelper(getApplicationContext(), getIntent());
        } catch (IllegalStateException e) {
            Log.e("TvUsbDialogActivity", "unable to initialize", e);
            finish();
        }
    }

    @Override // android.app.Activity
    public void onResume() {
        super.onResume();
        UsbDialogHelper usbDialogHelper = this.mDialogHelper;
        Objects.requireNonNull(usbDialogHelper);
        if (usbDialogHelper.mIsUsbDevice) {
            usbDialogHelper.mDisconnectedReceiver = new UsbDisconnectedReceiver(this, usbDialogHelper.mDevice);
        } else {
            usbDialogHelper.mDisconnectedReceiver = new UsbDisconnectedReceiver(this, usbDialogHelper.mAccessory);
        }
    }
}
