package guru.qa.niffler.test;

import guru.qa.grpc.niffler.grpc.*;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static guru.qa.grpc.niffler.grpc.CurrencyValues.*;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;

public class NifflerCurrencyGrpcTest extends BaseGrpcTest {
    static Stream<Arguments> getAllCurrenciesTest() {
        return Stream.of(
                Arguments.of(RUB, 0.015),
                Arguments.of(KZT, 0.0021),
                Arguments.of(EUR, 1.08),
                Arguments.of(USD, 1.0)
        );
    }

    @MethodSource
    @AllureId("901")
    @ParameterizedTest(name = "The currency {0} has a rate {1}")
    void getAllCurrenciesTest(CurrencyValues currency,
                              double expectedRate) {
        CurrencyResponse allCurrencies = step("Get all currencies", () ->
                currencyStub.getAllCurrencies(EMPTY)
        );
        final List<Currency> currenciesList = allCurrencies.getAllCurrenciesList();

        step("Check that response contains the currency", () -> assertEquals(4, currenciesList.size()));

        step("Check the currency rate", () -> assertEquals(expectedRate, currenciesList.stream()
                .filter(c -> c.getCurrency() == currency)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Currency not found in the response")).getCurrencyRate()));
    }


    static Stream<Arguments> calculateCurrencyRateTest() {
        return Stream.of(
                Arguments.of(CurrencyValues.USD, CurrencyValues.RUB, 200.0, 7777.77),
                Arguments.of(CurrencyValues.RUB, CurrencyValues.USD, 150.0, 2.0),
                Arguments.of(CurrencyValues.USD, CurrencyValues.USD, 200.0, 200.0),
                Arguments.of(CurrencyValues.USD, CurrencyValues.KZT, 200.0, 95238.10),
                Arguments.of(CurrencyValues.KZT, CurrencyValues.EUR, 200.0, 0.38),
                Arguments.of(CurrencyValues.KZT, CurrencyValues.USD, -200.0, -0.42),
                Arguments.of(CurrencyValues.USD, CurrencyValues.KZT, -200.0, -95238.10)
        );
    }

    @MethodSource
    @AllureId("902")
    @ParameterizedTest(name = "When recalculating values from {0} to {1} the sum of {2}," +
            " the result {3} should be returned")
    void calculateCurrencyRateTest(CurrencyValues spendCurrency,
                                            CurrencyValues desiredCurrency,
                                            double amount,
                                            double expected) {
        CalculateRequest requestRate = CalculateRequest.newBuilder()
                .setAmount(amount)
                .setSpendCurrency(spendCurrency)
                .setDesiredCurrency(desiredCurrency)
                .build();

        final CalculateResponse calculateResponse = step("Get all currencies", () ->
                currencyStub.calculateRate(requestRate)
        );

        step("Check calculated rate", () ->
                assertEquals(expected, calculateResponse.getCalculatedAmount())
        );
    }

    @Test
    @AllureId("900")
    void calculateRateWithZeroTest() {
        CalculateRequest request = CalculateRequest.newBuilder()
                .setAmount(0)
                .setSpendCurrency(CurrencyValues.RUB)
                .setDesiredCurrency(CurrencyValues.KZT)
                .build();
        final CalculateResponse calculateResponse = currencyStub.calculateRate(request);
        assertEquals(0, calculateResponse.getCalculatedAmount());
    }
}
