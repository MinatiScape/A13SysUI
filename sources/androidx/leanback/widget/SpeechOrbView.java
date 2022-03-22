package androidx.leanback.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import androidx.leanback.widget.SearchOrbView;
/* loaded from: classes.dex */
public class SpeechOrbView extends SearchOrbView {
    public SearchOrbView.Colors mNotListeningOrbColors;

    public SpeechOrbView(Context context) {
        this(context, null);
    }

    @Override // androidx.leanback.widget.SearchOrbView
    public final int getLayoutResourceId() {
        return 2131624231;
    }

    public SpeechOrbView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SpeechOrbView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        Resources resources = context.getResources();
        resources.getFraction(2131361810, 1, 1);
        this.mNotListeningOrbColors = new SearchOrbView.Colors(resources.getColor(2131099946), resources.getColor(2131099948), resources.getColor(2131099947));
        new SearchOrbView.Colors(resources.getColor(2131099949), resources.getColor(2131099949), 0);
        SearchOrbView.Colors colors = this.mNotListeningOrbColors;
        this.mColors = colors;
        this.mIcon.setColorFilter(colors.iconColor);
        if (this.mColorAnimator == null) {
            int i2 = this.mColors.color;
            if (this.mSearchOrbView.getBackground() instanceof GradientDrawable) {
                ((GradientDrawable) this.mSearchOrbView.getBackground()).setColor(i2);
            }
        } else {
            this.mColorAnimationEnabled = true;
            updateColorAnimator();
        }
        this.mIcon.setImageDrawable(getResources().getDrawable(2131232382));
        animateOnFocus(hasFocus());
        this.mSearchOrbView.setScaleX(1.0f);
        this.mSearchOrbView.setScaleY(1.0f);
    }
}
