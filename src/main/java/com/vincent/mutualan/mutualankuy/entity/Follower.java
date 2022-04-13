package com.vincent.mutualan.mutualankuy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Follower {

    @Id
    @SequenceGenerator(
            name = "follower_sequence",
            sequenceName = "follower_sequence",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "follower_sequence")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id")
    @Nullable
    private Account from_user_id;

    @ManyToOne
    @JoinColumn(name = "id")
    @Nullable
    private Account to_user_id;

    private Date createdAt = new Date();

    private Date updatedAt = new Date();
}
