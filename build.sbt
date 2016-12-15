name                  := "SuicideWatch"
version               := "0.1.0"
organization          := "de.sciss"
description           := "A simple console monitoring utility"
scalaVersion          := "2.11.8"
homepage              := Some(url(s"https://github.com/Sciss/${name.value}"))
licenses              := Seq("LGPL v2.1+" -> url("http://www.gnu.org/licenses/lgpl-2.1.txt"))

libraryDependencies ++= Seq(
  "com.github.scopt" %% "scopt" % "3.5.0"
)

assemblyJarName in assembly := "suicide.jar"

target in assembly := baseDirectory.value
