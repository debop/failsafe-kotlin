package io.github.debop.failsafekotlin.dsl

import net.jodah.failsafe.RetryPolicy
import net.jodah.failsafe.event.ExecutionAttemptedEvent
import net.jodah.failsafe.event.ExecutionCompletedEvent
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
        const val maxRetries: Int = 2
        @JvmField val delay: Duration = Duration.ZERO
        @JvmField val maxDuration: Duration = Duration.ofSeconds(1)
    }

    var maxRetries: Int = Default.maxRetries
    var delay: Duration = Default.delay
    var maxDuration: Duration = Default.maxDuration

    var abortIf: ((R, Throwable) -> Boolean)? = null
    var abortOnPredicate: ((Throwable) -> Boolean)? = null

    var handle: List<Class<Throwable>>? = null
    var handleIf: ((R, Throwable) -> Boolean)? = null

    var handleResult: R? = null
    var handleResultIf: ((R) -> Boolean)? = null

    var onAbort: ((ExecutionCompletedEvent<R>) -> Unit)? = null
    var onFailedAttempt: ((ExecutionAttemptedEvent<R>) -> Unit)? = null
    var onRetriesExceeded: ((ExecutionCompletedEvent<R>) -> Unit)? = null
    var onRetry: ((ExecutionAttemptedEvent<R>) -> Unit)? = null
    var onFailure: ((ExecutionCompletedEvent<R>) -> Unit)? = null
    var onSuccess: ((ExecutionCompletedEvent<R>) -> Unit)? = null

    val onAbortHandlers = mutableListOf<(ExecutionCompletedEvent<R>) -> Unit>()


    internal fun build(): RetryPolicy<R> {
        val retry = RetryPolicy<R>()
            .withMaxRetries(maxRetries)
            .withDelay(delay)
            .withMaxDuration(maxDuration)

        abortIf?.let { retry.abortIf(it) }
        abortOnPredicate?.let { retry.abortOn(it) }

        handle?.let { retry.handle(handle) }
        handleIf?.let { retry.handleIf(it) }

        handleResult?.let { retry.handleResult(it) }
        handleResultIf?.let { retry.handleResultIf(it) }

        onAbort?.let { retry.onAbort(it) }
        onFailedAttempt?.let { retry.onFailedAttempt(it) }
        onRetriesExceeded?.let { retry.onRetriesExceeded(it) }
        onRetry?.let { retry.onRetry(it) }
        onFailure?.let { retry.onFailure(it) }
        onSuccess?.let { retry.onSuccess(it) }

        onAbortHandlers.forEach {
            retry.onAbort(it)
        }

        return retry
    }
}

/**
 * Define [RetryPolicy]
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

fun <R> RetryPolicyDsl<R>.onAbort(handler: (ExecutionCompletedEvent<R>) -> Unit): RetryPolicyDsl<R> = apply {
    onAbortHandlers.add(handler)
}