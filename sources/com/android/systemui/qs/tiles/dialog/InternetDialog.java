package com.android.systemui.qs.tiles.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SubscriptionInfo;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.internal.logging.UiEventLogger;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline3;
import com.android.settingslib.mobile.MobileMappings;
import com.android.systemui.ImageWallpaper$GLEngine$$ExternalSyntheticLambda0;
import com.android.systemui.Prefs;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.doze.DozeUi$$ExternalSyntheticLambda1;
import com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda1;
import com.android.systemui.people.PeopleSpaceUtils$$ExternalSyntheticLambda4;
import com.android.systemui.qs.tiles.dialog.InternetDialogController;
import com.android.systemui.statusbar.phone.DozeParameters$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.wifitrackerlib.WifiEntry;
import com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda12;
import com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda8;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/* loaded from: classes.dex */
public final class InternetDialog extends SystemUIDialog implements InternetDialogController.InternetDialogCallback {
    public static final boolean DEBUG = Log.isLoggable("InternetDialog", 3);
    public InternetAdapter mAdapter;
    public Button mAirplaneModeButton;
    public TextView mAirplaneModeSummaryText;
    public AlertDialog mAlertDialog;
    public final Executor mBackgroundExecutor;
    public Drawable mBackgroundOn;
    public boolean mCanConfigMobileData;
    public boolean mCanConfigWifi;
    public LinearLayout mConnectedWifListLayout;
    public WifiEntry mConnectedWifiEntry;
    public ImageView mConnectedWifiIcon;
    public TextView mConnectedWifiSummaryText;
    public TextView mConnectedWifiTitleText;
    public Context mContext;
    public int mDefaultDataSubId;
    public View mDialogView;
    public View mDivider;
    public Button mDoneButton;
    public LinearLayout mEthernetLayout;
    public final Handler mHandler;
    public boolean mHasMoreWifiEntries;
    public InternetDialogController mInternetDialogController;
    public InternetDialogFactory mInternetDialogFactory;
    public TextView mInternetDialogSubTitle;
    public TextView mInternetDialogTitle;
    public boolean mIsProgressBarVisible;
    public boolean mIsSearchingHidden;
    public KeyguardStateController mKeyguard;
    public Switch mMobileDataToggle;
    public LinearLayout mMobileNetworkLayout;
    public TextView mMobileSummaryText;
    public TextView mMobileTitleText;
    public View mMobileToggleDivider;
    public ProgressBar mProgressBar;
    public LinearLayout mSeeAllLayout;
    public ImageView mSignalIcon;
    public TelephonyManager mTelephonyManager;
    public LinearLayout mTurnWifiOnLayout;
    public UiEventLogger mUiEventLogger;
    public Switch mWiFiToggle;
    public int mWifiEntriesCount;
    public WifiManager mWifiManager;
    public int mWifiNetworkHeight;
    public RecyclerView mWifiRecyclerView;
    public LinearLayout mWifiScanNotifyLayout;
    public TextView mWifiScanNotifyText;
    public ImageView mWifiSettingsIcon;
    public TextView mWifiToggleTitleText;
    public Drawable mBackgroundOff = null;
    public final DozeUi$$ExternalSyntheticLambda1 mHideProgressBarRunnable = new DozeUi$$ExternalSyntheticLambda1(this, 5);
    public ImageWallpaper$GLEngine$$ExternalSyntheticLambda0 mHideSearchingRunnable = new ImageWallpaper$GLEngine$$ExternalSyntheticLambda0(this, 4);

    /* JADX WARN: Failed to restore enum class, 'enum' modifier removed */
    /* loaded from: classes.dex */
    public static final class InternetDialogEvent extends Enum<InternetDialogEvent> implements UiEventLogger.UiEventEnum {
        public static final /* synthetic */ InternetDialogEvent[] $VALUES;
        public static final InternetDialogEvent INTERNET_DIALOG_SHOW;
        private final int mId = 843;

        static {
            InternetDialogEvent internetDialogEvent = new InternetDialogEvent();
            INTERNET_DIALOG_SHOW = internetDialogEvent;
            $VALUES = new InternetDialogEvent[]{internetDialogEvent};
        }

        public static InternetDialogEvent valueOf(String str) {
            return (InternetDialogEvent) Enum.valueOf(InternetDialogEvent.class, str);
        }

        public static InternetDialogEvent[] values() {
            return (InternetDialogEvent[]) $VALUES.clone();
        }

        public final int getId() {
            return this.mId;
        }
    }

    public void hideWifiViews() {
        setProgressBarVisible(false);
        this.mTurnWifiOnLayout.setVisibility(8);
        this.mConnectedWifListLayout.setVisibility(8);
        this.mWifiRecyclerView.setVisibility(8);
        this.mSeeAllLayout.setVisibility(8);
    }

    public final CharSequence getDialogTitleText() {
        InternetDialogController internetDialogController = this.mInternetDialogController;
        Objects.requireNonNull(internetDialogController);
        if (internetDialogController.isAirplaneModeEnabled()) {
            return internetDialogController.mContext.getText(2131951873);
        }
        return internetDialogController.mContext.getText(2131953115);
    }

