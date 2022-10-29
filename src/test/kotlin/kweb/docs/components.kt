package kweb.docs

import kweb.ElementCreator
import kweb.*
import kweb.InputType.text
import kweb.state.*
import kweb.util.json

// ANCHOR: simple_component
class SimpleComponent(
    val prompt: String = "Enter Your Name",
    val name: KVar<String>
) : Component<Unit> {
    override fun render(elementCreator: ElementCreator<*>) {
        with(elementCreator) {
            div {
                h1().text(prompt)
                input(type = text).value = name
            }
            div {
                span().text(name.map { "Hello, $it" })
            }
        }
    }
}
// ANCHOR_END: simple_component

fun simple_component() {
    // ANCHOR: component_usage
Kweb(port = 16097) {
    doc.body.new {
        render(SimpleComponent(name = kvar("World")))
    }
}
    // ANCHOR_END: component_usage
}


// ANCHOR: component_input_example
class BulmaInput(
    val type : InputType,
    val color: KVal<BulmaColor>? = null,
    val size: KVal<BulmaSize>? = null,
    val style: KVal<BulmaStyle>? = null,
    val state: KVal<BulmaState>? = null,
    val disabled: KVal<Boolean>? = null,
    val value: KVar<String>
) : Component<InputElement> {
    override fun render(elementCreator: ElementCreator<*>) : InputElement {
        with(elementCreator) {
            val renderedInput = input(type = type) {
                var inputClassList: KVal<List<String>> = kval(listOf("input"))
                element {

                    if (color != null) {
                        inputClassList += color.map { listOf(it.className) }
                    }
                    if (size != null) {
                        inputClassList += size.map { listOf(it.className) }
                    }
                    if (this@BulmaInput.style != null) {
                        inputClassList += this@BulmaInput.style.map { listOf(it.className) }
                    }
                    if (state != null) {
                        inputClassList += state.map { listOf(it.className) }
                    }

                    if (disabled != null) {
                        this["disabled"] = disabled.map { it.json }
                    }

                    classes(inputClassList.map { it.joinToString(" ") })

                    this.value = this@BulmaInput.value

                }
            }

            return renderedInput
        }
    }

    enum class BulmaColor(val className: String) {
        PRIMARY("is-primary"),
        LINK("is-link"),
        INFO("is-info"),
        SUCCESS("is-success"),
        WARNING("is-warning"),
        DANGER("is-danger")
    }

    enum class BulmaSize(val className: String) {
        SMALL("is-small"),
        NORMAL("is-normal"),
        MEDIUM("is-medium"),
        LARGE("is-large")
    }

    enum class BulmaStyle(val className: String) {
        ROUNDED("is-rounded"),
        FOCUSED("is-focused")
    }

    enum class BulmaState(val className: String) {
        NORMAL("is-normal"),
        HOVER("is-hovered"),
        FOCUS("is-focused"),
        LOADING("is-loading"),
    }
}
// ANCHOR_END: component_input_example

fun editableFieldExample1() {
    Kweb(port = 12354) {
        doc.body {
            val username = kvar("")
            render(BulmaInput(type = text, value = username))
        }
    }
}