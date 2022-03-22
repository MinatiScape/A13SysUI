package com.android.systemui.controls.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.service.controls.Control;
import android.service.controls.templates.ControlTemplate;
import android.service.controls.templates.RangeTemplate;
import android.service.controls.templates.TemperatureControlTemplate;
import android.service.controls.templates.ToggleRangeTemplate;
import android.util.Log;
import android.util.MathUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.controls.ui.ToggleRangeBehavior;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Objects;
import java.util.Set;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ToggleRangeBehavior.kt */
/* loaded from: classes.dex */
public final class ToggleRangeBehavior implements Behavior {
    public Drawable clipLayer;
    public int colorOffset;
    public Context context;
    public Control control;
    public ControlViewHolder cvh;
    public boolean isChecked;
    public boolean isToggleable;
    public ValueAnimator rangeAnimator;
    public RangeTemplate rangeTemplate;
    public String templateId;
    public CharSequence currentStatusText = "";
    public String currentRangeValue = "";

    /* compiled from: ToggleRangeBehavior.kt */
    /* loaded from: classes.dex */
    public final class ToggleRangeGestureListener extends GestureDetector.SimpleOnGestureListener {
        public boolean isDragging;
        public final View v;

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public final boolean onDown(MotionEvent motionEvent) {
            return true;
        }

