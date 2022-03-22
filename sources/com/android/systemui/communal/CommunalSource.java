package com.android.systemui.communal;

import com.google.common.util.concurrent.ListenableFuture;
/* loaded from: classes.dex */
public interface CommunalSource {

    /* loaded from: classes.dex */
    public static class CommunalViewResult {
    }

    ListenableFuture requestCommunalView();
}
