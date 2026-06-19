# Apple Business client

Automate device management activities, view device information, and manage users and user groups using the Apple Business API.

To use the Java client, you must already have a valid API account in Apple Business with the corresponding private key. 

See Apple's [Apple Business API](https://developer.apple.com/documentation/applebusinessapi) documentation for the full list of supported API calls.

## Snapshots

Latest [SNAPSHOT](https://github.com/petarov/apple-mdm-clients/packages) built from the `main` branch. This requires an [authenticated](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry) GitHub user.

## Code Example

```java
var builder = AppleBusinessClient.newBuilder();
builder.setClientId("BUSINESSAPI.<your-client-id>");
builder.setKeyId("<your-key-id>");
builder.setPrivateKey(AppleBusinessPrivateKey.createFromPEM(
        Path.of("<path-to-private-key>/private_key.pem")));

var client = builder.build();

// display all devices in the organization
var response = client.fetchOrgDevices();
response.data().forEach(device -> System.out.println(
        device.id() + " | " + device.attributes().productFamily() + " | " + device.attributes().status()));

var paging = response.meta().paging();
if (!paging.nextCursor().isBlank()) {
    System.out.println("Next cursor: " + paging.nextCursor());
}
```

## See also

See the [main README](../README.md) for proxy, logging, and build instructions.
