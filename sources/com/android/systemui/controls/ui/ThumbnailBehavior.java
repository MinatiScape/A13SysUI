package com.android.systemui.controls.ui;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.graphics.drawable.LayerDrawable;
import android.service.controls.Control;
import android.service.controls.templates.ControlTemplate;
import android.service.controls.templates.ThumbnailTemplate;
import android.util.TypedValue;
import android.view.View;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.Objects;
import java.util.Set;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ThumbnailBehavior.kt */
/* loaded from: classes.dex */
public final class ThumbnailBehavior implements Behavior {
    public Control control;
    public ControlViewHolder cvh;
    public int shadowColor;
    public float shadowOffsetX;
    public float shadowOffsetY;
    public float shadowRadius;
    public ThumbnailTemplate template;

    @Override // com.android.systemui.controls.ui.Behavior
    public final void bind(ControlWithState controlWithState, final int i) {
        int i2;
        Control control = controlWithState.control;
        Intrinsics.checkNotNull(control);
        this.control = control;
        ControlViewHolder cvh = getCvh();
        Control control2 = this.control;
        ThumbnailTemplate thumbnailTemplate = null;
        if (control2 == null) {
            control2 = null;
        }
        CharSequence statusText = control2.getStatusText();
        Set<Integer> set = ControlViewHolder.FORCE_PANEL_DEVICES;
        cvh.setStatusText(statusText, false);
        Control control3 = this.control;
        if (control3 == null) {
            control3 = null;
        }
        ThumbnailTemplate controlTemplate = control3.getControlTemplate();
        Objects.requireNonNull(controlTemplate, "null cannot be cast to non-null type android.service.controls.templates.ThumbnailTemplate");
        this.template = controlTemplate;
        ControlViewHolder cvh2 = getCvh();
        Objects.requireNonNull(cvh2);
        Drawable background = cvh2.layout.getBackground();
        Objects.requireNonNull(background, "null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
        Drawable findDrawableByLayerId = ((LayerDrawable) background).findDrawableByLayerId(2131427711);
        Objects.requireNonNull(findDrawableByLayerId, "null cannot be cast to non-null type android.graphics.drawable.ClipDrawable");
        final ClipDrawable clipDrawable = (ClipDrawable) findDrawableByLayerId;
        ThumbnailTemplate thumbnailTemplate2 = this.template;
        if (thumbnailTemplate2 == null) {
            thumbnailTemplate2 = null;
        }
        if (thumbnailTemplate2.isActive()) {
            i2 = 10000;
        } else {
            i2 = 0;
        }
        clipDrawable.setLevel(i2);
        ThumbnailTemplate thumbnailTemplate3 = this.template;
        if (thumbnailTemplate3 == null) {
            thumbnailTemplate3 = null;
        }
        if (thumbnailTemplate3.isActive()) {
            ControlViewHolder cvh3 = getCvh();
            Objects.requireNonNull(cvh3);
            cvh3.title.setVisibility(4);
            ControlViewHolder cvh4 = getCvh();
            Objects.requireNonNull(cvh4);
            cvh4.subtitle.setVisibility(4);
            ControlViewHolder cvh5 = getCvh();
            Objects.requireNonNull(cvh5);
            cvh5.status.setShadowLayer(this.shadowOffsetX, this.shadowOffsetY, this.shadowRadius, this.shadowColor);
            ControlViewHolder cvh6 = getCvh();
            Objects.requireNonNull(cvh6);
            cvh6.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.ui.ThumbnailBehavior$bind$1
                @Override // java.lang.Runnable
                public final void run() {
                    ThumbnailBehavior thumbnailBehavior = ThumbnailBehavior.this;
                    Objects.requireNonNull(thumbnailBehavior);
                    ThumbnailTemplate thumbnailTemplate4 = thumbnailBehavior.template;
                    if (thumbnailTemplate4 == null) {
                        thumbnailTemplate4 = null;
                    }
                    Icon thumbnail = thumbnailTemplate4.getThumbnail();
                    ControlViewHolder cvh7 = ThumbnailBehavior.this.getCvh();
                    Objects.requireNonNull(cvh7);
                    final Drawable loadDrawable = thumbnail.loadDrawable(cvh7.context);
                    ControlViewHolder cvh8 = ThumbnailBehavior.this.getCvh();
                    Objects.requireNonNull(cvh8);
                    DelayableExecutor delayableExecutor = cvh8.uiExecutor;
                    final ThumbnailBehavior thumbnailBehavior2 = ThumbnailBehavior.this;
                    final ClipDrawable clipDrawable2 = clipDrawable;
                    final int i3 = i;
                    delayableExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.ui.ThumbnailBehavior$bind$1.1
                        @Override // java.lang.Runnable
                        public final void run() {
                            ControlViewHolder cvh9 = ThumbnailBehavior.this.getCvh();
                            Objects.requireNonNull(cvh9);
                            clipDrawable2.setDrawable(new CornerDrawable(loadDrawable, cvh9.context.getResources().getDimensionPixelSize(2131165532)));
                            ClipDrawable clipDrawable3 = clipDrawable2;
                            ControlViewHolder cvh10 = ThumbnailBehavior.this.getCvh();
                            Objects.requireNonNull(cvh10);
                            clipDrawable3.setColorFilter(new BlendModeColorFilter(cvh10.context.getResources().getColor(2131099792), BlendMode.LUMINOSITY));
                            ControlViewHolder cvh11 = ThumbnailBehavior.this.getCvh();
                            ThumbnailBehavior thumbnailBehavior3 = ThumbnailBehavior.this;
                            Objects.requireNonNull(thumbnailBehavior3);
                            ThumbnailTemplate thumbnailTemplate5 = thumbnailBehavior3.template;
                            if (thumbnailTemplate5 == null) {
                                thumbnailTemplate5 = null;
                            }
                            cvh11.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core(thumbnailTemplate5.isActive(), i3, true);
                        }
                    });
                }
            });
        } else {
            ControlViewHolder cvh7 = getCvh();
            Objects.requireNonNull(cvh7);
            cvh7.title.setVisibility(0);
            ControlViewHolder cvh8 = getCvh();
            Objects.requireNonNull(cvh8);
            cvh8.subtitle.setVisibility(0);
            ControlViewHolder cvh9 = getCvh();
            Objects.requireNonNull(cvh9);
            cvh9.status.setShadowLayer(0.0f, 0.0f, 0.0f, this.shadowColor);
        }
        ControlViewHolder cvh10 = getCvh();
        ThumbnailTemplate thumbnailTemplate4 = this.template;
        if (thumbnailTemplate4 != null) {
            thumbnailTemplate = thumbnailTemplate4;
        }
        cvh10.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core(thumbnailTemplate.isActive(), i, true);
    }

    public final ControlViewHolder getCvh() {
        ControlViewHolder controlViewHolder = this.cvh;
        if (controlViewHolder != null) {
            return controlViewHolder;
        }
        return null;
    }

    @Override // com.android.systemui.controls.ui.Behavior
    public final void initialize(final ControlViewHolder controlViewHolder) {
        this.cvh = controlViewHolder;
        TypedValue typedValue = new TypedValue();
        controlViewHolder.context.getResources().getValue(2131165590, typedValue, true);
        this.shadowOffsetX = typedValue.getFloat();
        controlViewHolder.context.getResources().getValue(2131165591, typedValue, true);
        this.shadowOffsetY = typedValue.getFloat();
        controlViewHolder.context.getResources().getValue(2131165589, typedValue, true);
        this.shadowRadius = typedValue.getFloat();
        this.shadowColor = controlViewHolder.context.getResources().getColor(2131099791);
        controlViewHolder.layout.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.ui.ThumbnailBehavior$initialize$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ControlViewHolder controlViewHolder2 = ControlViewHolder.this;
                Objects.requireNonNull(controlViewHolder2);
                ControlActionCoordinator controlActionCoordinator = controlViewHolder2.controlActionCoordinator;
                ControlViewHolder controlViewHolder3 = ControlViewHolder.this;
                ThumbnailBehavior thumbnailBehavior = this;
                Objects.requireNonNull(thumbnailBehavior);
                ControlTemplate controlTemplate = thumbnailBehavior.template;
                Control control = null;
                if (controlTemplate == null) {
                    controlTemplate = null;
                }
                String templateId = controlTemplate.getTemplateId();
                ThumbnailBehavior thumbnailBehavior2 = this;
                Objects.requireNonNull(thumbnailBehavior2);
                Control control2 = thumbnailBehavior2.control;
                if (control2 != null) {
                    control = control2;
                }
                controlActionCoordinator.touch(controlViewHolder3, templateId, control);
            }
        });
    }
}
