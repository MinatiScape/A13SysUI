package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ServiceInfo;
import android.os.UserHandle;
import android.util.Log;
import androidx.cardview.R$attr;
import com.android.settingslib.applications.ServiceListing;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.settings.UserTracker;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.collections.CollectionsKt__IteratorsJVMKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsListingControllerImpl.kt */
/* loaded from: classes.dex */
public final class ControlsListingControllerImpl implements ControlsListingController {
    public Set<ComponentName> availableComponents;
    public List<? extends ServiceInfo> availableServices;
    public final Executor backgroundExecutor;
    public final LinkedHashSet callbacks;
    public final Context context;
    public int currentUserId;
    public ServiceListing serviceListing;
    public final Function1<Context, ServiceListing> serviceListingBuilder;
    public final ControlsListingControllerImpl$serviceListingCallback$1 serviceListingCallback;
    public AtomicInteger userChangeInProgress;

    /* compiled from: ControlsListingControllerImpl.kt */
    /* renamed from: com.android.systemui.controls.management.ControlsListingControllerImpl$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public /* synthetic */ class AnonymousClass1 extends FunctionReferenceImpl implements Function1<Context, ServiceListing> {
        public static final AnonymousClass1 INSTANCE = new AnonymousClass1();

        public AnonymousClass1() {
            super(1, R$attr.class, "createServiceListing", "createServiceListing(Landroid/content/Context;)Lcom/android/settingslib/applications/ServiceListing;", 1);
        }

        @Override // kotlin.jvm.functions.Function1
        public final ServiceListing invoke(Context context) {
            return new ServiceListing(context, "controls_providers", "controls_providers", "android.service.controls.ControlsProviderService", "android.permission.BIND_CONTROLS", "Controls Provider", true);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v8, types: [com.android.systemui.controls.management.ControlsListingControllerImpl$serviceListingCallback$1, java.lang.Object] */
    /* JADX WARN: Unknown variable types count: 1 */
    @com.android.internal.annotations.VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public ControlsListingControllerImpl(android.content.Context r1, java.util.concurrent.Executor r2, kotlin.jvm.functions.Function1<? super android.content.Context, ? extends com.android.settingslib.applications.ServiceListing> r3, com.android.systemui.settings.UserTracker r4) {
        /*
            r0 = this;
            r0.<init>()
            r0.context = r1
            r0.backgroundExecutor = r2
            r0.serviceListingBuilder = r3
            java.lang.Object r1 = r3.invoke(r1)
            com.android.settingslib.applications.ServiceListing r1 = (com.android.settingslib.applications.ServiceListing) r1
            r0.serviceListing = r1
            java.util.LinkedHashSet r1 = new java.util.LinkedHashSet
            r1.<init>()
            r0.callbacks = r1
            kotlin.collections.EmptySet r1 = kotlin.collections.EmptySet.INSTANCE
            r0.availableComponents = r1
            kotlin.collections.EmptyList r1 = kotlin.collections.EmptyList.INSTANCE
            r0.availableServices = r1
            java.util.concurrent.atomic.AtomicInteger r1 = new java.util.concurrent.atomic.AtomicInteger
            r2 = 0
            r1.<init>(r2)
            r0.userChangeInProgress = r1
            int r1 = r4.getUserId()
            r0.currentUserId = r1
            com.android.systemui.controls.management.ControlsListingControllerImpl$serviceListingCallback$1 r1 = new com.android.systemui.controls.management.ControlsListingControllerImpl$serviceListingCallback$1
            r1.<init>()
            r0.serviceListingCallback = r1
            java.lang.String r2 = "ControlsListingControllerImpl"
            java.lang.String r3 = "Initializing"
            android.util.Log.d(r2, r3)
            com.android.settingslib.applications.ServiceListing r2 = r0.serviceListing
            java.util.Objects.requireNonNull(r2)
            java.util.ArrayList r2 = r2.mCallbacks
            r2.add(r1)
            com.android.settingslib.applications.ServiceListing r1 = r0.serviceListing
            r2 = 1
            r1.setListening(r2)
            com.android.settingslib.applications.ServiceListing r0 = r0.serviceListing
            r0.reload()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.management.ControlsListingControllerImpl.<init>(android.content.Context, java.util.concurrent.Executor, kotlin.jvm.functions.Function1, com.android.systemui.settings.UserTracker):void");
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(ControlsListingController.ControlsListingCallback controlsListingCallback) {
        this.backgroundExecutor.execute(new ControlsListingControllerImpl$addCallback$1(this, controlsListingCallback));
    }

    @Override // com.android.systemui.util.UserAwareController
    public final void changeUser(final UserHandle userHandle) {
        this.userChangeInProgress.incrementAndGet();
        this.serviceListing.setListening(false);
        this.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.management.ControlsListingControllerImpl$changeUser$1
            @Override // java.lang.Runnable
            public final void run() {
                if (ControlsListingControllerImpl.this.userChangeInProgress.decrementAndGet() == 0) {
                    ControlsListingControllerImpl.this.currentUserId = userHandle.getIdentifier();
                    Context createContextAsUser = ControlsListingControllerImpl.this.context.createContextAsUser(userHandle, 0);
                    ControlsListingControllerImpl controlsListingControllerImpl = ControlsListingControllerImpl.this;
                    controlsListingControllerImpl.serviceListing = controlsListingControllerImpl.serviceListingBuilder.invoke(createContextAsUser);
                    ControlsListingControllerImpl controlsListingControllerImpl2 = ControlsListingControllerImpl.this;
                    ServiceListing serviceListing = controlsListingControllerImpl2.serviceListing;
                    ControlsListingControllerImpl$serviceListingCallback$1 controlsListingControllerImpl$serviceListingCallback$1 = controlsListingControllerImpl2.serviceListingCallback;
                    Objects.requireNonNull(serviceListing);
                    serviceListing.mCallbacks.add(controlsListingControllerImpl$serviceListingCallback$1);
                    ControlsListingControllerImpl.this.serviceListing.setListening(true);
                    ControlsListingControllerImpl.this.serviceListing.reload();
                }
            }
        });
    }

    public final ArrayList getCurrentServices() {
        List<? extends ServiceInfo> list = this.availableServices;
        ArrayList arrayList = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(list, 10));
        for (ServiceInfo serviceInfo : list) {
            arrayList.add(new ControlsServiceInfo(this.context, serviceInfo));
        }
        return arrayList;
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(ControlsListingController.ControlsListingCallback controlsListingCallback) {
        final ControlsListingController.ControlsListingCallback controlsListingCallback2 = controlsListingCallback;
        this.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.management.ControlsListingControllerImpl$removeCallback$1
            @Override // java.lang.Runnable
            public final void run() {
                Log.d("ControlsListingControllerImpl", "Unsubscribing callback");
                ControlsListingControllerImpl.this.callbacks.remove(controlsListingCallback2);
            }
        });
    }

    @Override // com.android.systemui.controls.management.ControlsListingController
    public final CharSequence getAppLabel(ComponentName componentName) {
        Object obj;
        Iterator it = getCurrentServices().iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            if (Intrinsics.areEqual(((ControlsServiceInfo) obj).componentName, componentName)) {
                break;
            }
        }
        ControlsServiceInfo controlsServiceInfo = (ControlsServiceInfo) obj;
        if (controlsServiceInfo == null) {
            return null;
        }
        return controlsServiceInfo.loadLabel();
    }

    public ControlsListingControllerImpl(Context context, Executor executor, UserTracker userTracker) {
        this(context, executor, AnonymousClass1.INSTANCE, userTracker);
    }

    @Override // com.android.systemui.util.UserAwareController
    public final int getCurrentUserId() {
        return this.currentUserId;
    }
}
