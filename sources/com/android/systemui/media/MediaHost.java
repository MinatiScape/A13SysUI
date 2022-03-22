package com.android.systemui.media;

import android.graphics.Rect;
import android.util.ArraySet;
import android.view.View;
import android.view.ViewGroupOverlay;
import android.view.ViewOverlay;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.MediaHost;
import com.android.systemui.util.animation.DisappearParameters;
import com.android.systemui.util.animation.MeasurementInput;
import com.android.systemui.util.animation.MeasurementOutput;
import com.android.systemui.util.animation.UniqueObjectHostView;
import java.util.Iterator;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaHost.kt */
/* loaded from: classes.dex */
public final class MediaHost implements MediaHostState {
    public UniqueObjectHostView hostView;
    public boolean inited;
    public boolean listeningToMediaData;
    public final MediaDataManager mediaDataManager;
    public final MediaHierarchyManager mediaHierarchyManager;
    public final MediaHostStatesManager mediaHostStatesManager;
    public final MediaHostStateHolder state;
    public int location = -1;
    public ArraySet<Function1<Boolean, Unit>> visibleChangedListeners = new ArraySet<>();
    public final int[] tmpLocationOnScreen = {0, 0};
    public final Rect currentBounds = new Rect();
    public final MediaHost$listener$1 listener = new MediaDataManager.Listener() { // from class: com.android.systemui.media.MediaHost$listener$1
        @Override // com.android.systemui.media.MediaDataManager.Listener
        public final void onMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z, int i) {
            if (z) {
                MediaHost.this.updateViewVisibility();
            }
        }

        @Override // com.android.systemui.media.MediaDataManager.Listener
        public final void onMediaDataRemoved(String str) {
            MediaHost.this.updateViewVisibility();
        }

