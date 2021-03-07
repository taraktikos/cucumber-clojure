package com.github.taraktikos.cucumberclojure.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.IconLoader
import com.intellij.util.Consumer

/**
 * @author Taras S.
 */
class CucumberClojureAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val icon = IconLoader.getIcon("/resources/META-INF/pluginIcon.svg", CucumberClojureAction::class.java)
        Messages.showMessageDialog(e.project, "Cucumber Clojure plugin version 2020.3.2.0", "Cucumber Clojure", icon)
    }

    private fun showFileDialog(e: AnActionEvent) {
        val fileChooserDescriptor = FileChooserDescriptor(false, true, false, false, false, false)
        fileChooserDescriptor.title = "Demo Pick Directory"
        fileChooserDescriptor.description = "My chooser"
        FileChooser.chooseFile(fileChooserDescriptor, e.project, null, Consumer {
            Messages.showMessageDialog(e.project, it.path, "Path", Messages.getInformationIcon())
        })
    }
}
