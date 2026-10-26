
package com.employee.Employee_Management_Payroll;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Employee name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Department is required")
    @Size(max = 80, message = "Department cannot exceed 80 characters")
    private String department;

    @NotNull(message = "Base salary is required")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Base salary must be greater than zero")
    private Double baseSalary;

    @NotBlank(message = "Please select an employee role")
    private String role;

    public Employee() {
    }

    public Employee(String name, String department,
                    Double baseSalary, String role) {
        this.name = name;
        this.department = department;
        this.baseSalary = baseSalary;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(Double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Transient
    public double getBonus() {
        if (baseSalary == null) {
            return 0.0;
        }

        return baseSalary * ("Manager".equalsIgnoreCase(role) ? 0.25 : 0.15);
    }

    @Transient
    public double getDeductions() {
        if (baseSalary == null) {
            return 0.0;
        }

        double deduction = baseSalary * 0.12;

        if ("Manager".equalsIgnoreCase(role)) {
            deduction += 200.0;
        }

        return deduction;
    }

    @Transient
    public double getNetSalary() {
        if (baseSalary == null) {
            return 0.0;
        }

        return baseSalary + getBonus() - getDeductions();
    }
}
