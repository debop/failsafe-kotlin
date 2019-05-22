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
import net.jodah.failsafe.Failsafe
import net.jodah.failsafe.FailsafeExecutor
import net.jodah.failsafe.Fallback
import net.jodah.failsafe.Policy
import net.jodah.failsafe.RetryPolicy

fun <R> failsafe(setup: FailsafeDsl<R>.() -> Unit): FailsafeExecutor<R> =
    FailsafeDsl<R>().also { setup(it) }.build()

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

    private val onCompleteHandlers = mutableListOf<ExecutionCompletedEventHandler<R>>()
    private val onSuccessHandlers = mutableListOf<ExecutionCompletedEventHandler<R>>()
    private val onFailureHandlers = mutableListOf<ExecutionCompletedEventHandler<R>>()


    fun onComplete(handler: ExecutionCompletedEventHandler<R>): FailsafeDsl<R> =
        apply {
            onCompleteHandlers.add(handler)
        }

    fun onSuccess(handler: ExecutionCompletedEventHandler<R>): FailsafeDsl<R> =
        apply {
            onSuccessHandlers.add(handler)
        }

    fun onFailure(handler: ExecutionCompletedEventHandler<R>): FailsafeDsl<R> =
        apply {
            onFailureHandlers.add(handler)
        }

    internal fun build(): FailsafeExecutor<R> {

        val policies = mutableListOf<Policy<R>>()

        circuitBreaker?.let { policies.add(it) }
        retry?.let { policies.add(it) }
        fallback?.let { policies.add(it) }

        val failsafe = Failsafe.with(*policies.toTypedArray())

        onCompleteHandlers.forEach { failsafe.onComplete(it) }
        onSuccessHandlers.forEach { failsafe.onSuccess(it) }
        onFailureHandlers.forEach { failsafe.onFailure(it) }

        return failsafe
    }
}








