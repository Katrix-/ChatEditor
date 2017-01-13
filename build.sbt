def removeSnapshot(str: String): String = if(str.endsWith("-SNAPSHOT")) str.substring(0, str.length - 9) else str
def katLibDependecy(module: String) = "com.github.Katrix-.KatLib" % s"katlib-$module" % "2.0.1" % Provided

lazy val commonSettings = Seq(
  name := s"ChatEditor-${removeSnapshot(spongeApiVersion.value)}",
	organization := "io.github.katrix",
  version := "0.2.0",
	scalaVersion := "2.12.0",
	resolvers += "jitpack" at "https://jitpack.io",
  libraryDependencies += katLibDependecy("shared"),
  scalacOptions ++= Seq(
    "-deprecation",
    "-feature",
    "-unchecked",
    "-Xlint",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-unused-import"
  ),
	crossPaths := false,
  assemblyShadeRules in assembly := Seq(
    ShadeRule.rename("scala.**" -> "io.github.katrix.katlib.shade.scala.@1").inProject,
    ShadeRule.rename("shapeless.**" -> "io.github.katrix.katlib.shade.shapeless.@1").inProject
  ),
	autoScalaLibrary := false,

  spongePluginInfo := spongePluginInfo.value.copy(
    id = "chateditor",
    name = Some("ChatEditor"),
    version = Some(s"${removeSnapshot(spongeApiVersion.value)}-${version.value}"),
    authors = Seq("Katrix"),
    dependencies = Set(
      DependencyInfo("spongeapi", Some(removeSnapshot(spongeApiVersion.value))),
      DependencyInfo("katlib", Some(s"2.0.1-${removeSnapshot(spongeApiVersion.value)}"))
    )
  )
)

lazy val editorShared = (project in file("shared"))
  .enablePlugins(SpongePlugin)
  .settings(commonSettings: _*)
  .settings(
	  name := "ChatEditor-Shared",
    assembleArtifact := false,
    spongeMetaCreate := false,
    //Default version, needs to build correctly against all supported versions
    spongeApiVersion := "4.1.0"
	)

lazy val editorV410 = (project in file("4.1.0"))
  .enablePlugins(SpongePlugin)
  .dependsOn(editorShared)
  .settings(commonSettings: _*)
  .settings(
    spongeApiVersion := "4.1.0",
    libraryDependencies += katLibDependecy("4-1-0")
	)

lazy val editorV500 = (project in file("5.0.0"))
  .enablePlugins(SpongePlugin)
  .dependsOn(editorShared)
  .settings(commonSettings: _*)
  .settings(
    spongeApiVersion := "5.0.0",
    libraryDependencies += katLibDependecy("5-0-0")
  )

lazy val editorV600 = (project in file("6.0.0"))
  .enablePlugins(SpongePlugin)
  .dependsOn(editorShared)
  .settings(commonSettings: _*)
  .settings(
    spongeApiVersion := "6.0.0-SNAPSHOT",
    libraryDependencies += katLibDependecy("6-0-0")
  )


lazy val editorRoot = project in file(".") settings (publishArtifact := false) disablePlugins AssemblyPlugin aggregate(editorV410, editorV500, editorV600)