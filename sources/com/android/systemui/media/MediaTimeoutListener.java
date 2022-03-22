package com.android.systemui.media;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.util.Log;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.MediaTimeoutListener;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.LinkedHashMap;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaTimeoutListener.kt */
/* loaded from: classes.dex */
public final class MediaTimeoutListener implements MediaDataManager.Listener {
    public final DelayableExecutor mainExecutor;
    public final MediaControllerFactory mediaControllerFactory;
    public final LinkedHashMap mediaListeners = new LinkedHashMap();
    public Function2<? super String, ? super Boolean, Unit> timeoutCallback;

    /* compiled from: MediaTimeoutListener.kt */
    /* loaded from: classes.dex */
    public final class PlaybackStateListener extends MediaController.Callback {
        public Runnable cancellation;
        public boolean destroyed;
        public String key;
        public MediaController mediaController;
        public MediaData mediaData;
        public Boolean playing;
        public Boolean resumption;
        public boolean timedOut;

        @Override // android.media.session.MediaController.Callback
        public final void onPlaybackStateChanged(PlaybackState playbackState) {
            processState(playbackState, true);
        }

        public final void setMediaData(MediaData mediaData) {
            MediaController mediaController;
            this.destroyed = false;
            MediaController mediaController2 = this.mediaController;
            if (mediaController2 != null) {
                mediaController2.unregisterCallback(this);
            }
            this.mediaData = mediaData;
            Objects.requireNonNull(mediaData);
            PlaybackState playbackState = null;
            if (mediaData.token != null) {
                MediaControllerFactory mediaControllerFactory = MediaTimeoutListener.this.mediaControllerFactory;
                MediaData mediaData2 = this.mediaData;
                Objects.requireNonNull(mediaData2);
                MediaSession.Token token = mediaData2.token;
                Objects.requireNonNull(mediaControllerFactory);
                mediaController = new MediaController(mediaControllerFactory.mContext, token);
            } else {
                mediaController = null;
            }
            this.mediaController = mediaController;
            if (mediaController != null) {
                mediaController.registerCallback(this);
            }
            MediaController mediaController3 = this.mediaController;
            if (mediaController3 != null) {
                playbackState = mediaController3.getPlaybackState();
            }
            processState(playbackState, false);
        }

        public PlaybackStateListener(String str, MediaData mediaData) {
            this.key = str;
            this.mediaData = mediaData;
            setMediaData(mediaData);
        }

        public final void expireMediaTimeout(String str, String str2) {
            Runnable runnable = this.cancellation;
            if (runnable != null) {
                Log.v("MediaTimeout", "media timeout cancelled for  " + str + ", reason: " + str2);
                runnable.run();
            }
            this.cancellation = null;
        }

        @Override // android.media.session.MediaController.Callback
        public final void onSessionDestroyed() {
            Log.d("MediaTimeout", Intrinsics.stringPlus("Session destroyed for ", this.key));
            if (Intrinsics.areEqual(this.resumption, Boolean.TRUE)) {
                MediaController mediaController = this.mediaController;
                if (mediaController != null) {
                    mediaController.unregisterCallback(this);
                    return;
                }
                return;
            }
            MediaController mediaController2 = this.mediaController;
            if (mediaController2 != null) {
                mediaController2.unregisterCallback(this);
            }
            Runnable runnable = this.cancellation;
            if (runnable != null) {
                runnable.run();
            }
            this.destroyed = true;
        }

