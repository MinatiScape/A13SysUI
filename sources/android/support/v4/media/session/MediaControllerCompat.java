package android.support.v4.media.session;

import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.IMediaControllerCallback;
import android.support.v4.media.session.IMediaSession;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import androidx.versionedparcelable.ParcelImpl;
import androidx.versionedparcelable.ParcelUtils;
import androidx.versionedparcelable.VersionedParcelable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MediaControllerCompat {
    public final MediaControllerImplApi21 mImpl;

    /* loaded from: classes.dex */
    public static abstract class Callback implements IBinder.DeathRecipient {
        public final MediaControllerCallbackApi21 mCallbackFwk = new MediaControllerCallbackApi21(this);
        public MessageHandler mHandler;
        public MediaControllerImplApi21.ExtraCallback mIControllerCallback;

        /* loaded from: classes.dex */
        public static class MediaControllerCallbackApi21 extends MediaController.Callback {
            public final WeakReference<Callback> mCallback;

            @Override // android.media.session.MediaController.Callback
            public final void onAudioInfoChanged(MediaController.PlaybackInfo playbackInfo) {
                if (this.mCallback.get() != null) {
                    playbackInfo.getPlaybackType();
                    playbackInfo.getAudioAttributes();
                    playbackInfo.getVolumeControl();
                    playbackInfo.getMaxVolume();
                    playbackInfo.getCurrentVolume();
                }
            }

            @Override // android.media.session.MediaController.Callback
            public final void onMetadataChanged(MediaMetadata mediaMetadata) {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.onMetadataChanged(MediaMetadataCompat.fromMediaMetadata(mediaMetadata));
                }
            }

            @Override // android.media.session.MediaController.Callback
            public final void onPlaybackStateChanged(PlaybackState playbackState) {
                Callback callback = this.mCallback.get();
                if (callback != null && callback.mIControllerCallback == null) {
                    callback.onPlaybackStateChanged(PlaybackStateCompat.fromPlaybackState(playbackState));
                }
            }

            @Override // android.media.session.MediaController.Callback
            public final void onQueueChanged(List<MediaSession.QueueItem> list) {
                MediaSessionCompat.QueueItem queueItem;
                if (!(this.mCallback.get() == null || list == null)) {
                    ArrayList arrayList = new ArrayList();
                    for (MediaSession.QueueItem queueItem2 : list) {
                        if (queueItem2 != null) {
                            MediaSession.QueueItem queueItem3 = queueItem2;
                            queueItem = new MediaSessionCompat.QueueItem(MediaDescriptionCompat.fromMediaDescription(queueItem3.getDescription()), queueItem3.getQueueId());
                        } else {
                            queueItem = null;
                        }
                        arrayList.add(queueItem);
                    }
                }
            }

            @Override // android.media.session.MediaController.Callback
            public final void onQueueTitleChanged(CharSequence charSequence) {
                this.mCallback.get();
            }

            @Override // android.media.session.MediaController.Callback
            public final void onSessionDestroyed() {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.onSessionDestroyed();
                }
            }

            public MediaControllerCallbackApi21(Callback callback) {
                this.mCallback = new WeakReference<>(callback);
            }

            @Override // android.media.session.MediaController.Callback
            public final void onExtrasChanged(Bundle bundle) {
                MediaSessionCompat.ensureClassLoader(bundle);
                this.mCallback.get();
            }

            @Override // android.media.session.MediaController.Callback
            public final void onSessionEvent(String str, Bundle bundle) {
                MediaSessionCompat.ensureClassLoader(bundle);
                this.mCallback.get();
            }
        }

        /* loaded from: classes.dex */
        public class MessageHandler extends Handler {
        }

        public void onMetadataChanged(MediaMetadataCompat mediaMetadataCompat) {
        }

        public void onPlaybackStateChanged(PlaybackStateCompat playbackStateCompat) {
        }

        public void onSessionDestroyed() {
        }

        /* loaded from: classes.dex */
        public static class StubCompat extends IMediaControllerCallback.Stub {
            public final WeakReference<Callback> mCallback;

            public StubCompat(Callback callback) {
                this.mCallback = new WeakReference<>(callback);
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public final void binderDied() {
            postToHandler(8, null, null);
        }

        public final void postToHandler(int i, Object obj, Bundle bundle) {
            MessageHandler messageHandler = this.mHandler;
            if (messageHandler != null) {
                Message obtainMessage = messageHandler.obtainMessage(i, obj);
                obtainMessage.setData(bundle);
                obtainMessage.sendToTarget();
            }
        }
    }

    /* loaded from: classes.dex */
    public static class MediaControllerImplApi21 {
        public HashMap<Callback, ExtraCallback> mCallbackMap;
        public final MediaController mControllerFwk;
        public final Object mLock;
        public final ArrayList mPendingCallbacks;
        public final MediaSessionCompat.Token mSessionToken;

        /* loaded from: classes.dex */
        public static class ExtraBinderRequestResultReceiver extends ResultReceiver {
            public WeakReference<MediaControllerImplApi21> mMediaControllerImpl;

            @Override // android.os.ResultReceiver
            public final void onReceiveResult(int i, Bundle bundle) {
                IMediaSession iMediaSession;
                MediaControllerImplApi21 mediaControllerImplApi21 = this.mMediaControllerImpl.get();
                if (mediaControllerImplApi21 != null && bundle != null) {
                    synchronized (mediaControllerImplApi21.mLock) {
                        MediaSessionCompat.Token token = mediaControllerImplApi21.mSessionToken;
                        IBinder binder = bundle.getBinder("android.support.v4.media.session.EXTRA_BINDER");
                        int i2 = IMediaSession.Stub.$r8$clinit;
                        VersionedParcelable versionedParcelable = null;
                        if (binder == null) {
                            iMediaSession = null;
                        } else {
                            IInterface queryLocalInterface = binder.queryLocalInterface("android.support.v4.media.session.IMediaSession");
                            if (queryLocalInterface == null || !(queryLocalInterface instanceof IMediaSession)) {
                                iMediaSession = new IMediaSession.Stub.Proxy(binder);
                            } else {
                                iMediaSession = (IMediaSession) queryLocalInterface;
                            }
                        }
                        Objects.requireNonNull(token);
                        synchronized (token.mLock) {
                            token.mExtraBinder = iMediaSession;
                        }
                        MediaSessionCompat.Token token2 = mediaControllerImplApi21.mSessionToken;
                        try {
                            Bundle bundle2 = (Bundle) bundle.getParcelable("android.support.v4.media.session.SESSION_TOKEN2");
                            if (bundle2 != null) {
                                bundle2.setClassLoader(ParcelUtils.class.getClassLoader());
                                Parcelable parcelable = bundle2.getParcelable("a");
                                if (parcelable instanceof ParcelImpl) {
                                    ParcelImpl parcelImpl = (ParcelImpl) parcelable;
                                    Objects.requireNonNull(parcelImpl);
                                    versionedParcelable = parcelImpl.mParcel;
                                } else {
                                    throw new IllegalArgumentException("Invalid parcel");
                                }
                            }
                        } catch (RuntimeException unused) {
                        }
                        Objects.requireNonNull(token2);
                        synchronized (token2.mLock) {
                            token2.mSession2Token = versionedParcelable;
                        }
                        mediaControllerImplApi21.processPendingCallbacksLocked();
                    }
                }
            }
        }

        /* loaded from: classes.dex */
        public static class ExtraCallback extends Callback.StubCompat {
            @Override // android.support.v4.media.session.IMediaControllerCallback
            public final void onExtrasChanged(Bundle bundle) throws RemoteException {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public final void onMetadataChanged(MediaMetadataCompat mediaMetadataCompat) throws RemoteException {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public final void onQueueChanged(ArrayList arrayList) throws RemoteException {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public final void onQueueTitleChanged(CharSequence charSequence) throws RemoteException {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public final void onSessionDestroyed() throws RemoteException {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public final void onVolumeInfoChanged(ParcelableVolumeInfo parcelableVolumeInfo) throws RemoteException {
                throw new AssertionError();
            }

            public ExtraCallback(Callback callback) {
                super(callback);
            }
        }

        public final void processPendingCallbacksLocked() {
            if (this.mSessionToken.getExtraBinder() != null) {
                Iterator it = this.mPendingCallbacks.iterator();
                while (it.hasNext()) {
                    Callback callback = (Callback) it.next();
                    ExtraCallback extraCallback = new ExtraCallback(callback);
                    this.mCallbackMap.put(callback, extraCallback);
                    callback.mIControllerCallback = extraCallback;
                    try {
                        this.mSessionToken.getExtraBinder().registerCallbackListener(extraCallback);
                        callback.postToHandler(13, null, null);
                    } catch (RemoteException e) {
                        Log.e("MediaControllerCompat", "Dead object in registerCallback.", e);
                    }
                }
                this.mPendingCallbacks.clear();
            }
        }
    }

    /* loaded from: classes.dex */
    public static abstract class TransportControls {
    }

    /* loaded from: classes.dex */
    public static class TransportControlsApi21 extends TransportControls {
        public final MediaController.TransportControls mControlsFwk;
    }

    /* loaded from: classes.dex */
    public static class TransportControlsApi23 extends TransportControlsApi21 {
    }

    /* loaded from: classes.dex */
    public static class TransportControlsApi24 extends TransportControlsApi23 {
    }

    /* loaded from: classes.dex */
    public static class TransportControlsApi29 extends TransportControlsApi24 {
    }

    public final TransportControlsApi29 getTransportControls() {
        throw null;
    }

    public final void unregisterCallback(Callback callback) {
        throw null;
    }
}
