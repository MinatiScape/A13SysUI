package com.android.systemui.controls.management;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.service.controls.Control;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.controller.ControlInfo;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.controller.StructureInfo;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.ui.RenderInfo;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.util.LifecycleActivity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsRequestDialog.kt */
/* loaded from: classes.dex */
public class ControlsRequestDialog extends LifecycleActivity implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener {
    public final ControlsRequestDialog$callback$1 callback = new ControlsListingController.ControlsListingCallback() { // from class: com.android.systemui.controls.management.ControlsRequestDialog$callback$1
        @Override // com.android.systemui.controls.management.ControlsListingController.ControlsListingCallback
        public final void onServicesUpdated(ArrayList arrayList) {
        }
    };
    public Control control;
    public ComponentName controlComponent;
    public final ControlsController controller;
    public final ControlsListingController controlsListingController;
    public final ControlsRequestDialog$currentUserTracker$1 currentUserTracker;
    public AlertDialog dialog;

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        if (i == -1) {
            ControlsController controlsController = this.controller;
            ComponentName componentName = this.controlComponent;
            Control control = null;
            if (componentName == null) {
                componentName = null;
            }
            Control control2 = this.control;
            if (control2 == null) {
                control2 = null;
            }
            CharSequence structure = control2.getStructure();
            if (structure == null) {
                structure = "";
            }
            Control control3 = this.control;
            if (control3 == null) {
                control3 = null;
            }
            String controlId = control3.getControlId();
            Control control4 = this.control;
            if (control4 == null) {
                control4 = null;
            }
            CharSequence title = control4.getTitle();
            Control control5 = this.control;
            if (control5 == null) {
                control5 = null;
            }
            CharSequence subtitle = control5.getSubtitle();
            Control control6 = this.control;
            if (control6 != null) {
                control = control6;
            }
            controlsController.addFavorite(componentName, structure, new ControlInfo(controlId, title, subtitle, control.getDeviceType()));
        }
        finish();
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public final void onDestroy() {
        AlertDialog alertDialog = this.dialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        stopTracking();
        this.controlsListingController.removeCallback(this.callback);
        super.onDestroy();
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.controls.management.ControlsRequestDialog$callback$1] */
    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.controls.management.ControlsRequestDialog$currentUserTracker$1] */
    public ControlsRequestDialog(ControlsController controlsController, final BroadcastDispatcher broadcastDispatcher, ControlsListingController controlsListingController) {
        this.controller = controlsController;
        this.controlsListingController = controlsListingController;
        this.currentUserTracker = new CurrentUserTracker(broadcastDispatcher) { // from class: com.android.systemui.controls.management.ControlsRequestDialog$currentUserTracker$1
            public final int startingUser;

            {
                this.startingUser = ControlsRequestDialog.this.controller.getCurrentUserId();
            }

            @Override // com.android.systemui.settings.CurrentUserTracker
            public final void onUserSwitched(int i) {
                if (i != this.startingUser) {
                    stopTracking();
                    ControlsRequestDialog.this.finish();
                }
            }
        };
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        startTracking();
        this.controlsListingController.addCallback(this.callback);
        int intExtra = getIntent().getIntExtra("android.intent.extra.USER_ID", -10000);
        int currentUserId = this.controller.getCurrentUserId();
        if (intExtra != currentUserId) {
            Log.w("ControlsRequestDialog", "Current user (" + currentUserId + ") different from request user (" + intExtra + ')');
            finish();
        }
        ComponentName componentName = (ComponentName) getIntent().getParcelableExtra("android.intent.extra.COMPONENT_NAME");
        if (componentName == null) {
            Log.e("ControlsRequestDialog", "Request did not contain componentName");
            finish();
            return;
        }
        this.controlComponent = componentName;
        Control control = (Control) getIntent().getParcelableExtra("android.service.controls.extra.CONTROL");
        if (control == null) {
            Log.e("ControlsRequestDialog", "Request did not contain control");
            finish();
            return;
        }
        this.control = control;
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public final void onResume() {
        Object[] objArr;
        Object[] objArr2;
        super.onResume();
        ControlsListingController controlsListingController = this.controlsListingController;
        ComponentName componentName = this.controlComponent;
        Control control = null;
        ComponentName componentName2 = null;
        if (componentName == null) {
            componentName = null;
        }
        CharSequence appLabel = controlsListingController.getAppLabel(componentName);
        if (appLabel == null) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("The component specified (");
            ComponentName componentName3 = this.controlComponent;
            if (componentName3 != null) {
                componentName2 = componentName3;
            }
            m.append((Object) componentName2.flattenToString());
            m.append(" is not a valid ControlsProviderService");
            Log.e("ControlsRequestDialog", m.toString());
            finish();
            return;
        }
        ControlsController controlsController = this.controller;
        ComponentName componentName4 = this.controlComponent;
        if (componentName4 == null) {
            componentName4 = null;
        }
        List<StructureInfo> favoritesForComponent = controlsController.getFavoritesForComponent(componentName4);
        if (!(favoritesForComponent instanceof Collection) || !favoritesForComponent.isEmpty()) {
            for (StructureInfo structureInfo : favoritesForComponent) {
                Objects.requireNonNull(structureInfo);
                List<ControlInfo> list = structureInfo.controls;
                if (!(list instanceof Collection) || !list.isEmpty()) {
                    for (ControlInfo controlInfo : list) {
                        Objects.requireNonNull(controlInfo);
                        String str = controlInfo.controlId;
                        Control control2 = this.control;
                        if (control2 == null) {
                            control2 = null;
                        }
                        if (Intrinsics.areEqual(str, control2.getControlId())) {
                            objArr2 = 1;
                            continue;
                            break;
                        }
                    }
                }
                objArr2 = null;
                continue;
                if (objArr2 != null) {
                    objArr = 1;
                    break;
                }
            }
        }
        objArr = null;
        if (objArr != null) {
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("The control ");
            Control control3 = this.control;
            if (control3 == null) {
                control3 = null;
            }
            m2.append((Object) control3.getTitle());
            m2.append(" is already a favorite");
            Log.w("ControlsRequestDialog", m2.toString());
            finish();
        }
        SparseArray<Drawable> sparseArray = RenderInfo.iconMap;
        ComponentName componentName5 = this.controlComponent;
        if (componentName5 == null) {
            componentName5 = null;
        }
        Control control4 = this.control;
        if (control4 == null) {
            control4 = null;
        }
        RenderInfo lookup = RenderInfo.Companion.lookup(this, componentName5, control4.getDeviceType(), 0);
        View inflate = LayoutInflater.from(this).inflate(2131624046, (ViewGroup) null);
        ImageView imageView = (ImageView) inflate.requireViewById(2131428102);
        imageView.setImageDrawable(lookup.icon);
        imageView.setImageTintList(imageView.getContext().getResources().getColorStateList(lookup.foreground, imageView.getContext().getTheme()));
        TextView textView = (TextView) inflate.requireViewById(2131429057);
        Control control5 = this.control;
        if (control5 == null) {
            control5 = null;
        }
        textView.setText(control5.getTitle());
        TextView textView2 = (TextView) inflate.requireViewById(2131428947);
        Control control6 = this.control;
        if (control6 != null) {
            control = control6;
        }
        textView2.setText(control.getSubtitle());
        inflate.requireViewById(2131427749).setElevation(inflate.getResources().getFloat(2131165530));
        AlertDialog create = new AlertDialog.Builder(this).setTitle(getString(2131952162)).setMessage(getString(2131952160, appLabel)).setPositiveButton(2131952161, this).setNegativeButton(17039360, this).setOnCancelListener(this).setView(inflate).create();
        SystemUIDialog.registerDismissListener(create);
        create.setCanceledOnTouchOutside(true);
        this.dialog = create;
        create.show();
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public final void onCancel(DialogInterface dialogInterface) {
        finish();
    }
}
