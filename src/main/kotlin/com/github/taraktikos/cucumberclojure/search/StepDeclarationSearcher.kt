package com.github.taraktikos.cucumberclojure.search

import com.github.taraktikos.cucumberclojure.StepDeclaration
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressManager
import com.intellij.pom.PomDeclarationSearcher
import com.intellij.pom.PomTarget
import com.intellij.psi.PsiElement
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.util.Consumer
import cursive.psi.api.ClList
import cursive.psi.api.ClLiteral

/**
 * @author Taras S.
 */
class StepDeclarationSearcher : PomDeclarationSearcher() {

    fun <T> inReadAction(body: () -> T): T {
        return ApplicationManager.getApplication().run {
            if (isReadAccessAllowed) {
                body()
            } else runReadAction<T>(body)
        }
    }

    override fun findDeclarationsAt(element: PsiElement, offsetInElement: Int, consumer: Consumer<PomTarget>) {
        val injectionHost = InjectedLanguageManager.getInstance(element.project).getInjectionHost(element) ?: element

        ProgressManager.checkCanceled()

        val stepDeclaration = inReadAction {
            (injectionHost.parent as? ClList)?.let { candidate ->
                (element as? ClLiteral)?.let { elem ->
                    val keyword = candidate.children[0].text
                    if (listOf("Given", "When", "Then", "And", "But").contains(keyword)) {
                        val stepName = elem.text.replace("\"", "")
                        getStepDeclaration(candidate.firstChild, "$keyword $stepName")
                    } else {
                        null
                    }
                }
            }
        }

        stepDeclaration?.let {
            consumer.consume(it)
        }
    }

    private fun getStepDeclaration(element: PsiElement, stepName: String): StepDeclaration? {
        return CachedValuesManager.getCachedValue(element) {
            CachedValueProvider.Result.create(StepDeclaration(element, stepName), element)
        }
    }
}
