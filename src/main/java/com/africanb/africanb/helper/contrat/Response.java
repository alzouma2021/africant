package com.africanb.africanb.helper.contrat;

import com.fasterxml.jackson.annotation.JsonInclude;


import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;


@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> extends ResponseBase {

    protected List<T> items;
    protected T             		item;
    protected List<T> 				datas;
    protected Map<String, Object> itemsAsMap;

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    public Map<String, Object> getItemsAsMap() {
        return itemsAsMap;
    }

    public void setItemsAsMap(Map<String, Object> itemsAsMap) {
        this.itemsAsMap = itemsAsMap;
    }

    @Override
    public String toString() {
        return "Response{" +
                "itemsAsMap=" + itemsAsMap +
                ", status=" + status +
                ", hasError=" + hasError +
                ", sessionUser='" + sessionUser + '\'' +
                ", count=" + count +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
