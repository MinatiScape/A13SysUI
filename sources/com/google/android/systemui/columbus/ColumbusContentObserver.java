package com.google.android.systemui.columbus;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import com.android.systemui.settings.UserTracker;
import java.util.Objects;
import java.util.concurrent.Executor;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
/* compiled from: ColumbusContentObserver.kt */
/* loaded from: classes.dex */
public final class ColumbusContentObserver extends ContentObserver {
    public final Function1<Uri, Unit> callback;
    public final ContentResolverWrapper contentResolver;
    public final Executor executor;
    public final Uri settingsUri;
    public final UserTracker userTracker;
    public final ColumbusContentObserver$userTrackerCallback$1 userTrackerCallback;

    public ColumbusContentObserver() {
        throw null;
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.google.android.systemui.columbus.ColumbusContentObserver$userTrackerCallback$1] */
    public ColumbusContentObserver(ContentResolverWrapper contentResolverWrapper, Uri uri, Function1 function1, UserTracker userTracker, Executor executor, Handler handler) {
        super(handler);
        this.contentResolver = contentResolverWrapper;
        this.settingsUri = uri;
        this.callback = function1;
        this.userTracker = userTracker;
        this.executor = executor;
        this.userTrackerCallback = new UserTracker.Callback() { // from class: com.google.android.systemui.columbus.ColumbusContentObserver$userTrackerCallback$1
            @Override // com.android.systemui.settings.UserTracker.Callback
            public final void onUserChanged(int i) {
                ColumbusContentObserver.this.updateContentObserver();
                ColumbusContentObserver columbusContentObserver = ColumbusContentObserver.this;
                columbusContentObserver.callback.invoke(columbusContentObserver.settingsUri);
            }
        };
    }

    /* compiled from: ColumbusContentObserver.kt */
    /* loaded from: classes.dex */
    public static final class Factory {
        public final ContentResolverWrapper contentResolver;
        public final Executor executor;
        public final Handler handler;
        public final UserTracker userTracker;

        public Factory(ContentResolverWrapper contentResolverWrapper, UserTracker userTracker, Handler handler, Executor executor) {
            this.contentResolver = contentResolverWrapper;
            this.userTracker = userTracker;
            this.handler = handler;
            this.executor = executor;
        }
    }

    @Override // android.database.ContentObserver
    public final void onChange(boolean z, Uri uri) {
        this.callback.invoke(uri);
    }

    public final void updateContentObserver() {
        ContentResolverWrapper contentResolverWrapper = this.contentResolver;
        Objects.requireNonNull(contentResolverWrapper);
        contentResolverWrapper.contentResolver.unregisterContentObserver(this);
        ContentResolverWrapper contentResolverWrapper2 = this.contentResolver;
        Uri uri = this.settingsUri;
        int userId = this.userTracker.getUserId();
        Objects.requireNonNull(contentResolverWrapper2);
        contentResolverWrapper2.contentResolver.registerContentObserver(uri, false, this, userId);
    }
}
