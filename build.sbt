lazy val root = project
  .in(file("."))
  .aggregate(`core-01`, `core-native`)

val foreignAPISettings = Seq(
  javaOptions ++= Seq(
    "--enable-native-access=ALL-UNNAMED"
    // for JDK 17. Comment out when using JDK 19.
    // "--add-modules=jdk.incubator.foreign"
  )
)

val slinc03Settings = Seq(
  scalaVersion := "3.3.0-RC3",
  libraryDependencies ++= Seq(
    "fr.hammons" %% "slinc-runtime" % "0.3.0"
  )
)

val slinc01Settings = Seq(
  scalaVersion := "3.3.0-RC3",
  libraryDependencies ++= Seq(
    "fr.hammons" %% "slinc-runtime" % "0.1.1-110-7863cb"
  )
)

lazy val `core-01` = project
  .in(file("core-0.1"))
  .enablePlugins(JniJavah, JniLoad)
  .settings(
    sbtJniCoreScope := Compile,
    javah / target := (`core-native` / nativeCompile / sourceDirectory).value / "include",
    run / fork := true
  )
  .settings(slinc01Settings)
  .settings(foreignAPISettings)
  .dependsOn(`core-native` % Runtime)

lazy val `core-03` = project
  .in(file("core-0.3"))
  .enablePlugins(JniJavah, JniLoad)
  .settings(
    sbtJniCoreScope := Compile,
    javah / target := (`core-native` / nativeCompile / sourceDirectory).value / "include",
    run / fork := true
  )
  .settings(slinc03Settings)
  .settings(foreignAPISettings)
  .dependsOn(`core-native` % Runtime)
lazy val `core-native` = project
  .in(file("core-native"))
  .settings(
    nativeCompile / sourceDirectory := sourceDirectory.value
    // nativeCompile / target := target.value / "native" / nativePlatform.value
  )
  .enablePlugins(JniNative)

lazy val bench01 = project
  .in(file("bench-0.1"))
  .dependsOn(`core-01` % "test->test;compile->compile")
  .enablePlugins(JmhPlugin)
  .settings(slinc01Settings)
  .settings(
    Jmh / classDirectory := (Test / classDirectory).value,
    Jmh / sourceDirectory := (Test / sourceDirectory).value,
    Jmh / dependencyClasspath := (Test / dependencyClasspath).value,
    Jmh / compile := (Jmh / compile).dependsOn(Test / compile).value,
    Jmh / run := (Jmh / run).dependsOn(Jmh / compile).evaluated,
    Jmh / javaOptions ++= Seq(
      "--enable-native-access=ALL-UNNAMED"
      // for JDK 17. Comment out when using JDK 19.
      // "--add-modules=jdk.incubator.foreign"
    )
  )

lazy val bench03 = project
  .in(file("bench-0.3"))
  .dependsOn(`core-03` % "test->test;compile->compile")
  .enablePlugins(JmhPlugin)
  .settings(slinc03Settings)
  .settings(
    Jmh / classDirectory := (Test / classDirectory).value,
    Jmh / sourceDirectory := (Test / sourceDirectory).value,
    Jmh / dependencyClasspath := (Test / dependencyClasspath).value,
    Jmh / compile := (Jmh / compile).dependsOn(Test / compile).value,
    Jmh / run := (Jmh / run).dependsOn(Jmh / compile).evaluated,
    Jmh / javaOptions ++= Seq(
      "--enable-native-access=ALL-UNNAMED"
      // for JDK 17. Comment out when using JDK 19.
      // "--add-modules=jdk.incubator.foreign"
    )
  )
