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

import kotlinx.coroutines.runBlocking
import net.jodah.failsafe.ExecutionContext
import net.jodah.failsafe.FailsafeExecutor
import net.jodah.failsafe.function.CheckedRunnable
import net.jodah.failsafe.function.CheckedSupplier
import net.jodah.failsafe.function.ContextualRunnable
import net.jodah.failsafe.function.ContextualSupplier
import java.util.concurrent.CompletableFuture


fun <R, T : R> FailsafeExecutor<R>.getChecked(supplier: () -> T): T =
    get(CheckedSupplier(supplier))

fun <R, T : R> FailsafeExecutor<R>.get(supplier: (ExecutionContext) -> T): T =
    get(ContextualSupplier(supplier))

fun <R, T : R> FailsafeExecutor<R>.getAsync(supplier: () -> T): CompletableFuture<T> =
    getAsync(CheckedSupplier(supplier))

fun <R, F : R> FailsafeExecutor<R>.getStageAsync(supplier: () -> CompletableFuture<R>): CompletableFuture<R> =
    getStageAsync(CheckedSupplier(supplier))

fun <R> FailsafeExecutor<R>.runChecked(runnable: () -> Unit) =
    run(CheckedRunnable(runnable))

fun <R> FailsafeExecutor<R>.run(runnable: (ExecutionContext) -> Unit): Unit =
    run(ContextualRunnable(runnable))

fun <R> FailsafeExecutor<R>.runAsync(runnable: () -> Unit): CompletableFuture<Void> =
    runAsync(CheckedRunnable(runnable))

fun <R> FailsafeExecutor<R>.runAsync(runnable: (ExecutionContext) -> Unit): CompletableFuture<Void> =
    runAsync(ContextualRunnable(runnable))

fun <R> FailsafeExecutor<R>.runSuspend(suspendable: suspend () -> Unit): CompletableFuture<Void> {
    return runAsync(CheckedRunnable {
        runBlocking {
            suspendable.invoke()
        }
    })
}