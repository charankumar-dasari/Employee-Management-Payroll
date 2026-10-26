package com.employee.Employee_Management_Payroll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
    
    @Autowired
    private EmployeeService service;

    @Autowired
    private EmployeeReportService reportService;

    // View Dashboard
    @GetMapping
    public String listEmployees(Model model) {
        List<Employee> employees = service.getAllEmployees();
        model.addAttribute("employees", employees);
        return "employee-list";
    }

    
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employee-form";
    }

    
    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") Employee employee) {
        service.saveEmployee(employee);
        return "redirect:/employees";
    }

    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Employee employee = service.getEmployeeById(id);
        if (employee != null) {
            model.addAttribute("employee", employee);
            return "employee-form";
        }
        return "redirect:/employees";
    }

    
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        service.deleteEmployee(id);
        return "redirect:/employees";
    }

    
    @GetMapping("/export")
    public String exportReport() {
        List<Employee> employees = service.getAllEmployees();
        reportService.exportPayrollReport(employees, "Payroll_Report.txt");
        return "redirect:/employees?exported=true";
    }
}
