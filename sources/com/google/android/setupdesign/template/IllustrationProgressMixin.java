package com.google.android.setupdesign.template;

import android.annotation.TargetApi;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.template.Mixin;
import com.google.android.setupdesign.GlifLayout;
@TargetApi(14)
@Deprecated
/* loaded from: classes.dex */
public final class IllustrationProgressMixin implements Mixin {

    /* loaded from: classes.dex */
    public enum ProgressConfig {
        /* JADX INFO: Fake field, exist only in values array */
        CONFIG_DEFAULT(PartnerConfig.CONFIG_PROGRESS_ILLUSTRATION_DEFAULT),
        /* JADX INFO: Fake field, exist only in values array */
        CONFIG_ACCOUNT(PartnerConfig.CONFIG_PROGRESS_ILLUSTRATION_ACCOUNT),
        /* JADX INFO: Fake field, exist only in values array */
        CONFIG_CONNECTION(PartnerConfig.CONFIG_PROGRESS_ILLUSTRATION_CONNECTION),
        /* JADX INFO: Fake field, exist only in values array */
        CONFIG_UPDATE(PartnerConfig.CONFIG_PROGRESS_ILLUSTRATION_UPDATE);
        
        private final PartnerConfig config;

        ProgressConfig(PartnerConfig partnerConfig) {
            if (partnerConfig.getResourceType() == PartnerConfig.ResourceType.ILLUSTRATION) {
                this.config = partnerConfig;
                return;
            }
            throw new IllegalArgumentException("Illustration progress only allow illustration resource");
        }
    }

    public IllustrationProgressMixin(GlifLayout glifLayout) {
        ProgressConfig[] progressConfigArr = ProgressConfig.$VALUES;
        glifLayout.getContext();
    }
}
