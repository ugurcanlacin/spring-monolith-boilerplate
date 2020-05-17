package com.monolith.boilerplate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "COMMUNICATION_LOG")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommunicationLogEntity extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR(50)")
    private String id;

    private String communicationType;

    private String messageType;

    private String receiverId;

    private String message;

}
