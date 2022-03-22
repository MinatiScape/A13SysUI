package com.android.wm.shell.dagger;

import com.android.wm.shell.common.TransactionPool;
import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class WMShellBaseModule_ProvideTransactionPoolFactory implements Factory<TransactionPool> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final WMShellBaseModule_ProvideTransactionPoolFactory INSTANCE = new WMShellBaseModule_ProvideTransactionPoolFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new TransactionPool();
    }
}
