package com.android.systemui.media;

import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceConfig;
import android.app.smartspace.SmartspaceManager;
import android.app.smartspace.SmartspaceSession;
import android.app.smartspace.SmartspaceTarget;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Icon;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.UserHandle;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import com.android.keyguard.KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.Assert;
import com.android.systemui.util.Utils;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: MediaDataManager.kt */
/* loaded from: classes.dex */
public final class MediaDataManager implements Dumpable, BcSmartspaceDataPlugin.SmartspaceTargetListener {
    public final ActivityStarter activityStarter;
    public boolean allowMediaRecommendations;
    public final Executor backgroundExecutor;
    public final int bgColor;
    public final Context context;
    public final DelayableExecutor foregroundExecutor;
    public final LinkedHashSet internalListeners;
    public final MediaControllerFactory mediaControllerFactory;
    public final MediaDataFilter mediaDataFilter;
    public final MediaFlags mediaFlags;
    public final SmartspaceMediaDataProvider smartspaceMediaDataProvider;
    public SmartspaceSession smartspaceSession;
    public final SystemClock systemClock;
    public final int themeText;
    public boolean useMediaResumption;
    public final boolean useQsMediaPlayer;
    public final LinkedHashMap<String, MediaData> mediaEntries = new LinkedHashMap<>();
    public SmartspaceMediaData smartspaceMediaData = MediaDataManagerKt.EMPTY_SMARTSPACE_MEDIA_DATA;

    /* compiled from: MediaDataManager.kt */
    /* renamed from: com.android.systemui.media.MediaDataManager$1  reason: invalid class name */
    /* loaded from: classes.dex */
    final class AnonymousClass1 extends Lambda implements Function2<String, Boolean, Unit> {
        public AnonymousClass1() {
            super(2);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Unit invoke(String str, Boolean bool) {
            boolean booleanValue = bool.booleanValue();
            MediaDataManager.this.setTimedOut$frameworks__base__packages__SystemUI__android_common__SystemUI_core(str, booleanValue, false);
            return Unit.INSTANCE;
        }
    }

    /* compiled from: MediaDataManager.kt */
    /* loaded from: classes.dex */
    public interface Listener {

        /* compiled from: MediaDataManager.kt */
        /* loaded from: classes.dex */
        public static final class DefaultImpls {
            public static /* synthetic */ void onMediaDataLoaded$default(Listener listener, String str, String str2, MediaData mediaData, boolean z, int i, int i2) {
                if ((i2 & 8) != 0) {
                    z = true;
                }
                if ((i2 & 16) != 0) {
                    i = 0;
                }
                listener.onMediaDataLoaded(str, str2, mediaData, z, i);
            }
        }

        void onMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z, int i);

        void onMediaDataRemoved(String str);

        void onSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData, boolean z, boolean z2);

