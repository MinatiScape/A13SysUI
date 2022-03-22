package com.android.systemui.user;

import android.content.Context;
import android.os.UserManager;
/* loaded from: classes.dex */
public final class UserCreator {
    public final Context mContext;
    public final UserManager mUserManager;

    public UserCreator(Context context, UserManager userManager) {
        this.mContext = context;
        this.mUserManager = userManager;
    }
}
