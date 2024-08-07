package brut.androlib.meta

/**
 * To parse YAML generated by apk-tool
 */
data class MetaInfo(
    var sdkInfo: SdkInfo? = null,
    var versionInfo: VersionInfo? = null
) {
    data class SdkInfo(
        var minSdkVersion: Int? = null,
        var targetSdkVersion: Int? = null
    )

    data class VersionInfo(
        var versionCode: Int? = null,
        var versionName: String? = null
    )
}