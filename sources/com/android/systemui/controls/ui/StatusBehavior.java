package com.android.systemui.controls.ui;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.service.controls.Control;
import android.view.View;
import java.util.Objects;
/* compiled from: StatusBehavior.kt */
/* loaded from: classes.dex */
public final class StatusBehavior implements Behavior {
    public ControlViewHolder cvh;

    @Override // com.android.systemui.controls.ui.Behavior
    public final void bind(final ControlWithState controlWithState, int i) {
        int i2;
        int i3;
        Control control = controlWithState.control;
        if (control == null) {
            i2 = 0;
        } else {
            i2 = control.getStatus();
        }
        if (i2 == 2) {
            ControlViewHolder cvh = getCvh();
            Objects.requireNonNull(cvh);
            cvh.layout.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.ui.StatusBehavior$bind$msg$1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    StatusBehavior statusBehavior = StatusBehavior.this;
                    StatusBehavior.access$showNotFoundDialog(statusBehavior, statusBehavior.getCvh(), controlWithState);
                }
            });
            ControlViewHolder cvh2 = getCvh();
            Objects.requireNonNull(cvh2);
            cvh2.layout.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.systemui.controls.ui.StatusBehavior$bind$msg$2
                @Override // android.view.View.OnLongClickListener
                public final boolean onLongClick(View view) {
                    StatusBehavior statusBehavior = StatusBehavior.this;
                    StatusBehavior.access$showNotFoundDialog(statusBehavior, statusBehavior.getCvh(), controlWithState);
                    return true;
                }
            });
            i3 = 2131952165;
        } else if (i2 == 3) {
            i3 = 2131952164;
        } else if (i2 != 4) {
            ControlViewHolder cvh3 = getCvh();
            Objects.requireNonNull(cvh3);
            cvh3.isLoading = true;
            i3 = 17040572;
        } else {
            i3 = 2131952168;
        }
        ControlViewHolder cvh4 = getCvh();
        ControlViewHolder cvh5 = getCvh();
        Objects.requireNonNull(cvh5);
        cvh4.setStatusText(cvh5.context.getString(i3), false);
        getCvh().applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core(false, i, true);
    }

    public final ControlViewHolder getCvh() {
        ControlViewHolder controlViewHolder = this.cvh;
        if (controlViewHolder != null) {
            return controlViewHolder;
        }
        return null;
    }

    public static final void access$showNotFoundDialog(StatusBehavior statusBehavior, final ControlViewHolder controlViewHolder, final ControlWithState controlWithState) {
        Objects.requireNonNull(statusBehavior);
        Objects.requireNonNull(controlViewHolder);
        PackageManager packageManager = controlViewHolder.context.getPackageManager();
        Objects.requireNonNull(controlWithState);
        CharSequence applicationLabel = packageManager.getApplicationLabel(packageManager.getApplicationInfo(controlWithState.componentName.getPackageName(), 128));
        final AlertDialog.Builder builder = new AlertDialog.Builder(controlViewHolder.context, 16974545);
        Resources resources = controlViewHolder.context.getResources();
        builder.setTitle(resources.getString(2131952167));
        builder.setMessage(resources.getString(2131952166, controlViewHolder.title.getText(), applicationLabel));
        builder.setPositiveButton(2131952196, new DialogInterface.OnClickListener() { // from class: com.android.systemui.controls.ui.StatusBehavior$showNotFoundDialog$builder$1$1
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                PendingIntent appIntent;
                try {
                    ControlWithState controlWithState2 = ControlWithState.this;
                    Objects.requireNonNull(controlWithState2);
                    Control control = controlWithState2.control;
                    if (!(control == null || (appIntent = control.getAppIntent()) == null)) {
                        appIntent.send();
                    }
                    builder.getContext().sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
                } catch (PendingIntent.CanceledException unused) {
                    controlViewHolder.setErrorStatus();
                }
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(17039360, StatusBehavior$showNotFoundDialog$builder$1$2.INSTANCE);
        AlertDialog create = builder.create();
        create.getWindow().setType(2020);
        create.show();
        controlViewHolder.visibleDialog = create;
    }

    @Override // com.android.systemui.controls.ui.Behavior
    public final void initialize(ControlViewHolder controlViewHolder) {
        this.cvh = controlViewHolder;
    }
}
