# App and Book Management Client

Manage apps and books for students and employees through Apple's App and Book Management API (`mdm/v2`).

To use the Java client, you must already have a valid content token created in Apple Business Manager.
See [Getting started with the management API](https://developer.apple.com/documentation/devicemanagement/getting-started-with-the-management-api#Authenticate)
for help on setting things up.

See Apple's [App, Book, and Subscription Management](https://developer.apple.com/documentation/devicemanagement/app-book-and-subscription-management) documentation for the full list of API calls.

## Snapshots

Latest [SNAPSHOT](https://github.com/petarov/apple-mdm-clients/packages) built from the `main` branch. This requires an [authenticated](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry) GitHub user.

## Code Example

```java
var builder = AppAndBookClient.newBuilder();
builder.setUserAgent("my-mdm-app-vpp-v2");
builder.setServerToken(AppAndBookToken.create(
        Path.of("<path-to-token>/sToken_for_your_company.vpptoken")));

var client = builder.build();

// display all assets assigned to this token in ABM
var assets = client.fetchAssets();
assets.assets().forEach(asset -> System.out.println(
        asset.adamId() + " | " + asset.pricingParam() + " | available: " + asset.availableCount()));

// assign an app license to a device serial number and check the status of the asynchronous event
var event = client.associateAssets(Set.of(new RequestAsset("1234567890", PricingParam.STDQ)),
        Set.of(), Set.of("A9C1R3Q8KJA9"));
System.out.println(client.fetchEventStatus(event.eventId()).eventStatus());

// create a user and retire it later by its unique id
client.createUsers(Set.of(new RequestUser("client-100", "client-100@example.org", null)));
client.retireUsers(Set.of("client-100"));
```

## See also

See the [main README](../README.md) for proxy, logging, and build instructions.
