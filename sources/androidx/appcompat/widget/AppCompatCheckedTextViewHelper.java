package androidx.appcompat.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.CheckedTextView;
/* loaded from: classes.dex */
public final class AppCompatCheckedTextViewHelper {
    public ColorStateList mCheckMarkTintList = null;
    public PorterDuff.Mode mCheckMarkTintMode = null;
    public boolean mHasCheckMarkTint = false;
    public boolean mHasCheckMarkTintMode = false;
    public boolean mSkipNextApply;
    public final CheckedTextView mView;

    public final void applyCheckMarkTint() {
        Drawable checkMarkDrawable = this.mView.getCheckMarkDrawable();
        if (checkMarkDrawable == null) {
            return;
        }
        if (this.mHasCheckMarkTint || this.mHasCheckMarkTintMode) {
            Drawable mutate = checkMarkDrawable.mutate();
            if (this.mHasCheckMarkTint) {
                mutate.setTintList(this.mCheckMarkTintList);
            }
            if (this.mHasCheckMarkTintMode) {
                mutate.setTintMode(this.mCheckMarkTintMode);
            }
            if (mutate.isStateful()) {
                mutate.setState(this.mView.getDrawableState());
            }
            this.mView.setCheckMarkDrawable(mutate);
        }
    }

    public AppCompatCheckedTextViewHelper(CheckedTextView checkedTextView) {
        this.mView = checkedTextView;
    }
}
