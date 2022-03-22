package com.android.systemui.controls.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.service.controls.Control;
import android.service.controls.actions.ControlAction;
import android.util.Log;
import android.util.Pair;
import android.view.ContextThemeWrapper;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ListPopupWindow;
import androidx.slice.view.R$id;
import com.android.systemui.controls.ControlsMetricsLogger;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.CustomIconCache;
import com.android.systemui.controls.controller.ControlInfo;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.controller.StructureInfo;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.Lazy;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__IteratorsJVMKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsUiControllerImpl.kt */
/* loaded from: classes.dex */
public final class ControlsUiControllerImpl implements ControlsUiController {
    public static final ComponentName EMPTY_COMPONENT;
    public static final StructureInfo EMPTY_STRUCTURE;
    public Context activityContext;
    public final ActivityStarter activityStarter;
    public List<StructureInfo> allStructures;
    public final DelayableExecutor bgExecutor;
    public final Context context;
    public final ControlActionCoordinator controlActionCoordinator;
    public final Lazy<ControlsController> controlsController;
    public final Lazy<ControlsListingController> controlsListingController;
    public final ControlsMetricsLogger controlsMetricsLogger;
    public final CustomIconCache iconCache;
    public final KeyguardStateController keyguardStateController;
    public ControlsUiControllerImpl$createCallback$1 listingCallback;
    public final ControlsUiControllerImpl$special$$inlined$compareBy$1 localeComparator;
    public Runnable onDismiss;
    public ViewGroup parent;
    public ListPopupWindow popup;
    public final ContextThemeWrapper popupThemedContext;
    public boolean retainCache;
    public final ShadeController shadeController;
    public final SharedPreferences sharedPreferences;
    public final DelayableExecutor uiExecutor;
    public StructureInfo selectedStructure = EMPTY_STRUCTURE;
    public final LinkedHashMap controlsById = new LinkedHashMap();
    public final LinkedHashMap controlViewsById = new LinkedHashMap();
    public boolean hidden = true;
    public final ControlsUiControllerImpl$onSeedingComplete$1 onSeedingComplete = new Consumer() { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$onSeedingComplete$1
        @Override // java.util.function.Consumer
        public final void accept(Object obj) {
            T t;
            ViewGroup viewGroup = null;
            if (((Boolean) obj).booleanValue()) {
                ControlsUiControllerImpl controlsUiControllerImpl = ControlsUiControllerImpl.this;
                Objects.requireNonNull(controlsUiControllerImpl);
                Iterator<T> it = controlsUiControllerImpl.controlsController.get().getFavorites().iterator();
                if (!it.hasNext()) {
                    t = null;
                } else {
                    t = it.next();
                    if (it.hasNext()) {
                        StructureInfo structureInfo = (StructureInfo) t;
                        Objects.requireNonNull(structureInfo);
                        int size = structureInfo.controls.size();
                        do {
                            T next = it.next();
                            StructureInfo structureInfo2 = (StructureInfo) next;
                            Objects.requireNonNull(structureInfo2);
                            int size2 = structureInfo2.controls.size();
                            if (size < size2) {
                                t = next;
                                size = size2;
                            }
                        } while (it.hasNext());
                    }
                }
                StructureInfo structureInfo3 = (StructureInfo) t;
                if (structureInfo3 == null) {
                    structureInfo3 = ControlsUiControllerImpl.EMPTY_STRUCTURE;
                }
                controlsUiControllerImpl.selectedStructure = structureInfo3;
                ControlsUiControllerImpl controlsUiControllerImpl2 = ControlsUiControllerImpl.this;
                controlsUiControllerImpl2.updatePreferences(controlsUiControllerImpl2.selectedStructure);
            }
            ControlsUiControllerImpl controlsUiControllerImpl3 = ControlsUiControllerImpl.this;
            ViewGroup viewGroup2 = controlsUiControllerImpl3.parent;
            if (viewGroup2 != null) {
                viewGroup = viewGroup2;
            }
            controlsUiControllerImpl3.reload(viewGroup);
        }
    };

