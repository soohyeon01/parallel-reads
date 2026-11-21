package com.parallelreads.dashboard.controller;

import com.parallelreads.dashboard.dto.BookDashboardResponse;
import com.parallelreads.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public List<BookDashboardResponse> findDashboard() {
        return dashboardService.findDashboard();
    }
}