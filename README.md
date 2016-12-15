# SuicideWatch

[![Build Status](https://travis-ci.org/Sciss/ScalaOSC.svg?branch=master)](https://travis-ci.org/Sciss/ScalaOSC)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.sciss/scalaosc_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.sciss/scalaosc_2.11)

## statement

SuicideWatch is a simple utility to track console output and react upon the detection of a given pattern. It is (C)opyright 2016 by Hanns Holger Rutz. All rights reserved. This project is released under the [GNU Lesser General Public License](https://raw.github.com/Sciss/SuicideWatch/master/LICENSE) v2.1+ and comes with absolutely no warranties. To contact the author, send an email to `contact at sciss.de`

## requirements / installation

This project builds against Scala 2.11 using sbt 0.13.

## contributing

Please see the file [CONTRIBUTING.md](CONTRIBUTING.md)

## overview

Build with `sbt assembly`. Then an example run:

```
$ java -jar suicide.jar --help
Error: Unknown option --help
Usage: SuicideWatch [options]

  -n, --num <value>      Number of pattern occurrences to trace; (default: 10)
  -p, --period <value>   Period in milliseconds in which number of pattern occurrences must occur; (default: 10000)
  -m, --match <value>    Matching expression; (default: JackDriver: exception in real time: alloc failed)
  -f, --prefix <value>   Prefix for piping the input; (default: )
  -c, --command <value>  Command to issue if patterns have occurred; (default: sudo reboot now)
  -v, --verbose
```

Real example:

```bash
jackd &> >(java -jar suicide.jar --verbose --match 'exception in real time' --prefix 'WATCH: ' --command 'killall scsynth' -n 10 -p 10000)
```