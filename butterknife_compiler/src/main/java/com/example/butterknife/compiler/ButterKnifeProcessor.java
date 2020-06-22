package com.example.butterknife.compiler;


import com.example.butterknife.annotations.BindView;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

//加上这个才能
@AutoService(Processor.class)
public class ButterKnifeProcessor extends AbstractProcessor {

    //生成的文件
    private Filer mFiler;
    private Messager mMessager;
    private Elements mElementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        mMessager = processingEnv.getMessager();
        mElementUtils = processingEnvironment.getElementUtils();
    }

    //1.指定处理的版本
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    //2.指定需要处理的注解
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        //这里为我们annotation里面的BindView
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : getSupportAnnotations()) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }

    private Set<Class<? extends Annotation>> getSupportAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        //需要解析的自定义注解，如BindView OnClick
        annotations.add(BindView.class);
        return annotations;

    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        //每次编译都会走进这里？
        info("????????????????????????????????????");
        //process代表所有注解都会进来，其他Activity都会，这里需要把一个Activity对应的多个注解单独拿出来 activlty->List<Element>
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        for (Element element : elements) {
            //这个对应的是属性名，如TextView tvaaa,就是tvaaa
            String elementName = element.getSimpleName().toString();
            //对应Activity的名字，如MainActivity,不加simple则为全路径
            String enclosingElementName = element.getEnclosingElement().getSimpleName().toString();
            print("elementName->" + elementName + ",enclosingElementName->" + enclosingElementName);
        }

        //activlty->List<Element>
        Map<Element, List<Element>> elementMap = new LinkedHashMap<>();
        for (Element element : elements) {
            //获取对应Activity
            Element enclosingElement = element.getEnclosingElement();
            //获取对应的Element集合
            List<Element> viewBindElements = elementMap.get(enclosingElement);
            if (viewBindElements == null) {
                viewBindElements = new ArrayList<>();
                elementMap.put(enclosingElement, viewBindElements);
            }
            //添加
            viewBindElements.add(element);

        }

        //遍历集合，生成代码
        for (Map.Entry<Element, List<Element>> entry : elementMap.entrySet()) {
            Element enclosingElement = entry.getKey();
            List<Element> viewBindElements = entry.getValue();
            String activityName = enclosingElement.getSimpleName().toString();

            //使用google生成器 生成class名及修饰符 实现 Unbinder
            //public final class xxx_ViewBinding implement Unbinder
            //因为找不到这个class，所以要这样获取 android.support.annotation.CallSuper
            ClassName unbinder = ClassName.get("com.example.butterknife", "Unbinder");
            ClassName activityClassName = ClassName.bestGuess(activityName);
            TypeSpec.Builder classBuilder = TypeSpec.classBuilder(activityName + "_ViewBinding")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addSuperinterface(unbinder)
                    //变量类型 变量名 修饰符
                    .addField(activityClassName, "target", Modifier.PRIVATE);

            //构造函数
            MethodSpec.Builder constructorMethodBuilder = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(activityClassName, "target");
            constructorMethodBuilder.addStatement("this.target = target");

            //实现接口方法
            MethodSpec.Builder unbindMethodBuilder = MethodSpec.methodBuilder("unbind")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC);
            unbindMethodBuilder.addStatement("$T target = this.target",activityClassName);

            for (Element viewBindElement : viewBindElements) {
                String filedName = viewBindElement.getSimpleName().toString();
                ClassName utilsClassName = ClassName.get("com.example.butterknife", "Utils");
                int resId = viewBindElement.getAnnotation(BindView.class).value();
                constructorMethodBuilder.addStatement("target.$L = $T.findViewById(target,$L)",
                        filedName, utilsClassName, resId);

                unbindMethodBuilder.addStatement("target.$L = null",filedName);
            }
            classBuilder.addMethod(unbindMethodBuilder.build());

            classBuilder.addMethod(constructorMethodBuilder.build());

            try {
                //生成类文件并写入
                String packageName = mElementUtils.getPackageOf(enclosingElement).getQualifiedName().toString();
                JavaFile.builder(packageName, classBuilder.build())
                        .addFileComment("butterknife自动生成")
                        .build().writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private void print(String tag) {
        System.out.println(tag);
    }

    private void info(String msg, Object... args) {
        mMessager.printMessage(
                Diagnostic.Kind.NOTE,
                String.format(msg, args));
    }
}
