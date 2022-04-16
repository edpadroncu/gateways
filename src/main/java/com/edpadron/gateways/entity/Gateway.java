package com.edpadron.gateways.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Data
@Entity
@Table(name="gateway")
public class Gateway implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idg;

    @Column(name = "serial_number", unique = true)
    @NotEmpty(message = "serial_number is required")
    private String serial_number;

    @Column(name = "name")
    @NotEmpty(message = "name is required")
    private String name;

    @Column(name = "ipv4")
    private String ipv4;

    @OneToMany(mappedBy = "gateway", fetch = FetchType.LAZY)
    private Set<Peripheral> peripherals = new HashSet<>();

}
