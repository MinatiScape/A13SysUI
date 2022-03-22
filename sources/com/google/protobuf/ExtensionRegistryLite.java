package com.google.protobuf;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public final class ExtensionRegistryLite {
    public static final ExtensionRegistryLite EMPTY_REGISTRY_LITE;
    public static volatile ExtensionRegistryLite emptyRegistry;
    public final Map<Object, Object> extensionsByNumber;

    public ExtensionRegistryLite() {
        this.extensionsByNumber = new HashMap();
    }

    static {
        try {
            Class.forName("com.google.protobuf.Extension");
        } catch (ClassNotFoundException unused) {
        }
        EMPTY_REGISTRY_LITE = new ExtensionRegistryLite(0);
    }

    public static ExtensionRegistryLite getEmptyRegistry() {
        ExtensionRegistryLite extensionRegistryLite = emptyRegistry;
        if (extensionRegistryLite == null) {
            synchronized (ExtensionRegistryLite.class) {
                extensionRegistryLite = emptyRegistry;
                if (extensionRegistryLite == null) {
                    Class<?> cls = ExtensionRegistryFactory.EXTENSION_REGISTRY_CLASS;
                    if (cls != null) {
                        try {
                            extensionRegistryLite = (ExtensionRegistryLite) cls.getDeclaredMethod("getEmptyRegistry", new Class[0]).invoke(null, new Object[0]);
                        } catch (Exception unused) {
                        }
                        emptyRegistry = extensionRegistryLite;
                    }
                    extensionRegistryLite = EMPTY_REGISTRY_LITE;
                    emptyRegistry = extensionRegistryLite;
                }
            }
        }
        return extensionRegistryLite;
    }

    public ExtensionRegistryLite(int i) {
        this.extensionsByNumber = Collections.emptyMap();
    }
}
