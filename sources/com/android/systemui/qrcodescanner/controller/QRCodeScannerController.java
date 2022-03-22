package com.android.systemui.qrcodescanner.controller;

import android.R;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.provider.DeviceConfig;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline1;
import com.android.keyguard.KeyguardVisibilityHelper$$ExternalSyntheticLambda0;
import com.android.settingslib.wifi.AccessPoint$$ExternalSyntheticLambda0;
import com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda3;
import com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda4;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.statusbar.policy.LocationControllerImpl$$ExternalSyntheticLambda0;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.settings.SecureSettings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
/* loaded from: classes.dex */
public final class QRCodeScannerController implements CallbackController<Callback> {
    public final boolean mConfigEnableLockScreenButton;
    public final Context mContext;
    public final DeviceConfigProxy mDeviceConfigProxy;
    public final Executor mExecutor;
    public boolean mQRCodeScannerEnabled;
    public final SecureSettings mSecureSettings;
    public final UserTracker mUserTracker;
    public final ArrayList<Callback> mCallbacks = new ArrayList<>();
    public HashMap<Integer, ContentObserver> mQRCodeScannerPreferenceObserver = new HashMap<>();
    public LocationControllerImpl$$ExternalSyntheticLambda0 mOnDefaultQRCodeScannerChangedListener = null;
    public AnonymousClass2 mUserChangedListener = null;
    public Intent mIntent = null;
    public String mQRCodeScannerActivity = null;
    public AtomicInteger mQRCodeScannerPreferenceChangeEvents = new AtomicInteger(0);
    public AtomicInteger mDefaultQRCodeScannerChangeEvents = new AtomicInteger(0);
    public Boolean mIsCameraAvailable = null;

    /* renamed from: com.android.systemui.qrcodescanner.controller.QRCodeScannerController$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends ContentObserver {
        public static final /* synthetic */ int $r8$clinit = 0;

        public AnonymousClass1() {
            super(null);
        }

