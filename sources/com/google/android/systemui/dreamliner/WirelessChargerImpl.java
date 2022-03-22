package com.google.android.systemui.dreamliner;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Bundle;
import android.os.Handler;
import android.os.HwBinder;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.os.IHwInterface;
import android.os.Looper;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.core.graphics.drawable.IconCompat;
import androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline0;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline3;
import com.android.systemui.Dependency;
import com.android.systemui.ImageWallpaper$GLEngine$$ExternalSyntheticLambda3;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.volume.CaptionsToggleImageButton$$ExternalSyntheticLambda0;
import com.google.android.systemui.dreamliner.DockObserver;
import com.google.android.systemui.dreamliner.WirelessCharger;
import com.google.android.systemui.elmyra.gates.KeyguardVisibility;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import vendor.google.wireless_charger.V1_0.DockInfo;
import vendor.google.wireless_charger.V1_0.KeyExchangeResponse;
import vendor.google.wireless_charger.V1_1.IWirelessChargerInfoCallback$Stub;
import vendor.google.wireless_charger.V1_3.FanDetailedInfo;
import vendor.google.wireless_charger.V1_3.FanInfo;
import vendor.google.wireless_charger.V1_3.IWirelessCharger;
/* loaded from: classes.dex */
public class WirelessChargerImpl extends WirelessCharger implements IHwBinder.DeathRecipient {
    public static final /* synthetic */ int $r8$clinit = 0;
    public static final long MAX_POLLING_TIMEOUT_NS = TimeUnit.SECONDS.toNanos(5);
    public IsDockPresentCallbackWrapper mCallback;
    public long mPollingStartedTimeNs;
    public IWirelessCharger mWirelessCharger;
    public final Handler mHandler = new Handler(Looper.getMainLooper());
    public final AnonymousClass1 mRunnable = new Runnable() { // from class: com.google.android.systemui.dreamliner.WirelessChargerImpl.1
        @Override // java.lang.Runnable
        public final void run() {
            WirelessChargerImpl wirelessChargerImpl = WirelessChargerImpl.this;
            int i = WirelessChargerImpl.$r8$clinit;
            Objects.requireNonNull(wirelessChargerImpl);
            wirelessChargerImpl.initHALInterface();
            IWirelessCharger iWirelessCharger = wirelessChargerImpl.mWirelessCharger;
            if (iWirelessCharger != null) {
                try {
                    iWirelessCharger.isDockPresent(wirelessChargerImpl);
                } catch (Exception e) {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("isDockPresent fail: ");
                    m.append(e.getMessage());
                    Log.i("Dreamliner-WLC_HAL", m.toString());
                }
            }
        }
    };

    /* loaded from: classes.dex */
    public final class ChallengeCallbackWrapper {
        public final WirelessCharger.ChallengeCallback mCallback;

        public final void onValues(byte b, ArrayList<Byte> arrayList) {
            WirelessCharger.ChallengeCallback challengeCallback = this.mCallback;
            int intValue = Byte.valueOf(b).intValue();
            DockObserver.ChallengeCallback challengeCallback2 = (DockObserver.ChallengeCallback) challengeCallback;
            Objects.requireNonNull(challengeCallback2);
            Log.d("DLObserver", "C() Result: " + intValue);
            Bundle bundle = null;
            if (intValue == 0) {
                Log.d("DLObserver", "C() response: " + arrayList);
                ResultReceiver resultReceiver = challengeCallback2.mResultReceiver;
                DockObserver dockObserver = DockObserver.this;
                String str = DockObserver.ACTION_START_DREAMLINER_CONTROL_SERVICE;
                Objects.requireNonNull(dockObserver);
                if (arrayList != null && !arrayList.isEmpty()) {
                    byte[] convertArrayListToPrimitiveArray = DockObserver.convertArrayListToPrimitiveArray(arrayList);
                    bundle = new Bundle();
                    bundle.putByteArray("challenge_response", convertArrayListToPrimitiveArray);
                }
                resultReceiver.send(0, bundle);
                return;
            }
            challengeCallback2.mResultReceiver.send(1, null);
        }

        public ChallengeCallbackWrapper(WirelessCharger.ChallengeCallback challengeCallback) {
            this.mCallback = challengeCallback;
        }
    }

    /* loaded from: classes.dex */
    public static final class GetFanInformationCallbackWrapper {
        public final WirelessCharger.GetFanInformationCallback mCallback;
        public final byte mFanId;

