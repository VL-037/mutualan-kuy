package com.vincent.mutualan.mutualankuy.helper.response;

import com.vincent.mutualan.mutualankuy.model.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ResponseHelper {

  public static <T> BaseResponse<T> getBaseResponse(T data, Integer statusCode) {

    return BaseResponse.<T>builder()
            .status(statusCode)
            .data(data)
            .build();
  }

  public final static Integer STATUS_OK() {

    return HttpStatus.OK.value();
  }
}
