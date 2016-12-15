/*
 * Watch.scala
 * (SuicideWatch)
 *
 * Copyright (c) 2016 Hanns Holger Rutz. All rights reserved.
 *
 * This software is published under the GNU Lesser General Public License v2.1+
 *
 *
 * For further information, please contact Hanns Holger Rutz at
 * contact@sciss.de
 */

package de.sciss.suicide

import java.io.IOException
import java.text.SimpleDateFormat
import java.{util => ju}
import java.util.{Locale, Scanner}

object Watch {
  final case class Config(mat: String = "JackDriver: exception in real time: alloc failed",
                          num: Int = 10,
                          period: Long = 10000,
                          command: String = "sudo reboot now",
                          prefix: String = "",
                          verbose: Boolean = false)

  def main(args: Array[String]) {
    val defaultConfig = Config()
    val p = new scopt.OptionParser[Config]("SuicideWatch") {
      opt[Int]('n', "num")
        .text(s"Number of pattern occurrences to trace; (default: ${defaultConfig.num})")
        .validate { i => if (i >= 0) success else failure(s"Must be >= 0")}
        .action { (v, c) => c.copy(num = v) }

      opt[Long]('p', "period")
        .text(s"Period in milliseconds in which number of pattern occurrences must occur; default: ${defaultConfig.period})")
        .validate { i => if (i >= 0) success else failure(s"Must be >= 0")}
        .action { (v, c) => c.copy(period = v) }

      opt[String]('m', "match")
        .text(s"Matching expression; (default: ${defaultConfig.mat})")
        .action { (v, c) => c.copy(mat = v) }

      opt[String]('f', "prefix")
        .text(s"Prefix for piping the input; (default: ${defaultConfig.prefix})")
        .action { (v, c) => c.copy(prefix = v) }

      opt[String]('c', "command")
        .text(s"Command to issue if patterns have occurred; (default: ${defaultConfig.command})")
        .action { (v, c) => c.copy(command = v) }

      opt[Unit]('v', "verbose")
        .action { (_, c) => c.copy(verbose = true) }
    }

    p.parse(args, defaultConfig).fold(sys.exit(1)) { config =>
      import config._
      if (verbose) {
        println("::: Suicide Watch :::")
        println(s" pattern: '$mat'")
        println(s" command: '$command'")
        println(s" num    : $num")
        println(s" period : $period")
      }
      run(config)
    }
  }

  def run(config: Config): Unit = {
    import config._

    val sc = new Scanner(System.in)
    val logFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss.SSS 'suicide!'", Locale.US)
    val detected = new Array[Long](num)
    var detectIdx = 0
    val pb = new ProcessBuilder(command.split(' '): _*)
    val hasPrefix = !prefix.isEmpty
    while (sc.hasNextLine) {
      val line = sc.nextLine()
      if (hasPrefix) System.err.print(prefix)
      System.err.println(line)
      if (line.contains(mat)) {
        val isDead = num == 1 || {
          val now = System.currentTimeMillis()
          detected(detectIdx) = now
          detectIdx = (detectIdx + 1) % detected.length
          val oldest = detected(detectIdx)
          (now - oldest) < period
        }

        if (isDead) {
          ju.Arrays.fill(detected, 0L)  // do not trigger multiple times
          if (verbose) System.err.println(logFormat.format(new java.util.Date()))
          try {
            val p = pb.start()
            try {
              p.waitFor()
            } catch {
              case e: InterruptedException =>
                e.printStackTrace()
            }
          } catch {
            case e: IOException =>
              e.printStackTrace()
          }
        }
      }
    }
  }
}