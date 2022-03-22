package com.google.android.systemui.controls;

import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.controller.ControlsTileResourceConfiguration;
import com.android.systemui.controls.controller.StructureInfo;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: GoogleControlsTileResourceConfigurationImpl.kt */
/* loaded from: classes.dex */
public final class GoogleControlsTileResourceConfigurationImpl implements ControlsTileResourceConfiguration {
    public final ControlsController controlsController;

    @Override // com.android.systemui.controls.controller.ControlsTileResourceConfiguration
    public final int getTileImageId() {
        StructureInfo preferredStructure = this.controlsController.getPreferredStructure();
        Objects.requireNonNull(preferredStructure);
        if (Intrinsics.areEqual(preferredStructure.componentName.getPackageName(), "com.google.android.apps.chromecast.app")) {
            return 2131231728;
        }
        return 2131231671;
    }

    @Override // com.android.systemui.controls.controller.ControlsTileResourceConfiguration
    public final int getTileTitleId() {
        StructureInfo preferredStructure = this.controlsController.getPreferredStructure();
        Objects.requireNonNull(preferredStructure);
        if (Intrinsics.areEqual(preferredStructure.componentName.getPackageName(), "com.google.android.apps.chromecast.app")) {
            return 2131952448;
        }
        return 2131953053;
    }

    public GoogleControlsTileResourceConfigurationImpl(ControlsController controlsController) {
        this.controlsController = controlsController;
    }
}
