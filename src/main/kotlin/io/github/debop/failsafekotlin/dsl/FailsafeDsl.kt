package io.github.debop.failsafekotlin.dsl

import io.github.debop.failsafekotlin.event.ExecutionCompletedEventHandler
import net.jodah.failsafe.CircuitBreaker
import net.jodah.failsafe.Failsafe
import net.jodah.failsafe.FailsafeExecutor
import net.jodah.failsafe.Fallback
import net.jodah.failsafe.Policy
import net.jodah.failsafe.RetryPolicy

/**
 * FailsafeDsl
 *
 * @author debop
 * @since 19. 3. 29
 */
@FailsafeBuilderDsl
class FailsafeDsl<R> : AbstractPolicyDsl<R>() {

    var fallback: Fallback<R>? = null
    var retry: RetryPolicy<R>? = null
    var circuitBreaker: CircuitBreaker<R>? = null

    private val onCompleteHandlers = mutableListOf<ExecutionCompletedEventHandler<R>>()
    private val onSuccessHandlers = mutableListOf<ExecutionCompletedEventHandler<R>>()
    private val onFailureHandlers = mutableListOf<ExecutionCompletedEventHandler<R>>()


    fun onComplete(handler: ExecutionCompletedEventHandler<R>): FailsafeDsl<R> =
        apply {
            onCompleteHandlers.add(handler)
        }

    fun onSuccess(handler: ExecutionCompletedEventHandler<R>): FailsafeDsl<R> =
        apply {
            onSuccessHandlers.add(handler)
        }

    fun onFailure(handler: ExecutionCompletedEventHandler<R>): FailsafeDsl<R> =
        apply {
            onFailureHandlers.add(handler)
        }

    internal fun build(): FailsafeExecutor<R> {

        val policies = mutableListOf<Policy<R>>()

        circuitBreaker?.let { policies.add(it) }
        retry?.let { policies.add(it) }
        fallback?.let { policies.add(it) }

        val failsafe = Failsafe.with(*policies.toTypedArray())

        onCompleteHandlers.forEach { failsafe.onComplete(it) }
        onSuccessHandlers.forEach { failsafe.onSuccess(it) }
        onFailureHandlers.forEach { failsafe.onFailure(it) }

        return failsafe
    }
}

fun <R> failsafe(setup: FailsafeDsl<R>.() -> Unit): FailsafeExecutor<R> {

    val dsl = FailsafeDsl<R>()
    setup.invoke(dsl)

    return dsl.build()
}






