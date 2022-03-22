package com.android.wm.shell.pip.phone;

import android.app.PendingIntent;
import android.app.RemoteAction;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import com.android.settingslib.users.AvatarPhotoController;
import com.android.settingslib.users.AvatarPickerActivity;
import com.android.systemui.statusbar.notification.row.NotificationInfo;
import com.google.android.systemui.assist.uihints.TranscriptionController;
import com.google.android.systemui.gamedashboard.GameMenuActivity;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PipMenuView$$ExternalSyntheticLambda1 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ PipMenuView$$ExternalSyntheticLambda1(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                try {
                    ((RemoteAction) this.f$0).getActionIntent().send();
                    return;
                } catch (PendingIntent.CanceledException e) {
                    Log.w("PipMenuView", "Failed to send action", e);
                    return;
                }
            case 1:
                AvatarPickerActivity.AvatarAdapter avatarAdapter = (AvatarPickerActivity.AvatarAdapter) this.f$0;
                Objects.requireNonNull(avatarAdapter);
                AvatarPhotoController avatarPhotoController = AvatarPickerActivity.this.mAvatarPhotoController;
                Objects.requireNonNull(avatarPhotoController);
                Intent intent = new Intent("android.provider.action.PICK_IMAGES", (Uri) null);
                intent.setType("image/*");
                avatarPhotoController.mActivity.startActivityForResult(intent, 1001);
                return;
            case 2:
                NotificationInfo notificationInfo = (NotificationInfo) this.f$0;
                int i = NotificationInfo.$r8$clinit;
                Objects.requireNonNull(notificationInfo);
                notificationInfo.mPressedApply = true;
                notificationInfo.mGutsContainer.closeControls(view, true);
                return;
            case 3:
                TranscriptionController transcriptionController = (TranscriptionController) this.f$0;
                Objects.requireNonNull(transcriptionController);
                PendingIntent pendingIntent = transcriptionController.mOnTranscriptionTap;
                if (pendingIntent == null) {
                    transcriptionController.mDefaultOnTap.onTouchInside();
                    return;
                }
                try {
                    pendingIntent.send();
                    return;
                } catch (PendingIntent.CanceledException unused) {
                    Log.e("TranscriptionController", "Transcription tap PendingIntent cancelled");
                    transcriptionController.mDefaultOnTap.onTouchInside();
                    return;
                }
            default:
                GameMenuActivity gameMenuActivity = (GameMenuActivity) this.f$0;
                IntentFilter intentFilter = GameMenuActivity.DND_FILTER;
                Objects.requireNonNull(gameMenuActivity);
                gameMenuActivity.onGameModeSelectionChanged(view);
                return;
        }
    }
}
