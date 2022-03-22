package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.IBinder;
import android.os.UserHandle;
import android.service.controls.Control;
import android.service.controls.IControlsActionCallback;
import android.service.controls.IControlsSubscriber;
import android.service.controls.IControlsSubscription;
import android.service.controls.actions.ControlAction;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.controls.controller.ControlsBindingController;
import com.android.systemui.controls.controller.ControlsBindingControllerImpl;
import com.android.systemui.controls.controller.ControlsProviderLifecycleManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.Lazy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.collections.CollectionsKt__IteratorsJVMKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: ControlsBindingControllerImpl.kt */
@VisibleForTesting
/* loaded from: classes.dex */
public class ControlsBindingControllerImpl implements ControlsBindingController {
    public static final ControlsBindingControllerImpl$Companion$emptyCallback$1 emptyCallback = new ControlsBindingController.LoadCallback() { // from class: com.android.systemui.controls.controller.ControlsBindingControllerImpl$Companion$emptyCallback$1
        @Override // com.android.systemui.controls.controller.ControlsBindingController.LoadCallback
        public final void error(String str) {
        }

        @Override // java.util.function.Consumer
        public final /* bridge */ /* synthetic */ void accept(List<? extends Control> list) {
        }
    };
    public final ControlsBindingControllerImpl$actionCallbackService$1 actionCallbackService = new IControlsActionCallback.Stub() { // from class: com.android.systemui.controls.controller.ControlsBindingControllerImpl$actionCallbackService$1
        public final void accept(IBinder iBinder, String str, int i) {
            ControlsBindingControllerImpl controlsBindingControllerImpl = ControlsBindingControllerImpl.this;
            controlsBindingControllerImpl.backgroundExecutor.execute(new ControlsBindingControllerImpl.OnActionResponseRunnable(iBinder, str, i));
        }
    };
    public final DelayableExecutor backgroundExecutor;
    public final Context context;
    public ControlsProviderLifecycleManager currentProvider;
    public UserHandle currentUser;
    public final Lazy<ControlsController> lazyController;
    public StatefulControlSubscriber statefulControlSubscriber;

    /* compiled from: ControlsBindingControllerImpl.kt */
    /* loaded from: classes.dex */
    public abstract class CallbackRunnable implements Runnable {
        public final ControlsProviderLifecycleManager provider;
        public final IBinder token;

        public abstract void doRun();

        public CallbackRunnable(IBinder iBinder) {
            this.token = iBinder;
            this.provider = ControlsBindingControllerImpl.this.currentProvider;
        }

