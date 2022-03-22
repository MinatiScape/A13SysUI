package androidx.core.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda6;
import com.android.systemui.user.CreateUserActivity$$ExternalSyntheticLambda1;
/* loaded from: classes.dex */
public class ContentLoadingProgressBar extends ProgressBar {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final KeyguardUpdateMonitor$$ExternalSyntheticLambda6 mDelayedHide = new KeyguardUpdateMonitor$$ExternalSyntheticLambda6(this, 1);
    public final CreateUserActivity$$ExternalSyntheticLambda1 mDelayedShow = new CreateUserActivity$$ExternalSyntheticLambda1(this, 1);

    public ContentLoadingProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
    }

    @Override // android.widget.ProgressBar, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        removeCallbacks(this.mDelayedHide);
        removeCallbacks(this.mDelayedShow);
    }

    @Override // android.widget.ProgressBar, android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(this.mDelayedHide);
        removeCallbacks(this.mDelayedShow);
    }
}