        public final void onValues(byte b, FanDetailedInfo fanDetailedInfo) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("command=0, result=");
            m.append(Byte.valueOf(b).intValue());
            m.append(", i=");
            m.append((int) this.mFanId);
            m.append(", m=");
            m.append((int) fanDetailedInfo.fanMode);
            m.append(", cr=");
            m.append((int) fanDetailedInfo.currentRpm);
            m.append(", mir=");
            m.append((int) fanDetailedInfo.minimumRpm);
            m.append(", mxr=");
            m.append((int) fanDetailedInfo.maximumRpm);
            m.append(", t=");
            m.append((int) fanDetailedInfo.type);
            m.append(", c=");
            KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(m, fanDetailedInfo.count, "Dreamliner-WLC_HAL");
            WirelessCharger.GetFanInformationCallback getFanInformationCallback = this.mCallback;
            int intValue = Byte.valueOf(b).intValue();
            byte b2 = this.mFanId;
            int i = WirelessChargerImpl.$r8$clinit;
            Bundle bundle = new Bundle();
            bundle.putByte("fan_id", b2);
            bundle.putByte("fan_mode", fanDetailedInfo.fanMode);
            bundle.putInt("fan_current_rpm", fanDetailedInfo.currentRpm);
            bundle.putInt("fan_min_rpm", fanDetailedInfo.minimumRpm);
            bundle.putInt("fan_max_rpm", fanDetailedInfo.maximumRpm);
            bundle.putByte("fan_type", fanDetailedInfo.type);
            bundle.putByte("fan_count", fanDetailedInfo.count);
            DockObserver.GetFanInformationCallback getFanInformationCallback2 = (DockObserver.GetFanInformationCallback) getFanInformationCallback;
            Objects.requireNonNull(getFanInformationCallback2);
            StringBuilder sb = new StringBuilder();
            sb.append("Callback of command=0, result=");
            sb.append(intValue);
            sb.append(", i=");
            KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(sb, getFanInformationCallback2.mFanId, "DLObserver");
            if (intValue == 0) {
                StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Callback of command=0, i=");
                m2.append(bundle.getByte("fan_id", (byte) -1));
                m2.append(", m=");
                m2.append(bundle.getByte("fan_mode", (byte) -1));
                m2.append(", cr=");
                m2.append(bundle.getInt("fan_current_rpm", -1));
                m2.append(", mir=");
                m2.append(bundle.getInt("fan_min_rpm", -1));
                m2.append(", mxr=");
                m2.append(bundle.getInt("fan_max_rpm", -1));
                m2.append(", t=");
                m2.append(bundle.getByte("fan_type", (byte) -1));
                m2.append(", c=");
                m2.append(bundle.getByte("fan_count", (byte) -1));
                Log.d("DLObserver", m2.toString());
                getFanInformationCallback2.mResultReceiver.send(0, bundle);
                return;
            }
            getFanInformationCallback2.mResultReceiver.send(1, null);
        }

