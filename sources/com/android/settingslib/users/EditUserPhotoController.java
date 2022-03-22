package com.android.settingslib.users;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import com.android.settingslib.drawable.CircleFramedDrawable;
import com.android.systemui.user.CreateUserActivity;
import com.android.systemui.user.CreateUserActivity$$ExternalSyntheticLambda0;
import java.io.File;
import java.util.Objects;
/* loaded from: classes.dex */
public final class EditUserPhotoController {
    public final Activity mActivity;
    public final ActivityStarter mActivityStarter;
    public final String mFileAuthority;
    public final ImageView mImageView;
    public final File mImagesDir;
    public Bitmap mNewUserPhotoBitmap;
    public CircleFramedDrawable mNewUserPhotoDrawable;

    public EditUserPhotoController(Activity activity, ActivityStarter activityStarter, ImageView imageView, Bitmap bitmap, String str) {
        this.mActivity = activity;
        this.mActivityStarter = activityStarter;
        this.mFileAuthority = str;
        File file = new File(activity.getCacheDir(), "multi_user");
        this.mImagesDir = file;
        file.mkdir();
        this.mImageView = imageView;
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.android.settingslib.users.EditUserPhotoController$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                EditUserPhotoController editUserPhotoController = EditUserPhotoController.this;
                Objects.requireNonNull(editUserPhotoController);
                Intent intent = new Intent(editUserPhotoController.mImageView.getContext(), AvatarPickerActivity.class);
                intent.putExtra("file_authority", editUserPhotoController.mFileAuthority);
                CreateUserActivity$$ExternalSyntheticLambda0 createUserActivity$$ExternalSyntheticLambda0 = (CreateUserActivity$$ExternalSyntheticLambda0) editUserPhotoController.mActivityStarter;
                Objects.requireNonNull(createUserActivity$$ExternalSyntheticLambda0);
                CreateUserActivity createUserActivity = createUserActivity$$ExternalSyntheticLambda0.f$0;
                int i = CreateUserActivity.$r8$clinit;
                Objects.requireNonNull(createUserActivity);
                EditUserInfoController editUserInfoController = createUserActivity.mEditUserInfoController;
                Objects.requireNonNull(editUserInfoController);
                editUserInfoController.mWaitingForActivityResult = true;
                createUserActivity.startActivityForResult(intent, 1004);
            }
        });
        this.mNewUserPhotoBitmap = bitmap;
    }
}
