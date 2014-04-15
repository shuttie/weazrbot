package so.evil.weazrbot.core.db

import com.datastax.driver.core.{BatchStatement, Cluster}
import so.evil.weazrbot.core.models.Weather
import org.joda.time.DateTime

/**
 * Created by shutty on 4/15/14.
 */
class Cassandra {
  val cluster = Cluster.builder().addContactPoint("localhost").build()
  val session = cluster.connect("weazr")

  val weatherAddStatement = session.prepare("insert into weather (zone,supplier,run_at,forecast_for,lat,lon,tmp,rh,precip) values (?,?,?,?,?,?,?,?,?)")
  val latestImportStatement = session.prepare("select run_at from imports where supplier = ? limit 1")
  val batch = new BatchStatement()

  def close() = {
    session.close()
    cluster.close()
  }

  def addWeather(weather:Weather) = {
    batch.add(weatherAddStatement.bind(
      weather.zone:java.lang.Integer,
      weather.supplier,
      new java.util.Date(weather.runAt.getMillis),
      new java.util.Date(weather.forecastFor.getMillis),
      weather.lat:java.lang.Float,
      weather.lon:java.lang.Float,
      weather.tmp:java.lang.Float,
      weather.rh:java.lang.Float,
      weather.precip:java.lang.Float
    ))
    if (batch.getStatements.size() > 100) {
      session.execute(batch)
      batch.clear()
    }
  }

  def getLatestRun(supplier:String):Option[DateTime] = {
    val result = session.execute(latestImportStatement.bind(supplier))
    val it = result.iterator()
    if (it.hasNext)
      Some(new DateTime(it.next().getDate("run_at")))
    else
      None
  }
}

object Cassandra {
  def apply() = new Cassandra()
}