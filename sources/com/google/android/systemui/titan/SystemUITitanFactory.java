package com.google.android.systemui.titan;

import android.content.Context;
import com.android.systemui.dagger.GlobalRootComponent;
import com.google.android.systemui.SystemUIGoogleFactory;
import com.google.android.systemui.titan.DaggerTitanGlobalRootComponent;
import java.util.Objects;
/* compiled from: SystemUITitanFactory.kt */
/* loaded from: classes.dex */
public final class SystemUITitanFactory extends SystemUIGoogleFactory {
    @Override // com.google.android.systemui.SystemUIGoogleFactory, com.android.systemui.SystemUIFactory
    public final GlobalRootComponent buildGlobalRootComponent(Context context) {
        DaggerTitanGlobalRootComponent.Builder builder = (DaggerTitanGlobalRootComponent.Builder) DaggerTitanGlobalRootComponent.builder();
        Objects.requireNonNull(builder);
        builder.context = context;
        return builder.build();
    }
}
