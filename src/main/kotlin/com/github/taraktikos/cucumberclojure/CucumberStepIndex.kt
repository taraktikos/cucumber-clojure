package com.github.taraktikos.cucumberclojure

import com.intellij.lang.LighterAST
import com.intellij.lang.LighterASTNode
import com.intellij.psi.impl.source.tree.RecursiveLighterASTNodeWalkingVisitor
import com.intellij.util.indexing.DefaultFileTypeSpecificInputFilter
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.ID
import cursive.file.ClojureFileType
import cursive.parser.ClojureElementTypes.*
import org.jetbrains.plugins.cucumber.CucumberStepIndex

/**
 * @author Taras S.
 */
class CucumberStepIndex : CucumberStepIndex() {
    companion object {
        val INDEX_ID = ID.create<Boolean, List<Int>>("clojure.cucumber.step")
        val PACKAGES = arrayOf("lambdaisland.cucumber.")
    }

    override fun getName(): ID<Boolean, List<Int>> {
        return INDEX_ID
    }

    override fun getVersion(): Int {
        return 1
    }

    override fun getInputFilter(): FileBasedIndex.InputFilter {
        return DefaultFileTypeSpecificInputFilter(ClojureFileType.INSTANCE)
    }

    override fun getPackagesToScan(): Array<String> {
        return PACKAGES
    }

    /*
    (Given "some expression" [state param]) -> ClojureElementTypes.LIST

    [0] ( -> ClojureElementTypes.LEFT_PAREN
    [1] Given -> ClojureElementTypes.SYMBOL
    [2] " " -> ClojureElementTypes.WHITESPACE
    [3] ""some expression"" -> ClojureElementTypes.LITERAL
    [4] " " -> ClojureElementTypes.WHITESPACE
    [5] [state param] -> ClojureElementTypes.VECTOR
     */
    override fun getStepDefinitionOffsets(lighterAst: LighterAST, text: CharSequence): List<Int> {
        var result = mutableListOf<Int>()

        val visitor: RecursiveLighterASTNodeWalkingVisitor =
            object : RecursiveLighterASTNodeWalkingVisitor(lighterAst) {
                override fun visitNode(element: LighterASTNode) {
                    if (element.tokenType == LIST) {
                        val children = lighterAst.getChildren(element)

                        if (children.size > 5) {
                            if (children[1].tokenType == SYMBOL &&
                                children[3].tokenType == LITERAL &&
                                children[5].tokenType == VECTOR &&
                                isStepDefinitionCall(children[1], text)
                            ) {
                                result.add(element.startOffset)
                            }
                        }
                    }
                    super.visitNode(element)
                }
            }

        visitor.visitNode(lighterAst.root)

        return result
    }
}
