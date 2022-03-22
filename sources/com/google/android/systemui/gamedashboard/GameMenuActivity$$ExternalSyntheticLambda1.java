package com.google.android.systemui.gamedashboard;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.google.android.systemui.assist.uihints.TranscriptionController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class GameMenuActivity$$ExternalSyntheticLambda1 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ GameMenuActivity$$ExternalSyntheticLambda1(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        Intent intent;
        switch (this.$r8$classId) {
            case 0:
                GameMenuActivity gameMenuActivity = (GameMenuActivity) this.f$0;
                IntentFilter intentFilter = GameMenuActivity.DND_FILTER;
                Objects.requireNonNull(gameMenuActivity);
                gameMenuActivity.onGameModeSelectionChanged(view);
                return;
            case 1:
                NotificationStackScrollLayout notificationStackScrollLayout = (NotificationStackScrollLayout) this.f$0;
                boolean z = NotificationStackScrollLayout.SPEW;
                Objects.requireNonNull(notificationStackScrollLayout);
                if (notificationStackScrollLayout.mController.isHistoryEnabled()) {
                    intent = new Intent("android.settings.NOTIFICATION_HISTORY");
                } else {
                    intent = new Intent("android.settings.NOTIFICATION_SETTINGS");
                }
                notificationStackScrollLayout.mStatusBar.startActivity(intent, true, true, 536870912);
                return;
            default:
                TranscriptionController transcriptionController = (TranscriptionController) this.f$0;
                Objects.requireNonNull(transcriptionController);
                PendingIntent pendingIntent = transcriptionController.mOnGreetingTap;
                if (pendingIntent == null) {
                    transcriptionController.mDefaultOnTap.onTouchInside();
                    return;
                }
                try {
                    pendingIntent.send();
                    return;
                } catch (PendingIntent.CanceledException unused) {
                    Log.e("TranscriptionController", "Greeting tap PendingIntent cancelled");
                    transcriptionController.mDefaultOnTap.onTouchInside();
                    return;
                }
        }
    }
}
