package com.edpadron.gateways.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Data
@Entity
@Table(name="peripheral")
public class Peripheral implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idp;

    @NotNull(message = "uid is required")
    private BigInteger uid;

    @Column(name = "vendor", nullable = false)
    @NotEmpty(message = "vendor is required")
    private String vendor;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime created_at;

    @Column(name = "status", nullable = false)
    @NotEmpty(message = "status is required")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idg")
    private Gateway gateway;

}
