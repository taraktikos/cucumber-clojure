package com.github.taraktikos.cucumberclojure

import com.intellij.ide.util.EditSourceUtil
import com.intellij.pom.PomNamedTarget
import com.intellij.psi.PsiElement

/**
 * @author Taras S.
 */
class StepDeclaration(val element: PsiElement, val stepName: String) : PomNamedTarget {
    override fun navigate(requestFocus: Boolean) {
        EditSourceUtil.getDescriptor(element)?.navigate(requestFocus)
    }

    override fun canNavigate(): Boolean {
        return EditSourceUtil.canNavigate(element)
    }

    override fun canNavigateToSource(): Boolean {
        return canNavigate()
    }

    override fun isValid(): Boolean = element.isValid

    override fun getName(): String {
        return stepName
    }

}
