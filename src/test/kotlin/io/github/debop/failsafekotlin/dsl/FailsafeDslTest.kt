package io.github.debop.failsafekotlin.dsl

import io.github.debop.failsafekotlin.runChecked
import mu.KLogging
import org.junit.jupiter.api.Test
import java.time.Duration

/**
 * FailsafeDslTest
 *
 * @author debop
 * @since 19. 3. 29
 */
class FailsafeDslTest {

    companion object : KLogging()

    @Test
    fun `build failsafe executor by dsl`() {

        val failsafe = failsafe<Any?> {

            retry = retry {
                maxRetries = 5
                delay = Duration.ofMillis(100)
            }

            circuitBreaker = circuitBreaker {
                delay = Duration.ofMinutes(1)
                failureThreshold = 5
                successThreshold = 5
            }

            onComplete = { evt ->
                logger.debug { "on completed. evt=$evt" }
            }

            onSuccess = { evt ->
                logger.debug { "on Success. evt=$evt" }
            }

            onFailure = { evt ->
                logger.error(evt.failure) { "on Failure. evt=$evt" }
            }
        }

        failsafe.runChecked {
            logger.debug { "Start failsafe." }
        }
    }
}