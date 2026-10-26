
package com.employee.Employee_Management_Payroll;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class EmployeeReportService {

    private final EmployeeService employeeService;

    public EmployeeReportService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public Path exportPayrollReport() throws IOException {
        List<Employee> employees = employeeService.getAllEmployees();

        Path directory = Paths.get("reports");
        Files.createDirectories(directory);

        String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        Path file = directory.resolve(
                "Payroll_Report_" + timestamp + ".csv");

        try (BufferedWriter writer = Files.newBufferedWriter(
                file, StandardCharsets.UTF_8)) {

            writer.write(
                    "Employee ID,Name,Department,Role,Base Salary,Bonus,Deductions,Net Salary");
            writer.newLine();

            for (Employee employee : employees) {
                writer.write(String.join(",",
                        csv(String.valueOf(employee.getId())),
                        csv(employee.getName()),
                        csv(employee.getDepartment()),
                        csv(employee.getRole()),
                        money(employee.getBaseSalary()),
                        money(employee.getBonus()),
                        money(employee.getDeductions()),
                        money(employee.getNetSalary())));
                writer.newLine();
            }

            writer.write(String.join(",",
                    "TOTAL", "", "", "",
                    money(employeeService.getTotalBaseSalary()),
                    money(employeeService.getTotalBonus()),
                    money(employeeService.getTotalDeductions()),
                    money(employeeService.getTotalNetSalary())));
            writer.newLine();
        }

        return file;
    }

    private String money(Double amount) {
        return String.format(java.util.Locale.US, "%.2f",
                amount == null ? 0.0 : amount);
    }

    private String csv(String value) {
        if (value == null) {
            return "";
        }

        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
