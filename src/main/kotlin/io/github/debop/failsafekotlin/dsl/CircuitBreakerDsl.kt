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

import io.github.debop.failsafekotlin.event.ExecutionCompletedEventHandler
import net.jodah.failsafe.CircuitBreaker
import net.jodah.failsafe.function.CheckedRunnable
import java.time.Duration


/**
 * Create [CircuitBreaker]
 *
 * @param R
 * @param setup
 * @return
 */
fun <R> circuitBreaker(setup: CircuitBreakerDsl<R>.() -> Unit): CircuitBreaker<R> =
    CircuitBreakerDsl<R>().also { setup(it) }.build()

/**
 * CircuitBreaker DSL
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


