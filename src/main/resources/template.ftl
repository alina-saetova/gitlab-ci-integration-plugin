image: ${image}

<#if image == "openjdk:8-jdk">
variables:
  ANDROID_COMPILE_SDK: ${androidCompileSdk}
  ANDROID_BUILD_TOOLS: ${androidBuildTools}
  ANDROID_SDK_TOOLS: ${androidSdkTools}
</#if>

before_script:
- export GRADLE_USER_HOME=$(pwd)/.gradle
- chmod +x ./gradlew
<#if image == "openjdk:8-jdk">
- apt-get --quiet update --yes
- apt-get --quiet install --yes wget tar unzip lib32stdc++6 lib32z1
- wget --quiet --output-document=android-sdk.zip https://dl.google.com/android/repository/sdk-tools-linux-${androidSdkTools}.zip
- unzip -d android-sdk-linux android-sdk.zip
- echo y | android-sdk-linux/tools/bin/sdkmanager
"platforms;android-${androidCompileSdk}" >/dev/null
- echo y | android-sdk-linux/tools/bin/sdkmanager "platform-tools"
>/dev/null
- echo y | android-sdk-linux/tools/bin/sdkmanager
"build-tools;${androidBuildTools}" >/dev/null
- export ANDROID_HOME=$PWD/android-sdk-linux
- export PATH=$PATH:$PWD/android-sdk-linux/platform-tools/
- chmod +x ./gradlew
# temporarily disable checking for EPIPE error and use yes to accept
all licenses
- set +o pipefail
- yes | android-sdk-linux/tools/bin/sdkmanager --licenses
- set -o pipefail
</#if>

<#if cacheEnabled>
cache:
  <#if cacheKeyValue??>
  key: ${cacheKeyValue}
  </#if>
  paths:
  <#if gradleFilesCached>
   - .gradle/
  </#if>
  <#if customFilesCached>
   - .${cacheCustomFilePath}/
  </#if>
</#if>

stages:
  - lint
  - build

<#if lint == "ANDROIDLINT">
lintDebug:
<#elseif lint == "KTLINT">
ktlint:
<#elseif lint == "DETEKT">
detekt:
</#if>
  stage: lint
  script:
  <#if lint == "ANDROIDLINT">
    - ./gradlew -PbuildDir=lint
  <#elseif lint == "KTLINT">
    - ./gradlew ktlint
  <#elseif lint == "DETEKT">
    - ./gradlew detekt
  </#if>
  when: ${runningWhen}
  only:
    - ${runningOnly}
  <#if cachingEnabled>
  artifacts:
    when: ${cachingWhen}
    expire_in: ${cachingExpireIn}
    paths:
      - ${cachingPath}
  </#if>

build:
  stage: build
  script:
    - ./gradlew assembleDebug
  when: ${runningWhen}
  only:
    - ${runningOnly}
  <#if artifactEnabled>
  artifacts:
    when: ${whenArtifact}
    expire_in: ${expireInArtifact}
    paths:
      - app/build/outputs/
  </#if>
