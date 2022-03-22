package com.google.android.apps.miphone.aiai.matchmaker.overview.common;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.ContentParcelables$AppActionSuggestion;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.ContentParcelables$AppIcon;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.ContentParcelables$AppIconType;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.ContentParcelables$AppPackage;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.ContentParcelables$ContentGroup;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.ContentParcelables$Contents;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.ContentParcelables$ExecutionInfo;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.ContentParcelables$SearchSuggestion;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.ContentParcelables$Selection;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.InteractionContextParcelables$InteractionContext;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.SuggestParcelables$ContentRect;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.SuggestParcelables$InteractionType;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.SuggestParcelables$SetupInfo;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.SuggestParcelables$Stats;
import com.google.android.setupcompat.partnerconfig.ResourceEntry;
import java.util.ArrayList;
import java.util.Iterator;
import kotlinx.coroutines.internal.LockFreeLinkedListKt;
/* loaded from: classes.dex */
public final class BundleUtils {
    public static Bundle createClassificationsRequest(String packageName, String activityName, int taskId, long captureTimestampMs, @Nullable Bundle assistBundle, @Nullable InteractionContextParcelables$InteractionContext interactionContext, ContentParcelables$Contents contents) {
        String str;
        Bundle bundle;
        Bundle bundle2;
        String str2;
        String str3;
        Iterator it;
        Bundle bundle3;
        Iterator it2;
        String str4;
        String str5;
        Bundle bundle4;
        ContentParcelables$Contents contentParcelables$Contents = contents;
        Bundle bundle5 = new Bundle();
        bundle5.putString("PackageName", packageName);
        bundle5.putString("ActivityName", activityName);
        bundle5.putInt("TaskId", taskId);
        bundle5.putLong("CaptureTimestampMs", captureTimestampMs);
        bundle5.putBundle("AssistBundle", assistBundle);
        Bundle bundle6 = new Bundle();
        String str6 = "id";
        bundle6.putString(str6, contentParcelables$Contents.id);
        bundle6.putLong("screenSessionId", contentParcelables$Contents.screenSessionId);
        String str7 = "opaquePayload";
        ArrayList<? extends Parcelable> arrayList = null;
        if (contentParcelables$Contents.contentGroups == null) {
            bundle6.putParcelableArrayList("contentGroups", null);
            bundle = bundle5;
            str = str7;
        } else {
            ArrayList<? extends Parcelable> arrayList2 = new ArrayList<>(contentParcelables$Contents.contentGroups.size());
            Iterator it3 = contentParcelables$Contents.contentGroups.iterator();
            while (it3.hasNext()) {
                ContentParcelables$ContentGroup contentParcelables$ContentGroup = (ContentParcelables$ContentGroup) it3.next();
                if (contentParcelables$ContentGroup == null) {
                    arrayList2.add(arrayList);
                    bundle3 = bundle5;
                    str3 = str6;
                    str2 = str7;
                    it = it3;
                } else {
                    Bundle bundle7 = new Bundle();
                    if (contentParcelables$ContentGroup.contentRects == null) {
                        bundle7.putParcelableArrayList("contentRects", arrayList);
                    } else {
                        ArrayList<? extends Parcelable> arrayList3 = new ArrayList<>(contentParcelables$ContentGroup.contentRects.size());
                        Iterator it4 = contentParcelables$ContentGroup.contentRects.iterator();
                        while (it4.hasNext()) {
                            SuggestParcelables$ContentRect suggestParcelables$ContentRect = (SuggestParcelables$ContentRect) it4.next();
                            if (suggestParcelables$ContentRect == null) {
                                arrayList3.add(arrayList);
                            } else {
                                arrayList3.add(suggestParcelables$ContentRect.writeToBundle());
                            }
                        }
                        bundle7.putParcelableArrayList("contentRects", arrayList3);
                    }
                    if (contentParcelables$ContentGroup.selections == null) {
                        bundle7.putParcelableArrayList("selections", arrayList);
                        bundle3 = bundle5;
                        it = it3;
                    } else {
                        ArrayList<? extends Parcelable> arrayList4 = new ArrayList<>(contentParcelables$ContentGroup.selections.size());
                        Iterator it5 = contentParcelables$ContentGroup.selections.iterator();
                        while (it5.hasNext()) {
                            ContentParcelables$Selection contentParcelables$Selection = (ContentParcelables$Selection) it5.next();
                            if (contentParcelables$Selection == null) {
                                arrayList4.add(arrayList);
                            } else {
                                Bundle bundle8 = new Bundle();
                                if (contentParcelables$Selection.rectIndices == null) {
                                    bundle8.putIntegerArrayList("rectIndices", null);
                                    bundle4 = bundle5;
                                } else {
                                    bundle4 = bundle5;
                                    bundle8.putIntegerArrayList("rectIndices", new ArrayList<>(contentParcelables$Selection.rectIndices));
                                }
                                bundle8.putString(str6, contentParcelables$Selection.id);
                                bundle8.putBoolean("isSmartSelection", contentParcelables$Selection.isSmartSelection);
                                bundle8.putInt("suggestedPresentationMode", contentParcelables$Selection.suggestedPresentationMode);
                                bundle8.putString(str7, contentParcelables$Selection.opaquePayload);
                                SuggestParcelables$InteractionType suggestParcelables$InteractionType = contentParcelables$Selection.interactionType;
                                if (suggestParcelables$InteractionType == null) {
                                    bundle8.putBundle("interactionType", null);
                                } else {
                                    Bundle bundle9 = new Bundle();
                                    bundle9.putInt("value", suggestParcelables$InteractionType.value);
                                    bundle8.putBundle("interactionType", bundle9);
                                }
                                bundle8.putInt("contentGroupIndex", contentParcelables$Selection.contentGroupIndex);
                                arrayList4.add(bundle8);
                                it3 = it3;
                                it5 = it5;
                                bundle5 = bundle4;
                                arrayList = null;
                            }
                        }
                        bundle3 = bundle5;
                        it = it3;
                        bundle7.putParcelableArrayList("selections", arrayList4);
                    }
                    bundle7.putString("text", contentParcelables$ContentGroup.text);
                    bundle7.putInt("numLines", contentParcelables$ContentGroup.numLines);
                    if (contentParcelables$ContentGroup.searchSuggestions == null) {
                        bundle7.putParcelableArrayList("searchSuggestions", null);
                        str3 = str6;
                        str2 = str7;
                    } else {
                        ArrayList<? extends Parcelable> arrayList5 = new ArrayList<>(contentParcelables$ContentGroup.searchSuggestions.size());
                        Iterator it6 = contentParcelables$ContentGroup.searchSuggestions.iterator();
                        while (it6.hasNext()) {
                            ContentParcelables$SearchSuggestion contentParcelables$SearchSuggestion = (ContentParcelables$SearchSuggestion) it6.next();
                            if (contentParcelables$SearchSuggestion == null) {
                                arrayList5.add(null);
                            } else {
                                Bundle bundle10 = new Bundle();
                                ContentParcelables$AppActionSuggestion contentParcelables$AppActionSuggestion = contentParcelables$SearchSuggestion.appActionSuggestion;
                                if (contentParcelables$AppActionSuggestion == null) {
                                    bundle10.putBundle("appActionSuggestion", null);
                                    str4 = str6;
                                    it2 = it6;
                                } else {
                                    Bundle bundle11 = new Bundle();
                                    str4 = str6;
                                    it2 = it6;
                                    bundle11.putString("displayText", contentParcelables$AppActionSuggestion.displayText);
                                    bundle11.putString("subtitle", contentParcelables$AppActionSuggestion.subtitle);
                                    bundle10.putBundle("appActionSuggestion", bundle11);
                                }
                                ContentParcelables$AppIcon contentParcelables$AppIcon = contentParcelables$SearchSuggestion.appIcon;
                                if (contentParcelables$AppIcon == null) {
                                    bundle10.putBundle("appIcon", null);
                                    str5 = str7;
                                } else {
                                    Bundle bundle12 = new Bundle();
                                    bundle12.putString("iconUri", contentParcelables$AppIcon.iconUri);
                                    ContentParcelables$AppPackage contentParcelables$AppPackage = contentParcelables$AppIcon.appPackage;
                                    if (contentParcelables$AppPackage == null) {
                                        str5 = str7;
                                        bundle12.putBundle("appPackage", null);
                                    } else {
                                        str5 = str7;
                                        Bundle bundle13 = new Bundle();
                                        bundle13.putString(ResourceEntry.KEY_PACKAGE_NAME, contentParcelables$AppPackage.packageName);
                                        bundle12.putBundle("appPackage", bundle13);
                                    }
                                    ContentParcelables$AppIconType contentParcelables$AppIconType = contentParcelables$AppIcon.appIconType;
                                    if (contentParcelables$AppIconType == null) {
                                        bundle12.putBundle("appIconType", null);
                                    } else {
                                        Bundle bundle14 = new Bundle();
                                        bundle14.putInt("value", contentParcelables$AppIconType.value);
                                        bundle12.putBundle("appIconType", bundle14);
                                    }
                                    bundle10.putBundle("appIcon", bundle12);
                                }
                                ContentParcelables$ExecutionInfo contentParcelables$ExecutionInfo = contentParcelables$SearchSuggestion.executionInfo;
                                if (contentParcelables$ExecutionInfo == null) {
                                    bundle10.putBundle("executionInfo", null);
                                } else {
                                    Bundle bundle15 = new Bundle();
                                    bundle15.putString("deeplinkUri", contentParcelables$ExecutionInfo.deeplinkUri);
                                    bundle10.putBundle("executionInfo", bundle15);
                                }
                                bundle10.putFloat("confScore", contentParcelables$SearchSuggestion.confScore);
                                arrayList5.add(bundle10);
                                str6 = str4;
                                it6 = it2;
                                str7 = str5;
                            }
                        }
                        str3 = str6;
                        str2 = str7;
                        bundle7.putParcelableArrayList("searchSuggestions", arrayList5);
                    }
                    arrayList2.add(bundle7);
                }
                it3 = it;
                str6 = str3;
                str7 = str2;
                bundle5 = bundle3;
                arrayList = null;
            }
            bundle = bundle5;
            str = str7;
            bundle6.putParcelableArrayList("contentGroups", arrayList2);
            contentParcelables$Contents = contents;
        }
        SuggestParcelables$Stats suggestParcelables$Stats = contentParcelables$Contents.stats;
        if (suggestParcelables$Stats == null) {
            bundle2 = null;
            bundle6.putBundle("stats", null);
        } else {
            bundle2 = null;
            bundle6.putBundle("stats", suggestParcelables$Stats.writeToBundle());
        }
        if (contentParcelables$Contents.debugInfo == null) {
            bundle6.putBundle("debugInfo", bundle2);
        } else {
            bundle6.putBundle("debugInfo", new Bundle());
        }
        bundle6.putString(str, contentParcelables$Contents.opaquePayload);
        SuggestParcelables$SetupInfo suggestParcelables$SetupInfo = contentParcelables$Contents.setupInfo;
        if (suggestParcelables$SetupInfo == null) {
            bundle6.putBundle("setupInfo", bundle2);
        } else {
            bundle6.putBundle("setupInfo", suggestParcelables$SetupInfo.writeToBundle());
        }
        bundle6.putString("contentUri", contentParcelables$Contents.contentUri);
        bundle.putBundle("Contents", bundle6);
        if (interactionContext == null) {
            bundle.putBundle("InteractionContext", null);
        } else {
            bundle.putBundle("InteractionContext", interactionContext.writeToBundle());
        }
        bundle.putInt("Version", 4);
        bundle.putInt("BundleTypedVersion", 3);
        return bundle;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:29:0x011b  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x017a  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x01ba  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x01eb  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x02cd  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x02fd  */
    /* JADX WARN: Type inference failed for: r0v18, types: [android.os.Bundle, java.lang.String] */
    /* JADX WARN: Type inference failed for: r0v19 */
    /* JADX WARN: Type inference failed for: r0v23 */
    /* JADX WARN: Type inference failed for: r0v24 */
    /* JADX WARN: Type inference failed for: r0v48 */
    /* JADX WARN: Type inference failed for: r0v6 */
    /* JADX WARN: Type inference failed for: r11v0, types: [android.os.Bundle, java.lang.Object, android.os.BaseBundle] */
    /* JADX WARN: Unknown variable types count: 2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static android.os.Bundle createFeedbackRequest(@android.support.annotation.Nullable com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$FeedbackBatch r22) {
        /*
            Method dump skipped, instructions count: 854
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.apps.miphone.aiai.matchmaker.overview.common.BundleUtils.createFeedbackRequest(com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$FeedbackBatch):android.os.Bundle");
    }

    public static Bundle createSelectionsRequest(String packageName, String activityName, int taskId, long captureTimestampMs, @Nullable InteractionContextParcelables$InteractionContext interactionContext, @Nullable Bundle assistBundle, @Nullable LockFreeLinkedListKt parsedViewHierarchy) {
        Bundle bundle = new Bundle();
        bundle.putString("PackageName", packageName);
        bundle.putString("ActivityName", activityName);
        bundle.putInt("TaskId", taskId);
        bundle.putLong("CaptureTimestampMs", captureTimestampMs);
        if (interactionContext == null) {
            bundle.putBundle("InteractionContext", null);
        } else {
            bundle.putBundle("InteractionContext", interactionContext.writeToBundle());
        }
        bundle.putBundle("AssistBundle", assistBundle);
        if (parsedViewHierarchy == null) {
            bundle.putBundle("ParsedViewHierarchy", null);
        } else {
            Bundle bundle2 = new Bundle();
            bundle2.putLong("acquisitionStartTime", 0L);
            bundle2.putLong("acquisitionEndTime", 0L);
            bundle2.putBoolean("isHomeActivity", false);
            bundle2.putParcelableArrayList("windows", null);
            bundle2.putBoolean("hasKnownIssues", false);
            bundle2.putString(ResourceEntry.KEY_PACKAGE_NAME, null);
            bundle2.putString("activityClassName", null);
            bundle2.putBundle("insetsRect", null);
            bundle.putBundle("ParsedViewHierarchy", bundle2);
        }
        bundle.putInt("Version", 4);
        bundle.putInt("BundleTypedVersion", 3);
        return bundle;
    }
}
