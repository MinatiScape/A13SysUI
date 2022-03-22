package com.android.systemui.controls.management;

import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import androidx.mediarouter.R$bool;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.ui.ControlsActivity;
import com.android.systemui.controls.ui.ControlsUiController;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.util.LifecycleActivity;
import java.util.concurrent.Executor;
/* compiled from: ControlsProviderSelectorActivity.kt */
/* loaded from: classes.dex */
public final class ControlsProviderSelectorActivity extends LifecycleActivity {
    public final Executor backExecutor;
    public boolean backShouldExit;
    public final ControlsController controlsController;
    public final ControlsProviderSelectorActivity$currentUserTracker$1 currentUserTracker;
    public final Executor executor;
    public final ControlsListingController listingController;
    public RecyclerView recyclerView;
    public final ControlsUiController uiController;

    @Override // android.app.Activity
    public final void onBackPressed() {
        if (!this.backShouldExit) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(getApplicationContext(), ControlsActivity.class));
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, new Pair[0]).toBundle());
        }
        R$bool.exitAnimation((ViewGroup) requireViewById(2131427765), new ControlsProviderSelectorActivity$animateExitAndFinish$1(this)).start();
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public final void onDestroy() {
        stopTracking();
        super.onDestroy();
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.controls.management.ControlsProviderSelectorActivity$currentUserTracker$1] */
    public ControlsProviderSelectorActivity(Executor executor, Executor executor2, ControlsListingController controlsListingController, ControlsController controlsController, final BroadcastDispatcher broadcastDispatcher, ControlsUiController controlsUiController) {
        this.executor = executor;
        this.backExecutor = executor2;
        this.listingController = controlsListingController;
        this.controlsController = controlsController;
        this.uiController = controlsUiController;
        this.currentUserTracker = new CurrentUserTracker(broadcastDispatcher) { // from class: com.android.systemui.controls.management.ControlsProviderSelectorActivity$currentUserTracker$1
            public final int startingUser;

            {
                this.startingUser = ControlsProviderSelectorActivity.this.listingController.getCurrentUserId();
            }

            @Override // com.android.systemui.settings.CurrentUserTracker
            public final void onUserSwitched(int i) {
                if (i != this.startingUser) {
                    stopTracking();
                    ControlsProviderSelectorActivity.this.finish();
                }
            }
        };
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(2131624050);
        Lifecycle lifecycle = this.lifecycle;
        Window window = getWindow();
        lifecycle.addObserver(new ControlsAnimations$observerForAnimations$1(getIntent(), (ViewGroup) requireViewById(2131427765), window));
        ViewStub viewStub = (ViewStub) requireViewById(2131428940);
        viewStub.setLayoutResource(2131624051);
        viewStub.inflate();
        RecyclerView recyclerView = (RecyclerView) requireViewById(2131428259);
        this.recyclerView = recyclerView;
        getApplicationContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(1));
        TextView textView = (TextView) requireViewById(2131429057);
        textView.setText(textView.getResources().getText(2131952202));
        Button button = (Button) requireViewById(2131428544);
        button.setVisibility(0);
        button.setText(17039360);
        button.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.management.ControlsProviderSelectorActivity$onCreate$3$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ControlsProviderSelectorActivity.this.onBackPressed();
            }
        });
        requireViewById(2131427865).setVisibility(8);
        this.backShouldExit = getIntent().getBooleanExtra("back_should_exit", false);
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public final void onStart() {
        super.onStart();
        startTracking();
        RecyclerView recyclerView = this.recyclerView;
        RecyclerView recyclerView2 = null;
        if (recyclerView == null) {
            recyclerView = null;
        }
        recyclerView.setAlpha(0.0f);
        RecyclerView recyclerView3 = this.recyclerView;
        if (recyclerView3 != null) {
            recyclerView2 = recyclerView3;
        }
        AppAdapter appAdapter = new AppAdapter(this.backExecutor, this.executor, this.lifecycle, this.listingController, LayoutInflater.from(this), new ControlsProviderSelectorActivity$onStart$1(this), new FavoritesRenderer(getResources(), new ControlsProviderSelectorActivity$onStart$2(this.controlsController)), getResources());
        appAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() { // from class: com.android.systemui.controls.management.ControlsProviderSelectorActivity$onStart$3$1
            public boolean hasAnimated;

            @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
            public final void onChanged() {
                if (!this.hasAnimated) {
                    this.hasAnimated = true;
                    RecyclerView recyclerView4 = ControlsProviderSelectorActivity.this.recyclerView;
                    if (recyclerView4 == null) {
                        recyclerView4 = null;
                    }
                    R$bool.enterAnimation(recyclerView4).start();
                }
            }
        });
        recyclerView2.setAdapter(appAdapter);
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public final void onStop() {
        super.onStop();
        stopTracking();
    }
}
