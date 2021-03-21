package com.github.taraktikos.cucumberclojure.search

import com.github.taraktikos.cucumberclojure.StepDeclaration
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.pom.PomTargetPsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.search.searches.ReferencesSearch.SearchParameters
import com.intellij.util.Processor
import org.jetbrains.plugins.cucumber.CucumberUtil

/**
 * @author Taras S.
 */
class StepDefinitionUsageSearcher : QueryExecutorBase<PsiReference, SearchParameters>() {
    fun <T> inReadAction(body: () -> T): T {
        return ApplicationManager.getApplication().run {
            if (isReadAccessAllowed) {
                body()
            } else runReadAction<T>(body)
        }
    }

    override fun processQuery(queryParameters: SearchParameters, consumer: Processor<in PsiReference>) {
        val elementToSearch = queryParameters.elementToSearch
        if (elementToSearch !is PomTargetPsiElement) return

        val declaration = elementToSearch.target
        if (declaration !is StepDeclaration) return

        inReadAction {
            CucumberUtil.findGherkinReferencesToElement(
                declaration.element,
                declaration.stepName,
                consumer,
                queryParameters.effectiveSearchScope
            )
        }
    }
}
