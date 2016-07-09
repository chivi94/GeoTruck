package com.ivan.geotruck.fragments.about_us_fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ivan.geotruck.R;
import com.ivan.geotruck.activities.AboutUsActivity;

/**
 * Created by Ivan on 18/05/2015.
 */
public class AboutGeoTruckDeveloperFragment extends Fragment implements View.OnClickListener {

    private ImageButton buttonTwitter;
    private ImageButton buttonEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ly_developer_info, container, false);

        setUpComponents(rootView);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        int buttonTag = (int) v.getTag();
        switch (buttonTag) {
            //Para el twitter
            case 1:
                Intent twitter = new Intent(Intent.ACTION_VIEW);
                twitter.setData(Uri.parse("https://twitter.com/ivangonrin"));
                ((AboutUsActivity) getActivity()).startActivity(twitter);
                break;
            //Para el email
            case 2:
                Intent email = new Intent(
                        android.content.Intent.ACTION_SEND);
                email.setType("plain/text");
                email.putExtra(android.content.Intent.EXTRA_EMAIL,
                        new String[]{"ivgorin@hotmail.com"});
                // creteChooser nos permitirá elegir con qué aplicación queremos
                // enviar el correo.
                getActivity().startActivity(Intent
                        .createChooser(email, getString(R.string.dialog_selectEmailActivity)));
                break;
        }
    }

    private void setUpComponents(View rootView) {
        buttonTwitter = (ImageButton) rootView.findViewById(R.id.button_twitter);
        buttonTwitter.setTag(1);
        buttonTwitter.setOnClickListener(this);

        buttonEmail = (ImageButton) rootView.findViewById(R.id.button_developerEmail);
        buttonEmail.setTag(2);
        buttonEmail.setOnClickListener(this);
    }
}
