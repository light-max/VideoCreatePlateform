package com.lifengqiang.video.fieldcheck;

/**
 * 在编译期对标注了{@link FieldCheck}的类进行接口的添加<br>
 * 添加的接口为{@link FieldCheckInterface2}
 */
//@SupportedSourceVersion(SourceVersion.RELEASE_8)
//@SupportedAnnotationTypes("*")
//@AutoService(Processor.class)
//public class FieldCheckProcessor extends AbstractProcessor {
//    @Override
//    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        Trees trees = Trees.instance(processingEnv);
//        Messager messager = processingEnv.getMessager();
//        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
//        TreeMaker maker = TreeMaker.instance(context);
//        Names names = Names.instance(context);
//        JCTree.JCIdent classPackage = maker.Ident(names.fromString(FieldCheckInterface2.class.getPackage().getName()));
//        JCTree.JCIdent className = maker.Ident(names.fromString(FieldCheckInterface2.class.getSimpleName()));
//        JCTree.JCImport jcImport = maker.Import(maker.Select(classPackage, className.name), false);
//        for (Element e : roundEnv.getElementsAnnotatedWith(FieldCheck.class)) {
//            // 获取语法树
//            TreePath path = trees.getPath(e);
//            JCTree.JCCompilationUnit unit = (JCTree.JCCompilationUnit) path.getCompilationUnit();
//            JCTree.JCClassDecl decl = (JCTree.JCClassDecl) trees.getTree((TypeElement) e);
//
//            // 判断是否已经继承FieldCheckInterface接口
//            if (isImplementation(decl)) {
//                messager.printMessage(
//                        Diagnostic.Kind.WARNING,
//                        String.format("继承了 %s 接口, 还标注了 %s 注解", FieldCheckInterface.class.getName(), FieldCheck.class.getName()),
//                        e
//                );
//                continue;
//            }
//
//            // 导包
//            ArrayList<JCTree> list = new ArrayList<>(unit.defs);
//            list.add(1, jcImport);
//            unit.defs = List.from(list);
//
//            // 继承接口
//            decl.implementing = decl.implementing.prepend(className);
//        }
//        return false;
//    }
//
//    private final String fieldCheckInterface = FieldCheckInterface.class.getSimpleName();
//
//    /**
//     * 判断类是否已经继承{@link FieldCheckInterface}
//     */
//    private boolean isImplementation(JCTree.JCClassDecl decl) {
//        for (JCTree.JCExpression expression : decl.implementing) {
//            String className = expression.type.tsym.name.toString();
//            if (fieldCheckInterface.equals(className)) {
//                return true;
//            }
//        }
//        return false;
//    }
//}
