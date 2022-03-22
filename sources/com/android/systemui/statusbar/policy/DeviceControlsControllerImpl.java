package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.android.keyguard.KeyguardStatusView$$ExternalSyntheticLambda0;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.controller.SeedResponse;
import com.android.systemui.controls.dagger.ControlsComponent;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.policy.DeviceControlsController;
import com.android.systemui.util.settings.SecureSettings;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.EmptySet;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DeviceControlsControllerImpl.kt */
/* loaded from: classes.dex */
public final class DeviceControlsControllerImpl implements DeviceControlsController {
    public DeviceControlsController.Callback callback;
    public final Context context;
    public final ControlsComponent controlsComponent;
    public final DeviceControlsControllerImpl$listingCallback$1 listingCallback = new ControlsListingController.ControlsListingCallback() { // from class: com.android.systemui.statusbar.policy.DeviceControlsControllerImpl$listingCallback$1
        @Override // com.android.systemui.controls.management.ControlsListingController.ControlsListingCallback
        public final void onServicesUpdated(ArrayList arrayList) {
            if (!arrayList.isEmpty()) {
                final DeviceControlsControllerImpl deviceControlsControllerImpl = DeviceControlsControllerImpl.this;
                Objects.requireNonNull(deviceControlsControllerImpl);
                String[] stringArray = deviceControlsControllerImpl.context.getResources().getStringArray(2130903089);
                final SharedPreferences sharedPreferences = deviceControlsControllerImpl.userContextProvider.getUserContext().getSharedPreferences("controls_prefs", 0);
                Set<String> stringSet = sharedPreferences.getStringSet("SeedingCompleted", EmptySet.INSTANCE);
                ControlsController controlsController = deviceControlsControllerImpl.controlsComponent.getControlsController().get();
                ArrayList arrayList2 = new ArrayList();
                for (int i = 0; i < Math.min(2, stringArray.length); i++) {
                    String str = stringArray[i];
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        ControlsServiceInfo controlsServiceInfo = (ControlsServiceInfo) it.next();
                        if (str.equals(controlsServiceInfo.componentName.getPackageName()) && !stringSet.contains(str)) {
                            if (controlsController.countFavoritesForComponent(controlsServiceInfo.componentName) > 0) {
                                Set<String> mutableSet = CollectionsKt___CollectionsKt.toMutableSet(sharedPreferences.getStringSet("SeedingCompleted", EmptySet.INSTANCE));
                                mutableSet.add(str);
                                sharedPreferences.edit().putStringSet("SeedingCompleted", mutableSet).apply();
                            } else {
                                arrayList2.add(controlsServiceInfo.componentName);
                            }
                        }
                    }
                }
                if (!arrayList2.isEmpty()) {
                    controlsController.seedFavoritesForComponents(arrayList2, new Consumer() { // from class: com.android.systemui.statusbar.policy.DeviceControlsControllerImpl$seedFavorites$2
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            SeedResponse seedResponse = (SeedResponse) obj;
                            Log.d("DeviceControlsControllerImpl", Intrinsics.stringPlus("Controls seeded: ", seedResponse));
                            if (seedResponse.accepted) {
                                DeviceControlsControllerImpl deviceControlsControllerImpl2 = DeviceControlsControllerImpl.this;
                                SharedPreferences sharedPreferences2 = sharedPreferences;
                                String str2 = seedResponse.packageName;
                                Objects.requireNonNull(deviceControlsControllerImpl2);
                                Set<String> mutableSet2 = CollectionsKt___CollectionsKt.toMutableSet(sharedPreferences2.getStringSet("SeedingCompleted", EmptySet.INSTANCE));
                                mutableSet2.add(str2);
                                sharedPreferences2.edit().putStringSet("SeedingCompleted", mutableSet2).apply();
                                DeviceControlsControllerImpl deviceControlsControllerImpl3 = DeviceControlsControllerImpl.this;
                                Objects.requireNonNull(deviceControlsControllerImpl3);
                                if (deviceControlsControllerImpl3.position == null) {
                                    DeviceControlsControllerImpl deviceControlsControllerImpl4 = DeviceControlsControllerImpl.this;
                                    Objects.requireNonNull(deviceControlsControllerImpl4);
                                    deviceControlsControllerImpl4.position = 7;
                                }
                                DeviceControlsControllerImpl.this.fireControlsUpdate();
                                Optional<ControlsListingController> controlsListingController = DeviceControlsControllerImpl.this.controlsComponent.getControlsListingController();
                                final DeviceControlsControllerImpl deviceControlsControllerImpl5 = DeviceControlsControllerImpl.this;
                                controlsListingController.ifPresent(new Consumer() { // from class: com.android.systemui.statusbar.policy.DeviceControlsControllerImpl$seedFavorites$2.1
                                    @Override // java.util.function.Consumer
                                    public final void accept(Object obj2) {
                                        ((ControlsListingController) obj2).removeCallback(DeviceControlsControllerImpl.this.listingCallback);
                                    }
                                });
                            }
                        }
                    });
                }
            }
        }
    };
    public Integer position;
    public final SecureSettings secureSettings;
    public final UserContextProvider userContextProvider;

    @Override // com.android.systemui.statusbar.policy.DeviceControlsController
    public final void removeCallback() {
        this.position = null;
        this.callback = null;
        this.controlsComponent.getControlsListingController().ifPresent(new Consumer() { // from class: com.android.systemui.statusbar.policy.DeviceControlsControllerImpl$removeCallback$1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((ControlsListingController) obj).removeCallback(DeviceControlsControllerImpl.this.listingCallback);
            }
        });
    }

    public final void fireControlsUpdate() {
        Log.i("DeviceControlsControllerImpl", Intrinsics.stringPlus("Setting DeviceControlsTile position: ", this.position));
        DeviceControlsController.Callback callback = this.callback;
        if (callback != null) {
            Integer num = this.position;
            AutoTileManager.AnonymousClass4 r0 = (AutoTileManager.AnonymousClass4) callback;
            if (!AutoTileManager.this.mAutoTracker.isAdded("controls")) {
                if (num != null) {
                    AutoTileManager.this.mHost.addTile("controls", num.intValue());
                }
                AutoTileManager.this.mAutoTracker.setTileAdded("controls");
                AutoTileManager.this.mHandler.post(new KeyguardStatusView$$ExternalSyntheticLambda0(r0, 3));
            }
        }
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.statusbar.policy.DeviceControlsControllerImpl$listingCallback$1] */
    public DeviceControlsControllerImpl(Context context, ControlsComponent controlsComponent, UserContextProvider userContextProvider, SecureSettings secureSettings) {
        this.context = context;
        this.controlsComponent = controlsComponent;
        this.userContextProvider = userContextProvider;
        this.secureSettings = secureSettings;
    }

    @Override // com.android.systemui.statusbar.policy.DeviceControlsController
    public final void setCallback(AutoTileManager.AnonymousClass4 r3) {
        removeCallback();
        this.callback = r3;
        if (this.secureSettings.getInt("controls_enabled", 1) == 0) {
            fireControlsUpdate();
            return;
        }
        this.controlsComponent.getControlsController().ifPresent(new Consumer() { // from class: com.android.systemui.statusbar.policy.DeviceControlsControllerImpl$checkMigrationToQs$1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                if (!((ControlsController) obj).getFavorites().isEmpty()) {
                    DeviceControlsControllerImpl deviceControlsControllerImpl = DeviceControlsControllerImpl.this;
                    Objects.requireNonNull(deviceControlsControllerImpl);
                    deviceControlsControllerImpl.position = 3;
                    DeviceControlsControllerImpl.this.fireControlsUpdate();
                }
            }
        });
        this.controlsComponent.getControlsListingController().ifPresent(new Consumer() { // from class: com.android.systemui.statusbar.policy.DeviceControlsControllerImpl$setCallback$1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((ControlsListingController) obj).addCallback(DeviceControlsControllerImpl.this.listingCallback);
            }
        });
    }
}
