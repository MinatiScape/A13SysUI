package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes.dex */
public final class SuggestParcelables$Entity {
    @Nullable
    public ArrayList actions;
    public float annotationScore;
    @Nullable
    public String annotationSourceName;
    @Nullable
    public String annotationTypeName;
    public int contentGroupIndex;
    public int endIndex;
    @Nullable
    public ArrayList entitySpans;
    @Nullable
    public String id;
    public SuggestParcelables$InteractionType interactionType;
    public boolean isSmartSelection;
    public int numWords;
    @Nullable
    public String opaquePayload;
    @Nullable
    public String searchQueryHint;
    public int selectionIndex;
    public boolean shouldStartForResult;
    public int startIndex;
    public int suggestedPresentationMode;
    @Nullable
    public String verticalTypeName;

    public SuggestParcelables$Entity(Bundle bundle) {
        if (bundle.containsKey("id")) {
            this.id = bundle.getString("id");
        }
        if (bundle.containsKey("actions")) {
            ArrayList parcelableArrayList = bundle.getParcelableArrayList("actions");
            if (parcelableArrayList == null) {
                this.actions = null;
            } else {
                this.actions = new ArrayList(parcelableArrayList.size());
                Iterator it = parcelableArrayList.iterator();
                while (it.hasNext()) {
                    Bundle bundle2 = (Bundle) it.next();
                    if (bundle2 == null) {
                        this.actions.add(null);
                    } else {
                        this.actions.add(new SuggestParcelables$ActionGroup(bundle2));
                    }
                }
            }
        }
        if (bundle.containsKey("entitySpans")) {
            ArrayList parcelableArrayList2 = bundle.getParcelableArrayList("entitySpans");
            if (parcelableArrayList2 == null) {
                this.entitySpans = null;
            } else {
                this.entitySpans = new ArrayList(parcelableArrayList2.size());
                Iterator it2 = parcelableArrayList2.iterator();
                while (it2.hasNext()) {
                    Bundle bundle3 = (Bundle) it2.next();
                    if (bundle3 == null) {
                        this.entitySpans.add(null);
                    } else {
                        this.entitySpans.add(new SuggestParcelables$EntitySpan(bundle3));
                    }
                }
            }
        }
        if (bundle.containsKey("searchQueryHint")) {
            this.searchQueryHint = bundle.getString("searchQueryHint");
        }
        if (bundle.containsKey("annotationTypeName")) {
            this.annotationTypeName = bundle.getString("annotationTypeName");
        }
        if (bundle.containsKey("annotationSourceName")) {
            this.annotationSourceName = bundle.getString("annotationSourceName");
        }
        if (bundle.containsKey("verticalTypeName")) {
            this.verticalTypeName = bundle.getString("verticalTypeName");
        }
        if (bundle.containsKey("annotationScore")) {
            this.annotationScore = bundle.getFloat("annotationScore");
        }
        if (bundle.containsKey("contentGroupIndex")) {
            this.contentGroupIndex = bundle.getInt("contentGroupIndex");
        }
        if (bundle.containsKey("selectionIndex")) {
            this.selectionIndex = bundle.getInt("selectionIndex");
        }
        if (bundle.containsKey("isSmartSelection")) {
            this.isSmartSelection = bundle.getBoolean("isSmartSelection");
        }
        if (bundle.containsKey("suggestedPresentationMode")) {
            this.suggestedPresentationMode = bundle.getInt("suggestedPresentationMode");
        }
        if (bundle.containsKey("numWords")) {
            this.numWords = bundle.getInt("numWords");
        }
        if (bundle.containsKey("startIndex")) {
            this.startIndex = bundle.getInt("startIndex");
        }
        if (bundle.containsKey("endIndex")) {
            this.endIndex = bundle.getInt("endIndex");
        }
        if (bundle.containsKey("opaquePayload")) {
            this.opaquePayload = bundle.getString("opaquePayload");
        }
        if (bundle.containsKey("interactionType")) {
            Bundle bundle4 = bundle.getBundle("interactionType");
            if (bundle4 == null) {
                this.interactionType = null;
            } else {
                this.interactionType = SuggestParcelables$InteractionType.create(bundle4);
            }
        }
        if (bundle.containsKey("shouldStartForResult")) {
            this.shouldStartForResult = bundle.getBoolean("shouldStartForResult");
        }
    }

    public SuggestParcelables$Entity() {
    }
}
