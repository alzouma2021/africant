package com.africanb.africanb.helper.contrat;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class RequestBase {
    protected String sessionUser;
    protected Integer size;
    protected Integer index;
    protected String lang;
    protected String businessLineCode;
    protected String caseEngine;
    protected Boolean isAnd;
    public static Integer userID;
    protected String compagnieRaisonSociale;
    protected Boolean isSimpleLoading;
    protected String[] datasElasticSearchIndices;
}
