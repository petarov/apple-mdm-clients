# Apple MDM Clients for Java

Java clients for Apple Mobile Device Management (MDM) services.

Among other technical goals, this library aims to use as few external dependencies as possible.

## Device Assignment Client

    Status: Work in progress ...

Manage your organization's Apple Business Manager (`ABM`) devices. Device assignment, previously known as "The Device Enrollment
Program `(DEP)`" allows for creating device enrollment profiles and assigning them to your organization's devices.

In order to use the Java client it is required that you already have created a valid device assignment token on ABM.
The private key with which the token must be decrypted is also required.

### Client Example 

```java
var builder = new DeviceAssignmentClientBuilder();
builder.setUserAgent("apple-mdm-device-assignment-v1");
builder.setServerToken(DeviceAssignmentServerToken.create(
		Path.of("<path-to-token>/token_file_smime.p7m"), 
		DeviceAssignmentPrivateKey.createFromDER(
				Path.of("<path-to-private-key>/private.der"))
var client = builder.build();
		
// Display account information
System.out.println(client.fetchAccount());

// Create a new profile and assign 2 device serial numbers to it
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

See the complete list of service API calls on Apple's [Device Assignment](https://developer.apple.com/documentation/devicemanagement/device-assignment) web page.

## App and Book Management Client

    Status: Planing

# Build

Requires [Java 21](https://adoptium.net/temurin/releases/) or later

    ./gradlew clean build

# License

[MIT License](LICENSE)

