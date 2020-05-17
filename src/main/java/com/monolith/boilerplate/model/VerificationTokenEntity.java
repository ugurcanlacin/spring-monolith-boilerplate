package com.monolith.boilerplate.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "VERIFICATION_TOKEN")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"userEntity"})
@EqualsAndHashCode(exclude = "userEntity")
public class VerificationTokenEntity extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR(50)")
    private String id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private Boolean verified;

    @ManyToOne
    UserEntity userEntity;
}
