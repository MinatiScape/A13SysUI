package com.google.android.systemui.assist.uihints;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import java.util.Objects;
/* loaded from: classes.dex */
public class ChipView extends FrameLayout {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final Drawable BACKGROUND_DARK;
    public final Drawable BACKGROUND_LIGHT;
    public final int TEXT_COLOR_DARK;
    public final int TEXT_COLOR_LIGHT;
    public LinearLayout mChip;
    public ImageView mIconView;
    public TextView mLabelView;
    public Space mSpaceView;

    public ChipView(Context context) {
        this(context, null);
    }

    public ChipView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ChipView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        LinearLayout linearLayout = (LinearLayout) findViewById(2131427697);
        Objects.requireNonNull(linearLayout);
        this.mChip = linearLayout;
        ImageView imageView = (ImageView) findViewById(2131427702);
        Objects.requireNonNull(imageView);
        this.mIconView = imageView;
        TextView textView = (TextView) findViewById(2131427703);
        Objects.requireNonNull(textView);
        this.mLabelView = textView;
        Space space = (Space) findViewById(2131427700);
        Objects.requireNonNull(space);
        this.mSpaceView = space;
    }

    public ChipView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.BACKGROUND_DARK = context.getDrawable(2131231599);
        this.BACKGROUND_LIGHT = context.getDrawable(2131231600);
        this.TEXT_COLOR_DARK = context.getColor(2131099705);
        this.TEXT_COLOR_LIGHT = context.getColor(2131099706);
    }
}
