package com.android.systemui.unfold;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.devicestate.DeviceStateManager;
import android.hardware.display.DisplayManager;
import android.hardware.input.InputManager;
import android.os.IBinder;
import android.os.Trace;
import android.view.Choreographer;
import android.view.Display;
import android.view.DisplayInfo;
import android.view.IRotationWatcher;
import android.view.IWindowManager;
import android.view.SurfaceControl;
import android.view.SurfaceControlViewHost;
import android.view.SurfaceSession;
import android.view.WindowManager;
import android.view.WindowlessWindowManager;
import com.android.systemui.statusbar.LightRevealScrim;
import com.android.systemui.statusbar.LinearLightRevealEffect;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import com.android.wm.shell.displayareahelper.DisplayAreaHelper;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.FilteringSequence$iterator$1;
import kotlin.sequences.SequencesKt___SequencesKt;
import kotlin.sequences.TransformingSequence;
/* compiled from: UnfoldLightRevealOverlayAnimation.kt */
/* loaded from: classes.dex */
public final class UnfoldLightRevealOverlayAnimation {
    public final Executor backgroundExecutor;
    public final Context context;
    public int currentRotation;
    public final DeviceStateManager deviceStateManager;
    public final Optional<DisplayAreaHelper> displayAreaHelper;
    public final DisplayManager displayManager;
    public final Executor executor;
    public boolean isFolded;
    public SurfaceControl overlayContainer;
    public SurfaceControlViewHost root;
    public LightRevealScrim scrimView;
    public final UnfoldTransitionProgressProvider unfoldTransitionProgressProvider;
    public DisplayInfo unfoldedDisplayInfo;
    public final IWindowManager windowManagerInterface;
    public WindowlessWindowManager wwm;
    public final TransitionListener transitionListener = new TransitionListener();
    public final RotationWatcher rotationWatcher = new RotationWatcher();
    public boolean isUnfoldHandled = true;

