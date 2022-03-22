package com.android.systemui.media;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.media.MediaDescription;
import android.media.browse.MediaBrowser;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.Dumpable;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.ResumeMediaBrowser;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.time.SystemClock;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import kotlin.Pair;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.EmptyList;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt__StringsKt;
/* compiled from: MediaResumeListener.kt */
/* loaded from: classes.dex */
public final class MediaResumeListener implements MediaDataManager.Listener, Dumpable {
    public final Executor backgroundExecutor;
    public final Context context;
    public int currentUserId;
    public ResumeMediaBrowser mediaBrowser;
    public final ResumeMediaBrowserFactory mediaBrowserFactory;
    public MediaDataManager mediaDataManager;
    public final SystemClock systemClock;
    public final TunerService tunerService;
    public boolean useMediaResumption;
    public final MediaResumeListener$userChangeReceiver$1 userChangeReceiver;
    public final ConcurrentLinkedQueue<Pair<ComponentName, Long>> resumeComponents = new ConcurrentLinkedQueue<>();
    public final MediaResumeListener$mediaBrowserCallback$1 mediaBrowserCallback = new ResumeMediaBrowser.Callback() { // from class: com.android.systemui.media.MediaResumeListener$mediaBrowserCallback$1
        @Override // com.android.systemui.media.ResumeMediaBrowser.Callback
        public final void addTrack(final MediaDescription mediaDescription, ComponentName componentName, ResumeMediaBrowser resumeMediaBrowser) {
            final MediaSession.Token token;
            final MediaDataManager mediaDataManager;
            MediaBrowser mediaBrowser = resumeMediaBrowser.mMediaBrowser;
            if (mediaBrowser == null || !mediaBrowser.isConnected()) {
                token = null;
            } else {
                token = resumeMediaBrowser.mMediaBrowser.getSessionToken();
            }
            final PendingIntent activity = PendingIntent.getActivity(resumeMediaBrowser.mContext, 0, resumeMediaBrowser.mContext.getPackageManager().getLaunchIntentForPackage(resumeMediaBrowser.mComponentName.getPackageName()), 33554432);
            PackageManager packageManager = MediaResumeListener.this.context.getPackageManager();
            CharSequence packageName = componentName.getPackageName();
            MediaResumeListener mediaResumeListener = MediaResumeListener.this;
            Objects.requireNonNull(mediaResumeListener);
            final MediaResumeListener$getResumeAction$1 mediaResumeListener$getResumeAction$1 = new MediaResumeListener$getResumeAction$1(mediaResumeListener, componentName);
            try {
                packageName = packageManager.getApplicationLabel(packageManager.getApplicationInfo(componentName.getPackageName(), 0));
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("MediaResumeListener", "Error getting package information", e);
            }
            Log.d("MediaResumeListener", Intrinsics.stringPlus("Adding resume controls ", mediaDescription));
            MediaResumeListener mediaResumeListener2 = MediaResumeListener.this;
            MediaDataManager mediaDataManager2 = mediaResumeListener2.mediaDataManager;
            if (mediaDataManager2 == null) {
                mediaDataManager = null;
            } else {
                mediaDataManager = mediaDataManager2;
            }
            final int i = mediaResumeListener2.currentUserId;
            final String obj = packageName.toString();
            final String packageName2 = componentName.getPackageName();
            Objects.requireNonNull(mediaDataManager);
            if (!mediaDataManager.mediaEntries.containsKey(packageName2)) {
                mediaDataManager.mediaEntries.put(packageName2, MediaData.copy$default(MediaDataManagerKt.LOADING, null, null, packageName2, null, false, mediaResumeListener$getResumeAction$1, false, true, null, false, 15661055));
            }
            mediaDataManager.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaDataManager$addResumptionControls$1
                @Override // java.lang.Runnable
                public final void run() {
                    final Icon icon;
                    final MediaDataManager mediaDataManager3 = MediaDataManager.this;
                    final int i2 = i;
                    final MediaDescription mediaDescription2 = mediaDescription;
                    final Runnable runnable = mediaResumeListener$getResumeAction$1;
                    final MediaSession.Token token2 = token;
                    final String str = obj;
                    final PendingIntent pendingIntent = activity;
                    final String str2 = packageName2;
                    Objects.requireNonNull(mediaDataManager3);
                    if (TextUtils.isEmpty(mediaDescription2.getTitle())) {
                        Log.e("MediaDataManager", "Description incomplete");
                        mediaDataManager3.mediaEntries.remove(str2);
                        return;
                    }
                    Log.d("MediaDataManager", "adding track for " + i2 + " from browser: " + mediaDescription2);
                    Bitmap iconBitmap = mediaDescription2.getIconBitmap();
                    if (iconBitmap == null && mediaDescription2.getIconUri() != null) {
                        Uri iconUri = mediaDescription2.getIconUri();
                        Intrinsics.checkNotNull(iconUri);
                        iconBitmap = mediaDataManager3.loadBitmapFromUri(iconUri);
                    }
                    if (iconBitmap != null) {
                        icon = Icon.createWithBitmap(iconBitmap);
                    } else {
                        icon = null;
                    }
                    final MediaAction resumeMediaAction = mediaDataManager3.getResumeMediaAction(runnable);
                    final long elapsedRealtime = mediaDataManager3.systemClock.elapsedRealtime();
                    mediaDataManager3.foregroundExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaDataManager$loadMediaDataInBgForResumption$1
                        @Override // java.lang.Runnable
                        public final void run() {
                            MediaDataManager mediaDataManager4 = MediaDataManager.this;
                            String str3 = str2;
                            int i3 = i2;
                            int i4 = mediaDataManager4.bgColor;
                            String str4 = str;
                            CharSequence subtitle = mediaDescription2.getSubtitle();
                            CharSequence title = mediaDescription2.getTitle();
                            Icon icon2 = icon;
                            List singletonList = Collections.singletonList(resumeMediaAction);
                            List singletonList2 = Collections.singletonList(0);
                            String str5 = str2;
                            mediaDataManager4.onMediaDataLoaded(str3, null, new MediaData(i3, true, i4, str4, null, subtitle, title, icon2, singletonList, singletonList2, null, str5, token2, pendingIntent, null, false, runnable, 0, true, str5, true, null, false, elapsedRealtime, 6422528));
                        }
                    });
                }
            });
        }
    };

    @VisibleForTesting
    public static /* synthetic */ void getUserChangeReceiver$annotations() {
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public final void onMediaDataRemoved(String str) {
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public final void onSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData, boolean z, boolean z2) {
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public final void onSmartspaceMediaDataRemoved(String str, boolean z) {
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println(Intrinsics.stringPlus("resumeComponents: ", this.resumeComponents));
    }

    public final void loadSavedComponents() {
        long j;
        long j2;
        boolean z;
        this.resumeComponents.clear();
        boolean z2 = false;
        Iterable<String> iterable = null;
        String string = this.context.getSharedPreferences("media_control_prefs", 0).getString(Intrinsics.stringPlus("browser_components_", Integer.valueOf(this.currentUserId)), null);
        if (string != null) {
            List split = new Regex(":").split(string);
            if (!split.isEmpty()) {
                ListIterator listIterator = split.listIterator(split.size());
                while (listIterator.hasPrevious()) {
                    if (((String) listIterator.previous()).length() == 0) {
                        z = true;
                        continue;
                    } else {
                        z = false;
                        continue;
                    }
                    if (!z) {
                        iterable = CollectionsKt___CollectionsKt.take(split, listIterator.nextIndex() + 1);
                        break;
                    }
                }
            }
            iterable = EmptyList.INSTANCE;
        }
        if (iterable != null) {
            boolean z3 = false;
            for (String str : iterable) {
                List split$default = StringsKt__StringsKt.split$default(str, new String[]{"/"});
                ComponentName componentName = new ComponentName((String) split$default.get(0), (String) split$default.get(1));
                if (split$default.size() == 3) {
                    try {
                        j = Long.parseLong((String) split$default.get(2));
                    } catch (NumberFormatException unused) {
                        j2 = this.systemClock.currentTimeMillis();
                    }
                    this.resumeComponents.add(new Pair<>(componentName, Long.valueOf(j)));
                } else {
                    j2 = this.systemClock.currentTimeMillis();
                }
                j = j2;
                z3 = true;
                this.resumeComponents.add(new Pair<>(componentName, Long.valueOf(j)));
            }
            z2 = z3;
        }
        Log.d("MediaResumeListener", Intrinsics.stringPlus("loaded resume components ", Arrays.toString(this.resumeComponents.toArray())));
        if (z2) {
            writeSharedPrefs();
        }
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public final void onMediaDataLoaded(final String str, String str2, MediaData mediaData, boolean z, int i) {
        boolean z2;
        final ArrayList arrayList;
        if (this.useMediaResumption) {
            if (!str.equals(str2)) {
                ResumeMediaBrowser resumeMediaBrowser = this.mediaBrowser;
                if (resumeMediaBrowser != null) {
                    resumeMediaBrowser.disconnect();
                }
                this.mediaBrowser = null;
            }
            if (mediaData.resumeAction == null && !mediaData.hasCheckedForResume) {
                if (mediaData.playbackLocation == 0) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (z2) {
                    Log.d("MediaResumeListener", Intrinsics.stringPlus("Checking for service component for ", mediaData.packageName));
                    List<ResolveInfo> queryIntentServices = this.context.getPackageManager().queryIntentServices(new Intent("android.media.browse.MediaBrowserService"), 0);
                    if (queryIntentServices == null) {
                        arrayList = null;
                    } else {
                        arrayList = new ArrayList();
                        for (Object obj : queryIntentServices) {
                            if (Intrinsics.areEqual(((ResolveInfo) obj).serviceInfo.packageName, mediaData.packageName)) {
                                arrayList.add(obj);
                            }
                        }
                    }
                    if (arrayList == null || arrayList.size() <= 0) {
                        MediaDataManager mediaDataManager = this.mediaDataManager;
                        if (mediaDataManager == null) {
                            mediaDataManager = null;
                        }
                        mediaDataManager.setResumeAction(str, null);
                        return;
                    }
                    this.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaResumeListener$onMediaDataLoaded$1
                        @Override // java.lang.Runnable
                        public final void run() {
                            final MediaResumeListener mediaResumeListener = MediaResumeListener.this;
                            final String str3 = str;
                            List<ResolveInfo> list = arrayList;
                            Intrinsics.checkNotNull(list);
                            final ComponentName componentName = list.get(0).getComponentInfo().getComponentName();
                            Objects.requireNonNull(mediaResumeListener);
                            Log.d("MediaResumeListener", Intrinsics.stringPlus("Testing if we can connect to ", componentName));
                            MediaDataManager mediaDataManager2 = mediaResumeListener.mediaDataManager;
                            if (mediaDataManager2 == null) {
                                mediaDataManager2 = null;
                            }
                            mediaDataManager2.setResumeAction(str3, null);
                            ResumeMediaBrowser resumeMediaBrowser2 = mediaResumeListener.mediaBrowser;
                            if (resumeMediaBrowser2 != null) {
                                resumeMediaBrowser2.disconnect();
                            }
                            ResumeMediaBrowserFactory resumeMediaBrowserFactory = mediaResumeListener.mediaBrowserFactory;
                            ResumeMediaBrowser.Callback mediaResumeListener$tryUpdateResumptionList$1 = new ResumeMediaBrowser.Callback() { // from class: com.android.systemui.media.MediaResumeListener$tryUpdateResumptionList$1
                                @Override // com.android.systemui.media.ResumeMediaBrowser.Callback
                                public final void addTrack(MediaDescription mediaDescription, ComponentName componentName2, ResumeMediaBrowser resumeMediaBrowser3) {
                                    Pair<ComponentName, Long> pair;
                                    Log.d("MediaResumeListener", Intrinsics.stringPlus("Can get resumable media from ", componentName));
                                    MediaResumeListener mediaResumeListener2 = mediaResumeListener;
                                    MediaDataManager mediaDataManager3 = mediaResumeListener2.mediaDataManager;
                                    if (mediaDataManager3 == null) {
                                        mediaDataManager3 = null;
                                    }
                                    mediaDataManager3.setResumeAction(str3, new MediaResumeListener$getResumeAction$1(mediaResumeListener2, componentName));
                                    MediaResumeListener mediaResumeListener3 = mediaResumeListener;
                                    ComponentName componentName3 = componentName;
                                    Objects.requireNonNull(mediaResumeListener3);
                                    ConcurrentLinkedQueue<Pair<ComponentName, Long>> concurrentLinkedQueue = mediaResumeListener3.resumeComponents;
                                    Iterator<Pair<ComponentName, Long>> it = concurrentLinkedQueue.iterator();
                                    while (true) {
                                        if (!it.hasNext()) {
                                            pair = null;
                                            break;
                                        }
                                        pair = it.next();
                                        if (pair.getFirst().equals(componentName3)) {
                                            break;
                                        }
                                    }
                                    concurrentLinkedQueue.remove(pair);
                                    mediaResumeListener3.resumeComponents.add(new Pair<>(componentName3, Long.valueOf(mediaResumeListener3.systemClock.currentTimeMillis())));
                                    if (mediaResumeListener3.resumeComponents.size() > 5) {
                                        mediaResumeListener3.resumeComponents.remove();
                                    }
                                    mediaResumeListener3.writeSharedPrefs();
                                    mediaResumeListener.mediaBrowser = null;
                                }

                                @Override // com.android.systemui.media.ResumeMediaBrowser.Callback
                                public final void onConnected() {
                                    Log.d("MediaResumeListener", Intrinsics.stringPlus("Connected to ", componentName));
                                }

                                @Override // com.android.systemui.media.ResumeMediaBrowser.Callback
                                public final void onError() {
                                    Log.e("MediaResumeListener", Intrinsics.stringPlus("Cannot resume with ", componentName));
                                    mediaResumeListener.mediaBrowser = null;
                                }
                            };
                            Objects.requireNonNull(resumeMediaBrowserFactory);
                            ResumeMediaBrowser resumeMediaBrowser3 = new ResumeMediaBrowser(resumeMediaBrowserFactory.mContext, mediaResumeListener$tryUpdateResumptionList$1, componentName, resumeMediaBrowserFactory.mBrowserFactory);
                            mediaResumeListener.mediaBrowser = resumeMediaBrowser3;
                            resumeMediaBrowser3.disconnect();
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("android.service.media.extra.RECENT", true);
                            MediaBrowserFactory mediaBrowserFactory = resumeMediaBrowser3.mBrowserFactory;
                            ComponentName componentName2 = resumeMediaBrowser3.mComponentName;
                            ResumeMediaBrowser.AnonymousClass2 r3 = resumeMediaBrowser3.mConnectionCallback;
                            Objects.requireNonNull(mediaBrowserFactory);
                            MediaBrowser mediaBrowser = new MediaBrowser(mediaBrowserFactory.mContext, componentName2, r3, bundle);
                            resumeMediaBrowser3.mMediaBrowser = mediaBrowser;
                            mediaBrowser.connect();
                        }
                    });
                }
            }
        }
    }

    public final void writeSharedPrefs() {
        StringBuilder sb = new StringBuilder();
        Iterator<T> it = this.resumeComponents.iterator();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            sb.append(((ComponentName) pair.getFirst()).flattenToString());
            sb.append("/");
            sb.append(((Number) pair.getSecond()).longValue());
            sb.append(":");
        }
        this.context.getSharedPreferences("media_control_prefs", 0).edit().putString(Intrinsics.stringPlus("browser_components_", Integer.valueOf(this.currentUserId)), sb.toString()).apply();
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.media.MediaResumeListener$mediaBrowserCallback$1] */
    /* JADX WARN: Type inference failed for: r3v3, types: [com.android.systemui.media.MediaResumeListener$userChangeReceiver$1, android.content.BroadcastReceiver] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public MediaResumeListener(android.content.Context r1, com.android.systemui.broadcast.BroadcastDispatcher r2, java.util.concurrent.Executor r3, com.android.systemui.tuner.TunerService r4, com.android.systemui.media.ResumeMediaBrowserFactory r5, com.android.systemui.dump.DumpManager r6, com.android.systemui.util.time.SystemClock r7) {
        /*
            r0 = this;
            r0.<init>()
            r0.context = r1
            r0.backgroundExecutor = r3
            r0.tunerService = r4
            r0.mediaBrowserFactory = r5
            r0.systemClock = r7
            boolean r3 = com.android.systemui.util.Utils.useMediaResumption(r1)
            r0.useMediaResumption = r3
            java.util.concurrent.ConcurrentLinkedQueue r3 = new java.util.concurrent.ConcurrentLinkedQueue
            r3.<init>()
            r0.resumeComponents = r3
            int r1 = r1.getUserId()
            r0.currentUserId = r1
            com.android.systemui.media.MediaResumeListener$userChangeReceiver$1 r3 = new com.android.systemui.media.MediaResumeListener$userChangeReceiver$1
            r3.<init>()
            r0.userChangeReceiver = r3
            com.android.systemui.media.MediaResumeListener$mediaBrowserCallback$1 r1 = new com.android.systemui.media.MediaResumeListener$mediaBrowserCallback$1
            r1.<init>()
            r0.mediaBrowserCallback = r1
            boolean r1 = r0.useMediaResumption
            if (r1 == 0) goto L_0x0051
            java.lang.String r1 = "MediaResumeListener"
            r6.registerDumpable(r1, r0)
            android.content.IntentFilter r4 = new android.content.IntentFilter
            r4.<init>()
            java.lang.String r1 = "android.intent.action.USER_UNLOCKED"
            r4.addAction(r1)
            java.lang.String r1 = "android.intent.action.USER_SWITCHED"
            r4.addAction(r1)
            r5 = 0
            android.os.UserHandle r6 = android.os.UserHandle.ALL
            r7 = 16
            com.android.systemui.broadcast.BroadcastDispatcher.registerReceiver$default(r2, r3, r4, r5, r6, r7)
            r0.loadSavedComponents()
        L_0x0051:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaResumeListener.<init>(android.content.Context, com.android.systemui.broadcast.BroadcastDispatcher, java.util.concurrent.Executor, com.android.systemui.tuner.TunerService, com.android.systemui.media.ResumeMediaBrowserFactory, com.android.systemui.dump.DumpManager, com.android.systemui.util.time.SystemClock):void");
    }
}
