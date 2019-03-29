package io.github.debop.failsafekotlin.dsl

import net.jodah.failsafe.RetryPolicy
import java.time.Duration

/**
 * RetryPolicyDsl
 *
 * @author debop
 * @since 19. 3. 29
 */
@FailsafeBuilderDsl
class RetryPolicyDsl<R> : AbstractPolicyDsl<R>() {

    object Default {
        @JvmField val maxRetries: Int = 2
        @JvmField val delay: Duration = Duration.ZERO
        @JvmField val maxDuration: Duration = Duration.ofSeconds(1)
    }

    var maxRetries: Int = Default.maxRetries
    var delay: Duration = Default.delay
    var maxDuration: Duration = Default.maxDuration


    internal fun build(): RetryPolicy<R> {
        return RetryPolicy<R>()
            .withMaxRetries(maxRetries)
            .withDelay(delay)
            .withMaxDuration(maxDuration)
    }
}

/**
 * Define
 *
 * @param R
 * @param setup
 * @return
 */
fun <R> retry(setup: RetryPolicyDsl<R>.() -> Unit): RetryPolicy<R> {

    val dsl = RetryPolicyDsl<R>()
    setup.invoke(dsl)

    return dsl.build()
}