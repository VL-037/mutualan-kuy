package com.vincent.mutualan.mutualankuy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> implements Serializable {

  private static final long serialVersionUID = 1618002060247672538L;

  private Integer status;

  private T data;

  private Map<String, List<String>> errors;

  @Override
  public String toString() {
    return "BaseResponse{" +
            "status=" + status +
            ", data=" + data +
            ", errors=" + errors +
            '}';
  }
}
