package tech.jhipster.apt;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;

public interface JavaOutput {
    void init(ProcessingEnvironment processingEnv);
    void addField(String fieldName, TypeMirror type);
    ClassCreator build();
}
