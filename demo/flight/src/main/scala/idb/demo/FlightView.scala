package idb.demo

import java.util.{Calendar, Date}


import idb.syntax.iql._
import idb.syntax.iql.IR._
import idb.SetTable

object FlightView {

  val airport = SetTable.empty[Airport]
  val flight = SetTable.empty[Flight]

  val q = (
    SELECT ((s: Rep[String]) => s,
            COUNT(*))
    FROM (airport, airport, flight)
    WHERE ((a1, a2, f) =>
      f.from == a1.id AND
      f.to == a2.id AND
      a2.code == "PDX" AND
      f.takeoff >= new Date(2014, 01, 01) AND
      f.takeoff < new Date(2015, 01, 01))
    GROUP BY ((a1: Rep[Airport], a2: Rep[Airport], f: Rep[Flight]) => a1.city)
  )


  def initAirports(): Unit = {
    airport += Airport(1, "AMS", "Amsterdam")
    airport += Airport(2, "BOS", "Boston")
    airport += Airport(3, "SFO", "San Francisco")
    airport += Airport(4, "NRT", "Tokyo")
    airport += Airport(5, "PDX", "Portland")
  }


  def initFlights(): Unit = {
    for (d <- 1 to 360)
      flight += Flight(1, 5, new Date(2014,  1,  d, 10,  0))

    for (d <- 1 to 349)
      flight += Flight(2, 5, new Date(2014, 1, d, 17,  5))
    flight += Flight(2, 5, new Date(2014, 12, 31, 17,  5))

    for (d <- 1 to 3467)
      flight += Flight(3, 5, new Date(2014, 1, 1, d,  0))

    for (d <- 1 to 349)
      flight += Flight(4, 5, new Date(2014, 1, d, 16,  0))
  }


  def main(args: Array[String]): Unit = {
    import Predef.println

    val result = q.asMaterialized
    result.foreach(println(_))


    initAirports()
    initFlights()

    println("Initial flights:")
    result.foreach(println(_))

    println()
    flight += Flight(3, 5, new Date(2014,  9, 14, 20, 15))
    println("Updated flights:")
    result.foreach(println(_))

    println()
    flight ~= Flight(2, 5, new Date(2014, 12, 31, 17,  5)) -> Flight(2, 5, new Date(2015,  1,  1, 11,  5))
    println("Updated flights (2):")
    result.foreach(println(_))
  }
}