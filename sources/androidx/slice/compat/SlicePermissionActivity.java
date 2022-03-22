package androidx.slice.compat;

import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.Html;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AlertController;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.BidiFormatter;
import androidx.slice.compat.SliceProviderCompat;
/* loaded from: classes.dex */
public class SlicePermissionActivity extends AppCompatActivity implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener {
    public String mCallingPkg;
    public AlertDialog mDialog;
    public String mProviderPkg;
    public Uri mUri;

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        if (i == -1) {
            String packageName = getPackageName();
            String str = this.mCallingPkg;
            Uri build = this.mUri.buildUpon().path("").build();
            try {
                SliceProviderCompat.ProviderHolder acquireClient = SliceProviderCompat.acquireClient(getContentResolver(), build);
                Bundle bundle = new Bundle();
                bundle.putParcelable("slice_uri", build);
                bundle.putString("provider_pkg", packageName);
                bundle.putString("pkg", str);
                acquireClient.mProvider.call("grant_perms", "supports_versioned_parcelable", bundle);
                acquireClient.close();
            } catch (RemoteException e) {
                Log.e("SliceProviderCompat", "Unable to get slice descendants", e);
            }
        }
        finish();
    }

    public static CharSequence loadSafeLabel(PackageManager packageManager, ApplicationInfo applicationInfo) {
        String obj = Html.fromHtml(applicationInfo.loadLabel(packageManager).toString()).toString();
        int length = obj.length();
        int i = 0;
        while (i < length) {
            int codePointAt = obj.codePointAt(i);
            int type = Character.getType(codePointAt);
            if (type == 13 || type == 15 || type == 14) {
                obj = obj.substring(0, i);
                break;
            }
            if (type == 12) {
                obj = obj.substring(0, i) + " " + obj.substring(Character.charCount(codePointAt) + i);
            }
            i += Character.charCount(codePointAt);
        }
        String trim = obj.trim();
        if (trim.isEmpty()) {
            return applicationInfo.packageName;
        }
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(42.0f);
        return TextUtils.ellipsize(trim, textPaint, 500.0f, TextUtils.TruncateAt.END);
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mUri = (Uri) getIntent().getParcelableExtra("slice_uri");
        this.mCallingPkg = getIntent().getStringExtra("pkg");
        this.mProviderPkg = getIntent().getStringExtra("provider_pkg");
        try {
            PackageManager packageManager = getPackageManager();
            String unicodeWrap = BidiFormatter.getInstance().unicodeWrap(loadSafeLabel(packageManager, packageManager.getApplicationInfo(this.mCallingPkg, 0)).toString());
            String unicodeWrap2 = BidiFormatter.getInstance().unicodeWrap(loadSafeLabel(packageManager, packageManager.getApplicationInfo(this.mProviderPkg, 0)).toString());
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String string = getString(2131951651, unicodeWrap, unicodeWrap2);
            AlertController.AlertParams alertParams = builder.P;
            alertParams.mTitle = string;
            alertParams.mViewLayoutResId = 2131623969;
            alertParams.mNegativeButtonText = alertParams.mContext.getText(2131951648);
            AlertController.AlertParams alertParams2 = builder.P;
            alertParams2.mNegativeButtonListener = this;
            alertParams2.mPositiveButtonText = alertParams2.mContext.getText(2131951647);
            AlertController.AlertParams alertParams3 = builder.P;
            alertParams3.mPositiveButtonListener = this;
            alertParams3.mOnDismissListener = this;
            AlertDialog create = builder.create();
            create.show();
            this.mDialog = create;
            ((TextView) create.getWindow().getDecorView().findViewById(2131429025)).setText(getString(2131951649, unicodeWrap2));
            ((TextView) this.mDialog.getWindow().getDecorView().findViewById(2131429026)).setText(getString(2131951650, unicodeWrap2));
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("SlicePermissionActivity", "Couldn't find package", e);
            finish();
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public final void onDestroy() {
        super.onDestroy();
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog != null && alertDialog.isShowing()) {
            this.mDialog.cancel();
        }
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        finish();
    }
}
