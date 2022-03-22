package com.android.systemui.media;

import android.content.ComponentName;
import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.util.Log;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.statusbar.phone.NotificationListenerWithPlugins;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import kotlin.collections.ArrayAsCollection;
import kotlin.collections.CollectionsKt__IteratorsJVMKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaSessionBasedFilter.kt */
/* loaded from: classes.dex */
public final class MediaSessionBasedFilter implements MediaDataManager.Listener {
    public final Executor backgroundExecutor;
    public final Executor foregroundExecutor;
    public final MediaSessionManager sessionManager;
    public final LinkedHashSet listeners = new LinkedHashSet();
    public final LinkedHashMap<String, List<MediaController>> packageControllers = new LinkedHashMap<>();
    public final LinkedHashMap keyedTokens = new LinkedHashMap();
    public final LinkedHashSet tokensWithNotifications = new LinkedHashSet();
    public final MediaSessionBasedFilter$sessionListener$1 sessionListener = new MediaSessionManager.OnActiveSessionsChangedListener() { // from class: com.android.systemui.media.MediaSessionBasedFilter$sessionListener$1
        @Override // android.media.session.MediaSessionManager.OnActiveSessionsChangedListener
        public final void onActiveSessionsChanged(List<MediaController> list) {
            MediaSessionBasedFilter.access$handleControllersChanged(MediaSessionBasedFilter.this, list);
        }
    };

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public final void onMediaDataLoaded(final String str, final String str2, final MediaData mediaData, final boolean z, int i) {
        this.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaSessionBasedFilter$onMediaDataLoaded$1
            @Override // java.lang.Runnable
            public final void run() {
                Object[] objArr;
                ArrayList arrayList;
                Object[] objArr2;
                Boolean bool;
                Set set;
                MediaData mediaData2 = MediaData.this;
                Objects.requireNonNull(mediaData2);
                MediaSession.Token token = mediaData2.token;
                if (token != null) {
                    this.tokensWithNotifications.add(token);
                }
                String str3 = str2;
                boolean z2 = true;
                if (str3 == null || Intrinsics.areEqual(str, str3)) {
                    objArr = null;
                } else {
                    objArr = 1;
                }
                if (!(objArr == null || (set = (Set) this.keyedTokens.remove(str2)) == null)) {
                    Set set2 = (Set) this.keyedTokens.put(str, set);
                }
                MediaData mediaData3 = MediaData.this;
                Objects.requireNonNull(mediaData3);
                MediaController mediaController = null;
                Object obj = null;
                if (mediaData3.token != null) {
                    Set set3 = (Set) this.keyedTokens.get(str);
                    if (set3 == null) {
                        bool = null;
                    } else {
                        MediaData mediaData4 = MediaData.this;
                        Objects.requireNonNull(mediaData4);
                        bool = Boolean.valueOf(set3.add(mediaData4.token));
                    }
                    if (bool == null) {
                        MediaSessionBasedFilter mediaSessionBasedFilter = this;
                        MediaData mediaData5 = MediaData.this;
                        String str4 = str;
                        Objects.requireNonNull(mediaData5);
                        MediaSession.Token[] tokenArr = {mediaData5.token};
                        LinkedHashSet linkedHashSet = new LinkedHashSet(MapsKt__MapsJVMKt.mapCapacity(1));
                        int i2 = 0;
                        while (i2 < 1) {
                            MediaSession.Token token2 = tokenArr[i2];
                            i2++;
                            linkedHashSet.add(token2);
                        }
                        Set set4 = (Set) mediaSessionBasedFilter.keyedTokens.put(str4, linkedHashSet);
                    }
                }
                LinkedHashMap<String, List<MediaController>> linkedHashMap = this.packageControllers;
                MediaData mediaData6 = MediaData.this;
                Objects.requireNonNull(mediaData6);
                List<MediaController> list = linkedHashMap.get(mediaData6.packageName);
                if (list == null) {
                    arrayList = null;
                } else {
                    arrayList = new ArrayList();
                    for (Object obj2 : list) {
                        MediaController.PlaybackInfo playbackInfo = ((MediaController) obj2).getPlaybackInfo();
                        if (playbackInfo != null && playbackInfo.getPlaybackType() == 2) {
                            objArr2 = 1;
                        } else {
                            objArr2 = null;
                        }
                        if (objArr2 != null) {
                            arrayList.add(obj2);
                        }
                    }
                }
                if (arrayList == null || arrayList.size() != 1) {
                    z2 = false;
                }
                if (z2) {
                    if (!arrayList.isEmpty()) {
                        obj = arrayList.get(0);
                    }
                    mediaController = (MediaController) obj;
                }
                if (objArr == null && mediaController != null) {
                    MediaSession.Token sessionToken = mediaController.getSessionToken();
                    MediaData mediaData7 = MediaData.this;
                    Objects.requireNonNull(mediaData7);
                    if (!Intrinsics.areEqual(sessionToken, mediaData7.token) && this.tokensWithNotifications.contains(mediaController.getSessionToken())) {
                        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("filtering key=");
                        m.append(str);
                        m.append(" local=");
                        MediaData mediaData8 = MediaData.this;
                        Objects.requireNonNull(mediaData8);
                        m.append(mediaData8.token);
                        m.append(" remote=");
                        m.append(mediaController.getSessionToken());
                        Log.d("MediaSessionBasedFilter", m.toString());
                        Object obj3 = this.keyedTokens.get(str);
                        Intrinsics.checkNotNull(obj3);
                        if (!((Set) obj3).contains(mediaController.getSessionToken())) {
                            MediaSessionBasedFilter mediaSessionBasedFilter2 = this;
                            String str5 = str;
                            Objects.requireNonNull(mediaSessionBasedFilter2);
                            mediaSessionBasedFilter2.foregroundExecutor.execute(new MediaSessionBasedFilter$dispatchMediaDataRemoved$1(mediaSessionBasedFilter2, str5));
                            return;
                        }
                        return;
                    }
                }
                final MediaSessionBasedFilter mediaSessionBasedFilter3 = this;
                final String str6 = str;
                final String str7 = str2;
                final MediaData mediaData9 = MediaData.this;
                final boolean z3 = z;
                Objects.requireNonNull(mediaSessionBasedFilter3);
                mediaSessionBasedFilter3.foregroundExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaSessionBasedFilter$dispatchMediaDataLoaded$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        Set<MediaDataManager.Listener> set5 = CollectionsKt___CollectionsKt.toSet(MediaSessionBasedFilter.this.listeners);
                        String str8 = str6;
                        String str9 = str7;
                        MediaData mediaData10 = mediaData9;
                        boolean z4 = z3;
                        for (MediaDataManager.Listener listener : set5) {
                            MediaDataManager.Listener.DefaultImpls.onMediaDataLoaded$default(listener, str8, str9, mediaData10, z4, 0, 16);
                        }
                    }
                });
            }
        });
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public final void onMediaDataRemoved(final String str) {
        this.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaSessionBasedFilter$onMediaDataRemoved$1
            @Override // java.lang.Runnable
            public final void run() {
                MediaSessionBasedFilter.this.keyedTokens.remove(str);
                MediaSessionBasedFilter mediaSessionBasedFilter = MediaSessionBasedFilter.this;
                String str2 = str;
                Objects.requireNonNull(mediaSessionBasedFilter);
                mediaSessionBasedFilter.foregroundExecutor.execute(new MediaSessionBasedFilter$dispatchMediaDataRemoved$1(mediaSessionBasedFilter, str2));
            }
        });
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public final void onSmartspaceMediaDataLoaded(final String str, final SmartspaceMediaData smartspaceMediaData, boolean z, boolean z2) {
        this.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaSessionBasedFilter$onSmartspaceMediaDataLoaded$1
            @Override // java.lang.Runnable
            public final void run() {
                final MediaSessionBasedFilter mediaSessionBasedFilter = MediaSessionBasedFilter.this;
                final String str2 = str;
                final SmartspaceMediaData smartspaceMediaData2 = smartspaceMediaData;
                Objects.requireNonNull(mediaSessionBasedFilter);
                mediaSessionBasedFilter.foregroundExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaSessionBasedFilter$dispatchSmartspaceMediaDataLoaded$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        Set<MediaDataManager.Listener> set = CollectionsKt___CollectionsKt.toSet(MediaSessionBasedFilter.this.listeners);
                        String str3 = str2;
                        SmartspaceMediaData smartspaceMediaData3 = smartspaceMediaData2;
                        for (MediaDataManager.Listener listener : set) {
                            listener.onSmartspaceMediaDataLoaded(str3, smartspaceMediaData3, false, false);
                        }
                    }
                });
            }
        });
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public final void onSmartspaceMediaDataRemoved(final String str, final boolean z) {
        this.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaSessionBasedFilter$onSmartspaceMediaDataRemoved$1
            @Override // java.lang.Runnable
            public final void run() {
                final MediaSessionBasedFilter mediaSessionBasedFilter = MediaSessionBasedFilter.this;
                final String str2 = str;
                final boolean z2 = z;
                Objects.requireNonNull(mediaSessionBasedFilter);
                mediaSessionBasedFilter.foregroundExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaSessionBasedFilter$dispatchSmartspaceMediaDataRemoved$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        Set<MediaDataManager.Listener> set = CollectionsKt___CollectionsKt.toSet(MediaSessionBasedFilter.this.listeners);
                        String str3 = str2;
                        boolean z3 = z2;
                        for (MediaDataManager.Listener listener : set) {
                            listener.onSmartspaceMediaDataRemoved(str3, z3);
                        }
                    }
                });
            }
        });
    }

    /* JADX WARN: Type inference failed for: r2v5, types: [com.android.systemui.media.MediaSessionBasedFilter$sessionListener$1] */
    public MediaSessionBasedFilter(final Context context, MediaSessionManager mediaSessionManager, Executor executor, Executor executor2) {
        this.sessionManager = mediaSessionManager;
        this.foregroundExecutor = executor;
        this.backgroundExecutor = executor2;
        executor2.execute(new Runnable() { // from class: com.android.systemui.media.MediaSessionBasedFilter.1
            @Override // java.lang.Runnable
            public final void run() {
                ComponentName componentName = new ComponentName(context, NotificationListenerWithPlugins.class);
                MediaSessionBasedFilter mediaSessionBasedFilter = this;
                mediaSessionBasedFilter.sessionManager.addOnActiveSessionsChangedListener(mediaSessionBasedFilter.sessionListener, componentName);
                MediaSessionBasedFilter mediaSessionBasedFilter2 = this;
                MediaSessionBasedFilter.access$handleControllersChanged(mediaSessionBasedFilter2, mediaSessionBasedFilter2.sessionManager.getActiveSessions(componentName));
            }
        });
    }

    public static final void access$handleControllersChanged(MediaSessionBasedFilter mediaSessionBasedFilter, List list) {
        Boolean bool;
        Objects.requireNonNull(mediaSessionBasedFilter);
        mediaSessionBasedFilter.packageControllers.clear();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            MediaController mediaController = (MediaController) it.next();
            List<MediaController> list2 = mediaSessionBasedFilter.packageControllers.get(mediaController.getPackageName());
            if (list2 == null) {
                bool = null;
            } else {
                bool = Boolean.valueOf(list2.add(mediaController));
            }
            if (bool == null) {
                mediaSessionBasedFilter.packageControllers.put(mediaController.getPackageName(), new ArrayList(new ArrayAsCollection(new MediaController[]{mediaController}, true)));
            }
        }
        LinkedHashSet linkedHashSet = mediaSessionBasedFilter.tokensWithNotifications;
        ArrayList arrayList = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(list, 10));
        Iterator it2 = list.iterator();
        while (it2.hasNext()) {
            arrayList.add(((MediaController) it2.next()).getSessionToken());
        }
        linkedHashSet.retainAll(arrayList);
    }
}
