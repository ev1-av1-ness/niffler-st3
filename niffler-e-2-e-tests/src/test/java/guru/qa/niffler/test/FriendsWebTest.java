package guru.qa.niffler.test;


import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.jupiter.User;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.jupiter.User.UserType.WITH_FRIENDS;
import static io.qameta.allure.Allure.step;

public class FriendsWebTest extends BaseWebTest {

    @BeforeEach
    void doLogin(@User(userType = WITH_FRIENDS) UserJson userForTest) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(userForTest.getUsername());
        $("input[name='password']").setValue(userForTest.getPassword());
        $("button[type='submit']").click();
    }

    @Test
    @AllureId("105")
    void friendShouldBeDisplayedInTable1(@User(userType = WITH_FRIENDS) UserJson userForTest) throws InterruptedException {
        step("Открыть страницу \"Friends\"", ()->
                $("[data-tooltip-id='friends']").click());

        SelenideElement friendsTable =
                $(".people-content")
                        .$("table")
                        .shouldBe(Condition.visible);

        friendsTable.$("tbody").$$("tr")
                .shouldHave(CollectionCondition.size(1));
        friendsTable.$("tbody").$$("td")
                .filterBy(text("You are friends")).shouldHave(CollectionCondition.size(1));
    }

    @Test
    @AllureId("106")
    void friendShouldBeDisplayedInTable2(@User(userType = WITH_FRIENDS) UserJson userForTest) throws InterruptedException {
        step("Открыть страницу \"Friends\"", ()->
                $("[data-tooltip-id='friends']").click());

        SelenideElement friendsTable =
                $(".people-content")
                        .$("table")
                        .shouldBe(Condition.visible);

        friendsTable.$("tbody").$$("tr")
                .shouldHave(CollectionCondition.size(1));
        friendsTable.$("tbody").$$("td")
                .filterBy(text("You are friends")).shouldHave(CollectionCondition.size(1));
    }
}
