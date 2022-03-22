package androidx.core.app;

import android.app.PendingIntent;
import android.os.Bundle;
import androidx.core.graphics.drawable.IconCompat;
/* loaded from: classes.dex */
public final class NotificationCompat$Action {
    public PendingIntent actionIntent;
    @Deprecated
    public int icon;
    public final Bundle mExtras;
    public boolean mShowsUserInterface;
    public CharSequence title;
    public IconCompat mIcon = null;
    public final RemoteInput[] mRemoteInputs = null;
    public final RemoteInput[] mDataOnlyRemoteInputs = null;
    public boolean mAllowGeneratedReplies = true;
    public final int mSemanticAction = 0;
    public final boolean mIsContextual = false;

    public NotificationCompat$Action(String str, PendingIntent pendingIntent) {
        Bundle bundle = new Bundle();
        this.mShowsUserInterface = true;
        this.title = NotificationCompat$Builder.limitCharSequenceLength(str);
        this.actionIntent = pendingIntent;
        this.mExtras = bundle;
        this.mShowsUserInterface = true;
    }

    public final IconCompat getIconCompat() {
        int i;
        if (this.mIcon == null && (i = this.icon) != 0) {
            this.mIcon = IconCompat.createWithResource(null, "", i);
        }
        return this.mIcon;
    }
}
