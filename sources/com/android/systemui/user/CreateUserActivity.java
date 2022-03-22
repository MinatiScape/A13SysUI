package com.android.systemui.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.IActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import androidx.recyclerview.R$dimen;
import com.android.settingslib.users.EditUserInfoController;
import com.android.settingslib.users.EditUserPhotoController;
import com.android.systemui.navigationbar.NavigationBarView$$ExternalSyntheticLambda0;
import com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda3;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
/* loaded from: classes.dex */
public class CreateUserActivity extends Activity {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final IActivityManager mActivityManager;
    public final EditUserInfoController mEditUserInfoController;
    public AlertDialog mSetupUserDialog;
    public final UserCreator mUserCreator;

    @Override // android.app.Activity
    public final void onSaveInstanceState(Bundle bundle) {
        EditUserPhotoController editUserPhotoController;
        AlertDialog alertDialog = this.mSetupUserDialog;
        if (alertDialog != null && alertDialog.isShowing()) {
            bundle.putBundle("create_user_dialog_state", this.mSetupUserDialog.onSaveInstanceState());
        }
        EditUserInfoController editUserInfoController = this.mEditUserInfoController;
        Objects.requireNonNull(editUserInfoController);
        if (!(editUserInfoController.mEditUserInfoDialog == null || (editUserPhotoController = editUserInfoController.mEditUserPhotoController) == null)) {
            File file = null;
            if (editUserPhotoController.mNewUserPhotoBitmap != null) {
                try {
                    File file2 = new File(editUserPhotoController.mImagesDir, "NewUserPhoto.png");
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    editUserPhotoController.mNewUserPhotoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    file = file2;
                } catch (IOException e) {
                    Log.e("EditUserPhotoController", "Cannot create temp file", e);
                }
            }
            if (file != null) {
                bundle.putString("pending_photo", file.getPath());
            }
        }
        bundle.putBoolean("awaiting_result", editUserInfoController.mWaitingForActivityResult);
        super.onSaveInstanceState(bundle);
    }

    public CreateUserActivity(UserCreator userCreator, EditUserInfoController editUserInfoController, IActivityManager iActivityManager) {
        this.mUserCreator = userCreator;
        this.mEditUserInfoController = editUserInfoController;
        this.mActivityManager = iActivityManager;
    }

