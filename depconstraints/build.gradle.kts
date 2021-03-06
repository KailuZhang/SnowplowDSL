/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    id("java-platform")
    id("maven-publish")
}

val appcompat = "1.1.0"
val activity = "1.0.0"
val cardview = "1.0.0"
val archTesting = "2.0.0"
val arcore = "1.7.0"
val benchmark = "1.0.0"
val browser = "1.0.0"
val constraintLayout = "1.1.3"
val core = "1.2.0"
val coroutines = "1.3.4"
val coroutinesTest = "1.3.4"
val crashlytics = "2.9.8"
val drawerLayout = "1.1.0-rc01"
val espresso = "3.1.1"
val firebaseAnalytics = "17.4.0"
val firebaseAuth = "19.3.1"
val firebaseConfig = "19.1.4"
val firebaseFirestore = "21.4.3"
val firebaseFunctions = "19.0.2"
val firebaseMessaging = "20.1.6"
val firebaseUi = "4.0.0"
val flexbox = "1.1.0"
val fragment = "1.2.4"
val glide = "4.9.0"
val googleMapUtils = "0.5"
val googlePlayServicesMaps = "16.0.0"
val googlePlayServicesVision = "17.0.2"
val gson = "2.8.6"
val hamcrest = "1.3"
val hiltJetPack = "1.0.0-alpha01"
val junit = "4.13"
val junitExt = "1.1.1"
val lifecycle = "2.2.0"
val lottie = "3.0.0"
val material = "1.1.0"
val mockito = "3.3.1"
val mockitoKotlin = "1.5.0"
val okhttp = "3.10.0"
val okio = "1.14.0"
val pageIndicator = "1.3.0"
val playCore = "1.6.5"
val room = "2.2.5"
val rules = "1.1.1"
val runner = "1.2.0"
val threetenabp = "1.0.5"
val timber = "4.7.1"
val viewpager2 = "1.0.0"
val snowplow = "1.5.0"

dependencies {
    constraints {
        api("${Libs.ACTIVITY_KTX}:$activity")
        api("${Libs.APPCOMPAT}:$appcompat")
        api("${Libs.CARDVIEW}:$cardview")
        api("${Libs.BROWSER}:$browser")
        api("${Libs.CONSTRAINT_LAYOUT}:$constraintLayout")
        api("${Libs.CORE_KTX}:$core")
        api("${Libs.COROUTINES}:$coroutines")
        api("${Libs.COROUTINES_TEST}:$coroutines")
        api("${Libs.FLEXBOX}:$flexbox")
        api("${Libs.FRAGMENT_KTX}:$fragment")
        api("${Libs.GLIDE}:$glide")
        api("${Libs.GLIDE_COMPILER}:$glide")
        api("${Libs.GSON}:$gson")
        api("${Libs.JUNIT}:$junit")
        api("${Libs.EXT_JUNIT}:$junitExt")
        api("${Libs.KOTLIN_STDLIB}:${Versions.KOTLIN}")
        api("${Libs.LIFECYCLE_COMPILER}:$lifecycle")
        api("${Libs.LIFECYCLE_LIVE_DATA_KTX}:$lifecycle")
        api("${Libs.LIFECYCLE_VIEW_MODEL_KTX}:$lifecycle")
        api("${Libs.SNOWPLOW}:$snowplow")
    }
}

publishing {
    publications {
        create<MavenPublication>("myPlatform") {
            from(components["javaPlatform"])
        }
    }
}
