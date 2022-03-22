package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Resources;
import android.hardware.display.ColorDisplayManager;
import android.hardware.display.NightDisplayListener;
import android.os.Handler;
import android.os.UserHandle;
import android.util.Log;
import androidx.constraintlayout.motion.widget.MotionLayout$$ExternalSyntheticOutline0;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda1;
import com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda2;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.qs.AutoAddTracker;
import com.android.systemui.qs.QSTileHost;
import com.android.systemui.qs.ReduceBrightColorsController;
import com.android.systemui.qs.SettingObserver;
import com.android.systemui.qs.external.CustomTile;
import com.android.systemui.qs.tiles.DndTile$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.phone.ManagedProfileController;
import com.android.systemui.statusbar.policy.CastController;
import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.systemui.statusbar.policy.DeviceControlsController;
import com.android.systemui.statusbar.policy.HotspotController;
import com.android.systemui.statusbar.policy.WalletController;
import com.android.systemui.util.UserAwareController;
import com.android.systemui.util.settings.SecureSettings;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda16;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda18;
import com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda3;
import com.google.android.systemui.elmyra.actions.Action$$ExternalSyntheticLambda0;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public class AutoTileManager implements UserAwareController {
    public final AutoAddTracker mAutoTracker;
    public final CastController mCastController;
    public final Context mContext;
    public UserHandle mCurrentUser;
    public final DataSaverController mDataSaverController;
    public final DeviceControlsController mDeviceControlsController;
    public final Handler mHandler;
    public final QSTileHost mHost;
    public final HotspotController mHotspotController;
    public boolean mInitialized;
    public final boolean mIsReduceBrightColorsAvailable;
    public final ManagedProfileController mManagedProfileController;
    public final NightDisplayListener mNightDisplayListener;
    public final ReduceBrightColorsController mReduceBrightColorsController;
    public final SecureSettings mSecureSettings;
    public final WalletController mWalletController;
    public final ArrayList<AutoAddSetting> mAutoAddSettingList = new ArrayList<>();
    public final AnonymousClass1 mProfileCallback = new ManagedProfileController.Callback() { // from class: com.android.systemui.statusbar.phone.AutoTileManager.1
        @Override // com.android.systemui.statusbar.phone.ManagedProfileController.Callback
        public final void onManagedProfileRemoved() {
        }

        @Override // com.android.systemui.statusbar.phone.ManagedProfileController.Callback
        public final void onManagedProfileChanged() {
            if (!AutoTileManager.this.mAutoTracker.isAdded("work") && AutoTileManager.this.mManagedProfileController.hasActiveProfile()) {
                QSTileHost qSTileHost = AutoTileManager.this.mHost;
                Objects.requireNonNull(qSTileHost);
                qSTileHost.addTile("work", -1);
                AutoTileManager.this.mAutoTracker.setTileAdded("work");
            }
        }
    };
    public final AnonymousClass2 mDataSaverListener = new AnonymousClass2();
    public final AnonymousClass3 mHotspotCallback = new AnonymousClass3();
    public final AnonymousClass4 mDeviceControlsCallback = new AnonymousClass4();
    @VisibleForTesting
    public final NightDisplayListener.Callback mNightDisplayCallback = new AnonymousClass5();
    @VisibleForTesting
    public final ReduceBrightColorsController.Listener mReduceBrightColorsCallback = new AnonymousClass6();
    @VisibleForTesting
    public final CastController.Callback mCastCallback = new AnonymousClass7();

    /* renamed from: com.android.systemui.statusbar.phone.AutoTileManager$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 implements DataSaverController.Listener {
        public AnonymousClass2() {
        }

        @Override // com.android.systemui.statusbar.policy.DataSaverController.Listener
        public final void onDataSaverChanged(boolean z) {
            if (!AutoTileManager.this.mAutoTracker.isAdded("saver") && z) {
                QSTileHost qSTileHost = AutoTileManager.this.mHost;
                Objects.requireNonNull(qSTileHost);
                qSTileHost.addTile("saver", -1);
                AutoTileManager.this.mAutoTracker.setTileAdded("saver");
                AutoTileManager.this.mHandler.post(new LockIconViewController$$ExternalSyntheticLambda2(this, 5));
            }
        }
    }

    /* renamed from: com.android.systemui.statusbar.phone.AutoTileManager$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass3 implements HotspotController.Callback {
        public AnonymousClass3() {
        }

        @Override // com.android.systemui.statusbar.policy.HotspotController.Callback
        public final void onHotspotChanged(boolean z, int i) {
            if (!AutoTileManager.this.mAutoTracker.isAdded("hotspot") && z) {
                QSTileHost qSTileHost = AutoTileManager.this.mHost;
                Objects.requireNonNull(qSTileHost);
                qSTileHost.addTile("hotspot", -1);
                AutoTileManager.this.mAutoTracker.setTileAdded("hotspot");
                AutoTileManager.this.mHandler.post(new LockIconViewController$$ExternalSyntheticLambda1(this, 4));
            }
        }
    }

    /* renamed from: com.android.systemui.statusbar.phone.AutoTileManager$4  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass4 implements DeviceControlsController.Callback {
        public AnonymousClass4() {
        }
    }

    /* renamed from: com.android.systemui.statusbar.phone.AutoTileManager$5  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass5 implements NightDisplayListener.Callback {
        public final void onAutoModeChanged(int i) {
            if (i == 1 || i == 2) {
                addNightTile();
            }
        }

        public AnonymousClass5() {
        }

        public final void addNightTile() {
            if (!AutoTileManager.this.mAutoTracker.isAdded("night")) {
                QSTileHost qSTileHost = AutoTileManager.this.mHost;
                Objects.requireNonNull(qSTileHost);
                qSTileHost.addTile("night", -1);
                AutoTileManager.this.mAutoTracker.setTileAdded("night");
                AutoTileManager.this.mHandler.post(new BubbleStackView$$ExternalSyntheticLambda16(this, 2));
            }
        }

        public final void onActivated(boolean z) {
            if (z) {
                addNightTile();
            }
        }
    }

    /* renamed from: com.android.systemui.statusbar.phone.AutoTileManager$6  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass6 implements ReduceBrightColorsController.Listener {
        public AnonymousClass6() {
        }

        @Override // com.android.systemui.qs.ReduceBrightColorsController.Listener
        public final void onActivated(boolean z) {
            if (z && !AutoTileManager.this.mAutoTracker.isAdded("reduce_brightness")) {
                QSTileHost qSTileHost = AutoTileManager.this.mHost;
                Objects.requireNonNull(qSTileHost);
                qSTileHost.addTile("reduce_brightness", -1);
                AutoTileManager.this.mAutoTracker.setTileAdded("reduce_brightness");
                AutoTileManager.this.mHandler.post(new StatusBar$$ExternalSyntheticLambda18(this, 10));
            }
        }
    }

    /* renamed from: com.android.systemui.statusbar.phone.AutoTileManager$7  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass7 implements CastController.Callback {
        public AnonymousClass7() {
        }

        @Override // com.android.systemui.statusbar.policy.CastController.Callback
        public final void onCastDevicesChanged() {
            if (!AutoTileManager.this.mAutoTracker.isAdded("cast")) {
                boolean z = false;
                for (CastController.CastDevice castDevice : AutoTileManager.this.mCastController.getCastDevices()) {
                    int i = castDevice.state;
                    if (i != 2) {
                        if (i == 1) {
                        }
                    }
                    z = true;
                }
                if (z) {
                    QSTileHost qSTileHost = AutoTileManager.this.mHost;
                    Objects.requireNonNull(qSTileHost);
                    qSTileHost.addTile("cast", -1);
                    AutoTileManager.this.mAutoTracker.setTileAdded("cast");
                    AutoTileManager.this.mHandler.post(new BubbleStackView$$ExternalSyntheticLambda18(this, 5));
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public class AutoAddSetting extends SettingObserver {
        public static final /* synthetic */ int $r8$clinit = 0;
        public final String mSpec;

        public AutoAddSetting(SecureSettings secureSettings, Handler handler, String str, int i, String str2) {
            super(secureSettings, handler, str, i);
            this.mSpec = str2;
        }

        @Override // com.android.systemui.qs.SettingObserver
        public final void handleValueChanged(int i, boolean z) {
            if (AutoTileManager.this.mAutoTracker.isAdded(this.mSpec)) {
                AutoTileManager.this.mHandler.post(new Action$$ExternalSyntheticLambda0(this, 4));
            } else if (i != 0) {
                if (this.mSpec.startsWith("custom(")) {
                    AutoTileManager.this.mHost.addTile(CustomTile.getComponentFromSpec(this.mSpec), true);
                } else {
                    QSTileHost qSTileHost = AutoTileManager.this.mHost;
                    String str = this.mSpec;
                    Objects.requireNonNull(qSTileHost);
                    qSTileHost.addTile(str, -1);
                }
                AutoTileManager.this.mAutoTracker.setTileAdded(this.mSpec);
                AutoTileManager.this.mHandler.post(new PipTaskOrganizer$$ExternalSyntheticLambda3(this, 3));
            }
        }
    }

    /* JADX WARN: Type inference failed for: r3v1, types: [com.android.systemui.statusbar.phone.AutoTileManager$1] */
    public AutoTileManager(Context context, AutoAddTracker.Builder builder, QSTileHost qSTileHost, Handler handler, SecureSettings secureSettings, HotspotController hotspotController, DataSaverController dataSaverController, ManagedProfileController managedProfileController, NightDisplayListener nightDisplayListener, CastController castController, ReduceBrightColorsController reduceBrightColorsController, DeviceControlsController deviceControlsController, WalletController walletController, boolean z) {
        this.mContext = context;
        this.mHost = qSTileHost;
        this.mSecureSettings = secureSettings;
        Objects.requireNonNull(qSTileHost);
        UserHandle user = qSTileHost.mUserContext.getUser();
        this.mCurrentUser = user;
        int identifier = user.getIdentifier();
        Objects.requireNonNull(builder);
        builder.userId = identifier;
        this.mAutoTracker = new AutoAddTracker(builder.secureSettings, builder.broadcastDispatcher, builder.qsHost, builder.dumpManager, builder.handler, builder.executor, builder.userId);
        this.mHandler = handler;
        this.mHotspotController = hotspotController;
        this.mDataSaverController = dataSaverController;
        this.mManagedProfileController = managedProfileController;
        this.mNightDisplayListener = nightDisplayListener;
        this.mCastController = castController;
        this.mReduceBrightColorsController = reduceBrightColorsController;
        this.mIsReduceBrightColorsAvailable = z;
        this.mDeviceControlsController = deviceControlsController;
        this.mWalletController = walletController;
    }

    @Override // com.android.systemui.util.UserAwareController
    public final void changeUser(UserHandle userHandle) {
        if (!this.mInitialized) {
            throw new IllegalStateException("AutoTileManager not initialized");
        } else if (!Thread.currentThread().equals(this.mHandler.getLooper().getThread())) {
            this.mHandler.post(new DndTile$$ExternalSyntheticLambda0(this, userHandle, 1));
        } else if (userHandle.getIdentifier() != this.mCurrentUser.getIdentifier()) {
            stopListening();
            this.mCurrentUser = userHandle;
            int size = this.mAutoAddSettingList.size();
            for (int i = 0; i < size; i++) {
                this.mAutoAddSettingList.get(i).setUserId(userHandle.getIdentifier());
            }
            this.mAutoTracker.changeUser(userHandle);
            startControllersAndSettingsListeners();
        }
    }

    @VisibleForTesting
    public SettingObserver getSecureSettingForKey(String str) {
        Iterator<AutoAddSetting> it = this.mAutoAddSettingList.iterator();
        while (it.hasNext()) {
            AutoAddSetting next = it.next();
            Objects.requireNonNull(next);
            if (Objects.equals(str, next.mSettingName)) {
                return next;
            }
        }
        return null;
    }

    public void init() {
        String[] stringArray;
        if (this.mInitialized) {
            Log.w("AutoTileManager", "Trying to re-initialize");
            return;
        }
        AutoAddTracker autoAddTracker = this.mAutoTracker;
        Objects.requireNonNull(autoAddTracker);
        autoAddTracker.dumpManager.registerDumpable("AutoAddTracker", autoAddTracker);
        autoAddTracker.loadTiles();
        SecureSettings secureSettings = autoAddTracker.secureSettings;
        secureSettings.registerContentObserverForUser(secureSettings.getUriFor("qs_auto_tiles"), autoAddTracker.contentObserver, -1);
        BroadcastDispatcher.registerReceiver$default(autoAddTracker.broadcastDispatcher, autoAddTracker.restoreReceiver, AutoAddTracker.FILTER, autoAddTracker.backgroundExecutor, UserHandle.of(autoAddTracker.userId), 16);
        try {
            for (String str : this.mContext.getResources().getStringArray(2130903098)) {
                String[] split = str.split(":");
                if (split.length == 2) {
                    this.mAutoAddSettingList.add(new AutoAddSetting(this.mSecureSettings, this.mHandler, split[0], this.mCurrentUser.getIdentifier(), split[1]));
                } else {
                    MotionLayout$$ExternalSyntheticOutline0.m("Malformed item in array: ", str, "AutoTileManager");
                }
            }
        } catch (Resources.NotFoundException unused) {
            Log.w("AutoTileManager", "Missing config resource");
        }
        startControllersAndSettingsListeners();
        this.mInitialized = true;
    }

    public void startControllersAndSettingsListeners() {
        Integer walletPosition;
        if (!this.mAutoTracker.isAdded("hotspot")) {
            this.mHotspotController.addCallback(this.mHotspotCallback);
        }
        if (!this.mAutoTracker.isAdded("saver")) {
            this.mDataSaverController.addCallback(this.mDataSaverListener);
        }
        if (!this.mAutoTracker.isAdded("work")) {
            this.mManagedProfileController.addCallback(this.mProfileCallback);
        }
        if (!this.mAutoTracker.isAdded("night") && ColorDisplayManager.isNightDisplayAvailable(this.mContext)) {
            this.mNightDisplayListener.setCallback(this.mNightDisplayCallback);
        }
        if (!this.mAutoTracker.isAdded("cast")) {
            this.mCastController.addCallback(this.mCastCallback);
        }
        if (!this.mAutoTracker.isAdded("reduce_brightness") && this.mIsReduceBrightColorsAvailable) {
            this.mReduceBrightColorsController.addCallback(this.mReduceBrightColorsCallback);
        }
        if (!this.mAutoTracker.isAdded("controls")) {
            this.mDeviceControlsController.setCallback(this.mDeviceControlsCallback);
        }
        if (!this.mAutoTracker.isAdded("wallet") && !this.mAutoTracker.isAdded("wallet") && (walletPosition = this.mWalletController.getWalletPosition()) != null) {
            this.mHost.addTile("wallet", walletPosition.intValue());
            this.mAutoTracker.setTileAdded("wallet");
        }
        int size = this.mAutoAddSettingList.size();
        for (int i = 0; i < size; i++) {
            if (!this.mAutoTracker.isAdded(this.mAutoAddSettingList.get(i).mSpec)) {
                this.mAutoAddSettingList.get(i).setListening(true);
            }
        }
    }

    public void stopListening() {
        this.mHotspotController.removeCallback(this.mHotspotCallback);
        this.mDataSaverController.removeCallback(this.mDataSaverListener);
        this.mManagedProfileController.removeCallback(this.mProfileCallback);
        if (ColorDisplayManager.isNightDisplayAvailable(this.mContext)) {
            this.mNightDisplayListener.setCallback((NightDisplayListener.Callback) null);
        }
        if (this.mIsReduceBrightColorsAvailable) {
            this.mReduceBrightColorsController.removeCallback(this.mReduceBrightColorsCallback);
        }
        this.mCastController.removeCallback(this.mCastCallback);
        this.mDeviceControlsController.removeCallback();
        int size = this.mAutoAddSettingList.size();
        for (int i = 0; i < size; i++) {
            this.mAutoAddSettingList.get(i).setListening(false);
        }
    }
}
