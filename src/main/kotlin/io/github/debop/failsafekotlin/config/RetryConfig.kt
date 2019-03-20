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

package io.github.debop.failsafekotlin.config

import java.io.Serializable
import java.util.concurrent.TimeUnit
import java.util.function.Predicate

/**
 * RetryConfig
 * @author debop
 * @since 2019-02-17
 */
data class RetryConfig @JvmOverloads constructor(val maxRetries: Int = 3,
                                                 val delay: Long? = null,
                                                 val maxDelay: Long? = null,
                                                 val dealyUnit: TimeUnit = TimeUnit.MILLISECONDS,
                                                 val backoffDelay: Long? = 1000,
                                                 val backoffMaxDelay: Long? = 10_000,
                                                 var backoffTimeUnit: TimeUnit = TimeUnit.MILLISECONDS,
                                                 val backoffFactor: Double? = 2.0) : Serializable {

    companion object {
        val DEFAULT: RetryConfig = RetryConfig()


    }

    var abortOnClass: Class<*>? = null
    var abortIf: Predicate<*>? = null

}