package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes.dex */
public final class ContentParcelables$ContentGroup {
    @Nullable
    public ArrayList contentRects;
    public int numLines;
    @Nullable
    public ArrayList searchSuggestions;
    @Nullable
    public ArrayList selections;
    @Nullable
    public String text;

    public ContentParcelables$ContentGroup(Bundle bundle) {
        if (bundle.containsKey("contentRects")) {
            ArrayList parcelableArrayList = bundle.getParcelableArrayList("contentRects");
            if (parcelableArrayList == null) {
                this.contentRects = null;
            } else {
                this.contentRects = new ArrayList(parcelableArrayList.size());
                Iterator it = parcelableArrayList.iterator();
                while (it.hasNext()) {
                    Bundle bundle2 = (Bundle) it.next();
                    if (bundle2 == null) {
                        this.contentRects.add(null);
                    } else {
                        this.contentRects.add(new SuggestParcelables$ContentRect(bundle2));
                    }
                }
            }
        }
        if (bundle.containsKey("selections")) {
            ArrayList parcelableArrayList2 = bundle.getParcelableArrayList("selections");
            if (parcelableArrayList2 == null) {
                this.selections = null;
            } else {
                this.selections = new ArrayList(parcelableArrayList2.size());
                Iterator it2 = parcelableArrayList2.iterator();
                while (it2.hasNext()) {
                    Bundle bundle3 = (Bundle) it2.next();
                    if (bundle3 == null) {
                        this.selections.add(null);
                    } else {
                        this.selections.add(new ContentParcelables$Selection(bundle3));
                    }
                }
            }
        }
        if (bundle.containsKey("text")) {
            this.text = bundle.getString("text");
        }
        if (bundle.containsKey("numLines")) {
            this.numLines = bundle.getInt("numLines");
        }
        if (bundle.containsKey("searchSuggestions")) {
            ArrayList parcelableArrayList3 = bundle.getParcelableArrayList("searchSuggestions");
            if (parcelableArrayList3 == null) {
                this.searchSuggestions = null;
                return;
            }
            this.searchSuggestions = new ArrayList(parcelableArrayList3.size());
            Iterator it3 = parcelableArrayList3.iterator();
            while (it3.hasNext()) {
                Bundle bundle4 = (Bundle) it3.next();
                if (bundle4 == null) {
                    this.searchSuggestions.add(null);
                } else {
                    this.searchSuggestions.add(new ContentParcelables$SearchSuggestion(bundle4));
                }
            }
        }
    }

    public ContentParcelables$ContentGroup() {
    }
}
