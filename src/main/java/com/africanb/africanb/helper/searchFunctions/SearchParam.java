package com.africanb.africanb.helper.searchFunctions;


import java.util.List;
public class SearchParam<T> {

    String	operator;
    T		start;
    T		end;
    List<T> datas;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public T getStart() {
        return start;
    }

    public void setStart(T start) {
        this.start = start;
    }

    public T getEnd() {
        return end;
    }

    public void setEnd(T end) {
        this.end = end;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    @Override
    public String toString() {
        return "SearchParam{" +
                "operator='" + operator + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", datas=" + datas +
                '}';
    }

    public SearchParam(String operator, T start, T end, List<T> datas) {
        super();
        this.operator = operator;
        this.start = start;
        this.end = end;
        this.datas = datas;
    }

    public SearchParam(String operator) {
        this(operator, null, null, null);
    }

    public SearchParam(String operator, T start, T end) {
        this(operator, start, end, null);
    }

    public SearchParam(String operator, List<T> datas) {
        this(operator, null, null, datas);
    }
}
