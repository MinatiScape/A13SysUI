package com.android.wm.shell.legacysplitscreen;

import android.app.ActivityManager;
import com.android.systemui.dreams.touch.DreamOverlayTouchMonitor;
import java.util.Set;
import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WindowManagerProxy$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ WindowManagerProxy$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                return WindowManagerProxy.$r8$lambda$47w38wy_iigsOkn3kFhRTExSL3k((LegacySplitScreenTaskListener) this.f$0, (ActivityManager.RunningTaskInfo) obj);
            default:
                return !((Set) this.f$0).contains((DreamOverlayTouchMonitor.TouchSessionImpl) obj);
        }
    }
}
