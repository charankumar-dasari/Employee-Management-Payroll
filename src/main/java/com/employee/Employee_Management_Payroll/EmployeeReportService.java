package com.employee.Employee_Management_Payroll;

import org.springframework.stereotype.Service;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeReportService {

    public Employee searchById(List<Employee> employees, Long id) {
        return employees.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));
    }

    public List<Employee> searchByDepartment(List<Employee> employees, String department) {
        return employees.stream()
                .filter(e -> e.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }

    public void exportPayrollReport(List<Employee> employees, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("--- COMPANY PAYROLL REPORT ---\n");
            for (Employee e : employees) {
                writer.write(String.format("ID: %d | Name: %s | Role: %s | Dept: %s | Net Salary: $%.2f\n",
                        e.getId(), e.getName(), e.getRole(), e.getDepartment(), e.calculateNetSalary()));
            }
        } catch (IOException e) {
            System.err.println("Failed to write report to file: " + e.getMessage());
        }
    }
}
