package by.koronatech.office.core.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private BigDecimal salary;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    private Boolean isManager;

    public boolean isManager() {
        return isManager;
    }

    public void setIsManager(boolean manager) {
        this.isManager = manager;
    }
}
