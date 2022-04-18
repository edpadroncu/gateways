package com.edpadron.gateways.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="gateway")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class Gateway implements Serializable {

    public Gateway(String serial_number, String name, String ipv4) {
        this.serial_number = serial_number;
        this.name = name;
        this.ipv4 = ipv4;
    }

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
