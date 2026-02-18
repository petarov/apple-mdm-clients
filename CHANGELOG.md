CHANGELOG
===========

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