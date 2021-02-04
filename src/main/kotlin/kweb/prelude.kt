package kweb

import com.github.salomonbrys.kotson.toJson
import io.ktor.routing.*
import io.mola.galimatias.URL
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kweb.html.ElementReader
import kweb.html.HeadElement
import kweb.html.TitleElement
import kweb.html.events.Event
import kweb.html.fileUpload.FileFormInput
import kweb.routing.PathTemplate
import kweb.routing.RouteReceiver
import kweb.routing.UrlToPathSegmentsRF
import kweb.state.*
import kweb.util.pathQueryFragment
import java.util.concurrent.CompletableFuture
import kotlin.collections.set

/*
 * Mostly extension functions (and any simple classes they depend on), placed here such that an `import kweb.*`
 * will pick them up.
 */

fun ElementCreator<HeadElement>.title(
    attributes: Map<String, Any> = emptyMap(),
    new: (ElementCreator<TitleElement>.() -> Unit)? = null
): TitleElement {
    return TitleElement(element("title", attributes)).also {
        if (new != null) new(ElementCreator(parent = it, position = null))
    }
}

open class ULElement(parent: Element) : Element(parent)

fun ElementCreator<Element>.ul(
    attributes: Map<String, Any> = emptyMap(),
    new: (ElementCreator<ULElement>.() -> Unit)? = null
): ULElement {
    return ULElement(element("ul", attributes)).also {
        if (new != null) new(ElementCreator(parent = it, position = null))
    }
}

open class OLElement(parent: Element) : Element(parent)

fun ElementCreator<Element>.ol(
    attributes: Map<String, Any> = emptyMap(),
    new: (ElementCreator<OLElement>.() -> Unit)? = null
): OLElement {
    return OLElement(element("ol", attributes)).also {
        if (new != null) new(ElementCreator(parent = it, position = null))
    }
}

open class LIElement(parent: Element) : Element(parent)

fun ElementCreator<Element>.li(
    attributes: Map<String, Any> = emptyMap(),
    new: (ElementCreator<LIElement>.() -> Unit)? = null
): LIElement {
    return LIElement(element("li", attributes)).also {
        if (new != null) new(ElementCreator(parent = it, position = null))
    }
}

open class ButtonElement(val wrapped: Element) : Element(wrapped)
enum class ButtonType {
    button, reset, submit
}

fun ElementCreator<Element>.button(
    attributes: Map<String, Any> = emptyMap(),
    type: ButtonType? = ButtonType.button,
    autofocus: Boolean? = null,
    new: (ElementCreator<ButtonElement>.() -> Unit)? = null,
): ButtonElement {
    return ButtonElement(
        element(
            "button", attributes
                .set("type", type?.name)
                .set("autofocus", autofocus)
        )
    ).also {
        if (new != null) new(ElementCreator(parent = it, position = null))
    }
}

open class SpanElement(parent: Element) : Element(parent)

fun ElementCreator<Element>.span(
    attributes: Map<String, Any> = emptyMap(),
    new: (ElementCreator<SpanElement>.() -> Unit)? = null
): SpanElement {
    return SpanElement(element("span", attributes)).also {
        if (new != null) new(ElementCreator(parent = it, position = null))
    }
}

open class DivElement(parent: Element) : Element(parent)

fun ElementCreator<Element>.div(
    attributes: Map<String, Any> = emptyMap(),
    new: (ElementCreator<DivElement>.() -> Unit)? = null
): DivElement {
    return DivElement(element("div", attributes)).also {
        if (new != null) new(ElementCreator(parent = it, position = null))
    }
}

open class IElement(parent: Element) : Element(parent)

fun ElementCreator<Element>.i(
    attributes: Map<String, Any> = emptyMap(),
    new: (ElementCreator<IElement>.() -> Unit)? = null
): IElement {
    return IElement(element("i", attributes)).also {
        if (new != null) new(ElementCreator(parent = it, position = null))
    }
}

open class FormElement(parent: Element) : Element(parent)

fun ElementCreator<Element>.form(
    attributes: Map<String, Any> = emptyMap(),
    new: (ElementCreator<FormElement>.() -> Unit)? = null
): FormElement {
    return FormElement(element("form", attributes)).also {
        if (new != null) new(ElementCreator(parent = it, position = null))
    }
}

open class AElement(parent: Element) : Element(parent)

fun ElementCreator<Element>.a(
    attributes: Map<String, Any> = emptyMap(),
    href: String? = null,
    new: (ElementCreator<AElement>.() -> Unit)? = null
): AElement {
    return AElement(element("a", attributes.set("href", href))).also {
        if (new != null) new(ElementCreator(parent = it, position = null))
    }
}


