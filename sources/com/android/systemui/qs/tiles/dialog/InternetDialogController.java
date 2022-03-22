package com.android.systemui.qs.tiles.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline1;
import androidx.mediarouter.media.MediaRoute2Provider$$ExternalSyntheticLambda0;
import com.android.keyguard.KeyguardStatusView$$ExternalSyntheticLambda0;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline3;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.keyguard.KeyguardVisibilityHelper$$ExternalSyntheticLambda0;
import com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda0;
import com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda1;
import com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda2;
import com.android.settingslib.Utils;
import com.android.settingslib.graph.SignalDrawable;
import com.android.settingslib.mobile.MobileMappings;
import com.android.settingslib.wifi.WifiUtils;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.animation.DialogLaunchAnimator$createActivityLaunchController$1;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.connectivity.AccessPointController;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda18;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda24;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.LocationController;
import com.android.systemui.toast.SystemUIToast;
import com.android.systemui.toast.ToastFactory;
import com.android.systemui.util.CarrierConfigTracker;
import com.android.systemui.util.settings.GlobalSettings;
import com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda11;
import com.android.wifitrackerlib.MergedCarrierEntry;
import com.android.wifitrackerlib.WifiEntry;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda15;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda18;
import com.google.android.apps.miphone.aiai.matchmaker.overview.ui.SuggestController$$ExternalSyntheticLambda1;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import kotlinx.coroutines.YieldKt;
/* loaded from: classes.dex */
public final class InternetDialogController implements AccessPointController.AccessPointCallback {
    public static final long SHORT_DURATION_TIMEOUT = 4000;
    public static final float TOAST_PARAMS_HORIZONTAL_WEIGHT = 1.0f;
    public static final float TOAST_PARAMS_VERTICAL_WEIGHT = 1.0f;
    public AccessPointController mAccessPointController;
    public ActivityStarter mActivityStarter;
    public BroadcastDispatcher mBroadcastDispatcher;
    public InternetDialogCallback mCallback;
    public boolean mCanConfigWifi;
    public CarrierConfigTracker mCarrierConfigTracker;
    public ConnectedWifiInternetMonitor mConnectedWifiInternetMonitor;
    public IntentFilter mConnectionStateFilter;
    public ConnectivityManager mConnectivityManager;
    public DataConnectivityListener mConnectivityManagerNetworkCallback;
    public Context mContext;
    public DialogLaunchAnimator mDialogLaunchAnimator;
    public Executor mExecutor;
    public GlobalSettings mGlobalSettings;
    public Handler mHandler;
    public boolean mHasWifiEntries;
    public InternetTelephonyCallback mInternetTelephonyCallback;
    public KeyguardStateController mKeyguardStateController;
    public KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public LocationController mLocationController;
    public SubscriptionManager.OnSubscriptionsChangedListener mOnSubscriptionsChangedListener;
    public SignalDrawable mSignalDrawable;
    public SubscriptionManager mSubscriptionManager;
    public TelephonyManager mTelephonyManager;
    public ToastFactory mToastFactory;
    public WifiUtils.InternetIconInjector mWifiIconInjector;
    public WifiManager mWifiManager;
    public WindowManager mWindowManager;
    public Handler mWorkerHandler;
    public static final ColorDrawable EMPTY_DRAWABLE = new ColorDrawable(0);
    public static final int SUBTITLE_TEXT_WIFI_IS_OFF = 2131953560;
    public static final int SUBTITLE_TEXT_TAP_A_NETWORK_TO_CONNECT = 2131953350;
    public static final int SUBTITLE_TEXT_UNLOCK_TO_VIEW_NETWORKS = 2131953437;
    public static final int SUBTITLE_TEXT_SEARCHING_FOR_NETWORKS = 2131953553;
    public static final int SUBTITLE_TEXT_NON_CARRIER_NETWORK_UNAVAILABLE = 2131952875;
    public static final int SUBTITLE_TEXT_ALL_CARRIER_NETWORK_UNAVAILABLE = 2131951880;
    public static final boolean DEBUG = Log.isLoggable("InternetDialogController", 3);
    public TelephonyDisplayInfo mTelephonyDisplayInfo = new TelephonyDisplayInfo(0, 0);
    public MobileMappings.Config mConfig = null;
    public int mDefaultDataSubId = -1;
    public boolean mHasEthernet = false;
    public final AnonymousClass1 mKeyguardUpdateCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialogController.1
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onRefreshCarrierInfo() {
            InternetDialog internetDialog = (InternetDialog) InternetDialogController.this.mCallback;
            Objects.requireNonNull(internetDialog);
            internetDialog.mHandler.post(new BubbleStackView$$ExternalSyntheticLambda15(internetDialog, 4));
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onSimStateChanged(int i, int i2, int i3) {
            InternetDialog internetDialog = (InternetDialog) InternetDialogController.this.mCallback;
            Objects.requireNonNull(internetDialog);
            internetDialog.mHandler.post(new SuggestController$$ExternalSyntheticLambda1(internetDialog, 2));
        }
    };
    public final AnonymousClass2 mConnectionStateReceiver = new BroadcastReceiver() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialogController.2
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED".equals(action)) {
                if (InternetDialogController.DEBUG) {
                    Log.d("InternetDialogController", "ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
                }
                InternetDialogController.this.mConfig = MobileMappings.Config.readConfig(context);
                InternetDialogController.m79$$Nest$mupdateListener(InternetDialogController.this);
            } else if ("android.net.wifi.supplicant.CONNECTION_CHANGE".equals(action)) {
                InternetDialogController.m79$$Nest$mupdateListener(InternetDialogController.this);
            }
        }
    };

    /* loaded from: classes.dex */
    public class ConnectedWifiInternetMonitor implements WifiEntry.WifiEntryCallback {
        public WifiEntry mWifiEntry;

        public ConnectedWifiInternetMonitor() {
        }

        @Override // com.android.wifitrackerlib.WifiEntry.WifiEntryCallback
        public final void onUpdated() {
            WifiEntry wifiEntry = this.mWifiEntry;
            if (wifiEntry != null) {
                if (wifiEntry.getConnectedState() != 2) {
                    WifiEntry wifiEntry2 = this.mWifiEntry;
                    if (wifiEntry2 != null) {
                        synchronized (wifiEntry2) {
                            wifiEntry2.mListener = null;
                        }
                        this.mWifiEntry = null;
                    }
                } else if (wifiEntry.mIsDefaultNetwork && wifiEntry.mIsValidated) {
                    WifiEntry wifiEntry3 = this.mWifiEntry;
                    if (wifiEntry3 != null) {
                        synchronized (wifiEntry3) {
                            wifiEntry3.mListener = null;
                        }
                        this.mWifiEntry = null;
                    }
                    InternetDialogController internetDialogController = InternetDialogController.this;
                    ColorDrawable colorDrawable = InternetDialogController.EMPTY_DRAWABLE;
                    Objects.requireNonNull(internetDialogController);
                    if (internetDialogController.mCanConfigWifi) {
                        internetDialogController.mAccessPointController.scanForAccessPoints();
                    }
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public class DataConnectivityListener extends ConnectivityManager.NetworkCallback {
        public DataConnectivityListener() {
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public final void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            InternetDialogController.this.mHasEthernet = networkCapabilities.hasTransport(3);
            InternetDialogController internetDialogController = InternetDialogController.this;
            if (internetDialogController.mCanConfigWifi && (internetDialogController.mHasEthernet || networkCapabilities.hasTransport(1))) {
                InternetDialogController internetDialogController2 = InternetDialogController.this;
                Objects.requireNonNull(internetDialogController2);
                if (internetDialogController2.mCanConfigWifi) {
                    internetDialogController2.mAccessPointController.scanForAccessPoints();
                }
            }
            InternetDialog internetDialog = (InternetDialog) InternetDialogController.this.mCallback;
            Objects.requireNonNull(internetDialog);
            internetDialog.mHandler.post(new VolumeDialogImpl$$ExternalSyntheticLambda11(internetDialog, 1));
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public final void onLost(Network network) {
            InternetDialogController internetDialogController = InternetDialogController.this;
            internetDialogController.mHasEthernet = false;
            InternetDialog internetDialog = (InternetDialog) internetDialogController.mCallback;
            Objects.requireNonNull(internetDialog);
            internetDialog.mHandler.post(new KeyguardVisibilityHelper$$ExternalSyntheticLambda0(internetDialog, 2));
        }
    }

    /* loaded from: classes.dex */
    public interface InternetDialogCallback {
    }

    /* loaded from: classes.dex */
    public class InternetOnSubscriptionChangedListener extends SubscriptionManager.OnSubscriptionsChangedListener {
        public InternetOnSubscriptionChangedListener() {
        }

        @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
        public final void onSubscriptionsChanged() {
            InternetDialogController.m79$$Nest$mupdateListener(InternetDialogController.this);
        }
    }

    /* loaded from: classes.dex */
    public class InternetTelephonyCallback extends TelephonyCallback implements TelephonyCallback.DataConnectionStateListener, TelephonyCallback.DisplayInfoListener, TelephonyCallback.ServiceStateListener, TelephonyCallback.SignalStrengthsListener, TelephonyCallback.UserMobileDataStateListener {
        public InternetTelephonyCallback() {
        }

        public final void onDataConnectionStateChanged(int i, int i2) {
            InternetDialog internetDialog = (InternetDialog) InternetDialogController.this.mCallback;
            Objects.requireNonNull(internetDialog);
            internetDialog.mHandler.post(new StatusBar$$ExternalSyntheticLambda18(internetDialog, 5));
        }

        public final void onDisplayInfoChanged(TelephonyDisplayInfo telephonyDisplayInfo) {
            InternetDialogController internetDialogController = InternetDialogController.this;
            internetDialogController.mTelephonyDisplayInfo = telephonyDisplayInfo;
            InternetDialog internetDialog = (InternetDialog) internetDialogController.mCallback;
            Objects.requireNonNull(internetDialog);
            internetDialog.mHandler.post(new KeyguardStatusView$$ExternalSyntheticLambda0(internetDialog, 2));
        }

        public final void onServiceStateChanged(ServiceState serviceState) {
            InternetDialog internetDialog = (InternetDialog) InternetDialogController.this.mCallback;
            Objects.requireNonNull(internetDialog);
            internetDialog.mHandler.post(new LockIconViewController$$ExternalSyntheticLambda1(internetDialog, 2));
        }

        public final void onSignalStrengthsChanged(SignalStrength signalStrength) {
            InternetDialog internetDialog = (InternetDialog) InternetDialogController.this.mCallback;
            Objects.requireNonNull(internetDialog);
            internetDialog.mHandler.post(new LockIconViewController$$ExternalSyntheticLambda0(internetDialog, 2));
        }

        public final void onUserMobileDataStateChanged(boolean z) {
            InternetDialog internetDialog = (InternetDialog) InternetDialogController.this.mCallback;
            Objects.requireNonNull(internetDialog);
            internetDialog.mHandler.post(new BubbleStackView$$ExternalSyntheticLambda18(internetDialog, 2));
        }
    }

    /* loaded from: classes.dex */
    public static class WifiEntryConnectCallback implements WifiEntry.ConnectCallback {
        public final ActivityStarter mActivityStarter;
        public final InternetDialogController mInternetDialogController;
        public final WifiEntry mWifiEntry;

        public final void onConnectResult(int i) {
            boolean z = InternetDialogController.DEBUG;
            if (z) {
                ExifInterface$$ExternalSyntheticOutline1.m("onConnectResult ", i, "InternetDialogController");
            }
            if (i == 1) {
                Intent putExtra = new Intent("com.android.settings.WIFI_DIALOG").putExtra("key_chosen_wifientry_key", this.mWifiEntry.getKey());
                putExtra.addFlags(268435456);
                this.mActivityStarter.startActivity(putExtra, false);
            } else if (i == 2) {
                this.mInternetDialogController.makeOverlayToast(2131953555);
            } else if (z) {
                ExifInterface$$ExternalSyntheticOutline1.m("connect failure reason=", i, "InternetDialogController");
            }
        }

        public WifiEntryConnectCallback(ActivityStarter activityStarter, WifiEntry wifiEntry, InternetDialogController internetDialogController) {
            this.mActivityStarter = activityStarter;
            this.mWifiEntry = wifiEntry;
            this.mInternetDialogController = internetDialogController;
        }
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [com.android.systemui.qs.tiles.dialog.InternetDialogController$1] */
    /* JADX WARN: Type inference failed for: r1v4, types: [com.android.systemui.qs.tiles.dialog.InternetDialogController$2] */
    public InternetDialogController(Context context, ActivityStarter activityStarter, AccessPointController accessPointController, SubscriptionManager subscriptionManager, TelephonyManager telephonyManager, WifiManager wifiManager, ConnectivityManager connectivityManager, Handler handler, Executor executor, BroadcastDispatcher broadcastDispatcher, KeyguardUpdateMonitor keyguardUpdateMonitor, GlobalSettings globalSettings, KeyguardStateController keyguardStateController, WindowManager windowManager, ToastFactory toastFactory, Handler handler2, CarrierConfigTracker carrierConfigTracker, LocationController locationController, DialogLaunchAnimator dialogLaunchAnimator) {
        if (DEBUG) {
            Log.d("InternetDialogController", "Init InternetDialogController");
        }
        this.mHandler = handler;
        this.mWorkerHandler = handler2;
        this.mExecutor = executor;
        this.mContext = context;
        this.mGlobalSettings = globalSettings;
        this.mWifiManager = wifiManager;
        this.mTelephonyManager = telephonyManager;
        this.mConnectivityManager = connectivityManager;
        this.mSubscriptionManager = subscriptionManager;
        this.mCarrierConfigTracker = carrierConfigTracker;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mKeyguardStateController = keyguardStateController;
        IntentFilter intentFilter = new IntentFilter();
        this.mConnectionStateFilter = intentFilter;
        intentFilter.addAction("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
        this.mConnectionStateFilter.addAction("android.net.wifi.supplicant.CONNECTION_CHANGE");
        this.mActivityStarter = activityStarter;
        this.mAccessPointController = accessPointController;
        this.mWifiIconInjector = new WifiUtils.InternetIconInjector(this.mContext);
        this.mConnectivityManagerNetworkCallback = new DataConnectivityListener();
        this.mWindowManager = windowManager;
        this.mToastFactory = toastFactory;
        this.mSignalDrawable = new SignalDrawable(this.mContext);
        this.mLocationController = locationController;
        this.mDialogLaunchAnimator = dialogLaunchAnimator;
        this.mConnectedWifiInternetMonitor = new ConnectedWifiInternetMonitor();
    }

    /* renamed from: com.android.systemui.qs.tiles.dialog.InternetDialogController$1DisplayInfo  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class C1DisplayInfo {
        public CharSequence originalName;
        public SubscriptionInfo subscriptionInfo;
        public CharSequence uniqueName;

        public C1DisplayInfo(SubscriptionInfo subscriptionInfo, String str) {
            this.subscriptionInfo = subscriptionInfo;
            this.originalName = str;
        }
    }

    public final boolean activeNetworkIsCellular() {
        NetworkCapabilities networkCapabilities;
        ConnectivityManager connectivityManager = this.mConnectivityManager;
        if (connectivityManager == null) {
            if (DEBUG) {
                Log.d("InternetDialogController", "ConnectivityManager is null, can not check active network.");
            }
            return false;
        }
        Network activeNetwork = connectivityManager.getActiveNetwork();
        if (activeNetwork == null || (networkCapabilities = this.mConnectivityManager.getNetworkCapabilities(activeNetwork)) == null) {
            return false;
        }
        return networkCapabilities.hasTransport(0);
    }

    public Intent getSettingsIntent() {
        return new Intent("android.settings.NETWORK_PROVIDER_SETTINGS").addFlags(268435456);
    }

    public final LayerDrawable getSignalStrengthDrawableWithLevel(boolean z) {
        int i;
        int i2;
        SignalStrength signalStrength = this.mTelephonyManager.getSignalStrength();
        if (signalStrength == null) {
            i = 0;
        } else {
            i = signalStrength.getLevel();
        }
        int i3 = 5;
        if ((this.mSubscriptionManager != null && YieldKt.shouldInflateSignalStrength(this.mContext, this.mDefaultDataSubId)) || z) {
            if (z) {
                i = 5;
            } else {
                i++;
            }
            i3 = 6;
        }
        Context context = this.mContext;
        boolean z2 = !isMobileDataEnabled();
        SignalDrawable signalDrawable = this.mSignalDrawable;
        int i4 = SignalDrawable.$r8$clinit;
        if (z2) {
            i2 = 2;
        } else {
            i2 = 0;
        }
        signalDrawable.setLevel(i | (i2 << 16) | (i3 << 8));
        Drawable[] drawableArr = {EMPTY_DRAWABLE, this.mSignalDrawable};
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(2131167018);
        LayerDrawable layerDrawable = new LayerDrawable(drawableArr);
        layerDrawable.setLayerGravity(0, 51);
        layerDrawable.setLayerGravity(1, 85);
        layerDrawable.setLayerSize(1, dimensionPixelSize, dimensionPixelSize);
        layerDrawable.setTintList(Utils.getColorAttr(context, 16843282));
        return layerDrawable;
    }

    public final boolean hasActiveSubId() {
        if (this.mSubscriptionManager == null) {
            if (DEBUG) {
                Log.d("InternetDialogController", "SubscriptionManager is null, can not check carrier.");
            }
            return false;
        } else if (isAirplaneModeEnabled() || this.mTelephonyManager == null || this.mSubscriptionManager.getActiveSubscriptionIdList().length <= 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isAirplaneModeEnabled() {
        if (this.mGlobalSettings.getInt("airplane_mode_on", 0) != 0) {
            return true;
        }
        return false;
    }

    public final boolean isCarrierNetworkActive() {
        MergedCarrierEntry mergedCarrierEntry = this.mAccessPointController.getMergedCarrierEntry();
        if (mergedCarrierEntry == null || !mergedCarrierEntry.mIsDefaultNetwork) {
            return false;
        }
        return true;
    }

    public final boolean isDataStateInService() {
        NetworkRegistrationInfo networkRegistrationInfo;
        ServiceState serviceState = this.mTelephonyManager.getServiceState();
        if (serviceState == null) {
            networkRegistrationInfo = null;
        } else {
            networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, 1);
        }
        if (networkRegistrationInfo == null) {
            return false;
        }
        return networkRegistrationInfo.isRegistered();
    }

    public final boolean isMobileDataEnabled() {
        TelephonyManager telephonyManager = this.mTelephonyManager;
        if (telephonyManager == null || !telephonyManager.isDataEnabled()) {
            return false;
        }
        return true;
    }

    public final boolean isVoiceStateInService() {
        TelephonyManager telephonyManager = this.mTelephonyManager;
        if (telephonyManager == null) {
            if (DEBUG) {
                Log.d("InternetDialogController", "TelephonyManager is null, can not detect voice state.");
            }
            return false;
        }
        ServiceState serviceState = telephonyManager.getServiceState();
        if (serviceState == null || serviceState.getState() != 0) {
            return false;
        }
        return true;
    }

    public final void makeOverlayToast(int i) {
        Resources resources = this.mContext.getResources();
        final SystemUIToast createToast = this.mToastFactory.createToast(this.mContext, resources.getString(i), this.mContext.getPackageName(), UserHandle.myUserId(), resources.getConfiguration().orientation);
        final View view = createToast.mToastView;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.height = -2;
        layoutParams.width = -2;
        layoutParams.format = -3;
        layoutParams.type = 2017;
        layoutParams.flags = 152;
        layoutParams.y = createToast.getYOffset().intValue();
        int absoluteGravity = Gravity.getAbsoluteGravity(createToast.getGravity().intValue(), resources.getConfiguration().getLayoutDirection());
        layoutParams.gravity = absoluteGravity;
        if ((absoluteGravity & 7) == 7) {
            layoutParams.horizontalWeight = 1.0f;
        }
        if ((absoluteGravity & 112) == 112) {
            layoutParams.verticalWeight = 1.0f;
        }
        this.mWindowManager.addView(view, layoutParams);
        Animator animator = createToast.mInAnimator;
        if (animator != null) {
            animator.start();
        }
        this.mHandler.postDelayed(new Runnable() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialogController.3
            @Override // java.lang.Runnable
            public final void run() {
                SystemUIToast systemUIToast = createToast;
                Objects.requireNonNull(systemUIToast);
                Animator animator2 = systemUIToast.mOutAnimator;
                if (animator2 != null) {
                    animator2.start();
                    animator2.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialogController.3.1
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public final void onAnimationEnd(Animator animator3) {
                            AnonymousClass3 r0 = AnonymousClass3.this;
                            InternetDialogController.this.mWindowManager.removeViewImmediate(view);
                        }
                    });
                }
            }
        }, 4000L);
    }

    @Override // com.android.systemui.statusbar.connectivity.AccessPointController.AccessPointCallback
    public final void onAccessPointsChanged(List<WifiEntry> list) {
        int i;
        final boolean z;
        final ArrayList arrayList;
        final WifiEntry wifiEntry;
        final boolean z2;
        if (this.mCanConfigWifi) {
            if (list == null) {
                i = 0;
            } else {
                i = list.size();
            }
            if (i > 3) {
                z = true;
            } else {
                z = false;
            }
            WifiEntry wifiEntry2 = null;
            if (i > 0) {
                ArrayList arrayList2 = new ArrayList();
                if (z) {
                    i = 3;
                }
                ConnectedWifiInternetMonitor connectedWifiInternetMonitor = this.mConnectedWifiInternetMonitor;
                Objects.requireNonNull(connectedWifiInternetMonitor);
                WifiEntry wifiEntry3 = connectedWifiInternetMonitor.mWifiEntry;
                if (wifiEntry3 != null) {
                    synchronized (wifiEntry3) {
                        wifiEntry3.mListener = null;
                    }
                    connectedWifiInternetMonitor.mWifiEntry = null;
                }
                for (int i2 = 0; i2 < i; i2++) {
                    WifiEntry wifiEntry4 = list.get(i2);
                    ConnectedWifiInternetMonitor connectedWifiInternetMonitor2 = this.mConnectedWifiInternetMonitor;
                    if (wifiEntry4 == null) {
                        Objects.requireNonNull(connectedWifiInternetMonitor2);
                    } else if (connectedWifiInternetMonitor2.mWifiEntry == null && wifiEntry4.getConnectedState() == 2 && (!wifiEntry4.mIsDefaultNetwork || !wifiEntry4.mIsValidated)) {
                        connectedWifiInternetMonitor2.mWifiEntry = wifiEntry4;
                        synchronized (wifiEntry4) {
                            wifiEntry4.mListener = connectedWifiInternetMonitor2;
                        }
                    }
                    if (wifiEntry2 == null) {
                        Objects.requireNonNull(wifiEntry4);
                        if (wifiEntry4.mIsDefaultNetwork && wifiEntry4.mIsValidated) {
                            wifiEntry2 = wifiEntry4;
                        }
                    }
                    arrayList2.add(wifiEntry4);
                }
                this.mHasWifiEntries = true;
                wifiEntry = wifiEntry2;
                arrayList = arrayList2;
            } else {
                this.mHasWifiEntries = false;
                wifiEntry = null;
                arrayList = null;
            }
            final InternetDialog internetDialog = (InternetDialog) this.mCallback;
            Objects.requireNonNull(internetDialog);
            if (internetDialog.mMobileNetworkLayout.getVisibility() != 0 || !internetDialog.mInternetDialogController.isAirplaneModeEnabled()) {
                z2 = false;
            } else {
                z2 = true;
            }
            internetDialog.mHandler.post(new Runnable() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    int i3;
                    InternetDialog internetDialog2 = InternetDialog.this;
                    WifiEntry wifiEntry5 = wifiEntry;
                    List<WifiEntry> list2 = arrayList;
                    boolean z3 = z;
                    boolean z4 = z2;
                    Objects.requireNonNull(internetDialog2);
                    internetDialog2.mConnectedWifiEntry = wifiEntry5;
                    if (list2 == null) {
                        i3 = 0;
                    } else {
                        i3 = list2.size();
                    }
                    internetDialog2.mWifiEntriesCount = i3;
                    internetDialog2.mHasMoreWifiEntries = z3;
                    internetDialog2.updateDialog(z4);
                    InternetAdapter internetAdapter = internetDialog2.mAdapter;
                    int i4 = internetDialog2.mWifiEntriesCount;
                    Objects.requireNonNull(internetAdapter);
                    internetAdapter.mWifiEntries = list2;
                    int i5 = internetAdapter.mMaxEntriesCount;
                    if (i4 >= i5) {
                        i4 = i5;
                    }
                    internetAdapter.mWifiEntriesCount = i4;
                    internetDialog2.mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    public final void setMobileDataEnabled(Context context, int i, boolean z) {
        TelephonyManager telephonyManager = this.mTelephonyManager;
        if (telephonyManager == null) {
            if (DEBUG) {
                Log.d("InternetDialogController", "TelephonyManager is null, can not set mobile data.");
            }
        } else if (this.mSubscriptionManager != null) {
            telephonyManager.setDataEnabled(z);
            this.mWorkerHandler.post(new StatusBar$$ExternalSyntheticLambda24(this, i, z));
        } else if (DEBUG) {
            Log.d("InternetDialogController", "SubscriptionManager is null, can not set mobile data.");
        }
    }

    public final void startActivity(Intent intent, View view) {
        DialogLaunchAnimator dialogLaunchAnimator = this.mDialogLaunchAnimator;
        Objects.requireNonNull(dialogLaunchAnimator);
        DialogLaunchAnimator$createActivityLaunchController$1 createActivityLaunchController$default = DialogLaunchAnimator.createActivityLaunchController$default(dialogLaunchAnimator, view);
        if (createActivityLaunchController$default == null) {
            InternetDialog internetDialog = (InternetDialog) this.mCallback;
            if (InternetDialog.DEBUG) {
                Objects.requireNonNull(internetDialog);
                Log.d("InternetDialog", "dismissDialog");
            }
            Objects.requireNonNull(internetDialog.mInternetDialogFactory);
            if (InternetDialogFactoryKt.DEBUG) {
                Log.d("InternetDialogFactory", "destroyDialog");
            }
            InternetDialogFactory.internetDialog = null;
            internetDialog.dismiss();
        }
        this.mActivityStarter.postStartActivityDismissingKeyguard(intent, 0, createActivityLaunchController$default);
    }

    /* renamed from: -$$Nest$mupdateListener  reason: not valid java name */
    public static void m79$$Nest$mupdateListener(InternetDialogController internetDialogController) {
        Objects.requireNonNull(internetDialogController);
        int defaultDataSubscriptionId = internetDialogController.getDefaultDataSubscriptionId();
        if (internetDialogController.mDefaultDataSubId != internetDialogController.getDefaultDataSubscriptionId()) {
            internetDialogController.mDefaultDataSubId = defaultDataSubscriptionId;
            if (DEBUG) {
                KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(VendorAtomValue$$ExternalSyntheticOutline1.m("DDS: defaultDataSubId:"), internetDialogController.mDefaultDataSubId, "InternetDialogController");
            }
            if (SubscriptionManager.isUsableSubscriptionId(internetDialogController.mDefaultDataSubId)) {
                internetDialogController.mTelephonyManager.unregisterTelephonyCallback(internetDialogController.mInternetTelephonyCallback);
                TelephonyManager createForSubscriptionId = internetDialogController.mTelephonyManager.createForSubscriptionId(internetDialogController.mDefaultDataSubId);
                internetDialogController.mTelephonyManager = createForSubscriptionId;
                Handler handler = internetDialogController.mHandler;
                Objects.requireNonNull(handler);
                createForSubscriptionId.registerTelephonyCallback(new MediaRoute2Provider$$ExternalSyntheticLambda0(handler), internetDialogController.mInternetTelephonyCallback);
                InternetDialogCallback internetDialogCallback = internetDialogController.mCallback;
                int i = internetDialogController.mDefaultDataSubId;
                InternetDialog internetDialog = (InternetDialog) internetDialogCallback;
                Objects.requireNonNull(internetDialog);
                internetDialog.mDefaultDataSubId = i;
                internetDialog.mTelephonyManager = internetDialog.mTelephonyManager.createForSubscriptionId(i);
                internetDialog.mHandler.post(new LockIconViewController$$ExternalSyntheticLambda2(internetDialog, 3));
            }
        } else if (DEBUG) {
            Log.d("InternetDialogController", "DDS: no change");
        }
    }

    public int getDefaultDataSubscriptionId() {
        return SubscriptionManager.getDefaultDataSubscriptionId();
    }

    public final void launchWifiNetworkDetailsSetting(String str, View view) {
        Intent intent;
        if (TextUtils.isEmpty(str)) {
            if (DEBUG) {
                Log.d("InternetDialogController", "connected entry's key is empty");
            }
            intent = null;
        } else {
            Intent intent2 = new Intent("android.settings.WIFI_DETAILS_SETTINGS");
            Bundle bundle = new Bundle();
            bundle.putString("key_chosen_wifientry_key", str);
            intent2.putExtra(":settings:show_fragment_args", bundle);
            intent = intent2;
        }
        if (intent != null) {
            startActivity(intent, view);
        }
    }
}