        @Override // java.lang.Runnable
        public final void run() {
            ControlsProviderLifecycleManager controlsProviderLifecycleManager = this.provider;
            if (controlsProviderLifecycleManager == null) {
                Log.e("ControlsBindingControllerImpl", "No current provider set");
            } else if (!Intrinsics.areEqual(controlsProviderLifecycleManager.user, ControlsBindingControllerImpl.this.currentUser)) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("User ");
                ControlsProviderLifecycleManager controlsProviderLifecycleManager2 = this.provider;
                Objects.requireNonNull(controlsProviderLifecycleManager2);
                m.append(controlsProviderLifecycleManager2.user);
                m.append(" is not current user");
                Log.e("ControlsBindingControllerImpl", m.toString());
            } else {
                IBinder iBinder = this.token;
                ControlsProviderLifecycleManager controlsProviderLifecycleManager3 = this.provider;
                Objects.requireNonNull(controlsProviderLifecycleManager3);
                if (!Intrinsics.areEqual(iBinder, controlsProviderLifecycleManager3.token)) {
                    StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Provider for token:");
                    m2.append(this.token);
                    m2.append(" does not exist anymore");
                    Log.e("ControlsBindingControllerImpl", m2.toString());
                    return;
                }
                doRun();
            }
        }
    }

    /* compiled from: ControlsBindingControllerImpl.kt */
    /* loaded from: classes.dex */
    public final class LoadSubscriber extends IControlsSubscriber.Stub {
        public Lambda _loadCancelInternal;
        public ControlsBindingController.LoadCallback callback;
        public final long requestLimit;
        public IControlsSubscription subscription;
        public final ArrayList<Control> loadedControls = new ArrayList<>();
        public AtomicBoolean isTerminated = new AtomicBoolean(false);

        public LoadSubscriber(ControlsBindingController.LoadCallback loadCallback, long j) {
            this.callback = loadCallback;
            this.requestLimit = j;
        }

        public final void maybeTerminateAndRun(final CallbackRunnable callbackRunnable) {
            if (!this.isTerminated.get()) {
                this._loadCancelInternal = ControlsBindingControllerImpl$LoadSubscriber$maybeTerminateAndRun$1.INSTANCE;
                this.callback = ControlsBindingControllerImpl.emptyCallback;
                ControlsProviderLifecycleManager controlsProviderLifecycleManager = ControlsBindingControllerImpl.this.currentProvider;
                if (controlsProviderLifecycleManager != null) {
                    Runnable runnable = controlsProviderLifecycleManager.onLoadCanceller;
                    if (runnable != null) {
                        runnable.run();
                    }
                    controlsProviderLifecycleManager.onLoadCanceller = null;
                }
                ControlsBindingControllerImpl.this.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsBindingControllerImpl$LoadSubscriber$maybeTerminateAndRun$2
                    @Override // java.lang.Runnable
                    public final void run() {
                        ControlsBindingControllerImpl.LoadSubscriber.this.isTerminated.compareAndSet(false, true);
                        callbackRunnable.run();
                    }
                });
            }
        }

        public final void onComplete(IBinder iBinder) {
            maybeTerminateAndRun(new OnLoadRunnable(ControlsBindingControllerImpl.this, iBinder, this.loadedControls, this.callback));
        }

        public final void onError(IBinder iBinder, String str) {
            maybeTerminateAndRun(new OnLoadErrorRunnable(ControlsBindingControllerImpl.this, iBinder, str, this.callback));
        }

        public final void onNext(final IBinder iBinder, final Control control) {
            final ControlsBindingControllerImpl controlsBindingControllerImpl = ControlsBindingControllerImpl.this;
            controlsBindingControllerImpl.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsBindingControllerImpl$LoadSubscriber$onNext$1
                @Override // java.lang.Runnable
                public final void run() {
                    if (!ControlsBindingControllerImpl.LoadSubscriber.this.isTerminated.get()) {
                        ControlsBindingControllerImpl.LoadSubscriber loadSubscriber = ControlsBindingControllerImpl.LoadSubscriber.this;
                        Objects.requireNonNull(loadSubscriber);
                        loadSubscriber.loadedControls.add(control);
                        ControlsBindingControllerImpl.LoadSubscriber loadSubscriber2 = ControlsBindingControllerImpl.LoadSubscriber.this;
                        Objects.requireNonNull(loadSubscriber2);
                        ControlsBindingControllerImpl.LoadSubscriber loadSubscriber3 = ControlsBindingControllerImpl.LoadSubscriber.this;
                        Objects.requireNonNull(loadSubscriber3);
                        if (loadSubscriber2.loadedControls.size() >= loadSubscriber3.requestLimit) {
                            ControlsBindingControllerImpl.LoadSubscriber loadSubscriber4 = ControlsBindingControllerImpl.LoadSubscriber.this;
                            ControlsBindingControllerImpl controlsBindingControllerImpl2 = controlsBindingControllerImpl;
                            IBinder iBinder2 = iBinder;
                            Objects.requireNonNull(loadSubscriber4);
                            ArrayList<Control> arrayList = loadSubscriber4.loadedControls;
                            ControlsBindingControllerImpl.LoadSubscriber loadSubscriber5 = ControlsBindingControllerImpl.LoadSubscriber.this;
                            IControlsSubscription iControlsSubscription = loadSubscriber5.subscription;
                            if (iControlsSubscription == null) {
                                iControlsSubscription = null;
                            }
                            loadSubscriber4.maybeTerminateAndRun(new ControlsBindingControllerImpl.OnCancelAndLoadRunnable(controlsBindingControllerImpl2, iBinder2, arrayList, iControlsSubscription, loadSubscriber5.callback));
                        }
                    }
                }
            });
        }

        public final void onSubscribe(IBinder iBinder, IControlsSubscription iControlsSubscription) {
            this.subscription = iControlsSubscription;
            ControlsBindingControllerImpl controlsBindingControllerImpl = ControlsBindingControllerImpl.this;
            this._loadCancelInternal = new ControlsBindingControllerImpl$LoadSubscriber$onSubscribe$1(controlsBindingControllerImpl, this);
            controlsBindingControllerImpl.backgroundExecutor.execute(new OnSubscribeRunnable(controlsBindingControllerImpl, iBinder, iControlsSubscription, this.requestLimit));
        }
    }

    /* compiled from: ControlsBindingControllerImpl.kt */
    /* loaded from: classes.dex */
    public final class OnActionResponseRunnable extends CallbackRunnable {
        public final String controlId;
        public final int response;

        public OnActionResponseRunnable(IBinder iBinder, String str, int i) {
            super(iBinder);
            this.controlId = str;
            this.response = i;
        }

        @Override // com.android.systemui.controls.controller.ControlsBindingControllerImpl.CallbackRunnable
        public final void doRun() {
            ControlsProviderLifecycleManager controlsProviderLifecycleManager = this.provider;
            if (controlsProviderLifecycleManager != null) {
                ControlsBindingControllerImpl.this.lazyController.get().onActionResponse(controlsProviderLifecycleManager.componentName, this.controlId, this.response);
            }
        }
    }

    /* compiled from: ControlsBindingControllerImpl.kt */
    /* loaded from: classes.dex */
    public final class OnCancelAndLoadRunnable extends CallbackRunnable {
        public final ControlsBindingController.LoadCallback callback;
        public final List<Control> list;
        public final IControlsSubscription subscription;

        @Override // com.android.systemui.controls.controller.ControlsBindingControllerImpl.CallbackRunnable
        public final void doRun() {
            Log.d("ControlsBindingControllerImpl", "LoadSubscription: Canceling and loading controls");
            ControlsProviderLifecycleManager controlsProviderLifecycleManager = this.provider;
            if (controlsProviderLifecycleManager != null) {
                controlsProviderLifecycleManager.cancelSubscription(this.subscription);
            }
            this.callback.accept(this.list);
        }

        public OnCancelAndLoadRunnable(ControlsBindingControllerImpl controlsBindingControllerImpl, IBinder iBinder, ArrayList arrayList, IControlsSubscription iControlsSubscription, ControlsBindingController.LoadCallback loadCallback) {
            super(iBinder);
            this.list = arrayList;
            this.subscription = iControlsSubscription;
            this.callback = loadCallback;
        }
    }

    /* compiled from: ControlsBindingControllerImpl.kt */
    /* loaded from: classes.dex */
    public final class OnLoadErrorRunnable extends CallbackRunnable {
        public final ControlsBindingController.LoadCallback callback;
        public final String error;

        @Override // com.android.systemui.controls.controller.ControlsBindingControllerImpl.CallbackRunnable
        public final void doRun() {
            this.callback.error(this.error);
            ControlsProviderLifecycleManager controlsProviderLifecycleManager = this.provider;
            if (controlsProviderLifecycleManager != null) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("onError receive from '");
                m.append(controlsProviderLifecycleManager.componentName);
                m.append("': ");
                m.append(this.error);
                Log.e("ControlsBindingControllerImpl", m.toString());
            }
        }

        public OnLoadErrorRunnable(ControlsBindingControllerImpl controlsBindingControllerImpl, IBinder iBinder, String str, ControlsBindingController.LoadCallback loadCallback) {
            super(iBinder);
            this.error = str;
            this.callback = loadCallback;
        }
    }

    /* compiled from: ControlsBindingControllerImpl.kt */
    /* loaded from: classes.dex */
    public final class OnLoadRunnable extends CallbackRunnable {
        public final ControlsBindingController.LoadCallback callback;
        public final List<Control> list;

        @Override // com.android.systemui.controls.controller.ControlsBindingControllerImpl.CallbackRunnable
        public final void doRun() {
            Log.d("ControlsBindingControllerImpl", "LoadSubscription: Complete and loading controls");
            this.callback.accept(this.list);
        }

        public OnLoadRunnable(ControlsBindingControllerImpl controlsBindingControllerImpl, IBinder iBinder, ArrayList arrayList, ControlsBindingController.LoadCallback loadCallback) {
            super(iBinder);
            this.list = arrayList;
            this.callback = loadCallback;
        }
    }

    /* compiled from: ControlsBindingControllerImpl.kt */
    /* loaded from: classes.dex */
    public final class OnSubscribeRunnable extends CallbackRunnable {
        public final long requestLimit;
        public final IControlsSubscription subscription;

        @Override // com.android.systemui.controls.controller.ControlsBindingControllerImpl.CallbackRunnable
        public final void doRun() {
            Log.d("ControlsBindingControllerImpl", "LoadSubscription: Starting subscription");
            ControlsProviderLifecycleManager controlsProviderLifecycleManager = this.provider;
            if (controlsProviderLifecycleManager != null) {
                controlsProviderLifecycleManager.startSubscription(this.subscription, this.requestLimit);
            }
        }

        public OnSubscribeRunnable(ControlsBindingControllerImpl controlsBindingControllerImpl, IBinder iBinder, IControlsSubscription iControlsSubscription, long j) {
            super(iBinder);
            this.subscription = iControlsSubscription;
            this.requestLimit = j;
        }
    }

    @Override // com.android.systemui.controls.controller.ControlsBindingController
    public final void action(ComponentName componentName, ControlInfo controlInfo, ControlAction controlAction) {
        if (this.statefulControlSubscriber == null) {
            Log.w("ControlsBindingControllerImpl", "No actions can occur outside of an active subscription. Ignoring.");
            return;
        }
        ControlsProviderLifecycleManager retrieveLifecycleManager = retrieveLifecycleManager(componentName);
        String str = controlInfo.controlId;
        Objects.requireNonNull(retrieveLifecycleManager);
        retrieveLifecycleManager.invokeOrQueue(new ControlsProviderLifecycleManager.Action(str, controlAction));
    }

    /* JADX WARN: Type inference failed for: r4v2, types: [com.android.systemui.controls.controller.ControlsBindingControllerImpl$LoadSubscriber$loadCancel$1] */
    @Override // com.android.systemui.controls.controller.ControlsBindingController
    public final ControlsBindingControllerImpl$LoadSubscriber$loadCancel$1 bindAndLoad(ComponentName componentName, ControlsControllerImpl$loadForComponent$2 controlsControllerImpl$loadForComponent$2) {
        final LoadSubscriber loadSubscriber = new LoadSubscriber(controlsControllerImpl$loadForComponent$2, 100000L);
        final ControlsProviderLifecycleManager retrieveLifecycleManager = retrieveLifecycleManager(componentName);
        Objects.requireNonNull(retrieveLifecycleManager);
        retrieveLifecycleManager.onLoadCanceller = retrieveLifecycleManager.executor.executeDelayed(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsProviderLifecycleManager$maybeBindAndLoad$1
            @Override // java.lang.Runnable
            public final void run() {
                ControlsProviderLifecycleManager controlsProviderLifecycleManager = ControlsProviderLifecycleManager.this;
                Log.d(controlsProviderLifecycleManager.TAG, Intrinsics.stringPlus("Timeout waiting onLoad for ", controlsProviderLifecycleManager.componentName));
                IControlsSubscriber.Stub stub = loadSubscriber;
                ControlsProviderLifecycleManager controlsProviderLifecycleManager2 = ControlsProviderLifecycleManager.this;
                Objects.requireNonNull(controlsProviderLifecycleManager2);
                stub.onError(controlsProviderLifecycleManager2.token, "Timeout waiting onLoad");
                ControlsProviderLifecycleManager controlsProviderLifecycleManager3 = ControlsProviderLifecycleManager.this;
                Objects.requireNonNull(controlsProviderLifecycleManager3);
                Runnable runnable = controlsProviderLifecycleManager3.onLoadCanceller;
                if (runnable != null) {
                    runnable.run();
                }
                controlsProviderLifecycleManager3.onLoadCanceller = null;
                controlsProviderLifecycleManager3.executor.execute(new ControlsProviderLifecycleManager$bindService$1(controlsProviderLifecycleManager3, false));
            }
        }, 20L, TimeUnit.SECONDS);
        retrieveLifecycleManager.invokeOrQueue(new ControlsProviderLifecycleManager.Load(loadSubscriber));
        return new Runnable() { // from class: com.android.systemui.controls.controller.ControlsBindingControllerImpl$LoadSubscriber$loadCancel$1
            /* JADX WARN: Type inference failed for: r2v2, types: [kotlin.jvm.functions.Function0, kotlin.jvm.internal.Lambda] */
            /* JADX WARN: Unknown variable types count: 1 */
            @Override // java.lang.Runnable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public final void run() {
                /*
                    r2 = this;
                    com.android.systemui.controls.controller.ControlsBindingControllerImpl$LoadSubscriber r2 = com.android.systemui.controls.controller.ControlsBindingControllerImpl.LoadSubscriber.this
                    kotlin.jvm.internal.Lambda r2 = r2._loadCancelInternal
                    if (r2 != 0) goto L_0x0007
                    goto L_0x0011
                L_0x0007:
                    java.lang.String r0 = "ControlsBindingControllerImpl"
                    java.lang.String r1 = "Canceling loadSubscribtion"
                    android.util.Log.d(r0, r1)
                    r2.invoke()
                L_0x0011:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.controller.ControlsBindingControllerImpl$LoadSubscriber$loadCancel$1.run():void");
            }
        };
    }

    @Override // com.android.systemui.controls.controller.ControlsBindingController
    public final void bindAndLoadSuggested(ComponentName componentName, ControlsControllerImpl$startSeeding$1 controlsControllerImpl$startSeeding$1) {
        final LoadSubscriber loadSubscriber = new LoadSubscriber(controlsControllerImpl$startSeeding$1, 36L);
        final ControlsProviderLifecycleManager retrieveLifecycleManager = retrieveLifecycleManager(componentName);
        Objects.requireNonNull(retrieveLifecycleManager);
        retrieveLifecycleManager.onLoadCanceller = retrieveLifecycleManager.executor.executeDelayed(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsProviderLifecycleManager$maybeBindAndLoadSuggested$1
            @Override // java.lang.Runnable
            public final void run() {
                ControlsProviderLifecycleManager controlsProviderLifecycleManager = ControlsProviderLifecycleManager.this;
                Log.d(controlsProviderLifecycleManager.TAG, Intrinsics.stringPlus("Timeout waiting onLoadSuggested for ", controlsProviderLifecycleManager.componentName));
                IControlsSubscriber.Stub stub = loadSubscriber;
                ControlsProviderLifecycleManager controlsProviderLifecycleManager2 = ControlsProviderLifecycleManager.this;
                Objects.requireNonNull(controlsProviderLifecycleManager2);
                stub.onError(controlsProviderLifecycleManager2.token, "Timeout waiting onLoadSuggested");
                ControlsProviderLifecycleManager controlsProviderLifecycleManager3 = ControlsProviderLifecycleManager.this;
                Objects.requireNonNull(controlsProviderLifecycleManager3);
                Runnable runnable = controlsProviderLifecycleManager3.onLoadCanceller;
                if (runnable != null) {
                    runnable.run();
                }
                controlsProviderLifecycleManager3.onLoadCanceller = null;
                controlsProviderLifecycleManager3.executor.execute(new ControlsProviderLifecycleManager$bindService$1(controlsProviderLifecycleManager3, false));
            }
        }, 20L, TimeUnit.SECONDS);
        retrieveLifecycleManager.invokeOrQueue(new ControlsProviderLifecycleManager.Suggest(loadSubscriber));
    }

    @Override // com.android.systemui.util.UserAwareController
    public final void changeUser(UserHandle userHandle) {
        if (!Intrinsics.areEqual(userHandle, this.currentUser)) {
            unbind();
            this.currentUser = userHandle;
        }
    }

    @VisibleForTesting
    public ControlsProviderLifecycleManager createProviderManager$frameworks__base__packages__SystemUI__android_common__SystemUI_core(ComponentName componentName) {
        return new ControlsProviderLifecycleManager(this.context, this.backgroundExecutor, this.actionCallbackService, this.currentUser, componentName);
    }

    @Override // com.android.systemui.controls.controller.ControlsBindingController
    public final void onComponentRemoved(final ComponentName componentName) {
        this.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsBindingControllerImpl$onComponentRemoved$1
            @Override // java.lang.Runnable
            public final void run() {
                ControlsBindingControllerImpl controlsBindingControllerImpl = ControlsBindingControllerImpl.this;
                ControlsProviderLifecycleManager controlsProviderLifecycleManager = controlsBindingControllerImpl.currentProvider;
                if (controlsProviderLifecycleManager != null) {
                    if (Intrinsics.areEqual(controlsProviderLifecycleManager.componentName, componentName)) {
                        controlsBindingControllerImpl.unbind();
                    }
                }
            }
        });
    }

    public final ControlsProviderLifecycleManager retrieveLifecycleManager(ComponentName componentName) {
        ComponentName componentName2;
        ControlsProviderLifecycleManager controlsProviderLifecycleManager = this.currentProvider;
        if (controlsProviderLifecycleManager != null) {
            if (controlsProviderLifecycleManager == null) {
                componentName2 = null;
            } else {
                componentName2 = controlsProviderLifecycleManager.componentName;
            }
            if (!Intrinsics.areEqual(componentName2, componentName)) {
                unbind();
            }
        }
        ControlsProviderLifecycleManager controlsProviderLifecycleManager2 = this.currentProvider;
        if (controlsProviderLifecycleManager2 == null) {
            controlsProviderLifecycleManager2 = createProviderManager$frameworks__base__packages__SystemUI__android_common__SystemUI_core(componentName);
        }
        this.currentProvider = controlsProviderLifecycleManager2;
        return controlsProviderLifecycleManager2;
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder("  ControlsBindingController:\n");
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("    currentUser=");
        m.append(this.currentUser);
        m.append('\n');
        sb.append(m.toString());
        sb.append(Intrinsics.stringPlus("    StatefulControlSubscriber=", this.statefulControlSubscriber));
        sb.append("    Providers=" + this.currentProvider + '\n');
        return sb.toString();
    }

    @Override // com.android.systemui.controls.controller.ControlsBindingController
    public final void unsubscribe() {
        final StatefulControlSubscriber statefulControlSubscriber = this.statefulControlSubscriber;
        if (statefulControlSubscriber != null && statefulControlSubscriber.subscriptionOpen) {
            statefulControlSubscriber.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.StatefulControlSubscriber$cancel$1
                @Override // java.lang.Runnable
                public final void run() {
                    StatefulControlSubscriber statefulControlSubscriber2 = StatefulControlSubscriber.this;
                    if (statefulControlSubscriber2.subscriptionOpen) {
                        statefulControlSubscriber2.subscriptionOpen = false;
                        IControlsSubscription iControlsSubscription = statefulControlSubscriber2.subscription;
                        if (iControlsSubscription != null) {
                            statefulControlSubscriber2.provider.cancelSubscription(iControlsSubscription);
                        }
                        StatefulControlSubscriber.this.subscription = null;
                    }
                }
            });
        }
        this.statefulControlSubscriber = null;
    }

    public ControlsBindingControllerImpl(Context context, DelayableExecutor delayableExecutor, Lazy<ControlsController> lazy, UserTracker userTracker) {
        this.context = context;
        this.backgroundExecutor = delayableExecutor;
        this.lazyController = lazy;
        this.currentUser = userTracker.getUserHandle();
    }

    @Override // com.android.systemui.controls.controller.ControlsBindingController
    public final void subscribe(StructureInfo structureInfo) {
        unsubscribe();
        ControlsProviderLifecycleManager retrieveLifecycleManager = retrieveLifecycleManager(structureInfo.componentName);
        StatefulControlSubscriber statefulControlSubscriber = new StatefulControlSubscriber(this.lazyController.get(), retrieveLifecycleManager, this.backgroundExecutor);
        this.statefulControlSubscriber = statefulControlSubscriber;
        List<ControlInfo> list = structureInfo.controls;
        ArrayList arrayList = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(list, 10));
        for (ControlInfo controlInfo : list) {
            Objects.requireNonNull(controlInfo);
            arrayList.add(controlInfo.controlId);
        }
        retrieveLifecycleManager.invokeOrQueue(new ControlsProviderLifecycleManager.Subscribe(arrayList, statefulControlSubscriber));
    }

    public final void unbind() {
        unsubscribe();
        ControlsProviderLifecycleManager controlsProviderLifecycleManager = this.currentProvider;
        if (controlsProviderLifecycleManager != null) {
            Runnable runnable = controlsProviderLifecycleManager.onLoadCanceller;
            if (runnable != null) {
                runnable.run();
            }
            controlsProviderLifecycleManager.onLoadCanceller = null;
            controlsProviderLifecycleManager.executor.execute(new ControlsProviderLifecycleManager$bindService$1(controlsProviderLifecycleManager, false));
        }
        this.currentProvider = null;
    }
}