open class OptionElement(parent: Element) : Element(parent)

fun ElementCreator<Element>.option(
    attributes: Map<String, Any> = emptyMap(),
    new: (ElementCreator<OptionElement>.() -> Unit)? = null
): OptionElement {
    return OptionElement(element("option", attributes)).also {
        if (new != null) new(ElementCreator(parent = it, position = null))
    }
}

open class H1Element(parent: Element) : Element(parent)

fun ElementCreator<Element>.h1(
    attributes: Map<String, Any> = emptyMap(),
    new: (ElementCreator<H1Element>.() -> Unit)? = null
): H1Element {
    return H1Element(element("h1", attributes)).also {
        if (new != null) new(ElementCreator(parent = it, position = null))
    }
}

open class H2Element(parent: Element) : Element(parent)

fun ElementCreator<Element>.h2(
    attributes: Map<String, Any> = emptyMap(),
    new: (ElementCreator<H2Element>.() -> Unit)? = null
): H2Element {
    return H2Element(element("h2", attributes)).also {
        if (new != null) new(ElementCreator(parent = it, position = null))
    }
}

open class H3Element(parent: Element) : Element(parent)

fun ElementCreator<Element>.h3(
    attributes: Map<String, Any> = emptyMap(),
    new: (ElementCreator<H3Element>.() -> Unit)? = null
): H3Element {
    return H3Element(element("h3", attributes)).also {
        if (new != null) new(ElementCreator(parent = it, position = null))
    }
}

open class H4Element(parent: Element) : Element(parent)

fun ElementCreator<Element>.h4(
    attributes: Map<String, Any> = emptyMap(),
    new: (ElementCreator<H4Element>.() -> Unit)? = null
): H4Element {
    return H4Element(element("h4", attributes)).also {
        if (new != null) new(ElementCreator(parent = it, position = null))
    }
}

open class H5Element(parent: Element) : Element(parent)

fun ElementCreator<Element>.h5(
    attributes: Map<String, Any> = emptyMap(),
    new: (ElementCreator<H5Element>.() -> Unit)? = null
): H5Element {
    return H5Element(element("h5", attributes)).also {
        if (new != null) new(ElementCreator(parent = it, position = null))
    }
}

open class PElement(parent: Element) : Element(parent)

fun ElementCreator<Element>.p(
    attributes: Map<String, Any> = emptyMap(),
    new: (ElementCreator<PElement>.() -> Unit)? = null
): PElement {
    return PElement(element("p", attributes)).also {
        if (new != null) new(ElementCreator(parent = it, position = null))
    }
}

open class NavElement(parent: Element) : Element(parent)

fun ElementCreator<Element>.nav(
    attributes: Map<String, Any> = emptyMap(),
    new: (ElementCreator<NavElement>.() -> Unit)? = null
): NavElement {
    return NavElement(element("nav", attributes)).also {
        if (new != null) new(ElementCreator(parent = it, position = null))
    }
}

open class SectionElement(parent: Element) : Element(parent)

fun ElementCreator<Element>.section(
    attributes: Map<String, Any> = emptyMap(),
    new: (ElementCreator<SectionElement>.() -> Unit)? = null
): SectionElement {
    return SectionElement(element("section", attributes)).also {
        if (new != null) new(ElementCreator(parent = it, position = null))
    }
}

open class ImageElement(parent: Element) : Element(parent)

fun ElementCreator<Element>.img(
    attributes: Map<String, Any> = emptyMap(),
    new: (ElementCreator<ImageElement>.() -> Unit)? = null
): ImageElement {
    return ImageElement(element("img", attributes)).also {
        if (new != null) new(ElementCreator(parent = it, position = null))
    }
}

open class CanvasElement(parent: Element) : Element(parent)

fun ElementCreator<Element>.canvas(
    attributes: Map<String, Any> = emptyMap(),
    width: Int, height: Int,
    new: (ElementCreator<CanvasElement>.() -> Unit)? = null
): CanvasElement {
    return CanvasElement(
        element(
            "canvas",
            attributes
                .set("width", width).set("height", height)
        )
    ).also {
        if (new != null) new(ElementCreator(parent = it, position = null))
    }
}

open class BrElement(parent: Element) : Element(parent)

fun ElementCreator<Element>.br(
    attributes: Map<String, Any> = emptyMap(),
    new: (ElementCreator<BrElement>.() -> Unit)? = null
): BrElement {
    return BrElement(element("br", attributes)).also {
        if (new != null) new(ElementCreator(parent = it, position = null))
    }
}

