package com.google.android.material.snackbar;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SnackbarManager {
    public static SnackbarManager snackbarManager;
    public final Object lock = new Object();

    /* loaded from: classes.dex */
    public static class SnackbarRecord {
    }

    public SnackbarManager() {
        new Handler(Looper.getMainLooper(), new Handler.Callback() { // from class: com.google.android.material.snackbar.SnackbarManager.1
            @Override // android.os.Handler.Callback
            public final boolean handleMessage(Message message) {
                if (message.what != 0) {
                    return false;
                }
                SnackbarManager snackbarManager2 = SnackbarManager.this;
                SnackbarRecord snackbarRecord = (SnackbarRecord) message.obj;
                Objects.requireNonNull(snackbarManager2);
                synchronized (snackbarManager2.lock) {
                    if (snackbarRecord == null) {
                        Objects.requireNonNull(snackbarRecord);
                        throw null;
                    }
                }
                return true;
            }
        });
    }
}
