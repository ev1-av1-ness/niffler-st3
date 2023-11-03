package guru.qa.niffler.test.web;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.jupiter.annotation.*;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.GeneratedUser;
import guru.qa.niffler.jupiter.annotation.IncomeInvitation;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.jupiter.annotation.GeneratedUser.Selector.NESTED;
import static guru.qa.niffler.jupiter.annotation.GeneratedUser.Selector.OUTER;
import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;
import static io.qameta.allure.Allure.step;

@Disabled
public class FriendsWebTest extends BaseWebTest {

//    @BeforeEach
//    void doLogin(@User(userType = WITH_FRIENDS) UserJson userForTest) {
//        Selenide.open("http://127.0.0.1:3000/main");
//        $("a[href*='redirect']").click();
//        $("input[name='username']").setValue(userForTest.getUsername());
//        $("input[name='password']").setValue(userForTest.getPassword());
//        $("button[type='submit']").click();
//    }

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


    @ApiLogin(
            user = @GenerateUser(
                    incomeInvitations = @IncomeInvitation
            )
    )
    @GenerateUser
    @Test
    @AllureId("21324")
    void incomeInvitationShouldBePresentInTable(@GeneratedUser(selector = NESTED) UserJson userForTest,
                                                @GeneratedUser(selector = OUTER) UserJson another) {
        open(CFG.nifflerFrontUrl() + "/main");
        System.out.println();
    }

}
