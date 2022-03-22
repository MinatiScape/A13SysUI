package com.android.systemui.statusbar.phone;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.IActivityManager;
import android.app.SynchronousUserSwitchObserver;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserManager;
import android.telecom.TelecomManager;
import android.text.format.DateFormat;
import android.util.Log;
import com.android.keyguard.KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda7;
import com.android.settingslib.wifi.AccessPoint$$ExternalSyntheticLambda0;
import com.android.settingslib.wifi.AccessPoint$$ExternalSyntheticLambda1;
import com.android.systemui.ImageWallpaper$GLEngine$$ExternalSyntheticLambda0;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.privacy.PrivacyItem;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.privacy.PrivacyType;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.qs.tiles.RotationLockTile;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.PhoneStatusBarPolicy;
import com.android.systemui.statusbar.policy.BluetoothController;
import com.android.systemui.statusbar.policy.CastController;
import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.HotspotController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.LocationController;
import com.android.systemui.statusbar.policy.NextAlarmController;
import com.android.systemui.statusbar.policy.RotationLockController;
import com.android.systemui.statusbar.policy.SensorPrivacyController;
import com.android.systemui.statusbar.policy.UserInfoController;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.user.CreateUserActivity$$ExternalSyntheticLambda1;
import com.android.systemui.user.CreateUserActivity$$ExternalSyntheticLambda2;
import com.android.systemui.util.RingerModeTracker;
import com.android.systemui.util.time.DateFormatUtil;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda15;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executor;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
/* loaded from: classes.dex */
public final class PhoneStatusBarPolicy implements BluetoothController.Callback, CommandQueue.Callbacks, RotationLockController.RotationLockControllerCallback, DataSaverController.Listener, ZenModeController.Callback, DeviceProvisionedController.DeviceProvisionedListener, KeyguardStateController.Callback, PrivacyItemController.Callback, LocationController.LocationChangeCallback, RecordingController.RecordingStateChangeCallback {
    public static final boolean DEBUG = Log.isLoggable("PhoneStatusBarPolicy", 3);
    public static final int LOCATION_STATUS_ICON_ID = PrivacyType.TYPE_LOCATION.getIconId();
    public final AlarmManager mAlarmManager;
    public BluetoothController mBluetooth;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public final CastController mCast;
    public final CommandQueue mCommandQueue;
    public boolean mCurrentUserSetup;
    public final DataSaverController mDataSaver;
    public final DateFormatUtil mDateFormatUtil;
    public final DevicePolicyManager mDevicePolicyManager;
    public final int mDisplayId;
    public final HotspotController mHotspot;
    public final IActivityManager mIActivityManager;
    public final StatusBarIconController mIconController;
    public final KeyguardStateController mKeyguardStateController;
    public final LocationController mLocationController;
    public boolean mMuteVisible;
    public AlarmManager.AlarmClockInfo mNextAlarm;
    public final NextAlarmController mNextAlarmController;
    public final PrivacyItemController mPrivacyItemController;
    public final PrivacyLogger mPrivacyLogger;
    public final DeviceProvisionedController mProvisionedController;
    public final RecordingController mRecordingController;
    public final Resources mResources;
    public final RingerModeTracker mRingerModeTracker;
    public final RotationLockController mRotationLockController;
    public final SensorPrivacyController mSensorPrivacyController;
    public final SharedPreferences mSharedPreferences;
    public final String mSlotAlarmClock;
    public final String mSlotBluetooth;
    public final String mSlotCamera;
    public final String mSlotCast;
    public final String mSlotDataSaver;
    public final String mSlotHeadset;
    public final String mSlotHotspot;
    public final String mSlotLocation;
    public final String mSlotManagedProfile;
    public final String mSlotMicrophone;
    public final String mSlotMute;
    public final String mSlotRotate;
    public final String mSlotScreenRecord;
    public final String mSlotSensorsOff;
    public final String mSlotTty;
    public final String mSlotVibrate;
    public final String mSlotZen;
    public final TelecomManager mTelecomManager;
    public final Executor mUiBgExecutor;
    public final UserInfoController mUserInfoController;
    public final UserManager mUserManager;
    public boolean mVibrateVisible;
    public final ZenModeController mZenController;
    public boolean mZenVisible;
    public final Handler mHandler = new Handler();
    public boolean mManagedProfileIconVisible = false;
    public final AnonymousClass1 mUserSwitchListener = new AnonymousClass1();
    public final AnonymousClass2 mHotspotCallback = new HotspotController.Callback() { // from class: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy.2
        @Override // com.android.systemui.statusbar.policy.HotspotController.Callback
        public final void onHotspotChanged(boolean z, int i) {
            PhoneStatusBarPolicy phoneStatusBarPolicy = PhoneStatusBarPolicy.this;
            phoneStatusBarPolicy.mIconController.setIconVisibility(phoneStatusBarPolicy.mSlotHotspot, z);
        }
    };
    public final AnonymousClass3 mCastCallback = new CastController.Callback() { // from class: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy.3
        /* JADX WARN: Removed duplicated region for block: B:5:0x0016  */
        @Override // com.android.systemui.statusbar.policy.CastController.Callback
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void onCastDevicesChanged() {
            /*
                r6 = this;
                com.android.systemui.statusbar.phone.PhoneStatusBarPolicy r6 = com.android.systemui.statusbar.phone.PhoneStatusBarPolicy.this
                java.util.Objects.requireNonNull(r6)
                com.android.systemui.statusbar.policy.CastController r0 = r6.mCast
                java.util.ArrayList r0 = r0.getCastDevices()
                java.util.Iterator r0 = r0.iterator()
            L_0x000f:
                boolean r1 = r0.hasNext()
                r2 = 1
                if (r1 == 0) goto L_0x0025
                java.lang.Object r1 = r0.next()
                com.android.systemui.statusbar.policy.CastController$CastDevice r1 = (com.android.systemui.statusbar.policy.CastController.CastDevice) r1
                int r1 = r1.state
                if (r1 == r2) goto L_0x0023
                r3 = 2
                if (r1 != r3) goto L_0x000f
            L_0x0023:
                r0 = r2
                goto L_0x0026
            L_0x0025:
                r0 = 0
            L_0x0026:
                boolean r1 = com.android.systemui.statusbar.phone.PhoneStatusBarPolicy.DEBUG
                java.lang.String r3 = "PhoneStatusBarPolicy"
                if (r1 == 0) goto L_0x0041
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>()
                java.lang.String r5 = "updateCast: isCasting: "
                r4.append(r5)
                r4.append(r0)
                java.lang.String r4 = r4.toString()
                android.util.Log.v(r3, r4)
            L_0x0041:
                android.os.Handler r4 = r6.mHandler
                com.android.systemui.statusbar.phone.PhoneStatusBarPolicy$7 r5 = r6.mRemoveCastIconRunnable
                r4.removeCallbacks(r5)
                if (r0 == 0) goto L_0x006d
                com.android.systemui.screenrecord.RecordingController r0 = r6.mRecordingController
                boolean r0 = r0.isRecording()
                if (r0 != 0) goto L_0x006d
                com.android.systemui.statusbar.phone.StatusBarIconController r0 = r6.mIconController
                java.lang.String r1 = r6.mSlotCast
                r3 = 2131232675(0x7f0807a3, float:1.8081466E38)
                android.content.res.Resources r4 = r6.mResources
                r5 = 2131951687(0x7f130047, float:1.9539796E38)
                java.lang.String r4 = r4.getString(r5)
                r0.setIcon(r1, r3, r4)
                com.android.systemui.statusbar.phone.StatusBarIconController r0 = r6.mIconController
                java.lang.String r6 = r6.mSlotCast
                r0.setIconVisibility(r6, r2)
                goto L_0x007e
            L_0x006d:
                if (r1 == 0) goto L_0x0075
                java.lang.String r0 = "updateCast: hiding icon in 3 sec..."
                android.util.Log.v(r3, r0)
            L_0x0075:
                android.os.Handler r0 = r6.mHandler
                com.android.systemui.statusbar.phone.PhoneStatusBarPolicy$7 r6 = r6.mRemoveCastIconRunnable
                r1 = 3000(0xbb8, double:1.482E-320)
                r0.postDelayed(r6, r1)
            L_0x007e:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy.AnonymousClass3.onCastDevicesChanged():void");
        }
    };
    public final AnonymousClass4 mNextAlarmCallback = new NextAlarmController.NextAlarmChangeCallback() { // from class: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy.4
        @Override // com.android.systemui.statusbar.policy.NextAlarmController.NextAlarmChangeCallback
        public final void onNextAlarmChanged(AlarmManager.AlarmClockInfo alarmClockInfo) {
            PhoneStatusBarPolicy phoneStatusBarPolicy = PhoneStatusBarPolicy.this;
            phoneStatusBarPolicy.mNextAlarm = alarmClockInfo;
            phoneStatusBarPolicy.updateAlarm();
        }
    };
    public final AnonymousClass5 mSensorPrivacyListener = new AnonymousClass5();
    public AnonymousClass6 mIntentReceiver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy.6
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            boolean z;
            boolean z2;
            int i;
            int i2;
            String action = intent.getAction();
            Objects.requireNonNull(action);
            char c = 65535;
            switch (action.hashCode()) {
                case -1676458352:
                    if (action.equals("android.intent.action.HEADSET_PLUG")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1238404651:
                    if (action.equals("android.intent.action.MANAGED_PROFILE_UNAVAILABLE")) {
                        c = 1;
                        break;
                    }
                    break;
                case -864107122:
                    if (action.equals("android.intent.action.MANAGED_PROFILE_AVAILABLE")) {
                        c = 2;
                        break;
                    }
                    break;
                case -229777127:
                    if (action.equals("android.intent.action.SIM_STATE_CHANGED")) {
                        c = 3;
                        break;
                    }
                    break;
                case 1051344550:
                    if (action.equals("android.telecom.action.CURRENT_TTY_MODE_CHANGED")) {
                        c = 4;
                        break;
                    }
                    break;
                case 1051477093:
                    if (action.equals("android.intent.action.MANAGED_PROFILE_REMOVED")) {
                        c = 5;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    PhoneStatusBarPolicy phoneStatusBarPolicy = PhoneStatusBarPolicy.this;
                    Objects.requireNonNull(phoneStatusBarPolicy);
                    if (intent.getIntExtra("state", 0) != 0) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (intent.getIntExtra("microphone", 0) != 0) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    if (z) {
                        Resources resources = phoneStatusBarPolicy.mResources;
                        if (z2) {
                            i = 2131951813;
                        } else {
                            i = 2131951812;
                        }
                        String string = resources.getString(i);
                        StatusBarIconController statusBarIconController = phoneStatusBarPolicy.mIconController;
                        String str = phoneStatusBarPolicy.mSlotHeadset;
                        if (z2) {
                            i2 = 2131232682;
                        } else {
                            i2 = 2131232681;
                        }
                        statusBarIconController.setIcon(str, i2, string);
                        phoneStatusBarPolicy.mIconController.setIconVisibility(phoneStatusBarPolicy.mSlotHeadset, true);
                        return;
                    }
                    phoneStatusBarPolicy.mIconController.setIconVisibility(phoneStatusBarPolicy.mSlotHeadset, false);
                    return;
                case 1:
                case 2:
                case 5:
                    PhoneStatusBarPolicy.this.updateManagedProfile();
                    return;
                case 3:
                    intent.getBooleanExtra("rebroadcastOnUnlock", false);
                    return;
                case 4:
                    PhoneStatusBarPolicy.this.updateTTY(intent.getIntExtra("android.telecom.extra.CURRENT_TTY_MODE", 0));
                    return;
                default:
                    return;
            }
        }
    };
    public AnonymousClass7 mRemoveCastIconRunnable = new Runnable() { // from class: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy.7
        @Override // java.lang.Runnable
        public final void run() {
            if (PhoneStatusBarPolicy.DEBUG) {
                Log.v("PhoneStatusBarPolicy", "updateCast: hiding icon NOW");
            }
            PhoneStatusBarPolicy phoneStatusBarPolicy = PhoneStatusBarPolicy.this;
            phoneStatusBarPolicy.mIconController.setIconVisibility(phoneStatusBarPolicy.mSlotCast, false);
        }
    };

    /* renamed from: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends SynchronousUserSwitchObserver {
        public static final /* synthetic */ int $r8$clinit = 0;

        public AnonymousClass1() {
        }

        public final void onUserSwitchComplete(int i) throws RemoteException {
            PhoneStatusBarPolicy.this.mHandler.post(new AccessPoint$$ExternalSyntheticLambda0(this, 8));
        }

        public final void onUserSwitching(int i) throws RemoteException {
            PhoneStatusBarPolicy.this.mHandler.post(new AccessPoint$$ExternalSyntheticLambda1(this, 6));
        }
    }

    /* renamed from: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy$5  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass5 implements SensorPrivacyController.OnSensorPrivacyChangedListener {
        public AnonymousClass5() {
        }

        @Override // com.android.systemui.statusbar.policy.SensorPrivacyController.OnSensorPrivacyChangedListener
        public final void onSensorPrivacyChanged(final boolean z) {
            PhoneStatusBarPolicy.this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy$5$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStatusBarPolicy.AnonymousClass5 r0 = PhoneStatusBarPolicy.AnonymousClass5.this;
                    boolean z2 = z;
                    Objects.requireNonNull(r0);
                    PhoneStatusBarPolicy phoneStatusBarPolicy = PhoneStatusBarPolicy.this;
                    phoneStatusBarPolicy.mIconController.setIconVisibility(phoneStatusBarPolicy.mSlotSensorsOff, z2);
                }
            });
        }
    }

    /* JADX WARN: Type inference failed for: r2v3, types: [com.android.systemui.statusbar.phone.PhoneStatusBarPolicy$2] */
    /* JADX WARN: Type inference failed for: r2v4, types: [com.android.systemui.statusbar.phone.PhoneStatusBarPolicy$3] */
    /* JADX WARN: Type inference failed for: r2v5, types: [com.android.systemui.statusbar.phone.PhoneStatusBarPolicy$4] */
    /* JADX WARN: Type inference failed for: r2v7, types: [com.android.systemui.statusbar.phone.PhoneStatusBarPolicy$6] */
    /* JADX WARN: Type inference failed for: r2v8, types: [com.android.systemui.statusbar.phone.PhoneStatusBarPolicy$7] */
    public PhoneStatusBarPolicy(StatusBarIconController statusBarIconController, CommandQueue commandQueue, BroadcastDispatcher broadcastDispatcher, Executor executor, Resources resources, CastController castController, HotspotController hotspotController, BluetoothController bluetoothController, NextAlarmController nextAlarmController, UserInfoController userInfoController, RotationLockController rotationLockController, DataSaverController dataSaverController, ZenModeController zenModeController, DeviceProvisionedController deviceProvisionedController, KeyguardStateController keyguardStateController, LocationController locationController, SensorPrivacyController sensorPrivacyController, IActivityManager iActivityManager, AlarmManager alarmManager, UserManager userManager, DevicePolicyManager devicePolicyManager, RecordingController recordingController, TelecomManager telecomManager, int i, SharedPreferences sharedPreferences, DateFormatUtil dateFormatUtil, RingerModeTracker ringerModeTracker, PrivacyItemController privacyItemController, PrivacyLogger privacyLogger) {
        this.mIconController = statusBarIconController;
        this.mCommandQueue = commandQueue;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mResources = resources;
        this.mCast = castController;
        this.mHotspot = hotspotController;
        this.mBluetooth = bluetoothController;
        this.mNextAlarmController = nextAlarmController;
        this.mAlarmManager = alarmManager;
        this.mUserInfoController = userInfoController;
        this.mIActivityManager = iActivityManager;
        this.mUserManager = userManager;
        this.mDevicePolicyManager = devicePolicyManager;
        this.mRotationLockController = rotationLockController;
        this.mDataSaver = dataSaverController;
        this.mZenController = zenModeController;
        this.mProvisionedController = deviceProvisionedController;
        this.mKeyguardStateController = keyguardStateController;
        this.mLocationController = locationController;
        this.mPrivacyItemController = privacyItemController;
        this.mSensorPrivacyController = sensorPrivacyController;
        this.mRecordingController = recordingController;
        this.mUiBgExecutor = executor;
        this.mTelecomManager = telecomManager;
        this.mRingerModeTracker = ringerModeTracker;
        this.mPrivacyLogger = privacyLogger;
        this.mSlotCast = resources.getString(17041539);
        this.mSlotHotspot = resources.getString(17041546);
        this.mSlotBluetooth = resources.getString(17041536);
        this.mSlotTty = resources.getString(17041564);
        this.mSlotZen = resources.getString(17041568);
        this.mSlotMute = resources.getString(17041552);
        this.mSlotVibrate = resources.getString(17041565);
        this.mSlotAlarmClock = resources.getString(17041534);
        this.mSlotManagedProfile = resources.getString(17041549);
        this.mSlotRotate = resources.getString(17041557);
        this.mSlotHeadset = resources.getString(17041545);
        this.mSlotDataSaver = resources.getString(17041543);
        this.mSlotLocation = resources.getString(17041548);
        this.mSlotMicrophone = resources.getString(17041550);
        this.mSlotCamera = resources.getString(17041538);
        this.mSlotSensorsOff = resources.getString(17041560);
        this.mSlotScreenRecord = resources.getString(17041558);
        this.mDisplayId = i;
        this.mSharedPreferences = sharedPreferences;
        this.mDateFormatUtil = dateFormatUtil;
    }

    public final void updateTTY(int i) {
        boolean z;
        if (i != 0) {
            z = true;
        } else {
            z = false;
        }
        boolean z2 = DEBUG;
        if (z2) {
            Log.v("PhoneStatusBarPolicy", "updateTTY: enabled: " + z);
        }
        if (z) {
            if (z2) {
                Log.v("PhoneStatusBarPolicy", "updateTTY: set TTY on");
            }
            this.mIconController.setIcon(this.mSlotTty, 2131232696, this.mResources.getString(2131951815));
            this.mIconController.setIconVisibility(this.mSlotTty, true);
            return;
        }
        if (z2) {
            Log.v("PhoneStatusBarPolicy", "updateTTY: set TTY off");
        }
        this.mIconController.setIconVisibility(this.mSlotTty, false);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void appTransitionStarting(int i, long j, long j2, boolean z) {
        if (this.mDisplayId == i) {
            updateManagedProfile();
        }
    }

    @Override // com.android.systemui.screenrecord.RecordingController.RecordingStateChangeCallback
    public final void onCountdown(long j) {
        if (DEBUG) {
            Log.d("PhoneStatusBarPolicy", "screenrecord: countdown " + j);
        }
        int floorDiv = (int) Math.floorDiv(j + 500, 1000);
        int i = 2131232691;
        String num = Integer.toString(floorDiv);
        if (floorDiv == 1) {
            i = 2131232692;
        } else if (floorDiv == 2) {
            i = 2131232693;
        } else if (floorDiv == 3) {
            i = 2131232694;
        }
        this.mIconController.setIcon(this.mSlotScreenRecord, i, num);
        this.mIconController.setIconVisibility(this.mSlotScreenRecord, true);
        this.mIconController.setIconAccessibilityLiveRegion(this.mSlotScreenRecord, 2);
    }

    @Override // com.android.systemui.screenrecord.RecordingController.RecordingStateChangeCallback
    public final void onCountdownEnd() {
        if (DEBUG) {
            Log.d("PhoneStatusBarPolicy", "screenrecord: hiding icon during countdown");
        }
        this.mHandler.post(new ImageWallpaper$GLEngine$$ExternalSyntheticLambda0(this, 5));
        this.mHandler.post(new BubbleStackView$$ExternalSyntheticLambda15(this, 5));
    }

    @Override // com.android.systemui.statusbar.policy.DataSaverController.Listener
    public final void onDataSaverChanged(boolean z) {
        this.mIconController.setIconVisibility(this.mSlotDataSaver, z);
    }

    @Override // com.android.systemui.statusbar.policy.LocationController.LocationChangeCallback
    public final void onLocationActiveChanged() {
        PrivacyItemController privacyItemController = this.mPrivacyItemController;
        Objects.requireNonNull(privacyItemController);
        if (privacyItemController.locationAvailable) {
            return;
        }
        if (this.mLocationController.isLocationActive()) {
            this.mIconController.setIconVisibility(this.mSlotLocation, true);
        } else {
            this.mIconController.setIconVisibility(this.mSlotLocation, false);
        }
    }

    @Override // com.android.systemui.screenrecord.RecordingController.RecordingStateChangeCallback
    public final void onRecordingEnd() {
        if (DEBUG) {
            Log.d("PhoneStatusBarPolicy", "screenrecord: hiding icon");
        }
        this.mHandler.post(new KeyguardUpdateMonitor$$ExternalSyntheticLambda7(this, 2));
    }

    @Override // com.android.systemui.screenrecord.RecordingController.RecordingStateChangeCallback
    public final void onRecordingStart() {
        if (DEBUG) {
            Log.d("PhoneStatusBarPolicy", "screenrecord: showing icon");
        }
        this.mIconController.setIcon(this.mSlotScreenRecord, 2131232691, this.mResources.getString(2131953211));
        this.mHandler.post(new CreateUserActivity$$ExternalSyntheticLambda1(this, 3));
    }

    @Override // com.android.systemui.statusbar.policy.RotationLockController.RotationLockControllerCallback
    public final void onRotationLockStateChanged(boolean z) {
        boolean z2;
        RotationLockController rotationLockController = this.mRotationLockController;
        Resources resources = this.mResources;
        int i = RotationLockTile.$r8$clinit;
        int rotationLockOrientation = rotationLockController.getRotationLockOrientation();
        if (rotationLockOrientation != 0 ? rotationLockOrientation == 2 : resources.getConfiguration().orientation == 2) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (z) {
            if (z2) {
                this.mIconController.setIcon(this.mSlotRotate, 2131232690, this.mResources.getString(2131951806));
            } else {
                this.mIconController.setIcon(this.mSlotRotate, 2131232689, this.mResources.getString(2131951805));
            }
            this.mIconController.setIconVisibility(this.mSlotRotate, true);
            return;
        }
        this.mIconController.setIconVisibility(this.mSlotRotate, false);
    }

    @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController.DeviceProvisionedListener
    public final void onUserSetupChanged() {
        boolean isCurrentUserSetup = this.mProvisionedController.isCurrentUserSetup();
        if (this.mCurrentUserSetup != isCurrentUserSetup) {
            this.mCurrentUserSetup = isCurrentUserSetup;
            updateAlarm();
        }
    }

    public final void updateAlarm() {
        boolean z;
        boolean z2;
        int i;
        String str;
        String str2;
        AlarmManager.AlarmClockInfo nextAlarmClock = this.mAlarmManager.getNextAlarmClock(-2);
        boolean z3 = true;
        if (nextAlarmClock == null || nextAlarmClock.getTriggerTime() <= 0) {
            z = false;
        } else {
            z = true;
        }
        if (this.mZenController.getZen() == 2) {
            z2 = true;
        } else {
            z2 = false;
        }
        StatusBarIconController statusBarIconController = this.mIconController;
        String str3 = this.mSlotAlarmClock;
        if (z2) {
            i = 2131232673;
        } else {
            i = 2131232672;
        }
        if (this.mNextAlarm == null) {
            str = this.mResources.getString(2131953314);
        } else {
            DateFormatUtil dateFormatUtil = this.mDateFormatUtil;
            Objects.requireNonNull(dateFormatUtil);
            if (DateFormat.is24HourFormat(dateFormatUtil.mContext, ActivityManager.getCurrentUser())) {
                str2 = "EHm";
            } else {
                str2 = "Ehma";
            }
            str = this.mResources.getString(2131951783, DateFormat.format(DateFormat.getBestDateTimePattern(Locale.getDefault(), str2), this.mNextAlarm.getTriggerTime()).toString());
        }
        statusBarIconController.setIcon(str3, i, str);
        StatusBarIconController statusBarIconController2 = this.mIconController;
        String str4 = this.mSlotAlarmClock;
        if (!this.mCurrentUserSetup || !z) {
            z3 = false;
        }
        statusBarIconController2.setIconVisibility(str4, z3);
    }

    public final void updateBluetooth() {
        boolean z;
        String string = this.mResources.getString(2131951785);
        BluetoothController bluetoothController = this.mBluetooth;
        if (bluetoothController == null || !bluetoothController.isBluetoothConnected() || (!this.mBluetooth.isBluetoothAudioActive() && this.mBluetooth.isBluetoothAudioProfileOnly())) {
            z = false;
        } else {
            string = this.mResources.getString(2131951681);
            z = this.mBluetooth.isBluetoothEnabled();
        }
        this.mIconController.setIcon(this.mSlotBluetooth, 2131232676, string);
        this.mIconController.setIconVisibility(this.mSlotBluetooth, z);
    }

    public final void updateManagedProfile() {
        this.mUiBgExecutor.execute(new CreateUserActivity$$ExternalSyntheticLambda2(this, 3));
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0079, code lost:
        if (r0.intValue() == 0) goto L_0x007d;
     */
    /* JADX WARN: Removed duplicated region for block: B:28:0x007f  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x008a  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0097  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00a4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateVolumeZen() {
        /*
            r8 = this;
            com.android.systemui.statusbar.policy.ZenModeController r0 = r8.mZenController
            int r0 = r0.getZen()
            android.content.SharedPreferences r1 = r8.mSharedPreferences
            android.content.Intent r2 = com.android.systemui.qs.tiles.DndTile.ZEN_SETTINGS
            java.lang.String r2 = "DndTileVisible"
            r3 = 0
            boolean r1 = r1.getBoolean(r2, r3)
            r2 = 2131232678(0x7f0807a6, float:1.8081472E38)
            r4 = 1
            if (r1 != 0) goto L_0x0042
            android.content.SharedPreferences r1 = r8.mSharedPreferences
            java.lang.String r5 = "DndTileCombinedIcon"
            boolean r1 = r1.getBoolean(r5, r3)
            if (r1 == 0) goto L_0x0022
            goto L_0x0042
        L_0x0022:
            r1 = 2
            if (r0 != r1) goto L_0x002f
            android.content.res.Resources r1 = r8.mResources
            r5 = 2131952473(0x7f130359, float:1.954139E38)
            java.lang.String r1 = r1.getString(r5)
            goto L_0x003a
        L_0x002f:
            if (r0 != r4) goto L_0x003d
            android.content.res.Resources r1 = r8.mResources
            r5 = 2131952476(0x7f13035c, float:1.9541396E38)
            java.lang.String r1 = r1.getString(r5)
        L_0x003a:
            r5 = r1
            r1 = r4
            goto L_0x0050
        L_0x003d:
            r1 = 0
            r5 = r1
            r1 = r3
            r2 = r1
            goto L_0x0050
        L_0x0042:
            if (r0 == 0) goto L_0x0046
            r1 = r4
            goto L_0x0047
        L_0x0046:
            r1 = r3
        L_0x0047:
            android.content.res.Resources r5 = r8.mResources
            r6 = 2131953107(0x7f1305d3, float:1.9542676E38)
            java.lang.String r5 = r5.getString(r6)
        L_0x0050:
            com.android.systemui.statusbar.policy.ZenModeController r6 = r8.mZenController
            android.app.NotificationManager$Policy r6 = r6.getConsolidatedPolicy()
            boolean r0 = android.service.notification.ZenModeConfig.isZenOverridingRinger(r0, r6)
            if (r0 != 0) goto L_0x007c
            com.android.systemui.util.RingerModeTracker r0 = r8.mRingerModeTracker
            com.android.systemui.util.RingerModeLiveData r0 = r0.getRingerModeInternal()
            java.util.Objects.requireNonNull(r0)
            java.lang.Integer r0 = r0.getValue()
            if (r0 == 0) goto L_0x007c
            int r6 = r0.intValue()
            if (r6 != r4) goto L_0x0075
            r7 = r4
            r4 = r3
            r3 = r7
            goto L_0x007d
        L_0x0075:
            int r0 = r0.intValue()
            if (r0 != 0) goto L_0x007c
            goto L_0x007d
        L_0x007c:
            r4 = r3
        L_0x007d:
            if (r1 == 0) goto L_0x0086
            com.android.systemui.statusbar.phone.StatusBarIconController r0 = r8.mIconController
            java.lang.String r6 = r8.mSlotZen
            r0.setIcon(r6, r2, r5)
        L_0x0086:
            boolean r0 = r8.mZenVisible
            if (r1 == r0) goto L_0x0093
            com.android.systemui.statusbar.phone.StatusBarIconController r0 = r8.mIconController
            java.lang.String r2 = r8.mSlotZen
            r0.setIconVisibility(r2, r1)
            r8.mZenVisible = r1
        L_0x0093:
            boolean r0 = r8.mVibrateVisible
            if (r3 == r0) goto L_0x00a0
            com.android.systemui.statusbar.phone.StatusBarIconController r0 = r8.mIconController
            java.lang.String r1 = r8.mSlotVibrate
            r0.setIconVisibility(r1, r3)
            r8.mVibrateVisible = r3
        L_0x00a0:
            boolean r0 = r8.mMuteVisible
            if (r4 == r0) goto L_0x00ad
            com.android.systemui.statusbar.phone.StatusBarIconController r0 = r8.mIconController
            java.lang.String r1 = r8.mSlotMute
            r0.setIconVisibility(r1, r4)
            r8.mMuteVisible = r4
        L_0x00ad:
            r8.updateAlarm()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy.updateVolumeZen():void");
    }

    @Override // com.android.systemui.privacy.PrivacyItemController.Callback
    public final void onPrivacyItemsChanged(List<PrivacyItem> list) {
        List<PrivacyItem> list2;
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        for (PrivacyItem privacyItem : list) {
            if (privacyItem != null) {
                int ordinal = privacyItem.privacyType.ordinal();
                if (ordinal == 0) {
                    z = true;
                } else if (ordinal == 1) {
                    z2 = true;
                } else if (ordinal == 2) {
                    z3 = true;
                }
            } else {
                Log.e("PhoneStatusBarPolicy", "updatePrivacyItems - null item found");
                StringWriter stringWriter = new StringWriter();
                PrivacyItemController privacyItemController = this.mPrivacyItemController;
                PrintWriter printWriter = new PrintWriter(stringWriter);
                Objects.requireNonNull(privacyItemController);
                printWriter.println("PrivacyItemController state:");
                KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0.m(privacyItemController.listening, "  Listening: ", printWriter);
                printWriter.println(Intrinsics.stringPlus("  Current user ids: ", privacyItemController.currentUserIds));
                printWriter.println("  Privacy Items:");
                synchronized (privacyItemController) {
                    list2 = CollectionsKt___CollectionsKt.toList(privacyItemController.privacyList);
                }
                for (PrivacyItem privacyItem2 : list2) {
                    printWriter.print("    ");
                    printWriter.println(privacyItem2.toString());
                }
                printWriter.println("  Callbacks:");
                Iterator it = privacyItemController.callbacks.iterator();
                while (it.hasNext()) {
                    PrivacyItemController.Callback callback = (PrivacyItemController.Callback) ((WeakReference) it.next()).get();
                    if (callback != null) {
                        printWriter.print("    ");
                        printWriter.println(callback.toString());
                    }
                }
                throw new NullPointerException(stringWriter.toString());
            }
        }
        this.mPrivacyLogger.logStatusBarIconsVisible(z, z2, z3);
    }

    @Override // com.android.systemui.statusbar.policy.ZenModeController.Callback
    public final void onZenChanged(int i) {
        updateVolumeZen();
    }

    @Override // com.android.systemui.statusbar.policy.BluetoothController.Callback
    public final void onBluetoothDevicesChanged() {
        updateBluetooth();
    }

    @Override // com.android.systemui.statusbar.policy.BluetoothController.Callback
    public final void onBluetoothStateChange() {
        updateBluetooth();
    }

    @Override // com.android.systemui.statusbar.policy.ZenModeController.Callback
    public final void onConfigChanged() {
        updateVolumeZen();
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
    public final void onKeyguardShowingChanged() {
        updateManagedProfile();
    }
}
