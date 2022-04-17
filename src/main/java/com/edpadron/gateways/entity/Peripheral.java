package com.edpadron.gateways.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import java.util.regex.Pattern;


@Data
@Entity
@Table(name="peripheral")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Peripheral implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idp;

    @Column(name = "uid", nullable = false)
    @NotEmpty(message = "uid is required")
    private String uid;

    @Column(name = "vendor", nullable = false)
    @NotEmpty(message = "vendor is required")
    private String vendor;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime created_at;

    @Column(name = "status", nullable = false)
    @NotEmpty(message = "status is required")
    private String status;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idg")
    private Gateway gateway;

    @JsonIgnore
    public boolean isValidStatus(){
        return status.contains("online") || status.contains("offline");
    }

    @JsonIgnore
    public boolean isValidUid(){
        Pattern pattern = Pattern.compile("^[\\d]+$");
        return pattern.matcher(uid).matches();
    }

}
