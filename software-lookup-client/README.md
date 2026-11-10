# Software Lookup Service Client

Look up available software updates and upgrades for Apple devices through Apple's Software Lookup Service.
The service requires no authentication.

## Code Example

```java
var client = SoftwareLookupServiceClient.newBuilder()
        .setUserAgent("my-mdm-app-v2")
        .build();

var releases = client.fetchSoftwareReleases();

// iOS family releases a device management service can push (also iPadOS, tvOS, watchOS and HomePod)
releases.assetSets().ios().forEach(release -> System.out.println(
        release.productVersion() + " (" + release.build() + ") posted: " + release.postingDate()
                + " devices: " + release.supportedDevices()));
```

## See also

See the [main README](../README.md) for proxy, logging, and build instructions.
