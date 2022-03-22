package com.android.settingslib.users;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.UserHandle;
import android.widget.ImageView;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import java.io.File;
/* loaded from: classes.dex */
public final class EditUserInfoController {
    public AlertDialog mEditUserInfoDialog;
    public EditUserPhotoController mEditUserPhotoController;
    public Bitmap mSavedPhoto;
    public boolean mWaitingForActivityResult = false;
    public final String mFileAuthority = "com.android.systemui.fileprovider";

    public final void clear() {
        EditUserPhotoController editUserPhotoController = this.mEditUserPhotoController;
        if (editUserPhotoController != null) {
            new File(editUserPhotoController.mImagesDir, "NewUserPhoto.png").delete();
        }
        this.mEditUserInfoDialog = null;
        this.mSavedPhoto = null;
    }

    public EditUserPhotoController createEditUserPhotoController(Activity activity, ActivityStarter activityStarter, ImageView imageView) {
        return new EditUserPhotoController(activity, activityStarter, imageView, this.mSavedPhoto, this.mFileAuthority);
    }

    public RestrictedLockUtils.EnforcedAdmin getChangePhotoAdminRestriction(Context context) {
        return RestrictedLockUtilsInternal.checkIfRestrictionEnforced(context, "no_set_user_icon", UserHandle.myUserId());
    }

    public boolean isChangePhotoRestrictedByBase(Context context) {
        return RestrictedLockUtilsInternal.hasBaseUserRestriction(context, "no_set_user_icon", UserHandle.myUserId());
    }
}
