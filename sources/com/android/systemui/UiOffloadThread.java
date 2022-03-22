package com.android.systemui;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/* loaded from: classes.dex */
public final class UiOffloadThread {
    public final ExecutorService mExecutorService = Executors.newSingleThreadExecutor();
}
