package com.android.systemui.statusbar;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.AsyncTask;
import android.os.Trace;
import android.service.notification.StatusBarNotification;
import android.util.ArraySet;
import android.widget.ImageView;
import com.android.systemui.Dependency;
import com.android.systemui.Dumpable;
import com.android.systemui.SystemUIApplication$$ExternalSyntheticLambda3;
import com.android.systemui.SystemUIApplication$$ExternalSyntheticLambda4;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaData;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.SmartspaceMediaData;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.DismissedByUserStats;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.icon.IconPack;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.LockscreenWallpaper;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarNotificationPresenter;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.Utils;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda6;
import com.android.wifitrackerlib.SavedNetworkTracker$$ExternalSyntheticLambda1;
import dagger.Lazy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public final class NotificationMediaManager implements Dumpable {
    public static final HashSet<Integer> PAUSED_MEDIA_STATES;
    public BackDropView mBackdrop;
    public ImageView mBackdropBack;
    public ImageView mBackdropFront;
    public BiometricUnlockController mBiometricUnlockController;
    public final Context mContext;
    public final NotificationEntryManager mEntryManager;
    public final KeyguardBypassController mKeyguardBypassController;
    public LockscreenWallpaper mLockscreenWallpaper;
    public final DelayableExecutor mMainExecutor;
    public final MediaArtworkProcessor mMediaArtworkProcessor;
    public MediaController mMediaController;
    public final MediaDataManager mMediaDataManager;
    public MediaMetadata mMediaMetadata;
    public String mMediaNotificationKey;
    public final NotifCollection mNotifCollection;
    public final NotifPipeline mNotifPipeline;
    public Lazy<NotificationShadeWindowController> mNotificationShadeWindowController;
    public NotificationPresenter mPresenter;
    public ScrimController mScrimController;
    public final Lazy<Optional<StatusBar>> mStatusBarOptionalLazy;
    public final boolean mUsingNotifPipeline;
    public final NotificationVisibilityProvider mVisibilityProvider;
    public final StatusBarStateController mStatusBarStateController = (StatusBarStateController) Dependency.get(StatusBarStateController.class);
    public final SysuiColorExtractor mColorExtractor = (SysuiColorExtractor) Dependency.get(SysuiColorExtractor.class);
    public final KeyguardStateController mKeyguardStateController = (KeyguardStateController) Dependency.get(KeyguardStateController.class);
    public final ArraySet mProcessArtworkTasks = new ArraySet();
    public final AnonymousClass1 mMediaListener = new MediaController.Callback() { // from class: com.android.systemui.statusbar.NotificationMediaManager.1
        @Override // android.media.session.MediaController.Callback
        public final void onMetadataChanged(MediaMetadata mediaMetadata) {
            super.onMetadataChanged(mediaMetadata);
            Objects.requireNonNull(NotificationMediaManager.this.mMediaArtworkProcessor);
            NotificationMediaManager notificationMediaManager = NotificationMediaManager.this;
            notificationMediaManager.mMediaMetadata = mediaMetadata;
            notificationMediaManager.dispatchUpdateMediaMetaData(true);
        }

        @Override // android.media.session.MediaController.Callback
        public final void onPlaybackStateChanged(PlaybackState playbackState) {
            super.onPlaybackStateChanged(playbackState);
            if (playbackState != null) {
                NotificationMediaManager notificationMediaManager = NotificationMediaManager.this;
                int state = playbackState.getState();
                Objects.requireNonNull(notificationMediaManager);
                boolean z = true;
                if (state == 1 || state == 7 || state == 0) {
                    z = false;
                }
                if (!z) {
                    NotificationMediaManager notificationMediaManager2 = NotificationMediaManager.this;
                    Objects.requireNonNull(notificationMediaManager2);
                    notificationMediaManager2.mMediaNotificationKey = null;
                    Objects.requireNonNull(notificationMediaManager2.mMediaArtworkProcessor);
                    notificationMediaManager2.mMediaMetadata = null;
                    MediaController mediaController = notificationMediaManager2.mMediaController;
                    if (mediaController != null) {
                        mediaController.unregisterCallback(notificationMediaManager2.mMediaListener);
                    }
                    notificationMediaManager2.mMediaController = null;
                }
                NotificationMediaManager.this.findAndUpdateMediaNotifications();
            }
        }
    };
    public final AnonymousClass7 mHideBackdropFront = new Runnable() { // from class: com.android.systemui.statusbar.NotificationMediaManager.7
        @Override // java.lang.Runnable
        public final void run() {
            NotificationMediaManager.this.mBackdropFront.setVisibility(4);
            NotificationMediaManager.this.mBackdropFront.animate().cancel();
            NotificationMediaManager.this.mBackdropFront.setImageDrawable(null);
        }
    };
    public final ArrayList<MediaListener> mMediaListeners = new ArrayList<>();

    /* renamed from: com.android.systemui.statusbar.NotificationMediaManager$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass3 implements MediaDataManager.Listener {
        @Override // com.android.systemui.media.MediaDataManager.Listener
        public final void onMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z, int i) {
        }

        @Override // com.android.systemui.media.MediaDataManager.Listener
        public final void onSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData, boolean z, boolean z2) {
        }

        @Override // com.android.systemui.media.MediaDataManager.Listener
        public final void onSmartspaceMediaDataRemoved(String str, boolean z) {
        }

        public AnonymousClass3() {
        }

        @Override // com.android.systemui.media.MediaDataManager.Listener
        public final void onMediaDataRemoved(String str) {
            NotificationMediaManager.this.mNotifPipeline.getAllNotifs().stream().filter(new SavedNetworkTracker$$ExternalSyntheticLambda1(str, 1)).findAny().ifPresent(new WMShell$$ExternalSyntheticLambda6(this, 1));
        }
    }

    /* loaded from: classes.dex */
    public interface MediaListener {
        default void onPrimaryMetadataOrStateChanged(MediaMetadata mediaMetadata, int i) {
        }
    }

    /* loaded from: classes.dex */
    public static final class ProcessArtworkTask extends AsyncTask<Bitmap, Void, Bitmap> {
        public final boolean mAllowEnterAnimation;
        public final WeakReference<NotificationMediaManager> mManagerRef;
        public final boolean mMetaDataChanged;

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:44:0x00ef  */
        /* JADX WARN: Removed duplicated region for block: B:46:0x00f5  */
        /* JADX WARN: Removed duplicated region for block: B:49:0x00fe  */
        /* JADX WARN: Removed duplicated region for block: B:55:0x010c  */
        /* JADX WARN: Removed duplicated region for block: B:57:0x0112  */
        /* JADX WARN: Removed duplicated region for block: B:60:0x011b  */
        /* JADX WARN: Type inference failed for: r0v26 */
        /* JADX WARN: Type inference failed for: r0v27 */
        /* JADX WARN: Type inference failed for: r0v28 */
        /* JADX WARN: Type inference failed for: r0v3, types: [android.content.Context] */
        /* JADX WARN: Type inference failed for: r0v4 */
        /* JADX WARN: Type inference failed for: r0v8 */
        /* JADX WARN: Type inference failed for: r0v9 */
        /* JADX WARN: Type inference failed for: r1v14 */
        /* JADX WARN: Type inference failed for: r1v6 */
        /* JADX WARN: Type inference failed for: r1v7 */
        /* JADX WARN: Type inference failed for: r2v1, types: [java.lang.Object, com.android.systemui.statusbar.MediaArtworkProcessor] */
        /* JADX WARN: Type inference failed for: r2v17, types: [android.renderscript.Allocation] */
        /* JADX WARN: Type inference failed for: r2v19 */
        /* JADX WARN: Type inference failed for: r2v2 */
        /* JADX WARN: Type inference failed for: r2v20 */
        /* JADX WARN: Type inference failed for: r2v21 */
        /* JADX WARN: Type inference failed for: r2v4 */
        /* JADX WARN: Type inference failed for: r2v5 */
        /* JADX WARN: Type inference failed for: r2v6, types: [android.renderscript.Allocation] */
        /* JADX WARN: Type inference failed for: r2v7 */
        /* JADX WARN: Type inference failed for: r3v3, types: [android.renderscript.Allocation] */
        /* JADX WARN: Type inference failed for: r3v4 */
        /* JADX WARN: Type inference failed for: r3v5 */
        /* JADX WARN: Type inference failed for: r3v6, types: [android.renderscript.Allocation] */
        /* JADX WARN: Type inference failed for: r4v1, types: [android.renderscript.BaseObj, android.renderscript.ScriptIntrinsicBlur] */
        /* JADX WARN: Unknown variable types count: 4 */
        @Override // android.os.AsyncTask
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final android.graphics.Bitmap doInBackground(android.graphics.Bitmap[] r8) {
            /*
                Method dump skipped, instructions count: 288
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.NotificationMediaManager.ProcessArtworkTask.doInBackground(java.lang.Object[]):java.lang.Object");
        }

        @Override // android.os.AsyncTask
        public final void onCancelled(Bitmap bitmap) {
            Bitmap bitmap2 = bitmap;
            if (bitmap2 != null) {
                bitmap2.recycle();
            }
            NotificationMediaManager notificationMediaManager = this.mManagerRef.get();
            if (notificationMediaManager != null) {
                notificationMediaManager.mProcessArtworkTasks.remove(this);
            }
        }

        @Override // android.os.AsyncTask
        public final void onPostExecute(Bitmap bitmap) {
            Bitmap bitmap2 = bitmap;
            NotificationMediaManager notificationMediaManager = this.mManagerRef.get();
            if (notificationMediaManager != null && !isCancelled()) {
                notificationMediaManager.mProcessArtworkTasks.remove(this);
                notificationMediaManager.finishUpdateMediaMetaData(this.mMetaDataChanged, this.mAllowEnterAnimation, bitmap2);
            }
        }

        public ProcessArtworkTask(NotificationMediaManager notificationMediaManager, boolean z, boolean z2) {
            this.mManagerRef = new WeakReference<>(notificationMediaManager);
            this.mMetaDataChanged = z;
            this.mAllowEnterAnimation = z2;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:100:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:103:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x004c  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x008b  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x008d  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0097  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x010b  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0135  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0139  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x019d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void finishUpdateMediaMetaData(boolean r16, boolean r17, android.graphics.Bitmap r18) {
        /*
            Method dump skipped, instructions count: 549
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.NotificationMediaManager.finishUpdateMediaMetaData(boolean, boolean, android.graphics.Bitmap):void");
    }

    static {
        HashSet<Integer> hashSet = new HashSet<>();
        PAUSED_MEDIA_STATES = hashSet;
        hashSet.add(0);
        hashSet.add(1);
        hashSet.add(2);
        hashSet.add(7);
        hashSet.add(8);
    }

    public static boolean isPlayingState(int i) {
        return !PAUSED_MEDIA_STATES.contains(Integer.valueOf(i));
    }

    public final void addCallback(MediaListener mediaListener) {
        int i;
        PlaybackState playbackState;
        this.mMediaListeners.add(mediaListener);
        MediaMetadata mediaMetadata = this.mMediaMetadata;
        MediaController mediaController = this.mMediaController;
        if (mediaController == null || (playbackState = mediaController.getPlaybackState()) == null) {
            i = 0;
        } else {
            i = playbackState.getState();
        }
        mediaListener.onPrimaryMetadataOrStateChanged(mediaMetadata, i);
    }

    public final void dispatchUpdateMediaMetaData(boolean z) {
        int i;
        PlaybackState playbackState;
        NotificationPresenter notificationPresenter = this.mPresenter;
        if (notificationPresenter != null) {
            ((StatusBarNotificationPresenter) notificationPresenter).updateMediaMetaData(z, true);
        }
        MediaController mediaController = this.mMediaController;
        if (mediaController == null || (playbackState = mediaController.getPlaybackState()) == null) {
            i = 0;
        } else {
            i = playbackState.getState();
        }
        ArrayList arrayList = new ArrayList(this.mMediaListeners);
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            ((MediaListener) arrayList.get(i2)).onPrimaryMetadataOrStateChanged(this.mMediaMetadata, i);
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.print("    mMediaNotificationKey=");
        printWriter.println(this.mMediaNotificationKey);
        printWriter.print("    mMediaController=");
        printWriter.print(this.mMediaController);
        if (this.mMediaController != null) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m(" state=");
            m.append(this.mMediaController.getPlaybackState());
            printWriter.print(m.toString());
        }
        printWriter.println();
        printWriter.print("    mMediaMetadata=");
        printWriter.print(this.mMediaMetadata);
        if (this.mMediaMetadata != null) {
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m(" title=");
            m2.append((Object) this.mMediaMetadata.getText("android.media.metadata.TITLE"));
            printWriter.print(m2.toString());
        }
        printWriter.println();
    }

    public final void findAndUpdateMediaNotifications() {
        boolean z;
        boolean findPlayingMediaNotification;
        if (this.mUsingNotifPipeline) {
            z = findPlayingMediaNotification(this.mNotifPipeline.getAllNotifs());
        } else {
            synchronized (this.mEntryManager) {
                findPlayingMediaNotification = findPlayingMediaNotification(this.mEntryManager.getAllNotifs());
            }
            if (findPlayingMediaNotification) {
                this.mEntryManager.updateNotifications("NotificationMediaManager - metaDataChanged");
            }
            z = findPlayingMediaNotification;
        }
        dispatchUpdateMediaMetaData(z);
    }

    public final Icon getMediaIcon() {
        String str = this.mMediaNotificationKey;
        if (str == null) {
            return null;
        }
        if (this.mUsingNotifPipeline) {
            return (Icon) Optional.ofNullable(this.mNotifPipeline.getEntry(str)).map(SystemUIApplication$$ExternalSyntheticLambda4.INSTANCE$1).map(SystemUIApplication$$ExternalSyntheticLambda3.INSTANCE$1).orElse(null);
        }
        synchronized (this.mEntryManager) {
            NotificationEntry activeNotificationUnfiltered = this.mEntryManager.getActiveNotificationUnfiltered(this.mMediaNotificationKey);
            if (activeNotificationUnfiltered != null) {
                IconPack iconPack = activeNotificationUnfiltered.mIcons;
                Objects.requireNonNull(iconPack);
                if (iconPack.mShelfIcon != null) {
                    IconPack iconPack2 = activeNotificationUnfiltered.mIcons;
                    Objects.requireNonNull(iconPack2);
                    StatusBarIconView statusBarIconView = iconPack2.mShelfIcon;
                    Objects.requireNonNull(statusBarIconView);
                    return statusBarIconView.mIcon.icon;
                }
            }
            return null;
        }
    }

    public final void updateMediaMetaData(boolean z, boolean z2) {
        boolean z3;
        Bitmap bitmap;
        Trace.beginSection("StatusBar#updateMediaMetaData");
        if (this.mBackdrop == null) {
            Trace.endSection();
            return;
        }
        BiometricUnlockController biometricUnlockController = this.mBiometricUnlockController;
        if (biometricUnlockController == null || !biometricUnlockController.isWakeAndUnlock()) {
            z3 = false;
        } else {
            z3 = true;
        }
        if (this.mKeyguardStateController.isLaunchTransitionFadingAway() || z3) {
            this.mBackdrop.setVisibility(4);
            Trace.endSection();
            return;
        }
        MediaMetadata mediaMetadata = this.mMediaMetadata;
        if (mediaMetadata == null || this.mKeyguardBypassController.getBypassEnabled()) {
            bitmap = null;
        } else {
            bitmap = mediaMetadata.getBitmap("android.media.metadata.ART");
            if (bitmap == null) {
                bitmap = mediaMetadata.getBitmap("android.media.metadata.ALBUM_ART");
            }
        }
        if (z) {
            Iterator it = this.mProcessArtworkTasks.iterator();
            while (it.hasNext()) {
                ((AsyncTask) it.next()).cancel(true);
            }
            this.mProcessArtworkTasks.clear();
        }
        if (bitmap == null || Utils.useQsMediaPlayer(this.mContext)) {
            finishUpdateMediaMetaData(z, z2, null);
        } else {
            this.mProcessArtworkTasks.add(new ProcessArtworkTask(this, z, z2).execute(bitmap));
        }
        Trace.endSection();
    }

    /* renamed from: -$$Nest$mremoveEntry  reason: not valid java name */
    public static void m88$$Nest$mremoveEntry(NotificationMediaManager notificationMediaManager, NotificationEntry notificationEntry) {
        Objects.requireNonNull(notificationMediaManager);
        Objects.requireNonNull(notificationEntry);
        if (notificationEntry.mKey.equals(notificationMediaManager.mMediaNotificationKey)) {
            notificationMediaManager.mMediaNotificationKey = null;
            Objects.requireNonNull(notificationMediaManager.mMediaArtworkProcessor);
            notificationMediaManager.mMediaMetadata = null;
            MediaController mediaController = notificationMediaManager.mMediaController;
            if (mediaController != null) {
                mediaController.unregisterCallback(notificationMediaManager.mMediaListener);
            }
            notificationMediaManager.mMediaController = null;
            notificationMediaManager.dispatchUpdateMediaMetaData(true);
        }
        notificationMediaManager.mMediaDataManager.onNotificationRemoved(notificationEntry.mKey);
    }

    /* JADX WARN: Type inference failed for: r0v10, types: [com.android.systemui.statusbar.NotificationMediaManager$1] */
    /* JADX WARN: Type inference failed for: r0v11, types: [com.android.systemui.statusbar.NotificationMediaManager$7] */
    public NotificationMediaManager(Context context, Lazy<Optional<StatusBar>> lazy, Lazy<NotificationShadeWindowController> lazy2, NotificationVisibilityProvider notificationVisibilityProvider, NotificationEntryManager notificationEntryManager, MediaArtworkProcessor mediaArtworkProcessor, KeyguardBypassController keyguardBypassController, NotifPipeline notifPipeline, NotifCollection notifCollection, NotifPipelineFlags notifPipelineFlags, DelayableExecutor delayableExecutor, MediaDataManager mediaDataManager, DumpManager dumpManager) {
        this.mContext = context;
        this.mMediaArtworkProcessor = mediaArtworkProcessor;
        this.mKeyguardBypassController = keyguardBypassController;
        this.mStatusBarOptionalLazy = lazy;
        this.mNotificationShadeWindowController = lazy2;
        this.mVisibilityProvider = notificationVisibilityProvider;
        this.mEntryManager = notificationEntryManager;
        this.mMainExecutor = delayableExecutor;
        this.mMediaDataManager = mediaDataManager;
        this.mNotifPipeline = notifPipeline;
        this.mNotifCollection = notifCollection;
        if (!notifPipelineFlags.isNewPipelineEnabled()) {
            notificationEntryManager.addNotificationEntryListener(new NotificationEntryListener() { // from class: com.android.systemui.statusbar.NotificationMediaManager.4
                @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
                public final void onEntryInflated(NotificationEntry notificationEntry) {
                    NotificationMediaManager.this.findAndUpdateMediaNotifications();
                }

                @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
                public final void onEntryReinflated(NotificationEntry notificationEntry) {
                    NotificationMediaManager.this.findAndUpdateMediaNotifications();
                }

                @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
                public final void onEntryRemoved(NotificationEntry notificationEntry, boolean z) {
                    NotificationMediaManager.m88$$Nest$mremoveEntry(NotificationMediaManager.this, notificationEntry);
                }

                @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
                public final void onPendingEntryAdded(NotificationEntry notificationEntry) {
                    NotificationMediaManager.this.mMediaDataManager.onNotificationAdded(notificationEntry.mKey, notificationEntry.mSbn);
                }

                @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
                public final void onPreEntryUpdated(NotificationEntry notificationEntry) {
                    NotificationMediaManager.this.mMediaDataManager.onNotificationAdded(notificationEntry.mKey, notificationEntry.mSbn);
                }
            });
            notificationEntryManager.addCollectionListener(new NotifCollectionListener() { // from class: com.android.systemui.statusbar.NotificationMediaManager.5
                @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
                public final void onEntryCleanUp(NotificationEntry notificationEntry) {
                    NotificationMediaManager.m88$$Nest$mremoveEntry(NotificationMediaManager.this, notificationEntry);
                }
            });
            mediaDataManager.addListener(new MediaDataManager.Listener() { // from class: com.android.systemui.statusbar.NotificationMediaManager.6
                @Override // com.android.systemui.media.MediaDataManager.Listener
                public final void onMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z, int i) {
                }

                @Override // com.android.systemui.media.MediaDataManager.Listener
                public final void onSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData, boolean z, boolean z2) {
                }

                @Override // com.android.systemui.media.MediaDataManager.Listener
                public final void onSmartspaceMediaDataRemoved(String str, boolean z) {
                }

                @Override // com.android.systemui.media.MediaDataManager.Listener
                public final void onMediaDataRemoved(String str) {
                    NotificationEntry pendingOrActiveNotif = NotificationMediaManager.this.mEntryManager.getPendingOrActiveNotif(str);
                    if (pendingOrActiveNotif != null) {
                        NotificationMediaManager notificationMediaManager = NotificationMediaManager.this;
                        notificationMediaManager.mEntryManager.performRemoveNotification(pendingOrActiveNotif.mSbn, new DismissedByUserStats(3, notificationMediaManager.mVisibilityProvider.obtain(pendingOrActiveNotif, true)), 2);
                    }
                }
            });
            this.mUsingNotifPipeline = false;
        } else {
            notifPipeline.addCollectionListener(new NotifCollectionListener() { // from class: com.android.systemui.statusbar.NotificationMediaManager.2
                @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
                public final void onEntryAdded(NotificationEntry notificationEntry) {
                    MediaDataManager mediaDataManager2 = NotificationMediaManager.this.mMediaDataManager;
                    Objects.requireNonNull(notificationEntry);
                    mediaDataManager2.onNotificationAdded(notificationEntry.mKey, notificationEntry.mSbn);
                }

                @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
                public final void onEntryBind(NotificationEntry notificationEntry, StatusBarNotification statusBarNotification) {
                    NotificationMediaManager.this.findAndUpdateMediaNotifications();
                }

                @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
                public final void onEntryCleanUp(NotificationEntry notificationEntry) {
                    NotificationMediaManager.m88$$Nest$mremoveEntry(NotificationMediaManager.this, notificationEntry);
                }

                @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
                public final void onEntryRemoved(NotificationEntry notificationEntry, int i) {
                    NotificationMediaManager.m88$$Nest$mremoveEntry(NotificationMediaManager.this, notificationEntry);
                }

                @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
                public final void onEntryUpdated(NotificationEntry notificationEntry) {
                    MediaDataManager mediaDataManager2 = NotificationMediaManager.this.mMediaDataManager;
                    Objects.requireNonNull(notificationEntry);
                    mediaDataManager2.onNotificationAdded(notificationEntry.mKey, notificationEntry.mSbn);
                }
            });
            mediaDataManager.addListener(new AnonymousClass3());
            this.mUsingNotifPipeline = true;
        }
        dumpManager.registerDumpable(this);
    }

    public final boolean findPlayingMediaNotification(Collection<NotificationEntry> collection) {
        boolean z;
        NotificationEntry notificationEntry;
        MediaController mediaController;
        boolean z2;
        MediaSession.Token token;
        int i;
        Iterator<NotificationEntry> it = collection.iterator();
        while (true) {
            z = false;
            if (!it.hasNext()) {
                notificationEntry = null;
                mediaController = null;
                break;
            }
            notificationEntry = it.next();
            Objects.requireNonNull(notificationEntry);
            if (notificationEntry.mSbn.getNotification().isMediaNotification() && (token = (MediaSession.Token) notificationEntry.mSbn.getNotification().extras.getParcelable("android.mediaSession")) != null) {
                mediaController = new MediaController(this.mContext, token);
                PlaybackState playbackState = mediaController.getPlaybackState();
                if (playbackState != null) {
                    i = playbackState.getState();
                } else {
                    i = 0;
                }
                if (3 == i) {
                    break;
                }
            }
        }
        if (mediaController != null) {
            MediaController mediaController2 = this.mMediaController;
            if (mediaController2 == mediaController) {
                z2 = true;
            } else if (mediaController2 == null) {
                z2 = false;
            } else {
                z2 = mediaController2.controlsSameSession(mediaController);
            }
            if (!z2) {
                Objects.requireNonNull(this.mMediaArtworkProcessor);
                this.mMediaMetadata = null;
                MediaController mediaController3 = this.mMediaController;
                if (mediaController3 != null) {
                    mediaController3.unregisterCallback(this.mMediaListener);
                }
                this.mMediaController = mediaController;
                mediaController.registerCallback(this.mMediaListener);
                this.mMediaMetadata = this.mMediaController.getMetadata();
                z = true;
            }
        }
        if (notificationEntry != null && !notificationEntry.mSbn.getKey().equals(this.mMediaNotificationKey)) {
            this.mMediaNotificationKey = notificationEntry.mSbn.getKey();
        }
        return z;
    }
}
