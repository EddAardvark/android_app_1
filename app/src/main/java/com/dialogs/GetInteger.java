package com.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.tutorialapp.R;

public class GetInteger extends DialogFragment {
    public static GetInteger construct (int n) {
        GetInteger frag = new GetInteger();
        Bundle args = new Bundle();
        args.putInt("cerrent", n);
        frag.setArguments(args);
        return frag;
    }
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.get_integer_prompt));
        builder.setPositiveButton("Accept", null);
        builder.setNegativeButton("Cancel", null);
        return builder.create();
    }
}
