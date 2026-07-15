package com.infosys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeTools {
    private static final Logger log = LoggerFactory.getLogger(EmployeeTools.class);
    private final EmployeeService employeeService;

    public EmployeeTools(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Tool(name="createNewEmployee", description="create a new infosys employee with the given details.")
    public Employee addEmployee(@ToolParam(required = false, description="Employee Id") Integer empId,
                                @ToolParam(required = true, description = "Employee Name") String name,
                                @ToolParam(required = true, description = "Employee Email") String email) {
        return employeeService.addEmployee(name, empId, email);
    }

    @Tool(name="updateExistingEmployee", description="update the employee details for the employee with given id")
    public Employee updateEmployee(@ToolParam(required = false, description="Employee Id") Integer empId,
                                   Employee employee) {
        return employeeService.updateEmployee(employee);
    }

    @Tool(name="deleteExistingEmployee", description="delete the employee details for the employee with given id")
    public Employee deleteEmployee(@ToolParam(required = false, description="Employee Id") Integer empId) {
        return employeeService.deleteEmployee(empId);
    }

    @Tool(name="deleteAllExistingEmployees", description="delete all employees details for infosys company")
    public void deleteAllEmployees(@ToolParam(required = false, description="Employee Id") Integer empId) {
        employeeService.deleteAllEmployees();
    }

    @Tool(description = "Get employee details for a given employee id of Infosys company")
    public Employee getEmployee(Integer empId) {
        log.info("Getting employee: {}", empId);
        Employee employee = employeeService.getEmployee(empId);
        log.info("Employee: {}", employee);
        return employee;
    }

    @Tool(description = "Find employees of Infosys company who are on leave for a given date in YYYY-MM-DD format")
    public List<Employee> findEmployeesOnLeave(LocalDate date) {
        log.info("Finding employees on leave for date: {}", date);
        List<Employee> employeesOnLeave = employeeService.findEmployeesOnLeave(date);
        log.info("Employees on leave: {} on date {}", employeesOnLeave, date);
        return employeesOnLeave;
    }

    @Tool(description = "Find all existing employees in Infosys")
    public List<Employee> findAllEmployees(){
        List<Employee> employees = employeeService.getAllEmployees();
        log.info("Employees are: {}", employees);
        return  employees;
    }

    @Tool(description = "Apply leave for a given employee id of Infosys company and date in YYYY-MM-DD format")
    public void applyLeave(Integer empId, LocalDate date) {
        log.info("Applying leave for employee: {} on date: {}", empId, date);
        employeeService.applyLeave(empId, date);
    }

    @Tool(description = "Apply leaves for a given employee id of Infosys company between the given start date and end date where date is in YYYY-MM-DD format")
    public void applyLeavesBetweenGivenDates(Integer empId, LocalDate startDate, LocalDate endDate) {
        log.info("Applying leaves for employee: {} on between start date: {}  and end date: {}", empId, startDate, endDate);
        employeeService.applyLeavesWithInDates(empId, startDate, endDate);
    }


    @Tool(description = "Cancel leave for a given employee id of Infosys company and date in YYYY-MM-DD format")
    public void cancelLeave(Integer empId, LocalDate date) {
        log.info("Cancelling leave for employee: {} on date: {}", empId, date);
        employeeService.cancelLeave(empId, date);
    }

    @Tool(description = "Cancel leaves for a given employee id of Infosys company between the given start date and end date where date is in YYYY-MM-DD format")
    public void cancelLeavesBetweenGivenDates(Integer empId, LocalDate startDate, LocalDate endDate) {
        log.info("Cancelling leaves for employee: {} on between start date: {}  and end date: {}", empId, startDate, endDate);
        employeeService.cancelLeavesWithInDates(empId, startDate, endDate);
    }

}
