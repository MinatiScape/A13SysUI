package com.android.systemui.statusbar.connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.net.NetworkCapabilities;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.CarrierConfigManager;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardDisplayManager$$ExternalSyntheticLambda1;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline1;
import com.android.keyguard.KeyguardVisibilityHelper$$ExternalSyntheticLambda0;
import com.android.keyguard.LockIconView$$ExternalSyntheticOutline0;
import com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda0;
import com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda1;
import com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda2;
import com.android.settingslib.SignalIcon$MobileIconGroup;
import com.android.settingslib.Utils;
import com.android.settingslib.mobile.MobileMappings;
import com.android.settingslib.mobile.MobileStatusTracker;
import com.android.settingslib.mobile.TelephonyIcons;
import com.android.settingslib.net.DataUsageController;
import com.android.settingslib.wifi.AccessPoint$$ExternalSyntheticLambda0;
import com.android.settingslib.wifi.AccessPoint$$ExternalSyntheticLambda1;
import com.android.settingslib.wifi.WifiStatusTracker;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.tiles.dialog.InternetDialogFactory;
import com.android.systemui.statusbar.policy.DataSaverControllerImpl;
import com.android.systemui.statusbar.policy.EncryptionHelper;
import com.android.systemui.telephony.TelephonyCallback;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.util.CarrierConfigTracker;
import com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda10;
import com.android.wifitrackerlib.WifiEntry$$ExternalSyntheticLambda2;
import com.android.wifitrackerlib.WifiPickerTracker;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executor;
import kotlinx.coroutines.YieldKt;
/* loaded from: classes.dex */
public final class NetworkControllerImpl extends BroadcastReceiver implements NetworkController, DemoMode, Dumpable {
    public final AccessPointControllerImpl mAccessPoints;
    public final Executor mBgExecutor;
    public final Looper mBgLooper;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public final CallbackHandler mCallbackHandler;
    public final CarrierConfigTracker mCarrierConfigTracker;
    public MobileMappings.Config mConfig;
    public final Context mContext;
    public final DataSaverControllerImpl mDataSaverController;
    public final DataUsageController mDataUsageController;
    public MobileSignalController mDefaultSignalController;
    public boolean mDemoInetCondition;
    public final DemoModeController mDemoModeController;
    public WifiState mDemoWifiState;
    public int mEmergencySource;
    @VisibleForTesting
    public final EthernetSignalController mEthernetSignalController;
    public final FeatureFlags mFeatureFlags;
    public boolean mForceCellularValidated;
    public final boolean mHasMobileDataFeature;
    public boolean mHasNoSubs;
    public int mHistoryIndex;
    public boolean mInetCondition;
    public InternetDialogFactory mInternetDialogFactory;
    public boolean mIsEmergency;
    public NetworkCapabilities mLastDefaultNetworkCapabilities;
    @VisibleForTesting
    public ServiceState mLastServiceState;
    @VisibleForTesting
    public boolean mListening;
    public Locale mLocale;
    public Handler mMainHandler;
    public final TelephonyManager mPhone;
    public NetworkControllerImpl$$ExternalSyntheticLambda0 mPhoneStateListener;
    public final boolean mProviderModelBehavior;
    public final Handler mReceiverHandler;
    public boolean mSimDetected;
    public final MobileStatusTracker.SubscriptionDefaults mSubDefaults;
    public SubListener mSubscriptionListener;
    public final SubscriptionManager mSubscriptionManager;
    public final TelephonyListenerManager mTelephonyListenerManager;
    public boolean mUserSetup;
    public final AnonymousClass3 mUserTracker;
    public final WifiManager mWifiManager;
    @VisibleForTesting
    public final WifiSignalController mWifiSignalController;
    public static final boolean DEBUG = Log.isLoggable("NetworkController", 3);
    public static final boolean CHATTY = Log.isLoggable("NetworkControllerChat", 3);
    public static final SimpleDateFormat SSDF = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
    public final Object mLock = new Object();
    public int mActiveMobileDataSubscription = -1;
    @VisibleForTesting
    public final SparseArray<MobileSignalController> mMobileSignalControllers = new SparseArray<>();
    public final BitSet mConnectedTransports = new BitSet();
    public final BitSet mValidatedTransports = new BitSet();
    public boolean mAirplaneMode = false;
    public boolean mNoDefaultNetwork = false;
    public boolean mNoNetworksAvailable = true;
    public List<SubscriptionInfo> mCurrentSubscriptions = new ArrayList();
    public final String[] mHistory = new String[16];
    public final KeyguardDisplayManager$$ExternalSyntheticLambda1 mClearForceValidated = new KeyguardDisplayManager$$ExternalSyntheticLambda1(this, 4);
    public final KeyguardVisibilityHelper$$ExternalSyntheticLambda0 mRegisterListeners = new KeyguardVisibilityHelper$$ExternalSyntheticLambda0(this, 4);

    /* renamed from: com.android.systemui.statusbar.connectivity.NetworkControllerImpl$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 implements DataUsageController.Callback {
        public AnonymousClass2() {
        }
    }

    /* loaded from: classes.dex */
    public class SubListener extends SubscriptionManager.OnSubscriptionsChangedListener {
        public SubListener(Looper looper) {
            super(looper);
        }

        @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
        public final void onSubscriptionsChanged() {
            NetworkControllerImpl.this.updateMobileControllers();
        }
    }

