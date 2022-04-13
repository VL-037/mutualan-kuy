package com.vincent.mutualan.mutualankuy.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

  @Id
  @SequenceGenerator(
      name = "account_sequence",
      sequenceName = "account_sequence",
      allocationSize = 1)
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "account_sequence")
  private Long id;

  @NotBlank
  private String firstName;

  @Nullable
  private String middleName;

  @NotBlank
  private String lastName;

  @NotNull
  @Past
  private LocalDate birthDate;

  @Transient // won't be a column
  private Integer age;

  @NotBlank
  private String username;

  @Nullable
  private String bio;

  // private String email;
  //
  // private String password;

  private Boolean isVerified = false;

  @OneToMany(mappedBy = "follower")
  private List<Follower> followers = new ArrayList<>();

  private Date createdAt = new Date();

  private Date updatedAt = new Date();

  public Integer getAge() {

    return Period.between(this.birthDate, LocalDate.now())
        .getYears();
  }

  public String getFullName() {

    return (this.firstName + " " + this.middleName + " " + this.lastName).trim();
  }
}