    @Override // com.android.systemui.controls.ui.ControlsUiController
    public final void hide() {
        this.hidden = true;
        ListPopupWindow listPopupWindow = this.popup;
        if (listPopupWindow != null) {
            listPopupWindow.dismissImmediate();
        }
        ControlsUiControllerImpl$createCallback$1 controlsUiControllerImpl$createCallback$1 = null;
        this.popup = null;
        for (Map.Entry entry : this.controlViewsById.entrySet()) {
            ControlViewHolder controlViewHolder = (ControlViewHolder) entry.getValue();
            Objects.requireNonNull(controlViewHolder);
            Dialog dialog = controlViewHolder.lastChallengeDialog;
            if (dialog != null) {
                dialog.dismiss();
            }
            controlViewHolder.lastChallengeDialog = null;
            Dialog dialog2 = controlViewHolder.visibleDialog;
            if (dialog2 != null) {
                dialog2.dismiss();
            }
            controlViewHolder.visibleDialog = null;
        }
        this.controlActionCoordinator.closeDialogs();
        this.controlsController.get().unsubscribe();
        ViewGroup viewGroup = this.parent;
        if (viewGroup == null) {
            viewGroup = null;
        }
        viewGroup.removeAllViews();
        this.controlsById.clear();
        this.controlViewsById.clear();
        ControlsListingController controlsListingController = this.controlsListingController.get();
        ControlsUiControllerImpl$createCallback$1 controlsUiControllerImpl$createCallback$12 = this.listingCallback;
        if (controlsUiControllerImpl$createCallback$12 != null) {
            controlsUiControllerImpl$createCallback$1 = controlsUiControllerImpl$createCallback$12;
        }
        controlsListingController.removeCallback(controlsUiControllerImpl$createCallback$1);
        if (!this.retainCache) {
            RenderInfo.iconMap.clear();
            RenderInfo.appIconMap.clear();
        }
    }

    static {
        ComponentName componentName = new ComponentName("", "");
        EMPTY_COMPONENT = componentName;
        EMPTY_STRUCTURE = new StructureInfo(componentName, "", new ArrayList());
    }

