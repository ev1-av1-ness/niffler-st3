package guru.qa.niffler.test;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.User;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.jupiter.User.UserType.*;
import static io.qameta.allure.Allure.step;

public class SendInvitationAndInvitationReceivedWebTest extends BaseWebTest {

    @Test
    @AllureId("108")
    void sendInvitationAndInvitationReceivedWebTest(@User(userType = INVITATION_SENT) UserJson userForTest1, @User(userType = INVITATION_RECEIVED) UserJson userForTest2) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(userForTest1.getUsername());
        $("input[name='password']").setValue(userForTest1.getPassword());
        $("button[type='submit']").click();

        step("Открыть страницу \"Friends\"", ()->
                $("[data-tooltip-id='people']").click());

        step("Количество строк со статусом \"Pending invitation\" должно равняться 1", ()->
                $(".people-content").$("table")
                        .shouldBe(Condition.visible)
                        .$("tbody")
                        .$$("td")
                        .filterBy(text("Pending invitation"))
                        .shouldHave(CollectionCondition.size(1))
        );

        step("Разлогиниться", ()->
                $("[data-tooltip-id='logout']").click());

        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(userForTest2.getUsername());
        $("input[name='password']").setValue(userForTest2.getPassword());
        $("button[type='submit']").click();

        step("Открыть страницу \"All people\"", ()->
                $("[data-tooltip-id='people']").click());

        step("Количество кнопок \"Submit invitation\" должно равняться 1", ()->
                $(".people-content").$("table")
                        .shouldBe(Condition.visible)
                        .$("tbody")
                        .$$("[data-tooltip-id='submit-invitation']")
                        .shouldHave(CollectionCondition.size(1))
        );

        step("Количество кнопок \"Decline invitation\" должно равняться 1", ()->
                $(".people-content").$("table").shouldBe(Condition.visible)
                        .$("tbody")
                        .$$("[data-tooltip-id='decline-invitation']")
                        .shouldHave(CollectionCondition.size(1))
        );
    }
}
