package io.github.debop.failsafekotlin.dsl

import net.jodah.failsafe.CircuitBreaker
import net.jodah.failsafe.Failsafe
import net.jodah.failsafe.FailsafeExecutor
import net.jodah.failsafe.Fallback
import net.jodah.failsafe.Policy
import net.jodah.failsafe.RetryPolicy
import net.jodah.failsafe.event.ExecutionCompletedEvent

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

    var onComplete: ((ExecutionCompletedEvent<R>) -> Unit)? = null
    var onSuccess: ((ExecutionCompletedEvent<R>) -> Unit)? = null
    var onFailure: ((ExecutionCompletedEvent<R>) -> Unit)? = null

    internal fun build(): FailsafeExecutor<R> {

        val policies = mutableListOf<Policy<R>>()

        circuitBreaker?.let { policies.add(it) }
        retry?.let { policies.add(it) }
        fallback?.let { policies.add(it) }

        val failsafe = Failsafe.with(*policies.toTypedArray())

        onComplete?.let { failsafe.onComplete(it) }
        onSuccess?.let { failsafe.onSuccess(it) }
        onFailure?.let { failsafe.onFailure(it) }

        return failsafe
    }
}

fun <R> failsafe(setup: FailsafeDsl<R>.() -> Unit): FailsafeExecutor<R> {

    val dsl = FailsafeDsl<R>()
    setup.invoke(dsl)

    return dsl.build()
}