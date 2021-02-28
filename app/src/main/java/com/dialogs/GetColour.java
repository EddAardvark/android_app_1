package com.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.JWPatterns.R;
import com.misc.MessageBox;

public class GetColour extends DialogFragment {


    public interface Result {
        abstract void SetColour(int id, int value);
    }

    DialogInterface.OnClickListener m_listener = new GetColour.ClickListener();
    NumberPicker.OnValueChangeListener m_number_change = new NewNumbrListener();

    GetColour.Result m_result;
    int m_id;
    int m_granularity = 16;  ///< granularity in the colour component picker

    NumberPicker m_RedPicker;
    NumberPicker m_GreenPicker;
    NumberPicker m_BluePicker;
    View m_layout_colour;
    int m_red;
    int m_green;
    int m_blue;
    int m_opaque;

    /**
     * Seems to be required for rotation handling, without this the app will crash with MethodNotFoundExtension
     * However, the dialog is now useless as the callback interface in m_result no longer exists. I can keep the instance
     * alive by storing the reference statically, but it no longer points to the active instance of the called,
     * which has been recreated during the rotation, so we just need to make the dialog disappear.
     */
    public GetColour (){

    }

    GetColour(GetColour.Result res, int id) {
        m_id = id;
        m_result = res;
    }

    public static GetColour construct(GetColour.Result res, int id, int colour, String title, String message) {

        GetColour frag = new GetColour(res, id);
        Bundle args = new Bundle();
        args.putInt("current", colour);
        args.putString("message", message);
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflator = requireActivity().getLayoutInflater();
        builder.setView(inflator.inflate(R.layout.colour_chooser, null));
        builder.setTitle(args.getString("title"));
        builder.setMessage(args.getString("message"));
        builder.setPositiveButton(getString(R.string.accept), m_listener);
        builder.setNegativeButton(getString(R.string.cancel), m_listener);

        int colour = args.getInt("current");
        m_opaque = (colour >> 24) & 0xFF;
        m_red = (colour >> 16) & 0xFF;
        m_green = (colour >> 8) & 0xFF;
        m_blue = colour & 0xFF;

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = this.getDialog();

        m_layout_colour = dialog.findViewById(R.id.colour_display);

        m_RedPicker = (NumberPicker) dialog.findViewById(R.id.set_red);
        m_GreenPicker = (NumberPicker) dialog.findViewById(R.id.set_green);
        m_BluePicker = (NumberPicker) dialog.findViewById(R.id.set_blue);

        m_RedPicker.setMinValue(0);
        m_RedPicker.setMaxValue(256/ m_granularity);
        m_RedPicker.setValue(((m_red == 255) ? 256 : m_red)/ m_granularity);

        m_GreenPicker.setMinValue(0);
        m_GreenPicker.setMaxValue(256/ m_granularity);
        m_GreenPicker.setValue(((m_green == 255) ? 256 : m_green)/ m_granularity);

        m_BluePicker.setMinValue(0);
        m_BluePicker.setMaxValue(256/ m_granularity);
        m_BluePicker.setValue(((m_blue == 255) ? 256 : m_blue)/ m_granularity);

        m_RedPicker.setOnValueChangedListener(m_number_change);
        m_GreenPicker.setOnValueChangedListener(m_number_change);
        m_BluePicker.setOnValueChangedListener(m_number_change);

        setColour ();
    }

    // TODO remove listeners at end

    void setColour ()
    {
        m_layout_colour.setBackgroundColor(Color.argb(m_opaque, m_red, m_green, m_blue));
    }

    public class ClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int id) {

            switch (id) {
                case Dialog.BUTTON_NEGATIVE:
                    break;
                case Dialog.BUTTON_POSITIVE:
                    if (m_result == null) {
                        MessageBox.showOK(getActivity(), "No owner", "This dialog no longer has an owner, possibly because a screen rotate has disconnected it", "OK");
                        return;
                    }
                    m_result.SetColour(m_id, Color.argb(m_opaque, m_red, m_green, m_blue));
                    break;
                case Dialog.BUTTON_NEUTRAL:
                    break;
            }
        }
    }

    public class NewNumbrListener implements NumberPicker.OnValueChangeListener {

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

            int id = picker.getId();

            if (id == R.id.set_red) {
                m_red = Math.min(newVal * m_granularity, 255);
            } else if (id == R.id.set_green) {
                m_green = Math.min(newVal * m_granularity, 255);
            } else if (id == R.id.set_blue) {
                m_blue = Math.min(newVal * m_granularity, 255);
            }
            setColour();
        }
    }
}
