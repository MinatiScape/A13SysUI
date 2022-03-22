package com.android.systemui.media;

import android.graphics.drawable.Drawable;
import android.media.AudioDeviceAttributes;
import android.media.MediaRouter2Manager;
import android.media.RoutingSessionInfo;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import com.android.settingslib.media.InfoMediaManager;
import com.android.settingslib.media.LocalMediaManager;
import com.android.settingslib.media.MediaDevice;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.flags.Flags;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.MediaDeviceManager;
import com.android.systemui.media.muteawait.MediaMuteAwaitConnectionManager;
import com.android.systemui.media.muteawait.MediaMuteAwaitConnectionManagerFactory;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaDeviceManager.kt */
/* loaded from: classes.dex */
public final class MediaDeviceManager implements MediaDataManager.Listener, Dumpable {
    public final Executor bgExecutor;
    public final MediaControllerFactory controllerFactory;
    public final Executor fgExecutor;
    public final LocalMediaManagerFactory localMediaManagerFactory;
    public final MediaRouter2Manager mr2manager;
    public final MediaMuteAwaitConnectionManagerFactory muteAwaitConnectionManagerFactory;
    public final LinkedHashSet listeners = new LinkedHashSet();
    public final LinkedHashMap entries = new LinkedHashMap();

    /* compiled from: MediaDeviceManager.kt */
    /* loaded from: classes.dex */
    public final class Entry extends MediaController.Callback implements LocalMediaManager.DeviceCallback {
        public MediaDeviceData aboutToConnectDeviceOverride;
        public final MediaController controller;
        public MediaDeviceData current;
        public final String key;
        public final LocalMediaManager localMediaManager;
        public final MediaMuteAwaitConnectionManager muteAwaitConnectionManager;
        public final String oldKey;
        public int playbackType;
        public boolean started;

        @Override // com.android.settingslib.media.LocalMediaManager.DeviceCallback
        public final void onAboutToConnectDeviceChanged(String str, Drawable drawable) {
            MediaDeviceData mediaDeviceData = null;
            if (!(str == null || drawable == null)) {
                mediaDeviceData = new MediaDeviceData(true, drawable, str, null);
            }
            this.aboutToConnectDeviceOverride = mediaDeviceData;
            updateCurrent();
        }

        public Entry(String str, String str2, MediaController mediaController, LocalMediaManager localMediaManager, MediaMuteAwaitConnectionManager mediaMuteAwaitConnectionManager) {
            this.key = str;
            this.oldKey = str2;
            this.controller = mediaController;
            this.localMediaManager = localMediaManager;
            this.muteAwaitConnectionManager = mediaMuteAwaitConnectionManager;
        }

        @Override // android.media.session.MediaController.Callback
        public final void onAudioInfoChanged(MediaController.PlaybackInfo playbackInfo) {
            int i;
            if (playbackInfo == null) {
                i = 0;
            } else {
                i = playbackInfo.getPlaybackType();
            }
            if (i != this.playbackType) {
                this.playbackType = i;
                updateCurrent();
            }
        }

