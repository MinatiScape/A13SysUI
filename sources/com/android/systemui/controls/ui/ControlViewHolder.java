package com.android.systemui.controls.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.Icon;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.service.controls.Control;
import android.service.controls.actions.ControlAction;
import android.service.controls.templates.ControlTemplate;
import android.service.controls.templates.RangeTemplate;
import android.service.controls.templates.StatelessTemplate;
import android.service.controls.templates.TemperatureControlTemplate;
import android.service.controls.templates.ThumbnailTemplate;
import android.service.controls.templates.ToggleRangeTemplate;
import android.service.controls.templates.ToggleTemplate;
import android.util.MathUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.graphics.ColorUtils;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.controls.ControlsMetricsLogger;
import com.android.systemui.controls.controller.ControlInfo;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.ui.RenderInfo;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import kotlin.Unit;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.ClassReference;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
/* compiled from: ControlViewHolder.kt */
/* loaded from: classes.dex */
public final class ControlViewHolder {
    public final GradientDrawable baseLayer;
    public Behavior behavior;
    public final DelayableExecutor bgExecutor;
    public final ImageView chevronIcon;
    public final ClipDrawable clipLayer;
    public final Context context;
    public final ControlActionCoordinator controlActionCoordinator;
    public final ControlsController controlsController;
    public final ControlsMetricsLogger controlsMetricsLogger;
    public ControlWithState cws;
    public final ImageView icon;
    public boolean isLoading;
    public ControlAction lastAction;
    public Dialog lastChallengeDialog;
    public final ViewGroup layout;
    public CharSequence nextStatusText = "";
    public final Function0<Unit> onDialogCancel = new ControlViewHolder$onDialogCancel$1(this);
    public ValueAnimator stateAnimator;
    public final TextView status;
    public Animator statusAnimator;
    public final TextView subtitle;
    public final TextView title;
    public final float toggleBackgroundIntensity;
    public final DelayableExecutor uiExecutor;
    public final int uid;
    public boolean userInteractionInProgress;
    public Dialog visibleDialog;
    public static final Set<Integer> FORCE_PANEL_DEVICES = SetsKt__SetsKt.setOf(49, 50);
    public static final int[] ATTR_ENABLED = {16842910};
    public static final int[] ATTR_DISABLED = {-16842910};

