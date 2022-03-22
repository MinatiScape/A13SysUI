package com.google.android.systemui.communal.dreams;

import android.content.Intent;
import android.view.View;
import com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda0;
import com.android.systemui.dreams.DreamOverlayService;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.dreams.complication.ComplicationLayoutParams;
import com.android.systemui.dreams.complication.ComplicationViewModel;
import com.android.systemui.plugins.ActivityStarter;
import com.google.android.systemui.communal.dreams.dagger.SetupDreamComponent$Factory;
import com.google.android.systemui.titan.DaggerTitanGlobalRootComponent;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SetupDreamComplication implements Complication {
    public final SetupDreamComponent$Factory mComponentFactory;

    /* loaded from: classes.dex */
    public static class SetupDreamViewHolder implements Complication.ViewHolder {
        public final ComplicationLayoutParams mComplicationLayoutParams;
        public final View mView;

        public SetupDreamViewHolder(View view, ComplicationLayoutParams complicationLayoutParams, final ComplicationViewModel complicationViewModel, final Intent intent, final ActivityStarter activityStarter) {
            this.mView = view;
            this.mComplicationLayoutParams = complicationLayoutParams;
            view.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.systemui.communal.dreams.SetupDreamComplication$SetupDreamViewHolder$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ComplicationViewModel complicationViewModel2 = ComplicationViewModel.this;
                    ActivityStarter activityStarter2 = activityStarter;
                    Intent intent2 = intent;
                    Objects.requireNonNull(complicationViewModel2);
                    DreamOverlayService.AnonymousClass1 r5 = (DreamOverlayService.AnonymousClass1) complicationViewModel2.mHost;
                    Objects.requireNonNull(r5);
                    DreamOverlayService dreamOverlayService = DreamOverlayService.this;
                    dreamOverlayService.mExecutor.execute(new LockIconViewController$$ExternalSyntheticLambda0(dreamOverlayService, 1));
                    activityStarter2.startActivity(intent2, true);
                }
            });
        }

        @Override // com.android.systemui.dreams.complication.Complication.ViewHolder
        public final ComplicationLayoutParams getLayoutParams() {
            return this.mComplicationLayoutParams;
        }

        @Override // com.android.systemui.dreams.complication.Complication.ViewHolder
        public final View getView() {
            return this.mView;
        }
    }

    @Override // com.android.systemui.dreams.complication.Complication
    public final Complication.ViewHolder createView(ComplicationViewModel complicationViewModel) {
        DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.SetupDreamComponentImpl create = this.mComponentFactory.create(complicationViewModel);
        Objects.requireNonNull(create);
        return new SetupDreamViewHolder(DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.this.namedView(), new ComplicationLayoutParams(9, 2, 0), create.model, new Intent("android.settings.DREAM_SETTINGS"), DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.this.activityStarter());
    }

    public SetupDreamComplication(SetupDreamComponent$Factory setupDreamComponent$Factory) {
        this.mComponentFactory = setupDreamComponent$Factory;
    }
}
