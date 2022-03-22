package androidx.preference;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.TextView;
import androidx.preference.DialogPreference;
import java.util.Objects;
@Deprecated
/* loaded from: classes.dex */
public abstract class PreferenceDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
    public BitmapDrawable mDialogIcon;
    public int mDialogLayoutRes;
    public CharSequence mDialogMessage;
    public CharSequence mDialogTitle;
    public CharSequence mNegativeButtonText;
    public CharSequence mPositiveButtonText;
    public DialogPreference mPreference;
    public int mWhichButtonClicked;

    @Deprecated
    public abstract void onDialogClosed(boolean z);

    @Deprecated
    public void onPrepareDialogBuilder(AlertDialog.Builder builder) {
    }

    @Deprecated
    public final DialogPreference getPreference() {
        if (this.mPreference == null) {
            this.mPreference = (DialogPreference) ((DialogPreference.TargetFragment) getTargetFragment()).findPreference(getArguments().getString("key"));
        }
        return this.mPreference;
    }

    @Deprecated
    public void onBindDialogView(View view) {
        View findViewById = view.findViewById(16908299);
        if (findViewById != null) {
            CharSequence charSequence = this.mDialogMessage;
            int i = 8;
            if (!TextUtils.isEmpty(charSequence)) {
                if (findViewById instanceof TextView) {
                    ((TextView) findViewById).setText(charSequence);
                }
                i = 0;
            }
            if (findViewById.getVisibility() != i) {
                findViewById.setVisibility(i);
            }
        }
    }

    @Override // android.app.DialogFragment, android.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Fragment targetFragment = getTargetFragment();
        if (targetFragment instanceof DialogPreference.TargetFragment) {
            DialogPreference.TargetFragment targetFragment2 = (DialogPreference.TargetFragment) targetFragment;
            String string = getArguments().getString("key");
            if (bundle == null) {
                DialogPreference dialogPreference = (DialogPreference) targetFragment2.findPreference(string);
                this.mPreference = dialogPreference;
                Objects.requireNonNull(dialogPreference);
                this.mDialogTitle = dialogPreference.mDialogTitle;
                DialogPreference dialogPreference2 = this.mPreference;
                Objects.requireNonNull(dialogPreference2);
                this.mPositiveButtonText = dialogPreference2.mPositiveButtonText;
                DialogPreference dialogPreference3 = this.mPreference;
                Objects.requireNonNull(dialogPreference3);
                this.mNegativeButtonText = dialogPreference3.mNegativeButtonText;
                DialogPreference dialogPreference4 = this.mPreference;
                Objects.requireNonNull(dialogPreference4);
                this.mDialogMessage = dialogPreference4.mDialogMessage;
                DialogPreference dialogPreference5 = this.mPreference;
                Objects.requireNonNull(dialogPreference5);
                this.mDialogLayoutRes = dialogPreference5.mDialogLayoutResId;
                DialogPreference dialogPreference6 = this.mPreference;
                Objects.requireNonNull(dialogPreference6);
                Drawable drawable = dialogPreference6.mDialogIcon;
                if (drawable == null || (drawable instanceof BitmapDrawable)) {
                    this.mDialogIcon = (BitmapDrawable) drawable;
                    return;
                }
                Bitmap createBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
                this.mDialogIcon = new BitmapDrawable(getResources(), createBitmap);
                return;
            }
            this.mDialogTitle = bundle.getCharSequence("PreferenceDialogFragment.title");
            this.mPositiveButtonText = bundle.getCharSequence("PreferenceDialogFragment.positiveText");
            this.mNegativeButtonText = bundle.getCharSequence("PreferenceDialogFragment.negativeText");
            this.mDialogMessage = bundle.getCharSequence("PreferenceDialogFragment.message");
            this.mDialogLayoutRes = bundle.getInt("PreferenceDialogFragment.layout", 0);
            Bitmap bitmap = (Bitmap) bundle.getParcelable("PreferenceDialogFragment.icon");
            if (bitmap != null) {
                this.mDialogIcon = new BitmapDrawable(getResources(), bitmap);
                return;
            }
            return;
        }
        throw new IllegalStateException("Target fragment must implement TargetFragment interface");
    }

    @Override // android.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        Activity activity = getActivity();
        this.mWhichButtonClicked = -2;
        AlertDialog.Builder negativeButton = new AlertDialog.Builder(activity).setTitle(this.mDialogTitle).setIcon(this.mDialogIcon).setPositiveButton(this.mPositiveButtonText, this).setNegativeButton(this.mNegativeButtonText, this);
        int i = this.mDialogLayoutRes;
        View view = null;
        if (i != 0) {
            view = LayoutInflater.from(activity).inflate(i, (ViewGroup) null);
        }
        if (view != null) {
            onBindDialogView(view);
            negativeButton.setView(view);
        } else {
            negativeButton.setMessage(this.mDialogMessage);
        }
        onPrepareDialogBuilder(negativeButton);
        AlertDialog create = negativeButton.create();
        if (this instanceof EditTextPreferenceDialogFragment) {
            create.getWindow().getDecorView().getWindowInsetsController().show(WindowInsets.Type.ime());
        }
        return create;
    }

    @Override // android.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        boolean z;
        super.onDismiss(dialogInterface);
        if (this.mWhichButtonClicked == -1) {
            z = true;
        } else {
            z = false;
        }
        onDialogClosed(z);
    }

    @Override // android.app.DialogFragment, android.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putCharSequence("PreferenceDialogFragment.title", this.mDialogTitle);
        bundle.putCharSequence("PreferenceDialogFragment.positiveText", this.mPositiveButtonText);
        bundle.putCharSequence("PreferenceDialogFragment.negativeText", this.mNegativeButtonText);
        bundle.putCharSequence("PreferenceDialogFragment.message", this.mDialogMessage);
        bundle.putInt("PreferenceDialogFragment.layout", this.mDialogLayoutRes);
        BitmapDrawable bitmapDrawable = this.mDialogIcon;
        if (bitmapDrawable != null) {
            bundle.putParcelable("PreferenceDialogFragment.icon", bitmapDrawable.getBitmap());
        }
    }

    @Override // android.content.DialogInterface.OnClickListener
    @Deprecated
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.mWhichButtonClicked = i;
    }
}
