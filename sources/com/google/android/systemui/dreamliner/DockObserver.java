package com.google.android.systemui.dreamliner;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.service.dreams.IDreamManager;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.core.view.ViewCompat$$ExternalSyntheticLambda0;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline1;
import androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline0;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline3;
import com.android.systemui.dock.DockManager;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.KeyguardIndicationController$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.volume.CaptionsToggleImageButton$$ExternalSyntheticLambda0;
import com.android.systemui.wmshell.BubblesManager$5$$ExternalSyntheticLambda3;
import com.android.wm.shell.common.ExecutorUtils$$ExternalSyntheticLambda1;
import com.google.android.systemui.dreamliner.DockObserver;
import com.google.android.systemui.dreamliner.WirelessCharger;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/* loaded from: classes.dex */
public final class DockObserver extends BroadcastReceiver implements DockManager {
    @VisibleForTesting
    public static final String ACTION_ALIGN_STATE_CHANGE = "com.google.android.systemui.dreamliner.ALIGNMENT_CHANGE";
    @VisibleForTesting
    public static final String ACTION_CHALLENGE = "com.google.android.systemui.dreamliner.ACTION_CHALLENGE";
    @VisibleForTesting
    public static final String ACTION_DOCK_UI_ACTIVE = "com.google.android.systemui.dreamliner.ACTION_DOCK_UI_ACTIVE";
    @VisibleForTesting
    public static final String ACTION_DOCK_UI_IDLE = "com.google.android.systemui.dreamliner.ACTION_DOCK_UI_IDLE";
    @VisibleForTesting
    public static final String ACTION_GET_DOCK_INFO = "com.google.android.systemui.dreamliner.ACTION_GET_DOCK_INFO";
    @VisibleForTesting
    public static final String ACTION_KEY_EXCHANGE = "com.google.android.systemui.dreamliner.ACTION_KEY_EXCHANGE";
    @VisibleForTesting
    public static final String ACTION_REBIND_DOCK_SERVICE = "com.google.android.systemui.dreamliner.ACTION_REBIND_DOCK_SERVICE";
    @VisibleForTesting
    public static final String ACTION_START_DREAMLINER_CONTROL_SERVICE = "com.google.android.apps.dreamliner.START";
    @VisibleForTesting
    public static final String COMPONENTNAME_DREAMLINER_CONTROL_SERVICE = "com.google.android.apps.dreamliner/.DreamlinerControlService";
    @VisibleForTesting
    public static final String EXTRA_ALIGN_STATE = "align_state";
    @VisibleForTesting
    public static final String EXTRA_CHALLENGE_DATA = "challenge_data";
    @VisibleForTesting
    public static final String EXTRA_CHALLENGE_DOCK_ID = "challenge_dock_id";
    @VisibleForTesting
    public static final String EXTRA_PUBLIC_KEY = "public_key";
    @VisibleForTesting
    public static final String KEY_SHOWING = "showing";
    @VisibleForTesting
    public static final String PERMISSION_WIRELESS_CHARGER_STATUS = "com.google.android.systemui.permission.WIRELESS_CHARGER_STATUS";
    @VisibleForTesting
    public static final int RESULT_NOT_FOUND = 1;
    @VisibleForTesting
    public static final int RESULT_OK = 0;
    @VisibleForTesting
    public static volatile ExecutorService mSingleThreadExecutor = null;
    public static boolean sIsDockingUiShowing = false;
    public final ConfigurationController mConfigurationController;
    public final Context mContext;
    public final DockAlignmentController mDockAlignmentController;
    @VisibleForTesting
    public DockGestureController mDockGestureController;
    public ImageView mDreamlinerGear;
    @VisibleForTesting
    public DreamlinerServiceConn mDreamlinerServiceConn;
    public DockIndicationController mIndicationController;
    public final AnonymousClass2 mInterruptSuppressor;
    public final DelayableExecutor mMainExecutor;
    public DockObserver$$ExternalSyntheticLambda0 mPhotoAction;
    public FrameLayout mPhotoPreview;
    public final StatusBarStateController mStatusBarStateController;
    public final AnonymousClass1 mUserTracker;
    public final WirelessCharger mWirelessCharger;
    @VisibleForTesting
    public final DreamlinerBroadcastReceiver mDreamlinerReceiver = new DreamlinerBroadcastReceiver();
    @VisibleForTesting
    public int mDockState = 0;
    @VisibleForTesting
    public int mLastAlignState = -1;
    public int mFanLevel = -1;
    public final ArrayList mClients = new ArrayList();
    public final ArrayList mAlignmentStateListeners = new ArrayList();

    @VisibleForTesting
    /* loaded from: classes.dex */
    public final class ChallengeCallback implements WirelessCharger.ChallengeCallback {
        public final ResultReceiver mResultReceiver;

        public ChallengeCallback(ResultReceiver resultReceiver) {
            this.mResultReceiver = resultReceiver;
        }
    }

    /* loaded from: classes.dex */
    public class ChallengeWithDock implements Runnable {
        public final byte[] challengeData;
        public final byte dockId;
        public final ResultReceiver resultReceiver;

        public ChallengeWithDock(ResultReceiver resultReceiver, byte b, byte[] bArr) {
            this.dockId = b;
            this.challengeData = bArr;
            this.resultReceiver = resultReceiver;
        }

