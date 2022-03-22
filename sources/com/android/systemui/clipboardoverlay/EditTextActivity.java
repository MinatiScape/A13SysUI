package com.android.systemui.clipboardoverlay;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.pm.PackageManager;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import com.android.settingslib.widget.LayoutPreference$$ExternalSyntheticLambda0;
import com.android.systemui.biometrics.AuthBiometricView$$ExternalSyntheticLambda5;
import java.util.Objects;
/* loaded from: classes.dex */
public class EditTextActivity extends Activity {
    public static final /* synthetic */ int $r8$clinit = 0;
    public TextView mAttribution;
    public ClipboardManager mClipboardManager;
    public EditText mEditText;

    @Override // android.app.Activity
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(2131624031);
        findViewById(2131427781).setOnClickListener(new AuthBiometricView$$ExternalSyntheticLambda5(this, 2));
        findViewById(2131428854).setOnClickListener(new LayoutPreference$$ExternalSyntheticLambda0(this, 2));
        this.mEditText = (EditText) findViewById(2131427897);
        this.mAttribution = (TextView) findViewById(2131427521);
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(ClipboardManager.class);
        Objects.requireNonNull(clipboardManager);
        this.mClipboardManager = clipboardManager;
    }

    @Override // android.app.Activity
    public final void onStart() {
        super.onStart();
        ClipData primaryClip = this.mClipboardManager.getPrimaryClip();
        if (primaryClip == null) {
            finish();
            return;
        }
        PackageManager packageManager = getApplicationContext().getPackageManager();
        try {
            this.mAttribution.setText(getResources().getString(2131952114, packageManager.getApplicationLabel(packageManager.getApplicationInfo(this.mClipboardManager.getPrimaryClipSource(), PackageManager.ApplicationInfoFlags.of(0L)))));
        } catch (PackageManager.NameNotFoundException e) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Package not found: ");
            m.append(this.mClipboardManager.getPrimaryClipSource());
            Log.w("EditTextActivity", m.toString(), e);
        }
        this.mEditText.setText(primaryClip.getItemAt(0).getText());
        this.mEditText.requestFocus();
    }
}
