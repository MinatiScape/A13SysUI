package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final class ContentParcelables$Selection {
    public int contentGroupIndex;
    @Nullable
    public String id;
    public SuggestParcelables$InteractionType interactionType;
    public boolean isSmartSelection;
    @Nullable
    public String opaquePayload;
    @Nullable
    public ArrayList rectIndices;
    public int suggestedPresentationMode;

    public ContentParcelables$Selection(Bundle bundle) {
        if (bundle.containsKey("rectIndices")) {
            this.rectIndices = bundle.getIntegerArrayList("rectIndices");
        }
        if (bundle.containsKey("id")) {
            this.id = bundle.getString("id");
        }
        if (bundle.containsKey("isSmartSelection")) {
            this.isSmartSelection = bundle.getBoolean("isSmartSelection");
        }
        if (bundle.containsKey("suggestedPresentationMode")) {
            this.suggestedPresentationMode = bundle.getInt("suggestedPresentationMode");
        }
        if (bundle.containsKey("opaquePayload")) {
            this.opaquePayload = bundle.getString("opaquePayload");
        }
        if (bundle.containsKey("interactionType")) {
            Bundle bundle2 = bundle.getBundle("interactionType");
            if (bundle2 == null) {
                this.interactionType = null;
            } else {
                this.interactionType = SuggestParcelables$InteractionType.create(bundle2);
            }
        }
        if (bundle.containsKey("contentGroupIndex")) {
            this.contentGroupIndex = bundle.getInt("contentGroupIndex");
        }
    }

    public ContentParcelables$Selection() {
    }
}
