import java.util.concurrent.ForkJoinPool
import com.typesafe.slick.driver.ms.SQLServerDriver.api._
import contracts._
import slick.backend.StaticDatabaseConfig
import scala.concurrent.Future

@StaticDatabaseConfig("file:src/main/resources/application.conf#db.tsql")
object WlDb {
  implicit val ctx = concurrent.ExecutionContext.fromExecutor(new ForkJoinPool(100))
  val db = Database.forConfig("db.wl")

  def getFormatById(id: Short): Future[Option[FormatType]] = {
    val cmd: DBIO[Option[FormatTypeOps.Tuple]] =
      tsql"""select cast(IDFormatType as smallint), Name, IsCategorized, IsSafe, IsUnknown, IsObsolete, IDElf
             from dbo.FormatType
             where IDFormatType = $id""".headOption

    db.run(cmd).map(_.map(FormatType.tupled))
  }
}