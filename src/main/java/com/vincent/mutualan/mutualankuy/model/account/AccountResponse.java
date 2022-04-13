package com.vincent.mutualan.mutualankuy.model.account;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse implements Serializable {

    private static final long serialVersionUID = 6659584132588460514L;
    private Long id;

    private String firstName;

    private String middleName;

    private String lastName;

    private LocalDate birthDate;

    private Integer age;

    private String username;

    private String bio;

    // private String email;
    //
    // private String password;
    //
    private Boolean isVerified;

    private Date createdAt;

    private Date updatedAt;

    @Override
    public String toString() {
        return "AccountResponse{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", age=" + age +
                ", username='" + username + '\'' +
                ", bio='" + bio + '\'' +
                ", isVerified=" + isVerified +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
