import AssemblyKeys._ // put this at the top of the file

assemblySettings

name := "weazrbot"

version := "1.0"

scalaVersion := "2.10.4"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "UCAR repo" at "http://artifacts.unidata.ucar.edu/content/repositories/unidata-releases/"

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.10" % "2.3.2"

libraryDependencies += "com.datastax.cassandra" % "cassandra-driver-core" % "2.0.1"

libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "0.8.0"

libraryDependencies += "org.apache.commons" % "commons-io" % "1.3.2"

libraryDependencies += "commons-net" % "commons-net" % "3.3"

libraryDependencies += "org.xerial.snappy" % "snappy-java" % "1.1.0.1"

libraryDependencies += "net.jpountz.lz4" % "lz4" % "1.2.0"

libraryDependencies += "edu.ucar" % "netcdf" % "4.3.21"

libraryDependencies += "edu.ucar" % "grib" % "4.3.21"