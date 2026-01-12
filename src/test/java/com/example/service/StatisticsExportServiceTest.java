package com.example.service;

import com.example.hotel_booking.statistics.model.StatisticEventDocument;
import com.example.hotel_booking.statistics.repository.StatisticRepository;
import com.example.hotel_booking.statistics.service.StatisticsExportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatisticsExportServiceTest {

    @Mock
    private StatisticRepository statisticRepository;

    @InjectMocks
    private StatisticsExportService exportService;

    @Test
    void exportToCsv_whenEventsExist_returnsValidCsv() throws IOException {
        StatisticEventDocument event = StatisticEventDocument.builder()
                .eventType("USER_REGISTERED")
                .userId(1L)
                .createdAt(Instant.now())
                .build();

        when(statisticRepository.findAll()).thenReturn(List.of(event));

        byte[] csvData = exportService.exportToCsv();
        String content = new String(csvData);

        assertNotNull(csvData);
        assertTrue(csvData.length > 0);
        assertTrue(content.contains("USER_REGISTERED"));
        assertTrue(content.contains("userId"));

        verify(statisticRepository, times(1)).findAll();
    }
}
