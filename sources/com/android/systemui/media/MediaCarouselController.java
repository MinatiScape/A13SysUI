package com.android.systemui.media;

import android.app.smartspace.SmartspaceAction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.MathUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline0;
import com.android.settingslib.Utils;
import com.android.systemui.Dumpable;
import com.android.systemui.media.MediaPlayerData;
import com.android.systemui.media.MediaViewController;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.qs.PageIndicator;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.android.systemui.statusbar.notification.collection.provider.VisualStabilityProvider;
import com.android.systemui.util.animation.MeasurementInput;
import com.android.systemui.util.animation.TransitionLayout;
import com.android.systemui.util.animation.TransitionViewState;
import com.android.systemui.util.time.SystemClock;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import javax.inject.Provider;
import kotlin.Triple;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__IteratorsJVMKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaCarouselController.kt */
/* loaded from: classes.dex */
public final class MediaCarouselController implements Dumpable {
    public final ActivityStarter activityStarter;
    public int bgColor;
    public int carouselMeasureHeight;
    public int carouselMeasureWidth;
    public final Context context;
    public int currentCarouselHeight;
    public int currentCarouselWidth;
    public boolean currentlyShowingOnlyActive;
    public MediaHostState desiredHostState;
    public boolean isRtl;
    public final MediaScrollView mediaCarousel;
    public final MediaCarouselScrollHandler mediaCarouselScrollHandler;
    public final ViewGroup mediaContent;
    public final Provider<MediaControlPanel> mediaControlPanelFactory;
    public final MediaFlags mediaFlags;
    public final ViewGroup mediaFrame;
    public final MediaHostStatesManager mediaHostStatesManager;
    public final MediaDataManager mediaManager;
    public boolean needsReordering;
    public final PageIndicator pageIndicator;
    public boolean playersVisible;
    public View settingsButton;
    public boolean shouldScrollToActivePlayer;
    public final SystemClock systemClock;
    public Function0<Unit> updateUserVisibility;
    public final AnonymousClass5 visualStabilityCallback;
    public final VisualStabilityProvider visualStabilityProvider;
    public int desiredLocation = -1;
    public int currentEndLocation = -1;
    public int currentStartLocation = -1;
    public float currentTransitionProgress = 1.0f;
    public LinkedHashSet keysNeedRemoval = new LinkedHashSet();
    public boolean currentlyExpanded = true;

    /* compiled from: MediaCarouselController.kt */
    /* renamed from: com.android.systemui.media.MediaCarouselController$1 */
    /* loaded from: classes.dex */
    public /* synthetic */ class AnonymousClass1 extends FunctionReferenceImpl implements Function0<Unit> {
        public AnonymousClass1(Object obj) {
            super(0, obj, MediaCarouselController.class, "onSwipeToDismiss", "onSwipeToDismiss()V", 0);
        }

        @Override // kotlin.jvm.functions.Function0
        public final Unit invoke() {
            boolean z;
            MediaCarouselController mediaCarouselController = (MediaCarouselController) this.receiver;
            Objects.requireNonNull(mediaCarouselController);
            Objects.requireNonNull(MediaPlayerData.INSTANCE);
            Iterator it = MediaPlayerData.players().iterator();
            int i = 0;
            while (true) {
                MediaDataManager mediaDataManager = null;
                if (it.hasNext()) {
                    Object next = it.next();
                    int i2 = i + 1;
                    if (i >= 0) {
                        MediaControlPanel mediaControlPanel = (MediaControlPanel) next;
                        if (mediaControlPanel.mIsImpressed) {
                            int i3 = mediaControlPanel.mInstanceId;
                            int i4 = mediaControlPanel.mUid;
                            if (mediaControlPanel.mRecommendationViewHolder != null) {
                                z = true;
                            } else {
                                z = false;
                            }
                            MediaCarouselController.logSmartspaceCardReported$default(mediaCarouselController, 761, i3, i4, z, new int[]{mediaControlPanel.getSurfaceForSmartspaceLogging()}, 0, 0, -1, 0, 352);
                            mediaControlPanel.mIsImpressed = false;
                        }
                        i = i2;
                    } else {
                        SetsKt__SetsKt.throwIndexOverflow();
                        throw null;
                    }
                } else {
                    MediaDataManager mediaDataManager2 = mediaCarouselController.mediaManager;
                    Objects.requireNonNull(mediaDataManager2);
                    MediaDataFilter mediaDataFilter = mediaDataManager2.mediaDataFilter;
                    Objects.requireNonNull(mediaDataFilter);
                    Log.d("MediaDataFilter", "Media carousel swiped away");
                    for (String str : CollectionsKt___CollectionsKt.toSet(mediaDataFilter.userEntries.keySet())) {
                        MediaDataManager mediaDataManager3 = mediaDataFilter.mediaDataManager;
                        if (mediaDataManager3 == null) {
                            mediaDataManager3 = null;
                        }
                        mediaDataManager3.setTimedOut$frameworks__base__packages__SystemUI__android_common__SystemUI_core(str, true, true);
                    }
                    SmartspaceMediaData smartspaceMediaData = mediaDataFilter.smartspaceMediaData;
                    Objects.requireNonNull(smartspaceMediaData);
                    if (smartspaceMediaData.isActive) {
                        SmartspaceMediaData smartspaceMediaData2 = mediaDataFilter.smartspaceMediaData;
                        Objects.requireNonNull(smartspaceMediaData2);
                        Intent intent = smartspaceMediaData2.dismissIntent;
                        if (intent == null) {
                            Log.w("MediaDataFilter", "Cannot create dismiss action click action: extras missing dismiss_intent.");
                        } else if (intent.getComponent() == null || !Intrinsics.areEqual(intent.getComponent().getClassName(), "com.google.android.apps.gsa.staticplugins.opa.smartspace.ExportedSmartspaceTrampolineActivity")) {
                            mediaDataFilter.context.sendBroadcast(intent);
                        } else {
                            mediaDataFilter.context.startActivity(intent);
                        }
                        SmartspaceMediaData smartspaceMediaData3 = MediaDataManagerKt.EMPTY_SMARTSPACE_MEDIA_DATA;
                        SmartspaceMediaData smartspaceMediaData4 = mediaDataFilter.smartspaceMediaData;
                        Objects.requireNonNull(smartspaceMediaData4);
                        String str2 = smartspaceMediaData4.targetId;
                        SmartspaceMediaData smartspaceMediaData5 = mediaDataFilter.smartspaceMediaData;
                        Objects.requireNonNull(smartspaceMediaData5);
                        mediaDataFilter.smartspaceMediaData = SmartspaceMediaData.copy$default(smartspaceMediaData3, str2, false, smartspaceMediaData5.isValid, null, 0, 0L, 506);
                    }
                    MediaDataManager mediaDataManager4 = mediaDataFilter.mediaDataManager;
                    if (mediaDataManager4 != null) {
                        mediaDataManager = mediaDataManager4;
                    }
                    SmartspaceMediaData smartspaceMediaData6 = mediaDataFilter.smartspaceMediaData;
                    Objects.requireNonNull(smartspaceMediaData6);
                    mediaDataManager.dismissSmartspaceRecommendation(smartspaceMediaData6.targetId, 0L);
                    return Unit.INSTANCE;
                }
            }
        }
    }

    /* compiled from: MediaCarouselController.kt */
    /* renamed from: com.android.systemui.media.MediaCarouselController$2 */
    /* loaded from: classes.dex */
    public /* synthetic */ class AnonymousClass2 extends FunctionReferenceImpl implements Function0<Unit> {
        public AnonymousClass2(Object obj) {
            super(0, obj, MediaCarouselController.class, "updatePageIndicatorLocation", "updatePageIndicatorLocation()V", 0);
        }

        @Override // kotlin.jvm.functions.Function0
        public final Unit invoke() {
            ((MediaCarouselController) this.receiver).updatePageIndicatorLocation();
            return Unit.INSTANCE;
        }
    }

