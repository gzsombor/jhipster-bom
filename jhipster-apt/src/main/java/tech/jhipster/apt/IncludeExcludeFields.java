package tech.jhipster.apt;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;

public class IncludeExcludeFields implements JavaOutput {

    final Set<String> excludeFields = new HashSet<>();
    final Map<String, TypeMirror> includeFields = new HashMap<>();
    final JavaOutput output;

    IncludeExcludeFields(JavaOutput output) {
        this.output = output;
    }

    public void addExclusion(String[] fields) {
        excludeFields.addAll(Arrays.asList(fields));
    }

    public void addExtra(String fieldName, TypeMirror type) {
        includeFields.put(fieldName, type);
    }

    @Override
    public void addField(String fieldName, TypeMirror type) {
        if (excludeFields.contains(fieldName)) {
            return;
        }
        output.addField(fieldName, type);
    }
    
    @Override
    public void init(ProcessingEnvironment processingEnv) {
        output.init(processingEnv);
    }

    @Override
    public ClassCreator build() {
        for (Map.Entry<String, TypeMirror> entry : includeFields.entrySet()) {
            output.addField(entry.getKey(), entry.getValue());
        }
        return output.build();
    }

}
