package com.darshan09200.employeemanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.darshan09200.employeemanagementsystem.databinding.FragmentHomeBinding;

import java.util.ArrayList;

interface OnFabClickListener {
    void onFabClick();
}

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    OnFabClickListener fabClickListener;

    ArrayList<Employee> employees;
    ArrayAdapter<Employee> adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnFabClickListener) {
            fabClickListener = (OnFabClickListener) context;
        } else {
            throw new ClassCastException(context
                    + "must implement OnFabClickListener interface"
            );
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        employees = new ArrayList<>();
        employees.addAll(Database.getInstance().getEmployees());
        adapter = new ArrayAdapter<Employee>(getContext(), android.R.layout.simple_list_item_2, android.R.id.text1, employees) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);
                Employee employee = getItem(position);
                text1.setText(employee.getName());
                text2.setText(employee.getRole().getLabel());
                return view;
            }
        };
        adapter.setNotifyOnChange(true);
        binding.listView.setAdapter(adapter);
        binding.listView.setOnItemClickListener((parent, view, position, id) -> {
//            Employee employee = (Employee) parent.getItemAtPosition(position);
//            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
//            intent.putExtra("empId", employee.getEmpId());
//            startActivity(intent);
        });

        binding.addEmployee.setOnClickListener(v -> fabClickListener.onFabClick());

        return binding.getRoot();
    }

    public void refreshData() {
        employees.clear();
        employees.addAll(Database.getInstance().getEmployees());
        adapter.notifyDataSetChanged();
    }
}
