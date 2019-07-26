package com.zptioning.lib_apt;

import com.google.auto.service.AutoService;
import com.zptioning.lib_annotation.AppLifeCycle;

import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public class AppLikeProcessor extends AbstractProcessor {

    private Elements mElementUtils;
    private Map<String, AppLikeProxyClassCreator> mMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mElementUtils = processingEnvironment.getElementUtils();
    }

    /**
     * 支持解析的注解
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        set.add(AppLifeCycle.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(AppLifeCycle.class);
        mMap.clear();
        for (Element element : elements) {
            if (!element.getKind().isClass()) {
                throw new RuntimeException("Annotation AppLifeCycle can only be used in class.");
            }
            TypeElement typeElement = (TypeElement) element;

            //这里检查一下，使用了该注解的类，同时必须要实现com.zptioning.lib_api.IAppLike接口，否则会报错，因为我们要实现一个代理类
            List<? extends TypeMirror> mirrorList = typeElement.getInterfaces();
            if (mirrorList.isEmpty()) {
                throw new RuntimeException(typeElement.getQualifiedName() + " must implements interface com.zptioning.lib_api.IAppLike");
            }
            boolean checkInterfaceFlag = false;
            for (TypeMirror mirror : mirrorList) {
                if ("com.zptioning.lib_api.IAppLike".equals(mirror.toString())) {
                    checkInterfaceFlag = true;
                }
            }
            if (!checkInterfaceFlag) {
                throw new RuntimeException(typeElement.getQualifiedName() + " must implements interface com.zptioning.lib_api.IAppLike");
            }

            String fullClassName = typeElement.getQualifiedName().toString();
            if (!mMap.containsKey(fullClassName)) {
                System.out.println("process class name : " + fullClassName);
                AppLikeProxyClassCreator creator = new AppLikeProxyClassCreator(mElementUtils, typeElement);
                mMap.put(fullClassName, creator);
            }
        }

        System.out.println("start to generate proxy class code");
        for (Map.Entry<String, AppLikeProxyClassCreator> entry : mMap.entrySet()) {
            String className = entry.getKey();
            AppLikeProxyClassCreator creator = entry.getValue();
            System.out.println("generate proxy class for " + className);

            try {
                JavaFileObject jfo = processingEnv.getFiler().createSourceFile(creator.getProxyClassFullName());
                Writer writer = jfo.openWriter();
                writer.write(creator.generateJavaCode());
                writer.flush();
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }
}
