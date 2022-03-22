package com.android.settingslib;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.UserInfo;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import com.android.settingslib.RestrictedLockUtils;
import java.util.List;
/* loaded from: classes.dex */
public final class RestrictedLockUtilsInternal extends RestrictedLockUtils {
    public static final boolean DEBUG = Log.isLoggable("RestrictedLockUtils", 3);
    public static Proxy sProxy = new Proxy();

    /* loaded from: classes.dex */
    public static class Proxy {
    }

    public static RestrictedLockUtils.EnforcedAdmin checkIfRestrictionEnforced(Context context, String str, int i) {
        ComponentName deviceOwnerComponentOnAnyUser;
        if (((DevicePolicyManager) context.getSystemService("device_policy")) == null) {
            return null;
        }
        UserManager userManager = UserManager.get(context);
        UserHandle of = UserHandle.of(i);
        List userRestrictionSources = userManager.getUserRestrictionSources(str, of);
        if (userRestrictionSources.isEmpty()) {
            return null;
        }
        int size = userRestrictionSources.size();
        if (size > 1) {
            RestrictedLockUtils.EnforcedAdmin enforcedAdmin = new RestrictedLockUtils.EnforcedAdmin();
            enforcedAdmin.enforcedRestriction = str;
            enforcedAdmin.user = of;
            if (DEBUG) {
                Log.d("RestrictedLockUtils", "Multiple (" + size + ") enforcing users for restriction '" + str + "' on user " + of + "; returning default admin (" + enforcedAdmin + ")");
            }
            return enforcedAdmin;
        }
        int userRestrictionSource = ((UserManager.EnforcingUser) userRestrictionSources.get(0)).getUserRestrictionSource();
        int identifier = ((UserManager.EnforcingUser) userRestrictionSources.get(0)).getUserHandle().getIdentifier();
        if (userRestrictionSource == 4) {
            if (identifier == i) {
                return getProfileOwner(context, str, identifier);
            }
            UserInfo profileParent = userManager.getProfileParent(identifier);
            if (profileParent != null && profileParent.id == i) {
                return getProfileOwner(context, str, identifier);
            }
            RestrictedLockUtils.EnforcedAdmin enforcedAdmin2 = new RestrictedLockUtils.EnforcedAdmin();
            enforcedAdmin2.enforcedRestriction = str;
            return enforcedAdmin2;
        } else if (userRestrictionSource != 2) {
            return null;
        } else {
            if (identifier == i) {
                DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService("device_policy");
                if (devicePolicyManager == null || (deviceOwnerComponentOnAnyUser = devicePolicyManager.getDeviceOwnerComponentOnAnyUser()) == null) {
                    return null;
                }
                return new RestrictedLockUtils.EnforcedAdmin(deviceOwnerComponentOnAnyUser, str, devicePolicyManager.getDeviceOwnerUser());
            }
            RestrictedLockUtils.EnforcedAdmin enforcedAdmin3 = new RestrictedLockUtils.EnforcedAdmin();
            enforcedAdmin3.enforcedRestriction = str;
            return enforcedAdmin3;
        }
    }

    public static RestrictedLockUtils.EnforcedAdmin getProfileOwner(Context context, String str, int i) {
        DevicePolicyManager devicePolicyManager;
        ComponentName profileOwnerAsUser;
        UserHandle userHandle = null;
        if (i == -10000 || (devicePolicyManager = (DevicePolicyManager) context.getSystemService("device_policy")) == null || (profileOwnerAsUser = devicePolicyManager.getProfileOwnerAsUser(i)) == null) {
            return null;
        }
        if (i != -10000) {
            userHandle = UserHandle.of(i);
        }
        return new RestrictedLockUtils.EnforcedAdmin(profileOwnerAsUser, str, userHandle);
    }

    public static boolean hasBaseUserRestriction(Context context, String str, int i) {
        return ((UserManager) context.getSystemService("user")).hasBaseUserRestriction(str, UserHandle.of(i));
    }
}
