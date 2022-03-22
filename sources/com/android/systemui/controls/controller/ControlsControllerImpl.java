package com.android.systemui.controls.controller;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.service.controls.Control;
import android.service.controls.actions.ControlAction;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0;
import com.android.systemui.Dumpable;
import com.android.systemui.controls.ControlStatus;
import com.android.systemui.controls.controller.ControlsBindingController;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.ui.ControlsUiController;
import com.android.systemui.controls.ui.ControlsUiControllerImpl$onSeedingComplete$1;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import kotlin.collections.BrittleContainsOptimizationKt;
import kotlin.collections.CollectionsKt__IteratorsJVMKt;
import kotlin.collections.CollectionsKt__ReversedViewsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.EmptyList;
import kotlin.collections.MapsKt___MapsKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsControllerImpl.kt */
/* loaded from: classes.dex */
public final class ControlsControllerImpl implements Dumpable, ControlsController {
    public AuxiliaryPersistenceWrapper auxiliaryPersistenceWrapper;
    public final ControlsBindingController bindingController;
    public final Context context;
    public UserHandle currentUser;
    public final DelayableExecutor executor;
    public final ControlsListingController listingController;
    public final ControlsFavoritePersistenceWrapper persistenceWrapper;
    public final ControlsControllerImpl$restoreFinishedReceiver$1 restoreFinishedReceiver;
    public final ArrayList seedingCallbacks = new ArrayList();
    public boolean seedingInProgress;
    public final ControlsUiController uiController;
    public boolean userChanging;
    public UserStructure userStructure;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r9v0, types: [com.android.systemui.controls.controller.ControlsControllerImpl$restoreFinishedReceiver$1, android.content.BroadcastReceiver] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public ControlsControllerImpl(android.content.Context r12, com.android.systemui.util.concurrency.DelayableExecutor r13, com.android.systemui.controls.ui.ControlsUiController r14, com.android.systemui.controls.controller.ControlsBindingController r15, com.android.systemui.controls.management.ControlsListingController r16, com.android.systemui.broadcast.BroadcastDispatcher r17, java.util.Optional<com.android.systemui.controls.controller.ControlsFavoritePersistenceWrapper> r18, com.android.systemui.dump.DumpManager r19, com.android.systemui.settings.UserTracker r20) {
        /*
            r11 = this;
            r0 = r11
            r1 = r12
            r5 = r13
            r8 = r16
            r11.<init>()
            r0.context = r1
            r0.executor = r5
            r2 = r14
            r0.uiController = r2
            r2 = r15
            r0.bindingController = r2
            r0.listingController = r8
            r2 = 1
            r0.userChanging = r2
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>()
            r0.seedingCallbacks = r2
            android.os.UserHandle r2 = r20.getUserHandle()
            r0.currentUser = r2
            com.android.systemui.controls.controller.UserStructure r3 = new com.android.systemui.controls.controller.UserStructure
            r3.<init>(r12, r2)
            r0.userStructure = r3
            com.android.systemui.controls.controller.ControlsControllerImpl$1 r2 = new com.android.systemui.controls.controller.ControlsControllerImpl$1
            r2.<init>()
            r3 = r18
            java.lang.Object r2 = r3.orElseGet(r2)
            com.android.systemui.controls.controller.ControlsFavoritePersistenceWrapper r2 = (com.android.systemui.controls.controller.ControlsFavoritePersistenceWrapper) r2
            r0.persistenceWrapper = r2
            com.android.systemui.controls.controller.AuxiliaryPersistenceWrapper r2 = new com.android.systemui.controls.controller.AuxiliaryPersistenceWrapper
            com.android.systemui.controls.controller.UserStructure r3 = r0.userStructure
            java.util.Objects.requireNonNull(r3)
            java.io.File r3 = r3.auxiliaryFile
            com.android.systemui.controls.controller.ControlsFavoritePersistenceWrapper r4 = new com.android.systemui.controls.controller.ControlsFavoritePersistenceWrapper
            r6 = 0
            r4.<init>(r3, r13, r6)
            r2.<init>(r4)
            r0.auxiliaryPersistenceWrapper = r2
            com.android.systemui.controls.controller.ControlsControllerImpl$userSwitchReceiver$1 r3 = new com.android.systemui.controls.controller.ControlsControllerImpl$userSwitchReceiver$1
            r3.<init>()
            com.android.systemui.controls.controller.ControlsControllerImpl$restoreFinishedReceiver$1 r9 = new com.android.systemui.controls.controller.ControlsControllerImpl$restoreFinishedReceiver$1
            r9.<init>()
            r0.restoreFinishedReceiver = r9
            com.android.systemui.controls.controller.ControlsControllerImpl$settingObserver$1 r2 = new com.android.systemui.controls.controller.ControlsControllerImpl$settingObserver$1
            r2.<init>()
            com.android.systemui.controls.controller.ControlsControllerImpl$listingCallback$1 r10 = new com.android.systemui.controls.controller.ControlsControllerImpl$listingCallback$1
            r10.<init>()
            java.lang.Class<com.android.systemui.controls.controller.ControlsControllerImpl> r2 = com.android.systemui.controls.controller.ControlsControllerImpl.class
            java.lang.String r2 = r2.getName()
            r4 = r19
            r4.registerDumpable(r2, r11)
            r11.resetFavorites()
            r2 = 0
            r0.userChanging = r2
            android.content.IntentFilter r4 = new android.content.IntentFilter
            java.lang.String r0 = "android.intent.action.USER_SWITCHED"
            r4.<init>(r0)
            android.os.UserHandle r6 = android.os.UserHandle.ALL
            r7 = 16
            r2 = r17
            com.android.systemui.broadcast.BroadcastDispatcher.registerReceiver$default(r2, r3, r4, r5, r6, r7)
            android.content.IntentFilter r2 = new android.content.IntentFilter
            java.lang.String r0 = "com.android.systemui.backup.RESTORE_FINISHED"
            r2.<init>(r0)
            java.lang.String r3 = "com.android.systemui.permission.SELF"
            r4 = 0
            r5 = 4
            r0 = r12
            r1 = r9
            r0.registerReceiver(r1, r2, r3, r4, r5)
            r8.addCallback(r10)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.controller.ControlsControllerImpl.<init>(android.content.Context, com.android.systemui.util.concurrency.DelayableExecutor, com.android.systemui.controls.ui.ControlsUiController, com.android.systemui.controls.controller.ControlsBindingController, com.android.systemui.controls.management.ControlsListingController, com.android.systemui.broadcast.BroadcastDispatcher, java.util.Optional, com.android.systemui.dump.DumpManager, com.android.systemui.settings.UserTracker):void");
    }

    @VisibleForTesting
    public static /* synthetic */ void getAuxiliaryPersistenceWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getRestoreFinishedReceiver$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getSettingObserver$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    @Override // com.android.systemui.controls.controller.ControlsController
    public final boolean addSeedingFavoritesCallback(final ControlsUiControllerImpl$onSeedingComplete$1 controlsUiControllerImpl$onSeedingComplete$1) {
        if (!this.seedingInProgress) {
            return false;
        }
        this.executor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$addSeedingFavoritesCallback$1
            @Override // java.lang.Runnable
            public final void run() {
                ControlsControllerImpl controlsControllerImpl = ControlsControllerImpl.this;
                if (controlsControllerImpl.seedingInProgress) {
                    controlsControllerImpl.seedingCallbacks.add(controlsUiControllerImpl$onSeedingComplete$1);
                } else {
                    controlsUiControllerImpl$onSeedingComplete$1.accept(Boolean.FALSE);
                }
            }
        });
        return true;
    }

    public final boolean confirmAvailability() {
        if (!this.userChanging) {
            return true;
        }
        Log.w("ControlsControllerImpl", "Controls not available while user is changing");
        return false;
    }

    public final ControlStatus createRemovedStatus(ComponentName componentName, ControlInfo controlInfo, CharSequence charSequence, boolean z) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setPackage(componentName.getPackageName());
        PendingIntent activity = PendingIntent.getActivity(this.context, componentName.hashCode(), intent, 67108864);
        Objects.requireNonNull(controlInfo);
        return new ControlStatus(new Control.StatelessBuilder(controlInfo.controlId, activity).setTitle(controlInfo.controlTitle).setSubtitle(controlInfo.controlSubtitle).setStructure(charSequence).setDeviceType(controlInfo.deviceType).build(), componentName, true, z);
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("ControlsController state:");
        KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0.m(this.userChanging, "  Changing users: ", printWriter);
        printWriter.println(Intrinsics.stringPlus("  Current user: ", Integer.valueOf(this.currentUser.getIdentifier())));
        printWriter.println("  Favorites:");
        Iterator it = Favorites.getAllStructures().iterator();
        while (it.hasNext()) {
            StructureInfo structureInfo = (StructureInfo) it.next();
            printWriter.println(Intrinsics.stringPlus("    ", structureInfo));
            Objects.requireNonNull(structureInfo);
            for (ControlInfo controlInfo : structureInfo.controls) {
                printWriter.println(Intrinsics.stringPlus("      ", controlInfo));
            }
        }
        printWriter.println(this.bindingController.toString());
    }

    @Override // com.android.systemui.util.UserAwareController
    public final int getCurrentUserId() {
        return this.currentUser.getIdentifier();
    }

    @Override // com.android.systemui.controls.controller.ControlsController
    public final StructureInfo getPreferredStructure() {
        return this.uiController.getPreferredStructure(Favorites.getAllStructures());
    }

    public final void resetFavorites() {
        Map<ComponentName, ? extends List<StructureInfo>> map = Favorites.favMap;
        Favorites.favMap = MapsKt___MapsKt.emptyMap();
        List<StructureInfo> readFavorites = this.persistenceWrapper.readFavorites();
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Object obj : readFavorites) {
            StructureInfo structureInfo = (StructureInfo) obj;
            Objects.requireNonNull(structureInfo);
            ComponentName componentName = structureInfo.componentName;
            Object obj2 = linkedHashMap.get(componentName);
            if (obj2 == null) {
                obj2 = new ArrayList();
                linkedHashMap.put(componentName, obj2);
            }
            ((List) obj2).add(obj);
        }
        Favorites.favMap = linkedHashMap;
    }

    @Override // com.android.systemui.controls.controller.ControlsController
    public final void seedFavoritesForComponents(final List<ComponentName> list, final Consumer<SeedResponse> consumer) {
        if (!this.seedingInProgress && !list.isEmpty()) {
            if (confirmAvailability()) {
                this.seedingInProgress = true;
                startSeeding(list, consumer, false);
            } else if (this.userChanging) {
                this.executor.executeDelayed(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$seedFavoritesForComponents$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        ControlsControllerImpl.this.seedFavoritesForComponents(list, consumer);
                    }
                }, 500L, TimeUnit.MILLISECONDS);
            } else {
                for (ComponentName componentName : list) {
                    consumer.accept(new SeedResponse(componentName.getPackageName(), false));
                }
            }
        }
    }

    @Override // com.android.systemui.controls.controller.ControlsController
    public final void action(ComponentName componentName, ControlInfo controlInfo, ControlAction controlAction) {
        if (confirmAvailability()) {
            this.bindingController.action(componentName, controlInfo, controlAction);
        }
    }

    @Override // com.android.systemui.controls.controller.ControlsController
    public final void addFavorite(final ComponentName componentName, final CharSequence charSequence, final ControlInfo controlInfo) {
        if (confirmAvailability()) {
            this.executor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$addFavorite$1
                /* JADX WARN: Multi-variable type inference failed */
                @Override // java.lang.Runnable
                public final void run() {
                    boolean z;
                    Map<ComponentName, ? extends List<StructureInfo>> map = Favorites.favMap;
                    ComponentName componentName2 = componentName;
                    CharSequence charSequence2 = charSequence;
                    ControlInfo controlInfo2 = controlInfo;
                    ArrayList controlsForComponent = Favorites.getControlsForComponent(componentName2);
                    boolean z2 = true;
                    if (!controlsForComponent.isEmpty()) {
                        Iterator it = controlsForComponent.iterator();
                        while (it.hasNext()) {
                            ControlInfo controlInfo3 = (ControlInfo) it.next();
                            Objects.requireNonNull(controlInfo3);
                            if (Intrinsics.areEqual(controlInfo3.controlId, controlInfo2.controlId)) {
                                z = true;
                                break;
                            }
                        }
                    }
                    z = false;
                    if (z) {
                        z2 = false;
                    } else {
                        List list = (List) Favorites.favMap.get(componentName2);
                        StructureInfo structureInfo = null;
                        if (list != null) {
                            Iterator it2 = list.iterator();
                            while (true) {
                                if (!it2.hasNext()) {
                                    break;
                                }
                                Object next = it2.next();
                                StructureInfo structureInfo2 = (StructureInfo) next;
                                Objects.requireNonNull(structureInfo2);
                                if (Intrinsics.areEqual(structureInfo2.structure, charSequence2)) {
                                    structureInfo = next;
                                    break;
                                }
                            }
                            structureInfo = structureInfo;
                        }
                        if (structureInfo == null) {
                            structureInfo = new StructureInfo(componentName2, charSequence2, EmptyList.INSTANCE);
                        }
                        Favorites.replaceControls(new StructureInfo(structureInfo.componentName, structureInfo.structure, CollectionsKt___CollectionsKt.plus(structureInfo.controls, controlInfo2)));
                    }
                    if (z2) {
                        this.persistenceWrapper.storeFavorites(Favorites.getAllStructures());
                    }
                }
            });
        }
    }

    @Override // com.android.systemui.controls.controller.ControlsController
    public final int countFavoritesForComponent(ComponentName componentName) {
        return Favorites.getControlsForComponent(componentName).size();
    }

    @Override // com.android.systemui.controls.controller.ControlsController
    public final ArrayList getFavorites() {
        return Favorites.getAllStructures();
    }

    @Override // com.android.systemui.controls.controller.ControlsController
    public final List<StructureInfo> getFavoritesForComponent(ComponentName componentName) {
        return Favorites.getStructuresForComponent(componentName);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0, types: [com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2] */
    public final void loadForComponent(final ComponentName componentName, final Consumer<ControlsController.LoadData> consumer, final Consumer<Runnable> consumer2) {
        if (!confirmAvailability()) {
            if (this.userChanging) {
                this.executor.executeDelayed(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        ControlsControllerImpl.this.loadForComponent(componentName, consumer, consumer2);
                    }
                }, 500L, TimeUnit.MILLISECONDS);
            }
            EmptyList emptyList = EmptyList.INSTANCE;
            consumer.accept(new ControlsControllerKt$createLoadDataObject$1(emptyList, emptyList, true));
        }
        consumer2.accept(this.bindingController.bindAndLoad(componentName, new ControlsBindingController.LoadCallback() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2
            @Override // java.util.function.Consumer
            public final void accept(List<? extends Control> list) {
                final List<? extends Control> list2 = list;
                final ControlsControllerImpl controlsControllerImpl = ControlsControllerImpl.this;
                DelayableExecutor delayableExecutor = controlsControllerImpl.executor;
                final ComponentName componentName2 = componentName;
                final Consumer<ControlsController.LoadData> consumer3 = consumer;
                delayableExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2$accept$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        Set set;
                        LinkedHashSet linkedHashSet;
                        Map<ComponentName, ? extends List<StructureInfo>> map = Favorites.favMap;
                        ArrayList controlsForComponent = Favorites.getControlsForComponent(componentName2);
                        ArrayList arrayList = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(controlsForComponent, 10));
                        Iterator it = controlsForComponent.iterator();
                        while (it.hasNext()) {
                            ControlInfo controlInfo = (ControlInfo) it.next();
                            Objects.requireNonNull(controlInfo);
                            arrayList.add(controlInfo.controlId);
                        }
                        Map<ComponentName, ? extends List<StructureInfo>> map2 = Favorites.favMap;
                        if (Favorites.updateControls(componentName2, list2)) {
                            controlsControllerImpl.persistenceWrapper.storeFavorites(Favorites.getAllStructures());
                        }
                        ControlsControllerImpl controlsControllerImpl2 = controlsControllerImpl;
                        Set set2 = CollectionsKt___CollectionsKt.toSet(arrayList);
                        List<Control> list3 = list2;
                        Objects.requireNonNull(controlsControllerImpl2);
                        ArrayList arrayList2 = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(list3, 10));
                        for (Control control : list3) {
                            arrayList2.add(control.getControlId());
                        }
                        Collection<?> convertToSetForSetOperationWith = BrittleContainsOptimizationKt.convertToSetForSetOperationWith(arrayList2, set2);
                        if (convertToSetForSetOperationWith.isEmpty()) {
                            set = CollectionsKt___CollectionsKt.toSet(set2);
                        } else {
                            if (convertToSetForSetOperationWith instanceof Set) {
                                linkedHashSet = new LinkedHashSet();
                                for (Object obj : set2) {
                                    if (!convertToSetForSetOperationWith.contains(obj)) {
                                        linkedHashSet.add(obj);
                                    }
                                }
                            } else {
                                linkedHashSet = new LinkedHashSet(set2);
                                linkedHashSet.removeAll(convertToSetForSetOperationWith);
                            }
                            set = linkedHashSet;
                        }
                        List<Control> list4 = list2;
                        ComponentName componentName3 = componentName2;
                        ArrayList arrayList3 = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(list4, 10));
                        for (Control control2 : list4) {
                            arrayList3.add(new ControlStatus(control2, componentName3, arrayList.contains(control2.getControlId()), false));
                        }
                        ArrayList arrayList4 = new ArrayList();
                        Map<ComponentName, ? extends List<StructureInfo>> map3 = Favorites.favMap;
                        List<StructureInfo> structuresForComponent = Favorites.getStructuresForComponent(componentName2);
                        ControlsControllerImpl controlsControllerImpl3 = controlsControllerImpl;
                        ComponentName componentName4 = componentName2;
                        for (StructureInfo structureInfo : structuresForComponent) {
                            Objects.requireNonNull(structureInfo);
                            for (ControlInfo controlInfo2 : structureInfo.controls) {
                                Objects.requireNonNull(controlInfo2);
                                if (set.contains(controlInfo2.controlId)) {
                                    arrayList4.add(controlsControllerImpl3.createRemovedStatus(componentName4, controlInfo2, structureInfo.structure, true));
                                }
                            }
                        }
                        consumer3.accept(new ControlsControllerKt$createLoadDataObject$1(CollectionsKt___CollectionsKt.plus((List) arrayList4, (List) arrayList3), arrayList, false));
                    }
                });
            }

            @Override // com.android.systemui.controls.controller.ControlsBindingController.LoadCallback
            public final void error(String str) {
                final ControlsControllerImpl controlsControllerImpl = ControlsControllerImpl.this;
                DelayableExecutor delayableExecutor = controlsControllerImpl.executor;
                final ComponentName componentName2 = componentName;
                final Consumer<ControlsController.LoadData> consumer3 = consumer;
                delayableExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2$error$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        Map<ComponentName, ? extends List<StructureInfo>> map = Favorites.favMap;
                        List<StructureInfo> structuresForComponent = Favorites.getStructuresForComponent(componentName2);
                        ControlsControllerImpl controlsControllerImpl2 = controlsControllerImpl;
                        ComponentName componentName3 = componentName2;
                        ArrayList arrayList = new ArrayList();
                        for (StructureInfo structureInfo : structuresForComponent) {
                            Objects.requireNonNull(structureInfo);
                            List<ControlInfo> list = structureInfo.controls;
                            ArrayList arrayList2 = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(list, 10));
                            for (ControlInfo controlInfo : list) {
                                arrayList2.add(controlsControllerImpl2.createRemovedStatus(componentName3, controlInfo, structureInfo.structure, false));
                            }
                            CollectionsKt__ReversedViewsKt.addAll(arrayList, arrayList2);
                        }
                        ArrayList arrayList3 = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(arrayList, 10));
                        Iterator it = arrayList.iterator();
                        while (it.hasNext()) {
                            ControlStatus controlStatus = (ControlStatus) it.next();
                            Objects.requireNonNull(controlStatus);
                            arrayList3.add(controlStatus.control.getControlId());
                        }
                        consumer3.accept(new ControlsControllerKt$createLoadDataObject$1(arrayList, arrayList3, true));
                    }
                });
            }
        }));
    }

    @Override // com.android.systemui.controls.controller.ControlsController
    public final void onActionResponse(ComponentName componentName, String str, int i) {
        if (confirmAvailability()) {
            this.uiController.onActionResponse(componentName, str, i);
        }
    }

    @Override // com.android.systemui.controls.controller.ControlsController
    public final void refreshStatus(final ComponentName componentName, final Control control) {
        if (!confirmAvailability()) {
            Log.d("ControlsControllerImpl", "Controls not available");
            return;
        }
        if (control.getStatus() == 1) {
            this.executor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$refreshStatus$1
                @Override // java.lang.Runnable
                public final void run() {
                    Map<ComponentName, ? extends List<StructureInfo>> map = Favorites.favMap;
                    if (Favorites.updateControls(componentName, Collections.singletonList(control))) {
                        this.persistenceWrapper.storeFavorites(Favorites.getAllStructures());
                    }
                }
            });
        }
        this.uiController.onRefreshState(componentName, Collections.singletonList(control));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1] */
    public final void startSeeding(List<ComponentName> list, final Consumer<SeedResponse> consumer, final boolean z) {
        if (list.isEmpty()) {
            boolean z2 = !z;
            this.seedingInProgress = false;
            Iterator it = this.seedingCallbacks.iterator();
            while (it.hasNext()) {
                ((Consumer) it.next()).accept(Boolean.valueOf(z2));
            }
            this.seedingCallbacks.clear();
            return;
        }
        final ComponentName componentName = list.get(0);
        Log.d("ControlsControllerImpl", Intrinsics.stringPlus("Beginning request to seed favorites for: ", componentName));
        final List drop = CollectionsKt___CollectionsKt.drop(list, 1);
        this.bindingController.bindAndLoadSuggested(componentName, new ControlsBindingController.LoadCallback() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1
            @Override // java.util.function.Consumer
            public final void accept(List<? extends Control> list2) {
                final List<? extends Control> list3 = list2;
                final ControlsControllerImpl controlsControllerImpl = ControlsControllerImpl.this;
                DelayableExecutor delayableExecutor = controlsControllerImpl.executor;
                final Consumer<SeedResponse> consumer2 = consumer;
                final ComponentName componentName2 = componentName;
                final List<ComponentName> list4 = drop;
                final boolean z3 = z;
                delayableExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1$accept$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        ArrayMap arrayMap = new ArrayMap();
                        for (Control control : list3) {
                            CharSequence structure = control.getStructure();
                            if (structure == null) {
                                structure = "";
                            }
                            List list5 = (List) arrayMap.get(structure);
                            if (list5 == null) {
                                list5 = new ArrayList();
                            }
                            if (list5.size() < 6) {
                                list5.add(new ControlInfo(control.getControlId(), control.getTitle(), control.getSubtitle(), control.getDeviceType()));
                                arrayMap.put(structure, list5);
                            }
                        }
                        ComponentName componentName3 = componentName2;
                        for (Map.Entry entry : arrayMap.entrySet()) {
                            Favorites.replaceControls(new StructureInfo(componentName3, (CharSequence) entry.getKey(), (List) entry.getValue()));
                        }
                        controlsControllerImpl.persistenceWrapper.storeFavorites(Favorites.getAllStructures());
                        consumer2.accept(new SeedResponse(componentName2.getPackageName(), true));
                        controlsControllerImpl.startSeeding(list4, consumer2, z3);
                    }
                });
            }

            @Override // com.android.systemui.controls.controller.ControlsBindingController.LoadCallback
            public final void error(String str) {
                Log.e("ControlsControllerImpl", Intrinsics.stringPlus("Unable to seed favorites: ", str));
                final ControlsControllerImpl controlsControllerImpl = ControlsControllerImpl.this;
                DelayableExecutor delayableExecutor = controlsControllerImpl.executor;
                final Consumer<SeedResponse> consumer2 = consumer;
                final ComponentName componentName2 = componentName;
                final List<ComponentName> list2 = drop;
                delayableExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1$error$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        consumer2.accept(new SeedResponse(componentName2.getPackageName(), false));
                        controlsControllerImpl.startSeeding(list2, consumer2, true);
                    }
                });
            }
        });
    }

    @Override // com.android.systemui.controls.controller.ControlsController
    public final void subscribeToFavorites(StructureInfo structureInfo) {
        if (confirmAvailability()) {
            this.bindingController.subscribe(structureInfo);
        }
    }

    @Override // com.android.systemui.controls.controller.ControlsController
    public final void unsubscribe() {
        if (confirmAvailability()) {
            this.bindingController.unsubscribe();
        }
    }
}
