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

  public final static Integer STATUS_OK() { // 200

    return HttpStatus.OK.value();
  }

  public final static Integer STATUS_NO_CONTENT() { // 200

    return HttpStatus.NO_CONTENT.value();
  }

  public final static Integer STATUS_NOT_FOUND() { // 404

    return HttpStatus.NOT_FOUND.value();
  }

  public final static Integer STATUS_CONFLICT() { // 409

    return HttpStatus.CONFLICT.value();
  }

  public final static Integer STATUS_UNPROCESSABLE() { // 422

    return HttpStatus.UNPROCESSABLE_ENTITY.value();
  }
}
