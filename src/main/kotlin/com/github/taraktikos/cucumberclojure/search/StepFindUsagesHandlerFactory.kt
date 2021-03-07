package com.github.taraktikos.cucumberclojure.search

import com.github.taraktikos.cucumberclojure.StepDeclaration
import com.intellij.find.findUsages.FindUsagesHandler
import com.intellij.find.findUsages.FindUsagesHandlerFactory
import com.intellij.pom.PomTargetPsiElement
import com.intellij.psi.PsiElement

/**
 * @author Taras S.
 */
class StepFindUsagesHandlerFactory : FindUsagesHandlerFactory() {
    override fun canFindUsages(element: PsiElement): Boolean {
        if (element is PomTargetPsiElement) {
            if (element.target is StepDeclaration) {
                return true
            }
        }
        return false
    }

    override fun createFindUsagesHandler(element: PsiElement, forHighlightUsages: Boolean): FindUsagesHandler {
        return object : FindUsagesHandler(element) {}
    }
}
