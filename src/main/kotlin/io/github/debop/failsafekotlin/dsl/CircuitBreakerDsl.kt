package io.github.debop.failsafekotlin.dsl

import net.jodah.failsafe.CircuitBreaker
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

    internal fun build(): CircuitBreaker<R> {

        return CircuitBreaker<R>()
            .withDelay(delay)
            .withFailureThreshold(failureThreshold)
            .withSuccessThreshold(successThreshold)
    }
}

fun <R> circuitBreaker(setup: CircuitBreakerDsl<R>.() -> Unit): CircuitBreaker<R> {

    val dsl = CircuitBreakerDsl<R>()
    setup.invoke(dsl)

    return dsl.build()
}