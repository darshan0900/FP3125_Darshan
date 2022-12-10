package com.darshan09200.employeemanagementsystem;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.darshan09200.employeemanagementsystem.databinding.FragmentHomeBinding;

interface OnFabClickListener {
    void onFabClick();
}

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    OnFabClickListener fabClickListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnFabClickListener) {
            fabClickListener = (OnFabClickListener) context;
        } else {
            throw new ClassCastException(context
                    + "must implement OnUpdatePressedListener interface"
            );
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        binding.fabBtn.setOnClickListener(v -> fabClickListener.onFabClick());

        return binding.getRoot();
    }
}
