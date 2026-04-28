FROM openjdk:17-jdk-slim
ENV ANDROID_SDK_ROOT /opt/android-sdk
RUN apt-get update && apt-get install -y wget unzip
RUN wget https://dl.google.com/android/repository/commandlinetools-linux-8512546_latest.zip -O sdk.zip     && mkdir -p $ANDROID_SDK_ROOT/cmdline-tools     && unzip sdk.zip -d $ANDROID_SDK_ROOT/cmdline-tools     && mv $ANDROID_SDK_ROOT/cmdline-tools/cmdline-tools $ANDROID_SDK_ROOT/cmdline-tools/latest
RUN yes | $ANDROID_SDK_ROOT/cmdline-tools/latest/bin/sdkmanager --licenses
RUN $ANDROID_SDK_ROOT/cmdline-tools/latest/bin/sdkmanager "build-tools;34.0.0" "platforms;android-34"

WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew :app:assembleDebug

# Copy out the APK
CMD cp app/build/outputs/apk/debug/app-debug.apk /output/
