package guru.qa.niffler.test;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SENT;
import static io.qameta.allure.Allure.step;

public class InvitationSentWebTest extends BaseWebTest {

    @BeforeEach
    void doLogin(@User(userType = INVITATION_SENT) UserJson userForTest) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(userForTest.getUsername());
        $("input[name='password']").setValue(userForTest.getPassword());
        $("button[type='submit']").click();
    }

    @Test
    @AllureId("101")
    void invitationShouldBeDisplayedInTable5(@User(userType = INVITATION_SENT) UserJson userForTest) throws InterruptedException {
        step("Открыть страницу \"All people\"", ()->
                $("[data-tooltip-id='people']").click());

        step("Количество строк со статусом \"Pending invitation\" должно равняться 1", ()->
                $(".people-content").$("table")
                        .shouldBe(Condition.visible)
                        .$("tbody")
                        .$$("td")
                        .filterBy(text("Pending invitation"))
                        .shouldHave(CollectionCondition.size(1)));
    }

    @Test
    @AllureId("102")
    void invitationShouldBeDisplayedInTable6(@User(userType = INVITATION_SENT) UserJson userForTest) throws InterruptedException {
        step("Открыть страницу \"All people\"", ()->
                $("[data-tooltip-id='people']").click());

        step("Количество строк со статусом \"Pending invitation\" должно равняться 1", ()->
                $(".people-content").$("table")
                        .shouldBe(Condition.visible)
                        .$("tbody")
                        .$$("td")
                        .filterBy(text("Pending invitation"))
                        .shouldHave(CollectionCondition.size(1)));
    }
}
