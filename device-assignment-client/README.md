# Device Assignment Client

[![MvnRepository](https://badges.mvnrepository.com/badge/net.vexelon.mdm/device-assignment-client/badge.svg?label=MvnRepository)](https://mvnrepository.com/artifact/net.vexelon.mdm/device-assignment-client)

Manage your organization's Apple Business Manager (`ABM`) devices. Automated Device Enrollment `(ADE)`, previously known
as "The Device Enrollment Program" `(DEP)`, allows you to create device enrollment profiles and assign them to your
organization's devices.

To use the Java client, you must already have a valid device assignment token created in Apple Business Manager, 
together with the private key required to decrypt the token. See [Get an Automated Device Enrollment program server token](https://developer.apple.com/documentation/devicemanagement/authenticating-with-the-automated-device-enrollment-program#Get-an-Automated-Device-Enrollment-program-server-token)
for help on setting things up.

See Apple's [Device Assignment](https://developer.apple.com/documentation/devicemanagement/device-assignment) documentation for the full list of supported API calls.

## Releases

    implementation 'net.vexelon.mdm:device-assignment-client:1.0.0'

```xml
<dependency>
    <groupId>net.vexelon.mdm</groupId>
    <artifactId>device-assignment-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Snapshots

Latest [SNAPSHOT](https://github.com/petarov/apple-mdm-clients/packages/2517819) built from the `main` branch. This requires an [authenticated](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry) GitHub user.

## Code Example

```java
var builder = DeviceAssignmentClient.newBuilder();
builder.setUserAgent("my-mdm-app-dep-v1");
builder.setServerToken(DeviceAssignmentServerToken.create(
        Path.of("<path-to-token>/token_file_smime.p7m"),
        DeviceAssignmentPrivateKey.createFromDER(
                Path.of("<path-to-private-key>/private.der"))

var client = builder.build();

// display account information
System.out.println(client.fetchAccount());

// create a new profile and assign 2 device serial numbers to it
var response = client.createProfile(new Profile.ProfileBuilder()
    .setProfileName("mdm-server-01-sales-profile")
    .setUrl("https://mdm-server-01.local")
    .setDepartment("Sales")
    .setAwaitDeviceConfigured(true)
    .setMdmRemovable(true)
    .setSupportPhoneNumber("555-555-555")
    .setSupportEmailAddress("sales-it@example.org")
    .setDevices(Set.of("A9C1R3Q8KJA9", "B112R4L8KJC7"))
    .setSkipSetupItems(EnumSet.of(ProfileSkipItem.ENABLE_LOCKDOWN_MODE,
        ProfileSkipItem.TAP_TO_SETUP, ProfileSkipItem.ICLOUD_DIAGNOSTICS,
        ProfileSkipItem.ICLOUD_STORAGE)).build());
System.out.println(response.profileUuid());
```

## See also

See the [main README](../README.md) for proxy, logging, and build instructions.
