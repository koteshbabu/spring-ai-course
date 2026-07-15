package com.infosys;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="employee_leaves")
public class EmployeeLeave {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDate leaveDate;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public EmployeeLeave(){

    }

    public EmployeeLeave(Integer id, LocalDate leaveDate, Employee employee) {
        this.id = id;
        this.leaveDate = leaveDate;
        this.employee = employee;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(LocalDate leaveDate) {
        this.leaveDate = leaveDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
