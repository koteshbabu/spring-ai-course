package com.infosys;

import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.util.Strings;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private static final Map<Integer, Employee> employeeTable = new HashMap<>();
    private static final Map<LocalDate, List<Integer>> employeeLeavesTable = new HashMap<>();

//    @PostConstruct
//    void init() {
//        employeeTable.put(1001, new Employee(1001, "John Doe", "john.doe@example.com"));
//        employeeTable.put(1002, new Employee(1002, "Koti", "koti@example.com"));
//        employeeTable.put(1003, new Employee(1003, "James", "james@example.com"));
//
//        employeeLeavesTable.put(LocalDate.now(), List.of(1001, 1003));
//        employeeLeavesTable.put(LocalDate.of(2025, 1, 1), List.of(1001, 1002));
//        employeeLeavesTable.put(LocalDate.of(2025, 1, 2), List.of(1002, 1003));
//    }

    Employee deleteEmployee(Integer empId) {
       return employeeTable.remove(empId);
    }

    void deleteAllEmployees() {
        employeeTable.clear();
    }

    Employee getEmployee(Integer empId) {
        return employeeTable.get(empId);
    }

    List<Employee> getAllEmployees() {
        return employeeTable.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap( Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1)).values().stream().toList();
    }

    List<Employee> findEmployeesOnLeave(LocalDate date) {
        List<Integer> empIds = employeeLeavesTable.get(date);
        return empIds == null? List.of() : getEmployees(empIds);
    }

    List<Employee> getEmployees(List<Integer> empIds) {
        return empIds.stream().map(employeeTable::get).toList();
    }

    void cancelLeave(Integer empId, LocalDate leaveDate) {
        if (leaveDate.isBefore(LocalDate.now())) {
            throw new RuntimeException("Leaves of past dates can't be cancelled");
        }
        List<Integer> empIds = employeeLeavesTable.get(leaveDate);
        if (CollectionUtils.isEmpty(empIds)) {
            throw new RuntimeException("Employee: "+empId+" is not on leave on "+leaveDate);
        }
        empIds.remove(empId);
    }

    void applyLeave(Integer empId, LocalDate date) {

        if (date.isBefore(LocalDate.now())) {
            throw new RuntimeException("Date should not be in the past while applying for dates");
        }
        List<Integer> empIds = employeeLeavesTable.get(date);
        if(empIds == null) {
            empIds = new ArrayList<>();
            empIds.add(empId);
        } else {
            empIds.add(empId);
        }
        employeeLeavesTable.put(date, empIds);
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
        employeeTable.put(employee.empId(), employee);
        return employee;
    }


    public Employee addEmployee(String name, Integer empId, String email) {
        if (Strings.isBlank(name) || Strings.isBlank(email)) {
            throw new RuntimeException("Employee Name and Email are mandatory");
        }

        if (empId == null) {
           List<Integer> empIds = employeeTable.values().stream().map(Employee::empId).toList();
            empId = CollectionUtils.isEmpty(empIds) ? 1 : Collections.max(empIds)+1;
        } else {
            if (employeeTable.get(empId) != null) {
                throw new RuntimeException("An employee with that id: "+empId+" exists already.");
            }
        }

        Employee employee = new Employee(empId, name,email);
        employeeTable.put(empId, employee);
        return employee;
    }
}
