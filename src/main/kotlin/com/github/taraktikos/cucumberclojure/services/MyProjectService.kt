package com.github.taraktikos.cucumberclojure.services

import com.github.taraktikos.cucumberclojure.MyBundle
import com.intellij.openapi.project.Project

/**
 * @author Taras S.
 */
class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
