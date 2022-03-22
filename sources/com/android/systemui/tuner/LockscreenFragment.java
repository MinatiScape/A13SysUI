package com.android.systemui.tuner;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.SwitchPreference;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.Dependency;
import com.android.systemui.plugins.IntentButtonProvider;
import com.android.systemui.statusbar.ScalingDrawableWrapper;
import com.android.systemui.statusbar.phone.ExpandableIndicator;
import com.android.systemui.statusbar.policy.ExtensionController;
import com.android.systemui.tuner.ShortcutParser;
import com.android.systemui.tuner.TunerService;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public class LockscreenFragment extends PreferenceFragment {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final ArrayList<TunerService.Tunable> mTunables = new ArrayList<>();
    public TunerService mTunerService;

    /* loaded from: classes.dex */
    public static class Adapter extends RecyclerView.Adapter<Holder> {
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public final void onBindViewHolder(Holder holder, int i) {
            throw null;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public final RecyclerView.ViewHolder onCreateViewHolder(RecyclerView recyclerView, int i) {
            return new Holder(LayoutInflater.from(recyclerView.getContext()).inflate(2131624626, (ViewGroup) recyclerView, false));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public final int getItemCount() {
            throw null;
        }
    }

    /* loaded from: classes.dex */
    public static class App extends Item {
    }

    /* loaded from: classes.dex */
    public static class LockButtonFactory implements ExtensionController.TunerFactory<IntentButtonProvider.IntentButton> {
        public final Context mContext;
        public final String mKey;

        public final IntentButtonProvider.IntentButton create(ArrayMap arrayMap) {
            ActivityInfo activityInfo;
            String str = (String) arrayMap.get(this.mKey);
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            if (str.contains("::")) {
                ShortcutParser.Shortcut shortcutInfo = LockscreenFragment.getShortcutInfo(this.mContext, str);
                if (shortcutInfo != null) {
                    return new ShortcutButton(this.mContext, shortcutInfo);
                }
                return null;
            } else if (!str.contains("/")) {
                return null;
            } else {
                Context context = this.mContext;
                int i = LockscreenFragment.$r8$clinit;
                try {
                    activityInfo = context.getPackageManager().getActivityInfo(ComponentName.unflattenFromString(str), 0);
                } catch (PackageManager.NameNotFoundException unused) {
                    activityInfo = null;
                }
                if (activityInfo != null) {
                    return new ActivityButton(this.mContext, activityInfo);
                }
                return null;
            }
        }

        public LockButtonFactory(Context context, String str) {
            this.mContext = context;
            this.mKey = str;
        }
    }

    /* loaded from: classes.dex */
    public static class ShortcutButton implements IntentButtonProvider.IntentButton {
        public final IntentButtonProvider.IntentButton.IconState mIconState;
        public final ShortcutParser.Shortcut mShortcut;

        @Override // com.android.systemui.plugins.IntentButtonProvider.IntentButton
        public final Intent getIntent() {
            return this.mShortcut.intent;
        }

        public ShortcutButton(Context context, ShortcutParser.Shortcut shortcut) {
            this.mShortcut = shortcut;
            IntentButtonProvider.IntentButton.IconState iconState = new IntentButtonProvider.IntentButton.IconState();
            this.mIconState = iconState;
            iconState.isVisible = true;
            iconState.drawable = shortcut.icon.loadDrawable(context).mutate();
            iconState.contentDescription = shortcut.label;
            Drawable drawable = iconState.drawable;
            iconState.drawable = new ScalingDrawableWrapper(drawable, ((int) TypedValue.applyDimension(1, 32.0f, context.getResources().getDisplayMetrics())) / drawable.getIntrinsicWidth());
            iconState.tint = false;
        }

        @Override // com.android.systemui.plugins.IntentButtonProvider.IntentButton
        public final IntentButtonProvider.IntentButton.IconState getIcon() {
            return this.mIconState;
        }
    }

    /* loaded from: classes.dex */
    public static class StaticShortcut extends Item {
    }

    /* loaded from: classes.dex */
    public static class ActivityButton implements IntentButtonProvider.IntentButton {
        public final IntentButtonProvider.IntentButton.IconState mIconState;
        public final Intent mIntent;

        public ActivityButton(Context context, ActivityInfo activityInfo) {
            this.mIntent = new Intent().setComponent(new ComponentName(activityInfo.packageName, activityInfo.name));
            IntentButtonProvider.IntentButton.IconState iconState = new IntentButtonProvider.IntentButton.IconState();
            this.mIconState = iconState;
            iconState.isVisible = true;
            iconState.drawable = activityInfo.loadIcon(context.getPackageManager()).mutate();
            iconState.contentDescription = activityInfo.loadLabel(context.getPackageManager());
            Drawable drawable = iconState.drawable;
            iconState.drawable = new ScalingDrawableWrapper(drawable, ((int) TypedValue.applyDimension(1, 32.0f, context.getResources().getDisplayMetrics())) / drawable.getIntrinsicWidth());
            iconState.tint = false;
        }

        @Override // com.android.systemui.plugins.IntentButtonProvider.IntentButton
        public final IntentButtonProvider.IntentButton.IconState getIcon() {
            return this.mIconState;
        }

        @Override // com.android.systemui.plugins.IntentButtonProvider.IntentButton
        public final Intent getIntent() {
            return this.mIntent;
        }
    }

    /* loaded from: classes.dex */
    public static class Holder extends RecyclerView.ViewHolder {
        public Holder(View view) {
            super(view);
            ImageView imageView = (ImageView) view.findViewById(16908294);
            TextView textView = (TextView) view.findViewById(16908310);
            ExpandableIndicator expandableIndicator = (ExpandableIndicator) view.findViewById(2131427942);
        }
    }

    public static ShortcutParser.Shortcut getShortcutInfo(Context context, String str) {
        String[] split = str.split("::");
        try {
            Iterator it = new ShortcutParser(context, new ComponentName(split[0], split[1])).getShortcuts().iterator();
            while (it.hasNext()) {
                ShortcutParser.Shortcut shortcut = (ShortcutParser.Shortcut) it.next();
                if (shortcut.id.equals(split[2])) {
                    return shortcut;
                }
            }
        } catch (PackageManager.NameNotFoundException unused) {
        }
        return null;
    }

    @Override // androidx.preference.PreferenceFragment
    public final void onCreatePreferences(String str) {
        this.mTunerService = (TunerService) Dependency.get(TunerService.class);
        new Handler();
        addPreferencesFromResource(2132213762);
        setupGroup("sysui_keyguard_left", "sysui_keyguard_left_unlock");
        setupGroup("sysui_keyguard_right", "sysui_keyguard_right_unlock");
    }

    @Override // android.app.Fragment
    public final void onDestroy() {
        super.onDestroy();
        this.mTunables.forEach(new LockscreenFragment$$ExternalSyntheticLambda1(this, 0));
    }

    public final void setupGroup(String str, String str2) {
        final Preference findPreference = findPreference(str);
        final SwitchPreference switchPreference = (SwitchPreference) findPreference(str2);
        TunerService.Tunable lockscreenFragment$$ExternalSyntheticLambda0 = new TunerService.Tunable() { // from class: com.android.systemui.tuner.LockscreenFragment$$ExternalSyntheticLambda0
            @Override // com.android.systemui.tuner.TunerService.Tunable
            public final void onTuningChanged(String str3, String str4) {
                ActivityInfo activityInfo;
                LockscreenFragment lockscreenFragment = LockscreenFragment.this;
                SwitchPreference switchPreference2 = switchPreference;
                Preference preference = findPreference;
                int i = LockscreenFragment.$r8$clinit;
                Objects.requireNonNull(lockscreenFragment);
                switchPreference2.setVisible(!TextUtils.isEmpty(str4));
                if (str4 == null) {
                    preference.setSummary$1();
                    return;
                }
                CharSequence charSequence = null;
                if (str4.contains("::")) {
                    ShortcutParser.Shortcut shortcutInfo = LockscreenFragment.getShortcutInfo(lockscreenFragment.getContext(), str4);
                    if (shortcutInfo != null) {
                        charSequence = shortcutInfo.label;
                    }
                    preference.setSummary(charSequence);
                } else if (str4.contains("/")) {
                    try {
                        activityInfo = lockscreenFragment.getContext().getPackageManager().getActivityInfo(ComponentName.unflattenFromString(str4), 0);
                    } catch (PackageManager.NameNotFoundException unused) {
                        activityInfo = null;
                    }
                    if (activityInfo != null) {
                        charSequence = activityInfo.loadLabel(lockscreenFragment.getContext().getPackageManager());
                    }
                    preference.setSummary(charSequence);
                } else {
                    preference.setSummary$1();
                }
            }
        };
        this.mTunables.add(lockscreenFragment$$ExternalSyntheticLambda0);
        this.mTunerService.addTunable(lockscreenFragment$$ExternalSyntheticLambda0, str);
    }

    /* loaded from: classes.dex */
    public static abstract class Item {
        private Item() {
        }
    }
}
