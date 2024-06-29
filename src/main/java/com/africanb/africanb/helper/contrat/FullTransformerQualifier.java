package com.africanb.africanb.helper.contrat;

import org.mapstruct.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Qualifier
@Target(ElementType.METHOD)
public @interface FullTransformerQualifier {

}