package com.vincent.mutualan.mutualankuy.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRelationship {

    @Id
    @SequenceGenerator(
            name = "account_relationship_sequence",
            sequenceName = "account_relationship_sequence",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "account_relationship_sequence")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower")
    @NotNull
    @JsonIgnore
    private Account follower;

    @ManyToOne
    @JoinColumn(name = "followed")
    @NotNull
    @JsonIgnore
    private Account followed;

    private Date createdAt = new Date();

    private Date updatedAt = new Date();
}
