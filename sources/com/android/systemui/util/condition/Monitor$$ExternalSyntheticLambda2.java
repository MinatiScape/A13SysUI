package com.android.systemui.util.condition;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import com.android.systemui.classifier.FalsingDataProvider;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler;
import com.android.systemui.util.condition.Condition;
import com.android.wm.shell.onehanded.IOneHanded;
import com.android.wm.shell.onehanded.OneHanded;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class Monitor$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ Monitor$$ExternalSyntheticLambda2(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                Monitor monitor = (Monitor) this.f$0;
                Objects.requireNonNull(monitor);
                ((Condition) obj).removeCallback((Condition.Callback) monitor.mConditionCallback);
                return;
            case 1:
                ((FalsingDataProvider.MotionEventListener) obj).onMotionEvent((MotionEvent) this.f$0);
                return;
            case 2:
                EdgeBackGestureHandler edgeBackGestureHandler = (EdgeBackGestureHandler) this.f$0;
                Objects.requireNonNull(edgeBackGestureHandler);
                edgeBackGestureHandler.mPipExcludedBounds.set((Rect) obj);
                return;
            default:
                IOneHanded.Stub stub = (IOneHanded.Stub) ((OneHanded) obj).createExternalInterface();
                Objects.requireNonNull(stub);
                ((Bundle) this.f$0).putBinder("extra_shell_one_handed", stub);
                return;
        }
    }
}
