package com.android.systemui.wmshell;

import android.view.InputEvent;
import com.android.systemui.shared.system.InputChannelCompat$InputEventListener;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WMShell$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ WMShell$$ExternalSyntheticLambda3(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                ((WMShell) this.f$0).initLegacySplitScreen((LegacySplitScreen) obj);
                return;
            default:
                ((InputChannelCompat$InputEventListener) obj).onInputEvent((InputEvent) this.f$0);
                return;
        }
    }
}
