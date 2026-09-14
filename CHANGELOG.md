CHANGELOG
===========

[v1.1.0](https://github.com/petarov/apple-mdm-clients/releases/tag/1.1.0)

* Added `apple-business-client`: a new public client module for the Apple Business Manager API, see [apple-business-client/README.md](apple-business-client/README.md) for more info.
* DEP: bumped `X-Server-Protocol-Version` from 8 to 10.
* DEP device: added `eid`, `ethernetMacAddress`, `wifiMacAddress`, `bluetoothMacAddress`, `imei`, `meid`, `isReplacementDevice` and `isReleasedByReplacement` fields (require protocol version 10).
* DEP: added `fetchReplacementDetails` (Get Replacement Details) call.
* DEP: `fetchReplacementDetails` now returns an empty `Optional` instead of throwing when Apple responds with `400 DEVICE_NOT_FOUND` for a non-replacement device.
* DEP: added `assignAccountDrivenEnrollmentProfile`, `fetchAccountDrivenEnrollmentProfile` and `removeAccountDrivenEnrollmentProfile` device assignment calls.
* DEP: the assign profile call response now includes retry-after/throttle information.
* DEP: `deviceFamily`, `opType`, `os` and `profileStatus` on `Device`, `orgType` on `AccountDetail`, and `os` on `SeedBuildToken` are now enum types instead of `String`, falling back to `UNKNOWN` on null/unrecognized values.
* DEP: added more `DeviceFamily`/`DeviceOs` enum values: VisionOS, WatchOS, IPadOS, HomePodOS.
* DEP skip items: added `AccessibilityAppearance` (since iOS/iPadOS 17+).
* DEP skip items: added `LiquidGlass` (since iOS/iPadOS 27+).
* DEP skip items: added `DeviceFeaturesTour` (since iOS/iPadOS/macOS 27+).
* DEP skip items: removed `OSShowcase` (since iOS/iPadOS/macOS 27+).
* DEP skip items: removed `WebContentFiltering` (since iOS/iPadOS 26.1+).
* Renamed `setAppleHost` to `setAppleServiceUrl` on the Apple host builder, for consistency.
* Simplified how unknown/null enum values are deserialized, using `@JsonEnumDefaultValue` instead of a custom mechanism.
* Split README into per-subproject README files.
* Bump Jackson from 2.21.4 to 2.22.2
* Bump Bouncycastle from 1.84 to 1.85
* Bump SLF4J API from 2.0.18 to 2.0.19
* Bump JUnit Jupiter from 6.1.0 to 6.1.3
* Bump Gradle from 9.5.1 to 9.7.1
* Bump Maven Publish Plugin from 0.36.0 to 0.37.0
* Full Changelog: https://github.com/petarov/apple-mdm-clients/compare/1.0.0...1.1.0

[v1.0.0](https://github.com/petarov/apple-mdm-clients/releases/tag/1.0.0)

* DEP skip items: removed `AgeAssurance`.
* DEP skip items: removed `AgeBasedSafetySettings`.
* DEP skip items: removed deprecated `Wallpaper`.
* DEP skip items: `Accessibility` now scoped to macOS.
* DEP skip items: `Location` extended to tvOS and visionOS.
* Fixed a broken code example in the README.
* Bump Jackson from 2.21.1 to 2.21.4
* Bump Bouncycastle from 1.83 to 1.84
* Bump SLF4J API from 2.0.17 to 2.0.18
* Bump JUnit Jupiter from 6.0.3 to 6.1.0
* Bump Gradle from 9.4.0 to 9.5.1
* Full Changelog: https://github.com/petarov/apple-mdm-clients/compare/0.0.4...1.0.0

[v0.0.4 (pre-release)](https://github.com/petarov/apple-mdm-clients/releases/tag/0.0.4)
* Renamed boolean DEP profile fields: drop the `is_` prefix.
    ```
    is_return_to_service -> return_to_service
    is_mandatory -> mandatory
    is_mdm_removable -> mdm_removable
    is_multi_user -> multi_user
    is_supervised -> supervised
    ```
* Mark DEP profile `isMandatory` and `isSupervised` as deprecated and `forRemoval`.
* Bump Jackson from 2.21.0 to 2.21.1
* Bump Gradle from 9.3.1 to 9.4.0
* Full Changelog: https://github.com/petarov/apple-mdm-clients/compare/0.0.3...0.0.4

[v0.0.3 (pre-release)](https://github.com/petarov/apple-mdm-clients/releases/tag/0.0.3)

* DEP device: added support for `mdm_migration_deadline` field.
* DEP skip items: added `AgeAssurance`.
* DEP skip items: added `AgeBasedSafetySettings`.
* DEP skip items: extended visionOS support.
* DEP skip items: improved javadocs.
* Updated `X-Server-Protocol-Version` from 3 to 8.
* Bump Gradle from 8.9 to 9.3.1.
* Bump Jackson from 2.20.0 to 2.21.0.
* Bump Bouncycastle from 1.82 to 1.83.
* Bump JUnit Jupiter from 5.13.4 to 6.0.3.
* Bump Maven Publish Plugin from 0.34.0 to 0.36.0.
* Full Changelog: https://github.com/petarov/apple-mdm-clients/compare/0.0.2...0.0.3

[v0.0.2 (pre-release)](https://github.com/petarov/apple-mdm-clients/releases/tag/0.0.2)

* Switched from MIT to Apache, Version 2.0 license.
* Added `ofEmpty` static helper methods to various DEP and VPP model classes. This allows for no-args object creation, e.g., for Kotlin data classes and so on.
* DEP skip items: `is_mandatory` is marked as deprecated and `is_supervised` is added again but marked as deprecated. See commit messages for more info.
* DEP skip items: fixes a problem where an unknown item results in JSON deserialization exception.
* All models now have Jackson `ignoreUnknown = true` annotation declared.
* Added string and int helper constants to `DeviceAssignmentClient` and `LegacyAppAndBookClient`.
* Completely drop `javax.mail` and `bcmail` dependencies in favor of `bcpkix`.
* Bump jackson from 2.19.2 to 2.20.0 by @dependabot[bot] in https://github.com/petarov/apple-mdm-clients/pull/20
* Bump bouncycastle from 1.81 to 1.82 by @dependabot[bot] in https://github.com/petarov/apple-mdm-clients/pull/21
* Full Changelog: https://github.com/petarov/apple-mdm-clients/compare/0.0.1...0.0.2