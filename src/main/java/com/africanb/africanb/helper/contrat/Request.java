package com.africanb.africanb.helper.contrat;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;


@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Request<T> extends RequestBase{
    private T data;
    private List<T> datas;
}
