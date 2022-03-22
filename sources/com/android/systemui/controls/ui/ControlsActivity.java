package com.android.systemui.controls.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import androidx.mediarouter.R$bool;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.management.ControlsAnimations$observerForAnimations$1;
import com.android.systemui.util.LifecycleActivity;
/* compiled from: ControlsActivity.kt */
/* loaded from: classes.dex */
public final class ControlsActivity extends LifecycleActivity {
    public final BroadcastDispatcher broadcastDispatcher;
    public ControlsActivity$initBroadcastReceiver$1 broadcastReceiver;
    public ViewGroup parent;
    public final ControlsUiController uiController;

    public ControlsActivity(ControlsUiController controlsUiController, BroadcastDispatcher broadcastDispatcher) {
        this.uiController = controlsUiController;
        this.broadcastDispatcher = broadcastDispatcher;
    }

    /* JADX WARN: Type inference failed for: r7v5, types: [com.android.systemui.controls.ui.ControlsActivity$initBroadcastReceiver$1] */
    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(2131624048);
        Lifecycle lifecycle = this.lifecycle;
        Window window = getWindow();
        lifecycle.addObserver(new ControlsAnimations$observerForAnimations$1(getIntent(), (ViewGroup) requireViewById(2131427753), window));
        ((ViewGroup) requireViewById(2131427753)).setOnApplyWindowInsetsListener(ControlsActivity$onCreate$1$1.INSTANCE);
        this.broadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.controls.ui.ControlsActivity$initBroadcastReceiver$1
            @Override // android.content.BroadcastReceiver
            public final void onReceive(Context context, Intent intent) {
                if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
                    ControlsActivity.this.finish();
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        BroadcastDispatcher broadcastDispatcher = this.broadcastDispatcher;
        ControlsActivity$initBroadcastReceiver$1 controlsActivity$initBroadcastReceiver$1 = this.broadcastReceiver;
        if (controlsActivity$initBroadcastReceiver$1 == null) {
            controlsActivity$initBroadcastReceiver$1 = null;
        }
        BroadcastDispatcher.registerReceiver$default(broadcastDispatcher, controlsActivity$initBroadcastReceiver$1, intentFilter, null, null, 28);
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public final void onDestroy() {
        super.onDestroy();
        BroadcastDispatcher broadcastDispatcher = this.broadcastDispatcher;
        ControlsActivity$initBroadcastReceiver$1 controlsActivity$initBroadcastReceiver$1 = this.broadcastReceiver;
        if (controlsActivity$initBroadcastReceiver$1 == null) {
            controlsActivity$initBroadcastReceiver$1 = null;
        }
        broadcastDispatcher.unregisterReceiver(controlsActivity$initBroadcastReceiver$1);
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public final void onPause() {
        super.onPause();
        this.uiController.hide();
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public final void onResume() {
        super.onResume();
        ViewGroup viewGroup = (ViewGroup) requireViewById(2131428025);
        this.parent = viewGroup;
        viewGroup.setAlpha(0.0f);
        ControlsUiController controlsUiController = this.uiController;
        ViewGroup viewGroup2 = this.parent;
        ViewGroup viewGroup3 = null;
        if (viewGroup2 == null) {
            viewGroup2 = null;
        }
        controlsUiController.show(viewGroup2, new Runnable() { // from class: com.android.systemui.controls.ui.ControlsActivity$onResume$1
            @Override // java.lang.Runnable
            public final void run() {
                ControlsActivity.this.finish();
            }
        }, this);
        ViewGroup viewGroup4 = this.parent;
        if (viewGroup4 != null) {
            viewGroup3 = viewGroup4;
        }
        R$bool.enterAnimation(viewGroup3).start();
    }

    @Override // android.app.Activity
    public final void onBackPressed() {
        finish();
    }
}
