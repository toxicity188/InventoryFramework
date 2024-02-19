package kr.toxicity.inventory.equation

import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import kotlin.math.E
import kotlin.math.PI

class TEquation(pattern: String) {
    private val equation = ExpressionBuilder(pattern)
        .variables(
            "t",
            "pi",
            "e"
        )
        .build()

    fun evaluate(t: Double) = Expression(equation)
        .setVariables(mapOf(
            "t" to t,
            "pi" to PI,
            "e" to E
        ))
        .evaluate()
}