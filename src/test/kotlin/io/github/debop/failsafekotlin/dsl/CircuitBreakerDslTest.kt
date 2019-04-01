package io.github.debop.failsafekotlin.dsl

import mu.KLogging
import net.jodah.failsafe.util.Ratio
import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Test
import java.time.Duration

/**
 * CircuitBreakerDslTest
 *
 * @author debop
 * @since 19. 3. 29
 */
class CircuitBreakerDslTest {
    companion object : KLogging()

    @Test
    fun `create circuit breaker policy by dsl`() {

        val breaker = circuitBreaker<Any> {

            delay = Duration.ofSeconds(30)
            failureThreshold = 10
            successThreshold = 3

            onClose { logger.debug { "Circuit is closed" } }
            onOpen { logger.debug { "Circuit is open" } }
            onHalfOpen { logger.debug { "Circuit is halfopen" } }

            onSuccess { evt -> logger.debug { "onSuccess evt=$evt" } }
            onFailure { evt -> logger.debug { "onSuccess evt=$evt" } }
        }

        breaker.delay shouldEqual Duration.ofSeconds(30)
        breaker.failureThreshold shouldEqual Ratio(10, 10)
        breaker.successThreshold shouldEqual Ratio(3, 3)

    }
}