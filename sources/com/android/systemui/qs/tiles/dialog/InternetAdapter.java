package com.android.systemui.qs.tiles.dialog;

import android.content.Context;
import android.content.Intent;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline3;
import com.android.settingslib.Utils;
import com.android.settingslib.wifi.WifiUtils;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.qs.tiles.dialog.InternetAdapter;
import com.android.systemui.qs.tiles.dialog.InternetDialogController;
import com.android.wifitrackerlib.WifiEntry;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
/* loaded from: classes.dex */
public final class InternetAdapter extends RecyclerView.Adapter<InternetViewHolder> {
    public View mHolderView;
    public final InternetDialogController mInternetDialogController;
    @VisibleForTesting
    public int mMaxEntriesCount = 3;
    public List<WifiEntry> mWifiEntries;
    @VisibleForTesting
    public int mWifiEntriesCount;

    /* loaded from: classes.dex */
    public static class InternetViewHolder extends RecyclerView.ViewHolder {
        public final Context mContext;
        public final InternetDialogController mInternetDialogController;
        public final ImageView mWifiEndIcon;
        public final ImageView mWifiIcon;
        @VisibleForTesting
        public WifiUtils.InternetIconInjector mWifiIconInjector;
        public final LinearLayout mWifiListLayout;
        public final TextView mWifiSummaryText;
        public final TextView mWifiTitleText;

