package com.example.hotel_booking.statistics.service;

import com.example.hotel_booking.statistics.model.StatisticEventDocument;
import com.example.hotel_booking.statistics.repository.StatisticRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsExportService {

    private  final StatisticRepository repository;

    public byte[] exportToCsv() throws IOException {
        List<StatisticEventDocument> events = repository.findAll();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader(
                        "eventType",
                        "userId",
                        "checkInDate",
                        "checkOutDate",
                        "createdAt"
                )
                .build();

        CSVPrinter printer = new CSVPrinter(
                new OutputStreamWriter(out),
                format
        );

        for (StatisticEventDocument e: events){
            printer.printRecord(
                    e.getEventType(),
                    e.getUserId(),
                    e.getCheckInDate(),
                    e.getCheckOutDate(),
                    e.getCreatedAt()

            );
        }

        printer.flush();
        return out.toByteArray();
    }
}
