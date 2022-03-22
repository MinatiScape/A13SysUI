package com.android.systemui.statusbar.policy;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.ContactsContract;
import android.util.Log;
import com.android.internal.util.UserIcons;
import com.android.settingslib.drawable.UserIconDrawable;
import com.android.systemui.statusbar.policy.UserInfoController;
import com.android.systemui.theme.ThemeOverlayApplier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class UserInfoControllerImpl implements UserInfoController {
    public final ArrayList<UserInfoController.OnUserInfoChangedListener> mCallbacks = new ArrayList<>();
    public final Context mContext;
    public final AnonymousClass2 mProfileReceiver;
    public final AnonymousClass1 mReceiver;
    public String mUserAccount;
    public Drawable mUserDrawable;
    public AsyncTask<Void, Void, UserInfoQueryResult> mUserInfoTask;
    public String mUserName;

    /* loaded from: classes.dex */
    public static class UserInfoQueryResult {
        public Drawable mAvatar;
        public String mName;
        public String mUserAccount;

        public UserInfoQueryResult(String str, Drawable drawable, String str2) {
            this.mName = str;
            this.mAvatar = drawable;
            this.mUserAccount = str2;
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(UserInfoController.OnUserInfoChangedListener onUserInfoChangedListener) {
        UserInfoController.OnUserInfoChangedListener onUserInfoChangedListener2 = onUserInfoChangedListener;
        this.mCallbacks.add(onUserInfoChangedListener2);
        onUserInfoChangedListener2.onUserInfoChanged(this.mUserName, this.mUserDrawable);
    }

    @Override // com.android.systemui.statusbar.policy.UserInfoController
    public final void reloadUserInfo() {
        final boolean z;
        AsyncTask<Void, Void, UserInfoQueryResult> asyncTask = this.mUserInfoTask;
        if (asyncTask != null) {
            asyncTask.cancel(false);
            this.mUserInfoTask = null;
        }
        try {
            UserInfo currentUser = ActivityManager.getService().getCurrentUser();
            final Context createPackageContextAsUser = this.mContext.createPackageContextAsUser(ThemeOverlayApplier.ANDROID_PACKAGE, 0, new UserHandle(currentUser.id));
            final int i = currentUser.id;
            final boolean isGuest = currentUser.isGuest();
            final String str = currentUser.name;
            if (this.mContext.getThemeResId() != 2132018190) {
                z = true;
            } else {
                z = false;
            }
            Resources resources = this.mContext.getResources();
            final int max = Math.max(resources.getDimensionPixelSize(2131166580), resources.getDimensionPixelSize(2131166581));
            AsyncTask<Void, Void, UserInfoQueryResult> asyncTask2 = new AsyncTask<Void, Void, UserInfoQueryResult>() { // from class: com.android.systemui.statusbar.policy.UserInfoControllerImpl.3
                @Override // android.os.AsyncTask
                public final UserInfoQueryResult doInBackground(Void[] voidArr) {
                    UserIconDrawable userIconDrawable;
                    Cursor query;
                    int i2;
                    UserManager userManager = UserManager.get(UserInfoControllerImpl.this.mContext);
                    String str2 = str;
                    Bitmap userIcon = userManager.getUserIcon(i);
                    if (userIcon != null) {
                        UserIconDrawable userIconDrawable2 = new UserIconDrawable(max);
                        userIconDrawable2.setIcon(userIcon);
                        userIconDrawable2.setBadgeIfManagedUser(UserInfoControllerImpl.this.mContext, i);
                        if (userIconDrawable2.mSize > 0) {
                            int i3 = userIconDrawable2.mSize;
                            userIconDrawable2.onBoundsChange(new Rect(0, 0, i3, i3));
                            userIconDrawable2.rebake();
                            userIconDrawable2.mFrameColor = null;
                            userIconDrawable2.mFramePaint = null;
                            userIconDrawable2.mClearPaint = null;
                            Drawable drawable = userIconDrawable2.mUserDrawable;
                            if (drawable != null) {
                                drawable.setCallback(null);
                                userIconDrawable2.mUserDrawable = null;
                                userIconDrawable = userIconDrawable2;
                            } else {
                                Bitmap bitmap = userIconDrawable2.mUserIcon;
                                userIconDrawable = userIconDrawable2;
                                if (bitmap != null) {
                                    bitmap.recycle();
                                    userIconDrawable2.mUserIcon = null;
                                    userIconDrawable = userIconDrawable2;
                                }
                            }
                        } else {
                            throw new IllegalStateException("Baking requires an explicit intrinsic size");
                        }
                    } else {
                        Resources resources2 = createPackageContextAsUser.getResources();
                        if (isGuest) {
                            i2 = -10000;
                        } else {
                            i2 = i;
                        }
                        userIconDrawable = UserIcons.getDefaultUserIcon(resources2, i2, z);
                    }
                    if (userManager.getUsers().size() <= 1 && (query = createPackageContextAsUser.getContentResolver().query(ContactsContract.Profile.CONTENT_URI, new String[]{"_id", "display_name"}, null, null, null)) != null) {
                        try {
                            if (query.moveToFirst()) {
                                str2 = query.getString(query.getColumnIndex("display_name"));
                            }
                        } finally {
                            query.close();
                        }
                    }
                    return new UserInfoQueryResult(str2, userIconDrawable, userManager.getUserAccount(i));
                }

                @Override // android.os.AsyncTask
                public final void onPostExecute(UserInfoQueryResult userInfoQueryResult) {
                    UserInfoQueryResult userInfoQueryResult2 = userInfoQueryResult;
                    UserInfoControllerImpl userInfoControllerImpl = UserInfoControllerImpl.this;
                    Objects.requireNonNull(userInfoQueryResult2);
                    userInfoControllerImpl.mUserName = userInfoQueryResult2.mName;
                    UserInfoControllerImpl userInfoControllerImpl2 = UserInfoControllerImpl.this;
                    userInfoControllerImpl2.mUserDrawable = userInfoQueryResult2.mAvatar;
                    userInfoControllerImpl2.mUserAccount = userInfoQueryResult2.mUserAccount;
                    userInfoControllerImpl2.mUserInfoTask = null;
                    Iterator<UserInfoController.OnUserInfoChangedListener> it = userInfoControllerImpl2.mCallbacks.iterator();
                    while (it.hasNext()) {
                        it.next().onUserInfoChanged(userInfoControllerImpl2.mUserName, userInfoControllerImpl2.mUserDrawable);
                    }
                }
            };
            this.mUserInfoTask = asyncTask2;
            asyncTask2.execute(new Void[0]);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("UserInfoController", "Couldn't create user context", e);
            throw new RuntimeException(e);
        } catch (RemoteException e2) {
            Log.e("UserInfoController", "Couldn't get user info", e2);
            throw new RuntimeException(e2);
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(UserInfoController.OnUserInfoChangedListener onUserInfoChangedListener) {
        this.mCallbacks.remove(onUserInfoChangedListener);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.systemui.statusbar.policy.UserInfoControllerImpl$1, android.content.BroadcastReceiver] */
    /* JADX WARN: Type inference failed for: r2v0, types: [com.android.systemui.statusbar.policy.UserInfoControllerImpl$2, android.content.BroadcastReceiver] */
    /* JADX WARN: Unknown variable types count: 2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public UserInfoControllerImpl(android.content.Context r9) {
        /*
            r8 = this;
            r8.<init>()
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r8.mCallbacks = r0
            com.android.systemui.statusbar.policy.UserInfoControllerImpl$1 r0 = new com.android.systemui.statusbar.policy.UserInfoControllerImpl$1
            r0.<init>()
            r8.mReceiver = r0
            com.android.systemui.statusbar.policy.UserInfoControllerImpl$2 r2 = new com.android.systemui.statusbar.policy.UserInfoControllerImpl$2
            r2.<init>()
            r8.mProfileReceiver = r2
            r8.mContext = r9
            android.content.IntentFilter r8 = new android.content.IntentFilter
            r8.<init>()
            java.lang.String r1 = "android.intent.action.USER_SWITCHED"
            r8.addAction(r1)
            r9.registerReceiver(r0, r8)
            android.content.IntentFilter r4 = new android.content.IntentFilter
            r4.<init>()
            java.lang.String r8 = "android.provider.Contacts.PROFILE_CHANGED"
            r4.addAction(r8)
            java.lang.String r8 = "android.intent.action.USER_INFO_CHANGED"
            r4.addAction(r8)
            android.os.UserHandle r3 = android.os.UserHandle.ALL
            r5 = 0
            r6 = 0
            r7 = 2
            r1 = r9
            r1.registerReceiverAsUser(r2, r3, r4, r5, r6, r7)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.UserInfoControllerImpl.<init>(android.content.Context):void");
    }
}
