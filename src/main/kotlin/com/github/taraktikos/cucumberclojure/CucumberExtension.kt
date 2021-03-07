package com.github.taraktikos.cucumberclojure

import com.github.taraktikos.cucumberclojure.steps.StepDefinition
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.progress.ProgressManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.ProjectScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.indexing.FileBasedIndex
import cursive.file.ClojureFileType
import cursive.psi.api.ClList
import cursive.psi.api.ClojureFile
import org.jetbrains.plugins.cucumber.BDDFrameworkType
import org.jetbrains.plugins.cucumber.StepDefinitionCreator
import org.jetbrains.plugins.cucumber.psi.GherkinFile
import org.jetbrains.plugins.cucumber.steps.AbstractCucumberExtension
import org.jetbrains.plugins.cucumber.steps.AbstractStepDefinition
import com.github.taraktikos.cucumberclojure.steps.StepDefinitionCreator as ClojureStepDefinitionCreator

/**
 * @author Taras S.
 */
class CucumberExtension : AbstractCucumberExtension() {
    override fun isStepLikeFile(child: PsiElement, parent: PsiElement): Boolean {
        return child is ClojureFile
    }

    override fun isWritableStepLikeFile(child: PsiElement, parent: PsiElement): Boolean {
        return (child as? ClojureFile)?.containingFile?.virtualFile?.isWritable ?: false
    }

    override fun getStepFileType(): BDDFrameworkType {
        return BDDFrameworkType(ClojureFileType.INSTANCE)
    }

    override fun getStepDefinitionCreator(): StepDefinitionCreator {
        return ClojureStepDefinitionCreator()
    }

    override fun loadStepsFor(featureFile: PsiFile?, module: Module): List<AbstractStepDefinition> {
        val fileBasedIndex = FileBasedIndex.getInstance()
        val project = module.project
        val scope = module
            .getModuleWithDependenciesAndLibrariesScope(true)
            .uniteWith(ProjectScope.getLibrariesScope(project))

        var result = mutableListOf<AbstractStepDefinition>()

        fileBasedIndex.processValues(CucumberStepIndex.INDEX_ID, true, null, { file, value ->
            ProgressManager.checkCanceled()
            val psiFile = PsiManager.getInstance(project).findFile(file)
            if (psiFile == null) {
                true
            } else {
                for (offset in value) {
                    val element = psiFile.findElementAt(offset + 1)
                    val stepDefPsi = PsiTreeUtil.getParentOfType(element, ClList::class.java)
                    stepDefPsi?.let {
                        result.add(StepDefinition(stepDefPsi))
                    }
                }
                true
            }
        }, scope)

        return result
    }

    override fun getStepDefinitionContainers(featureFile: GherkinFile): Collection<PsiFile> {
        val module = ModuleUtilCore.findModuleForPsiElement(featureFile)
        val steps = module?.let {
            loadStepsFor(featureFile, it)
        }
        val psiFiles = steps
            ?.map { it.element?.containingFile }
            ?.filter { isWritableStepLikeFile(it!!, it.parent!!) }
            ?.filterNotNull()
            ?: emptyList()
        return psiFiles
    }
}
