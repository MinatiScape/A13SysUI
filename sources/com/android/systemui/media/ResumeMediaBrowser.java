package com.android.systemui.media;

import android.content.ComponentName;
import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.media.MediaDescription;
import android.media.browse.MediaBrowser;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.util.List;
/* loaded from: classes.dex */
public final class ResumeMediaBrowser {
    public MediaBrowserFactory mBrowserFactory;
    public final Callback mCallback;
    public ComponentName mComponentName;
    public final Context mContext;
    public MediaBrowser mMediaBrowser;
    public final AnonymousClass1 mSubscriptionCallback = new MediaBrowser.SubscriptionCallback() { // from class: com.android.systemui.media.ResumeMediaBrowser.1
        @Override // android.media.browse.MediaBrowser.SubscriptionCallback
        public final void onError(String str) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Subscribe error for ");
            m.append(ResumeMediaBrowser.this.mComponentName);
            m.append(": ");
            m.append(str);
            Log.d("ResumeMediaBrowser", m.toString());
            Callback callback = ResumeMediaBrowser.this.mCallback;
            if (callback != null) {
                callback.onError();
            }
            ResumeMediaBrowser.this.disconnect();
        }

        @Override // android.media.browse.MediaBrowser.SubscriptionCallback
        public final void onChildrenLoaded(String str, List<MediaBrowser.MediaItem> list) {
            ResumeMediaBrowser resumeMediaBrowser;
            MediaBrowser mediaBrowser;
            if (list.size() == 0) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("No children found for ");
                m.append(ResumeMediaBrowser.this.mComponentName);
                Log.d("ResumeMediaBrowser", m.toString());
                Callback callback = ResumeMediaBrowser.this.mCallback;
                if (callback != null) {
                    callback.onError();
                }
            } else {
                MediaBrowser.MediaItem mediaItem = list.get(0);
                MediaDescription description = mediaItem.getDescription();
                if (!mediaItem.isPlayable() || (mediaBrowser = (resumeMediaBrowser = ResumeMediaBrowser.this).mMediaBrowser) == null) {
                    StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Child found but not playable for ");
                    m2.append(ResumeMediaBrowser.this.mComponentName);
                    Log.d("ResumeMediaBrowser", m2.toString());
                    Callback callback2 = ResumeMediaBrowser.this.mCallback;
                    if (callback2 != null) {
                        callback2.onError();
                    }
                } else {
                    Callback callback3 = resumeMediaBrowser.mCallback;
                    if (callback3 != null) {
                        callback3.addTrack(description, mediaBrowser.getServiceComponent(), ResumeMediaBrowser.this);
                    }
                }
            }
            ResumeMediaBrowser.this.disconnect();
        }

        @Override // android.media.browse.MediaBrowser.SubscriptionCallback
        public final void onError(String str, Bundle bundle) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Subscribe error for ");
            m.append(ResumeMediaBrowser.this.mComponentName);
            m.append(": ");
            m.append(str);
            m.append(", options: ");
            m.append(bundle);
            Log.d("ResumeMediaBrowser", m.toString());
            Callback callback = ResumeMediaBrowser.this.mCallback;
            if (callback != null) {
                callback.onError();
            }
            ResumeMediaBrowser.this.disconnect();
        }
    };
    public final AnonymousClass2 mConnectionCallback = new MediaBrowser.ConnectionCallback() { // from class: com.android.systemui.media.ResumeMediaBrowser.2
        @Override // android.media.browse.MediaBrowser.ConnectionCallback
        public final void onConnected() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Service connected for ");
            m.append(ResumeMediaBrowser.this.mComponentName);
            Log.d("ResumeMediaBrowser", m.toString());
            MediaBrowser mediaBrowser = ResumeMediaBrowser.this.mMediaBrowser;
            if (mediaBrowser != null && mediaBrowser.isConnected()) {
                String root = ResumeMediaBrowser.this.mMediaBrowser.getRoot();
                if (!TextUtils.isEmpty(root)) {
                    Callback callback = ResumeMediaBrowser.this.mCallback;
                    if (callback != null) {
                        callback.onConnected();
                    }
                    ResumeMediaBrowser resumeMediaBrowser = ResumeMediaBrowser.this;
                    MediaBrowser mediaBrowser2 = resumeMediaBrowser.mMediaBrowser;
                    if (mediaBrowser2 != null) {
                        mediaBrowser2.subscribe(root, resumeMediaBrowser.mSubscriptionCallback);
                        return;
                    }
                    return;
                }
            }
            Callback callback2 = ResumeMediaBrowser.this.mCallback;
            if (callback2 != null) {
                callback2.onError();
            }
            ResumeMediaBrowser.this.disconnect();
        }

        @Override // android.media.browse.MediaBrowser.ConnectionCallback
        public final void onConnectionFailed() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Connection failed for ");
            m.append(ResumeMediaBrowser.this.mComponentName);
            Log.d("ResumeMediaBrowser", m.toString());
            Callback callback = ResumeMediaBrowser.this.mCallback;
            if (callback != null) {
                callback.onError();
            }
            ResumeMediaBrowser.this.disconnect();
        }

        @Override // android.media.browse.MediaBrowser.ConnectionCallback
        public final void onConnectionSuspended() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Connection suspended for ");
            m.append(ResumeMediaBrowser.this.mComponentName);
            Log.d("ResumeMediaBrowser", m.toString());
            Callback callback = ResumeMediaBrowser.this.mCallback;
            if (callback != null) {
                callback.onError();
            }
            ResumeMediaBrowser.this.disconnect();
        }
    };

    /* loaded from: classes.dex */
    public static class Callback {
        public void addTrack(MediaDescription mediaDescription, ComponentName componentName, ResumeMediaBrowser resumeMediaBrowser) {
            throw null;
        }

        public void onConnected() {
        }

        public void onError() {
        }
    }

    @VisibleForTesting
    public MediaController createMediaController(MediaSession.Token token) {
        return new MediaController(this.mContext, token);
    }

    public final void disconnect() {
        MediaBrowser mediaBrowser = this.mMediaBrowser;
        if (mediaBrowser != null) {
            mediaBrowser.disconnect();
        }
        this.mMediaBrowser = null;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.media.ResumeMediaBrowser$1] */
    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.systemui.media.ResumeMediaBrowser$2] */
    public ResumeMediaBrowser(Context context, Callback callback, ComponentName componentName, MediaBrowserFactory mediaBrowserFactory) {
        this.mContext = context;
        this.mCallback = callback;
        this.mComponentName = componentName;
        this.mBrowserFactory = mediaBrowserFactory;
    }
}
