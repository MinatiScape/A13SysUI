package com.android.systemui.controls.controller;

import android.content.Context;
import android.os.Environment;
import android.os.UserHandle;
import java.io.File;
/* compiled from: ControlsControllerImpl.kt */
/* loaded from: classes.dex */
public final class UserStructure {
    public final File auxiliaryFile;
    public final File file;
    public final Context userContext;

    public UserStructure(Context context, UserHandle userHandle) {
        Context createContextAsUser = context.createContextAsUser(userHandle, 0);
        this.userContext = createContextAsUser;
        this.file = Environment.buildPath(createContextAsUser.getFilesDir(), new String[]{"controls_favorites.xml"});
        this.auxiliaryFile = Environment.buildPath(createContextAsUser.getFilesDir(), new String[]{"aux_controls_favorites.xml"});
    }
}
