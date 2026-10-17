package com.brightline.health;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
class HealthService {
    private final JsonLogRepository repo;
    private final HealthPolicyProperties p;

    HealthService(JsonLogRepository r, HealthPolicyProperties p) {
        repo = r;
        this.p = p;
    }

    HealthReport evaluate(Integer requestedWindow) {
        var all = repo.findAll();
        var now = all.stream().map(RequestLog::timestamp).max(Comparator.naturalOrder()).orElse(LocalDateTime.now());
        int mins = requestedWindow == null ? p.windowMinutes() : Math.max(1, Math.min(requestedWindow, 1440));
        var from = now.minusMinutes(mins);
        var names = all.stream().map(RequestLog::integration).collect(Collectors.toCollection(TreeSet::new));
        var out = new ArrayList<IntegrationHealth>();
        for (var name : names) {
            var history = all.stream().filter(x -> x.integration().equals(name)).toList();
            var rows = history.stream().filter(x -> !x.timestamp().isBefore(from)).toList();
            var last = history.stream().map(RequestLog::timestamp).max(Comparator.naturalOrder()).orElse(null);
            out.add(score(name, rows, last, now));
        }
        return new HealthReport(now, mins, out);
    }

    private IntegrationHealth score(String name, List<RequestLog> rows, LocalDateTime last, LocalDateTime now) {
        if (last == null || last.isBefore(now.minusMinutes(p.noDataMinutes())))
            return result(name, HealthStatus.NO_DATA, rows, last, List.of("NO_RECENT_REQUESTS"));
        long n = rows.size(), f = rows.stream().filter(RequestLog::failed).count(), t = rows.stream().filter(RequestLog::timeout).count();
        double er = (double) f / n, tr = (double) t / n;
        long p95 = percentile95(rows.stream().map(RequestLog::latencyMs).sorted().toList());
        var reasons = new ArrayList<String>();
        HealthStatus s;
        if (n < p.minimumRequests()) {
            s = HealthStatus.INSUFFICIENT_DATA;
            reasons.add("BELOW_MINIMUM_VOLUME");
        } else if (er >= p.unhealthyErrorRate() || tr >= p.unhealthyTimeoutRate() || p95 >= p.unhealthyP95Ms()) {
            s = HealthStatus.UNHEALTHY;
            if (er >= p.unhealthyErrorRate()) reasons.add("HIGH_ERROR_RATE");
            if (tr >= p.unhealthyTimeoutRate()) reasons.add("HIGH_TIMEOUT_RATE");
            if (p95 >= p.unhealthyP95Ms()) reasons.add("HIGH_P95_LATENCY");
        } else if (er >= p.degradedErrorRate() || tr >= p.degradedTimeoutRate() || p95 >= p.degradedP95Ms()) {
            s = HealthStatus.DEGRADED;
            if (er >= p.degradedErrorRate()) reasons.add("ELEVATED_ERROR_RATE");
            if (tr >= p.degradedTimeoutRate()) reasons.add("ELEVATED_TIMEOUT_RATE");
            if (p95 >= p.degradedP95Ms()) reasons.add("ELEVATED_P95_LATENCY");
        } else {
            s = HealthStatus.HEALTHY;
            reasons.add("WITHIN_CONFIGURED_THRESHOLDS");
        }
        return result(name, s, rows, last, reasons);
    }

    private IntegrationHealth result(String name, HealthStatus s, List<RequestLog> rows, LocalDateTime last, List<String> reasons) {
        long n = rows.size(), f = rows.stream().filter(RequestLog::failed).count(), t = rows.stream().filter(RequestLog::timeout).count();
        long p95 = percentile95(rows.stream().map(RequestLog::latencyMs).sorted().toList());
        var codes = rows.stream().collect(Collectors.groupingBy(x -> x.statusCode() == null ? "null" : String.valueOf(x.statusCode()), TreeMap::new, Collectors.counting()));
        return new IntegrationHealth(name, s, n, f, t, n == 0 ? 0 : round((double) f / n), n == 0 ? 0 : round((double) t / n), p95, last, codes, reasons);
    }

    private static double round(double x) {
        return Math.round(x * 10000.0) / 10000.0;
    }

    static long percentile95(List<Long> x) {
        if (x.isEmpty()) return 0;
        return x.get(Math.max(0, (int) Math.ceil(.95 * x.size()) - 1));
    }
}
