package io.github.debop.failsafekotlin.dsl

import io.github.debop.failsafekotlin.event.ExecutionCompletedEventHandler
import net.jodah.failsafe.CircuitBreaker
import net.jodah.failsafe.function.CheckedRunnable
import java.time.Duration

/**
 * CircuitBreakerDsl
 *
 * @author debop
 * @since 19. 3. 29
 */
@FailsafeBuilderDsl
class CircuitBreakerDsl<R> : AbstractPolicyDsl<R>() {

    object Default {
        @JvmField val delay: Duration = Duration.ofSeconds(60)
        const val failureThreshold: Int = 5
        const val successThreshold: Int = 3
    }

    var delay: Duration = Default.delay
    var failureThreshold: Int = Default.failureThreshold
    var successThreshold: Int = Default.successThreshold
    var timeout: Duration? = null

    private val onCloseHandlers = mutableListOf<CheckedRunnable>()
    private val onOpenHandlers = mutableListOf<CheckedRunnable>()
    private val onHalfOpenHandlers = mutableListOf<CheckedRunnable>()

    private val onSuccessHandlers = mutableListOf<ExecutionCompletedEventHandler<R>>()
    private val onFailureHandlers = mutableListOf<ExecutionCompletedEventHandler<R>>()

    fun onClose(handler: () -> Unit): CircuitBreakerDsl<R> =
        apply {
            onCloseHandlers.add(CheckedRunnable { handler.invoke() })
        }

    fun onOpen(handler: () -> Unit): CircuitBreakerDsl<R> =
        apply {
            onOpenHandlers.add(CheckedRunnable { handler.invoke() })
        }

    fun onHalfOpen(handler: () -> Unit): CircuitBreakerDsl<R> =
        apply {
            onHalfOpenHandlers.add(CheckedRunnable { handler.invoke() })
        }

    fun onSuccess(handler: ExecutionCompletedEventHandler<R>): CircuitBreakerDsl<R> =
        apply {
            onSuccessHandlers.add(handler)
        }

    fun onFailure(handler: ExecutionCompletedEventHandler<R>): CircuitBreakerDsl<R> =
        apply {
            onSuccessHandlers.add(handler)
        }

    internal fun build(): CircuitBreaker<R> {

        val breaker = CircuitBreaker<R>()
            .withDelay(delay)
            .withFailureThreshold(failureThreshold)
            .withSuccessThreshold(successThreshold)

        timeout?.let { breaker.withTimeout(it) }

        onCloseHandlers.forEach { breaker.onClose(it) }
        onOpenHandlers.forEach { breaker.onOpen(it) }
        onHalfOpenHandlers.forEach { breaker.onHalfOpen(it) }

        onSuccessHandlers.forEach { breaker.onSuccess(it) }
        onFailureHandlers.forEach { breaker.onFailure(it) }

        return breaker

    }
}

fun <R> circuitBreaker(setup: CircuitBreakerDsl<R>.() -> Unit): CircuitBreaker<R> {

    val dsl = CircuitBreakerDsl<R>()
    setup.invoke(dsl)

    return dsl.build()
}



