package so.evil.weazrbot.modules

import so.evil.weazrbot.core.WeatherSnapshot
import org.joda.time.DateTime
import java.io.{OutputStream, InputStream}

/**
 * Created by shutty on 3/1/14.
 */
trait Module {
  def dir:String
  def files:List[String]
  def parse(fileName:String):WeatherSnapshot
}
