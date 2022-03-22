package com.android.wm.shell.draganddrop;

import android.animation.ValueAnimator;
import android.view.SurfaceControl;
import com.android.wm.shell.draganddrop.DragAndDropController;
import com.android.wm.shell.transition.DefaultTransitionHandler;
import java.util.ArrayList;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class DragAndDropController$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;
    public final /* synthetic */ Object f$2;

    public /* synthetic */ DragAndDropController$$ExternalSyntheticLambda0(Object obj, Object obj2, Object obj3, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
        this.f$2 = obj3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                DragAndDropController dragAndDropController = (DragAndDropController) this.f$0;
                DragAndDropController.PerDisplay perDisplay = (DragAndDropController.PerDisplay) this.f$1;
                SurfaceControl surfaceControl = (SurfaceControl) this.f$2;
                Objects.requireNonNull(dragAndDropController);
                if (perDisplay.activeDragCount == 0) {
                    DragAndDropController.setDropTargetWindowVisibility(perDisplay, 4);
                }
                dragAndDropController.mTransaction.reparent(surfaceControl, null);
                dragAndDropController.mTransaction.apply();
                return;
            default:
                boolean z = DefaultTransitionHandler.sDisableCustomTaskAnimationProperty;
                ((ArrayList) this.f$0).remove((ValueAnimator) this.f$1);
                ((Runnable) this.f$2).run();
                return;
        }
    }
}
