package com.android.settingslib.users;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.TypedArray;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settingslib.users.AvatarPhotoController;
import com.android.settingslib.users.AvatarPickerActivity;
import com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda3;
import com.android.wm.shell.pip.phone.PipMenuView$$ExternalSyntheticLambda1;
import com.google.android.setupcompat.template.FooterButton;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import libcore.io.Streams;
/* loaded from: classes.dex */
public class AvatarPickerActivity extends Activity {
    public static final /* synthetic */ int $r8$clinit = 0;
    public AvatarAdapter mAdapter;
    public AvatarPhotoController mAvatarPhotoController;
    public FooterButton mDoneButton;
    public boolean mWaitingForActivityResult;

    /* loaded from: classes.dex */
    public class AvatarAdapter extends RecyclerView.Adapter<AvatarViewHolder> {
        public final int mChoosePhotoPosition;
        public final List<String> mImageDescriptions;
        public final ArrayList mImageDrawables;
        public final int mPreselectedImageStartPosition;
        public final TypedArray mPreselectedImages;
        public int mSelectedPosition = -1;
        public final int mTakePhotoPosition;
        public final int[] mUserIconColors;

        /* JADX WARN: Removed duplicated region for block: B:21:0x005e  */
        /* JADX WARN: Removed duplicated region for block: B:22:0x0060  */
        /* JADX WARN: Removed duplicated region for block: B:25:0x0065  */
        /* JADX WARN: Removed duplicated region for block: B:31:0x0094  */
        /* JADX WARN: Removed duplicated region for block: B:38:0x00df A[LOOP:1: B:38:0x00df->B:40:0x00e4, LOOP_START, PHI: r2 
          PHI: (r2v2 int) = (r2v1 int), (r2v3 int) binds: [B:37:0x00dc, B:40:0x00e4] A[DONT_GENERATE, DONT_INLINE]] */
        /* JADX WARN: Removed duplicated region for block: B:43:0x0102  */
        /* JADX WARN: Removed duplicated region for block: B:44:0x0114  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public AvatarAdapter() {
            /*
                Method dump skipped, instructions count: 280
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.users.AvatarPickerActivity.AvatarAdapter.<init>(com.android.settingslib.users.AvatarPickerActivity):void");
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public final int getItemCount() {
            return this.mImageDrawables.size() + this.mPreselectedImageStartPosition;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public final void onBindViewHolder(AvatarViewHolder avatarViewHolder, final int i) {
            AvatarViewHolder avatarViewHolder2 = avatarViewHolder;
            boolean z = true;
            if (i == this.mTakePhotoPosition) {
                avatarViewHolder2.mImageView.setImageDrawable(AvatarPickerActivity.this.getDrawable(2131231606));
                avatarViewHolder2.mImageView.setContentDescription(AvatarPickerActivity.this.getString(2131953474));
                avatarViewHolder2.mImageView.setOnClickListener(new InternetDialog$$ExternalSyntheticLambda3(this, 1));
            } else if (i == this.mChoosePhotoPosition) {
                avatarViewHolder2.mImageView.setImageDrawable(AvatarPickerActivity.this.getDrawable(2131231604));
                avatarViewHolder2.mImageView.setContentDescription(AvatarPickerActivity.this.getString(2131953472));
                avatarViewHolder2.mImageView.setOnClickListener(new PipMenuView$$ExternalSyntheticLambda1(this, 1));
            } else {
                int i2 = this.mPreselectedImageStartPosition;
                if (i >= i2) {
                    int i3 = i - i2;
                    if (i != this.mSelectedPosition) {
                        z = false;
                    }
                    avatarViewHolder2.mImageView.setSelected(z);
                    avatarViewHolder2.mImageView.setImageDrawable((Drawable) this.mImageDrawables.get(i3));
                    List<String> list = this.mImageDescriptions;
                    if (list != null) {
                        avatarViewHolder2.mImageView.setContentDescription(list.get(i3));
                    } else {
                        avatarViewHolder2.mImageView.setContentDescription(AvatarPickerActivity.this.getString(2131952261));
                    }
                    avatarViewHolder2.mImageView.setOnClickListener(new View.OnClickListener() { // from class: com.android.settingslib.users.AvatarPickerActivity$AvatarAdapter$$ExternalSyntheticLambda0
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            AvatarPickerActivity.AvatarAdapter avatarAdapter = AvatarPickerActivity.AvatarAdapter.this;
                            int i4 = i;
                            Objects.requireNonNull(avatarAdapter);
                            int i5 = avatarAdapter.mSelectedPosition;
                            if (i5 == i4) {
                                avatarAdapter.mSelectedPosition = -1;
                                avatarAdapter.notifyItemChanged(i4);
                                AvatarPickerActivity.this.mDoneButton.setEnabled(false);
                                return;
                            }
                            avatarAdapter.mSelectedPosition = i4;
                            avatarAdapter.notifyItemChanged(i4);
                            if (i5 != -1) {
                                avatarAdapter.notifyItemChanged(i5);
                            } else {
                                AvatarPickerActivity.this.mDoneButton.setEnabled(true);
                            }
                        }
                    });
                }
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public final RecyclerView.ViewHolder onCreateViewHolder(RecyclerView recyclerView, int i) {
            return new AvatarViewHolder(LayoutInflater.from(recyclerView.getContext()).inflate(2131624010, (ViewGroup) recyclerView, false));
        }
    }

    @Override // android.app.Activity
    public final void onActivityResult(int i, int i2, Intent intent) {
        final Uri uri;
        boolean z;
        boolean z2;
        boolean z3 = false;
        this.mWaitingForActivityResult = false;
        final AvatarPhotoController avatarPhotoController = this.mAvatarPhotoController;
        Objects.requireNonNull(avatarPhotoController);
        if (i2 == -1) {
            if (intent == null || intent.getData() == null) {
                uri = avatarPhotoController.mTakePictureUri;
            } else {
                uri = intent.getData();
            }
            if (!"content".equals(uri.getScheme())) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Invalid pictureUri scheme: ");
                m.append(uri.getScheme());
                Log.e("AvatarPhotoController", m.toString());
                EventLog.writeEvent(1397638484, "172939189", -1, uri.getPath());
                return;
            }
            switch (i) {
                case 1001:
                case 1002:
                    if (avatarPhotoController.mTakePictureUri.equals(uri)) {
                        AvatarPickerActivity avatarPickerActivity = avatarPhotoController.mActivity;
                        Intent intent2 = new Intent("com.android.camera.action.CROP");
                        intent2.setType("image/*");
                        if (avatarPickerActivity.getPackageManager().queryIntentActivities(intent2, 0).size() > 0) {
                            z = true;
                        } else {
                            z = false;
                        }
                        if (z) {
                            KeyguardManager keyguardManager = (KeyguardManager) avatarPickerActivity.getSystemService(KeyguardManager.class);
                            if (keyguardManager == null || keyguardManager.isDeviceLocked()) {
                                z2 = true;
                            } else {
                                z2 = false;
                            }
                            if (!z2) {
                                z3 = true;
                            }
                        }
                        if (z3) {
                            avatarPhotoController.cropPhoto();
                            return;
                        } else {
                            new AvatarPhotoController.AnonymousClass2(uri).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
                            return;
                        }
                    } else {
                        new AsyncTask<Void, Void, Void>() { // from class: com.android.settingslib.users.AvatarPhotoController.1
                            @Override // android.os.AsyncTask
                            public final Void doInBackground(Void[] voidArr) {
                                ContentResolver contentResolver = AvatarPhotoController.this.mActivity.getContentResolver();
                                try {
                                    InputStream openInputStream = contentResolver.openInputStream(uri);
                                    OutputStream openOutputStream = contentResolver.openOutputStream(AvatarPhotoController.this.mTakePictureUri);
                                    Streams.copy(openInputStream, openOutputStream);
                                    if (openOutputStream != null) {
                                        openOutputStream.close();
                                    }
                                    if (openInputStream == null) {
                                        return null;
                                    }
                                    openInputStream.close();
                                    return null;
                                } catch (IOException e) {
                                    Log.w("AvatarPhotoController", "Failed to copy photo", e);
                                    return null;
                                }
                            }

                            @Override // android.os.AsyncTask
                            public final void onPostExecute(Void r1) {
                                if (!AvatarPhotoController.this.mActivity.isFinishing() && !AvatarPhotoController.this.mActivity.isDestroyed()) {
                                    AvatarPhotoController.this.cropPhoto();
                                }
                            }
                        }.execute(new Void[0]);
                        return;
                    }
                case 1003:
                    AvatarPickerActivity avatarPickerActivity2 = avatarPhotoController.mActivity;
                    Objects.requireNonNull(avatarPickerActivity2);
                    Intent intent3 = new Intent();
                    intent3.setData(uri);
                    avatarPickerActivity2.setResult(-1, intent3);
                    avatarPickerActivity2.finish();
                    return;
                default:
                    return;
            }
        }
    }

    @Override // android.app.Activity
    public final void startActivityForResult(Intent intent, int i) {
        this.mWaitingForActivityResult = true;
        super.startActivityForResult(intent, i);
    }

    /* loaded from: classes.dex */
    public static class AvatarViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mImageView;

        public AvatarViewHolder(View view) {
            super(view);
            this.mImageView = (ImageView) view.findViewById(2131427534);
        }
    }

    @Override // android.app.Activity
    public final void onSaveInstanceState(Bundle bundle) {
        bundle.putBoolean("awaiting_result", this.mWaitingForActivityResult);
        bundle.putInt("selected_position", this.mAdapter.mSelectedPosition);
        super.onSaveInstanceState(bundle);
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0035  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0166  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x018a  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0190  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x003e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // android.app.Activity
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onCreate(android.os.Bundle r8) {
        /*
            Method dump skipped, instructions count: 408
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.users.AvatarPickerActivity.onCreate(android.os.Bundle):void");
    }
}
