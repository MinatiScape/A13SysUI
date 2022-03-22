package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
/* loaded from: classes.dex */
public final class SuggestParcelables$Stats {
    public long endTimestampMs;
    public long entityExtractionMs;
    public long ocrDetectionMs;
    public long ocrMs;
    public long startTimestampMs;

    public final Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putLong("startTimestampMs", this.startTimestampMs);
        bundle.putLong("endTimestampMs", this.endTimestampMs);
        bundle.putLong("ocrMs", this.ocrMs);
        bundle.putLong("ocrDetectionMs", this.ocrDetectionMs);
        bundle.putLong("entityExtractionMs", this.entityExtractionMs);
        return bundle;
    }

    public SuggestParcelables$Stats(Bundle bundle) {
        if (bundle.containsKey("startTimestampMs")) {
            this.startTimestampMs = bundle.getLong("startTimestampMs");
        }
        if (bundle.containsKey("endTimestampMs")) {
            this.endTimestampMs = bundle.getLong("endTimestampMs");
        }
        if (bundle.containsKey("ocrMs")) {
            this.ocrMs = bundle.getLong("ocrMs");
        }
        if (bundle.containsKey("ocrDetectionMs")) {
            this.ocrDetectionMs = bundle.getLong("ocrDetectionMs");
        }
        if (bundle.containsKey("entityExtractionMs")) {
            this.entityExtractionMs = bundle.getLong("entityExtractionMs");
        }
    }
}
