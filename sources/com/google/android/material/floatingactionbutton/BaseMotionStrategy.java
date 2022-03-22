package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Property;
import android.view.View;
import android.view.animation.LinearInterpolator;
import androidx.leanback.R$fraction;
import com.android.framework.protobuf.nano.CodedOutputByteBufferNano;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.animation.MotionSpec;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class BaseMotionStrategy implements MotionStrategy {
    public final Context context;
    public MotionSpec defaultMotionSpec;
    public final ExtendedFloatingActionButton fab;
    public final ArrayList<Animator.AnimatorListener> listeners = new ArrayList<>();
    public MotionSpec motionSpec;
    public final CodedOutputByteBufferNano tracker;

    @Override // com.google.android.material.floatingactionbutton.MotionStrategy
    public AnimatorSet createAnimator() {
        MotionSpec motionSpec = this.motionSpec;
        if (motionSpec == null) {
            if (this.defaultMotionSpec == null) {
                this.defaultMotionSpec = MotionSpec.createFromResource(this.context, getDefaultMotionSpecResource());
            }
            motionSpec = this.defaultMotionSpec;
            Objects.requireNonNull(motionSpec);
        }
        return createAnimator(motionSpec);
    }

    @Override // com.google.android.material.floatingactionbutton.MotionStrategy
    public void onAnimationCancel() {
        CodedOutputByteBufferNano codedOutputByteBufferNano = this.tracker;
        Objects.requireNonNull(codedOutputByteBufferNano);
        codedOutputByteBufferNano.buffer = null;
    }

    @Override // com.google.android.material.floatingactionbutton.MotionStrategy
    public void onAnimationStart(Animator animator) {
        CodedOutputByteBufferNano codedOutputByteBufferNano = this.tracker;
        Objects.requireNonNull(codedOutputByteBufferNano);
        Animator animator2 = (Animator) codedOutputByteBufferNano.buffer;
        if (animator2 != null) {
            animator2.cancel();
        }
        codedOutputByteBufferNano.buffer = animator;
    }

    public BaseMotionStrategy(ExtendedFloatingActionButton extendedFloatingActionButton, CodedOutputByteBufferNano codedOutputByteBufferNano) {
        this.fab = extendedFloatingActionButton;
        this.context = extendedFloatingActionButton.getContext();
        this.tracker = codedOutputByteBufferNano;
    }

    public final AnimatorSet createAnimator(MotionSpec motionSpec) {
        ArrayList arrayList = new ArrayList();
        if (motionSpec.hasPropertyValues("opacity")) {
            arrayList.add(motionSpec.getAnimator("opacity", this.fab, View.ALPHA));
        }
        if (motionSpec.hasPropertyValues("scale")) {
            arrayList.add(motionSpec.getAnimator("scale", this.fab, View.SCALE_Y));
            arrayList.add(motionSpec.getAnimator("scale", this.fab, View.SCALE_X));
        }
        if (motionSpec.hasPropertyValues("width")) {
            arrayList.add(motionSpec.getAnimator("width", this.fab, ExtendedFloatingActionButton.WIDTH));
        }
        if (motionSpec.hasPropertyValues("height")) {
            arrayList.add(motionSpec.getAnimator("height", this.fab, ExtendedFloatingActionButton.HEIGHT));
        }
        if (motionSpec.hasPropertyValues("paddingStart")) {
            arrayList.add(motionSpec.getAnimator("paddingStart", this.fab, ExtendedFloatingActionButton.PADDING_START));
        }
        if (motionSpec.hasPropertyValues("paddingEnd")) {
            arrayList.add(motionSpec.getAnimator("paddingEnd", this.fab, ExtendedFloatingActionButton.PADDING_END));
        }
        if (motionSpec.hasPropertyValues("labelOpacity")) {
            arrayList.add(motionSpec.getAnimator("labelOpacity", this.fab, new Property<ExtendedFloatingActionButton, Float>() { // from class: com.google.android.material.floatingactionbutton.BaseMotionStrategy.1
                @Override // android.util.Property
                public final Float get(ExtendedFloatingActionButton extendedFloatingActionButton) {
                    ExtendedFloatingActionButton extendedFloatingActionButton2 = extendedFloatingActionButton;
                    float alpha = (Color.alpha(extendedFloatingActionButton2.getCurrentTextColor()) / 255.0f) / Color.alpha(extendedFloatingActionButton2.originalTextCsl.getColorForState(extendedFloatingActionButton2.getDrawableState(), BaseMotionStrategy.this.fab.originalTextCsl.getDefaultColor()));
                    LinearInterpolator linearInterpolator = AnimationUtils.LINEAR_INTERPOLATOR;
                    return Float.valueOf((alpha * 1.0f) + 0.0f);
                }

                @Override // android.util.Property
                public final void set(ExtendedFloatingActionButton extendedFloatingActionButton, Float f) {
                    ExtendedFloatingActionButton extendedFloatingActionButton2 = extendedFloatingActionButton;
                    Float f2 = f;
                    int colorForState = extendedFloatingActionButton2.originalTextCsl.getColorForState(extendedFloatingActionButton2.getDrawableState(), BaseMotionStrategy.this.fab.originalTextCsl.getDefaultColor());
                    float floatValue = f2.floatValue();
                    LinearInterpolator linearInterpolator = AnimationUtils.LINEAR_INTERPOLATOR;
                    ColorStateList valueOf = ColorStateList.valueOf(Color.argb((int) (((((Color.alpha(colorForState) / 255.0f) - 0.0f) * floatValue) + 0.0f) * 255.0f), Color.red(colorForState), Color.green(colorForState), Color.blue(colorForState)));
                    if (f2.floatValue() == 1.0f) {
                        extendedFloatingActionButton2.silentlyUpdateTextColor(extendedFloatingActionButton2.originalTextCsl);
                    } else {
                        extendedFloatingActionButton2.silentlyUpdateTextColor(valueOf);
                    }
                }
            }));
        }
        AnimatorSet animatorSet = new AnimatorSet();
        R$fraction.playTogether(animatorSet, arrayList);
        return animatorSet;
    }
}
