lazy val root = project
  .in(file("."))
  .aggregate(core,`core-native`)


lazy val core = project
  .in(file("core"))
  .enablePlugins(JniJavah, JniLoad)
  .settings(
    sbtJniCoreScope := Compile,
    scalaVersion := "3.2.2",
    javah / target := (`core-native` / nativeCompile / sourceDirectory).value / "include",
    libraryDependencies ++= Seq(
      "fr.hammons" %% "slinc-runtime" % "0.1.1-110-7863cb"
    ),
    run / fork := true,
    javaOptions ++= Seq(
      "--enable-native-access=ALL-UNNAMED",
      "--add-modules=jdk.incubator.foreign"
    )
  )
  .dependsOn(`core-native` % Runtime)

lazy val `core-native` = project
  .in(file("core-native"))
  .settings(
    nativeCompile / sourceDirectory := sourceDirectory.value,
    //nativeCompile / target := target.value / "native" / nativePlatform.value
  )
  .enablePlugins(JniNative)

lazy val bench = project
  .in(file("bench"))
  .dependsOn(core % "test->test")
  .enablePlugins(JmhPlugin)
  .settings(
    scalaVersion := "3.2.2",
    Jmh / classDirectory := (Test / classDirectory).value,
    Jmh / sourceDirectory := (Test / sourceDirectory).value,
    Jmh / dependencyClasspath := (Test / dependencyClasspath).value,
    Jmh / compile := (Jmh / compile).dependsOn(Test / compile).value,
    Jmh / run := (Jmh / run).dependsOn(Jmh / compile).evaluated,
    Jmh / javaOptions ++= Seq(
      "--enable-native-access=ALL-UNNAMED",
      "--add-modules=jdk.incubator.foreign"
    )
  )
