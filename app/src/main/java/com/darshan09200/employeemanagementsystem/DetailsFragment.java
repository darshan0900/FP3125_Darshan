package com.darshan09200.employeemanagementsystem;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.darshan09200.employeemanagementsystem.databinding.FragmentDetailsBinding;

interface OnDetailsActionListener {
    void onEdit();

    void onDelete();

    void onClose();
}

public class DetailsFragment extends Fragment implements View.OnClickListener {
    FragmentDetailsBinding binding;
    OnDetailsActionListener detailsActionListener;

    Employee employee;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailsActionListener) {
            detailsActionListener = (OnDetailsActionListener) context;
        } else {
            throw new ClassCastException(context
                    + "must implement OnDetailsActionListener interface"
            );
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        setupData();
        binding.edit.setOnClickListener(this);
        binding.delete.setOnClickListener(this);
        binding.close.setOnClickListener(this);
        if (!isSplitLayoutActive()) {
            binding.close.setVisibility(View.GONE);
        }
        return binding.getRoot();
    }

    private boolean isSplitLayoutActive() {
        return getResources().getBoolean(R.bool.isSplitLayoutActive);
    }

    public void setupData() {
        System.out.println("refreshed");
        employee = Database.getInstance().getEmployee(Database.getInstance().getViewEmpId());

        if (binding != null && employee != null) {
            String filepath = employee.getProfileImage();
            System.out.println(filepath);
            if (filepath.length() > 0) {
                binding.profileImage.setImageBitmap(BitmapFactory.decodeFile(filepath));
            }else{
                binding.profileImage.setImageResource(R.drawable.ic_user);
            }
            binding.empId.setText(employee.getEmpId());
            binding.name.setText(employee.getFirstName() + " " + employee.getLastName());
            binding.age.setText(String.valueOf(employee.getAge()));
            binding.role.setText(employee.getRole().getLabel());
            double annualIncome = 0;
            String bonusLabel = "";
            int bonusValue = 0;
            if (employee instanceof Manager) {
                Manager manager = (Manager) employee;
                annualIncome = manager.getAnnualIncome();
                bonusLabel = "Clients";
                bonusValue = manager.getNbClients();
            } else if (employee instanceof Programmer) {
                Programmer programmer = (Programmer) employee;
                annualIncome = programmer.getAnnualIncome();
                bonusLabel = "Projects";
                bonusValue = programmer.getNbProjects();
            } else if (employee instanceof Tester) {
                Tester tester = (Tester) employee;
                annualIncome = tester.getAnnualIncome();
                bonusLabel = "Bugs";
                bonusValue = tester.getNbBugs();
            }
            binding.annualIncome.setText(String.format("%.2f", annualIncome));
            binding.bonusLabel.setText(String.format("Number of %s", bonusLabel));
            binding.bonus.setText(String.format("%d", bonusValue));

            Vehicle vehicle = employee.getVehicle();

            binding.vehicleMake.setText(vehicle.getMake().getLabel());
            binding.vehicleCategory.setText(vehicle.getCategory().getLabel());
            binding.vehicleColor.setText(vehicle.getColor().getLabel());
            binding.vehiclePlate.setText(vehicle.getPlate());

            if (vehicle instanceof Car) {
                Car car = (Car) vehicle;
                binding.vehicleKind.setImageResource(R.drawable.ic_car);
                binding.vehicleTypeLayout.setVisibility(View.VISIBLE);
                binding.vehicleType.setText(car.getType().getLabel());
                binding.sidecarLayout.setVisibility(View.GONE);
            } else if (vehicle instanceof Motorcycle) {
                Motorcycle motorcycle = (Motorcycle) vehicle;
                binding.vehicleKind.setImageResource(R.drawable.ic_motorcycle);
                binding.sidecarLayout.setVisibility(View.VISIBLE);
                setSidecar(motorcycle.isSidecar());
                binding.vehicleTypeLayout.setVisibility(View.GONE);
            }
        }
    }

    public void setSidecar(boolean isChecked) {
        if (isChecked) {
            binding.sidecar.setColorFilter(ContextCompat.getColor(getContext(), R.color.primary));
        } else {
            binding.sidecar.clearColorFilter();
        }
        Registration.getInstance().setSidecarChecked(isChecked);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit:
                setupEditData();
                detailsActionListener.onEdit();
                break;
            case R.id.delete:
                onDelete();
                break;
            case R.id.close:
                detailsActionListener.onClose();
                break;
        }
    }

    public void onDelete() {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Employee")
                .setMessage("Are you sure you want to delete " + employee.getEmpId() + "?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    Database.getInstance().deleteEmployee(employee);
                    detailsActionListener.onDelete();
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void setupEditData() {
        if (employee != null) {

            Registration.getInstance().setProfileImage(employee.getProfileImage());
            Registration.getInstance().setEmpId(employee.getEmpId());
            Registration.getInstance().setFirstName(employee.getFirstName());
            Registration.getInstance().setLastName(employee.getLastName());
            Registration.getInstance().setDob(employee.getDob());
            Registration.getInstance().setEmployeeType(employee.getRole());
            Registration.getInstance().setMonthlySalary(String.valueOf(employee.getMonthlySalary()));
            Registration.getInstance().setOccupationRate(String.valueOf(employee.getOccupationRate()));
            int bonusValue = 0;
            if (employee instanceof Manager) {
                Manager manager = (Manager) employee;
                bonusValue = manager.getNbClients();
            } else if (employee instanceof Programmer) {
                Programmer programmer = (Programmer) employee;
                bonusValue = programmer.getNbProjects();
            } else if (employee instanceof Tester) {
                Tester tester = (Tester) employee;
                bonusValue = tester.getNbBugs();
            }
            Registration.getInstance().setBonusValue(String.valueOf(bonusValue));

            Vehicle vehicle = employee.getVehicle();

            Registration.getInstance().setVehicleMake(vehicle.getMake());
            Registration.getInstance().setVehicleCategory(vehicle.getCategory());
            Registration.getInstance().setVehicleColor(vehicle.getColor());
            Registration.getInstance().setVehiclePlate(vehicle.getPlate());

            if (vehicle instanceof Car) {
                Car car = (Car) vehicle;
                Registration.getInstance().setVehicleKind(VehicleKind.CAR);
                Registration.getInstance().setVehicleType(car.getType());
            } else if (vehicle instanceof Motorcycle) {
                Motorcycle motorcycle = (Motorcycle) vehicle;
                Registration.getInstance().setVehicleKind(VehicleKind.MOTORCYCLE);
                Registration.getInstance().setSidecarChecked(motorcycle.isSidecar());
            }
            Registration.getInstance().setEdit(true);
        } else {
            detailsActionListener.onClose();
        }
    }
}