    public final CharSequence getMobileNetworkTitle() {
        final InternetDialogController internetDialogController = this.mInternetDialogController;
        Objects.requireNonNull(internetDialogController);
        int i = internetDialogController.mDefaultDataSubId;
        final Context context = internetDialogController.mContext;
        Supplier internetDialogController$$ExternalSyntheticLambda6 = new Supplier() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialogController$$ExternalSyntheticLambda6
            @Override // java.util.function.Supplier
            public final Object get() {
                final InternetDialogController internetDialogController2 = InternetDialogController.this;
                Objects.requireNonNull(internetDialogController2);
                return internetDialogController2.mKeyguardUpdateMonitor.getFilteredSubscriptionInfo().stream().filter(InternetDialogController$$ExternalSyntheticLambda5.INSTANCE).map(new Function() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialogController$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        SubscriptionInfo subscriptionInfo = (SubscriptionInfo) obj;
                        Objects.requireNonNull(InternetDialogController.this);
                        return new InternetDialogController.C1DisplayInfo(subscriptionInfo, subscriptionInfo.getDisplayName().toString().trim());
                    }
                });
            }
        };
        final HashSet hashSet = new HashSet();
        final Set set = (Set) ((Stream) internetDialogController$$ExternalSyntheticLambda6.get()).filter(new Predicate() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialogController$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return !hashSet.add(((InternetDialogController.C1DisplayInfo) obj).originalName);
            }
        }).map(WifiPickerTracker$$ExternalSyntheticLambda8.INSTANCE$1).collect(Collectors.toSet());
        hashSet.clear();
        final Set set2 = (Set) ((Stream) internetDialogController$$ExternalSyntheticLambda6.get()).map(new Function() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialogController$$ExternalSyntheticLambda2
            /* JADX WARN: Removed duplicated region for block: B:20:0x0062  */
            /* JADX WARN: Removed duplicated region for block: B:23:0x0073  */
            /* JADX WARN: Removed duplicated region for block: B:26:0x007b  */
            /* JADX WARN: Removed duplicated region for block: B:27:0x0080  */
            @Override // java.util.function.Function
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public final java.lang.Object apply(java.lang.Object r5) {
                /*
                    r4 = this;
                    java.util.Set r0 = r1
                    android.content.Context r4 = r2
                    com.android.systemui.qs.tiles.dialog.InternetDialogController$1DisplayInfo r5 = (com.android.systemui.qs.tiles.dialog.InternetDialogController.C1DisplayInfo) r5
                    java.lang.CharSequence r1 = r5.originalName
                    boolean r0 = r0.contains(r1)
                    if (r0 == 0) goto L_0x0099
                    android.telephony.SubscriptionInfo r0 = r5.subscriptionInfo
                    if (r0 == 0) goto L_0x0055
                    int r0 = r0.getSubscriptionId()
                    java.lang.String r1 = android.os.Build.VERSION.CODENAME
                    java.lang.String r2 = "REL"
                    boolean r2 = r2.equals(r1)
                    r3 = 0
                    if (r2 == 0) goto L_0x0022
                    goto L_0x002b
                L_0x0022:
                    java.lang.String r2 = "T"
                    int r1 = r1.compareTo(r2)
                    if (r1 < 0) goto L_0x002b
                    r3 = 1
                L_0x002b:
                    if (r3 == 0) goto L_0x003a
                    java.lang.Class<android.telephony.SubscriptionManager> r1 = android.telephony.SubscriptionManager.class
                    java.lang.Object r4 = r4.getSystemService(r1)
                    android.telephony.SubscriptionManager r4 = (android.telephony.SubscriptionManager) r4
                    java.lang.String r4 = r4.getPhoneNumber(r0)
                    goto L_0x004a
                L_0x003a:
                    java.lang.Class<android.telephony.TelephonyManager> r1 = android.telephony.TelephonyManager.class
                    java.lang.Object r4 = r4.getSystemService(r1)
                    android.telephony.TelephonyManager r4 = (android.telephony.TelephonyManager) r4
                    android.telephony.TelephonyManager r4 = r4.createForSubscriptionId(r0)
                    java.lang.String r4 = r4.getLine1Number()
                L_0x004a:
                    boolean r0 = android.text.TextUtils.isEmpty(r4)
                    if (r0 != 0) goto L_0x0055
                    java.lang.String r4 = android.telephony.PhoneNumberUtils.formatNumber(r4)
                    goto L_0x0056
                L_0x0055:
                    r4 = 0
                L_0x0056:
                    android.text.BidiFormatter r0 = android.text.BidiFormatter.getInstance()
                    android.text.TextDirectionHeuristic r1 = android.text.TextDirectionHeuristics.LTR
                    java.lang.String r4 = r0.unicodeWrap(r4, r1)
                    if (r4 == 0) goto L_0x0073
                    int r0 = r4.length()
                    r1 = 4
                    if (r0 <= r1) goto L_0x0075
                    int r0 = r4.length()
                    int r0 = r0 - r1
                    java.lang.String r4 = r4.substring(r0)
                    goto L_0x0075
                L_0x0073:
                    java.lang.String r4 = ""
                L_0x0075:
                    boolean r0 = android.text.TextUtils.isEmpty(r4)
                    if (r0 == 0) goto L_0x0080
                    java.lang.CharSequence r4 = r5.originalName
                    r5.uniqueName = r4
                    goto L_0x009d
                L_0x0080:
                    java.lang.StringBuilder r0 = new java.lang.StringBuilder
                    r0.<init>()
                    java.lang.CharSequence r1 = r5.originalName
                    r0.append(r1)
                    java.lang.String r1 = " "
                    r0.append(r1)
                    r0.append(r4)
                    java.lang.String r4 = r0.toString()
                    r5.uniqueName = r4
                    goto L_0x009d
                L_0x0099:
                    java.lang.CharSequence r4 = r5.originalName
                    r5.uniqueName = r4
                L_0x009d:
                    return r5
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.tiles.dialog.InternetDialogController$$ExternalSyntheticLambda2.apply(java.lang.Object):java.lang.Object");
            }
        }).filter(new WifiPickerTracker$$ExternalSyntheticLambda12(hashSet, 2)).map(InternetDialogController$$ExternalSyntheticLambda3.INSTANCE).collect(Collectors.toSet());
        return (CharSequence) ((Map) ((Stream) internetDialogController$$ExternalSyntheticLambda6.get()).map(new Function() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialogController$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public final java.lang.Object apply(java.lang.Object r5) {
                /*
                    r4 = this;
                    java.util.Set r0 = r1
                    android.content.Context r4 = r2
                    com.android.systemui.qs.tiles.dialog.InternetDialogController$1DisplayInfo r5 = (com.android.systemui.qs.tiles.dialog.InternetDialogController.C1DisplayInfo) r5
                    java.lang.CharSequence r1 = r5.originalName
                    boolean r0 = r0.contains(r1)
                    if (r0 == 0) goto L_0x0099
                    android.telephony.SubscriptionInfo r0 = r5.subscriptionInfo
                    if (r0 == 0) goto L_0x0055
                    int r0 = r0.getSubscriptionId()
                    java.lang.String r1 = android.os.Build.VERSION.CODENAME
                    java.lang.String r2 = "REL"
                    boolean r2 = r2.equals(r1)
                    r3 = 0
                    if (r2 == 0) goto L_0x0022
                    goto L_0x002b
                L_0x0022:
                    java.lang.String r2 = "T"
                    int r1 = r1.compareTo(r2)
                    if (r1 < 0) goto L_0x002b
                    r3 = 1
                L_0x002b:
                    if (r3 == 0) goto L_0x003a
                    java.lang.Class<android.telephony.SubscriptionManager> r1 = android.telephony.SubscriptionManager.class
                    java.lang.Object r4 = r4.getSystemService(r1)
                    android.telephony.SubscriptionManager r4 = (android.telephony.SubscriptionManager) r4
                    java.lang.String r4 = r4.getPhoneNumber(r0)
                    goto L_0x004a
                L_0x003a:
                    java.lang.Class<android.telephony.TelephonyManager> r1 = android.telephony.TelephonyManager.class
                    java.lang.Object r4 = r4.getSystemService(r1)
                    android.telephony.TelephonyManager r4 = (android.telephony.TelephonyManager) r4
                    android.telephony.TelephonyManager r4 = r4.createForSubscriptionId(r0)
                    java.lang.String r4 = r4.getLine1Number()
                L_0x004a:
                    boolean r0 = android.text.TextUtils.isEmpty(r4)
                    if (r0 != 0) goto L_0x0055
                    java.lang.String r4 = android.telephony.PhoneNumberUtils.formatNumber(r4)
                    goto L_0x0056
                L_0x0055:
                    r4 = 0
                L_0x0056:
                    android.text.BidiFormatter r0 = android.text.BidiFormatter.getInstance()
                    android.text.TextDirectionHeuristic r1 = android.text.TextDirectionHeuristics.LTR
                    java.lang.String r4 = r0.unicodeWrap(r4, r1)
                    if (r4 == 0) goto L_0x0073
                    int r0 = r4.length()
                    r1 = 4
                    if (r0 <= r1) goto L_0x0075
                    int r0 = r4.length()
                    int r0 = r0 - r1
                    java.lang.String r4 = r4.substring(r0)
                    goto L_0x0075
                L_0x0073:
                    java.lang.String r4 = ""
                L_0x0075:
                    boolean r0 = android.text.TextUtils.isEmpty(r4)
                    if (r0 == 0) goto L_0x0080
                    java.lang.CharSequence r4 = r5.originalName
                    r5.uniqueName = r4
                    goto L_0x009d
                L_0x0080:
                    java.lang.StringBuilder r0 = new java.lang.StringBuilder
                    r0.<init>()
                    java.lang.CharSequence r1 = r5.originalName
                    r0.append(r1)
                    java.lang.String r1 = " "
                    r0.append(r1)
                    r0.append(r4)
                    java.lang.String r4 = r0.toString()
                    r5.uniqueName = r4
                    goto L_0x009d
                L_0x0099:
                    java.lang.CharSequence r4 = r5.originalName
                    r5.uniqueName = r4
                L_0x009d:
                    return r5
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.tiles.dialog.InternetDialogController$$ExternalSyntheticLambda2.apply(java.lang.Object):java.lang.Object");
            }
        }).map(new Function() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialogController$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                InternetDialogController.C1DisplayInfo r2 = (InternetDialogController.C1DisplayInfo) obj;
                if (set2.contains(r2.uniqueName)) {
                    r2.uniqueName = ((Object) r2.originalName) + " " + r2.subscriptionInfo.getSubscriptionId();
                }
                return r2;
            }
        }).collect(Collectors.toMap(DozeParameters$$ExternalSyntheticLambda0.INSTANCE$2, PeopleSpaceUtils$$ExternalSyntheticLambda4.INSTANCE$1))).getOrDefault(Integer.valueOf(i), "");
    }

    public final CharSequence getSubtitleText() {
        boolean z;
        InternetDialogController internetDialogController = this.mInternetDialogController;
        if (!this.mIsProgressBarVisible || this.mIsSearchingHidden) {
            z = false;
        } else {
            z = true;
        }
        Objects.requireNonNull(internetDialogController);
        if (internetDialogController.mCanConfigWifi && !internetDialogController.mWifiManager.isWifiEnabled()) {
            if (InternetDialogController.DEBUG) {
                Log.d("InternetDialogController", "Wi-Fi off.");
            }
            return internetDialogController.mContext.getText(InternetDialogController.SUBTITLE_TEXT_WIFI_IS_OFF);
        } else if (!internetDialogController.mKeyguardStateController.isUnlocked()) {
            if (InternetDialogController.DEBUG) {
                Log.d("InternetDialogController", "The device is locked.");
            }
            return internetDialogController.mContext.getText(InternetDialogController.SUBTITLE_TEXT_UNLOCK_TO_VIEW_NETWORKS);
        } else {
            if (internetDialogController.mHasWifiEntries) {
                if (internetDialogController.mCanConfigWifi) {
                    return internetDialogController.mContext.getText(InternetDialogController.SUBTITLE_TEXT_TAP_A_NETWORK_TO_CONNECT);
                }
            } else if (internetDialogController.mCanConfigWifi && z) {
                return internetDialogController.mContext.getText(InternetDialogController.SUBTITLE_TEXT_SEARCHING_FOR_NETWORKS);
            } else {
                if (internetDialogController.isCarrierNetworkActive()) {
                    return internetDialogController.mContext.getText(InternetDialogController.SUBTITLE_TEXT_NON_CARRIER_NETWORK_UNAVAILABLE);
                }
                boolean z2 = InternetDialogController.DEBUG;
                if (z2) {
                    Log.d("InternetDialogController", "No Wi-Fi item.");
                }
                if (!internetDialogController.hasActiveSubId() || (!internetDialogController.isVoiceStateInService() && !internetDialogController.isDataStateInService())) {
                    if (z2) {
                        Log.d("InternetDialogController", "No carrier or service is out of service.");
                    }
                    return internetDialogController.mContext.getText(InternetDialogController.SUBTITLE_TEXT_ALL_CARRIER_NETWORK_UNAVAILABLE);
                } else if (internetDialogController.mCanConfigWifi && !internetDialogController.isMobileDataEnabled()) {
                    if (z2) {
                        Log.d("InternetDialogController", "Mobile data off");
                    }
                    return internetDialogController.mContext.getText(InternetDialogController.SUBTITLE_TEXT_NON_CARRIER_NETWORK_UNAVAILABLE);
                } else if (!internetDialogController.activeNetworkIsCellular()) {
                    if (z2) {
                        Log.d("InternetDialogController", "No carrier data.");
                    }
                    return internetDialogController.mContext.getText(InternetDialogController.SUBTITLE_TEXT_ALL_CARRIER_NETWORK_UNAVAILABLE);
                } else if (internetDialogController.mCanConfigWifi) {
                    return internetDialogController.mContext.getText(InternetDialogController.SUBTITLE_TEXT_NON_CARRIER_NETWORK_UNAVAILABLE);
                }
            }
            return null;
        }
    }

    public int getWifiListMaxCount() {
        int i;
        int i2 = 3;
        if (this.mEthernetLayout.getVisibility() == 0) {
            i = 3;
        } else {
            i = 4;
        }
        if (this.mMobileNetworkLayout.getVisibility() == 0) {
            i--;
        }
        if (i <= 3) {
            i2 = i;
        }
        if (this.mConnectedWifListLayout.getVisibility() == 0) {
            return i2 - 1;
        }
        return i2;
    }

    public final void setProgressBarVisible(boolean z) {
        int i;
        if (this.mIsProgressBarVisible != z) {
            this.mIsProgressBarVisible = z;
            ProgressBar progressBar = this.mProgressBar;
            int i2 = 0;
            if (z) {
                i = 0;
            } else {
                i = 8;
            }
            progressBar.setVisibility(i);
            this.mProgressBar.setIndeterminate(z);
            View view = this.mDivider;
            if (z) {
                i2 = 8;
            }
            view.setVisibility(i2);
            this.mInternetDialogSubTitle.setText(getSubtitleText());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:126:0x0272  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0282  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x028f  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x02f5 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:170:0x034b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x010d  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0115  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x014c  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x015b  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x016f  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0171  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0177  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x017b  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x018b  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x01a4  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x01ad  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x01b0  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x01d2  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x01e2  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x01e4  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x01ee  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x01f0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateDialog(boolean r14) {
        /*
            Method dump skipped, instructions count: 920
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.tiles.dialog.InternetDialog.updateDialog(boolean):void");
    }

    public InternetDialog(Context context, InternetDialogFactory internetDialogFactory, InternetDialogController internetDialogController, boolean z, boolean z2, UiEventLogger uiEventLogger, Handler handler, Executor executor, KeyguardStateController keyguardStateController) {
        super(context);
        this.mDefaultDataSubId = -1;
        if (DEBUG) {
            Log.d("InternetDialog", "Init InternetDialog");
        }
        this.mContext = getContext();
        this.mHandler = handler;
        this.mBackgroundExecutor = executor;
        this.mInternetDialogFactory = internetDialogFactory;
        this.mInternetDialogController = internetDialogController;
        Objects.requireNonNull(internetDialogController);
        this.mDefaultDataSubId = this.mInternetDialogController.getDefaultDataSubscriptionId();
        InternetDialogController internetDialogController2 = this.mInternetDialogController;
        Objects.requireNonNull(internetDialogController2);
        this.mTelephonyManager = internetDialogController2.mTelephonyManager;
        InternetDialogController internetDialogController3 = this.mInternetDialogController;
        Objects.requireNonNull(internetDialogController3);
        this.mWifiManager = internetDialogController3.mWifiManager;
        this.mCanConfigMobileData = z;
        this.mCanConfigWifi = z2;
        this.mKeyguard = keyguardStateController;
        this.mUiEventLogger = uiEventLogger;
        this.mAdapter = new InternetAdapter(this.mInternetDialogController);
    }

    /* JADX WARN: Finally extract failed */
    @Override // com.android.systemui.statusbar.phone.SystemUIDialog, android.app.AlertDialog, android.app.Dialog
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (DEBUG) {
            Log.d("InternetDialog", "onCreate");
        }
        this.mUiEventLogger.log(InternetDialogEvent.INTERNET_DIALOG_SHOW);
        this.mDialogView = LayoutInflater.from(this.mContext).inflate(2131624141, (ViewGroup) null);
        Window window = getWindow();
        window.setContentView(this.mDialogView);
        window.setWindowAnimations(2132017162);
        this.mWifiNetworkHeight = this.mContext.getResources().getDimensionPixelSize(2131165819);
        LinearLayout linearLayout = (LinearLayout) this.mDialogView.requireViewById(2131428130);
        this.mInternetDialogTitle = (TextView) this.mDialogView.requireViewById(2131428133);
        this.mInternetDialogSubTitle = (TextView) this.mDialogView.requireViewById(2131428132);
        this.mDivider = this.mDialogView.requireViewById(2131427855);
        this.mProgressBar = (ProgressBar) this.mDialogView.requireViewById(2131429265);
        this.mEthernetLayout = (LinearLayout) this.mDialogView.requireViewById(2131427940);
        this.mMobileNetworkLayout = (LinearLayout) this.mDialogView.requireViewById(2131428377);
        this.mTurnWifiOnLayout = (LinearLayout) this.mDialogView.requireViewById(2131429115);
        this.mWifiToggleTitleText = (TextView) this.mDialogView.requireViewById(2131429272);
        this.mWifiScanNotifyLayout = (LinearLayout) this.mDialogView.requireViewById(2131429263);
        this.mWifiScanNotifyText = (TextView) this.mDialogView.requireViewById(2131429264);
        this.mConnectedWifListLayout = (LinearLayout) this.mDialogView.requireViewById(2131429252);
        this.mConnectedWifiIcon = (ImageView) this.mDialogView.requireViewById(2131429251);
        this.mConnectedWifiTitleText = (TextView) this.mDialogView.requireViewById(2131429254);
        this.mConnectedWifiSummaryText = (TextView) this.mDialogView.requireViewById(2131429253);
        this.mWifiSettingsIcon = (ImageView) this.mDialogView.requireViewById(2131429266);
        this.mWifiRecyclerView = (RecyclerView) this.mDialogView.requireViewById(2131429260);
        this.mSeeAllLayout = (LinearLayout) this.mDialogView.requireViewById(2131428818);
        this.mDoneButton = (Button) this.mDialogView.requireViewById(2131427866);
        this.mAirplaneModeButton = (Button) this.mDialogView.requireViewById(2131427493);
        this.mSignalIcon = (ImageView) this.mDialogView.requireViewById(2131428860);
        this.mMobileTitleText = (TextView) this.mDialogView.requireViewById(2131428384);
        this.mMobileSummaryText = (TextView) this.mDialogView.requireViewById(2131428383);
        this.mAirplaneModeSummaryText = (TextView) this.mDialogView.requireViewById(2131427465);
        this.mMobileToggleDivider = this.mDialogView.requireViewById(2131428386);
        this.mMobileDataToggle = (Switch) this.mDialogView.requireViewById(2131428385);
        this.mWiFiToggle = (Switch) this.mDialogView.requireViewById(2131429271);
        this.mBackgroundOn = this.mContext.getDrawable(2131232651);
        this.mInternetDialogTitle.setText(getDialogTitleText());
        this.mInternetDialogTitle.setGravity(8388627);
        int i = 0;
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(new int[]{16843534});
        try {
            this.mBackgroundOff = obtainStyledAttributes.getDrawable(0);
            obtainStyledAttributes.recycle();
            this.mMobileNetworkLayout.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda7
                /* JADX WARN: Code restructure failed: missing block: B:14:0x003b, code lost:
                    if (r4.mIsCellDefaultRoute == false) goto L_0x003f;
                 */
                @Override // android.view.View.OnClickListener
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public final void onClick(android.view.View r4) {
                    /*
                        r3 = this;
                        com.android.systemui.qs.tiles.dialog.InternetDialog r3 = com.android.systemui.qs.tiles.dialog.InternetDialog.this
                        java.util.Objects.requireNonNull(r3)
                        com.android.systemui.qs.tiles.dialog.InternetDialogController r4 = r3.mInternetDialogController
                        boolean r4 = r4.isMobileDataEnabled()
                        if (r4 == 0) goto L_0x0050
                        com.android.systemui.qs.tiles.dialog.InternetDialogController r4 = r3.mInternetDialogController
                        java.util.Objects.requireNonNull(r4)
                        com.android.systemui.statusbar.policy.KeyguardStateController r4 = r4.mKeyguardStateController
                        boolean r4 = r4.isUnlocked()
                        r0 = 1
                        r4 = r4 ^ r0
                        if (r4 != 0) goto L_0x0050
                        com.android.systemui.qs.tiles.dialog.InternetDialogController r4 = r3.mInternetDialogController
                        boolean r4 = r4.activeNetworkIsCellular()
                        if (r4 != 0) goto L_0x0050
                        com.android.systemui.qs.tiles.dialog.InternetDialogController r3 = r3.mInternetDialogController
                        java.util.Objects.requireNonNull(r3)
                        com.android.systemui.statusbar.connectivity.AccessPointController r4 = r3.mAccessPointController
                        com.android.wifitrackerlib.MergedCarrierEntry r4 = r4.getMergedCarrierEntry()
                        if (r4 == 0) goto L_0x0050
                        monitor-enter(r4)
                        int r1 = r4.getConnectedState()     // Catch: all -> 0x004d
                        r2 = 0
                        if (r1 != 0) goto L_0x003e
                        boolean r1 = r4.mIsCellDefaultRoute     // Catch: all -> 0x004d
                        if (r1 != 0) goto L_0x003e
                        goto L_0x003f
                    L_0x003e:
                        r0 = r2
                    L_0x003f:
                        monitor-exit(r4)
                        if (r0 == 0) goto L_0x0050
                        r0 = 0
                        r4.connect(r0, r2)
                        r4 = 2131953611(0x7f1307cb, float:1.9543698E38)
                        r3.makeOverlayToast(r4)
                        goto L_0x0050
                    L_0x004d:
                        r3 = move-exception
                        monitor-exit(r4)
                        throw r3
                    L_0x0050:
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda7.onClick(android.view.View):void");
                }
            });
            this.mMobileDataToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda9
                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    boolean z2;
                    final InternetDialog internetDialog = InternetDialog.this;
                    Objects.requireNonNull(internetDialog);
                    boolean z3 = true;
                    if (!z) {
                        boolean z4 = Prefs.getBoolean(internetDialog.mContext, "QsHasTurnedOffMobileData");
                        if (!internetDialog.mInternetDialogController.isMobileDataEnabled() || z4) {
                            z2 = false;
                        } else {
                            z2 = true;
                        }
                        if (z2) {
                            CharSequence mobileNetworkTitle = internetDialog.getMobileNetworkTitle();
                            boolean isVoiceStateInService = internetDialog.mInternetDialogController.isVoiceStateInService();
                            if (TextUtils.isEmpty(mobileNetworkTitle) || !isVoiceStateInService) {
                                mobileNetworkTitle = internetDialog.mContext.getString(2131952769);
                            }
                            AlertDialog create = new AlertDialog.Builder(internetDialog.mContext).setTitle(2131952770).setMessage(internetDialog.mContext.getString(2131952768, mobileNetworkTitle)).setNegativeButton(17039360, new DialogInterface.OnClickListener() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda1
                                @Override // android.content.DialogInterface.OnClickListener
                                public final void onClick(DialogInterface dialogInterface, int i2) {
                                    InternetDialog internetDialog2 = InternetDialog.this;
                                    Objects.requireNonNull(internetDialog2);
                                    internetDialog2.mMobileDataToggle.setChecked(true);
                                }
                            }).setPositiveButton(17039652, new DialogInterface.OnClickListener() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda2
                                @Override // android.content.DialogInterface.OnClickListener
                                public final void onClick(DialogInterface dialogInterface, int i2) {
                                    InternetDialog internetDialog2 = InternetDialog.this;
                                    Objects.requireNonNull(internetDialog2);
                                    internetDialog2.mInternetDialogController.setMobileDataEnabled(internetDialog2.mContext, internetDialog2.mDefaultDataSubId, false);
                                    internetDialog2.mMobileDataToggle.setChecked(false);
                                    Prefs.putBoolean(internetDialog2.mContext, "QsHasTurnedOffMobileData", true);
                                }
                            }).create();
                            internetDialog.mAlertDialog = create;
                            create.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda0
                                @Override // android.content.DialogInterface.OnCancelListener
                                public final void onCancel(DialogInterface dialogInterface) {
                                    InternetDialog internetDialog2 = InternetDialog.this;
                                    Objects.requireNonNull(internetDialog2);
                                    internetDialog2.mMobileDataToggle.setChecked(true);
                                }
                            });
                            internetDialog.mAlertDialog.getWindow().setType(2009);
                            SystemUIDialog.setShowForAllUsers(internetDialog.mAlertDialog);
                            SystemUIDialog.registerDismissListener(internetDialog.mAlertDialog);
                            SystemUIDialog.setWindowOnTop(internetDialog.mAlertDialog, internetDialog.mKeyguard.isShowing());
                            internetDialog.mAlertDialog.show();
                            return;
                        }
                    }
                    boolean z5 = Prefs.getBoolean(internetDialog.mContext, "QsHasTurnedOffMobileData");
                    if (!internetDialog.mInternetDialogController.isMobileDataEnabled() || z5) {
                        z3 = false;
                    }
                    if (!z3) {
                        internetDialog.mInternetDialogController.setMobileDataEnabled(internetDialog.mContext, internetDialog.mDefaultDataSubId, z);
                    }
                }
            });
            this.mConnectedWifListLayout.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda6
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    InternetDialog internetDialog = InternetDialog.this;
                    Objects.requireNonNull(internetDialog);
                    WifiEntry wifiEntry = internetDialog.mConnectedWifiEntry;
                    if (wifiEntry != null) {
                        internetDialog.mInternetDialogController.launchWifiNetworkDetailsSetting(wifiEntry.getKey(), view);
                    }
                }
            });
            this.mSeeAllLayout.setOnClickListener(new InternetDialog$$ExternalSyntheticLambda5(this, 0));
            this.mWiFiToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda8
                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    InternetDialog internetDialog = InternetDialog.this;
                    Objects.requireNonNull(internetDialog);
                    if (internetDialog.mWifiManager != null) {
                        compoundButton.setChecked(z);
                        internetDialog.mWifiManager.setWifiEnabled(z);
                    }
                }
            });
            this.mDoneButton.setOnClickListener(new MediaControlPanel$$ExternalSyntheticLambda1(this, 1));
            this.mAirplaneModeButton.setOnClickListener(new InternetDialog$$ExternalSyntheticLambda4(this, 0));
            this.mTurnWifiOnLayout.setBackground(null);
            Button button = this.mAirplaneModeButton;
            if (!this.mInternetDialogController.isAirplaneModeEnabled()) {
                i = 8;
            }
            button.setVisibility(i);
            this.mWifiRecyclerView.setLayoutManager(new LinearLayoutManager(1));
            this.mWifiRecyclerView.setAdapter(this.mAdapter);
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    @Override // com.android.systemui.statusbar.phone.SystemUIDialog, android.app.Dialog
    public final void onStart() {
        super.onStart();
        if (DEBUG) {
            Log.d("InternetDialog", "onStart");
        }
        InternetDialogController internetDialogController = this.mInternetDialogController;
        boolean z = this.mCanConfigWifi;
        boolean z2 = InternetDialogController.DEBUG;
        if (z2) {
            Objects.requireNonNull(internetDialogController);
            Log.d("InternetDialogController", "onStart");
        }
        internetDialogController.mCallback = this;
        internetDialogController.mKeyguardUpdateMonitor.registerCallback(internetDialogController.mKeyguardUpdateCallback);
        internetDialogController.mAccessPointController.addAccessPointCallback(internetDialogController);
        BroadcastDispatcher broadcastDispatcher = internetDialogController.mBroadcastDispatcher;
        InternetDialogController.AnonymousClass2 r6 = internetDialogController.mConnectionStateReceiver;
        IntentFilter intentFilter = internetDialogController.mConnectionStateFilter;
        Executor executor = internetDialogController.mExecutor;
        Objects.requireNonNull(broadcastDispatcher);
        BroadcastDispatcher.registerReceiver$default(broadcastDispatcher, r6, intentFilter, executor, null, 24);
        InternetDialogController.InternetOnSubscriptionChangedListener internetOnSubscriptionChangedListener = new InternetDialogController.InternetOnSubscriptionChangedListener();
        internetDialogController.mOnSubscriptionsChangedListener = internetOnSubscriptionChangedListener;
        internetDialogController.mSubscriptionManager.addOnSubscriptionsChangedListener(internetDialogController.mExecutor, internetOnSubscriptionChangedListener);
        internetDialogController.mDefaultDataSubId = internetDialogController.getDefaultDataSubscriptionId();
        if (z2) {
            KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(VendorAtomValue$$ExternalSyntheticOutline1.m("Init, SubId: "), internetDialogController.mDefaultDataSubId, "InternetDialogController");
        }
        internetDialogController.mConfig = MobileMappings.Config.readConfig(internetDialogController.mContext);
        internetDialogController.mTelephonyManager = internetDialogController.mTelephonyManager.createForSubscriptionId(internetDialogController.mDefaultDataSubId);
        TelephonyCallback internetTelephonyCallback = new InternetDialogController.InternetTelephonyCallback();
        internetDialogController.mInternetTelephonyCallback = internetTelephonyCallback;
        internetDialogController.mTelephonyManager.registerTelephonyCallback(internetDialogController.mExecutor, internetTelephonyCallback);
        internetDialogController.mConnectivityManager.registerDefaultNetworkCallback(internetDialogController.mConnectivityManagerNetworkCallback);
        internetDialogController.mCanConfigWifi = z;
        if (z) {
            internetDialogController.mAccessPointController.scanForAccessPoints();
        }
        if (!this.mCanConfigWifi) {
            hideWifiViews();
        }
    }

    @Override // com.android.systemui.statusbar.phone.SystemUIDialog, android.app.Dialog
    public final void onStop() {
        super.onStop();
        if (DEBUG) {
            Log.d("InternetDialog", "onStop");
        }
        this.mHandler.removeCallbacks(this.mHideProgressBarRunnable);
        this.mHandler.removeCallbacks(this.mHideSearchingRunnable);
        this.mMobileNetworkLayout.setOnClickListener(null);
        this.mMobileDataToggle.setOnCheckedChangeListener(null);
        this.mConnectedWifListLayout.setOnClickListener(null);
        this.mSeeAllLayout.setOnClickListener(null);
        this.mWiFiToggle.setOnCheckedChangeListener(null);
        this.mDoneButton.setOnClickListener(null);
        this.mAirplaneModeButton.setOnClickListener(null);
        InternetDialogController internetDialogController = this.mInternetDialogController;
        if (InternetDialogController.DEBUG) {
            Objects.requireNonNull(internetDialogController);
            Log.d("InternetDialogController", "onStop");
        }
        internetDialogController.mBroadcastDispatcher.unregisterReceiver(internetDialogController.mConnectionStateReceiver);
        internetDialogController.mTelephonyManager.unregisterTelephonyCallback(internetDialogController.mInternetTelephonyCallback);
        internetDialogController.mSubscriptionManager.removeOnSubscriptionsChangedListener(internetDialogController.mOnSubscriptionsChangedListener);
        internetDialogController.mAccessPointController.removeAccessPointCallback(internetDialogController);
        internetDialogController.mKeyguardUpdateMonitor.removeCallback(internetDialogController.mKeyguardUpdateCallback);
        internetDialogController.mConnectivityManager.unregisterNetworkCallback(internetDialogController.mConnectivityManagerNetworkCallback);
        InternetDialogController.ConnectedWifiInternetMonitor connectedWifiInternetMonitor = internetDialogController.mConnectedWifiInternetMonitor;
        Objects.requireNonNull(connectedWifiInternetMonitor);
        WifiEntry wifiEntry = connectedWifiInternetMonitor.mWifiEntry;
        if (wifiEntry != null) {
            synchronized (wifiEntry) {
                wifiEntry.mListener = null;
            }
            connectedWifiInternetMonitor.mWifiEntry = null;
        }
        Objects.requireNonNull(this.mInternetDialogFactory);
        if (InternetDialogFactoryKt.DEBUG) {
            Log.d("InternetDialogFactory", "destroyDialog");
        }
        InternetDialogFactory.internetDialog = null;
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public final void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        AlertDialog alertDialog = this.mAlertDialog;
        if (alertDialog != null && !alertDialog.isShowing() && !z && isShowing()) {
            dismiss();
        }
    }
}
