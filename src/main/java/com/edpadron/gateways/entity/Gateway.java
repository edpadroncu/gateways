package com.edpadron.gateways.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Data
@Entity
@Table(name="gateway")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class Gateway implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idg;

    @Column(name = "serial_number", unique = true)
    @NotEmpty(message = "is required")
    private String serial_number;

    @Column(name = "name", nullable = false)
    @NotEmpty(message = "is required")
    private String name;

    @Column(name = "ipv4", nullable = false)
    @NotEmpty(message = "is required")
    private String ipv4;

    @JsonIgnore
    @OneToMany(mappedBy = "gateway", fetch = FetchType.LAZY)
    private Set<Peripheral> peripherals = new HashSet<>();

    @JsonIgnore
    public boolean isValidIpv4(){
        Pattern pattern = Pattern.compile("^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$");
        return pattern.matcher(ipv4).matches();
    }

}
