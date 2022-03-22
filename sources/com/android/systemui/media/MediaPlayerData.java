package com.android.systemui.media;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaCarouselController.kt */
/* loaded from: classes.dex */
public final class MediaPlayerData {
    public static final MediaData EMPTY;
    public static final MediaPlayerData$special$$inlined$thenByDescending$6 comparator;
    public static final TreeMap<MediaSortKey, MediaControlPanel> mediaPlayers;
    public static boolean shouldPrioritizeSs;
    public static SmartspaceMediaData smartspaceMediaData;
    public static final MediaPlayerData INSTANCE = new MediaPlayerData();
    public static final LinkedHashMap mediaData = new LinkedHashMap();

    /* compiled from: MediaCarouselController.kt */
    /* loaded from: classes.dex */
    public static final class MediaSortKey {
        public final MediaData data;
        public final boolean isSsMediaRec;
        public final long updateTime;

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof MediaSortKey)) {
                return false;
            }
            MediaSortKey mediaSortKey = (MediaSortKey) obj;
            return this.isSsMediaRec == mediaSortKey.isSsMediaRec && Intrinsics.areEqual(this.data, mediaSortKey.data) && this.updateTime == mediaSortKey.updateTime;
        }

        public final int hashCode() {
            boolean z = this.isSsMediaRec;
            if (z) {
                z = true;
            }
            int i = z ? 1 : 0;
            int i2 = z ? 1 : 0;
            int hashCode = this.data.hashCode();
            return Long.hashCode(this.updateTime) + ((hashCode + (i * 31)) * 31);
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("MediaSortKey(isSsMediaRec=");
            m.append(this.isSsMediaRec);
            m.append(", data=");
            m.append(this.data);
            m.append(", updateTime=");
            m.append(this.updateTime);
            m.append(')');
            return m.toString();
        }

        public MediaSortKey(boolean z, MediaData mediaData, long j) {
            this.isSsMediaRec = z;
            this.data = mediaData;
            this.updateTime = j;
        }
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.systemui.media.MediaPlayerData$special$$inlined$compareByDescending$1] */
    /* JADX WARN: Type inference failed for: r0v3, types: [com.android.systemui.media.MediaPlayerData$special$$inlined$thenByDescending$2] */
    /* JADX WARN: Type inference failed for: r0v4, types: [com.android.systemui.media.MediaPlayerData$special$$inlined$thenByDescending$4] */
    /* JADX WARN: Type inference failed for: r0v5, types: [com.android.systemui.media.MediaPlayerData$special$$inlined$thenByDescending$6, java.util.Comparator] */
    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.media.MediaPlayerData$special$$inlined$thenByDescending$1] */
    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.media.MediaPlayerData$special$$inlined$thenByDescending$3] */
    /* JADX WARN: Type inference failed for: r1v3, types: [com.android.systemui.media.MediaPlayerData$special$$inlined$thenByDescending$5] */
    /* JADX WARN: Unknown variable types count: 7 */
    static {
        /*
            com.android.systemui.media.MediaPlayerData r0 = new com.android.systemui.media.MediaPlayerData
            r0.<init>()
            com.android.systemui.media.MediaPlayerData.INSTANCE = r0
            com.android.systemui.media.MediaData r0 = new com.android.systemui.media.MediaData
            r1 = r0
            kotlin.collections.EmptyList r11 = kotlin.collections.EmptyList.INSTANCE
            r10 = r11
            r2 = -1
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            r8 = 0
            r9 = 0
            r12 = 0
            java.lang.String r13 = "INVALID"
            r14 = 0
            r15 = 0
            r16 = 0
            r17 = 1
            r18 = 0
            r19 = 0
            r20 = 0
            r21 = 0
            r22 = 0
            r23 = 0
            r24 = 0
            r25 = 0
            r27 = 16647168(0xfe0400, float:2.3327651E-38)
            r1.<init>(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23, r24, r25, r27)
            com.android.systemui.media.MediaPlayerData.EMPTY = r0
            com.android.systemui.media.MediaPlayerData$special$$inlined$compareByDescending$1 r0 = new com.android.systemui.media.MediaPlayerData$special$$inlined$compareByDescending$1
            r0.<init>()
            com.android.systemui.media.MediaPlayerData$special$$inlined$thenByDescending$1 r1 = new com.android.systemui.media.MediaPlayerData$special$$inlined$thenByDescending$1
            r1.<init>()
            com.android.systemui.media.MediaPlayerData$special$$inlined$thenByDescending$2 r0 = new com.android.systemui.media.MediaPlayerData$special$$inlined$thenByDescending$2
            r0.<init>()
            com.android.systemui.media.MediaPlayerData$special$$inlined$thenByDescending$3 r1 = new com.android.systemui.media.MediaPlayerData$special$$inlined$thenByDescending$3
            r1.<init>()
            com.android.systemui.media.MediaPlayerData$special$$inlined$thenByDescending$4 r0 = new com.android.systemui.media.MediaPlayerData$special$$inlined$thenByDescending$4
            r0.<init>()
            com.android.systemui.media.MediaPlayerData$special$$inlined$thenByDescending$5 r1 = new com.android.systemui.media.MediaPlayerData$special$$inlined$thenByDescending$5
            r1.<init>()
            com.android.systemui.media.MediaPlayerData$special$$inlined$thenByDescending$6 r0 = new com.android.systemui.media.MediaPlayerData$special$$inlined$thenByDescending$6
            r0.<init>()
            com.android.systemui.media.MediaPlayerData.comparator = r0
            java.util.TreeMap r1 = new java.util.TreeMap
            r1.<init>(r0)
            com.android.systemui.media.MediaPlayerData.mediaPlayers = r1
            java.util.LinkedHashMap r0 = new java.util.LinkedHashMap
            r0.<init>()
            com.android.systemui.media.MediaPlayerData.mediaData = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaPlayerData.<clinit>():void");
    }

    public static int firstActiveMediaIndex() {
        int i = 0;
        for (Object obj : mediaPlayers.entrySet()) {
            int i2 = i + 1;
            if (i >= 0) {
                Map.Entry entry = (Map.Entry) obj;
                MediaSortKey mediaSortKey = (MediaSortKey) entry.getKey();
                Objects.requireNonNull(mediaSortKey);
                if (!mediaSortKey.isSsMediaRec) {
                    MediaSortKey mediaSortKey2 = (MediaSortKey) entry.getKey();
                    Objects.requireNonNull(mediaSortKey2);
                    MediaData mediaData2 = mediaSortKey2.data;
                    Objects.requireNonNull(mediaData2);
                    if (mediaData2.active) {
                        return i;
                    }
                }
                i = i2;
            } else {
                SetsKt__SetsKt.throwIndexOverflow();
                throw null;
            }
        }
        return -1;
    }

    public static MediaControlPanel getMediaPlayer(String str) {
        MediaSortKey mediaSortKey = (MediaSortKey) mediaData.get(str);
        if (mediaSortKey == null) {
            return null;
        }
        return mediaPlayers.get(mediaSortKey);
    }

    public static int getMediaPlayerIndex(String str) {
        MediaSortKey mediaSortKey = (MediaSortKey) mediaData.get(str);
        int i = 0;
        for (Object obj : mediaPlayers.entrySet()) {
            int i2 = i + 1;
            if (i < 0) {
                SetsKt__SetsKt.throwIndexOverflow();
                throw null;
            } else if (Intrinsics.areEqual(((Map.Entry) obj).getKey(), mediaSortKey)) {
                return i;
            } else {
                i = i2;
            }
        }
        return -1;
    }

    public static Collection players() {
        return mediaPlayers.values();
    }

    public static MediaControlPanel removeMediaPlayer(String str) {
        MediaSortKey mediaSortKey = (MediaSortKey) mediaData.remove(str);
        if (mediaSortKey == null) {
            return null;
        }
        if (mediaSortKey.isSsMediaRec) {
            smartspaceMediaData = null;
        }
        return mediaPlayers.remove(mediaSortKey);
    }

    public final void clear() {
        mediaData.clear();
        mediaPlayers.clear();
    }

    private MediaPlayerData() {
    }
}