        @Override // android.database.ContentObserver
        public final void onChange(boolean z) {
            QRCodeScannerController.this.mExecutor.execute(new KeyguardVisibilityHelper$$ExternalSyntheticLambda0(this, 1));
        }
    }

    /* loaded from: classes.dex */
    public interface Callback {
        default void onQRCodeScannerActivityChanged() {
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(Callback callback) {
        Callback callback2 = callback;
        if (isCameraAvailable()) {
            synchronized (this.mCallbacks) {
                this.mCallbacks.add(callback2);
            }
        }
    }

    public final boolean isCameraAvailable() {
        if (this.mIsCameraAvailable == null) {
            this.mIsCameraAvailable = Boolean.valueOf(this.mContext.getPackageManager().hasSystemFeature("android.hardware.camera"));
        }
        return this.mIsCameraAvailable.booleanValue();
    }

    public final void registerQRCodePreferenceObserver() {
        if (this.mConfigEnableLockScreenButton) {
            int userId = this.mUserTracker.getUserId();
            if (this.mQRCodeScannerPreferenceObserver.getOrDefault(Integer.valueOf(userId), null) == null) {
                this.mExecutor.execute(new AccessPoint$$ExternalSyntheticLambda0(this, 4));
                this.mQRCodeScannerPreferenceObserver.put(Integer.valueOf(userId), new AnonymousClass1());
                SecureSettings secureSettings = this.mSecureSettings;
                secureSettings.registerContentObserverForUser(secureSettings.getUriFor("lock_screen_show_qr_code_scanner"), false, this.mQRCodeScannerPreferenceObserver.get(Integer.valueOf(userId)), userId);
            }
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(Callback callback) {
        Callback callback2 = callback;
        if (isCameraAvailable()) {
            synchronized (this.mCallbacks) {
                this.mCallbacks.remove(callback2);
            }
        }
    }

    public final void updateQRCodeScannerActivityDetails() {
        boolean z;
        ArrayList arrayList;
        Objects.requireNonNull(this.mDeviceConfigProxy);
        String string = DeviceConfig.getString("systemui", "default_qr_code_scanner", "");
        if (Objects.equals(string, "")) {
            string = this.mContext.getResources().getString(2131952260);
        }
        String str = this.mQRCodeScannerActivity;
        Intent intent = new Intent();
        if (string != null) {
            intent.setComponent(ComponentName.unflattenFromString(string));
            intent.addFlags(335544320);
        }
        if (intent.getComponent() == null) {
            z = false;
        } else {
            z = !this.mContext.getPackageManager().queryIntentActivities(intent, 537698816).isEmpty();
        }
        if (z) {
            this.mQRCodeScannerActivity = string;
            this.mIntent = intent;
        } else {
            this.mQRCodeScannerActivity = null;
            this.mIntent = null;
        }
        if (!Objects.equals(this.mQRCodeScannerActivity, str)) {
            synchronized (this.mCallbacks) {
                arrayList = (ArrayList) this.mCallbacks.clone();
            }
            arrayList.forEach(QSTileHost$$ExternalSyntheticLambda4.INSTANCE$2);
        }
    }

    public final void updateQRCodeScannerPreferenceDetails(boolean z) {
        ArrayList arrayList;
        if (this.mConfigEnableLockScreenButton) {
            boolean z2 = this.mQRCodeScannerEnabled;
            boolean z3 = false;
            if (this.mSecureSettings.getIntForUser("lock_screen_show_qr_code_scanner", 0, this.mUserTracker.getUserId()) != 0) {
                z3 = true;
            }
            this.mQRCodeScannerEnabled = z3;
            if (z) {
                this.mSecureSettings.putStringForUser("show_qr_code_scanner_setting", this.mQRCodeScannerActivity, this.mUserTracker.getUserId());
            }
            if (!Objects.equals(Boolean.valueOf(this.mQRCodeScannerEnabled), Boolean.valueOf(z2))) {
                synchronized (this.mCallbacks) {
                    arrayList = (ArrayList) this.mCallbacks.clone();
                }
                arrayList.forEach(QSTileHost$$ExternalSyntheticLambda3.INSTANCE$2);
            }
        }
    }

    public QRCodeScannerController(Context context, Executor executor, SecureSettings secureSettings, DeviceConfigProxy deviceConfigProxy, UserTracker userTracker) {
        this.mContext = context;
        this.mExecutor = executor;
        this.mSecureSettings = secureSettings;
        this.mDeviceConfigProxy = deviceConfigProxy;
        this.mUserTracker = userTracker;
        this.mConfigEnableLockScreenButton = context.getResources().getBoolean(R.bool.config_enableQrCodeScannerOnLockScreen);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v7, types: [com.android.systemui.qrcodescanner.controller.QRCodeScannerController$2, com.android.systemui.settings.UserTracker$Callback] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void registerQRCodeScannerChangeObservers(int... r7) {
        /*
            r6 = this;
            boolean r0 = r6.isCameraAvailable()
            if (r0 != 0) goto L_0x0007
            return
        L_0x0007:
            int r0 = r7.length
            r1 = 0
        L_0x0009:
            if (r1 >= r0) goto L_0x0062
            r2 = r7[r1]
            r3 = 1
            if (r2 == 0) goto L_0x0036
            if (r2 == r3) goto L_0x001a
            java.lang.String r3 = "Unrecognised event: "
            java.lang.String r4 = "QRCodeScannerController"
            com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline1.m(r3, r2, r4)
            goto L_0x005f
        L_0x001a:
            java.util.concurrent.atomic.AtomicInteger r2 = r6.mQRCodeScannerPreferenceChangeEvents
            r2.incrementAndGet()
            r6.registerQRCodePreferenceObserver()
            com.android.systemui.qrcodescanner.controller.QRCodeScannerController$2 r2 = r6.mUserChangedListener
            if (r2 == 0) goto L_0x0027
            goto L_0x005f
        L_0x0027:
            com.android.systemui.qrcodescanner.controller.QRCodeScannerController$2 r2 = new com.android.systemui.qrcodescanner.controller.QRCodeScannerController$2
            r2.<init>()
            r6.mUserChangedListener = r2
            com.android.systemui.settings.UserTracker r3 = r6.mUserTracker
            java.util.concurrent.Executor r4 = r6.mExecutor
            r3.addCallback(r2, r4)
            goto L_0x005f
        L_0x0036:
            java.util.concurrent.atomic.AtomicInteger r2 = r6.mDefaultQRCodeScannerChangeEvents
            r2.incrementAndGet()
            com.android.systemui.statusbar.policy.LocationControllerImpl$$ExternalSyntheticLambda0 r2 = r6.mOnDefaultQRCodeScannerChangedListener
            if (r2 == 0) goto L_0x0040
            goto L_0x005f
        L_0x0040:
            java.util.concurrent.Executor r2 = r6.mExecutor
            com.android.wifitrackerlib.WifiEntry$$ExternalSyntheticLambda2 r4 = new com.android.wifitrackerlib.WifiEntry$$ExternalSyntheticLambda2
            r5 = 3
            r4.<init>(r6, r5)
            r2.execute(r4)
            com.android.systemui.statusbar.policy.LocationControllerImpl$$ExternalSyntheticLambda0 r2 = new com.android.systemui.statusbar.policy.LocationControllerImpl$$ExternalSyntheticLambda0
            r2.<init>(r6, r3)
            r6.mOnDefaultQRCodeScannerChangedListener = r2
            com.android.systemui.util.DeviceConfigProxy r3 = r6.mDeviceConfigProxy
            java.util.concurrent.Executor r4 = r6.mExecutor
            java.util.Objects.requireNonNull(r3)
            java.lang.String r3 = "systemui"
            android.provider.DeviceConfig.addOnPropertiesChangedListener(r3, r4, r2)
        L_0x005f:
            int r1 = r1 + 1
            goto L_0x0009
        L_0x0062:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qrcodescanner.controller.QRCodeScannerController.registerQRCodeScannerChangeObservers(int[]):void");
    }

    public final void unregisterQRCodeScannerChangeObservers(int... iArr) {
        if (isCameraAvailable()) {
            for (int i : iArr) {
                if (i != 0) {
                    if (i != 1) {
                        KeyguardUpdateMonitor$$ExternalSyntheticOutline1.m("Unrecognised event: ", i, "QRCodeScannerController");
                    } else if (this.mUserTracker != null && this.mQRCodeScannerPreferenceChangeEvents.decrementAndGet() == 0) {
                        if (this.mConfigEnableLockScreenButton) {
                            this.mQRCodeScannerPreferenceObserver.forEach(new BiConsumer() { // from class: com.android.systemui.qrcodescanner.controller.QRCodeScannerController$$ExternalSyntheticLambda0
                                @Override // java.util.function.BiConsumer
                                public final void accept(Object obj, Object obj2) {
                                    QRCodeScannerController qRCodeScannerController = QRCodeScannerController.this;
                                    Integer num = (Integer) obj;
                                    Objects.requireNonNull(qRCodeScannerController);
                                    qRCodeScannerController.mSecureSettings.unregisterContentObserver((ContentObserver) obj2);
                                }
                            });
                            this.mQRCodeScannerPreferenceObserver = new HashMap<>();
                            this.mSecureSettings.putStringForUser("show_qr_code_scanner_setting", null, this.mUserTracker.getUserId());
                        }
                        this.mUserTracker.removeCallback(this.mUserChangedListener);
                        this.mUserChangedListener = null;
                        this.mQRCodeScannerEnabled = false;
                    }
                } else if (this.mOnDefaultQRCodeScannerChangedListener != null && this.mDefaultQRCodeScannerChangeEvents.decrementAndGet() == 0) {
                    DeviceConfigProxy deviceConfigProxy = this.mDeviceConfigProxy;
                    LocationControllerImpl$$ExternalSyntheticLambda0 locationControllerImpl$$ExternalSyntheticLambda0 = this.mOnDefaultQRCodeScannerChangedListener;
                    Objects.requireNonNull(deviceConfigProxy);
                    DeviceConfig.removeOnPropertiesChangedListener(locationControllerImpl$$ExternalSyntheticLambda0);
                    this.mOnDefaultQRCodeScannerChangedListener = null;
                    this.mQRCodeScannerActivity = null;
                    this.mIntent = null;
                }
            }
        }
    }
}
