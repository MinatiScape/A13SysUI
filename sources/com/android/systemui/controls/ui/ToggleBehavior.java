package com.android.systemui.controls.ui;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.service.controls.Control;
import android.service.controls.templates.ControlTemplate;
import android.service.controls.templates.TemperatureControlTemplate;
import android.service.controls.templates.ToggleTemplate;
import android.util.Log;
import android.view.View;
import java.util.Objects;
import java.util.Set;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ToggleBehavior.kt */
/* loaded from: classes.dex */
public final class ToggleBehavior implements Behavior {
    public Control control;
    public ControlViewHolder cvh;
    public ToggleTemplate template;

    @Override // com.android.systemui.controls.ui.Behavior
    public final void bind(ControlWithState controlWithState, int i) {
        ToggleTemplate toggleTemplate;
        Control control = controlWithState.control;
        Intrinsics.checkNotNull(control);
        this.control = control;
        ControlViewHolder controlViewHolder = this.cvh;
        ControlViewHolder controlViewHolder2 = null;
        if (controlViewHolder == null) {
            controlViewHolder = null;
        }
        CharSequence statusText = control.getStatusText();
        Set<Integer> set = ControlViewHolder.FORCE_PANEL_DEVICES;
        controlViewHolder.setStatusText(statusText, false);
        Control control2 = this.control;
        if (control2 == null) {
            control2 = null;
        }
        ControlTemplate controlTemplate = control2.getControlTemplate();
        if (controlTemplate instanceof ToggleTemplate) {
            toggleTemplate = (ToggleTemplate) controlTemplate;
        } else if (controlTemplate instanceof TemperatureControlTemplate) {
            ControlTemplate template = ((TemperatureControlTemplate) controlTemplate).getTemplate();
            Objects.requireNonNull(template, "null cannot be cast to non-null type android.service.controls.templates.ToggleTemplate");
            toggleTemplate = (ToggleTemplate) template;
        } else {
            Log.e("ControlsUiController", Intrinsics.stringPlus("Unsupported template type: ", controlTemplate));
            return;
        }
        this.template = toggleTemplate;
        ControlViewHolder controlViewHolder3 = this.cvh;
        if (controlViewHolder3 == null) {
            controlViewHolder3 = null;
        }
        Objects.requireNonNull(controlViewHolder3);
        Drawable background = controlViewHolder3.layout.getBackground();
        Objects.requireNonNull(background, "null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
        ((LayerDrawable) background).findDrawableByLayerId(2131427711).setLevel(10000);
        ToggleTemplate toggleTemplate2 = this.template;
        if (toggleTemplate2 == null) {
            toggleTemplate2 = null;
        }
        boolean isChecked = toggleTemplate2.isChecked();
        ControlViewHolder controlViewHolder4 = this.cvh;
        if (controlViewHolder4 != null) {
            controlViewHolder2 = controlViewHolder4;
        }
        controlViewHolder2.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core(isChecked, i, true);
    }

    @Override // com.android.systemui.controls.ui.Behavior
    public final void initialize(final ControlViewHolder controlViewHolder) {
        this.cvh = controlViewHolder;
        controlViewHolder.layout.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.ui.ToggleBehavior$initialize$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ControlViewHolder controlViewHolder2 = ControlViewHolder.this;
                Objects.requireNonNull(controlViewHolder2);
                ControlActionCoordinator controlActionCoordinator = controlViewHolder2.controlActionCoordinator;
                ControlViewHolder controlViewHolder3 = ControlViewHolder.this;
                ToggleBehavior toggleBehavior = this;
                Objects.requireNonNull(toggleBehavior);
                ToggleTemplate toggleTemplate = toggleBehavior.template;
                ToggleTemplate toggleTemplate2 = null;
                if (toggleTemplate == null) {
                    toggleTemplate = null;
                }
                String templateId = toggleTemplate.getTemplateId();
                ToggleBehavior toggleBehavior2 = this;
                Objects.requireNonNull(toggleBehavior2);
                ToggleTemplate toggleTemplate3 = toggleBehavior2.template;
                if (toggleTemplate3 != null) {
                    toggleTemplate2 = toggleTemplate3;
                }
                controlActionCoordinator.toggle(controlViewHolder3, templateId, toggleTemplate2.isChecked());
            }
        });
    }
}
