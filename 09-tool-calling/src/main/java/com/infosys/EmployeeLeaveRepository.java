package com.infosys;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeLeaveRepository extends JpaRepository<EmployeeLeave, Integer> {

    List<EmployeeLeave> findAllByEmployeeId(Integer employeeId);
    List<EmployeeLeave> findAllByLeaveDate(LocalDate leaveDate);

    void  deleteByLeaveDateAndEmployee(LocalDate leaveDate, Employee employee);
    void  deleteByLeaveDateAndEmployeeId(LocalDate leaveDate, Integer employeeId);


}
