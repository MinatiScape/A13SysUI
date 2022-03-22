package com.android.systemui.media;

import android.app.smartspace.SmartspaceAction;
import android.content.Context;
import android.util.Log;
import androidx.fragment.R$styleable;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.util.time.SystemClock;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaDataFilter.kt */
/* loaded from: classes.dex */
public final class MediaDataFilter implements MediaDataManager.Listener {
    public final Context context;
    public final Executor executor;
    public final NotificationLockscreenUserManager lockscreenUserManager;
    public MediaDataManager mediaDataManager;
    public String reactivatedKey;
    public final SystemClock systemClock;
    public final AnonymousClass1 userTracker;
    public final LinkedHashSet _listeners = new LinkedHashSet();
    public final LinkedHashMap<String, MediaData> allEntries = new LinkedHashMap<>();
    public final LinkedHashMap<String, MediaData> userEntries = new LinkedHashMap<>();
    public SmartspaceMediaData smartspaceMediaData = MediaDataManagerKt.EMPTY_SMARTSPACE_MEDIA_DATA;

    public final Set<MediaDataManager.Listener> getListeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return CollectionsKt___CollectionsKt.toSet(this._listeners);
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public final void onMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z, int i) {
        if (str2 != null && !Intrinsics.areEqual(str2, str)) {
            this.allEntries.remove(str2);
        }
        this.allEntries.put(str, mediaData);
        if (this.lockscreenUserManager.isCurrentProfile(mediaData.userId)) {
            if (str2 != null && !Intrinsics.areEqual(str2, str)) {
                this.userEntries.remove(str2);
            }
            this.userEntries.put(str, mediaData);
            for (MediaDataManager.Listener listener : getListeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
                MediaDataManager.Listener.DefaultImpls.onMediaDataLoaded$default(listener, str, str2, mediaData, false, 0, 24);
            }
        }
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public final void onMediaDataRemoved(String str) {
        this.allEntries.remove(str);
        if (this.userEntries.remove(str) != null) {
            for (MediaDataManager.Listener listener : getListeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
                listener.onMediaDataRemoved(str);
            }
        }
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public final void onSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData, boolean z, boolean z2) {
        boolean z3;
        if (!smartspaceMediaData.isActive) {
            Log.d("MediaDataFilter", "Inactive recommendation data. Skip triggering.");
            return;
        }
        this.smartspaceMediaData = smartspaceMediaData;
        LinkedHashMap<String, MediaData> linkedHashMap = this.userEntries;
        TreeMap treeMap = new TreeMap(new Comparator() { // from class: com.android.systemui.media.MediaDataFilter$onSmartspaceMediaDataLoaded$$inlined$compareBy$1
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                int i;
                int i2;
                MediaData mediaData = MediaDataFilter.this.userEntries.get((String) t);
                if (mediaData == null) {
                    i = -1;
                } else {
                    i = Long.valueOf(mediaData.lastActive);
                }
                MediaData mediaData2 = MediaDataFilter.this.userEntries.get((String) t2);
                if (mediaData2 == null) {
                    i2 = -1;
                } else {
                    i2 = Long.valueOf(mediaData2.lastActive);
                }
                return R$styleable.compareValues(i, i2);
            }
        });
        treeMap.putAll(linkedHashMap);
        long j = Long.MAX_VALUE;
        if (!treeMap.isEmpty()) {
            long elapsedRealtime = this.systemClock.elapsedRealtime();
            MediaData mediaData = (MediaData) treeMap.get((String) treeMap.lastKey());
            if (mediaData != null) {
                j = elapsedRealtime - mediaData.lastActive;
            }
        }
        long j2 = MediaDataFilterKt.SMARTSPACE_MAX_AGE;
        SmartspaceAction smartspaceAction = smartspaceMediaData.cardAction;
        if (smartspaceAction != null) {
            long j3 = smartspaceAction.getExtras().getLong("resumable_media_max_age_seconds", 0L);
            if (j3 > 0) {
                j2 = TimeUnit.SECONDS.toMillis(j3);
            }
        }
        LinkedHashMap<String, MediaData> linkedHashMap2 = this.userEntries;
        LinkedHashMap linkedHashMap3 = new LinkedHashMap();
        for (Map.Entry<String, MediaData> entry : linkedHashMap2.entrySet()) {
            entry.getKey();
            MediaData value = entry.getValue();
            Objects.requireNonNull(value);
            if (value.active) {
                linkedHashMap3.put(entry.getKey(), entry.getValue());
            }
        }
        boolean isEmpty = linkedHashMap3.isEmpty();
        boolean z4 = false;
        if (!isEmpty || !(!this.userEntries.isEmpty())) {
            z3 = false;
        } else {
            z3 = true;
        }
        if (j >= j2) {
            z4 = true;
        } else if (z3) {
            String str2 = (String) treeMap.lastKey();
            Log.d("MediaDataFilter", "reactivating " + ((Object) str2) + " instead of smartspace");
            this.reactivatedKey = str2;
            Object obj = treeMap.get(str2);
            Intrinsics.checkNotNull(obj);
            MediaData copy$default = MediaData.copy$default((MediaData) obj, null, null, null, null, true, null, false, false, null, false, 16744447);
            for (MediaDataManager.Listener listener : getListeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
                MediaDataManager.Listener.DefaultImpls.onMediaDataLoaded$default(listener, str2, str2, copy$default, false, (int) (this.systemClock.currentTimeMillis() - smartspaceMediaData.headphoneConnectionTimeMillis), 8);
            }
        }
        if (!smartspaceMediaData.isValid) {
            Log.d("MediaDataFilter", "Invalid recommendation data. Skip showing the rec card");
            return;
        }
        for (MediaDataManager.Listener listener2 : getListeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
            listener2.onSmartspaceMediaDataLoaded(str, smartspaceMediaData, z4, z3);
        }
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public final void onSmartspaceMediaDataRemoved(String str, boolean z) {
        String str2 = this.reactivatedKey;
        if (str2 != null) {
            this.reactivatedKey = null;
            Log.d("MediaDataFilter", Intrinsics.stringPlus("expiring reactivated key ", str2));
            MediaData mediaData = this.userEntries.get(str2);
            if (mediaData != null) {
                for (MediaDataManager.Listener listener : getListeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
                    MediaDataManager.Listener.DefaultImpls.onMediaDataLoaded$default(listener, str2, str2, mediaData, z, 0, 16);
                }
            }
        }
        SmartspaceMediaData smartspaceMediaData = this.smartspaceMediaData;
        Objects.requireNonNull(smartspaceMediaData);
        if (smartspaceMediaData.isActive) {
            SmartspaceMediaData smartspaceMediaData2 = MediaDataManagerKt.EMPTY_SMARTSPACE_MEDIA_DATA;
            SmartspaceMediaData smartspaceMediaData3 = this.smartspaceMediaData;
            Objects.requireNonNull(smartspaceMediaData3);
            String str3 = smartspaceMediaData3.targetId;
            SmartspaceMediaData smartspaceMediaData4 = this.smartspaceMediaData;
            Objects.requireNonNull(smartspaceMediaData4);
            this.smartspaceMediaData = SmartspaceMediaData.copy$default(smartspaceMediaData2, str3, false, smartspaceMediaData4.isValid, null, 0, 0L, 506);
        }
        for (MediaDataManager.Listener listener2 : getListeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
            listener2.onSmartspaceMediaDataRemoved(str, z);
        }
    }

    /* JADX WARN: Type inference failed for: r1v5, types: [com.android.systemui.media.MediaDataFilter$1, com.android.systemui.settings.CurrentUserTracker] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public MediaDataFilter(android.content.Context r1, com.android.systemui.broadcast.BroadcastDispatcher r2, com.android.systemui.statusbar.NotificationLockscreenUserManager r3, java.util.concurrent.Executor r4, com.android.systemui.util.time.SystemClock r5) {
        /*
            r0 = this;
            r0.<init>()
            r0.context = r1
            r0.lockscreenUserManager = r3
            r0.executor = r4
            r0.systemClock = r5
            java.util.LinkedHashSet r1 = new java.util.LinkedHashSet
            r1.<init>()
            r0._listeners = r1
            java.util.LinkedHashMap r1 = new java.util.LinkedHashMap
            r1.<init>()
            r0.allEntries = r1
            java.util.LinkedHashMap r1 = new java.util.LinkedHashMap
            r1.<init>()
            r0.userEntries = r1
            com.android.systemui.media.SmartspaceMediaData r1 = com.android.systemui.media.MediaDataManagerKt.EMPTY_SMARTSPACE_MEDIA_DATA
            r0.smartspaceMediaData = r1
            com.android.systemui.media.MediaDataFilter$1 r1 = new com.android.systemui.media.MediaDataFilter$1
            r1.<init>(r2)
            r0.userTracker = r1
            r1.startTracking()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaDataFilter.<init>(android.content.Context, com.android.systemui.broadcast.BroadcastDispatcher, com.android.systemui.statusbar.NotificationLockscreenUserManager, java.util.concurrent.Executor, com.android.systemui.util.time.SystemClock):void");
    }

    @VisibleForTesting
    public final void handleUserSwitched$frameworks__base__packages__SystemUI__android_common__SystemUI_core(int i) {
        Set<MediaDataManager.Listener> listeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core = getListeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
        ArrayList arrayList = new ArrayList(this.userEntries.keySet());
        this.userEntries.clear();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            Log.d("MediaDataFilter", "Removing " + str + " after user change");
            for (MediaDataManager.Listener listener : listeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core) {
                listener.onMediaDataRemoved(str);
            }
        }
        for (Map.Entry<String, MediaData> entry : this.allEntries.entrySet()) {
            String key = entry.getKey();
            MediaData value = entry.getValue();
            NotificationLockscreenUserManager notificationLockscreenUserManager = this.lockscreenUserManager;
            Objects.requireNonNull(value);
            if (notificationLockscreenUserManager.isCurrentProfile(value.userId)) {
                Log.d("MediaDataFilter", "Re-adding " + key + " after user change");
                this.userEntries.put(key, value);
                for (MediaDataManager.Listener listener2 : listeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core) {
                    MediaDataManager.Listener.DefaultImpls.onMediaDataLoaded$default(listener2, key, null, value, false, 0, 24);
                }
            }
        }
    }
}
