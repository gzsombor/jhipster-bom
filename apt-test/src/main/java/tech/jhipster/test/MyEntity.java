package tech.jhipster.test;

import java.beans.JavaBean;

import tech.jhipster.annotations.Field;
import tech.jhipster.annotations.GenerateClass;

@GenerateClass(name = "MyEntityDto", packageName = "tech.jhipster.test.dto", extraFields = { @Field(name = "some", type = long.class) })
@GenerateClass(name = "DtoInTheSamePackage", extraFields = { @Field(name = "notSome", type = X.class) })
@JavaBean
public class MyEntity {

    String name;

    Long longNumber;
    
    OtherEntityWithoutPackage otherStuff;

}

class X { }