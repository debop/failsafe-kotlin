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

plugins {
    id("org.jetbrains.kotlin.jvm").version("1.3.21")
}

repositories {
    jcenter()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    compile("net.jodah:failsafe:2.0.1")
    compile("io.github.microutils:kotlin-logging:1.6.23")

    testCompile("ch.qos.logback:logback-classic:1.2.3")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.3.2")
}
