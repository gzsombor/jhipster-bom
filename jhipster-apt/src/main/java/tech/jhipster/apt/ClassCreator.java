package tech.jhipster.apt;

import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

public class ClassCreator {
    final String packageName;
    final String className;
    final Builder typeSpec;

    public ClassCreator(String packageName, String className) {
        this.packageName = packageName;
        this.className = className;
        this.typeSpec = TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC);
    }

    public String getFullName() {
        return packageName + "." + className;
    }

    protected void addDefaultConstructor() {
        typeSpec.addMethod(
            MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .build());
    }

    protected void addField(String fieldName, TypeMirror type) {
        typeSpec.addField(TypeName.get(type), fieldName, Modifier.PRIVATE);
    }

    protected void addSetter(String fieldName, TypeMirror type) {
        TypeName fieldType = TypeName.get(type);
        typeSpec.addMethod(
            MethodSpec.methodBuilder(prefixAndUpperCase("set", fieldName))
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(fieldType, "value")
                .addStatement("this.$N = value", fieldName)
                .build());
    }

    protected void addFluentSetter(String fieldName, TypeMirror type) {
        TypeName fieldType = TypeName.get(type);
        typeSpec.addMethod(
            MethodSpec.methodBuilder(fieldName)
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(packageName, className))
                .addParameter(fieldType, "value")
                .addStatement("this.$N = value", fieldName)
                .addStatement("return this")
                .build());
    }

    protected void addGetter(String fieldName, TypeMirror type) {
        TypeName fieldType = TypeName.get(type);
        typeSpec.addMethod(
            MethodSpec.methodBuilder(prefixAndUpperCase("get", fieldName))
                .addModifiers(Modifier.PUBLIC)
                .returns(fieldType)
                .addStatement("return this.$N", fieldName)
                .build());
    }

    void write(Filer filer) throws IOException {
        JavaFile.builder(packageName, typeSpec.build())
            .build()
            .writeTo(filer);
    }

    private static String prefixAndUpperCase(String prefix, String fieldName) {
        return prefix + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
    }
}
