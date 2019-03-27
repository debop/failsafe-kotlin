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

package io.github.debop.failsafekotlin

import net.jodah.failsafe.CircuitBreaker
import net.jodah.failsafe.function.CheckedRunnable
import kotlin.reflect.KClass

/**
 * CircuitBreakerExtensions
 * @author debop
 * @since 2019-03-20
 */

fun <R> CircuitBreaker<R>.get(body: () -> Unit) {
    if (allowsExecution()) {
        try {
            preExecute()
            body.invoke()
            recordSuccess()
        } catch (e: Exception) {
            recordFailure()
        }
    }
}

fun <R> CircuitBreaker<R>.handle(failure: KClass<out Exception>): CircuitBreaker<R> = handle(failure.java)

fun <R> CircuitBreaker<R>.handle(vararg failures: KClass<out Exception>): CircuitBreaker<R> =
    this.handle(failures.map { it.java })

fun <R> CircuitBreaker<R>.handle(failures: Iterable<KClass<out Exception>>): CircuitBreaker<R> =
    this.handle(failures.map { it.java })

fun <R> CircuitBreaker<R>.onClose(handler: () -> Unit): CircuitBreaker<R> =
    this.onClose(CheckedRunnable(handler))

fun <R> CircuitBreaker<R>.onHalfOpen(handler: () -> Unit): CircuitBreaker<R> =
    this.onHalfOpen(CheckedRunnable(handler))

fun <R> CircuitBreaker<R>.onOpen(handler: () -> Unit): CircuitBreaker<R> =
    this.onOpen(CheckedRunnable(handler))
