package com.employee.Employee_Management_Payroll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository repository;

    public List<Employee> getAllEmployees(){
         return repository.findAll(); 
        }
    public Employee saveEmployee(Employee emp){
         return repository.save(emp); 
        }
    public Employee getEmployeeById(Long id){
         return repository.findById(id).orElse(null);
         }
    public void deleteEmployee(Long id){
         repository.deleteById(id); 
        }
}