open class MetaElement(parent: Element) : Element(parent)

fun ElementCreator<Element>.meta(
    attributes: Map<String, Any> = emptyMap(),
    name: String, content: String, httpEquiv: String? = null, charset: String? = null,
    new: (ElementCreator<MetaElement>.() -> Unit)? = null
): MetaElement {
    return MetaElement(
        element(
            "meta", attributes.set("name", name)
                .set("content", content)
                .set("http-equiv", httpEquiv)
                .set("charset", charset)
        )
    ).also {
        if (new != null) new(ElementCreator(parent = it, position = null))
    }
}

open class InputElement(override val element: Element) : ValueElement(element) {
    fun checked(checked: Boolean) = if (checked) setAttributeRaw("checked", checked) else removeAttribute("checked")


    fun select() = element.execute("$jsExpression.select();")

    fun setSelectionRange(start: Int, end: Int) = element.execute("$jsExpression.setSelectionRange($start, $end);")

    fun setReadOnly(ro: Boolean) = element.execute("$jsExpression.readOnly = $ro;")
}

enum class InputType {
    button, checkbox, color, date, datetime, email, file, hidden, image, month, number, password, radio, range, reset, search, submit, tel, text, time, url, week
}

fun ElementCreator<Element>.input(
    attributes: Map<String, Any> = emptyMap(),
    type: InputType? = null,
    name: String? = null,
    initialValue: String? = null,
    size: Int? = null,
    placeholder: String? = null,
    new: (ElementCreator<InputElement>.() -> Unit)? = null
): InputElement {
    return InputElement(
        element(
            "input", attributes.set("type", type?.name)
                .set("name", name)
                .set("value", initialValue)
                .set("placeholder", placeholder)
                .set("size", size)
        )
    ).also {
        if (new != null) new(ElementCreator(parent = it, position = null))
    }
}

fun ElementCreator<Element>.textArea(
    attributes: Map<String, Any> = emptyMap(),
    rows: Int? = null, cols: Int? = null, required: Boolean? = null,
    new: (ElementCreator<TextAreaElement>.() -> Unit)? = null
): TextAreaElement {
    return TextAreaElement(
        element(
            "textArea", attributes.set("rows", rows)
                .set("cols", cols)
                .set("required", required)
        )
    ).also {
        if (new != null) new(ElementCreator(parent = it, position = null))
    }
}

open class SelectElement(parent: Element) : ValueElement(parent, kvarUpdateEvent = "change")

fun ElementCreator<Element>.select(
    attributes: Map<String, Any> = emptyMap(),
    name: String? = null, required: Boolean? = null,
    new: (ElementCreator<SelectElement>.() -> Unit)? = null
): SelectElement {
    return SelectElement(
        element(
            "select", attributes
                .set("name", name)
                .set("required", required)
        )
    ).also {
        if (new != null) new(ElementCreator(parent = it, position = null))
    }
}

open class TextAreaElement(parent: Element) : ValueElement(parent) {
    override val read get() = TextAreaElementReader(this)
}

fun ElementCreator<Element>.textArea(
    attributes: Map<String, Any> = emptyMap(),
    new: (ElementCreator<TextAreaElement>.() -> Unit)? = null
): TextAreaElement {
    return TextAreaElement(element("textArea", attributes)).also {
        if (new != null) new(ElementCreator(parent = it, position = null))
    }
}

open class TextAreaElementReader(element: TextAreaElement) : ElementReader(element) {
    val value get() = receiver.evaluate("($jsExpression.innerText);")
}

open class LabelElement(wrapped: Element) : Element(wrapped)

fun ElementCreator<Element>.label(
    attributes: Map<String, Any> = emptyMap(),
    new: (ElementCreator<LabelElement>.() -> Unit)? = null
): LabelElement {
    return LabelElement(element("label", attributes)).also {
        if (new != null) new(ElementCreator(parent = it, position = null))
    }
}

/**
 * Abstract class for the various elements that have a `value` attribute and which support `change` and `input` events.
 */
abstract class ValueElement(open val element: Element, val kvarUpdateEvent: String = "input") : Element(element) {
    fun getValue(): CompletableFuture<String> = element.evaluate("$jsExpression.value;") { it.toString() }
        ?: error("Not sure why .evaluate() would return null")

    fun setValue(newValue: String) = element.browser.execute("$jsExpression.value=${newValue.toJson()};")
    fun setValue(newValue: KVal<String>) {
        val initialValue = newValue.value
        setValue(initialValue)
        val listenerHandle = newValue.addListener { _, new ->
            setValue(new)
        }
        element.creator?.onCleanup(true) {
            newValue.removeListener(listenerHandle)
        }
    }

