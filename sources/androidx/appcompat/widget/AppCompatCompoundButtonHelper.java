package androidx.appcompat.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.CompoundButton;
/* loaded from: classes.dex */
public final class AppCompatCompoundButtonHelper {
    public ColorStateList mButtonTintList = null;
    public PorterDuff.Mode mButtonTintMode = null;
    public boolean mHasButtonTint = false;
    public boolean mHasButtonTintMode = false;
    public boolean mSkipNextApply;
    public final CompoundButton mView;

    public final void applyButtonTint() {
        Drawable buttonDrawable = this.mView.getButtonDrawable();
        if (buttonDrawable == null) {
            return;
        }
        if (this.mHasButtonTint || this.mHasButtonTintMode) {
            Drawable mutate = buttonDrawable.mutate();
            if (this.mHasButtonTint) {
                mutate.setTintList(this.mButtonTintList);
            }
            if (this.mHasButtonTintMode) {
                mutate.setTintMode(this.mButtonTintMode);
            }
            if (mutate.isStateful()) {
                mutate.setState(this.mView.getDrawableState());
            }
            this.mView.setButtonDrawable(mutate);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x005b A[Catch: all -> 0x007e, TryCatch #1 {all -> 0x007e, blocks: (B:3:0x001d, B:5:0x0024, B:7:0x002a, B:10:0x003b, B:12:0x0041, B:14:0x0047, B:15:0x0054, B:17:0x005b, B:18:0x0064, B:20:0x006b), top: B:29:0x001d }] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x006b A[Catch: all -> 0x007e, TRY_LEAVE, TryCatch #1 {all -> 0x007e, blocks: (B:3:0x001d, B:5:0x0024, B:7:0x002a, B:10:0x003b, B:12:0x0041, B:14:0x0047, B:15:0x0054, B:17:0x005b, B:18:0x0064, B:20:0x006b), top: B:29:0x001d }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void loadFromAttributes(android.util.AttributeSet r9, int r10) {
        /*
            r8 = this;
            android.widget.CompoundButton r0 = r8.mView
            android.content.Context r0 = r0.getContext()
            int[] r3 = androidx.appcompat.R$styleable.CompoundButton
            androidx.appcompat.widget.TintTypedArray r0 = androidx.appcompat.widget.TintTypedArray.obtainStyledAttributes(r0, r9, r3, r10)
            android.widget.CompoundButton r1 = r8.mView
            android.content.Context r2 = r1.getContext()
            android.content.res.TypedArray r5 = r0.mWrapped
            java.util.WeakHashMap<android.view.View, androidx.core.view.ViewPropertyAnimatorCompat> r4 = androidx.core.view.ViewCompat.sViewPropertyAnimatorMap
            r7 = 0
            r4 = r9
            r6 = r10
            androidx.core.view.ViewCompat.Api29Impl.saveAttributeDataForStyleable(r1, r2, r3, r4, r5, r6, r7)
            r9 = 1
            boolean r10 = r0.hasValue(r9)     // Catch: all -> 0x007e
            r1 = 0
            if (r10 == 0) goto L_0x0038
            int r10 = r0.getResourceId(r9, r1)     // Catch: all -> 0x007e
            if (r10 == 0) goto L_0x0038
            android.widget.CompoundButton r2 = r8.mView     // Catch: NotFoundException -> 0x0038, all -> 0x007e
            android.content.Context r3 = r2.getContext()     // Catch: NotFoundException -> 0x0038, all -> 0x007e
            android.graphics.drawable.Drawable r10 = androidx.appcompat.content.res.AppCompatResources.getDrawable(r3, r10)     // Catch: NotFoundException -> 0x0038, all -> 0x007e
            r2.setButtonDrawable(r10)     // Catch: NotFoundException -> 0x0038, all -> 0x007e
            goto L_0x0039
        L_0x0038:
            r9 = r1
        L_0x0039:
            if (r9 != 0) goto L_0x0054
            boolean r9 = r0.hasValue(r1)     // Catch: all -> 0x007e
            if (r9 == 0) goto L_0x0054
            int r9 = r0.getResourceId(r1, r1)     // Catch: all -> 0x007e
            if (r9 == 0) goto L_0x0054
            android.widget.CompoundButton r10 = r8.mView     // Catch: all -> 0x007e
            android.content.Context r1 = r10.getContext()     // Catch: all -> 0x007e
            android.graphics.drawable.Drawable r9 = androidx.appcompat.content.res.AppCompatResources.getDrawable(r1, r9)     // Catch: all -> 0x007e
            r10.setButtonDrawable(r9)     // Catch: all -> 0x007e
        L_0x0054:
            r9 = 2
            boolean r10 = r0.hasValue(r9)     // Catch: all -> 0x007e
            if (r10 == 0) goto L_0x0064
            android.widget.CompoundButton r10 = r8.mView     // Catch: all -> 0x007e
            android.content.res.ColorStateList r9 = r0.getColorStateList(r9)     // Catch: all -> 0x007e
            r10.setButtonTintList(r9)     // Catch: all -> 0x007e
        L_0x0064:
            r9 = 3
            boolean r10 = r0.hasValue(r9)     // Catch: all -> 0x007e
            if (r10 == 0) goto L_0x007a
            android.widget.CompoundButton r8 = r8.mView     // Catch: all -> 0x007e
            r10 = -1
            int r9 = r0.getInt(r9, r10)     // Catch: all -> 0x007e
            r10 = 0
            android.graphics.PorterDuff$Mode r9 = androidx.appcompat.widget.DrawableUtils.parseTintMode(r9, r10)     // Catch: all -> 0x007e
            r8.setButtonTintMode(r9)     // Catch: all -> 0x007e
        L_0x007a:
            r0.recycle()
            return
        L_0x007e:
            r8 = move-exception
            r0.recycle()
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.AppCompatCompoundButtonHelper.loadFromAttributes(android.util.AttributeSet, int):void");
    }

    public AppCompatCompoundButtonHelper(CompoundButton compoundButton) {
        this.mView = compoundButton;
    }
}
