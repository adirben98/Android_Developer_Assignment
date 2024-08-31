package com.example.android_developer_assignment.ui;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class SimpleTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Default implementation ignored
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Default implementation ignored
    }

    @Override
    public void afterTextChanged(Editable s) {
        // This method will be overridden by the subclass or anonymous class
    }
}
