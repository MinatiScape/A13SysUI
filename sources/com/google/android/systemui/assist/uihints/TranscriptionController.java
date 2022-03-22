package com.google.android.systemui.assist.uihints;

import android.app.PendingIntent;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import com.android.systemui.qs.external.TileServices$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.notification.row.NotificationInfo$$ExternalSyntheticLambda2;
import com.android.systemui.statusbar.phone.BiometricUnlockController$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda4;
import com.android.wm.shell.pip.phone.PipMenuView$$ExternalSyntheticLambda1;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import com.google.android.systemui.assist.uihints.TranscriptionController;
import com.google.android.systemui.assist.uihints.input.TouchActionRegion;
import com.google.android.systemui.assist.uihints.input.TouchInsideRegion;
import com.google.android.systemui.gamedashboard.GameMenuActivity$$ExternalSyntheticLambda1;
import com.google.common.base.Function;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public final class TranscriptionController implements NgaMessageHandler.CardInfoListener, NgaMessageHandler.TranscriptionInfoListener, NgaMessageHandler.GreetingInfoListener, NgaMessageHandler.ChipsInfoListener, NgaMessageHandler.ClearListener, ConfigurationController.ConfigurationListener, TouchActionRegion, TouchInsideRegion {
    public State mCurrentState;
    public final TouchInsideHandler mDefaultOnTap;
    public final FlingVelocityWrapper mFlingVelocity;
    public ListenableFuture<Void> mHideFuture;
    public TranscriptionSpaceListener mListener;
    public PendingIntent mOnGreetingTap;
    public PendingIntent mOnTranscriptionTap;
    public Runnable mQueuedCompletion;
    public State mQueuedState;
    public HashMap mViewMap;
    public boolean mHasAccurateBackground = false;
    public boolean mQueuedStateAnimates = false;

    /* loaded from: classes.dex */
    public enum State {
        TRANSCRIPTION,
        GREETING,
        CHIPS,
        NONE
    }

    /* loaded from: classes.dex */
    public interface TranscriptionSpaceListener {
    }

    /* loaded from: classes.dex */
    public interface TranscriptionSpaceView {
        void getHitRect(Rect rect);

        ListenableFuture<Void> hide(boolean z);

        void onFontSizeChanged();

        default void setCardVisible(boolean z) {
        }

        void setHasDarkBackground(boolean z);
    }

    public final Optional<Region> getTouchRegion() {
        TranscriptionSpaceView transcriptionSpaceView = (TranscriptionSpaceView) this.mViewMap.get(this.mCurrentState);
        if (transcriptionSpaceView == null) {
            return Optional.empty();
        }
        Rect rect = new Rect();
        transcriptionSpaceView.getHitRect(rect);
        return Optional.of(new Region(rect));
    }

    public final boolean hasTapAction() {
        int ordinal = this.mCurrentState.ordinal();
        if (ordinal != 0) {
            if (ordinal != 1) {
                if (ordinal != 2) {
                    return false;
                }
                return true;
            } else if (this.mOnGreetingTap != null) {
                return true;
            } else {
                return false;
            }
        } else if (this.mOnTranscriptionTap != null) {
            return true;
        } else {
            return false;
        }
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [com.google.android.systemui.assist.uihints.TranscriptionController$$ExternalSyntheticLambda0] */
    public final void maybeSetState() {
        boolean z;
        State state = State.NONE;
        State state2 = this.mCurrentState;
        State state3 = this.mQueuedState;
        if (state2 == state3) {
            Runnable runnable = this.mQueuedCompletion;
            if (runnable != null) {
                runnable.run();
            }
        } else if (this.mHasAccurateBackground || state3 == state) {
            ListenableFuture<Void> listenableFuture = this.mHideFuture;
            if (listenableFuture == null || listenableFuture.isDone()) {
                State state4 = this.mQueuedState;
                TranscriptionSpaceListener transcriptionSpaceListener = this.mListener;
                if (transcriptionSpaceListener != null) {
                    ScrimController scrimController = (ScrimController) transcriptionSpaceListener;
                    if (state4 != state) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (scrimController.mTranscriptionVisible != z) {
                        scrimController.mTranscriptionVisible = z;
                        scrimController.refresh();
                    }
                }
                if (state.equals(this.mCurrentState)) {
                    this.mCurrentState = this.mQueuedState;
                    Runnable runnable2 = this.mQueuedCompletion;
                    if (runnable2 != null) {
                        runnable2.run();
                        return;
                    }
                    return;
                }
                ListenableFuture<Void> hide = ((TranscriptionSpaceView) this.mViewMap.get(this.mCurrentState)).hide(this.mQueuedStateAnimates);
                this.mHideFuture = hide;
                Futures.transform(hide, new Function() { // from class: com.google.android.systemui.assist.uihints.TranscriptionController$$ExternalSyntheticLambda0
                    @Override // com.google.common.base.Function
                    public final Object apply(Object obj) {
                        TranscriptionController transcriptionController = TranscriptionController.this;
                        Void r1 = (Void) obj;
                        Objects.requireNonNull(transcriptionController);
                        transcriptionController.mCurrentState = transcriptionController.mQueuedState;
                        Runnable runnable3 = transcriptionController.mQueuedCompletion;
                        if (runnable3 == null) {
                            return null;
                        }
                        runnable3.run();
                        return null;
                    }
                });
            }
        }
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.CardInfoListener
    public final void onCardInfo(boolean z, int i, boolean z2, boolean z3) {
        for (TranscriptionSpaceView transcriptionSpaceView : this.mViewMap.values()) {
            transcriptionSpaceView.setCardVisible(z);
        }
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.ChipsInfoListener
    public final void onChipsInfo(final ArrayList arrayList) {
        Optional optional;
        State state = State.CHIPS;
        if (arrayList == null || arrayList.size() == 0) {
            Log.e("TranscriptionController", "Null or empty chip list received; clearing transcription space");
            onClear(false);
            return;
        }
        FlingVelocityWrapper flingVelocityWrapper = this.mFlingVelocity;
        Objects.requireNonNull(flingVelocityWrapper);
        if (flingVelocityWrapper.mGuarded) {
            optional = Optional.empty();
        } else {
            flingVelocityWrapper.mGuarded = true;
            optional = Optional.of(Float.valueOf(flingVelocityWrapper.mVelocity));
        }
        if (this.mCurrentState != State.NONE || !optional.isPresent()) {
            State state2 = this.mCurrentState;
            if (state2 == State.GREETING || state2 == State.TRANSCRIPTION) {
                setQueuedState(state, false, new TileServices$$ExternalSyntheticLambda0(this, arrayList, 2));
            } else {
                setQueuedState(state, false, new Runnable() { // from class: com.google.android.systemui.assist.uihints.TranscriptionController$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        TranscriptionController transcriptionController = TranscriptionController.this;
                        List<Bundle> list = arrayList;
                        Objects.requireNonNull(transcriptionController);
                        ChipsContainer chipsContainer = (ChipsContainer) transcriptionController.mViewMap.get(TranscriptionController.State.CHIPS);
                        Objects.requireNonNull(chipsContainer);
                        chipsContainer.mChips = list;
                        chipsContainer.setChipsInternal();
                        chipsContainer.setVisibility(0);
                    }
                });
            }
        } else {
            setQueuedState(state, false, new NotificationInfo$$ExternalSyntheticLambda2(this, arrayList, optional, 1));
        }
        maybeSetState();
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.ClearListener
    public final void onClear(boolean z) {
        setQueuedState(State.NONE, z, null);
        maybeSetState();
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public final void onDensityOrFontScaleChanged() {
        for (TranscriptionSpaceView transcriptionSpaceView : this.mViewMap.values()) {
            transcriptionSpaceView.onFontSizeChanged();
        }
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.GreetingInfoListener
    public final void onGreetingInfo(final String str, PendingIntent pendingIntent) {
        final Optional optional;
        State state = State.GREETING;
        if (!TextUtils.isEmpty(str)) {
            this.mOnGreetingTap = pendingIntent;
            FlingVelocityWrapper flingVelocityWrapper = this.mFlingVelocity;
            Objects.requireNonNull(flingVelocityWrapper);
            if (flingVelocityWrapper.mGuarded) {
                optional = Optional.empty();
            } else {
                flingVelocityWrapper.mGuarded = true;
                optional = Optional.of(Float.valueOf(flingVelocityWrapper.mVelocity));
            }
            if (this.mCurrentState != State.NONE || !optional.isPresent()) {
                setQueuedState(state, false, new PipTaskOrganizer$$ExternalSyntheticLambda4(this, str, 5));
            } else {
                setQueuedState(state, false, new Runnable() { // from class: com.google.android.systemui.assist.uihints.TranscriptionController$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        TranscriptionController transcriptionController = TranscriptionController.this;
                        String str2 = str;
                        Optional optional2 = optional;
                        Objects.requireNonNull(transcriptionController);
                        ((GreetingView) transcriptionController.mViewMap.get(TranscriptionController.State.GREETING)).setGreetingAnimated(str2, ((Float) optional2.get()).floatValue());
                    }
                });
            }
            maybeSetState();
        }
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.TranscriptionInfoListener
    public final void onTranscriptionInfo(String str, PendingIntent pendingIntent, int i) {
        this.mOnTranscriptionTap = pendingIntent;
        State state = State.TRANSCRIPTION;
        setQueuedState(state, false, new BiometricUnlockController$$ExternalSyntheticLambda0(this, str, 2));
        maybeSetState();
        TranscriptionView transcriptionView = (TranscriptionView) this.mViewMap.get(state);
        Objects.requireNonNull(transcriptionView);
        transcriptionView.mRequestedTextColor = i;
        transcriptionView.updateColor();
    }

    public final void setQueuedState(State state, boolean z, Runnable runnable) {
        this.mQueuedState = state;
        this.mQueuedStateAnimates = z;
        this.mQueuedCompletion = runnable;
    }

    public TranscriptionController(ViewGroup viewGroup, TouchInsideHandler touchInsideHandler, FlingVelocityWrapper flingVelocityWrapper, ConfigurationController configurationController) {
        this.mViewMap = new HashMap();
        State state = State.NONE;
        this.mCurrentState = state;
        this.mQueuedState = state;
        this.mDefaultOnTap = touchInsideHandler;
        this.mFlingVelocity = flingVelocityWrapper;
        this.mViewMap = new HashMap();
        TranscriptionView transcriptionView = (TranscriptionView) viewGroup.findViewById(2131429084);
        transcriptionView.setOnClickListener(new PipMenuView$$ExternalSyntheticLambda1(this, 3));
        transcriptionView.setOnTouchListener(touchInsideHandler);
        this.mViewMap.put(State.TRANSCRIPTION, transcriptionView);
        GreetingView greetingView = (GreetingView) viewGroup.findViewById(2131428040);
        greetingView.setOnClickListener(new GameMenuActivity$$ExternalSyntheticLambda1(this, 2));
        greetingView.setOnTouchListener(touchInsideHandler);
        this.mViewMap.put(State.GREETING, greetingView);
        this.mViewMap.put(State.CHIPS, (ChipsContainer) viewGroup.findViewById(2131427704));
        configurationController.addCallback(this);
    }

    @Override // com.google.android.systemui.assist.uihints.input.TouchActionRegion
    public final Optional<Region> getTouchActionRegion() {
        if (hasTapAction()) {
            return getTouchRegion();
        }
        return Optional.empty();
    }

    @Override // com.google.android.systemui.assist.uihints.input.TouchInsideRegion
    public final Optional<Region> getTouchInsideRegion() {
        if (hasTapAction()) {
            return Optional.empty();
        }
        return getTouchRegion();
    }
}
