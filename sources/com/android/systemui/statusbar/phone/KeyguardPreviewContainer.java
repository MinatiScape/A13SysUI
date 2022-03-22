package com.android.systemui.statusbar.phone;

import android.view.WindowInsets;
import android.widget.FrameLayout;
/* loaded from: classes.dex */
public class KeyguardPreviewContainer extends FrameLayout {
    public AnonymousClass1 mBlackBarDrawable;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1, types: [android.graphics.drawable.Drawable, com.android.systemui.statusbar.phone.KeyguardPreviewContainer$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public KeyguardPreviewContainer(android.content.Context r1, android.util.AttributeSet r2) {
        /*
            r0 = this;
            r0.<init>(r1, r2)
            com.android.systemui.statusbar.phone.KeyguardPreviewContainer$1 r1 = new com.android.systemui.statusbar.phone.KeyguardPreviewContainer$1
            r1.<init>()
            r0.mBlackBarDrawable = r1
            r0.setBackground(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.KeyguardPreviewContainer.<init>(android.content.Context, android.util.AttributeSet):void");
    }

    @Override // android.view.View
    public final WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        setPadding(0, 0, 0, windowInsets.getStableInsetBottom());
        return super.onApplyWindowInsets(windowInsets);
    }
}