        @Override // java.lang.Runnable
        public final void run() {
            DockObserver dockObserver = DockObserver.this;
            WirelessCharger wirelessCharger = dockObserver.mWirelessCharger;
            if (wirelessCharger != null) {
                wirelessCharger.challenge(this.dockId, this.challengeData, new ChallengeCallback(this.resultReceiver));
            }
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public class DreamlinerBroadcastReceiver extends BroadcastReceiver {
        public static final /* synthetic */ int $r8$clinit = 0;
        public boolean mListening;

        public DreamlinerBroadcastReceiver() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            char c;
            if (intent != null) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Dock Receiver.onReceive(): ");
                m.append(intent.getAction());
                Log.d("DLObserver", m.toString());
                String action = intent.getAction();
                Objects.requireNonNull(action);
                switch (action.hashCode()) {
                    case -2133451883:
                        if (action.equals("com.google.android.systemui.dreamliner.ACTION_GET_FAN_LEVEL")) {
                            c = 0;
                            break;
                        }
                        c = 65535;
                        break;
                    case -1627881412:
                        if (action.equals("com.google.android.systemui.dreamliner.ACTION_SET_FAN")) {
                            c = 1;
                            break;
                        }
                        c = 65535;
                        break;
                    case -1616532553:
                        if (action.equals(DockObserver.ACTION_GET_DOCK_INFO)) {
                            c = 2;
                            break;
                        }
                        c = 65535;
                        break;
                    case -1598391011:
                        if (action.equals(DockObserver.ACTION_KEY_EXCHANGE)) {
                            c = 3;
                            break;
                        }
                        c = 65535;
                        break;
                    case -1584152500:
                        if (action.equals("com.google.android.systemui.dreamliner.photo_error")) {
                            c = 4;
                            break;
                        }
                        c = 65535;
                        break;
                    case -1579804275:
                        if (action.equals(DockObserver.ACTION_DOCK_UI_IDLE)) {
                            c = 5;
                            break;
                        }
                        c = 65535;
                        break;
                    case -1458969207:
                        if (action.equals(DockObserver.ACTION_CHALLENGE)) {
                            c = 6;
                            break;
                        }
                        c = 65535;
                        break;
                    case -1185055092:
                        if (action.equals("com.google.android.systemui.dreamliner.ACTION_GET_FAN_SIMPLE_INFO")) {
                            c = 7;
                            break;
                        }
                        c = 65535;
                        break;
                    case -686255721:
                        if (action.equals("com.google.android.systemui.dreamliner.ACTION_GET_WPC_DIGESTS")) {
                            c = '\b';
                            break;
                        }
                        c = 65535;
                        break;
                    case -545730616:
                        if (action.equals("com.google.android.systemui.dreamliner.paired")) {
                            c = '\t';
                            break;
                        }
                        c = 65535;
                        break;
                    case -484477188:
                        if (action.equals("com.google.android.systemui.dreamliner.resume")) {
                            c = '\n';
                            break;
                        }
                        c = 65535;
                        break;
                    case -390730981:
                        if (action.equals("com.google.android.systemui.dreamliner.undock")) {
                            c = 11;
                            break;
                        }
                        c = 65535;
                        break;
                    case 664552276:
                        if (action.equals("com.google.android.systemui.dreamliner.dream")) {
                            c = '\f';
                            break;
                        }
                        c = 65535;
                        break;
                    case 675144007:
                        if (action.equals("com.google.android.systemui.dreamliner.pause")) {
                            c = '\r';
                            break;
                        }
                        c = 65535;
                        break;
                    case 675346819:
                        if (action.equals("com.google.android.systemui.dreamliner.photo")) {
                            c = 14;
                            break;
                        }
                        c = 65535;
                        break;
                    case 717413661:
                        if (action.equals("com.google.android.systemui.dreamliner.assistant_poodle")) {
                            c = 15;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1954561023:
                        if (action.equals("com.google.android.systemui.dreamliner.ACTION_GET_WPC_CERTIFICATE")) {
                            c = 16;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1996802687:
                        if (action.equals(DockObserver.ACTION_DOCK_UI_ACTIVE)) {
                            c = 17;
                            break;
                        }
                        c = 65535;
                        break;
                    case 2009307741:
                        if (action.equals("com.google.android.systemui.dreamliner.ACTION_GET_FAN_INFO")) {
                            c = 18;
                            break;
                        }
                        c = 65535;
                        break;
                    case 2121889077:
                        if (action.equals("com.google.android.systemui.dreamliner.ACTION_GET_WPC_CHALLENGE_RESPONSE")) {
                            c = 19;
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
                        DockObserver.this.refreshFanLevel(new Runnable() { // from class: com.google.android.systemui.dreamliner.DockObserver$DreamlinerBroadcastReceiver$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                DockObserver.DreamlinerBroadcastReceiver dreamlinerBroadcastReceiver = DockObserver.DreamlinerBroadcastReceiver.this;
                                int i = DockObserver.DreamlinerBroadcastReceiver.$r8$clinit;
                                Objects.requireNonNull(dreamlinerBroadcastReceiver);
                                DockObserver.this.notifyDreamlinerLatestFanLevel();
                            }
                        });
                        return;
                    case 1:
                        StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("command=1, i=");
                        m2.append((int) intent.getByteExtra("fan_id", (byte) -1));
                        m2.append(", m=");
                        m2.append((int) intent.getByteExtra("fan_mode", (byte) -1));
                        m2.append(", r=");
                        m2.append(intent.getIntExtra("fan_rpm", -1));
                        Log.d("DLObserver", m2.toString());
                        byte byteExtra = intent.getByteExtra("fan_id", (byte) 0);
                        byte byteExtra2 = intent.getByteExtra("fan_mode", (byte) 0);
                        int intExtra = intent.getIntExtra("fan_rpm", -1);
                        if (byteExtra2 == 1 && intExtra == -1) {
                            Log.e("DLObserver", "Failed to get r.");
                            return;
                        } else {
                            DockObserver.runOnBackgroundThread(new SetFan(byteExtra, byteExtra2, intExtra));
                            return;
                        }
                    case 2:
                        ResultReceiver resultReceiver = (ResultReceiver) intent.getParcelableExtra("android.intent.extra.RESULT_RECEIVER");
                        if (resultReceiver != null) {
                            DockObserver.runOnBackgroundThread(new GetDockInfo(resultReceiver));
                            return;
                        }
                        return;
                    case 3:
                        DockObserver dockObserver = DockObserver.this;
                        Objects.requireNonNull(dockObserver);
                        Log.d("DLObserver", "triggerKeyExchangeWithDock");
                        ResultReceiver resultReceiver2 = (ResultReceiver) intent.getParcelableExtra("android.intent.extra.RESULT_RECEIVER");
                        if (resultReceiver2 != null) {
                            byte[] byteArrayExtra = intent.getByteArrayExtra(DockObserver.EXTRA_PUBLIC_KEY);
                            if (byteArrayExtra == null || byteArrayExtra.length <= 0) {
                                resultReceiver2.send(1, null);
                                return;
                            } else {
                                DockObserver.runOnBackgroundThread(new KeyExchangeWithDock(resultReceiver2, byteArrayExtra));
                                return;
                            }
                        } else {
                            return;
                        }
                    case 4:
                        DockObserver dockObserver2 = DockObserver.this;
                        Objects.requireNonNull(dockObserver2);
                        Log.w("DLObserver", "Fail to launch photo");
                        DockGestureController dockGestureController = dockObserver2.mDockGestureController;
                        if (dockGestureController != null) {
                            dockGestureController.hidePhotoPreview(false);
                            return;
                        }
                        return;
                    case 5:
                        Objects.requireNonNull(DockObserver.this);
                        Log.d("DLObserver", "sendDockIdleIntent()");
                        context.sendBroadcast(new Intent("android.intent.action.DOCK_IDLE").addFlags(1073741824));
                        DockObserver.sIsDockingUiShowing = true;
                        return;
                    case FalsingManager.VERSION /* 6 */:
                        DockObserver dockObserver3 = DockObserver.this;
                        Objects.requireNonNull(dockObserver3);
                        Log.d("DLObserver", "triggerChallengeWithDock");
                        ResultReceiver resultReceiver3 = (ResultReceiver) intent.getParcelableExtra("android.intent.extra.RESULT_RECEIVER");
                        if (resultReceiver3 != null) {
                            byte byteExtra3 = intent.getByteExtra(DockObserver.EXTRA_CHALLENGE_DOCK_ID, (byte) -1);
                            byte[] byteArrayExtra2 = intent.getByteArrayExtra(DockObserver.EXTRA_CHALLENGE_DATA);
                            if (byteArrayExtra2 == null || byteArrayExtra2.length <= 0 || byteExtra3 < 0) {
                                resultReceiver3.send(1, null);
                                return;
                            } else {
                                DockObserver.runOnBackgroundThread(new ChallengeWithDock(resultReceiver3, byteExtra3, byteArrayExtra2));
                                return;
                            }
                        } else {
                            return;
                        }
                    case 7:
                        StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("command=3, i=");
                        m3.append((int) intent.getByteExtra("fan_id", (byte) -1));
                        Log.d("DLObserver", m3.toString());
                        ResultReceiver resultReceiver4 = (ResultReceiver) intent.getParcelableExtra("android.intent.extra.RESULT_RECEIVER");
                        if (resultReceiver4 != null) {
                            DockObserver.runOnBackgroundThread(new GetFanSimpleInformation(intent.getByteExtra("fan_id", (byte) 0), resultReceiver4));
                            return;
                        }
                        return;
                    case '\b':
                        byte byteExtra4 = intent.getByteExtra("slot_mask", (byte) -1);
                        Log.d("DLObserver", "gWAD, mask=" + ((int) byteExtra4));
                        ResultReceiver resultReceiver5 = (ResultReceiver) intent.getParcelableExtra("android.intent.extra.RESULT_RECEIVER");
                        if (resultReceiver5 == null) {
                            return;
                        }
                        if (byteExtra4 == -1) {
                            resultReceiver5.send(1, null);
                            return;
                        } else {
                            DockObserver.runOnBackgroundThread(new GetWpcAuthDigests(resultReceiver5, byteExtra4));
                            return;
                        }
                    case '\t':
                        if (DockObserver.assertNotNull(DockObserver.this.mDockGestureController)) {
                            DockGestureController dockGestureController2 = DockObserver.this.mDockGestureController;
                            Objects.requireNonNull(dockGestureController2);
                            dockGestureController2.mTapAction = (PendingIntent) intent.getParcelableExtra("single_tap_action");
                            break;
                        }
                        break;
                    case '\n':
                        break;
                    case QSTileImpl.H.STALE /* 11 */:
                        DockObserver.this.onDockStateChanged(0);
                        if (DockObserver.assertNotNull(DockObserver.this.mDockGestureController)) {
                            DockObserver.this.mDockGestureController.stopMonitoring();
                            return;
                        }
                        return;
                    case KeyguardSliceProvider.ALARM_VISIBILITY_HOURS /* 12 */:
                        Objects.requireNonNull(DockObserver.this);
                        PowerManager powerManager = (PowerManager) context.getSystemService(PowerManager.class);
                        if (powerManager.isScreenOn()) {
                            powerManager.goToSleep(SystemClock.uptimeMillis());
                            return;
                        }
                        return;
                    case QS.VERSION /* 13 */:
                        DockObserver.this.onDockStateChanged(2);
                        if (DockObserver.assertNotNull(DockObserver.this.mDockGestureController)) {
                            DockObserver.this.mDockGestureController.stopMonitoring();
                            return;
                        }
                        return;
                    case 14:
                        DockObserver dockObserver4 = DockObserver.this;
                        Objects.requireNonNull(dockObserver4);
                        ResultReceiver resultReceiver6 = (ResultReceiver) intent.getParcelableExtra("android.intent.extra.RESULT_RECEIVER");
                        boolean booleanExtra = intent.getBooleanExtra("enabled", false);
                        ViewCompat$$ExternalSyntheticLambda0.m("configPhotoAction, enabled=", booleanExtra, "DLObserver");
                        DockGestureController dockGestureController3 = dockObserver4.mDockGestureController;
                        if (dockGestureController3 != null) {
                            dockGestureController3.mPhotoEnabled = booleanExtra;
                        }
                        if (!(resultReceiver6 == null || dockObserver4.mIndicationController == null)) {
                            dockObserver4.mPhotoAction = new DockObserver$$ExternalSyntheticLambda0(dockObserver4, resultReceiver6, 0);
                        }
                        DockObserver.this.runPhotoAction();
                        return;
                    case 15:
                        DockIndicationController dockIndicationController = DockObserver.this.mIndicationController;
                        if (dockIndicationController != null) {
                            dockIndicationController.mTopIconShowing = intent.getBooleanExtra(DockObserver.KEY_SHOWING, false);
                            dockIndicationController.updateVisibility();
                            return;
                        }
                        return;
                    case 16:
                        byte byteExtra5 = intent.getByteExtra("slot_number", (byte) -1);
                        short shortExtra = intent.getShortExtra("cert_offset", (short) -1);
                        short shortExtra2 = intent.getShortExtra("cert_length", (short) -1);
                        StringBuilder m4 = GridLayoutManager$$ExternalSyntheticOutline0.m("gWAC, num=", byteExtra5, ", offset=", shortExtra, ", length=");
                        m4.append((int) shortExtra2);
                        Log.d("DLObserver", m4.toString());
                        ResultReceiver resultReceiver7 = (ResultReceiver) intent.getParcelableExtra("android.intent.extra.RESULT_RECEIVER");
                        if (resultReceiver7 == null) {
                            return;
                        }
                        if (byteExtra5 == -1 || shortExtra == -1 || shortExtra2 == -1) {
                            resultReceiver7.send(1, null);
                            return;
                        } else {
                            DockObserver.runOnBackgroundThread(new GetWpcAuthCertificate(resultReceiver7, byteExtra5, shortExtra, shortExtra2));
                            return;
                        }
                    case 17:
                        Objects.requireNonNull(DockObserver.this);
                        Log.d("DLObserver", "sendDockActiveIntent()");
                        context.sendBroadcast(new Intent("android.intent.action.DOCK_ACTIVE").addFlags(1073741824));
                        DockObserver.sIsDockingUiShowing = false;
                        return;
                    case 18:
                        StringBuilder m5 = VendorAtomValue$$ExternalSyntheticOutline1.m("command=0, i=");
                        m5.append((int) intent.getByteExtra("fan_id", (byte) -1));
                        Log.d("DLObserver", m5.toString());
                        ResultReceiver resultReceiver8 = (ResultReceiver) intent.getParcelableExtra("android.intent.extra.RESULT_RECEIVER");
                        if (resultReceiver8 != null) {
                            DockObserver.runOnBackgroundThread(new GetFanInformation(intent.getByteExtra("fan_id", (byte) 0), resultReceiver8));
                            return;
                        }
                        return;
                    case 19:
                        byte byteExtra6 = intent.getByteExtra("slot_number", (byte) -1);
                        Log.d("DLObserver", "gWACR, num=" + ((int) byteExtra6));
                        ResultReceiver resultReceiver9 = (ResultReceiver) intent.getParcelableExtra("android.intent.extra.RESULT_RECEIVER");
                        if (resultReceiver9 != null) {
                            byte[] byteArrayExtra3 = intent.getByteArrayExtra("wpc_nonce");
                            if (byteArrayExtra3 == null || byteArrayExtra3.length <= 0) {
                                resultReceiver9.send(1, null);
                                return;
                            } else {
                                DockObserver.runOnBackgroundThread(new GetWpcAuthChallengeResponse(resultReceiver9, byteExtra6, byteArrayExtra3));
                                return;
                            }
                        } else {
                            return;
                        }
                    default:
                        return;
                }
                DockObserver.this.onDockStateChanged(1);
                if (DockObserver.assertNotNull(DockObserver.this.mDockGestureController)) {
                    DockGestureController dockGestureController4 = DockObserver.this.mDockGestureController;
                    Objects.requireNonNull(dockGestureController4);
                    dockGestureController4.mSettingsGear.setVisibility(4);
                    dockGestureController4.onDozingChanged(dockGestureController4.mStatusBarStateController.isDozing());
                    dockGestureController4.mStatusBarStateController.addCallback(dockGestureController4);
                    dockGestureController4.mKeyguardStateController.addCallback(dockGestureController4.mKeyguardMonitorCallback);
                }
            }
        }

        public final void registerReceiver(Context context) {
            if (!this.mListening) {
                UserHandle userHandle = UserHandle.ALL;
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(DockObserver.ACTION_GET_DOCK_INFO);
                intentFilter.addAction(DockObserver.ACTION_DOCK_UI_IDLE);
                intentFilter.addAction(DockObserver.ACTION_DOCK_UI_ACTIVE);
                intentFilter.addAction(DockObserver.ACTION_KEY_EXCHANGE);
                intentFilter.addAction(DockObserver.ACTION_CHALLENGE);
                intentFilter.addAction("com.google.android.systemui.dreamliner.dream");
                intentFilter.addAction("com.google.android.systemui.dreamliner.paired");
                intentFilter.addAction("com.google.android.systemui.dreamliner.pause");
                intentFilter.addAction("com.google.android.systemui.dreamliner.resume");
                intentFilter.addAction("com.google.android.systemui.dreamliner.undock");
                intentFilter.addAction("com.google.android.systemui.dreamliner.assistant_poodle");
                intentFilter.addAction("com.google.android.systemui.dreamliner.photo");
                intentFilter.addAction("com.google.android.systemui.dreamliner.photo_error");
                intentFilter.addAction("com.google.android.systemui.dreamliner.ACTION_GET_FAN_INFO");
                intentFilter.addAction("com.google.android.systemui.dreamliner.ACTION_GET_FAN_SIMPLE_INFO");
                intentFilter.addAction("com.google.android.systemui.dreamliner.ACTION_SET_FAN");
                intentFilter.addAction("com.google.android.systemui.dreamliner.ACTION_GET_FAN_LEVEL");
                intentFilter.addAction("com.google.android.systemui.dreamliner.ACTION_GET_WPC_DIGESTS");
                intentFilter.addAction("com.google.android.systemui.dreamliner.ACTION_GET_WPC_CERTIFICATE");
                intentFilter.addAction("com.google.android.systemui.dreamliner.ACTION_GET_WPC_CHALLENGE_RESPONSE");
                context.registerReceiverAsUser(this, userHandle, intentFilter, DockObserver.PERMISSION_WIRELESS_CHARGER_STATUS, null, 2);
                this.mListening = true;
            }
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public final class DreamlinerServiceConn implements ServiceConnection {
        public final Context mContext;

        @Override // android.content.ServiceConnection
        public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        }

        public DreamlinerServiceConn(Context context) {
            this.mContext = context;
        }

        @Override // android.content.ServiceConnection
        public final void onBindingDied(ComponentName componentName) {
            DockObserver dockObserver = DockObserver.this;
            Context context = this.mContext;
            String str = DockObserver.ACTION_START_DREAMLINER_CONTROL_SERVICE;
            dockObserver.stopDreamlinerService(context);
            DockObserver.sIsDockingUiShowing = false;
        }

        @Override // android.content.ServiceConnection
        public final void onServiceDisconnected(ComponentName componentName) {
            DockObserver dockObserver = DockObserver.this;
            Context context = this.mContext;
            String str = DockObserver.ACTION_START_DREAMLINER_CONTROL_SERVICE;
            Objects.requireNonNull(dockObserver);
            Log.d("DLObserver", "sendDockActiveIntent()");
            context.sendBroadcast(new Intent("android.intent.action.DOCK_ACTIVE").addFlags(1073741824));
        }
    }

    /* loaded from: classes.dex */
    public class GetDockInfo implements Runnable {
        public final ResultReceiver resultReceiver;

        public GetDockInfo(ResultReceiver resultReceiver) {
            this.resultReceiver = resultReceiver;
        }

        @Override // java.lang.Runnable
        public final void run() {
            WirelessCharger wirelessCharger = DockObserver.this.mWirelessCharger;
            if (wirelessCharger != null) {
                wirelessCharger.getInformation(new GetInformationCallback(this.resultReceiver));
            }
        }
    }

    /* loaded from: classes.dex */
    public class GetFanInformation implements Runnable {
        public final byte mFanId;
        public final ResultReceiver mResultReceiver;

        public GetFanInformation(byte b, ResultReceiver resultReceiver) {
            this.mFanId = b;
            this.mResultReceiver = resultReceiver;
        }

        @Override // java.lang.Runnable
        public final void run() {
            WirelessCharger wirelessCharger = DockObserver.this.mWirelessCharger;
            if (wirelessCharger != null) {
                byte b = this.mFanId;
                wirelessCharger.getFanInformation(b, new GetFanInformationCallback(b, this.mResultReceiver));
            }
        }
    }

    /* loaded from: classes.dex */
    public class GetFanSimpleInformation implements Runnable {
        public final byte mFanId;
        public final ResultReceiver mResultReceiver;

        public GetFanSimpleInformation(byte b, ResultReceiver resultReceiver) {
            this.mFanId = b;
            this.mResultReceiver = resultReceiver;
        }

        @Override // java.lang.Runnable
        public final void run() {
            WirelessCharger wirelessCharger = DockObserver.this.mWirelessCharger;
            if (wirelessCharger != null) {
                byte b = this.mFanId;
                wirelessCharger.getFanSimpleInformation(b, new GetFanSimpleInformationCallback(b, this.mResultReceiver));
            }
        }
    }

    /* loaded from: classes.dex */
    public class GetFeatures implements Runnable {
        public final long mChargerId;
        public final ResultReceiver mResultReceiver;

        public GetFeatures(ResultReceiver resultReceiver, long j) {
            this.mResultReceiver = resultReceiver;
            this.mChargerId = j;
        }

        @Override // java.lang.Runnable
        public final void run() {
            DockObserver dockObserver = DockObserver.this;
            WirelessCharger wirelessCharger = dockObserver.mWirelessCharger;
            if (wirelessCharger != null) {
                wirelessCharger.getFeatures(this.mChargerId, new GetFeaturesCallback(this.mResultReceiver));
            }
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public final class GetFeaturesCallback implements WirelessCharger.GetFeaturesCallback {
        public final ResultReceiver mResultReceiver;

        public GetFeaturesCallback(ResultReceiver resultReceiver) {
            this.mResultReceiver = resultReceiver;
        }
    }

    /* loaded from: classes.dex */
    public class GetWpcAuthCertificate implements Runnable {
        public final short mLength;
        public final short mOffset;
        public final ResultReceiver mResultReceiver;
        public final byte mSlotNum;

        public GetWpcAuthCertificate(ResultReceiver resultReceiver, byte b, short s, short s2) {
            this.mResultReceiver = resultReceiver;
            this.mSlotNum = b;
            this.mOffset = s;
            this.mLength = s2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            DockObserver dockObserver = DockObserver.this;
            WirelessCharger wirelessCharger = dockObserver.mWirelessCharger;
            if (wirelessCharger != null) {
                wirelessCharger.getWpcAuthCertificate(this.mSlotNum, this.mOffset, this.mLength, new GetWpcAuthCertificateCallback(this.mResultReceiver));
            }
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public final class GetWpcAuthCertificateCallback implements WirelessCharger.GetWpcAuthCertificateCallback {
        public final ResultReceiver mResultReceiver;

        public GetWpcAuthCertificateCallback(ResultReceiver resultReceiver) {
            this.mResultReceiver = resultReceiver;
        }
    }

    /* loaded from: classes.dex */
    public class GetWpcAuthChallengeResponse implements Runnable {
        public final ResultReceiver mResultReceiver;
        public final byte mSlotNum;
        public final byte[] mWpcNonce;

        public GetWpcAuthChallengeResponse(ResultReceiver resultReceiver, byte b, byte[] bArr) {
            this.mResultReceiver = resultReceiver;
            this.mSlotNum = b;
            this.mWpcNonce = bArr;
        }

        @Override // java.lang.Runnable
        public final void run() {
            DockObserver dockObserver = DockObserver.this;
            WirelessCharger wirelessCharger = dockObserver.mWirelessCharger;
            if (wirelessCharger != null) {
                wirelessCharger.getWpcAuthChallengeResponse(this.mSlotNum, this.mWpcNonce, new GetWpcAuthChallengeResponseCallback(this.mResultReceiver));
            }
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public final class GetWpcAuthChallengeResponseCallback implements WirelessCharger.GetWpcAuthChallengeResponseCallback {
        public final ResultReceiver mResultReceiver;

        public GetWpcAuthChallengeResponseCallback(ResultReceiver resultReceiver) {
            this.mResultReceiver = resultReceiver;
        }
    }

    /* loaded from: classes.dex */
    public class GetWpcAuthDigests implements Runnable {
        public final ResultReceiver mResultReceiver;
        public final byte mSlotMask;

        public GetWpcAuthDigests(ResultReceiver resultReceiver, byte b) {
            this.mResultReceiver = resultReceiver;
            this.mSlotMask = b;
        }

        @Override // java.lang.Runnable
        public final void run() {
            DockObserver dockObserver = DockObserver.this;
            WirelessCharger wirelessCharger = dockObserver.mWirelessCharger;
            if (wirelessCharger != null) {
                wirelessCharger.getWpcAuthDigests(this.mSlotMask, new GetWpcAuthDigestsCallback(this.mResultReceiver));
            }
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public final class GetWpcAuthDigestsCallback implements WirelessCharger.GetWpcAuthDigestsCallback {
        public final ResultReceiver mResultReceiver;

        public GetWpcAuthDigestsCallback(ResultReceiver resultReceiver) {
            this.mResultReceiver = resultReceiver;
        }
    }

    /* loaded from: classes.dex */
    public class IsDockPresent implements Runnable {
        public final Context context;

        public IsDockPresent(Context context) {
            this.context = context;
        }

        @Override // java.lang.Runnable
        public final void run() {
            DockObserver dockObserver = DockObserver.this;
            WirelessCharger wirelessCharger = dockObserver.mWirelessCharger;
            if (wirelessCharger != null) {
                wirelessCharger.asyncIsDockPresent(new IsDockPresentCallback(this.context));
            }
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public final class IsDockPresentCallback implements WirelessCharger.IsDockPresentCallback {
        public final Context mContext;

        public IsDockPresentCallback(Context context) {
            this.mContext = context;
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public final class KeyExchangeCallback implements WirelessCharger.KeyExchangeCallback {
        public final ResultReceiver mResultReceiver;

        public KeyExchangeCallback(ResultReceiver resultReceiver) {
            this.mResultReceiver = resultReceiver;
        }

        public final void onCallback(int i, byte b, ArrayList<Byte> arrayList) {
            ExifInterface$$ExternalSyntheticOutline1.m("KE() Result: ", i, "DLObserver");
            Bundle bundle = null;
            if (i == 0) {
                Log.d("DLObserver", "KE() response: pk=" + arrayList);
                ResultReceiver resultReceiver = this.mResultReceiver;
                DockObserver dockObserver = DockObserver.this;
                String str = DockObserver.ACTION_START_DREAMLINER_CONTROL_SERVICE;
                Objects.requireNonNull(dockObserver);
                if (arrayList != null && !arrayList.isEmpty()) {
                    byte[] convertArrayListToPrimitiveArray = DockObserver.convertArrayListToPrimitiveArray(arrayList);
                    bundle = new Bundle();
                    bundle.putByte("dock_id", b);
                    bundle.putByteArray("dock_public_key", convertArrayListToPrimitiveArray);
                }
                resultReceiver.send(0, bundle);
                return;
            }
            this.mResultReceiver.send(1, null);
        }
    }

    /* loaded from: classes.dex */
    public class KeyExchangeWithDock implements Runnable {
        public final byte[] publicKey;
        public final ResultReceiver resultReceiver;

        public KeyExchangeWithDock(ResultReceiver resultReceiver, byte[] bArr) {
            this.publicKey = bArr;
            this.resultReceiver = resultReceiver;
        }

        @Override // java.lang.Runnable
        public final void run() {
            DockObserver dockObserver = DockObserver.this;
            WirelessCharger wirelessCharger = dockObserver.mWirelessCharger;
            if (wirelessCharger != null) {
                wirelessCharger.keyExchange(this.publicKey, new KeyExchangeCallback(this.resultReceiver));
            }
        }
    }

    /* loaded from: classes.dex */
    public class SetFan implements Runnable {
        public final byte mFanId;
        public final byte mFanMode;
        public final int mFanRpm;

        public SetFan(byte b, byte b2, int i) {
            this.mFanId = b;
            this.mFanMode = b2;
            this.mFanRpm = i;
        }

        @Override // java.lang.Runnable
        public final void run() {
            WirelessCharger wirelessCharger = DockObserver.this.mWirelessCharger;
            if (wirelessCharger != null) {
                wirelessCharger.setFan(this.mFanId, this.mFanMode, this.mFanRpm, new SetFanCallback());
            }
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public static final class SetFanCallback implements WirelessCharger.SetFanCallback {
    }

    /* loaded from: classes.dex */
    public class SetFeatures implements Runnable {
        public final long mChargerId;
        public final long mFeature;
        public final ResultReceiver mResultReceiver;

        public SetFeatures(ResultReceiver resultReceiver, long j, long j2) {
            this.mResultReceiver = resultReceiver;
            this.mChargerId = j;
            this.mFeature = j2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            WirelessCharger wirelessCharger = DockObserver.this.mWirelessCharger;
            if (wirelessCharger != null) {
                wirelessCharger.setFeatures(this.mChargerId, this.mFeature, new CaptionsToggleImageButton$$ExternalSyntheticLambda0(this));
            }
        }
    }

    public final void stopDreamlinerService(Context context) {
        notifyForceEnabledAmbientDisplay(false);
        onDockStateChanged(0);
        try {
            if (this.mDreamlinerServiceConn != null) {
                if (assertNotNull(this.mDockGestureController)) {
                    this.mConfigurationController.removeCallback(this.mDockGestureController);
                    this.mDockGestureController.stopMonitoring();
                    this.mDockGestureController = null;
                }
                stopTracking();
                DreamlinerBroadcastReceiver dreamlinerBroadcastReceiver = this.mDreamlinerReceiver;
                Objects.requireNonNull(dreamlinerBroadcastReceiver);
                if (dreamlinerBroadcastReceiver.mListening) {
                    context.unregisterReceiver(dreamlinerBroadcastReceiver);
                    dreamlinerBroadcastReceiver.mListening = false;
                }
                context.unbindService(this.mDreamlinerServiceConn);
                this.mDreamlinerServiceConn = null;
            }
        } catch (IllegalArgumentException e) {
            Log.e("DLObserver", e.getMessage(), e);
        }
    }

    @VisibleForTesting
    public final void updateCurrentDockingStatus(Context context) {
        notifyForceEnabledAmbientDisplay(false);
        checkIsDockPresentIfNeeded(context);
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public static final class GetFanInformationCallback implements WirelessCharger.GetFanInformationCallback {
        public final byte mFanId;
        public final ResultReceiver mResultReceiver;

        public GetFanInformationCallback(byte b, ResultReceiver resultReceiver) {
            this.mFanId = b;
            this.mResultReceiver = resultReceiver;
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public static final class GetFanSimpleInformationCallback implements WirelessCharger.GetFanSimpleInformationCallback {
        public final byte mFanId;
        public final ResultReceiver mResultReceiver;

        public GetFanSimpleInformationCallback(byte b, ResultReceiver resultReceiver) {
            this.mFanId = b;
            this.mResultReceiver = resultReceiver;
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public final class GetInformationCallback implements WirelessCharger.GetInformationCallback {
        public final ResultReceiver mResultReceiver;

        public GetInformationCallback(ResultReceiver resultReceiver) {
            this.mResultReceiver = resultReceiver;
        }
    }

    public static boolean assertNotNull(DockGestureController dockGestureController) {
        if (dockGestureController != null) {
            return true;
        }
        Log.w("DLObserver", "DockGestureController is null");
        return false;
    }

    public static byte[] convertArrayListToPrimitiveArray(ArrayList arrayList) {
        if (arrayList == null || arrayList.isEmpty()) {
            return null;
        }
        int size = arrayList.size();
        byte[] bArr = new byte[size];
        for (int i = 0; i < size; i++) {
            bArr[i] = ((Byte) arrayList.get(i)).byteValue();
        }
        return bArr;
    }

    public static void notifyForceEnabledAmbientDisplay(boolean z) {
        IDreamManager asInterface = IDreamManager.Stub.asInterface(ServiceManager.checkService("dreams"));
        if (asInterface != null) {
            try {
                asInterface.forceAmbientDisplayEnabled(z);
            } catch (RemoteException unused) {
            }
        } else {
            Log.e("DLObserver", "DreamManager not found");
        }
    }

    public static void runOnBackgroundThread(Runnable runnable) {
        if (mSingleThreadExecutor == null) {
            mSingleThreadExecutor = Executors.newSingleThreadExecutor();
        }
        mSingleThreadExecutor.execute(runnable);
    }

    @Override // com.android.systemui.dock.DockManager
    public final void addAlignmentStateListener(KeyguardIndicationController$$ExternalSyntheticLambda0 keyguardIndicationController$$ExternalSyntheticLambda0) {
        Log.d("DLObserver", "add alignment listener: " + keyguardIndicationController$$ExternalSyntheticLambda0);
        if (!this.mAlignmentStateListeners.contains(keyguardIndicationController$$ExternalSyntheticLambda0)) {
            this.mAlignmentStateListeners.add(keyguardIndicationController$$ExternalSyntheticLambda0);
        }
    }

    @Override // com.android.systemui.dock.DockManager
    public final void addListener(DockManager.DockEventListener dockEventListener) {
        Log.d("DLObserver", "add listener: " + dockEventListener);
        if (!this.mClients.contains(dockEventListener)) {
            this.mClients.add(dockEventListener);
        }
        this.mMainExecutor.execute(new BubblesManager$5$$ExternalSyntheticLambda3(this, dockEventListener, 3));
    }

    public final void checkIsDockPresentIfNeeded(Context context) {
        if (this.mWirelessCharger != null) {
            Intent registerReceiver = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            boolean z = false;
            if (registerReceiver == null) {
                Log.i("DLObserver", "null battery intent when checking plugged status");
            } else {
                int intExtra = registerReceiver.getIntExtra("plugged", -1);
                ExifInterface$$ExternalSyntheticOutline1.m("plugged=", intExtra, "DLObserver");
                if (intExtra == 4) {
                    z = true;
                }
            }
            if (z) {
                runOnBackgroundThread(new IsDockPresent(context));
            }
        }
    }

    public final void dispatchDockEvent(DockManager.DockEventListener dockEventListener) {
        KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(VendorAtomValue$$ExternalSyntheticOutline1.m("onDockEvent mDockState = "), this.mDockState, "DLObserver");
        dockEventListener.onEvent(this.mDockState);
    }

    @Override // com.android.systemui.dock.DockManager
    public final boolean isDocked() {
        int i = this.mDockState;
        if (i == 1 || i == 2) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.dock.DockManager
    public final boolean isHidden() {
        if (this.mDockState == 2) {
            return true;
        }
        return false;
    }

    public final void notifyDreamlinerLatestFanLevel() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("notify l=");
        m.append(this.mFanLevel);
        m.append(", isDocked=");
        m.append(isDocked());
        Log.d("DLObserver", m.toString());
        if (isDocked()) {
            this.mContext.sendBroadcastAsUser(new Intent("com.google.android.systemui.dreamliner.ACTION_UPDATE_FAN_LEVEL").putExtra("fan_level", this.mFanLevel).addFlags(1073741824), UserHandle.CURRENT);
        }
    }

    public final void onDockStateChanged(int i) {
        if (this.mDockState != i) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("dock state changed from ");
            m.append(this.mDockState);
            m.append(" to ");
            m.append(i);
            Log.d("DLObserver", m.toString());
            int i2 = this.mDockState;
            this.mDockState = i;
            for (int i3 = 0; i3 < this.mClients.size(); i3++) {
                dispatchDockEvent((DockManager.DockEventListener) this.mClients.get(i3));
            }
            DockIndicationController dockIndicationController = this.mIndicationController;
            if (dockIndicationController != null) {
                boolean isDocked = isDocked();
                dockIndicationController.mDocking = isDocked;
                if (!isDocked) {
                    dockIndicationController.mTopIconShowing = false;
                    dockIndicationController.mShowPromo = false;
                }
                dockIndicationController.updateVisibility();
                dockIndicationController.updateLiveRegionIfNeeded();
            }
            if (i2 == 0 && i == 1) {
                notifyDreamlinerAlignStateChanged(this.mLastAlignState);
                notifyDreamlinerLatestFanLevel();
            }
        }
    }

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        if (intent != null) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("onReceive(); ");
            m.append(intent.getAction());
            Log.d("DLObserver", m.toString());
            String action = intent.getAction();
            Objects.requireNonNull(action);
            char c = 65535;
            switch (action.hashCode()) {
                case -1886648615:
                    if (action.equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1863595884:
                    if (action.equals("com.google.android.systemui.dreamliner.ACTION_SET_FEATURES")) {
                        c = 1;
                        break;
                    }
                    break;
                case 798292259:
                    if (action.equals("android.intent.action.BOOT_COMPLETED")) {
                        c = 2;
                        break;
                    }
                    break;
                case 882378784:
                    if (action.equals("com.google.android.systemui.dreamliner.ACTION_GET_FEATURES")) {
                        c = 3;
                        break;
                    }
                    break;
                case 1019184907:
                    if (action.equals("android.intent.action.ACTION_POWER_CONNECTED")) {
                        c = 4;
                        break;
                    }
                    break;
                case 1318602046:
                    if (action.equals(ACTION_REBIND_DOCK_SERVICE)) {
                        c = 5;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    stopDreamlinerService(context);
                    sIsDockingUiShowing = false;
                    return;
                case 1:
                    long longExtra = intent.getLongExtra("charger_id", -1L);
                    long longExtra2 = intent.getLongExtra("charger_feature", -1L);
                    ResultReceiver resultReceiver = (ResultReceiver) intent.getParcelableExtra("android.intent.extra.RESULT_RECEIVER");
                    Log.d("DLObserver", "sF, id=" + longExtra + ", feature=" + longExtra2);
                    if (resultReceiver == null) {
                        return;
                    }
                    if (longExtra == -1 || longExtra2 == -1) {
                        resultReceiver.send(1, null);
                        return;
                    } else {
                        runOnBackgroundThread(new SetFeatures(resultReceiver, longExtra, longExtra2));
                        return;
                    }
                case 2:
                case 5:
                    updateCurrentDockingStatus(context);
                    return;
                case 3:
                    long longExtra3 = intent.getLongExtra("charger_id", -1L);
                    Log.d("DLObserver", "gF, id=" + longExtra3);
                    ResultReceiver resultReceiver2 = (ResultReceiver) intent.getParcelableExtra("android.intent.extra.RESULT_RECEIVER");
                    if (resultReceiver2 == null) {
                        return;
                    }
                    if (longExtra3 == -1) {
                        resultReceiver2.send(1, null);
                        return;
                    } else {
                        runOnBackgroundThread(new GetFeatures(resultReceiver2, longExtra3));
                        return;
                    }
                case 4:
                    checkIsDockPresentIfNeeded(context);
                    return;
                default:
                    return;
            }
        }
    }

    public final void refreshFanLevel(Runnable runnable) {
        Log.d("DLObserver", "command=2");
        runOnBackgroundThread(new ExecutorUtils$$ExternalSyntheticLambda1(this, runnable, 6));
    }

    @Override // com.android.systemui.dock.DockManager
    public final void removeListener(DockManager.DockEventListener dockEventListener) {
        Log.d("DLObserver", "remove listener: " + dockEventListener);
        this.mClients.remove(dockEventListener);
    }

    public final void runPhotoAction() {
        boolean z;
        if (this.mLastAlignState == 0 && this.mPhotoAction != null) {
            DockIndicationController dockIndicationController = this.mIndicationController;
            Objects.requireNonNull(dockIndicationController);
            if (dockIndicationController.mDockPromo.getVisibility() == 0) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                this.mMainExecutor.executeDelayed(this.mPhotoAction, Duration.ofSeconds(3L).toMillis());
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v3, types: [com.android.systemui.statusbar.notification.interruption.NotificationInterruptSuppressor, com.google.android.systemui.dreamliner.DockObserver$2] */
    /* JADX WARN: Type inference failed for: r14v3, types: [com.google.android.systemui.dreamliner.DockObserver$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public DockObserver(final android.content.Context r8, com.google.android.systemui.dreamliner.WirelessCharger r9, com.android.systemui.broadcast.BroadcastDispatcher r10, com.android.systemui.plugins.statusbar.StatusBarStateController r11, com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider r12, com.android.systemui.statusbar.policy.ConfigurationController r13, com.android.systemui.util.concurrency.DelayableExecutor r14) {
        /*
            r7 = this;
            r7.<init>()
            com.google.android.systemui.dreamliner.DockObserver$DreamlinerBroadcastReceiver r0 = new com.google.android.systemui.dreamliner.DockObserver$DreamlinerBroadcastReceiver
            r0.<init>()
            r7.mDreamlinerReceiver = r0
            r0 = 0
            r7.mDockState = r0
            r0 = -1
            r7.mLastAlignState = r0
            r7.mFanLevel = r0
            com.google.android.systemui.dreamliner.DockObserver$2 r0 = new com.google.android.systemui.dreamliner.DockObserver$2
            r0.<init>()
            r7.mInterruptSuppressor = r0
            r7.mMainExecutor = r14
            r7.mContext = r8
            java.util.ArrayList r14 = new java.util.ArrayList
            r14.<init>()
            r7.mClients = r14
            java.util.ArrayList r14 = new java.util.ArrayList
            r14.<init>()
            r7.mAlignmentStateListeners = r14
            com.google.android.systemui.dreamliner.DockObserver$1 r14 = new com.google.android.systemui.dreamliner.DockObserver$1
            r14.<init>(r10)
            r7.mUserTracker = r14
            r7.mWirelessCharger = r9
            if (r9 != 0) goto L_0x003e
            java.lang.String r10 = "DLObserver"
            java.lang.String r14 = "wireless charger is null, check dock component."
            android.util.Log.i(r10, r14)
        L_0x003e:
            r7.mStatusBarStateController = r11
            android.content.IntentFilter r3 = new android.content.IntentFilter
            r3.<init>()
            java.lang.String r10 = "android.intent.action.ACTION_POWER_CONNECTED"
            r3.addAction(r10)
            java.lang.String r10 = "android.intent.action.ACTION_POWER_DISCONNECTED"
            r3.addAction(r10)
            java.lang.String r10 = "android.intent.action.BOOT_COMPLETED"
            r3.addAction(r10)
            java.lang.String r10 = "com.google.android.systemui.dreamliner.ACTION_REBIND_DOCK_SERVICE"
            r3.addAction(r10)
            java.lang.String r10 = "com.google.android.systemui.dreamliner.ACTION_GET_FEATURES"
            r3.addAction(r10)
            java.lang.String r10 = "com.google.android.systemui.dreamliner.ACTION_SET_FEATURES"
            r3.addAction(r10)
            r10 = 1000(0x3e8, float:1.401E-42)
            r3.setPriority(r10)
            r5 = 0
            r6 = 2
            java.lang.String r4 = "com.google.android.systemui.permission.WIRELESS_CHARGER_STATUS"
            r1 = r8
            r2 = r7
            r1.registerReceiver(r2, r3, r4, r5, r6)
            com.google.android.systemui.dreamliner.DockAlignmentController r8 = new com.google.android.systemui.dreamliner.DockAlignmentController
            r8.<init>(r9, r7)
            r7.mDockAlignmentController = r8
            r12.addSuppressor(r0)
            r7.mConfigurationController = r13
            r8 = 0
            r7.refreshFanLevel(r8)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.dreamliner.DockObserver.<init>(android.content.Context, com.google.android.systemui.dreamliner.WirelessCharger, com.android.systemui.broadcast.BroadcastDispatcher, com.android.systemui.plugins.statusbar.StatusBarStateController, com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider, com.android.systemui.statusbar.policy.ConfigurationController, com.android.systemui.util.concurrency.DelayableExecutor):void");
    }

    public final void notifyDreamlinerAlignStateChanged(int i) {
        if (isDocked()) {
            this.mContext.sendBroadcastAsUser(new Intent(ACTION_ALIGN_STATE_CHANGE).putExtra(EXTRA_ALIGN_STATE, i).addFlags(1073741824), UserHandle.CURRENT);
        }
    }
}
