package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.util.ArrayMap;
import com.android.systemui.plugins.IntentButtonProvider;
import com.android.systemui.plugins.Plugin;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.statusbar.policy.ExtensionController;
import com.android.systemui.tuner.LockscreenFragment;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.leak.LeakDetector;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public final class ExtensionControllerImpl implements ExtensionController {
    public final ConfigurationController mConfigurationController;
    public final Context mDefaultContext;
    public final LeakDetector mLeakDetector;
    public final PluginManager mPluginManager;
    public final TunerService mTunerService;

    /* loaded from: classes.dex */
    public class ExtensionBuilder<T> {
        public ExtensionImpl<T> mExtension;

        public final ExtensionImpl build() {
            Collections.sort(this.mExtension.mProducers, Comparator.comparingInt(ExtensionControllerImpl$ExtensionBuilder$$ExternalSyntheticLambda0.INSTANCE));
            ExtensionImpl.m112$$Nest$mnotifyChanged(this.mExtension);
            return this.mExtension;
        }

        public final ExtensionBuilder withDefault(Supplier supplier) {
            ExtensionImpl<T> extensionImpl = this.mExtension;
            Objects.requireNonNull(extensionImpl);
            extensionImpl.mProducers.add(new ExtensionImpl.Default(supplier));
            return this;
        }

        public final ExtensionBuilder withTunerFactory(LockscreenFragment.LockButtonFactory lockButtonFactory) {
            ExtensionImpl<T> extensionImpl = this.mExtension;
            String[] strArr = {lockButtonFactory.mKey};
            Objects.requireNonNull(extensionImpl);
            extensionImpl.mProducers.add(new ExtensionImpl.TunerItem(lockButtonFactory, strArr));
            return this;
        }

        public ExtensionBuilder(ExtensionControllerImpl extensionControllerImpl) {
            this.mExtension = new ExtensionImpl<>();
        }
    }

    /* loaded from: classes.dex */
    public class ExtensionImpl<T> implements ExtensionController.Extension<T> {
        public T mItem;
        public Context mPluginContext;
        public final ArrayList<Item<T>> mProducers = new ArrayList<>();
        public final ArrayList<Consumer<T>> mCallbacks = new ArrayList<>();

        /* loaded from: classes.dex */
        public class Default<T> implements Item<T> {
            public final Supplier<T> mSupplier;

            @Override // com.android.systemui.statusbar.policy.ExtensionControllerImpl.Producer
            public final void destroy() {
            }

            @Override // com.android.systemui.statusbar.policy.ExtensionControllerImpl.Item
            public final int sortOrder() {
                return 4;
            }

            @Override // com.android.systemui.statusbar.policy.ExtensionControllerImpl.Producer
            public final T get() {
                return this.mSupplier.get();
            }

            public Default(Supplier supplier) {
                this.mSupplier = supplier;
            }
        }

        /* loaded from: classes.dex */
        public class PluginItem<P extends Plugin> implements Item<T>, PluginListener<P> {
            public final ExtensionController.PluginConverter<T, P> mConverter;
            public T mItem;

            @Override // com.android.systemui.statusbar.policy.ExtensionControllerImpl.Item
            public final int sortOrder() {
                return 0;
            }

            public PluginItem(String str, Class<P> cls, ExtensionController.PluginConverter<T, P> pluginConverter) {
                this.mConverter = pluginConverter;
                ExtensionControllerImpl.this.mPluginManager.addPluginListener(str, this, cls);
            }

            @Override // com.android.systemui.statusbar.policy.ExtensionControllerImpl.Producer
            public final void destroy() {
                ExtensionControllerImpl.this.mPluginManager.removePluginListener(this);
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.android.systemui.plugins.PluginListener
            public final void onPluginConnected(P p, Context context) {
                ExtensionImpl.this.mPluginContext = context;
                ExtensionController.PluginConverter<T, P> pluginConverter = this.mConverter;
                if (pluginConverter != null) {
                    this.mItem = (T) pluginConverter.getInterfaceFromPlugin(p);
                } else {
                    this.mItem = p;
                }
                ExtensionImpl.m112$$Nest$mnotifyChanged(ExtensionImpl.this);
            }

            @Override // com.android.systemui.plugins.PluginListener
            public final void onPluginDisconnected(P p) {
                ExtensionImpl extensionImpl = ExtensionImpl.this;
                extensionImpl.mPluginContext = null;
                this.mItem = null;
                ExtensionImpl.m112$$Nest$mnotifyChanged(extensionImpl);
            }

            @Override // com.android.systemui.statusbar.policy.ExtensionControllerImpl.Producer
            public final T get() {
                return this.mItem;
            }
        }

        /* loaded from: classes.dex */
        public class TunerItem<T> implements Item<T>, TunerService.Tunable {
            public final ExtensionController.TunerFactory<T> mFactory;
            public IntentButtonProvider.IntentButton mItem;
            public final ArrayMap<String, String> mSettings = new ArrayMap<>();

            @Override // com.android.systemui.statusbar.policy.ExtensionControllerImpl.Item
            public final int sortOrder() {
                return 1;
            }

            public TunerItem(LockscreenFragment.LockButtonFactory lockButtonFactory, String... strArr) {
                this.mFactory = lockButtonFactory;
                ExtensionControllerImpl.this.mTunerService.addTunable(this, strArr);
            }

            @Override // com.android.systemui.statusbar.policy.ExtensionControllerImpl.Producer
            public final void destroy() {
                ExtensionControllerImpl.this.mTunerService.removeTunable(this);
            }

            @Override // com.android.systemui.tuner.TunerService.Tunable
            public final void onTuningChanged(String str, String str2) {
                this.mSettings.put(str, str2);
                this.mItem = ((LockscreenFragment.LockButtonFactory) this.mFactory).create(this.mSettings);
                ExtensionImpl.m112$$Nest$mnotifyChanged(ExtensionImpl.this);
            }

            @Override // com.android.systemui.statusbar.policy.ExtensionControllerImpl.Producer
            public final T get() {
                return (T) this.mItem;
            }
        }

        public ExtensionImpl() {
        }

        /* renamed from: -$$Nest$mnotifyChanged  reason: not valid java name */
        public static void m112$$Nest$mnotifyChanged(ExtensionImpl extensionImpl) {
            Objects.requireNonNull(extensionImpl);
            T t = extensionImpl.mItem;
            if (t != null) {
                ExtensionControllerImpl.this.mLeakDetector.trackGarbage(t);
            }
            extensionImpl.mItem = null;
            int i = 0;
            while (true) {
                if (i >= extensionImpl.mProducers.size()) {
                    break;
                }
                T t2 = extensionImpl.mProducers.get(i).get();
                if (t2 != null) {
                    extensionImpl.mItem = t2;
                    break;
                }
                i++;
            }
            for (int i2 = 0; i2 < extensionImpl.mCallbacks.size(); i2++) {
                extensionImpl.mCallbacks.get(i2).accept(extensionImpl.mItem);
            }
        }
    }

    /* loaded from: classes.dex */
    public interface Item<T> extends Producer<T> {
        int sortOrder();
    }

    /* loaded from: classes.dex */
    public interface Producer<T> {
        void destroy();

        T get();
    }

    @Override // com.android.systemui.statusbar.policy.ExtensionController
    public final ExtensionBuilder newExtension() {
        return new ExtensionBuilder(this);
    }

    public ExtensionControllerImpl(Context context, LeakDetector leakDetector, PluginManager pluginManager, TunerService tunerService, ConfigurationController configurationController) {
        this.mDefaultContext = context;
        this.mLeakDetector = leakDetector;
        this.mPluginManager = pluginManager;
        this.mTunerService = tunerService;
        this.mConfigurationController = configurationController;
    }
}
