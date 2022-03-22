package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
/* loaded from: classes.dex */
public final class SuggestParcelables$ContentRect {
    public int beginChar;
    public int contentGroupIndex;
    public SuggestParcelables$ContentType contentType;
    @Nullable
    public String contentUri;
    public int endChar;
    public int lineId;
    @Nullable
    public SuggestParcelables$OnScreenRect rect;
    @Nullable
    public String text;

    public SuggestParcelables$ContentRect(Bundle bundle) {
        SuggestParcelables$ContentType suggestParcelables$ContentType = null;
        if (bundle.containsKey("rect")) {
            Bundle bundle2 = bundle.getBundle("rect");
            if (bundle2 == null) {
                this.rect = null;
            } else {
                this.rect = new SuggestParcelables$OnScreenRect(bundle2);
            }
        }
        if (bundle.containsKey("text")) {
            this.text = bundle.getString("text");
        }
        if (bundle.containsKey("contentType")) {
            Bundle bundle3 = bundle.getBundle("contentType");
            if (bundle3 == null) {
                this.contentType = null;
            } else {
                int i = bundle3.getInt("value");
                if (i == 0) {
                    suggestParcelables$ContentType = SuggestParcelables$ContentType.CONTENT_TYPE_UNKNOWN;
                } else if (i == 1) {
                    suggestParcelables$ContentType = SuggestParcelables$ContentType.CONTENT_TYPE_TEXT;
                } else if (i == 2) {
                    suggestParcelables$ContentType = SuggestParcelables$ContentType.CONTENT_TYPE_IMAGE;
                }
                this.contentType = suggestParcelables$ContentType;
            }
        }
        if (bundle.containsKey("lineId")) {
            this.lineId = bundle.getInt("lineId");
        }
        if (bundle.containsKey("contentUri")) {
            this.contentUri = bundle.getString("contentUri");
        }
        if (bundle.containsKey("contentGroupIndex")) {
            this.contentGroupIndex = bundle.getInt("contentGroupIndex");
        }
        if (bundle.containsKey("beginChar")) {
            this.beginChar = bundle.getInt("beginChar");
        }
        if (bundle.containsKey("endChar")) {
            this.endChar = bundle.getInt("endChar");
        }
    }

    public final Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        SuggestParcelables$OnScreenRect suggestParcelables$OnScreenRect = this.rect;
        if (suggestParcelables$OnScreenRect == null) {
            bundle.putBundle("rect", null);
        } else {
            Bundle bundle2 = new Bundle();
            bundle2.putFloat("left", suggestParcelables$OnScreenRect.left);
            bundle2.putFloat("top", suggestParcelables$OnScreenRect.top);
            bundle2.putFloat("width", suggestParcelables$OnScreenRect.width);
            bundle2.putFloat("height", suggestParcelables$OnScreenRect.height);
            bundle.putBundle("rect", bundle2);
        }
        bundle.putString("text", this.text);
        SuggestParcelables$ContentType suggestParcelables$ContentType = this.contentType;
        if (suggestParcelables$ContentType == null) {
            bundle.putBundle("contentType", null);
        } else {
            Bundle bundle3 = new Bundle();
            bundle3.putInt("value", suggestParcelables$ContentType.value);
            bundle.putBundle("contentType", bundle3);
        }
        bundle.putInt("lineId", this.lineId);
        bundle.putString("contentUri", this.contentUri);
        bundle.putInt("contentGroupIndex", this.contentGroupIndex);
        bundle.putInt("beginChar", this.beginChar);
        bundle.putInt("endChar", this.endChar);
        return bundle;
    }

    public SuggestParcelables$ContentRect() {
    }
}
