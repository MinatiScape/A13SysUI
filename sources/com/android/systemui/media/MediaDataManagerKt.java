package com.android.systemui.media;

import com.android.internal.annotations.VisibleForTesting;
import kotlin.collections.EmptyList;
/* compiled from: MediaDataManager.kt */
/* loaded from: classes.dex */
public final class MediaDataManagerKt {
    public static final String[] ART_URIS = {"android.media.metadata.ALBUM_ART_URI", "android.media.metadata.ART_URI", "android.media.metadata.DISPLAY_ICON_URI"};
    public static final SmartspaceMediaData EMPTY_SMARTSPACE_MEDIA_DATA;
    public static final MediaData LOADING;

    @VisibleForTesting
    public static /* synthetic */ void getEMPTY_SMARTSPACE_MEDIA_DATA$annotations() {
    }

    static {
        EmptyList emptyList = EmptyList.INSTANCE;
        LOADING = new MediaData(-1, false, 0, null, null, null, null, null, emptyList, emptyList, null, "INVALID", null, null, null, true, null, 0, false, null, false, null, false, 0L, 16647168);
        EMPTY_SMARTSPACE_MEDIA_DATA = new SmartspaceMediaData("INVALID", false, false, "INVALID", null, emptyList, null, 0, 0L);
    }
}
