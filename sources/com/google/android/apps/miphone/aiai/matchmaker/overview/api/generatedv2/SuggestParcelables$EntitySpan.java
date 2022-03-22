package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes.dex */
public final class SuggestParcelables$EntitySpan {
    @Nullable
    public ArrayList rectIndices;
    @Nullable
    public ArrayList rects;
    @Nullable
    public String selectionId;

    public SuggestParcelables$EntitySpan(Bundle bundle) {
        if (bundle.containsKey("rects")) {
            ArrayList parcelableArrayList = bundle.getParcelableArrayList("rects");
            if (parcelableArrayList == null) {
                this.rects = null;
            } else {
                this.rects = new ArrayList(parcelableArrayList.size());
                Iterator it = parcelableArrayList.iterator();
                while (it.hasNext()) {
                    Bundle bundle2 = (Bundle) it.next();
                    if (bundle2 == null) {
                        this.rects.add(null);
                    } else {
                        this.rects.add(new SuggestParcelables$ContentRect(bundle2));
                    }
                }
            }
        }
        if (bundle.containsKey("selectionId")) {
            this.selectionId = bundle.getString("selectionId");
        }
        if (bundle.containsKey("rectIndices")) {
            this.rectIndices = bundle.getIntegerArrayList("rectIndices");
        }
    }

    public SuggestParcelables$EntitySpan() {
    }
}
