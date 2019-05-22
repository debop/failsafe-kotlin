/*
 * Copyright (c) 2019. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.debop.failsafekotlin.dsl

import io.github.debop.failsafekotlin.event.ExecutionAttemptedEventHandler
import io.github.debop.failsafekotlin.event.ExecutionCompletedEventHandler
import net.jodah.failsafe.RetryPolicy
import java.time.Duration

/**
 * Define [RetryPolicy]
 *
 * @param R
 * @param setup
 * @return
 */
fun <R> retry(setup: RetryPolicyDsl<R>.() -> Unit): RetryPolicy<R> =
    RetryPolicyDsl<R>().also { setup(it) }.build()

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

    private val handleIfHandlers = mutableListOf<(R, Throwable) -> Boolean>()

    private val onAbortHandlers = mutableListOf<ExecutionCompletedEventHandler<R>>()
    private val onFailedAttemptHandlers = mutableListOf<ExecutionAttemptedEventHandler<R>>()
    private val onRetriesExceededHandlers = mutableListOf<ExecutionCompletedEventHandler<R>>()
    private val onRetryHandlers = mutableListOf<ExecutionAttemptedEventHandler<R>>()
    private val onFailureHandlers = mutableListOf<ExecutionCompletedEventHandler<R>>()
    private val onSuccessHandlers = mutableListOf<ExecutionCompletedEventHandler<R>>()

    fun onAbort(handler: ExecutionCompletedEventHandler<R>): RetryPolicyDsl<R> =
        apply {
            onAbortHandlers.add(handler)
        }

    fun onFailedAttempt(handler: ExecutionAttemptedEventHandler<R>): RetryPolicyDsl<R> =
        apply {
            onFailedAttemptHandlers.add(handler)
        }

    fun onRetriesExceeded(handler: ExecutionCompletedEventHandler<R>): RetryPolicyDsl<R> =
        apply {
            onRetriesExceededHandlers.add(handler)
        }

    fun onRetry(handler: ExecutionAttemptedEventHandler<R>): RetryPolicyDsl<R> =
        apply {
            onRetryHandlers.add(handler)
        }

    fun onFailure(handler: ExecutionCompletedEventHandler<R>): RetryPolicyDsl<R> =
        apply {
            onFailureHandlers.add(handler)
        }

    fun onSuccess(handler: ExecutionCompletedEventHandler<R>): RetryPolicyDsl<R> =
        apply {
            onSuccessHandlers.add(handler)
        }


    internal fun build(): RetryPolicy<R> {
        val retry = RetryPolicy<R>()
            .withMaxRetries(maxRetries)
            .withDelay(delay)
            .withMaxDuration(maxDuration)

        abortIf?.let { retry.abortIf(it) }
        abortOnPredicate?.let { retry.abortOn(it) }

        handle?.let { retry.handle(handle) }
        handleIf?.let { retry.handleIf(it) }

        handleIfHandlers.forEach { retry.handleIf(it) }

        handleResult?.let { retry.handleResult(it) }
        handleResultIf?.let { retry.handleResultIf(it) }

        onAbortHandlers.forEach { retry.onAbort(it) }
        onFailedAttemptHandlers.forEach { retry.onFailedAttempt(it) }
        onRetriesExceededHandlers.forEach { retry.onRetriesExceeded(it) }
        onRetryHandlers.forEach { retry.onRetry(it) }
        onFailureHandlers.forEach { retry.onFailure(it) }
        onSuccessHandlers.forEach { retry.onSuccess(it) }

        return retry
    }
}

