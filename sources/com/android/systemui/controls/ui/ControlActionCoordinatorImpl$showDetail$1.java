package com.android.systemui.controls.ui;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ResolveInfo;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.wm.shell.TaskView;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: ControlActionCoordinatorImpl.kt */
/* loaded from: classes.dex */
public final class ControlActionCoordinatorImpl$showDetail$1 implements Runnable {
    public final /* synthetic */ ControlViewHolder $cvh;
    public final /* synthetic */ PendingIntent $pendingIntent;
    public final /* synthetic */ ControlActionCoordinatorImpl this$0;

    public ControlActionCoordinatorImpl$showDetail$1(ControlActionCoordinatorImpl controlActionCoordinatorImpl, PendingIntent pendingIntent, ControlViewHolder controlViewHolder) {
        this.this$0 = controlActionCoordinatorImpl;
        this.$pendingIntent = pendingIntent;
        this.$cvh = controlViewHolder;
    }

    @Override // java.lang.Runnable
    public final void run() {
        final List<ResolveInfo> queryIntentActivities = this.this$0.context.getPackageManager().queryIntentActivities(this.$pendingIntent.getIntent(), 65536);
        final ControlActionCoordinatorImpl controlActionCoordinatorImpl = this.this$0;
        DelayableExecutor delayableExecutor = controlActionCoordinatorImpl.uiExecutor;
        final ControlViewHolder controlViewHolder = this.$cvh;
        final PendingIntent pendingIntent = this.$pendingIntent;
        delayableExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$showDetail$1.1
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r4v0, types: [com.android.systemui.controls.ui.ControlActionCoordinatorImpl$showDetail$1$1$1] */
            @Override // java.lang.Runnable
            public final void run() {
                if (!(!queryIntentActivities.isEmpty()) || !controlActionCoordinatorImpl.taskViewFactory.isPresent()) {
                    controlViewHolder.setErrorStatus();
                    return;
                }
                final ControlActionCoordinatorImpl controlActionCoordinatorImpl2 = controlActionCoordinatorImpl;
                Context context = controlActionCoordinatorImpl2.context;
                DelayableExecutor delayableExecutor2 = controlActionCoordinatorImpl2.uiExecutor;
                final PendingIntent pendingIntent2 = pendingIntent;
                final ControlViewHolder controlViewHolder2 = controlViewHolder;
                controlActionCoordinatorImpl.taskViewFactory.get().create(context, delayableExecutor2, new Consumer() { // from class: com.android.systemui.controls.ui.ControlActionCoordinatorImpl.showDetail.1.1.1
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        TaskView taskView = (TaskView) obj;
                        ControlActionCoordinatorImpl controlActionCoordinatorImpl3 = ControlActionCoordinatorImpl.this;
                        ControlActionCoordinatorImpl controlActionCoordinatorImpl4 = ControlActionCoordinatorImpl.this;
                        Objects.requireNonNull(controlActionCoordinatorImpl4);
                        Context context2 = controlActionCoordinatorImpl4.activityContext;
                        if (context2 == null) {
                            context2 = null;
                        }
                        DetailDialog detailDialog = new DetailDialog(context2, taskView, pendingIntent2, controlViewHolder2);
                        final ControlActionCoordinatorImpl controlActionCoordinatorImpl5 = ControlActionCoordinatorImpl.this;
                        detailDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$showDetail$1$1$1$1$1
                            @Override // android.content.DialogInterface.OnDismissListener
                            public final void onDismiss(DialogInterface dialogInterface) {
                                ControlActionCoordinatorImpl.this.dialog = null;
                            }
                        });
                        detailDialog.show();
                        controlActionCoordinatorImpl3.dialog = detailDialog;
                    }
                });
            }
        });
    }
}
