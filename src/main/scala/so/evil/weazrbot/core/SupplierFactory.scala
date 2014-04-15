package so.evil.weazrbot.core

import so.evil.weazrbot.modules.GFSSupplier
import so.evil.weazrbot.core.db.Cassandra

/**
 * Created by shutty on 4/15/14.
 */
object SupplierFactory {
  def build(db:Cassandra, name:String) = name match {
    case "gfs" => new GFSSupplier(db)
  }
}
