package com.android.systemui.dreams.complication;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import com.android.settingslib.wifi.WifiTracker$$ExternalSyntheticLambda0;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.util.ViewController;
import com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda0;
import com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda12;
import com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda8;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;
/* loaded from: classes.dex */
public final class ComplicationHostViewController extends ViewController<ConstraintLayout> {
    public final ComplicationCollectionViewModel mComplicationCollectionViewModel;
    public final HashMap<ComplicationId, Complication.ViewHolder> mComplications = new HashMap<>();
    public final ComplicationLayoutEngine mLayoutEngine;
    public final LifecycleOwner mLifecycleOwner;

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
    }

    @Override // com.android.systemui.util.ViewController
    public final void onInit() {
        ComplicationCollectionViewModel complicationCollectionViewModel = this.mComplicationCollectionViewModel;
        Objects.requireNonNull(complicationCollectionViewModel);
        complicationCollectionViewModel.mComplications.observe(this.mLifecycleOwner, new Observer() { // from class: com.android.systemui.dreams.complication.ComplicationHostViewController$$ExternalSyntheticLambda0
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                ComplicationHostViewController complicationHostViewController = ComplicationHostViewController.this;
                Collection collection = (Collection) obj;
                Objects.requireNonNull(complicationHostViewController);
                ((Collection) complicationHostViewController.mComplications.keySet().stream().filter(new WifiTracker$$ExternalSyntheticLambda0((Collection) collection.stream().map(ComplicationHostViewController$$ExternalSyntheticLambda1.INSTANCE).collect(Collectors.toSet()), 1)).collect(Collectors.toSet())).forEach(new WifiPickerTracker$$ExternalSyntheticLambda0(complicationHostViewController, 1));
                ((Collection) collection.stream().filter(new WifiPickerTracker$$ExternalSyntheticLambda12(complicationHostViewController, 1)).collect(Collectors.toSet())).forEach(new BubbleController$$ExternalSyntheticLambda8(complicationHostViewController, 1));
            }
        });
    }

    public ComplicationHostViewController(ConstraintLayout constraintLayout, ComplicationLayoutEngine complicationLayoutEngine, LifecycleOwner lifecycleOwner, ComplicationCollectionViewModel complicationCollectionViewModel) {
        super(constraintLayout);
        this.mLayoutEngine = complicationLayoutEngine;
        this.mLifecycleOwner = lifecycleOwner;
        this.mComplicationCollectionViewModel = complicationCollectionViewModel;
    }
}
