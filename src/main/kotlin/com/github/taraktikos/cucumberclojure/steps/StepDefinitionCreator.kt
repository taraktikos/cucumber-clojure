package com.github.taraktikos.cucumberclojure.steps

import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import org.jetbrains.plugins.cucumber.AbstractStepDefinitionCreator
import org.jetbrains.plugins.cucumber.psi.GherkinStep

/**
 * @author Taras S.
 */
class StepDefinitionCreator : AbstractStepDefinitionCreator() {
    override fun createStepDefinitionContainer(directory: PsiDirectory, name: String): PsiFile {
        TODO("Not yet implemented")
    }

    override fun getDefaultStepFileName(p0: GherkinStep): String {
        TODO("Not yet implemented")
    }
}
