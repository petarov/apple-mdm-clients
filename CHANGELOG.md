CHANGELOG
=============================

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


**Full Changelog**: https://github.com/petarov/apple-mdm-clients/compare/0.0.1...0.0.2