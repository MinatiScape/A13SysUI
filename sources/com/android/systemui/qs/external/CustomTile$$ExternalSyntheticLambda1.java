package com.android.systemui.qs.external;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.phone.KeyguardBottomAreaView;
import java.util.Objects;
import java.util.function.Supplier;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class CustomTile$$ExternalSyntheticLambda1 implements Supplier {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ CustomTile$$ExternalSyntheticLambda1(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Supplier
    public final Object get() {
        Drawable.ConstantState constantState;
        switch (this.$r8$classId) {
            case 0:
                Drawable drawable = (Drawable) this.f$0;
                if (drawable == null || (constantState = drawable.getConstantState()) == null) {
                    return null;
                }
                return new QSTileImpl.DrawableIcon(constantState.newDrawable());
            default:
                KeyguardBottomAreaView keyguardBottomAreaView = (KeyguardBottomAreaView) this.f$0;
                Intent intent = KeyguardBottomAreaView.PHONE_INTENT;
                Objects.requireNonNull(keyguardBottomAreaView);
                return new KeyguardBottomAreaView.DefaultLeftButton();
        }
    }
}
