package dagger.internal;

import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DelegateFactory<T> implements Factory<T> {
    public Provider<T> delegate;

    public static <T> void setDelegate(Provider<T> provider, Provider<T> provider2) {
        DelegateFactory delegateFactory = (DelegateFactory) provider;
        if (delegateFactory.delegate == null) {
            delegateFactory.delegate = provider2;
            return;
        }
        throw new IllegalStateException();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final T mo144get() {
        Provider<T> provider = this.delegate;
        if (provider != null) {
            return provider.mo144get();
        }
        throw new IllegalStateException();
    }
}
