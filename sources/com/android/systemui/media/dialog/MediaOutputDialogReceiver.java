package com.android.systemui.media.dialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaOutputDialogReceiver.kt */
/* loaded from: classes.dex */
public final class MediaOutputDialogReceiver extends BroadcastReceiver {
    public final MediaOutputDialogFactory mediaOutputDialogFactory;

    public MediaOutputDialogReceiver(MediaOutputDialogFactory mediaOutputDialogFactory) {
        this.mediaOutputDialogFactory = mediaOutputDialogFactory;
    }

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        if (TextUtils.equals("com.android.systemui.action.LAUNCH_MEDIA_OUTPUT_DIALOG", intent.getAction())) {
            String stringExtra = intent.getStringExtra("package_name");
            if (!TextUtils.isEmpty(stringExtra)) {
                MediaOutputDialogFactory mediaOutputDialogFactory = this.mediaOutputDialogFactory;
                Intrinsics.checkNotNull(stringExtra);
                mediaOutputDialogFactory.create(stringExtra, false, null);
            } else if (MediaOutputDialogReceiverKt.DEBUG) {
                Log.e("MediaOutputDlgReceiver", "Unable to launch media output dialog. Package name is empty.");
            }
        }
    }
}
