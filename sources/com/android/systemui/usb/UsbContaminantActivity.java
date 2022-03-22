package com.android.systemui.usb;

import android.app.Activity;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbPort;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import com.android.systemui.theme.ThemeOverlayApplier;
/* loaded from: classes.dex */
public class UsbContaminantActivity extends Activity implements View.OnClickListener {
    public TextView mEnableUsb;
    public TextView mGotIt;
    public TextView mLearnMore;
    public TextView mMessage;
    public TextView mTitle;
    public UsbPort mUsbPort;

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        if (view == this.mEnableUsb) {
            try {
                this.mUsbPort.enableContaminantDetection(false);
                Toast.makeText(this, 2131953460, 0).show();
            } catch (Exception e) {
                Log.e("UsbContaminantActivity", "Unable to notify Usb service", e);
            }
        } else if (view == this.mLearnMore) {
            Intent intent = new Intent();
            intent.setClassName(ThemeOverlayApplier.SETTINGS_PACKAGE, "com.android.settings.HelpTrampoline");
            intent.putExtra("android.intent.extra.TEXT", "help_url_usb_contaminant_detected");
            startActivity(intent);
        }
        finish();
    }

    @Override // android.app.Activity
    public final void onCreate(Bundle bundle) {
        Window window = getWindow();
        window.addSystemFlags(524288);
        window.setType(2008);
        requestWindowFeature(1);
        super.onCreate(bundle);
        setContentView(2131624041);
        this.mUsbPort = getIntent().getParcelableExtra("port").getUsbPort((UsbManager) getSystemService(UsbManager.class));
        this.mLearnMore = (TextView) findViewById(2131428241);
        this.mEnableUsb = (TextView) findViewById(2131427917);
        this.mGotIt = (TextView) findViewById(2131428033);
        this.mTitle = (TextView) findViewById(2131429057);
        this.mMessage = (TextView) findViewById(2131428366);
        this.mTitle.setText(getString(2131953448));
        this.mMessage.setText(getString(2131953447));
        this.mEnableUsb.setText(getString(2131953459));
        this.mGotIt.setText(getString(2131952410));
        this.mLearnMore.setText(getString(2131952650));
        if (getResources().getBoolean(17891731)) {
            this.mLearnMore.setVisibility(0);
        }
        this.mEnableUsb.setOnClickListener(this);
        this.mGotIt.setOnClickListener(this);
        this.mLearnMore.setOnClickListener(this);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public final void onWindowAttributesChanged(WindowManager.LayoutParams layoutParams) {
        super.onWindowAttributesChanged(layoutParams);
    }
}
