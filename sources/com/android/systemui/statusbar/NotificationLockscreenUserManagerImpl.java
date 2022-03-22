package com.android.systemui.statusbar;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.UserInfo;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.DejankUtils;
import com.android.systemui.Dependency;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.phone.StatusBarNotificationPresenter;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.wifitrackerlib.WifiEntry$$ExternalSyntheticLambda2;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$resetStateOnUserChange$listener$1;
import dagger.Lazy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public class NotificationLockscreenUserManagerImpl implements Dumpable, NotificationLockscreenUserManager, StatusBarStateController.StateListener {
    public boolean mAllowLockscreenRemoteInput;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public final NotificationClickNotifier mClickNotifier;
    public final Lazy<CommonNotifCollection> mCommonNotifCollectionLazy;
    public final Context mContext;
    public int mCurrentUserId;
    public final DevicePolicyManager mDevicePolicyManager;
    public final DeviceProvisionedController mDeviceProvisionedController;
    public NotificationEntryManager mEntryManager;
    public KeyguardManager mKeyguardManager;
    public final KeyguardStateController mKeyguardStateController;
    public LockPatternUtils mLockPatternUtils;
    public AnonymousClass3 mLockscreenSettingsObserver;
    public final Handler mMainHandler;
    public NotificationPresenter mPresenter;
    public AnonymousClass4 mSettingsObserver;
    public boolean mShowLockscreenNotifications;
    public final UserManager mUserManager;
    public final Lazy<NotificationVisibilityProvider> mVisibilityProviderLazy;
    public final Object mLock = new Object();
    public final SparseBooleanArray mLockscreenPublicMode = new SparseBooleanArray();
    public final SparseBooleanArray mUsersWithSeparateWorkChallenge = new SparseBooleanArray();
    public final SparseBooleanArray mUsersAllowingPrivateNotifications = new SparseBooleanArray();
    public final SparseBooleanArray mUsersAllowingNotifications = new SparseBooleanArray();
    public final SparseBooleanArray mUsersInLockdownLatestResult = new SparseBooleanArray();
    public final SparseBooleanArray mShouldHideNotifsLatestResult = new SparseBooleanArray();
    public final ArrayList mListeners = new ArrayList();
    public int mState = 0;
    public ArrayList mKeyguardSuppressors = new ArrayList();
    public final AnonymousClass1 mAllUsersReceiver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl.1
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            if ("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED".equals(intent.getAction()) && NotificationLockscreenUserManagerImpl.this.isCurrentProfile(getSendingUserId())) {
                NotificationLockscreenUserManagerImpl.this.mUsersAllowingPrivateNotifications.clear();
                NotificationLockscreenUserManagerImpl.this.updateLockscreenNotificationSetting();
                NotificationLockscreenUserManagerImpl.this.getEntryManager().updateNotifications("ACTION_DEVICE_POLICY_MANAGER_STATE_CHANGED");
            }
        }
    };
    public final AnonymousClass2 mBaseBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl.2
        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            char c;
            String action = intent.getAction();
            Objects.requireNonNull(action);
            switch (action.hashCode()) {
                case -1238404651:
                    if (action.equals("android.intent.action.MANAGED_PROFILE_UNAVAILABLE")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case -864107122:
                    if (action.equals("android.intent.action.MANAGED_PROFILE_AVAILABLE")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case -598152660:
                    if (action.equals("com.android.systemui.statusbar.work_challenge_unlocked_notification_action")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 833559602:
                    if (action.equals("android.intent.action.USER_UNLOCKED")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 959232034:
                    if (action.equals("android.intent.action.USER_SWITCHED")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case 1121780209:
                    if (action.equals("android.intent.action.USER_ADDED")) {
                        c = 5;
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
                case 1:
                case 5:
                    NotificationLockscreenUserManagerImpl.this.updateCurrentProfilesCache();
                    return;
                case 2:
                    IntentSender intentSender = (IntentSender) intent.getParcelableExtra("android.intent.extra.INTENT");
                    String stringExtra = intent.getStringExtra("android.intent.extra.INDEX");
                    if (intentSender != null) {
                        try {
                            NotificationLockscreenUserManagerImpl.this.mContext.startIntentSender(intentSender, null, 0, 0, 0);
                        } catch (IntentSender.SendIntentException unused) {
                        }
                    }
                    if (stringExtra != null) {
                        NotificationVisibility obtain = NotificationLockscreenUserManagerImpl.this.mVisibilityProviderLazy.get().obtain(stringExtra);
                        NotificationClickNotifier notificationClickNotifier = NotificationLockscreenUserManagerImpl.this.mClickNotifier;
                        Objects.requireNonNull(notificationClickNotifier);
                        try {
                            notificationClickNotifier.barService.onNotificationClick(stringExtra, obtain);
                        } catch (RemoteException unused2) {
                        }
                        notificationClickNotifier.mainExecutor.execute(new NotificationClickNotifier$onNotificationClick$1(notificationClickNotifier, stringExtra));
                        return;
                    }
                    return;
                case 3:
                    ((OverviewProxyService) Dependency.get(OverviewProxyService.class)).startConnectionToCurrentUser();
                    return;
                case 4:
                    NotificationLockscreenUserManagerImpl.this.mCurrentUserId = intent.getIntExtra("android.intent.extra.user_handle", -1);
                    NotificationLockscreenUserManagerImpl.this.updateCurrentProfilesCache();
                    Log.v("LockscreenUserManager", "userId " + NotificationLockscreenUserManagerImpl.this.mCurrentUserId + " is in the house");
                    NotificationLockscreenUserManagerImpl.this.updateLockscreenNotificationSetting();
                    NotificationLockscreenUserManagerImpl.this.updatePublicMode();
                    NotificationLockscreenUserManagerImpl.this.getEntryManager().reapplyFilterAndSort("user switched");
                    NotificationLockscreenUserManagerImpl notificationLockscreenUserManagerImpl = NotificationLockscreenUserManagerImpl.this;
                    ((StatusBarNotificationPresenter) notificationLockscreenUserManagerImpl.mPresenter).onUserSwitched(notificationLockscreenUserManagerImpl.mCurrentUserId);
                    Iterator it = NotificationLockscreenUserManagerImpl.this.mListeners.iterator();
                    while (it.hasNext()) {
                        ((NotificationLockscreenUserManager.UserChangedListener) it.next()).onUserChanged(NotificationLockscreenUserManagerImpl.this.mCurrentUserId);
                    }
                    return;
                default:
                    return;
            }
        }
    };
    public final SparseArray<UserInfo> mCurrentProfiles = new SparseArray<>();
    public final SparseArray<UserInfo> mCurrentManagedProfiles = new SparseArray<>();

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public final boolean isLockscreenPublicMode(int i) {
        if (i == -1) {
            return this.mLockscreenPublicMode.get(this.mCurrentUserId, false);
        }
        return this.mLockscreenPublicMode.get(i, false);
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public final boolean shouldHideNotifications(int i) {
        boolean z;
        int i2;
        if ((!isLockscreenPublicMode(i) || userAllowsNotificationsInPublic(i)) && (i == (i2 = this.mCurrentUserId) || !shouldHideNotifications(i2))) {
            int i3 = i == -1 ? this.mCurrentUserId : i;
            KeyguardUpdateMonitor keyguardUpdateMonitor = (KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class);
            Objects.requireNonNull(keyguardUpdateMonitor);
            boolean containsFlag = KeyguardUpdateMonitor.containsFlag(keyguardUpdateMonitor.mStrongAuthTracker.getStrongAuthForUser(i3), 32);
            this.mUsersInLockdownLatestResult.put(i3, containsFlag);
            if (!containsFlag) {
                z = false;
                this.mShouldHideNotifsLatestResult.put(i, z);
                return z;
            }
        }
        z = true;
        this.mShouldHideNotifsLatestResult.put(i, z);
        return z;
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public final boolean userAllowsPrivateNotificationsInPublic(int i) {
        boolean z;
        boolean z2;
        boolean z3 = true;
        if (i == -1) {
            return true;
        }
        if (this.mUsersAllowingPrivateNotifications.indexOfKey(i) >= 0) {
            return this.mUsersAllowingPrivateNotifications.get(i);
        }
        if (Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "lock_screen_allow_private_notifications", 0, i) != 0) {
            z = true;
        } else {
            z = false;
        }
        if (i == -1 || (this.mDevicePolicyManager.getKeyguardDisabledFeatures(null, i) & 8) == 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (!z || !z2) {
            z3 = false;
        }
        this.mUsersAllowingPrivateNotifications.append(i, z3);
        return z3;
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public final void addKeyguardNotificationSuppressor(NotificationLockscreenUserManager.KeyguardNotificationSuppressor keyguardNotificationSuppressor) {
        this.mKeyguardSuppressors.add(keyguardNotificationSuppressor);
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public final void addUserChangedListener(NotificationLockscreenUserManager.UserChangedListener userChangedListener) {
        this.mListeners.add(userChangedListener);
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("NotificationLockscreenUserManager state:");
        printWriter.print("  mCurrentUserId=");
        printWriter.println(this.mCurrentUserId);
        printWriter.print("  mShowLockscreenNotifications=");
        printWriter.println(this.mShowLockscreenNotifications);
        printWriter.print("  mAllowLockscreenRemoteInput=");
        printWriter.println(this.mAllowLockscreenRemoteInput);
        printWriter.print("  mCurrentProfiles=");
        synchronized (this.mLock) {
            for (int size = this.mCurrentProfiles.size() - 1; size >= 0; size += -1) {
                printWriter.print("" + this.mCurrentProfiles.valueAt(size).id + " ");
            }
        }
        printWriter.println();
        printWriter.print("  mCurrentManagedProfiles=");
        synchronized (this.mLock) {
            for (int size2 = this.mCurrentManagedProfiles.size() - 1; size2 >= 0; size2 += -1) {
                printWriter.print("" + this.mCurrentManagedProfiles.valueAt(size2).id + " ");
            }
        }
        printWriter.println();
        printWriter.print("  mLockscreenPublicMode=");
        printWriter.println(this.mLockscreenPublicMode);
        printWriter.print("  mUsersWithSeparateWorkChallenge=");
        printWriter.println(this.mUsersWithSeparateWorkChallenge);
        printWriter.print("  mUsersAllowingPrivateNotifications=");
        printWriter.println(this.mUsersAllowingPrivateNotifications);
        printWriter.print("  mUsersAllowingNotifications=");
        printWriter.println(this.mUsersAllowingNotifications);
        printWriter.print("  mUsersInLockdownLatestResult=");
        printWriter.println(this.mUsersInLockdownLatestResult);
        printWriter.print("  mShouldHideNotifsLatestResult=");
        printWriter.println(this.mShouldHideNotifsLatestResult);
    }

    public final NotificationEntryManager getEntryManager() {
        if (this.mEntryManager == null) {
            this.mEntryManager = (NotificationEntryManager) Dependency.get(NotificationEntryManager.class);
        }
        return this.mEntryManager;
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public final boolean isAnyProfilePublicMode() {
        synchronized (this.mLock) {
            for (int size = this.mCurrentProfiles.size() - 1; size >= 0; size--) {
                if (isLockscreenPublicMode(this.mCurrentProfiles.valueAt(size).id)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public final boolean isCurrentProfile(int i) {
        boolean z;
        synchronized (this.mLock) {
            if (i != -1) {
                try {
                    if (this.mCurrentProfiles.get(i) == null) {
                        z = false;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            z = true;
        }
        return z;
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public final boolean needsSeparateWorkChallenge(int i) {
        return this.mUsersWithSeparateWorkChallenge.get(i, false);
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public final void onStateChanged(int i) {
        this.mState = i;
        updatePublicMode();
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public final void removeUserChangedListener(NotificationVoiceReplyController$resetStateOnUserChange$listener$1 notificationVoiceReplyController$resetStateOnUserChange$listener$1) {
        this.mListeners.remove(notificationVoiceReplyController$resetStateOnUserChange$listener$1);
    }

    /* JADX WARN: Type inference failed for: r12v1, types: [com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl$3] */
    /* JADX WARN: Type inference failed for: r12v2, types: [com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl$4] */
    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public final void setUpWithPresenter(NotificationPresenter notificationPresenter) {
        this.mPresenter = notificationPresenter;
        this.mLockscreenSettingsObserver = new ContentObserver(this.mMainHandler) { // from class: com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl.3
            @Override // android.database.ContentObserver
            public final void onChange(boolean z) {
                NotificationLockscreenUserManagerImpl.this.mUsersAllowingPrivateNotifications.clear();
                NotificationLockscreenUserManagerImpl.this.mUsersAllowingNotifications.clear();
                NotificationLockscreenUserManagerImpl.this.updateLockscreenNotificationSetting();
                NotificationLockscreenUserManagerImpl.this.getEntryManager().updateNotifications("LOCK_SCREEN_SHOW_NOTIFICATIONS, or LOCK_SCREEN_ALLOW_PRIVATE_NOTIFICATIONS change");
            }
        };
        this.mSettingsObserver = new ContentObserver(this.mMainHandler) { // from class: com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl.4
            @Override // android.database.ContentObserver
            public final void onChange(boolean z) {
                NotificationLockscreenUserManagerImpl.this.updateLockscreenNotificationSetting();
                if (NotificationLockscreenUserManagerImpl.this.mDeviceProvisionedController.isDeviceProvisioned()) {
                    NotificationLockscreenUserManagerImpl.this.getEntryManager().updateNotifications("LOCK_SCREEN_ALLOW_REMOTE_INPUT or ZEN_MODE change");
                }
            }
        };
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("lock_screen_show_notifications"), false, this.mLockscreenSettingsObserver, -1);
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("lock_screen_allow_private_notifications"), true, this.mLockscreenSettingsObserver, -1);
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("zen_mode"), false, this.mSettingsObserver);
        this.mBroadcastDispatcher.registerReceiver(this.mAllUsersReceiver, new IntentFilter("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED"), null, UserHandle.ALL);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        intentFilter.addAction("android.intent.action.USER_ADDED");
        intentFilter.addAction("android.intent.action.USER_UNLOCKED");
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_AVAILABLE");
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_UNAVAILABLE");
        this.mBroadcastDispatcher.registerReceiver(this.mBaseBroadcastReceiver, intentFilter, null, UserHandle.ALL);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("com.android.systemui.statusbar.work_challenge_unlocked_notification_action");
        this.mContext.registerReceiver(this.mBaseBroadcastReceiver, intentFilter2, "com.android.systemui.permission.SELF", null, 2);
        this.mCurrentUserId = ActivityManager.getCurrentUser();
        updateCurrentProfilesCache();
        onChange(false);
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public final boolean shouldShowOnKeyguard(NotificationEntry notificationEntry) {
        boolean z;
        if (this.mCommonNotifCollectionLazy.get() == null) {
            Log.wtf("LockscreenUserManager", "mCommonNotifCollectionLazy was null!", new Throwable());
            return false;
        }
        for (int i = 0; i < this.mKeyguardSuppressors.size(); i++) {
            if (((NotificationLockscreenUserManager.KeyguardNotificationSuppressor) this.mKeyguardSuppressors.get(i)).shouldSuppressOnKeyguard(notificationEntry)) {
                return false;
            }
        }
        if (((Boolean) DejankUtils.whitelistIpcs(new Supplier() { // from class: com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                NotificationLockscreenUserManagerImpl notificationLockscreenUserManagerImpl = NotificationLockscreenUserManagerImpl.this;
                Objects.requireNonNull(notificationLockscreenUserManagerImpl);
                boolean z2 = true;
                if (Settings.Secure.getInt(notificationLockscreenUserManagerImpl.mContext.getContentResolver(), "lock_screen_show_silent_notifications", 1) != 0) {
                    z2 = false;
                }
                return Boolean.valueOf(z2);
            }
        })).booleanValue()) {
            Objects.requireNonNull(notificationEntry);
            int i2 = notificationEntry.mBucket;
            if (i2 == 1 || (i2 != 6 && notificationEntry.getImportance() >= 3)) {
                z = true;
            } else {
                z = false;
            }
        } else {
            Objects.requireNonNull(notificationEntry);
            z = !notificationEntry.mRanking.isAmbient();
        }
        if (!this.mShowLockscreenNotifications || !z) {
            return false;
        }
        return true;
    }

    public final void updateCurrentProfilesCache() {
        synchronized (this.mLock) {
            this.mCurrentProfiles.clear();
            this.mCurrentManagedProfiles.clear();
            UserManager userManager = this.mUserManager;
            if (userManager != null) {
                for (UserInfo userInfo : userManager.getProfiles(this.mCurrentUserId)) {
                    this.mCurrentProfiles.put(userInfo.id, userInfo);
                    if ("android.os.usertype.profile.MANAGED".equals(userInfo.userType)) {
                        this.mCurrentManagedProfiles.put(userInfo.id, userInfo);
                    }
                }
            }
        }
        this.mMainHandler.post(new WifiEntry$$ExternalSyntheticLambda2(this, 4));
    }

    public final void updateLockscreenNotificationSetting() {
        boolean z;
        boolean z2;
        boolean z3 = true;
        if (Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "lock_screen_show_notifications", 1, this.mCurrentUserId) != 0) {
            z = true;
        } else {
            z = false;
        }
        if ((this.mDevicePolicyManager.getKeyguardDisabledFeatures(null, this.mCurrentUserId) & 4) == 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (!z || !z2) {
            z3 = false;
        }
        this.mShowLockscreenNotifications = z3;
        this.mAllowLockscreenRemoteInput = false;
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public void updatePublicMode() {
        boolean z;
        boolean z2;
        boolean z3;
        if (this.mState != 0 || this.mKeyguardStateController.isShowing()) {
            z = true;
        } else {
            z = false;
        }
        if (!z || !this.mKeyguardStateController.isMethodSecure()) {
            z2 = false;
        } else {
            z2 = true;
        }
        SparseArray<UserInfo> sparseArray = this.mCurrentProfiles;
        this.mUsersWithSeparateWorkChallenge.clear();
        for (int size = sparseArray.size() - 1; size >= 0; size--) {
            final int i = sparseArray.valueAt(size).id;
            boolean booleanValue = ((Boolean) DejankUtils.whitelistIpcs(new Supplier() { // from class: com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl$$ExternalSyntheticLambda1
                @Override // java.util.function.Supplier
                public final Object get() {
                    NotificationLockscreenUserManagerImpl notificationLockscreenUserManagerImpl = NotificationLockscreenUserManagerImpl.this;
                    int i2 = i;
                    Objects.requireNonNull(notificationLockscreenUserManagerImpl);
                    return Boolean.valueOf(notificationLockscreenUserManagerImpl.mLockPatternUtils.isSeparateProfileChallengeEnabled(i2));
                }
            })).booleanValue();
            if (z2 || i == this.mCurrentUserId || !booleanValue || !this.mLockPatternUtils.isSecure(i)) {
                z3 = z2;
            } else if (z || this.mKeyguardManager.isDeviceLocked(i)) {
                z3 = true;
            } else {
                z3 = false;
            }
            this.mLockscreenPublicMode.put(i, z3);
            this.mUsersWithSeparateWorkChallenge.put(i, booleanValue);
        }
        getEntryManager().updateNotifications("NotificationLockscreenUserManager.updatePublicMode");
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl$1] */
    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl$2] */
    public NotificationLockscreenUserManagerImpl(Context context, BroadcastDispatcher broadcastDispatcher, DevicePolicyManager devicePolicyManager, UserManager userManager, Lazy<NotificationVisibilityProvider> lazy, Lazy<CommonNotifCollection> lazy2, NotificationClickNotifier notificationClickNotifier, KeyguardManager keyguardManager, StatusBarStateController statusBarStateController, Handler handler, DeviceProvisionedController deviceProvisionedController, KeyguardStateController keyguardStateController, DumpManager dumpManager) {
        this.mCurrentUserId = 0;
        this.mContext = context;
        this.mMainHandler = handler;
        this.mDevicePolicyManager = devicePolicyManager;
        this.mUserManager = userManager;
        this.mCurrentUserId = ActivityManager.getCurrentUser();
        this.mVisibilityProviderLazy = lazy;
        this.mCommonNotifCollectionLazy = lazy2;
        this.mClickNotifier = notificationClickNotifier;
        statusBarStateController.addCallback(this);
        this.mLockPatternUtils = new LockPatternUtils(context);
        this.mKeyguardManager = keyguardManager;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mKeyguardStateController = keyguardStateController;
        dumpManager.registerDumpable(this);
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0069  */
    /* JADX WARN: Removed duplicated region for block: B:26:? A[RETURN, SYNTHETIC] */
    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean needsRedaction(com.android.systemui.statusbar.notification.collection.NotificationEntry r6) {
        /*
            r5 = this;
            java.util.Objects.requireNonNull(r6)
            android.service.notification.StatusBarNotification r0 = r6.mSbn
            int r0 = r0.getUserId()
            int r1 = r5.mCurrentUserId
            boolean r1 = r5.userAllowsPrivateNotificationsInPublic(r1)
            r2 = 1
            r1 = r1 ^ r2
            android.util.SparseArray<android.content.pm.UserInfo> r3 = r5.mCurrentManagedProfiles
            boolean r3 = r3.contains(r0)
            boolean r0 = r5.userAllowsPrivateNotificationsInPublic(r0)
            r0 = r0 ^ r2
            r4 = 0
            if (r3 != 0) goto L_0x0021
            if (r1 != 0) goto L_0x0023
        L_0x0021:
            if (r0 == 0) goto L_0x0025
        L_0x0023:
            r0 = r2
            goto L_0x0026
        L_0x0025:
            r0 = r4
        L_0x0026:
            android.service.notification.StatusBarNotification r1 = r6.mSbn
            android.app.Notification r1 = r1.getNotification()
            int r1 = r1.visibility
            if (r1 != 0) goto L_0x0032
            r1 = r2
            goto L_0x0033
        L_0x0032:
            r1 = r4
        L_0x0033:
            android.service.notification.StatusBarNotification r6 = r6.mSbn
            java.lang.String r6 = r6.getKey()
            dagger.Lazy<com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection> r3 = r5.mCommonNotifCollectionLazy
            java.lang.Object r3 = r3.get()
            if (r3 != 0) goto L_0x004e
            java.lang.Throwable r5 = new java.lang.Throwable
            r5.<init>()
            java.lang.String r6 = "LockscreenUserManager"
            java.lang.String r3 = "mEntryManager was null!"
            android.util.Log.wtf(r6, r3, r5)
            goto L_0x0064
        L_0x004e:
            dagger.Lazy<com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection> r5 = r5.mCommonNotifCollectionLazy
            java.lang.Object r5 = r5.get()
            com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection r5 = (com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection) r5
            com.android.systemui.statusbar.notification.collection.NotificationEntry r5 = r5.getEntry(r6)
            if (r5 == 0) goto L_0x0066
            android.service.notification.NotificationListenerService$Ranking r5 = r5.mRanking
            int r5 = r5.getLockscreenVisibilityOverride()
            if (r5 != 0) goto L_0x0066
        L_0x0064:
            r5 = r2
            goto L_0x0067
        L_0x0066:
            r5 = r4
        L_0x0067:
            if (r5 != 0) goto L_0x006f
            if (r1 == 0) goto L_0x006e
            if (r0 == 0) goto L_0x006e
            goto L_0x006f
        L_0x006e:
            r2 = r4
        L_0x006f:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl.needsRedaction(com.android.systemui.statusbar.notification.collection.NotificationEntry):boolean");
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public final boolean userAllowsNotificationsInPublic(int i) {
        boolean z;
        boolean z2;
        boolean z3 = true;
        if (isCurrentProfile(i) && i != this.mCurrentUserId) {
            return true;
        }
        if (this.mUsersAllowingNotifications.indexOfKey(i) >= 0) {
            return this.mUsersAllowingNotifications.get(i);
        }
        if (Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "lock_screen_show_notifications", 0, i) != 0) {
            z = true;
        } else {
            z = false;
        }
        if (i == -1 || (this.mDevicePolicyManager.getKeyguardDisabledFeatures(null, i) & 4) == 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        boolean privateNotificationsAllowed = this.mKeyguardManager.getPrivateNotificationsAllowed();
        if (!z || !z2 || !privateNotificationsAllowed) {
            z3 = false;
        }
        this.mUsersAllowingNotifications.append(i, z3);
        return z3;
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public final boolean shouldHideNotifications(String str) {
        if (this.mCommonNotifCollectionLazy.get() == null) {
            Log.wtf("LockscreenUserManager", "mCommonNotifCollectionLazy was null!", new Throwable());
            return true;
        }
        NotificationEntry entry = this.mCommonNotifCollectionLazy.get().getEntry(str);
        return isLockscreenPublicMode(this.mCurrentUserId) && entry != null && entry.mRanking.getLockscreenVisibilityOverride() == -1;
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public final int getCurrentUserId() {
        return this.mCurrentUserId;
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public final boolean shouldAllowLockscreenRemoteInput() {
        return this.mAllowLockscreenRemoteInput;
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public final boolean shouldShowLockscreenNotifications() {
        return this.mShowLockscreenNotifications;
    }
}
