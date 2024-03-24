package com.example.springbootexceptionhandling.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.springbootexceptionhandling.exception.ResourceNotFoundException;
import com.example.springbootexceptionhandling.model.Employee;
import com.example.springbootexceptionhandling.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    void testGetAllEmployees() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeRepository.findAll()).thenReturn(employees);

        assertEquals(employees, employeeController.getAllEmployees());
    }

    @Test
    void testGetEmployeeById() throws ResourceNotFoundException {
        Long employeeId = 1L;
        Employee employee = new Employee();
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        ResponseEntity<Employee> responseEntity = employeeController.getEmployeeById(employeeId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(employee, responseEntity.getBody());
    }

    @Test
    void testGetEmployeeById_NotFound() {
        Long employeeId = 1L;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = org.junit.jupiter.api.Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> employeeController.getEmployeeById(employeeId)
        );
        assertEquals("Employee not found for this id :: " + employeeId, exception.getMessage());
    }

    @Test
    void testCreateEmployee() {
        Employee employee = new Employee();
        when(employeeRepository.save(employee)).thenReturn(employee);

        assertEquals(employee, employeeController.createEmployee(employee));
    }

    @Test
    void testUpdateEmployee() throws ResourceNotFoundException {
        Long employeeId = 1L;
        Employee existingEmployee = new Employee();
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));

        Employee updatedEmployee = new Employee();
        updatedEmployee.setFirstName("Updated");
        updatedEmployee.setLastName("Employee");
        updatedEmployee.setEmailId("updated@example.com");

        ResponseEntity<Employee> responseEntity = employeeController.updateEmployee(employeeId, updatedEmployee);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedEmployee.getFirstName(), existingEmployee.getFirstName());
        assertEquals(updatedEmployee.getLastName(), existingEmployee.getLastName());
        assertEquals(updatedEmployee.getEmailId(), existingEmployee.getEmailId());
    }

    @Test
    void testUpdateEmployee_NotFound() {
        Long employeeId = 1L;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        Employee updatedEmployee = new Employee();

        ResourceNotFoundException exception = org.junit.jupiter.api.Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> employeeController.updateEmployee(employeeId, updatedEmployee)
        );
        assertEquals("Employee not found for this id :: " + employeeId, exception.getMessage());
    }

    @Test
    void testDeleteEmployee() throws ResourceNotFoundException {
        Long employeeId = 1L;
        Employee employee = new Employee();
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        Map<String, Boolean> response = employeeController.deleteEmployee(employeeId);

        assertEquals(1, response.size());
        assertEquals(true, response.get("deleted"));
        verify(employeeRepository, times(1)).delete(employee);
    }

    @Test
    void testDeleteEmployee_NotFound() {
        Long employeeId = 1L;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = org.junit.jupiter.api.Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> employeeController.deleteEmployee(employeeId)
        );
        assertEquals("Employee not found for this id :: " + employeeId, exception.getMessage());
    }
}
