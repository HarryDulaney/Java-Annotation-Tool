package org.harrydulaney.aptprocessor;

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
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import org.harrydulaney.log.DevLog;

	@SupportedAnnotationTypes(value = {"org.harrydulaney.ann.PublicFinal"})
	@SupportedSourceVersion(SourceVersion.RELEASE_6)

public class PublicFinalProcessor extends AbstractProcessor {
		
		private Filer filer;
		  private Messager messager;
		 
		  @Override
		  public void init(ProcessingEnvironment env) {
		      filer = env.getFiler();
		      messager = env.getMessager();
		  }
		 
		  @Override
		  public boolean process(Set<? extends TypeElement> annotations,
		          RoundEnvironment env) {
		      DevLog.log("\n\n");
		      DevLog.log(" ======================================================== ");
		      DevLog.log("#process(...) in " + this.getClass().getSimpleName());
		      DevLog.log(" ======================================================== ");
		 
		      DevLog.log(" annotations count = " + annotations.size());
		 
		      for (TypeElement ann : annotations) {
		 
		          Set<? extends Element> e2s = env.getElementsAnnotatedWith(ann);
		          for (Element e2 : e2s) {
		              DevLog.log("- e2 = " + e2);
		 
		              Set<Modifier> modifiers = e2.getModifiers();
		 
		              // @PublicFinal only using for public & final
		              // Notify if misuse
		              if (!(modifiers.contains(Modifier.FINAL) && modifiers
		                      .contains(Modifier.PUBLIC))) {
		                  DevLog.log("- Error!!!");
		                  messager.printMessage(Kind.ERROR,
		                          "Method/field wasn't public and final", e2);
		 
		              }
		          }
		      }
		 
		      // All PublicFinal annotations are handled by this Processor.
		      return true;
		  }
		

}
