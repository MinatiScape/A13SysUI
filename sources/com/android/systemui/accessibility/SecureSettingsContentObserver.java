package com.android.systemui.accessibility;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class SecureSettingsContentObserver<T> {
    public final ContentResolver mContentResolver;
    public final String mKey;
    @VisibleForTesting
    public final List<T> mListeners = new ArrayList();
    @VisibleForTesting
    public final ContentObserver mContentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) { // from class: com.android.systemui.accessibility.SecureSettingsContentObserver.1
        @Override // android.database.ContentObserver
        public final void onChange(boolean z) {
            SecureSettingsContentObserver secureSettingsContentObserver = SecureSettingsContentObserver.this;
            Objects.requireNonNull(secureSettingsContentObserver);
            String stringForUser = Settings.Secure.getStringForUser(secureSettingsContentObserver.mContentResolver, secureSettingsContentObserver.mKey, -2);
            int size = secureSettingsContentObserver.mListeners.size();
            for (int i = 0; i < size; i++) {
                secureSettingsContentObserver.onValueChanged(secureSettingsContentObserver.mListeners.get(i), stringForUser);
            }
        }
    };

    public abstract void onValueChanged(T t, String str);

    public final void addListener(T t) {
        Objects.requireNonNull(t, "listener must be non-null");
        if (!this.mListeners.contains(t)) {
            this.mListeners.add(t);
        }
        if (this.mListeners.size() == 1) {
            this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor(this.mKey), false, this.mContentObserver, -1);
        }
    }

    public SecureSettingsContentObserver(Context context, String str) {
        this.mKey = str;
        this.mContentResolver = context.getContentResolver();
    }
}
