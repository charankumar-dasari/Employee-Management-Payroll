
package com.employee.Employee_Management_Payroll;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import jakarta.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EmployeePageController {

    private final EmployeeService employeeService;
    private final EmployeeReportService reportService;

    public EmployeePageController(
            EmployeeService employeeService,
            EmployeeReportService reportService) {
        this.employeeService = employeeService;
        this.reportService = reportService;
    }

    @GetMapping({"/", "/employees"})
    public String dashboard(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "") String department,
            @RequestParam(required = false, defaultValue = "") String role,
            @RequestParam(required = false, defaultValue = "newest") String sortBy,
            Model model) {

        model.addAttribute("employees",
                employeeService.searchEmployees(
                        keyword, department, role, sortBy));

        model.addAttribute("totalEmployees",
                employeeService.getTotalEmployees());
        model.addAttribute("totalBaseSalary",
                employeeService.getTotalBaseSalary());
        model.addAttribute("totalBonus",
                employeeService.getTotalBonus());
        model.addAttribute("totalDeductions",
                employeeService.getTotalDeductions());
        model.addAttribute("totalNetSalary",
                employeeService.getTotalNetSalary());
        model.addAttribute("managerCount",
                employeeService.getManagerCount());
        model.addAttribute("developerCount",
                employeeService.getDeveloperCount());
        model.addAttribute("departments",
                employeeService.getDepartments());

        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedDepartment", department);
        model.addAttribute("selectedRole", role);
        model.addAttribute("sortBy", sortBy);

        return "index";
    }

    @GetMapping("/employees/add")
    public String addEmployee(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("pageTitle", "Add Employee");
        return "employee-form";
    }

    @GetMapping("/employees/edit/{id}")
    public String editEmployee(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            model.addAttribute("employee",
                    employeeService.getEmployeeById(id));
            model.addAttribute("pageTitle", "Edit Employee");
            return "employee-form";
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", exception.getMessage());
            return "redirect:/employees";
        }
    }

    @PostMapping("/employees/save")
    public String saveEmployee(
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle",
                    employee.getId() == null ? "Add Employee" : "Edit Employee");
            return "employee-form";
        }

        try {
            if (employee.getId() == null) {
                employeeService.saveEmployee(employee);
                redirectAttributes.addFlashAttribute(
                        "successMessage", "Employee added successfully.");
            } else {
                employeeService.updateEmployee(employee.getId(), employee);
                redirectAttributes.addFlashAttribute(
                        "successMessage", "Employee updated successfully.");
            }
        } catch (IllegalArgumentException exception) {
            model.addAttribute("pageTitle",
                    employee.getId() == null ? "Add Employee" : "Edit Employee");
            model.addAttribute("errorMessage", exception.getMessage());
            return "employee-form";
        }

        return "redirect:/employees";
    }

    @PostMapping("/employees/delete/{id}")
    public String deleteEmployee(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            employeeService.deleteEmployee(id);
            redirectAttributes.addFlashAttribute(
                    "successMessage", "Employee deleted successfully.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", exception.getMessage());
        }

        return "redirect:/employees";
    }

    @GetMapping("/employees/export")
    public ResponseEntity<byte[]> exportReport() throws IOException {
        Path file = reportService.exportPayrollReport();
        byte[] content = Files.readAllBytes(file);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFileName() + "\"")
                .body(content);
    }
}
