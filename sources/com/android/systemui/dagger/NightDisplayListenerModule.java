package com.android.systemui.dagger;

import android.content.Context;
import android.os.Handler;
/* loaded from: classes.dex */
public final class NightDisplayListenerModule {

    /* loaded from: classes.dex */
    public static class Builder {
        public final Handler mBgHandler;
        public final Context mContext;
        public int mUserId = 0;

        public Builder(Context context, Handler handler) {
            this.mContext = context;
            this.mBgHandler = handler;
        }
    }
}
