package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Handler;
import android.util.ArraySet;
import android.util.Log;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline2;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline1;
import com.android.keyguard.KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0;
import com.android.settingslib.mobile.TelephonyIcons;
import com.android.systemui.ScreenDecorations$$ExternalSyntheticLambda3;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.statusbar.connectivity.IconState;
import com.android.systemui.statusbar.connectivity.MobileDataIndicators;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.connectivity.SignalCallback;
import com.android.systemui.statusbar.connectivity.WifiIndicators;
import com.android.systemui.statusbar.policy.SecurityController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.CarrierConfigTracker;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class StatusBarSignalPolicy implements SignalCallback, SecurityController.SecurityControllerCallback, TunerService.Tunable {
    public static final boolean DEBUG = Log.isLoggable("StatusBarSignalPolicy", 3);
    public boolean mActivityEnabled;
    public final CarrierConfigTracker mCarrierConfigTracker;
    public final Context mContext;
    public final FeatureFlags mFeatureFlags;
    public boolean mHideAirplane;
    public boolean mHideEthernet;
    public boolean mHideMobile;
    public boolean mHideWifi;
    public final StatusBarIconController mIconController;
    public boolean mInitialized;
    public final NetworkController mNetworkController;
    public final SecurityController mSecurityController;
    public final String mSlotAirplane;
    public final String mSlotCallStrength;
    public final String mSlotEthernet;
    public final String mSlotMobile;
    public final String mSlotNoCalling;
    public final String mSlotVpn;
    public final String mSlotWifi;
    public final TunerService mTunerService;
    public final Handler mHandler = Handler.getMain();
    public boolean mIsAirplaneMode = false;
    public boolean mIsWifiEnabled = false;
    public ArrayList<MobileIconState> mMobileStates = new ArrayList<>();
    public ArrayList<CallIndicatorIconState> mCallIndicatorStates = new ArrayList<>();
    public WifiIconState mWifiIconState = new WifiIconState();

    /* loaded from: classes.dex */
    public static class CallIndicatorIconState {
        public String callStrengthDescription;
        public boolean isNoCalling;
        public String noCallingDescription;
        public int subId;
        public int noCallingResId = 2131232227;
        public int callStrengthResId = TelephonyIcons.MOBILE_CALL_STRENGTH_ICONS[0];

        public final boolean equals(Object obj) {
            if (obj == null || CallIndicatorIconState.class != obj.getClass()) {
                return false;
            }
            CallIndicatorIconState callIndicatorIconState = (CallIndicatorIconState) obj;
            return this.isNoCalling == callIndicatorIconState.isNoCalling && this.noCallingResId == callIndicatorIconState.noCallingResId && this.callStrengthResId == callIndicatorIconState.callStrengthResId && this.subId == callIndicatorIconState.subId && this.noCallingDescription == callIndicatorIconState.noCallingDescription && this.callStrengthDescription == callIndicatorIconState.callStrengthDescription;
        }

        public final int hashCode() {
            return Objects.hash(Boolean.valueOf(this.isNoCalling), Integer.valueOf(this.noCallingResId), Integer.valueOf(this.callStrengthResId), Integer.valueOf(this.subId), this.noCallingDescription, this.callStrengthDescription);
        }

        /* renamed from: -$$Nest$smcopyStates  reason: not valid java name */
        public static ArrayList m109$$Nest$smcopyStates(ArrayList arrayList) {
            ArrayList arrayList2 = new ArrayList();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                CallIndicatorIconState callIndicatorIconState = (CallIndicatorIconState) it.next();
                CallIndicatorIconState callIndicatorIconState2 = new CallIndicatorIconState(callIndicatorIconState.subId);
                callIndicatorIconState2.isNoCalling = callIndicatorIconState.isNoCalling;
                callIndicatorIconState2.noCallingResId = callIndicatorIconState.noCallingResId;
                callIndicatorIconState2.callStrengthResId = callIndicatorIconState.callStrengthResId;
                callIndicatorIconState2.subId = callIndicatorIconState.subId;
                callIndicatorIconState2.noCallingDescription = callIndicatorIconState.noCallingDescription;
                callIndicatorIconState2.callStrengthDescription = callIndicatorIconState.callStrengthDescription;
                arrayList2.add(callIndicatorIconState2);
            }
            return arrayList2;
        }

        public CallIndicatorIconState(int i) {
            this.subId = i;
        }
    }

    /* loaded from: classes.dex */
    public static class MobileIconState extends SignalIconState {
        public boolean needsLeadingPadding;
        public boolean roaming;
        public boolean showTriangle;
        public int strengthId;
        public int subId;
        public CharSequence typeContentDescription;
        public int typeId;

        public MobileIconState(int i) {
            super(0);
            this.subId = i;
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarSignalPolicy.SignalIconState
        public final boolean equals(Object obj) {
            if (obj == null || MobileIconState.class != obj.getClass() || !super.equals(obj)) {
                return false;
            }
            MobileIconState mobileIconState = (MobileIconState) obj;
            return this.subId == mobileIconState.subId && this.strengthId == mobileIconState.strengthId && this.typeId == mobileIconState.typeId && this.showTriangle == mobileIconState.showTriangle && this.roaming == mobileIconState.roaming && this.needsLeadingPadding == mobileIconState.needsLeadingPadding && Objects.equals(this.typeContentDescription, mobileIconState.typeContentDescription);
        }

        public final void copyTo(MobileIconState mobileIconState) {
            mobileIconState.visible = this.visible;
            mobileIconState.activityIn = this.activityIn;
            mobileIconState.activityOut = this.activityOut;
            mobileIconState.slot = this.slot;
            mobileIconState.contentDescription = this.contentDescription;
            mobileIconState.subId = this.subId;
            mobileIconState.strengthId = this.strengthId;
            mobileIconState.typeId = this.typeId;
            mobileIconState.showTriangle = this.showTriangle;
            mobileIconState.roaming = this.roaming;
            mobileIconState.needsLeadingPadding = this.needsLeadingPadding;
            mobileIconState.typeContentDescription = this.typeContentDescription;
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarSignalPolicy.SignalIconState
        public final int hashCode() {
            return Objects.hash(Integer.valueOf(super.hashCode()), Integer.valueOf(this.subId), Integer.valueOf(this.strengthId), Integer.valueOf(this.typeId), Boolean.valueOf(this.showTriangle), Boolean.valueOf(this.roaming), Boolean.valueOf(this.needsLeadingPadding), this.typeContentDescription);
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("MobileIconState(subId=");
            m.append(this.subId);
            m.append(", strengthId=");
            m.append(this.strengthId);
            m.append(", showTriangle=");
            m.append(this.showTriangle);
            m.append(", roaming=");
            m.append(this.roaming);
            m.append(", typeId=");
            m.append(this.typeId);
            m.append(", visible=");
            m.append(this.visible);
            m.append(")");
            return m.toString();
        }
    }

    /* loaded from: classes.dex */
    public static abstract class SignalIconState {
        public boolean activityIn;
        public boolean activityOut;
        public String contentDescription;
        public String slot;
        public boolean visible;

        public SignalIconState() {
        }

        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            SignalIconState signalIconState = (SignalIconState) obj;
            return this.visible == signalIconState.visible && this.activityOut == signalIconState.activityOut && this.activityIn == signalIconState.activityIn && Objects.equals(this.contentDescription, signalIconState.contentDescription) && Objects.equals(this.slot, signalIconState.slot);
        }

        public int hashCode() {
            return Objects.hash(Boolean.valueOf(this.visible), Boolean.valueOf(this.activityOut), this.slot);
        }

        public SignalIconState(int i) {
        }
    }

    /* loaded from: classes.dex */
    public static class WifiIconState extends SignalIconState {
        public boolean airplaneSpacerVisible;
        public boolean noDefaultNetwork;
        public boolean noNetworksAvailable;
        public boolean noValidatedNetwork;
        public int resId;
        public boolean signalSpacerVisible;

        public WifiIconState() {
            super(0);
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarSignalPolicy.SignalIconState
        public final boolean equals(Object obj) {
            if (obj == null || WifiIconState.class != obj.getClass() || !super.equals(obj)) {
                return false;
            }
            WifiIconState wifiIconState = (WifiIconState) obj;
            return this.resId == wifiIconState.resId && this.airplaneSpacerVisible == wifiIconState.airplaneSpacerVisible && this.signalSpacerVisible == wifiIconState.signalSpacerVisible && this.noDefaultNetwork == wifiIconState.noDefaultNetwork && this.noValidatedNetwork == wifiIconState.noValidatedNetwork && this.noNetworksAvailable == wifiIconState.noNetworksAvailable;
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarSignalPolicy.SignalIconState
        public final int hashCode() {
            return Objects.hash(Integer.valueOf(super.hashCode()), Integer.valueOf(this.resId), Boolean.valueOf(this.airplaneSpacerVisible), Boolean.valueOf(this.signalSpacerVisible), Boolean.valueOf(this.noDefaultNetwork), Boolean.valueOf(this.noValidatedNetwork), Boolean.valueOf(this.noNetworksAvailable));
        }

        public final WifiIconState copy() {
            WifiIconState wifiIconState = new WifiIconState();
            wifiIconState.visible = this.visible;
            wifiIconState.activityIn = this.activityIn;
            wifiIconState.activityOut = this.activityOut;
            wifiIconState.slot = this.slot;
            wifiIconState.contentDescription = this.contentDescription;
            wifiIconState.resId = this.resId;
            wifiIconState.airplaneSpacerVisible = this.airplaneSpacerVisible;
            wifiIconState.signalSpacerVisible = this.signalSpacerVisible;
            wifiIconState.noDefaultNetwork = this.noDefaultNetwork;
            wifiIconState.noValidatedNetwork = this.noValidatedNetwork;
            wifiIconState.noNetworksAvailable = this.noNetworksAvailable;
            return wifiIconState;
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("WifiIconState(resId=");
            m.append(this.resId);
            m.append(", visible=");
            m.append(this.visible);
            m.append(")");
            return m.toString();
        }
    }

    @Override // com.android.systemui.statusbar.connectivity.SignalCallback
    public final void setMobileDataEnabled(boolean z) {
    }

    @Override // com.android.systemui.statusbar.connectivity.SignalCallback
    public final void setNoSims(boolean z, boolean z2) {
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController.SecurityControllerCallback
    public final void onStateChanged() {
        this.mHandler.post(new ScreenDecorations$$ExternalSyntheticLambda3(this, 3));
    }

    @Override // com.android.systemui.tuner.TunerService.Tunable
    public final void onTuningChanged(String str, String str2) {
        if ("icon_blacklist".equals(str)) {
            ArraySet<String> iconHideList = StatusBarIconController.getIconHideList(this.mContext, str2);
            boolean contains = iconHideList.contains(this.mSlotAirplane);
            boolean contains2 = iconHideList.contains(this.mSlotMobile);
            boolean contains3 = iconHideList.contains(this.mSlotWifi);
            boolean contains4 = iconHideList.contains(this.mSlotEthernet);
            if (contains != this.mHideAirplane || contains2 != this.mHideMobile || contains4 != this.mHideEthernet || contains3 != this.mHideWifi) {
                this.mHideAirplane = contains;
                this.mHideMobile = contains2;
                this.mHideEthernet = contains4;
                this.mHideWifi = contains3;
                this.mNetworkController.removeCallback(this);
                this.mNetworkController.addCallback(this);
            }
        }
    }

    @Override // com.android.systemui.statusbar.connectivity.SignalCallback
    public final void setCallIndicator(IconState iconState, int i) {
        CallIndicatorIconState callIndicatorIconState;
        if (DEBUG) {
            Log.d("StatusBarSignalPolicy", "setCallIndicator: statusIcon = " + iconState + ",subId = " + i);
        }
        Iterator<CallIndicatorIconState> it = this.mCallIndicatorStates.iterator();
        while (true) {
            if (!it.hasNext()) {
                KeyguardUpdateMonitor$$ExternalSyntheticOutline1.m("Unexpected subscription ", i, "StatusBarSignalPolicy");
                callIndicatorIconState = null;
                break;
            }
            callIndicatorIconState = it.next();
            if (callIndicatorIconState.subId == i) {
                break;
            }
        }
        if (callIndicatorIconState != null) {
            int i2 = iconState.icon;
            if (i2 == 2131232227) {
                callIndicatorIconState.isNoCalling = iconState.visible;
                callIndicatorIconState.noCallingDescription = iconState.contentDescription;
            } else {
                callIndicatorIconState.callStrengthResId = i2;
                callIndicatorIconState.callStrengthDescription = iconState.contentDescription;
            }
            if (this.mCarrierConfigTracker.getCallStrengthConfig(i)) {
                this.mIconController.setCallStrengthIcons(this.mSlotCallStrength, CallIndicatorIconState.m109$$Nest$smcopyStates(this.mCallIndicatorStates));
            } else {
                this.mIconController.removeIcon(this.mSlotCallStrength, i);
            }
            this.mIconController.setNoCallingIcons(this.mSlotNoCalling, CallIndicatorIconState.m109$$Nest$smcopyStates(this.mCallIndicatorStates));
        }
    }

    @Override // com.android.systemui.statusbar.connectivity.SignalCallback
    public final void setConnectivityStatus(boolean z, boolean z2, boolean z3) {
        if (this.mFeatureFlags.isEnabled(Flags.COMBINED_STATUS_BAR_SIGNAL_ICONS)) {
            if (DEBUG) {
                StringBuilder sb = new StringBuilder();
                sb.append("setConnectivityStatus: noDefaultNetwork = ");
                sb.append(z);
                sb.append(",noValidatedNetwork = ");
                sb.append(z2);
                sb.append(",noNetworksAvailable = ");
                KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0.m(sb, z3, "StatusBarSignalPolicy");
            }
            WifiIconState copy = this.mWifiIconState.copy();
            copy.noDefaultNetwork = z;
            copy.noValidatedNetwork = z2;
            copy.noNetworksAvailable = z3;
            copy.slot = this.mSlotWifi;
            boolean z4 = this.mIsAirplaneMode;
            copy.airplaneSpacerVisible = z4;
            if (z && z3 && !z4) {
                copy.visible = true;
                copy.resId = 2131232229;
            } else if (!z || z3 || (z4 && (!z4 || !this.mIsWifiEnabled))) {
                copy.visible = false;
                copy.resId = 0;
            } else {
                copy.visible = true;
                copy.resId = 2131232228;
            }
            updateWifiIconWithState(copy);
            this.mWifiIconState = copy;
        }
    }

    @Override // com.android.systemui.statusbar.connectivity.SignalCallback
    public final void setEthernetIndicators(IconState iconState) {
        boolean z = iconState.visible;
        int i = iconState.icon;
        String str = iconState.contentDescription;
        if (i > 0) {
            this.mIconController.setIcon(this.mSlotEthernet, i, str);
            this.mIconController.setIconVisibility(this.mSlotEthernet, true);
            return;
        }
        this.mIconController.setIconVisibility(this.mSlotEthernet, false);
    }

    @Override // com.android.systemui.statusbar.connectivity.SignalCallback
    public final void setIsAirplaneMode(IconState iconState) {
        boolean z;
        String str;
        if (DEBUG) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("setIsAirplaneMode: icon = ");
            if (iconState == null) {
                str = "";
            } else {
                str = iconState.toString();
            }
            ExifInterface$$ExternalSyntheticOutline2.m(m, str, "StatusBarSignalPolicy");
        }
        if (!iconState.visible || this.mHideAirplane) {
            z = false;
        } else {
            z = true;
        }
        this.mIsAirplaneMode = z;
        int i = iconState.icon;
        String str2 = iconState.contentDescription;
        if (!z || i <= 0) {
            this.mIconController.setIconVisibility(this.mSlotAirplane, false);
            return;
        }
        this.mIconController.setIcon(this.mSlotAirplane, i, str2);
        this.mIconController.setIconVisibility(this.mSlotAirplane, true);
    }

    @Override // com.android.systemui.statusbar.connectivity.SignalCallback
    public final void setMobileDataIndicators(MobileDataIndicators mobileDataIndicators) {
        MobileIconState mobileIconState;
        MobileIconState mobileIconState2;
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        String str;
        if (DEBUG) {
            Log.d("StatusBarSignalPolicy", "setMobileDataIndicators: " + mobileDataIndicators);
        }
        int i = mobileDataIndicators.subId;
        Iterator<MobileIconState> it = this.mMobileStates.iterator();
        while (true) {
            mobileIconState = null;
            if (!it.hasNext()) {
                KeyguardUpdateMonitor$$ExternalSyntheticOutline1.m("Unexpected subscription ", i, "StatusBarSignalPolicy");
                mobileIconState2 = null;
                break;
            }
            mobileIconState2 = it.next();
            if (mobileIconState2.subId == i) {
                break;
            }
        }
        if (mobileIconState2 != null) {
            int i2 = mobileDataIndicators.statusType;
            int i3 = mobileIconState2.typeId;
            boolean z5 = true;
            if (i2 == i3 || !(i2 == 0 || i3 == 0)) {
                z = false;
            } else {
                z = true;
            }
            IconState iconState = mobileDataIndicators.statusIcon;
            if (!iconState.visible || this.mHideMobile) {
                z2 = false;
            } else {
                z2 = true;
            }
            mobileIconState2.visible = z2;
            mobileIconState2.strengthId = iconState.icon;
            mobileIconState2.typeId = i2;
            mobileIconState2.contentDescription = iconState.contentDescription;
            mobileIconState2.typeContentDescription = mobileDataIndicators.typeContentDescription;
            mobileIconState2.showTriangle = mobileDataIndicators.showTriangle;
            mobileIconState2.roaming = mobileDataIndicators.roaming;
            if (!mobileDataIndicators.activityIn || !this.mActivityEnabled) {
                z3 = false;
            } else {
                z3 = true;
            }
            mobileIconState2.activityIn = z3;
            if (!mobileDataIndicators.activityOut || !this.mActivityEnabled) {
                z4 = false;
            } else {
                z4 = true;
            }
            mobileIconState2.activityOut = z4;
            if (DEBUG) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("MobileIconStates: ");
                ArrayList<MobileIconState> arrayList = this.mMobileStates;
                if (arrayList == null) {
                    str = "";
                } else {
                    str = arrayList.toString();
                }
                ExifInterface$$ExternalSyntheticOutline2.m(m, str, "StatusBarSignalPolicy");
            }
            StatusBarIconController statusBarIconController = this.mIconController;
            String str2 = this.mSlotMobile;
            ArrayList<MobileIconState> arrayList2 = this.mMobileStates;
            ArrayList arrayList3 = new ArrayList();
            Iterator<MobileIconState> it2 = arrayList2.iterator();
            while (it2.hasNext()) {
                MobileIconState next = it2.next();
                MobileIconState mobileIconState3 = new MobileIconState(next.subId);
                next.copyTo(mobileIconState3);
                arrayList3.add(mobileIconState3);
            }
            statusBarIconController.setMobileIcons(str2, arrayList3);
            if (z) {
                WifiIconState copy = this.mWifiIconState.copy();
                if (this.mMobileStates.size() > 0) {
                    mobileIconState = this.mMobileStates.get(0);
                }
                if (mobileIconState == null || mobileIconState.typeId == 0) {
                    z5 = false;
                }
                copy.signalSpacerVisible = z5;
                if (!copy.equals(this.mWifiIconState)) {
                    updateWifiIconWithState(copy);
                    this.mWifiIconState = copy;
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0049 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x004a  */
    @Override // com.android.systemui.statusbar.connectivity.SignalCallback
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void setSubs(java.util.List<android.telephony.SubscriptionInfo> r10) {
        /*
            r9 = this;
            boolean r0 = com.android.systemui.statusbar.phone.StatusBarSignalPolicy.DEBUG
            if (r0 == 0) goto L_0x0019
            java.lang.String r0 = "setSubs: "
            java.lang.StringBuilder r0 = android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1.m(r0)
            if (r10 != 0) goto L_0x0010
            java.lang.String r1 = ""
            goto L_0x0014
        L_0x0010:
            java.lang.String r1 = r10.toString()
        L_0x0014:
            java.lang.String r2 = "StatusBarSignalPolicy"
            androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline2.m(r0, r1, r2)
        L_0x0019:
            int r0 = r10.size()
            java.util.ArrayList<com.android.systemui.statusbar.phone.StatusBarSignalPolicy$MobileIconState> r1 = r9.mMobileStates
            int r1 = r1.size()
            r2 = 0
            r3 = 1
            if (r0 == r1) goto L_0x0028
            goto L_0x0041
        L_0x0028:
            r1 = r2
        L_0x0029:
            if (r1 >= r0) goto L_0x0046
            java.util.ArrayList<com.android.systemui.statusbar.phone.StatusBarSignalPolicy$MobileIconState> r4 = r9.mMobileStates
            java.lang.Object r4 = r4.get(r1)
            com.android.systemui.statusbar.phone.StatusBarSignalPolicy$MobileIconState r4 = (com.android.systemui.statusbar.phone.StatusBarSignalPolicy.MobileIconState) r4
            int r4 = r4.subId
            java.lang.Object r5 = r10.get(r1)
            android.telephony.SubscriptionInfo r5 = (android.telephony.SubscriptionInfo) r5
            int r5 = r5.getSubscriptionId()
            if (r4 == r5) goto L_0x0043
        L_0x0041:
            r0 = r2
            goto L_0x0047
        L_0x0043:
            int r1 = r1 + 1
            goto L_0x0029
        L_0x0046:
            r0 = r3
        L_0x0047:
            if (r0 == 0) goto L_0x004a
            return
        L_0x004a:
            com.android.systemui.statusbar.phone.StatusBarIconController r0 = r9.mIconController
            java.lang.String r1 = r9.mSlotMobile
            r0.removeAllIconsForSlot(r1)
            com.android.systemui.statusbar.phone.StatusBarIconController r0 = r9.mIconController
            java.lang.String r1 = r9.mSlotNoCalling
            r0.removeAllIconsForSlot(r1)
            com.android.systemui.statusbar.phone.StatusBarIconController r0 = r9.mIconController
            java.lang.String r1 = r9.mSlotCallStrength
            r0.removeAllIconsForSlot(r1)
            java.util.ArrayList<com.android.systemui.statusbar.phone.StatusBarSignalPolicy$MobileIconState> r0 = r9.mMobileStates
            r0.clear()
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            java.util.ArrayList<com.android.systemui.statusbar.phone.StatusBarSignalPolicy$CallIndicatorIconState> r1 = r9.mCallIndicatorStates
            r0.addAll(r1)
            java.util.ArrayList<com.android.systemui.statusbar.phone.StatusBarSignalPolicy$CallIndicatorIconState> r1 = r9.mCallIndicatorStates
            r1.clear()
            int r1 = r10.size()
            r4 = r2
        L_0x0078:
            if (r4 >= r1) goto L_0x00cd
            java.util.ArrayList<com.android.systemui.statusbar.phone.StatusBarSignalPolicy$MobileIconState> r5 = r9.mMobileStates
            com.android.systemui.statusbar.phone.StatusBarSignalPolicy$MobileIconState r6 = new com.android.systemui.statusbar.phone.StatusBarSignalPolicy$MobileIconState
            java.lang.Object r7 = r10.get(r4)
            android.telephony.SubscriptionInfo r7 = (android.telephony.SubscriptionInfo) r7
            int r7 = r7.getSubscriptionId()
            r6.<init>(r7)
            r5.add(r6)
            java.util.Iterator r5 = r0.iterator()
        L_0x0092:
            boolean r6 = r5.hasNext()
            if (r6 == 0) goto L_0x00b3
            java.lang.Object r6 = r5.next()
            com.android.systemui.statusbar.phone.StatusBarSignalPolicy$CallIndicatorIconState r6 = (com.android.systemui.statusbar.phone.StatusBarSignalPolicy.CallIndicatorIconState) r6
            int r7 = r6.subId
            java.lang.Object r8 = r10.get(r4)
            android.telephony.SubscriptionInfo r8 = (android.telephony.SubscriptionInfo) r8
            int r8 = r8.getSubscriptionId()
            if (r7 != r8) goto L_0x0092
            java.util.ArrayList<com.android.systemui.statusbar.phone.StatusBarSignalPolicy$CallIndicatorIconState> r5 = r9.mCallIndicatorStates
            r5.add(r6)
            r5 = r2
            goto L_0x00b4
        L_0x00b3:
            r5 = r3
        L_0x00b4:
            if (r5 == 0) goto L_0x00ca
            java.util.ArrayList<com.android.systemui.statusbar.phone.StatusBarSignalPolicy$CallIndicatorIconState> r5 = r9.mCallIndicatorStates
            com.android.systemui.statusbar.phone.StatusBarSignalPolicy$CallIndicatorIconState r6 = new com.android.systemui.statusbar.phone.StatusBarSignalPolicy$CallIndicatorIconState
            java.lang.Object r7 = r10.get(r4)
            android.telephony.SubscriptionInfo r7 = (android.telephony.SubscriptionInfo) r7
            int r7 = r7.getSubscriptionId()
            r6.<init>(r7)
            r5.add(r6)
        L_0x00ca:
            int r4 = r4 + 1
            goto L_0x0078
        L_0x00cd:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.StatusBarSignalPolicy.setSubs(java.util.List):void");
    }

    @Override // com.android.systemui.statusbar.connectivity.SignalCallback
    public final void setWifiIndicators(WifiIndicators wifiIndicators) {
        boolean z;
        boolean z2;
        boolean z3;
        MobileIconState mobileIconState;
        boolean z4;
        if (DEBUG) {
            Log.d("StatusBarSignalPolicy", "setWifiIndicators: " + wifiIndicators);
        }
        boolean z5 = true;
        if (!wifiIndicators.statusIcon.visible || this.mHideWifi) {
            z = false;
        } else {
            z = true;
        }
        if (!wifiIndicators.activityIn || !this.mActivityEnabled || !z) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (!wifiIndicators.activityOut || !this.mActivityEnabled || !z) {
            z3 = false;
        } else {
            z3 = true;
        }
        this.mIsWifiEnabled = wifiIndicators.enabled;
        WifiIconState copy = this.mWifiIconState.copy();
        WifiIconState wifiIconState = this.mWifiIconState;
        boolean z6 = wifiIconState.noDefaultNetwork;
        if (z6 && wifiIconState.noNetworksAvailable && !this.mIsAirplaneMode) {
            copy.visible = true;
            copy.resId = 2131232229;
        } else if (!z6 || wifiIconState.noNetworksAvailable || ((z4 = this.mIsAirplaneMode) && (!z4 || !this.mIsWifiEnabled))) {
            copy.visible = z;
            IconState iconState = wifiIndicators.statusIcon;
            copy.resId = iconState.icon;
            copy.activityIn = z2;
            copy.activityOut = z3;
            copy.contentDescription = iconState.contentDescription;
            if (this.mMobileStates.size() > 0) {
                mobileIconState = this.mMobileStates.get(0);
            } else {
                mobileIconState = null;
            }
            if (mobileIconState == null || mobileIconState.typeId == 0) {
                z5 = false;
            }
            copy.signalSpacerVisible = z5;
        } else {
            copy.visible = true;
            copy.resId = 2131232228;
        }
        copy.slot = this.mSlotWifi;
        copy.airplaneSpacerVisible = this.mIsAirplaneMode;
        updateWifiIconWithState(copy);
        this.mWifiIconState = copy;
    }

    public final void updateWifiIconWithState(WifiIconState wifiIconState) {
        String str;
        if (DEBUG) {
            if (("WifiIconState: " + wifiIconState) == null) {
                str = "";
            } else {
                str = wifiIconState.toString();
            }
            Log.d("StatusBarSignalPolicy", str);
        }
        if (!wifiIconState.visible || wifiIconState.resId <= 0) {
            this.mIconController.setIconVisibility(this.mSlotWifi, false);
            return;
        }
        this.mIconController.setSignalIcon(this.mSlotWifi, wifiIconState);
        this.mIconController.setIconVisibility(this.mSlotWifi, true);
    }

    public StatusBarSignalPolicy(Context context, StatusBarIconController statusBarIconController, CarrierConfigTracker carrierConfigTracker, NetworkController networkController, SecurityController securityController, TunerService tunerService, FeatureFlags featureFlags) {
        this.mContext = context;
        this.mIconController = statusBarIconController;
        this.mCarrierConfigTracker = carrierConfigTracker;
        this.mNetworkController = networkController;
        this.mSecurityController = securityController;
        this.mTunerService = tunerService;
        this.mFeatureFlags = featureFlags;
        this.mSlotAirplane = context.getString(17041533);
        this.mSlotMobile = context.getString(17041551);
        this.mSlotWifi = context.getString(17041567);
        this.mSlotEthernet = context.getString(17041544);
        this.mSlotVpn = context.getString(17041566);
        this.mSlotNoCalling = context.getString(17041554);
        this.mSlotCallStrength = context.getString(17041537);
        this.mActivityEnabled = context.getResources().getBoolean(2131034159);
    }
}
