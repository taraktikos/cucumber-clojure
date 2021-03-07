package com.github.taraktikos.cucumberclojure.search

import com.github.taraktikos.cucumberclojure.StepDeclaration
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.util.Computable
import com.intellij.pom.PomDeclarationSearcher
import com.intellij.pom.PomTarget
import com.intellij.psi.PsiElement
import com.intellij.util.Consumer
import cursive.psi.api.ClList

/**
 * @author Taras S.
 */
class StepDeclarationSearcher : PomDeclarationSearcher() {
    override fun findDeclarationsAt(element: PsiElement, offsetInElement: Int, consumer: Consumer<PomTarget>) {
//        val injectionHost = InjectedLanguageManager.getInstance(element.project).getInjectionHost(element) ?: element

        ProgressManager.checkCanceled()

        val stepDeclaration = ApplicationManager.getApplication().runReadAction(
            Computable {
                (element as? ClList)?.let {
                    if (it.firstChild.text == "(" && it.firstChild.nextSibling.firstChild.text == "Given") {
                        val elem = it.firstChild.nextSibling.nextSibling.nextSibling.firstChild
                        val stepName = elem.text.replace("\"", "")
                        println(stepName)
                        StepDeclaration(elem, stepName)
                    } else {
                        null
                    }
                }
            }
        )
        if (stepDeclaration != null) {
            consumer.consume(stepDeclaration)
        }
    }
}
