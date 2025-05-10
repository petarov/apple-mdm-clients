package net.vexelon.mdm.da.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Describes a beta enrollment token available for the given organization.
 *
 * @param os    the platform related to beta build: {@code homePodOS}, {@code iOS}, {@code OSX}, {@code tvOS}, {@code visionOS}, {@code watchOS}
 * @param title the public facing name, like <i>"iOS 17 Public Beta"</i>
 * @param token the token to use when requesting the beta build
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/seedbuildtoken">SeedBuildToken</a>
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public record SeedBuildToken(String os, String title, String token) {}
