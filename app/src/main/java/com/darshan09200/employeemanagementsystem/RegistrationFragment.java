package com.darshan09200.employeemanagementsystem;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.darshan09200.employeemanagementsystem.databinding.FragmentRegistrationBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

interface OnRegistrationActionListener {
    void onSubmit();

    void onClose();
}

public class RegistrationFragment extends Fragment implements ActivityResultCallback<ActivityResult> {

    FragmentRegistrationBinding binding;
    RegistrationController controller;
    OnRegistrationActionListener registrationActionListener;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnRegistrationActionListener) {
            registrationActionListener = (OnRegistrationActionListener) context;
        } else {
            throw new ClassCastException(context + "must implement OnRegistrationActionListener interface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false);
        controller = new RegistrationController(getContext(), binding, registrationActionListener);
        if (!isSplitLayoutActive()) {
            binding.close.setVisibility(View.GONE);
            setupUI(binding.parent);
        } else {
            setupUI(getActivity().findViewById(R.id.main_parent));
        }
        binding.profileImage.setOnClickListener(view -> {
            openImagePicker();
        });
        loadImage();
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this);
        return binding.getRoot();
    }

    private boolean isSplitLayoutActive() {
        return getResources().getBoolean(R.bool.isSplitLayoutActive);
    }

    public void setupUI(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(this::onTouch);
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public void hideKeyboard(View view) {
        try {
            if (view != null) view.clearFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean onTouch(View v, MotionEvent event) {
        Activity activity = RegistrationFragment.this.getActivity();
        if (activity != null) hideKeyboard(activity.getCurrentFocus());
        return false;
    }

    private void openImagePicker() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        activityResultLauncher.launch(i);
    }

    @Override
    public void onActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                Bitmap selectedImageBitmap;
                try {
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                    String filepath = saveImage(selectedImageBitmap);
                    if (filepath != null) {
                        Registration.getInstance().setProfileImage(filepath);
                        loadImage();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String saveImage(Bitmap finalBitmap) {
        String root = getActivity().getFilesDir().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();

        String timeStamp = String.valueOf(new Date().getTime());
        String filename = timeStamp + ".jpg";

        File file = new File(myDir, filename);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void loadImage() {
        String filepath = Registration.getInstance().getProfileImage();
        if (filepath.length() > 0)
            binding.profileImage.setImageBitmap(BitmapFactory.decodeFile(filepath));
        else
            binding.profileImage.setImageResource(R.drawable.ic_user);
    }
}