        public GetFanInformationCallbackWrapper(byte b, WirelessCharger.GetFanInformationCallback getFanInformationCallback) {
            this.mFanId = b;
            this.mCallback = getFanInformationCallback;
        }
    }

    /* loaded from: classes.dex */
    public static final class GetFanSimpleInformationCallbackWrapper {
        public final WirelessCharger.GetFanSimpleInformationCallback mCallback;
        public final byte mFanId;

        public final void onValues(byte b, FanInfo fanInfo) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("command=3, result=");
            m.append(Byte.valueOf(b).intValue());
            m.append(", i=");
            m.append((int) this.mFanId);
            m.append(", m=");
            m.append((int) fanInfo.fanMode);
            m.append(", cr=");
            KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(m, fanInfo.currentRpm, "Dreamliner-WLC_HAL");
            WirelessCharger.GetFanSimpleInformationCallback getFanSimpleInformationCallback = this.mCallback;
            int intValue = Byte.valueOf(b).intValue();
            byte b2 = this.mFanId;
            int i = WirelessChargerImpl.$r8$clinit;
            Bundle bundle = new Bundle();
            bundle.putByte("fan_id", b2);
            bundle.putByte("fan_mode", fanInfo.fanMode);
            bundle.putInt("fan_current_rpm", fanInfo.currentRpm);
            DockObserver.GetFanSimpleInformationCallback getFanSimpleInformationCallback2 = (DockObserver.GetFanSimpleInformationCallback) getFanSimpleInformationCallback;
            Objects.requireNonNull(getFanSimpleInformationCallback2);
            StringBuilder sb = new StringBuilder();
            sb.append("Callback of command=3, result=");
            sb.append(intValue);
            sb.append(", i=");
            KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(sb, getFanSimpleInformationCallback2.mFanId, "DLObserver");
            if (intValue == 0) {
                StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Callback of command=3, i=");
                m2.append(bundle.getByte("fan_id", (byte) -1));
                m2.append(", m=");
                m2.append(bundle.getByte("fan_mode", (byte) -1));
                m2.append(", cr=");
                m2.append(bundle.getInt("fan_current_rpm", -1));
                Log.d("DLObserver", m2.toString());
                getFanSimpleInformationCallback2.mResultReceiver.send(0, bundle);
                return;
            }
            getFanSimpleInformationCallback2.mResultReceiver.send(1, null);
        }

        public GetFanSimpleInformationCallbackWrapper(byte b, WirelessCharger.GetFanSimpleInformationCallback getFanSimpleInformationCallback) {
            this.mFanId = b;
            this.mCallback = getFanSimpleInformationCallback;
        }
    }

    /* loaded from: classes.dex */
    public static final class GetFeaturesCallbackWrapper {
        public final WirelessCharger.GetFeaturesCallback mCallback;

        public final void onValues(byte b, long j) {
            WirelessCharger.GetFeaturesCallback getFeaturesCallback = this.mCallback;
            int intValue = Byte.valueOf(b).intValue();
            DockObserver.GetFeaturesCallback getFeaturesCallback2 = (DockObserver.GetFeaturesCallback) getFeaturesCallback;
            Objects.requireNonNull(getFeaturesCallback2);
            Log.d("DLObserver", "GF() result: " + intValue);
            if (intValue == 0) {
                Log.d("DLObserver", "GF() response: f=" + j);
                ResultReceiver resultReceiver = getFeaturesCallback2.mResultReceiver;
                DockObserver dockObserver = DockObserver.this;
                String str = DockObserver.ACTION_START_DREAMLINER_CONTROL_SERVICE;
                Objects.requireNonNull(dockObserver);
                Bundle bundle = new Bundle();
                bundle.putLong("charger_feature", j);
                resultReceiver.send(0, bundle);
                return;
            }
            getFeaturesCallback2.mResultReceiver.send(1, null);
        }

        public GetFeaturesCallbackWrapper(WirelessCharger.GetFeaturesCallback getFeaturesCallback) {
            this.mCallback = getFeaturesCallback;
        }
    }

    /* loaded from: classes.dex */
    public final class GetInformationCallbackWrapper {
        public final WirelessCharger.GetInformationCallback mCallback;

        public final void onValues(byte b, DockInfo dockInfo) {
            WirelessCharger.GetInformationCallback getInformationCallback = this.mCallback;
            int intValue = Byte.valueOf(b).intValue();
            String str = dockInfo.manufacturer;
            String str2 = dockInfo.model;
            String str3 = dockInfo.serial;
            int intValue2 = Byte.valueOf(dockInfo.type).intValue();
            DockObserver.GetInformationCallback getInformationCallback2 = (DockObserver.GetInformationCallback) getInformationCallback;
            Objects.requireNonNull(getInformationCallback2);
            Log.d("DLObserver", "GI() Result: " + intValue);
            if (intValue == 0) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("GI() response: di=");
                m.append(str + ", " + str2 + ", " + str3 + ", " + intValue2);
                Log.d("DLObserver", m.toString());
                ResultReceiver resultReceiver = getInformationCallback2.mResultReceiver;
                Bundle bundle = new Bundle();
                bundle.putString("manufacturer", str);
                bundle.putString("model", str2);
                bundle.putString("serialNumber", str3);
                bundle.putInt("accessoryType", intValue2);
                resultReceiver.send(0, bundle);
            } else if (intValue != 1) {
                getInformationCallback2.mResultReceiver.send(1, null);
            }
        }

        public GetInformationCallbackWrapper(WirelessCharger.GetInformationCallback getInformationCallback) {
            this.mCallback = getInformationCallback;
        }
    }

    /* loaded from: classes.dex */
    public static final class GetWpcAuthCertificateCallbackWrapper {
        public final WirelessCharger.GetWpcAuthCertificateCallback mCallback;

        public final void onValues(byte b, ArrayList<Byte> arrayList) {
            WirelessCharger.GetWpcAuthCertificateCallback getWpcAuthCertificateCallback = this.mCallback;
            int intValue = Byte.valueOf(b).intValue();
            DockObserver.GetWpcAuthCertificateCallback getWpcAuthCertificateCallback2 = (DockObserver.GetWpcAuthCertificateCallback) getWpcAuthCertificateCallback;
            Objects.requireNonNull(getWpcAuthCertificateCallback2);
            Log.d("DLObserver", "GWAC() result: " + intValue);
            Bundle bundle = null;
            if (intValue == 0) {
                Log.d("DLObserver", "GWAC() response: c=" + arrayList);
                ResultReceiver resultReceiver = getWpcAuthCertificateCallback2.mResultReceiver;
                DockObserver dockObserver = DockObserver.this;
                String str = DockObserver.ACTION_START_DREAMLINER_CONTROL_SERVICE;
                Objects.requireNonNull(dockObserver);
                if (arrayList != null && !arrayList.isEmpty()) {
                    byte[] convertArrayListToPrimitiveArray = DockObserver.convertArrayListToPrimitiveArray(arrayList);
                    bundle = new Bundle();
                    bundle.putByteArray("wpc_cert", convertArrayListToPrimitiveArray);
                }
                resultReceiver.send(0, bundle);
                return;
            }
            getWpcAuthCertificateCallback2.mResultReceiver.send(1, null);
        }

        public GetWpcAuthCertificateCallbackWrapper(WirelessCharger.GetWpcAuthCertificateCallback getWpcAuthCertificateCallback) {
            this.mCallback = getWpcAuthCertificateCallback;
        }
    }

    /* loaded from: classes.dex */
    public static final class GetWpcAuthChallengeResponseCallbackWrapper {
        public final WirelessCharger.GetWpcAuthChallengeResponseCallback mCallback;

        public final void onValues(byte b, byte b2, byte b3, byte b4, ArrayList<Byte> arrayList, ArrayList<Byte> arrayList2) {
            WirelessCharger.GetWpcAuthChallengeResponseCallback getWpcAuthChallengeResponseCallback = this.mCallback;
            int intValue = Byte.valueOf(b).intValue();
            DockObserver.GetWpcAuthChallengeResponseCallback getWpcAuthChallengeResponseCallback2 = (DockObserver.GetWpcAuthChallengeResponseCallback) getWpcAuthChallengeResponseCallback;
            Objects.requireNonNull(getWpcAuthChallengeResponseCallback2);
            Log.d("DLObserver", "GWACR() result: " + intValue);
            if (intValue == 0) {
                StringBuilder m = GridLayoutManager$$ExternalSyntheticOutline0.m("GWACR() response: mpv=", b2, ", pm=", b3, ", chl=");
                m.append((int) b4);
                m.append(", rv=");
                m.append(arrayList);
                m.append(", sv=");
                m.append(arrayList2);
                Log.d("DLObserver", m.toString());
                ResultReceiver resultReceiver = getWpcAuthChallengeResponseCallback2.mResultReceiver;
                DockObserver dockObserver = DockObserver.this;
                String str = DockObserver.ACTION_START_DREAMLINER_CONTROL_SERVICE;
                Objects.requireNonNull(dockObserver);
                Bundle bundle = new Bundle();
                bundle.putByte("max_protocol_ver", b2);
                bundle.putByte("slot_populated_mask", b3);
                bundle.putByte("cert_lsb", b4);
                bundle.putByteArray("signature_r", DockObserver.convertArrayListToPrimitiveArray(arrayList));
                bundle.putByteArray("signature_s", DockObserver.convertArrayListToPrimitiveArray(arrayList2));
                resultReceiver.send(0, bundle);
                return;
            }
            getWpcAuthChallengeResponseCallback2.mResultReceiver.send(1, null);
        }

        public GetWpcAuthChallengeResponseCallbackWrapper(WirelessCharger.GetWpcAuthChallengeResponseCallback getWpcAuthChallengeResponseCallback) {
            this.mCallback = getWpcAuthChallengeResponseCallback;
        }
    }

    /* loaded from: classes.dex */
    public static final class GetWpcAuthDigestsCallbackWrapper {
        public final WirelessCharger.GetWpcAuthDigestsCallback mCallback;

        public final void onValues(byte b, byte b2, byte b3, ArrayList<byte[]> arrayList) {
            WirelessCharger.GetWpcAuthDigestsCallback getWpcAuthDigestsCallback = this.mCallback;
            int intValue = Byte.valueOf(b).intValue();
            DockObserver.GetWpcAuthDigestsCallback getWpcAuthDigestsCallback2 = (DockObserver.GetWpcAuthDigestsCallback) getWpcAuthDigestsCallback;
            Objects.requireNonNull(getWpcAuthDigestsCallback2);
            Log.d("DLObserver", "GWAD() result: " + intValue);
            if (intValue == 0) {
                StringBuilder m = GridLayoutManager$$ExternalSyntheticOutline0.m("GWAD() response: pm=", b2, ", rm=", b3, ", d=");
                m.append(arrayList);
                Log.d("DLObserver", m.toString());
                ResultReceiver resultReceiver = getWpcAuthDigestsCallback2.mResultReceiver;
                DockObserver dockObserver = DockObserver.this;
                String str = DockObserver.ACTION_START_DREAMLINER_CONTROL_SERVICE;
                Objects.requireNonNull(dockObserver);
                Bundle bundle = new Bundle();
                bundle.putByte("slot_populated_mask", b2);
                bundle.putByte("slot_returned_mask", b3);
                ArrayList<? extends Parcelable> arrayList2 = new ArrayList<>();
                arrayList.forEach(new ImageWallpaper$GLEngine$$ExternalSyntheticLambda3(arrayList2, 2));
                bundle.putParcelableArrayList("wpc_digests", arrayList2);
                resultReceiver.send(0, bundle);
                return;
            }
            getWpcAuthDigestsCallback2.mResultReceiver.send(1, null);
        }

        public GetWpcAuthDigestsCallbackWrapper(WirelessCharger.GetWpcAuthDigestsCallback getWpcAuthDigestsCallback) {
            this.mCallback = getWpcAuthDigestsCallback;
        }
    }

    /* loaded from: classes.dex */
    public final class IsDockPresentCallbackWrapper {
        public final WirelessCharger.IsDockPresentCallback mCallback;

        public final void onValues(boolean z, byte b, byte b2, boolean z2, int i) {
            DockObserver.IsDockPresentCallback isDockPresentCallback = (DockObserver.IsDockPresentCallback) this.mCallback;
            Objects.requireNonNull(isDockPresentCallback);
            Log.i("DLObserver", "IDP() response: d=" + z + ", i=" + i + ", t=" + ((int) b) + ", o=" + ((int) b2) + ", sgi=" + z2);
            if (z) {
                DockObserver dockObserver = DockObserver.this;
                Context context = isDockPresentCallback.mContext;
                String str = DockObserver.ACTION_START_DREAMLINER_CONTROL_SERVICE;
                Objects.requireNonNull(dockObserver);
                synchronized (dockObserver) {
                    DockObserver.notifyForceEnabledAmbientDisplay(true);
                    if (dockObserver.mDreamlinerServiceConn == null) {
                        dockObserver.mDreamlinerReceiver.registerReceiver(context);
                        ImageView imageView = dockObserver.mDreamlinerGear;
                        DockGestureController dockGestureController = new DockGestureController(context, imageView, dockObserver.mPhotoPreview, (View) imageView.getParent(), dockObserver.mIndicationController, dockObserver.mStatusBarStateController, (KeyguardStateController) Dependency.get(KeyguardStateController.class));
                        dockObserver.mDockGestureController = dockGestureController;
                        dockObserver.mConfigurationController.addCallback(dockGestureController);
                        Intent intent = new Intent(DockObserver.ACTION_START_DREAMLINER_CONTROL_SERVICE);
                        intent.setComponent(ComponentName.unflattenFromString(DockObserver.COMPONENTNAME_DREAMLINER_CONTROL_SERVICE));
                        intent.putExtra(IconCompat.EXTRA_TYPE, (int) b);
                        intent.putExtra("orientation", (int) b2);
                        intent.putExtra("id", i);
                        intent.putExtra("occluded", new KeyguardVisibility(context).mKeyguardStateController.isOccluded());
                        try {
                            DockObserver.DreamlinerServiceConn dreamlinerServiceConn = new DockObserver.DreamlinerServiceConn(context);
                            dockObserver.mDreamlinerServiceConn = dreamlinerServiceConn;
                            if (context.bindServiceAsUser(intent, dreamlinerServiceConn, 1, new UserHandle(dockObserver.mUserTracker.getCurrentUserId()))) {
                                dockObserver.mUserTracker.startTracking();
                                return;
                            }
                        } catch (SecurityException e) {
                            Log.e("DLObserver", e.getMessage(), e);
                        }
                        dockObserver.mDreamlinerServiceConn = null;
                        Log.w("DLObserver", "Unable to bind Dreamliner service: " + intent);
                    }
                }
            }
        }

        public IsDockPresentCallbackWrapper(WirelessCharger.IsDockPresentCallback isDockPresentCallback) {
            this.mCallback = isDockPresentCallback;
        }
    }

    /* loaded from: classes.dex */
    public final class KeyExchangeCallbackWrapper {
        public final WirelessCharger.KeyExchangeCallback mCallback;

        public final void onValues(byte b, KeyExchangeResponse keyExchangeResponse) {
            ((DockObserver.KeyExchangeCallback) this.mCallback).onCallback(Byte.valueOf(b).intValue(), keyExchangeResponse.dockId, keyExchangeResponse.dockPublicKey);
        }

        public KeyExchangeCallbackWrapper(WirelessCharger.KeyExchangeCallback keyExchangeCallback) {
            this.mCallback = keyExchangeCallback;
        }
    }

    /* loaded from: classes.dex */
    public static final class SetFanCallbackWrapper {
        public final WirelessCharger.SetFanCallback mCallback;
        public final byte mFanId;

        public final void onValues(byte b, FanInfo fanInfo) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("command=1, result=");
            m.append(Byte.valueOf(b).intValue());
            m.append(", i=");
            m.append((int) this.mFanId);
            m.append(", m=");
            m.append((int) fanInfo.fanMode);
            m.append(", cr=");
            KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(m, fanInfo.currentRpm, "Dreamliner-WLC_HAL");
            WirelessCharger.SetFanCallback setFanCallback = this.mCallback;
            Byte.valueOf(b).intValue();
            byte b2 = this.mFanId;
            int i = WirelessChargerImpl.$r8$clinit;
            Bundle bundle = new Bundle();
            bundle.putByte("fan_id", b2);
            bundle.putByte("fan_mode", fanInfo.fanMode);
            bundle.putInt("fan_current_rpm", fanInfo.currentRpm);
            Objects.requireNonNull((DockObserver.SetFanCallback) setFanCallback);
            Log.d("DLObserver", "Callback of command=1, i=" + bundle.getByte("fan_id", (byte) -1) + ", m=" + bundle.getByte("fan_mode", (byte) -1) + ", cr=" + bundle.getInt("fan_current_rpm", -1));
        }

        public SetFanCallbackWrapper(byte b, WirelessCharger.SetFanCallback setFanCallback) {
            this.mFanId = b;
            this.mCallback = setFanCallback;
        }
    }

    /* loaded from: classes.dex */
    public final class WirelessChargerInfoCallback extends IWirelessChargerInfoCallback$Stub {
        public final WirelessCharger.AlignInfoListener mListener;

        public WirelessChargerInfoCallback(WirelessCharger.AlignInfoListener alignInfoListener) {
            this.mListener = alignInfoListener;
        }
    }

    public static ArrayList convertPrimitiveArrayToArrayList(byte[] bArr) {
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (byte b : bArr) {
            arrayList.add(Byte.valueOf(b));
        }
        return arrayList;
    }

    /* JADX WARN: Finally extract failed */
    public final void initHALInterface() {
        IWirelessCharger.Proxy proxy;
        if (this.mWirelessCharger == null) {
            try {
                IHwBinder service = HwBinder.getService("vendor.google.wireless_charger@1.3::IWirelessCharger", "default");
                if (service != null) {
                    IHwInterface queryLocalInterface = service.queryLocalInterface("vendor.google.wireless_charger@1.3::IWirelessCharger");
                    if (queryLocalInterface == null || !(queryLocalInterface instanceof IWirelessCharger)) {
                        IWirelessCharger.Proxy proxy2 = new IWirelessCharger.Proxy(service);
                        try {
                            HwParcel hwParcel = new HwParcel();
                            hwParcel.writeInterfaceToken("android.hidl.base@1.0::IBase");
                            HwParcel hwParcel2 = new HwParcel();
                            try {
                                proxy2.mRemote.transact(256067662, hwParcel, hwParcel2, 0);
                                hwParcel2.verifySuccess();
                                hwParcel.releaseTemporaryStorage();
                                ArrayList readStringVector = hwParcel2.readStringVector();
                                hwParcel2.release();
                                Iterator it = readStringVector.iterator();
                                while (it.hasNext()) {
                                    if (((String) it.next()).equals("vendor.google.wireless_charger@1.3::IWirelessCharger")) {
                                        proxy = proxy2;
                                        break;
                                    }
                                }
                            } catch (Throwable th) {
                                hwParcel2.release();
                                throw th;
                            }
                        } catch (RemoteException unused) {
                        }
                    } else {
                        proxy = (IWirelessCharger) queryLocalInterface;
                    }
                    this.mWirelessCharger = proxy;
                    proxy.linkToDeath(this);
                }
                proxy = null;
                this.mWirelessCharger = proxy;
                proxy.linkToDeath(this);
            } catch (Exception e) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("no wireless charger hal found: ");
                m.append(e.getMessage());
                Log.i("Dreamliner-WLC_HAL", m.toString());
                this.mWirelessCharger = null;
            }
        }
    }

    public final void serviceDied(long j) {
        Log.i("Dreamliner-WLC_HAL", "serviceDied");
        this.mWirelessCharger = null;
    }

    @Override // com.google.android.systemui.dreamliner.WirelessCharger
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public void asyncIsDockPresent(WirelessCharger.IsDockPresentCallback isDockPresentCallback) {
        initHALInterface();
        if (this.mWirelessCharger != null) {
            this.mPollingStartedTimeNs = System.nanoTime();
            this.mCallback = new IsDockPresentCallbackWrapper(isDockPresentCallback);
            this.mHandler.removeCallbacks(this.mRunnable);
            this.mHandler.postDelayed(this.mRunnable, 100L);
        }
    }

    @Override // com.google.android.systemui.dreamliner.WirelessCharger
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public void challenge(byte b, byte[] bArr, WirelessCharger.ChallengeCallback challengeCallback) {
        initHALInterface();
        if (this.mWirelessCharger != null) {
            try {
                this.mWirelessCharger.challenge(b, convertPrimitiveArrayToArrayList(bArr), new ChallengeCallbackWrapper(challengeCallback));
            } catch (Exception e) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("challenge fail: ");
                m.append(e.getMessage());
                Log.i("Dreamliner-WLC_HAL", m.toString());
            }
        }
    }

    @Override // com.google.android.systemui.dreamliner.WirelessCharger
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public void getFanInformation(byte b, WirelessCharger.GetFanInformationCallback getFanInformationCallback) {
        initHALInterface();
        Log.d("Dreamliner-WLC_HAL", "command=0");
        IWirelessCharger iWirelessCharger = this.mWirelessCharger;
        if (iWirelessCharger != null) {
            try {
                iWirelessCharger.getFanInformation(b, new GetFanInformationCallbackWrapper(b, getFanInformationCallback));
            } catch (Exception e) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("command=0 fail: ");
                m.append(e.getMessage());
                Log.i("Dreamliner-WLC_HAL", m.toString());
            }
        }
    }

    @Override // com.google.android.systemui.dreamliner.WirelessCharger
    public final int getFanLevel() {
        initHALInterface();
        Log.d("Dreamliner-WLC_HAL", "command=2");
        IWirelessCharger iWirelessCharger = this.mWirelessCharger;
        if (iWirelessCharger == null) {
            return -1;
        }
        try {
            return iWirelessCharger.getFanLevel();
        } catch (Exception e) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("command=2 fail: ");
            m.append(e.getMessage());
            Log.i("Dreamliner-WLC_HAL", m.toString());
            return -1;
        }
    }

    @Override // com.google.android.systemui.dreamliner.WirelessCharger
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public void getFanSimpleInformation(byte b, WirelessCharger.GetFanSimpleInformationCallback getFanSimpleInformationCallback) {
        initHALInterface();
        Log.d("Dreamliner-WLC_HAL", "command=3");
        IWirelessCharger iWirelessCharger = this.mWirelessCharger;
        if (iWirelessCharger != null) {
            try {
                iWirelessCharger.getFan(b, new GetFanSimpleInformationCallbackWrapper(b, getFanSimpleInformationCallback));
            } catch (Exception e) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("command=3 fail: ");
                m.append(e.getMessage());
                Log.i("Dreamliner-WLC_HAL", m.toString());
            }
        }
    }

    @Override // com.google.android.systemui.dreamliner.WirelessCharger
    public final void getFeatures(long j, WirelessCharger.GetFeaturesCallback getFeaturesCallback) {
        initHALInterface();
        IWirelessCharger iWirelessCharger = this.mWirelessCharger;
        if (iWirelessCharger != null) {
            try {
                iWirelessCharger.getFeatures(j, new GetFeaturesCallbackWrapper(getFeaturesCallback));
            } catch (Exception e) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("get features fail: ");
                m.append(e.getMessage());
                Log.i("Dreamliner-WLC_HAL", m.toString());
            }
        }
    }

    @Override // com.google.android.systemui.dreamliner.WirelessCharger
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public void getInformation(WirelessCharger.GetInformationCallback getInformationCallback) {
        initHALInterface();
        IWirelessCharger iWirelessCharger = this.mWirelessCharger;
        if (iWirelessCharger != null) {
            try {
                iWirelessCharger.getInformation(new GetInformationCallbackWrapper(getInformationCallback));
            } catch (Exception e) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("getInformation fail: ");
                m.append(e.getMessage());
                Log.i("Dreamliner-WLC_HAL", m.toString());
            }
        }
    }

    @Override // com.google.android.systemui.dreamliner.WirelessCharger
    public final void getWpcAuthCertificate(byte b, short s, short s2, WirelessCharger.GetWpcAuthCertificateCallback getWpcAuthCertificateCallback) {
        initHALInterface();
        IWirelessCharger iWirelessCharger = this.mWirelessCharger;
        if (iWirelessCharger != null) {
            try {
                iWirelessCharger.getWpcAuthCertificate(b, s, s2, new GetWpcAuthCertificateCallbackWrapper(getWpcAuthCertificateCallback));
            } catch (Exception e) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("get wpc cert fail: ");
                m.append(e.getMessage());
                Log.i("Dreamliner-WLC_HAL", m.toString());
            }
        }
    }

    @Override // com.google.android.systemui.dreamliner.WirelessCharger
    public final void getWpcAuthChallengeResponse(byte b, byte[] bArr, WirelessCharger.GetWpcAuthChallengeResponseCallback getWpcAuthChallengeResponseCallback) {
        initHALInterface();
        IWirelessCharger iWirelessCharger = this.mWirelessCharger;
        if (iWirelessCharger != null) {
            try {
                iWirelessCharger.getWpcAuthChallengeResponse(b, convertPrimitiveArrayToArrayList(bArr), new GetWpcAuthChallengeResponseCallbackWrapper(getWpcAuthChallengeResponseCallback));
            } catch (Exception e) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("get wpc challenge response fail: ");
                m.append(e.getMessage());
                Log.i("Dreamliner-WLC_HAL", m.toString());
            }
        }
    }

    @Override // com.google.android.systemui.dreamliner.WirelessCharger
    public final void getWpcAuthDigests(byte b, WirelessCharger.GetWpcAuthDigestsCallback getWpcAuthDigestsCallback) {
        initHALInterface();
        IWirelessCharger iWirelessCharger = this.mWirelessCharger;
        if (iWirelessCharger != null) {
            try {
                iWirelessCharger.getWpcAuthDigests(b, new GetWpcAuthDigestsCallbackWrapper(getWpcAuthDigestsCallback));
            } catch (Exception e) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("get wpc digests fail: ");
                m.append(e.getMessage());
                Log.i("Dreamliner-WLC_HAL", m.toString());
            }
        }
    }

    @Override // com.google.android.systemui.dreamliner.WirelessCharger
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public void keyExchange(byte[] bArr, WirelessCharger.KeyExchangeCallback keyExchangeCallback) {
        initHALInterface();
        if (this.mWirelessCharger != null) {
            try {
                this.mWirelessCharger.keyExchange(convertPrimitiveArrayToArrayList(bArr), new KeyExchangeCallbackWrapper(keyExchangeCallback));
            } catch (Exception e) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("keyExchange fail: ");
                m.append(e.getMessage());
                Log.i("Dreamliner-WLC_HAL", m.toString());
            }
        }
    }

    public final void onValues(boolean z, byte b, byte b2, boolean z2, int i) {
        if (System.nanoTime() >= this.mPollingStartedTimeNs + MAX_POLLING_TIMEOUT_NS || i != 0) {
            IsDockPresentCallbackWrapper isDockPresentCallbackWrapper = this.mCallback;
            if (isDockPresentCallbackWrapper != null) {
                isDockPresentCallbackWrapper.onValues(z, b, b2, z2, i);
                this.mCallback = null;
                return;
            }
            return;
        }
        this.mHandler.postDelayed(this.mRunnable, 100L);
    }

    @Override // com.google.android.systemui.dreamliner.WirelessCharger
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public void registerAlignInfo(WirelessCharger.AlignInfoListener alignInfoListener) {
        initHALInterface();
        IWirelessCharger iWirelessCharger = this.mWirelessCharger;
        if (iWirelessCharger != null) {
            try {
                iWirelessCharger.registerCallback(new WirelessChargerInfoCallback(alignInfoListener));
            } catch (Exception e) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("register alignInfo callback fail: ");
                m.append(e.getMessage());
                Log.i("Dreamliner-WLC_HAL", m.toString());
            }
        }
    }

    @Override // com.google.android.systemui.dreamliner.WirelessCharger
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public void setFan(byte b, byte b2, int i, WirelessCharger.SetFanCallback setFanCallback) {
        initHALInterface();
        Log.d("Dreamliner-WLC_HAL", "command=1, i=" + ((int) b) + ", m=" + ((int) b2) + ", r=" + i);
        if (this.mWirelessCharger != null) {
            try {
                long currentTimeMillis = System.currentTimeMillis();
                this.mWirelessCharger.setFan(b, b2, (short) i, new SetFanCallbackWrapper(b, setFanCallback));
                Log.d("Dreamliner-WLC_HAL", "command=1 spending time: " + (System.currentTimeMillis() - currentTimeMillis));
            } catch (Exception e) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("command=1 fail: ");
                m.append(e.getMessage());
                Log.i("Dreamliner-WLC_HAL", m.toString());
            }
        }
    }

    @Override // com.google.android.systemui.dreamliner.WirelessCharger
    public final void setFeatures(long j, long j2, CaptionsToggleImageButton$$ExternalSyntheticLambda0 captionsToggleImageButton$$ExternalSyntheticLambda0) {
        initHALInterface();
        IWirelessCharger iWirelessCharger = this.mWirelessCharger;
        if (iWirelessCharger != null) {
            try {
                byte features = iWirelessCharger.setFeatures(j, j2);
                DockObserver.SetFeatures setFeatures = (DockObserver.SetFeatures) captionsToggleImageButton$$ExternalSyntheticLambda0.f$0;
                Objects.requireNonNull(setFeatures);
                setFeatures.mResultReceiver.send(features, null);
            } catch (Exception e) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("set features fail: ");
                m.append(e.getMessage());
                Log.i("Dreamliner-WLC_HAL", m.toString());
            }
        }
    }
}
