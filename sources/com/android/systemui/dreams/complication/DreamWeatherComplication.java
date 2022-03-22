package com.android.systemui.dreams.complication;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.android.systemui.CoreStartable;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.dreams.complication.dagger.DreamWeatherComplicationComponent$Factory;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController;
import com.android.systemui.util.ViewController;
import com.google.android.systemui.titan.DaggerTitanGlobalRootComponent;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DreamWeatherComplication implements Complication {
    public DreamWeatherComplicationComponent$Factory mComponentFactory;

    /* loaded from: classes.dex */
    public static class DreamWeatherViewController extends ViewController<TextView> {
        public final LockscreenSmartspaceController mSmartSpaceController;
        public DreamWeatherComplication$DreamWeatherViewController$$ExternalSyntheticLambda0 mSmartspaceTargetListener;

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.plugins.BcSmartspaceDataPlugin$SmartspaceTargetListener, com.android.systemui.dreams.complication.DreamWeatherComplication$DreamWeatherViewController$$ExternalSyntheticLambda0] */
        /* JADX WARN: Unknown variable types count: 1 */
        @Override // com.android.systemui.util.ViewController
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void onViewAttached() {
            /*
                r2 = this;
                com.android.systemui.dreams.complication.DreamWeatherComplication$DreamWeatherViewController$$ExternalSyntheticLambda0 r0 = new com.android.systemui.dreams.complication.DreamWeatherComplication$DreamWeatherViewController$$ExternalSyntheticLambda0
                r0.<init>()
                r2.mSmartspaceTargetListener = r0
                com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController r2 = r2.mSmartSpaceController
                java.util.Objects.requireNonNull(r2)
                com.android.systemui.util.concurrency.Execution r1 = r2.execution
                r1.assertIsMainThread()
                com.android.systemui.plugins.BcSmartspaceDataPlugin r2 = r2.plugin
                if (r2 != 0) goto L_0x0016
                goto L_0x0019
            L_0x0016:
                r2.registerListener(r0)
            L_0x0019:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dreams.complication.DreamWeatherComplication.DreamWeatherViewController.onViewAttached():void");
        }

        @Override // com.android.systemui.util.ViewController
        public final void onViewDetached() {
            LockscreenSmartspaceController lockscreenSmartspaceController = this.mSmartSpaceController;
            DreamWeatherComplication$DreamWeatherViewController$$ExternalSyntheticLambda0 dreamWeatherComplication$DreamWeatherViewController$$ExternalSyntheticLambda0 = this.mSmartspaceTargetListener;
            Objects.requireNonNull(lockscreenSmartspaceController);
            lockscreenSmartspaceController.execution.assertIsMainThread();
            BcSmartspaceDataPlugin bcSmartspaceDataPlugin = lockscreenSmartspaceController.plugin;
            if (bcSmartspaceDataPlugin != null) {
                bcSmartspaceDataPlugin.unregisterListener(dreamWeatherComplication$DreamWeatherViewController$$ExternalSyntheticLambda0);
            }
        }

        public DreamWeatherViewController(TextView textView, LockscreenSmartspaceController lockscreenSmartspaceController) {
            super(textView);
            this.mSmartSpaceController = lockscreenSmartspaceController;
        }
    }

    /* loaded from: classes.dex */
    public static class Registrant extends CoreStartable {
        public final DreamWeatherComplication mComplication;
        public final DreamOverlayStateController mDreamOverlayStateController;
        public final LockscreenSmartspaceController mSmartSpaceController;

        @Override // com.android.systemui.CoreStartable
        public final void start() {
            if (this.mSmartSpaceController.isEnabled()) {
                this.mDreamOverlayStateController.addComplication(this.mComplication);
            }
        }

        public Registrant(Context context, LockscreenSmartspaceController lockscreenSmartspaceController, DreamOverlayStateController dreamOverlayStateController, DreamWeatherComplication dreamWeatherComplication) {
            super(context);
            this.mSmartSpaceController = lockscreenSmartspaceController;
            this.mDreamOverlayStateController = dreamOverlayStateController;
            this.mComplication = dreamWeatherComplication;
        }
    }

    @Override // com.android.systemui.dreams.complication.Complication
    public final int getRequiredTypeAvailability() {
        return 4;
    }

    /* loaded from: classes.dex */
    public static class DreamWeatherViewHolder implements Complication.ViewHolder {
        public final ComplicationLayoutParams mLayoutParams;
        public final TextView mView;

        public DreamWeatherViewHolder(TextView textView, DreamWeatherViewController dreamWeatherViewController, ComplicationLayoutParams complicationLayoutParams) {
            this.mView = textView;
            this.mLayoutParams = complicationLayoutParams;
            dreamWeatherViewController.init();
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
        DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.DreamWeatherComplicationComponentImpl create = this.mComponentFactory.create();
        Objects.requireNonNull(create);
        return new DreamWeatherViewHolder(create.provideComplicationViewProvider.mo144get(), new DreamWeatherViewController(create.provideComplicationViewProvider.mo144get(), DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.this.lockscreenSmartspaceControllerProvider.mo144get()), create.provideLayoutParamsProvider.mo144get());
    }

    public DreamWeatherComplication(DreamWeatherComplicationComponent$Factory dreamWeatherComplicationComponent$Factory) {
        this.mComponentFactory = dreamWeatherComplicationComponent$Factory;
    }
}
