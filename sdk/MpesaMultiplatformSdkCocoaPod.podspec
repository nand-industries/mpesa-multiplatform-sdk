Pod::Spec.new do |spec|
    spec.name                     = 'MpesaMultiplatformSdkCocoaPod'
    spec.version                  = '2.0.0'
    spec.homepage                 = 'https://github.com/nand-industries/mpesa-multiplatform-sdk'
    spec.source                   = { :http=> ''}
    spec.authors                  = 'Calebe Miquissene, Yazalde Filimone'
    spec.license                  = { :type => 'Apache-2.0' }
    spec.summary                  = 'Compose Multiplatform SDK for interacting with Vodacom M-Pesa APIs.'
    spec.vendored_frameworks      = 'build/cocoapods/framework/sdk.framework'
    spec.libraries                = 'c++'
    spec.ios.deployment_target    = '12.0'
                
                
    if !Dir.exist?('build/cocoapods/framework/sdk.framework') || Dir.empty?('build/cocoapods/framework/sdk.framework')
        raise "

        Kotlin framework 'sdk' doesn't exist yet, so a proper Xcode project can't be generated.
        'pod install' should be executed after running ':generateDummyFramework' Gradle task:

            ./gradlew :sdk:generateDummyFramework

        Alternatively, proper pod installation is performed during Gradle sync in the IDE (if Podfile location is set)"
    end
                
    spec.xcconfig = {
        'ENABLE_USER_SCRIPT_SANDBOXING' => 'NO',
    }
                
    spec.pod_target_xcconfig = {
        'KOTLIN_PROJECT_PATH' => ':sdk',
        'PRODUCT_MODULE_NAME' => 'sdk',
    }
                
    spec.script_phases = [
        {
            :name => 'Build MpesaMultiplatformSdkCocoaPod',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                if [ "YES" = "$OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED" ]; then
                  echo "Skipping Gradle build task invocation due to OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED environment variable set to \"YES\""
                  exit 0
                fi
                set -ev
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/../gradlew" -p "$REPO_ROOT" $KOTLIN_PROJECT_PATH:syncFramework \
                    -Pkotlin.native.cocoapods.platform=$PLATFORM_NAME \
                    -Pkotlin.native.cocoapods.archs="$ARCHS" \
                    -Pkotlin.native.cocoapods.configuration="$CONFIGURATION"
            SCRIPT
        }
    ]
    spec.resources = ['build/compose/cocoapods/compose-resources']
end