    @Override // com.android.systemui.controls.ui.ControlsUiController
    public final void onActionResponse(ComponentName componentName, String str, final int i) {
        final ControlKey controlKey = new ControlKey(componentName, str);
        this.uiExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$onActionResponse$1
            @Override // java.lang.Runnable
            public final void run() {
                boolean z;
                final ControlViewHolder controlViewHolder = (ControlViewHolder) ControlsUiControllerImpl.this.controlViewsById.get(controlKey);
                if (controlViewHolder != null) {
                    int i2 = i;
                    ControlActionCoordinator controlActionCoordinator = controlViewHolder.controlActionCoordinator;
                    ControlWithState cws = controlViewHolder.getCws();
                    Objects.requireNonNull(cws);
                    ControlInfo controlInfo = cws.ci;
                    Objects.requireNonNull(controlInfo);
                    controlActionCoordinator.enableActionOnTouch(controlInfo.controlId);
                    if (controlViewHolder.lastChallengeDialog != null) {
                        z = true;
                    } else {
                        z = false;
                    }
                    AlertDialog alertDialog = null;
                    if (i2 == 0) {
                        controlViewHolder.lastChallengeDialog = null;
                        controlViewHolder.setErrorStatus();
                    } else if (i2 == 1) {
                        controlViewHolder.lastChallengeDialog = null;
                    } else if (i2 == 2) {
                        controlViewHolder.lastChallengeDialog = null;
                        controlViewHolder.setErrorStatus();
                    } else if (i2 == 3) {
                        final Function0<Unit> function0 = controlViewHolder.onDialogCancel;
                        final ControlAction controlAction = controlViewHolder.lastAction;
                        if (controlAction == null) {
                            Log.e("ControlsUiController", "Confirmation Dialog attempted but no last action is set. Will not show");
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(controlViewHolder.context, 16974545);
                            builder.setTitle(controlViewHolder.context.getResources().getString(2131952159, controlViewHolder.title.getText()));
                            builder.setPositiveButton(17039370, new DialogInterface.OnClickListener() { // from class: com.android.systemui.controls.ui.ChallengeDialogs$createConfirmationDialog$builder$1$1
                                @Override // android.content.DialogInterface.OnClickListener
                                public final void onClick(DialogInterface dialogInterface, int i3) {
                                    ControlViewHolder.this.action(R$id.access$addChallengeValue(controlAction, "true"));
                                    dialogInterface.dismiss();
                                }
                            });
                            builder.setNegativeButton(17039360, new DialogInterface.OnClickListener() { // from class: com.android.systemui.controls.ui.ChallengeDialogs$createConfirmationDialog$builder$1$2
                                @Override // android.content.DialogInterface.OnClickListener
                                public final void onClick(DialogInterface dialogInterface, int i3) {
                                    function0.invoke();
                                    dialogInterface.cancel();
                                }
                            });
                            alertDialog = builder.create();
                            alertDialog.getWindow().setType(2020);
                        }
                        controlViewHolder.lastChallengeDialog = alertDialog;
                        if (alertDialog != null) {
                            alertDialog.show();
                        }
                    } else if (i2 == 4) {
                        ChallengeDialogs$createPinDialog$1 createPinDialog = R$id.createPinDialog(controlViewHolder, false, z, controlViewHolder.onDialogCancel);
                        controlViewHolder.lastChallengeDialog = createPinDialog;
                        if (createPinDialog != null) {
                            createPinDialog.show();
                        }
                    } else if (i2 == 5) {
                        ChallengeDialogs$createPinDialog$1 createPinDialog2 = R$id.createPinDialog(controlViewHolder, true, z, controlViewHolder.onDialogCancel);
                        controlViewHolder.lastChallengeDialog = createPinDialog2;
                        if (createPinDialog2 != null) {
                            createPinDialog2.show();
                        }
                    }
                }
            }
        });
    }

    @Override // com.android.systemui.controls.ui.ControlsUiController
    public final void onRefreshState(ComponentName componentName, List<Control> list) {
        final boolean z = !this.keyguardStateController.isUnlocked();
        for (Control control : list) {
            ControlWithState controlWithState = (ControlWithState) this.controlsById.get(new ControlKey(componentName, control.getControlId()));
            if (controlWithState != null) {
                Log.d("ControlsUiController", Intrinsics.stringPlus("onRefreshState() for id: ", control.getControlId()));
                CustomIconCache customIconCache = this.iconCache;
                String controlId = control.getControlId();
                Icon customIcon = control.getCustomIcon();
                Objects.requireNonNull(customIconCache);
                if (!Intrinsics.areEqual(componentName, customIconCache.currentComponent)) {
                    synchronized (customIconCache.cache) {
                        customIconCache.cache.clear();
                    }
                    customIconCache.currentComponent = componentName;
                }
                synchronized (customIconCache.cache) {
                    if (customIcon != null) {
                        Icon icon = (Icon) customIconCache.cache.put(controlId, customIcon);
                    } else {
                        Icon icon2 = (Icon) customIconCache.cache.remove(controlId);
                    }
                }
                final ControlWithState controlWithState2 = new ControlWithState(componentName, controlWithState.ci, control);
                ControlKey controlKey = new ControlKey(componentName, control.getControlId());
                this.controlsById.put(controlKey, controlWithState2);
                final ControlViewHolder controlViewHolder = (ControlViewHolder) this.controlViewsById.get(controlKey);
                if (controlViewHolder != null) {
                    this.uiExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$onRefreshState$1$1$1$1
                        @Override // java.lang.Runnable
                        public final void run() {
                            ControlViewHolder.this.bindData(controlWithState2, z);
                        }
                    });
                }
            }
        }
    }

    public final void reload(final ViewGroup viewGroup) {
        if (!this.hidden) {
            ControlsListingController controlsListingController = this.controlsListingController.get();
            ControlsUiControllerImpl$createCallback$1 controlsUiControllerImpl$createCallback$1 = this.listingCallback;
            if (controlsUiControllerImpl$createCallback$1 == null) {
                controlsUiControllerImpl$createCallback$1 = null;
            }
            controlsListingController.removeCallback(controlsUiControllerImpl$createCallback$1);
            this.controlsController.get().unsubscribe();
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(viewGroup, "alpha", 1.0f, 0.0f);
            ofFloat.setInterpolator(new AccelerateInterpolator(1.0f));
            ofFloat.setDuration(200L);
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$reload$1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    ControlsUiControllerImpl.this.controlViewsById.clear();
                    ControlsUiControllerImpl.this.controlsById.clear();
                    ControlsUiControllerImpl controlsUiControllerImpl = ControlsUiControllerImpl.this;
                    ViewGroup viewGroup2 = viewGroup;
                    Runnable runnable = controlsUiControllerImpl.onDismiss;
                    Context context = null;
                    if (runnable == null) {
                        runnable = null;
                    }
                    Context context2 = controlsUiControllerImpl.activityContext;
                    if (context2 != null) {
                        context = context2;
                    }
                    controlsUiControllerImpl.show(viewGroup2, runnable, context);
                    ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(viewGroup, "alpha", 0.0f, 1.0f);
                    ofFloat2.setInterpolator(new DecelerateInterpolator(1.0f));
                    ofFloat2.setDuration(200L);
                    ofFloat2.start();
                }
            });
            ofFloat.start();
        }
    }

    /* JADX WARN: Type inference failed for: r7v4, types: [com.android.systemui.controls.ui.ControlsUiControllerImpl$createCallback$1] */
    /* JADX WARN: Type inference failed for: r7v7, types: [com.android.systemui.controls.ui.ControlsUiControllerImpl$createCallback$1] */
    /* JADX WARN: Type inference failed for: r7v8, types: [com.android.systemui.controls.ui.ControlsUiControllerImpl$createCallback$1] */
    @Override // com.android.systemui.controls.ui.ControlsUiController
    public final void show(ViewGroup viewGroup, Runnable runnable, Context context) {
        Log.d("ControlsUiController", "show()");
        this.parent = viewGroup;
        this.onDismiss = runnable;
        this.activityContext = context;
        this.hidden = false;
        this.retainCache = false;
        this.controlActionCoordinator.setActivityContext(context);
        ArrayList favorites = this.controlsController.get().getFavorites();
        this.allStructures = favorites;
        ControlsUiControllerImpl$createCallback$1 controlsUiControllerImpl$createCallback$1 = null;
        if (favorites == null) {
            favorites = null;
        }
        this.selectedStructure = getPreferredStructure(favorites);
        if (this.controlsController.get().addSeedingFavoritesCallback(this.onSeedingComplete)) {
            final ControlsUiControllerImpl$show$1 controlsUiControllerImpl$show$1 = new ControlsUiControllerImpl$show$1(this);
            this.listingCallback = new ControlsListingController.ControlsListingCallback() { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$createCallback$1
                @Override // com.android.systemui.controls.management.ControlsListingController.ControlsListingCallback
                public final void onServicesUpdated(ArrayList arrayList) {
                    final ArrayList arrayList2 = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(arrayList, 10));
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        ControlsServiceInfo controlsServiceInfo = (ControlsServiceInfo) it.next();
                        Objects.requireNonNull(controlsServiceInfo);
                        arrayList2.add(new SelectionItem(controlsServiceInfo.loadLabel(), "", controlsServiceInfo.loadIcon(), controlsServiceInfo.componentName, controlsServiceInfo.serviceInfo.applicationInfo.uid));
                    }
                    ControlsUiControllerImpl controlsUiControllerImpl = ControlsUiControllerImpl.this;
                    Objects.requireNonNull(controlsUiControllerImpl);
                    DelayableExecutor delayableExecutor = controlsUiControllerImpl.uiExecutor;
                    final ControlsUiControllerImpl controlsUiControllerImpl2 = ControlsUiControllerImpl.this;
                    final Function1<List<SelectionItem>, Unit> function1 = controlsUiControllerImpl$show$1;
                    delayableExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$createCallback$1$onServicesUpdated$1
                        @Override // java.lang.Runnable
                        public final void run() {
                            ViewGroup viewGroup2 = ControlsUiControllerImpl.this.parent;
                            if (viewGroup2 == null) {
                                viewGroup2 = null;
                            }
                            viewGroup2.removeAllViews();
                            if (arrayList2.size() > 0) {
                                function1.invoke(arrayList2);
                            }
                        }
                    });
                }
            };
        } else {
            StructureInfo structureInfo = this.selectedStructure;
            Objects.requireNonNull(structureInfo);
            if (structureInfo.controls.isEmpty()) {
                List<StructureInfo> list = this.allStructures;
                if (list == null) {
                    list = null;
                }
                if (list.size() <= 1) {
                    final ControlsUiControllerImpl$show$2 controlsUiControllerImpl$show$2 = new ControlsUiControllerImpl$show$2(this);
                    this.listingCallback = new ControlsListingController.ControlsListingCallback() { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$createCallback$1
                        @Override // com.android.systemui.controls.management.ControlsListingController.ControlsListingCallback
                        public final void onServicesUpdated(ArrayList arrayList) {
                            final ArrayList arrayList2 = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(arrayList, 10));
                            Iterator it = arrayList.iterator();
                            while (it.hasNext()) {
                                ControlsServiceInfo controlsServiceInfo = (ControlsServiceInfo) it.next();
                                Objects.requireNonNull(controlsServiceInfo);
                                arrayList2.add(new SelectionItem(controlsServiceInfo.loadLabel(), "", controlsServiceInfo.loadIcon(), controlsServiceInfo.componentName, controlsServiceInfo.serviceInfo.applicationInfo.uid));
                            }
                            ControlsUiControllerImpl controlsUiControllerImpl = ControlsUiControllerImpl.this;
                            Objects.requireNonNull(controlsUiControllerImpl);
                            DelayableExecutor delayableExecutor = controlsUiControllerImpl.uiExecutor;
                            final ControlsUiControllerImpl controlsUiControllerImpl2 = ControlsUiControllerImpl.this;
                            final Function1 function1 = controlsUiControllerImpl$show$2;
                            delayableExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$createCallback$1$onServicesUpdated$1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    ViewGroup viewGroup2 = ControlsUiControllerImpl.this.parent;
                                    if (viewGroup2 == null) {
                                        viewGroup2 = null;
                                    }
                                    viewGroup2.removeAllViews();
                                    if (arrayList2.size() > 0) {
                                        function1.invoke(arrayList2);
                                    }
                                }
                            });
                        }
                    };
                }
            }
            StructureInfo structureInfo2 = this.selectedStructure;
            Objects.requireNonNull(structureInfo2);
            List<ControlInfo> list2 = structureInfo2.controls;
            ArrayList arrayList = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(list2, 10));
            for (ControlInfo controlInfo : list2) {
                StructureInfo structureInfo3 = this.selectedStructure;
                Objects.requireNonNull(structureInfo3);
                arrayList.add(new ControlWithState(structureInfo3.componentName, controlInfo, null));
            }
            LinkedHashMap linkedHashMap = this.controlsById;
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                Object next = it.next();
                ControlWithState controlWithState = (ControlWithState) next;
                StructureInfo structureInfo4 = this.selectedStructure;
                Objects.requireNonNull(structureInfo4);
                ComponentName componentName = structureInfo4.componentName;
                Objects.requireNonNull(controlWithState);
                ControlInfo controlInfo2 = controlWithState.ci;
                Objects.requireNonNull(controlInfo2);
                linkedHashMap.put(new ControlKey(componentName, controlInfo2.controlId), next);
            }
            final ControlsUiControllerImpl$show$5 controlsUiControllerImpl$show$5 = new ControlsUiControllerImpl$show$5(this);
            this.listingCallback = new ControlsListingController.ControlsListingCallback() { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$createCallback$1
                @Override // com.android.systemui.controls.management.ControlsListingController.ControlsListingCallback
                public final void onServicesUpdated(ArrayList arrayList2) {
                    final ArrayList arrayList22 = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(arrayList2, 10));
                    Iterator it2 = arrayList2.iterator();
                    while (it2.hasNext()) {
                        ControlsServiceInfo controlsServiceInfo = (ControlsServiceInfo) it2.next();
                        Objects.requireNonNull(controlsServiceInfo);
                        arrayList22.add(new SelectionItem(controlsServiceInfo.loadLabel(), "", controlsServiceInfo.loadIcon(), controlsServiceInfo.componentName, controlsServiceInfo.serviceInfo.applicationInfo.uid));
                    }
                    ControlsUiControllerImpl controlsUiControllerImpl = ControlsUiControllerImpl.this;
                    Objects.requireNonNull(controlsUiControllerImpl);
                    DelayableExecutor delayableExecutor = controlsUiControllerImpl.uiExecutor;
                    final ControlsUiControllerImpl controlsUiControllerImpl2 = ControlsUiControllerImpl.this;
                    final Function1 function1 = controlsUiControllerImpl$show$5;
                    delayableExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$createCallback$1$onServicesUpdated$1
                        @Override // java.lang.Runnable
                        public final void run() {
                            ViewGroup viewGroup2 = ControlsUiControllerImpl.this.parent;
                            if (viewGroup2 == null) {
                                viewGroup2 = null;
                            }
                            viewGroup2.removeAllViews();
                            if (arrayList22.size() > 0) {
                                function1.invoke(arrayList22);
                            }
                        }
                    });
                }
            };
            this.controlsController.get().subscribeToFavorites(this.selectedStructure);
        }
        ControlsListingController controlsListingController = this.controlsListingController.get();
        ControlsUiControllerImpl$createCallback$1 controlsUiControllerImpl$createCallback$12 = this.listingCallback;
        if (controlsUiControllerImpl$createCallback$12 != null) {
            controlsUiControllerImpl$createCallback$1 = controlsUiControllerImpl$createCallback$12;
        }
        controlsListingController.addCallback(controlsUiControllerImpl$createCallback$1);
    }

    public final void startActivity(Intent intent) {
        Context context;
        intent.putExtra("extra_animate", true);
        if (this.keyguardStateController.isShowing()) {
            this.activityStarter.postStartActivityDismissingKeyguard(intent, 0);
            return;
        }
        Context context2 = this.activityContext;
        if (context2 == null) {
            context = null;
        } else {
            context = context2;
        }
        if (context2 == null) {
            context2 = null;
        }
        context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context2, new Pair[0]).toBundle());
    }

    public final void startTargetedActivity(StructureInfo structureInfo, Class<?> cls) {
        Context context = this.activityContext;
        if (context == null) {
            context = null;
        }
        Intent intent = new Intent(context, cls);
        Objects.requireNonNull(structureInfo);
        intent.putExtra("extra_app_label", this.controlsListingController.get().getAppLabel(structureInfo.componentName));
        intent.putExtra("extra_structure", structureInfo.structure);
        intent.putExtra("android.intent.extra.COMPONENT_NAME", structureInfo.componentName);
        startActivity(intent);
        this.retainCache = true;
    }

    public final void updatePreferences(StructureInfo structureInfo) {
        if (!Intrinsics.areEqual(structureInfo, EMPTY_STRUCTURE)) {
            SharedPreferences.Editor edit = this.sharedPreferences.edit();
            Objects.requireNonNull(structureInfo);
            edit.putString("controls_component", structureInfo.componentName.flattenToString()).putString("controls_structure", structureInfo.structure.toString()).commit();
        }
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [com.android.systemui.controls.ui.ControlsUiControllerImpl$onSeedingComplete$1] */
    /* JADX WARN: Type inference failed for: r2v2, types: [com.android.systemui.controls.ui.ControlsUiControllerImpl$special$$inlined$compareBy$1] */
    public ControlsUiControllerImpl(Lazy<ControlsController> lazy, Context context, DelayableExecutor delayableExecutor, DelayableExecutor delayableExecutor2, Lazy<ControlsListingController> lazy2, SharedPreferences sharedPreferences, ControlActionCoordinator controlActionCoordinator, ActivityStarter activityStarter, ShadeController shadeController, CustomIconCache customIconCache, ControlsMetricsLogger controlsMetricsLogger, KeyguardStateController keyguardStateController) {
        this.controlsController = lazy;
        this.context = context;
        this.uiExecutor = delayableExecutor;
        this.bgExecutor = delayableExecutor2;
        this.controlsListingController = lazy2;
        this.sharedPreferences = sharedPreferences;
        this.controlActionCoordinator = controlActionCoordinator;
        this.activityStarter = activityStarter;
        this.shadeController = shadeController;
        this.iconCache = customIconCache;
        this.controlsMetricsLogger = controlsMetricsLogger;
        this.keyguardStateController = keyguardStateController;
        this.popupThemedContext = new ContextThemeWrapper(context, 2132017471);
        final Collator instance = Collator.getInstance(context.getResources().getConfiguration().getLocales().get(0));
        this.localeComparator = new Comparator() { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$special$$inlined$compareBy$1
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                return instance.compare(((SelectionItem) t).getTitle(), ((SelectionItem) t2).getTitle());
            }
        };
    }

    @Override // com.android.systemui.controls.ui.ControlsUiController
    public final StructureInfo getPreferredStructure(List<StructureInfo> list) {
        ComponentName componentName;
        boolean z;
        if (list.isEmpty()) {
            return EMPTY_STRUCTURE;
        }
        Object obj = null;
        String string = this.sharedPreferences.getString("controls_component", null);
        if (string == null) {
            componentName = null;
        } else {
            componentName = ComponentName.unflattenFromString(string);
        }
        if (componentName == null) {
            componentName = EMPTY_COMPONENT;
        }
        String string2 = this.sharedPreferences.getString("controls_structure", "");
        Iterator<T> it = list.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            StructureInfo structureInfo = (StructureInfo) next;
            Objects.requireNonNull(structureInfo);
            if (!Intrinsics.areEqual(componentName, structureInfo.componentName) || !Intrinsics.areEqual(string2, structureInfo.structure)) {
                z = false;
                continue;
            } else {
                z = true;
                continue;
            }
            if (z) {
                obj = next;
                break;
            }
        }
        StructureInfo structureInfo2 = (StructureInfo) obj;
        if (structureInfo2 == null) {
            return list.get(0);
        }
        return structureInfo2;
    }
}
