package com.android.systemui.fragments;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Handler;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.qs.QSFragment;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.wm.shell.transition.OneShotRemoteHandler$2$$ExternalSyntheticLambda0;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Objects;
/* loaded from: classes.dex */
public final class FragmentService implements Dumpable {
    public final ArrayMap<View, FragmentHostState> mHosts = new ArrayMap<>();
    public final ArrayMap<String, FragmentInstantiationInfo> mInjectionMap = new ArrayMap<>();
    public final Handler mHandler = new Handler();
    public AnonymousClass1 mConfigurationListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.fragments.FragmentService.1
        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onConfigChanged(Configuration configuration) {
            for (FragmentHostState fragmentHostState : FragmentService.this.mHosts.values()) {
                Objects.requireNonNull(fragmentHostState);
                FragmentService.this.mHandler.post(new OneShotRemoteHandler$2$$ExternalSyntheticLambda0(fragmentHostState, configuration, 1));
            }
        }
    };

    /* loaded from: classes.dex */
    public interface FragmentCreator {

        /* loaded from: classes.dex */
        public interface Factory {
            FragmentCreator build();
        }

        QSFragment createQSFragment();
    }

    /* loaded from: classes.dex */
    public class FragmentHostState {
        public FragmentHostManager mFragmentHostManager;
        public final View mView;

        public FragmentHostState(View view) {
            this.mView = view;
            this.mFragmentHostManager = new FragmentHostManager(FragmentService.this, view);
        }
    }

    /* loaded from: classes.dex */
    public static class FragmentInstantiationInfo {
        public final Object mDaggerComponent;
        public final Method mMethod;

        public FragmentInstantiationInfo(Method method, Object obj) {
            this.mMethod = method;
            this.mDaggerComponent = obj;
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("Dumping fragments:");
        for (FragmentHostState fragmentHostState : this.mHosts.values()) {
            fragmentHostState.mFragmentHostManager.getFragmentManager().dump("  ", fileDescriptor, printWriter, strArr);
        }
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [com.android.systemui.fragments.FragmentService$1] */
    public FragmentService(FragmentCreator.Factory factory, ConfigurationController configurationController, DumpManager dumpManager) {
        addFragmentInstantiationProvider(factory.build());
        configurationController.addCallback(this.mConfigurationListener);
        dumpManager.registerDumpable("FragmentService", this);
    }

    public final void addFragmentInstantiationProvider(Object obj) {
        Method[] declaredMethods;
        for (Method method : obj.getClass().getDeclaredMethods()) {
            if (Fragment.class.isAssignableFrom(method.getReturnType()) && (method.getModifiers() & 1) != 0) {
                String name = method.getReturnType().getName();
                if (this.mInjectionMap.containsKey(name)) {
                    Log.w("FragmentService", "Fragment " + name + " is already provided by different Dagger component; Not adding method");
                } else {
                    this.mInjectionMap.put(name, new FragmentInstantiationInfo(method, obj));
                }
            }
        }
    }

    public final FragmentHostManager getFragmentHostManager(View view) {
        View rootView = view.getRootView();
        FragmentHostState fragmentHostState = this.mHosts.get(rootView);
        if (fragmentHostState == null) {
            fragmentHostState = new FragmentHostState(rootView);
            this.mHosts.put(rootView, fragmentHostState);
        }
        return fragmentHostState.mFragmentHostManager;
    }
}
