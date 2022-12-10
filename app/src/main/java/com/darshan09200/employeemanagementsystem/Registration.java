package com.darshan09200.employeemanagementsystem;


import java.util.ArrayList;

interface EnhancedEnum {
    String getLabel();
}

enum EmployeeType implements EnhancedEnum {
    MANAGER("Manager"),
    PROGRAMMER("Programmer"),
    TESTER("Tester");

    private final String label;

    EmployeeType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}


enum VehicleKind implements EnhancedEnum {
    BOTH("Both"),
    CAR("Car"),
    MOTORCYCLE("Motorcycle");

    private final String label;

    VehicleKind(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

enum VehicleMake implements EnhancedEnum {
    CHOOSE_MAKE("Please choose a make", VehicleKind.BOTH),
    KAWASAKI("Kawasaki", VehicleKind.MOTORCYCLE),
    HONDA("Honda", VehicleKind.BOTH),
    LAMBORGHINI("Lamborghini", VehicleKind.CAR),
    BMW("BMW", VehicleKind.CAR),
    RENAULT("Renault", VehicleKind.CAR),
    MAZDA("Mazda", VehicleKind.CAR);

    private final String label;
    private final VehicleKind vehicle;

    VehicleMake(String label, VehicleKind vehicle) {
        this.label = label;
        this.vehicle = vehicle;
    }

    public String getLabel() {
        return label;
    }

    public VehicleKind getVehicle() {
        return vehicle;
    }
}

enum VehicleCategory implements EnhancedEnum {
    CHOSE_CATEGORY("Please choose a category", VehicleKind.BOTH),
    RACE_MOTORCYCLE("Race Motorcycle", VehicleKind.MOTORCYCLE),
    NOT_FOR_RACE("Not for Race", VehicleKind.BOTH),
    FAMILY("Family", VehicleKind.CAR);

    private final String label;
    private final VehicleKind vehicle;

    VehicleCategory(String label, VehicleKind vehicle) {
        this.label = label;
        this.vehicle = vehicle;
    }

    public String getLabel() {
        return label;
    }

    public VehicleKind getVehicle() {
        return vehicle;
    }
}

enum VehicleType implements EnhancedEnum {
    CHOOSE_TYPE("Please choose a type"),
    SEDAN("Sedan"),
    SPORT("Sport"),
    HATCHBACK("Hatchback"),
    SUV("SUV");

    private final String label;

    VehicleType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

enum VehicleColor implements EnhancedEnum {
    CHOOSE_COLOR("Please choose a color"),
    YELLOW("Yellow"),
    BLACK("Black"),
    WHITE("White"),
    RED("Red");

    private final String label;

    VehicleColor(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}


public class Registration {
    private static Registration instance;

    private VehicleKind vehicle = VehicleKind.CAR;

    private Registration() {
    }

    public static Registration getInstance() {
        if (instance == null) instance = new Registration();
        return instance;
    }

    public VehicleKind getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleKind vehicleKind) {
        this.vehicle = vehicleKind;
    }

    public ArrayList<String> getEmployeeTypeData() {
        ArrayList<String> employeeTypes = new ArrayList<>();

        for (EmployeeType employeeType :
                EmployeeType.values()) {
            employeeTypes.add(employeeType.getLabel());
        }
        return employeeTypes;
    }

    public ArrayList<String> getVehicleMakeData() {
        ArrayList<String> vehicleMakes = new ArrayList<>();

        for (VehicleMake vehicleMake :
                VehicleMake.values()) {
            if (vehicleMake.getVehicle() == vehicle || vehicleMake.getVehicle() == VehicleKind.BOTH)
                vehicleMakes.add(vehicleMake.getLabel());
        }
        return vehicleMakes;
    }

    public ArrayList<String> getVehicleCategoryData() {
        ArrayList<String> vehicleCategories = new ArrayList<>();

        for (VehicleCategory vehicleCategory :
                VehicleCategory.values()) {
            if (vehicleCategory.getVehicle() == vehicle || vehicleCategory.getVehicle() == VehicleKind.BOTH)
                vehicleCategories.add(vehicleCategory.getLabel());
        }
        return vehicleCategories;
    }

    public ArrayList<String> getVehicleTypeData() {
        ArrayList<String> vehicleTypes = new ArrayList<>();

        for (VehicleType vehicleType :
                VehicleType.values()) {
            vehicleTypes.add(vehicleType.getLabel());
        }
        return vehicleTypes;
    }

    public ArrayList<String> getVehicleColorData() {
        ArrayList<String> vehicleColors = new ArrayList<>();

        for (VehicleColor vehicleColor :
                VehicleColor.values()) {
            vehicleColors.add(vehicleColor.getLabel());
        }
        return vehicleColors;
    }

}
