package com.darshan09200.employeemanagementsystem;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

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
        if (!isSplitLayoutActive()) binding.close.setVisibility(View.GONE);
        return binding.getRoot();
    }

    private boolean isSplitLayoutActive() {
        return getResources().getBoolean(R.bool.isSplitLayoutActive);
    }
}
