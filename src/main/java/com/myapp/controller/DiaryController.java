package com.myapp.controller;

import io.javalin.http.Context;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class DiaryController {

    // Structure: diary-data/YYYY/MM/DD.MM.YYYY.txt
    private static final Path DIARY_ROOT = Paths.get(System.getProperty("user.dir"), "diary-data");

    public void getMonths(Context ctx) {
        String year = ctx.pathParam("year");
        if (!isValidYear(year)) {
            ctx.status(400).result("Invalid year");
            return;
        }
        ctx.json(listSubfolders(DIARY_ROOT.resolve(year)));
    }

    public void getDays(Context ctx) {
        String year  = ctx.pathParam("year");
        String month = ctx.pathParam("month");
        if (!isValidYear(year) || !isValidMonth(month)) {
            ctx.status(400).result("Invalid year/month");
            return;
        }
        ctx.json(listDays(DIARY_ROOT.resolve(year).resolve(month)));
    }

    public void getEntry(Context ctx) {
        String year  = ctx.pathParam("year");
        String month = ctx.pathParam("month");
        String day   = ctx.pathParam("day");

        if (!isValidYear(year) || !isValidMonth(month) || !isValidDay(day)) {
            ctx.status(400).result("Invalid date");
            return;
        }

        String filename = day + "." + month + "." + year + ".txt";
        Path filePath = DIARY_ROOT.resolve(year).resolve(month).resolve(filename);

        if (!Files.exists(filePath)) {
            ctx.status(404).result("No entry found for this day");
            return;
        }

        try {
            String content = Files.readString(filePath, StandardCharsets.UTF_8);
            ctx.contentType("text/plain; charset=utf-8");
            ctx.result(content);
        } catch (IOException e) {
            ctx.status(500).result("Could not read entry");
        }
    }

    private List<String> listSubfolders(Path dir) {
        if (!Files.isDirectory(dir)) return List.of();
        try (Stream<Path> stream = Files.list(dir)) {
            return stream
                .filter(Files::isDirectory)
                .map(p -> p.getFileName().toString())
                .filter(name -> name.matches("\\d{2}"))
                .sorted()
                .collect(Collectors.toList());
        } catch (IOException e) {
            return List.of();
        }
    }

    private List<String> listDays(Path monthDir) {
        if (!Files.isDirectory(monthDir)) return List.of();
        try (Stream<Path> stream = Files.list(monthDir)) {
            return stream
                .filter(Files::isRegularFile)
                .map(p -> p.getFileName().toString())
                .filter(name -> name.matches("\\d{2}\\.\\d{2}\\.\\d{4}\\.txt"))
                .map(name -> name.substring(0, 2))
                .sorted()
                .collect(Collectors.toList());
        } catch (IOException e) {
            return List.of();
        }
    }

    private boolean isValidYear(String y)  { return y != null && y.matches("\\d{4}"); }
    private boolean isValidMonth(String m) { return m != null && m.matches("0[1-9]|1[0-2]"); }
    private boolean isValidDay(String d)   { return d != null && d.matches("0[1-9]|[12]\\d|3[01]"); }
}