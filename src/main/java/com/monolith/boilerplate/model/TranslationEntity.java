package com.monolith.boilerplate.model;

import lombok.*;
import org.apache.tomcat.jni.Local;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "TRANSLATION")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"textEntity"})
@EqualsAndHashCode(exclude = "textEntity")
public class TranslationEntity extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR(50)")
    private String id;
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "localeId")
    LocaleEntity locale;

    @ManyToOne
    TextEntity textEntity;
}
