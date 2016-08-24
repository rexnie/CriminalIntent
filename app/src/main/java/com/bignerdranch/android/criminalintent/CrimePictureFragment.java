package com.bignerdranch.android.criminalintent;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by niedaocai on 16-7-23.
 */
public class CrimePictureFragment extends DialogFragment {
    private static final String TAG = "CrimePictureFragment";
    private static final String ARG_PICTURE_FILE = "picture_file";

    public static CrimePictureFragment newInstance(File photoFile) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PICTURE_FILE, photoFile);
        CrimePictureFragment fragment = new CrimePictureFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_picture_preview, null);
        ImageView pictureImageView =
                (ImageView) view.findViewById(R.id.crime_picture_prevew_image_view);

        File file = (File) getArguments().getSerializable(ARG_PICTURE_FILE);
        if (file == null) {
            pictureImageView.setImageDrawable(null);
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            pictureImageView.setImageBitmap(bitmap);
        }


        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.crime_picture)
                .setView(view)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }
}