        public InternetViewHolder(View view, InternetDialogController internetDialogController) {
            super(view);
            this.mContext = view.getContext();
            this.mInternetDialogController = internetDialogController;
            LinearLayout linearLayout = (LinearLayout) view.requireViewById(2131428131);
            this.mWifiListLayout = (LinearLayout) view.requireViewById(2131429259);
            LinearLayout linearLayout2 = (LinearLayout) view.requireViewById(2131429261);
            this.mWifiIcon = (ImageView) view.requireViewById(2131429257);
            this.mWifiTitleText = (TextView) view.requireViewById(2131429270);
            this.mWifiSummaryText = (TextView) view.requireViewById(2131429269);
            this.mWifiEndIcon = (ImageView) view.requireViewById(2131429255);
            Objects.requireNonNull(internetDialogController);
            this.mWifiIconInjector = internetDialogController.mWifiIconInjector;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final void onBindViewHolder(InternetViewHolder internetViewHolder, int i) {
        Drawable drawable;
        Drawable icon;
        final InternetViewHolder internetViewHolder2 = internetViewHolder;
        List<WifiEntry> list = this.mWifiEntries;
        if (list != null && i < this.mWifiEntriesCount) {
            final WifiEntry wifiEntry = list.get(i);
            ImageView imageView = internetViewHolder2.mWifiIcon;
            Objects.requireNonNull(wifiEntry);
            int i2 = wifiEntry.mLevel;
            boolean shouldShowXLevelIcon = wifiEntry.shouldShowXLevelIcon();
            Drawable drawable2 = null;
            if (i2 == -1 || (icon = internetViewHolder2.mWifiIconInjector.getIcon(shouldShowXLevelIcon, i2)) == null) {
                drawable = null;
            } else {
                icon.setTint(Utils.getColorAttrDefaultColor(internetViewHolder2.mContext, 16843282));
                AtomicReference atomicReference = new AtomicReference();
                atomicReference.set(icon);
                drawable = (Drawable) atomicReference.get();
            }
            imageView.setImageDrawable(drawable);
            String title = wifiEntry.getTitle();
            Spanned fromHtml = Html.fromHtml(wifiEntry.getSummary(false), 0);
            internetViewHolder2.mWifiTitleText.setText(title);
            if (TextUtils.isEmpty(fromHtml)) {
                internetViewHolder2.mWifiSummaryText.setVisibility(8);
            } else {
                internetViewHolder2.mWifiSummaryText.setVisibility(0);
                internetViewHolder2.mWifiSummaryText.setText(fromHtml);
            }
            int connectedState = wifiEntry.getConnectedState();
            char c = 3;
            switch (com.android.wifitrackerlib.Utils.getSingleSecurityTypeFromMultipleSecurityTypes(wifiEntry.getSecurityTypes())) {
                case 1:
                    c = 1;
                    break;
                case 2:
                    c = 2;
                    break;
                case 3:
                case QSTileImpl.H.STALE /* 11 */:
                case KeyguardSliceProvider.ALARM_VISIBILITY_HOURS /* 12 */:
                    break;
                case 4:
                    c = 5;
                    break;
                case 5:
                    c = 6;
                    break;
                case FalsingManager.VERSION /* 6 */:
                    c = 4;
                    break;
                case 7:
                case 8:
                case 10:
                default:
                    c = 0;
                    break;
                case 9:
                    c = 7;
                    break;
            }
            if (connectedState != 0) {
                drawable2 = internetViewHolder2.mContext.getDrawable(2131232260);
            } else if (!(c == 0 || c == 4)) {
                drawable2 = internetViewHolder2.mContext.getDrawable(2131231955);
            }
            if (drawable2 == null) {
                internetViewHolder2.mWifiEndIcon.setVisibility(8);
            } else {
                internetViewHolder2.mWifiEndIcon.setVisibility(0);
                internetViewHolder2.mWifiEndIcon.setImageDrawable(drawable2);
            }
            if (connectedState != 0) {
                internetViewHolder2.mWifiListLayout.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.qs.tiles.dialog.InternetAdapter$InternetViewHolder$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        InternetAdapter.InternetViewHolder internetViewHolder3 = InternetAdapter.InternetViewHolder.this;
                        WifiEntry wifiEntry2 = wifiEntry;
                        Objects.requireNonNull(internetViewHolder3);
                        internetViewHolder3.mInternetDialogController.launchWifiNetworkDetailsSetting(wifiEntry2.getKey(), view);
                    }
                });
            } else {
                internetViewHolder2.mWifiListLayout.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.qs.tiles.dialog.InternetAdapter$InternetViewHolder$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        InternetAdapter.InternetViewHolder internetViewHolder3 = InternetAdapter.InternetViewHolder.this;
                        WifiEntry wifiEntry2 = wifiEntry;
                        Objects.requireNonNull(internetViewHolder3);
                        if (wifiEntry2.shouldEditBeforeConnect()) {
                            Intent intent = new Intent("com.android.settings.WIFI_DIALOG");
                            intent.addFlags(268435456);
                            intent.addFlags(131072);
                            intent.putExtra("key_chosen_wifientry_key", wifiEntry2.getKey());
                            intent.putExtra("connect_for_caller", false);
                            internetViewHolder3.mContext.startActivity(intent);
                        }
                        InternetDialogController internetDialogController = internetViewHolder3.mInternetDialogController;
                        Objects.requireNonNull(internetDialogController);
                        if (wifiEntry2.getWifiConfiguration() != null) {
                            if (InternetDialogController.DEBUG) {
                                KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(VendorAtomValue$$ExternalSyntheticOutline1.m("connect networkId="), wifiEntry2.getWifiConfiguration().networkId, "InternetDialogController");
                            }
                        } else if (InternetDialogController.DEBUG) {
                            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("connect to unsaved network ");
                            m.append(wifiEntry2.getTitle());
                            Log.d("InternetDialogController", m.toString());
                        }
                        wifiEntry2.connect(new InternetDialogController.WifiEntryConnectCallback(internetDialogController.mActivityStarter, wifiEntry2, internetDialogController));
                    }
                });
            }
        }
    }

    public InternetAdapter(InternetDialogController internetDialogController) {
        this.mInternetDialogController = internetDialogController;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final RecyclerView.ViewHolder onCreateViewHolder(RecyclerView recyclerView, int i) {
        this.mHolderView = LayoutInflater.from(recyclerView.getContext()).inflate(2131624142, (ViewGroup) recyclerView, false);
        return new InternetViewHolder(this.mHolderView, this.mInternetDialogController);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final int getItemCount() {
        return this.mWifiEntriesCount;
    }
}