        public ToggleRangeGestureListener(ViewGroup viewGroup) {
            this.v = viewGroup;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public final void onLongPress(MotionEvent motionEvent) {
            if (!this.isDragging) {
                ControlViewHolder cvh = ToggleRangeBehavior.this.getCvh();
                Objects.requireNonNull(cvh);
                cvh.controlActionCoordinator.longPress(ToggleRangeBehavior.this.getCvh());
            }
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public final boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (!this.isDragging) {
                this.v.getParent().requestDisallowInterceptTouchEvent(true);
                ToggleRangeBehavior toggleRangeBehavior = ToggleRangeBehavior.this;
                Objects.requireNonNull(toggleRangeBehavior);
                ControlViewHolder cvh = toggleRangeBehavior.getCvh();
                Objects.requireNonNull(cvh);
                cvh.userInteractionInProgress = true;
                ControlViewHolder cvh2 = toggleRangeBehavior.getCvh();
                Context context = toggleRangeBehavior.context;
                if (context == null) {
                    context = null;
                }
                Objects.requireNonNull(cvh2);
                cvh2.status.setTextSize(0, context.getResources().getDimensionPixelSize(2131165547));
                this.isDragging = true;
            }
            ToggleRangeBehavior toggleRangeBehavior2 = ToggleRangeBehavior.this;
            toggleRangeBehavior2.updateRange(toggleRangeBehavior2.getClipLayer().getLevel() + ((int) (10000 * ((-f) / this.v.getWidth()))), true, true);
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public final boolean onSingleTapUp(MotionEvent motionEvent) {
            ToggleRangeBehavior toggleRangeBehavior = ToggleRangeBehavior.this;
            Objects.requireNonNull(toggleRangeBehavior);
            if (!toggleRangeBehavior.isToggleable) {
                return false;
            }
            ControlViewHolder cvh = ToggleRangeBehavior.this.getCvh();
            Objects.requireNonNull(cvh);
            ControlActionCoordinator controlActionCoordinator = cvh.controlActionCoordinator;
            ControlViewHolder cvh2 = ToggleRangeBehavior.this.getCvh();
            ToggleRangeBehavior toggleRangeBehavior2 = ToggleRangeBehavior.this;
            Objects.requireNonNull(toggleRangeBehavior2);
            String str = toggleRangeBehavior2.templateId;
            if (str == null) {
                str = null;
            }
            ToggleRangeBehavior toggleRangeBehavior3 = ToggleRangeBehavior.this;
            Objects.requireNonNull(toggleRangeBehavior3);
            controlActionCoordinator.toggle(cvh2, str, toggleRangeBehavior3.isChecked);
            return true;
        }
    }

    public final String format(String str, String str2, float f) {
        try {
            return String.format(str, Arrays.copyOf(new Object[]{Float.valueOf(findNearestStep(f))}, 1));
        } catch (IllegalFormatException e) {
            Log.w("ControlsUiController", "Illegal format in range template", e);
            if (Intrinsics.areEqual(str2, "")) {
                return "";
            }
            return this.format(str2, "", f);
        }
    }

    @Override // com.android.systemui.controls.ui.Behavior
    public final void bind(ControlWithState controlWithState, int i) {
        Control control = controlWithState.control;
        Intrinsics.checkNotNull(control);
        this.control = control;
        this.colorOffset = i;
        this.currentStatusText = control.getStatusText();
        ControlViewHolder cvh = getCvh();
        Objects.requireNonNull(cvh);
        Control control2 = null;
        cvh.layout.setOnLongClickListener(null);
        ControlViewHolder cvh2 = getCvh();
        Objects.requireNonNull(cvh2);
        Drawable background = cvh2.layout.getBackground();
        Objects.requireNonNull(background, "null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
        this.clipLayer = ((LayerDrawable) background).findDrawableByLayerId(2131427711);
        Control control3 = this.control;
        if (control3 != null) {
            control2 = control3;
        }
        ControlTemplate controlTemplate = control2.getControlTemplate();
        if (setupTemplate(controlTemplate)) {
            this.templateId = controlTemplate.getTemplateId();
            updateRange((int) MathUtils.constrainedMap(0.0f, 10000.0f, getRangeTemplate().getMinValue(), getRangeTemplate().getMaxValue(), getRangeTemplate().getCurrentValue()), this.isChecked, false);
            getCvh().applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core(this.isChecked, i, true);
            ControlViewHolder cvh3 = getCvh();
            Objects.requireNonNull(cvh3);
            cvh3.layout.setAccessibilityDelegate(new View.AccessibilityDelegate() { // from class: com.android.systemui.controls.ui.ToggleRangeBehavior$bind$1
                @Override // android.view.View.AccessibilityDelegate
                public final boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
                    return true;
                }

                @Override // android.view.View.AccessibilityDelegate
                public final boolean performAccessibilityAction(View view, int i2, Bundle bundle) {
                    boolean z;
                    if (i2 == 16) {
                        ToggleRangeBehavior toggleRangeBehavior = ToggleRangeBehavior.this;
                        Objects.requireNonNull(toggleRangeBehavior);
                        if (toggleRangeBehavior.isToggleable) {
                            ControlViewHolder cvh4 = ToggleRangeBehavior.this.getCvh();
                            Objects.requireNonNull(cvh4);
                            ControlActionCoordinator controlActionCoordinator = cvh4.controlActionCoordinator;
                            ControlViewHolder cvh5 = ToggleRangeBehavior.this.getCvh();
                            ToggleRangeBehavior toggleRangeBehavior2 = ToggleRangeBehavior.this;
                            Objects.requireNonNull(toggleRangeBehavior2);
                            String str = toggleRangeBehavior2.templateId;
                            if (str == null) {
                                str = null;
                            }
                            ToggleRangeBehavior toggleRangeBehavior3 = ToggleRangeBehavior.this;
                            Objects.requireNonNull(toggleRangeBehavior3);
                            controlActionCoordinator.toggle(cvh5, str, toggleRangeBehavior3.isChecked);
                            z = true;
                        }
                        z = false;
                    } else {
                        if (i2 == 32) {
                            ControlViewHolder cvh6 = ToggleRangeBehavior.this.getCvh();
                            Objects.requireNonNull(cvh6);
                            cvh6.controlActionCoordinator.longPress(ToggleRangeBehavior.this.getCvh());
                        } else {
                            if (i2 == AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_PROGRESS.getId() && bundle != null && bundle.containsKey("android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE")) {
                                float f = bundle.getFloat("android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE");
                                ToggleRangeBehavior toggleRangeBehavior4 = ToggleRangeBehavior.this;
                                Objects.requireNonNull(toggleRangeBehavior4);
                                ToggleRangeBehavior toggleRangeBehavior5 = ToggleRangeBehavior.this;
                                Objects.requireNonNull(toggleRangeBehavior5);
                                toggleRangeBehavior5.updateRange((int) MathUtils.constrainedMap(0.0f, 10000.0f, toggleRangeBehavior4.getRangeTemplate().getMinValue(), toggleRangeBehavior4.getRangeTemplate().getMaxValue(), f), toggleRangeBehavior5.isChecked, true);
                                ToggleRangeBehavior.this.endUpdateRange();
                            }
                            z = false;
                        }
                        z = true;
                    }
                    return z || super.performAccessibilityAction(view, i2, bundle);
                }

                @Override // android.view.View.AccessibilityDelegate
                public final void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                    super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                    int i2 = 0;
                    float levelToRangeValue = ToggleRangeBehavior.this.levelToRangeValue(0);
                    ToggleRangeBehavior toggleRangeBehavior = ToggleRangeBehavior.this;
                    float levelToRangeValue2 = toggleRangeBehavior.levelToRangeValue(toggleRangeBehavior.getClipLayer().getLevel());
                    float levelToRangeValue3 = ToggleRangeBehavior.this.levelToRangeValue(10000);
                    double stepValue = ToggleRangeBehavior.this.getRangeTemplate().getStepValue();
                    if (stepValue == Math.floor(stepValue)) {
                        i2 = 1;
                    }
                    int i3 = i2 ^ 1;
                    ToggleRangeBehavior toggleRangeBehavior2 = ToggleRangeBehavior.this;
                    Objects.requireNonNull(toggleRangeBehavior2);
                    if (toggleRangeBehavior2.isChecked) {
                        accessibilityNodeInfo.setRangeInfo(AccessibilityNodeInfo.RangeInfo.obtain(i3, levelToRangeValue, levelToRangeValue3, levelToRangeValue2));
                    }
                    accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_PROGRESS);
                }
            });
        }
    }