    @Override // android.app.Activity
    public final void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        EditUserInfoController editUserInfoController = this.mEditUserInfoController;
        Objects.requireNonNull(editUserInfoController);
        editUserInfoController.mWaitingForActivityResult = false;
        EditUserPhotoController editUserPhotoController = editUserInfoController.mEditUserPhotoController;
        if (editUserPhotoController != null && editUserInfoController.mEditUserInfoDialog != null && i2 == -1 && i == 1004) {
            if (intent.hasExtra("default_icon_tint_color")) {
                try {
                    R$dimen.postOnBackgroundThread(new OverviewProxyService$1$$ExternalSyntheticLambda3(editUserPhotoController, intent.getIntExtra("default_icon_tint_color", -1), 1)).get();
                } catch (InterruptedException | ExecutionException e) {
                    Log.e("EditUserPhotoController", "Error processing default icon", e);
                }
            } else if (intent.getData() != null) {
                R$dimen.postOnBackgroundThread(new NavigationBarView$$ExternalSyntheticLambda0(editUserPhotoController, intent.getData(), 1));
            }
        }
    }

    @Override // android.app.Activity
    public final void onBackPressed() {
        super.onBackPressed();
        AlertDialog alertDialog = this.mSetupUserDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    /* JADX WARN: Type inference failed for: r5v0, types: [com.android.systemui.user.CreateUserActivity$$ExternalSyntheticLambda3] */
    /* JADX WARN: Unknown variable types count: 1 */
    @Override // android.app.Activity
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onCreate(android.os.Bundle r14) {
        /*
            r13 = this;
            super.onCreate(r14)
            r0 = 1
            r13.setShowWhenLocked(r0)
            r1 = 2131623986(0x7f0e0032, float:1.8875139E38)
            r13.setContentView(r1)
            r1 = 0
            if (r14 == 0) goto L_0x0034
            com.android.settingslib.users.EditUserInfoController r2 = r13.mEditUserInfoController
            java.util.Objects.requireNonNull(r2)
            java.lang.String r3 = "pending_photo"
            java.lang.String r3 = r14.getString(r3)
            if (r3 == 0) goto L_0x002c
            java.io.File r4 = new java.io.File
            r4.<init>(r3)
            java.lang.String r3 = r4.getAbsolutePath()
            android.graphics.Bitmap r3 = android.graphics.BitmapFactory.decodeFile(r3)
            r2.mSavedPhoto = r3
        L_0x002c:
            java.lang.String r3 = "awaiting_result"
            boolean r14 = r14.getBoolean(r3, r1)
            r2.mWaitingForActivityResult = r14
        L_0x0034:
            r14 = 2131953479(0x7f130747, float:1.954343E38)
            java.lang.String r14 = r13.getString(r14)
            com.android.settingslib.users.EditUserInfoController r2 = r13.mEditUserInfoController
            com.android.systemui.user.CreateUserActivity$$ExternalSyntheticLambda0 r3 = new com.android.systemui.user.CreateUserActivity$$ExternalSyntheticLambda0
            r3.<init>(r13)
            r4 = 2131953464(0x7f130738, float:1.95434E38)
            java.lang.String r4 = r13.getString(r4)
            com.android.systemui.user.CreateUserActivity$$ExternalSyntheticLambda3 r5 = new com.android.systemui.user.CreateUserActivity$$ExternalSyntheticLambda3
            r5.<init>()
            com.android.systemui.user.CreateUserActivity$$ExternalSyntheticLambda1 r6 = new com.android.systemui.user.CreateUserActivity$$ExternalSyntheticLambda1
            r6.<init>(r13, r1)
            java.util.Objects.requireNonNull(r2)
            android.view.LayoutInflater r7 = android.view.LayoutInflater.from(r13)
            r8 = 2131624093(0x7f0e009d, float:1.8875356E38)
            r9 = 0
            android.view.View r7 = r7.inflate(r8, r9)
            r8 = 2131429169(0x7f0b0731, float:1.8480003E38)
            android.view.View r8 = r7.findViewById(r8)
            android.widget.EditText r8 = (android.widget.EditText) r8
            r8.setText(r14)
            r9 = 2131429170(0x7f0b0732, float:1.8480005E38)
            android.view.View r9 = r7.findViewById(r9)
            android.widget.ImageView r9 = (android.widget.ImageView) r9
            android.content.res.Resources r10 = r13.getResources()
            r11 = -10000(0xffffffffffffd8f0, float:NaN)
            android.graphics.drawable.Drawable r10 = com.android.internal.util.UserIcons.getDefaultUserIcon(r10, r11, r1)
            android.graphics.Bitmap r11 = r2.mSavedPhoto
            if (r11 == 0) goto L_0x0098
            int r10 = com.android.settingslib.drawable.CircleFramedDrawable.$r8$clinit
            android.content.res.Resources r10 = r13.getResources()
            r12 = 17105621(0x10502d5, float:2.4430274E-38)
            int r10 = r10.getDimensionPixelSize(r12)
            com.android.settingslib.drawable.CircleFramedDrawable r12 = new com.android.settingslib.drawable.CircleFramedDrawable
            r12.<init>(r11, r10)
            r10 = r12
        L_0x0098:
            r9.setImageDrawable(r10)
            boolean r10 = r2.isChangePhotoRestrictedByBase(r13)
            if (r10 == 0) goto L_0x00ae
            r1 = 2131427462(0x7f0b0086, float:1.847654E38)
            android.view.View r1 = r7.findViewById(r1)
            r3 = 8
            r1.setVisibility(r3)
            goto L_0x00c3
        L_0x00ae:
            com.android.settingslib.RestrictedLockUtils$EnforcedAdmin r10 = r2.getChangePhotoAdminRestriction(r13)
            if (r10 == 0) goto L_0x00bd
            com.android.settingslib.users.EditUserInfoController$$ExternalSyntheticLambda3 r3 = new com.android.settingslib.users.EditUserInfoController$$ExternalSyntheticLambda3
            r3.<init>(r13, r10, r1)
            r9.setOnClickListener(r3)
            goto L_0x00c3
        L_0x00bd:
            com.android.settingslib.users.EditUserPhotoController r1 = r2.createEditUserPhotoController(r13, r3, r9)
            r2.mEditUserPhotoController = r1
        L_0x00c3:
            android.app.AlertDialog$Builder r1 = new android.app.AlertDialog$Builder
            r1.<init>(r13)
            android.app.AlertDialog$Builder r1 = r1.setTitle(r4)
            android.app.AlertDialog$Builder r1 = r1.setView(r7)
            android.app.AlertDialog$Builder r0 = r1.setCancelable(r0)
            com.android.settingslib.users.EditUserInfoController$$ExternalSyntheticLambda1 r1 = new com.android.settingslib.users.EditUserInfoController$$ExternalSyntheticLambda1
            r1.<init>()
            r14 = 17039370(0x104000a, float:2.42446E-38)
            android.app.AlertDialog$Builder r14 = r0.setPositiveButton(r14, r1)
            com.android.settingslib.users.EditUserInfoController$$ExternalSyntheticLambda2 r0 = new com.android.settingslib.users.EditUserInfoController$$ExternalSyntheticLambda2
            r0.<init>()
            r1 = 17039360(0x1040000, float:2.424457E-38)
            android.app.AlertDialog$Builder r14 = r14.setNegativeButton(r1, r0)
            com.android.settingslib.users.EditUserInfoController$$ExternalSyntheticLambda0 r0 = new com.android.settingslib.users.EditUserInfoController$$ExternalSyntheticLambda0
            r0.<init>()
            android.app.AlertDialog$Builder r14 = r14.setOnCancelListener(r0)
            android.app.AlertDialog r14 = r14.create()
            r2.mEditUserInfoDialog = r14
            android.view.Window r14 = r14.getWindow()
            r0 = 4
            r14.setSoftInputMode(r0)
            android.app.AlertDialog r14 = r2.mEditUserInfoDialog
            r13.mSetupUserDialog = r14
            r14.show()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.user.CreateUserActivity.onCreate(android.os.Bundle):void");
    }

    @Override // android.app.Activity
    public final void onRestoreInstanceState(Bundle bundle) {
        AlertDialog alertDialog;
        super.onRestoreInstanceState(bundle);
        Bundle bundle2 = bundle.getBundle("create_user_dialog_state");
        if (bundle2 != null && (alertDialog = this.mSetupUserDialog) != null) {
            alertDialog.onRestoreInstanceState(bundle2);
        }
    }
}
