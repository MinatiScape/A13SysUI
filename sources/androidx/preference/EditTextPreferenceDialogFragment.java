package androidx.preference;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.util.Objects;
@Deprecated
/* loaded from: classes.dex */
public final class EditTextPreferenceDialogFragment extends PreferenceDialogFragment {
    public EditText mEditText;
    public CharSequence mText;

    @Override // androidx.preference.PreferenceDialogFragment
    @Deprecated
    public final void onDialogClosed(boolean z) {
        if (z) {
            String obj = this.mEditText.getText().toString();
            if (((EditTextPreference) getPreference()).callChangeListener(obj)) {
                ((EditTextPreference) getPreference()).setText(obj);
            }
        }
    }

    @Override // androidx.preference.PreferenceDialogFragment
    public final void onBindDialogView(View view) {
        super.onBindDialogView(view);
        EditText editText = (EditText) view.findViewById(16908291);
        this.mEditText = editText;
        editText.requestFocus();
        EditText editText2 = this.mEditText;
        if (editText2 != null) {
            editText2.setText(this.mText);
            EditText editText3 = this.mEditText;
            editText3.setSelection(editText3.getText().length());
            return;
        }
        throw new IllegalStateException("Dialog view must contain an EditText with id @android:id/edit");
    }

    @Override // androidx.preference.PreferenceDialogFragment, android.app.DialogFragment, android.app.Fragment
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            EditTextPreference editTextPreference = (EditTextPreference) getPreference();
            Objects.requireNonNull(editTextPreference);
            this.mText = editTextPreference.mText;
            return;
        }
        this.mText = bundle.getCharSequence("EditTextPreferenceDialogFragment.text");
    }

    @Override // androidx.preference.PreferenceDialogFragment, android.app.DialogFragment, android.app.Fragment
    public final void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putCharSequence("EditTextPreferenceDialogFragment.text", this.mText);
    }
}
