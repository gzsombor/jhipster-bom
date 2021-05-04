package tech.jhipster.test;

import tech.jhipster.annotations.GenerateClass;

@GenerateClass(name = "OtherDto", packageName = "tech.jhipster.test.dto")
public interface OtherEntityWithoutPackage {

    Long getId();
    
    String getName();
}
