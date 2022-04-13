package com.vincent.mutualan.mutualankuy.model.role;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse implements Serializable {

    private static final long serialVersionUID = 6659584132588460515L;
    private Long id;

    private String name;

    @Override
    public String toString() {
        return "RoleResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
