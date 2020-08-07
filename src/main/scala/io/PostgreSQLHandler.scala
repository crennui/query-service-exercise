package io

import akka.http.scaladsl.model.StatusCodes
import org.slf4j.{Logger, LoggerFactory}
import service.QueryToFileRequest
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.H2Profile.api._
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class PostgreSQLHandler extends RequestHandler[QueryToFileRequest, Unit] {
  private var db: Database = _
  implicit private var executionContext : ExecutionContext = _
  private final val log: Logger = LoggerFactory.getLogger(this.getClass)

  def initConnection(configName: String): Unit = {
    this.db = Database.forConfig(configName)
    this.executionContext = this.db.ioExecutionContext

  }

  def closeConnection(): Unit = this.db.close()

  def executeRequest(request: QueryToFileRequest): Unit = {
    val csvFileResult = new CsvFileResult(request.fileName)
    val query = sql"#${request.query}".as(csvFileResult)
    val result = this.db.run(query)

    result.onComplete({
      case Success(value) => log.info("The request completed successfully")
      case Failure(e : Throwable) =>  log.error(s"There was a problem while handling the request \n${e.getMessage}\n${e.printStackTrace()}")
    })
  }
}
