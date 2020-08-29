package io.kailuzhang.github.snowplow

import com.snowplowanalytics.snowplow.tracker.Tracker
import com.snowplowanalytics.snowplow.tracker.events.SelfDescribing
import com.snowplowanalytics.snowplow.tracker.payload.SelfDescribingJson
import com.snowplowanalytics.snowplow.tracker.utils.Util

@DslMarker
annotation class DescribingJsonMarker

@DescribingJsonMarker
abstract class MapDescribingJson(
    private val scheme: String,
    private val data: HashMap<String, out Any?>,
) {
    abstract fun checkData(data: Map<String, Any>)

    fun getData(): Map<String, Any> = data

    fun build() = SelfDescribingJson(scheme, data)
}

class CommonDescribingJson(
    private val data: HashMap<String, String?>,
) : MapDescribingJson("iglu:com.artemis/common/jsonschema/1-0-1", data) {
    fun pageCode(pageCode: String) {
        data["page_code"] = pageCode
    }

    fun extra(extra: Map<String, String?>) {
        data.putAll(extra)
    }

    override fun checkData(data: Map<String, Any>) {
    }
}

class AppCommonDescribingJson(
    private val data: HashMap<String, String?>,
) : MapDescribingJson("iglu:com.artemis/app_common/jsonschema/1-0-1", data) {
    fun landingPage(pageCode: String) {
        data["landing_page"] = pageCode
    }

    fun refer(pageCode: String) {
        data["refer"] = pageCode
    }

    fun uri(uri: String) {
        data["uri"] = uri
    }

    fun extra(extra: Map<String, String?>) {
        data.putAll(extra)
    }

    override fun checkData(data: Map<String, Any>) {
    }
}

class PageView(
    private val data: HashMap<String, Any>,
) : MapDescribingJson("iglu:com.artemis/page_view/jsonschema/1-0-0", data) {
    fun viewType(viewType: String) {
        data["view_type"] = viewType
    }

    fun startTime(startTime: Long) {
        data["start_time"] = startTime
    }

    fun endTime(endTime: Long) {
        data["end_time"] = endTime
    }

    override fun checkData(data: Map<String, Any>) {
    }
}

class Click(
    private val data: HashMap<String, Any>,
) : MapDescribingJson("iglu:com.artemis/click/jsonschema/1-0-0", data) {
    fun type(type: String) {
    }

    fun elementName(elementName: String) {
        data["element_name"] = elementName
    }

    fun elementId(elementId: String) {
        data["element_id"] = elementId
    }

    fun elementType(elementType: String) {
        data["element_type"] = elementType
    }

    fun listType(listType: String) {
        data["list_type"] = listType
    }

    fun extra(vararg pairs: Pair<String, String>) {
        if (pairs.isEmpty()) return
        data["extra"] = pairs.toMap()
    }

    fun extra(pairs: LinkedHashMap<String, String>.() -> Unit) {
    }

    override fun checkData(data: Map<String, Any>) {
    }
}

open class Impression(
    private val data: HashMap<String, Any>,
) : MapDescribingJson("iglu:com.artemis/impressions/jsonschema/1-0-0", data) {

    fun listType(listType: String) {
        data["list_type"] = listType
    }

    fun list(list: List<ImpressionItem>) {
        data["list"] = list
    }

    class ImpressionItem(private val item: HashMap<String, Any>) {
        fun elementName(elementName: String) {
            item["element_name"] = elementName
        }

        fun elementId(elementId: String) {
            item["element_id"] = elementId
        }

        fun elementType(elementType: String) {
            item["element_type"] = elementType
        }

        fun elementPosition(elementPosition: Int) {
            item["element_position"] = elementPosition
        }

        fun extra(map: Map<String, String>) {
            if (map.isEmpty()) return
            item["extra"] = map
        }

        fun extra(vararg pairs: Pair<String, String>) {
            if (pairs.isEmpty()) return
            item["extra"] = pairs.toMap()
        }

        fun extra(pairs: LinkedHashMap<String, String>.() -> Unit) {
        }
    }

    override fun checkData(data: Map<String, Any>) {
    }
}

