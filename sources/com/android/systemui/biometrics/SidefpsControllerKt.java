package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import java.util.Iterator;
import kotlin.collections.SetsKt__SetsKt;
/* compiled from: SidefpsController.kt */
/* loaded from: classes.dex */
public final class SidefpsControllerKt {
    public static final void addOverlayDynamicColor$update(Context context, LottieAnimationView lottieAnimationView) {
        final int color = context.getColor(2131099720);
        Iterator it = SetsKt__SetsKt.listOf(".blue600", ".blue400").iterator();
        while (it.hasNext()) {
            lottieAnimationView.addValueCallback(new KeyPath((String) it.next(), "**"), (KeyPath) LottieProperty.COLOR_FILTER, (SimpleLottieValueCallback<KeyPath>) new SimpleLottieValueCallback() { // from class: com.android.systemui.biometrics.SidefpsControllerKt$addOverlayDynamicColor$update$1
                @Override // com.airbnb.lottie.value.SimpleLottieValueCallback
                public final PorterDuffColorFilter getValue() {
                    return new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                }
            });
        }
    }
}
