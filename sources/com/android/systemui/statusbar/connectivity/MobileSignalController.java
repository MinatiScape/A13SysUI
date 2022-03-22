package com.android.systemui.statusbar.connectivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.CarrierConfigManager;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.ims.ImsException;
import android.telephony.ims.ImsMmTelManager;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsRegistrationAttributes;
import android.telephony.ims.RegistrationManager;
import android.util.Log;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline2;
import androidx.leanback.R$layout;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.SignalIcon$IconGroup;
import com.android.settingslib.SignalIcon$MobileIconGroup;
import com.android.settingslib.graph.SignalDrawable;
import com.android.settingslib.mobile.MobileMappings;
import com.android.settingslib.mobile.MobileStatusTracker;
import com.android.settingslib.mobile.MobileStatusTracker$$ExternalSyntheticLambda0;
import com.android.settingslib.mobile.TelephonyIcons;
import com.android.systemui.util.CarrierConfigTracker;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MobileSignalController extends SignalController<MobileState, SignalIcon$MobileIconGroup> {
    public static final SimpleDateFormat SSDF = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
    public final CarrierConfigTracker mCarrierConfigTracker;
    public MobileMappings.Config mConfig;
    public SignalIcon$MobileIconGroup mDefaultIcons;
    public final MobileStatusTracker.SubscriptionDefaults mDefaults;
    public final ImsMmTelManager mImsMmTelManager;
    public int mLastLevel;
    public int mLastWlanCrossSimLevel;
    public int mLastWlanLevel;
    public int mLastWwanLevel;
    public final AnonymousClass1 mMobileCallback;
    public int mMobileStatusHistoryIndex;
    @VisibleForTesting
    public MobileStatusTracker mMobileStatusTracker;
    public final String mNetworkNameDefault;
    public HashMap mNetworkToIconLookup;
    public final AnonymousClass3 mObserver;
    public final TelephonyManager mPhone;
    public final boolean mProviderModelBehavior;
    public final Handler mReceiverHandler;
    public final SubscriptionInfo mSubscriptionInfo;
    public int mImsType = 1;
    @VisibleForTesting
    public boolean mInflateSignalStrengths = false;
    public final String[] mMobileStatusHistory = new String[64];
    public final AnonymousClass2 mRegistrationCallback = new RegistrationManager.RegistrationCallback() { // from class: com.android.systemui.statusbar.connectivity.MobileSignalController.2
        public final void onRegistered(ImsRegistrationAttributes imsRegistrationAttributes) {
            String str = MobileSignalController.this.mTag;
            Log.d(str, "onRegistered: attributes=" + imsRegistrationAttributes);
            int transportType = imsRegistrationAttributes.getTransportType();
            int attributeFlags = imsRegistrationAttributes.getAttributeFlags();
            if (transportType == 1) {
                MobileSignalController mobileSignalController = MobileSignalController.this;
                mobileSignalController.mImsType = 1;
                int callStrengthIcon = MobileSignalController.getCallStrengthIcon(mobileSignalController.mLastWwanLevel, false);
                MobileSignalController mobileSignalController2 = MobileSignalController.this;
                IconState iconState = new IconState(true, callStrengthIcon, mobileSignalController2.getCallStrengthDescription(mobileSignalController2.mLastWwanLevel, false));
                MobileSignalController mobileSignalController3 = MobileSignalController.this;
                mobileSignalController3.notifyCallStateChange(iconState, mobileSignalController3.mSubscriptionInfo.getSubscriptionId());
            } else if (transportType != 2) {
            } else {
                if (attributeFlags == 0) {
                    MobileSignalController mobileSignalController4 = MobileSignalController.this;
                    mobileSignalController4.mImsType = 2;
                    int callStrengthIcon2 = MobileSignalController.getCallStrengthIcon(mobileSignalController4.mLastWlanLevel, true);
                    MobileSignalController mobileSignalController5 = MobileSignalController.this;
                    IconState iconState2 = new IconState(true, callStrengthIcon2, mobileSignalController5.getCallStrengthDescription(mobileSignalController5.mLastWlanLevel, true));
                    MobileSignalController mobileSignalController6 = MobileSignalController.this;
                    mobileSignalController6.notifyCallStateChange(iconState2, mobileSignalController6.mSubscriptionInfo.getSubscriptionId());
                } else if (attributeFlags == 1) {
                    MobileSignalController mobileSignalController7 = MobileSignalController.this;
                    mobileSignalController7.mImsType = 3;
                    int callStrengthIcon3 = MobileSignalController.getCallStrengthIcon(mobileSignalController7.mLastWlanCrossSimLevel, false);
                    MobileSignalController mobileSignalController8 = MobileSignalController.this;
                    IconState iconState3 = new IconState(true, callStrengthIcon3, mobileSignalController8.getCallStrengthDescription(mobileSignalController8.mLastWlanCrossSimLevel, false));
                    MobileSignalController mobileSignalController9 = MobileSignalController.this;
                    mobileSignalController9.notifyCallStateChange(iconState3, mobileSignalController9.mSubscriptionInfo.getSubscriptionId());
                }
            }
        }

        @Override // android.telephony.ims.RegistrationManager.RegistrationCallback
        public final void onUnregistered(ImsReasonInfo imsReasonInfo) {
            String str = MobileSignalController.this.mTag;
            Log.d(str, "onDeregistered: info=" + imsReasonInfo);
            MobileSignalController mobileSignalController = MobileSignalController.this;
            mobileSignalController.mImsType = 1;
            int callStrengthIcon = MobileSignalController.getCallStrengthIcon(mobileSignalController.mLastWwanLevel, false);
            MobileSignalController mobileSignalController2 = MobileSignalController.this;
            IconState iconState = new IconState(true, callStrengthIcon, mobileSignalController2.getCallStrengthDescription(mobileSignalController2.mLastWwanLevel, false));
            MobileSignalController mobileSignalController3 = MobileSignalController.this;
            mobileSignalController3.notifyCallStateChange(iconState, mobileSignalController3.mSubscriptionInfo.getSubscriptionId());
        }
    };
    public final AnonymousClass4 mTryRegisterIms = new Runnable() { // from class: com.android.systemui.statusbar.connectivity.MobileSignalController.4
        public int mRetryCount;

        @Override // java.lang.Runnable
        public final void run() {
            try {
                this.mRetryCount++;
                MobileSignalController mobileSignalController = MobileSignalController.this;
                ImsMmTelManager imsMmTelManager = mobileSignalController.mImsMmTelManager;
                Handler handler = mobileSignalController.mReceiverHandler;
                Objects.requireNonNull(handler);
                imsMmTelManager.registerImsRegistrationCallback(new MobileStatusTracker$$ExternalSyntheticLambda0(handler, 1), MobileSignalController.this.mRegistrationCallback);
                Log.d(MobileSignalController.this.mTag, "registerImsRegistrationCallback succeeded");
            } catch (ImsException | RuntimeException e) {
                if (this.mRetryCount < 12) {
                    Log.e(MobileSignalController.this.mTag, this.mRetryCount + " registerImsRegistrationCallback failed", e);
                    MobileSignalController mobileSignalController2 = MobileSignalController.this;
                    mobileSignalController2.mReceiverHandler.postDelayed(mobileSignalController2.mTryRegisterIms, 5000L);
                }
            }
        }
    };
    public final String mNetworkNameSeparator = getTextIfExists(2131953318).toString();

    /* renamed from: com.android.systemui.statusbar.connectivity.MobileSignalController$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements MobileStatusTracker.Callback {
        public String mLastStatus;

        public AnonymousClass1() {
        }

        public final void onMobileStatusChanged(boolean z, MobileStatusTracker.MobileStatus mobileStatus) {
            int i;
            int i2;
            int signalLevel;
            if (Log.isLoggable(MobileSignalController.this.mTag, 3)) {
                Log.d(MobileSignalController.this.mTag, "onMobileStatusChanged= updateTelephony=" + z + " mobileStatus=" + mobileStatus.toString());
            }
            String mobileStatus2 = mobileStatus.toString();
            if (!mobileStatus2.equals(this.mLastStatus)) {
                this.mLastStatus = mobileStatus2;
                MobileSignalController mobileSignalController = MobileSignalController.this;
                Objects.requireNonNull(mobileSignalController);
                String[] strArr = mobileSignalController.mMobileStatusHistory;
                int i3 = mobileSignalController.mMobileStatusHistoryIndex;
                strArr[i3] = MobileSignalController.SSDF.format(Long.valueOf(System.currentTimeMillis())) + "," + mobileStatus2;
                mobileSignalController.mMobileStatusHistoryIndex = (i3 + 1) % 64;
            }
            MobileSignalController mobileSignalController2 = MobileSignalController.this;
            Objects.requireNonNull(mobileSignalController2);
            MobileState mobileState = (MobileState) mobileSignalController2.mCurrentState;
            Objects.requireNonNull(mobileState);
            ServiceState serviceState = mobileState.serviceState;
            if (serviceState == null) {
                i = -1;
            } else {
                i = serviceState.getState();
            }
            MobileState mobileState2 = (MobileState) mobileSignalController2.mCurrentState;
            Objects.requireNonNull(mobileState2);
            mobileState2.activityIn = mobileStatus.activityIn;
            mobileState2.activityOut = mobileStatus.activityOut;
            mobileState2.dataSim = mobileStatus.dataSim;
            mobileState2.carrierNetworkChangeMode = mobileStatus.carrierNetworkChangeMode;
            mobileState2.dataState = mobileStatus.dataState;
            SignalStrength signalStrength = mobileStatus.signalStrength;
            mobileState2.signalStrength = signalStrength;
            mobileState2.telephonyDisplayInfo = mobileStatus.telephonyDisplayInfo;
            mobileState2.serviceState = mobileStatus.serviceState;
            boolean z2 = false;
            if (mobileSignalController2.mProviderModelBehavior && (signalLevel = mobileSignalController2.getSignalLevel(signalStrength)) != mobileSignalController2.mLastLevel) {
                mobileSignalController2.mLastLevel = signalLevel;
                mobileSignalController2.mLastWwanLevel = signalLevel;
                if (mobileSignalController2.mImsType == 1) {
                    mobileSignalController2.notifyCallStateChange(new IconState(true, MobileSignalController.getCallStrengthIcon(signalLevel, false), mobileSignalController2.getCallStrengthDescription(signalLevel, false)), mobileSignalController2.mSubscriptionInfo.getSubscriptionId());
                }
                if (((MobileState) mobileSignalController2.mCurrentState).dataSim) {
                    NetworkControllerImpl networkControllerImpl = mobileSignalController2.mNetworkController;
                    Objects.requireNonNull(networkControllerImpl);
                    for (int i4 = 0; i4 < networkControllerImpl.mMobileSignalControllers.size(); i4++) {
                        MobileSignalController valueAt = networkControllerImpl.mMobileSignalControllers.valueAt(i4);
                        Objects.requireNonNull(valueAt);
                        if (valueAt.mProviderModelBehavior) {
                            valueAt.mLastWlanCrossSimLevel = signalLevel;
                            if (valueAt.mImsType == 3) {
                                valueAt.notifyCallStateChange(new IconState(true, MobileSignalController.getCallStrengthIcon(signalLevel, false), valueAt.getCallStrengthDescription(signalLevel, false)), valueAt.mSubscriptionInfo.getSubscriptionId());
                            }
                        }
                    }
                }
            }
            if (mobileSignalController2.mProviderModelBehavior) {
                MobileState mobileState3 = (MobileState) mobileSignalController2.mCurrentState;
                Objects.requireNonNull(mobileState3);
                ServiceState serviceState2 = mobileState3.serviceState;
                if (serviceState2 == null) {
                    i2 = -1;
                } else {
                    i2 = serviceState2.getState();
                }
                if (i != i2 && (i == -1 || i == 0 || i2 == 0)) {
                    MobileState mobileState4 = (MobileState) mobileSignalController2.mCurrentState;
                    Objects.requireNonNull(mobileState4);
                    ServiceState serviceState3 = mobileState4.serviceState;
                    if (serviceState3 != null && serviceState3.getState() == 0) {
                        z2 = true;
                    }
                    mobileSignalController2.notifyCallStateChange(new IconState((!z2) & (!mobileSignalController2.hideNoCalling()), 2131232227, mobileSignalController2.getTextIfExists(2131951761).toString()), mobileSignalController2.mSubscriptionInfo.getSubscriptionId());
                }
            }
            if (z) {
                MobileSignalController.this.updateTelephony();
            } else {
                MobileSignalController.this.notifyListenersIfNecessary();
            }
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /* JADX WARN: Type inference failed for: r0v19, types: [com.android.systemui.statusbar.connectivity.MobileSignalController$3] */
    /* JADX WARN: Type inference failed for: r0v7, types: [com.android.systemui.statusbar.connectivity.MobileSignalController$2] */
    /* JADX WARN: Type inference failed for: r0v8, types: [com.android.systemui.statusbar.connectivity.MobileSignalController$4] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public MobileSignalController(android.content.Context r11, com.android.settingslib.mobile.MobileMappings.Config r12, boolean r13, android.telephony.TelephonyManager r14, com.android.systemui.statusbar.connectivity.CallbackHandler r15, com.android.systemui.statusbar.connectivity.NetworkControllerImpl r16, android.telephony.SubscriptionInfo r17, com.android.settingslib.mobile.MobileStatusTracker.SubscriptionDefaults r18, android.os.Looper r19, com.android.systemui.util.CarrierConfigTracker r20, com.android.systemui.flags.FeatureFlags r21) {
        /*
            Method dump skipped, instructions count: 228
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.connectivity.MobileSignalController.<init>(android.content.Context, com.android.settingslib.mobile.MobileMappings$Config, boolean, android.telephony.TelephonyManager, com.android.systemui.statusbar.connectivity.CallbackHandler, com.android.systemui.statusbar.connectivity.NetworkControllerImpl, android.telephony.SubscriptionInfo, com.android.settingslib.mobile.MobileStatusTracker$SubscriptionDefaults, android.os.Looper, com.android.systemui.util.CarrierConfigTracker, com.android.systemui.flags.FeatureFlags):void");
    }

    public final int getSignalLevel(SignalStrength signalStrength) {
        if (signalStrength == null) {
            return 0;
        }
        if (signalStrength.isGsm() || !this.mConfig.alwaysShowCdmaRssi) {
            return signalStrength.getLevel();
        }
        List cellSignalStrengths = signalStrength.getCellSignalStrengths(CellSignalStrengthCdma.class);
        if (!cellSignalStrengths.isEmpty()) {
            return ((CellSignalStrengthCdma) cellSignalStrengths.get(0)).getLevel();
        }
        return 0;
    }

    /* loaded from: classes.dex */
    public static final class QsInfo {
        public final CharSequence description;
        public final IconState icon;
        public final int ratTypeIcon;

        public QsInfo(int i, IconState iconState, String str) {
            this.ratTypeIcon = i;
            this.icon = iconState;
            this.description = str;
        }
    }

    public static int getCallStrengthIcon(int i, boolean z) {
        if (z) {
            return TelephonyIcons.WIFI_CALL_STRENGTH_ICONS[i];
        }
        return TelephonyIcons.MOBILE_CALL_STRENGTH_ICONS[i];
    }

    public final void checkDefaultData() {
        MobileState mobileState = (MobileState) this.mCurrentState;
        boolean z = false;
        if (mobileState.iconGroup != TelephonyIcons.NOT_DEFAULT_DATA) {
            mobileState.defaultDataOff = false;
            return;
        }
        NetworkControllerImpl networkControllerImpl = this.mNetworkController;
        Objects.requireNonNull(networkControllerImpl);
        Objects.requireNonNull(networkControllerImpl.mSubDefaults);
        MobileSignalController controllerWithSubId = networkControllerImpl.getControllerWithSubId(SubscriptionManager.getActiveDataSubscriptionId());
        if (controllerWithSubId != null) {
            z = !controllerWithSubId.mPhone.isDataConnectionAllowed();
        }
        mobileState.defaultDataOff = z;
    }

    @Override // com.android.systemui.statusbar.connectivity.SignalController
    public final MobileState cleanState() {
        return new MobileState();
    }

    public final String getCallStrengthDescription(int i, boolean z) {
        if (z) {
            return getTextIfExists(R$layout.WIFI_CONNECTION_STRENGTH[i]).toString();
        }
        return getTextIfExists(R$layout.PHONE_SIGNAL_STRENGTH[i]).toString();
    }

    @Override // com.android.systemui.statusbar.connectivity.SignalController
    public final int getCurrentIconId() {
        int i;
        boolean z;
        boolean z2;
        boolean z3;
        int i2;
        int i3;
        MobileState mobileState = (MobileState) this.mCurrentState;
        SignalIcon$IconGroup signalIcon$IconGroup = mobileState.iconGroup;
        if (signalIcon$IconGroup == TelephonyIcons.CARRIER_NETWORK_CHANGE) {
            if (this.mInflateSignalStrengths) {
                i3 = CellSignalStrength.getNumSignalStrengthLevels() + 1;
            } else {
                i3 = CellSignalStrength.getNumSignalStrengthLevels();
            }
            int i4 = SignalDrawable.$r8$clinit;
            return (i3 << 8) | 196608;
        }
        int i5 = 0;
        if (mobileState.connected) {
            int i6 = mobileState.level;
            boolean z4 = this.mInflateSignalStrengths;
            if (z4) {
                i6++;
            }
            if (!mobileState.userSetup || (signalIcon$IconGroup != TelephonyIcons.DATA_DISABLED && (signalIcon$IconGroup != TelephonyIcons.NOT_DEFAULT_DATA || !mobileState.defaultDataOff))) {
                z = false;
            } else {
                z = true;
            }
            if (mobileState.inetCondition == 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z || z2) {
                z3 = true;
            } else {
                z3 = false;
            }
            if (z4) {
                i2 = CellSignalStrength.getNumSignalStrengthLevels() + 1;
            } else {
                i2 = CellSignalStrength.getNumSignalStrengthLevels();
            }
            int i7 = SignalDrawable.$r8$clinit;
            if (z3) {
                i5 = 2;
            }
            return (i2 << 8) | (i5 << 16) | i6;
        } else if (!mobileState.enabled) {
            return 0;
        } else {
            if (this.mInflateSignalStrengths) {
                i = CellSignalStrength.getNumSignalStrengthLevels() + 1;
            } else {
                i = CellSignalStrength.getNumSignalStrengthLevels();
            }
            int i8 = SignalDrawable.$r8$clinit;
            return (i << 8) | 131072 | 0;
        }
    }

    public final boolean hideNoCalling() {
        boolean z;
        NetworkControllerImpl networkControllerImpl = this.mNetworkController;
        Objects.requireNonNull(networkControllerImpl);
        if (!networkControllerImpl.mNoDefaultNetwork) {
            CarrierConfigTracker carrierConfigTracker = this.mCarrierConfigTracker;
            int subscriptionId = this.mSubscriptionInfo.getSubscriptionId();
            Objects.requireNonNull(carrierConfigTracker);
            synchronized (carrierConfigTracker.mNoCallingConfigs) {
                if (carrierConfigTracker.mNoCallingConfigs.indexOfKey(subscriptionId) >= 0) {
                    z = carrierConfigTracker.mNoCallingConfigs.get(subscriptionId);
                } else {
                    if (!carrierConfigTracker.mDefaultNoCallingConfigLoaded) {
                        carrierConfigTracker.mDefaultNoCallingConfig = CarrierConfigManager.getDefaultConfig().getBoolean("use_ip_for_calling_indicator_bool");
                        carrierConfigTracker.mDefaultNoCallingConfigLoaded = true;
                    }
                    z = carrierConfigTracker.mDefaultNoCallingConfig;
                }
            }
            if (z) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:102:0x015e, code lost:
        if (r2.airplaneMode == false) goto L_0x0160;
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x0160, code lost:
        r9 = r1;
        r18 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x0122, code lost:
        if (((com.android.systemui.statusbar.connectivity.MobileState) r19.mCurrentState).airplaneMode == false) goto L_0x0160;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00ba  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x00e6  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0125  */
    /* JADX WARN: Type inference failed for: r1v5, types: [T extends com.android.systemui.statusbar.connectivity.ConnectivityState, com.android.systemui.statusbar.connectivity.ConnectivityState] */
    /* JADX WARN: Type inference failed for: r6v27, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r6v28 */
    /* JADX WARN: Type inference failed for: r6v33 */
    /* JADX WARN: Type inference failed for: r6v34, types: [java.lang.String] */
    /* JADX WARN: Unknown variable types count: 1 */
    @Override // com.android.systemui.statusbar.connectivity.SignalController
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void notifyListeners(com.android.systemui.statusbar.connectivity.SignalCallback r20) {
        /*
            Method dump skipped, instructions count: 436
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.connectivity.MobileSignalController.notifyListeners(com.android.systemui.statusbar.connectivity.SignalCallback):void");
    }

    public final void refreshCallIndicator(SignalCallback signalCallback) {
        boolean z;
        MobileState mobileState = (MobileState) this.mCurrentState;
        Objects.requireNonNull(mobileState);
        ServiceState serviceState = mobileState.serviceState;
        if (serviceState != null && serviceState.getState() == 0) {
            z = true;
        } else {
            z = false;
        }
        IconState iconState = new IconState((!z) & (!hideNoCalling()), 2131232227, getTextIfExists(2131951761).toString());
        signalCallback.setCallIndicator(iconState, this.mSubscriptionInfo.getSubscriptionId());
        int i = this.mImsType;
        if (i == 1) {
            iconState = new IconState(true, getCallStrengthIcon(this.mLastWwanLevel, false), getCallStrengthDescription(this.mLastWwanLevel, false));
        } else if (i == 2) {
            iconState = new IconState(true, getCallStrengthIcon(this.mLastWlanLevel, true), getCallStrengthDescription(this.mLastWlanLevel, true));
        } else if (i == 3) {
            iconState = new IconState(true, getCallStrengthIcon(this.mLastWlanCrossSimLevel, false), getCallStrengthDescription(this.mLastWlanCrossSimLevel, false));
        }
        signalCallback.setCallIndicator(iconState, this.mSubscriptionInfo.getSubscriptionId());
    }

    public final void registerListener() {
        this.mMobileStatusTracker.setListening(true);
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("mobile_data"), true, this.mObserver);
        ContentResolver contentResolver = this.mContext.getContentResolver();
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("mobile_data");
        m.append(this.mSubscriptionInfo.getSubscriptionId());
        contentResolver.registerContentObserver(Settings.Global.getUriFor(m.toString()), true, this.mObserver);
        if (this.mProviderModelBehavior) {
            this.mReceiverHandler.post(this.mTryRegisterIms);
        }
    }

    @VisibleForTesting
    public void setActivity(int i) {
        boolean z;
        T t = this.mCurrentState;
        MobileState mobileState = (MobileState) t;
        boolean z2 = false;
        if (i == 3 || i == 1) {
            z = true;
        } else {
            z = false;
        }
        mobileState.activityIn = z;
        MobileState mobileState2 = (MobileState) t;
        if (i == 3 || i == 2) {
            z2 = true;
        }
        mobileState2.activityOut = z2;
        notifyListenersIfNecessary();
    }

    public final void updateConnectivity(BitSet bitSet, BitSet bitSet2) {
        int i;
        boolean z = bitSet2.get(this.mTransportType);
        ((MobileState) this.mCurrentState).isDefault = bitSet.get(this.mTransportType);
        MobileState mobileState = (MobileState) this.mCurrentState;
        if (z || !mobileState.isDefault) {
            i = 1;
        } else {
            i = 0;
        }
        mobileState.inetCondition = i;
        notifyListenersIfNecessary();
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x00d7, code lost:
        if (r5.mPhone.getCdmaEnhancedRoamingIndicatorDisplayNumber() != 1) goto L_0x00eb;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00e9, code lost:
        if (r0.getRoaming() != false) goto L_0x00eb;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00eb, code lost:
        r0 = true;
     */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00f9  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0100  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x014d  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0196  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateTelephony() {
        /*
            Method dump skipped, instructions count: 438
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.connectivity.MobileSignalController.updateTelephony():void");
    }

    @Override // com.android.systemui.statusbar.connectivity.SignalController
    public final void dump(PrintWriter printWriter) {
        super.dump(printWriter);
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("  mSubscription=");
        m.append(this.mSubscriptionInfo);
        m.append(",");
        printWriter.println(m.toString());
        printWriter.println("  mProviderModelBehavior=" + this.mProviderModelBehavior + ",");
        printWriter.println("  mInflateSignalStrengths=" + this.mInflateSignalStrengths + ",");
        printWriter.println("  isDataDisabled=" + (this.mPhone.isDataConnectionAllowed() ^ true) + ",");
        printWriter.println("  mNetworkToIconLookup=" + this.mNetworkToIconLookup + ",");
        printWriter.println("  MobileStatusHistory");
        int i = 0;
        for (int i2 = 0; i2 < 64; i2++) {
            if (this.mMobileStatusHistory[i2] != null) {
                i++;
            }
        }
        int i3 = this.mMobileStatusHistoryIndex + 64;
        while (true) {
            i3--;
            if (i3 >= (this.mMobileStatusHistoryIndex + 64) - i) {
                StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("  Previous MobileStatus(");
                m2.append((this.mMobileStatusHistoryIndex + 64) - i3);
                m2.append("): ");
                m2.append(this.mMobileStatusHistory[i3 & 63]);
                printWriter.println(m2.toString());
            } else {
                return;
            }
        }
    }

    public final void handleBroadcast(Intent intent) {
        String action = intent.getAction();
        boolean z = false;
        if (action.equals("android.telephony.action.SERVICE_PROVIDERS_UPDATED")) {
            boolean booleanExtra = intent.getBooleanExtra("android.telephony.extra.SHOW_SPN", false);
            String stringExtra = intent.getStringExtra("android.telephony.extra.SPN");
            String stringExtra2 = intent.getStringExtra("android.telephony.extra.DATA_SPN");
            boolean booleanExtra2 = intent.getBooleanExtra("android.telephony.extra.SHOW_PLMN", false);
            String stringExtra3 = intent.getStringExtra("android.telephony.extra.PLMN");
            if (SignalController.CHATTY) {
                StringBuilder sb = new StringBuilder();
                sb.append("updateNetworkName showSpn=");
                sb.append(booleanExtra);
                sb.append(" spn=");
                sb.append(stringExtra);
                sb.append(" dataSpn=");
                sb.append(stringExtra2);
                sb.append(" showPlmn=");
                sb.append(booleanExtra2);
                sb.append(" plmn=");
                ExifInterface$$ExternalSyntheticOutline2.m(sb, stringExtra3, "CarrierLabel");
            }
            StringBuilder sb2 = new StringBuilder();
            StringBuilder sb3 = new StringBuilder();
            if (booleanExtra2 && stringExtra3 != null) {
                sb2.append(stringExtra3);
                sb3.append(stringExtra3);
            }
            if (booleanExtra && stringExtra != null) {
                if (sb2.length() != 0) {
                    sb2.append(this.mNetworkNameSeparator);
                }
                sb2.append(stringExtra);
            }
            if (sb2.length() != 0) {
                ((MobileState) this.mCurrentState).networkName = sb2.toString();
            } else {
                ((MobileState) this.mCurrentState).networkName = this.mNetworkNameDefault;
            }
            if (booleanExtra && stringExtra2 != null) {
                if (sb3.length() != 0) {
                    sb3.append(this.mNetworkNameSeparator);
                }
                sb3.append(stringExtra2);
            }
            if (sb3.length() != 0) {
                ((MobileState) this.mCurrentState).networkNameData = sb3.toString();
            } else {
                ((MobileState) this.mCurrentState).networkNameData = this.mNetworkNameDefault;
            }
            notifyListenersIfNecessary();
        } else if (action.equals("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED")) {
            Objects.requireNonNull(this.mDefaults);
            int activeDataSubscriptionId = SubscriptionManager.getActiveDataSubscriptionId();
            if (SubscriptionManager.isValidSubscriptionId(activeDataSubscriptionId)) {
                MobileState mobileState = (MobileState) this.mCurrentState;
                if (activeDataSubscriptionId == this.mSubscriptionInfo.getSubscriptionId()) {
                    z = true;
                }
                mobileState.dataSim = z;
            } else {
                ((MobileState) this.mCurrentState).dataSim = true;
            }
            notifyListenersIfNecessary();
        }
    }

    @VisibleForTesting
    public void setImsType(int i) {
        this.mImsType = i;
    }
}
