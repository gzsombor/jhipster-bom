package tech.jhipster.apt;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;

public class DtoMapping implements JavaOutput {

    private final JavaOutput output;
    private final String sourcePackage;
    private final String dtoPackage;
    private final String suffix;

    public DtoMapping(JavaOutput output, String sourcePackage, String dtoPackage, String suffix) {
        this.output = output;
        this.sourcePackage = sourcePackage;
        this.dtoPackage = dtoPackage;
        this.suffix = suffix;
    }

    @Override
    public void addField(String fieldName, TypeMirror type) {

    }

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        output.init(processingEnv);
    }

    @Override
    public ClassCreator build() {
        return output.build();
    }

}