    /* compiled from: UnfoldLightRevealOverlayAnimation.kt */
    /* loaded from: classes.dex */
    public final class FoldListener extends DeviceStateManager.FoldStateListener {
        public FoldListener(final UnfoldLightRevealOverlayAnimation unfoldLightRevealOverlayAnimation) {
            super(unfoldLightRevealOverlayAnimation.context, new Consumer() { // from class: com.android.systemui.unfold.UnfoldLightRevealOverlayAnimation.FoldListener.1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    Boolean bool = (Boolean) obj;
                    if (bool.booleanValue()) {
                        UnfoldLightRevealOverlayAnimation.this.ensureOverlayRemoved();
                        UnfoldLightRevealOverlayAnimation.this.isUnfoldHandled = false;
                    }
                    UnfoldLightRevealOverlayAnimation.this.isFolded = bool.booleanValue();
                }
            });
        }
    }

    /* compiled from: UnfoldLightRevealOverlayAnimation.kt */
    /* loaded from: classes.dex */
    public final class RotationWatcher extends IRotationWatcher.Stub {
        public RotationWatcher() {
        }

        public final void onRotationChanged(int i) {
            boolean z;
            UnfoldLightRevealOverlayAnimation unfoldLightRevealOverlayAnimation = UnfoldLightRevealOverlayAnimation.this;
            Trace.beginSection("UnfoldLightRevealOverlayAnimation#onRotationChanged");
            try {
                if (unfoldLightRevealOverlayAnimation.currentRotation != i) {
                    unfoldLightRevealOverlayAnimation.currentRotation = i;
                    LightRevealScrim lightRevealScrim = unfoldLightRevealOverlayAnimation.scrimView;
                    if (lightRevealScrim != null) {
                        if (!(i == 0 || i == 2)) {
                            z = false;
                            lightRevealScrim.setRevealEffect(new LinearLightRevealEffect(z));
                        }
                        z = true;
                        lightRevealScrim.setRevealEffect(new LinearLightRevealEffect(z));
                    }
                    SurfaceControlViewHost surfaceControlViewHost = unfoldLightRevealOverlayAnimation.root;
                    if (surfaceControlViewHost != null) {
                        surfaceControlViewHost.relayout(unfoldLightRevealOverlayAnimation.getLayoutParams());
                    }
                }
            } finally {
                Trace.endSection();
            }
        }
    }

    /* compiled from: UnfoldLightRevealOverlayAnimation.kt */
    /* loaded from: classes.dex */
    public final class TransitionListener implements UnfoldTransitionProgressProvider.TransitionProgressListener {
        public TransitionListener() {
        }

        @Override // com.android.systemui.unfold.UnfoldTransitionProgressProvider.TransitionProgressListener
        public final void onTransitionFinished() {
            UnfoldLightRevealOverlayAnimation.this.ensureOverlayRemoved();
        }

        @Override // com.android.systemui.unfold.UnfoldTransitionProgressProvider.TransitionProgressListener
        public final void onTransitionProgress(float f) {
            LightRevealScrim lightRevealScrim = UnfoldLightRevealOverlayAnimation.this.scrimView;
            if (lightRevealScrim != null) {
                lightRevealScrim.setRevealAmount(f);
            }
        }

        @Override // com.android.systemui.unfold.UnfoldTransitionProgressProvider.TransitionProgressListener
        public final void onTransitionStarted() {
            UnfoldLightRevealOverlayAnimation unfoldLightRevealOverlayAnimation = UnfoldLightRevealOverlayAnimation.this;
            if (unfoldLightRevealOverlayAnimation.scrimView == null) {
                unfoldLightRevealOverlayAnimation.addView(null);
            }
            InputManager.getInstance().cancelCurrentTouch();
        }
    }

    public final void addView(final Runnable runnable) {
        boolean z;
        if (this.wwm != null) {
            ensureOverlayRemoved();
            Context context = this.context;
            Display display = context.getDisplay();
            Intrinsics.checkNotNull(display);
            WindowlessWindowManager windowlessWindowManager = this.wwm;
            if (windowlessWindowManager == null) {
                windowlessWindowManager = null;
            }
            SurfaceControlViewHost surfaceControlViewHost = new SurfaceControlViewHost(context, display, windowlessWindowManager, false);
            LightRevealScrim lightRevealScrim = new LightRevealScrim(this.context, null);
            int i = this.currentRotation;
            if (i == 0 || i == 2) {
                z = true;
            } else {
                z = false;
            }
            lightRevealScrim.setRevealEffect(new LinearLightRevealEffect(z));
            lightRevealScrim.isScrimOpaqueChangedListener = UnfoldLightRevealOverlayAnimation$addView$newView$1$1.INSTANCE;
            lightRevealScrim.setRevealAmount(0.0f);
            WindowManager.LayoutParams layoutParams = getLayoutParams();
            surfaceControlViewHost.setView(lightRevealScrim, layoutParams);
            if (runnable != null) {
                Trace.beginAsyncSection("UnfoldLightRevealOverlayAnimation#relayout", 0);
                surfaceControlViewHost.relayout(layoutParams, new WindowlessWindowManager.ResizeCompleteCallback() { // from class: com.android.systemui.unfold.UnfoldLightRevealOverlayAnimation$addView$2$1
                    public final void finished(final SurfaceControl.Transaction transaction) {
                        final long vsyncId = Choreographer.getSfInstance().getVsyncId();
                        Executor executor = UnfoldLightRevealOverlayAnimation.this.backgroundExecutor;
                        final Runnable runnable2 = runnable;
                        executor.execute(new Runnable() { // from class: com.android.systemui.unfold.UnfoldLightRevealOverlayAnimation$addView$2$1.1
                            @Override // java.lang.Runnable
                            public final void run() {
                                transaction.setFrameTimelineVsync(vsyncId).apply(true);
                                transaction.setFrameTimelineVsync(vsyncId + 1).apply(true);
                                Trace.endAsyncSection("UnfoldLightRevealOverlayAnimation#relayout", 0);
                                runnable2.run();
                            }
                        });
                    }
                });
            }
            this.scrimView = lightRevealScrim;
            this.root = surfaceControlViewHost;
        } else if (runnable != null) {
            runnable.run();
        }
    }

    public final void ensureOverlayRemoved() {
        SurfaceControlViewHost surfaceControlViewHost = this.root;
        if (surfaceControlViewHost != null) {
            surfaceControlViewHost.release();
        }
        this.root = null;
        this.scrimView = null;
    }

    public final WindowManager.LayoutParams getLayoutParams() {
        boolean z;
        int i;
        int i2;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        int i3 = this.currentRotation;
        if (i3 == 0 || i3 == 2) {
            z = true;
        } else {
            z = false;
        }
        DisplayInfo displayInfo = null;
        DisplayInfo displayInfo2 = this.unfoldedDisplayInfo;
        if (z) {
            if (displayInfo2 == null) {
                displayInfo2 = null;
            }
            i = displayInfo2.getNaturalHeight();
        } else {
            if (displayInfo2 == null) {
                displayInfo2 = null;
            }
            i = displayInfo2.getNaturalWidth();
        }
        layoutParams.height = i;
        if (z) {
            DisplayInfo displayInfo3 = this.unfoldedDisplayInfo;
            if (displayInfo3 != null) {
                displayInfo = displayInfo3;
            }
            i2 = displayInfo.getNaturalWidth();
        } else {
            DisplayInfo displayInfo4 = this.unfoldedDisplayInfo;
            if (displayInfo4 != null) {
                displayInfo = displayInfo4;
            }
            i2 = displayInfo.getNaturalHeight();
        }
        layoutParams.width = i2;
        layoutParams.format = -3;
        layoutParams.type = 2026;
        layoutParams.setTitle("Unfold Light Reveal Animation");
        layoutParams.layoutInDisplayCutoutMode = 3;
        layoutParams.setFitInsetsTypes(0);
        layoutParams.flags = 8;
        layoutParams.setTrustedOverlay();
        layoutParams.packageName = this.context.getOpPackageName();
        return layoutParams;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v4, types: [com.android.systemui.unfold.UnfoldLightRevealOverlayAnimation$init$1] */
    public final void init() {
        Object obj;
        this.deviceStateManager.registerCallback(this.executor, new FoldListener(this));
        this.unfoldTransitionProgressProvider.addCallback(this.transitionListener);
        this.windowManagerInterface.watchRotation(this.rotationWatcher, this.context.getDisplay().getDisplayId());
        this.displayAreaHelper.get().attachToRootDisplayArea(new SurfaceControl.Builder(new SurfaceSession()).setContainerLayer().setName("unfold-overlay-container"), new Consumer() { // from class: com.android.systemui.unfold.UnfoldLightRevealOverlayAnimation$init$1
            @Override // java.util.function.Consumer
            public final void accept(Object obj2) {
                final SurfaceControl.Builder builder = (SurfaceControl.Builder) obj2;
                final UnfoldLightRevealOverlayAnimation unfoldLightRevealOverlayAnimation = UnfoldLightRevealOverlayAnimation.this;
                unfoldLightRevealOverlayAnimation.executor.execute(new Runnable() { // from class: com.android.systemui.unfold.UnfoldLightRevealOverlayAnimation$init$1.1
                    @Override // java.lang.Runnable
                    public final void run() {
                        UnfoldLightRevealOverlayAnimation.this.overlayContainer = builder.build();
                        SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
                        SurfaceControl surfaceControl = UnfoldLightRevealOverlayAnimation.this.overlayContainer;
                        if (surfaceControl == null) {
                            surfaceControl = null;
                        }
                        SurfaceControl.Transaction layer = transaction.setLayer(surfaceControl, Integer.MAX_VALUE);
                        SurfaceControl surfaceControl2 = UnfoldLightRevealOverlayAnimation.this.overlayContainer;
                        if (surfaceControl2 == null) {
                            surfaceControl2 = null;
                        }
                        layer.show(surfaceControl2).apply();
                        UnfoldLightRevealOverlayAnimation unfoldLightRevealOverlayAnimation2 = UnfoldLightRevealOverlayAnimation.this;
                        Configuration configuration = UnfoldLightRevealOverlayAnimation.this.context.getResources().getConfiguration();
                        SurfaceControl surfaceControl3 = UnfoldLightRevealOverlayAnimation.this.overlayContainer;
                        if (surfaceControl3 == null) {
                            surfaceControl3 = null;
                        }
                        unfoldLightRevealOverlayAnimation2.wwm = new WindowlessWindowManager(configuration, surfaceControl3, (IBinder) null);
                    }
                });
            }
        });
        FilteringSequence$iterator$1 filteringSequence$iterator$1 = new FilteringSequence$iterator$1(SequencesKt___SequencesKt.filter(new TransformingSequence(ArraysKt___ArraysKt.asSequence(this.displayManager.getDisplays()), UnfoldLightRevealOverlayAnimation$getUnfoldedDisplayInfo$1.INSTANCE), UnfoldLightRevealOverlayAnimation$getUnfoldedDisplayInfo$2.INSTANCE));
        if (!filteringSequence$iterator$1.hasNext()) {
            obj = null;
        } else {
            obj = filteringSequence$iterator$1.next();
            if (filteringSequence$iterator$1.hasNext()) {
                int naturalWidth = ((DisplayInfo) obj).getNaturalWidth();
                do {
                    Object next = filteringSequence$iterator$1.next();
                    int naturalWidth2 = ((DisplayInfo) next).getNaturalWidth();
                    if (naturalWidth < naturalWidth2) {
                        obj = next;
                        naturalWidth = naturalWidth2;
                    }
                } while (filteringSequence$iterator$1.hasNext());
            }
        }
        Intrinsics.checkNotNull(obj);
        this.unfoldedDisplayInfo = (DisplayInfo) obj;
    }

    public UnfoldLightRevealOverlayAnimation(Context context, DeviceStateManager deviceStateManager, DisplayManager displayManager, UnfoldTransitionProgressProvider unfoldTransitionProgressProvider, Optional<DisplayAreaHelper> optional, Executor executor, Executor executor2, IWindowManager iWindowManager) {
        this.context = context;
        this.deviceStateManager = deviceStateManager;
        this.displayManager = displayManager;
        this.unfoldTransitionProgressProvider = unfoldTransitionProgressProvider;
        this.displayAreaHelper = optional;
        this.executor = executor;
        this.backgroundExecutor = executor2;
        this.windowManagerInterface = iWindowManager;
        Display display = context.getDisplay();
        Intrinsics.checkNotNull(display);
        this.currentRotation = display.getRotation();
    }
}
