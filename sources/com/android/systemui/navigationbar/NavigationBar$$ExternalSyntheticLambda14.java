package com.android.systemui.navigationbar;

import android.net.Uri;
import com.android.systemui.statusbar.CommandQueue;
import com.android.wm.shell.pip.Pip;
import com.google.android.systemui.assist.uihints.KeyboardMonitor;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsListener;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView;
import com.google.android.systemui.elmyra.actions.SilenceCall;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class NavigationBar$$ExternalSyntheticLambda14 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ NavigationBar$$ExternalSyntheticLambda14(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                NavigationBarView navigationBarView = (NavigationBarView) this.f$0;
                Objects.requireNonNull(navigationBarView);
                ((Pip) obj).removePipExclusionBoundsChangeListener(navigationBarView.mPipListener);
                return;
            case 1:
                KeyboardMonitor keyboardMonitor = (KeyboardMonitor) this.f$0;
                Objects.requireNonNull(keyboardMonitor);
                ((CommandQueue) obj).addCallback((CommandQueue.Callbacks) keyboardMonitor);
                return;
            case 2:
                EdgeLightsView edgeLightsView = (EdgeLightsView) this.f$0;
                int i = EdgeLightsView.$r8$clinit;
                Objects.requireNonNull(edgeLightsView);
                ((EdgeLightsListener) obj).onModeStarted(edgeLightsView.mMode);
                return;
            default:
                SilenceCall silenceCall = (SilenceCall) this.f$0;
                Uri uri = (Uri) obj;
                Objects.requireNonNull(silenceCall);
                silenceCall.updatePhoneStateListener();
                return;
        }
    }
}
