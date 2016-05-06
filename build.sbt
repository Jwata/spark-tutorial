
name := "movie-lens-als"

version := "1.0"

scalaVersion := "2.10.5"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.6.1",
  "org.apache.spark" %% "spark-mllib" % "1.6.1"
)

assemblyOutputPath in assembly := baseDirectory.value / "build" / (assemblyJarName in assembly).value

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs@_*) => MergeStrategy.discard
  case x => {MergeStrategy.first}
}
