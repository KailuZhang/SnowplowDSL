package io.kailuzhang.github.snowplow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.snowplowanalytics.snowplow.tracker.DevicePlatforms
import com.snowplowanalytics.snowplow.tracker.Emitter
import com.snowplowanalytics.snowplow.tracker.Emitter.EmitterBuilder
import com.snowplowanalytics.snowplow.tracker.Gdpr
import com.snowplowanalytics.snowplow.tracker.Subject
import com.snowplowanalytics.snowplow.tracker.Tracker
import com.snowplowanalytics.snowplow.tracker.constants.Parameters
import com.snowplowanalytics.snowplow.tracker.constants.TrackerConstants
import com.snowplowanalytics.snowplow.tracker.payload.SelfDescribingJson
import com.snowplowanalytics.snowplow.tracker.utils.LogLevel
import com.snowplowanalytics.snowplow.tracker.utils.Util.addToMap
import io.kailuzhang.github.snowplow.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAndroidTracker()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.helloWorld.setOnClickListener {
            snowplow<Click>("Main") {
                elementName("hello_world")
                elementId("2020")
                elementType("word")
                extra("list_uri" to "list", "list_name" to "aaa")
                extra {
                    put("list_uri", "list")
                    put("list_name", "aaa")
                }
            }

            binding.helloWorld.text = result().toSpanned()
        }

        snowplow<SingleImpression>("Main") {
            elementName("hello_world")
            elementId("2020")
            elementType("word")
            elementPosition(55)
            listType("/ddd")
            extra("list_uri" to "list", "list_name" to "aaa")
        }

        snowplow<GoodsImpression>("Main") {
            listType("/goods")
            goods(listOf())
        }

        // binding.helloWorld.setOnClickListener {
        //     snowplow {
        //         appCommon {
        //             landingPage("landing")
        //             refer("last")
        //         }
        //         common {
        //             pageCode("Main")
        //         }
        //         click {
        //             elementName("hello_world")
        //             elementId("2020")
        //             elementType("word")
        //             extra("list_uri" to "list", "list_name" to "aaa")
        //         }
        //     }
        // }
    }

    override fun onResume() {
        super.onResume()
        snowplow<PageView>("Main") {
            viewType("show")
        }
    }

    override fun onPause() {
        super.onPause()
        snowplow<PageView>("Main") {
            viewType("hide")
            startTime(100L)
            endTime(10000L)
        }
    }

    private fun initAndroidTracker() {
        Tracker.close()
        val emitter: Emitter = EmitterBuilder("", this.applicationContext)
            .tick(1)
            .build()
        val subject: Subject = Subject.SubjectBuilder()
            .context(this.applicationContext)
            .build()
        Tracker.init(Tracker.TrackerBuilder(emitter, "namespace", "appId", this.applicationContext)
            .level(LogLevel.VERBOSE)
            .base64(false)
            .platform(DevicePlatforms.Mobile)
            .subject(subject)
            .threadCount(20)
            .sessionContext(true)
            .mobileContext(true)
            .geoLocationContext(true)
            .applicationCrash(true)
            .trackerDiagnostic(true)
            .lifecycleEvents(true)
            .foregroundTimeout(60)
            .backgroundTimeout(30)
            .screenviewEvents(true)
            .screenContext(true)
            .installTracking(true)
            .applicationContext(false)
            .build()
        )
        val pairs: Map<String, Any> = HashMap()
        addToMap(Parameters.APP_VERSION, "0.3.0", pairs)
        addToMap(Parameters.APP_BUILD, "3", pairs)
        Tracker.instance()
            .addGlobalContext(SelfDescribingJson(TrackerConstants.SCHEMA_APPLICATION, pairs))
        Tracker.instance().enableGdprContext(Gdpr.Basis.CONSENT,
            "someId",
            "0.1.0",
            "this is a demo document description")
    }

    fun result() =
        html {
            head {
                title { +"XML encoding with Kotlin" }
            }
            body {
                h1 { +"XML encoding with Kotlin" }
                p { +"this format can be used as an alternative markup to XML" }

                // an element with attributes and text content
                a(href = "http://kotlinlang.org") { +"Kotlin" }

                // mixed content
                p {
                    +"This is some"
                    b { +"mixed" }
                    +"text. For more see the"
                    a(href = "http://kotlinlang.org") { +"Kotlin" }
                    +"project"
                }
                p { +"some text" }
            }
        }
}