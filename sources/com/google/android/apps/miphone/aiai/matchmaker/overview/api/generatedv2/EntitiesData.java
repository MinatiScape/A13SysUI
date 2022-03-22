package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes.dex */
public class EntitiesData implements Parcelable {
    public static final Parcelable.Creator<EntitiesData> CREATOR = new Parcelable.Creator<EntitiesData>() { // from class: com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.EntitiesData.1
        @Override // android.os.Parcelable.Creator
        public final EntitiesData createFromParcel(Parcel in) {
            SuggestParcelables$Entities suggestParcelables$Entities = new SuggestParcelables$Entities(in.readBundle());
            HashMap hashMap = new HashMap();
            SuggestParcelables$ExtrasInfo suggestParcelables$ExtrasInfo = suggestParcelables$Entities.extrasInfo;
            if (suggestParcelables$ExtrasInfo != null && suggestParcelables$ExtrasInfo.containsBitmaps) {
                in.readMap(hashMap, Bitmap.class.getClassLoader());
            }
            HashMap hashMap2 = new HashMap();
            SuggestParcelables$ExtrasInfo suggestParcelables$ExtrasInfo2 = suggestParcelables$Entities.extrasInfo;
            if (suggestParcelables$ExtrasInfo2 != null && suggestParcelables$ExtrasInfo2.containsPendingIntents) {
                in.readMap(hashMap2, PendingIntent.class.getClassLoader());
            }
            return new EntitiesData(suggestParcelables$Entities, hashMap, hashMap2);
        }

        @Override // android.os.Parcelable.Creator
        public final EntitiesData[] newArray(int size) {
            return new EntitiesData[size];
        }
    };
    public final Map<String, Bitmap> bitmapMap;
    public final SuggestParcelables$Entities entities;
    public final Map<String, PendingIntent> pendingIntentMap;

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel out, int flags) {
        Bundle bundle;
        String str;
        Iterator it;
        Iterator it2;
        Iterator it3;
        ArrayList<? extends Parcelable> arrayList;
        Iterator it4;
        SuggestParcelables$Entities suggestParcelables$Entities = this.entities;
        Objects.requireNonNull(suggestParcelables$Entities);
        Bundle bundle2 = new Bundle();
        String str2 = "id";
        bundle2.putString(str2, suggestParcelables$Entities.id);
        bundle2.putBoolean("success", suggestParcelables$Entities.success);
        ArrayList<? extends Parcelable> arrayList2 = null;
        if (suggestParcelables$Entities.entities == null) {
            bundle2.putParcelableArrayList("entities", null);
        } else {
            ArrayList<? extends Parcelable> arrayList3 = new ArrayList<>(suggestParcelables$Entities.entities.size());
            Iterator it5 = suggestParcelables$Entities.entities.iterator();
            while (it5.hasNext()) {
                SuggestParcelables$Entity suggestParcelables$Entity = (SuggestParcelables$Entity) it5.next();
                if (suggestParcelables$Entity == null) {
                    arrayList3.add(arrayList2);
                } else {
                    Bundle bundle3 = new Bundle();
                    bundle3.putString(str2, suggestParcelables$Entity.id);
                    if (suggestParcelables$Entity.actions == null) {
                        bundle3.putParcelableArrayList("actions", arrayList2);
                        str = str2;
                        it = it5;
                    } else {
                        ArrayList<? extends Parcelable> arrayList4 = new ArrayList<>(suggestParcelables$Entity.actions.size());
                        Iterator it6 = suggestParcelables$Entity.actions.iterator();
                        while (it6.hasNext()) {
                            SuggestParcelables$ActionGroup suggestParcelables$ActionGroup = (SuggestParcelables$ActionGroup) it6.next();
                            if (suggestParcelables$ActionGroup == null) {
                                arrayList4.add(arrayList2);
                            } else {
                                Bundle bundle4 = new Bundle();
                                bundle4.putString(str2, suggestParcelables$ActionGroup.id);
                                bundle4.putString("displayName", suggestParcelables$ActionGroup.displayName);
                                SuggestParcelables$Action suggestParcelables$Action = suggestParcelables$ActionGroup.mainAction;
                                if (suggestParcelables$Action == null) {
                                    it3 = it6;
                                    arrayList = null;
                                    bundle4.putBundle("mainAction", null);
                                } else {
                                    it3 = it6;
                                    arrayList = null;
                                    bundle4.putBundle("mainAction", suggestParcelables$Action.writeToBundle());
                                }
                                if (suggestParcelables$ActionGroup.alternateActions == null) {
                                    bundle4.putParcelableArrayList("alternateActions", arrayList);
                                } else {
                                    ArrayList<? extends Parcelable> arrayList5 = new ArrayList<>(suggestParcelables$ActionGroup.alternateActions.size());
                                    Iterator it7 = suggestParcelables$ActionGroup.alternateActions.iterator();
                                    while (it7.hasNext()) {
                                        SuggestParcelables$Action suggestParcelables$Action2 = (SuggestParcelables$Action) it7.next();
                                        if (suggestParcelables$Action2 == null) {
                                            it4 = it7;
                                            arrayList5.add(null);
                                        } else {
                                            it4 = it7;
                                            arrayList5.add(suggestParcelables$Action2.writeToBundle());
                                        }
                                        it7 = it4;
                                    }
                                    bundle4.putParcelableArrayList("alternateActions", arrayList5);
                                }
                                bundle4.putBoolean("isHiddenAction", suggestParcelables$ActionGroup.isHiddenAction);
                                bundle4.putString("opaquePayload", suggestParcelables$ActionGroup.opaquePayload);
                                arrayList4.add(bundle4);
                                it5 = it5;
                                str2 = str2;
                                it6 = it3;
                                arrayList2 = null;
                            }
                        }
                        str = str2;
                        it = it5;
                        bundle3.putParcelableArrayList("actions", arrayList4);
                    }
                    if (suggestParcelables$Entity.entitySpans == null) {
                        bundle3.putParcelableArrayList("entitySpans", null);
                    } else {
                        ArrayList<? extends Parcelable> arrayList6 = new ArrayList<>(suggestParcelables$Entity.entitySpans.size());
                        Iterator it8 = suggestParcelables$Entity.entitySpans.iterator();
                        while (it8.hasNext()) {
                            SuggestParcelables$EntitySpan suggestParcelables$EntitySpan = (SuggestParcelables$EntitySpan) it8.next();
                            if (suggestParcelables$EntitySpan == null) {
                                arrayList6.add(null);
                            } else {
                                Bundle bundle5 = new Bundle();
                                if (suggestParcelables$EntitySpan.rects == null) {
                                    bundle5.putParcelableArrayList("rects", null);
                                } else {
                                    ArrayList<? extends Parcelable> arrayList7 = new ArrayList<>(suggestParcelables$EntitySpan.rects.size());
                                    Iterator it9 = suggestParcelables$EntitySpan.rects.iterator();
                                    while (it9.hasNext()) {
                                        SuggestParcelables$ContentRect suggestParcelables$ContentRect = (SuggestParcelables$ContentRect) it9.next();
                                        if (suggestParcelables$ContentRect == null) {
                                            it2 = it9;
                                            arrayList7.add(null);
                                        } else {
                                            it2 = it9;
                                            arrayList7.add(suggestParcelables$ContentRect.writeToBundle());
                                        }
                                        it9 = it2;
                                    }
                                    bundle5.putParcelableArrayList("rects", arrayList7);
                                }
                                bundle5.putString("selectionId", suggestParcelables$EntitySpan.selectionId);
                                if (suggestParcelables$EntitySpan.rectIndices == null) {
                                    bundle5.putIntegerArrayList("rectIndices", null);
                                } else {
                                    bundle5.putIntegerArrayList("rectIndices", new ArrayList<>(suggestParcelables$EntitySpan.rectIndices));
                                }
                                arrayList6.add(bundle5);
                                it8 = it8;
                            }
                        }
                        bundle3.putParcelableArrayList("entitySpans", arrayList6);
                    }
                    bundle3.putString("searchQueryHint", suggestParcelables$Entity.searchQueryHint);
                    bundle3.putString("annotationTypeName", suggestParcelables$Entity.annotationTypeName);
                    bundle3.putString("annotationSourceName", suggestParcelables$Entity.annotationSourceName);
                    bundle3.putString("verticalTypeName", suggestParcelables$Entity.verticalTypeName);
                    bundle3.putFloat("annotationScore", suggestParcelables$Entity.annotationScore);
                    bundle3.putInt("contentGroupIndex", suggestParcelables$Entity.contentGroupIndex);
                    bundle3.putInt("selectionIndex", suggestParcelables$Entity.selectionIndex);
                    bundle3.putBoolean("isSmartSelection", suggestParcelables$Entity.isSmartSelection);
                    bundle3.putInt("suggestedPresentationMode", suggestParcelables$Entity.suggestedPresentationMode);
                    bundle3.putInt("numWords", suggestParcelables$Entity.numWords);
                    bundle3.putInt("startIndex", suggestParcelables$Entity.startIndex);
                    bundle3.putInt("endIndex", suggestParcelables$Entity.endIndex);
                    bundle3.putString("opaquePayload", suggestParcelables$Entity.opaquePayload);
                    SuggestParcelables$InteractionType suggestParcelables$InteractionType = suggestParcelables$Entity.interactionType;
                    if (suggestParcelables$InteractionType == null) {
                        bundle3.putBundle("interactionType", null);
                    } else {
                        Bundle bundle6 = new Bundle();
                        bundle6.putInt("value", suggestParcelables$InteractionType.value);
                        bundle3.putBundle("interactionType", bundle6);
                    }
                    bundle3.putBoolean("shouldStartForResult", suggestParcelables$Entity.shouldStartForResult);
                    arrayList3.add(bundle3);
                    it5 = it;
                    str2 = str;
                    arrayList2 = null;
                }
            }
            bundle2.putParcelableArrayList("entities", arrayList3);
        }
        SuggestParcelables$Stats suggestParcelables$Stats = suggestParcelables$Entities.stats;
        if (suggestParcelables$Stats == null) {
            bundle = null;
            bundle2.putBundle("stats", null);
        } else {
            bundle = null;
            bundle2.putBundle("stats", suggestParcelables$Stats.writeToBundle());
        }
        if (suggestParcelables$Entities.debugInfo == null) {
            bundle2.putBundle("debugInfo", bundle);
        } else {
            bundle2.putBundle("debugInfo", new Bundle());
        }
        SuggestParcelables$ExtrasInfo suggestParcelables$ExtrasInfo = suggestParcelables$Entities.extrasInfo;
        if (suggestParcelables$ExtrasInfo == null) {
            bundle2.putBundle("extrasInfo", bundle);
        } else {
            Bundle bundle7 = new Bundle();
            bundle7.putBoolean("containsPendingIntents", suggestParcelables$ExtrasInfo.containsPendingIntents);
            bundle7.putBoolean("containsBitmaps", suggestParcelables$ExtrasInfo.containsBitmaps);
            bundle2.putBundle("extrasInfo", bundle7);
        }
        bundle2.putString("opaquePayload", suggestParcelables$Entities.opaquePayload);
        SuggestParcelables$SetupInfo suggestParcelables$SetupInfo = suggestParcelables$Entities.setupInfo;
        if (suggestParcelables$SetupInfo == null) {
            bundle2.putBundle("setupInfo", null);
        } else {
            bundle2.putBundle("setupInfo", suggestParcelables$SetupInfo.writeToBundle());
        }
        bundle2.writeToParcel(out, 0);
        SuggestParcelables$Entities suggestParcelables$Entities2 = this.entities;
        Objects.requireNonNull(suggestParcelables$Entities2);
        if (suggestParcelables$Entities2.extrasInfo != null) {
            SuggestParcelables$Entities suggestParcelables$Entities3 = this.entities;
            Objects.requireNonNull(suggestParcelables$Entities3);
            SuggestParcelables$ExtrasInfo suggestParcelables$ExtrasInfo2 = suggestParcelables$Entities3.extrasInfo;
            Objects.requireNonNull(suggestParcelables$ExtrasInfo2);
            if (suggestParcelables$ExtrasInfo2.containsBitmaps) {
                out.writeMap(this.bitmapMap);
            }
            SuggestParcelables$Entities suggestParcelables$Entities4 = this.entities;
            Objects.requireNonNull(suggestParcelables$Entities4);
            SuggestParcelables$ExtrasInfo suggestParcelables$ExtrasInfo3 = suggestParcelables$Entities4.extrasInfo;
            Objects.requireNonNull(suggestParcelables$ExtrasInfo3);
            if (suggestParcelables$ExtrasInfo3.containsPendingIntents) {
                out.writeMap(this.pendingIntentMap);
            }
        }
    }

    public EntitiesData(SuggestParcelables$Entities entities, HashMap bitmapMap, HashMap pendingIntentMap) {
        this.entities = entities;
        this.bitmapMap = bitmapMap;
        this.pendingIntentMap = pendingIntentMap;
    }
}
