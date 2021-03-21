package com.github.taraktikos.cucumberclojure.search

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.search.TextOccurenceProcessor
import com.intellij.util.Processor


//        val proc = TestProcessor(declaration.element, consumer)
//        val instance = PsiSearchHelper.getInstance(declaration.element.project)
//        val word = CucumberUtil.getTheBiggestWordToSearchByIndex(declaration.stepName)
//        val context = (UsageSearchContext.IN_STRINGS or UsageSearchContext.IN_CODE)
//        instance.processElementsWithWord(proc, queryParameters.effectiveSearchScope, word, context, true)

class TestProcessor(
    private val myElementToFind: PsiElement,
    private val myConsumer: Processor<in PsiReference>
) : TextOccurenceProcessor {
    override fun execute(element: PsiElement, offsetInElement: Int): Boolean {
        val parent = element.parent
        val result = executeInternal(element)
        // We check element and its parent (StringLiteral is probably child of GherkinStep that has reference)
        // TODO: Search for GherkinStep parent?
        return if (result && parent != null) {
            executeInternal(parent)
        } else result
    }

    /**
     * Gets all injected reference and checks if some of them points to [.myElementToFind]
     *
     * @param referenceOwner element with injected references
     * @return true if element found and consumed
     */
    private fun executeInternal(referenceOwner: PsiElement): Boolean {
        for (ref in referenceOwner.references) {
            if (ref != null && ref.isReferenceTo(myElementToFind)) {
                if (!myConsumer.process(ref)) {
                    return false
                }
            }
        }
        return true
    }
}