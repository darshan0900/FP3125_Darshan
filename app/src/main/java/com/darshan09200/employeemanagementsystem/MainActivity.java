package com.darshan09200.employeemanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.darshan09200.employeemanagementsystem.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements OnFabClickListener {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction().add(R.id.mainFragment, new HomeFragment()).commit();
    }

    @Override
    public void onFabClick() {
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment, new RegistrationFragment()).commit();
    }
}