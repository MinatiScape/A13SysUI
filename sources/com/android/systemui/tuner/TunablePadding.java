package com.android.systemui.tuner;

import com.android.systemui.tuner.TunerService;
/* loaded from: classes.dex */
public class TunablePadding implements TunerService.Tunable {

    /* loaded from: classes.dex */
    public static class TunablePaddingService {
    }

    @Override // com.android.systemui.tuner.TunerService.Tunable
    public final void onTuningChanged(String str, String str2) {
        if (str2 != null) {
            try {
                Integer.parseInt(str2);
            } catch (NumberFormatException unused) {
            }
        }
        throw null;
    }
}
