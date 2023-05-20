package com.africanb.africanb.helper.contrat;

import org.mapstruct.Qualifier;
//import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Qualifier
@Target(ElementType.METHOD)
public @interface FullTransformerQualifier {

}