package by.koronatech.office.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Can't be empty")
    @Size(min = 2, max = 30, message = "Should contain between 2 and 30 characters")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Should exist")
    @Positive(message = "Should be greater than 0")
    @Column(nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private BigDecimal salary;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isManager = false;

    public boolean isManager() {
        return isManager;
    }

    public void setIsManager(boolean manager) {
        this.isManager = manager;
    }
}
