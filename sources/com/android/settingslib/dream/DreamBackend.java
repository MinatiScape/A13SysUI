package com.android.settingslib.dream;

import android.content.Context;
import android.content.res.Resources;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.service.dreams.IDreamManager;
import android.util.Log;
import com.android.wm.shell.bubbles.BubbleData$$ExternalSyntheticLambda4;
import com.android.wm.shell.bubbles.BubbleData$$ExternalSyntheticLambda5;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/* loaded from: classes.dex */
public final class DreamBackend {
    public static DreamBackend sInstance;
    public final Context mContext;
    public final Set<Integer> mDefaultEnabledComplications;

    public DreamBackend(Context context) {
        Context applicationContext = context.getApplicationContext();
        this.mContext = applicationContext;
        Resources resources = applicationContext.getResources();
        IDreamManager asInterface = IDreamManager.Stub.asInterface(ServiceManager.getService("dreams"));
        if (asInterface != null) {
            try {
                asInterface.getDefaultDreamComponentForUser(applicationContext.getUserId());
            } catch (RemoteException e) {
                Log.w("DreamBackend", "Failed to get default dream", e);
            }
        }
        resources.getBoolean(17891613);
        resources.getBoolean(17891612);
        resources.getBoolean(17891611);
        Set set = (Set) Arrays.stream(resources.getStringArray(17236029)).map(BubbleData$$ExternalSyntheticLambda4.INSTANCE$1).collect(Collectors.toSet());
        Set set2 = (Set) Arrays.stream(resources.getIntArray(17236126)).boxed().collect(Collectors.toSet());
        Stream<Integer> boxed = Arrays.stream(resources.getIntArray(17236053)).boxed();
        Objects.requireNonNull(set2);
        this.mDefaultEnabledComplications = (Set) boxed.filter(new BubbleData$$ExternalSyntheticLambda5(set2, 1)).collect(Collectors.toSet());
    }
}