        public final void processState(PlaybackState playbackState, boolean z) {
            boolean z2;
            long j;
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("processState ");
            m.append(this.key);
            m.append(": ");
            m.append(playbackState);
            Log.v("MediaTimeout", m.toString());
            if (playbackState == null || !NotificationMediaManager.isPlayingState(playbackState.getState())) {
                z2 = false;
            } else {
                z2 = true;
            }
            Boolean bool = this.resumption;
            MediaData mediaData = this.mediaData;
            Objects.requireNonNull(mediaData);
            boolean areEqual = true ^ Intrinsics.areEqual(bool, Boolean.valueOf(mediaData.resumption));
            if (!Intrinsics.areEqual(this.playing, Boolean.valueOf(z2)) || this.playing == null || areEqual) {
                this.playing = Boolean.valueOf(z2);
                MediaData mediaData2 = this.mediaData;
                Objects.requireNonNull(mediaData2);
                this.resumption = Boolean.valueOf(mediaData2.resumption);
                if (!z2) {
                    StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("schedule timeout for ");
                    m2.append(this.key);
                    m2.append(" playing ");
                    m2.append(z2);
                    m2.append(", ");
                    m2.append(this.resumption);
                    Log.v("MediaTimeout", m2.toString());
                    if (this.cancellation == null || areEqual) {
                        expireMediaTimeout(this.key, "PLAYBACK STATE CHANGED - " + playbackState + ", " + this.resumption);
                        MediaData mediaData3 = this.mediaData;
                        Objects.requireNonNull(mediaData3);
                        if (mediaData3.resumption) {
                            j = MediaTimeoutListenerKt.RESUME_MEDIA_TIMEOUT;
                        } else {
                            j = MediaTimeoutListenerKt.PAUSED_MEDIA_TIMEOUT;
                        }
                        final MediaTimeoutListener mediaTimeoutListener = MediaTimeoutListener.this;
                        this.cancellation = mediaTimeoutListener.mainExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.media.MediaTimeoutListener$PlaybackStateListener$processState$1
                            @Override // java.lang.Runnable
                            public final void run() {
                                MediaTimeoutListener.PlaybackStateListener playbackStateListener = MediaTimeoutListener.PlaybackStateListener.this;
                                Function2<? super String, ? super Boolean, Unit> function2 = null;
                                playbackStateListener.cancellation = null;
                                Log.v("MediaTimeout", Intrinsics.stringPlus("Execute timeout for ", playbackStateListener.key));
                                MediaTimeoutListener.PlaybackStateListener playbackStateListener2 = MediaTimeoutListener.PlaybackStateListener.this;
                                Objects.requireNonNull(playbackStateListener2);
                                playbackStateListener2.timedOut = true;
                                MediaTimeoutListener mediaTimeoutListener2 = mediaTimeoutListener;
                                Objects.requireNonNull(mediaTimeoutListener2);
                                Function2<? super String, ? super Boolean, Unit> function22 = mediaTimeoutListener2.timeoutCallback;
                                if (function22 != null) {
                                    function2 = function22;
                                }
                                MediaTimeoutListener.PlaybackStateListener playbackStateListener3 = MediaTimeoutListener.PlaybackStateListener.this;
                                Objects.requireNonNull(playbackStateListener3);
                                String str = playbackStateListener3.key;
                                MediaTimeoutListener.PlaybackStateListener playbackStateListener4 = MediaTimeoutListener.PlaybackStateListener.this;
                                Objects.requireNonNull(playbackStateListener4);
                                function2.invoke(str, Boolean.valueOf(playbackStateListener4.timedOut));
                            }
                        }, j);
                        return;
                    }
                    Log.d("MediaTimeout", "cancellation already exists, continuing.");
                    return;
                }
                expireMediaTimeout(this.key, "playback started - " + playbackState + ", " + this.key);
                this.timedOut = false;
                if (z) {
                    MediaTimeoutListener mediaTimeoutListener2 = MediaTimeoutListener.this;
                    Objects.requireNonNull(mediaTimeoutListener2);
                    Function2<? super String, ? super Boolean, Unit> function2 = mediaTimeoutListener2.timeoutCallback;
                    if (function2 == null) {
                        function2 = null;
                    }
                    function2.invoke(this.key, Boolean.valueOf(this.timedOut));
                }
            }
        }
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public final void onSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData, boolean z, boolean z2) {
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public final void onSmartspaceMediaDataRemoved(String str, boolean z) {
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public final void onMediaDataLoaded(final String str, String str2, MediaData mediaData, boolean z, int i) {
        Object obj;
        boolean z2;
        PlaybackStateListener playbackStateListener = (PlaybackStateListener) this.mediaListeners.get(str);
        if (playbackStateListener == null) {
            obj = null;
        } else if (playbackStateListener.destroyed) {
            Log.d("MediaTimeout", Intrinsics.stringPlus("Reusing destroyed listener ", str));
            obj = playbackStateListener;
        } else {
            return;
        }
        boolean z3 = false;
        if (str2 == null || Intrinsics.areEqual(str, str2)) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (z2) {
            obj = this.mediaListeners.remove(str2);
            if (obj != null) {
                Log.d("MediaTimeout", "migrating key " + ((Object) str2) + " to " + str + ", for resumption");
            } else {
                Log.w("MediaTimeout", "Old key " + ((Object) str2) + " for player " + str + " doesn't exist. Continuing...");
            }
        }
        PlaybackStateListener playbackStateListener2 = (PlaybackStateListener) obj;
        if (playbackStateListener2 == null) {
            this.mediaListeners.put(str, new PlaybackStateListener(str, mediaData));
            return;
        }
        Boolean bool = playbackStateListener2.playing;
        if (bool != null) {
            z3 = bool.booleanValue();
        }
        Log.d("MediaTimeout", "updating listener for " + str + ", was playing? " + z3);
        playbackStateListener2.setMediaData(mediaData);
        playbackStateListener2.key = str;
        this.mediaListeners.put(str, playbackStateListener2);
        if (!Intrinsics.areEqual(Boolean.valueOf(z3), playbackStateListener2.playing)) {
            this.mainExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaTimeoutListener$onMediaDataLoaded$2$1
                @Override // java.lang.Runnable
                public final void run() {
                    boolean z4;
                    MediaTimeoutListener.PlaybackStateListener playbackStateListener3 = (MediaTimeoutListener.PlaybackStateListener) MediaTimeoutListener.this.mediaListeners.get(str);
                    if (playbackStateListener3 == null) {
                        z4 = false;
                    } else {
                        z4 = Intrinsics.areEqual(playbackStateListener3.playing, Boolean.TRUE);
                    }
                    if (z4) {
                        Log.d("MediaTimeout", Intrinsics.stringPlus("deliver delayed playback state for ", str));
                        MediaTimeoutListener mediaTimeoutListener = MediaTimeoutListener.this;
                        Objects.requireNonNull(mediaTimeoutListener);
                        Function2<? super String, ? super Boolean, Unit> function2 = mediaTimeoutListener.timeoutCallback;
                        if (function2 == null) {
                            function2 = null;
                        }
                        function2.invoke(str, Boolean.FALSE);
                    }
                }
            });
        }
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public final void onMediaDataRemoved(String str) {
        PlaybackStateListener playbackStateListener = (PlaybackStateListener) this.mediaListeners.remove(str);
        if (playbackStateListener != null) {
            MediaController mediaController = playbackStateListener.mediaController;
            if (mediaController != null) {
                mediaController.unregisterCallback(playbackStateListener);
            }
            Runnable runnable = playbackStateListener.cancellation;
            if (runnable != null) {
                runnable.run();
            }
            playbackStateListener.destroyed = true;
        }
    }

    public MediaTimeoutListener(MediaControllerFactory mediaControllerFactory, DelayableExecutor delayableExecutor) {
        this.mediaControllerFactory = mediaControllerFactory;
        this.mainExecutor = delayableExecutor;
    }
}
