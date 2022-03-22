package com.google.android.setupcompat.internal;

import com.google.android.setupcompat.partnerconfig.PartnerConfig;
/* loaded from: classes.dex */
public final class FooterButtonPartnerConfig {
    public final PartnerConfig buttonBackgroundConfig;
    public final PartnerConfig buttonDisableAlphaConfig;
    public final PartnerConfig buttonDisableBackgroundConfig;
    public final PartnerConfig buttonDisableTextColorConfig;
    public final PartnerConfig buttonIconConfig;
    public final PartnerConfig buttonMarginStartConfig;
    public final PartnerConfig buttonMinHeightConfig;
    public final PartnerConfig buttonRadiusConfig;
    public final PartnerConfig buttonRippleColorAlphaConfig;
    public final PartnerConfig buttonTextColorConfig;
    public final PartnerConfig buttonTextSizeConfig;
    public final PartnerConfig buttonTextStyleConfig;
    public final PartnerConfig buttonTextTypeFaceConfig;
    public final int partnerTheme;

    public FooterButtonPartnerConfig(int i, PartnerConfig partnerConfig, PartnerConfig partnerConfig2, PartnerConfig partnerConfig3, PartnerConfig partnerConfig4, PartnerConfig partnerConfig5, PartnerConfig partnerConfig6, PartnerConfig partnerConfig7, PartnerConfig partnerConfig8, PartnerConfig partnerConfig9, PartnerConfig partnerConfig10, PartnerConfig partnerConfig11, PartnerConfig partnerConfig12, PartnerConfig partnerConfig13) {
        this.partnerTheme = i;
        this.buttonTextColorConfig = partnerConfig6;
        this.buttonMarginStartConfig = partnerConfig7;
        this.buttonTextSizeConfig = partnerConfig8;
        this.buttonMinHeightConfig = partnerConfig9;
        this.buttonTextTypeFaceConfig = partnerConfig10;
        this.buttonTextStyleConfig = partnerConfig11;
        this.buttonBackgroundConfig = partnerConfig;
        this.buttonDisableAlphaConfig = partnerConfig2;
        this.buttonDisableBackgroundConfig = partnerConfig3;
        this.buttonDisableTextColorConfig = partnerConfig4;
        this.buttonRadiusConfig = partnerConfig12;
        this.buttonIconConfig = partnerConfig5;
        this.buttonRippleColorAlphaConfig = partnerConfig13;
    }
}