        void onSmartspaceMediaDataRemoved(String str, boolean z);
    }

    public MediaDataManager(Context context, Executor executor, DelayableExecutor delayableExecutor, MediaControllerFactory mediaControllerFactory, DumpManager dumpManager, BroadcastDispatcher broadcastDispatcher, MediaTimeoutListener mediaTimeoutListener, final MediaResumeListener mediaResumeListener, MediaSessionBasedFilter mediaSessionBasedFilter, MediaDeviceManager mediaDeviceManager, MediaDataCombineLatest mediaDataCombineLatest, MediaDataFilter mediaDataFilter, ActivityStarter activityStarter, SmartspaceMediaDataProvider smartspaceMediaDataProvider, SystemClock systemClock, TunerService tunerService, MediaFlags mediaFlags) {
        boolean useMediaResumption = Utils.useMediaResumption(context);
        boolean useQsMediaPlayer = Utils.useQsMediaPlayer(context);
        this.context = context;
        this.backgroundExecutor = executor;
        this.foregroundExecutor = delayableExecutor;
        this.mediaControllerFactory = mediaControllerFactory;
        this.mediaDataFilter = mediaDataFilter;
        this.activityStarter = activityStarter;
        this.smartspaceMediaDataProvider = smartspaceMediaDataProvider;
        this.useMediaResumption = useMediaResumption;
        this.useQsMediaPlayer = useQsMediaPlayer;
        this.systemClock = systemClock;
        this.mediaFlags = mediaFlags;
        this.themeText = com.android.settingslib.Utils.getColorAttr(context, 16842806).getDefaultColor();
        this.bgColor = context.getColor(2131100337);
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        this.internalListeners = linkedHashSet;
        boolean z = true;
        this.allowMediaRecommendations = (!Utils.useQsMediaPlayer(context) || Settings.Secure.getInt(context.getContentResolver(), "qs_media_recommend", 1) <= 0) ? false : z;
        BroadcastReceiver mediaDataManager$appChangeReceiver$1 = new BroadcastReceiver() { // from class: com.android.systemui.media.MediaDataManager$appChangeReceiver$1
            @Override // android.content.BroadcastReceiver
            public final void onReceive(Context context2, Intent intent) {
                String[] stringArrayExtra;
                String encodedSchemeSpecificPart;
                String action = intent.getAction();
                if (action != null) {
                    int hashCode = action.hashCode();
                    if (hashCode != -1001645458) {
                        if (hashCode != -757780528) {
                            if (hashCode != 525384130 || !action.equals("android.intent.action.PACKAGE_REMOVED")) {
                                return;
                            }
                        } else if (!action.equals("android.intent.action.PACKAGE_RESTARTED")) {
                            return;
                        }
                        Uri data = intent.getData();
                        if (data != null && (encodedSchemeSpecificPart = data.getEncodedSchemeSpecificPart()) != null) {
                            MediaDataManager.access$removeAllForPackage(MediaDataManager.this, encodedSchemeSpecificPart);
                        }
                    } else if (action.equals("android.intent.action.PACKAGES_SUSPENDED") && (stringArrayExtra = intent.getStringArrayExtra("android.intent.extra.changed_package_list")) != null) {
                        MediaDataManager mediaDataManager = MediaDataManager.this;
                        int i = 0;
                        int length = stringArrayExtra.length;
                        while (i < length) {
                            String str = stringArrayExtra[i];
                            i++;
                            MediaDataManager.access$removeAllForPackage(mediaDataManager, str);
                        }
                    }
                }
            }
        };
        dumpManager.registerDumpable("MediaDataManager", this);
        linkedHashSet.add(mediaTimeoutListener);
        linkedHashSet.add(mediaResumeListener);
        linkedHashSet.add(mediaSessionBasedFilter);
        Objects.requireNonNull(mediaSessionBasedFilter);
        mediaSessionBasedFilter.listeners.add(mediaDeviceManager);
        mediaSessionBasedFilter.listeners.add(mediaDataCombineLatest);
        mediaDeviceManager.listeners.add(mediaDataCombineLatest);
        mediaDataCombineLatest.listeners.add(mediaDataFilter);
        mediaTimeoutListener.timeoutCallback = new AnonymousClass1();
        mediaResumeListener.mediaDataManager = this;
        mediaResumeListener.tunerService.addTunable(new TunerService.Tunable() { // from class: com.android.systemui.media.MediaResumeListener$setManager$1
            @Override // com.android.systemui.tuner.TunerService.Tunable
            public final void onTuningChanged(String str, String str2) {
                MediaResumeListener mediaResumeListener2 = MediaResumeListener.this;
                mediaResumeListener2.useMediaResumption = Utils.useMediaResumption(mediaResumeListener2.context);
                MediaResumeListener mediaResumeListener3 = MediaResumeListener.this;
                MediaDataManager mediaDataManager = mediaResumeListener3.mediaDataManager;
                if (mediaDataManager == null) {
                    mediaDataManager = null;
                }
                boolean z2 = mediaResumeListener3.useMediaResumption;
                Objects.requireNonNull(mediaDataManager);
                if (mediaDataManager.useMediaResumption != z2) {
                    mediaDataManager.useMediaResumption = z2;
                    if (!z2) {
                        LinkedHashMap<String, MediaData> linkedHashMap = mediaDataManager.mediaEntries;
                        LinkedHashMap linkedHashMap2 = new LinkedHashMap();
                        for (Map.Entry<String, MediaData> entry : linkedHashMap.entrySet()) {
                            MediaData value = entry.getValue();
                            Objects.requireNonNull(value);
                            if (!value.active) {
                                linkedHashMap2.put(entry.getKey(), entry.getValue());
                            }
                        }
                        for (Map.Entry entry2 : linkedHashMap2.entrySet()) {
                            mediaDataManager.mediaEntries.remove(entry2.getKey());
                            mediaDataManager.notifyMediaDataRemoved((String) entry2.getKey());
                        }
                    }
                }
            }
        }, "qs_media_resumption");
        mediaDataFilter.mediaDataManager = this;
        BroadcastDispatcher.registerReceiver$default(broadcastDispatcher, mediaDataManager$appChangeReceiver$1, new IntentFilter("android.intent.action.PACKAGES_SUSPENDED"), null, UserHandle.ALL, 16);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addAction("android.intent.action.PACKAGE_RESTARTED");
        intentFilter.addDataScheme("package");
        context.registerReceiver(mediaDataManager$appChangeReceiver$1, intentFilter);
        smartspaceMediaDataProvider.registerListener(this);
        SmartspaceSession createSmartspaceSession = ((SmartspaceManager) context.getSystemService(SmartspaceManager.class)).createSmartspaceSession(new SmartspaceConfig.Builder(context, "media_data_manager").build());
        this.smartspaceSession = createSmartspaceSession;
        if (createSmartspaceSession != null) {
            createSmartspaceSession.addOnTargetsAvailableListener(Executors.newCachedThreadPool(), new SmartspaceSession.OnTargetsAvailableListener() { // from class: com.android.systemui.media.MediaDataManager$2$1
                public final void onTargetsAvailable(List<SmartspaceTarget> list) {
                    MediaDataManager.this.smartspaceMediaDataProvider.onTargetsAvailable(list);
                }
            });
        }
        SmartspaceSession smartspaceSession = this.smartspaceSession;
        if (smartspaceSession != null) {
            smartspaceSession.requestSmartspaceUpdate();
        }
        tunerService.addTunable(new TunerService.Tunable() { // from class: com.android.systemui.media.MediaDataManager.4
            @Override // com.android.systemui.tuner.TunerService.Tunable
            public final void onTuningChanged(String str, String str2) {
                MediaDataManager mediaDataManager = MediaDataManager.this;
                Context context2 = mediaDataManager.context;
                boolean z2 = true;
                int i = Settings.Secure.getInt(context2.getContentResolver(), "qs_media_recommend", 1);
                if (!Utils.useQsMediaPlayer(context2) || i <= 0) {
                    z2 = false;
                }
                mediaDataManager.allowMediaRecommendations = z2;
                MediaDataManager mediaDataManager2 = MediaDataManager.this;
                if (!mediaDataManager2.allowMediaRecommendations) {
                    SmartspaceMediaData smartspaceMediaData = mediaDataManager2.smartspaceMediaData;
                    Objects.requireNonNull(smartspaceMediaData);
                    mediaDataManager2.dismissSmartspaceRecommendation(smartspaceMediaData.targetId, 0L);
                }
            }
        }, "qs_media_recommend");
    }

    public final MediaAction getStandardAction(final MediaController mediaController, long j, long j2) {
        if ((j & j2) == 0) {
            return null;
        }
        if (j2 == 4) {
            return new MediaAction(Icon.createWithResource(this.context, 2131232043), new Runnable() { // from class: com.android.systemui.media.MediaDataManager$getStandardAction$1
                @Override // java.lang.Runnable
                public final void run() {
                    mediaController.getTransportControls().play();
                }
            }, this.context.getString(2131952181));
        }
        if (j2 == 2) {
            return new MediaAction(Icon.createWithResource(this.context, 2131232040), new Runnable() { // from class: com.android.systemui.media.MediaDataManager$getStandardAction$2
                @Override // java.lang.Runnable
                public final void run() {
                    mediaController.getTransportControls().pause();
                }
            }, this.context.getString(2131952180));
        }
        if (j2 == 16) {
            return new MediaAction(Icon.createWithResource(this.context, 2131232046), new Runnable() { // from class: com.android.systemui.media.MediaDataManager$getStandardAction$3
                @Override // java.lang.Runnable
                public final void run() {
                    mediaController.getTransportControls().skipToPrevious();
                }
            }, this.context.getString(2131952182));
        }
        if (j2 == 32) {
            return new MediaAction(Icon.createWithResource(this.context, 2131232039), new Runnable() { // from class: com.android.systemui.media.MediaDataManager$getStandardAction$4
                @Override // java.lang.Runnable
                public final void run() {
                    mediaController.getTransportControls().skipToNext();
                }
            }, this.context.getString(2131952179));
        }
        return null;
    }

    public final void addListener(Listener listener) {
        MediaDataFilter mediaDataFilter = this.mediaDataFilter;
        Objects.requireNonNull(mediaDataFilter);
        mediaDataFilter._listeners.add(listener);
    }

    public final boolean dismissMediaData(final String str, long j) {
        boolean z;
        if (this.mediaEntries.get(str) != null) {
            z = true;
        } else {
            z = false;
        }
        this.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaDataManager$dismissMediaData$1
            @Override // java.lang.Runnable
            public final void run() {
                boolean z2;
                MediaSession.Token token;
                MediaData mediaData = MediaDataManager.this.mediaEntries.get(str);
                if (mediaData != null) {
                    MediaDataManager mediaDataManager = MediaDataManager.this;
                    if (mediaData.playbackLocation == 0) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    if (z2 && (token = mediaData.token) != null) {
                        MediaControllerFactory mediaControllerFactory = mediaDataManager.mediaControllerFactory;
                        Objects.requireNonNull(mediaControllerFactory);
                        new MediaController(mediaControllerFactory.mContext, token).getTransportControls().stop();
                    }
                }
            }
        });
        this.foregroundExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.media.MediaDataManager$dismissMediaData$2
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataManager mediaDataManager = MediaDataManager.this;
                String str2 = str;
                Objects.requireNonNull(mediaDataManager);
                mediaDataManager.mediaEntries.remove(str2);
                mediaDataManager.notifyMediaDataRemoved(str2);
            }
        }, j);
        return z;
    }

    public final void dismissSmartspaceRecommendation(String str, long j) {
        SmartspaceMediaData smartspaceMediaData = this.smartspaceMediaData;
        Objects.requireNonNull(smartspaceMediaData);
        if (Intrinsics.areEqual(smartspaceMediaData.targetId, str)) {
            Log.d("MediaDataManager", "Dismissing Smartspace media target");
            SmartspaceMediaData smartspaceMediaData2 = this.smartspaceMediaData;
            Objects.requireNonNull(smartspaceMediaData2);
            if (smartspaceMediaData2.isActive) {
                SmartspaceMediaData smartspaceMediaData3 = MediaDataManagerKt.EMPTY_SMARTSPACE_MEDIA_DATA;
                SmartspaceMediaData smartspaceMediaData4 = this.smartspaceMediaData;
                Objects.requireNonNull(smartspaceMediaData4);
                this.smartspaceMediaData = SmartspaceMediaData.copy$default(smartspaceMediaData3, smartspaceMediaData4.targetId, false, false, null, 0, 0L, 510);
            }
            this.foregroundExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.media.MediaDataManager$dismissSmartspaceRecommendation$1
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataManager mediaDataManager = MediaDataManager.this;
                    Objects.requireNonNull(mediaDataManager);
                    SmartspaceMediaData smartspaceMediaData5 = mediaDataManager.smartspaceMediaData;
                    Objects.requireNonNull(smartspaceMediaData5);
                    mediaDataManager.notifySmartspaceMediaDataRemoved(smartspaceMediaData5.targetId, true);
                }
            }, j);
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println(Intrinsics.stringPlus("internalListeners: ", this.internalListeners));
        printWriter.println(Intrinsics.stringPlus("externalListeners: ", this.mediaDataFilter.getListeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core()));
        printWriter.println(Intrinsics.stringPlus("mediaEntries: ", this.mediaEntries));
        KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0.m(this.useMediaResumption, "useMediaResumption: ", printWriter);
    }

    public final MediaAction getResumeMediaAction(Runnable runnable) {
        return new MediaAction(Icon.createWithResource(this.context, 2131232043).setTint(this.themeText), runnable, this.context.getString(2131952186));
    }

    public final boolean hasActiveMedia() {
        boolean z;
        MediaDataFilter mediaDataFilter = this.mediaDataFilter;
        Objects.requireNonNull(mediaDataFilter);
        LinkedHashMap<String, MediaData> linkedHashMap = mediaDataFilter.userEntries;
        if (!linkedHashMap.isEmpty()) {
            for (Map.Entry<String, MediaData> entry : linkedHashMap.entrySet()) {
                MediaData value = entry.getValue();
                Objects.requireNonNull(value);
                if (value.active) {
                    z = true;
                    break;
                }
            }
        }
        z = false;
        if (z) {
            return true;
        }
        SmartspaceMediaData smartspaceMediaData = mediaDataFilter.smartspaceMediaData;
        Objects.requireNonNull(smartspaceMediaData);
        if (smartspaceMediaData.isActive) {
            return true;
        }
        return false;
    }

    public final Bitmap loadBitmapFromUri(Uri uri) {
        if (uri.getScheme() == null) {
            return null;
        }
        if (!uri.getScheme().equals("content") && !uri.getScheme().equals("android.resource") && !uri.getScheme().equals("file")) {
            return null;
        }
        try {
            return ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.context.getContentResolver(), uri), MediaDataManager$loadBitmapFromUri$1.INSTANCE);
        } catch (IOException e) {
            Log.e("MediaDataManager", "Unable to load bitmap", e);
            return null;
        } catch (RuntimeException e2) {
            Log.e("MediaDataManager", "Unable to load bitmap", e2);
            return null;
        }
    }

    public final void notifyMediaDataLoaded(String str, String str2, MediaData mediaData) {
        for (Listener listener : this.internalListeners) {
            Listener.DefaultImpls.onMediaDataLoaded$default(listener, str, str2, mediaData, false, 0, 24);
        }
    }

    public final void notifyMediaDataRemoved(String str) {
        for (Listener listener : this.internalListeners) {
            listener.onMediaDataRemoved(str);
        }
    }

    public final void notifySmartspaceMediaDataRemoved(String str, boolean z) {
        for (Listener listener : this.internalListeners) {
            listener.onSmartspaceMediaDataRemoved(str, z);
        }
    }

    public final void onNotificationAdded(final String str, final StatusBarNotification statusBarNotification) {
        if (this.useQsMediaPlayer) {
            String[] strArr = MediaDataManagerKt.ART_URIS;
            if (statusBarNotification.getNotification().isMediaNotification()) {
                Assert.isMainThread();
                final String packageName = statusBarNotification.getPackageName();
                if (this.mediaEntries.containsKey(str)) {
                    packageName = str;
                } else if (!this.mediaEntries.containsKey(packageName)) {
                    packageName = null;
                }
                if (packageName == null) {
                    this.mediaEntries.put(str, MediaData.copy$default(MediaDataManagerKt.LOADING, null, null, statusBarNotification.getPackageName(), null, false, null, false, false, null, false, 16775167));
                } else if (!Intrinsics.areEqual(packageName, str)) {
                    MediaData remove = this.mediaEntries.remove(packageName);
                    Intrinsics.checkNotNull(remove);
                    this.mediaEntries.put(str, remove);
                }
                this.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaDataManager$loadMediaData$1
                    /* JADX WARN: Multi-variable type inference failed */
                    /* JADX WARN: Removed duplicated region for block: B:103:0x02d6  */
                    /* JADX WARN: Removed duplicated region for block: B:109:0x02eb  */
                    /* JADX WARN: Removed duplicated region for block: B:163:0x0442  */
                    /* JADX WARN: Type inference failed for: r0v11, types: [T, kotlin.collections.EmptyList] */
                    /* JADX WARN: Type inference failed for: r0v43, types: [T, java.lang.Object] */
                    /* JADX WARN: Type inference failed for: r0v44, types: [T, java.lang.Object] */
                    /* JADX WARN: Type inference failed for: r5v27, types: [com.android.systemui.media.MediaDeviceData, T] */
                    /* JADX WARN: Unknown variable types count: 1 */
                    @Override // java.lang.Runnable
                    /*
                        Code decompiled incorrectly, please refer to instructions dump.
                        To view partially-correct add '--show-bad-code' argument
                    */
                    public final void run() {
                        /*
                            Method dump skipped, instructions count: 1171
                            To view this dump add '--comments-level debug' option
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaDataManager$loadMediaData$1.run():void");
                    }
                });
                return;
            }
        }
        onNotificationRemoved(str);
    }

    public final void onNotificationRemoved(String str) {
        Runnable runnable;
        boolean z;
        Assert.isMainThread();
        MediaData remove = this.mediaEntries.remove(str);
        if (this.useMediaResumption) {
            Boolean bool = null;
            if (remove == null) {
                runnable = null;
            } else {
                runnable = remove.resumeAction;
            }
            if (runnable != null) {
                boolean z2 = true;
                if (remove != null) {
                    if (remove.playbackLocation == 0) {
                        z = true;
                    } else {
                        z = false;
                    }
                    bool = Boolean.valueOf(z);
                }
                if (bool.booleanValue()) {
                    Log.d("MediaDataManager", "Not removing " + str + " because resumable");
                    Objects.requireNonNull(remove);
                    Runnable runnable2 = remove.resumeAction;
                    Intrinsics.checkNotNull(runnable2);
                    MediaData copy$default = MediaData.copy$default(remove, Collections.singletonList(getResumeMediaAction(runnable2)), Collections.singletonList(0), null, null, false, null, true, false, Boolean.FALSE, true, 10185983);
                    String str2 = remove.packageName;
                    if (this.mediaEntries.put(str2, copy$default) != null) {
                        z2 = false;
                    }
                    if (z2) {
                        notifyMediaDataLoaded(str2, str, copy$default);
                        return;
                    }
                    notifyMediaDataRemoved(str);
                    notifyMediaDataLoaded(str2, str2, copy$default);
                    return;
                }
            }
        }
        if (remove != null) {
            notifyMediaDataRemoved(str);
        }
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceTargetListener
    public final void onSmartspaceTargetsUpdated(List<? extends Parcelable> list) {
        Intent intent;
        String str;
        SmartspaceMediaData smartspaceMediaData;
        String string;
        if (!this.allowMediaRecommendations) {
            Log.d("MediaDataManager", "Smartspace recommendation is disabled in Settings.");
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (Object obj : list) {
            if (obj instanceof SmartspaceTarget) {
                arrayList.add(obj);
            }
        }
        int size = arrayList.size();
        if (size == 0) {
            SmartspaceMediaData smartspaceMediaData2 = this.smartspaceMediaData;
            Objects.requireNonNull(smartspaceMediaData2);
            if (smartspaceMediaData2.isActive) {
                Log.d("MediaDataManager", "Set Smartspace media to be inactive for the data update");
                SmartspaceMediaData smartspaceMediaData3 = MediaDataManagerKt.EMPTY_SMARTSPACE_MEDIA_DATA;
                SmartspaceMediaData smartspaceMediaData4 = this.smartspaceMediaData;
                Objects.requireNonNull(smartspaceMediaData4);
                SmartspaceMediaData copy$default = SmartspaceMediaData.copy$default(smartspaceMediaData3, smartspaceMediaData4.targetId, false, false, null, 0, 0L, 510);
                this.smartspaceMediaData = copy$default;
                notifySmartspaceMediaDataRemoved(copy$default.targetId, false);
            }
        } else if (size != 1) {
            Log.wtf("MediaDataManager", "More than 1 Smartspace Media Update. Resetting the status...");
            SmartspaceMediaData smartspaceMediaData5 = this.smartspaceMediaData;
            Objects.requireNonNull(smartspaceMediaData5);
            notifySmartspaceMediaDataRemoved(smartspaceMediaData5.targetId, false);
            this.smartspaceMediaData = MediaDataManagerKt.EMPTY_SMARTSPACE_MEDIA_DATA;
        } else {
            SmartspaceTarget smartspaceTarget = (SmartspaceTarget) arrayList.get(0);
            SmartspaceMediaData smartspaceMediaData6 = this.smartspaceMediaData;
            Objects.requireNonNull(smartspaceMediaData6);
            if (!Intrinsics.areEqual(smartspaceMediaData6.targetId, smartspaceTarget.getSmartspaceTargetId())) {
                Log.d("MediaDataManager", "Forwarding Smartspace media update.");
                if (smartspaceTarget.getBaseAction() == null || smartspaceTarget.getBaseAction().getExtras() == null) {
                    intent = null;
                } else {
                    intent = (Intent) smartspaceTarget.getBaseAction().getExtras().getParcelable("dismiss_intent");
                }
                List<SmartspaceAction> iconGrid = smartspaceTarget.getIconGrid();
                if (iconGrid == null || iconGrid.isEmpty()) {
                    Log.w("MediaDataManager", "Empty or null media recommendation list.");
                } else {
                    for (SmartspaceAction smartspaceAction : iconGrid) {
                        Bundle extras = smartspaceAction.getExtras();
                        if (!(extras == null || (string = extras.getString("package_name")) == null)) {
                            str = string;
                            break;
                        }
                    }
                    Log.w("MediaDataManager", "No valid package name is provided.");
                }
                str = null;
                if (str == null) {
                    smartspaceMediaData = SmartspaceMediaData.copy$default(MediaDataManagerKt.EMPTY_SMARTSPACE_MEDIA_DATA, smartspaceTarget.getSmartspaceTargetId(), true, false, intent, 0, smartspaceTarget.getCreationTimeMillis(), 188);
                } else {
                    smartspaceMediaData = new SmartspaceMediaData(smartspaceTarget.getSmartspaceTargetId(), true, true, str, smartspaceTarget.getBaseAction(), smartspaceTarget.getIconGrid(), intent, 0, smartspaceTarget.getCreationTimeMillis());
                }
                this.smartspaceMediaData = smartspaceMediaData;
                String str2 = smartspaceMediaData.targetId;
                for (Listener listener : this.internalListeners) {
                    listener.onSmartspaceMediaDataLoaded(str2, smartspaceMediaData, false, false);
                }
            }
        }
    }

    public final void setResumeAction(String str, MediaResumeListener$getResumeAction$1 mediaResumeListener$getResumeAction$1) {
        MediaData mediaData = this.mediaEntries.get(str);
        if (mediaData != null) {
            mediaData.resumeAction = mediaResumeListener$getResumeAction$1;
            mediaData.hasCheckedForResume = true;
        }
    }

    public final void setTimedOut$frameworks__base__packages__SystemUI__android_common__SystemUI_core(String str, boolean z, boolean z2) {
        MediaData mediaData = this.mediaEntries.get(str);
        if (mediaData != null) {
            boolean z3 = !z;
            if (mediaData.active != z3 || z2) {
                mediaData.active = z3;
                Log.d("MediaDataManager", "Updating " + str + " timedOut: " + z);
                onMediaDataLoaded(str, str, mediaData);
            } else if (mediaData.resumption) {
                Log.d("MediaDataManager", Intrinsics.stringPlus("timing out resume player ", str));
                dismissMediaData(str, 0L);
            }
        }
    }

    public static final void access$removeAllForPackage(MediaDataManager mediaDataManager, String str) {
        Objects.requireNonNull(mediaDataManager);
        Assert.isMainThread();
        LinkedHashMap<String, MediaData> linkedHashMap = mediaDataManager.mediaEntries;
        LinkedHashMap linkedHashMap2 = new LinkedHashMap();
        for (Map.Entry<String, MediaData> entry : linkedHashMap.entrySet()) {
            MediaData value = entry.getValue();
            Objects.requireNonNull(value);
            if (Intrinsics.areEqual(value.packageName, str)) {
                linkedHashMap2.put(entry.getKey(), entry.getValue());
            }
        }
        for (Map.Entry entry2 : linkedHashMap2.entrySet()) {
            String str2 = (String) entry2.getKey();
            mediaDataManager.mediaEntries.remove(str2);
            mediaDataManager.notifyMediaDataRemoved(str2);
        }
    }

    public final void onMediaDataLoaded(String str, String str2, MediaData mediaData) {
        Assert.isMainThread();
        if (this.mediaEntries.containsKey(str)) {
            this.mediaEntries.put(str, mediaData);
            notifyMediaDataLoaded(str, str2, mediaData);
        }
    }
}
