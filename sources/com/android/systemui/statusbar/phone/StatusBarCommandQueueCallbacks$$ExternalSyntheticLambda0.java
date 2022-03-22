package com.android.systemui.statusbar.phone;

import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class StatusBarCommandQueueCallbacks$$ExternalSyntheticLambda0 implements Consumer {
    public static final /* synthetic */ StatusBarCommandQueueCallbacks$$ExternalSyntheticLambda0 INSTANCE = new StatusBarCommandQueueCallbacks$$ExternalSyntheticLambda0();

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((LegacySplitScreen) obj).onAppTransitionFinished();
    }
}
