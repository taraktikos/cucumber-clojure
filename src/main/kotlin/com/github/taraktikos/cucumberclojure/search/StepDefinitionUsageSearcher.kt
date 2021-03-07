package com.github.taraktikos.cucumberclojure.search

import com.github.taraktikos.cucumberclojure.StepDeclaration
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.openapi.util.Computable
import com.intellij.pom.PomTargetPsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.search.searches.ReferencesSearch.SearchParameters
import com.intellij.util.Processor
import org.jetbrains.plugins.cucumber.CucumberUtil

/**
 * @author Taras S.
 */
class StepDefinitionUsageSearcher : QueryExecutorBase<PsiReference, SearchParameters>() {
    override fun processQuery(queryParameters: SearchParameters, consumer: Processor<in PsiReference>) {
        ApplicationManager.getApplication().runReadAction(Computable {
            val elementToSearch = queryParameters.elementToSearch
            if (elementToSearch is PomTargetPsiElement) {
                val stepDeclaration = elementToSearch.target
                if (stepDeclaration is StepDeclaration) {
                    CucumberUtil.findGherkinReferencesToElement(
                        stepDeclaration.element,
                        stepDeclaration.stepName,
                        consumer,
                        queryParameters.effectiveSearchScope
                    )
                }
            }
        })
    }
}
