package org.example.attributeconverter18052024.utils;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Data
@Accessors(chain = true)
public class BaseResponse<T> {

    private T payload;
    private String message;
    private Integer status;
    private Object metadata;

    public static  <T>BaseResponse<T> ok(){

        return   new BaseResponse<T>().
                setStatus(HttpStatus.OK.value()).
                setMessage("Get data success");

    }

    public static  <T>BaseResponse<T> successCreated() {

        return new BaseResponse<T>().
                setMessage("Data created successfully").
                setStatus(HttpStatus.CREATED.value());

    }

    public static  <T>BaseResponse<T> successUpdated() {

        return new BaseResponse<T>().
                setStatus(HttpStatus.CREATED.value()).
                setMessage("Data updated successfully");

    }

    public static  <T>BaseResponse<T> successDeleted() {

        return new BaseResponse<T>().
                setStatus(HttpStatus.OK.value()).
                setMessage("Data deleted successfully");

    }

    public static  <T>BaseResponse<T> badRequest() {

        return new BaseResponse<T>().
                setMessage("Bad Request").
                setStatus(HttpStatus.BAD_REQUEST.value());

    }

    public static  <T>BaseResponse<T> notFound() {

        return new BaseResponse<T>().
                setMessage("Data not found").
                setStatus(HttpStatus.NOT_FOUND.value());

    }
}
