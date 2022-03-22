package com.android.systemui.tv;

import android.content.Context;
import com.android.systemui.SystemUIFactory;
import com.android.systemui.dagger.GlobalRootComponent;
import com.android.systemui.tv.DaggerTvGlobalRootComponent;
import java.util.Objects;
/* loaded from: classes.dex */
public class TvSystemUIFactory extends SystemUIFactory {
    @Override // com.android.systemui.SystemUIFactory
    public final GlobalRootComponent buildGlobalRootComponent(Context context) {
        DaggerTvGlobalRootComponent.Builder builder = (DaggerTvGlobalRootComponent.Builder) DaggerTvGlobalRootComponent.builder();
        Objects.requireNonNull(builder);
        Objects.requireNonNull(context);
        builder.context = context;
        return builder.build();
    }
}
