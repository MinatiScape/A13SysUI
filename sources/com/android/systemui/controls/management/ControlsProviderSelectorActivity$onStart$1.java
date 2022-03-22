package com.android.systemui.controls.management;

import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Pair;
import android.view.ViewGroup;
import androidx.mediarouter.R$bool;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
/* compiled from: ControlsProviderSelectorActivity.kt */
/* loaded from: classes.dex */
public /* synthetic */ class ControlsProviderSelectorActivity$onStart$1 extends FunctionReferenceImpl implements Function1<ComponentName, Unit> {
    public ControlsProviderSelectorActivity$onStart$1(Object obj) {
        super(1, obj, ControlsProviderSelectorActivity.class, "launchFavoritingActivity", "launchFavoritingActivity(Landroid/content/ComponentName;)V", 0);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Unit invoke(ComponentName componentName) {
        final ComponentName componentName2 = componentName;
        final ControlsProviderSelectorActivity controlsProviderSelectorActivity = (ControlsProviderSelectorActivity) this.receiver;
        Objects.requireNonNull(controlsProviderSelectorActivity);
        controlsProviderSelectorActivity.executor.execute(new Runnable() { // from class: com.android.systemui.controls.management.ControlsProviderSelectorActivity$launchFavoritingActivity$1
            @Override // java.lang.Runnable
            public final void run() {
                ComponentName componentName3 = componentName2;
                if (componentName3 != null) {
                    ControlsProviderSelectorActivity controlsProviderSelectorActivity2 = controlsProviderSelectorActivity;
                    Intent intent = new Intent(controlsProviderSelectorActivity2.getApplicationContext(), ControlsFavoritingActivity.class);
                    intent.putExtra("extra_app_label", controlsProviderSelectorActivity2.listingController.getAppLabel(componentName3));
                    intent.putExtra("android.intent.extra.COMPONENT_NAME", componentName3);
                    intent.putExtra("extra_from_provider_selector", true);
                    controlsProviderSelectorActivity2.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(controlsProviderSelectorActivity2, new Pair[0]).toBundle());
                    R$bool.exitAnimation((ViewGroup) controlsProviderSelectorActivity2.requireViewById(2131427765), new ControlsProviderSelectorActivity$animateExitAndFinish$1(controlsProviderSelectorActivity2)).start();
                }
            }
        });
        return Unit.INSTANCE;
    }
}