    private @Volatile
    var _valueKvar: KVar<String>? = null

    var value: KVar<String>
        get() {
            if (_valueKvar == null) {
                _valueKvar = KVar("")
            }
            return _valueKvar!!
        }
        set(v) {
            if (_valueKvar != null) error("`value` may only be set once, and cannot be set after it has been retrieved")
            setValue(v, updateOn = kvarUpdateEvent)
            _valueKvar = v
        }


    /**
     * Automatically update `toBind` with the value of this INPUT element when `updateOn` event occurs.
     */

    //I wasn't sure where this data class and function should go
    @Serializable
    data class DiffData(val prefixEnd: Int, val postFixOffset: Int, val diff: String)

    private fun applyDiff(oldString: String, diffData: DiffData) : String {
        return when {
            diffData.postFixOffset == 0 -> {//these 2 edge cases prevent the prefix or the postfix from being
                // repeated when you append text to the beginning of the text or the end of the text
                oldString.substring(0, diffData.prefixEnd) + diffData.diff
            }
            diffData.prefixEnd == 0 -> {
                diffData.diff + oldString.substring(oldString.length - 1 - diffData.postFixOffset)
            }
            else -> {
                oldString.substring(0, diffData.prefixEnd) + diffData.diff +
                        oldString.substring(oldString.length - 1 - diffData.postFixOffset)
            }
        }
    }

    fun setValue(toBind: KVar<String>, updateOn: String = "input") {
        setValue(toBind as KVal<String>)

        // TODO: Would be really nice if it just did a diff on the value and sent that, rather than the
        //       entire value each time PARTICULARLY for large inputs
        on(retrieveJs = "get_diff_changes(${jsExpression})").event(updateOn, Event::class) {
            val diffDataJson = it.retrieved ?: error("No diff data was retrieved")
            val diffData = Json.decodeFromString<DiffData>(diffDataJson)
            toBind.value = applyDiff(toBind.value, diffData)
        }
    }
}

/******************************
 * Route extension
 ******************************/

fun ElementCreator<*>.route(routeReceiver: RouteReceiver.() -> Unit) {
    val rr = RouteReceiver()
    routeReceiver(rr)
    val pathKVar: KVar<List<String>> = this.browser.url.map(UrlToPathSegmentsRF)
    val matchingTemplate: KVal<PathTemplate?> = pathKVar.map { path ->
        val size = if (path != listOf("")) path.size else 0
        val templatesOfSameLength = rr.templatesByLength[size]
        val tpl = templatesOfSameLength?.keys?.firstOrNull { tpl ->
            tpl.isEmpty() || tpl.withIndex().all {
                val tf = it.value.kind != RoutingPathSegmentKind.Constant || path[it.index] == it.value.value
                tf
            }
        }
        tpl
    }

    render(matchingTemplate) { template ->
        if (template != null) {
            val parameters = HashMap<String, KVar<String>>()
            for ((pos, part) in template.withIndex()) {
                if (part.kind == RoutingPathSegmentKind.Parameter) {
                    val str = part.value
                    val paramKVar = pathKVar[pos]
                    closeOnElementCreatorCleanup(paramKVar)
                    parameters[str.substring(str.indexOf('{') + 1, str.indexOf('}'))] = paramKVar
                }
            }

            val pathRenderer = rr.templatesByLength[template.size]?.get(template)

            if (pathRenderer != null) {
                pathRenderer(this, parameters)
            } else {
                error("Unable to find pathRenderer for template $template")
            }
        } else {
            rr.notFoundReceiver.invoke(this, this.browser.gurl.path.value)
        }
    }
}

/******************************
 * KVar extensions
 ******************************/

operator fun <T : Any> KVar<List<T>>.get(pos: Int): KVar<T> {
    return this.map(object : ReversibleFunction<List<T>, T>("get($pos)") {
        override fun invoke(from: List<T>): T {
            return try {
                from[pos]
            } catch (e: IndexOutOfBoundsException) {
                throw kotlin.IndexOutOfBoundsException("Index $pos out of bounds in list $from")
            }
        }

        override fun reverse(original: List<T>, change: T) = original
            .subList(0, pos)
            .plus(change)
            .plus(original.subList(pos + 1, original.size))
    })
}

operator fun <K : Any, V : Any> KVar<Map<K, V>>.get(k: K): KVar<V?> {
    return this.map(object : ReversibleFunction<Map<K, V>, V?>("map[$k]") {
        override fun invoke(from: Map<K, V>): V? = from[k]

        override fun reverse(original: Map<K, V>, change: V?): Map<K, V> {
            return if (change != null) {
                original + (k to change)
            } else {
                original - k
            }
        }
    })
}

