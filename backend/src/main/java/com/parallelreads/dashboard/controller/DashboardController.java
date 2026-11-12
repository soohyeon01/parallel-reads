package com.parallelreads.dashboard.controller;

import com.parallelreads.dashboard.dto.BookDashboardResponse;
import com.parallelreads.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<List<BookDashboardResponse>> findDashboard() {
        return ResponseEntity.ok(dashboardService.findDashboard());
    }
}