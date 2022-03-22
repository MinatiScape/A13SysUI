package com.google.android.material.timepicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import com.android.systemui.plugins.FalsingManager;
import com.google.android.material.button.MaterialButtonToggleGroup;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
class TimePickerView extends ConstraintLayout {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final AnonymousClass1 selectionListener;
    public final MaterialButtonToggleGroup toggle;

    public TimePickerView(Context context) {
        this(context, null);
    }

    public TimePickerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public final void updateToggleConstraints() {
        boolean z;
        if (this.toggle.getVisibility() == 0) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(this);
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            char c = 1;
            if (ViewCompat.Api17Impl.getLayoutDirection(this) == 0) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                c = 2;
            }
            if (constraintSet.mConstraints.containsKey(2131428302)) {
                ConstraintSet.Constraint constraint = constraintSet.mConstraints.get(2131428302);
                switch (c) {
                    case 1:
                        ConstraintSet.Layout layout = constraint.layout;
                        layout.leftToRight = -1;
                        layout.leftToLeft = -1;
                        layout.leftMargin = -1;
                        layout.goneLeftMargin = -1;
                        break;
                    case 2:
                        ConstraintSet.Layout layout2 = constraint.layout;
                        layout2.rightToRight = -1;
                        layout2.rightToLeft = -1;
                        layout2.rightMargin = -1;
                        layout2.goneRightMargin = -1;
                        break;
                    case 3:
                        ConstraintSet.Layout layout3 = constraint.layout;
                        layout3.topToBottom = -1;
                        layout3.topToTop = -1;
                        layout3.topMargin = -1;
                        layout3.goneTopMargin = -1;
                        break;
                    case 4:
                        ConstraintSet.Layout layout4 = constraint.layout;
                        layout4.bottomToTop = -1;
                        layout4.bottomToBottom = -1;
                        layout4.bottomMargin = -1;
                        layout4.goneBottomMargin = -1;
                        break;
                    case 5:
                        constraint.layout.baselineToBaseline = -1;
                        break;
                    case FalsingManager.VERSION /* 6 */:
                        ConstraintSet.Layout layout5 = constraint.layout;
                        layout5.startToEnd = -1;
                        layout5.startToStart = -1;
                        layout5.startMargin = -1;
                        layout5.goneStartMargin = -1;
                        break;
                    case 7:
                        ConstraintSet.Layout layout6 = constraint.layout;
                        layout6.endToStart = -1;
                        layout6.endToEnd = -1;
                        layout6.endMargin = -1;
                        layout6.goneEndMargin = -1;
                        break;
                    default:
                        throw new IllegalArgumentException("unknown constraint");
                }
            }
            constraintSet.applyTo(this);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v1, types: [android.view.View$OnClickListener, com.google.android.material.timepicker.TimePickerView$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public TimePickerView(android.content.Context r4, android.util.AttributeSet r5, int r6) {
        /*
            r3 = this;
            r3.<init>(r4, r5, r6)
            com.google.android.material.timepicker.TimePickerView$1 r5 = new com.google.android.material.timepicker.TimePickerView$1
            r5.<init>()
            r3.selectionListener = r5
            android.view.LayoutInflater r4 = android.view.LayoutInflater.from(r4)
            r6 = 2131624255(0x7f0e013f, float:1.8875685E38)
            r4.inflate(r6, r3)
            r4 = 2131428303(0x7f0b03cf, float:1.8478247E38)
            android.view.View r4 = r3.findViewById(r4)
            com.google.android.material.timepicker.ClockFaceView r4 = (com.google.android.material.timepicker.ClockFaceView) r4
            r4 = 2131428307(0x7f0b03d3, float:1.8478255E38)
            android.view.View r4 = r3.findViewById(r4)
            com.google.android.material.button.MaterialButtonToggleGroup r4 = (com.google.android.material.button.MaterialButtonToggleGroup) r4
            r3.toggle = r4
            com.google.android.material.timepicker.TimePickerView$2 r6 = new com.google.android.material.timepicker.TimePickerView$2
            r6.<init>()
            java.util.Objects.requireNonNull(r4)
            java.util.LinkedHashSet<com.google.android.material.button.MaterialButtonToggleGroup$OnButtonCheckedListener> r4 = r4.onButtonCheckedListeners
            r4.add(r6)
            r4 = 2131428312(0x7f0b03d8, float:1.8478265E38)
            android.view.View r4 = r3.findViewById(r4)
            com.google.android.material.chip.Chip r4 = (com.google.android.material.chip.Chip) r4
            r6 = 2131428309(0x7f0b03d5, float:1.8478259E38)
            android.view.View r6 = r3.findViewById(r6)
            com.google.android.material.chip.Chip r6 = (com.google.android.material.chip.Chip) r6
            r0 = 2131428304(0x7f0b03d0, float:1.8478249E38)
            android.view.View r0 = r3.findViewById(r0)
            com.google.android.material.timepicker.ClockHandView r0 = (com.google.android.material.timepicker.ClockHandView) r0
            java.util.WeakHashMap<android.view.View, androidx.core.view.ViewPropertyAnimatorCompat> r0 = androidx.core.view.ViewCompat.sViewPropertyAnimatorMap
            r0 = 2
            androidx.core.view.ViewCompat.Api19Impl.setAccessibilityLiveRegion(r4, r0)
            androidx.core.view.ViewCompat.Api19Impl.setAccessibilityLiveRegion(r6, r0)
            android.view.GestureDetector r0 = new android.view.GestureDetector
            android.content.Context r1 = r3.getContext()
            com.google.android.material.timepicker.TimePickerView$3 r2 = new com.google.android.material.timepicker.TimePickerView$3
            r2.<init>()
            r0.<init>(r1, r2)
            com.google.android.material.timepicker.TimePickerView$4 r3 = new com.google.android.material.timepicker.TimePickerView$4
            r3.<init>()
            r4.setOnTouchListener(r3)
            r6.setOnTouchListener(r3)
            r3 = 12
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
            r0 = 2131428828(0x7f0b05dc, float:1.8479312E38)
            r4.setTag(r0, r3)
            r3 = 10
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
            r6.setTag(r0, r3)
            r4.setOnClickListener(r5)
            r6.setOnClickListener(r5)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.timepicker.TimePickerView.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        updateToggleConstraints();
    }

    @Override // android.view.View
    public final void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        if (view == this && i == 0) {
            updateToggleConstraints();
        }
    }
}