    /* compiled from: MediaCarouselController.kt */
    /* renamed from: com.android.systemui.media.MediaCarouselController$3 */
    /* loaded from: classes.dex */
    public /* synthetic */ class AnonymousClass3 extends FunctionReferenceImpl implements Function1<Boolean, Unit> {
        public AnonymousClass3(Object obj) {
            super(1, obj, MediaCarouselController.class, "closeGuts", "closeGuts(Z)V", 0);
        }

        @Override // kotlin.jvm.functions.Function1
        public final Unit invoke(Boolean bool) {
            boolean booleanValue = bool.booleanValue();
            Objects.requireNonNull((MediaCarouselController) this.receiver);
            Objects.requireNonNull(MediaPlayerData.INSTANCE);
            for (MediaControlPanel mediaControlPanel : MediaPlayerData.players()) {
                mediaControlPanel.closeGuts(booleanValue);
            }
            return Unit.INSTANCE;
        }
    }

    /* compiled from: MediaCarouselController.kt */
    /* renamed from: com.android.systemui.media.MediaCarouselController$4 */
    /* loaded from: classes.dex */
    public /* synthetic */ class AnonymousClass4 extends FunctionReferenceImpl implements Function1<Boolean, Unit> {
        public AnonymousClass4(Object obj) {
            super(1, obj, MediaCarouselController.class, "logSmartspaceImpression", "logSmartspaceImpression(Z)V", 0);
        }

        @Override // kotlin.jvm.functions.Function1
        public final Unit invoke(Boolean bool) {
            ((MediaCarouselController) this.receiver).logSmartspaceImpression(bool.booleanValue());
            return Unit.INSTANCE;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v9, types: [java.lang.Object, com.android.systemui.media.MediaCarouselController$5] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public MediaCarouselController(android.content.Context r22, javax.inject.Provider<com.android.systemui.media.MediaControlPanel> r23, com.android.systemui.statusbar.notification.collection.provider.VisualStabilityProvider r24, com.android.systemui.media.MediaHostStatesManager r25, com.android.systemui.plugins.ActivityStarter r26, com.android.systemui.util.time.SystemClock r27, com.android.systemui.util.concurrency.DelayableExecutor r28, com.android.systemui.media.MediaDataManager r29, com.android.systemui.statusbar.policy.ConfigurationController r30, com.android.systemui.classifier.FalsingCollector r31, com.android.systemui.plugins.FalsingManager r32, com.android.systemui.dump.DumpManager r33, com.android.systemui.media.MediaFlags r34) {
        /*
            Method dump skipped, instructions count: 298
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaCarouselController.<init>(android.content.Context, javax.inject.Provider, com.android.systemui.statusbar.notification.collection.provider.VisualStabilityProvider, com.android.systemui.media.MediaHostStatesManager, com.android.systemui.plugins.ActivityStarter, com.android.systemui.util.time.SystemClock, com.android.systemui.util.concurrency.DelayableExecutor, com.android.systemui.media.MediaDataManager, com.android.systemui.statusbar.policy.ConfigurationController, com.android.systemui.classifier.FalsingCollector, com.android.systemui.plugins.FalsingManager, com.android.systemui.dump.DumpManager, com.android.systemui.media.MediaFlags):void");
    }

    public static void logSmartspaceCardReported$default(MediaCarouselController mediaCarouselController, int i, int i2, int i3, boolean z, int[] iArr, int i4, int i5, int i6, int i7, int i8) {
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int[] iArr2 = iArr;
        int i15 = 0;
        if ((i8 & 32) != 0) {
            i9 = 0;
        } else {
            i9 = i4;
        }
        if ((i8 & 64) != 0) {
            i10 = 0;
        } else {
            i10 = i5;
        }
        if ((i8 & 128) != 0) {
            MediaCarouselScrollHandler mediaCarouselScrollHandler = mediaCarouselController.mediaCarouselScrollHandler;
            Objects.requireNonNull(mediaCarouselScrollHandler);
            i11 = mediaCarouselScrollHandler.visibleMediaIndex;
        } else {
            i11 = i6;
        }
        if ((i8 & 256) != 0) {
            i12 = 0;
        } else {
            i12 = i7;
        }
        Objects.requireNonNull(mediaCarouselController);
        if (!z) {
            MediaDataManager mediaDataManager = mediaCarouselController.mediaManager;
            Objects.requireNonNull(mediaDataManager);
            SmartspaceMediaData smartspaceMediaData = mediaDataManager.smartspaceMediaData;
            Objects.requireNonNull(smartspaceMediaData);
            if (!smartspaceMediaData.isActive) {
                Objects.requireNonNull(MediaPlayerData.INSTANCE);
                if (MediaPlayerData.smartspaceMediaData == null) {
                    return;
                }
            }
        }
        int childCount = mediaCarouselController.mediaContent.getChildCount();
        int length = iArr2.length;
        while (i15 < length) {
            int i16 = iArr2[i15];
            int i17 = i15 + 1;
            if (z) {
                i13 = 15;
            } else {
                i13 = 31;
            }
            SysUiStatsLog.write(i, i2, i16, i11, childCount, i13, i3, i9, i10, i12);
            if (MediaCarouselControllerKt.DEBUG) {
                StringBuilder m = GridLayoutManager$$ExternalSyntheticOutline0.m("Log Smartspace card event id: ", i, " instance id: ", i2, " surface: ");
                m.append(i16);
                m.append(" rank: ");
                m.append(i11);
                m.append(" cardinality: ");
                m.append(childCount);
                m.append(" isRecommendationCard: ");
                m.append(z);
                m.append(" uid: ");
                m.append(i3);
                m.append(" interactedSubcardRank: ");
                m.append(i9);
                m.append(" interactedSubcardCardinality: ");
                i14 = i10;
                m.append(i14);
                m.append(" received_latency_millis: ");
                m.append(i12);
                Log.d("MediaCarouselController", m.toString());
            } else {
                i14 = i10;
            }
            iArr2 = iArr;
            i11 = i11;
            i10 = i14;
            length = length;
            i15 = i17;
        }
    }

    public final boolean addOrUpdatePlayer(String str, String str2, MediaData mediaData) {
        TransitionLayout transitionLayout;
        Objects.requireNonNull(MediaPlayerData.INSTANCE);
        if (str2 != null && !Intrinsics.areEqual(str2, str)) {
            LinkedHashMap linkedHashMap = MediaPlayerData.mediaData;
            MediaPlayerData.MediaSortKey mediaSortKey = (MediaPlayerData.MediaSortKey) linkedHashMap.remove(str2);
            if (mediaSortKey != null) {
                MediaPlayerData.removeMediaPlayer(str);
                MediaPlayerData.MediaSortKey mediaSortKey2 = (MediaPlayerData.MediaSortKey) linkedHashMap.put(str, mediaSortKey);
            }
        }
        MediaControlPanel mediaPlayer = MediaPlayerData.getMediaPlayer(str);
        TreeMap<MediaPlayerData.MediaSortKey, MediaControlPanel> treeMap = MediaPlayerData.mediaPlayers;
        Set<MediaPlayerData.MediaSortKey> keySet = treeMap.keySet();
        MediaCarouselScrollHandler mediaCarouselScrollHandler = this.mediaCarouselScrollHandler;
        Objects.requireNonNull(mediaCarouselScrollHandler);
        MediaPlayerData.MediaSortKey mediaSortKey3 = (MediaPlayerData.MediaSortKey) CollectionsKt___CollectionsKt.elementAtOrNull(keySet, mediaCarouselScrollHandler.visibleMediaIndex);
        if (mediaPlayer == null) {
            MediaControlPanel mediaControlPanel = this.mediaControlPanelFactory.mo144get();
            if (this.mediaFlags.useMediaSessionLayout()) {
                Set<Integer> set = PlayerSessionViewHolder.controlsIds;
                View inflate = LayoutInflater.from(this.context).inflate(2131624263, this.mediaContent, false);
                inflate.setLayerType(2, null);
                inflate.setLayoutDirection(3);
                PlayerSessionViewHolder playerSessionViewHolder = new PlayerSessionViewHolder(inflate);
                playerSessionViewHolder.seekBar.setLayoutDirection(0);
                mediaControlPanel.attachPlayer(playerSessionViewHolder, MediaViewController.TYPE.PLAYER_SESSION);
            } else {
                Set<Integer> set2 = PlayerViewHolder.controlsIds;
                View inflate2 = LayoutInflater.from(this.context).inflate(2131624268, this.mediaContent, false);
                inflate2.setLayerType(2, null);
                inflate2.setLayoutDirection(3);
                PlayerViewHolder playerViewHolder = new PlayerViewHolder(inflate2);
                playerViewHolder.seekBar.setLayoutDirection(0);
                playerViewHolder.progressTimes.setLayoutDirection(0);
                mediaControlPanel.attachPlayer(playerViewHolder, MediaViewController.TYPE.PLAYER);
            }
            Objects.requireNonNull(mediaControlPanel);
            MediaViewController mediaViewController = mediaControlPanel.mMediaViewController;
            MediaCarouselController$addOrUpdatePlayer$1 mediaCarouselController$addOrUpdatePlayer$1 = new MediaCarouselController$addOrUpdatePlayer$1(this);
            Objects.requireNonNull(mediaViewController);
            mediaViewController.sizeChangedListener = mediaCarouselController$addOrUpdatePlayer$1;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
            MediaViewHolder mediaViewHolder = mediaControlPanel.mMediaViewHolder;
            if (!(mediaViewHolder == null || (transitionLayout = mediaViewHolder.player) == null)) {
                transitionLayout.setLayoutParams(layoutParams);
            }
            mediaControlPanel.bindPlayer(mediaData, str);
            boolean z = this.currentlyExpanded;
            SeekBarViewModel seekBarViewModel = mediaControlPanel.mSeekBarViewModel;
            Objects.requireNonNull(seekBarViewModel);
            seekBarViewModel.bgExecutor.execute(new SeekBarViewModel$listening$1(seekBarViewModel, z));
            SystemClock systemClock = this.systemClock;
            MediaPlayerData.removeMediaPlayer(str);
            MediaPlayerData.MediaSortKey mediaSortKey4 = new MediaPlayerData.MediaSortKey(false, mediaData, systemClock.currentTimeMillis());
            MediaPlayerData.mediaData.put(str, mediaSortKey4);
            treeMap.put(mediaSortKey4, mediaControlPanel);
            mediaControlPanel.mMediaViewController.setCurrentState(this.currentStartLocation, this.currentEndLocation, this.currentTransitionProgress, true);
            reorderAllPlayers(mediaSortKey3);
        } else {
            mediaPlayer.bindPlayer(mediaData, str);
            SystemClock systemClock2 = this.systemClock;
            MediaPlayerData.removeMediaPlayer(str);
            MediaPlayerData.MediaSortKey mediaSortKey5 = new MediaPlayerData.MediaSortKey(false, mediaData, systemClock2.currentTimeMillis());
            MediaPlayerData.mediaData.put(str, mediaSortKey5);
            treeMap.put(mediaSortKey5, mediaPlayer);
            VisualStabilityProvider visualStabilityProvider = this.visualStabilityProvider;
            Objects.requireNonNull(visualStabilityProvider);
            if (visualStabilityProvider.isReorderingAllowed || this.shouldScrollToActivePlayer) {
                reorderAllPlayers(mediaSortKey3);
            } else {
                this.needsReordering = true;
            }
        }
        updatePageIndicator();
        this.mediaCarouselScrollHandler.onPlayersChanged();
        this.mediaFrame.setTag(2131428693, Boolean.TRUE);
        if (MediaPlayerData.players().size() != this.mediaContent.getChildCount()) {
            Log.wtf("MediaCarouselController", "Size of players list and number of views in carousel are out of sync");
        }
        if (mediaPlayer == null) {
            return true;
        }
        return false;
    }

    public final void addSmartspaceMediaRecommendations(String str, SmartspaceMediaData smartspaceMediaData, boolean z) {
        String str2;
        String str3;
        String str4;
        List<ViewGroup> list;
        List<ImageView> list2;
        List<SmartspaceAction> list3;
        int i;
        boolean z2;
        TransitionLayout transitionLayout;
        String str5 = "MediaCarouselController";
        if (MediaCarouselControllerKt.DEBUG) {
            Log.d(str5, "Updating smartspace target in carousel");
        }
        Objects.requireNonNull(MediaPlayerData.INSTANCE);
        if (MediaPlayerData.getMediaPlayer(str) != null) {
            Log.w(str5, "Skip adding smartspace target in carousel");
            return;
        }
        Iterator it = MediaPlayerData.mediaData.entrySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                str2 = null;
                break;
            }
            Map.Entry entry = (Map.Entry) it.next();
            MediaPlayerData.MediaSortKey mediaSortKey = (MediaPlayerData.MediaSortKey) entry.getValue();
            Objects.requireNonNull(mediaSortKey);
            if (mediaSortKey.isSsMediaRec) {
                str2 = (String) entry.getKey();
                break;
            }
        }
        if (str2 != null) {
            Objects.requireNonNull(MediaPlayerData.INSTANCE);
            MediaPlayerData.removeMediaPlayer(str2);
        }
        final MediaControlPanel mediaControlPanel = this.mediaControlPanelFactory.mo144get();
        Set<Integer> set = RecommendationViewHolder.controlsIds;
        int i2 = 0;
        View inflate = LayoutInflater.from(this.context).inflate(2131624265, this.mediaContent, false);
        inflate.setLayoutDirection(3);
        RecommendationViewHolder recommendationViewHolder = new RecommendationViewHolder(inflate);
        Objects.requireNonNull(mediaControlPanel);
        mediaControlPanel.mRecommendationViewHolder = recommendationViewHolder;
        mediaControlPanel.mMediaViewController.attach(recommendationViewHolder.recommendations, MediaViewController.TYPE.RECOMMENDATION);
        RecommendationViewHolder recommendationViewHolder2 = mediaControlPanel.mRecommendationViewHolder;
        Objects.requireNonNull(recommendationViewHolder2);
        recommendationViewHolder2.recommendations.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda12
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                MediaControlPanel mediaControlPanel2 = MediaControlPanel.this;
                Objects.requireNonNull(mediaControlPanel2);
                MediaViewController mediaViewController = mediaControlPanel2.mMediaViewController;
                Objects.requireNonNull(mediaViewController);
                if (!mediaViewController.isGutsVisible) {
                    mediaControlPanel2.openGuts();
                    return true;
                }
                mediaControlPanel2.closeGuts(false);
                return true;
            }
        });
        RecommendationViewHolder recommendationViewHolder3 = mediaControlPanel.mRecommendationViewHolder;
        Objects.requireNonNull(recommendationViewHolder3);
        recommendationViewHolder3.cancel.setOnClickListener(new MediaControlPanel$$ExternalSyntheticLambda2(mediaControlPanel, 0));
        RecommendationViewHolder recommendationViewHolder4 = mediaControlPanel.mRecommendationViewHolder;
        Objects.requireNonNull(recommendationViewHolder4);
        recommendationViewHolder4.settings.setOnClickListener(new MediaControlPanel$$ExternalSyntheticLambda1(mediaControlPanel, 0));
        MediaViewController mediaViewController = mediaControlPanel.mMediaViewController;
        MediaCarouselController$addSmartspaceMediaRecommendations$2 mediaCarouselController$addSmartspaceMediaRecommendations$2 = new MediaCarouselController$addSmartspaceMediaRecommendations$2(this);
        Objects.requireNonNull(mediaViewController);
        mediaViewController.sizeChangedListener = mediaCarouselController$addSmartspaceMediaRecommendations$2;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        RecommendationViewHolder recommendationViewHolder5 = mediaControlPanel.mRecommendationViewHolder;
        if (!(recommendationViewHolder5 == null || (transitionLayout = recommendationViewHolder5.recommendations) == null)) {
            transitionLayout.setLayoutParams(layoutParams);
        }
        SmartspaceMediaData copy$default = SmartspaceMediaData.copy$default(smartspaceMediaData, null, false, false, null, this.bgColor, 0L, 383);
        if (mediaControlPanel.mRecommendationViewHolder == null) {
            str3 = str5;
        } else {
            mediaControlPanel.mInstanceId = Math.abs(Math.floorMod(Objects.hashCode(copy$default.targetId), 8192));
            mediaControlPanel.mBackgroundColor = copy$default.backgroundColor;
            RecommendationViewHolder recommendationViewHolder6 = mediaControlPanel.mRecommendationViewHolder;
            Objects.requireNonNull(recommendationViewHolder6);
            TransitionLayout transitionLayout2 = recommendationViewHolder6.recommendations;
            transitionLayout2.setBackgroundTintList(ColorStateList.valueOf(mediaControlPanel.mBackgroundColor));
            List<SmartspaceAction> list4 = copy$default.recommendations;
            if (list4 == null || list4.isEmpty()) {
                str3 = str5;
                Log.w("MediaControlPanel", "Empty media recommendations");
            } else {
                try {
                    ApplicationInfo applicationInfo = mediaControlPanel.mContext.getPackageManager().getApplicationInfo(copy$default.packageName, 0);
                    mediaControlPanel.mUid = applicationInfo.uid;
                    PackageManager packageManager = mediaControlPanel.mContext.getPackageManager();
                    Drawable applicationIcon = packageManager.getApplicationIcon(applicationInfo);
                    ColorMatrix colorMatrix = new ColorMatrix();
                    colorMatrix.setSaturation(0.0f);
                    applicationIcon.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
                    RecommendationViewHolder recommendationViewHolder7 = mediaControlPanel.mRecommendationViewHolder;
                    Objects.requireNonNull(recommendationViewHolder7);
                    recommendationViewHolder7.cardIcon.setImageDrawable(applicationIcon);
                    CharSequence applicationLabel = packageManager.getApplicationLabel(applicationInfo);
                    if (applicationLabel.length() != 0) {
                        RecommendationViewHolder recommendationViewHolder8 = mediaControlPanel.mRecommendationViewHolder;
                        Objects.requireNonNull(recommendationViewHolder8);
                        recommendationViewHolder8.cardText.setText(applicationLabel);
                    }
                    mediaControlPanel.setSmartspaceRecItemOnClickListener(transitionLayout2, copy$default.cardAction, -1);
                    transitionLayout2.setContentDescription(mediaControlPanel.mContext.getString(2131952189, applicationLabel));
                    RecommendationViewHolder recommendationViewHolder9 = mediaControlPanel.mRecommendationViewHolder;
                    Objects.requireNonNull(recommendationViewHolder9);
                    List<ImageView> list5 = recommendationViewHolder9.mediaCoverItems;
                    RecommendationViewHolder recommendationViewHolder10 = mediaControlPanel.mRecommendationViewHolder;
                    Objects.requireNonNull(recommendationViewHolder10);
                    List<ViewGroup> list6 = recommendationViewHolder10.mediaCoverContainers;
                    RecommendationViewHolder recommendationViewHolder11 = mediaControlPanel.mRecommendationViewHolder;
                    Objects.requireNonNull(recommendationViewHolder11);
                    List<Integer> list7 = recommendationViewHolder11.mediaCoverItemsResIds;
                    RecommendationViewHolder recommendationViewHolder12 = mediaControlPanel.mRecommendationViewHolder;
                    Objects.requireNonNull(recommendationViewHolder12);
                    List<Integer> list8 = recommendationViewHolder12.mediaCoverContainersResIds;
                    MediaViewController mediaViewController2 = mediaControlPanel.mMediaViewController;
                    Objects.requireNonNull(mediaViewController2);
                    ConstraintSet constraintSet = mediaViewController2.expandedLayout;
                    MediaViewController mediaViewController3 = mediaControlPanel.mMediaViewController;
                    Objects.requireNonNull(mediaViewController3);
                    ConstraintSet constraintSet2 = mediaViewController3.collapsedLayout;
                    int min = Math.min(list4.size(), 6);
                    int i3 = 0;
                    while (i3 < min && i2 < min) {
                        SmartspaceAction smartspaceAction = list4.get(i3);
                        if (smartspaceAction.getIcon() == null) {
                            Log.w("MediaControlPanel", "No media cover is provided. Skipping this item...");
                            str4 = str5;
                            list2 = list5;
                            list = list6;
                            list3 = list4;
                        } else {
                            list2 = list5;
                            ImageView imageView = list5.get(i2);
                            list3 = list4;
                            imageView.setImageIcon(smartspaceAction.getIcon());
                            ViewGroup viewGroup = list6.get(i2);
                            mediaControlPanel.setSmartspaceRecItemOnClickListener(viewGroup, smartspaceAction, i2);
                            list = list6;
                            viewGroup.setOnLongClickListener(MediaControlPanel$$ExternalSyntheticLambda13.INSTANCE);
                            str4 = str5;
                            String string = smartspaceAction.getExtras().getString("artist_name", "");
                            if (string.isEmpty()) {
                                z2 = true;
                                imageView.setContentDescription(mediaControlPanel.mContext.getString(2131952191, smartspaceAction.getTitle(), applicationLabel));
                                i = 3;
                            } else {
                                i = 3;
                                z2 = true;
                                imageView.setContentDescription(mediaControlPanel.mContext.getString(2131952190, smartspaceAction.getTitle(), string, applicationLabel));
                            }
                            if (i2 < i) {
                                MediaControlPanel.setVisibleAndAlpha(constraintSet2, list7.get(i2).intValue(), z2);
                                MediaControlPanel.setVisibleAndAlpha(constraintSet2, list8.get(i2).intValue(), z2);
                            } else {
                                MediaControlPanel.setVisibleAndAlpha(constraintSet2, list7.get(i2).intValue(), false);
                                MediaControlPanel.setVisibleAndAlpha(constraintSet2, list8.get(i2).intValue(), false);
                            }
                            MediaControlPanel.setVisibleAndAlpha(constraintSet, list7.get(i2).intValue(), true);
                            MediaControlPanel.setVisibleAndAlpha(constraintSet, list8.get(i2).intValue(), true);
                            i2++;
                        }
                        i3++;
                        list4 = list3;
                        min = min;
                        list5 = list2;
                        list6 = list;
                        str5 = str4;
                    }
                    str3 = str5;
                    mediaControlPanel.mSmartspaceMediaItemsCount = i2;
                    RecommendationViewHolder recommendationViewHolder13 = mediaControlPanel.mRecommendationViewHolder;
                    Objects.requireNonNull(recommendationViewHolder13);
                    recommendationViewHolder13.dismiss.setOnClickListener(new MediaControlPanel$$ExternalSyntheticLambda4(mediaControlPanel, copy$default, 0));
                    mediaControlPanel.mController = null;
                    mediaControlPanel.mMediaViewController.refreshState();
                } catch (PackageManager.NameNotFoundException e) {
                    str3 = str5;
                    Log.w("MediaControlPanel", "Fail to get media recommendation's app info", e);
                }
            }
        }
        Objects.requireNonNull(MediaPlayerData.INSTANCE);
        TreeMap<MediaPlayerData.MediaSortKey, MediaControlPanel> treeMap = MediaPlayerData.mediaPlayers;
        Set<MediaPlayerData.MediaSortKey> keySet = treeMap.keySet();
        MediaCarouselScrollHandler mediaCarouselScrollHandler = this.mediaCarouselScrollHandler;
        Objects.requireNonNull(mediaCarouselScrollHandler);
        SystemClock systemClock = this.systemClock;
        MediaPlayerData.shouldPrioritizeSs = z;
        MediaPlayerData.removeMediaPlayer(str);
        MediaPlayerData.MediaSortKey mediaSortKey2 = new MediaPlayerData.MediaSortKey(true, MediaData.copy$default(MediaPlayerData.EMPTY, null, null, null, null, false, null, false, false, Boolean.FALSE, false, 14680063), systemClock.currentTimeMillis());
        MediaPlayerData.mediaData.put(str, mediaSortKey2);
        treeMap.put(mediaSortKey2, mediaControlPanel);
        MediaPlayerData.smartspaceMediaData = smartspaceMediaData;
        mediaControlPanel.mMediaViewController.setCurrentState(this.currentStartLocation, this.currentEndLocation, this.currentTransitionProgress, true);
        reorderAllPlayers((MediaPlayerData.MediaSortKey) CollectionsKt___CollectionsKt.elementAtOrNull(keySet, mediaCarouselScrollHandler.visibleMediaIndex));
        updatePageIndicator();
        this.mediaFrame.setTag(2131428693, Boolean.TRUE);
        if (MediaPlayerData.players().size() != this.mediaContent.getChildCount()) {
            Log.wtf(str3, "Size of players list and number of views in carousel are out of sync");
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        Float f;
        printWriter.println(Intrinsics.stringPlus("keysNeedRemoval: ", this.keysNeedRemoval));
        Objects.requireNonNull(MediaPlayerData.INSTANCE);
        printWriter.println(Intrinsics.stringPlus("playerKeys: ", MediaPlayerData.mediaPlayers.keySet()));
        printWriter.println(Intrinsics.stringPlus("smartspaceMediaData: ", MediaPlayerData.smartspaceMediaData));
        printWriter.println(Intrinsics.stringPlus("shouldPrioritizeSs: ", Boolean.valueOf(MediaPlayerData.shouldPrioritizeSs)));
        printWriter.println("current size: " + this.currentCarouselWidth + " x " + this.currentCarouselHeight);
        printWriter.println(Intrinsics.stringPlus("location: ", Integer.valueOf(this.desiredLocation)));
        StringBuilder sb = new StringBuilder();
        sb.append("state: ");
        MediaHostState mediaHostState = this.desiredHostState;
        Boolean bool = null;
        if (mediaHostState == null) {
            f = null;
        } else {
            f = Float.valueOf(mediaHostState.getExpansion());
        }
        sb.append(f);
        sb.append(", only active ");
        MediaHostState mediaHostState2 = this.desiredHostState;
        if (mediaHostState2 != null) {
            bool = Boolean.valueOf(mediaHostState2.getShowsOnlyActiveMedia());
        }
        sb.append(bool);
        printWriter.println(sb.toString());
    }

    public final void inflateSettingsButton() {
        View inflate = LayoutInflater.from(this.context).inflate(2131624259, this.mediaFrame, false);
        Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.view.View");
        View view = this.settingsButton;
        View view2 = null;
        if (view != null) {
            ViewGroup viewGroup = this.mediaFrame;
            if (view == null) {
                view = null;
            }
            viewGroup.removeView(view);
        }
        this.settingsButton = inflate;
        this.mediaFrame.addView(inflate);
        MediaCarouselScrollHandler mediaCarouselScrollHandler = this.mediaCarouselScrollHandler;
        Objects.requireNonNull(mediaCarouselScrollHandler);
        mediaCarouselScrollHandler.settingsButton = inflate;
        Resources resources = inflate.getResources();
        View view3 = mediaCarouselScrollHandler.settingsButton;
        if (view3 == null) {
            view3 = null;
        }
        mediaCarouselScrollHandler.cornerRadius = resources.getDimensionPixelSize(Utils.getThemeAttr(view3.getContext(), 16844145));
        mediaCarouselScrollHandler.updateSettingsPresentation();
        mediaCarouselScrollHandler.scrollView.invalidateOutline();
        View view4 = this.settingsButton;
        if (view4 != null) {
            view2 = view4;
        }
        view2.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.media.MediaCarouselController$inflateSettingsButton$2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view5) {
                MediaCarouselController.this.activityStarter.startActivity(MediaCarouselControllerKt.settingsIntent, true);
            }
        });
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x0034, code lost:
        if (r1.booleanValue() != false) goto L_0x003e;
     */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0045  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0047  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void logSmartspaceImpression(boolean r15) {
        /*
            r14 = this;
            com.android.systemui.media.MediaCarouselScrollHandler r1 = r14.mediaCarouselScrollHandler
            java.util.Objects.requireNonNull(r1)
            int r1 = r1.visibleMediaIndex
            com.android.systemui.media.MediaPlayerData r2 = com.android.systemui.media.MediaPlayerData.INSTANCE
            java.util.Objects.requireNonNull(r2)
            java.util.Collection r2 = com.android.systemui.media.MediaPlayerData.players()
            int r2 = r2.size()
            if (r2 <= r1) goto L_0x006f
            java.util.Collection r2 = com.android.systemui.media.MediaPlayerData.players()
            java.lang.Object r1 = kotlin.collections.CollectionsKt___CollectionsKt.elementAt(r2, r1)
            r11 = r1
            com.android.systemui.media.MediaControlPanel r11 = (com.android.systemui.media.MediaControlPanel) r11
            com.android.systemui.media.SmartspaceMediaData r1 = com.android.systemui.media.MediaPlayerData.smartspaceMediaData
            r2 = 0
            r12 = 1
            if (r1 == 0) goto L_0x0037
            boolean r1 = r1.isActive
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)
            kotlin.jvm.internal.Intrinsics.checkNotNull(r1)
            boolean r1 = r1.booleanValue()
            if (r1 == 0) goto L_0x0037
            goto L_0x003e
        L_0x0037:
            int r1 = com.android.systemui.media.MediaPlayerData.firstActiveMediaIndex()
            r3 = -1
            if (r1 == r3) goto L_0x0040
        L_0x003e:
            r1 = r12
            goto L_0x0041
        L_0x0040:
            r1 = r2
        L_0x0041:
            com.android.systemui.media.RecommendationViewHolder r3 = r11.mRecommendationViewHolder
            if (r3 == 0) goto L_0x0047
            r4 = r12
            goto L_0x0048
        L_0x0047:
            r4 = r2
        L_0x0048:
            if (r1 != 0) goto L_0x004d
            if (r15 != 0) goto L_0x004d
            return
        L_0x004d:
            r1 = 800(0x320, float:1.121E-42)
            int r3 = r11.mInstanceId
            int r5 = r11.mUid
            int[] r6 = new int[r12]
            int r7 = r11.getSurfaceForSmartspaceLogging()
            r6[r2] = r7
            r7 = 0
            r8 = 0
            r9 = 0
            r10 = 0
            r13 = 480(0x1e0, float:6.73E-43)
            r0 = r14
            r2 = r3
            r3 = r5
            r5 = r6
            r6 = r7
            r7 = r8
            r8 = r9
            r9 = r10
            r10 = r13
            logSmartspaceCardReported$default(r0, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10)
            r11.mIsImpressed = r12
        L_0x006f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaCarouselController.logSmartspaceImpression(boolean):void");
    }

    public final void onDesiredLocationChanged(int i, MediaHostState mediaHostState, boolean z, long j, long j2) {
        boolean z2;
        Object[] objArr;
        ViewGroup viewGroup;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        MeasurementInput measurementInput;
        MeasurementInput measurementInput2;
        MeasurementInput measurementInput3;
        MeasurementInput measurementInput4;
        if (mediaHostState != null) {
            this.desiredLocation = i;
            this.desiredHostState = mediaHostState;
            if (mediaHostState.getExpansion() > 0.0f) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (this.currentlyExpanded != z2) {
                this.currentlyExpanded = z2;
                Objects.requireNonNull(MediaPlayerData.INSTANCE);
                for (MediaControlPanel mediaControlPanel : MediaPlayerData.players()) {
                    boolean z3 = this.currentlyExpanded;
                    Objects.requireNonNull(mediaControlPanel);
                    SeekBarViewModel seekBarViewModel = mediaControlPanel.mSeekBarViewModel;
                    Objects.requireNonNull(seekBarViewModel);
                    seekBarViewModel.bgExecutor.execute(new SeekBarViewModel$listening$1(seekBarViewModel, z3));
                }
            }
            if (this.currentlyExpanded || this.mediaManager.hasActiveMedia() || !mediaHostState.getShowsOnlyActiveMedia()) {
                objArr = null;
            } else {
                objArr = 1;
            }
            Objects.requireNonNull(MediaPlayerData.INSTANCE);
            Iterator it = MediaPlayerData.players().iterator();
            while (true) {
                viewGroup = null;
                TransitionViewState transitionViewState = null;
                if (!it.hasNext()) {
                    break;
                }
                MediaControlPanel mediaControlPanel2 = (MediaControlPanel) it.next();
                if (z) {
                    Objects.requireNonNull(mediaControlPanel2);
                    MediaViewController mediaViewController = mediaControlPanel2.mMediaViewController;
                    Objects.requireNonNull(mediaViewController);
                    mediaViewController.animateNextStateChange = true;
                    mediaViewController.animationDuration = j;
                    mediaViewController.animationDelay = j2;
                }
                if (objArr != null) {
                    Objects.requireNonNull(mediaControlPanel2);
                    MediaViewController mediaViewController2 = mediaControlPanel2.mMediaViewController;
                    Objects.requireNonNull(mediaViewController2);
                    if (mediaViewController2.isGutsVisible) {
                        mediaControlPanel2.closeGuts(!z);
                    }
                }
                Objects.requireNonNull(mediaControlPanel2);
                MediaViewController mediaViewController3 = mediaControlPanel2.mMediaViewController;
                Objects.requireNonNull(mediaViewController3);
                MediaHostStatesManager mediaHostStatesManager = mediaViewController3.mediaHostStatesManager;
                Objects.requireNonNull(mediaHostStatesManager);
                MediaHostState mediaHostState2 = (MediaHostState) mediaHostStatesManager.mediaHostStates.get(Integer.valueOf(i));
                if (mediaHostState2 != null) {
                    transitionViewState = mediaViewController3.obtainViewState(mediaHostState2);
                }
                if (transitionViewState != null) {
                    mediaViewController3.layoutController.setMeasureState(transitionViewState);
                }
            }
            MediaCarouselScrollHandler mediaCarouselScrollHandler = this.mediaCarouselScrollHandler;
            Objects.requireNonNull(mediaCarouselScrollHandler);
            mediaCarouselScrollHandler.showsSettingsButton = !mediaHostState.getShowsOnlyActiveMedia();
            MediaCarouselScrollHandler mediaCarouselScrollHandler2 = this.mediaCarouselScrollHandler;
            boolean falsingProtectionNeeded = mediaHostState.getFalsingProtectionNeeded();
            Objects.requireNonNull(mediaCarouselScrollHandler2);
            mediaCarouselScrollHandler2.falsingProtectionNeeded = falsingProtectionNeeded;
            boolean visible = mediaHostState.getVisible();
            if (visible != this.playersVisible) {
                this.playersVisible = visible;
                if (visible) {
                    this.mediaCarouselScrollHandler.resetTranslation(false);
                }
            }
            MediaHostState mediaHostState3 = this.desiredHostState;
            if (mediaHostState3 == null || (measurementInput4 = mediaHostState3.getMeasurementInput()) == null) {
                i2 = 0;
            } else {
                i2 = View.MeasureSpec.getSize(measurementInput4.widthMeasureSpec);
            }
            MediaHostState mediaHostState4 = this.desiredHostState;
            if (mediaHostState4 == null || (measurementInput3 = mediaHostState4.getMeasurementInput()) == null) {
                i3 = 0;
            } else {
                i3 = View.MeasureSpec.getSize(measurementInput3.heightMeasureSpec);
            }
            if (!((i2 == this.carouselMeasureWidth || i2 == 0) && (i3 == this.carouselMeasureHeight || i3 == 0))) {
                this.carouselMeasureWidth = i2;
                this.carouselMeasureHeight = i3;
                int dimensionPixelSize = this.context.getResources().getDimensionPixelSize(2131166889) + i2;
                MediaHostState mediaHostState5 = this.desiredHostState;
                if (mediaHostState5 == null || (measurementInput2 = mediaHostState5.getMeasurementInput()) == null) {
                    i4 = 0;
                } else {
                    i4 = measurementInput2.widthMeasureSpec;
                }
                MediaHostState mediaHostState6 = this.desiredHostState;
                if (mediaHostState6 == null || (measurementInput = mediaHostState6.getMeasurementInput()) == null) {
                    i5 = 0;
                } else {
                    i5 = measurementInput.heightMeasureSpec;
                }
                this.mediaCarousel.measure(i4, i5);
                MediaScrollView mediaScrollView = this.mediaCarousel;
                mediaScrollView.layout(0, 0, i2, mediaScrollView.getMeasuredHeight());
                MediaCarouselScrollHandler mediaCarouselScrollHandler3 = this.mediaCarouselScrollHandler;
                Objects.requireNonNull(mediaCarouselScrollHandler3);
                mediaCarouselScrollHandler3.playerWidthPlusPadding = dimensionPixelSize;
                int i7 = mediaCarouselScrollHandler3.visibleMediaIndex * dimensionPixelSize;
                int i8 = mediaCarouselScrollHandler3.scrollIntoCurrentMedia;
                if (i8 > dimensionPixelSize) {
                    i6 = (dimensionPixelSize - (i8 - dimensionPixelSize)) + i7;
                } else {
                    i6 = i7 + i8;
                }
                MediaScrollView mediaScrollView2 = mediaCarouselScrollHandler3.scrollView;
                Objects.requireNonNull(mediaScrollView2);
                if (mediaScrollView2.isLayoutRtl()) {
                    ViewGroup viewGroup2 = mediaScrollView2.contentContainer;
                    if (viewGroup2 != null) {
                        viewGroup = viewGroup2;
                    }
                    i6 = (viewGroup.getWidth() - mediaScrollView2.getWidth()) - i6;
                }
                mediaScrollView2.setScrollX(i6);
            }
        }
    }

    public final void removePlayer(String str, boolean z, boolean z2) {
        TransitionLayout transitionLayout;
        boolean z3;
        TransitionLayout transitionLayout2;
        Objects.requireNonNull(MediaPlayerData.INSTANCE);
        MediaControlPanel removeMediaPlayer = MediaPlayerData.removeMediaPlayer(str);
        if (removeMediaPlayer != null) {
            MediaCarouselScrollHandler mediaCarouselScrollHandler = this.mediaCarouselScrollHandler;
            Objects.requireNonNull(mediaCarouselScrollHandler);
            ViewGroup viewGroup = mediaCarouselScrollHandler.mediaContent;
            MediaViewHolder mediaViewHolder = removeMediaPlayer.mMediaViewHolder;
            TransitionLayout transitionLayout3 = null;
            if (mediaViewHolder == null) {
                transitionLayout = null;
            } else {
                transitionLayout = mediaViewHolder.player;
            }
            int indexOfChild = viewGroup.indexOfChild(transitionLayout);
            int i = mediaCarouselScrollHandler.visibleMediaIndex;
            boolean z4 = true;
            if (indexOfChild <= i) {
                z3 = true;
            } else {
                z3 = false;
            }
            if (z3) {
                mediaCarouselScrollHandler.visibleMediaIndex = Math.max(0, i - 1);
            }
            if (!mediaCarouselScrollHandler.scrollView.isLayoutRtl()) {
                z4 = z3;
            } else if (z3) {
                z4 = false;
            }
            if (z4) {
                MediaScrollView mediaScrollView = mediaCarouselScrollHandler.scrollView;
                mediaScrollView.setScrollX(Math.max(mediaScrollView.getScrollX() - mediaCarouselScrollHandler.playerWidthPlusPadding, 0));
            }
            ViewGroup viewGroup2 = this.mediaContent;
            MediaViewHolder mediaViewHolder2 = removeMediaPlayer.mMediaViewHolder;
            if (mediaViewHolder2 == null) {
                transitionLayout2 = null;
            } else {
                transitionLayout2 = mediaViewHolder2.player;
            }
            viewGroup2.removeView(transitionLayout2);
            ViewGroup viewGroup3 = this.mediaContent;
            RecommendationViewHolder recommendationViewHolder = removeMediaPlayer.mRecommendationViewHolder;
            if (recommendationViewHolder != null) {
                transitionLayout3 = recommendationViewHolder.recommendations;
            }
            viewGroup3.removeView(transitionLayout3);
            if (removeMediaPlayer.mSeekBarObserver != null) {
                SeekBarViewModel seekBarViewModel = removeMediaPlayer.mSeekBarViewModel;
                Objects.requireNonNull(seekBarViewModel);
                seekBarViewModel._progress.removeObserver(removeMediaPlayer.mSeekBarObserver);
            }
            final SeekBarViewModel seekBarViewModel2 = removeMediaPlayer.mSeekBarViewModel;
            Objects.requireNonNull(seekBarViewModel2);
            seekBarViewModel2.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.media.SeekBarViewModel$onDestroy$1
                @Override // java.lang.Runnable
                public final void run() {
                    SeekBarViewModel.this.setController(null);
                    SeekBarViewModel seekBarViewModel3 = SeekBarViewModel.this;
                    seekBarViewModel3.playbackState = null;
                    Runnable runnable = seekBarViewModel3.cancel;
                    if (runnable != null) {
                        runnable.run();
                    }
                    SeekBarViewModel.this.cancel = null;
                }
            });
            MediaViewController mediaViewController = removeMediaPlayer.mMediaViewController;
            Objects.requireNonNull(mediaViewController);
            MediaHostStatesManager mediaHostStatesManager = mediaViewController.mediaHostStatesManager;
            Objects.requireNonNull(mediaHostStatesManager);
            mediaHostStatesManager.controllers.remove(mediaViewController);
            mediaViewController.configurationController.removeCallback(mediaViewController.configurationListener);
            this.mediaCarouselScrollHandler.onPlayersChanged();
            updatePageIndicator();
            if (z) {
                this.mediaManager.dismissMediaData(str, 0L);
            }
            if (z2) {
                this.mediaManager.dismissSmartspaceRecommendation(str, 0L);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x0081, code lost:
        r6.mediaCarouselScrollHandler.scrollToPlayer(r3, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0086, code lost:
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void reorderAllPlayers(com.android.systemui.media.MediaPlayerData.MediaSortKey r7) {
        /*
            r6 = this;
            android.view.ViewGroup r0 = r6.mediaContent
            r0.removeAllViews()
            com.android.systemui.media.MediaPlayerData r0 = com.android.systemui.media.MediaPlayerData.INSTANCE
            java.util.Objects.requireNonNull(r0)
            java.util.Collection r0 = com.android.systemui.media.MediaPlayerData.players()
            java.util.Iterator r0 = r0.iterator()
        L_0x0012:
            boolean r1 = r0.hasNext()
            r2 = 0
            if (r1 == 0) goto L_0x003f
            java.lang.Object r1 = r0.next()
            com.android.systemui.media.MediaControlPanel r1 = (com.android.systemui.media.MediaControlPanel) r1
            java.util.Objects.requireNonNull(r1)
            com.android.systemui.media.MediaViewHolder r3 = r1.mMediaViewHolder
            if (r3 != 0) goto L_0x0027
            goto L_0x0030
        L_0x0027:
            android.view.ViewGroup r2 = r6.mediaContent
            com.android.systemui.util.animation.TransitionLayout r3 = r3.player
            r2.addView(r3)
            kotlin.Unit r2 = kotlin.Unit.INSTANCE
        L_0x0030:
            if (r2 != 0) goto L_0x0012
            com.android.systemui.media.RecommendationViewHolder r1 = r1.mRecommendationViewHolder
            if (r1 != 0) goto L_0x0037
            goto L_0x0012
        L_0x0037:
            android.view.ViewGroup r2 = r6.mediaContent
            com.android.systemui.util.animation.TransitionLayout r1 = r1.recommendations
            r2.addView(r1)
            goto L_0x0012
        L_0x003f:
            com.android.systemui.media.MediaCarouselScrollHandler r0 = r6.mediaCarouselScrollHandler
            r0.onPlayersChanged()
            boolean r0 = r6.shouldScrollToActivePlayer
            if (r0 == 0) goto L_0x0086
            r0 = 0
            r6.shouldScrollToActivePlayer = r0
            com.android.systemui.media.MediaPlayerData r1 = com.android.systemui.media.MediaPlayerData.INSTANCE
            java.util.Objects.requireNonNull(r1)
            int r1 = com.android.systemui.media.MediaPlayerData.firstActiveMediaIndex()
            r3 = -1
            if (r1 == r3) goto L_0x0086
            if (r7 != 0) goto L_0x005a
            goto L_0x0086
        L_0x005a:
            java.util.TreeMap<com.android.systemui.media.MediaPlayerData$MediaSortKey, com.android.systemui.media.MediaControlPanel> r4 = com.android.systemui.media.MediaPlayerData.mediaPlayers
            java.util.Set r4 = r4.keySet()
            java.util.Iterator r4 = r4.iterator()
        L_0x0064:
            boolean r5 = r4.hasNext()
            if (r5 == 0) goto L_0x0081
            java.lang.Object r5 = r4.next()
            if (r0 < 0) goto L_0x007d
            com.android.systemui.media.MediaPlayerData$MediaSortKey r5 = (com.android.systemui.media.MediaPlayerData.MediaSortKey) r5
            boolean r5 = kotlin.jvm.internal.Intrinsics.areEqual(r7, r5)
            if (r5 == 0) goto L_0x007a
            r3 = r0
            goto L_0x0081
        L_0x007a:
            int r0 = r0 + 1
            goto L_0x0064
        L_0x007d:
            kotlin.collections.SetsKt__SetsKt.throwIndexOverflow()
            throw r2
        L_0x0081:
            com.android.systemui.media.MediaCarouselScrollHandler r6 = r6.mediaCarouselScrollHandler
            r6.scrollToPlayer(r3, r1)
        L_0x0086:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaCarouselController.reorderAllPlayers(com.android.systemui.media.MediaPlayerData$MediaSortKey):void");
    }

    public final void updatePageIndicator() {
        int childCount = this.mediaContent.getChildCount();
        this.pageIndicator.setNumPages(childCount);
        if (childCount == 1) {
            this.pageIndicator.setLocation(0.0f);
        }
        updatePageIndicatorAlpha();
    }

    public final void updatePageIndicatorAlpha() {
        boolean z;
        float f;
        float f2;
        MediaHostStatesManager mediaHostStatesManager = this.mediaHostStatesManager;
        Objects.requireNonNull(mediaHostStatesManager);
        LinkedHashMap linkedHashMap = mediaHostStatesManager.mediaHostStates;
        MediaHostState mediaHostState = (MediaHostState) linkedHashMap.get(Integer.valueOf(this.currentEndLocation));
        boolean z2 = false;
        if (mediaHostState == null) {
            z = false;
        } else {
            z = mediaHostState.getVisible();
        }
        MediaHostState mediaHostState2 = (MediaHostState) linkedHashMap.get(Integer.valueOf(this.currentStartLocation));
        if (mediaHostState2 != null) {
            z2 = mediaHostState2.getVisible();
        }
        float f3 = 1.0f;
        if (z2) {
            f = 1.0f;
        } else {
            f = 0.0f;
        }
        if (z) {
            f2 = 1.0f;
        } else {
            f2 = 0.0f;
        }
        if (!z || !z2) {
            float f4 = this.currentTransitionProgress;
            if (!z) {
                f4 = 1.0f - f4;
            }
            f3 = MathUtils.lerp(f, f2, MathUtils.constrain(MathUtils.map(0.95f, 1.0f, 0.0f, 1.0f, f4), 0.0f, 1.0f));
        }
        this.pageIndicator.setAlpha(f3);
    }

    public final void updatePageIndicatorLocation() {
        int i;
        int i2;
        if (this.isRtl) {
            i2 = this.pageIndicator.getWidth();
            i = this.currentCarouselWidth;
        } else {
            i2 = this.currentCarouselWidth;
            i = this.pageIndicator.getWidth();
        }
        PageIndicator pageIndicator = this.pageIndicator;
        MediaCarouselScrollHandler mediaCarouselScrollHandler = this.mediaCarouselScrollHandler;
        Objects.requireNonNull(mediaCarouselScrollHandler);
        pageIndicator.setTranslationX(((i2 - i) / 2.0f) + mediaCarouselScrollHandler.contentTranslation);
        ViewGroup.LayoutParams layoutParams = this.pageIndicator.getLayoutParams();
        Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
        PageIndicator pageIndicator2 = this.pageIndicator;
        pageIndicator2.setTranslationY((this.currentCarouselHeight - pageIndicator2.getHeight()) - ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin);
    }

    public static final void access$recreatePlayers(MediaCarouselController mediaCarouselController) {
        int i;
        Objects.requireNonNull(mediaCarouselController);
        mediaCarouselController.bgColor = mediaCarouselController.context.getColor(2131100337);
        PageIndicator pageIndicator = mediaCarouselController.pageIndicator;
        if (mediaCarouselController.mediaFlags.useMediaSessionLayout()) {
            i = mediaCarouselController.context.getColor(2131100309);
        } else {
            i = mediaCarouselController.context.getColor(2131100327);
        }
        ColorStateList valueOf = ColorStateList.valueOf(i);
        Objects.requireNonNull(pageIndicator);
        if (!valueOf.equals(pageIndicator.mTint)) {
            pageIndicator.mTint = valueOf;
            int childCount = pageIndicator.getChildCount();
            for (int i2 = 0; i2 < childCount; i2++) {
                View childAt = pageIndicator.getChildAt(i2);
                if (childAt instanceof ImageView) {
                    ((ImageView) childAt).setImageTintList(pageIndicator.mTint);
                }
            }
        }
        Objects.requireNonNull(MediaPlayerData.INSTANCE);
        Set<Map.Entry> entrySet = MediaPlayerData.mediaData.entrySet();
        ArrayList arrayList = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(entrySet, 10));
        for (Map.Entry entry : entrySet) {
            Object key = entry.getKey();
            MediaPlayerData.MediaSortKey mediaSortKey = (MediaPlayerData.MediaSortKey) entry.getValue();
            Objects.requireNonNull(mediaSortKey);
            MediaData mediaData = mediaSortKey.data;
            MediaPlayerData.MediaSortKey mediaSortKey2 = (MediaPlayerData.MediaSortKey) entry.getValue();
            Objects.requireNonNull(mediaSortKey2);
            arrayList.add(new Triple(key, mediaData, Boolean.valueOf(mediaSortKey2.isSsMediaRec)));
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            Triple triple = (Triple) it.next();
            String str = (String) triple.component1();
            MediaData mediaData2 = (MediaData) triple.component2();
            if (((Boolean) triple.component3()).booleanValue()) {
                Objects.requireNonNull(MediaPlayerData.INSTANCE);
                SmartspaceMediaData smartspaceMediaData = MediaPlayerData.smartspaceMediaData;
                mediaCarouselController.removePlayer(str, false, false);
                if (smartspaceMediaData != null) {
                    mediaCarouselController.addSmartspaceMediaRecommendations(smartspaceMediaData.targetId, smartspaceMediaData, MediaPlayerData.shouldPrioritizeSs);
                }
            } else {
                mediaCarouselController.removePlayer(str, false, false);
                mediaCarouselController.addOrUpdatePlayer(str, null, mediaData2);
            }
        }
    }

    public static final void access$updateCarouselDimensions(MediaCarouselController mediaCarouselController) {
        float f;
        Objects.requireNonNull(mediaCarouselController);
        Objects.requireNonNull(MediaPlayerData.INSTANCE);
        int i = 0;
        int i2 = 0;
        for (MediaControlPanel mediaControlPanel : MediaPlayerData.players()) {
            Objects.requireNonNull(mediaControlPanel);
            MediaViewController mediaViewController = mediaControlPanel.mMediaViewController;
            int i3 = mediaViewController.currentWidth;
            TransitionLayout transitionLayout = mediaViewController.transitionLayout;
            float f2 = 0.0f;
            if (transitionLayout == null) {
                f = 0.0f;
            } else {
                f = transitionLayout.getTranslationX();
            }
            i = Math.max(i, i3 + ((int) f));
            int i4 = mediaViewController.currentHeight;
            TransitionLayout transitionLayout2 = mediaViewController.transitionLayout;
            if (transitionLayout2 != null) {
                f2 = transitionLayout2.getTranslationY();
            }
            i2 = Math.max(i2, i4 + ((int) f2));
        }
        if (!(i == mediaCarouselController.currentCarouselWidth && i2 == mediaCarouselController.currentCarouselHeight)) {
            mediaCarouselController.currentCarouselWidth = i;
            mediaCarouselController.currentCarouselHeight = i2;
            MediaCarouselScrollHandler mediaCarouselScrollHandler = mediaCarouselController.mediaCarouselScrollHandler;
            Objects.requireNonNull(mediaCarouselScrollHandler);
            int i5 = mediaCarouselScrollHandler.carouselHeight;
            if (!(i2 == i5 && i == i5)) {
                mediaCarouselScrollHandler.carouselWidth = i;
                mediaCarouselScrollHandler.carouselHeight = i2;
                mediaCarouselScrollHandler.scrollView.invalidateOutline();
            }
            mediaCarouselController.updatePageIndicatorLocation();
        }
    }
}
