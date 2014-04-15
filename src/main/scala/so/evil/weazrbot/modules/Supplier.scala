package so.evil.weazrbot.modules

import so.evil.weazrbot.core.db.Cassandra
import so.evil.weazrbot.core.models.Weather
import java.nio.ByteBuffer

/**
 * Created by shutty on 3/1/14.
 */
abstract class Supplier(val name:String, val db:Cassandra) {
  def shouldImport:Boolean
  def targetURLs:List[String]
  def parse(target:String, fileName:String):List[Weather]
}
