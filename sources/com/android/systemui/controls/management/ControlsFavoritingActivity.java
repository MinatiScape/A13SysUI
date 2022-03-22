package com.android.systemui.controls.management;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.mediarouter.R$bool;
import androidx.viewpager2.widget.CompositeOnPageChangeCallback;
import androidx.viewpager2.widget.FakeDrag;
import androidx.viewpager2.widget.ScrollEventAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.ControlStatus;
import com.android.systemui.controls.TooltipManager;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.controller.ControlsControllerImpl;
import com.android.systemui.controls.controller.ControlsControllerImpl$replaceFavoritesForStructure$1;
import com.android.systemui.controls.controller.StructureInfo;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.management.ControlsModel;
import com.android.systemui.controls.ui.ControlsActivity;
import com.android.systemui.controls.ui.ControlsUiController;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.util.LifecycleActivity;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.EmptyList;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsFavoritingActivity.kt */
/* loaded from: classes.dex */
public final class ControlsFavoritingActivity extends LifecycleActivity {
    public CharSequence appName;
    public Runnable cancelLoadRunnable;
    public ControlsFavoritingActivity$onCreate$$inlined$compareBy$1 comparator;
    public ComponentName component;
    public final ControlsControllerImpl controller;
    public final ControlsFavoritingActivity$currentUserTracker$1 currentUserTracker;
    public View doneButton;
    public final Executor executor;
    public boolean fromProviderSelector;
    public boolean isPagerLoaded;
    public final ControlsListingController listingController;
    public TooltipManager mTooltipManager;
    public View otherAppsButton;
    public ManagementPageIndicator pageIndicator;
    public TextView statusText;
    public CharSequence structureExtra;
    public ViewPager2 structurePager;
    public TextView subtitleView;
    public TextView titleView;
    public final ControlsUiController uiController;
    public List<StructureContainer> listOfStructures = EmptyList.INSTANCE;
    public final ControlsFavoritingActivity$listingCallback$1 listingCallback = new ControlsListingController.ControlsListingCallback() { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$listingCallback$1
        @Override // com.android.systemui.controls.management.ControlsListingController.ControlsListingCallback
        public final void onServicesUpdated(ArrayList arrayList) {
            if (arrayList.size() > 1) {
                final ControlsFavoritingActivity controlsFavoritingActivity = ControlsFavoritingActivity.this;
                View view = controlsFavoritingActivity.otherAppsButton;
                if (view == null) {
                    view = null;
                }
                view.post(new Runnable() { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$listingCallback$1$onServicesUpdated$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        View view2 = ControlsFavoritingActivity.this.otherAppsButton;
                        if (view2 == null) {
                            view2 = null;
                        }
                        view2.setVisibility(0);
                    }
                });
            }
        }
    };
    public final ControlsFavoritingActivity$controlsModelCallback$1 controlsModelCallback = new ControlsModel.ControlsModelCallback() { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$controlsModelCallback$1
        @Override // com.android.systemui.controls.management.ControlsModel.ControlsModelCallback
        public final void onFirstChange() {
            View view = ControlsFavoritingActivity.this.doneButton;
            if (view == null) {
                view = null;
            }
            view.setEnabled(true);
        }
    };

    @Override // android.app.Activity
    public final void onBackPressed() {
        if (!this.fromProviderSelector) {
            startActivity(new Intent(getApplicationContext(), ControlsActivity.class), ActivityOptions.makeSceneTransitionAnimation(this, new Pair[0]).toBundle());
        }
        animateExitAndFinish();
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public final void onDestroy() {
        Runnable runnable = this.cancelLoadRunnable;
        if (runnable != null) {
            runnable.run();
        }
        super.onDestroy();
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.controls.management.ControlsFavoritingActivity$currentUserTracker$1] */
    /* JADX WARN: Type inference failed for: r1v3, types: [com.android.systemui.controls.management.ControlsFavoritingActivity$listingCallback$1] */
    /* JADX WARN: Type inference failed for: r1v4, types: [com.android.systemui.controls.management.ControlsFavoritingActivity$controlsModelCallback$1] */
    public ControlsFavoritingActivity(Executor executor, ControlsControllerImpl controlsControllerImpl, ControlsListingController controlsListingController, final BroadcastDispatcher broadcastDispatcher, ControlsUiController controlsUiController) {
        this.executor = executor;
        this.controller = controlsControllerImpl;
        this.listingController = controlsListingController;
        this.uiController = controlsUiController;
        this.currentUserTracker = new CurrentUserTracker(broadcastDispatcher) { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$currentUserTracker$1
            public final int startingUser;

            {
                this.startingUser = ControlsFavoritingActivity.this.controller.getCurrentUserId();
            }

            @Override // com.android.systemui.settings.CurrentUserTracker
            public final void onUserSwitched(int i) {
                if (i != this.startingUser) {
                    stopTracking();
                    ControlsFavoritingActivity.this.finish();
                }
            }
        };
    }

    public final void animateExitAndFinish() {
        R$bool.exitAnimation((ViewGroup) requireViewById(2131427765), new Runnable() { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$animateExitAndFinish$1
            @Override // java.lang.Runnable
            public final void run() {
                ControlsFavoritingActivity.this.finish();
            }
        }).start();
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        TooltipManager tooltipManager = this.mTooltipManager;
        if (tooltipManager != null) {
            tooltipManager.hide(false);
        }
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [com.android.systemui.controls.management.ControlsFavoritingActivity$onCreate$$inlined$compareBy$1] */
    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public final void onCreate(Bundle bundle) {
        boolean z;
        super.onCreate(bundle);
        final Collator instance = Collator.getInstance(getResources().getConfiguration().getLocales().get(0));
        this.comparator = new Comparator() { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$onCreate$$inlined$compareBy$1
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                Comparator comparator = instance;
                StructureContainer structureContainer = (StructureContainer) t;
                Objects.requireNonNull(structureContainer);
                CharSequence charSequence = structureContainer.structureName;
                StructureContainer structureContainer2 = (StructureContainer) t2;
                Objects.requireNonNull(structureContainer2);
                return comparator.compare(charSequence, structureContainer2.structureName);
            }
        };
        this.appName = getIntent().getCharSequenceExtra("extra_app_label");
        this.structureExtra = getIntent().getCharSequenceExtra("extra_structure");
        this.component = (ComponentName) getIntent().getParcelableExtra("android.intent.extra.COMPONENT_NAME");
        this.fromProviderSelector = getIntent().getBooleanExtra("extra_from_provider_selector", false);
        setContentView(2131624050);
        this.lifecycle.addObserver(new ControlsAnimations$observerForAnimations$1(getIntent(), (ViewGroup) requireViewById(2131427765), getWindow()));
        ViewStub viewStub = (ViewStub) requireViewById(2131428940);
        viewStub.setLayoutResource(2131624053);
        viewStub.inflate();
        this.statusText = (TextView) requireViewById(2131428933);
        Context applicationContext = getApplicationContext();
        if (applicationContext.getSharedPreferences(applicationContext.getPackageName(), 0).getInt("ControlsStructureSwipeTooltipCount", 0) < 2) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            TextView textView = this.statusText;
            if (textView == null) {
                textView = null;
            }
            TooltipManager tooltipManager = new TooltipManager(textView.getContext());
            this.mTooltipManager = tooltipManager;
            addContentView(tooltipManager.layout, new FrameLayout.LayoutParams(-2, -2, 51));
        }
        ManagementPageIndicator managementPageIndicator = (ManagementPageIndicator) requireViewById(2131428938);
        ControlsFavoritingActivity$bindViews$2$1 controlsFavoritingActivity$bindViews$2$1 = new ControlsFavoritingActivity$bindViews$2$1(this);
        Objects.requireNonNull(managementPageIndicator);
        managementPageIndicator.visibilityListener = controlsFavoritingActivity$bindViews$2$1;
        this.pageIndicator = managementPageIndicator;
        CharSequence charSequence = this.structureExtra;
        if (charSequence == null && (charSequence = this.appName) == null) {
            charSequence = getResources().getText(2131952169);
        }
        TextView textView2 = (TextView) requireViewById(2131429057);
        textView2.setText(charSequence);
        this.titleView = textView2;
        TextView textView3 = (TextView) requireViewById(2131428947);
        textView3.setText(textView3.getResources().getText(2131952176));
        this.subtitleView = textView3;
        ViewPager2 viewPager2 = (ViewPager2) requireViewById(2131428939);
        this.structurePager = viewPager2;
        ViewPager2.OnPageChangeCallback controlsFavoritingActivity$bindViews$5 = new ViewPager2.OnPageChangeCallback() { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$bindViews$5
            @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
            public final void onPageSelected(int i) {
                TooltipManager tooltipManager2 = ControlsFavoritingActivity.this.mTooltipManager;
                if (tooltipManager2 != null) {
                    tooltipManager2.hide(true);
                }
            }
        };
        Objects.requireNonNull(viewPager2);
        CompositeOnPageChangeCallback compositeOnPageChangeCallback = viewPager2.mExternalPageChangeCallbacks;
        Objects.requireNonNull(compositeOnPageChangeCallback);
        compositeOnPageChangeCallback.mCallbacks.add(controlsFavoritingActivity$bindViews$5);
        View requireViewById = requireViewById(2131428544);
        final Button button = (Button) requireViewById;
        button.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$bindButtons$1$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                View view2 = ControlsFavoritingActivity.this.doneButton;
                if (view2 == null) {
                    view2 = null;
                }
                if (view2.isEnabled()) {
                    Toast.makeText(ControlsFavoritingActivity.this.getApplicationContext(), 2131952177, 0).show();
                }
                ControlsFavoritingActivity.this.startActivity(new Intent(button.getContext(), ControlsProviderSelectorActivity.class), ActivityOptions.makeSceneTransitionAnimation(ControlsFavoritingActivity.this, new Pair[0]).toBundle());
                ControlsFavoritingActivity.this.animateExitAndFinish();
            }
        });
        this.otherAppsButton = requireViewById;
        View requireViewById2 = requireViewById(2131427865);
        Button button2 = (Button) requireViewById2;
        button2.setEnabled(false);
        button2.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$bindButtons$2$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ControlsFavoritingActivity controlsFavoritingActivity = ControlsFavoritingActivity.this;
                if (controlsFavoritingActivity.component != null) {
                    for (StructureContainer structureContainer : controlsFavoritingActivity.listOfStructures) {
                        Objects.requireNonNull(structureContainer);
                        ArrayList favorites = structureContainer.model.getFavorites();
                        ControlsControllerImpl controlsControllerImpl = controlsFavoritingActivity.controller;
                        ComponentName componentName = controlsFavoritingActivity.component;
                        Intrinsics.checkNotNull(componentName);
                        StructureInfo structureInfo = new StructureInfo(componentName, structureContainer.structureName, favorites);
                        Objects.requireNonNull(controlsControllerImpl);
                        if (controlsControllerImpl.confirmAvailability()) {
                            controlsControllerImpl.executor.execute(new ControlsControllerImpl$replaceFavoritesForStructure$1(structureInfo, controlsControllerImpl));
                        }
                    }
                    ControlsFavoritingActivity.this.animateExitAndFinish();
                    ControlsFavoritingActivity controlsFavoritingActivity2 = ControlsFavoritingActivity.this;
                    Objects.requireNonNull(controlsFavoritingActivity2);
                    controlsFavoritingActivity2.startActivity(new Intent(controlsFavoritingActivity2.getApplicationContext(), ControlsActivity.class), ActivityOptions.makeSceneTransitionAnimation(controlsFavoritingActivity2, new Pair[0]).toBundle());
                }
            }
        });
        this.doneButton = requireViewById2;
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public final void onPause() {
        super.onPause();
        TooltipManager tooltipManager = this.mTooltipManager;
        if (tooltipManager != null) {
            tooltipManager.hide(false);
        }
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public final void onResume() {
        super.onResume();
        if (!this.isPagerLoaded) {
            ViewPager2 viewPager2 = this.structurePager;
            TextView textView = null;
            if (viewPager2 == null) {
                viewPager2 = null;
            }
            viewPager2.setAlpha(0.0f);
            ManagementPageIndicator managementPageIndicator = this.pageIndicator;
            if (managementPageIndicator == null) {
                managementPageIndicator = null;
            }
            managementPageIndicator.setAlpha(0.0f);
            ViewPager2 viewPager22 = this.structurePager;
            if (viewPager22 == null) {
                viewPager22 = null;
            }
            viewPager22.setAdapter(new StructureAdapter(EmptyList.INSTANCE));
            ViewPager2.OnPageChangeCallback controlsFavoritingActivity$setUpPager$1$1 = new ViewPager2.OnPageChangeCallback() { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$setUpPager$1$1
                @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
                public final void onPageScrolled(int i, float f, int i2) {
                    ManagementPageIndicator managementPageIndicator2 = ControlsFavoritingActivity.this.pageIndicator;
                    if (managementPageIndicator2 == null) {
                        managementPageIndicator2 = null;
                    }
                    managementPageIndicator2.setLocation(i + f);
                }

                @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
                public final void onPageSelected(int i) {
                    StructureContainer structureContainer = ControlsFavoritingActivity.this.listOfStructures.get(i);
                    Objects.requireNonNull(structureContainer);
                    CharSequence charSequence = structureContainer.structureName;
                    if (TextUtils.isEmpty(charSequence)) {
                        charSequence = ControlsFavoritingActivity.this.appName;
                    }
                    TextView textView2 = ControlsFavoritingActivity.this.titleView;
                    TextView textView3 = null;
                    if (textView2 == null) {
                        textView2 = null;
                    }
                    textView2.setText(charSequence);
                    TextView textView4 = ControlsFavoritingActivity.this.titleView;
                    if (textView4 != null) {
                        textView3 = textView4;
                    }
                    textView3.requestFocus();
                }
            };
            CompositeOnPageChangeCallback compositeOnPageChangeCallback = viewPager22.mExternalPageChangeCallbacks;
            Objects.requireNonNull(compositeOnPageChangeCallback);
            compositeOnPageChangeCallback.mCallbacks.add(controlsFavoritingActivity$setUpPager$1$1);
            ComponentName componentName = this.component;
            if (componentName != null) {
                TextView textView2 = this.statusText;
                if (textView2 != null) {
                    textView = textView2;
                }
                textView.setText(getResources().getText(17040572));
                final CharSequence text = getResources().getText(2131952172);
                this.controller.loadForComponent(componentName, new Consumer() { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$loadControls$1$1
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ControlsController.LoadData loadData = (ControlsController.LoadData) obj;
                        List<ControlStatus> allControls = loadData.getAllControls();
                        List<String> favoritesIds = loadData.getFavoritesIds();
                        final boolean errorOnLoad = loadData.getErrorOnLoad();
                        LinkedHashMap linkedHashMap = new LinkedHashMap();
                        for (T t : allControls) {
                            ControlStatus controlStatus = (ControlStatus) t;
                            Objects.requireNonNull(controlStatus);
                            CharSequence structure = controlStatus.control.getStructure();
                            if (structure == null) {
                                structure = "";
                            }
                            Object obj2 = linkedHashMap.get(structure);
                            if (obj2 == null) {
                                obj2 = new ArrayList();
                                linkedHashMap.put(structure, obj2);
                            }
                            ((List) obj2).add(t);
                        }
                        ControlsFavoritingActivity controlsFavoritingActivity = ControlsFavoritingActivity.this;
                        CharSequence charSequence = text;
                        ArrayList arrayList = new ArrayList(linkedHashMap.size());
                        for (Map.Entry entry : linkedHashMap.entrySet()) {
                            arrayList.add(new StructureContainer((CharSequence) entry.getKey(), new AllModel((List) entry.getValue(), favoritesIds, charSequence, controlsFavoritingActivity.controlsModelCallback)));
                        }
                        ControlsFavoritingActivity$onCreate$$inlined$compareBy$1 controlsFavoritingActivity$onCreate$$inlined$compareBy$1 = ControlsFavoritingActivity.this.comparator;
                        if (controlsFavoritingActivity$onCreate$$inlined$compareBy$1 == null) {
                            controlsFavoritingActivity$onCreate$$inlined$compareBy$1 = null;
                        }
                        controlsFavoritingActivity.listOfStructures = CollectionsKt___CollectionsKt.sortedWith(arrayList, controlsFavoritingActivity$onCreate$$inlined$compareBy$1);
                        ControlsFavoritingActivity controlsFavoritingActivity2 = ControlsFavoritingActivity.this;
                        Iterator<StructureContainer> it = controlsFavoritingActivity2.listOfStructures.iterator();
                        final int i = 0;
                        while (true) {
                            if (!it.hasNext()) {
                                i = -1;
                                break;
                            }
                            StructureContainer next = it.next();
                            Objects.requireNonNull(next);
                            if (Intrinsics.areEqual(next.structureName, controlsFavoritingActivity2.structureExtra)) {
                                break;
                            }
                            i++;
                        }
                        if (i == -1) {
                            i = 0;
                        }
                        if (ControlsFavoritingActivity.this.getIntent().getBooleanExtra("extra_single_structure", false)) {
                            ControlsFavoritingActivity controlsFavoritingActivity3 = ControlsFavoritingActivity.this;
                            controlsFavoritingActivity3.listOfStructures = Collections.singletonList(controlsFavoritingActivity3.listOfStructures.get(i));
                        }
                        final ControlsFavoritingActivity controlsFavoritingActivity4 = ControlsFavoritingActivity.this;
                        controlsFavoritingActivity4.executor.execute(new Runnable() { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$loadControls$1$1.2
                            @Override // java.lang.Runnable
                            public final void run() {
                                ControlsFavoritingActivity controlsFavoritingActivity5 = ControlsFavoritingActivity.this;
                                ViewPager2 viewPager23 = controlsFavoritingActivity5.structurePager;
                                TextView textView3 = null;
                                if (viewPager23 == null) {
                                    viewPager23 = null;
                                }
                                viewPager23.setAdapter(new StructureAdapter(controlsFavoritingActivity5.listOfStructures));
                                ViewPager2 viewPager24 = ControlsFavoritingActivity.this.structurePager;
                                if (viewPager24 == null) {
                                    viewPager24 = null;
                                }
                                int i2 = i;
                                Objects.requireNonNull(viewPager24);
                                FakeDrag fakeDrag = viewPager24.mFakeDragger;
                                Objects.requireNonNull(fakeDrag);
                                ScrollEventAdapter scrollEventAdapter = fakeDrag.mScrollEventAdapter;
                                Objects.requireNonNull(scrollEventAdapter);
                                if (!scrollEventAdapter.mFakeDragging) {
                                    viewPager24.setCurrentItemInternal(i2);
                                    int i3 = 0;
                                    if (errorOnLoad) {
                                        ControlsFavoritingActivity controlsFavoritingActivity6 = ControlsFavoritingActivity.this;
                                        TextView textView4 = controlsFavoritingActivity6.statusText;
                                        if (textView4 == null) {
                                            textView4 = null;
                                        }
                                        Resources resources = controlsFavoritingActivity6.getResources();
                                        Object[] objArr = new Object[1];
                                        Object obj3 = ControlsFavoritingActivity.this.appName;
                                        if (obj3 == null) {
                                            obj3 = "";
                                        }
                                        objArr[0] = obj3;
                                        textView4.setText(resources.getString(2131952170, objArr));
                                        TextView textView5 = ControlsFavoritingActivity.this.subtitleView;
                                        if (textView5 != null) {
                                            textView3 = textView5;
                                        }
                                        textView3.setVisibility(8);
                                    } else if (ControlsFavoritingActivity.this.listOfStructures.isEmpty()) {
                                        ControlsFavoritingActivity controlsFavoritingActivity7 = ControlsFavoritingActivity.this;
                                        TextView textView6 = controlsFavoritingActivity7.statusText;
                                        if (textView6 == null) {
                                            textView6 = null;
                                        }
                                        textView6.setText(controlsFavoritingActivity7.getResources().getString(2131952171));
                                        TextView textView7 = ControlsFavoritingActivity.this.subtitleView;
                                        if (textView7 != null) {
                                            textView3 = textView7;
                                        }
                                        textView3.setVisibility(8);
                                    } else {
                                        TextView textView8 = ControlsFavoritingActivity.this.statusText;
                                        if (textView8 == null) {
                                            textView8 = null;
                                        }
                                        textView8.setVisibility(8);
                                        ControlsFavoritingActivity controlsFavoritingActivity8 = ControlsFavoritingActivity.this;
                                        ManagementPageIndicator managementPageIndicator2 = controlsFavoritingActivity8.pageIndicator;
                                        if (managementPageIndicator2 == null) {
                                            managementPageIndicator2 = null;
                                        }
                                        managementPageIndicator2.setNumPages(controlsFavoritingActivity8.listOfStructures.size());
                                        ManagementPageIndicator managementPageIndicator3 = ControlsFavoritingActivity.this.pageIndicator;
                                        if (managementPageIndicator3 == null) {
                                            managementPageIndicator3 = null;
                                        }
                                        managementPageIndicator3.setLocation(0.0f);
                                        ControlsFavoritingActivity controlsFavoritingActivity9 = ControlsFavoritingActivity.this;
                                        ManagementPageIndicator managementPageIndicator4 = controlsFavoritingActivity9.pageIndicator;
                                        if (managementPageIndicator4 == null) {
                                            managementPageIndicator4 = null;
                                        }
                                        if (controlsFavoritingActivity9.listOfStructures.size() <= 1) {
                                            i3 = 4;
                                        }
                                        managementPageIndicator4.setVisibility(i3);
                                        ManagementPageIndicator managementPageIndicator5 = ControlsFavoritingActivity.this.pageIndicator;
                                        if (managementPageIndicator5 == null) {
                                            managementPageIndicator5 = null;
                                        }
                                        AnimatorSet enterAnimation = R$bool.enterAnimation(managementPageIndicator5);
                                        final ControlsFavoritingActivity controlsFavoritingActivity10 = ControlsFavoritingActivity.this;
                                        enterAnimation.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$loadControls$1$1$2$1$1
                                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                            public final void onAnimationEnd(Animator animator) {
                                                ManagementPageIndicator managementPageIndicator6 = ControlsFavoritingActivity.this.pageIndicator;
                                                ManagementPageIndicator managementPageIndicator7 = null;
                                                if (managementPageIndicator6 == null) {
                                                    managementPageIndicator6 = null;
                                                }
                                                if (managementPageIndicator6.getVisibility() == 0) {
                                                    ControlsFavoritingActivity controlsFavoritingActivity11 = ControlsFavoritingActivity.this;
                                                    if (controlsFavoritingActivity11.mTooltipManager != null) {
                                                        int[] iArr = new int[2];
                                                        ManagementPageIndicator managementPageIndicator8 = controlsFavoritingActivity11.pageIndicator;
                                                        if (managementPageIndicator8 == null) {
                                                            managementPageIndicator8 = null;
                                                        }
                                                        managementPageIndicator8.getLocationOnScreen(iArr);
                                                        int i4 = iArr[0];
                                                        ManagementPageIndicator managementPageIndicator9 = ControlsFavoritingActivity.this.pageIndicator;
                                                        if (managementPageIndicator9 == null) {
                                                            managementPageIndicator9 = null;
                                                        }
                                                        int width = (managementPageIndicator9.getWidth() / 2) + i4;
                                                        int i5 = iArr[1];
                                                        ManagementPageIndicator managementPageIndicator10 = ControlsFavoritingActivity.this.pageIndicator;
                                                        if (managementPageIndicator10 != null) {
                                                            managementPageIndicator7 = managementPageIndicator10;
                                                        }
                                                        int height = managementPageIndicator7.getHeight() + i5;
                                                        TooltipManager tooltipManager = ControlsFavoritingActivity.this.mTooltipManager;
                                                        if (tooltipManager != null) {
                                                            tooltipManager.show(width, height);
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                        enterAnimation.start();
                                        ViewPager2 viewPager25 = ControlsFavoritingActivity.this.structurePager;
                                        if (viewPager25 != null) {
                                            textView3 = viewPager25;
                                        }
                                        R$bool.enterAnimation(textView3).start();
                                    }
                                } else {
                                    throw new IllegalStateException("Cannot change current item when ViewPager2 is fake dragging");
                                }
                            }
                        });
                    }
                }, new Consumer() { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$loadControls$1$2
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ControlsFavoritingActivity.this.cancelLoadRunnable = (Runnable) obj;
                    }
                });
            }
            this.isPagerLoaded = true;
        }
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public final void onStart() {
        super.onStart();
        this.listingController.addCallback(this.listingCallback);
        startTracking();
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public final void onStop() {
        super.onStop();
        this.listingController.removeCallback(this.listingCallback);
        stopTracking();
    }
}
