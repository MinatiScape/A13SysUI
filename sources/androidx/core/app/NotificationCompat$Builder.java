package androidx.core.app;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final class NotificationCompat$Builder {
    public CharSequence mContentText;
    public CharSequence mContentTitle;
    public Context mContext;
    public Bundle mExtras;
    public Notification mNotification;
    public ArrayList<NotificationCompat$Action> mActions = new ArrayList<>();
    public ArrayList<Person> mPersonList = new ArrayList<>();
    public ArrayList<NotificationCompat$Action> mInvisibleActions = new ArrayList<>();
    public boolean mShowWhen = true;
    public String mChannelId = "BAT";
    @Deprecated
    public ArrayList<String> mPeople = new ArrayList<>();
    public boolean mAllowSystemGeneratedContextualActions = true;

    public static CharSequence limitCharSequenceLength(String str) {
        if (str == null) {
            return str;
        }
        if (str.length() > 5120) {
            return str.subSequence(0, 5120);
        }
        return str;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r6v1 */
    /* JADX WARN: Type inference failed for: r6v10 */
    /* JADX WARN: Type inference failed for: r6v2, types: [java.lang.CharSequence[], java.lang.CharSequence, android.net.Uri, java.lang.String, long[]] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final android.app.Notification build() {
        /*
            Method dump skipped, instructions count: 698
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.app.NotificationCompat$Builder.build():android.app.Notification");
    }

    public final Bundle getExtras() {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        return this.mExtras;
    }

    public NotificationCompat$Builder(Context context) {
        Notification notification = new Notification();
        this.mNotification = notification;
        this.mContext = context;
        notification.when = System.currentTimeMillis();
        this.mNotification.audioStreamType = -1;
    }
}
