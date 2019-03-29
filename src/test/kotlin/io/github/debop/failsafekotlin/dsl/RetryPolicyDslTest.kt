package io.github.debop.failsafekotlin.dsl

import mu.KLogging
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldEqualTo
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test
import java.time.Duration

/**
 * RetryPolicyDslTest
 *
 * @author debop
 * @since 19. 3. 29
 */
class RetryPolicyDslTest {

    companion object : KLogging()

    @Test
    fun `define retry policy by dsl`() {

        val retry = retry<String> {
            maxRetries = 3
            delay = Duration.ofSeconds(5)
            maxDuration = Duration.ofSeconds(10)

            onAbort = { evt ->
                logger.debug { "on Abort ... evt=$evt" }
            }

            onAbort { evt ->
                logger.debug { "on Abort ... evt=$evt" }
            }
        }

        retry.shouldNotBeNull()

        retry.maxRetries shouldEqualTo 3
        retry.delay shouldEqual Duration.ofSeconds(5)
        retry.maxDuration shouldEqual Duration.ofSeconds(10)
    }
}