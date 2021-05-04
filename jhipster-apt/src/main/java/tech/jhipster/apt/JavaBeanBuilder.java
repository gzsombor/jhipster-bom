package tech.jhipster.apt;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;

public class JavaBeanBuilder implements JavaOutput {
    private final ClassCreator creator;

    public JavaBeanBuilder(ClassCreator creator) {
        this.creator = creator;
    }

    @Override
    public void addField(String fieldName, TypeMirror type) {
        creator.addField(fieldName, type);
        creator.addGetter(fieldName, type);
        creator.addSetter(fieldName, type);
        creator.addFluentSetter(fieldName, type);
    }
    @Override
    public void init(ProcessingEnvironment processingEnv) {
    }

    @Override
    public ClassCreator build() {
        creator.addDefaultConstructor();
        return creator;
    }
}
