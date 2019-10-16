package org.harrydulaney.aptprocessor;

import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;

import org.harrydulaney.log.DevLog;

@SupportedAnnotationTypes({ "org.harrydulaney.ann.Action" })
@SupportedSourceVersion(SourceVersion.RELEASE_6)

public class ActionProcessor extends AbstractProcessor {

	private Filer filer;
	private Messager messager;

	@Override
	public void init(ProcessingEnvironment env) {
		filer = env.getFiler();
		messager = env.getMessager();

	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
		DevLog.log("/n/n");
		DevLog.log("========================================");
		DevLog.log("#process(...) in " + this.getClass().getSimpleName());

		for (TypeElement ann : annotations) {

			DevLog.log("==> TypeElement ann " + ann);

			List<? extends Element> es = ann.getEnclosedElements();
			DevLog.log("====> ann.getEnclosedElement() count = " + es.size());

			for (Element e : es) {
				DevLog.log("====> ann.getEnclosedElement: " + e);

			}
			Element enclosingElement = ann.getEnclosingElement();

			DevLog.log(" ====> ann.getEnclosingElement() = " + enclosingElement);

			ElementKind kind = ann.getKind();
			DevLog.log(" ====> ann.getKind() = " + kind);
			Set<? extends Element> e2s = env.getElementsAnnotatedWith(ann);

			DevLog.log(" ====> env.getElementsAnnotatedWith(ann) count = " + e2s.size());
			for (Element e2 : e2s) {
				DevLog.log(" ========> ElementsAnnotatedWith: " + e2);
				DevLog.log("           - Kind : " + e2.getKind());

				// @Action use for method only
				// notify if misuse
				if (e2.getKind() != ElementKind.METHOD) {
					DevLog.log("           - Error!!!");
					messager.printMessage(Kind.ERROR, "@Action using for method only ", e2);
				} else {

					// The name of the method is annotated by @Action
					String methodName = e2.getSimpleName().toString();

					// (ExecutableElement described for method, constructor,...)
					ExecutableElement method = (ExecutableElement) e2;

					DevLog.log("           - method : " + method);
					TypeMirror retType = method.getReturnType();
					DevLog.log("           -- method.getReturnType() : " + retType);

					// @Action Only used for method returns the String
					// Notify if misuse
					if (!String.class.getName().equals(retType.toString())) {
						DevLog.log("           - Error!!!");
						messager.printMessage(Kind.ERROR, "Method using @Action must return String", e2);
					}
				}
			}
		}
		return true;
	}

}