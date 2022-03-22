package com.android.settingslib.users;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
/* loaded from: classes.dex */
public final class AvatarPhotoController {
    public final AvatarPickerActivity mActivity;
    public final Uri mCropPictureUri;
    public final String mFileAuthority;
    public final File mImagesDir;
    public final int mPhotoSize;
    public final Uri mTakePictureUri;

    /* renamed from: com.android.settingslib.users.AvatarPhotoController$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass2 extends AsyncTask<Void, Void, Bitmap> {
        public final /* synthetic */ Uri val$data;

        public AnonymousClass2(Uri uri) {
            this.val$data = uri;
        }

        @Override // android.os.AsyncTask
        public final Bitmap doInBackground(Void[] voidArr) {
            int i;
            int i2 = AvatarPhotoController.this.mPhotoSize;
            Bitmap createBitmap = Bitmap.createBitmap(i2, i2, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            try {
                Bitmap decodeStream = BitmapFactory.decodeStream(AvatarPhotoController.this.mActivity.getContentResolver().openInputStream(this.val$data));
                if (decodeStream != null) {
                    int i3 = -1;
                    try {
                        i3 = new ExifInterface(AvatarPhotoController.this.mActivity.getContentResolver().openInputStream(this.val$data)).getAttributeInt("Orientation", -1);
                    } catch (IOException e) {
                        Log.e("AvatarPhotoController", "Error while getting rotation", e);
                    }
                    if (i3 == 3) {
                        i = 180;
                    } else if (i3 == 6) {
                        i = 90;
                    } else if (i3 != 8) {
                        i = 0;
                    } else {
                        i = 270;
                    }
                    int min = Math.min(decodeStream.getWidth(), decodeStream.getHeight());
                    int width = (decodeStream.getWidth() - min) / 2;
                    int height = (decodeStream.getHeight() - min) / 2;
                    Matrix matrix = new Matrix();
                    RectF rectF = new RectF(width, height, width + min, height + min);
                    float f = AvatarPhotoController.this.mPhotoSize;
                    matrix.setRectToRect(rectF, new RectF(0.0f, 0.0f, f, f), Matrix.ScaleToFit.CENTER);
                    float f2 = AvatarPhotoController.this.mPhotoSize / 2.0f;
                    matrix.postRotate(i, f2, f2);
                    canvas.drawBitmap(decodeStream, matrix, new Paint());
                    return createBitmap;
                }
            } catch (FileNotFoundException unused) {
            }
            return null;
        }

        @Override // android.os.AsyncTask
        public final void onPostExecute(Bitmap bitmap) {
            Bitmap bitmap2 = bitmap;
            AvatarPhotoController avatarPhotoController = AvatarPhotoController.this;
            File file = new File(AvatarPhotoController.this.mImagesDir, "CropEditUserPhoto.jpg");
            Objects.requireNonNull(avatarPhotoController);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                bitmap2.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                Log.e("AvatarPhotoController", "Cannot create temp file", e);
            }
            AvatarPhotoController avatarPhotoController2 = AvatarPhotoController.this;
            AvatarPickerActivity avatarPickerActivity = avatarPhotoController2.mActivity;
            Uri uri = avatarPhotoController2.mCropPictureUri;
            Objects.requireNonNull(avatarPickerActivity);
            Intent intent = new Intent();
            intent.setData(uri);
            avatarPickerActivity.setResult(-1, intent);
            avatarPickerActivity.finish();
        }
    }

    public final void cropPhoto() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(this.mTakePictureUri, "image/*");
        Uri uri = this.mCropPictureUri;
        intent.putExtra("output", uri);
        intent.addFlags(3);
        intent.setClipData(ClipData.newRawUri("output", uri));
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", this.mPhotoSize);
        intent.putExtra("outputY", this.mPhotoSize);
        if (intent.resolveActivity(this.mActivity.getPackageManager()) != null) {
            try {
                StrictMode.disableDeathOnFileUriExposure();
                this.mActivity.startActivityForResult(intent, 1003);
            } finally {
                StrictMode.enableDeathOnFileUriExposure();
            }
        } else {
            new AnonymousClass2(this.mTakePictureUri).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x006f, code lost:
        if (r7 != null) goto L_0x0071;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public AvatarPhotoController(com.android.settingslib.users.AvatarPickerActivity r7, boolean r8, java.lang.String r9) {
        /*
            r6 = this;
            r6.<init>()
            r6.mActivity = r7
            r6.mFileAuthority = r9
            java.io.File r0 = new java.io.File
            java.io.File r1 = r7.getCacheDir()
            java.lang.String r2 = "multi_user"
            r0.<init>(r1, r2)
            r6.mImagesDir = r0
            r0.mkdir()
            r8 = r8 ^ 1
            java.io.File r1 = new java.io.File
            java.lang.String r2 = "CropEditUserPhoto.jpg"
            r1.<init>(r0, r2)
            if (r8 == 0) goto L_0x0025
            r1.delete()
        L_0x0025:
            androidx.core.content.FileProvider$PathStrategy r2 = androidx.core.content.FileProvider.getPathStrategy(r7, r9)
            android.net.Uri r1 = r2.getUriForFile(r1)
            r6.mCropPictureUri = r1
            java.io.File r1 = new java.io.File
            java.lang.String r2 = "TakeEditUserPhoto.jpg"
            r1.<init>(r0, r2)
            if (r8 == 0) goto L_0x003b
            r1.delete()
        L_0x003b:
            androidx.core.content.FileProvider$PathStrategy r8 = androidx.core.content.FileProvider.getPathStrategy(r7, r9)
            android.net.Uri r8 = r8.getUriForFile(r1)
            r6.mTakePictureUri = r8
            android.content.ContentResolver r0 = r7.getContentResolver()
            android.net.Uri r1 = android.provider.ContactsContract.DisplayPhoto.CONTENT_MAX_DIMENSIONS_URI
            java.lang.String r7 = "display_max_dim"
            java.lang.String[] r2 = new java.lang.String[]{r7}
            r3 = 0
            r4 = 0
            r5 = 0
            android.database.Cursor r7 = r0.query(r1, r2, r3, r4, r5)
            if (r7 == 0) goto L_0x006d
            r7.moveToFirst()     // Catch: all -> 0x0063
            r8 = 0
            int r8 = r7.getInt(r8)     // Catch: all -> 0x0063
            goto L_0x0071
        L_0x0063:
            r6 = move-exception
            r7.close()     // Catch: all -> 0x0068
            goto L_0x006c
        L_0x0068:
            r7 = move-exception
            r6.addSuppressed(r7)
        L_0x006c:
            throw r6
        L_0x006d:
            r8 = 500(0x1f4, float:7.0E-43)
            if (r7 == 0) goto L_0x0074
        L_0x0071:
            r7.close()
        L_0x0074:
            r6.mPhotoSize = r8
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.users.AvatarPhotoController.<init>(com.android.settingslib.users.AvatarPickerActivity, boolean, java.lang.String):void");
    }
}
