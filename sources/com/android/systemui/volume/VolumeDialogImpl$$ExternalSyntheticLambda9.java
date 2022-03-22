package com.android.systemui.volume;

import android.content.Context;
import com.android.systemui.SystemUIAppComponentFactory;
import com.android.systemui.SystemUIFactory;
import com.android.systemui.volume.CaptionsToggleImageButton;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class VolumeDialogImpl$$ExternalSyntheticLambda9 implements SystemUIAppComponentFactory.ContextAvailableCallback, CaptionsToggleImageButton.ConfirmedTapListener {
    public final /* synthetic */ Object f$0;

    public /* synthetic */ VolumeDialogImpl$$ExternalSyntheticLambda9(Object obj) {
        this.f$0 = obj;
    }

    @Override // com.android.systemui.SystemUIAppComponentFactory.ContextAvailableCallback
    public final void onContextAvailable(Context context) {
        SystemUIAppComponentFactory systemUIAppComponentFactory = (SystemUIAppComponentFactory) this.f$0;
        int i = SystemUIAppComponentFactory.$r8$clinit;
        Objects.requireNonNull(systemUIAppComponentFactory);
        SystemUIFactory.createFromConfig(context, false);
        SystemUIFactory systemUIFactory = SystemUIFactory.mFactory;
        Objects.requireNonNull(systemUIFactory);
        systemUIFactory.mSysUIComponent.inject(systemUIAppComponentFactory);
    }
}