    /* compiled from: ControlViewHolder.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public static ClassReference findBehaviorClass(int i, ControlTemplate controlTemplate, int i2) {
            Class cls;
            if (i != 1) {
                return Reflection.getOrCreateKotlinClass(StatusBehavior.class);
            }
            if (Intrinsics.areEqual(controlTemplate, ControlTemplate.NO_TEMPLATE)) {
                return Reflection.getOrCreateKotlinClass(TouchBehavior.class);
            }
            if (controlTemplate instanceof ThumbnailTemplate) {
                return Reflection.getOrCreateKotlinClass(ThumbnailBehavior.class);
            }
            if (i2 == 50) {
                return Reflection.getOrCreateKotlinClass(TouchBehavior.class);
            }
            if (controlTemplate instanceof ToggleTemplate) {
                return Reflection.getOrCreateKotlinClass(ToggleBehavior.class);
            }
            if (controlTemplate instanceof StatelessTemplate) {
                return Reflection.getOrCreateKotlinClass(TouchBehavior.class);
            }
            if (controlTemplate instanceof ToggleRangeTemplate) {
                return Reflection.getOrCreateKotlinClass(ToggleRangeBehavior.class);
            }
            if (controlTemplate instanceof RangeTemplate) {
                return Reflection.getOrCreateKotlinClass(ToggleRangeBehavior.class);
            }
            if (controlTemplate instanceof TemperatureControlTemplate) {
                cls = TemperatureControlBehavior.class;
            } else {
                cls = DefaultBehavior.class;
            }
            return Reflection.getOrCreateKotlinClass(cls);
        }
    }

    public final void action(ControlAction controlAction) {
        this.lastAction = controlAction;
        ControlsController controlsController = this.controlsController;
        ControlWithState cws = getCws();
        Objects.requireNonNull(cws);
        ComponentName componentName = cws.componentName;
        ControlWithState cws2 = getCws();
        Objects.requireNonNull(cws2);
        controlsController.action(componentName, cws2.ci, controlAction);
    }

    public final void animateStatusChange(boolean z, final Function0<Unit> function0) {
        Animator animator = this.statusAnimator;
        if (animator != null) {
            animator.cancel();
        }
        if (!z) {
            function0.invoke();
        } else if (this.isLoading) {
            function0.invoke();
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.status, "alpha", 0.45f);
            ofFloat.setRepeatMode(2);
            ofFloat.setRepeatCount(-1);
            ofFloat.setDuration(500L);
            ofFloat.setInterpolator(Interpolators.LINEAR);
            ofFloat.setStartDelay(900L);
            ofFloat.start();
            this.statusAnimator = ofFloat;
        } else {
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.status, "alpha", 0.0f);
            ofFloat2.setDuration(200L);
            LinearInterpolator linearInterpolator = Interpolators.LINEAR;
            ofFloat2.setInterpolator(linearInterpolator);
            ofFloat2.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.controls.ui.ControlViewHolder$animateStatusChange$fadeOut$1$1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator2) {
                    function0.invoke();
                }
            });
            ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this.status, "alpha", 1.0f);
            ofFloat3.setDuration(200L);
            ofFloat3.setInterpolator(linearInterpolator);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playSequentially(ofFloat2, ofFloat3);
            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.controls.ui.ControlViewHolder$animateStatusChange$2$1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator2) {
                    ControlViewHolder controlViewHolder = ControlViewHolder.this;
                    Objects.requireNonNull(controlViewHolder);
                    controlViewHolder.status.setAlpha(1.0f);
                    ControlViewHolder.this.statusAnimator = null;
                }
            });
            animatorSet.start();
            this.statusAnimator = animatorSet;
        }
    }

    public final Behavior bindBehavior(Behavior behavior, ClassReference classReference, int i) {
        if (behavior == null || !Intrinsics.areEqual(Reflection.getOrCreateKotlinClass(behavior.getClass()), classReference)) {
            behavior = (Behavior) classReference.getJClass().newInstance();
            behavior.initialize(this);
            this.layout.setAccessibilityDelegate(null);
        }
        behavior.bind(getCws(), i);
        return behavior;
    }

    public final void bindData(ControlWithState controlWithState, boolean z) {
        ControlTemplate controlTemplate;
        int i;
        if (!this.userInteractionInProgress) {
            this.cws = controlWithState;
            boolean z2 = false;
            if (getControlStatus() == 0 || getControlStatus() == 2) {
                TextView textView = this.title;
                ControlInfo controlInfo = controlWithState.ci;
                Objects.requireNonNull(controlInfo);
                textView.setText(controlInfo.controlTitle);
                TextView textView2 = this.subtitle;
                ControlInfo controlInfo2 = controlWithState.ci;
                Objects.requireNonNull(controlInfo2);
                textView2.setText(controlInfo2.controlSubtitle);
            } else {
                Control control = controlWithState.control;
                if (control != null) {
                    this.title.setText(control.getTitle());
                    this.subtitle.setText(control.getSubtitle());
                    ImageView imageView = this.chevronIcon;
                    if (usePanel()) {
                        i = 0;
                    } else {
                        i = 4;
                    }
                    imageView.setVisibility(i);
                }
            }
            if (controlWithState.control != null) {
                this.layout.setClickable(true);
                this.layout.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.systemui.controls.ui.ControlViewHolder$bindData$2$1
                    @Override // android.view.View.OnLongClickListener
                    public final boolean onLongClick(View view) {
                        ControlViewHolder controlViewHolder = ControlViewHolder.this;
                        Objects.requireNonNull(controlViewHolder);
                        controlViewHolder.controlActionCoordinator.longPress(ControlViewHolder.this);
                        return true;
                    }
                });
                ControlActionCoordinator controlActionCoordinator = this.controlActionCoordinator;
                ControlInfo controlInfo3 = controlWithState.ci;
                Objects.requireNonNull(controlInfo3);
                controlActionCoordinator.runPendingAction(controlInfo3.controlId);
            }
            boolean z3 = this.isLoading;
            this.isLoading = false;
            Behavior behavior = this.behavior;
            int controlStatus = getControlStatus();
            ControlWithState cws = getCws();
            Objects.requireNonNull(cws);
            Control control2 = cws.control;
            if (control2 == null) {
                controlTemplate = null;
            } else {
                controlTemplate = control2.getControlTemplate();
            }
            if (controlTemplate == null) {
                controlTemplate = ControlTemplate.NO_TEMPLATE;
            }
            this.behavior = bindBehavior(behavior, Companion.findBehaviorClass(controlStatus, controlTemplate, getDeviceType()), 0);
            updateContentDescription();
            if (z3 && !this.isLoading) {
                z2 = true;
            }
            if (z2) {
                this.controlsMetricsLogger.refreshEnd(this, z);
            }
        }
    }

    public final ControlWithState getCws() {
        ControlWithState controlWithState = this.cws;
        if (controlWithState != null) {
            return controlWithState;
        }
        return null;
    }

    public final void setErrorStatus() {
        animateStatusChange(true, new ControlViewHolder$setErrorStatus$1(this, this.context.getResources().getString(2131952163)));
    }

    public final void setStatusText(CharSequence charSequence, boolean z) {
        if (z) {
            this.status.setAlpha(1.0f);
            this.status.setText(charSequence);
            updateContentDescription();
        }
        this.nextStatusText = charSequence;
    }

    public final void updateContentDescription() {
        ViewGroup viewGroup = this.layout;
        StringBuilder sb = new StringBuilder();
        sb.append((Object) this.title.getText());
        sb.append(' ');
        sb.append((Object) this.subtitle.getText());
        sb.append(' ');
        sb.append((Object) this.status.getText());
        viewGroup.setContentDescription(sb.toString());
    }

    public final void updateStatusRow$frameworks__base__packages__SystemUI__android_common__SystemUI_core(boolean z, CharSequence charSequence, Drawable drawable, ColorStateList colorStateList, Control control) {
        int[] iArr;
        Icon customIcon;
        this.status.setEnabled(z);
        this.icon.setEnabled(z);
        this.status.setText(charSequence);
        updateContentDescription();
        this.status.setTextColor(colorStateList);
        this.chevronIcon.setImageTintList(colorStateList);
        Unit unit = null;
        if (!(control == null || (customIcon = control.getCustomIcon()) == null)) {
            this.icon.setImageIcon(customIcon);
            this.icon.setImageTintList(customIcon.getTintList());
            unit = Unit.INSTANCE;
        }
        if (unit == null) {
            if (drawable instanceof StateListDrawable) {
                if (this.icon.getDrawable() == null || !(this.icon.getDrawable() instanceof StateListDrawable)) {
                    this.icon.setImageDrawable(drawable);
                }
                if (z) {
                    iArr = ATTR_ENABLED;
                } else {
                    iArr = ATTR_DISABLED;
                }
                this.icon.setImageState(iArr, true);
            } else {
                this.icon.setImageDrawable(drawable);
            }
            if (getDeviceType() != 52) {
                this.icon.setImageTintList(colorStateList);
            }
        }
    }

    public final boolean usePanel() {
        ControlTemplate controlTemplate;
        if (!FORCE_PANEL_DEVICES.contains(Integer.valueOf(getDeviceType()))) {
            ControlWithState cws = getCws();
            Objects.requireNonNull(cws);
            Control control = cws.control;
            if (control == null) {
                controlTemplate = null;
            } else {
                controlTemplate = control.getControlTemplate();
            }
            if (controlTemplate == null) {
                controlTemplate = ControlTemplate.NO_TEMPLATE;
            }
            if (!Intrinsics.areEqual(controlTemplate, ControlTemplate.NO_TEMPLATE)) {
                return false;
            }
        }
        return true;
    }

    public ControlViewHolder(ViewGroup viewGroup, ControlsController controlsController, DelayableExecutor delayableExecutor, DelayableExecutor delayableExecutor2, ControlActionCoordinator controlActionCoordinator, ControlsMetricsLogger controlsMetricsLogger, int i) {
        this.layout = viewGroup;
        this.controlsController = controlsController;
        this.uiExecutor = delayableExecutor;
        this.bgExecutor = delayableExecutor2;
        this.controlActionCoordinator = controlActionCoordinator;
        this.controlsMetricsLogger = controlsMetricsLogger;
        this.uid = i;
        this.toggleBackgroundIntensity = viewGroup.getContext().getResources().getFraction(2131361800, 1, 1);
        this.icon = (ImageView) viewGroup.requireViewById(2131428102);
        TextView textView = (TextView) viewGroup.requireViewById(2131428921);
        this.status = textView;
        this.title = (TextView) viewGroup.requireViewById(2131429057);
        this.subtitle = (TextView) viewGroup.requireViewById(2131428947);
        this.chevronIcon = (ImageView) viewGroup.requireViewById(2131427691);
        this.context = viewGroup.getContext();
        Drawable background = viewGroup.getBackground();
        Objects.requireNonNull(background, "null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
        LayerDrawable layerDrawable = (LayerDrawable) background;
        layerDrawable.mutate();
        Drawable findDrawableByLayerId = layerDrawable.findDrawableByLayerId(2131427711);
        Objects.requireNonNull(findDrawableByLayerId, "null cannot be cast to non-null type android.graphics.drawable.ClipDrawable");
        this.clipLayer = (ClipDrawable) findDrawableByLayerId;
        Drawable findDrawableByLayerId2 = layerDrawable.findDrawableByLayerId(2131427539);
        Objects.requireNonNull(findDrawableByLayerId2, "null cannot be cast to non-null type android.graphics.drawable.GradientDrawable");
        this.baseLayer = (GradientDrawable) findDrawableByLayerId2;
        textView.setSelected(true);
    }

    public final void applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core(boolean z, int i, boolean z2) {
        int i2;
        List list;
        final int i3;
        final int i4;
        ColorStateList color;
        int i5;
        ColorStateList customColor;
        if (getControlStatus() == 1 || getControlStatus() == 0) {
            i2 = getDeviceType();
        } else {
            i2 = -1000;
        }
        SparseArray<Drawable> sparseArray = RenderInfo.iconMap;
        Context context = this.context;
        ControlWithState cws = getCws();
        Objects.requireNonNull(cws);
        RenderInfo lookup = RenderInfo.Companion.lookup(context, cws.componentName, i2, i);
        ColorStateList colorStateList = this.context.getResources().getColorStateList(lookup.foreground, this.context.getTheme());
        CharSequence charSequence = this.nextStatusText;
        ControlWithState cws2 = getCws();
        Objects.requireNonNull(cws2);
        Control control = cws2.control;
        if (Intrinsics.areEqual(charSequence, this.status.getText())) {
            z2 = false;
        }
        animateStatusChange(z2, new ControlViewHolder$applyRenderInfo$1(this, z, charSequence, lookup, colorStateList, control));
        int i6 = lookup.enabledBackground;
        final int color2 = this.context.getResources().getColor(2131099777, this.context.getTheme());
        if (z) {
            ControlWithState cws3 = getCws();
            Objects.requireNonNull(cws3);
            Control control2 = cws3.control;
            Integer num = null;
            if (!(control2 == null || (customColor = control2.getCustomColor()) == null)) {
                num = Integer.valueOf(customColor.getColorForState(new int[]{16842910}, customColor.getDefaultColor()));
            }
            if (num == null) {
                i5 = this.context.getResources().getColor(i6, this.context.getTheme());
            } else {
                i5 = num.intValue();
            }
            list = SetsKt__SetsKt.listOf(Integer.valueOf(i5), 255);
        } else {
            list = SetsKt__SetsKt.listOf(Integer.valueOf(this.context.getResources().getColor(2131099777, this.context.getTheme())), 0);
        }
        final int intValue = ((Number) list.get(0)).intValue();
        int intValue2 = ((Number) list.get(1)).intValue();
        if (this.behavior instanceof ToggleRangeBehavior) {
            color2 = ColorUtils.blendARGB(color2, intValue, this.toggleBackgroundIntensity);
        }
        final Drawable drawable = this.clipLayer.getDrawable();
        if (drawable != null) {
            this.clipLayer.setAlpha(0);
            ValueAnimator valueAnimator = this.stateAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            if (z2) {
                if (!(drawable instanceof GradientDrawable) || (color = ((GradientDrawable) drawable).getColor()) == null) {
                    i3 = intValue;
                } else {
                    i3 = color.getDefaultColor();
                }
                ColorStateList color3 = this.baseLayer.getColor();
                if (color3 == null) {
                    i4 = color2;
                } else {
                    i4 = color3.getDefaultColor();
                }
                final float alpha = this.layout.getAlpha();
                ValueAnimator ofInt = ValueAnimator.ofInt(this.clipLayer.getAlpha(), intValue2);
                ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.controls.ui.ControlViewHolder$startBackgroundAnimation$1$1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        Object animatedValue = valueAnimator2.getAnimatedValue();
                        Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Int");
                        int intValue3 = ((Integer) animatedValue).intValue();
                        int blendARGB = ColorUtils.blendARGB(i3, intValue, valueAnimator2.getAnimatedFraction());
                        int blendARGB2 = ColorUtils.blendARGB(i4, color2, valueAnimator2.getAnimatedFraction());
                        float lerp = MathUtils.lerp(alpha, 1.0f, valueAnimator2.getAnimatedFraction());
                        ControlViewHolder controlViewHolder = this;
                        Drawable drawable2 = drawable;
                        Objects.requireNonNull(controlViewHolder);
                        drawable2.setAlpha(intValue3);
                        if (drawable2 instanceof GradientDrawable) {
                            ((GradientDrawable) drawable2).setColor(blendARGB);
                        }
                        controlViewHolder.baseLayer.setColor(blendARGB2);
                        controlViewHolder.layout.setAlpha(lerp);
                    }
                });
                ofInt.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.controls.ui.ControlViewHolder$startBackgroundAnimation$1$2
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator) {
                        ControlViewHolder.this.stateAnimator = null;
                    }
                });
                ofInt.setDuration(700L);
                ofInt.setInterpolator(Interpolators.CONTROL_STATE);
                ofInt.start();
                this.stateAnimator = ofInt;
                return;
            }
            drawable.setAlpha(intValue2);
            if (drawable instanceof GradientDrawable) {
                ((GradientDrawable) drawable).setColor(intValue);
            }
            this.baseLayer.setColor(color2);
            this.layout.setAlpha(1.0f);
        }
    }

    public final int getControlStatus() {
        ControlWithState cws = getCws();
        Objects.requireNonNull(cws);
        Control control = cws.control;
        if (control == null) {
            return 0;
        }
        return control.getStatus();
    }

    public final int getDeviceType() {
        Integer num;
        ControlWithState cws = getCws();
        Objects.requireNonNull(cws);
        Control control = cws.control;
        if (control == null) {
            num = null;
        } else {
            num = Integer.valueOf(control.getDeviceType());
        }
        if (num != null) {
            return num.intValue();
        }
        ControlWithState cws2 = getCws();
        Objects.requireNonNull(cws2);
        ControlInfo controlInfo = cws2.ci;
        Objects.requireNonNull(controlInfo);
        return controlInfo.deviceType;
    }
}
