package com.android.systemui.media.dream;

import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.dreams.complication.ComplicationViewModel;
import com.android.systemui.media.dream.dagger.MediaComplicationComponent$Factory;
import com.google.android.systemui.titan.DaggerTitanGlobalRootComponent;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MediaDreamComplication implements Complication {
    public MediaComplicationComponent$Factory mComponentFactory;

    @Override // com.android.systemui.dreams.complication.Complication
    public final int getRequiredTypeAvailability() {
        return 16;
    }

    @Override // com.android.systemui.dreams.complication.Complication
    public final Complication.ViewHolder createView(ComplicationViewModel complicationViewModel) {
        DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.MediaComplicationComponentImpl create = this.mComponentFactory.create();
        Objects.requireNonNull(create);
        return new MediaViewHolder(create.provideComplicationContainerProvider.mo144get(), new MediaComplicationViewController(create.provideComplicationContainerProvider.mo144get(), DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.this.providesDreamMediaHostProvider.mo144get()), create.provideLayoutParamsProvider.mo144get());
    }

    public MediaDreamComplication(MediaComplicationComponent$Factory mediaComplicationComponent$Factory) {
        this.mComponentFactory = mediaComplicationComponent$Factory;
    }
}