    public final Drawable getClipLayer() {
        Drawable drawable = this.clipLayer;
        if (drawable != null) {
            return drawable;
        }
        return null;
    }

    public final ControlViewHolder getCvh() {
        ControlViewHolder controlViewHolder = this.cvh;
        if (controlViewHolder != null) {
            return controlViewHolder;
        }
        return null;
    }

    public final RangeTemplate getRangeTemplate() {
        RangeTemplate rangeTemplate = this.rangeTemplate;
        if (rangeTemplate != null) {
            return rangeTemplate;
        }
        return null;
    }

    @Override // com.android.systemui.controls.ui.Behavior
    public final void initialize(ControlViewHolder controlViewHolder) {
        this.cvh = controlViewHolder;
        this.context = controlViewHolder.context;
        final ToggleRangeGestureListener toggleRangeGestureListener = new ToggleRangeGestureListener(controlViewHolder.layout);
        Context context = this.context;
        if (context == null) {
            context = null;
        }
        final GestureDetector gestureDetector = new GestureDetector(context, toggleRangeGestureListener);
        controlViewHolder.layout.setOnTouchListener(new View.OnTouchListener() { // from class: com.android.systemui.controls.ui.ToggleRangeBehavior$initialize$1
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                if (!gestureDetector.onTouchEvent(motionEvent) && motionEvent.getAction() == 1) {
                    ToggleRangeBehavior.ToggleRangeGestureListener toggleRangeGestureListener2 = toggleRangeGestureListener;
                    Objects.requireNonNull(toggleRangeGestureListener2);
                    if (toggleRangeGestureListener2.isDragging) {
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        ToggleRangeBehavior.ToggleRangeGestureListener toggleRangeGestureListener3 = toggleRangeGestureListener;
                        Objects.requireNonNull(toggleRangeGestureListener3);
                        toggleRangeGestureListener3.isDragging = false;
                        this.endUpdateRange();
                    }
                }
                return false;
            }
        });
    }

    public final boolean setupTemplate(ControlTemplate controlTemplate) {
        boolean z = false;
        if (controlTemplate instanceof ToggleRangeTemplate) {
            ToggleRangeTemplate toggleRangeTemplate = (ToggleRangeTemplate) controlTemplate;
            this.rangeTemplate = toggleRangeTemplate.getRange();
            this.isToggleable = true;
            this.isChecked = toggleRangeTemplate.isChecked();
            return true;
        } else if (controlTemplate instanceof RangeTemplate) {
            this.rangeTemplate = (RangeTemplate) controlTemplate;
            if (getRangeTemplate().getCurrentValue() == getRangeTemplate().getMinValue()) {
                z = true;
            }
            this.isChecked = !z;
            return true;
        } else if (controlTemplate instanceof TemperatureControlTemplate) {
            return setupTemplate(((TemperatureControlTemplate) controlTemplate).getTemplate());
        } else {
            Log.e("ControlsUiController", Intrinsics.stringPlus("Unsupported template type: ", controlTemplate));
            return false;
        }
    }

    public final void updateRange(int i, boolean z, boolean z2) {
        boolean z3;
        int max = Math.max(0, Math.min(10000, i));
        if (getClipLayer().getLevel() == 0 && max > 0) {
            getCvh().applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core(z, this.colorOffset, false);
        }
        ValueAnimator valueAnimator = this.rangeAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        if (z2) {
            if (max == 0 || max == 10000) {
                z3 = true;
            } else {
                z3 = false;
            }
            if (getClipLayer().getLevel() != max) {
                ControlViewHolder cvh = getCvh();
                Objects.requireNonNull(cvh);
                cvh.controlActionCoordinator.drag(z3);
                getClipLayer().setLevel(max);
            }
        } else if (max != getClipLayer().getLevel()) {
            ControlViewHolder cvh2 = getCvh();
            Objects.requireNonNull(cvh2);
            ValueAnimator ofInt = ValueAnimator.ofInt(cvh2.clipLayer.getLevel(), max);
            ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.controls.ui.ToggleRangeBehavior$updateRange$1$1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    ControlViewHolder cvh3 = ToggleRangeBehavior.this.getCvh();
                    Objects.requireNonNull(cvh3);
                    ClipDrawable clipDrawable = cvh3.clipLayer;
                    Object animatedValue = valueAnimator2.getAnimatedValue();
                    Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Int");
                    clipDrawable.setLevel(((Integer) animatedValue).intValue());
                }
            });
            ofInt.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.controls.ui.ToggleRangeBehavior$updateRange$1$2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    ToggleRangeBehavior.this.rangeAnimator = null;
                }
            });
            ofInt.setDuration(700L);
            ofInt.setInterpolator(Interpolators.CONTROL_STATE);
            ofInt.start();
            this.rangeAnimator = ofInt;
        }
        if (z) {
            this.currentRangeValue = format(getRangeTemplate().getFormatString().toString(), "%.1f", levelToRangeValue(max));
            if (z2) {
                getCvh().setStatusText(this.currentRangeValue, true);
                return;
            }
            Set<Integer> set = ControlViewHolder.FORCE_PANEL_DEVICES;
            getCvh().setStatusText(((Object) this.currentStatusText) + ' ' + this.currentRangeValue, false);
            return;
        }
        ControlViewHolder cvh3 = getCvh();
        CharSequence charSequence = this.currentStatusText;
        Set<Integer> set2 = ControlViewHolder.FORCE_PANEL_DEVICES;
        cvh3.setStatusText(charSequence, false);
    }

    public final void endUpdateRange() {
        ControlViewHolder cvh = getCvh();
        Context context = this.context;
        if (context == null) {
            context = null;
        }
        Objects.requireNonNull(cvh);
        cvh.status.setTextSize(0, context.getResources().getDimensionPixelSize(2131165548));
        ControlViewHolder cvh2 = getCvh();
        cvh2.setStatusText(((Object) this.currentStatusText) + ' ' + this.currentRangeValue, true);
        ControlViewHolder cvh3 = getCvh();
        Objects.requireNonNull(cvh3);
        cvh3.controlActionCoordinator.setValue(getCvh(), getRangeTemplate().getTemplateId(), findNearestStep(levelToRangeValue(getClipLayer().getLevel())));
        ControlViewHolder cvh4 = getCvh();
        Objects.requireNonNull(cvh4);
        cvh4.userInteractionInProgress = false;
    }

    public final float findNearestStep(float f) {
        float minValue = getRangeTemplate().getMinValue();
        float f2 = Float.MAX_VALUE;
        while (minValue <= getRangeTemplate().getMaxValue()) {
            float abs = Math.abs(f - minValue);
            if (abs >= f2) {
                return minValue - getRangeTemplate().getStepValue();
            }
            minValue += getRangeTemplate().getStepValue();
            f2 = abs;
        }
        return getRangeTemplate().getMaxValue();
    }

    public final float levelToRangeValue(int i) {
        return MathUtils.constrainedMap(getRangeTemplate().getMinValue(), getRangeTemplate().getMaxValue(), 0.0f, 10000.0f, i);
    }
}
