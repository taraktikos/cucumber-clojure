package com.github.taraktikos.cucumberclojure.search

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.openapi.editor.markup.GutterIconRenderer.Alignment.RIGHT
import com.intellij.psi.PsiElement
import cursive.psi.api.ClList
import icons.CucumberIcons.Cucumber

/**
 * @author Taras S.
 */
class CucumberLineMarkerProvider : LineMarkerProvider {
    private val keywords = listOf("Given", "When", "Then", "And", "But") // TODO: fix it
    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<PsiElement>? {
        return (element as? ClList)?.let {
            if (element.children.size > 3 && keywords.contains(element.children[0].text)) {
                val stepName = element.children[1].text
                LineMarkerInfo(element, element.textRange, Cucumber, { stepName }, null, RIGHT, { stepName })
            } else {
                null
            }
        }
    }
}