        @Override // com.android.settingslib.media.LocalMediaManager.DeviceCallback
        public final void onDeviceListUpdate(ArrayList arrayList) {
            MediaDeviceManager.this.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaDeviceManager$Entry$onDeviceListUpdate$1
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDeviceManager.Entry.this.updateCurrent();
                }
            });
        }

        @Override // com.android.settingslib.media.LocalMediaManager.DeviceCallback
        public final void onSelectedDeviceStateChanged(MediaDevice mediaDevice) {
            MediaDeviceManager.this.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaDeviceManager$Entry$onSelectedDeviceStateChanged$1
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDeviceManager.Entry.this.updateCurrent();
                }
            });
        }

        public final void updateCurrent() {
            RoutingSessionInfo routingSessionInfo;
            boolean z;
            String str;
            Drawable drawable;
            CharSequence name;
            final MediaDeviceData mediaDeviceData = this.aboutToConnectDeviceOverride;
            if (mediaDeviceData == null) {
                LocalMediaManager localMediaManager = this.localMediaManager;
                Objects.requireNonNull(localMediaManager);
                MediaDevice mediaDevice = localMediaManager.mCurrentConnectedDevice;
                MediaController mediaController = this.controller;
                if (mediaController == null) {
                    routingSessionInfo = null;
                } else {
                    routingSessionInfo = MediaDeviceManager.this.mr2manager.getRoutingSessionForMediaController(mediaController);
                }
                if (mediaDevice == null || (this.controller != null && routingSessionInfo == null)) {
                    z = false;
                } else {
                    z = true;
                }
                if (routingSessionInfo == null || (name = routingSessionInfo.getName()) == null) {
                    str = null;
                } else {
                    str = name.toString();
                }
                if (str == null) {
                    if (mediaDevice == null) {
                        str = null;
                    } else {
                        str = mediaDevice.getName();
                    }
                }
                if (mediaDevice == null) {
                    drawable = null;
                } else {
                    drawable = mediaDevice.getIconWithoutBackground();
                }
                final MediaDeviceData mediaDeviceData2 = new MediaDeviceData(z, drawable, str, null);
                if (!this.started || !Intrinsics.areEqual(mediaDeviceData2, this.current)) {
                    this.current = mediaDeviceData2;
                    final MediaDeviceManager mediaDeviceManager = MediaDeviceManager.this;
                    mediaDeviceManager.fgExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaDeviceManager$Entry$current$1
                        @Override // java.lang.Runnable
                        public final void run() {
                            MediaDeviceManager mediaDeviceManager2 = MediaDeviceManager.this;
                            MediaDeviceManager.Entry entry = this;
                            Objects.requireNonNull(entry);
                            String str2 = entry.key;
                            MediaDeviceManager.Entry entry2 = this;
                            Objects.requireNonNull(entry2);
                            String str3 = entry2.oldKey;
                            MediaDeviceData mediaDeviceData3 = mediaDeviceData2;
                            Objects.requireNonNull(mediaDeviceManager2);
                            for (MediaDeviceManager.Listener listener : mediaDeviceManager2.listeners) {
                                listener.onMediaDeviceChanged(str2, str3, mediaDeviceData3);
                            }
                        }
                    });
                }
            } else if (!this.started || !Intrinsics.areEqual(mediaDeviceData, this.current)) {
                this.current = mediaDeviceData;
                final MediaDeviceManager mediaDeviceManager2 = MediaDeviceManager.this;
                mediaDeviceManager2.fgExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaDeviceManager$Entry$current$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaDeviceManager mediaDeviceManager22 = MediaDeviceManager.this;
                        MediaDeviceManager.Entry entry = this;
                        Objects.requireNonNull(entry);
                        String str2 = entry.key;
                        MediaDeviceManager.Entry entry2 = this;
                        Objects.requireNonNull(entry2);
                        String str3 = entry2.oldKey;
                        MediaDeviceData mediaDeviceData3 = mediaDeviceData;
                        Objects.requireNonNull(mediaDeviceManager22);
                        for (MediaDeviceManager.Listener listener : mediaDeviceManager22.listeners) {
                            listener.onMediaDeviceChanged(str2, str3, mediaDeviceData3);
                        }
                    }
                });
            }
        }
    }

    /* compiled from: MediaDeviceManager.kt */
    /* loaded from: classes.dex */
    public interface Listener {
        void onKeyRemoved(String str);

        void onMediaDeviceChanged(String str, String str2, MediaDeviceData mediaDeviceData);
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public final void onSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData, boolean z, boolean z2) {
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public final void onSmartspaceMediaDataRemoved(String str, boolean z) {
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] strArr) {
        printWriter.println("MediaDeviceManager state:");
        this.entries.forEach(new BiConsumer(printWriter, fileDescriptor, printWriter, strArr) { // from class: com.android.systemui.media.MediaDeviceManager$dump$1$1
            public final /* synthetic */ PrintWriter $pw;
            public final /* synthetic */ PrintWriter $this_with;

            {
                this.$pw = printWriter;
            }

            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                RoutingSessionInfo routingSessionInfo;
                List list;
                CharSequence charSequence;
                MediaController.PlaybackInfo playbackInfo;
                MediaDeviceManager.Entry entry = (MediaDeviceManager.Entry) obj2;
                this.$this_with.println(Intrinsics.stringPlus("  key=", (String) obj));
                PrintWriter printWriter2 = this.$pw;
                MediaController mediaController = entry.controller;
                Integer num = null;
                if (mediaController == null) {
                    routingSessionInfo = null;
                } else {
                    routingSessionInfo = MediaDeviceManager.this.mr2manager.getRoutingSessionForMediaController(mediaController);
                }
                if (routingSessionInfo == null) {
                    list = null;
                } else {
                    list = MediaDeviceManager.this.mr2manager.getSelectedRoutes(routingSessionInfo);
                }
                MediaDeviceData mediaDeviceData = entry.current;
                if (mediaDeviceData == null) {
                    charSequence = null;
                } else {
                    charSequence = mediaDeviceData.name;
                }
                printWriter2.println(Intrinsics.stringPlus("    current device is ", charSequence));
                MediaController mediaController2 = entry.controller;
                if (!(mediaController2 == null || (playbackInfo = mediaController2.getPlaybackInfo()) == null)) {
                    num = Integer.valueOf(playbackInfo.getPlaybackType());
                }
                printWriter2.println("    PlaybackType=" + num + " (1 for local, 2 for remote) cached=" + entry.playbackType);
                printWriter2.println(Intrinsics.stringPlus("    routingSession=", routingSessionInfo));
                printWriter2.println(Intrinsics.stringPlus("    selectedRoutes=", list));
            }
        });
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public final void onMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z, int i) {
        MediaController mediaController;
        MediaSession.Token token;
        Entry entry;
        if (!(str2 == null || Intrinsics.areEqual(str2, str) || (entry = (Entry) this.entries.remove(str2)) == null)) {
            MediaDeviceManager.this.bgExecutor.execute(new MediaDeviceManager$Entry$stop$1(entry));
        }
        Entry entry2 = (Entry) this.entries.get(str);
        MediaMuteAwaitConnectionManager mediaMuteAwaitConnectionManager = null;
        if (entry2 != null) {
            MediaController mediaController2 = entry2.controller;
            if (mediaController2 == null) {
                token = null;
            } else {
                token = mediaController2.getSessionToken();
            }
            if (Intrinsics.areEqual(token, mediaData.token)) {
                return;
            }
        }
        if (entry2 != null) {
            MediaDeviceManager.this.bgExecutor.execute(new MediaDeviceManager$Entry$stop$1(entry2));
        }
        MediaDeviceData mediaDeviceData = mediaData.device;
        if (mediaDeviceData != null) {
            for (Listener listener : this.listeners) {
                listener.onMediaDeviceChanged(str, str2, mediaDeviceData);
            }
            return;
        }
        MediaSession.Token token2 = mediaData.token;
        if (token2 == null) {
            mediaController = null;
        } else {
            MediaControllerFactory mediaControllerFactory = this.controllerFactory;
            Objects.requireNonNull(mediaControllerFactory);
            mediaController = new MediaController(mediaControllerFactory.mContext, token2);
        }
        LocalMediaManagerFactory localMediaManagerFactory = this.localMediaManagerFactory;
        String str3 = mediaData.packageName;
        Objects.requireNonNull(localMediaManagerFactory);
        LocalMediaManager localMediaManager = new LocalMediaManager(localMediaManagerFactory.context, localMediaManagerFactory.localBluetoothManager, new InfoMediaManager(localMediaManagerFactory.context, str3, localMediaManagerFactory.localBluetoothManager), str3);
        MediaMuteAwaitConnectionManagerFactory mediaMuteAwaitConnectionManagerFactory = this.muteAwaitConnectionManagerFactory;
        Objects.requireNonNull(mediaMuteAwaitConnectionManagerFactory);
        MediaFlags mediaFlags = mediaMuteAwaitConnectionManagerFactory.mediaFlags;
        Objects.requireNonNull(mediaFlags);
        if (mediaFlags.featureFlags.isEnabled(Flags.MEDIA_MUTE_AWAIT)) {
            mediaMuteAwaitConnectionManager = new MediaMuteAwaitConnectionManager(mediaMuteAwaitConnectionManagerFactory.mainExecutor, localMediaManager, mediaMuteAwaitConnectionManagerFactory.context, mediaMuteAwaitConnectionManagerFactory.deviceIconUtil);
        }
        final Entry entry3 = new Entry(str, str2, mediaController, localMediaManager, mediaMuteAwaitConnectionManager);
        this.entries.put(str, entry3);
        this.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaDeviceManager$Entry$start$1
            @Override // java.lang.Runnable
            public final void run() {
                MediaController.PlaybackInfo playbackInfo;
                MediaDeviceManager.Entry entry4 = MediaDeviceManager.Entry.this;
                Objects.requireNonNull(entry4);
                LocalMediaManager localMediaManager2 = entry4.localMediaManager;
                MediaDeviceManager.Entry entry5 = MediaDeviceManager.Entry.this;
                Objects.requireNonNull(localMediaManager2);
                localMediaManager2.mCallbacks.add(entry5);
                MediaDeviceManager.Entry entry6 = MediaDeviceManager.Entry.this;
                Objects.requireNonNull(entry6);
                entry6.localMediaManager.startScan();
                MediaDeviceManager.Entry entry7 = MediaDeviceManager.Entry.this;
                Objects.requireNonNull(entry7);
                MediaMuteAwaitConnectionManager mediaMuteAwaitConnectionManager2 = entry7.muteAwaitConnectionManager;
                if (mediaMuteAwaitConnectionManager2 != null) {
                    mediaMuteAwaitConnectionManager2.audioManager.registerMuteAwaitConnectionCallback(mediaMuteAwaitConnectionManager2.mainExecutor, mediaMuteAwaitConnectionManager2.muteAwaitConnectionChangeListener);
                    AudioDeviceAttributes mutingExpectedDevice = mediaMuteAwaitConnectionManager2.audioManager.getMutingExpectedDevice();
                    if (mutingExpectedDevice != null) {
                        mediaMuteAwaitConnectionManager2.currentMutedDevice = mutingExpectedDevice;
                        mediaMuteAwaitConnectionManager2.localMediaManager.dispatchAboutToConnectDeviceChanged(mutingExpectedDevice.getName(), mediaMuteAwaitConnectionManager2.getIcon(mutingExpectedDevice));
                    }
                }
                MediaDeviceManager.Entry entry8 = MediaDeviceManager.Entry.this;
                Objects.requireNonNull(entry8);
                MediaController mediaController3 = entry8.controller;
                int i2 = 0;
                if (!(mediaController3 == null || (playbackInfo = mediaController3.getPlaybackInfo()) == null)) {
                    i2 = playbackInfo.getPlaybackType();
                }
                entry8.playbackType = i2;
                MediaDeviceManager.Entry entry9 = MediaDeviceManager.Entry.this;
                Objects.requireNonNull(entry9);
                MediaController mediaController4 = entry9.controller;
                if (mediaController4 != null) {
                    mediaController4.registerCallback(MediaDeviceManager.Entry.this);
                }
                MediaDeviceManager.Entry.this.updateCurrent();
                MediaDeviceManager.Entry.this.started = true;
            }
        });
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public final void onMediaDataRemoved(String str) {
        Entry entry = (Entry) this.entries.remove(str);
        if (entry != null) {
            MediaDeviceManager.this.bgExecutor.execute(new MediaDeviceManager$Entry$stop$1(entry));
        }
        if (entry != null) {
            for (Listener listener : this.listeners) {
                listener.onKeyRemoved(str);
            }
        }
    }

    public MediaDeviceManager(MediaControllerFactory mediaControllerFactory, LocalMediaManagerFactory localMediaManagerFactory, MediaRouter2Manager mediaRouter2Manager, MediaMuteAwaitConnectionManagerFactory mediaMuteAwaitConnectionManagerFactory, Executor executor, Executor executor2, DumpManager dumpManager) {
        this.controllerFactory = mediaControllerFactory;
        this.localMediaManagerFactory = localMediaManagerFactory;
        this.mr2manager = mediaRouter2Manager;
        this.muteAwaitConnectionManagerFactory = mediaMuteAwaitConnectionManagerFactory;
        this.fgExecutor = executor;
        this.bgExecutor = executor2;
        dumpManager.registerDumpable(MediaDeviceManager.class.getName(), this);
    }
}
