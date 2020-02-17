package com.monolith.boilerplate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "VERIFICATION_TOKEN")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationToken implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR(50)")
    private String id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column
    private Date expiresAt;

    @Column
    private Boolean isVerified = false;

    @ManyToOne
    private User user;
}
