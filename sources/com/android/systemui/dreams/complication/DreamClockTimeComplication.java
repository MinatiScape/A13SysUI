package com.android.systemui.dreams.complication;

import android.content.Context;
import android.view.View;
import com.android.systemui.CoreStartable;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.dreams.complication.dagger.DreamClockTimeComplicationComponent$Factory;
import com.google.android.systemui.titan.DaggerTitanGlobalRootComponent;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DreamClockTimeComplication implements Complication {
    public DreamClockTimeComplicationComponent$Factory mComponentFactory;

    /* loaded from: classes.dex */
    public static class Registrant extends CoreStartable {
        public final DreamClockTimeComplication mComplication;
        public final DreamOverlayStateController mDreamOverlayStateController;

        @Override // com.android.systemui.CoreStartable
        public final void start() {
            this.mDreamOverlayStateController.addComplication(this.mComplication);
        }

        public Registrant(Context context, DreamOverlayStateController dreamOverlayStateController, DreamClockTimeComplication dreamClockTimeComplication) {
            super(context);
            this.mDreamOverlayStateController = dreamOverlayStateController;
            this.mComplication = dreamClockTimeComplication;
        }
    }

    @Override // com.android.systemui.dreams.complication.Complication
    public final int getRequiredTypeAvailability() {
        return 1;
    }

    /* loaded from: classes.dex */
    public static class DreamClockTimeViewHolder implements Complication.ViewHolder {
        public final ComplicationLayoutParams mLayoutParams;
        public final View mView;

        public DreamClockTimeViewHolder(View view, ComplicationLayoutParams complicationLayoutParams) {
            this.mView = view;
            this.mLayoutParams = complicationLayoutParams;
        }

        @Override // com.android.systemui.dreams.complication.Complication.ViewHolder
        public final ComplicationLayoutParams getLayoutParams() {
            return this.mLayoutParams;
        }

        @Override // com.android.systemui.dreams.complication.Complication.ViewHolder
        public final View getView() {
            return this.mView;
        }
    }

    @Override // com.android.systemui.dreams.complication.Complication
    public final Complication.ViewHolder createView(ComplicationViewModel complicationViewModel) {
        DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.DreamClockTimeComplicationComponentImpl create = this.mComponentFactory.create();
        Objects.requireNonNull(create);
        return new DreamClockTimeViewHolder(create.provideComplicationViewProvider.mo144get(), create.provideLayoutParamsProvider.mo144get());
    }

    public DreamClockTimeComplication(DreamClockTimeComplicationComponent$Factory dreamClockTimeComplicationComponent$Factory) {
        this.mComponentFactory = dreamClockTimeComplicationComponent$Factory;
    }
}
