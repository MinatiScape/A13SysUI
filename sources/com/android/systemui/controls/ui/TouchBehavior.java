package com.android.systemui.controls.ui;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.service.controls.Control;
import android.service.controls.templates.ControlTemplate;
import android.service.controls.templates.StatelessTemplate;
import android.view.View;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.Objects;
import java.util.Set;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: TouchBehavior.kt */
/* loaded from: classes.dex */
public final class TouchBehavior implements Behavior {
    public Control control;
    public ControlViewHolder cvh;
    public int lastColorOffset;
    public boolean statelessTouch;
    public ControlTemplate template;

    @Override // com.android.systemui.controls.ui.Behavior
    public final void bind(ControlWithState controlWithState, int i) {
        Control control = controlWithState.control;
        Intrinsics.checkNotNull(control);
        this.control = control;
        this.lastColorOffset = i;
        ControlViewHolder controlViewHolder = this.cvh;
        ControlViewHolder controlViewHolder2 = null;
        if (controlViewHolder == null) {
            controlViewHolder = null;
        }
        CharSequence statusText = control.getStatusText();
        Set<Integer> set = ControlViewHolder.FORCE_PANEL_DEVICES;
        int i2 = 0;
        controlViewHolder.setStatusText(statusText, false);
        Control control2 = this.control;
        if (control2 == null) {
            control2 = null;
        }
        this.template = control2.getControlTemplate();
        ControlViewHolder controlViewHolder3 = this.cvh;
        if (controlViewHolder3 == null) {
            controlViewHolder3 = null;
        }
        Objects.requireNonNull(controlViewHolder3);
        Drawable background = controlViewHolder3.layout.getBackground();
        Objects.requireNonNull(background, "null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
        Drawable findDrawableByLayerId = ((LayerDrawable) background).findDrawableByLayerId(2131427711);
        if (getEnabled()) {
            i2 = 10000;
        }
        findDrawableByLayerId.setLevel(i2);
        ControlViewHolder controlViewHolder4 = this.cvh;
        if (controlViewHolder4 != null) {
            controlViewHolder2 = controlViewHolder4;
        }
        controlViewHolder2.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core(getEnabled(), i, true);
    }

    public final boolean getEnabled() {
        if (this.lastColorOffset > 0 || this.statelessTouch) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.controls.ui.Behavior
    public final void initialize(final ControlViewHolder controlViewHolder) {
        this.cvh = controlViewHolder;
        controlViewHolder.layout.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.ui.TouchBehavior$initialize$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ControlViewHolder controlViewHolder2 = ControlViewHolder.this;
                Objects.requireNonNull(controlViewHolder2);
                ControlActionCoordinator controlActionCoordinator = controlViewHolder2.controlActionCoordinator;
                ControlViewHolder controlViewHolder3 = ControlViewHolder.this;
                TouchBehavior touchBehavior = this;
                Objects.requireNonNull(touchBehavior);
                ControlTemplate controlTemplate = touchBehavior.template;
                ControlTemplate controlTemplate2 = null;
                if (controlTemplate == null) {
                    controlTemplate = null;
                }
                String templateId = controlTemplate.getTemplateId();
                TouchBehavior touchBehavior2 = this;
                Objects.requireNonNull(touchBehavior2);
                Control control = touchBehavior2.control;
                if (control == null) {
                    control = null;
                }
                controlActionCoordinator.touch(controlViewHolder3, templateId, control);
                TouchBehavior touchBehavior3 = this;
                Objects.requireNonNull(touchBehavior3);
                ControlTemplate controlTemplate3 = touchBehavior3.template;
                if (controlTemplate3 != null) {
                    controlTemplate2 = controlTemplate3;
                }
                if (controlTemplate2 instanceof StatelessTemplate) {
                    TouchBehavior touchBehavior4 = this;
                    touchBehavior4.statelessTouch = true;
                    ControlViewHolder.this.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core(touchBehavior4.getEnabled(), this.lastColorOffset, true);
                    ControlViewHolder controlViewHolder4 = ControlViewHolder.this;
                    Objects.requireNonNull(controlViewHolder4);
                    DelayableExecutor delayableExecutor = controlViewHolder4.uiExecutor;
                    final TouchBehavior touchBehavior5 = this;
                    final ControlViewHolder controlViewHolder5 = ControlViewHolder.this;
                    delayableExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.controls.ui.TouchBehavior$initialize$1.1
                        @Override // java.lang.Runnable
                        public final void run() {
                            TouchBehavior touchBehavior6 = TouchBehavior.this;
                            touchBehavior6.statelessTouch = false;
                            ControlViewHolder controlViewHolder6 = controlViewHolder5;
                            boolean enabled = touchBehavior6.getEnabled();
                            int i = TouchBehavior.this.lastColorOffset;
                            Set<Integer> set = ControlViewHolder.FORCE_PANEL_DEVICES;
                            controlViewHolder6.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core(enabled, i, true);
                        }
                    }, 3000L);
                }
            }
        });
    }
}
