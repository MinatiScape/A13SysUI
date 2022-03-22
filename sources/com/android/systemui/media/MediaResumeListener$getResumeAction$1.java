package com.android.systemui.media;

import android.content.ComponentName;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.media.browse.MediaBrowser;
import android.media.session.MediaController;
import android.os.Bundle;
import android.util.Log;
import java.util.Objects;
/* compiled from: MediaResumeListener.kt */
/* loaded from: classes.dex */
public final class MediaResumeListener$getResumeAction$1 implements Runnable {
    public final /* synthetic */ ComponentName $componentName;
    public final /* synthetic */ MediaResumeListener this$0;

    public MediaResumeListener$getResumeAction$1(MediaResumeListener mediaResumeListener, ComponentName componentName) {
        this.this$0 = mediaResumeListener;
        this.$componentName = componentName;
    }

    @Override // java.lang.Runnable
    public final void run() {
        MediaResumeListener mediaResumeListener = this.this$0;
        ResumeMediaBrowserFactory resumeMediaBrowserFactory = mediaResumeListener.mediaBrowserFactory;
        ComponentName componentName = this.$componentName;
        Objects.requireNonNull(resumeMediaBrowserFactory);
        mediaResumeListener.mediaBrowser = new ResumeMediaBrowser(resumeMediaBrowserFactory.mContext, null, componentName, resumeMediaBrowserFactory.mBrowserFactory);
        final ResumeMediaBrowser resumeMediaBrowser = this.this$0.mediaBrowser;
        if (resumeMediaBrowser != null) {
            resumeMediaBrowser.disconnect();
            Bundle bundle = new Bundle();
            bundle.putBoolean("android.service.media.extra.RECENT", true);
            MediaBrowserFactory mediaBrowserFactory = resumeMediaBrowser.mBrowserFactory;
            ComponentName componentName2 = resumeMediaBrowser.mComponentName;
            MediaBrowser.ConnectionCallback connectionCallback = new MediaBrowser.ConnectionCallback() { // from class: com.android.systemui.media.ResumeMediaBrowser.3
                @Override // android.media.browse.MediaBrowser.ConnectionCallback
                public final void onConnected() {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Connected for restart ");
                    m.append(resumeMediaBrowser.mMediaBrowser.isConnected());
                    Log.d("ResumeMediaBrowser", m.toString());
                    MediaBrowser mediaBrowser = resumeMediaBrowser.mMediaBrowser;
                    if (mediaBrowser == null || !mediaBrowser.isConnected()) {
                        Callback callback = resumeMediaBrowser.mCallback;
                        if (callback != null) {
                            callback.onError();
                        }
                        resumeMediaBrowser.disconnect();
                        return;
                    }
                    MediaController createMediaController = resumeMediaBrowser.createMediaController(resumeMediaBrowser.mMediaBrowser.getSessionToken());
                    createMediaController.getTransportControls();
                    createMediaController.getTransportControls().prepare();
                    createMediaController.getTransportControls().play();
                    Callback callback2 = resumeMediaBrowser.mCallback;
                    if (callback2 != null) {
                        callback2.onConnected();
                    }
                }

                @Override // android.media.browse.MediaBrowser.ConnectionCallback
                public final void onConnectionFailed() {
                    Callback callback = resumeMediaBrowser.mCallback;
                    if (callback != null) {
                        callback.onError();
                    }
                    resumeMediaBrowser.disconnect();
                }

                @Override // android.media.browse.MediaBrowser.ConnectionCallback
                public final void onConnectionSuspended() {
                    Callback callback = resumeMediaBrowser.mCallback;
                    if (callback != null) {
                        callback.onError();
                    }
                    resumeMediaBrowser.disconnect();
                }
            };
            Objects.requireNonNull(mediaBrowserFactory);
            MediaBrowser mediaBrowser = new MediaBrowser(mediaBrowserFactory.mContext, componentName2, connectionCallback, bundle);
            resumeMediaBrowser.mMediaBrowser = mediaBrowser;
            mediaBrowser.connect();
        }
    }
}
