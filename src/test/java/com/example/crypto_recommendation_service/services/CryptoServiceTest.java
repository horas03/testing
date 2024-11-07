package com.example.crypto_recommendation_service.services;

import com.example.crypto_recommendation_service.domain.model.CryptoRange;
import com.example.crypto_recommendation_service.domain.model.MetricsDto;
import com.example.crypto_recommendation_service.domain.model.MetricsDataDto;
import com.example.crypto_recommendation_service.entities.Crypto;
import com.example.crypto_recommendation_service.entities.Timeframe;
import com.example.crypto_recommendation_service.exceptions.BusinessException;
import com.example.crypto_recommendation_service.repositories.CryptoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CryptoServiceTest {

    @Mock
    private CryptoRepository cryptoRepository;

    @InjectMocks
    private CryptoService cryptoService;

    private final List<Crypto> cryptoList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        cryptoList.add(Crypto.builder().timestamp(1627862400000L).symbol("BTC").price(45000.0).build()); // 2021-08-02 00:00:00
        cryptoList.add(Crypto.builder().timestamp(1627995600000L).symbol("BTC").price(46000.0).build()); // 2021-08-03 16:00:00
        cryptoList.add(Crypto.builder().timestamp(1627891200000L).symbol("BTC").price(45500.0).build()); // 2021-08-02 08:00:00
    }

    @Test
    void getCryptoWithHighestNormalizedRange_ShouldReturnCryptoWithHighestRange() {
        LocalDate specificDate = LocalDate.of(2021, 8, 2);

        doReturn(cryptoList).when(cryptoRepository).findAll();

        CryptoRange result = cryptoService.getCryptoWithHighestNormalizedRange(specificDate);

        verify(cryptoRepository, times(1)).findAll();

        assertNotNull(result, "Result should not be null");
        assertEquals("BTC", result.getSymbol(), "Symbol should match BTC");
        assertTrue(result.getRange() > 0, "Normalized range should be greater than 0");
    }

    @Test
    void getAllCryptosSortedByNormalizedRange_ShouldReturnSortedCryptoDtos() {
        when(cryptoRepository.findAll()).thenReturn(cryptoList);

        List<CryptoRange> result = cryptoService.getAllCryptosSortedByNormalizedRange(Timeframe.FIVE_YEARS);

        assertFalse(result.isEmpty());
        verify(cryptoRepository, times(1)).findAll();
    }

    @Test
    void getCryptoMetrics_ShouldReturnMetricsDto() {
        String symbol = "BTC";
        when(cryptoRepository.findAll()).thenReturn(cryptoList);

        MetricsDto metricsDto = cryptoService.getCryptoMetrics(symbol, Timeframe.FIVE_YEARS);

        assertEquals(symbol, metricsDto.getSymbol());
        assertNotNull(metricsDto);

        MetricsDataDto metricsData = metricsDto.getMetricsDataDto();
        assertEquals(45000.0, metricsData.getMin());
        assertEquals(46000.0, metricsData.getMax());
        assertNotNull(metricsData.getNewest());
        assertNotNull(metricsData.getOldest());
    }

    @Test
    void getCryptoMetrics_ShouldThrowBusinessException_WhenNoDataForSymbol() {
        when(cryptoRepository.findAll()).thenReturn(List.of());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> cryptoService.getCryptoMetrics("BTC", Timeframe.ONE_MONTH));
        assertEquals("There is no data for symbol BTC", exception.getMessage());
    }



    @Test
    @SuppressWarnings("unchecked")
    void testFilterByTimeframe() throws Exception {

        Method method = CryptoService.class.getDeclaredMethod("filterByTimeframe", List.class, String.class);
        method.setAccessible(true);

        List<Crypto> filteredCryptos = (List<Crypto>) method.invoke(cryptoService, cryptoList, "1-month");

        assertTrue(filteredCryptos.stream().allMatch(
                crypto -> crypto.getFormattedTimestamp().isAfter(LocalDateTime.now().minusMonths(1))));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetMinAndMax() throws Exception {

        Method method = CryptoService.class.getDeclaredMethod("getMinAndMax", List.class);
        method.setAccessible(true);

        double expectedMin = cryptoList.stream().mapToDouble(Crypto::getPrice).min().orElse(Double.NaN);
        double expectedMax = cryptoList.stream().mapToDouble(Crypto::getPrice).max().orElse(Double.NaN);

        Map<String, Double> minMax = (Map<String, Double>) method.invoke(cryptoService, cryptoList);

        assertEquals(expectedMin, minMax.get("min"));
        assertEquals(expectedMax, minMax.get("max"));
    }

    @Test
    void testGetNormalizedRange() throws Exception {
        Method method = CryptoService.class.getDeclaredMethod("getNormalizedRange", List.class);
        method.setAccessible(true);

        double expectedMin = cryptoList.stream().mapToDouble(Crypto::getPrice).min().orElse(Double.NaN);
        double expectedMax = cryptoList.stream().mapToDouble(Crypto::getPrice).max().orElse(Double.NaN);

        assertEquals((expectedMax - expectedMin) / expectedMin, method.invoke(cryptoService, cryptoList));
    }

}

