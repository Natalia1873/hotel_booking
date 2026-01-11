package com.example.hotel_booking.statistics.controller;


import com.example.hotel_booking.statistics.service.StatisticsExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/statistics")
public class StatisticsController {

    private final StatisticsExportService exportService;

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportCsV() throws IOException {

        byte[] csv = exportService.exportToCsv();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=statistics.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv);
    }
}
