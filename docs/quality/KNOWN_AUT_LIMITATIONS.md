# Known Limitations

These are constraints of the public **Simple Grocery Store API** sandbox that this framework works around by design. None of them are framework defects — each was confirmed with a live API probe before the framework's behavior was decided, not assumed from REST conventions.

## Whole-cart deletion is not supported

**Impact:** every cart this suite creates remains on the sandbox indefinitely; there is no way to remove it.

**Framework behavior:** `DELETE /carts/{cartId}` returns the API's generic "route not found" response, not a business "cart not found" error — confirmed by comparing the two error shapes directly, and by a follow-up `GET` on the same cart succeeding immediately after the failed `DELETE`. Since cart IDs are always server-generated and never reused, repeated test runs remain safe (`REPEATABLE_VIA_UNIQUE_DATA`) even though the carts themselves are never cleaned up. Cart *items* can be removed individually (`DELETE /carts/{cartId}/items/{itemId}`, confirmed supported and used by `removeProductFromCartShouldSucceed`), but that doesn't remove the parent cart.

## API-client (registration) deletion is not supported

**Impact:** every client this suite registers (including the one `TokenManager` bootstraps per JVM) remains on the sandbox indefinitely.

**Framework behavior:** no delete route exists at `/api-clients`, `/api-clients/{token}`, or `/api-clients/me` — all three return the same route-not-found shape as the cart case above. `TestDataFactory` generates a collision-proof unique client name/email (timestamp + `AtomicLong` counter, proven collision-free at 10,000 concurrent generations) for every registration, so repeated runs never collide with a previous run's leftover clients.

## Order deletion is supported and is used

Unlike carts and clients, `DELETE /orders/{id}` is confirmed to work (`204`, verified by a follow-up `GET` returning `404`). The framework uses it: `OrderE2ETest` deletes the order it creates in a `finally` block (proven to still run and actually delete the order even when the test itself fails), and `OrderApiTest`'s lifecycle test deletes the order as part of what it's asserting. Orders do not accumulate across runs.

## Environment profiles currently resolve to one endpoint

**Impact:** selecting `dev`, `qa`, or `prod` (via `-Denv`) has no visible effect on which server is called today.

**Framework behavior:** all three profiles are configured to point at the same public sandbox (`https://simple-grocery-store-api.click`). The mechanism — profile resolution, fail-fast on an unsupported value, `-DbaseUrl` override — is fully implemented and tested; only the underlying URLs are currently identical. Selecting an unsupported environment name fails fast with a clear error before any request is sent, rather than silently defaulting.

## Availability and latency depend on the external sandbox

This is a shared public demo API, not a service the framework controls. Occasional latency or a transient error is possible at any time. The framework's retry policy (`GET` only, `502`/`503`/`504`, one retry, 1000ms delay) is deliberately scoped to that category of failure — see the README's [Retry Strategy](../../README.md#retry-strategy) section. It does **not** retry `500` responses: one transient `500` was observed during earlier development, but a `500` is also the primary signal for a genuine application defect, and the framework treats "don't risk masking a real bug" as more important than "recover from one historical anomaly." Across the 40-run / 380-test-execution qualification window described in the [Final Project Assessment](FINAL_PROJECT_ASSESSMENT.md), zero transient failures of any kind occurred — the sandbox was fully available throughout.
