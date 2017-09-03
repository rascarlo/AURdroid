package com.rascarlo.aurdroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AurDroidDialogFragment extends DialogFragment {

    private static final String BUNDLE_PARAMETER_TITLE = "bundle_parameter_title";
    private static final String BUNDLE_PARAMETER_MESSAGE = "bundle_parameter_message";
    private String bundleTitle;
    private String bundleMessage;

    public static AurDroidDialogFragment newInstance(String bundleTitle, String bundleMessage) {
        AurDroidDialogFragment aurDroidDialogFragment = new AurDroidDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PARAMETER_TITLE, bundleTitle);
        bundle.putString(BUNDLE_PARAMETER_MESSAGE, bundleMessage);
        aurDroidDialogFragment.setArguments(bundle);
        return aurDroidDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bundleTitle = getArguments().getString(BUNDLE_PARAMETER_TITLE);
            bundleMessage = getArguments().getString(BUNDLE_PARAMETER_MESSAGE);
        }
    }

    @Override
    public void setStyle(int style, @StyleRes int theme) {
        super.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_AlertDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_aur_droid_dialog_fragment, container, false);
        TextView titleTextView = rootView.findViewById(R.id.fragment_aur_droid_dialog_fragment_title_text_view);
        titleTextView.setText(bundleTitle);
        TextView messageTextView = rootView.findViewById(R.id.fragment_aur_droid_dialog_fragment_message_text_view);
        messageTextView.setText(bundleMessage);
        return rootView;
    }
}
