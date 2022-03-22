package com.android.wm.shell.pip;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.RemoteAction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.os.Handler;
import android.os.HandlerExecutor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class PipMediaController {
    public final Context mContext;
    public final HandlerExecutor mHandlerExecutor;
    public final Handler mMainHandler;
    public final AnonymousClass1 mMediaActionReceiver;
    public MediaController mMediaController;
    public final MediaSessionManager mMediaSessionManager;
    public final AnonymousClass2 mPlaybackChangedListener = new MediaController.Callback() { // from class: com.android.wm.shell.pip.PipMediaController.2
        @Override // android.media.session.MediaController.Callback
        public final void onMetadataChanged(MediaMetadata mediaMetadata) {
            PipMediaController pipMediaController = PipMediaController.this;
            Objects.requireNonNull(pipMediaController);
            if (!pipMediaController.mMetadataListeners.isEmpty()) {
                pipMediaController.mMetadataListeners.forEach(new PipMediaController$$ExternalSyntheticLambda2(mediaMetadata, 0));
            }
        }

        @Override // android.media.session.MediaController.Callback
        public final void onPlaybackStateChanged(PlaybackState playbackState) {
            PipMediaController pipMediaController = PipMediaController.this;
            Objects.requireNonNull(pipMediaController);
            if (!pipMediaController.mActionListeners.isEmpty()) {
                pipMediaController.mActionListeners.forEach(new PipMediaController$$ExternalSyntheticLambda1(pipMediaController.getMediaActions(), 0));
            }
        }
    };
    public final PipMediaController$$ExternalSyntheticLambda0 mSessionsChangedListener = new MediaSessionManager.OnActiveSessionsChangedListener() { // from class: com.android.wm.shell.pip.PipMediaController$$ExternalSyntheticLambda0
        @Override // android.media.session.MediaSessionManager.OnActiveSessionsChangedListener
        public final void onActiveSessionsChanged(List list) {
            PipMediaController.this.resolveActiveMediaController(list);
        }
    };
    public final ArrayList<ActionListener> mActionListeners = new ArrayList<>();
    public final ArrayList<MetadataListener> mMetadataListeners = new ArrayList<>();
    public RemoteAction mPauseAction = getDefaultRemoteAction(2131952983, 2131232561, "com.android.wm.shell.pip.PAUSE");
    public RemoteAction mPlayAction = getDefaultRemoteAction(2131952990, 2131232562, "com.android.wm.shell.pip.PLAY");
    public RemoteAction mNextAction = getDefaultRemoteAction(2131952992, 2131232564, "com.android.wm.shell.pip.NEXT");
    public RemoteAction mPrevAction = getDefaultRemoteAction(2131952993, 2131232565, "com.android.wm.shell.pip.PREV");

    /* loaded from: classes.dex */
    public interface ActionListener {
        void onMediaActionsChanged(List<RemoteAction> list);
    }

    /* loaded from: classes.dex */
    public interface MetadataListener {
        void onMediaMetadataChanged(MediaMetadata mediaMetadata);
    }

    public final RemoteAction getDefaultRemoteAction(int i, int i2, String str) {
        String string = this.mContext.getString(i);
        Intent intent = new Intent(str);
        intent.setPackage(this.mContext.getPackageName());
        return new RemoteAction(Icon.createWithResource(this.mContext, i2), string, string, PendingIntent.getBroadcast(this.mContext, 0, intent, 201326592));
    }

    @SuppressLint({"NewApi"})
    public final List<RemoteAction> getMediaActions() {
        boolean z;
        MediaController mediaController = this.mMediaController;
        if (mediaController == null || mediaController.getPlaybackState() == null) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        boolean isActive = this.mMediaController.getPlaybackState().isActive();
        long actions = this.mMediaController.getPlaybackState().getActions();
        RemoteAction remoteAction = this.mPrevAction;
        boolean z2 = true;
        if ((16 & actions) != 0) {
            z = true;
        } else {
            z = false;
        }
        remoteAction.setEnabled(z);
        arrayList.add(this.mPrevAction);
        if (!isActive && (4 & actions) != 0) {
            arrayList.add(this.mPlayAction);
        } else if (isActive && (2 & actions) != 0) {
            arrayList.add(this.mPauseAction);
        }
        RemoteAction remoteAction2 = this.mNextAction;
        if ((actions & 32) == 0) {
            z2 = false;
        }
        remoteAction2.setEnabled(z2);
        arrayList.add(this.mNextAction);
        return arrayList;
    }

    public final void resolveActiveMediaController(List<MediaController> list) {
        ComponentName componentName;
        if (!(list == null || (componentName = (ComponentName) PipUtils.getTopPipActivity(this.mContext).first) == null)) {
            for (int i = 0; i < list.size(); i++) {
                MediaController mediaController = list.get(i);
                if (mediaController.getPackageName().equals(componentName.getPackageName())) {
                    setActiveMediaController(mediaController);
                    return;
                }
            }
        }
        setActiveMediaController(null);
    }

    public final void setActiveMediaController(MediaController mediaController) {
        MediaMetadata mediaMetadata;
        MediaController mediaController2 = this.mMediaController;
        if (mediaController != mediaController2) {
            if (mediaController2 != null) {
                mediaController2.unregisterCallback(this.mPlaybackChangedListener);
            }
            this.mMediaController = mediaController;
            if (mediaController != null) {
                mediaController.registerCallback(this.mPlaybackChangedListener, this.mMainHandler);
            }
            if (!this.mActionListeners.isEmpty()) {
                this.mActionListeners.forEach(new PipMediaController$$ExternalSyntheticLambda1(getMediaActions(), 0));
            }
            MediaController mediaController3 = this.mMediaController;
            if (mediaController3 != null) {
                mediaMetadata = mediaController3.getMetadata();
            } else {
                mediaMetadata = null;
            }
            if (!this.mMetadataListeners.isEmpty()) {
                this.mMetadataListeners.forEach(new PipMediaController$$ExternalSyntheticLambda2(mediaMetadata, 0));
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.wm.shell.pip.PipMediaController$2] */
    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.wm.shell.pip.PipMediaController$$ExternalSyntheticLambda0] */
    /* JADX WARN: Type inference failed for: r1v0, types: [com.android.wm.shell.pip.PipMediaController$1, android.content.BroadcastReceiver] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public PipMediaController(android.content.Context r11, android.os.Handler r12) {
        /*
            r10 = this;
            r10.<init>()
            com.android.wm.shell.pip.PipMediaController$1 r1 = new com.android.wm.shell.pip.PipMediaController$1
            r1.<init>()
            r10.mMediaActionReceiver = r1
            com.android.wm.shell.pip.PipMediaController$2 r0 = new com.android.wm.shell.pip.PipMediaController$2
            r0.<init>()
            r10.mPlaybackChangedListener = r0
            com.android.wm.shell.pip.PipMediaController$$ExternalSyntheticLambda0 r0 = new com.android.wm.shell.pip.PipMediaController$$ExternalSyntheticLambda0
            r0.<init>()
            r10.mSessionsChangedListener = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r10.mActionListeners = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r10.mMetadataListeners = r0
            r10.mContext = r11
            r10.mMainHandler = r12
            android.os.HandlerExecutor r0 = new android.os.HandlerExecutor
            r0.<init>(r12)
            r10.mHandlerExecutor = r0
            android.content.IntentFilter r2 = new android.content.IntentFilter
            r2.<init>()
            java.lang.String r6 = "com.android.wm.shell.pip.PLAY"
            r2.addAction(r6)
            java.lang.String r7 = "com.android.wm.shell.pip.PAUSE"
            r2.addAction(r7)
            java.lang.String r8 = "com.android.wm.shell.pip.NEXT"
            r2.addAction(r8)
            java.lang.String r9 = "com.android.wm.shell.pip.PREV"
            r2.addAction(r9)
            java.lang.String r3 = "com.android.systemui.permission.SELF"
            r5 = 2
            r0 = r11
            r4 = r12
            r0.registerReceiverForAllUsers(r1, r2, r3, r4, r5)
            r12 = 2131952983(0x7f130557, float:1.9542424E38)
            r0 = 2131232561(0x7f080731, float:1.8081235E38)
            android.app.RemoteAction r12 = r10.getDefaultRemoteAction(r12, r0, r7)
            r10.mPauseAction = r12
            r12 = 2131952990(0x7f13055e, float:1.9542438E38)
            r0 = 2131232562(0x7f080732, float:1.8081237E38)
            android.app.RemoteAction r12 = r10.getDefaultRemoteAction(r12, r0, r6)
            r10.mPlayAction = r12
            r12 = 2131952992(0x7f130560, float:1.9542442E38)
            r0 = 2131232564(0x7f080734, float:1.808124E38)
            android.app.RemoteAction r12 = r10.getDefaultRemoteAction(r12, r0, r8)
            r10.mNextAction = r12
            r12 = 2131952993(0x7f130561, float:1.9542444E38)
            r0 = 2131232565(0x7f080735, float:1.8081243E38)
            android.app.RemoteAction r12 = r10.getDefaultRemoteAction(r12, r0, r9)
            r10.mPrevAction = r12
            java.lang.Class<android.media.session.MediaSessionManager> r12 = android.media.session.MediaSessionManager.class
            java.lang.Object r11 = r11.getSystemService(r12)
            android.media.session.MediaSessionManager r11 = (android.media.session.MediaSessionManager) r11
            r10.mMediaSessionManager = r11
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.pip.PipMediaController.<init>(android.content.Context, android.os.Handler):void");
    }
}
