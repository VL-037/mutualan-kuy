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
import javax.validation.constraints.NotBlank;
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
public class Tweet {

  @Id
  @SequenceGenerator(
      name = "tweet_sequence",
      sequenceName = "tweet_sequence",
      allocationSize = 1)
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "tweet_sequence")
  private Long id;

  @NotBlank
  private String message;

  private Date createdAt = new Date();

  private Date updatedAt = new Date();

  @ManyToOne
  @JoinColumn(name = "creator")
  @NotNull
  @JsonIgnore
  private Account creator;
}
