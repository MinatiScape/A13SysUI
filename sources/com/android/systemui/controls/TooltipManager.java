package com.android.systemui.controls;

import android.content.Context;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import com.android.systemui.recents.TriangleShape;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
/* compiled from: TooltipManager.kt */
/* loaded from: classes.dex */
public final class TooltipManager {
    public final View arrowView;
    public final View dismissView;
    public final ViewGroup layout;
    public final Function1<Integer, Unit> preferenceStorer;
    public int shown;
    public final TextView textView;
    public final int maxTimesShown = 2;
    public final boolean below = true;

    public TooltipManager(Context context) {
        this.shown = context.getSharedPreferences(context.getPackageName(), 0).getInt("ControlsStructureSwipeTooltipCount", 0);
        View inflate = LayoutInflater.from(context).inflate(2131624056, (ViewGroup) null);
        Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.view.ViewGroup");
        ViewGroup viewGroup = (ViewGroup) inflate;
        this.layout = viewGroup;
        this.preferenceStorer = new TooltipManager$preferenceStorer$1(context, this);
        viewGroup.setAlpha(0.0f);
        this.textView = (TextView) viewGroup.requireViewById(2131428532);
        View requireViewById = viewGroup.requireViewById(2131427850);
        requireViewById.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.TooltipManager$dismissView$1$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TooltipManager.this.hide(true);
            }
        });
        this.dismissView = requireViewById;
        View requireViewById2 = viewGroup.requireViewById(2131427514);
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(16843829, typedValue, true);
        int color = context.getResources().getColor(typedValue.resourceId, context.getTheme());
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(2131166924);
        ViewGroup.LayoutParams layoutParams = requireViewById2.getLayoutParams();
        float f = layoutParams.width;
        float f2 = layoutParams.height;
        int i = TriangleShape.$r8$clinit;
        Path path = new Path();
        path.moveTo(0.0f, f2);
        path.lineTo(f, f2);
        path.lineTo(f / 2.0f, 0.0f);
        path.close();
        ShapeDrawable shapeDrawable = new ShapeDrawable(new TriangleShape(path, f, f2));
        Paint paint = shapeDrawable.getPaint();
        paint.setColor(color);
        paint.setPathEffect(new CornerPathEffect(dimensionPixelSize));
        requireViewById2.setBackground(shapeDrawable);
        this.arrowView = requireViewById2;
    }

    public final void hide(final boolean z) {
        boolean z2;
        if (this.layout.getAlpha() == 0.0f) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (!z2) {
            this.layout.post(new Runnable() { // from class: com.android.systemui.controls.TooltipManager$hide$1
                @Override // java.lang.Runnable
                public final void run() {
                    if (z) {
                        TooltipManager tooltipManager = this;
                        Objects.requireNonNull(tooltipManager);
                        tooltipManager.layout.animate().alpha(0.0f).withLayer().setStartDelay(0L).setDuration(100L).setInterpolator(new AccelerateInterpolator()).start();
                        return;
                    }
                    TooltipManager tooltipManager2 = this;
                    Objects.requireNonNull(tooltipManager2);
                    tooltipManager2.layout.animate().cancel();
                    TooltipManager tooltipManager3 = this;
                    Objects.requireNonNull(tooltipManager3);
                    tooltipManager3.layout.setAlpha(0.0f);
                }
            });
        }
    }

    public final void show(final int i, final int i2) {
        boolean z;
        if (this.shown < this.maxTimesShown) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            this.textView.setText(2131952205);
            int i3 = this.shown + 1;
            this.shown = i3;
            ((TooltipManager$preferenceStorer$1) this.preferenceStorer).invoke(Integer.valueOf(i3));
            this.layout.post(new Runnable() { // from class: com.android.systemui.controls.TooltipManager$show$1
                @Override // java.lang.Runnable
                public final void run() {
                    TooltipManager tooltipManager;
                    int i4;
                    int[] iArr = new int[2];
                    TooltipManager tooltipManager2 = TooltipManager.this;
                    Objects.requireNonNull(tooltipManager2);
                    tooltipManager2.layout.getLocationOnScreen(iArr);
                    TooltipManager tooltipManager3 = TooltipManager.this;
                    Objects.requireNonNull(tooltipManager3);
                    ViewGroup viewGroup = tooltipManager3.layout;
                    boolean z2 = false;
                    int i5 = i - iArr[0];
                    Objects.requireNonNull(TooltipManager.this);
                    viewGroup.setTranslationX(i5 - (tooltipManager.layout.getWidth() / 2));
                    TooltipManager tooltipManager4 = TooltipManager.this;
                    Objects.requireNonNull(tooltipManager4);
                    ViewGroup viewGroup2 = tooltipManager4.layout;
                    float f = i2 - iArr[1];
                    TooltipManager tooltipManager5 = TooltipManager.this;
                    if (!tooltipManager5.below) {
                        Objects.requireNonNull(tooltipManager5);
                        i4 = tooltipManager5.layout.getHeight();
                    } else {
                        i4 = 0;
                    }
                    viewGroup2.setTranslationY(f - i4);
                    TooltipManager tooltipManager6 = TooltipManager.this;
                    Objects.requireNonNull(tooltipManager6);
                    if (tooltipManager6.layout.getAlpha() == 0.0f) {
                        z2 = true;
                    }
                    if (z2) {
                        TooltipManager tooltipManager7 = TooltipManager.this;
                        Objects.requireNonNull(tooltipManager7);
                        tooltipManager7.layout.animate().alpha(1.0f).withLayer().setStartDelay(500L).setDuration(300L).setInterpolator(new DecelerateInterpolator()).start();
                    }
                }
            });
        }
    }
}
