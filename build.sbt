lazy val commonSettings = Seq(
	organization := "io.github.katrix",
	scalaVersion := "2.11.8",
	resolvers += "SpongePowered" at "https://repo.spongepowered.org/maven",
	resolvers += "jitpack" at "https://jitpack.io",
	libraryDependencies += "com.github.Katrix-.KatLib" % "katlib-shared" % "develop-SNAPSHOT" % "provided",
	scalacOptions += "-Xexperimental",
	crossPaths := false,
	assemblyShadeRules in assembly := Seq(
		ShadeRule.rename("scala.**" -> "io.github.katrix.katlib.shade.scala.@1").inProject
	),
	autoScalaLibrary := false
)

lazy val editorShared = project in file("shared") settings(commonSettings: _*)  settings(
	name := "ChatEditor",
	version := "1.0.0",
	assembleArtifact := false, //Why doesn't this one disable stuff?
	//Default version
	libraryDependencies += "org.spongepowered" % "spongeapi" % "4.1.0" % "provided"
	)

lazy val editorV410 = project in file("4.1.0") dependsOn editorShared settings(commonSettings: _*) settings(
	name := "ChatEditor-4.1.0",
	version := "1.0.0",
	libraryDependencies += "org.spongepowered" % "spongeapi" % "4.1.0" % "provided",
	libraryDependencies += "com.github.Katrix-.KatLib" % "katlib-4-1-0" % "develop-SNAPSHOT" % "provided"
	)

lazy val editorV500 = project in file("5.0.0") dependsOn editorShared settings(commonSettings: _*) settings(
	name := "ChatEditor-5.0.0",
	version := "1.0.0",
	libraryDependencies += "org.spongepowered" % "spongeapi" % "5.0.0-SNAPSHOT" % "provided",
	libraryDependencies += "com.github.Katrix-.KatLib" % "katlib-5-0-0" % "develop-SNAPSHOT" % "provided"
	)

lazy val editorRoot = project in file(".") settings (publishArtifact := false) disablePlugins AssemblyPlugin aggregate(editorV410, editorV500)