## 2. Observed baseline

| Integration            | Failures | Error rate | Timeouts | p95 ms | HTTP pattern                    | Proposed status |
|------------------------|---------:|-----------:|---------:|-------:|---------------------------------|-----------------|
| Slack Notifications    |     1/12 |       8.3% |        0 |    310 | {'200': 11, '500': 1}           | HEALTHY         |
| ADP Payroll Sync       |     5/12 |      41.7% |        0 |    430 | {'200': 7, '500': 5}            | UNHEALTHY       |
| DocuSign Envelope API  |     4/12 |      33.3% |        0 |    900 | {'200': 8, '429': 4}            | UNHEALTHY       |
| Workday Directory Sync |     1/12 |       8.3% |        0 |    280 | {'200': 11, '503': 1}           | HEALTHY         |
| Greenhouse ATS Sync    |     6/12 |      50.0% |        5 |  30000 | {'200': 6, 'None': 5, '500': 1} | UNHEALTHY       |

## 3. Dataset-calibrated policy