package com.android.systemui.controls.ui;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.service.controls.Control;
import android.service.controls.templates.ControlTemplate;
import android.service.controls.templates.TemperatureControlTemplate;
import android.view.View;
import com.android.systemui.controls.ui.ControlViewHolder;
import java.util.Objects;
import java.util.Set;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: TemperatureControlBehavior.kt */
/* loaded from: classes.dex */
public final class TemperatureControlBehavior implements Behavior {
    public Drawable clipLayer;
    public Control control;
    public ControlViewHolder cvh;
    public Behavior subBehavior;

    @Override // com.android.systemui.controls.ui.Behavior
    public final void bind(ControlWithState controlWithState, int i) {
        boolean z;
        Control control = controlWithState.control;
        Intrinsics.checkNotNull(control);
        this.control = control;
        ControlViewHolder cvh = getCvh();
        Control control2 = this.control;
        Drawable drawable = null;
        Control control3 = null;
        if (control2 == null) {
            control2 = null;
        }
        CharSequence statusText = control2.getStatusText();
        Set<Integer> set = ControlViewHolder.FORCE_PANEL_DEVICES;
        int i2 = 0;
        cvh.setStatusText(statusText, false);
        ControlViewHolder cvh2 = getCvh();
        Objects.requireNonNull(cvh2);
        Drawable background = cvh2.layout.getBackground();
        Objects.requireNonNull(background, "null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
        this.clipLayer = ((LayerDrawable) background).findDrawableByLayerId(2131427711);
        Control control4 = this.control;
        if (control4 == null) {
            control4 = null;
        }
        ControlTemplate controlTemplate = control4.getControlTemplate();
        Objects.requireNonNull(controlTemplate, "null cannot be cast to non-null type android.service.controls.templates.TemperatureControlTemplate");
        final TemperatureControlTemplate temperatureControlTemplate = (TemperatureControlTemplate) controlTemplate;
        int currentActiveMode = temperatureControlTemplate.getCurrentActiveMode();
        ControlTemplate template = temperatureControlTemplate.getTemplate();
        if (Intrinsics.areEqual(template, ControlTemplate.getNoTemplateObject()) || Intrinsics.areEqual(template, ControlTemplate.getErrorTemplate())) {
            if (currentActiveMode == 0 || currentActiveMode == 1) {
                z = false;
            } else {
                z = true;
            }
            Drawable drawable2 = this.clipLayer;
            if (drawable2 != null) {
                drawable = drawable2;
            }
            if (z) {
                i2 = 10000;
            }
            drawable.setLevel(i2);
            getCvh().applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core(z, currentActiveMode, true);
            ControlViewHolder cvh3 = getCvh();
            Objects.requireNonNull(cvh3);
            cvh3.layout.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.ui.TemperatureControlBehavior$bind$1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ControlViewHolder cvh4 = TemperatureControlBehavior.this.getCvh();
                    Objects.requireNonNull(cvh4);
                    ControlActionCoordinator controlActionCoordinator = cvh4.controlActionCoordinator;
                    ControlViewHolder cvh5 = TemperatureControlBehavior.this.getCvh();
                    String templateId = temperatureControlTemplate.getTemplateId();
                    TemperatureControlBehavior temperatureControlBehavior = TemperatureControlBehavior.this;
                    Objects.requireNonNull(temperatureControlBehavior);
                    Control control5 = temperatureControlBehavior.control;
                    if (control5 == null) {
                        control5 = null;
                    }
                    controlActionCoordinator.touch(cvh5, templateId, control5);
                }
            });
            return;
        }
        ControlViewHolder cvh4 = getCvh();
        Behavior behavior = this.subBehavior;
        Control control5 = this.control;
        if (control5 == null) {
            control5 = null;
        }
        int status = control5.getStatus();
        Control control6 = this.control;
        if (control6 != null) {
            control3 = control6;
        }
        this.subBehavior = cvh4.bindBehavior(behavior, ControlViewHolder.Companion.findBehaviorClass(status, template, control3.getDeviceType()), currentActiveMode);
    }

    public final ControlViewHolder getCvh() {
        ControlViewHolder controlViewHolder = this.cvh;
        if (controlViewHolder != null) {
            return controlViewHolder;
        }
        return null;
    }

    @Override // com.android.systemui.controls.ui.Behavior
    public final void initialize(ControlViewHolder controlViewHolder) {
        this.cvh = controlViewHolder;
    }
}
