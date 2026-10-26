package com.employee.Employee_Management_Payroll;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Employee {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String department;
    private double baseSalary;
    private String role; 

    public Employee(){}

    public Employee(Long id, String name, String department, double baseSalary, String role) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.baseSalary = baseSalary;
        this.role = role;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public double getBaseSalary() { return baseSalary; }
    public void setBaseSalary(double baseSalary) { this.baseSalary = baseSalary; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return "Employee [id=" + id + ", name=" + name + ", department=" + department + ", baseSalary=" + baseSalary
                + ", role=" + role + "]";
    }
   
    public double calculateBonus() {
        if (this.role != null && this.role.equalsIgnoreCase("Manager")) {
            return this.baseSalary * 0.25; // 25% Manager Bonus
        } else {
            return this.baseSalary * 0.15; // 15% Developer Bonus
        }
    }

    public double calculateDeductions() {
        double standardDeduction = this.baseSalary * 0.12; // 12% standard tax
        if (this.role != null && this.role.equalsIgnoreCase("Manager")) {
            return standardDeduction + 200; // Extra executive tax tier
        }
        return standardDeduction;
    }

    public double calculateNetSalary() {
        return this.baseSalary + calculateBonus() - calculateDeductions();
    }
}
