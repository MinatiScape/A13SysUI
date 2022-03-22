package com.google.android.setupcompat.template;

import android.content.Context;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.ServiceConfigurationError;
import kotlinx.coroutines.android.AndroidExceptionPreHandler;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class FooterBarMixin$$ExternalSyntheticOutline0 {
    public static float m(Context context, Context context2, PartnerConfig partnerConfig, float f) {
        PartnerConfigHelper partnerConfigHelper = PartnerConfigHelper.get(context);
        Objects.requireNonNull(partnerConfigHelper);
        return partnerConfigHelper.getDimension(context2, partnerConfig, f);
    }

    public static /* synthetic */ Iterator m() {
        try {
            return Arrays.asList(new AndroidExceptionPreHandler()).iterator();
        } catch (Throwable th) {
            throw new ServiceConfigurationError(th.getMessage(), th);
        }
    }
}
