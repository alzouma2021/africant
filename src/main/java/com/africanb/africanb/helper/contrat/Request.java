package com.africanb.africanb.helper.contrat;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


@Data
@ToString
@NoArgsConstructor
@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Request<T> extends RequestBase{
    private T data;
    private List<T> datas;
}
