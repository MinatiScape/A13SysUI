package com.android.systemui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.IconDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.internal.app.AlertActivity;
import com.android.internal.app.AlertController;
import com.android.internal.logging.MetricsLogger;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ForegroundServicesDialog extends AlertActivity implements AdapterView.OnItemSelectedListener, DialogInterface.OnClickListener, AlertController.AlertParams.OnPrepareListViewListener {
    public PackageItemAdapter mAdapter;
    public AnonymousClass1 mAppClickListener = new DialogInterface.OnClickListener() { // from class: com.android.systemui.ForegroundServicesDialog.1
        /* JADX WARN: Type inference failed for: r2v1, types: [com.android.systemui.ForegroundServicesDialog, android.app.Activity] */
        /* JADX WARN: Type inference failed for: r3v7, types: [android.content.Context, com.android.systemui.ForegroundServicesDialog] */
        @Override // android.content.DialogInterface.OnClickListener
        public final void onClick(DialogInterface dialogInterface, int i) {
            String str = ForegroundServicesDialog.this.mAdapter.getItem(i).packageName;
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", str, null));
            ForegroundServicesDialog.this.startActivity(intent);
            ForegroundServicesDialog.this.finish();
        }
    };
    public LayoutInflater mInflater;
    public final MetricsLogger mMetricsLogger;
    public String[] mPackages;

    /* loaded from: classes.dex */
    public static class PackageItemAdapter extends ArrayAdapter<ApplicationInfo> {
        public final IconDrawableFactory mIconDrawableFactory;
        public final LayoutInflater mInflater;
        public final PackageManager mPm;

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public final View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = this.mInflater.inflate(2131624103, viewGroup, false);
            }
            ((ImageView) view.findViewById(2131427500)).setImageDrawable(this.mIconDrawableFactory.getBadgedIcon(getItem(i)));
            ((TextView) view.findViewById(2131427505)).setText(getItem(i).loadLabel(this.mPm));
            return view;
        }

        public PackageItemAdapter(Context context) {
            super(context, 2131624103);
            this.mPm = context.getPackageManager();
            this.mInflater = LayoutInflater.from(context);
            this.mIconDrawableFactory = IconDrawableFactory.newInstance(context, true);
        }
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public final void onItemSelected(AdapterView adapterView, View view, int i, long j) {
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public final void onNothingSelected(AdapterView adapterView) {
    }

    public final void onPrepareListView(ListView listView) {
    }

    public final void updateApps(Intent intent) {
        String[] stringArrayExtra = intent.getStringArrayExtra("packages");
        this.mPackages = stringArrayExtra;
        if (stringArrayExtra != null) {
            PackageItemAdapter packageItemAdapter = this.mAdapter;
            Objects.requireNonNull(packageItemAdapter);
            packageItemAdapter.clear();
            ArrayList arrayList = new ArrayList();
            for (String str : stringArrayExtra) {
                try {
                    arrayList.add(packageItemAdapter.mPm.getApplicationInfo(str, 4202496));
                } catch (PackageManager.NameNotFoundException unused) {
                }
            }
            arrayList.sort(new ApplicationInfo.DisplayNameComparator(packageItemAdapter.mPm));
            packageItemAdapter.addAll(arrayList);
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.ForegroundServicesDialog$1] */
    public ForegroundServicesDialog(MetricsLogger metricsLogger) {
        this.mMetricsLogger = metricsLogger;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void onCreate(Bundle bundle) {
        ForegroundServicesDialog.super.onCreate(bundle);
        this.mInflater = LayoutInflater.from(this);
        PackageItemAdapter packageItemAdapter = new PackageItemAdapter(this);
        this.mAdapter = packageItemAdapter;
        AlertController.AlertParams alertParams = ((AlertActivity) this).mAlertParams;
        alertParams.mAdapter = packageItemAdapter;
        alertParams.mOnClickListener = this.mAppClickListener;
        alertParams.mCustomTitleView = this.mInflater.inflate(2131624104, (ViewGroup) null);
        alertParams.mIsSingleChoice = true;
        alertParams.mOnItemSelectedListener = this;
        alertParams.mPositiveButtonText = getString(17040151);
        alertParams.mPositiveButtonListener = this;
        alertParams.mOnPrepareListViewListener = this;
        updateApps(getIntent());
        if (this.mPackages == null) {
            Log.w("ForegroundServicesDialog", "No packages supplied");
            finish();
            return;
        }
        setupAlert();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void onNewIntent(Intent intent) {
        ForegroundServicesDialog.super.onNewIntent(intent);
        updateApps(intent);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void onPause() {
        ForegroundServicesDialog.super.onPause();
        this.mMetricsLogger.hidden(944);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void onResume() {
        ForegroundServicesDialog.super.onResume();
        this.mMetricsLogger.visible(944);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void onStop() {
        ForegroundServicesDialog.super.onStop();
        if (!isChangingConfigurations()) {
            finish();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        finish();
    }
}