    public final void pushConnectivityToSignals() {
        for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
            this.mMobileSignalControllers.valueAt(i).updateConnectivity(this.mConnectedTransports, this.mValidatedTransports);
        }
        WifiSignalController wifiSignalController = this.mWifiSignalController;
        BitSet bitSet = this.mValidatedTransports;
        Objects.requireNonNull(wifiSignalController);
        wifiSignalController.mCurrentState.inetCondition = bitSet.get(wifiSignalController.mTransportType) ? 1 : 0;
        wifiSignalController.notifyListenersIfNecessary();
        this.mEthernetSignalController.updateConnectivity(this.mConnectedTransports, this.mValidatedTransports);
    }

    @VisibleForTesting
    public void registerListeners() {
        for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
            this.mMobileSignalControllers.valueAt(i).registerListener();
        }
        if (this.mSubscriptionListener == null) {
            this.mSubscriptionListener = new SubListener(this.mBgLooper);
        }
        this.mSubscriptionManager.addOnSubscriptionsChangedListener(this.mSubscriptionListener);
        TelephonyListenerManager telephonyListenerManager = this.mTelephonyListenerManager;
        NetworkControllerImpl$$ExternalSyntheticLambda0 networkControllerImpl$$ExternalSyntheticLambda0 = this.mPhoneStateListener;
        Objects.requireNonNull(telephonyListenerManager);
        TelephonyCallback telephonyCallback = telephonyListenerManager.mTelephonyCallback;
        Objects.requireNonNull(telephonyCallback);
        telephonyCallback.mActiveDataSubscriptionIdListeners.add(networkControllerImpl$$ExternalSyntheticLambda0);
        telephonyListenerManager.updateListening();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
        intentFilter.addAction("android.intent.action.ACTION_DEFAULT_VOICE_SUBSCRIPTION_CHANGED");
        intentFilter.addAction("android.intent.action.SERVICE_STATE");
        intentFilter.addAction("android.telephony.action.SERVICE_PROVIDERS_UPDATED");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
        intentFilter.addAction("android.settings.panel.action.INTERNET_CONNECTIVITY");
        this.mBroadcastDispatcher.registerReceiverWithHandler(this, intentFilter, this.mReceiverHandler);
        this.mListening = true;
        this.mReceiverHandler.post(new WifiEntry$$ExternalSyntheticLambda2(this, 5));
        Handler handler = this.mReceiverHandler;
        WifiSignalController wifiSignalController = this.mWifiSignalController;
        Objects.requireNonNull(wifiSignalController);
        handler.post(new AccessPoint$$ExternalSyntheticLambda1(wifiSignalController, 4));
        this.mReceiverHandler.post(new AccessPoint$$ExternalSyntheticLambda0(this, 7));
        updateMobileControllers();
        this.mReceiverHandler.post(new VolumeDialogImpl$$ExternalSyntheticLambda10(this, 5));
    }

    /* JADX WARN: Type inference failed for: r0v14, types: [com.android.systemui.settings.CurrentUserTracker, com.android.systemui.statusbar.connectivity.NetworkControllerImpl$3] */
    /* JADX WARN: Unknown variable types count: 1 */
    @com.android.internal.annotations.VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public NetworkControllerImpl(android.content.Context r17, android.net.ConnectivityManager r18, android.telephony.TelephonyManager r19, com.android.systemui.telephony.TelephonyListenerManager r20, android.net.wifi.WifiManager r21, android.net.NetworkScoreManager r22, android.telephony.SubscriptionManager r23, com.android.settingslib.mobile.MobileMappings.Config r24, android.os.Looper r25, java.util.concurrent.Executor r26, com.android.systemui.statusbar.connectivity.CallbackHandler r27, com.android.systemui.statusbar.connectivity.AccessPointControllerImpl r28, com.android.settingslib.net.DataUsageController r29, com.android.settingslib.mobile.MobileStatusTracker.SubscriptionDefaults r30, final com.android.systemui.statusbar.policy.DeviceProvisionedController r31, com.android.systemui.broadcast.BroadcastDispatcher r32, com.android.systemui.demomode.DemoModeController r33, com.android.systemui.util.CarrierConfigTracker r34, com.android.systemui.flags.FeatureFlags r35, com.android.systemui.dump.DumpManager r36) {
        /*
            Method dump skipped, instructions count: 303
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.connectivity.NetworkControllerImpl.<init>(android.content.Context, android.net.ConnectivityManager, android.telephony.TelephonyManager, com.android.systemui.telephony.TelephonyListenerManager, android.net.wifi.WifiManager, android.net.NetworkScoreManager, android.telephony.SubscriptionManager, com.android.settingslib.mobile.MobileMappings$Config, android.os.Looper, java.util.concurrent.Executor, com.android.systemui.statusbar.connectivity.CallbackHandler, com.android.systemui.statusbar.connectivity.AccessPointControllerImpl, com.android.settingslib.net.DataUsageController, com.android.settingslib.mobile.MobileStatusTracker$SubscriptionDefaults, com.android.systemui.statusbar.policy.DeviceProvisionedController, com.android.systemui.broadcast.BroadcastDispatcher, com.android.systemui.demomode.DemoModeController, com.android.systemui.util.CarrierConfigTracker, com.android.systemui.flags.FeatureFlags, com.android.systemui.dump.DumpManager):void");
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(SignalCallback signalCallback) {
        SignalCallback signalCallback2 = signalCallback;
        signalCallback2.setSubs(this.mCurrentSubscriptions);
        boolean z = this.mAirplaneMode;
        SignalIcon$MobileIconGroup signalIcon$MobileIconGroup = TelephonyIcons.CARRIER_NETWORK_CHANGE;
        signalCallback2.setIsAirplaneMode(new IconState(z, 2131232671, this.mContext.getString(2131951672)));
        signalCallback2.setNoSims(this.mHasNoSubs, this.mSimDetected);
        signalCallback2.setConnectivityStatus(this.mNoDefaultNetwork, !this.mInetCondition, this.mNoNetworksAvailable);
        this.mWifiSignalController.notifyListeners(signalCallback2);
        this.mEthernetSignalController.notifyListeners(signalCallback2);
        for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
            MobileSignalController valueAt = this.mMobileSignalControllers.valueAt(i);
            valueAt.notifyListeners(signalCallback2);
            if (this.mProviderModelBehavior) {
                valueAt.refreshCallIndicator(signalCallback2);
            }
        }
        CallbackHandler callbackHandler = this.mCallbackHandler;
        Objects.requireNonNull(callbackHandler);
        callbackHandler.obtainMessage(7, 1, 0, signalCallback2).sendToTarget();
    }

    public final SubscriptionInfo addSignalController(int i, int i2) {
        SubscriptionInfo subscriptionInfo = new SubscriptionInfo(i, "", i2, "", "", 0, 0, "", 0, null, null, null, "", false, null, null);
        MobileSignalController mobileSignalController = new MobileSignalController(this.mContext, this.mConfig, this.mHasMobileDataFeature, this.mPhone.createForSubscriptionId(subscriptionInfo.getSubscriptionId()), this.mCallbackHandler, this, subscriptionInfo, this.mSubDefaults, this.mReceiverHandler.getLooper(), this.mCarrierConfigTracker, this.mFeatureFlags);
        this.mMobileSignalControllers.put(i, mobileSignalController);
        ((MobileState) mobileSignalController.mCurrentState).userSetup = true;
        return subscriptionInfo;
    }

    @Override // com.android.systemui.demomode.DemoMode
    public final List<String> demoCommands() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("network");
        return arrayList;
    }

    /* JADX WARN: Removed duplicated region for block: B:192:0x037b  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x0392  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00fe  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0119  */
    @Override // com.android.systemui.demomode.DemoModeCommandReceiver
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void dispatchDemoCommand(java.lang.String r18, android.os.Bundle r19) {
        /*
            Method dump skipped, instructions count: 980
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.connectivity.NetworkControllerImpl.dispatchDemoCommand(java.lang.String, android.os.Bundle):void");
    }

    @VisibleForTesting
    public void doUpdateMobileControllers() {
        List<SubscriptionInfo> completeActiveSubscriptionInfoList = this.mSubscriptionManager.getCompleteActiveSubscriptionInfoList();
        if (completeActiveSubscriptionInfoList == null) {
            completeActiveSubscriptionInfoList = Collections.emptyList();
        }
        if (completeActiveSubscriptionInfoList.size() == 2) {
            SubscriptionInfo subscriptionInfo = completeActiveSubscriptionInfoList.get(0);
            SubscriptionInfo subscriptionInfo2 = completeActiveSubscriptionInfoList.get(1);
            if (subscriptionInfo.getGroupUuid() != null && subscriptionInfo.getGroupUuid().equals(subscriptionInfo2.getGroupUuid()) && (subscriptionInfo.isOpportunistic() || subscriptionInfo2.isOpportunistic())) {
                if (CarrierConfigManager.getDefaultConfig().getBoolean("always_show_primary_signal_bar_in_opportunistic_network_boolean")) {
                    if (!subscriptionInfo.isOpportunistic()) {
                        subscriptionInfo = subscriptionInfo2;
                    }
                    completeActiveSubscriptionInfoList.remove(subscriptionInfo);
                } else {
                    if (subscriptionInfo.getSubscriptionId() == this.mActiveMobileDataSubscription) {
                        subscriptionInfo = subscriptionInfo2;
                    }
                    completeActiveSubscriptionInfoList.remove(subscriptionInfo);
                }
            }
        }
        if (hasCorrectMobileControllers(completeActiveSubscriptionInfoList)) {
            updateNoSims();
            return;
        }
        synchronized (this.mLock) {
            setCurrentSubscriptionsLocked(completeActiveSubscriptionInfoList);
        }
        updateNoSims();
        recalculateEmergency();
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str;
        ArrayList arrayList;
        StringBuilder m = LockIconView$$ExternalSyntheticOutline0.m(printWriter, "NetworkController state:", "  mUserSetup=");
        m.append(this.mUserSetup);
        printWriter.println(m.toString());
        printWriter.println("  - telephony ------");
        printWriter.print("  hasVoiceCallingFeature()=");
        printWriter.println(hasVoiceCallingFeature());
        printWriter.println("  mListening=" + this.mListening);
        printWriter.println("  - connectivity ------");
        printWriter.print("  mConnectedTransports=");
        printWriter.println(this.mConnectedTransports);
        printWriter.print("  mValidatedTransports=");
        printWriter.println(this.mValidatedTransports);
        printWriter.print("  mInetCondition=");
        printWriter.println(this.mInetCondition);
        printWriter.print("  mAirplaneMode=");
        printWriter.println(this.mAirplaneMode);
        printWriter.print("  mLocale=");
        printWriter.println(this.mLocale);
        printWriter.print("  mLastServiceState=");
        printWriter.println(this.mLastServiceState);
        printWriter.print("  mIsEmergency=");
        printWriter.println(this.mIsEmergency);
        printWriter.print("  mEmergencySource=");
        int i = this.mEmergencySource;
        if (i > 300) {
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("ASSUMED_VOICE_CONTROLLER(");
            m2.append(i - 200);
            m2.append(")");
            str = m2.toString();
        } else if (i > 300) {
            StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("NO_SUB(");
            m3.append(i - 300);
            m3.append(")");
            str = m3.toString();
        } else if (i > 200) {
            StringBuilder m4 = VendorAtomValue$$ExternalSyntheticOutline1.m("VOICE_CONTROLLER(");
            m4.append(i - 200);
            m4.append(")");
            str = m4.toString();
        } else if (i > 100) {
            StringBuilder m5 = VendorAtomValue$$ExternalSyntheticOutline1.m("FIRST_CONTROLLER(");
            m5.append(i - 100);
            m5.append(")");
            str = m5.toString();
        } else if (i == 0) {
            str = "NO_CONTROLLERS";
        } else {
            str = "UNKNOWN_SOURCE";
        }
        printWriter.println(str);
        printWriter.println("  - DefaultNetworkCallback -----");
        int i2 = 0;
        for (int i3 = 0; i3 < 16; i3++) {
            if (this.mHistory[i3] != null) {
                i2++;
            }
        }
        int i4 = this.mHistoryIndex + 16;
        while (true) {
            i4--;
            if (i4 < (this.mHistoryIndex + 16) - i2) {
                break;
            }
            StringBuilder m6 = VendorAtomValue$$ExternalSyntheticOutline1.m("  Previous NetworkCallback(");
            m6.append((this.mHistoryIndex + 16) - i4);
            m6.append("): ");
            m6.append(this.mHistory[i4 & 15]);
            printWriter.println(m6.toString());
        }
        printWriter.println("  - config ------");
        for (int i5 = 0; i5 < this.mMobileSignalControllers.size(); i5++) {
            this.mMobileSignalControllers.valueAt(i5).dump(printWriter);
        }
        this.mWifiSignalController.dump(printWriter);
        this.mEthernetSignalController.dump(printWriter);
        AccessPointControllerImpl accessPointControllerImpl = this.mAccessPoints;
        Objects.requireNonNull(accessPointControllerImpl);
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter);
        indentingPrintWriter.println("AccessPointControllerImpl:");
        indentingPrintWriter.increaseIndent();
        StringBuilder m7 = VendorAtomValue$$ExternalSyntheticOutline1.m("Callbacks: ");
        m7.append(Arrays.toString(accessPointControllerImpl.mCallbacks.toArray()));
        indentingPrintWriter.println(m7.toString());
        indentingPrintWriter.println("WifiPickerTracker: " + accessPointControllerImpl.mWifiPickerTracker.toString());
        if (accessPointControllerImpl.mWifiPickerTracker != null && !accessPointControllerImpl.mCallbacks.isEmpty()) {
            StringBuilder m8 = VendorAtomValue$$ExternalSyntheticOutline1.m("Connected: ");
            WifiPickerTracker wifiPickerTracker = accessPointControllerImpl.mWifiPickerTracker;
            Objects.requireNonNull(wifiPickerTracker);
            m8.append(wifiPickerTracker.mConnectedWifiEntry);
            indentingPrintWriter.println(m8.toString());
            StringBuilder sb = new StringBuilder();
            sb.append("Other wifi entries: ");
            WifiPickerTracker wifiPickerTracker2 = accessPointControllerImpl.mWifiPickerTracker;
            Objects.requireNonNull(wifiPickerTracker2);
            synchronized (wifiPickerTracker2.mLock) {
                arrayList = new ArrayList(wifiPickerTracker2.mWifiEntries);
            }
            sb.append(Arrays.toString(arrayList.toArray()));
            indentingPrintWriter.println(sb.toString());
        } else if (accessPointControllerImpl.mWifiPickerTracker != null) {
            indentingPrintWriter.println("WifiPickerTracker not started, cannot get reliable entries");
        }
        indentingPrintWriter.decreaseIndent();
        CallbackHandler callbackHandler = this.mCallbackHandler;
        Objects.requireNonNull(callbackHandler);
        printWriter.println("  - CallbackHandler -----");
        int i6 = 0;
        for (int i7 = 0; i7 < 64; i7++) {
            if (callbackHandler.mHistory[i7] != null) {
                i6++;
            }
        }
        int i8 = callbackHandler.mHistoryIndex + 64;
        while (true) {
            i8--;
            if (i8 >= (callbackHandler.mHistoryIndex + 64) - i6) {
                StringBuilder m9 = VendorAtomValue$$ExternalSyntheticOutline1.m("  Previous Callback(");
                m9.append((callbackHandler.mHistoryIndex + 64) - i8);
                m9.append("): ");
                m9.append(callbackHandler.mHistory[i8 & 63]);
                printWriter.println(m9.toString());
            } else {
                return;
            }
        }
    }

    @Override // com.android.systemui.statusbar.connectivity.NetworkController
    public final String getMobileDataNetworkName() {
        Objects.requireNonNull(this.mSubDefaults);
        MobileSignalController controllerWithSubId = getControllerWithSubId(SubscriptionManager.getActiveDataSubscriptionId());
        if (controllerWithSubId != null) {
            return ((MobileState) controllerWithSubId.mCurrentState).networkNameData;
        }
        return "";
    }

    @Override // com.android.systemui.statusbar.connectivity.NetworkController
    public final int getNumberSubscriptions() {
        return this.mMobileSignalControllers.size();
    }

    @Override // com.android.systemui.statusbar.connectivity.NetworkController
    public final boolean hasVoiceCallingFeature() {
        if (this.mPhone.getPhoneType() != 0) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.connectivity.NetworkController
    public final boolean isMobileDataNetworkInService() {
        Objects.requireNonNull(this.mSubDefaults);
        MobileSignalController controllerWithSubId = getControllerWithSubId(SubscriptionManager.getActiveDataSubscriptionId());
        if (controllerWithSubId == null || !((MobileState) controllerWithSubId.mCurrentState).isInService()) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.statusbar.connectivity.NetworkController
    public final boolean isRadioOn() {
        return !this.mAirplaneMode;
    }

    public final void notifyListeners() {
        CallbackHandler callbackHandler = this.mCallbackHandler;
        boolean z = this.mAirplaneMode;
        SignalIcon$MobileIconGroup signalIcon$MobileIconGroup = TelephonyIcons.CARRIER_NETWORK_CHANGE;
        callbackHandler.setIsAirplaneMode(new IconState(z, 2131232671, this.mContext.getString(2131951672)));
        this.mCallbackHandler.setNoSims(this.mHasNoSubs, this.mSimDetected);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.systemui.demomode.DemoModeCommandReceiver
    public final void onDemoModeFinished() {
        if (DEBUG) {
            Log.d("NetworkController", "Exiting demo mode");
        }
        updateMobileControllers();
        for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
            MobileSignalController valueAt = this.mMobileSignalControllers.valueAt(i);
            Objects.requireNonNull(valueAt);
            valueAt.mCurrentState.copyFrom(valueAt.mLastState);
        }
        WifiSignalController wifiSignalController = this.mWifiSignalController;
        Objects.requireNonNull(wifiSignalController);
        wifiSignalController.mCurrentState.copyFrom(wifiSignalController.mLastState);
        this.mReceiverHandler.post(this.mRegisterListeners);
        notifyAllListeners();
    }

    @Override // com.android.systemui.demomode.DemoModeCommandReceiver
    public final void onDemoModeStarted() {
        if (DEBUG) {
            Log.d("NetworkController", "Entering demo mode");
        }
        this.mListening = false;
        for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
            MobileSignalController valueAt = this.mMobileSignalControllers.valueAt(i);
            Objects.requireNonNull(valueAt);
            valueAt.mMobileStatusTracker.setListening(false);
            valueAt.mContext.getContentResolver().unregisterContentObserver(valueAt.mObserver);
            valueAt.mImsMmTelManager.unregisterImsRegistrationCallback(valueAt.mRegistrationCallback);
        }
        this.mSubscriptionManager.removeOnSubscriptionsChangedListener(this.mSubscriptionListener);
        this.mBroadcastDispatcher.unregisterReceiver(this);
        this.mDemoInetCondition = this.mInetCondition;
        WifiSignalController wifiSignalController = this.mWifiSignalController;
        Objects.requireNonNull(wifiSignalController);
        WifiState wifiState = (WifiState) wifiSignalController.mCurrentState;
        this.mDemoWifiState = wifiState;
        wifiState.ssid = "DemoMode";
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        char c;
        if (CHATTY) {
            Log.d("NetworkController", "onReceive: intent=" + intent);
        }
        String action = intent.getAction();
        Objects.requireNonNull(action);
        switch (action.hashCode()) {
            case -2104353374:
                if (action.equals("android.intent.action.SERVICE_STATE")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -1465084191:
                if (action.equals("android.intent.action.ACTION_DEFAULT_VOICE_SUBSCRIPTION_CHANGED")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -1172645946:
                if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -1138588223:
                if (action.equals("android.telephony.action.CARRIER_CONFIG_CHANGED")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -1076576821:
                if (action.equals("android.intent.action.AIRPLANE_MODE")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -229777127:
                if (action.equals("android.intent.action.SIM_STATE_CHANGED")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case -25388475:
                if (action.equals("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 464243859:
                if (action.equals("android.settings.panel.action.INTERNET_CONNECTIVITY")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                this.mLastServiceState = ServiceState.newFromBundle(intent.getExtras());
                if (this.mMobileSignalControllers.size() == 0) {
                    recalculateEmergency();
                    return;
                }
                return;
            case 1:
                recalculateEmergency();
                return;
            case 2:
                updateConnectivity();
                return;
            case 3:
                this.mConfig = MobileMappings.Config.readConfig(this.mContext);
                this.mReceiverHandler.post(new LockIconViewController$$ExternalSyntheticLambda2(this, 4));
                return;
            case 4:
                refreshLocale();
                updateAirplaneMode(false);
                return;
            case 5:
                if (!intent.getBooleanExtra("rebroadcastOnUnlock", false)) {
                    updateMobileControllers();
                    return;
                }
                return;
            case FalsingManager.VERSION /* 6 */:
                break;
            case 7:
                this.mMainHandler.post(new LockIconViewController$$ExternalSyntheticLambda1(this, 3));
                return;
            default:
                int intExtra = intent.getIntExtra("android.telephony.extra.SUBSCRIPTION_INDEX", -1);
                if (!SubscriptionManager.isValidSubscriptionId(intExtra)) {
                    WifiSignalController wifiSignalController = this.mWifiSignalController;
                    Objects.requireNonNull(wifiSignalController);
                    WifiStatusTracker wifiStatusTracker = wifiSignalController.mWifiTracker;
                    Objects.requireNonNull(wifiStatusTracker);
                    if (wifiStatusTracker.mWifiManager != null && intent.getAction().equals("android.net.wifi.WIFI_STATE_CHANGED")) {
                        wifiStatusTracker.updateWifiState();
                    }
                    wifiSignalController.copyWifiStates();
                    wifiSignalController.notifyListenersIfNecessary();
                    return;
                } else if (this.mMobileSignalControllers.indexOfKey(intExtra) >= 0) {
                    this.mMobileSignalControllers.get(intExtra).handleBroadcast(intent);
                    return;
                } else {
                    updateMobileControllers();
                    return;
                }
        }
        for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
            this.mMobileSignalControllers.valueAt(i).handleBroadcast(intent);
        }
        this.mConfig = MobileMappings.Config.readConfig(this.mContext);
        this.mReceiverHandler.post(new LockIconViewController$$ExternalSyntheticLambda0(this, 4));
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x0014, code lost:
        if (r0.isEmergencyOnly() != false) goto L_0x00d5;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void recalculateEmergency() {
        /*
            r7 = this;
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r0 = r7.mMobileSignalControllers
            int r0 = r0.size()
            r1 = 0
            r2 = 1
            if (r0 != 0) goto L_0x0018
            r7.mEmergencySource = r1
            android.telephony.ServiceState r0 = r7.mLastServiceState
            if (r0 == 0) goto L_0x00d6
            boolean r0 = r0.isEmergencyOnly()
            if (r0 == 0) goto L_0x00d6
            goto L_0x00d5
        L_0x0018:
            com.android.settingslib.mobile.MobileStatusTracker$SubscriptionDefaults r0 = r7.mSubDefaults
            java.util.Objects.requireNonNull(r0)
            int r0 = android.telephony.SubscriptionManager.getDefaultVoiceSubscriptionId()
            boolean r3 = android.telephony.SubscriptionManager.isValidSubscriptionId(r0)
            java.lang.String r4 = "NetworkController"
            if (r3 != 0) goto L_0x0063
            r3 = r1
        L_0x002a:
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r5 = r7.mMobileSignalControllers
            int r5 = r5.size()
            if (r3 >= r5) goto L_0x0063
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r5 = r7.mMobileSignalControllers
            java.lang.Object r5 = r5.valueAt(r3)
            com.android.systemui.statusbar.connectivity.MobileSignalController r5 = (com.android.systemui.statusbar.connectivity.MobileSignalController) r5
            java.util.Objects.requireNonNull(r5)
            T extends com.android.systemui.statusbar.connectivity.ConnectivityState r6 = r5.mCurrentState
            com.android.systemui.statusbar.connectivity.MobileState r6 = (com.android.systemui.statusbar.connectivity.MobileState) r6
            boolean r6 = r6.isEmergency
            if (r6 != 0) goto L_0x0060
            android.telephony.SubscriptionInfo r0 = r5.mSubscriptionInfo
            int r0 = r0.getSubscriptionId()
            int r0 = r0 + 100
            r7.mEmergencySource = r0
            boolean r0 = com.android.systemui.statusbar.connectivity.NetworkControllerImpl.DEBUG
            if (r0 == 0) goto L_0x00d6
            java.lang.String r0 = "Found emergency "
            java.lang.StringBuilder r0 = android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1.m(r0)
            java.lang.String r2 = r5.mTag
            androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline2.m(r0, r2, r4)
            goto L_0x00d6
        L_0x0060:
            int r3 = r3 + 1
            goto L_0x002a
        L_0x0063:
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r3 = r7.mMobileSignalControllers
            int r3 = r3.indexOfKey(r0)
            if (r3 < 0) goto L_0x008a
            int r1 = r0 + 200
            r7.mEmergencySource = r1
            boolean r1 = com.android.systemui.statusbar.connectivity.NetworkControllerImpl.DEBUG
            if (r1 == 0) goto L_0x0078
            java.lang.String r1 = "Getting emergency from "
            androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline1.m(r1, r0, r4)
        L_0x0078:
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r1 = r7.mMobileSignalControllers
            java.lang.Object r0 = r1.get(r0)
            com.android.systemui.statusbar.connectivity.MobileSignalController r0 = (com.android.systemui.statusbar.connectivity.MobileSignalController) r0
            java.util.Objects.requireNonNull(r0)
            T extends com.android.systemui.statusbar.connectivity.ConnectivityState r0 = r0.mCurrentState
            com.android.systemui.statusbar.connectivity.MobileState r0 = (com.android.systemui.statusbar.connectivity.MobileState) r0
            boolean r1 = r0.isEmergency
            goto L_0x00d6
        L_0x008a:
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r3 = r7.mMobileSignalControllers
            int r3 = r3.size()
            if (r3 != r2) goto L_0x00c8
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r0 = r7.mMobileSignalControllers
            int r0 = r0.keyAt(r1)
            int r0 = r0 + 400
            r7.mEmergencySource = r0
            boolean r0 = com.android.systemui.statusbar.connectivity.NetworkControllerImpl.DEBUG
            if (r0 == 0) goto L_0x00b6
            java.lang.String r0 = "Getting assumed emergency from "
            java.lang.StringBuilder r0 = android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1.m(r0)
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r2 = r7.mMobileSignalControllers
            int r2 = r2.keyAt(r1)
            r0.append(r2)
            java.lang.String r0 = r0.toString()
            android.util.Log.d(r4, r0)
        L_0x00b6:
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r0 = r7.mMobileSignalControllers
            java.lang.Object r0 = r0.valueAt(r1)
            com.android.systemui.statusbar.connectivity.MobileSignalController r0 = (com.android.systemui.statusbar.connectivity.MobileSignalController) r0
            java.util.Objects.requireNonNull(r0)
            T extends com.android.systemui.statusbar.connectivity.ConnectivityState r0 = r0.mCurrentState
            com.android.systemui.statusbar.connectivity.MobileState r0 = (com.android.systemui.statusbar.connectivity.MobileState) r0
            boolean r1 = r0.isEmergency
            goto L_0x00d6
        L_0x00c8:
            boolean r1 = com.android.systemui.statusbar.connectivity.NetworkControllerImpl.DEBUG
            if (r1 == 0) goto L_0x00d1
            java.lang.String r1 = "Cannot find controller for voice sub: "
            com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline1.m(r1, r0, r4)
        L_0x00d1:
            int r0 = r0 + 300
            r7.mEmergencySource = r0
        L_0x00d5:
            r1 = r2
        L_0x00d6:
            r7.mIsEmergency = r1
            com.android.systemui.statusbar.connectivity.CallbackHandler r7 = r7.mCallbackHandler
            r7.setEmergencyCallsOnly(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.connectivity.NetworkControllerImpl.recalculateEmergency():void");
    }

    public final void refreshLocale() {
        Locale locale = this.mContext.getResources().getConfiguration().locale;
        if (!locale.equals(this.mLocale)) {
            this.mLocale = locale;
            WifiSignalController wifiSignalController = this.mWifiSignalController;
            Objects.requireNonNull(wifiSignalController);
            WifiStatusTracker wifiStatusTracker = wifiSignalController.mWifiTracker;
            Objects.requireNonNull(wifiStatusTracker);
            wifiStatusTracker.updateStatusLabel();
            wifiStatusTracker.mCallback.run();
            notifyAllListeners();
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(SignalCallback signalCallback) {
        CallbackHandler callbackHandler = this.mCallbackHandler;
        Objects.requireNonNull(callbackHandler);
        callbackHandler.obtainMessage(7, 0, 0, signalCallback).sendToTarget();
    }

    @GuardedBy({"mLock"})
    @VisibleForTesting
    public void setCurrentSubscriptionsLocked(List<SubscriptionInfo> list) {
        SparseArray sparseArray;
        int i;
        int i2;
        List<SubscriptionInfo> list2;
        List<SubscriptionInfo> list3 = list;
        Collections.sort(list3, new Comparator<SubscriptionInfo>() { // from class: com.android.systemui.statusbar.connectivity.NetworkControllerImpl.8
            @Override // java.util.Comparator
            public final int compare(SubscriptionInfo subscriptionInfo, SubscriptionInfo subscriptionInfo2) {
                int i3;
                int i4;
                SubscriptionInfo subscriptionInfo3 = subscriptionInfo;
                SubscriptionInfo subscriptionInfo4 = subscriptionInfo2;
                if (subscriptionInfo3.getSimSlotIndex() == subscriptionInfo4.getSimSlotIndex()) {
                    i4 = subscriptionInfo3.getSubscriptionId();
                    i3 = subscriptionInfo4.getSubscriptionId();
                } else {
                    i4 = subscriptionInfo3.getSimSlotIndex();
                    i3 = subscriptionInfo4.getSimSlotIndex();
                }
                return i4 - i3;
            }
        });
        this.mCurrentSubscriptions = list3;
        SparseArray sparseArray2 = new SparseArray();
        for (int i3 = 0; i3 < this.mMobileSignalControllers.size(); i3++) {
            sparseArray2.put(this.mMobileSignalControllers.keyAt(i3), this.mMobileSignalControllers.valueAt(i3));
        }
        this.mMobileSignalControllers.clear();
        int size = list.size();
        List<SubscriptionInfo> list4 = list3;
        int i4 = 0;
        while (i4 < size) {
            int subscriptionId = list4.get(i4).getSubscriptionId();
            if (sparseArray2.indexOfKey(subscriptionId) >= 0) {
                this.mMobileSignalControllers.put(subscriptionId, (MobileSignalController) sparseArray2.get(subscriptionId));
                sparseArray2.remove(subscriptionId);
                i2 = i4;
                i = size;
                list2 = list3;
                sparseArray = sparseArray2;
            } else {
                sparseArray = sparseArray2;
                i = size;
                MobileSignalController mobileSignalController = new MobileSignalController(this.mContext, this.mConfig, this.mHasMobileDataFeature, this.mPhone.createForSubscriptionId(subscriptionId), this.mCallbackHandler, this, list4.get(i4), this.mSubDefaults, this.mReceiverHandler.getLooper(), this.mCarrierConfigTracker, this.mFeatureFlags);
                ((MobileState) mobileSignalController.mCurrentState).userSetup = this.mUserSetup;
                mobileSignalController.notifyListenersIfNecessary();
                this.mMobileSignalControllers.put(subscriptionId, mobileSignalController);
                list2 = list;
                i2 = i4;
                if (list2.get(i2).getSimSlotIndex() == 0) {
                    this.mDefaultSignalController = mobileSignalController;
                }
                if (this.mListening) {
                    mobileSignalController.registerListener();
                }
                list4 = list2;
            }
            i4 = i2 + 1;
            list3 = list2;
            size = i;
            sparseArray2 = sparseArray;
        }
        if (this.mListening) {
            int i5 = 0;
            for (SparseArray sparseArray3 = sparseArray2; i5 < sparseArray3.size(); sparseArray3 = sparseArray3) {
                int keyAt = sparseArray3.keyAt(i5);
                if (sparseArray3.get(keyAt) == this.mDefaultSignalController) {
                    this.mDefaultSignalController = null;
                }
                MobileSignalController mobileSignalController2 = (MobileSignalController) sparseArray3.get(keyAt);
                Objects.requireNonNull(mobileSignalController2);
                mobileSignalController2.mMobileStatusTracker.setListening(false);
                mobileSignalController2.mContext.getContentResolver().unregisterContentObserver(mobileSignalController2.mObserver);
                mobileSignalController2.mImsMmTelManager.unregisterImsRegistrationCallback(mobileSignalController2.mRegistrationCallback);
                i5++;
            }
        }
        this.mCallbackHandler.setSubs(list4);
        notifyAllListeners();
        pushConnectivityToSignals();
        updateAirplaneMode(true);
    }

    @Override // com.android.systemui.statusbar.connectivity.NetworkController
    public final void setWifiEnabled(final boolean z) {
        new AsyncTask<Void, Void, Void>() { // from class: com.android.systemui.statusbar.connectivity.NetworkControllerImpl.7
            @Override // android.os.AsyncTask
            public final Void doInBackground(Void[] voidArr) {
                NetworkControllerImpl.this.mWifiManager.setWifiEnabled(z);
                return null;
            }
        }.execute(new Void[0]);
    }

    public final void updateAirplaneMode(boolean z) {
        boolean z2 = true;
        if (Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) != 1) {
            z2 = false;
        }
        if (z2 != this.mAirplaneMode || z) {
            this.mAirplaneMode = z2;
            for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
                MobileSignalController valueAt = this.mMobileSignalControllers.valueAt(i);
                boolean z3 = this.mAirplaneMode;
                Objects.requireNonNull(valueAt);
                ((MobileState) valueAt.mCurrentState).airplaneMode = z3;
                valueAt.notifyListenersIfNecessary();
            }
            notifyListeners();
        }
    }

    public final void updateConnectivity() {
        boolean z;
        boolean z2;
        int i;
        boolean z3;
        int[] transportTypes;
        this.mConnectedTransports.clear();
        this.mValidatedTransports.clear();
        NetworkCapabilities networkCapabilities = this.mLastDefaultNetworkCapabilities;
        boolean z4 = false;
        if (networkCapabilities != null) {
            for (int i2 : networkCapabilities.getTransportTypes()) {
                if (i2 == 0 || i2 == 1 || i2 == 3) {
                    if (i2 != 0 || Utils.tryGetWifiInfoForVcn(this.mLastDefaultNetworkCapabilities) == null) {
                        this.mConnectedTransports.set(i2);
                        if (this.mLastDefaultNetworkCapabilities.hasCapability(16)) {
                            this.mValidatedTransports.set(i2);
                        }
                    } else {
                        this.mConnectedTransports.set(1);
                        if (this.mLastDefaultNetworkCapabilities.hasCapability(16)) {
                            this.mValidatedTransports.set(1);
                        }
                    }
                }
            }
        }
        if (this.mForceCellularValidated) {
            this.mValidatedTransports.set(0);
        }
        if (CHATTY) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("updateConnectivity: mConnectedTransports=");
            m.append(this.mConnectedTransports);
            Log.d("NetworkController", m.toString());
            Log.d("NetworkController", "updateConnectivity: mValidatedTransports=" + this.mValidatedTransports);
        }
        if (this.mValidatedTransports.get(0) || this.mValidatedTransports.get(1) || this.mValidatedTransports.get(3)) {
            z = true;
        } else {
            z = false;
        }
        this.mInetCondition = z;
        pushConnectivityToSignals();
        if (this.mProviderModelBehavior) {
            if (this.mConnectedTransports.get(0) || this.mConnectedTransports.get(1) || this.mConnectedTransports.get(3)) {
                z2 = false;
            } else {
                z2 = true;
            }
            this.mNoDefaultNetwork = z2;
            this.mCallbackHandler.setConnectivityStatus(z2, !this.mInetCondition, this.mNoNetworksAvailable);
            for (int i3 = 0; i3 < this.mMobileSignalControllers.size(); i3++) {
                MobileSignalController valueAt = this.mMobileSignalControllers.valueAt(i3);
                Objects.requireNonNull(valueAt);
                MobileState mobileState = (MobileState) valueAt.mCurrentState;
                Objects.requireNonNull(mobileState);
                ServiceState serviceState = mobileState.serviceState;
                if (serviceState == null) {
                    i = -1;
                } else {
                    i = serviceState.getState();
                }
                if (i != 0) {
                    z3 = true;
                } else {
                    z3 = false;
                }
                valueAt.notifyCallStateChange(new IconState(z3 & (!valueAt.hideNoCalling()), 2131232227, valueAt.getTextIfExists(2131951761).toString()), valueAt.mSubscriptionInfo.getSubscriptionId());
            }
            notifyAllListeners();
            return;
        }
        if (!this.mConnectedTransports.get(0) && !this.mConnectedTransports.get(1) && !this.mConnectedTransports.get(3)) {
            z4 = true;
        }
        this.mNoDefaultNetwork = z4;
        this.mCallbackHandler.setConnectivityStatus(z4, true ^ this.mInetCondition, this.mNoNetworksAvailable);
    }

    public final void updateMobileControllers() {
        if (this.mListening) {
            doUpdateMobileControllers();
        }
    }

    @VisibleForTesting
    public void updateNoSims() {
        boolean z;
        boolean z2 = false;
        if (!this.mHasMobileDataFeature || this.mMobileSignalControllers.size() != 0) {
            z = false;
        } else {
            z = true;
        }
        int activeModemCount = this.mPhone.getActiveModemCount();
        int i = 0;
        while (true) {
            if (i < activeModemCount) {
                int simState = this.mPhone.getSimState(i);
                if (simState != 1 && simState != 0) {
                    z2 = true;
                    break;
                }
                i++;
            } else {
                break;
            }
        }
        if (z != this.mHasNoSubs || z2 != this.mSimDetected) {
            this.mHasNoSubs = z;
            this.mSimDetected = z2;
            this.mCallbackHandler.setNoSims(z, z2);
        }
    }

    /* renamed from: -$$Nest$mgetProcessedTransportTypes  reason: not valid java name */
    public static int[] m90$$Nest$mgetProcessedTransportTypes(NetworkControllerImpl networkControllerImpl, NetworkCapabilities networkCapabilities) {
        Objects.requireNonNull(networkControllerImpl);
        int[] transportTypes = networkCapabilities.getTransportTypes();
        int i = 0;
        while (true) {
            if (i < transportTypes.length) {
                if (transportTypes[i] == 0 && Utils.tryGetWifiInfoForVcn(networkCapabilities) != null) {
                    transportTypes[i] = 1;
                    break;
                }
                i++;
            } else {
                break;
            }
        }
        return transportTypes;
    }

    public final MobileSignalController getControllerWithSubId(int i) {
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            if (DEBUG) {
                Log.e("NetworkController", "No data sim selected");
            }
            return this.mDefaultSignalController;
        } else if (this.mMobileSignalControllers.indexOfKey(i) >= 0) {
            return this.mMobileSignalControllers.get(i);
        } else {
            if (DEBUG) {
                KeyguardUpdateMonitor$$ExternalSyntheticOutline1.m("Cannot find controller for data sub: ", i, "NetworkController");
            }
            return this.mDefaultSignalController;
        }
    }

    @VisibleForTesting
    public void handleConfigurationChanged() {
        SignalIcon$MobileIconGroup signalIcon$MobileIconGroup;
        updateMobileControllers();
        for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
            MobileSignalController valueAt = this.mMobileSignalControllers.valueAt(i);
            MobileMappings.Config config = this.mConfig;
            Objects.requireNonNull(valueAt);
            valueAt.mConfig = config;
            valueAt.mInflateSignalStrengths = YieldKt.shouldInflateSignalStrength(valueAt.mContext, valueAt.mSubscriptionInfo.getSubscriptionId());
            valueAt.mNetworkToIconLookup = MobileMappings.mapIconSets(valueAt.mConfig);
            if (!valueAt.mConfig.showAtLeast3G) {
                signalIcon$MobileIconGroup = TelephonyIcons.G;
            } else {
                signalIcon$MobileIconGroup = TelephonyIcons.THREE_G;
            }
            valueAt.mDefaultIcons = signalIcon$MobileIconGroup;
            valueAt.updateTelephony();
            if (this.mProviderModelBehavior) {
                valueAt.refreshCallIndicator(this.mCallbackHandler);
            }
        }
        refreshLocale();
    }

    @VisibleForTesting
    public boolean hasCorrectMobileControllers(List<SubscriptionInfo> list) {
        if (list.size() != this.mMobileSignalControllers.size()) {
            return false;
        }
        for (SubscriptionInfo subscriptionInfo : list) {
            if (this.mMobileSignalControllers.indexOfKey(subscriptionInfo.getSubscriptionId()) < 0) {
                return false;
            }
        }
        return true;
    }

    public final void notifyAllListeners() {
        notifyListeners();
        for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
            this.mMobileSignalControllers.valueAt(i).notifyListeners();
        }
        this.mWifiSignalController.notifyListeners();
        this.mEthernetSignalController.notifyListeners();
    }

    @VisibleForTesting
    public void setNoNetworksAvailable(boolean z) {
        this.mNoNetworksAvailable = z;
    }

    @Override // com.android.systemui.statusbar.connectivity.NetworkController
    public final DataSaverControllerImpl getDataSaverController() {
        return this.mDataSaverController;
    }

    @Override // com.android.systemui.statusbar.connectivity.NetworkController
    public final DataUsageController getMobileDataController() {
        return this.mDataUsageController;
    }

    @Override // com.android.systemui.statusbar.connectivity.NetworkController
    public final boolean hasEmergencyCryptKeeperText() {
        return EncryptionHelper.IS_DATA_ENCRYPTED;
    }

    @Override // com.android.systemui.statusbar.connectivity.NetworkController
    public final boolean hasMobileDataFeature() {
        return this.mHasMobileDataFeature;
    }

    @VisibleForTesting
    public boolean isUserSetup() {
        return this.mUserSetup;
    }
}
