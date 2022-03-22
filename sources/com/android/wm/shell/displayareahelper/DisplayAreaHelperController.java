package com.android.wm.shell.displayareahelper;

import android.view.SurfaceControl;
import com.android.systemui.unfold.UnfoldLightRevealOverlayAnimation$init$1;
import com.android.wm.shell.RootDisplayAreaOrganizer;
import com.android.wm.shell.common.ShellExecutor;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class DisplayAreaHelperController implements DisplayAreaHelper {
    public final Executor mExecutor;
    public final RootDisplayAreaOrganizer mRootDisplayAreaOrganizer;

    @Override // com.android.wm.shell.displayareahelper.DisplayAreaHelper
    public final void attachToRootDisplayArea(final SurfaceControl.Builder builder, final UnfoldLightRevealOverlayAnimation$init$1 unfoldLightRevealOverlayAnimation$init$1) {
        this.mExecutor.execute(new Runnable() { // from class: com.android.wm.shell.displayareahelper.DisplayAreaHelperController$$ExternalSyntheticLambda0
            public final /* synthetic */ int f$1 = 0;

            @Override // java.lang.Runnable
            public final void run() {
                DisplayAreaHelperController displayAreaHelperController = DisplayAreaHelperController.this;
                int i = this.f$1;
                SurfaceControl.Builder builder2 = builder;
                Consumer consumer = unfoldLightRevealOverlayAnimation$init$1;
                Objects.requireNonNull(displayAreaHelperController);
                RootDisplayAreaOrganizer rootDisplayAreaOrganizer = displayAreaHelperController.mRootDisplayAreaOrganizer;
                Objects.requireNonNull(rootDisplayAreaOrganizer);
                SurfaceControl surfaceControl = rootDisplayAreaOrganizer.mLeashes.get(i);
                if (surfaceControl != null) {
                    builder2.setParent(surfaceControl);
                }
                consumer.accept(builder2);
            }
        });
    }

    public DisplayAreaHelperController(ShellExecutor shellExecutor, RootDisplayAreaOrganizer rootDisplayAreaOrganizer) {
        this.mExecutor = shellExecutor;
        this.mRootDisplayAreaOrganizer = rootDisplayAreaOrganizer;
    }
}
