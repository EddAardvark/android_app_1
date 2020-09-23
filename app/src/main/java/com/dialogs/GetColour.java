package com.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.tutorialapp.R;

public class GetColour extends DialogFragment {


    public interface Result {
        abstract void SetColour(int id, int value);
    }

    DialogInterface.OnClickListener m_listener = new GetColour.ClickListener();
    NumberPicker.OnValueChangeListener m_number_change = new NewNumbrListener();

    GetColour.Result m_result;
    int m_id;
    int m_granularty = 16;  ///< granularity in the colour component picker

    NumberPicker m_RedPicker;
    NumberPicker m_GreenPicker;
    NumberPicker m_BluePicker;
    View m_layout_colour;
    int m_red;
    int m_green;
    int m_blue;
    int m_opaque;

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
        builder.setPositiveButton("Accept", m_listener);
        builder.setNegativeButton("Cancel", m_listener);

        int colour = args.getInt("current");
        m_opaque = (colour >> 24) & 0xFF;
        m_red = (colour >> 16) & 0xFF;
        m_green = (colour >> 8) & 0xFF;
        m_blue = colour & 0xFF;
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
        m_RedPicker.setMaxValue(256/m_granularty);
        m_RedPicker.setValue(((m_red == 255) ? 256 : m_red)/m_granularty);

        m_GreenPicker.setMinValue(0);
        m_GreenPicker.setMaxValue(256/m_granularty);
        m_GreenPicker.setValue(((m_green == 255) ? 256 : m_green)/m_granularty);

        m_BluePicker.setMinValue(0);
        m_BluePicker.setMaxValue(256/m_granularty);
        m_BluePicker.setValue(((m_blue == 255) ? 256 : m_blue)/m_granularty);

        m_RedPicker.setOnValueChangedListener(m_number_change);
        m_GreenPicker.setOnValueChangedListener(m_number_change);
        m_BluePicker.setOnValueChangedListener(m_number_change);

        setColour ();
    }
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

            switch (picker.getId()) {
                case R.id.set_red:
                    m_red = Math.min(newVal*m_granularty, 255);
                    break;
                case R.id.set_green:
                    m_green = Math.min(newVal*m_granularty, 255);;
                    break;
                case R.id.set_blue:
                    m_blue = Math.min(newVal*m_granularty, 255);;
                    break;
            }
            setColour();
        }
    }


    public class ShowListener implements DialogInterface.OnShowListener {

        @Override
        public void onShow(DialogInterface dialog) {
        }
    }
}
