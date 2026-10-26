
package com.employee.Employee_Management_Payroll;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private static final Set<String> VALID_ROLES =
            Set.of("Developer", "Manager");

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Employee not found: " + id));
    }

    public Employee saveEmployee(Employee employee) {
        validateEmployee(employee);
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee employee) {
        Employee existing = getEmployeeById(id);

        validateEmployee(employee);

        existing.setName(employee.getName().trim());
        existing.setDepartment(employee.getDepartment().trim());
        existing.setRole(employee.getRole());
        existing.setBaseSalary(employee.getBaseSalary());

        return employeeRepository.save(existing);
    }

    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new IllegalArgumentException("Employee not found: " + id);
        }

        employeeRepository.deleteById(id);
    }

    public List<Employee> searchEmployees(
            String keyword,
            String department,
            String role,
            String sortBy) {

        List<Employee> employees = employeeRepository.findAll();

        String search = keyword == null
                ? ""
                : keyword.trim().toLowerCase(Locale.ROOT);

        String selectedDepartment = department == null
                ? ""
                : department.trim();

        String selectedRole = role == null ? "" : role.trim();

        List<Employee> result = employees.stream()
                .filter(employee -> {
                    if (search.isEmpty()) {
                        return true;
                    }

                    return String.valueOf(employee.getId()).contains(search)
                            || safe(employee.getName()).toLowerCase(Locale.ROOT)
                                    .contains(search)
                            || safe(employee.getDepartment()).toLowerCase(Locale.ROOT)
                                    .contains(search)
                            || safe(employee.getRole()).toLowerCase(Locale.ROOT)
                                    .contains(search);
                })
                .filter(employee -> selectedDepartment.isEmpty()
                        || selectedDepartment.equalsIgnoreCase(
                                safe(employee.getDepartment())))
                .filter(employee -> selectedRole.isEmpty()
                        || selectedRole.equalsIgnoreCase(
                                safe(employee.getRole())))
                .collect(Collectors.toList());

        Comparator<Employee> comparator = switch (
                sortBy == null ? "newest" : sortBy) {
            case "nameAsc" -> Comparator.comparing(
                    Employee::getName,
                    String.CASE_INSENSITIVE_ORDER);
            case "salaryAsc" -> Comparator.comparing(
                    Employee::getBaseSalary,
                    Comparator.nullsLast(Double::compareTo));
            case "salaryDesc" -> Comparator.comparing(
                    Employee::getBaseSalary,
                    Comparator.nullsLast(Double::compareTo)).reversed();
            case "netSalaryAsc" -> Comparator.comparingDouble(
                    Employee::getNetSalary);
            case "netSalaryDesc" -> Comparator.comparingDouble(
                    Employee::getNetSalary).reversed();
            case "oldest" -> Comparator.comparing(Employee::getId);
            default -> Comparator.comparing(
                    Employee::getId,
                    Comparator.nullsLast(Comparator.reverseOrder()));
        };

        result.sort(comparator);
        return result;
    }

    public List<String> getDepartments() {
        return employeeRepository.findAll().stream()
                .map(Employee::getDepartment)
                .filter(department -> department != null
                        && !department.isBlank())
                .map(String::trim)
                .collect(Collectors.toCollection(TreeSet::new))
                .stream()
                .toList();
    }

    public long getTotalEmployees() {
        return employeeRepository.count();
    }

    public double getTotalBaseSalary() {
        return employeeRepository.findAll().stream()
                .mapToDouble(employee -> value(employee.getBaseSalary()))
                .sum();
    }

    public double getTotalBonus() {
        return employeeRepository.findAll().stream()
                .mapToDouble(Employee::getBonus)
                .sum();
    }

    public double getTotalDeductions() {
        return employeeRepository.findAll().stream()
                .mapToDouble(Employee::getDeductions)
                .sum();
    }

    public double getTotalNetSalary() {
        return employeeRepository.findAll().stream()
                .mapToDouble(Employee::getNetSalary)
                .sum();
    }

    public long getManagerCount() {
        return employeeRepository.findAll().stream()
                .filter(employee -> "Manager".equalsIgnoreCase(employee.getRole()))
                .count();
    }

    public long getDeveloperCount() {
        return employeeRepository.findAll().stream()
                .filter(employee -> "Developer".equalsIgnoreCase(employee.getRole()))
                .count();
    }

    private void validateEmployee(Employee employee) {
        if (employee.getName() == null || employee.getName().isBlank()) {
            throw new IllegalArgumentException("Employee name is required.");
        }

        if (employee.getDepartment() == null
                || employee.getDepartment().isBlank()) {
            throw new IllegalArgumentException("Department is required.");
        }

        if (employee.getRole() == null
                || !VALID_ROLES.contains(employee.getRole())) {
            throw new IllegalArgumentException(
                    "Role must be Developer or Manager.");
        }

        if (employee.getBaseSalary() == null
                || !Double.isFinite(employee.getBaseSalary())
                || employee.getBaseSalary() <= 0) {
            throw new IllegalArgumentException(
                    "Base salary must be a valid positive amount.");
        }

        employee.setName(employee.getName().trim());
        employee.setDepartment(employee.getDepartment().trim());
    }

    private double value(Double number) {
        return number == null ? 0.0 : number;
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}
