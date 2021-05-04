package tech.jhipster.apt;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementKindVisitor9;
import javax.lang.model.util.ElementScanner9;
import javax.tools.Diagnostic.Kind;

import com.google.auto.service.AutoService;

@SupportedAnnotationTypes({ "tech.jhipster.annotations.GenerateClass", "tech.jhipster.annotations.GenerateClasses" })
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class ClassGenerator extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Messager messager = processingEnv.getMessager();
        for (TypeElement annotation : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                messager.printMessage(Kind.WARNING, "Found annotation : " + annotation.getQualifiedName() + ", annotatedElements:" + element, element);
                GenerateClassesGem classListGem = GenerateClassesGem.instanceOn(element);
                TypeElement type = asTypeElement(element);
                if (classListGem != null) {
                    for (GenerateClassGem gem : classListGem.value().get()) {
                        writeClass(type, gem);
                    }
                } else {
                    GenerateClassGem gem = GenerateClassGem.instanceOn(element);
                    if (gem != null) {
                        writeClass(type, gem);
                    }

                }
            }
            // …
        }

        return false;
    }

    private static TypeElement asTypeElement(Element element) {
        return element.accept(new ElementKindVisitor9<TypeElement, Void>() {
            @Override
            public TypeElement visitTypeAsInterface(TypeElement e, Void p) {
                return e;
            }

            @Override
            public TypeElement visitTypeAsClass(TypeElement e, Void p) {
                return e;
            }

        }, null);
    }

    private void writeClass(TypeElement element, GenerateClassGem gem) {
        JavaOutput output = createOutput(element, gem);
        output.init(processingEnv);
        ClassCreator creator = generateClass(element, output);
        try {
            creator.write(processingEnv.getFiler());
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Kind.ERROR, "IO error: " + e.getMessage(), element);
            e.printStackTrace();
        }
    }

    private JavaOutput createOutput(TypeElement element, GenerateClassGem gem) {
        String packageName = gem.packageName().hasValue() ? gem.packageName().get() : getPackageName(element);
        processingEnv.getMessager().printMessage(Kind.WARNING,
                "-> " + gem.name().get() + " in " + gem.packageName().get() + " hasExtraValue:" + gem.extraFields().hasValue() + " -> pack " + packageName);
        final ClassCreator classCreator = new ClassCreator(packageName, gem.name().get());
        return new JavaBeanBuilder(classCreator);
    }

    private ClassCreator generateClass(TypeElement element, JavaOutput output) {
        final Messager messager = processingEnv.getMessager();

        new ElementScanner9<Element, JavaOutput>() {
            public Element visitVariable(VariableElement e, JavaOutput creator) {
                messager.printMessage(Kind.WARNING, "variable: " + e.getSimpleName() + " (" + e.getModifiers() + ") " + e.asType());
                creator.addField(e.getSimpleName().toString(), e.asType());
                return super.visitVariable(e, creator);
            }

            public Element visitExecutable(ExecutableElement e, JavaOutput creator) {
                messager.printMessage(Kind.WARNING, "method: " + e.getSimpleName() + " (" + e.getModifiers() + ") " + e.asType() + " kind:" + e.getKind());
                if (e.getKind() == ElementKind.METHOD && e.getParameters().isEmpty()) {
                    String methodName = e.getSimpleName().toString();
                    if (methodName.startsWith("get")) {
                        creator.addField(methodName.substring(3), e.getReturnType());
                    }
                }
                return super.visitExecutable(e, creator);
            };
        }.scan(element, output);
        return output.build();
    }

    private static String getPackageName(TypeElement element) {
        String qn = element.getQualifiedName().toString();
        int lastDot = qn.lastIndexOf('.');
        if (lastDot >= 0) {
            return qn.substring(0, lastDot);
        }
        return qn;
    }

}
