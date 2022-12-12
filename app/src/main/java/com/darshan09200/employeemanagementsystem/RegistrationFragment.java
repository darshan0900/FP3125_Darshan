package com.darshan09200.employeemanagementsystem;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.darshan09200.employeemanagementsystem.databinding.FragmentRegistrationBinding;

interface OnRegistrationActionListener {
    void onSubmit();
    void onClose();
}

public class RegistrationFragment extends Fragment {

    FragmentRegistrationBinding binding;
    RegistrationController controller;
    OnRegistrationActionListener registrationActionListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnRegistrationActionListener) {
            registrationActionListener = (OnRegistrationActionListener) context;
        } else {
            throw new ClassCastException(context
                    + "must implement OnRegistrationActionListener interface"
            );
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
            if (view != null)
                view.clearFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean onTouch(View v, MotionEvent event) {
        Activity activity = RegistrationFragment.this.getActivity();
        if (activity != null)
            hideKeyboard(activity.getCurrentFocus());
        return false;
    }
}
