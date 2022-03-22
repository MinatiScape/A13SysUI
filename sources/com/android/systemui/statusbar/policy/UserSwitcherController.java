package com.android.systemui.statusbar.policy;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.IActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.UserInfo;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.WindowManagerGlobal;
import android.widget.BaseAdapter;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline0;
import androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline1;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.util.LatencyTracker;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda7;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline0;
import com.android.keyguard.LockIconView$$ExternalSyntheticOutline0;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.wifi.AccessPoint$$ExternalSyntheticLambda1;
import com.android.systemui.Dumpable;
import com.android.systemui.GuestResumeSessionReceiver;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.people.PeopleTileViewHelper$$ExternalSyntheticLambda4;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.QSUserSwitcherEvent;
import com.android.systemui.qs.user.UserSwitchDialogController;
import com.android.systemui.scrim.ScrimView$$ExternalSyntheticLambda1;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.android.systemui.user.CreateUserActivity;
import dagger.Lazy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes.dex */
public final class UserSwitcherController implements Dumpable {
    public final IActivityManager mActivityManager;
    public final ActivityStarter mActivityStarter;
    @VisibleForTesting
    public Dialog mAddUserDialog;
    public boolean mAddUsersFromLockScreen;
    public final Executor mBgExecutor;
    public final AnonymousClass5 mCallback;
    public final Context mContext;
    public String mCreateSupervisedUserPackage;
    public final DevicePolicyManager mDevicePolicyManager;
    public final DeviceProvisionedController mDeviceProvisionedController;
    public final DialogLaunchAnimator mDialogLaunchAnimator;
    @VisibleForTesting
    public AlertDialog mExitGuestDialog;
    public FalsingManager mFalsingManager;
    public final AtomicBoolean mGuestCreationScheduled;
    public final AtomicBoolean mGuestIsResetting;
    @VisibleForTesting
    public final GuestResumeSessionReceiver mGuestResumeSessionReceiver;
    public final boolean mGuestUserAutoCreated;
    public final Handler mHandler;
    public final InteractionJankMonitor mInteractionJankMonitor;
    public final KeyguardStateController mKeyguardStateController;
    public final LatencyTracker mLatencyTracker;
    @VisibleForTesting
    public boolean mPauseRefreshUsers;
    public final AnonymousClass2 mPhoneStateListener;
    public Intent mSecondaryUserServiceIntent;
    public final AnonymousClass1 mSettingsObserver;
    public final Lazy<ShadeController> mShadeController;
    public boolean mSimpleUserSwitcher;
    public final UiEventLogger mUiEventLogger;
    public final Executor mUiExecutor;
    public final UserManager mUserManager;
    public final UserTracker mUserTracker;
    public View mView;
    public final ArrayList<WeakReference<BaseUserAdapter>> mAdapters = new ArrayList<>();
    public ArrayList<UserRecord> mUsers = new ArrayList<>();
    public int mLastNonGuestUser = 0;
    public boolean mResumeUserOnGuestLogout = true;
    public int mSecondaryUser = -10000;
    public SparseBooleanArray mForcePictureLoadForUserId = new SparseBooleanArray(2);
    public AnonymousClass3 mReceiver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.policy.UserSwitcherController.3
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            int i;
            boolean z;
            Context context2;
            int i2;
            int i3;
            int i4;
            boolean z2;
            boolean z3;
            if ("android.intent.action.USER_SWITCHED".equals(intent.getAction())) {
                AlertDialog alertDialog = UserSwitcherController.this.mExitGuestDialog;
                if (alertDialog != null && alertDialog.isShowing()) {
                    UserSwitcherController.this.mExitGuestDialog.cancel();
                    UserSwitcherController.this.mExitGuestDialog = null;
                }
                int intExtra = intent.getIntExtra("android.intent.extra.user_handle", -1);
                UserInfo userInfo = UserSwitcherController.this.mUserManager.getUserInfo(intExtra);
                int size = UserSwitcherController.this.mUsers.size();
                int i5 = 0;
                while (i5 < size) {
                    UserRecord userRecord = UserSwitcherController.this.mUsers.get(i5);
                    UserInfo userInfo2 = userRecord.info;
                    if (userInfo2 == null) {
                        i3 = intExtra;
                        i4 = size;
                    } else {
                        if (userInfo2.id == intExtra) {
                            z2 = true;
                        } else {
                            z2 = false;
                        }
                        if (userRecord.isCurrent != z2) {
                            i3 = intExtra;
                            i4 = size;
                            z3 = z2;
                            UserSwitcherController.this.mUsers.set(i5, new UserRecord(userInfo2, userRecord.picture, userRecord.isGuest, z2, userRecord.isAddUser, userRecord.isRestricted, userRecord.isSwitchToEnabled, userRecord.isAddSupervisedUser));
                        } else {
                            i3 = intExtra;
                            i4 = size;
                            z3 = z2;
                        }
                        if (z3 && !userRecord.isGuest) {
                            UserSwitcherController.this.mLastNonGuestUser = userRecord.info.id;
                        }
                        if ((userInfo == null || !userInfo.isAdmin()) && userRecord.isRestricted) {
                            UserSwitcherController.this.mUsers.remove(i5);
                            i5--;
                        }
                    }
                    i5++;
                    intExtra = i3;
                    size = i4;
                }
                UserSwitcherController.this.notifyAdapters();
                UserSwitcherController userSwitcherController = UserSwitcherController.this;
                int i6 = userSwitcherController.mSecondaryUser;
                if (i6 != -10000) {
                    context2 = context;
                    context2.stopServiceAsUser(userSwitcherController.mSecondaryUserServiceIntent, UserHandle.of(i6));
                    UserSwitcherController.this.mSecondaryUser = -10000;
                } else {
                    context2 = context;
                }
                if (!(userInfo == null || (i2 = userInfo.id) == 0)) {
                    context2.startServiceAsUser(UserSwitcherController.this.mSecondaryUserServiceIntent, UserHandle.of(i2));
                    UserSwitcherController.this.mSecondaryUser = userInfo.id;
                }
                UserSwitcherController userSwitcherController2 = UserSwitcherController.this;
                if (userSwitcherController2.mGuestUserAutoCreated) {
                    userSwitcherController2.guaranteeGuestPresent();
                }
                z = true;
                i = -10000;
            } else {
                if ("android.intent.action.USER_INFO_CHANGED".equals(intent.getAction())) {
                    i = intent.getIntExtra("android.intent.extra.user_handle", -10000);
                } else if (!"android.intent.action.USER_UNLOCKED".equals(intent.getAction()) || intent.getIntExtra("android.intent.extra.user_handle", -10000) == 0) {
                    i = -10000;
                } else {
                    return;
                }
                z = false;
            }
            UserSwitcherController.this.refreshUsers(i);
            if (z) {
                UserSwitcherController.this.mUnpauseRefreshUsers.run();
            }
        }
    };
    public final AnonymousClass4 mUnpauseRefreshUsers = new AnonymousClass4();
    public final AnonymousClass6 mGuaranteeGuestPresentAfterProvisioned = new AnonymousClass6();

    /* renamed from: com.android.systemui.statusbar.policy.UserSwitcherController$4  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass4 implements Runnable {
        public AnonymousClass4() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            UserSwitcherController.this.mHandler.removeCallbacks(this);
            UserSwitcherController userSwitcherController = UserSwitcherController.this;
            userSwitcherController.mPauseRefreshUsers = false;
            userSwitcherController.refreshUsers(-10000);
        }
    }

    /* renamed from: com.android.systemui.statusbar.policy.UserSwitcherController$6  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass6 implements DeviceProvisionedController.DeviceProvisionedListener {
        public AnonymousClass6() {
        }

        @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController.DeviceProvisionedListener
        public final void onDeviceProvisionedChanged() {
            if (UserSwitcherController.this.isDeviceAllowedToAddGuest()) {
                UserSwitcherController.this.mBgExecutor.execute(new AccessPoint$$ExternalSyntheticLambda1(this, 8));
                UserSwitcherController.this.guaranteeGuestPresent();
            }
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public final class AddUserDialog extends SystemUIDialog implements DialogInterface.OnClickListener {
        @Override // android.content.DialogInterface.OnClickListener
        public final void onClick(DialogInterface dialogInterface, int i) {
            int i2;
            if (i == -2) {
                i2 = 0;
            } else {
                i2 = 2;
            }
            if (!UserSwitcherController.this.mFalsingManager.isFalseTap(i2)) {
                if (i == -3) {
                    cancel();
                    return;
                }
                UserSwitcherController.this.mDialogLaunchAnimator.dismissStack(this);
                if (!ActivityManager.isUserAMonkey()) {
                    UserSwitcherController.this.mShadeController.get().collapsePanel();
                    Context context = getContext();
                    Context context2 = getContext();
                    int i3 = CreateUserActivity.$r8$clinit;
                    Intent intent = new Intent(context2, CreateUserActivity.class);
                    intent.addFlags(335544320);
                    context.startActivity(intent);
                }
            }
        }

        public AddUserDialog(Context context) {
            super(context);
            setTitle(2131953469);
            setMessage(context.getString(2131953468));
            setButton(-3, context.getString(17039360), this);
            setButton(-1, context.getString(17039370), this);
            SystemUIDialog.setWindowOnTop(this, UserSwitcherController.this.mKeyguardStateController.isShowing());
        }
    }

    /* loaded from: classes.dex */
    public static abstract class BaseUserAdapter extends BaseAdapter {
        public final UserSwitcherController mController;
        public final KeyguardStateController mKeyguardStateController;

        @Override // android.widget.Adapter
        public final long getItemId(int i) {
            return i;
        }

        public static Drawable getIconDrawable(Context context, UserRecord userRecord) {
            int i;
            if (userRecord.isAddUser) {
                i = 2131231740;
            } else if (userRecord.isGuest) {
                i = 2131231741;
            } else if (userRecord.isAddSupervisedUser) {
                i = 2131231748;
            } else {
                i = 2131231762;
            }
            return context.getDrawable(i);
        }

        @Override // android.widget.Adapter
        public final int getCount() {
            boolean isShowing = this.mKeyguardStateController.isShowing();
            int size = getUsers().size();
            int i = 0;
            for (int i2 = 0; i2 < size; i2++) {
                boolean z = getUsers().get(i2).isGuest;
                if (getUsers().get(i2).isRestricted && isShowing) {
                    break;
                }
                i++;
            }
            return i;
        }

        @Override // android.widget.Adapter
        public final UserRecord getItem(int i) {
            return getUsers().get(i);
        }

        public String getName(Context context, UserRecord userRecord) {
            int i;
            if (userRecord.isGuest) {
                if (userRecord.isCurrent) {
                    if (this.mController.mGuestUserAutoCreated) {
                        i = 2131952417;
                    } else {
                        i = 2131952411;
                    }
                    return context.getString(i);
                }
                int i2 = 2131952416;
                if (userRecord.info != null) {
                    return context.getString(2131952416);
                }
                UserSwitcherController userSwitcherController = this.mController;
                if (!userSwitcherController.mGuestUserAutoCreated) {
                    return context.getString(2131952415);
                }
                if (userSwitcherController.mGuestIsResetting.get()) {
                    i2 = 2131952420;
                }
                return context.getString(i2);
            } else if (userRecord.isAddUser) {
                return context.getString(2131953464);
            } else {
                if (userRecord.isAddSupervisedUser) {
                    return context.getString(2131951872);
                }
                return userRecord.info.name;
            }
        }

        public ArrayList<UserRecord> getUsers() {
            return this.mController.getUsers();
        }

        public final void onUserListItemClicked(UserRecord userRecord, UserSwitchDialogController.DialogShower dialogShower) {
            this.mController.onUserListItemClicked(userRecord, dialogShower);
        }

        public BaseUserAdapter(UserSwitcherController userSwitcherController) {
            this.mController = userSwitcherController;
            this.mKeyguardStateController = userSwitcherController.getKeyguardStateController();
            userSwitcherController.addAdapter(new WeakReference<>(this));
        }
    }

    /* loaded from: classes.dex */
    public final class ExitGuestDialog extends SystemUIDialog implements DialogInterface.OnClickListener {
        public final int mGuestId;
        public final int mTargetId;

        @Override // android.content.DialogInterface.OnClickListener
        public final void onClick(DialogInterface dialogInterface, int i) {
            int i2;
            if (i == -2) {
                i2 = 0;
            } else {
                i2 = 3;
            }
            if (!UserSwitcherController.this.mFalsingManager.isFalseTap(i2)) {
                if (i == -3) {
                    cancel();
                    return;
                }
                UserSwitcherController.this.mUiEventLogger.log(QSUserSwitcherEvent.QS_USER_GUEST_REMOVE);
                UserSwitcherController.this.mDialogLaunchAnimator.dismissStack(this);
                UserSwitcherController.this.removeGuestUser(this.mGuestId, this.mTargetId);
            }
        }

        public ExitGuestDialog(Context context, int i, int i2) {
            super(context);
            int i3;
            int i4;
            if (UserSwitcherController.this.mGuestUserAutoCreated) {
                i3 = 2131952419;
            } else {
                i3 = 2131952414;
            }
            setTitle(i3);
            setMessage(context.getString(2131952412));
            setButton(-3, context.getString(17039360), this);
            if (UserSwitcherController.this.mGuestUserAutoCreated) {
                i4 = 2131952418;
            } else {
                i4 = 2131952413;
            }
            setButton(-1, context.getString(i4), this);
            SystemUIDialog.setWindowOnTop(this, UserSwitcherController.this.mKeyguardStateController.isShowing());
            setCanceledOnTouchOutside(false);
            this.mGuestId = i;
            this.mTargetId = i2;
        }
    }

    /* loaded from: classes.dex */
    public static final class UserRecord {
        public RestrictedLockUtils.EnforcedAdmin enforcedAdmin;
        public final UserInfo info;
        public final boolean isAddSupervisedUser;
        public final boolean isAddUser;
        public final boolean isCurrent;
        public boolean isDisabledByAdmin;
        public final boolean isGuest;
        public final boolean isRestricted;
        public boolean isSwitchToEnabled;
        public final Bitmap picture;

        public final int resolveId() {
            UserInfo userInfo;
            if (this.isGuest || (userInfo = this.info) == null) {
                return -10000;
            }
            return userInfo.id;
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("UserRecord(");
            if (this.info != null) {
                m.append("name=\"");
                m.append(this.info.name);
                m.append("\" id=");
                m.append(this.info.id);
            } else if (this.isGuest) {
                m.append("<add guest placeholder>");
            } else if (this.isAddUser) {
                m.append("<add user placeholder>");
            }
            if (this.isGuest) {
                m.append(" <isGuest>");
            }
            if (this.isAddUser) {
                m.append(" <isAddUser>");
            }
            if (this.isAddSupervisedUser) {
                m.append(" <isAddSupervisedUser>");
            }
            if (this.isCurrent) {
                m.append(" <isCurrent>");
            }
            if (this.picture != null) {
                m.append(" <hasPicture>");
            }
            if (this.isRestricted) {
                m.append(" <isRestricted>");
            }
            if (this.isDisabledByAdmin) {
                m.append(" <isDisabledByAdmin>");
                m.append(" enforcedAdmin=");
                m.append(this.enforcedAdmin);
            }
            if (this.isSwitchToEnabled) {
                m.append(" <isSwitchToEnabled>");
            }
            m.append(')');
            return m.toString();
        }

        public UserRecord(UserInfo userInfo, Bitmap bitmap, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6) {
            this.info = userInfo;
            this.picture = bitmap;
            this.isGuest = z;
            this.isCurrent = z2;
            this.isAddUser = z3;
            this.isRestricted = z4;
            this.isSwitchToEnabled = z5;
            this.isAddSupervisedUser = z6;
        }
    }

    /* JADX WARN: Type inference failed for: r15v0, types: [com.android.systemui.statusbar.policy.UserSwitcherController$5, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r1v18, types: [com.android.systemui.statusbar.policy.UserSwitcherController$1, android.database.ContentObserver] */
    /* JADX WARN: Type inference failed for: r4v3, types: [com.android.systemui.statusbar.policy.UserSwitcherController$3] */
    /* JADX WARN: Unknown variable types count: 2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public UserSwitcherController(android.content.Context r17, android.app.IActivityManager r18, android.os.UserManager r19, com.android.systemui.settings.UserTracker r20, com.android.systemui.statusbar.policy.KeyguardStateController r21, com.android.systemui.statusbar.policy.DeviceProvisionedController r22, android.app.admin.DevicePolicyManager r23, android.os.Handler r24, com.android.systemui.plugins.ActivityStarter r25, com.android.systemui.broadcast.BroadcastDispatcher r26, com.android.internal.logging.UiEventLogger r27, com.android.systemui.plugins.FalsingManager r28, com.android.systemui.telephony.TelephonyListenerManager r29, com.android.systemui.util.settings.SecureSettings r30, java.util.concurrent.Executor r31, java.util.concurrent.Executor r32, com.android.internal.jank.InteractionJankMonitor r33, com.android.internal.util.LatencyTracker r34, com.android.systemui.dump.DumpManager r35, dagger.Lazy<com.android.systemui.statusbar.phone.ShadeController> r36, com.android.systemui.animation.DialogLaunchAnimator r37) {
        /*
            Method dump skipped, instructions count: 347
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.UserSwitcherController.<init>(android.content.Context, android.app.IActivityManager, android.os.UserManager, com.android.systemui.settings.UserTracker, com.android.systemui.statusbar.policy.KeyguardStateController, com.android.systemui.statusbar.policy.DeviceProvisionedController, android.app.admin.DevicePolicyManager, android.os.Handler, com.android.systemui.plugins.ActivityStarter, com.android.systemui.broadcast.BroadcastDispatcher, com.android.internal.logging.UiEventLogger, com.android.systemui.plugins.FalsingManager, com.android.systemui.telephony.TelephonyListenerManager, com.android.systemui.util.settings.SecureSettings, java.util.concurrent.Executor, java.util.concurrent.Executor, com.android.internal.jank.InteractionJankMonitor, com.android.internal.util.LatencyTracker, com.android.systemui.dump.DumpManager, dagger.Lazy, com.android.systemui.animation.DialogLaunchAnimator):void");
    }

    @VisibleForTesting
    public void addAdapter(WeakReference<BaseUserAdapter> weakReference) {
        this.mAdapters.add(weakReference);
    }

    public final void checkIfAddUserDisallowedByAdminOnly(UserRecord userRecord) {
        RestrictedLockUtils.EnforcedAdmin checkIfRestrictionEnforced = RestrictedLockUtilsInternal.checkIfRestrictionEnforced(this.mContext, "no_add_user", this.mUserTracker.getUserId());
        if (checkIfRestrictionEnforced == null || RestrictedLockUtilsInternal.hasBaseUserRestriction(this.mContext, "no_add_user", this.mUserTracker.getUserId())) {
            userRecord.isDisabledByAdmin = false;
            userRecord.enforcedAdmin = null;
            return;
        }
        userRecord.isDisabledByAdmin = true;
        userRecord.enforcedAdmin = checkIfRestrictionEnforced;
    }

    public final int createGuest() {
        try {
            UserManager userManager = this.mUserManager;
            Context context = this.mContext;
            UserInfo createGuest = userManager.createGuest(context, context.getString(2131952416));
            if (createGuest != null) {
                return createGuest.id;
            }
            Log.e("UserSwitcherController", "Couldn't create guest, most likely because there already exists one");
            return -10000;
        } catch (UserManager.UserOperationException e) {
            Log.e("UserSwitcherController", "Couldn't create guest user", e);
            return -10000;
        }
    }

    public final boolean currentUserCanCreateUsers() {
        UserInfo userInfo = this.mUserTracker.getUserInfo();
        if (userInfo == null || ((!userInfo.isAdmin() && this.mUserTracker.getUserId() != 0) || !(!this.mUserManager.hasBaseUserRestriction("no_add_user", UserHandle.SYSTEM)))) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        StringBuilder m = LockIconView$$ExternalSyntheticOutline0.m(printWriter, "UserSwitcherController state:", "  mLastNonGuestUser=");
        m.append(this.mLastNonGuestUser);
        printWriter.println(m.toString());
        printWriter.print("  mUsers.size=");
        printWriter.println(this.mUsers.size());
        for (int i = 0; i < this.mUsers.size(); i++) {
            printWriter.print("    ");
            printWriter.println(this.mUsers.get(i).toString());
        }
        StringBuilder m2 = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(VendorAtomValue$$ExternalSyntheticOutline1.m("mSimpleUserSwitcher="), this.mSimpleUserSwitcher, printWriter, "mGuestUserAutoCreated=");
        m2.append(this.mGuestUserAutoCreated);
        printWriter.println(m2.toString());
    }

    public final String getCurrentUserName() {
        UserRecord orElse;
        UserInfo userInfo;
        if (this.mUsers.isEmpty() || (orElse = this.mUsers.stream().filter(PeopleTileViewHelper$$ExternalSyntheticLambda4.INSTANCE$1).findFirst().orElse(null)) == null || (userInfo = orElse.info) == null) {
            return null;
        }
        if (orElse.isGuest) {
            return this.mContext.getString(2131952416);
        }
        return userInfo.name;
    }

    public final boolean isDeviceAllowedToAddGuest() {
        if (!this.mDeviceProvisionedController.isDeviceProvisioned() || this.mDevicePolicyManager.isDeviceManaged()) {
            return false;
        }
        return true;
    }

    public final void notifyAdapters() {
        for (int size = this.mAdapters.size() - 1; size >= 0; size--) {
            BaseUserAdapter baseUserAdapter = this.mAdapters.get(size).get();
            if (baseUserAdapter != null) {
                baseUserAdapter.notifyDataSetChanged();
            } else {
                this.mAdapters.remove(size);
            }
        }
    }

    @VisibleForTesting
    public void onUserListItemClicked(UserRecord userRecord, UserSwitchDialogController.DialogShower dialogShower) {
        int i;
        UserInfo userInfo;
        int i2;
        UserInfo userInfo2;
        int i3 = 0;
        if (userRecord.isGuest && userRecord.info == null) {
            i = createGuest();
            if (i != -10000) {
                this.mUiEventLogger.log(QSUserSwitcherEvent.QS_USER_GUEST_ADD);
            } else {
                return;
            }
        } else if (userRecord.isAddUser) {
            Dialog dialog = this.mAddUserDialog;
            if (dialog != null && dialog.isShowing()) {
                this.mAddUserDialog.cancel();
            }
            AddUserDialog addUserDialog = new AddUserDialog(this.mContext);
            this.mAddUserDialog = addUserDialog;
            if (dialogShower != null) {
                dialogShower.showDialog(addUserDialog);
                return;
            } else {
                addUserDialog.show();
                return;
            }
        } else if (userRecord.isAddSupervisedUser) {
            Intent addFlags = new Intent().setAction("android.os.action.CREATE_SUPERVISED_USER").setPackage(this.mCreateSupervisedUserPackage).addFlags(268435456);
            if (this.mContext.getPackageManager().resolveActivity(addFlags, 0) == null) {
                addFlags.setPackage(null).setClassName(ThemeOverlayApplier.SETTINGS_PACKAGE, "com.android.settings.users.AddSupervisedUserActivity");
            }
            this.mContext.startActivity(addFlags);
            return;
        } else {
            i = userRecord.info.id;
        }
        int userId = this.mUserTracker.getUserId();
        if (userId == i) {
            if (userRecord.isGuest) {
                if (this.mResumeUserOnGuestLogout && (i2 = this.mLastNonGuestUser) != 0 && (userInfo2 = this.mUserManager.getUserInfo(i2)) != null && userInfo2.isEnabled() && userInfo2.supportsSwitchToByUser()) {
                    i3 = userInfo2.id;
                }
                showExitGuestDialog(i, i3, dialogShower);
            }
        } else if (!UserManager.isGuestUserEphemeral() || (userInfo = this.mUserManager.getUserInfo(userId)) == null || !userInfo.isGuest()) {
            if (dialogShower != null) {
                dialogShower.dismiss();
            }
            switchToUserId(i);
        } else {
            showExitGuestDialog(userId, userRecord.resolveId(), dialogShower);
        }
    }

    public final void refreshUsers(int i) {
        UserInfo userInfo;
        if (i != -10000) {
            this.mForcePictureLoadForUserId.put(i, true);
        }
        if (!this.mPauseRefreshUsers) {
            boolean z = this.mForcePictureLoadForUserId.get(-1);
            SparseArray sparseArray = new SparseArray(this.mUsers.size());
            int size = this.mUsers.size();
            for (int i2 = 0; i2 < size; i2++) {
                UserRecord userRecord = this.mUsers.get(i2);
                if (!(userRecord == null || userRecord.picture == null || (userInfo = userRecord.info) == null || z || this.mForcePictureLoadForUserId.get(userInfo.id))) {
                    sparseArray.put(userRecord.info.id, userRecord.picture);
                }
            }
            this.mForcePictureLoadForUserId.clear();
            this.mBgExecutor.execute(new ScrimView$$ExternalSyntheticLambda1(this, sparseArray, 2));
        }
    }

    public final void removeGuestUser(int i, int i2) {
        UserInfo userInfo = this.mUserTracker.getUserInfo();
        if (userInfo.id != i) {
            StringBuilder m = ExifInterface$$ExternalSyntheticOutline0.m("User requesting to start a new session (", i, ") is not current user (");
            m.append(userInfo.id);
            m.append(")");
            Log.w("UserSwitcherController", m.toString());
        } else if (!userInfo.isGuest()) {
            Log.w("UserSwitcherController", "User requesting to start a new session (" + i + ") is not a guest");
        } else if (!this.mUserManager.markGuestForDeletion(userInfo.id)) {
            GridLayoutManager$$ExternalSyntheticOutline1.m("Couldn't mark the guest for deletion for user ", i, "UserSwitcherController");
        } else {
            try {
                if (i2 == -10000) {
                    int createGuest = createGuest();
                    if (createGuest == -10000) {
                        Log.e("UserSwitcherController", "Could not create new guest, switching back to system user");
                        switchToUserId(0);
                        this.mUserManager.removeUser(userInfo.id);
                        WindowManagerGlobal.getWindowManagerService().lockNow((Bundle) null);
                        return;
                    }
                    switchToUserId(createGuest);
                    this.mUserManager.removeUser(userInfo.id);
                    return;
                }
                if (this.mGuestUserAutoCreated) {
                    this.mGuestIsResetting.set(true);
                }
                switchToUserId(i2);
                this.mUserManager.removeUser(userInfo.id);
            } catch (RemoteException unused) {
                Log.e("UserSwitcherController", "Couldn't remove guest because ActivityManager or WindowManager is dead");
            }
        }
    }

    public final boolean shouldUseSimpleUserSwitcher() {
        if (Settings.Global.getInt(this.mContext.getContentResolver(), "lockscreenSimpleUserSwitcher", this.mContext.getResources().getBoolean(17891651) ? 1 : 0) != 0) {
            return true;
        }
        return false;
    }

    public final void showExitGuestDialog(int i, int i2, UserSwitchDialogController.DialogShower dialogShower) {
        AlertDialog alertDialog = this.mExitGuestDialog;
        if (alertDialog != null && alertDialog.isShowing()) {
            this.mExitGuestDialog.cancel();
        }
        ExitGuestDialog exitGuestDialog = new ExitGuestDialog(this.mContext, i, i2);
        this.mExitGuestDialog = exitGuestDialog;
        if (dialogShower != null) {
            dialogShower.showDialog(exitGuestDialog);
        } else {
            exitGuestDialog.show();
        }
    }

    public final void switchToUserId(int i) {
        try {
            this.mInteractionJankMonitor.begin(InteractionJankMonitor.Configuration.Builder.withView(37, this.mView).setTimeout(20000L));
            this.mLatencyTracker.onActionStart(12);
            if (!this.mPauseRefreshUsers) {
                this.mHandler.postDelayed(this.mUnpauseRefreshUsers, 3000L);
                this.mPauseRefreshUsers = true;
            }
            this.mActivityManager.switchUser(i);
        } catch (RemoteException e) {
            Log.e("UserSwitcherController", "Couldn't switch user.", e);
        }
    }

    public final void guaranteeGuestPresent() {
        if (isDeviceAllowedToAddGuest() && this.mUserManager.findCurrentGuestUser() == null && this.mGuestCreationScheduled.compareAndSet(false, true)) {
            this.mBgExecutor.execute(new KeyguardUpdateMonitor$$ExternalSyntheticLambda7(this, 3));
        }
    }

    @VisibleForTesting
    public KeyguardStateController getKeyguardStateController() {
        return this.mKeyguardStateController;
    }

    @VisibleForTesting
    public ArrayList<UserRecord> getUsers() {
        return this.mUsers;
    }
}
