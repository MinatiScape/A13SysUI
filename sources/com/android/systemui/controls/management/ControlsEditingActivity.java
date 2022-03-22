package com.android.systemui.controls.management;

import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;
import androidx.mediarouter.R$bool;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.CustomIconCache;
import com.android.systemui.controls.controller.ControlsControllerImpl;
import com.android.systemui.controls.controller.ControlsControllerImpl$replaceFavoritesForStructure$1;
import com.android.systemui.controls.controller.Favorites;
import com.android.systemui.controls.controller.StructureInfo;
import com.android.systemui.controls.management.FavoritesModel;
import com.android.systemui.controls.ui.ControlsActivity;
import com.android.systemui.controls.ui.ControlsUiController;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.util.LifecycleActivity;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import kotlin.Unit;
import kotlin.collections.EmptyList;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsEditingActivity.kt */
/* loaded from: classes.dex */
public final class ControlsEditingActivity extends LifecycleActivity {
    public static final /* synthetic */ int $r8$clinit = 0;
    public ComponentName component;
    public final ControlsControllerImpl controller;
    public final ControlsEditingActivity$currentUserTracker$1 currentUserTracker;
    public final CustomIconCache customIconCache;
    public final ControlsEditingActivity$favoritesModelCallback$1 favoritesModelCallback = new FavoritesModel.FavoritesModelCallback() { // from class: com.android.systemui.controls.management.ControlsEditingActivity$favoritesModelCallback$1
        @Override // com.android.systemui.controls.management.FavoritesModel.FavoritesModelCallback
        public final void onNoneChanged(boolean z) {
            TextView textView = null;
            if (z) {
                TextView textView2 = ControlsEditingActivity.this.subtitle;
                if (textView2 != null) {
                    textView = textView2;
                }
                int i = ControlsEditingActivity.$r8$clinit;
                textView.setText(2131952174);
                return;
            }
            TextView textView3 = ControlsEditingActivity.this.subtitle;
            if (textView3 != null) {
                textView = textView3;
            }
            int i2 = ControlsEditingActivity.$r8$clinit;
            textView.setText(2131952173);
        }

        @Override // com.android.systemui.controls.management.ControlsModel.ControlsModelCallback
        public final void onFirstChange() {
            View view = ControlsEditingActivity.this.saveButton;
            if (view == null) {
                view = null;
            }
            view.setEnabled(true);
        }
    };
    public FavoritesModel model;
    public View saveButton;
    public CharSequence structure;
    public TextView subtitle;
    public final ControlsUiController uiController;

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public final void onDestroy() {
        stopTracking();
        super.onDestroy();
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.controls.management.ControlsEditingActivity$currentUserTracker$1] */
    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.controls.management.ControlsEditingActivity$favoritesModelCallback$1] */
    public ControlsEditingActivity(ControlsControllerImpl controlsControllerImpl, final BroadcastDispatcher broadcastDispatcher, CustomIconCache customIconCache, ControlsUiController controlsUiController) {
        this.controller = controlsControllerImpl;
        this.customIconCache = customIconCache;
        this.uiController = controlsUiController;
        this.currentUserTracker = new CurrentUserTracker(broadcastDispatcher) { // from class: com.android.systemui.controls.management.ControlsEditingActivity$currentUserTracker$1
            public final int startingUser;

            {
                this.startingUser = ControlsEditingActivity.this.controller.getCurrentUserId();
            }

            @Override // com.android.systemui.settings.CurrentUserTracker
            public final void onUserSwitched(int i) {
                if (i != this.startingUser) {
                    stopTracking();
                    ControlsEditingActivity.this.finish();
                }
            }
        };
    }

    @Override // android.app.Activity
    public final void onBackPressed() {
        R$bool.exitAnimation((ViewGroup) requireViewById(2131427765), new ControlsEditingActivity$animateExitAndFinish$1(this)).start();
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public final void onCreate(Bundle bundle) {
        Unit unit;
        Unit unit2;
        super.onCreate(bundle);
        ComponentName componentName = (ComponentName) getIntent().getParcelableExtra("android.intent.extra.COMPONENT_NAME");
        CharSequence charSequence = null;
        if (componentName == null) {
            unit = null;
        } else {
            this.component = componentName;
            unit = Unit.INSTANCE;
        }
        if (unit == null) {
            finish();
        }
        CharSequence charSequenceExtra = getIntent().getCharSequenceExtra("extra_structure");
        if (charSequenceExtra == null) {
            unit2 = null;
        } else {
            this.structure = charSequenceExtra;
            unit2 = Unit.INSTANCE;
        }
        if (unit2 == null) {
            finish();
        }
        setContentView(2131624050);
        this.lifecycle.addObserver(new ControlsAnimations$observerForAnimations$1(getIntent(), (ViewGroup) requireViewById(2131427765), getWindow()));
        ViewStub viewStub = (ViewStub) requireViewById(2131428940);
        viewStub.setLayoutResource(2131624052);
        viewStub.inflate();
        TextView textView = (TextView) requireViewById(2131429057);
        CharSequence charSequence2 = this.structure;
        if (charSequence2 == null) {
            charSequence2 = null;
        }
        textView.setText(charSequence2);
        CharSequence charSequence3 = this.structure;
        if (charSequence3 != null) {
            charSequence = charSequence3;
        }
        setTitle(charSequence);
        TextView textView2 = (TextView) requireViewById(2131428947);
        textView2.setText(2131952173);
        this.subtitle = textView2;
        View requireViewById = requireViewById(2131427865);
        Button button = (Button) requireViewById;
        button.setEnabled(false);
        button.setText(2131953177);
        button.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.management.ControlsEditingActivity$bindButtons$1$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ControlsEditingActivity controlsEditingActivity = ControlsEditingActivity.this;
                int i = ControlsEditingActivity.$r8$clinit;
                Objects.requireNonNull(controlsEditingActivity);
                ControlsControllerImpl controlsControllerImpl = controlsEditingActivity.controller;
                ComponentName componentName2 = controlsEditingActivity.component;
                FavoritesModel favoritesModel = null;
                if (componentName2 == null) {
                    componentName2 = null;
                }
                CharSequence charSequence4 = controlsEditingActivity.structure;
                if (charSequence4 == null) {
                    charSequence4 = null;
                }
                FavoritesModel favoritesModel2 = controlsEditingActivity.model;
                if (favoritesModel2 != null) {
                    favoritesModel = favoritesModel2;
                }
                StructureInfo structureInfo = new StructureInfo(componentName2, charSequence4, favoritesModel.getFavorites());
                Objects.requireNonNull(controlsControllerImpl);
                if (controlsControllerImpl.confirmAvailability()) {
                    controlsControllerImpl.executor.execute(new ControlsControllerImpl$replaceFavoritesForStructure$1(structureInfo, controlsControllerImpl));
                }
                ControlsEditingActivity.this.startActivity(new Intent(ControlsEditingActivity.this.getApplicationContext(), ControlsActivity.class), ActivityOptions.makeSceneTransitionAnimation(ControlsEditingActivity.this, new Pair[0]).toBundle());
                ControlsEditingActivity controlsEditingActivity2 = ControlsEditingActivity.this;
                Objects.requireNonNull(controlsEditingActivity2);
                R$bool.exitAnimation((ViewGroup) controlsEditingActivity2.requireViewById(2131427765), new ControlsEditingActivity$animateExitAndFinish$1(controlsEditingActivity2)).start();
            }
        });
        this.saveButton = requireViewById;
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public final void onStart() {
        Object obj;
        List list;
        super.onStart();
        ControlsControllerImpl controlsControllerImpl = this.controller;
        ComponentName componentName = this.component;
        FavoritesModel favoritesModel = null;
        if (componentName == null) {
            componentName = null;
        }
        CharSequence charSequence = this.structure;
        if (charSequence == null) {
            charSequence = null;
        }
        Objects.requireNonNull(controlsControllerImpl);
        Map<ComponentName, ? extends List<StructureInfo>> map = Favorites.favMap;
        EmptyList emptyList = EmptyList.INSTANCE;
        Iterator it = Favorites.getStructuresForComponent(componentName).iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            StructureInfo structureInfo = (StructureInfo) obj;
            Objects.requireNonNull(structureInfo);
            if (Intrinsics.areEqual(structureInfo.structure, charSequence)) {
                break;
            }
        }
        StructureInfo structureInfo2 = (StructureInfo) obj;
        if (structureInfo2 == null) {
            list = null;
        } else {
            list = structureInfo2.controls;
        }
        if (list == null) {
            list = EmptyList.INSTANCE;
        }
        CustomIconCache customIconCache = this.customIconCache;
        ComponentName componentName2 = this.component;
        if (componentName2 == null) {
            componentName2 = null;
        }
        this.model = new FavoritesModel(customIconCache, componentName2, list, this.favoritesModelCallback);
        float f = getResources().getFloat(2131165530);
        final RecyclerView recyclerView = (RecyclerView) requireViewById(2131428259);
        recyclerView.setAlpha(0.0f);
        ControlAdapter controlAdapter = new ControlAdapter(f);
        controlAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() { // from class: com.android.systemui.controls.management.ControlsEditingActivity$setUpList$adapter$1$1
            public boolean hasAnimated;

            @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
            public final void onChanged() {
                if (!this.hasAnimated) {
                    this.hasAnimated = true;
                    R$bool.enterAnimation(RecyclerView.this).start();
                }
            }
        });
        int dimensionPixelSize = getResources().getDimensionPixelSize(2131165556);
        MarginItemDecorator marginItemDecorator = new MarginItemDecorator(dimensionPixelSize, dimensionPixelSize);
        recyclerView.setAdapter(controlAdapter);
        recyclerView.getContext();
        GridLayoutManager controlsEditingActivity$setUpList$1$1 = new GridLayoutManager() { // from class: com.android.systemui.controls.management.ControlsEditingActivity$setUpList$1$1
            @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public final int getRowCountForAccessibility(RecyclerView.Recycler recycler, RecyclerView.State state) {
                int rowCountForAccessibility = super.getRowCountForAccessibility(recycler, state);
                if (rowCountForAccessibility > 0) {
                    return rowCountForAccessibility - 1;
                }
                return rowCountForAccessibility;
            }
        };
        controlsEditingActivity$setUpList$1$1.mSpanSizeLookup = controlAdapter.spanSizeLookup;
        recyclerView.setLayoutManager(controlsEditingActivity$setUpList$1$1);
        recyclerView.addItemDecoration(marginItemDecorator);
        FavoritesModel favoritesModel2 = this.model;
        if (favoritesModel2 == null) {
            favoritesModel2 = null;
        }
        controlAdapter.model = favoritesModel2;
        controlAdapter.notifyDataSetChanged();
        FavoritesModel favoritesModel3 = this.model;
        if (favoritesModel3 == null) {
            favoritesModel3 = null;
        }
        Objects.requireNonNull(favoritesModel3);
        favoritesModel3.adapter = controlAdapter;
        FavoritesModel favoritesModel4 = this.model;
        if (favoritesModel4 != null) {
            favoritesModel = favoritesModel4;
        }
        Objects.requireNonNull(favoritesModel);
        new ItemTouchHelper(favoritesModel.itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        startTracking();
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public final void onStop() {
        super.onStop();
        stopTracking();
    }
}
