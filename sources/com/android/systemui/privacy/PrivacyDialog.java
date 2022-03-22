package com.android.systemui.privacy;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.keyguard.FontInterpolator$VarFontKey$$ExternalSyntheticOutline0;
import com.android.settingslib.Utils;
import com.android.systemui.privacy.PrivacyDialog;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: PrivacyDialog.kt */
/* loaded from: classes.dex */
public final class PrivacyDialog extends SystemUIDialog {
    public final PrivacyDialog$clickListener$1 clickListener;
    public final String enterpriseText;
    public final List<PrivacyElement> list;
    public final String phonecall;
    public ViewGroup rootView;
    public final ArrayList dismissListeners = new ArrayList();
    public final AtomicBoolean dismissed = new AtomicBoolean(false);
    public final int iconColorSolid = Utils.getColorAttrDefaultColor(getContext(), 16843827);

    /* compiled from: PrivacyDialog.kt */
    /* loaded from: classes.dex */
    public interface OnDialogDismissed {
        void onDialogDismissed();
    }

    /* compiled from: PrivacyDialog.kt */
    /* loaded from: classes.dex */
    public static final class PrivacyElement {
        public final boolean active;
        public final CharSequence applicationName;
        public final CharSequence attributionLabel;
        public final CharSequence attributionTag;
        public final StringBuilder builder;
        public final boolean enterprise;
        public final long lastActiveTimestamp;
        public final Intent navigationIntent;
        public final String packageName;
        public final CharSequence permGroupName;
        public final boolean phoneCall;
        public final CharSequence proxyLabel;
        public final PrivacyType type;
        public final int userId;

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof PrivacyElement)) {
                return false;
            }
            PrivacyElement privacyElement = (PrivacyElement) obj;
            return this.type == privacyElement.type && Intrinsics.areEqual(this.packageName, privacyElement.packageName) && this.userId == privacyElement.userId && Intrinsics.areEqual(this.applicationName, privacyElement.applicationName) && Intrinsics.areEqual(this.attributionTag, privacyElement.attributionTag) && Intrinsics.areEqual(this.attributionLabel, privacyElement.attributionLabel) && Intrinsics.areEqual(this.proxyLabel, privacyElement.proxyLabel) && this.lastActiveTimestamp == privacyElement.lastActiveTimestamp && this.active == privacyElement.active && this.enterprise == privacyElement.enterprise && this.phoneCall == privacyElement.phoneCall && Intrinsics.areEqual(this.permGroupName, privacyElement.permGroupName) && Intrinsics.areEqual(this.navigationIntent, privacyElement.navigationIntent);
        }

        public PrivacyElement(PrivacyType privacyType, String str, int i, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, CharSequence charSequence4, long j, boolean z, boolean z2, boolean z3, String str2, Intent intent) {
            this.type = privacyType;
            this.packageName = str;
            this.userId = i;
            this.applicationName = charSequence;
            this.attributionTag = charSequence2;
            this.attributionLabel = charSequence3;
            this.proxyLabel = charSequence4;
            this.lastActiveTimestamp = j;
            this.active = z;
            this.enterprise = z2;
            this.phoneCall = z3;
            this.permGroupName = str2;
            this.navigationIntent = intent;
            StringBuilder sb = new StringBuilder("PrivacyElement(");
            this.builder = sb;
            sb.append(Intrinsics.stringPlus("type=", privacyType.getLogName()));
            sb.append(Intrinsics.stringPlus(", packageName=", str));
            sb.append(Intrinsics.stringPlus(", userId=", Integer.valueOf(i)));
            sb.append(Intrinsics.stringPlus(", appName=", charSequence));
            if (charSequence2 != null) {
                sb.append(Intrinsics.stringPlus(", attributionTag=", charSequence2));
            }
            if (charSequence3 != null) {
                sb.append(Intrinsics.stringPlus(", attributionLabel=", charSequence3));
            }
            if (charSequence4 != null) {
                sb.append(Intrinsics.stringPlus(", proxyLabel=", charSequence4));
            }
            sb.append(Intrinsics.stringPlus(", lastActive=", Long.valueOf(j)));
            if (z) {
                sb.append(", active");
            }
            if (z2) {
                sb.append(", enterprise");
            }
            if (z3) {
                sb.append(", phoneCall");
            }
            sb.append(", permGroupName=" + ((Object) str2) + ')');
            sb.append(Intrinsics.stringPlus(", navigationIntent=", intent));
        }

        public final int hashCode() {
            int i;
            int i2;
            int i3;
            int hashCode = (this.applicationName.hashCode() + FontInterpolator$VarFontKey$$ExternalSyntheticOutline0.m(this.userId, (this.packageName.hashCode() + (this.type.hashCode() * 31)) * 31, 31)) * 31;
            CharSequence charSequence = this.attributionTag;
            int i4 = 0;
            if (charSequence == null) {
                i = 0;
            } else {
                i = charSequence.hashCode();
            }
            int i5 = (hashCode + i) * 31;
            CharSequence charSequence2 = this.attributionLabel;
            if (charSequence2 == null) {
                i2 = 0;
            } else {
                i2 = charSequence2.hashCode();
            }
            int i6 = (i5 + i2) * 31;
            CharSequence charSequence3 = this.proxyLabel;
            if (charSequence3 == null) {
                i3 = 0;
            } else {
                i3 = charSequence3.hashCode();
            }
            int hashCode2 = (Long.hashCode(this.lastActiveTimestamp) + ((i6 + i3) * 31)) * 31;
            boolean z = this.active;
            int i7 = 1;
            if (z) {
                z = true;
            }
            int i8 = z ? 1 : 0;
            int i9 = z ? 1 : 0;
            int i10 = (hashCode2 + i8) * 31;
            boolean z2 = this.enterprise;
            if (z2) {
                z2 = true;
            }
            int i11 = z2 ? 1 : 0;
            int i12 = z2 ? 1 : 0;
            int i13 = (i10 + i11) * 31;
            boolean z3 = this.phoneCall;
            if (!z3) {
                i7 = z3 ? 1 : 0;
            }
            int hashCode3 = (this.permGroupName.hashCode() + ((i13 + i7) * 31)) * 31;
            Intent intent = this.navigationIntent;
            if (intent != null) {
                i4 = intent.hashCode();
            }
            return hashCode3 + i4;
        }

        public final String toString() {
            return this.builder.toString();
        }
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [com.android.systemui.privacy.PrivacyDialog$clickListener$1] */
    public PrivacyDialog(Context context, ArrayList arrayList, final Function4 function4) {
        super(context, 2132017599);
        this.list = arrayList;
        this.enterpriseText = Intrinsics.stringPlus(" ", context.getString(2131952944));
        this.phonecall = context.getString(2131952946);
        this.clickListener = new View.OnClickListener() { // from class: com.android.systemui.privacy.PrivacyDialog$clickListener$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                Object tag = view.getTag();
                if (tag != null) {
                    PrivacyDialog.PrivacyElement privacyElement = (PrivacyDialog.PrivacyElement) tag;
                    function4.invoke(privacyElement.packageName, Integer.valueOf(privacyElement.userId), privacyElement.attributionTag, privacyElement.navigationIntent);
                }
            }
        };
    }

    @Override // com.android.systemui.statusbar.phone.SystemUIDialog, android.app.AlertDialog, android.app.Dialog
    public final void onCreate(Bundle bundle) {
        int i;
        int i2;
        CharSequence charSequence;
        super.onCreate(bundle);
        Window window = getWindow();
        if (window != null) {
            window.getAttributes().setFitInsetsTypes(window.getAttributes().getFitInsetsTypes() | WindowInsets.Type.statusBars());
            window.getAttributes().receiveInsetsIgnoringZOrder = true;
            window.setGravity(49);
        }
        setContentView(2131624412);
        this.rootView = (ViewGroup) requireViewById(2131428714);
        for (PrivacyElement privacyElement : this.list) {
            ViewGroup viewGroup = this.rootView;
            String str = null;
            if (viewGroup == null) {
                viewGroup = null;
            }
            LayoutInflater from = LayoutInflater.from(getContext());
            ViewGroup viewGroup2 = this.rootView;
            if (viewGroup2 == null) {
                viewGroup2 = null;
            }
            View inflate = from.inflate(2131624413, viewGroup2, false);
            Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.view.ViewGroup");
            ViewGroup viewGroup3 = (ViewGroup) inflate;
            Objects.requireNonNull(privacyElement);
            PrivacyType privacyType = privacyElement.type;
            Context context = getContext();
            int ordinal = privacyType.ordinal();
            if (ordinal == 0) {
                i = 2131232574;
            } else if (ordinal == 1) {
                i = 2131232576;
            } else if (ordinal == 2) {
                i = 2131232575;
            } else {
                throw new NoWhenBranchMatchedException();
            }
            Drawable drawable = context.getDrawable(i);
            Objects.requireNonNull(drawable, "null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
            LayerDrawable layerDrawable = (LayerDrawable) drawable;
            layerDrawable.findDrawableByLayerId(2131428102).setTint(this.iconColorSolid);
            ImageView imageView = (ImageView) viewGroup3.requireViewById(2131428102);
            imageView.setImageDrawable(layerDrawable);
            imageView.setContentDescription(privacyElement.type.getName(imageView.getContext()));
            if (privacyElement.active) {
                i2 = 2131952949;
            } else {
                i2 = 2131952947;
            }
            if (privacyElement.phoneCall) {
                charSequence = this.phonecall;
            } else {
                charSequence = privacyElement.applicationName;
            }
            if (privacyElement.enterprise) {
                charSequence = TextUtils.concat(charSequence, this.enterpriseText);
            }
            CharSequence string = getContext().getString(i2, charSequence);
            CharSequence charSequence2 = privacyElement.attributionLabel;
            CharSequence charSequence3 = privacyElement.proxyLabel;
            if (charSequence2 != null && charSequence3 != null) {
                str = getContext().getString(2131952942, charSequence2, charSequence3);
            } else if (charSequence2 != null) {
                str = getContext().getString(2131952941, charSequence2);
            } else if (charSequence3 != null) {
                str = getContext().getString(2131952943, charSequence3);
            }
            if (str != null) {
                string = TextUtils.concat(string, " ", str);
            }
            ((TextView) viewGroup3.requireViewById(2131429024)).setText(string);
            if (privacyElement.phoneCall) {
                viewGroup3.requireViewById(2131427690).setVisibility(8);
            }
            viewGroup3.setTag(privacyElement);
            if (!privacyElement.phoneCall) {
                viewGroup3.setOnClickListener(this.clickListener);
            }
            viewGroup.addView(viewGroup3);
        }
    }

    @Override // com.android.systemui.statusbar.phone.SystemUIDialog, android.app.Dialog
    public final void onStop() {
        super.onStop();
        this.dismissed.set(true);
        Iterator it = this.dismissListeners.iterator();
        while (it.hasNext()) {
            it.remove();
            OnDialogDismissed onDialogDismissed = (OnDialogDismissed) ((WeakReference) it.next()).get();
            if (onDialogDismissed != null) {
                onDialogDismissed.onDialogDismissed();
            }
        }
    }
}