fun <T : Any> KVar<List<T>>.subList(fromIx: Int, toIx: Int): KVar<List<T>> = this.map(object : ReversibleFunction<List<T>, List<T>>("subList($fromIx, $toIx)") {
    override fun invoke(from: List<T>): List<T> = from.subList(fromIx, toIx)

    override fun reverse(original: List<T>, change: List<T>): List<T> {
        return original.subList(0, fromIx) + change + original.subList(toIx, original.size)
    }
})

fun <T : Any> KVal<List<T>>.subList(fromIx: Int, toIx: Int): KVal<List<T>> = this.map { it.subList(fromIx, toIx) }

enum class Scheme {
    http, https
}

private val prx = "/".toRegex()

val KVar<URL>.path
    get() = this.map(object : ReversibleFunction<URL, String>("URL.path") {

        override fun invoke(from: URL): String = from.path()

        override fun reverse(original: URL, change: String): URL =
            original.withPath(change)

    })

val KVar<URL>.query
    get() = this.map(object : ReversibleFunction<URL, String?>("URL.query") {

        override fun invoke(from: URL): String? = from.query()

        override fun reverse(original: URL, change: String?): URL =
            original.withQuery(change)

    })

val KVar<URL>.pathSegments
    get() = this.map(object : ReversibleFunction<URL, List<String>>("URL.pathSegments") {

        override fun invoke(from: URL): List<String> {
            return from.pathSegments()
        }

        override fun reverse(original: URL, change: List<String>): URL {
            return original.withPath("/" + if (change.isEmpty()) "" else change.joinToString(separator = "/"))
        }

    })

/**
 * Given the URI specification:
 *
 * `URI = scheme:[//authority]path[?query][#fragment]`
 *
 * The `pqf` refers to the `path[?query][#fragment]` and can be used to change the path, query, and/or fragment
 * of the URL in one shot.
 */
val KVar<URL>.pathQueryFragment
    get() = this.map(object : ReversibleFunction<URL, String>("URL.pathQueryFragment") {
        override fun invoke(from: URL): String {
            return from.pathQueryFragment
        }

        override fun reverse(original: URL, change: String): URL {
            return original.resolve(change)
        }
    })

fun <A, B> Pair<KVar<A>, KVar<B>>.combine(): KVar<Pair<A, B>> {
    val newKVar = KVar(this.first.value to this.second.value)
    this.first.addListener { _, n -> newKVar.value = n to this.second.value }
    this.second.addListener { _, n -> newKVar.value = this.first.value to n }

    newKVar.addListener { o, n ->
        this.first.value = n.first
        this.second.value = n.second
    }
    return newKVar
}

infix operator fun KVar<String>.plus(s: String) = this.map { it + s }
infix operator fun String.plus(sKV: KVar<String>) = sKV.map { this + it }

fun KVar<String>.toInt() = this.map(object : ReversibleFunction<String, Int>(label = "KVar<String>.toInt()") {
    override fun invoke(from: String) = from.toInt()

    override fun reverse(original: String, change: Int): String {
        return change.toString()
    }
})

/**
 * Render each element of a List
 */
fun <T : Any> ElementCreator<*>.renderEach(list: KVar<List<T>>, block: ElementCreator<Element>.(value: KVar<T>) -> Unit) {
    /*
     * TODO: This will currently re-render the collection if the list size changes, rather than modifying existing
     *       DOM elements - this is inefficient.
     */
    render(list.map { it.size }) { size ->
        for (ix in 0 until size) {
            block(list[ix])
        }
    }
}

/**
 * Create a [FileReader](https://developer.mozilla.org/en-US/docs/Web/API/FileReader)
 *
 * @sample fileReaderSample
 */
fun ElementCreator<*>.fileInput(name: String? = null, initialValue: String? = null, size: Int? = null, placeholder: String? = null, attributes: Map<String, Any> = attr): FileFormInput {
    val inputElement = input(attributes, InputType.file, name, initialValue, size, placeholder)
    val formInput = FileFormInput()
    formInput.setInputElement(inputElement)
    return formInput
}

fun fileReaderSample() {
    val imageString = KVar("")
    Kweb(port = 123) {
        doc.body.new {
            val input = fileInput()
            input.onFileSelect {
                input.retrieveFile {
                    imageString.value = it.base64Content
                }
            }
            img().setAttribute("src", imageString)
        }
    }
}