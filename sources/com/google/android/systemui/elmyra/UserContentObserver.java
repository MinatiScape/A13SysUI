package com.google.android.systemui.elmyra;

import android.app.ActivityManager;
import android.app.SynchronousUserSwitchObserver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import java.util.Objects;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class UserContentObserver extends ContentObserver {
    public final Consumer<Uri> mCallback;
    public final Context mContext;
    public final Uri mSettingsUri;
    public final AnonymousClass1 mUserSwitchCallback = new SynchronousUserSwitchObserver() { // from class: com.google.android.systemui.elmyra.UserContentObserver.1
        public final void onUserSwitching(int i) throws RemoteException {
            UserContentObserver userContentObserver = UserContentObserver.this;
            Objects.requireNonNull(userContentObserver);
            userContentObserver.mContext.getContentResolver().unregisterContentObserver(userContentObserver);
            userContentObserver.mContext.getContentResolver().registerContentObserver(userContentObserver.mSettingsUri, false, userContentObserver, -2);
            UserContentObserver userContentObserver2 = UserContentObserver.this;
            userContentObserver2.mCallback.accept(userContentObserver2.mSettingsUri);
        }
    };

    public UserContentObserver(Context context, Uri uri, Consumer<Uri> consumer, boolean z) {
        super(new Handler(context.getMainLooper()));
        this.mContext = context;
        this.mSettingsUri = uri;
        this.mCallback = consumer;
        if (z) {
            activate();
        }
    }

    public final void activate() {
        this.mContext.getContentResolver().unregisterContentObserver(this);
        this.mContext.getContentResolver().registerContentObserver(this.mSettingsUri, false, this, -2);
        try {
            ActivityManager.getService().registerUserSwitchObserver(this.mUserSwitchCallback, "Elmyra/UserContentObserver");
        } catch (RemoteException e) {
            Log.e("Elmyra/UserContentObserver", "Failed to register user switch observer", e);
        }
    }

    @Override // android.database.ContentObserver
    public final void onChange(boolean z, Uri uri) {
        this.mCallback.accept(uri);
    }
}
