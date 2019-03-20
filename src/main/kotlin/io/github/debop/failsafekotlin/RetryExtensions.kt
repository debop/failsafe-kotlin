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

import net.jodah.failsafe.ExecutionContext
import net.jodah.failsafe.RetryPolicy
import net.jodah.failsafe.event.ExecutionAttemptedEvent
import net.jodah.failsafe.event.ExecutionCompletedEvent
import net.jodah.failsafe.function.CheckedConsumer
import java.time.Duration
import java.util.function.BiPredicate
import java.util.function.Predicate
import kotlin.reflect.KClass


@SinceKotlin("1.3")
fun <R> RetryPolicy<R>.isAbortable(result: Result<R>): Boolean =
    isAbortable(result.getOrNull(), result.exceptionOrNull())

@SinceKotlin("1.3")
fun <R> RetryPolicy<R>.canApplyDelayFn(result: Result<R>): Boolean =
    canApplyDelayFn(result.getOrNull(), result.exceptionOrNull())

fun <R, F : Throwable> RetryPolicy<R>.withDelayOn(failureKlass: KClass<F>,
                                                  delayFn: (R, F, ExecutionContext) -> Duration): RetryPolicy<R> =
    withDelayOn({ result, failure, context -> delayFn.invoke(result, failure, context) },
                failureKlass.java)

fun <R> RetryPolicy<R>.abortOn(klass: KClass<out Throwable>): RetryPolicy<R> =
    abortOn(klass.java)

fun <R> RetryPolicy<R>.abortOn(vararg klasses: KClass<out Throwable>): RetryPolicy<R> =
    abortOn(klasses.map { it.java })

fun <R> RetryPolicy<R>.abortOn(klasses: Iterable<KClass<out Throwable>>): RetryPolicy<R> =
    abortOn(klasses.map { it.java })

fun <R> RetryPolicy<R>.abortOn(failurePredicate: (Throwable) -> Boolean): RetryPolicy<R> =
    abortOn(Predicate(failurePredicate))

fun <R> RetryPolicy<R>.handle(failure: KClass<out Exception>): RetryPolicy<R> = handle(failure.java)

fun <R> RetryPolicy<R>.handle(vararg failures: KClass<out Exception>): RetryPolicy<R> =
    handle(failures.map { it.java })

fun <R> RetryPolicy<R>.handle(failures: Iterable<KClass<out Exception>>): RetryPolicy<R> =
    handle(failures.map { it.java })

fun <R> RetryPolicy<R>.handleIf(failurePredicate: (Throwable) -> Boolean): RetryPolicy<R> =
    handleIf(Predicate(failurePredicate))

fun <R> RetryPolicy<R>.handleIf(resultPredicate: (R, Throwable) -> Boolean): RetryPolicy<R> =
    handleIf(BiPredicate(resultPredicate))

fun <R> RetryPolicy<R>.handleResultIf(resultPredicate: (R) -> Boolean): RetryPolicy<R> =
    handleResultIf(Predicate(resultPredicate))

fun <R> RetryPolicy<R>.onAbort(listener: (ExecutionCompletedEvent<R>) -> Unit): RetryPolicy<R> =
    this.onAbort(CheckedConsumer(listener))

fun <R> RetryPolicy<R>.onFailedAttempt(listener: (ExecutionAttemptedEvent<R>) -> Unit): RetryPolicy<R> =
    this.onFailedAttempt(CheckedConsumer(listener))

fun <R> RetryPolicy<R>.onRetriesExceeded(listener: (ExecutionCompletedEvent<R>) -> Unit): RetryPolicy<R> =
    this.onRetriesExceeded(CheckedConsumer(listener))

fun <R> RetryPolicy<R>.onRetry(listener: (ExecutionAttemptedEvent<R>) -> Unit): RetryPolicy<R> =
    this.onRetry(CheckedConsumer(listener))

fun <R> RetryPolicy<R>.onFailure(listener: (ExecutionCompletedEvent<R>) -> Unit): RetryPolicy<R> =
    this.onFailure(CheckedConsumer(listener))

fun <R> RetryPolicy<R>.onSuccess(listener: (ExecutionCompletedEvent<R>) -> Unit): RetryPolicy<R> =
    this.onSuccess(CheckedConsumer(listener))
