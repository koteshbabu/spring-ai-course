package com.infosys;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private  EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeLeaveRepository employeeLeaveRepository;

    void deleteEmployee(Integer id) {
        employeeRepository.deleteById(id);
    }

    void deleteAllEmployees() {
        employeeRepository.deleteAll();
    }

    Employee getEmployee(Integer id) {
        return employeeRepository.findById(id).orElse(null);
    }

    List<Employee> getAllEmployees() {
        return employeeRepository.findAll().stream().sorted((e1, e2) -> e1.getId().compareTo(e2.getId())).toList();
    }

    List<Employee> findEmployeesOnLeave(LocalDate leaveDate) {
       return employeeLeaveRepository.findAllByLeaveDate(leaveDate).stream()
               .map(EmployeeLeave::getEmployee).toList();
    }

    List<Employee> getEmployees(List<Integer> empIds) {
        return empIds.stream().map(empId -> employeeRepository.findAll()).flatMap(e -> e.stream()).toList();
    }

    @Transactional
    void cancelLeave(Integer empId, LocalDate leaveDate) {
        if (leaveDate.isBefore(LocalDate.now())) {
            throw new RuntimeException("Leaves of past dates can't be cancelled");
        }
        employeeLeaveRepository.deleteByLeaveDateAndEmployeeId( leaveDate,  empId);
    }

    public Collection<LocalDate> findAllLeavesBasedOnEmployeeId(Integer employeeId){
        List<EmployeeLeave> employeeLeaves = employeeLeaveRepository.findAllByEmployeeId(employeeId);
        return employeeLeaves.stream().map(EmployeeLeave::getLeaveDate).collect(Collectors.toSet());
    }

    void applyLeave(Integer empId, LocalDate leaveDate) {

        if (leaveDate.isBefore(LocalDate.now())) {
            throw new RuntimeException("Date should not be in the past while applying for dates");
        }
    EmployeeLeave employeeLeave = new EmployeeLeave(null, leaveDate, employeeRepository.findById(empId).orElse(null));
        employeeLeaveRepository.save(employeeLeave);
    }

    void applyLeavesWithInDates(Integer empId, LocalDate startDate, LocalDate endDate) {

        List<LocalDate> inBetweenDates = startDate.datesUntil(endDate).toList();

        for (LocalDate date : startDate.datesUntil(endDate).toList()) {
            applyLeave(empId, date);
        }
        applyLeave(empId, endDate);
    }

    void cancelLeavesWithInDates(Integer empId, LocalDate startDate, LocalDate endDate) {
        List<LocalDate> inBetweenDates = startDate.datesUntil(endDate).toList();

        for (LocalDate date : startDate.datesUntil(endDate).toList()) {
            cancelLeave(empId, date);
        }
        cancelLeave(empId, endDate);
    }

    public  Employee updateEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }


    public Employee addEmployee(String name, String email) {
        if (Strings.isBlank(name) || Strings.isBlank(email)) {
            throw new RuntimeException("Employee Name and Email are mandatory");
        }
        Employee employee = new Employee(null, name,email);
        return employeeRepository.save(employee);
    }
}