        @Override // com.android.systemui.media.MediaDataManager.Listener
        public final void onSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData, boolean z, boolean z2) {
            MediaHost.this.updateViewVisibility();
        }

        @Override // com.android.systemui.media.MediaDataManager.Listener
        public final void onSmartspaceMediaDataRemoved(String str, boolean z) {
            if (z) {
                MediaHost.this.updateViewVisibility();
            }
        }
    };

    /* compiled from: MediaHost.kt */
    /* loaded from: classes.dex */
    public static final class MediaHostStateHolder implements MediaHostState {
        public Function0<Unit> changedListener;
        public DisappearParameters disappearParameters;
        public float expansion;
        public boolean falsingProtectionNeeded;
        public int lastDisappearHash;
        public MeasurementInput measurementInput;
        public boolean showsOnlyActiveMedia;
        public boolean visible = true;

        @Override // com.android.systemui.media.MediaHostState
        public final MediaHostStateHolder copy() {
            MeasurementInput measurementInput;
            MediaHostStateHolder mediaHostStateHolder = new MediaHostStateHolder();
            mediaHostStateHolder.setExpansion(this.expansion);
            boolean z = this.showsOnlyActiveMedia;
            if (!Boolean.valueOf(z).equals(Boolean.valueOf(mediaHostStateHolder.showsOnlyActiveMedia))) {
                mediaHostStateHolder.showsOnlyActiveMedia = z;
                Function0<Unit> function0 = mediaHostStateHolder.changedListener;
                if (function0 != null) {
                    function0.invoke();
                }
            }
            MeasurementInput measurementInput2 = this.measurementInput;
            if (measurementInput2 == null) {
                measurementInput = null;
            } else {
                measurementInput = new MeasurementInput(measurementInput2.widthMeasureSpec, measurementInput2.heightMeasureSpec);
            }
            boolean z2 = false;
            if (measurementInput != null && measurementInput.equals(mediaHostStateHolder.measurementInput)) {
                z2 = true;
            }
            if (!z2) {
                mediaHostStateHolder.measurementInput = measurementInput;
                Function0<Unit> function02 = mediaHostStateHolder.changedListener;
                if (function02 != null) {
                    function02.invoke();
                }
            }
            boolean z3 = this.visible;
            if (mediaHostStateHolder.visible != z3) {
                mediaHostStateHolder.visible = z3;
                Function0<Unit> function03 = mediaHostStateHolder.changedListener;
                if (function03 != null) {
                    function03.invoke();
                }
            }
            DisappearParameters disappearParameters = this.disappearParameters;
            Objects.requireNonNull(disappearParameters);
            DisappearParameters disappearParameters2 = new DisappearParameters();
            disappearParameters2.disappearSize.set(disappearParameters.disappearSize);
            disappearParameters2.gonePivot.set(disappearParameters.gonePivot);
            disappearParameters2.contentTranslationFraction.set(disappearParameters.contentTranslationFraction);
            disappearParameters2.disappearStart = disappearParameters.disappearStart;
            disappearParameters2.disappearEnd = disappearParameters.disappearEnd;
            disappearParameters2.fadeStartPosition = disappearParameters.fadeStartPosition;
            mediaHostStateHolder.setDisappearParameters(disappearParameters2);
            boolean z4 = this.falsingProtectionNeeded;
            if (mediaHostStateHolder.falsingProtectionNeeded != z4) {
                mediaHostStateHolder.falsingProtectionNeeded = z4;
                Function0<Unit> function04 = mediaHostStateHolder.changedListener;
                if (function04 != null) {
                    function04.invoke();
                }
            }
            return mediaHostStateHolder;
        }

        public final boolean equals(Object obj) {
            boolean z;
            if (!(obj instanceof MediaHostState)) {
                return false;
            }
            MediaHostState mediaHostState = (MediaHostState) obj;
            if (!Objects.equals(this.measurementInput, mediaHostState.getMeasurementInput())) {
                return false;
            }
            if (this.expansion == mediaHostState.getExpansion()) {
                z = true;
            } else {
                z = false;
            }
            if (z && this.showsOnlyActiveMedia == mediaHostState.getShowsOnlyActiveMedia() && this.visible == mediaHostState.getVisible() && this.falsingProtectionNeeded == mediaHostState.getFalsingProtectionNeeded() && this.disappearParameters.equals(mediaHostState.getDisappearParameters())) {
                return true;
            }
            return false;
        }

        public final int hashCode() {
            int i;
            int i2;
            MeasurementInput measurementInput = this.measurementInput;
            if (measurementInput == null) {
                i = 0;
            } else {
                i = measurementInput.hashCode();
            }
            int hashCode = Float.hashCode(this.expansion);
            int hashCode2 = Boolean.hashCode(this.falsingProtectionNeeded);
            int hashCode3 = (Boolean.hashCode(this.showsOnlyActiveMedia) + ((hashCode2 + ((hashCode + (i * 31)) * 31)) * 31)) * 31;
            if (this.visible) {
                i2 = 1;
            } else {
                i2 = 2;
            }
            return this.disappearParameters.hashCode() + ((hashCode3 + i2) * 31);
        }

        public MediaHostStateHolder() {
            DisappearParameters disappearParameters = new DisappearParameters();
            this.disappearParameters = disappearParameters;
            this.lastDisappearHash = disappearParameters.hashCode();
        }

        public final void setDisappearParameters(DisappearParameters disappearParameters) {
            int hashCode = disappearParameters.hashCode();
            if (!Integer.valueOf(this.lastDisappearHash).equals(Integer.valueOf(hashCode))) {
                this.disappearParameters = disappearParameters;
                this.lastDisappearHash = hashCode;
                Function0<Unit> function0 = this.changedListener;
                if (function0 != null) {
                    function0.invoke();
                }
            }
        }

        public final void setExpansion(float f) {
            if (!Float.valueOf(f).equals(Float.valueOf(this.expansion))) {
                this.expansion = f;
                Function0<Unit> function0 = this.changedListener;
                if (function0 != null) {
                    function0.invoke();
                }
            }
        }

        @Override // com.android.systemui.media.MediaHostState
        public final DisappearParameters getDisappearParameters() {
            return this.disappearParameters;
        }

        @Override // com.android.systemui.media.MediaHostState
        public final float getExpansion() {
            return this.expansion;
        }

        @Override // com.android.systemui.media.MediaHostState
        public final boolean getFalsingProtectionNeeded() {
            return this.falsingProtectionNeeded;
        }

        @Override // com.android.systemui.media.MediaHostState
        public final MeasurementInput getMeasurementInput() {
            return this.measurementInput;
        }

        @Override // com.android.systemui.media.MediaHostState
        public final boolean getShowsOnlyActiveMedia() {
            return this.showsOnlyActiveMedia;
        }

        @Override // com.android.systemui.media.MediaHostState
        public final boolean getVisible() {
            return this.visible;
        }
    }

    @Override // com.android.systemui.media.MediaHostState
    public final MediaHostStateHolder copy() {
        return this.state.copy();
    }

    public final void setExpansion(float f) {
        this.state.setExpansion(f);
    }

    @Override // com.android.systemui.media.MediaHostState
    public final DisappearParameters getDisappearParameters() {
        MediaHostStateHolder mediaHostStateHolder = this.state;
        Objects.requireNonNull(mediaHostStateHolder);
        return mediaHostStateHolder.disappearParameters;
    }

    @Override // com.android.systemui.media.MediaHostState
    public final float getExpansion() {
        MediaHostStateHolder mediaHostStateHolder = this.state;
        Objects.requireNonNull(mediaHostStateHolder);
        return mediaHostStateHolder.expansion;
    }

    @Override // com.android.systemui.media.MediaHostState
    public final boolean getFalsingProtectionNeeded() {
        MediaHostStateHolder mediaHostStateHolder = this.state;
        Objects.requireNonNull(mediaHostStateHolder);
        return mediaHostStateHolder.falsingProtectionNeeded;
    }

    public final UniqueObjectHostView getHostView() {
        UniqueObjectHostView uniqueObjectHostView = this.hostView;
        if (uniqueObjectHostView != null) {
            return uniqueObjectHostView;
        }
        return null;
    }

    @Override // com.android.systemui.media.MediaHostState
    public final MeasurementInput getMeasurementInput() {
        MediaHostStateHolder mediaHostStateHolder = this.state;
        Objects.requireNonNull(mediaHostStateHolder);
        return mediaHostStateHolder.measurementInput;
    }

    @Override // com.android.systemui.media.MediaHostState
    public final boolean getShowsOnlyActiveMedia() {
        MediaHostStateHolder mediaHostStateHolder = this.state;
        Objects.requireNonNull(mediaHostStateHolder);
        return mediaHostStateHolder.showsOnlyActiveMedia;
    }

    @Override // com.android.systemui.media.MediaHostState
    public final boolean getVisible() {
        MediaHostStateHolder mediaHostStateHolder = this.state;
        Objects.requireNonNull(mediaHostStateHolder);
        return mediaHostStateHolder.visible;
    }

    public final void init(final int i) {
        if (!this.inited) {
            this.inited = true;
            this.location = i;
            final MediaHierarchyManager mediaHierarchyManager = this.mediaHierarchyManager;
            Objects.requireNonNull(mediaHierarchyManager);
            final UniqueObjectHostView uniqueObjectHostView = new UniqueObjectHostView(mediaHierarchyManager.context);
            uniqueObjectHostView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.android.systemui.media.MediaHierarchyManager$createUniqueObjectHost$1
                @Override // android.view.View.OnAttachStateChangeListener
                public final void onViewDetachedFromWindow(View view) {
                }

                @Override // android.view.View.OnAttachStateChangeListener
                public final void onViewAttachedToWindow(View view) {
                    MediaHierarchyManager mediaHierarchyManager2 = MediaHierarchyManager.this;
                    if (mediaHierarchyManager2.rootOverlay == null) {
                        mediaHierarchyManager2.rootView = uniqueObjectHostView.getViewRootImpl().getView();
                        MediaHierarchyManager mediaHierarchyManager3 = MediaHierarchyManager.this;
                        View view2 = mediaHierarchyManager3.rootView;
                        Intrinsics.checkNotNull(view2);
                        ViewOverlay overlay = view2.getOverlay();
                        Objects.requireNonNull(overlay, "null cannot be cast to non-null type android.view.ViewGroupOverlay");
                        mediaHierarchyManager3.rootOverlay = (ViewGroupOverlay) overlay;
                    }
                    uniqueObjectHostView.removeOnAttachStateChangeListener(this);
                }
            });
            this.hostView = uniqueObjectHostView;
            this.visibleChangedListeners.add(new MediaHierarchyManager$register$1(this, mediaHierarchyManager));
            MediaHost[] mediaHostArr = mediaHierarchyManager.mediaHosts;
            int i2 = this.location;
            mediaHostArr[i2] = this;
            if (i2 == mediaHierarchyManager.desiredLocation) {
                mediaHierarchyManager.desiredLocation = -1;
            }
            if (i2 == mediaHierarchyManager.currentAttachmentLocation) {
                mediaHierarchyManager.currentAttachmentLocation = -1;
            }
            MediaHierarchyManager.updateDesiredLocation$default(mediaHierarchyManager, false, 3);
            this.hostView = uniqueObjectHostView;
            setListeningToMediaData(true);
            getHostView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.android.systemui.media.MediaHost$init$1
                @Override // android.view.View.OnAttachStateChangeListener
                public final void onViewAttachedToWindow(View view) {
                    MediaHost.this.setListeningToMediaData(true);
                    MediaHost.this.updateViewVisibility();
                }

                @Override // android.view.View.OnAttachStateChangeListener
                public final void onViewDetachedFromWindow(View view) {
                    MediaHost.this.setListeningToMediaData(false);
                }
            });
            UniqueObjectHostView hostView = getHostView();
            UniqueObjectHostView.MeasurementManager mediaHost$init$2 = new UniqueObjectHostView.MeasurementManager() { // from class: com.android.systemui.media.MediaHost$init$2
                @Override // com.android.systemui.util.animation.UniqueObjectHostView.MeasurementManager
                public final MeasurementOutput onMeasure(MeasurementInput measurementInput) {
                    if (View.MeasureSpec.getMode(measurementInput.widthMeasureSpec) == Integer.MIN_VALUE) {
                        measurementInput.widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measurementInput.widthMeasureSpec), 1073741824);
                    }
                    MediaHost.MediaHostStateHolder mediaHostStateHolder = MediaHost.this.state;
                    boolean z = false;
                    if (measurementInput.equals(mediaHostStateHolder.measurementInput)) {
                        z = true;
                    }
                    if (!z) {
                        mediaHostStateHolder.measurementInput = measurementInput;
                        Function0<Unit> function0 = mediaHostStateHolder.changedListener;
                        if (function0 != null) {
                            function0.invoke();
                        }
                    } else {
                        Objects.requireNonNull(mediaHostStateHolder);
                    }
                    MediaHost mediaHost = MediaHost.this;
                    return mediaHost.mediaHostStatesManager.updateCarouselDimensions(i, mediaHost.state);
                }
            };
            Objects.requireNonNull(hostView);
            hostView.measurementManager = mediaHost$init$2;
            MediaHostStateHolder mediaHostStateHolder = this.state;
            MediaHost$init$3 mediaHost$init$3 = new MediaHost$init$3(this, i);
            Objects.requireNonNull(mediaHostStateHolder);
            mediaHostStateHolder.changedListener = mediaHost$init$3;
            updateViewVisibility();
        }
    }

    public final void setListeningToMediaData(boolean z) {
        if (z != this.listeningToMediaData) {
            this.listeningToMediaData = z;
            if (z) {
                this.mediaDataManager.addListener(this.listener);
                return;
            }
            MediaDataManager mediaDataManager = this.mediaDataManager;
            MediaHost$listener$1 mediaHost$listener$1 = this.listener;
            Objects.requireNonNull(mediaDataManager);
            MediaDataFilter mediaDataFilter = mediaDataManager.mediaDataFilter;
            Objects.requireNonNull(mediaDataFilter);
            mediaDataFilter._listeners.remove(mediaHost$listener$1);
        }
    }

    public final void updateViewVisibility() {
        boolean z;
        MediaHostStateHolder mediaHostStateHolder = this.state;
        int i = 0;
        if (getShowsOnlyActiveMedia()) {
            z = this.mediaDataManager.hasActiveMedia();
        } else {
            MediaDataManager mediaDataManager = this.mediaDataManager;
            Objects.requireNonNull(mediaDataManager);
            MediaDataFilter mediaDataFilter = mediaDataManager.mediaDataFilter;
            Objects.requireNonNull(mediaDataFilter);
            if (!(!mediaDataFilter.userEntries.isEmpty())) {
                SmartspaceMediaData smartspaceMediaData = mediaDataFilter.smartspaceMediaData;
                Objects.requireNonNull(smartspaceMediaData);
                if (!smartspaceMediaData.isActive) {
                    z = false;
                }
            }
            z = true;
        }
        Objects.requireNonNull(mediaHostStateHolder);
        if (mediaHostStateHolder.visible != z) {
            mediaHostStateHolder.visible = z;
            Function0<Unit> function0 = mediaHostStateHolder.changedListener;
            if (function0 != null) {
                function0.invoke();
            }
        }
        if (!getVisible()) {
            i = 8;
        }
        if (i != getHostView().getVisibility()) {
            getHostView().setVisibility(i);
            Iterator<Function1<Boolean, Unit>> it = this.visibleChangedListeners.iterator();
            while (it.hasNext()) {
                it.next().invoke(Boolean.valueOf(getVisible()));
            }
        }
    }

    /* JADX WARN: Type inference failed for: r1v6, types: [com.android.systemui.media.MediaHost$listener$1] */
    public MediaHost(MediaHostStateHolder mediaHostStateHolder, MediaHierarchyManager mediaHierarchyManager, MediaDataManager mediaDataManager, MediaHostStatesManager mediaHostStatesManager) {
        this.state = mediaHostStateHolder;
        this.mediaHierarchyManager = mediaHierarchyManager;
        this.mediaDataManager = mediaDataManager;
        this.mediaHostStatesManager = mediaHostStatesManager;
    }

    public final Rect getCurrentBounds() {
        getHostView().getLocationOnScreen(this.tmpLocationOnScreen);
        int i = 0;
        int paddingLeft = getHostView().getPaddingLeft() + this.tmpLocationOnScreen[0];
        int paddingTop = getHostView().getPaddingTop() + this.tmpLocationOnScreen[1];
        int width = (getHostView().getWidth() + this.tmpLocationOnScreen[0]) - getHostView().getPaddingRight();
        int height = (getHostView().getHeight() + this.tmpLocationOnScreen[1]) - getHostView().getPaddingBottom();
        if (width < paddingLeft) {
            paddingLeft = 0;
            width = 0;
        }
        if (height < paddingTop) {
            height = 0;
        } else {
            i = paddingTop;
        }
        this.currentBounds.set(paddingLeft, i, width, height);
        return this.currentBounds;
    }
}
