package com.github.taraktikos.cucumberclojure.steps

import com.intellij.psi.PsiElement
import cursive.psi.api.ClList
import org.jetbrains.plugins.cucumber.steps.AbstractStepDefinition

/**
 * @author Taras S.
 */
class StepDefinition(val clList: ClList) : AbstractStepDefinition(clList.firstChild) {
    val integerRegexp = "(-?\\d+)"
    val floatRegexp = "([+-]?(\\d+.)?\\d+)"

    val paramRegexp = mapOf(
        "{int}" to integerRegexp,
        "{bigdecimal}" to floatRegexp,
        "{float}" to floatRegexp,
        "{double}" to floatRegexp
    )

    override fun getVariableNames(): List<String> {
        return listOf("a", "b")
    }

    override fun getCucumberRegexFromElement(element: PsiElement): String {
        val preparedRegexp = replaceParametersWithRegex(clList.children[1].text)
        return preparedRegexp
    }

    fun replaceParametersWithRegex(step: String): String {
        var replaced = step.replace("\"", "")
        paramRegexp.forEach { (key, replacement) -> replaced = replaced.replace(key, replacement) }
        return replaced
    }
}
