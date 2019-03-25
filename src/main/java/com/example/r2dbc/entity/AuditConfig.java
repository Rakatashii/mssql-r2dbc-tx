package com.example.r2dbc.entity;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Table("audit.auditconfig")
public class AuditConfig {
    @Id
    //@Generated(value = { "GenerationType.IDENTITY" })
    @Column("configid") @NotNull
    private Integer auditConfigId;

    @Column("clubnbr") @NotNull
    private Integer clubNbr;

    @Column("configkey")
    @NotNull private String key;

    @Column("value") @NotNull
    private String value;

    @Column("valuetype") @NotNull
    private String valueType;
}