class GoodsImpression(
    data: HashMap<String, Any>,
) : Impression(data) {

    fun goods(goods: List<Goods>) {
        val impressionItemList = goods.map { product ->
            ImpressionItem(hashMapOf()).apply {
                elementName("ddd")
                elementId(product.vId)
                elementPosition(product.position)
                extra(product.xx)
            }
        }
        list(impressionItemList)
    }

    override fun checkData(data: Map<String, Any>) {
    }

    data class Goods(
        val vId: String,
        val position: Int,
        val xx: Map<String, String>,
    )
}

class SingleImpression(
    data: HashMap<String, Any>,
) : Impression(data) {
    private val impressionItem = ImpressionItem(hashMapOf())

    init {
        list(arrayListOf(impressionItem))
    }

    fun elementName(elementName: String) {
        impressionItem.elementName(elementName)
    }

    fun elementId(elementId: String) = impressionItem.elementId(elementId)

    fun elementType(elementType: String) = impressionItem.elementType(elementType)

    fun elementPosition(elementPosition: Int) = impressionItem.elementPosition(elementPosition)

    fun extra(vararg pairs: Pair<String, String>) = impressionItem.extra(*pairs)

    fun extra(pairs: LinkedHashMap<String, String>.() -> Unit) = impressionItem.extra(pairs)

    override fun checkData(data: Map<String, Any>) {
    }
}

@DescribingJsonMarker
class Snowplow {
    private lateinit var eventData: SelfDescribingJson
    private val customContext = mutableListOf<SelfDescribingJson>()

    private fun <T : MapDescribingJson> initCustomContext(customContext: T, init: T.() -> Unit) {
        init.invoke(customContext)
        this.customContext.add(customContext.build())
    }

    private fun <T : MapDescribingJson> initEventData(eventData: T, init: T.() -> Unit) {
        init.invoke(eventData)
        this.eventData = eventData.build()
    }

    private fun Map<String, String?>.commonMap() = HashMap<String, String?>()
    private fun Map<String, String?>.appCommonMap() = HashMap<String, String?>()

    fun common(init: CommonDescribingJson.() -> Unit) =
        initCustomContext(CommonDescribingJson(hashMapOf<String, String?>().commonMap()), init)

    fun appCommon(init: AppCommonDescribingJson.() -> Unit) =
        initCustomContext(AppCommonDescribingJson(hashMapOf<String, String?>().appCommonMap()),
            init)

    fun pageView(init: PageView.() -> Unit) = initEventData(PageView(hashMapOf()), init)
    fun click(init: Click.() -> Unit) = initEventData(Click(hashMapOf()), init)
    fun impression(init: Impression.() -> Unit) = initEventData(Impression(hashMapOf()), init)
    fun goodsImpression(init: GoodsImpression.() -> Unit) =
        initEventData(GoodsImpression(hashMapOf()), init)

    fun singleImpression(init: SingleImpression.() -> Unit) =
        initEventData(SingleImpression(hashMapOf()), init)

    fun track() {
        Tracker.instance().track(
            SelfDescribing
                .builder()
                .eventData(eventData)
                .eventId(Util.getEventId())
                .customContext(customContext)
                .build()
        )
    }
}

fun snowplow(init: Snowplow.() -> Unit) {
    val snowplow = Snowplow()
    snowplow.init()
    snowplow.track()
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : MapDescribingJson> snowplow(pageCode: String, noinline init: T.() -> Unit) {
    val snowplow = Snowplow()
    snowplow.common {
        pageCode(pageCode)
    }
    snowplow.appCommon { }
    when (T::class.java) {
        PageView::class.java -> {
            snowplow.pageView(init as (PageView.() -> Unit))
        }
        Click::class.java -> {
            snowplow.click(init as (Click.() -> Unit))
        }
        GoodsImpression::class.java -> {
            snowplow.goodsImpression(init as (GoodsImpression.() -> Unit))
        }
        SingleImpression::class.java -> {
            snowplow.singleImpression(init as (SingleImpression.() -> Unit))
        }
        Impression::class.java -> {
            snowplow.impression(init as (Impression.() -> Unit))
        }
        else -> throw IllegalArgumentException("Not supported type")
    }
    snowplow.track()
}