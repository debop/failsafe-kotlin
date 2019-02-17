/*
 * Copyright (c) 2017-2020 by Coupang. Some rights reserved.
 * See the project homepage at: http://gitlab.coupang.net
 * Licensed under the Coupang License;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://coupang.net/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.debop.failsafekotlin

import mu.KLogging
import net.jodah.failsafe.RetryPolicy
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldEqualTo
import org.junit.jupiter.api.Test
import java.io.IOException
import java.time.Duration

class RetryExtensionsTest {

    companion object : KLogging()

    @Test
    fun `isAbortable with Result`() {
        val retry = RetryPolicy<Any>()

        retry.isAbortable(Result.failure(RuntimeException())) shouldEqualTo false

        retry.abortOn(RuntimeException::class)

        retry.isAbortable(Result.failure(RuntimeException())) shouldEqualTo true
        retry.isAbortable(Result.failure(IOException())) shouldEqualTo false
        retry.isAbortable(Result.failure(Exception())) shouldEqualTo false
    }

    @Test
    fun `canApply with Result`() {
        val retry = RetryPolicy<Any>().withDelay(Duration.ofSeconds(2))

        retry.canApplyDelayFn(Result.success(1)) shouldEqualTo true
        retry.canApplyDelayFn(Result.failure(RuntimeException())) shouldEqualTo true
        retry.canApplyDelayFn(Result.failure(RuntimeException())) shouldEqualTo true
        retry.delay shouldEqual Duration.ofSeconds(2)
    }
}