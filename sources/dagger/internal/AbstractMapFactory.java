package dagger.internal;

import androidx.lifecycle.runtime.R$id;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.inject.Provider;
/* loaded from: classes.dex */
public abstract class AbstractMapFactory<K, V, V2> implements Factory<Map<K, V2>> {
    public final Map<K, Provider<V>> contributingMap;

    /* loaded from: classes.dex */
    public static abstract class Builder<K, V, V2> {
        public final LinkedHashMap<K, Provider<V>> map;

        public Builder(int i) {
            this.map = R$id.newLinkedHashMapWithExpectedSize(i);
        }
    }

    public AbstractMapFactory(LinkedHashMap linkedHashMap) {
        this.contributingMap = Collections.unmodifiableMap(linkedHashMap);
    }
}
