package kweb

import io.github.bonigarcia.seljup.Arguments
import io.github.bonigarcia.seljup.SeleniumJupiter
import io.kotest.matchers.shouldBe
import kweb.*
import kweb.state.KVar
import org.awaitility.Awaitility
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ThreadGuard

@ExtendWith(SeleniumJupiter::class)
class HrefTest(@Arguments("--headless") private var unprotectedDriver: ChromeDriver) {

    val driver : WebDriver

    init {
        //ThreadGuard.protect ensures that the ChromeDriver can only be called by the thread that created it
        //This should make this test thread safe.
        driver = ThreadGuard.protect(unprotectedDriver)
    }

    companion object {
        private lateinit var hrefTestApp: HrefTestApp

        @JvmStatic
        @BeforeAll
        fun setupServer() {
            hrefTestApp = HrefTestApp()
        }

        @JvmStatic
        @AfterAll
        fun tearDownServer() {
            hrefTestApp.server.close()
        }
    }

    @Test
    fun testClick() {
        driver.get("http://localhost:7665/")
        val aElement = driver.findElement(By.tagName("a"))
        hrefTestApp.appUrl.value shouldBe "/"
        hrefTestApp.renderCount.value shouldBe 1
        aElement.click()
        Awaitility.await().untilAsserted { hrefTestApp.appUrl.value shouldBe "/two" }
        // Page shouldn't have been re-rendered for a relative link
        Awaitility.await().untilAsserted { hrefTestApp.renderCount.value shouldBe 1 }
    }


}

fun main() {
    HrefTestApp()
}

class HrefTestApp {

    lateinit var appUrl: KVar<String>

    val renderCount = KVar(0)

    val server: Kweb = Kweb(port = 7665) {
        appUrl = this.url
        doc.body {
            renderCount.value++
            route {
                path("/") {
                    a().let { a ->
                        a.href = "/two"
                        a.text("one")
                    }
                }
                path("/two") {

                }
            }
        }

    }
}
