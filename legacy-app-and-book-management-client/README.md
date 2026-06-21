# Legacy App and Book Management Client

[![MvnRepository](https://badges.mvnrepository.com/badge/net.vexelon.mdm/legacy-app-and-book-management-client/badge.svg?label=MvnRepository)](https://mvnrepository.com/artifact/net.vexelon.mdm/legacy-app-and-book-management-client)

Manage apps and books for students and employees. This API is still functional, however, Apple has deprecated it and
no longer maintains it.

To use the Java client, you must already have a valid content token created in Apple Business Manager.
See [Getting started with the management API](https://developer.apple.com/documentation/devicemanagement/getting-started-with-the-management-api#Authenticate)
for help on setting things up.

See Apple's [App and book management (Legacy)](https://developer.apple.com/documentation/devicemanagement/app-and-book-management-legacy) documentation for the full list of supported API calls.

## Releases

    implementation 'net.vexelon.mdm:legacy-app-and-book-management-client:1.0.0'

```xml
<dependency>
    <groupId>net.vexelon.mdm</groupId>
    <artifactId>legacy-app-and-book-management-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Snapshots

Latest [SNAPSHOT](https://github.com/petarov/apple-mdm-clients/packages/2517820) built from the `main` branch. This requires an [authenticated](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry) GitHub user.

## Code Example

```java
var builder = LegacyAppAndBookClient.newBuilder();
builder.setUserAgent("my-mdm-app-vpp-v1");
builder.setServerToken(LegacyAppAndBookToken.create(
        Path.of("<path-to-token>/sToken_for_your_company.vpptoken")));

var client = builder.build();

// display all assets assigned to this token in ABM
System.out.println(client.fetchAssets(false));

// retire a user by its unique id
System.out.println(client.retireUser(UserIdParam.of("MTY6MzAgZXN0YXIgbm8gbG9jYWwgZGV0ZXJtaW5hZG8=")));
```

## See also

See the [main README](../README.md) for proxy, logging, and build instructions.
