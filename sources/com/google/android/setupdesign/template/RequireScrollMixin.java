package com.google.android.setupdesign.template;

import android.os.Handler;
import android.os.Looper;
import com.google.android.setupcompat.template.Mixin;
/* loaded from: classes.dex */
public final class RequireScrollMixin implements Mixin {
    public final Handler handler = new Handler(Looper.getMainLooper());
    public boolean requiringScrollToBottom = false;
    public boolean everScrolledToBottom = false;
}
